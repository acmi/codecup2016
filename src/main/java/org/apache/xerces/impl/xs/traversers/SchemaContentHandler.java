/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.opti.SchemaDOMParser;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SAXLocatorWrapper;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLParseException;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.LocatorImpl;

final class SchemaContentHandler
implements ContentHandler {
    private SymbolTable fSymbolTable;
    private SchemaDOMParser fSchemaDOMParser;
    private final SAXLocatorWrapper fSAXLocatorWrapper = new SAXLocatorWrapper();
    private NamespaceSupport fNamespaceContext = new NamespaceSupport();
    private boolean fNeedPushNSContext;
    private boolean fNamespacePrefixes = false;
    private boolean fStringsInternalized = false;
    private final QName fElementQName = new QName();
    private final QName fAttributeQName = new QName();
    private final XMLAttributesImpl fAttributes = new XMLAttributesImpl();
    private final XMLString fTempString = new XMLString();
    private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();

    public Document getDocument() {
        return this.fSchemaDOMParser.getDocument();
    }

    public void setDocumentLocator(Locator locator) {
        this.fSAXLocatorWrapper.setLocator(locator);
    }

    public void startDocument() throws SAXException {
        this.fNeedPushNSContext = true;
        this.fNamespaceContext.reset();
        try {
            this.fSchemaDOMParser.startDocument(this.fSAXLocatorWrapper, null, this.fNamespaceContext, null);
        }
        catch (XMLParseException xMLParseException) {
            SchemaContentHandler.convertToSAXParseException(xMLParseException);
        }
        catch (XNIException xNIException) {
            SchemaContentHandler.convertToSAXException(xNIException);
        }
    }

    public void endDocument() throws SAXException {
        this.fSAXLocatorWrapper.setLocator(null);
        try {
            this.fSchemaDOMParser.endDocument(null);
        }
        catch (XMLParseException xMLParseException) {
            SchemaContentHandler.convertToSAXParseException(xMLParseException);
        }
        catch (XNIException xNIException) {
            SchemaContentHandler.convertToSAXException(xNIException);
        }
    }

    public void startPrefixMapping(String string, String string2) throws SAXException {
        if (this.fNeedPushNSContext) {
            this.fNeedPushNSContext = false;
            this.fNamespaceContext.pushContext();
        }
        if (!this.fStringsInternalized) {
            string = string != null ? this.fSymbolTable.addSymbol(string) : XMLSymbols.EMPTY_STRING;
            string2 = string2 != null && string2.length() > 0 ? this.fSymbolTable.addSymbol(string2) : null;
        } else {
            if (string == null) {
                string = XMLSymbols.EMPTY_STRING;
            }
            if (string2 != null && string2.length() == 0) {
                string2 = null;
            }
        }
        this.fNamespaceContext.declarePrefix(string, string2);
    }

    public void endPrefixMapping(String string) throws SAXException {
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        int n2;
        if (this.fNeedPushNSContext) {
            this.fNamespaceContext.pushContext();
        }
        this.fNeedPushNSContext = true;
        this.fillQName(this.fElementQName, string, string2, string3);
        this.fillXMLAttributes(attributes);
        if (!this.fNamespacePrefixes && (n2 = this.fNamespaceContext.getDeclaredPrefixCount()) > 0) {
            this.addNamespaceDeclarations(n2);
        }
        try {
            this.fSchemaDOMParser.startElement(this.fElementQName, this.fAttributes, null);
        }
        catch (XMLParseException xMLParseException) {
            SchemaContentHandler.convertToSAXParseException(xMLParseException);
        }
        catch (XNIException xNIException) {
            SchemaContentHandler.convertToSAXException(xNIException);
        }
    }

    public void endElement(String string, String string2, String string3) throws SAXException {
        block5 : {
            this.fillQName(this.fElementQName, string, string2, string3);
            try {
                try {
                    this.fSchemaDOMParser.endElement(this.fElementQName, null);
                }
                catch (XMLParseException xMLParseException) {
                    SchemaContentHandler.convertToSAXParseException(xMLParseException);
                    Object var7_5 = null;
                    this.fNamespaceContext.popContext();
                    break block5;
                }
                catch (XNIException xNIException) {
                    SchemaContentHandler.convertToSAXException(xNIException);
                    Object var7_6 = null;
                    this.fNamespaceContext.popContext();
                }
                Object var7_4 = null;
                this.fNamespaceContext.popContext();
            }
            catch (Throwable throwable) {
                Object var7_7 = null;
                this.fNamespaceContext.popContext();
                throw throwable;
            }
        }
    }

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        try {
            this.fTempString.setValues(arrc, n2, n3);
            this.fSchemaDOMParser.characters(this.fTempString, null);
        }
        catch (XMLParseException xMLParseException) {
            SchemaContentHandler.convertToSAXParseException(xMLParseException);
        }
        catch (XNIException xNIException) {
            SchemaContentHandler.convertToSAXException(xNIException);
        }
    }

    public void ignorableWhitespace(char[] arrc, int n2, int n3) throws SAXException {
        try {
            this.fTempString.setValues(arrc, n2, n3);
            this.fSchemaDOMParser.ignorableWhitespace(this.fTempString, null);
        }
        catch (XMLParseException xMLParseException) {
            SchemaContentHandler.convertToSAXParseException(xMLParseException);
        }
        catch (XNIException xNIException) {
            SchemaContentHandler.convertToSAXException(xNIException);
        }
    }

    public void processingInstruction(String string, String string2) throws SAXException {
        try {
            this.fTempString.setValues(string2.toCharArray(), 0, string2.length());
            this.fSchemaDOMParser.processingInstruction(string, this.fTempString, null);
        }
        catch (XMLParseException xMLParseException) {
            SchemaContentHandler.convertToSAXParseException(xMLParseException);
        }
        catch (XNIException xNIException) {
            SchemaContentHandler.convertToSAXException(xNIException);
        }
    }

    public void skippedEntity(String string) throws SAXException {
    }

    private void fillQName(QName qName, String string, String string2, String string3) {
        if (!this.fStringsInternalized) {
            string = string != null && string.length() > 0 ? this.fSymbolTable.addSymbol(string) : null;
            string2 = string2 != null ? this.fSymbolTable.addSymbol(string2) : XMLSymbols.EMPTY_STRING;
            string3 = string3 != null ? this.fSymbolTable.addSymbol(string3) : XMLSymbols.EMPTY_STRING;
        } else {
            if (string != null && string.length() == 0) {
                string = null;
            }
            if (string2 == null) {
                string2 = XMLSymbols.EMPTY_STRING;
            }
            if (string3 == null) {
                string3 = XMLSymbols.EMPTY_STRING;
            }
        }
        String string4 = XMLSymbols.EMPTY_STRING;
        int n2 = string3.indexOf(58);
        if (n2 != -1) {
            string4 = this.fSymbolTable.addSymbol(string3.substring(0, n2));
            if (string2 == XMLSymbols.EMPTY_STRING) {
                string2 = this.fSymbolTable.addSymbol(string3.substring(n2 + 1));
            }
        } else if (string2 == XMLSymbols.EMPTY_STRING) {
            string2 = string3;
        }
        qName.setValues(string4, string2, string3, string);
    }

    private void fillXMLAttributes(Attributes attributes) {
        this.fAttributes.removeAllAttributes();
        int n2 = attributes.getLength();
        int n3 = 0;
        while (n3 < n2) {
            this.fillQName(this.fAttributeQName, attributes.getURI(n3), attributes.getLocalName(n3), attributes.getQName(n3));
            String string = attributes.getType(n3);
            this.fAttributes.addAttributeNS(this.fAttributeQName, string != null ? string : XMLSymbols.fCDATASymbol, attributes.getValue(n3));
            this.fAttributes.setSpecified(n3, true);
            ++n3;
        }
    }

    private void addNamespaceDeclarations(int n2) {
        String string = null;
        String string2 = null;
        String string3 = null;
        String string4 = null;
        String string5 = null;
        int n3 = 0;
        while (n3 < n2) {
            string4 = this.fNamespaceContext.getDeclaredPrefixAt(n3);
            string5 = this.fNamespaceContext.getURI(string4);
            if (string4.length() > 0) {
                string = XMLSymbols.PREFIX_XMLNS;
                string2 = string4;
                this.fStringBuffer.clear();
                this.fStringBuffer.append(string);
                this.fStringBuffer.append(':');
                this.fStringBuffer.append(string2);
                string3 = this.fSymbolTable.addSymbol(this.fStringBuffer.ch, this.fStringBuffer.offset, this.fStringBuffer.length);
            } else {
                string = XMLSymbols.EMPTY_STRING;
                string2 = XMLSymbols.PREFIX_XMLNS;
                string3 = XMLSymbols.PREFIX_XMLNS;
            }
            this.fAttributeQName.setValues(string, string2, string3, NamespaceContext.XMLNS_URI);
            this.fAttributes.addAttribute(this.fAttributeQName, XMLSymbols.fCDATASymbol, string5 != null ? string5 : XMLSymbols.EMPTY_STRING);
            ++n3;
        }
    }

    public void reset(SchemaDOMParser schemaDOMParser, SymbolTable symbolTable, boolean bl, boolean bl2) {
        this.fSchemaDOMParser = schemaDOMParser;
        this.fSymbolTable = symbolTable;
        this.fNamespacePrefixes = bl;
        this.fStringsInternalized = bl2;
    }

    static void convertToSAXParseException(XMLParseException xMLParseException) throws SAXException {
        Exception exception = xMLParseException.getException();
        if (exception == null) {
            LocatorImpl locatorImpl = new LocatorImpl();
            locatorImpl.setPublicId(xMLParseException.getPublicId());
            locatorImpl.setSystemId(xMLParseException.getExpandedSystemId());
            locatorImpl.setLineNumber(xMLParseException.getLineNumber());
            locatorImpl.setColumnNumber(xMLParseException.getColumnNumber());
            throw new SAXParseException(xMLParseException.getMessage(), locatorImpl);
        }
        if (exception instanceof SAXException) {
            throw (SAXException)exception;
        }
        throw new SAXException(exception);
    }

    static void convertToSAXException(XNIException xNIException) throws SAXException {
        Exception exception = xNIException.getException();
        if (exception == null) {
            throw new SAXException(xNIException.getMessage());
        }
        if (exception instanceof SAXException) {
            throw (SAXException)exception;
        }
        throw new SAXException(exception);
    }
}

