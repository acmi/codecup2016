/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl;

import java.io.IOException;
import org.apache.xerces.impl.XMLDocumentScannerImpl;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLEntityScanner;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.XMLScanner;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;

public class XML11DocumentScannerImpl
extends XMLDocumentScannerImpl {
    private final XMLString fString = new XMLString();
    private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();
    private final XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
    private final XMLStringBuffer fStringBuffer3 = new XMLStringBuffer();

    protected int scanContent() throws IOException, XNIException {
        XMLString xMLString = this.fString;
        int n2 = this.fEntityScanner.scanContent(xMLString);
        if (n2 == 13 || n2 == 133 || n2 == 8232) {
            this.fEntityScanner.scanChar();
            this.fStringBuffer.clear();
            this.fStringBuffer.append(this.fString);
            this.fStringBuffer.append((char)n2);
            xMLString = this.fStringBuffer;
            n2 = -1;
        }
        if (this.fDocumentHandler != null && xMLString.length > 0) {
            this.fDocumentHandler.characters(xMLString, null);
        }
        if (n2 == 93 && this.fString.length == 0) {
            this.fStringBuffer.clear();
            this.fStringBuffer.append((char)this.fEntityScanner.scanChar());
            this.fInScanContent = true;
            if (this.fEntityScanner.skipChar(93)) {
                this.fStringBuffer.append(']');
                while (this.fEntityScanner.skipChar(93)) {
                    this.fStringBuffer.append(']');
                }
                if (this.fEntityScanner.skipChar(62)) {
                    this.reportFatalError("CDEndInContent", null);
                }
            }
            if (this.fDocumentHandler != null && this.fStringBuffer.length != 0) {
                this.fDocumentHandler.characters(this.fStringBuffer, null);
            }
            this.fInScanContent = false;
            n2 = -1;
        }
        return n2;
    }

    protected boolean scanAttributeValue(XMLString xMLString, XMLString xMLString2, String string, boolean bl, String string2) throws IOException, XNIException {
        int n2;
        int n3 = this.fEntityScanner.peekChar();
        if (n3 != 39 && n3 != 34) {
            this.reportFatalError("OpenQuoteExpected", new Object[]{string2, string});
        }
        this.fEntityScanner.scanChar();
        int n4 = this.fEntityDepth;
        int n5 = this.fEntityScanner.scanLiteral(n3, xMLString);
        int n6 = 0;
        if (n5 == n3 && (n6 = this.isUnchangedByNormalization(xMLString)) == -1) {
            xMLString2.setValues(xMLString);
            n2 = this.fEntityScanner.scanChar();
            if (n2 != n3) {
                this.reportFatalError("CloseQuoteExpected", new Object[]{string2, string});
            }
            return true;
        }
        this.fStringBuffer2.clear();
        this.fStringBuffer2.append(xMLString);
        this.normalizeWhitespace(xMLString, n6);
        if (n5 != n3) {
            this.fScanningAttribute = true;
            this.fStringBuffer.clear();
            do {
                this.fStringBuffer.append(xMLString);
                if (n5 == 38) {
                    this.fEntityScanner.skipChar(38);
                    if (n4 == this.fEntityDepth) {
                        this.fStringBuffer2.append('&');
                    }
                    if (this.fEntityScanner.skipChar(35)) {
                        if (n4 == this.fEntityDepth) {
                            this.fStringBuffer2.append('#');
                        }
                        if ((n2 = this.scanCharReferenceValue(this.fStringBuffer, this.fStringBuffer2)) != -1) {
                            // empty if block
                        }
                    } else {
                        String string3 = this.fEntityScanner.scanName();
                        if (string3 == null) {
                            this.reportFatalError("NameRequiredInReference", null);
                        } else if (n4 == this.fEntityDepth) {
                            this.fStringBuffer2.append(string3);
                        }
                        if (!this.fEntityScanner.skipChar(59)) {
                            this.reportFatalError("SemicolonRequiredInReference", new Object[]{string3});
                        } else if (n4 == this.fEntityDepth) {
                            this.fStringBuffer2.append(';');
                        }
                        if (string3 == XMLScanner.fAmpSymbol) {
                            this.fStringBuffer.append('&');
                        } else if (string3 == XMLScanner.fAposSymbol) {
                            this.fStringBuffer.append('\'');
                        } else if (string3 == XMLScanner.fLtSymbol) {
                            this.fStringBuffer.append('<');
                        } else if (string3 == XMLScanner.fGtSymbol) {
                            this.fStringBuffer.append('>');
                        } else if (string3 == XMLScanner.fQuotSymbol) {
                            this.fStringBuffer.append('\"');
                        } else if (this.fEntityManager.isExternalEntity(string3)) {
                            this.reportFatalError("ReferenceToExternalEntity", new Object[]{string3});
                        } else {
                            if (!this.fEntityManager.isDeclaredEntity(string3)) {
                                if (bl) {
                                    if (this.fValidation) {
                                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EntityNotDeclared", new Object[]{string3}, 1);
                                    }
                                } else {
                                    this.reportFatalError("EntityNotDeclared", new Object[]{string3});
                                }
                            }
                            this.fEntityManager.startEntity(string3, true);
                        }
                    }
                } else if (n5 == 60) {
                    this.reportFatalError("LessthanInAttValue", new Object[]{string2, string});
                    this.fEntityScanner.scanChar();
                    if (n4 == this.fEntityDepth) {
                        this.fStringBuffer2.append((char)n5);
                    }
                } else if (n5 == 37 || n5 == 93) {
                    this.fEntityScanner.scanChar();
                    this.fStringBuffer.append((char)n5);
                    if (n4 == this.fEntityDepth) {
                        this.fStringBuffer2.append((char)n5);
                    }
                } else if (n5 == 10 || n5 == 13 || n5 == 133 || n5 == 8232) {
                    this.fEntityScanner.scanChar();
                    this.fStringBuffer.append(' ');
                    if (n4 == this.fEntityDepth) {
                        this.fStringBuffer2.append('\n');
                    }
                } else if (n5 != -1 && XMLChar.isHighSurrogate(n5)) {
                    this.fStringBuffer3.clear();
                    if (this.scanSurrogates(this.fStringBuffer3)) {
                        this.fStringBuffer.append(this.fStringBuffer3);
                        if (n4 == this.fEntityDepth) {
                            this.fStringBuffer2.append(this.fStringBuffer3);
                        }
                    }
                } else if (n5 != -1 && this.isInvalidLiteral(n5)) {
                    this.reportFatalError("InvalidCharInAttValue", new Object[]{string2, string, Integer.toString(n5, 16)});
                    this.fEntityScanner.scanChar();
                    if (n4 == this.fEntityDepth) {
                        this.fStringBuffer2.append((char)n5);
                    }
                }
                n5 = this.fEntityScanner.scanLiteral(n3, xMLString);
                if (n4 == this.fEntityDepth) {
                    this.fStringBuffer2.append(xMLString);
                }
                this.normalizeWhitespace(xMLString);
            } while (n5 != n3 || n4 != this.fEntityDepth);
            this.fStringBuffer.append(xMLString);
            xMLString.setValues(this.fStringBuffer);
            this.fScanningAttribute = false;
        }
        xMLString2.setValues(this.fStringBuffer2);
        n2 = this.fEntityScanner.scanChar();
        if (n2 != n3) {
            this.reportFatalError("CloseQuoteExpected", new Object[]{string2, string});
        }
        return xMLString2.equals(xMLString.ch, xMLString.offset, xMLString.length);
    }

    protected boolean scanPubidLiteral(XMLString xMLString) throws IOException, XNIException {
        int n2 = this.fEntityScanner.scanChar();
        if (n2 != 39 && n2 != 34) {
            this.reportFatalError("QuoteRequiredInPublicID", null);
            return false;
        }
        this.fStringBuffer.clear();
        boolean bl = true;
        boolean bl2 = true;
        do {
            int n3;
            if ((n3 = this.fEntityScanner.scanChar()) == 32 || n3 == 10 || n3 == 13 || n3 == 133 || n3 == 8232) {
                if (bl) continue;
                this.fStringBuffer.append(' ');
                bl = true;
                continue;
            }
            if (n3 == n2) {
                if (bl) {
                    --this.fStringBuffer.length;
                    break;
                }
                break;
            }
            if (XMLChar.isPubid(n3)) {
                this.fStringBuffer.append((char)n3);
                bl = false;
                continue;
            }
            if (n3 == -1) {
                this.reportFatalError("PublicIDUnterminated", null);
                return false;
            }
            bl2 = false;
            this.reportFatalError("InvalidCharInPublicID", new Object[]{Integer.toHexString(n3)});
        } while (true);
        xMLString.setValues(this.fStringBuffer);
        return bl2;
    }

    protected void normalizeWhitespace(XMLString xMLString) {
        int n2 = xMLString.offset + xMLString.length;
        int n3 = xMLString.offset;
        while (n3 < n2) {
            char c2 = xMLString.ch[n3];
            if (XMLChar.isSpace(c2)) {
                xMLString.ch[n3] = 32;
            }
            ++n3;
        }
    }

    protected void normalizeWhitespace(XMLString xMLString, int n2) {
        int n3 = xMLString.offset + xMLString.length;
        int n4 = xMLString.offset + n2;
        while (n4 < n3) {
            char c2 = xMLString.ch[n4];
            if (XMLChar.isSpace(c2)) {
                xMLString.ch[n4] = 32;
            }
            ++n4;
        }
    }

    protected int isUnchangedByNormalization(XMLString xMLString) {
        int n2 = xMLString.offset + xMLString.length;
        int n3 = xMLString.offset;
        while (n3 < n2) {
            char c2 = xMLString.ch[n3];
            if (XMLChar.isSpace(c2)) {
                return n3 - xMLString.offset;
            }
            ++n3;
        }
        return -1;
    }

    protected boolean isInvalid(int n2) {
        return XML11Char.isXML11Invalid(n2);
    }

    protected boolean isInvalidLiteral(int n2) {
        return !XML11Char.isXML11ValidLiteral(n2);
    }

    protected boolean isValidNameChar(int n2) {
        return XML11Char.isXML11Name(n2);
    }

    protected boolean isValidNameStartChar(int n2) {
        return XML11Char.isXML11NameStart(n2);
    }

    protected boolean isValidNCName(int n2) {
        return XML11Char.isXML11NCName(n2);
    }

    protected boolean isValidNameStartHighSurrogate(int n2) {
        return XML11Char.isXML11NameHighSurrogate(n2);
    }

    protected boolean versionSupported(String string) {
        return string.equals("1.1") || string.equals("1.0");
    }

    protected String getVersionNotSupportedKey() {
        return "VersionNotSupported11";
    }
}

