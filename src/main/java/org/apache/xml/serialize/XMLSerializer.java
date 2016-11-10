/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serialize;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.xerces.dom.DOMErrorImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xml.serialize.BaseMarkupSerializer;
import org.apache.xml.serialize.ElementState;
import org.apache.xml.serialize.EncodingInfo;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.Printer;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSSerializerFilter;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class XMLSerializer
extends BaseMarkupSerializer {
    protected NamespaceSupport fNSBinder;
    protected NamespaceSupport fLocalNSBinder;
    protected SymbolTable fSymbolTable;
    protected boolean fNamespaces = false;
    protected boolean fNamespacePrefixes = true;
    private boolean fPreserveSpace;

    public XMLSerializer() {
        super(new OutputFormat("xml", null, false));
    }

    public XMLSerializer(OutputFormat outputFormat) {
        super(outputFormat != null ? outputFormat : new OutputFormat("xml", null, false));
        this._format.setMethod("xml");
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        try {
            String string4;
            Object object;
            String string5;
            Object object2;
            if (this._printer == null) {
                String string6 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoWriterSupplied", null);
                throw new IllegalStateException(string6);
            }
            ElementState elementState = this.getElementState();
            if (this.isDocumentState()) {
                if (!this._started) {
                    this.startDocument(string2 == null || string2.length() == 0 ? string3 : string2);
                }
            } else {
                if (elementState.empty) {
                    this._printer.printText('>');
                }
                if (elementState.inCData) {
                    this._printer.printText("]]>");
                    elementState.inCData = false;
                }
                if (this._indenting && !elementState.preserveSpace && (elementState.empty || elementState.afterElement || elementState.afterComment)) {
                    this._printer.breakLine();
                }
            }
            boolean bl = elementState.preserveSpace;
            attributes = this.extractNamespaces(attributes);
            if (string3 == null || string3.length() == 0) {
                if (string2 == null) {
                    String string7 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoName", null);
                    throw new SAXException(string7);
                }
                string3 = string != null && !string.equals("") ? ((object2 = this.getPrefix(string)) != null && object2.length() > 0 ? (String)object2 + ":" + string2 : string2) : string2;
            }
            this._printer.printText('<');
            this._printer.printText(string3);
            this._printer.indent();
            if (attributes != null) {
                int n2 = 0;
                while (n2 < attributes.getLength()) {
                    this._printer.printSpace();
                    string5 = attributes.getQName(n2);
                    if (string5 != null && string5.length() == 0) {
                        string5 = attributes.getLocalName(n2);
                        object = attributes.getURI(n2);
                        if (!(object == null || object.length() == 0 || string != null && string.length() != 0 && object.equals(string) || (object2 = this.getPrefix((String)object)) == null || object2.length() <= 0)) {
                            string5 = (String)object2 + ":" + string5;
                        }
                    }
                    if ((string4 = attributes.getValue(n2)) == null) {
                        string4 = "";
                    }
                    this._printer.printText(string5);
                    this._printer.printText("=\"");
                    this.printEscaped(string4);
                    this._printer.printText('\"');
                    if (string5.equals("xml:space")) {
                        bl = string4.equals("preserve") ? true : this._format.getPreserveSpace();
                    }
                    ++n2;
                }
            }
            if (this._prefixes != null) {
                object2 = this._prefixes.entrySet().iterator();
                while (object2.hasNext()) {
                    this._printer.printSpace();
                    object = (Map.Entry)object2.next();
                    string4 = (String)object.getKey();
                    string5 = (String)object.getValue();
                    if (string5.length() == 0) {
                        this._printer.printText("xmlns=\"");
                        this.printEscaped(string4);
                        this._printer.printText('\"');
                        continue;
                    }
                    this._printer.printText("xmlns:");
                    this._printer.printText(string5);
                    this._printer.printText("=\"");
                    this.printEscaped(string4);
                    this._printer.printText('\"');
                }
            }
            elementState = this.enterElementState(string, string2, string3, bl);
            string5 = string2 == null || string2.length() == 0 ? string3 : string + "^" + string2;
            elementState.doCData = this._format.isCDataElement(string5);
            elementState.unescaped = this._format.isNonEscapingElement(string5);
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    public void endElement(String string, String string2, String string3) throws SAXException {
        try {
            this.endElementIO(string, string2, string3);
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    public void endElementIO(String string, String string2, String string3) throws IOException {
        this._printer.unindent();
        ElementState elementState = this.getElementState();
        if (elementState.empty) {
            this._printer.printText("/>");
        } else {
            if (elementState.inCData) {
                this._printer.printText("]]>");
            }
            if (this._indenting && !elementState.preserveSpace && (elementState.afterElement || elementState.afterComment)) {
                this._printer.breakLine();
            }
            this._printer.printText("</");
            this._printer.printText(elementState.rawName);
            this._printer.printText('>');
        }
        elementState = this.leaveElementState();
        elementState.afterElement = true;
        elementState.afterComment = false;
        elementState.empty = false;
        if (this.isDocumentState()) {
            this._printer.flush();
        }
    }

    public void startElement(String string, AttributeList attributeList) throws SAXException {
        try {
            if (this._printer == null) {
                String string2 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoWriterSupplied", null);
                throw new IllegalStateException(string2);
            }
            ElementState elementState = this.getElementState();
            if (this.isDocumentState()) {
                if (!this._started) {
                    this.startDocument(string);
                }
            } else {
                if (elementState.empty) {
                    this._printer.printText('>');
                }
                if (elementState.inCData) {
                    this._printer.printText("]]>");
                    elementState.inCData = false;
                }
                if (this._indenting && !elementState.preserveSpace && (elementState.empty || elementState.afterElement || elementState.afterComment)) {
                    this._printer.breakLine();
                }
            }
            boolean bl = elementState.preserveSpace;
            this._printer.printText('<');
            this._printer.printText(string);
            this._printer.indent();
            if (attributeList != null) {
                int n2 = 0;
                while (n2 < attributeList.getLength()) {
                    this._printer.printSpace();
                    String string3 = attributeList.getName(n2);
                    String string4 = attributeList.getValue(n2);
                    if (string4 != null) {
                        this._printer.printText(string3);
                        this._printer.printText("=\"");
                        this.printEscaped(string4);
                        this._printer.printText('\"');
                    }
                    if (string3.equals("xml:space")) {
                        bl = string4.equals("preserve") ? true : this._format.getPreserveSpace();
                    }
                    ++n2;
                }
            }
            elementState = this.enterElementState(null, null, string, bl);
            elementState.doCData = this._format.isCDataElement(string);
            elementState.unescaped = this._format.isNonEscapingElement(string);
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    public void endElement(String string) throws SAXException {
        this.endElement(null, null, string);
    }

    protected void startDocument(String string) throws IOException {
        String string2 = this._printer.leaveDTD();
        if (!this._started) {
            if (!this._format.getOmitXMLDeclaration()) {
                StringBuffer stringBuffer = new StringBuffer("<?xml version=\"");
                if (this._format.getVersion() != null) {
                    stringBuffer.append(this._format.getVersion());
                } else {
                    stringBuffer.append("1.0");
                }
                stringBuffer.append('\"');
                String string3 = this._format.getEncoding();
                if (string3 != null) {
                    stringBuffer.append(" encoding=\"");
                    stringBuffer.append(string3);
                    stringBuffer.append('\"');
                }
                if (this._format.getStandalone() && this._docTypeSystemId == null && this._docTypePublicId == null) {
                    stringBuffer.append(" standalone=\"yes\"");
                }
                stringBuffer.append("?>");
                this._printer.printText(stringBuffer);
                this._printer.breakLine();
            }
            if (!this._format.getOmitDocumentType()) {
                if (this._docTypeSystemId != null) {
                    this._printer.printText("<!DOCTYPE ");
                    this._printer.printText(string);
                    if (this._docTypePublicId != null) {
                        this._printer.printText(" PUBLIC ");
                        this.printDoctypeURL(this._docTypePublicId);
                        if (this._indenting) {
                            this._printer.breakLine();
                            int n2 = 0;
                            while (n2 < 18 + string.length()) {
                                this._printer.printText(" ");
                                ++n2;
                            }
                        } else {
                            this._printer.printText(" ");
                        }
                        this.printDoctypeURL(this._docTypeSystemId);
                    } else {
                        this._printer.printText(" SYSTEM ");
                        this.printDoctypeURL(this._docTypeSystemId);
                    }
                    if (string2 != null && string2.length() > 0) {
                        this._printer.printText(" [");
                        this.printText(string2, true, true);
                        this._printer.printText(']');
                    }
                    this._printer.printText(">");
                    this._printer.breakLine();
                } else if (string2 != null && string2.length() > 0) {
                    this._printer.printText("<!DOCTYPE ");
                    this._printer.printText(string);
                    this._printer.printText(" [");
                    this.printText(string2, true, true);
                    this._printer.printText("]>");
                    this._printer.breakLine();
                }
            }
        }
        this._started = true;
        this.serializePreRoot();
    }

    protected void serializeElement(Element element) throws IOException {
        int n2;
        String string;
        Attr attr;
        String string2;
        if (this.fNamespaces) {
            this.fLocalNSBinder.reset();
            this.fNSBinder.pushContext();
        }
        String string3 = element.getTagName();
        ElementState elementState = this.getElementState();
        if (this.isDocumentState()) {
            if (!this._started) {
                this.startDocument(string3);
            }
        } else {
            if (elementState.empty) {
                this._printer.printText('>');
            }
            if (elementState.inCData) {
                this._printer.printText("]]>");
                elementState.inCData = false;
            }
            if (this._indenting && !elementState.preserveSpace && (elementState.empty || elementState.afterElement || elementState.afterComment)) {
                this._printer.breakLine();
            }
        }
        this.fPreserveSpace = elementState.preserveSpace;
        int n3 = 0;
        NamedNodeMap namedNodeMap = null;
        if (element.hasAttributes()) {
            namedNodeMap = element.getAttributes();
            n3 = namedNodeMap.getLength();
        }
        if (!this.fNamespaces) {
            this._printer.printText('<');
            this._printer.printText(string3);
            this._printer.indent();
            n2 = 0;
            while (n2 < n3) {
                attr = (Attr)namedNodeMap.item(n2);
                string = attr.getName();
                string2 = attr.getValue();
                if (string2 == null) {
                    string2 = "";
                }
                this.printAttribute(string, string2, attr.getSpecified(), attr);
                ++n2;
            }
        } else {
            String string4;
            boolean bl;
            String string5;
            String string6;
            n2 = 0;
            while (n2 < n3) {
                attr = (Attr)namedNodeMap.item(n2);
                string4 = attr.getNamespaceURI();
                if (string4 != null && string4.equals(NamespaceContext.XMLNS_URI)) {
                    string2 = attr.getNodeValue();
                    if (string2 == null) {
                        string2 = XMLSymbols.EMPTY_STRING;
                    }
                    if (string2.equals(NamespaceContext.XMLNS_URI)) {
                        if (this.fDOMErrorHandler != null) {
                            string6 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "CantBindXMLNS", null);
                            this.modifyDOMError(string6, 2, null, attr);
                            bl = this.fDOMErrorHandler.handleError(this.fDOMError);
                            if (!bl) {
                                throw new RuntimeException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "SerializationStopped", null));
                            }
                        }
                    } else {
                        string5 = attr.getPrefix();
                        string5 = string5 == null || string5.length() == 0 ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(string5);
                        string6 = this.fSymbolTable.addSymbol(attr.getLocalName());
                        if (string5 == XMLSymbols.PREFIX_XMLNS) {
                            if ((string2 = this.fSymbolTable.addSymbol(string2)).length() != 0) {
                                this.fNSBinder.declarePrefix(string6, string2);
                            }
                        } else {
                            string2 = this.fSymbolTable.addSymbol(string2);
                            this.fNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, string2);
                        }
                    }
                }
                ++n2;
            }
            string4 = element.getNamespaceURI();
            string5 = element.getPrefix();
            if (string4 != null && string5 != null && string4.length() == 0 && string5.length() != 0) {
                string5 = null;
                this._printer.printText('<');
                this._printer.printText(element.getLocalName());
                this._printer.indent();
            } else {
                this._printer.printText('<');
                this._printer.printText(string3);
                this._printer.indent();
            }
            if (string4 != null) {
                string4 = this.fSymbolTable.addSymbol(string4);
                String string7 = string5 = string5 == null || string5.length() == 0 ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(string5);
                if (this.fNSBinder.getURI(string5) != string4) {
                    if (this.fNamespacePrefixes) {
                        this.printNamespaceAttr(string5, string4);
                    }
                    this.fLocalNSBinder.declarePrefix(string5, string4);
                    this.fNSBinder.declarePrefix(string5, string4);
                }
            } else if (element.getLocalName() == null) {
                if (this.fDOMErrorHandler != null) {
                    string6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NullLocalElementName", new Object[]{element.getNodeName()});
                    this.modifyDOMError(string6, 2, null, element);
                    bl = this.fDOMErrorHandler.handleError(this.fDOMError);
                    if (!bl) {
                        throw new RuntimeException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "SerializationStopped", null));
                    }
                }
            } else {
                string4 = this.fNSBinder.getURI(XMLSymbols.EMPTY_STRING);
                if (string4 != null && string4.length() > 0) {
                    if (this.fNamespacePrefixes) {
                        this.printNamespaceAttr(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
                    }
                    this.fLocalNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
                    this.fNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
                }
            }
            n2 = 0;
            while (n2 < n3) {
                attr = (Attr)namedNodeMap.item(n2);
                string2 = attr.getValue();
                string = attr.getNodeName();
                string4 = attr.getNamespaceURI();
                if (string4 != null && string4.length() == 0) {
                    string4 = null;
                    string = attr.getLocalName();
                }
                if (string2 == null) {
                    string2 = XMLSymbols.EMPTY_STRING;
                }
                if (string4 != null) {
                    string5 = attr.getPrefix();
                    string5 = string5 == null ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(string5);
                    string6 = this.fSymbolTable.addSymbol(attr.getLocalName());
                    if (string4 != null && string4.equals(NamespaceContext.XMLNS_URI)) {
                        String string8;
                        string5 = attr.getPrefix();
                        string5 = string5 == null || string5.length() == 0 ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(string5);
                        string6 = this.fSymbolTable.addSymbol(attr.getLocalName());
                        if (string5 == XMLSymbols.PREFIX_XMLNS) {
                            string8 = this.fLocalNSBinder.getURI(string6);
                            if ((string2 = this.fSymbolTable.addSymbol(string2)).length() != 0 && string8 == null) {
                                if (this.fNamespacePrefixes) {
                                    this.printNamespaceAttr(string6, string2);
                                }
                                this.fLocalNSBinder.declarePrefix(string6, string2);
                            }
                        } else {
                            string4 = this.fNSBinder.getURI(XMLSymbols.EMPTY_STRING);
                            string8 = this.fLocalNSBinder.getURI(XMLSymbols.EMPTY_STRING);
                            string2 = this.fSymbolTable.addSymbol(string2);
                            if (string8 == null && this.fNamespacePrefixes) {
                                this.printNamespaceAttr(XMLSymbols.EMPTY_STRING, string2);
                            }
                        }
                    } else {
                        string4 = this.fSymbolTable.addSymbol(string4);
                        String string9 = this.fNSBinder.getURI(string5);
                        if (string5 == XMLSymbols.EMPTY_STRING || string9 != string4) {
                            string = attr.getNodeName();
                            String string10 = this.fNSBinder.getPrefix(string4);
                            if (string10 != null && string10 != XMLSymbols.EMPTY_STRING) {
                                string5 = string10;
                                string = string5 + ":" + string6;
                            } else {
                                if (string5 == XMLSymbols.EMPTY_STRING || this.fLocalNSBinder.getURI(string5) != null) {
                                    int n4 = 1;
                                    string5 = this.fSymbolTable.addSymbol("NS" + n4++);
                                    while (this.fLocalNSBinder.getURI(string5) != null) {
                                        string5 = this.fSymbolTable.addSymbol("NS" + n4++);
                                    }
                                    string = string5 + ":" + string6;
                                }
                                if (this.fNamespacePrefixes) {
                                    this.printNamespaceAttr(string5, string4);
                                }
                                string2 = this.fSymbolTable.addSymbol(string2);
                                this.fLocalNSBinder.declarePrefix(string5, string2);
                                this.fNSBinder.declarePrefix(string5, string4);
                            }
                        }
                        this.printAttribute(string, string2 == null ? XMLSymbols.EMPTY_STRING : string2, attr.getSpecified(), attr);
                    }
                } else if (attr.getLocalName() == null) {
                    if (this.fDOMErrorHandler != null) {
                        string6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NullLocalAttrName", new Object[]{attr.getNodeName()});
                        this.modifyDOMError(string6, 2, null, attr);
                        bl = this.fDOMErrorHandler.handleError(this.fDOMError);
                        if (!bl) {
                            throw new RuntimeException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "SerializationStopped", null));
                        }
                    }
                    this.printAttribute(string, string2, attr.getSpecified(), attr);
                } else {
                    this.printAttribute(string, string2, attr.getSpecified(), attr);
                }
                ++n2;
            }
        }
        if (element.hasChildNodes()) {
            elementState = this.enterElementState(null, null, string3, this.fPreserveSpace);
            elementState.doCData = this._format.isCDataElement(string3);
            elementState.unescaped = this._format.isNonEscapingElement(string3);
            Node node = element.getFirstChild();
            while (node != null) {
                this.serializeNode(node);
                node = node.getNextSibling();
            }
            if (this.fNamespaces) {
                this.fNSBinder.popContext();
            }
            this.endElementIO(null, null, string3);
        } else {
            if (this.fNamespaces) {
                this.fNSBinder.popContext();
            }
            this._printer.unindent();
            this._printer.printText("/>");
            elementState.afterElement = true;
            elementState.afterComment = false;
            elementState.empty = false;
            if (this.isDocumentState()) {
                this._printer.flush();
            }
        }
    }

    private void printNamespaceAttr(String string, String string2) throws IOException {
        this._printer.printSpace();
        if (string == XMLSymbols.EMPTY_STRING) {
            this._printer.printText(XMLSymbols.PREFIX_XMLNS);
        } else {
            this._printer.printText("xmlns:" + string);
        }
        this._printer.printText("=\"");
        this.printEscaped(string2);
        this._printer.printText('\"');
    }

    private void printAttribute(String string, String string2, boolean bl, Attr attr) throws IOException {
        if (bl || (this.features & 64) == 0) {
            if (this.fDOMFilter != null && (this.fDOMFilter.getWhatToShow() & 2) != 0) {
                short s2 = this.fDOMFilter.acceptNode(attr);
                switch (s2) {
                    case 2: 
                    case 3: {
                        return;
                    }
                }
            }
            this._printer.printSpace();
            this._printer.printText(string);
            this._printer.printText("=\"");
            this.printEscaped(string2);
            this._printer.printText('\"');
        }
        if (string.equals("xml:space")) {
            this.fPreserveSpace = string2.equals("preserve") ? true : this._format.getPreserveSpace();
        }
    }

    protected String getEntityRef(int n2) {
        switch (n2) {
            case 60: {
                return "lt";
            }
            case 62: {
                return "gt";
            }
            case 34: {
                return "quot";
            }
            case 39: {
                return "apos";
            }
            case 38: {
                return "amp";
            }
        }
        return null;
    }

    private Attributes extractNamespaces(Attributes attributes) throws SAXException {
        if (attributes == null) {
            return null;
        }
        int n2 = attributes.getLength();
        AttributesImpl attributesImpl = new AttributesImpl(attributes);
        int n3 = n2 - 1;
        while (n3 >= 0) {
            String string = attributesImpl.getQName(n3);
            if (string.startsWith("xmlns")) {
                if (string.length() == 5) {
                    this.startPrefixMapping("", attributes.getValue(n3));
                    attributesImpl.removeAttribute(n3);
                } else if (string.charAt(5) == ':') {
                    this.startPrefixMapping(string.substring(6), attributes.getValue(n3));
                    attributesImpl.removeAttribute(n3);
                }
            }
            --n3;
        }
        return attributesImpl;
    }

    protected void printEscaped(String string) throws IOException {
        int n2 = string.length();
        int n3 = 0;
        while (n3 < n2) {
            char c2 = string.charAt(n3);
            if (!XMLChar.isValid(c2)) {
                if (++n3 < n2) {
                    this.surrogates(c2, string.charAt(n3), false);
                } else {
                    this.fatalError("The character '" + c2 + "' is an invalid XML character");
                }
            } else if (c2 == '\n' || c2 == '\r' || c2 == '\t') {
                this.printHex(c2);
            } else if (c2 == '<') {
                this._printer.printText("&lt;");
            } else if (c2 == '&') {
                this._printer.printText("&amp;");
            } else if (c2 == '\"') {
                this._printer.printText("&quot;");
            } else if (c2 >= ' ' && this._encodingInfo.isPrintable(c2)) {
                this._printer.printText(c2);
            } else {
                this.printHex(c2);
            }
            ++n3;
        }
    }

    protected void printXMLChar(int n2) throws IOException {
        if (n2 == 13) {
            this.printHex(n2);
        } else if (n2 == 60) {
            this._printer.printText("&lt;");
        } else if (n2 == 38) {
            this._printer.printText("&amp;");
        } else if (n2 == 62) {
            this._printer.printText("&gt;");
        } else if (n2 == 10 || n2 == 9 || n2 >= 32 && this._encodingInfo.isPrintable((char)n2)) {
            this._printer.printText((char)n2);
        } else {
            this.printHex(n2);
        }
    }

    protected void printText(String string, boolean bl, boolean bl2) throws IOException {
        int n2 = string.length();
        if (bl) {
            int n3 = 0;
            while (n3 < n2) {
                char c2 = string.charAt(n3);
                if (!XMLChar.isValid(c2)) {
                    if (++n3 < n2) {
                        this.surrogates(c2, string.charAt(n3), true);
                    } else {
                        this.fatalError("The character '" + c2 + "' is an invalid XML character");
                    }
                } else if (bl2) {
                    this._printer.printText(c2);
                } else {
                    this.printXMLChar(c2);
                }
                ++n3;
            }
        } else {
            int n4 = 0;
            while (n4 < n2) {
                char c3 = string.charAt(n4);
                if (!XMLChar.isValid(c3)) {
                    if (++n4 < n2) {
                        this.surrogates(c3, string.charAt(n4), true);
                    } else {
                        this.fatalError("The character '" + c3 + "' is an invalid XML character");
                    }
                } else if (bl2) {
                    this._printer.printText(c3);
                } else {
                    this.printXMLChar(c3);
                }
                ++n4;
            }
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    protected void printText(char[] var1_1, int var2_2, int var3_3, boolean var4_4, boolean var5_5) throws IOException {
        if (!var4_4) ** GOTO lbl25
        while (var3_3-- > 0) {
            if (!XMLChar.isValid(var6_6 = var1_1[var2_2++])) {
                if (var3_3-- > 0) {
                    this.surrogates(var6_6, var1_1[var2_2++], true);
                    continue;
                }
                this.fatalError("The character '" + var6_6 + "' is an invalid XML character");
                continue;
            }
            if (var5_5) {
                this._printer.printText(var6_6);
                continue;
            }
            this.printXMLChar(var6_6);
        }
        return;
lbl-1000: // 1 sources:
        {
            if (!XMLChar.isValid(var6_7 = var1_1[var2_2++])) {
                if (var3_3-- > 0) {
                    this.surrogates(var6_7, var1_1[var2_2++], true);
                    continue;
                }
                this.fatalError("The character '" + var6_7 + "' is an invalid XML character");
                continue;
            }
            if (var5_5) {
                this._printer.printText(var6_7);
                continue;
            }
            this.printXMLChar(var6_7);
lbl25: // 5 sources:
            ** while (var3_3-- > 0)
        }
lbl26: // 1 sources:
    }

    protected void checkUnboundNamespacePrefixedNode(Node node) throws IOException {
        if (this.fNamespaces) {
            Node node2 = node.getFirstChild();
            while (node2 != null) {
                Node node3 = node2.getNextSibling();
                String string = node2.getPrefix();
                String string2 = string = string == null || string.length() == 0 ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(string);
                if (this.fNSBinder.getURI(string) == null && string != null) {
                    this.fatalError("The replacement text of the entity node '" + node.getNodeName() + "' contains an element node '" + node2.getNodeName() + "' with an undeclared prefix '" + string + "'.");
                }
                if (node2.getNodeType() == 1) {
                    NamedNodeMap namedNodeMap = node2.getAttributes();
                    int n2 = 0;
                    while (n2 < namedNodeMap.getLength()) {
                        String string3 = namedNodeMap.item(n2).getPrefix();
                        String string4 = string3 = string3 == null || string3.length() == 0 ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(string3);
                        if (this.fNSBinder.getURI(string3) == null && string3 != null) {
                            this.fatalError("The replacement text of the entity node '" + node.getNodeName() + "' contains an element node '" + node2.getNodeName() + "' with an attribute '" + namedNodeMap.item(n2).getNodeName() + "' an undeclared prefix '" + string3 + "'.");
                        }
                        ++n2;
                    }
                }
                if (node2.hasChildNodes()) {
                    this.checkUnboundNamespacePrefixedNode(node2);
                }
                node2 = node3;
            }
        }
    }

    public boolean reset() {
        super.reset();
        if (this.fNSBinder != null) {
            this.fNSBinder.reset();
            this.fNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
        }
        return true;
    }
}

