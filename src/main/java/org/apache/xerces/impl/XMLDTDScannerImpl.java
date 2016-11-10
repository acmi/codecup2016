/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl;

import java.io.IOException;
import org.apache.xerces.impl.XMLEntityHandler;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLEntityScanner;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.XMLScanner;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLResourceIdentifierImpl;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDTDScanner;
import org.apache.xerces.xni.parser.XMLInputSource;

public class XMLDTDScannerImpl
extends XMLScanner
implements XMLEntityHandler,
XMLComponent,
XMLDTDScanner {
    protected static final int SCANNER_STATE_END_OF_INPUT = 0;
    protected static final int SCANNER_STATE_TEXT_DECL = 1;
    protected static final int SCANNER_STATE_MARKUP_DECL = 2;
    private static final String[] RECOGNIZED_FEATURES = new String[]{"http://xml.org/sax/features/validation", "http://apache.org/xml/features/scanner/notify-char-refs"};
    private static final Boolean[] FEATURE_DEFAULTS = new Boolean[]{null, Boolean.FALSE};
    private static final String[] RECOGNIZED_PROPERTIES = new String[]{"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-manager"};
    private static final Object[] PROPERTY_DEFAULTS = new Object[]{null, null, null};
    private static final boolean DEBUG_SCANNER_STATE = false;
    protected XMLDTDHandler fDTDHandler;
    protected XMLDTDContentModelHandler fDTDContentModelHandler;
    protected int fScannerState;
    protected boolean fStandalone;
    protected boolean fSeenExternalDTD;
    protected boolean fSeenPEReferences;
    private boolean fStartDTDCalled;
    private int[] fContentStack = new int[5];
    private int fContentDepth;
    private int[] fPEStack = new int[5];
    private boolean[] fPEReport = new boolean[5];
    private int fPEDepth;
    private int fMarkUpDepth;
    private int fExtEntityDepth;
    private int fIncludeSectDepth;
    private final String[] fStrings = new String[3];
    private final XMLString fString = new XMLString();
    private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();
    private final XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
    private final XMLString fLiteral = new XMLString();
    private final XMLString fLiteral2 = new XMLString();
    private String[] fEnumeration = new String[5];
    private int fEnumerationCount;
    private final XMLStringBuffer fIgnoreConditionalBuffer = new XMLStringBuffer(128);

    public XMLDTDScannerImpl() {
    }

    public XMLDTDScannerImpl(SymbolTable symbolTable, XMLErrorReporter xMLErrorReporter, XMLEntityManager xMLEntityManager) {
        this.fSymbolTable = symbolTable;
        this.fErrorReporter = xMLErrorReporter;
        this.fEntityManager = xMLEntityManager;
        xMLEntityManager.setProperty("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
    }

    public void setInputSource(XMLInputSource xMLInputSource) throws IOException {
        if (xMLInputSource == null) {
            if (this.fDTDHandler != null) {
                this.fDTDHandler.startDTD(null, null);
                this.fDTDHandler.endDTD(null);
            }
            return;
        }
        this.fEntityManager.setEntityHandler(this);
        this.fEntityManager.startDTDEntity(xMLInputSource);
    }

    public boolean scanDTDExternalSubset(boolean bl) throws IOException, XNIException {
        this.fEntityManager.setEntityHandler(this);
        if (this.fScannerState == 1) {
            this.fSeenExternalDTD = true;
            boolean bl2 = this.scanTextDecl();
            if (this.fScannerState == 0) {
                return false;
            }
            this.setScannerState(2);
            if (bl2 && !bl) {
                return true;
            }
        }
        do {
            if (this.scanDecls(bl)) continue;
            return false;
        } while (bl);
        return true;
    }

    public boolean scanDTDInternalSubset(boolean bl, boolean bl2, boolean bl3) throws IOException, XNIException {
        this.fEntityScanner = this.fEntityManager.getEntityScanner();
        this.fEntityManager.setEntityHandler(this);
        this.fStandalone = bl2;
        if (this.fScannerState == 1) {
            if (this.fDTDHandler != null) {
                this.fDTDHandler.startDTD(this.fEntityScanner, null);
                this.fStartDTDCalled = true;
            }
            this.setScannerState(2);
        }
        do {
            if (this.scanDecls(bl)) continue;
            if (this.fDTDHandler != null && !bl3) {
                this.fDTDHandler.endDTD(null);
            }
            this.setScannerState(1);
            return false;
        } while (bl);
        return true;
    }

    public void reset(XMLComponentManager xMLComponentManager) throws XMLConfigurationException {
        super.reset(xMLComponentManager);
        this.init();
    }

    public void reset() {
        super.reset();
        this.init();
    }

    public String[] getRecognizedFeatures() {
        return (String[])RECOGNIZED_FEATURES.clone();
    }

    public String[] getRecognizedProperties() {
        return (String[])RECOGNIZED_PROPERTIES.clone();
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

    public void setDTDHandler(XMLDTDHandler xMLDTDHandler) {
        this.fDTDHandler = xMLDTDHandler;
    }

    public XMLDTDHandler getDTDHandler() {
        return this.fDTDHandler;
    }

    public void setDTDContentModelHandler(XMLDTDContentModelHandler xMLDTDContentModelHandler) {
        this.fDTDContentModelHandler = xMLDTDContentModelHandler;
    }

    public XMLDTDContentModelHandler getDTDContentModelHandler() {
        return this.fDTDContentModelHandler;
    }

    public void startEntity(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
        super.startEntity(string, xMLResourceIdentifier, string2, augmentations);
        boolean bl = string.equals("[dtd]");
        if (bl) {
            if (this.fDTDHandler != null && !this.fStartDTDCalled) {
                this.fDTDHandler.startDTD(this.fEntityScanner, null);
            }
            if (this.fDTDHandler != null) {
                this.fDTDHandler.startExternalSubset(xMLResourceIdentifier, null);
            }
            this.fEntityManager.startExternalSubset();
            ++this.fExtEntityDepth;
        } else if (string.charAt(0) == '%') {
            this.pushPEStack(this.fMarkUpDepth, this.fReportEntity);
            if (this.fEntityScanner.isExternal()) {
                ++this.fExtEntityDepth;
            }
        }
        if (this.fDTDHandler != null && !bl && this.fReportEntity) {
            this.fDTDHandler.startParameterEntity(string, xMLResourceIdentifier, string2, augmentations);
        }
    }

    public void endEntity(String string, Augmentations augmentations) throws XNIException {
        super.endEntity(string, augmentations);
        if (this.fScannerState == 0) {
            return;
        }
        boolean bl = this.fReportEntity;
        if (string.startsWith("%")) {
            bl = this.peekReportEntity();
            int n2 = this.popPEStack();
            if (n2 == 0 && n2 < this.fMarkUpDepth) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "ILL_FORMED_PARAMETER_ENTITY_WHEN_USED_IN_DECL", new Object[]{this.fEntityManager.fCurrentEntity.name}, 2);
            }
            if (n2 != this.fMarkUpDepth) {
                bl = false;
                if (this.fValidation) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "ImproperDeclarationNesting", new Object[]{string}, 1);
                }
            }
            if (this.fEntityScanner.isExternal()) {
                --this.fExtEntityDepth;
            }
            if (this.fDTDHandler != null && bl) {
                this.fDTDHandler.endParameterEntity(string, augmentations);
            }
        } else if (string.equals("[dtd]")) {
            if (this.fIncludeSectDepth != 0) {
                this.reportFatalError("IncludeSectUnterminated", null);
            }
            this.fScannerState = 0;
            this.fEntityManager.endExternalSubset();
            if (this.fDTDHandler != null) {
                this.fDTDHandler.endExternalSubset(null);
                this.fDTDHandler.endDTD(null);
            }
            --this.fExtEntityDepth;
        }
    }

    protected final void setScannerState(int n2) {
        this.fScannerState = n2;
    }

    private static String getScannerStateName(int n2) {
        return "??? (" + n2 + ')';
    }

    protected final boolean scanningInternalSubset() {
        return this.fExtEntityDepth == 0;
    }

    protected void startPE(String string, boolean bl) throws IOException, XNIException {
        int n2 = this.fPEDepth;
        String string2 = "%" + string;
        if (!this.fSeenPEReferences) {
            this.fSeenPEReferences = true;
            this.fEntityManager.notifyHasPEReferences();
        }
        if (this.fValidation && !this.fEntityManager.isDeclaredEntity(string2)) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EntityNotDeclared", new Object[]{string}, 1);
        }
        this.fEntityManager.startEntity(this.fSymbolTable.addSymbol(string2), bl);
        if (n2 != this.fPEDepth && this.fEntityScanner.isExternal()) {
            this.scanTextDecl();
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    protected final boolean scanTextDecl() throws IOException, XNIException {
        var1_1 = false;
        if (!this.fEntityScanner.skipString("<?xml")) ** GOTO lbl29
        ++this.fMarkUpDepth;
        if (!this.isValidNameChar(this.fEntityScanner.peekChar())) ** GOTO lbl9
        this.fStringBuffer.clear();
        this.fStringBuffer.append("xml");
        if (!this.fNamespaces) ** GOTO lbl26
        ** GOTO lbl23
lbl9: // 1 sources:
        var2_3 = null;
        var3_4 = null;
        this.scanXMLDeclOrTextDecl(true, this.fStrings);
        var1_1 = true;
        --this.fMarkUpDepth;
        var2_3 = this.fStrings[0];
        var3_4 = this.fStrings[1];
        this.fEntityScanner.setXMLVersion(var2_3);
        if (!this.fEntityScanner.fCurrentEntity.isEncodingExternallySpecified()) {
            this.fEntityScanner.setEncoding(var3_4);
        }
        if (this.fDTDHandler == null) ** GOTO lbl29
        this.fDTDHandler.textDecl(var2_3, var3_4, null);
        ** GOTO lbl29
lbl-1000: // 1 sources:
        {
            this.fStringBuffer.append((char)this.fEntityScanner.scanChar());
lbl23: // 2 sources:
            ** while (this.isValidNCName((int)this.fEntityScanner.peekChar()))
        }
lbl24: // 1 sources:
        ** GOTO lbl27
lbl-1000: // 1 sources:
        {
            this.fStringBuffer.append((char)this.fEntityScanner.scanChar());
lbl26: // 2 sources:
            ** while (this.isValidNameChar((int)this.fEntityScanner.peekChar()))
        }
lbl27: // 2 sources:
        var2_2 = this.fSymbolTable.addSymbol(this.fStringBuffer.ch, this.fStringBuffer.offset, this.fStringBuffer.length);
        this.scanPIData(var2_2, this.fString);
lbl29: // 4 sources:
        this.fEntityManager.fCurrentEntity.mayReadChunks = true;
        return var1_1;
    }

    protected final void scanPIData(String string, XMLString xMLString) throws IOException, XNIException {
        super.scanPIData(string, xMLString);
        --this.fMarkUpDepth;
        if (this.fDTDHandler != null) {
            this.fDTDHandler.processingInstruction(string, xMLString, null);
        }
    }

    protected final void scanComment() throws IOException, XNIException {
        this.fReportEntity = false;
        this.scanComment(this.fStringBuffer);
        --this.fMarkUpDepth;
        if (this.fDTDHandler != null) {
            this.fDTDHandler.comment(this.fStringBuffer, null);
        }
        this.fReportEntity = true;
    }

    protected final void scanElementDecl() throws IOException, XNIException {
        String string;
        this.fReportEntity = false;
        if (!this.skipSeparator(true, !this.scanningInternalSubset())) {
            this.reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ELEMENT_TYPE_IN_ELEMENTDECL", null);
        }
        if ((string = this.fEntityScanner.scanName()) == null) {
            this.reportFatalError("MSG_ELEMENT_TYPE_REQUIRED_IN_ELEMENTDECL", null);
        }
        if (!this.skipSeparator(true, !this.scanningInternalSubset())) {
            this.reportFatalError("MSG_SPACE_REQUIRED_BEFORE_CONTENTSPEC_IN_ELEMENTDECL", new Object[]{string});
        }
        if (this.fDTDContentModelHandler != null) {
            this.fDTDContentModelHandler.startContentModel(string, null);
        }
        String string2 = null;
        this.fReportEntity = true;
        if (this.fEntityScanner.skipString("EMPTY")) {
            string2 = "EMPTY";
            if (this.fDTDContentModelHandler != null) {
                this.fDTDContentModelHandler.empty(null);
            }
        } else if (this.fEntityScanner.skipString("ANY")) {
            string2 = "ANY";
            if (this.fDTDContentModelHandler != null) {
                this.fDTDContentModelHandler.any(null);
            }
        } else {
            if (!this.fEntityScanner.skipChar(40)) {
                this.reportFatalError("MSG_OPEN_PAREN_OR_ELEMENT_TYPE_REQUIRED_IN_CHILDREN", new Object[]{string});
            }
            if (this.fDTDContentModelHandler != null) {
                this.fDTDContentModelHandler.startGroup(null);
            }
            this.fStringBuffer.clear();
            this.fStringBuffer.append('(');
            ++this.fMarkUpDepth;
            this.skipSeparator(false, !this.scanningInternalSubset());
            if (this.fEntityScanner.skipString("#PCDATA")) {
                this.scanMixed(string);
            } else {
                this.scanChildren(string);
            }
            string2 = this.fStringBuffer.toString();
        }
        if (this.fDTDContentModelHandler != null) {
            this.fDTDContentModelHandler.endContentModel(null);
        }
        this.fReportEntity = false;
        this.skipSeparator(false, !this.scanningInternalSubset());
        if (!this.fEntityScanner.skipChar(62)) {
            this.reportFatalError("ElementDeclUnterminated", new Object[]{string});
        }
        this.fReportEntity = true;
        --this.fMarkUpDepth;
        if (this.fDTDHandler != null) {
            this.fDTDHandler.elementDecl(string, string2, null);
        }
    }

    private final void scanMixed(String string) throws IOException, XNIException {
        String string2 = null;
        this.fStringBuffer.append("#PCDATA");
        if (this.fDTDContentModelHandler != null) {
            this.fDTDContentModelHandler.pcdata(null);
        }
        this.skipSeparator(false, !this.scanningInternalSubset());
        while (this.fEntityScanner.skipChar(124)) {
            this.fStringBuffer.append('|');
            if (this.fDTDContentModelHandler != null) {
                this.fDTDContentModelHandler.separator(0, null);
            }
            this.skipSeparator(false, !this.scanningInternalSubset());
            string2 = this.fEntityScanner.scanName();
            if (string2 == null) {
                this.reportFatalError("MSG_ELEMENT_TYPE_REQUIRED_IN_MIXED_CONTENT", new Object[]{string});
            }
            this.fStringBuffer.append(string2);
            if (this.fDTDContentModelHandler != null) {
                this.fDTDContentModelHandler.element(string2, null);
            }
            this.skipSeparator(false, !this.scanningInternalSubset());
        }
        if (this.fEntityScanner.skipString(")*")) {
            this.fStringBuffer.append(")*");
            if (this.fDTDContentModelHandler != null) {
                this.fDTDContentModelHandler.endGroup(null);
                this.fDTDContentModelHandler.occurrence(3, null);
            }
        } else if (string2 != null) {
            this.reportFatalError("MixedContentUnterminated", new Object[]{string});
        } else if (this.fEntityScanner.skipChar(41)) {
            this.fStringBuffer.append(')');
            if (this.fDTDContentModelHandler != null) {
                this.fDTDContentModelHandler.endGroup(null);
            }
        } else {
            this.reportFatalError("MSG_CLOSE_PAREN_REQUIRED_IN_CHILDREN", new Object[]{string});
        }
        --this.fMarkUpDepth;
    }

    private final void scanChildren(String string) throws IOException, XNIException {
        this.fContentDepth = 0;
        this.pushContentStack(0);
        int n2 = 0;
        do {
            block23 : {
                short s2;
                if (this.fEntityScanner.skipChar(40)) {
                    ++this.fMarkUpDepth;
                    this.fStringBuffer.append('(');
                    if (this.fDTDContentModelHandler != null) {
                        this.fDTDContentModelHandler.startGroup(null);
                    }
                    this.pushContentStack(n2);
                    n2 = 0;
                    this.skipSeparator(false, !this.scanningInternalSubset());
                    continue;
                }
                this.skipSeparator(false, !this.scanningInternalSubset());
                String string2 = this.fEntityScanner.scanName();
                if (string2 == null) {
                    this.reportFatalError("MSG_OPEN_PAREN_OR_ELEMENT_TYPE_REQUIRED_IN_CHILDREN", new Object[]{string});
                    return;
                }
                if (this.fDTDContentModelHandler != null) {
                    this.fDTDContentModelHandler.element(string2, null);
                }
                this.fStringBuffer.append(string2);
                int n3 = this.fEntityScanner.peekChar();
                if (n3 == 63 || n3 == 42 || n3 == 43) {
                    if (this.fDTDContentModelHandler != null) {
                        s2 = n3 == 63 ? 2 : (n3 == 42 ? 3 : 4);
                        this.fDTDContentModelHandler.occurrence(s2, null);
                    }
                    this.fEntityScanner.scanChar();
                    this.fStringBuffer.append((char)n3);
                }
                do {
                    this.skipSeparator(false, !this.scanningInternalSubset());
                    n3 = this.fEntityScanner.peekChar();
                    if (n3 == 44 && n2 != 124) {
                        n2 = n3;
                        if (this.fDTDContentModelHandler != null) {
                            this.fDTDContentModelHandler.separator(1, null);
                        }
                        this.fEntityScanner.scanChar();
                        this.fStringBuffer.append(',');
                        break block23;
                    }
                    if (n3 == 124 && n2 != 44) {
                        n2 = n3;
                        if (this.fDTDContentModelHandler != null) {
                            this.fDTDContentModelHandler.separator(0, null);
                        }
                        this.fEntityScanner.scanChar();
                        this.fStringBuffer.append('|');
                        break block23;
                    }
                    if (n3 != 41) {
                        this.reportFatalError("MSG_CLOSE_PAREN_REQUIRED_IN_CHILDREN", new Object[]{string});
                    }
                    if (this.fDTDContentModelHandler != null) {
                        this.fDTDContentModelHandler.endGroup(null);
                    }
                    n2 = this.popContentStack();
                    if (this.fEntityScanner.skipString(")?")) {
                        this.fStringBuffer.append(")?");
                        if (this.fDTDContentModelHandler != null) {
                            s2 = 2;
                            this.fDTDContentModelHandler.occurrence(s2, null);
                        }
                    } else if (this.fEntityScanner.skipString(")+")) {
                        this.fStringBuffer.append(")+");
                        if (this.fDTDContentModelHandler != null) {
                            s2 = 4;
                            this.fDTDContentModelHandler.occurrence(s2, null);
                        }
                    } else if (this.fEntityScanner.skipString(")*")) {
                        this.fStringBuffer.append(")*");
                        if (this.fDTDContentModelHandler != null) {
                            s2 = 3;
                            this.fDTDContentModelHandler.occurrence(s2, null);
                        }
                    } else {
                        this.fEntityScanner.scanChar();
                        this.fStringBuffer.append(')');
                    }
                    --this.fMarkUpDepth;
                } while (this.fContentDepth != 0);
                return;
            }
            this.skipSeparator(false, !this.scanningInternalSubset());
        } while (true);
    }

    protected final void scanAttlistDecl() throws IOException, XNIException {
        String string;
        this.fReportEntity = false;
        if (!this.skipSeparator(true, !this.scanningInternalSubset())) {
            this.reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ELEMENT_TYPE_IN_ATTLISTDECL", null);
        }
        if ((string = this.fEntityScanner.scanName()) == null) {
            this.reportFatalError("MSG_ELEMENT_TYPE_REQUIRED_IN_ATTLISTDECL", null);
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.startAttlist(string, null);
        }
        if (!this.skipSeparator(true, !this.scanningInternalSubset())) {
            if (this.fEntityScanner.skipChar(62)) {
                if (this.fDTDHandler != null) {
                    this.fDTDHandler.endAttlist(null);
                }
                --this.fMarkUpDepth;
                return;
            }
            this.reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ATTRIBUTE_NAME_IN_ATTDEF", new Object[]{string});
        }
        while (!this.fEntityScanner.skipChar(62)) {
            String string2 = this.fEntityScanner.scanName();
            if (string2 == null) {
                this.reportFatalError("AttNameRequiredInAttDef", new Object[]{string});
            }
            if (!this.skipSeparator(true, !this.scanningInternalSubset())) {
                this.reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ATTTYPE_IN_ATTDEF", new Object[]{string, string2});
            }
            String string3 = this.scanAttType(string, string2);
            if (!this.skipSeparator(true, !this.scanningInternalSubset())) {
                this.reportFatalError("MSG_SPACE_REQUIRED_BEFORE_DEFAULTDECL_IN_ATTDEF", new Object[]{string, string2});
            }
            String string4 = this.scanAttDefaultDecl(string, string2, string3, this.fLiteral, this.fLiteral2);
            if (this.fDTDHandler != null) {
                String[] arrstring = null;
                if (this.fEnumerationCount != 0) {
                    arrstring = new String[this.fEnumerationCount];
                    System.arraycopy(this.fEnumeration, 0, arrstring, 0, this.fEnumerationCount);
                }
                if (string4 != null && (string4.equals("#REQUIRED") || string4.equals("#IMPLIED"))) {
                    this.fDTDHandler.attributeDecl(string, string2, string3, arrstring, string4, null, null, null);
                } else {
                    this.fDTDHandler.attributeDecl(string, string2, string3, arrstring, string4, this.fLiteral, this.fLiteral2, null);
                }
            }
            this.skipSeparator(false, !this.scanningInternalSubset());
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.endAttlist(null);
        }
        --this.fMarkUpDepth;
        this.fReportEntity = true;
    }

    private final String scanAttType(String string, String string2) throws IOException, XNIException {
        String string3 = null;
        this.fEnumerationCount = 0;
        if (this.fEntityScanner.skipString("CDATA")) {
            string3 = "CDATA";
        } else if (this.fEntityScanner.skipString("IDREFS")) {
            string3 = "IDREFS";
        } else if (this.fEntityScanner.skipString("IDREF")) {
            string3 = "IDREF";
        } else if (this.fEntityScanner.skipString("ID")) {
            string3 = "ID";
        } else if (this.fEntityScanner.skipString("ENTITY")) {
            string3 = "ENTITY";
        } else if (this.fEntityScanner.skipString("ENTITIES")) {
            string3 = "ENTITIES";
        } else if (this.fEntityScanner.skipString("NMTOKENS")) {
            string3 = "NMTOKENS";
        } else if (this.fEntityScanner.skipString("NMTOKEN")) {
            string3 = "NMTOKEN";
        } else if (this.fEntityScanner.skipString("NOTATION")) {
            int n2;
            string3 = "NOTATION";
            if (!this.skipSeparator(true, !this.scanningInternalSubset())) {
                this.reportFatalError("MSG_SPACE_REQUIRED_AFTER_NOTATION_IN_NOTATIONTYPE", new Object[]{string, string2});
            }
            if ((n2 = this.fEntityScanner.scanChar()) != 40) {
                this.reportFatalError("MSG_OPEN_PAREN_REQUIRED_IN_NOTATIONTYPE", new Object[]{string, string2});
            }
            ++this.fMarkUpDepth;
            do {
                this.skipSeparator(false, !this.scanningInternalSubset());
                String string4 = this.fEntityScanner.scanName();
                if (string4 == null) {
                    this.reportFatalError("MSG_NAME_REQUIRED_IN_NOTATIONTYPE", new Object[]{string, string2});
                    n2 = this.skipInvalidEnumerationValue();
                    if (n2 != 124) break;
                    continue;
                }
                this.ensureEnumerationSize(this.fEnumerationCount + 1);
                this.fEnumeration[this.fEnumerationCount++] = string4;
                this.skipSeparator(false, !this.scanningInternalSubset());
                n2 = this.fEntityScanner.scanChar();
            } while (n2 == 124);
            if (n2 != 41) {
                this.reportFatalError("NotationTypeUnterminated", new Object[]{string, string2});
            }
            --this.fMarkUpDepth;
        } else {
            string3 = "ENUMERATION";
            int n3 = this.fEntityScanner.scanChar();
            if (n3 != 40) {
                this.reportFatalError("AttTypeRequiredInAttDef", new Object[]{string, string2});
            }
            ++this.fMarkUpDepth;
            do {
                this.skipSeparator(false, !this.scanningInternalSubset());
                String string5 = this.fEntityScanner.scanNmtoken();
                if (string5 == null) {
                    this.reportFatalError("MSG_NMTOKEN_REQUIRED_IN_ENUMERATION", new Object[]{string, string2});
                    n3 = this.skipInvalidEnumerationValue();
                    if (n3 != 124) break;
                    continue;
                }
                this.ensureEnumerationSize(this.fEnumerationCount + 1);
                this.fEnumeration[this.fEnumerationCount++] = string5;
                this.skipSeparator(false, !this.scanningInternalSubset());
                n3 = this.fEntityScanner.scanChar();
            } while (n3 == 124);
            if (n3 != 41) {
                this.reportFatalError("EnumerationUnterminated", new Object[]{string, string2});
            }
            --this.fMarkUpDepth;
        }
        return string3;
    }

    protected final String scanAttDefaultDecl(String string, String string2, String string3, XMLString xMLString, XMLString xMLString2) throws IOException, XNIException {
        String string4 = null;
        this.fString.clear();
        xMLString.clear();
        if (this.fEntityScanner.skipString("#REQUIRED")) {
            string4 = "#REQUIRED";
        } else if (this.fEntityScanner.skipString("#IMPLIED")) {
            string4 = "#IMPLIED";
        } else {
            if (this.fEntityScanner.skipString("#FIXED")) {
                string4 = "#FIXED";
                if (!this.skipSeparator(true, !this.scanningInternalSubset())) {
                    this.reportFatalError("MSG_SPACE_REQUIRED_AFTER_FIXED_IN_DEFAULTDECL", new Object[]{string, string2});
                }
            }
            boolean bl = !this.fStandalone && (this.fSeenExternalDTD || this.fSeenPEReferences);
            this.scanAttributeValue(xMLString, xMLString2, string2, bl, string);
        }
        return string4;
    }

    private final void scanEntityDecl() throws IOException, XNIException {
        Object object;
        String string;
        String string2;
        boolean bl = false;
        boolean bl2 = false;
        this.fReportEntity = false;
        if (this.fEntityScanner.skipSpaces()) {
            if (!this.fEntityScanner.skipChar(37)) {
                bl = false;
            } else if (this.skipSeparator(true, !this.scanningInternalSubset())) {
                bl = true;
            } else if (this.scanningInternalSubset()) {
                this.reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ENTITY_NAME_IN_PEDECL", null);
                bl = true;
            } else if (this.fEntityScanner.peekChar() == 37) {
                this.skipSeparator(false, !this.scanningInternalSubset());
                bl = true;
            } else {
                bl2 = true;
            }
        } else if (this.scanningInternalSubset() || !this.fEntityScanner.skipChar(37)) {
            this.reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ENTITY_NAME_IN_ENTITYDECL", null);
            bl = false;
        } else if (this.fEntityScanner.skipSpaces()) {
            this.reportFatalError("MSG_SPACE_REQUIRED_BEFORE_PERCENT_IN_PEDECL", null);
            bl = false;
        } else {
            bl2 = true;
        }
        if (bl2) {
            do {
                if ((string = this.fEntityScanner.scanName()) == null) {
                    this.reportFatalError("NameRequiredInPEReference", null);
                } else if (!this.fEntityScanner.skipChar(59)) {
                    this.reportFatalError("SemicolonRequiredInPEReference", new Object[]{string});
                } else {
                    this.startPE(string, false);
                }
                this.fEntityScanner.skipSpaces();
                if (!this.fEntityScanner.skipChar(37)) break;
                if (bl) continue;
                if (this.skipSeparator(true, !this.scanningInternalSubset())) {
                    bl = true;
                    break;
                }
                bl = this.fEntityScanner.skipChar(37);
            } while (true);
        }
        string = null;
        string = this.fNamespaces ? this.fEntityScanner.scanNCName() : this.fEntityScanner.scanName();
        if (string == null) {
            this.reportFatalError("MSG_ENTITY_NAME_REQUIRED_IN_ENTITYDECL", null);
        }
        if (!this.skipSeparator(true, !this.scanningInternalSubset())) {
            if (this.fNamespaces && this.fEntityScanner.peekChar() == 58) {
                this.fEntityScanner.scanChar();
                object = new XMLStringBuffer(string);
                object.append(":");
                string2 = this.fEntityScanner.scanName();
                if (string2 != null) {
                    object.append(string2);
                }
                this.reportFatalError("ColonNotLegalWithNS", new Object[]{object.toString()});
                if (!this.skipSeparator(true, !this.scanningInternalSubset())) {
                    this.reportFatalError("MSG_SPACE_REQUIRED_AFTER_ENTITY_NAME_IN_ENTITYDECL", new Object[]{string});
                }
            } else {
                this.reportFatalError("MSG_SPACE_REQUIRED_AFTER_ENTITY_NAME_IN_ENTITYDECL", new Object[]{string});
            }
        }
        this.scanExternalID(this.fStrings, false);
        object = this.fStrings[0];
        string2 = this.fStrings[1];
        String string3 = null;
        boolean bl3 = this.skipSeparator(true, !this.scanningInternalSubset());
        if (!bl && this.fEntityScanner.skipString("NDATA")) {
            if (!bl3) {
                this.reportFatalError("MSG_SPACE_REQUIRED_BEFORE_NDATA_IN_UNPARSED_ENTITYDECL", new Object[]{string});
            }
            if (!this.skipSeparator(true, !this.scanningInternalSubset())) {
                this.reportFatalError("MSG_SPACE_REQUIRED_BEFORE_NOTATION_NAME_IN_UNPARSED_ENTITYDECL", new Object[]{string});
            }
            if ((string3 = this.fEntityScanner.scanName()) == null) {
                this.reportFatalError("MSG_NOTATION_NAME_REQUIRED_FOR_UNPARSED_ENTITYDECL", new Object[]{string});
            }
        }
        if (object == null) {
            this.scanEntityValue(this.fLiteral, this.fLiteral2);
            this.fStringBuffer.clear();
            this.fStringBuffer2.clear();
            this.fStringBuffer.append(this.fLiteral.ch, this.fLiteral.offset, this.fLiteral.length);
            this.fStringBuffer2.append(this.fLiteral2.ch, this.fLiteral2.offset, this.fLiteral2.length);
        }
        this.skipSeparator(false, !this.scanningInternalSubset());
        if (!this.fEntityScanner.skipChar(62)) {
            this.reportFatalError("EntityDeclUnterminated", new Object[]{string});
        }
        --this.fMarkUpDepth;
        if (bl) {
            string = "%" + string;
        }
        if (object != null) {
            String string4 = this.fEntityScanner.getBaseSystemId();
            if (string3 != null) {
                this.fEntityManager.addUnparsedEntity(string, string2, (String)object, string4, string3);
            } else {
                this.fEntityManager.addExternalEntity(string, string2, (String)object, string4);
            }
            if (this.fDTDHandler != null) {
                this.fResourceIdentifier.setValues(string2, (String)object, string4, XMLEntityManager.expandSystemId((String)object, string4, false));
                if (string3 != null) {
                    this.fDTDHandler.unparsedEntityDecl(string, this.fResourceIdentifier, string3, null);
                } else {
                    this.fDTDHandler.externalEntityDecl(string, this.fResourceIdentifier, null);
                }
            }
        } else {
            this.fEntityManager.addInternalEntity(string, this.fStringBuffer.toString());
            if (this.fDTDHandler != null) {
                this.fDTDHandler.internalEntityDecl(string, this.fStringBuffer, this.fStringBuffer2, null);
            }
        }
        this.fReportEntity = true;
    }

    protected final void scanEntityValue(XMLString xMLString, XMLString xMLString2) throws IOException, XNIException {
        int n2 = this.fEntityScanner.scanChar();
        if (n2 != 39 && n2 != 34) {
            this.reportFatalError("OpenQuoteMissingInDecl", null);
        }
        int n3 = this.fEntityDepth;
        XMLString xMLString3 = this.fString;
        XMLString xMLString4 = this.fString;
        if (this.fEntityScanner.scanLiteral(n2, this.fString) != n2) {
            this.fStringBuffer.clear();
            this.fStringBuffer2.clear();
            do {
                String string;
                this.fStringBuffer.append(this.fString);
                this.fStringBuffer2.append(this.fString);
                if (this.fEntityScanner.skipChar(38)) {
                    if (this.fEntityScanner.skipChar(35)) {
                        this.fStringBuffer2.append("&#");
                        this.scanCharReferenceValue(this.fStringBuffer, this.fStringBuffer2);
                        continue;
                    }
                    this.fStringBuffer.append('&');
                    this.fStringBuffer2.append('&');
                    string = this.fEntityScanner.scanName();
                    if (string == null) {
                        this.reportFatalError("NameRequiredInReference", null);
                    } else {
                        this.fStringBuffer.append(string);
                        this.fStringBuffer2.append(string);
                    }
                    if (!this.fEntityScanner.skipChar(59)) {
                        this.reportFatalError("SemicolonRequiredInReference", new Object[]{string});
                        continue;
                    }
                    this.fStringBuffer.append(';');
                    this.fStringBuffer2.append(';');
                    continue;
                }
                if (this.fEntityScanner.skipChar(37)) {
                    do {
                        this.fStringBuffer2.append('%');
                        string = this.fEntityScanner.scanName();
                        if (string == null) {
                            this.reportFatalError("NameRequiredInPEReference", null);
                        } else if (!this.fEntityScanner.skipChar(59)) {
                            this.reportFatalError("SemicolonRequiredInPEReference", new Object[]{string});
                        } else {
                            if (this.scanningInternalSubset()) {
                                this.reportFatalError("PEReferenceWithinMarkup", new Object[]{string});
                            }
                            this.fStringBuffer2.append(string);
                            this.fStringBuffer2.append(';');
                        }
                        this.startPE(string, true);
                        this.fEntityScanner.skipSpaces();
                    } while (this.fEntityScanner.skipChar(37));
                    continue;
                }
                int n4 = this.fEntityScanner.peekChar();
                if (XMLChar.isHighSurrogate(n4)) {
                    this.scanSurrogates(this.fStringBuffer2);
                    continue;
                }
                if (this.isInvalidLiteral(n4)) {
                    this.reportFatalError("InvalidCharInLiteral", new Object[]{Integer.toHexString(n4)});
                    this.fEntityScanner.scanChar();
                    continue;
                }
                if (n4 == n2 && n3 == this.fEntityDepth) continue;
                this.fStringBuffer.append((char)n4);
                this.fStringBuffer2.append((char)n4);
                this.fEntityScanner.scanChar();
            } while (this.fEntityScanner.scanLiteral(n2, this.fString) != n2);
            this.fStringBuffer.append(this.fString);
            this.fStringBuffer2.append(this.fString);
            xMLString3 = this.fStringBuffer;
            xMLString4 = this.fStringBuffer2;
        }
        xMLString.setValues(xMLString3);
        xMLString2.setValues(xMLString4);
        if (!this.fEntityScanner.skipChar(n2)) {
            this.reportFatalError("CloseQuoteMissingInDecl", null);
        }
    }

    private final void scanNotationDecl() throws IOException, XNIException {
        Object object;
        this.fReportEntity = false;
        if (!this.skipSeparator(true, !this.scanningInternalSubset())) {
            this.reportFatalError("MSG_SPACE_REQUIRED_BEFORE_NOTATION_NAME_IN_NOTATIONDECL", null);
        }
        String string = null;
        string = this.fNamespaces ? this.fEntityScanner.scanNCName() : this.fEntityScanner.scanName();
        if (string == null) {
            this.reportFatalError("MSG_NOTATION_NAME_REQUIRED_IN_NOTATIONDECL", null);
        }
        if (!this.skipSeparator(true, !this.scanningInternalSubset())) {
            if (this.fNamespaces && this.fEntityScanner.peekChar() == 58) {
                this.fEntityScanner.scanChar();
                object = new XMLStringBuffer(string);
                object.append(":");
                object.append(this.fEntityScanner.scanName());
                this.reportFatalError("ColonNotLegalWithNS", new Object[]{object.toString()});
                this.skipSeparator(true, !this.scanningInternalSubset());
            } else {
                this.reportFatalError("MSG_SPACE_REQUIRED_AFTER_NOTATION_NAME_IN_NOTATIONDECL", new Object[]{string});
            }
        }
        this.scanExternalID(this.fStrings, true);
        object = this.fStrings[0];
        String string2 = this.fStrings[1];
        String string3 = this.fEntityScanner.getBaseSystemId();
        if (object == null && string2 == null) {
            this.reportFatalError("ExternalIDorPublicIDRequired", new Object[]{string});
        }
        this.skipSeparator(false, !this.scanningInternalSubset());
        if (!this.fEntityScanner.skipChar(62)) {
            this.reportFatalError("NotationDeclUnterminated", new Object[]{string});
        }
        --this.fMarkUpDepth;
        if (this.fDTDHandler != null) {
            this.fResourceIdentifier.setValues(string2, (String)object, string3, XMLEntityManager.expandSystemId((String)object, string3, false));
            this.fDTDHandler.notationDecl(string, this.fResourceIdentifier, null);
        }
        this.fReportEntity = true;
    }

    private final void scanConditionalSect(int n2) throws IOException, XNIException {
        this.fReportEntity = false;
        this.skipSeparator(false, !this.scanningInternalSubset());
        if (this.fEntityScanner.skipString("INCLUDE")) {
            this.skipSeparator(false, !this.scanningInternalSubset());
            if (n2 != this.fPEDepth && this.fValidation) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "INVALID_PE_IN_CONDITIONAL", new Object[]{this.fEntityManager.fCurrentEntity.name}, 1);
            }
            if (!this.fEntityScanner.skipChar(91)) {
                this.reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
            }
            if (this.fDTDHandler != null) {
                this.fDTDHandler.startConditional(0, null);
            }
            ++this.fIncludeSectDepth;
            this.fReportEntity = true;
        } else {
            if (this.fEntityScanner.skipString("IGNORE")) {
                this.skipSeparator(false, !this.scanningInternalSubset());
                if (n2 != this.fPEDepth && this.fValidation) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "INVALID_PE_IN_CONDITIONAL", new Object[]{this.fEntityManager.fCurrentEntity.name}, 1);
                }
                if (this.fDTDHandler != null) {
                    this.fDTDHandler.startConditional(1, null);
                }
                if (!this.fEntityScanner.skipChar(91)) {
                    this.reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
                }
                this.fReportEntity = true;
                int n3 = ++this.fIncludeSectDepth;
                if (this.fDTDHandler != null) {
                    this.fIgnoreConditionalBuffer.clear();
                }
                do {
                    if (this.fEntityScanner.skipChar(60)) {
                        if (this.fDTDHandler != null) {
                            this.fIgnoreConditionalBuffer.append('<');
                        }
                        if (!this.fEntityScanner.skipChar(33)) continue;
                        if (this.fEntityScanner.skipChar(91)) {
                            if (this.fDTDHandler != null) {
                                this.fIgnoreConditionalBuffer.append("![");
                            }
                            ++this.fIncludeSectDepth;
                            continue;
                        }
                        if (this.fDTDHandler == null) continue;
                        this.fIgnoreConditionalBuffer.append("!");
                        continue;
                    }
                    if (this.fEntityScanner.skipChar(93)) {
                        if (this.fDTDHandler != null) {
                            this.fIgnoreConditionalBuffer.append(']');
                        }
                        if (!this.fEntityScanner.skipChar(93)) continue;
                        if (this.fDTDHandler != null) {
                            this.fIgnoreConditionalBuffer.append(']');
                        }
                        while (this.fEntityScanner.skipChar(93)) {
                            if (this.fDTDHandler == null) continue;
                            this.fIgnoreConditionalBuffer.append(']');
                        }
                        if (!this.fEntityScanner.skipChar(62)) continue;
                        if (this.fIncludeSectDepth-- == n3) {
                            --this.fMarkUpDepth;
                            if (this.fDTDHandler != null) {
                                this.fLiteral.setValues(this.fIgnoreConditionalBuffer.ch, 0, this.fIgnoreConditionalBuffer.length - 2);
                                this.fDTDHandler.ignoredCharacters(this.fLiteral, null);
                                this.fDTDHandler.endConditional(null);
                            }
                            return;
                        }
                        if (this.fDTDHandler == null) continue;
                        this.fIgnoreConditionalBuffer.append('>');
                        continue;
                    }
                    int n4 = this.fEntityScanner.scanChar();
                    if (this.fScannerState == 0) {
                        this.reportFatalError("IgnoreSectUnterminated", null);
                        return;
                    }
                    if (this.fDTDHandler == null) continue;
                    this.fIgnoreConditionalBuffer.append((char)n4);
                } while (true);
            }
            this.reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
        }
    }

    protected final boolean scanDecls(boolean bl) throws IOException, XNIException {
        this.skipSeparator(false, true);
        boolean bl2 = true;
        while (bl2 && this.fScannerState == 2) {
            bl2 = bl;
            if (this.fEntityScanner.skipChar(60)) {
                ++this.fMarkUpDepth;
                if (this.fEntityScanner.skipChar(63)) {
                    this.scanPI();
                } else if (this.fEntityScanner.skipChar(33)) {
                    if (this.fEntityScanner.skipChar(45)) {
                        if (!this.fEntityScanner.skipChar(45)) {
                            this.reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
                        } else {
                            this.scanComment();
                        }
                    } else if (this.fEntityScanner.skipString("ELEMENT")) {
                        this.scanElementDecl();
                    } else if (this.fEntityScanner.skipString("ATTLIST")) {
                        this.scanAttlistDecl();
                    } else if (this.fEntityScanner.skipString("ENTITY")) {
                        this.scanEntityDecl();
                    } else if (this.fEntityScanner.skipString("NOTATION")) {
                        this.scanNotationDecl();
                    } else if (this.fEntityScanner.skipChar(91) && !this.scanningInternalSubset()) {
                        this.scanConditionalSect(this.fPEDepth);
                    } else {
                        --this.fMarkUpDepth;
                        this.reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
                    }
                } else {
                    --this.fMarkUpDepth;
                    this.reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
                }
            } else if (this.fIncludeSectDepth > 0 && this.fEntityScanner.skipChar(93)) {
                if (!this.fEntityScanner.skipChar(93) || !this.fEntityScanner.skipChar(62)) {
                    this.reportFatalError("IncludeSectUnterminated", null);
                }
                if (this.fDTDHandler != null) {
                    this.fDTDHandler.endConditional(null);
                }
                --this.fIncludeSectDepth;
                --this.fMarkUpDepth;
            } else {
                if (this.scanningInternalSubset() && this.fEntityScanner.peekChar() == 93) {
                    return false;
                }
                if (!this.fEntityScanner.skipSpaces()) {
                    int n2;
                    this.reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
                    do {
                        this.fEntityScanner.scanChar();
                        this.skipSeparator(false, true);
                    } while ((n2 = this.fEntityScanner.peekChar()) != 60 && n2 != 93 && !XMLChar.isSpace(n2));
                }
            }
            this.skipSeparator(false, true);
        }
        return this.fScannerState != 0;
    }

    private boolean skipSeparator(boolean bl, boolean bl2) throws IOException, XNIException {
        int n2 = this.fPEDepth;
        boolean bl3 = this.fEntityScanner.skipSpaces();
        if (!bl2 || !this.fEntityScanner.skipChar(37)) {
            return !bl || bl3 || n2 != this.fPEDepth;
        }
        do {
            String string;
            if ((string = this.fEntityScanner.scanName()) == null) {
                this.reportFatalError("NameRequiredInPEReference", null);
            } else if (!this.fEntityScanner.skipChar(59)) {
                this.reportFatalError("SemicolonRequiredInPEReference", new Object[]{string});
            }
            this.startPE(string, false);
            this.fEntityScanner.skipSpaces();
        } while (this.fEntityScanner.skipChar(37));
        return true;
    }

    private final void pushContentStack(int n2) {
        if (this.fContentStack.length == this.fContentDepth) {
            int[] arrn = new int[this.fContentDepth * 2];
            System.arraycopy(this.fContentStack, 0, arrn, 0, this.fContentDepth);
            this.fContentStack = arrn;
        }
        this.fContentStack[this.fContentDepth++] = n2;
    }

    private final int popContentStack() {
        return this.fContentStack[--this.fContentDepth];
    }

    private final void pushPEStack(int n2, boolean bl) {
        if (this.fPEStack.length == this.fPEDepth) {
            int[] arrn = new int[this.fPEDepth * 2];
            System.arraycopy(this.fPEStack, 0, arrn, 0, this.fPEDepth);
            this.fPEStack = arrn;
            boolean[] arrbl = new boolean[this.fPEDepth * 2];
            System.arraycopy(this.fPEReport, 0, arrbl, 0, this.fPEDepth);
            this.fPEReport = arrbl;
        }
        this.fPEReport[this.fPEDepth] = bl;
        this.fPEStack[this.fPEDepth++] = n2;
    }

    private final int popPEStack() {
        return this.fPEStack[--this.fPEDepth];
    }

    private final boolean peekReportEntity() {
        return this.fPEReport[this.fPEDepth - 1];
    }

    private final void ensureEnumerationSize(int n2) {
        if (this.fEnumeration.length == n2) {
            String[] arrstring = new String[n2 * 2];
            System.arraycopy(this.fEnumeration, 0, arrstring, 0, n2);
            this.fEnumeration = arrstring;
        }
    }

    private void init() {
        this.fStartDTDCalled = false;
        this.fExtEntityDepth = 0;
        this.fIncludeSectDepth = 0;
        this.fMarkUpDepth = 0;
        this.fPEDepth = 0;
        this.fStandalone = false;
        this.fSeenExternalDTD = false;
        this.fSeenPEReferences = false;
        this.setScannerState(1);
    }

    private int skipInvalidEnumerationValue() throws IOException {
        int n2;
        while ((n2 = this.fEntityScanner.scanChar()) != 124 && n2 != 41) {
        }
        this.ensureEnumerationSize(this.fEnumerationCount + 1);
        this.fEnumeration[this.fEnumerationCount++] = XMLSymbols.EMPTY_STRING;
        return n2;
    }
}

