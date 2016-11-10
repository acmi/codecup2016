/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl;

import java.io.IOException;
import org.apache.xerces.impl.XMLDTDScannerImpl;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLEntityScanner;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;

public class XML11DTDScannerImpl
extends XMLDTDScannerImpl {
    private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();

    public XML11DTDScannerImpl() {
    }

    public XML11DTDScannerImpl(SymbolTable symbolTable, XMLErrorReporter xMLErrorReporter, XMLEntityManager xMLEntityManager) {
        super(symbolTable, xMLErrorReporter, xMLEntityManager);
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
        return !XML11Char.isXML11Valid(n2);
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

