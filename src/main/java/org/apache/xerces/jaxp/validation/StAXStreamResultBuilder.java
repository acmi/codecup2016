/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.jaxp.validation;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.StartDocument;
import javax.xml.transform.stax.StAXResult;
import org.apache.xerces.jaxp.validation.StAXDocumentHandler;
import org.apache.xerces.util.JAXPNamespaceContextWrapper;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLDocumentSource;

final class StAXStreamResultBuilder
implements StAXDocumentHandler {
    private XMLStreamWriter fStreamWriter;
    private final JAXPNamespaceContextWrapper fNamespaceContext;
    private boolean fIgnoreChars;
    private boolean fInCDATA;
    private final QName fAttrName = new QName();

    public StAXStreamResultBuilder(JAXPNamespaceContextWrapper jAXPNamespaceContextWrapper) {
        this.fNamespaceContext = jAXPNamespaceContextWrapper;
    }

    public void setStAXResult(StAXResult stAXResult) {
        this.fIgnoreChars = false;
        this.fInCDATA = false;
        this.fAttrName.clear();
        this.fStreamWriter = stAXResult != null ? stAXResult.getXMLStreamWriter() : null;
    }

    public void startDocument(XMLStreamReader xMLStreamReader) throws XMLStreamException {
        String string = xMLStreamReader.getVersion();
        String string2 = xMLStreamReader.getCharacterEncodingScheme();
        this.fStreamWriter.writeStartDocument(string2 != null ? string2 : "UTF-8", string != null ? string : "1.0");
    }

    public void endDocument(XMLStreamReader xMLStreamReader) throws XMLStreamException {
        this.fStreamWriter.writeEndDocument();
        this.fStreamWriter.flush();
    }

    public void comment(XMLStreamReader xMLStreamReader) throws XMLStreamException {
        this.fStreamWriter.writeComment(xMLStreamReader.getText());
    }

    public void processingInstruction(XMLStreamReader xMLStreamReader) throws XMLStreamException {
        String string = xMLStreamReader.getPIData();
        if (string != null && string.length() > 0) {
            this.fStreamWriter.writeProcessingInstruction(xMLStreamReader.getPITarget(), string);
        } else {
            this.fStreamWriter.writeProcessingInstruction(xMLStreamReader.getPITarget());
        }
    }

    public void entityReference(XMLStreamReader xMLStreamReader) throws XMLStreamException {
        this.fStreamWriter.writeEntityRef(xMLStreamReader.getLocalName());
    }

    public void startDocument(StartDocument startDocument) throws XMLStreamException {
        String string = startDocument.getVersion();
        String string2 = startDocument.getCharacterEncodingScheme();
        this.fStreamWriter.writeStartDocument(string2 != null ? string2 : "UTF-8", string != null ? string : "1.0");
    }

    public void endDocument(EndDocument endDocument) throws XMLStreamException {
        this.fStreamWriter.writeEndDocument();
        this.fStreamWriter.flush();
    }

    public void doctypeDecl(DTD dTD) throws XMLStreamException {
        this.fStreamWriter.writeDTD(dTD.getDocumentTypeDeclaration());
    }

    public void characters(Characters characters) throws XMLStreamException {
        this.fStreamWriter.writeCharacters(characters.getData());
    }

    public void cdata(Characters characters) throws XMLStreamException {
        this.fStreamWriter.writeCData(characters.getData());
    }

    public void comment(Comment comment) throws XMLStreamException {
        this.fStreamWriter.writeComment(comment.getText());
    }

    public void processingInstruction(ProcessingInstruction processingInstruction) throws XMLStreamException {
        String string = processingInstruction.getData();
        if (string != null && string.length() > 0) {
            this.fStreamWriter.writeProcessingInstruction(processingInstruction.getTarget(), string);
        } else {
            this.fStreamWriter.writeProcessingInstruction(processingInstruction.getTarget());
        }
    }

    public void entityReference(EntityReference entityReference) throws XMLStreamException {
        this.fStreamWriter.writeEntityRef(entityReference.getName());
    }

    public void setIgnoringCharacters(boolean bl) {
        this.fIgnoreChars = bl;
    }

    public void startDocument(XMLLocator xMLLocator, String string, NamespaceContext namespaceContext, Augmentations augmentations) throws XNIException {
    }

    public void xmlDecl(String string, String string2, String string3, Augmentations augmentations) throws XNIException {
    }

    public void doctypeDecl(String string, String string2, String string3, Augmentations augmentations) throws XNIException {
    }

    public void comment(XMLString xMLString, Augmentations augmentations) throws XNIException {
    }

    public void processingInstruction(String string, XMLString xMLString, Augmentations augmentations) throws XNIException {
    }

    public void startElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        try {
            if (qName.prefix.length() > 0) {
                this.fStreamWriter.writeStartElement(qName.prefix, qName.localpart, qName.uri != null ? qName.uri : "");
            } else if (qName.uri != null) {
                this.fStreamWriter.writeStartElement(qName.uri, qName.localpart);
            } else {
                this.fStreamWriter.writeStartElement(qName.localpart);
            }
            int n2 = this.fNamespaceContext.getDeclaredPrefixCount();
            javax.xml.namespace.NamespaceContext namespaceContext = this.fNamespaceContext.getNamespaceContext();
            int n3 = 0;
            while (n3 < n2) {
                String string = this.fNamespaceContext.getDeclaredPrefixAt(n3);
                String string2 = namespaceContext.getNamespaceURI(string);
                if (string.length() == 0) {
                    this.fStreamWriter.writeDefaultNamespace(string2 != null ? string2 : "");
                } else {
                    this.fStreamWriter.writeNamespace(string, string2 != null ? string2 : "");
                }
                ++n3;
            }
            n2 = xMLAttributes.getLength();
            int n4 = 0;
            while (n4 < n2) {
                xMLAttributes.getName(n4, this.fAttrName);
                if (this.fAttrName.prefix.length() > 0) {
                    this.fStreamWriter.writeAttribute(this.fAttrName.prefix, this.fAttrName.uri != null ? this.fAttrName.uri : "", this.fAttrName.localpart, xMLAttributes.getValue(n4));
                } else if (this.fAttrName.uri != null) {
                    this.fStreamWriter.writeAttribute(this.fAttrName.uri, this.fAttrName.localpart, xMLAttributes.getValue(n4));
                } else {
                    this.fStreamWriter.writeAttribute(this.fAttrName.localpart, xMLAttributes.getValue(n4));
                }
                ++n4;
            }
        }
        catch (XMLStreamException xMLStreamException) {
            throw new XNIException(xMLStreamException);
        }
    }

    public void emptyElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        this.startElement(qName, xMLAttributes, augmentations);
        this.endElement(qName, augmentations);
    }

    public void startGeneralEntity(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
    }

    public void textDecl(String string, String string2, Augmentations augmentations) throws XNIException {
    }

    public void endGeneralEntity(String string, Augmentations augmentations) throws XNIException {
    }

    public void characters(XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (!this.fIgnoreChars) {
            try {
                if (!this.fInCDATA) {
                    this.fStreamWriter.writeCharacters(xMLString.ch, xMLString.offset, xMLString.length);
                } else {
                    this.fStreamWriter.writeCData(xMLString.toString());
                }
            }
            catch (XMLStreamException xMLStreamException) {
                throw new XNIException(xMLStreamException);
            }
        }
    }

    public void ignorableWhitespace(XMLString xMLString, Augmentations augmentations) throws XNIException {
        this.characters(xMLString, augmentations);
    }

    public void endElement(QName qName, Augmentations augmentations) throws XNIException {
        try {
            this.fStreamWriter.writeEndElement();
        }
        catch (XMLStreamException xMLStreamException) {
            throw new XNIException(xMLStreamException);
        }
    }

    public void startCDATA(Augmentations augmentations) throws XNIException {
        this.fInCDATA = true;
    }

    public void endCDATA(Augmentations augmentations) throws XNIException {
        this.fInCDATA = false;
    }

    public void endDocument(Augmentations augmentations) throws XNIException {
    }

    public void setDocumentSource(XMLDocumentSource xMLDocumentSource) {
    }

    public XMLDocumentSource getDocumentSource() {
        return null;
    }
}

