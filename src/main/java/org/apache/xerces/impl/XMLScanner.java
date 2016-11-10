/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl;

import java.io.IOException;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLEntityScanner;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLResourceIdentifierImpl;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;

public abstract class XMLScanner
implements XMLComponent {
    protected static final String VALIDATION = "http://xml.org/sax/features/validation";
    protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    protected static final String NOTIFY_CHAR_REFS = "http://apache.org/xml/features/scanner/notify-char-refs";
    protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
    protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
    protected static final boolean DEBUG_ATTR_NORMALIZATION = false;
    protected boolean fValidation = false;
    protected boolean fNamespaces;
    protected boolean fNotifyCharRefs = false;
    protected boolean fParserSettings = true;
    protected SymbolTable fSymbolTable;
    protected XMLErrorReporter fErrorReporter;
    protected XMLEntityManager fEntityManager;
    protected XMLEntityScanner fEntityScanner;
    protected int fEntityDepth;
    protected String fCharRefLiteral = null;
    protected boolean fScanningAttribute;
    protected boolean fReportEntity;
    protected static final String fVersionSymbol = "version".intern();
    protected static final String fEncodingSymbol = "encoding".intern();
    protected static final String fStandaloneSymbol = "standalone".intern();
    protected static final String fAmpSymbol = "amp".intern();
    protected static final String fLtSymbol = "lt".intern();
    protected static final String fGtSymbol = "gt".intern();
    protected static final String fQuotSymbol = "quot".intern();
    protected static final String fAposSymbol = "apos".intern();
    private final XMLString fString = new XMLString();
    private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();
    private final XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
    private final XMLStringBuffer fStringBuffer3 = new XMLStringBuffer();
    protected final XMLResourceIdentifierImpl fResourceIdentifier = new XMLResourceIdentifierImpl();

    public void reset(XMLComponentManager xMLComponentManager) throws XMLConfigurationException {
        try {
            this.fParserSettings = xMLComponentManager.getFeature("http://apache.org/xml/features/internal/parser-settings");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fParserSettings = true;
        }
        if (!this.fParserSettings) {
            this.init();
            return;
        }
        this.fSymbolTable = (SymbolTable)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fErrorReporter = (XMLErrorReporter)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        this.fEntityManager = (XMLEntityManager)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-manager");
        try {
            this.fValidation = xMLComponentManager.getFeature("http://xml.org/sax/features/validation");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fValidation = false;
        }
        try {
            this.fNamespaces = xMLComponentManager.getFeature("http://xml.org/sax/features/namespaces");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fNamespaces = true;
        }
        try {
            this.fNotifyCharRefs = xMLComponentManager.getFeature("http://apache.org/xml/features/scanner/notify-char-refs");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fNotifyCharRefs = false;
        }
        this.init();
    }

    public void setProperty(String string, Object object) throws XMLConfigurationException {
        if (string.startsWith("http://apache.org/xml/properties/")) {
            int n2 = string.length() - "http://apache.org/xml/properties/".length();
            if (n2 == "internal/symbol-table".length() && string.endsWith("internal/symbol-table")) {
                this.fSymbolTable = (SymbolTable)object;
            } else if (n2 == "internal/error-reporter".length() && string.endsWith("internal/error-reporter")) {
                this.fErrorReporter = (XMLErrorReporter)object;
            } else if (n2 == "internal/entity-manager".length() && string.endsWith("internal/entity-manager")) {
                this.fEntityManager = (XMLEntityManager)object;
            }
        }
    }

    public void setFeature(String string, boolean bl) throws XMLConfigurationException {
        if ("http://xml.org/sax/features/validation".equals(string)) {
            this.fValidation = bl;
        } else if ("http://apache.org/xml/features/scanner/notify-char-refs".equals(string)) {
            this.fNotifyCharRefs = bl;
        }
    }

    public boolean getFeature(String string) throws XMLConfigurationException {
        if ("http://xml.org/sax/features/validation".equals(string)) {
            return this.fValidation;
        }
        if ("http://apache.org/xml/features/scanner/notify-char-refs".equals(string)) {
            return this.fNotifyCharRefs;
        }
        throw new XMLConfigurationException(0, string);
    }

    protected void reset() {
        this.init();
        this.fValidation = true;
        this.fNotifyCharRefs = false;
    }

    protected void scanXMLDeclOrTextDecl(boolean bl, String[] arrstring) throws IOException, XNIException {
        String string = null;
        String string2 = null;
        String string3 = null;
        int n2 = 0;
        boolean bl2 = false;
        boolean bl3 = this.fEntityScanner.skipDeclSpaces();
        XMLEntityManager.ScannedEntity scannedEntity = this.fEntityManager.getCurrentEntity();
        boolean bl4 = scannedEntity.literal;
        scannedEntity.literal = false;
        while (this.fEntityScanner.peekChar() != 63) {
            bl2 = true;
            String string4 = this.scanPseudoAttribute(bl, this.fString);
            switch (n2) {
                case 0: {
                    if (string4 == fVersionSymbol) {
                        if (!bl3) {
                            this.reportFatalError(bl ? "SpaceRequiredBeforeVersionInTextDecl" : "SpaceRequiredBeforeVersionInXMLDecl", null);
                        }
                        string = this.fString.toString();
                        n2 = 1;
                        if (this.versionSupported(string)) break;
                        this.reportFatalError(this.getVersionNotSupportedKey(), new Object[]{string});
                        break;
                    }
                    if (string4 == fEncodingSymbol) {
                        if (!bl) {
                            this.reportFatalError("VersionInfoRequired", null);
                        }
                        if (!bl3) {
                            this.reportFatalError(bl ? "SpaceRequiredBeforeEncodingInTextDecl" : "SpaceRequiredBeforeEncodingInXMLDecl", null);
                        }
                        string2 = this.fString.toString();
                        n2 = bl ? 3 : 2;
                        break;
                    }
                    if (bl) {
                        this.reportFatalError("EncodingDeclRequired", null);
                        break;
                    }
                    this.reportFatalError("VersionInfoRequired", null);
                    break;
                }
                case 1: {
                    if (string4 == fEncodingSymbol) {
                        if (!bl3) {
                            this.reportFatalError(bl ? "SpaceRequiredBeforeEncodingInTextDecl" : "SpaceRequiredBeforeEncodingInXMLDecl", null);
                        }
                        string2 = this.fString.toString();
                        n2 = bl ? 3 : 2;
                        break;
                    }
                    if (!bl && string4 == fStandaloneSymbol) {
                        if (!bl3) {
                            this.reportFatalError("SpaceRequiredBeforeStandalone", null);
                        }
                        string3 = this.fString.toString();
                        n2 = 3;
                        if (string3.equals("yes") || string3.equals("no")) break;
                        this.reportFatalError("SDDeclInvalid", new Object[]{string3});
                        break;
                    }
                    this.reportFatalError("EncodingDeclRequired", null);
                    break;
                }
                case 2: {
                    if (string4 == fStandaloneSymbol) {
                        if (!bl3) {
                            this.reportFatalError("SpaceRequiredBeforeStandalone", null);
                        }
                        string3 = this.fString.toString();
                        n2 = 3;
                        if (string3.equals("yes") || string3.equals("no")) break;
                        this.reportFatalError("SDDeclInvalid", new Object[]{string3});
                        break;
                    }
                    this.reportFatalError("EncodingDeclRequired", null);
                    break;
                }
                default: {
                    this.reportFatalError("NoMorePseudoAttributes", null);
                }
            }
            bl3 = this.fEntityScanner.skipDeclSpaces();
        }
        if (bl4) {
            scannedEntity.literal = true;
        }
        if (bl && n2 != 3) {
            this.reportFatalError("MorePseudoAttributes", null);
        }
        if (bl) {
            if (!bl2 && string2 == null) {
                this.reportFatalError("EncodingDeclRequired", null);
            }
        } else if (!bl2 && string == null) {
            this.reportFatalError("VersionInfoRequired", null);
        }
        if (!this.fEntityScanner.skipChar(63)) {
            this.reportFatalError("XMLDeclUnterminated", null);
        }
        if (!this.fEntityScanner.skipChar(62)) {
            this.reportFatalError("XMLDeclUnterminated", null);
        }
        arrstring[0] = string;
        arrstring[1] = string2;
        arrstring[2] = string3;
    }

    public String scanPseudoAttribute(boolean bl, XMLString xMLString) throws IOException, XNIException {
        String string = this.fEntityScanner.scanName();
        XMLEntityManager.print(this.fEntityManager.getCurrentEntity());
        if (string == null) {
            this.reportFatalError("PseudoAttrNameExpected", null);
        }
        this.fEntityScanner.skipDeclSpaces();
        if (!this.fEntityScanner.skipChar(61)) {
            this.reportFatalError(bl ? "EqRequiredInTextDecl" : "EqRequiredInXMLDecl", new Object[]{string});
        }
        this.fEntityScanner.skipDeclSpaces();
        int n2 = this.fEntityScanner.peekChar();
        if (n2 != 39 && n2 != 34) {
            this.reportFatalError(bl ? "QuoteRequiredInTextDecl" : "QuoteRequiredInXMLDecl", new Object[]{string});
        }
        this.fEntityScanner.scanChar();
        int n3 = this.fEntityScanner.scanLiteral(n2, xMLString);
        if (n3 != n2) {
            this.fStringBuffer2.clear();
            do {
                this.fStringBuffer2.append(xMLString);
                if (n3 == -1) continue;
                if (n3 == 38 || n3 == 37 || n3 == 60 || n3 == 93) {
                    this.fStringBuffer2.append((char)this.fEntityScanner.scanChar());
                    continue;
                }
                if (XMLChar.isHighSurrogate(n3)) {
                    this.scanSurrogates(this.fStringBuffer2);
                    continue;
                }
                if (!this.isInvalidLiteral(n3)) continue;
                String string2 = bl ? "InvalidCharInTextDecl" : "InvalidCharInXMLDecl";
                this.reportFatalError(string2, new Object[]{Integer.toString(n3, 16)});
                this.fEntityScanner.scanChar();
            } while ((n3 = this.fEntityScanner.scanLiteral(n2, xMLString)) != n2);
            this.fStringBuffer2.append(xMLString);
            xMLString.setValues(this.fStringBuffer2);
        }
        if (!this.fEntityScanner.skipChar(n2)) {
            this.reportFatalError(bl ? "CloseQuoteMissingInTextDecl" : "CloseQuoteMissingInXMLDecl", new Object[]{string});
        }
        return string;
    }

    protected void scanPI() throws IOException, XNIException {
        this.fReportEntity = false;
        String string = null;
        string = this.fNamespaces ? this.fEntityScanner.scanNCName() : this.fEntityScanner.scanName();
        if (string == null) {
            this.reportFatalError("PITargetRequired", null);
        }
        this.scanPIData(string, this.fString);
        this.fReportEntity = true;
    }

    protected void scanPIData(String string, XMLString xMLString) throws IOException, XNIException {
        int n2;
        if (string.length() == 3) {
            n2 = Character.toLowerCase(string.charAt(0));
            char c2 = Character.toLowerCase(string.charAt(1));
            char c3 = Character.toLowerCase(string.charAt(2));
            if (n2 == 120 && c2 == 'm' && c3 == 'l') {
                this.reportFatalError("ReservedPITarget", null);
            }
        }
        if (!this.fEntityScanner.skipSpaces()) {
            if (this.fEntityScanner.skipString("?>")) {
                xMLString.clear();
                return;
            }
            if (this.fNamespaces && this.fEntityScanner.peekChar() == 58) {
                this.fEntityScanner.scanChar();
                XMLStringBuffer xMLStringBuffer = new XMLStringBuffer(string);
                xMLStringBuffer.append(":");
                String string2 = this.fEntityScanner.scanName();
                if (string2 != null) {
                    xMLStringBuffer.append(string2);
                }
                this.reportFatalError("ColonNotLegalWithNS", new Object[]{xMLStringBuffer.toString()});
                this.fEntityScanner.skipSpaces();
            } else {
                this.reportFatalError("SpaceRequiredInPI", null);
            }
        }
        this.fStringBuffer.clear();
        if (this.fEntityScanner.scanData("?>", this.fStringBuffer)) {
            do {
                if ((n2 = this.fEntityScanner.peekChar()) == -1) continue;
                if (XMLChar.isHighSurrogate(n2)) {
                    this.scanSurrogates(this.fStringBuffer);
                    continue;
                }
                if (!this.isInvalidLiteral(n2)) continue;
                this.reportFatalError("InvalidCharInPI", new Object[]{Integer.toHexString(n2)});
                this.fEntityScanner.scanChar();
            } while (this.fEntityScanner.scanData("?>", this.fStringBuffer));
        }
        xMLString.setValues(this.fStringBuffer);
    }

    protected void scanComment(XMLStringBuffer xMLStringBuffer) throws IOException, XNIException {
        xMLStringBuffer.clear();
        while (this.fEntityScanner.scanData("--", xMLStringBuffer)) {
            int n2 = this.fEntityScanner.peekChar();
            if (n2 == -1) continue;
            if (XMLChar.isHighSurrogate(n2)) {
                this.scanSurrogates(xMLStringBuffer);
                continue;
            }
            if (!this.isInvalidLiteral(n2)) continue;
            this.reportFatalError("InvalidCharInComment", new Object[]{Integer.toHexString(n2)});
            this.fEntityScanner.scanChar();
        }
        if (!this.fEntityScanner.skipChar(62)) {
            this.reportFatalError("DashDashInComment", null);
        }
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
                        if (string3 == fAmpSymbol) {
                            this.fStringBuffer.append('&');
                        } else if (string3 == fAposSymbol) {
                            this.fStringBuffer.append('\'');
                        } else if (string3 == fLtSymbol) {
                            this.fStringBuffer.append('<');
                        } else if (string3 == fGtSymbol) {
                            this.fStringBuffer.append('>');
                        } else if (string3 == fQuotSymbol) {
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
                } else if (n5 == 10 || n5 == 13) {
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

    protected void scanExternalID(String[] arrstring, boolean bl) throws IOException, XNIException {
        String string = null;
        String string2 = null;
        if (this.fEntityScanner.skipString("PUBLIC")) {
            if (!this.fEntityScanner.skipSpaces()) {
                this.reportFatalError("SpaceRequiredAfterPUBLIC", null);
            }
            this.scanPubidLiteral(this.fString);
            string2 = this.fString.toString();
            if (!this.fEntityScanner.skipSpaces() && !bl) {
                this.reportFatalError("SpaceRequiredBetweenPublicAndSystem", null);
            }
        }
        if (string2 != null || this.fEntityScanner.skipString("SYSTEM")) {
            int n2;
            if (string2 == null && !this.fEntityScanner.skipSpaces()) {
                this.reportFatalError("SpaceRequiredAfterSYSTEM", null);
            }
            if ((n2 = this.fEntityScanner.peekChar()) != 39 && n2 != 34) {
                if (string2 != null && bl) {
                    arrstring[0] = null;
                    arrstring[1] = string2;
                    return;
                }
                this.reportFatalError("QuoteRequiredInSystemID", null);
            }
            this.fEntityScanner.scanChar();
            XMLString xMLString = this.fString;
            if (this.fEntityScanner.scanLiteral(n2, xMLString) != n2) {
                this.fStringBuffer.clear();
                do {
                    this.fStringBuffer.append(xMLString);
                    int n3 = this.fEntityScanner.peekChar();
                    if (XMLChar.isMarkup(n3) || n3 == 93) {
                        this.fStringBuffer.append((char)this.fEntityScanner.scanChar());
                        continue;
                    }
                    if (XMLChar.isHighSurrogate(n3)) {
                        this.scanSurrogates(this.fStringBuffer);
                        continue;
                    }
                    if (!this.isInvalidLiteral(n3)) continue;
                    this.reportFatalError("InvalidCharInSystemID", new Object[]{Integer.toHexString(n3)});
                    this.fEntityScanner.scanChar();
                } while (this.fEntityScanner.scanLiteral(n2, xMLString) != n2);
                this.fStringBuffer.append(xMLString);
                xMLString = this.fStringBuffer;
            }
            string = xMLString.toString();
            if (!this.fEntityScanner.skipChar(n2)) {
                this.reportFatalError("SystemIDUnterminated", null);
            }
        }
        arrstring[0] = string;
        arrstring[1] = string2;
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
            if ((n3 = this.fEntityScanner.scanChar()) == 32 || n3 == 10 || n3 == 13) {
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
            if (c2 < ' ') {
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
            if (c2 < ' ') {
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
            if (c2 < ' ') {
                return n3 - xMLString.offset;
            }
            ++n3;
        }
        return -1;
    }

    public void startEntity(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
        ++this.fEntityDepth;
        this.fEntityScanner = this.fEntityManager.getEntityScanner();
    }

    public void endEntity(String string, Augmentations augmentations) throws XNIException {
        --this.fEntityDepth;
    }

    protected int scanCharReferenceValue(XMLStringBuffer xMLStringBuffer, XMLStringBuffer xMLStringBuffer2) throws IOException, XNIException {
        int n2;
        Object object;
        boolean bl = false;
        if (this.fEntityScanner.skipChar(120)) {
            if (xMLStringBuffer2 != null) {
                xMLStringBuffer2.append('x');
            }
            bl = true;
            this.fStringBuffer3.clear();
            n2 = 1;
            object = this.fEntityScanner.peekChar();
            int n3 = n2 = object >= 48 && object <= 57 || object >= 97 && object <= 102 || object >= 65 && object <= 70 ? 1 : 0;
            if (n2 != 0) {
                if (xMLStringBuffer2 != null) {
                    xMLStringBuffer2.append((char)object);
                }
                this.fEntityScanner.scanChar();
                this.fStringBuffer3.append((char)object);
                do {
                    int n4 = n2 = (object = this.fEntityScanner.peekChar()) >= 48 && object <= 57 || object >= 97 && object <= 102 || object >= 65 && object <= 70 ? 1 : 0;
                    if (n2 == 0) continue;
                    if (xMLStringBuffer2 != null) {
                        xMLStringBuffer2.append((char)object);
                    }
                    this.fEntityScanner.scanChar();
                    this.fStringBuffer3.append((char)object);
                } while (n2 != 0);
            } else {
                this.reportFatalError("HexdigitRequiredInCharRef", null);
            }
        } else {
            this.fStringBuffer3.clear();
            n2 = 1;
            object = this.fEntityScanner.peekChar();
            int n5 = n2 = object >= 48 && object <= 57 ? 1 : 0;
            if (n2 != 0) {
                if (xMLStringBuffer2 != null) {
                    xMLStringBuffer2.append((char)object);
                }
                this.fEntityScanner.scanChar();
                this.fStringBuffer3.append((char)object);
                do {
                    int n6 = n2 = (object = this.fEntityScanner.peekChar()) >= 48 && object <= 57 ? 1 : 0;
                    if (n2 == 0) continue;
                    if (xMLStringBuffer2 != null) {
                        xMLStringBuffer2.append((char)object);
                    }
                    this.fEntityScanner.scanChar();
                    this.fStringBuffer3.append((char)object);
                } while (n2 != 0);
            } else {
                this.reportFatalError("DigitRequiredInCharRef", null);
            }
        }
        if (!this.fEntityScanner.skipChar(59)) {
            this.reportFatalError("SemicolonRequiredInCharRef", null);
        }
        if (xMLStringBuffer2 != null) {
            xMLStringBuffer2.append(';');
        }
        n2 = -1;
        try {
            n2 = Integer.parseInt(this.fStringBuffer3.toString(), bl ? 16 : 10);
            if (this.isInvalid(n2)) {
                StringBuffer stringBuffer = new StringBuffer(this.fStringBuffer3.length + 1);
                if (bl) {
                    stringBuffer.append('x');
                }
                stringBuffer.append(this.fStringBuffer3.ch, this.fStringBuffer3.offset, this.fStringBuffer3.length);
                this.reportFatalError("InvalidCharRef", new Object[]{stringBuffer.toString()});
            }
        }
        catch (NumberFormatException numberFormatException) {
            StringBuffer stringBuffer = new StringBuffer(this.fStringBuffer3.length + 1);
            if (bl) {
                stringBuffer.append('x');
            }
            stringBuffer.append(this.fStringBuffer3.ch, this.fStringBuffer3.offset, this.fStringBuffer3.length);
            this.reportFatalError("InvalidCharRef", new Object[]{stringBuffer.toString()});
        }
        if (!XMLChar.isSupplemental(n2)) {
            xMLStringBuffer.append((char)n2);
        } else {
            xMLStringBuffer.append(XMLChar.highSurrogate(n2));
            xMLStringBuffer.append(XMLChar.lowSurrogate(n2));
        }
        if (this.fNotifyCharRefs && n2 != -1) {
            object = "#" + (bl ? "x" : "") + this.fStringBuffer3.toString();
            if (!this.fScanningAttribute) {
                this.fCharRefLiteral = object;
            }
        }
        return n2;
    }

    protected boolean isInvalid(int n2) {
        return XMLChar.isInvalid(n2);
    }

    protected boolean isInvalidLiteral(int n2) {
        return XMLChar.isInvalid(n2);
    }

    protected boolean isValidNameChar(int n2) {
        return XMLChar.isName(n2);
    }

    protected boolean isValidNameStartChar(int n2) {
        return XMLChar.isNameStart(n2);
    }

    protected boolean isValidNCName(int n2) {
        return XMLChar.isNCName(n2);
    }

    protected boolean isValidNameStartHighSurrogate(int n2) {
        return false;
    }

    protected boolean versionSupported(String string) {
        return string.equals("1.0");
    }

    protected String getVersionNotSupportedKey() {
        return "VersionNotSupported";
    }

    protected boolean scanSurrogates(XMLStringBuffer xMLStringBuffer) throws IOException, XNIException {
        int n2 = this.fEntityScanner.scanChar();
        int n3 = this.fEntityScanner.peekChar();
        if (!XMLChar.isLowSurrogate(n3)) {
            this.reportFatalError("InvalidCharInContent", new Object[]{Integer.toString(n2, 16)});
            return false;
        }
        this.fEntityScanner.scanChar();
        int n4 = XMLChar.supplemental((char)n2, (char)n3);
        if (this.isInvalid(n4)) {
            this.reportFatalError("InvalidCharInContent", new Object[]{Integer.toString(n4, 16)});
            return false;
        }
        xMLStringBuffer.append((char)n2);
        xMLStringBuffer.append((char)n3);
        return true;
    }

    protected void reportFatalError(String string, Object[] arrobject) throws XNIException {
        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", string, arrobject, 2);
    }

    private void init() {
        this.fEntityScanner = null;
        this.fEntityDepth = 0;
        this.fReportEntity = true;
        this.fResourceIdentifier.clear();
    }

    public abstract Object getPropertyDefault(String var1);

    public abstract Boolean getFeatureDefault(String var1);

    public abstract String[] getRecognizedProperties();

    public abstract String[] getRecognizedFeatures();
}

