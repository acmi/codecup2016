/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serialize;

import java.io.IOException;
import org.apache.xerces.dom.DOMErrorImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.util.XMLChar;
import org.apache.xml.serialize.ElementState;
import org.apache.xml.serialize.EncodingInfo;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.Printer;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XML11Serializer
extends XMLSerializer {
    protected boolean fDOML1 = false;
    protected int fNamespaceCounter = 1;
    protected boolean fNamespaces = false;

    public XML11Serializer() {
        this._format.setVersion("1.1");
    }

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        try {
            ElementState elementState = this.content();
            if (elementState.inCData || elementState.doCData) {
                if (!elementState.inCData) {
                    this._printer.printText("<![CDATA[");
                    elementState.inCData = true;
                }
                int n4 = this._printer.getNextIndent();
                this._printer.setNextIndent(0);
                int n5 = n2 + n3;
                int n6 = n2;
                while (n6 < n5) {
                    char c2 = arrc[n6];
                    if (c2 == ']' && n6 + 2 < n5 && arrc[n6 + 1] == ']' && arrc[n6 + 2] == '>') {
                        this._printer.printText("]]]]><![CDATA[>");
                        n6 += 2;
                    } else if (!XML11Char.isXML11Valid(c2)) {
                        if (++n6 < n5) {
                            this.surrogates(c2, arrc[n6], true);
                        } else {
                            this.fatalError("The character '" + c2 + "' is an invalid XML character");
                        }
                    } else if (this._encodingInfo.isPrintable(c2) && XML11Char.isXML11ValidLiteral(c2)) {
                        this._printer.printText(c2);
                    } else {
                        this._printer.printText("]]>&#x");
                        this._printer.printText(Integer.toHexString(c2));
                        this._printer.printText(";<![CDATA[");
                    }
                    ++n6;
                }
                this._printer.setNextIndent(n4);
            } else if (elementState.preserveSpace) {
                int n7 = this._printer.getNextIndent();
                this._printer.setNextIndent(0);
                this.printText(arrc, n2, n3, true, elementState.unescaped);
                this._printer.setNextIndent(n7);
            } else {
                this.printText(arrc, n2, n3, false, elementState.unescaped);
            }
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    protected void printEscaped(String string) throws IOException {
        int n2 = string.length();
        int n3 = 0;
        while (n3 < n2) {
            char c2 = string.charAt(n3);
            if (!XML11Char.isXML11Valid(c2)) {
                if (++n3 < n2) {
                    this.surrogates(c2, string.charAt(n3), false);
                } else {
                    this.fatalError("The character '" + c2 + "' is an invalid XML character");
                }
            } else if (c2 == '\n' || c2 == '\r' || c2 == '\t' || c2 == '\u0085' || c2 == '\u2028') {
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

    protected final void printCDATAText(String string) throws IOException {
        int n2 = string.length();
        int n3 = 0;
        while (n3 < n2) {
            char c2 = string.charAt(n3);
            if (c2 == ']' && n3 + 2 < n2 && string.charAt(n3 + 1) == ']' && string.charAt(n3 + 2) == '>') {
                if (this.fDOMErrorHandler != null) {
                    String string2;
                    if ((this.features & 16) == 0 && (this.features & 2) == 0) {
                        string2 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "EndingCDATA", null);
                        this.modifyDOMError(string2, 3, null, this.fCurrentNode);
                        boolean bl = this.fDOMErrorHandler.handleError(this.fDOMError);
                        if (!bl) {
                            throw new IOException();
                        }
                    } else {
                        string2 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "SplittingCDATA", null);
                        this.modifyDOMError(string2, 1, null, this.fCurrentNode);
                        this.fDOMErrorHandler.handleError(this.fDOMError);
                    }
                }
                this._printer.printText("]]]]><![CDATA[>");
                n3 += 2;
            } else if (!XML11Char.isXML11Valid(c2)) {
                if (++n3 < n2) {
                    this.surrogates(c2, string.charAt(n3), true);
                } else {
                    this.fatalError("The character '" + c2 + "' is an invalid XML character");
                }
            } else if (this._encodingInfo.isPrintable(c2) && XML11Char.isXML11ValidLiteral(c2)) {
                this._printer.printText(c2);
            } else {
                this._printer.printText("]]>&#x");
                this._printer.printText(Integer.toHexString(c2));
                this._printer.printText(";<![CDATA[");
            }
            ++n3;
        }
    }

    protected final void printXMLChar(int n2) throws IOException {
        if (n2 == 13 || n2 == 133 || n2 == 8232) {
            this.printHex(n2);
        } else if (n2 == 60) {
            this._printer.printText("&lt;");
        } else if (n2 == 38) {
            this._printer.printText("&amp;");
        } else if (n2 == 62) {
            this._printer.printText("&gt;");
        } else if (this._encodingInfo.isPrintable((char)n2) && XML11Char.isXML11ValidLiteral(n2)) {
            this._printer.printText((char)n2);
        } else {
            this.printHex(n2);
        }
    }

    protected final void surrogates(int n2, int n3, boolean bl) throws IOException {
        if (XMLChar.isHighSurrogate(n2)) {
            if (!XMLChar.isLowSurrogate(n3)) {
                this.fatalError("The character '" + (char)n3 + "' is an invalid XML character");
            } else {
                int n4 = XMLChar.supplemental((char)n2, (char)n3);
                if (!XML11Char.isXML11Valid(n4)) {
                    this.fatalError("The character '" + (char)n4 + "' is an invalid XML character");
                } else if (bl && this.content().inCData) {
                    this._printer.printText("]]>&#x");
                    this._printer.printText(Integer.toHexString(n4));
                    this._printer.printText(";<![CDATA[");
                } else {
                    this.printHex(n4);
                }
            }
        } else {
            this.fatalError("The character '" + (char)n2 + "' is an invalid XML character");
        }
    }

    protected void printText(String string, boolean bl, boolean bl2) throws IOException {
        int n2 = string.length();
        if (bl) {
            int n3 = 0;
            while (n3 < n2) {
                char c2 = string.charAt(n3);
                if (!XML11Char.isXML11Valid(c2)) {
                    if (++n3 < n2) {
                        this.surrogates(c2, string.charAt(n3), true);
                    } else {
                        this.fatalError("The character '" + c2 + "' is an invalid XML character");
                    }
                } else if (bl2 && XML11Char.isXML11ValidLiteral(c2)) {
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
                if (!XML11Char.isXML11Valid(c3)) {
                    if (++n4 < n2) {
                        this.surrogates(c3, string.charAt(n4), true);
                    } else {
                        this.fatalError("The character '" + c3 + "' is an invalid XML character");
                    }
                } else if (bl2 && XML11Char.isXML11ValidLiteral(c3)) {
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
            if (!XML11Char.isXML11Valid(var6_6 = var1_1[var2_2++])) {
                if (var3_3-- > 0) {
                    this.surrogates(var6_6, var1_1[var2_2++], true);
                    continue;
                }
                this.fatalError("The character '" + var6_6 + "' is an invalid XML character");
                continue;
            }
            if (var5_5 && XML11Char.isXML11ValidLiteral(var6_6)) {
                this._printer.printText(var6_6);
                continue;
            }
            this.printXMLChar(var6_6);
        }
        return;
lbl-1000: // 1 sources:
        {
            if (!XML11Char.isXML11Valid(var6_7 = var1_1[var2_2++])) {
                if (var3_3-- > 0) {
                    this.surrogates(var6_7, var1_1[var2_2++], true);
                    continue;
                }
                this.fatalError("The character '" + var6_7 + "' is an invalid XML character");
                continue;
            }
            if (var5_5 && XML11Char.isXML11ValidLiteral(var6_7)) {
                this._printer.printText(var6_7);
                continue;
            }
            this.printXMLChar(var6_7);
lbl25: // 5 sources:
            ** while (var3_3-- > 0)
        }
lbl26: // 1 sources:
    }

    public boolean reset() {
        super.reset();
        return true;
    }
}

