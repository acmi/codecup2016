/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serialize;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xml.serialize.BaseMarkupSerializer;
import org.apache.xml.serialize.ElementState;
import org.apache.xml.serialize.HTMLdtd;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.Printer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class HTMLSerializer
extends BaseMarkupSerializer {
    private boolean _xhtml;
    private String fUserXHTMLNamespace = null;

    protected HTMLSerializer(boolean bl, OutputFormat outputFormat) {
        super(outputFormat);
        this._xhtml = bl;
    }

    public HTMLSerializer() {
        this(false, new OutputFormat("html", "ISO-8859-1", false));
    }

    public HTMLSerializer(OutputFormat outputFormat) {
        this(false, outputFormat != null ? outputFormat : new OutputFormat("html", "ISO-8859-1", false));
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        boolean bl = false;
        try {
            String string4;
            String string5;
            boolean bl2;
            Object object;
            if (this._printer == null) {
                throw new IllegalStateException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoWriterSupplied", null));
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
                if (this._indenting && !elementState.preserveSpace && (elementState.empty || elementState.afterElement)) {
                    this._printer.breakLine();
                }
            }
            boolean bl3 = elementState.preserveSpace;
            boolean bl4 = bl2 = string != null && string.length() != 0;
            if (string3 == null || string3.length() == 0) {
                string3 = string2;
                if (bl2 && (object = this.getPrefix(string)) != null && object.length() != 0) {
                    string3 = (String)object + ":" + string2;
                }
                bl = true;
            }
            String string6 = !bl2 ? string3 : (string.equals("http://www.w3.org/1999/xhtml") || this.fUserXHTMLNamespace != null && this.fUserXHTMLNamespace.equals(string) ? string2 : null);
            this._printer.printText('<');
            if (this._xhtml) {
                this._printer.printText(string3.toLowerCase(Locale.ENGLISH));
            } else {
                this._printer.printText(string3);
            }
            this._printer.indent();
            if (attributes != null) {
                int n2 = 0;
                while (n2 < attributes.getLength()) {
                    this._printer.printSpace();
                    string5 = attributes.getQName(n2).toLowerCase(Locale.ENGLISH);
                    string4 = attributes.getValue(n2);
                    if (this._xhtml || bl2) {
                        if (string4 == null) {
                            this._printer.printText(string5);
                            this._printer.printText("=\"\"");
                        } else {
                            this._printer.printText(string5);
                            this._printer.printText("=\"");
                            this.printEscaped(string4);
                            this._printer.printText('\"');
                        }
                    } else {
                        if (string4 == null) {
                            string4 = "";
                        }
                        if (!this._format.getPreserveEmptyAttributes() && string4.length() == 0) {
                            this._printer.printText(string5);
                        } else if (HTMLdtd.isURI(string3, string5)) {
                            this._printer.printText(string5);
                            this._printer.printText("=\"");
                            this._printer.printText(this.escapeURI(string4));
                            this._printer.printText('\"');
                        } else if (HTMLdtd.isBoolean(string3, string5)) {
                            this._printer.printText(string5);
                        } else {
                            this._printer.printText(string5);
                            this._printer.printText("=\"");
                            this.printEscaped(string4);
                            this._printer.printText('\"');
                        }
                    }
                    ++n2;
                }
            }
            if (string6 != null && HTMLdtd.isPreserveSpace(string6)) {
                bl3 = true;
            }
            if (bl) {
                object = this._prefixes.entrySet().iterator();
                while (object.hasNext()) {
                    this._printer.printSpace();
                    Map.Entry entry = (Map.Entry)object.next();
                    string4 = (String)entry.getKey();
                    string5 = (String)entry.getValue();
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
            elementState = this.enterElementState(string, string2, string3, bl3);
            if (string6 != null && (string6.equalsIgnoreCase("A") || string6.equalsIgnoreCase("TD"))) {
                elementState.empty = false;
                this._printer.printText('>');
            }
            if (string6 != null && (string3.equalsIgnoreCase("SCRIPT") || string3.equalsIgnoreCase("STYLE"))) {
                if (this._xhtml) {
                    elementState.doCData = true;
                } else {
                    elementState.unescaped = true;
                }
            }
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
        String string4 = elementState.namespaceURI == null || elementState.namespaceURI.length() == 0 ? elementState.rawName : (elementState.namespaceURI.equals("http://www.w3.org/1999/xhtml") || this.fUserXHTMLNamespace != null && this.fUserXHTMLNamespace.equals(elementState.namespaceURI) ? elementState.localName : null);
        if (this._xhtml) {
            if (elementState.empty) {
                this._printer.printText(" />");
            } else {
                if (elementState.inCData) {
                    this._printer.printText("]]>");
                }
                this._printer.printText("</");
                this._printer.printText(elementState.rawName.toLowerCase(Locale.ENGLISH));
                this._printer.printText('>');
            }
        } else {
            if (elementState.empty) {
                this._printer.printText('>');
            }
            if (string4 == null || !HTMLdtd.isOnlyOpening(string4)) {
                if (this._indenting && !elementState.preserveSpace && elementState.afterElement) {
                    this._printer.breakLine();
                }
                if (elementState.inCData) {
                    this._printer.printText("]]>");
                }
                this._printer.printText("</");
                this._printer.printText(elementState.rawName);
                this._printer.printText('>');
            }
        }
        elementState = this.leaveElementState();
        if (string4 == null || !string4.equalsIgnoreCase("A") && !string4.equalsIgnoreCase("TD")) {
            elementState.afterElement = true;
        }
        elementState.empty = false;
        if (this.isDocumentState()) {
            this._printer.flush();
        }
    }

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        try {
            ElementState elementState = this.content();
            elementState.doCData = false;
            super.characters(arrc, n2, n3);
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    public void startElement(String string, AttributeList attributeList) throws SAXException {
        try {
            if (this._printer == null) {
                throw new IllegalStateException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoWriterSupplied", null));
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
                if (this._indenting && !elementState.preserveSpace && (elementState.empty || elementState.afterElement)) {
                    this._printer.breakLine();
                }
            }
            boolean bl = elementState.preserveSpace;
            this._printer.printText('<');
            if (this._xhtml) {
                this._printer.printText(string.toLowerCase(Locale.ENGLISH));
            } else {
                this._printer.printText(string);
            }
            this._printer.indent();
            if (attributeList != null) {
                int n2 = 0;
                while (n2 < attributeList.getLength()) {
                    this._printer.printSpace();
                    String string2 = attributeList.getName(n2).toLowerCase(Locale.ENGLISH);
                    String string3 = attributeList.getValue(n2);
                    if (this._xhtml) {
                        if (string3 == null) {
                            this._printer.printText(string2);
                            this._printer.printText("=\"\"");
                        } else {
                            this._printer.printText(string2);
                            this._printer.printText("=\"");
                            this.printEscaped(string3);
                            this._printer.printText('\"');
                        }
                    } else {
                        if (string3 == null) {
                            string3 = "";
                        }
                        if (!this._format.getPreserveEmptyAttributes() && string3.length() == 0) {
                            this._printer.printText(string2);
                        } else if (HTMLdtd.isURI(string, string2)) {
                            this._printer.printText(string2);
                            this._printer.printText("=\"");
                            this._printer.printText(this.escapeURI(string3));
                            this._printer.printText('\"');
                        } else if (HTMLdtd.isBoolean(string, string2)) {
                            this._printer.printText(string2);
                        } else {
                            this._printer.printText(string2);
                            this._printer.printText("=\"");
                            this.printEscaped(string3);
                            this._printer.printText('\"');
                        }
                    }
                    ++n2;
                }
            }
            if (HTMLdtd.isPreserveSpace(string)) {
                bl = true;
            }
            elementState = this.enterElementState(null, null, string, bl);
            if (string.equalsIgnoreCase("A") || string.equalsIgnoreCase("TD")) {
                elementState.empty = false;
                this._printer.printText('>');
            }
            if (string.equalsIgnoreCase("SCRIPT") || string.equalsIgnoreCase("STYLE")) {
                if (this._xhtml) {
                    elementState.doCData = true;
                } else {
                    elementState.unescaped = true;
                }
            }
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    public void endElement(String string) throws SAXException {
        this.endElement(null, null, string);
    }

    protected void startDocument(String string) throws IOException {
        this._printer.leaveDTD();
        if (!this._started) {
            if (this._docTypePublicId == null && this._docTypeSystemId == null) {
                if (this._xhtml) {
                    this._docTypePublicId = "-//W3C//DTD XHTML 1.0 Strict//EN";
                    this._docTypeSystemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
                } else {
                    this._docTypePublicId = "-//W3C//DTD HTML 4.01//EN";
                    this._docTypeSystemId = "http://www.w3.org/TR/html4/strict.dtd";
                }
            }
            if (!this._format.getOmitDocumentType()) {
                if (!(this._docTypePublicId == null || this._xhtml && this._docTypeSystemId == null)) {
                    if (this._xhtml) {
                        this._printer.printText("<!DOCTYPE html PUBLIC ");
                    } else {
                        this._printer.printText("<!DOCTYPE HTML PUBLIC ");
                    }
                    this.printDoctypeURL(this._docTypePublicId);
                    if (this._docTypeSystemId != null) {
                        if (this._indenting) {
                            this._printer.breakLine();
                            this._printer.printText("                      ");
                        } else {
                            this._printer.printText(' ');
                        }
                        this.printDoctypeURL(this._docTypeSystemId);
                    }
                    this._printer.printText('>');
                    this._printer.breakLine();
                } else if (this._docTypeSystemId != null) {
                    if (this._xhtml) {
                        this._printer.printText("<!DOCTYPE html SYSTEM ");
                    } else {
                        this._printer.printText("<!DOCTYPE HTML SYSTEM ");
                    }
                    this.printDoctypeURL(this._docTypeSystemId);
                    this._printer.printText('>');
                    this._printer.breakLine();
                }
            }
        }
        this._started = true;
        this.serializePreRoot();
    }

    protected void serializeElement(Element element) throws IOException {
        String string = element.getTagName();
        ElementState elementState = this.getElementState();
        if (this.isDocumentState()) {
            if (!this._started) {
                this.startDocument(string);
            }
        } else {
            if (elementState.empty) {
                this._printer.printText('>');
            }
            if (this._indenting && !elementState.preserveSpace && (elementState.empty || elementState.afterElement)) {
                this._printer.breakLine();
            }
        }
        boolean bl = elementState.preserveSpace;
        this._printer.printText('<');
        if (this._xhtml) {
            this._printer.printText(string.toLowerCase(Locale.ENGLISH));
        } else {
            this._printer.printText(string);
        }
        this._printer.indent();
        NamedNodeMap namedNodeMap = element.getAttributes();
        if (namedNodeMap != null) {
            int n2 = 0;
            while (n2 < namedNodeMap.getLength()) {
                Attr attr = (Attr)namedNodeMap.item(n2);
                String string2 = attr.getName().toLowerCase(Locale.ENGLISH);
                String string3 = attr.getValue();
                if (attr.getSpecified()) {
                    this._printer.printSpace();
                    if (this._xhtml) {
                        if (string3 == null) {
                            this._printer.printText(string2);
                            this._printer.printText("=\"\"");
                        } else {
                            this._printer.printText(string2);
                            this._printer.printText("=\"");
                            this.printEscaped(string3);
                            this._printer.printText('\"');
                        }
                    } else {
                        if (string3 == null) {
                            string3 = "";
                        }
                        if (!this._format.getPreserveEmptyAttributes() && string3.length() == 0) {
                            this._printer.printText(string2);
                        } else if (HTMLdtd.isURI(string, string2)) {
                            this._printer.printText(string2);
                            this._printer.printText("=\"");
                            this._printer.printText(this.escapeURI(string3));
                            this._printer.printText('\"');
                        } else if (HTMLdtd.isBoolean(string, string2)) {
                            this._printer.printText(string2);
                        } else {
                            this._printer.printText(string2);
                            this._printer.printText("=\"");
                            this.printEscaped(string3);
                            this._printer.printText('\"');
                        }
                    }
                }
                ++n2;
            }
        }
        if (HTMLdtd.isPreserveSpace(string)) {
            bl = true;
        }
        if (element.hasChildNodes() || !HTMLdtd.isEmptyTag(string)) {
            elementState = this.enterElementState(null, null, string, bl);
            if (string.equalsIgnoreCase("A") || string.equalsIgnoreCase("TD")) {
                elementState.empty = false;
                this._printer.printText('>');
            }
            if (string.equalsIgnoreCase("SCRIPT") || string.equalsIgnoreCase("STYLE")) {
                if (this._xhtml) {
                    elementState.doCData = true;
                } else {
                    elementState.unescaped = true;
                }
            }
            Node node = element.getFirstChild();
            while (node != null) {
                this.serializeNode(node);
                node = node.getNextSibling();
            }
            this.endElementIO(null, null, string);
        } else {
            this._printer.unindent();
            if (this._xhtml) {
                this._printer.printText(" />");
            } else {
                this._printer.printText('>');
            }
            elementState.afterElement = true;
            elementState.empty = false;
            if (this.isDocumentState()) {
                this._printer.flush();
            }
        }
    }

    protected void characters(String string) throws IOException {
        this.content();
        super.characters(string);
    }

    protected String getEntityRef(int n2) {
        return HTMLdtd.fromChar(n2);
    }

    protected String escapeURI(String string) {
        int n2 = string.indexOf("\"");
        if (n2 >= 0) {
            return string.substring(0, n2);
        }
        return string;
    }
}

