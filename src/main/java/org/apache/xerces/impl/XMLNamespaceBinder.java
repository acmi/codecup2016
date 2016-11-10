/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl;

import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLDocumentSource;

public class XMLNamespaceBinder
implements XMLComponent,
XMLDocumentFilter {
    protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    private static final String[] RECOGNIZED_FEATURES = new String[]{"http://xml.org/sax/features/namespaces"};
    private static final Boolean[] FEATURE_DEFAULTS = new Boolean[]{null};
    private static final String[] RECOGNIZED_PROPERTIES = new String[]{"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter"};
    private static final Object[] PROPERTY_DEFAULTS = new Object[]{null, null};
    protected boolean fNamespaces;
    protected SymbolTable fSymbolTable;
    protected XMLErrorReporter fErrorReporter;
    protected XMLDocumentHandler fDocumentHandler;
    protected XMLDocumentSource fDocumentSource;
    protected boolean fOnlyPassPrefixMappingEvents;
    private NamespaceContext fNamespaceContext;
    private final QName fAttributeQName = new QName();

    public void setOnlyPassPrefixMappingEvents(boolean bl) {
        this.fOnlyPassPrefixMappingEvents = bl;
    }

    public boolean getOnlyPassPrefixMappingEvents() {
        return this.fOnlyPassPrefixMappingEvents;
    }

    public void reset(XMLComponentManager xMLComponentManager) throws XNIException {
        try {
            this.fNamespaces = xMLComponentManager.getFeature("http://xml.org/sax/features/namespaces");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fNamespaces = true;
        }
        this.fSymbolTable = (SymbolTable)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fErrorReporter = (XMLErrorReporter)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
    }

    public String[] getRecognizedFeatures() {
        return (String[])RECOGNIZED_FEATURES.clone();
    }

    public void setFeature(String string, boolean bl) throws XMLConfigurationException {
    }

    public String[] getRecognizedProperties() {
        return (String[])RECOGNIZED_PROPERTIES.clone();
    }

    public void setProperty(String string, Object object) throws XMLConfigurationException {
        if (string.startsWith("http://apache.org/xml/properties/")) {
            int n2 = string.length() - "http://apache.org/xml/properties/".length();
            if (n2 == "internal/symbol-table".length() && string.endsWith("internal/symbol-table")) {
                this.fSymbolTable = (SymbolTable)object;
            } else if (n2 == "internal/error-reporter".length() && string.endsWith("internal/error-reporter")) {
                this.fErrorReporter = (XMLErrorReporter)object;
            }
            return;
        }
    }

    public Boolean getFeatureDefault(String string) {
        int n2 = 0;
        while (n2 < RECOGNIZED_FEATURES.length) {
            if (RECOGNIZED_FEATURES[n2].equals(string)) {
                return FEATURE_DEFAULTS[n2];
            }
            ++n2;
        }
        return null;
    }

    public Object getPropertyDefault(String string) {
        int n2 = 0;
        while (n2 < RECOGNIZED_PROPERTIES.length) {
            if (RECOGNIZED_PROPERTIES[n2].equals(string)) {
                return PROPERTY_DEFAULTS[n2];
            }
            ++n2;
        }
        return null;
    }

    public void setDocumentHandler(XMLDocumentHandler xMLDocumentHandler) {
        this.fDocumentHandler = xMLDocumentHandler;
    }

    public XMLDocumentHandler getDocumentHandler() {
        return this.fDocumentHandler;
    }

    public void setDocumentSource(XMLDocumentSource xMLDocumentSource) {
        this.fDocumentSource = xMLDocumentSource;
    }

    public XMLDocumentSource getDocumentSource() {
        return this.fDocumentSource;
    }

    public void startGeneralEntity(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.startGeneralEntity(string, xMLResourceIdentifier, string2, augmentations);
        }
    }

    public void textDecl(String string, String string2, Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.textDecl(string, string2, augmentations);
        }
    }

    public void startDocument(XMLLocator xMLLocator, String string, NamespaceContext namespaceContext, Augmentations augmentations) throws XNIException {
        this.fNamespaceContext = namespaceContext;
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.startDocument(xMLLocator, string, namespaceContext, augmentations);
        }
    }

    public void xmlDecl(String string, String string2, String string3, Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.xmlDecl(string, string2, string3, augmentations);
        }
    }

    public void doctypeDecl(String string, String string2, String string3, Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.doctypeDecl(string, string2, string3, augmentations);
        }
    }

    public void comment(XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.comment(xMLString, augmentations);
        }
    }

    public void processingInstruction(String string, XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.processingInstruction(string, xMLString, augmentations);
        }
    }

    public void startElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        if (this.fNamespaces) {
            this.handleStartElement(qName, xMLAttributes, augmentations, false);
        } else if (this.fDocumentHandler != null) {
            this.fDocumentHandler.startElement(qName, xMLAttributes, augmentations);
        }
    }

    public void emptyElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        if (this.fNamespaces) {
            this.handleStartElement(qName, xMLAttributes, augmentations, true);
            this.handleEndElement(qName, augmentations, true);
        } else if (this.fDocumentHandler != null) {
            this.fDocumentHandler.emptyElement(qName, xMLAttributes, augmentations);
        }
    }

    public void characters(XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.characters(xMLString, augmentations);
        }
    }

    public void ignorableWhitespace(XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.ignorableWhitespace(xMLString, augmentations);
        }
    }

    public void endElement(QName qName, Augmentations augmentations) throws XNIException {
        if (this.fNamespaces) {
            this.handleEndElement(qName, augmentations, false);
        } else if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endElement(qName, augmentations);
        }
    }

    public void startCDATA(Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.startCDATA(augmentations);
        }
    }

    public void endCDATA(Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.endCDATA(augmentations);
        }
    }

    public void endDocument(Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.endDocument(augmentations);
        }
    }

    public void endGeneralEntity(String string, Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.endGeneralEntity(string, augmentations);
        }
    }

    protected void handleStartElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations, boolean bl) throws XNIException {
        String string;
        String string2;
        this.fNamespaceContext.pushContext();
        if (qName.prefix == XMLSymbols.PREFIX_XMLNS) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementXMLNSPrefix", new Object[]{qName.rawname}, 2);
        }
        int n2 = xMLAttributes.getLength();
        int n3 = 0;
        while (n3 < n2) {
            string2 = xMLAttributes.getLocalName(n3);
            String string3 = xMLAttributes.getPrefix(n3);
            if (string3 == XMLSymbols.PREFIX_XMLNS || string3 == XMLSymbols.EMPTY_STRING && string2 == XMLSymbols.PREFIX_XMLNS) {
                string = this.fSymbolTable.addSymbol(xMLAttributes.getValue(n3));
                if (string3 == XMLSymbols.PREFIX_XMLNS && string2 == XMLSymbols.PREFIX_XMLNS) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[]{xMLAttributes.getQName(n3)}, 2);
                }
                if (string == NamespaceContext.XMLNS_URI) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[]{xMLAttributes.getQName(n3)}, 2);
                }
                if (string2 == XMLSymbols.PREFIX_XML) {
                    if (string != NamespaceContext.XML_URI) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[]{xMLAttributes.getQName(n3)}, 2);
                    }
                } else if (string == NamespaceContext.XML_URI) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[]{xMLAttributes.getQName(n3)}, 2);
                }
                String string4 = string3 = string2 != XMLSymbols.PREFIX_XMLNS ? string2 : XMLSymbols.EMPTY_STRING;
                if (this.prefixBoundToNullURI(string, string2)) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "EmptyPrefixedAttName", new Object[]{xMLAttributes.getQName(n3)}, 2);
                } else {
                    this.fNamespaceContext.declarePrefix(string3, string.length() != 0 ? string : null);
                }
            }
            ++n3;
        }
        string2 = qName.prefix != null ? qName.prefix : XMLSymbols.EMPTY_STRING;
        qName.uri = this.fNamespaceContext.getURI(string2);
        if (qName.prefix == null && qName.uri != null) {
            qName.prefix = XMLSymbols.EMPTY_STRING;
        }
        if (qName.prefix != null && qName.uri == null) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementPrefixUnbound", new Object[]{qName.prefix, qName.rawname}, 2);
        }
        int n4 = 0;
        while (n4 < n2) {
            xMLAttributes.getName(n4, this.fAttributeQName);
            string = this.fAttributeQName.prefix != null ? this.fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
            String string5 = this.fAttributeQName.rawname;
            if (string5 == XMLSymbols.PREFIX_XMLNS) {
                this.fAttributeQName.uri = this.fNamespaceContext.getURI(XMLSymbols.PREFIX_XMLNS);
                xMLAttributes.setName(n4, this.fAttributeQName);
            } else if (string != XMLSymbols.EMPTY_STRING) {
                this.fAttributeQName.uri = this.fNamespaceContext.getURI(string);
                if (this.fAttributeQName.uri == null) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributePrefixUnbound", new Object[]{qName.rawname, string5, string}, 2);
                }
                xMLAttributes.setName(n4, this.fAttributeQName);
            }
            ++n4;
        }
        int n5 = xMLAttributes.getLength();
        int n6 = 0;
        while (n6 < n5 - 1) {
            String string6 = xMLAttributes.getURI(n6);
            if (string6 != null && string6 != NamespaceContext.XMLNS_URI) {
                String string7 = xMLAttributes.getLocalName(n6);
                int n7 = n6 + 1;
                while (n7 < n5) {
                    String string8 = xMLAttributes.getLocalName(n7);
                    String string9 = xMLAttributes.getURI(n7);
                    if (string7 == string8 && string6 == string9) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNSNotUnique", new Object[]{qName.rawname, string7, string6}, 2);
                    }
                    ++n7;
                }
            }
            ++n6;
        }
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            if (bl) {
                this.fDocumentHandler.emptyElement(qName, xMLAttributes, augmentations);
            } else {
                this.fDocumentHandler.startElement(qName, xMLAttributes, augmentations);
            }
        }
    }

    protected void handleEndElement(QName qName, Augmentations augmentations, boolean bl) throws XNIException {
        String string = qName.prefix != null ? qName.prefix : XMLSymbols.EMPTY_STRING;
        qName.uri = this.fNamespaceContext.getURI(string);
        if (qName.uri != null) {
            qName.prefix = string;
        }
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents && !bl) {
            this.fDocumentHandler.endElement(qName, augmentations);
        }
        this.fNamespaceContext.popContext();
    }

    protected boolean prefixBoundToNullURI(String string, String string2) {
        return string == XMLSymbols.EMPTY_STRING && string2 != XMLSymbols.PREFIX_XMLNS;
    }
}

