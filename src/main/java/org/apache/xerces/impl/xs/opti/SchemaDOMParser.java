/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.opti;

import java.io.IOException;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.opti.DefaultXMLDocumentHandler;
import org.apache.xerces.impl.xs.opti.ElementImpl;
import org.apache.xerces.impl.xs.opti.SchemaDOM;
import org.apache.xerces.impl.xs.opti.SchemaParsingConfig;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.w3c.dom.Document;

public class SchemaDOMParser
extends DefaultXMLDocumentHandler {
    public static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    public static final String GENERATE_SYNTHETIC_ANNOTATION = "http://apache.org/xml/features/generate-synthetic-annotations";
    protected XMLLocator fLocator;
    protected NamespaceContext fNamespaceContext = null;
    SchemaDOM schemaDOM;
    XMLParserConfiguration config;
    private ElementImpl fCurrentAnnotationElement;
    private int fAnnotationDepth = -1;
    private int fInnerAnnotationDepth = -1;
    private int fDepth = -1;
    XMLErrorReporter fErrorReporter;
    private boolean fGenerateSyntheticAnnotation = false;
    private BooleanStack fHasNonSchemaAttributes = new BooleanStack();
    private BooleanStack fSawAnnotation = new BooleanStack();
    private XMLAttributes fEmptyAttr = new XMLAttributesImpl();

    public SchemaDOMParser(XMLParserConfiguration xMLParserConfiguration) {
        this.config = xMLParserConfiguration;
        xMLParserConfiguration.setDocumentHandler(this);
        xMLParserConfiguration.setDTDHandler(this);
        xMLParserConfiguration.setDTDContentModelHandler(this);
    }

    public void startDocument(XMLLocator xMLLocator, String string, NamespaceContext namespaceContext, Augmentations augmentations) throws XNIException {
        this.fErrorReporter = (XMLErrorReporter)this.config.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        this.fGenerateSyntheticAnnotation = this.config.getFeature("http://apache.org/xml/features/generate-synthetic-annotations");
        this.fHasNonSchemaAttributes.clear();
        this.fSawAnnotation.clear();
        this.schemaDOM = new SchemaDOM();
        this.fCurrentAnnotationElement = null;
        this.fAnnotationDepth = -1;
        this.fInnerAnnotationDepth = -1;
        this.fDepth = -1;
        this.fLocator = xMLLocator;
        this.fNamespaceContext = namespaceContext;
        this.schemaDOM.setDocumentURI(xMLLocator.getExpandedSystemId());
    }

    public void endDocument(Augmentations augmentations) throws XNIException {
    }

    public void comment(XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (this.fAnnotationDepth > -1) {
            this.schemaDOM.comment(xMLString);
        }
    }

    public void processingInstruction(String string, XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (this.fAnnotationDepth > -1) {
            this.schemaDOM.processingInstruction(string, xMLString);
        }
    }

    public void characters(XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (this.fInnerAnnotationDepth == -1) {
            int n2 = xMLString.offset;
            while (n2 < xMLString.offset + xMLString.length) {
                if (!XMLChar.isSpace(xMLString.ch[n2])) {
                    String string = new String(xMLString.ch, n2, xMLString.length + xMLString.offset - n2);
                    this.fErrorReporter.reportError(this.fLocator, "http://www.w3.org/TR/xml-schema-1", "s4s-elt-character", new Object[]{string}, 1);
                    break;
                }
                ++n2;
            }
        } else {
            this.schemaDOM.characters(xMLString);
        }
    }

    public void startElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        ++this.fDepth;
        if (this.fAnnotationDepth == -1) {
            if (qName.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA && qName.localpart == SchemaSymbols.ELT_ANNOTATION) {
                if (this.fGenerateSyntheticAnnotation) {
                    if (this.fSawAnnotation.size() > 0) {
                        this.fSawAnnotation.pop();
                    }
                    this.fSawAnnotation.push(true);
                }
                this.fAnnotationDepth = this.fDepth;
                this.schemaDOM.startAnnotation(qName, xMLAttributes, this.fNamespaceContext);
                this.fCurrentAnnotationElement = this.schemaDOM.startElement(qName, xMLAttributes, this.fLocator.getLineNumber(), this.fLocator.getColumnNumber(), this.fLocator.getCharacterOffset());
                return;
            }
            if (qName.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA && this.fGenerateSyntheticAnnotation) {
                this.fSawAnnotation.push(false);
                this.fHasNonSchemaAttributes.push(this.hasNonSchemaAttributes(qName, xMLAttributes));
            }
        } else if (this.fDepth == this.fAnnotationDepth + 1) {
            this.fInnerAnnotationDepth = this.fDepth;
            this.schemaDOM.startAnnotationElement(qName, xMLAttributes);
        } else {
            this.schemaDOM.startAnnotationElement(qName, xMLAttributes);
            return;
        }
        this.schemaDOM.startElement(qName, xMLAttributes, this.fLocator.getLineNumber(), this.fLocator.getColumnNumber(), this.fLocator.getCharacterOffset());
    }

    public void emptyElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        if (this.fGenerateSyntheticAnnotation && this.fAnnotationDepth == -1 && qName.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA && qName.localpart != SchemaSymbols.ELT_ANNOTATION && this.hasNonSchemaAttributes(qName, xMLAttributes)) {
            this.schemaDOM.startElement(qName, xMLAttributes, this.fLocator.getLineNumber(), this.fLocator.getColumnNumber(), this.fLocator.getCharacterOffset());
            xMLAttributes.removeAllAttributes();
            String string = this.fNamespaceContext.getPrefix(SchemaSymbols.URI_SCHEMAFORSCHEMA);
            QName qName2 = new QName(string, SchemaSymbols.ELT_ANNOTATION, string + (string.length() == 0 ? "" : ":") + SchemaSymbols.ELT_ANNOTATION, SchemaSymbols.URI_SCHEMAFORSCHEMA);
            this.schemaDOM.startAnnotation(qName2, xMLAttributes, this.fNamespaceContext);
            QName qName3 = new QName(string, SchemaSymbols.ELT_DOCUMENTATION, string + (string.length() == 0 ? "" : ":") + SchemaSymbols.ELT_DOCUMENTATION, SchemaSymbols.URI_SCHEMAFORSCHEMA);
            this.schemaDOM.startAnnotationElement(qName3, xMLAttributes);
            this.schemaDOM.characters(new XMLString("SYNTHETIC_ANNOTATION".toCharArray(), 0, 20));
            this.schemaDOM.endSyntheticAnnotationElement(qName3, false);
            this.schemaDOM.endSyntheticAnnotationElement(qName2, true);
            this.schemaDOM.endElement();
            return;
        }
        if (this.fAnnotationDepth == -1) {
            if (qName.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA && qName.localpart == SchemaSymbols.ELT_ANNOTATION) {
                this.schemaDOM.startAnnotation(qName, xMLAttributes, this.fNamespaceContext);
            }
        } else {
            this.schemaDOM.startAnnotationElement(qName, xMLAttributes);
        }
        ElementImpl elementImpl = this.schemaDOM.emptyElement(qName, xMLAttributes, this.fLocator.getLineNumber(), this.fLocator.getColumnNumber(), this.fLocator.getCharacterOffset());
        if (this.fAnnotationDepth == -1) {
            if (qName.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA && qName.localpart == SchemaSymbols.ELT_ANNOTATION) {
                this.schemaDOM.endAnnotation(qName, elementImpl);
            }
        } else {
            this.schemaDOM.endAnnotationElement(qName);
        }
    }

    public void endElement(QName qName, Augmentations augmentations) throws XNIException {
        if (this.fAnnotationDepth > -1) {
            if (this.fInnerAnnotationDepth == this.fDepth) {
                this.fInnerAnnotationDepth = -1;
                this.schemaDOM.endAnnotationElement(qName);
                this.schemaDOM.endElement();
            } else if (this.fAnnotationDepth == this.fDepth) {
                this.fAnnotationDepth = -1;
                this.schemaDOM.endAnnotation(qName, this.fCurrentAnnotationElement);
                this.schemaDOM.endElement();
            } else {
                this.schemaDOM.endAnnotationElement(qName);
            }
        } else {
            if (qName.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA && this.fGenerateSyntheticAnnotation) {
                boolean bl = this.fHasNonSchemaAttributes.pop();
                boolean bl2 = this.fSawAnnotation.pop();
                if (bl && !bl2) {
                    String string;
                    QName qName2 = new QName(string, SchemaSymbols.ELT_ANNOTATION, string + ((string = this.fNamespaceContext.getPrefix(SchemaSymbols.URI_SCHEMAFORSCHEMA)).length() == 0 ? "" : ":") + SchemaSymbols.ELT_ANNOTATION, SchemaSymbols.URI_SCHEMAFORSCHEMA);
                    this.schemaDOM.startAnnotation(qName2, this.fEmptyAttr, this.fNamespaceContext);
                    QName qName3 = new QName(string, SchemaSymbols.ELT_DOCUMENTATION, string + (string.length() == 0 ? "" : ":") + SchemaSymbols.ELT_DOCUMENTATION, SchemaSymbols.URI_SCHEMAFORSCHEMA);
                    this.schemaDOM.startAnnotationElement(qName3, this.fEmptyAttr);
                    this.schemaDOM.characters(new XMLString("SYNTHETIC_ANNOTATION".toCharArray(), 0, 20));
                    this.schemaDOM.endSyntheticAnnotationElement(qName3, false);
                    this.schemaDOM.endSyntheticAnnotationElement(qName2, true);
                }
            }
            this.schemaDOM.endElement();
        }
        --this.fDepth;
    }

    private boolean hasNonSchemaAttributes(QName qName, XMLAttributes xMLAttributes) {
        int n2 = xMLAttributes.getLength();
        int n3 = 0;
        while (n3 < n2) {
            String string = xMLAttributes.getURI(n3);
            if (string != null && string != SchemaSymbols.URI_SCHEMAFORSCHEMA && string != NamespaceContext.XMLNS_URI && (string != NamespaceContext.XML_URI || xMLAttributes.getQName(n3) != SchemaSymbols.ATT_XML_LANG || qName.localpart != SchemaSymbols.ELT_SCHEMA)) {
                return true;
            }
            ++n3;
        }
        return false;
    }

    public void ignorableWhitespace(XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (this.fAnnotationDepth != -1) {
            this.schemaDOM.characters(xMLString);
        }
    }

    public void startCDATA(Augmentations augmentations) throws XNIException {
        if (this.fAnnotationDepth != -1) {
            this.schemaDOM.startAnnotationCDATA();
        }
    }

    public void endCDATA(Augmentations augmentations) throws XNIException {
        if (this.fAnnotationDepth != -1) {
            this.schemaDOM.endAnnotationCDATA();
        }
    }

    public Document getDocument() {
        return this.schemaDOM;
    }

    public void setFeature(String string, boolean bl) {
        this.config.setFeature(string, bl);
    }

    public boolean getFeature(String string) {
        return this.config.getFeature(string);
    }

    public void setProperty(String string, Object object) {
        this.config.setProperty(string, object);
    }

    public Object getProperty(String string) {
        return this.config.getProperty(string);
    }

    public void setEntityResolver(XMLEntityResolver xMLEntityResolver) {
        this.config.setEntityResolver(xMLEntityResolver);
    }

    public void parse(XMLInputSource xMLInputSource) throws IOException {
        this.config.parse(xMLInputSource);
    }

    public void reset() {
        ((SchemaParsingConfig)this.config).reset();
    }

    public void resetNodePool() {
        ((SchemaParsingConfig)this.config).resetNodePool();
    }

    private static final class BooleanStack {
        private int fDepth;
        private boolean[] fData;

        public int size() {
            return this.fDepth;
        }

        public void push(boolean bl) {
            this.ensureCapacity(this.fDepth + 1);
            this.fData[this.fDepth++] = bl;
        }

        public boolean pop() {
            return this.fData[--this.fDepth];
        }

        public void clear() {
            this.fDepth = 0;
        }

        private void ensureCapacity(int n2) {
            if (this.fData == null) {
                this.fData = new boolean[32];
            } else if (this.fData.length <= n2) {
                boolean[] arrbl = new boolean[this.fData.length * 2];
                System.arraycopy(this.fData, 0, arrbl, 0, this.fData.length);
                this.fData = arrbl;
            }
        }
    }

}

