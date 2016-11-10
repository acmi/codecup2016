/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl;

import java.io.CharConversionException;
import java.io.EOFException;
import java.io.IOException;
import org.apache.xerces.impl.ExternalSubsetResolver;
import org.apache.xerces.impl.XMLEntityHandler;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLEntityScanner;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.XMLScanner;
import org.apache.xerces.impl.io.MalformedByteSequenceException;
import org.apache.xerces.util.AugmentationsImpl;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentScanner;
import org.apache.xerces.xni.parser.XMLInputSource;

public class XMLDocumentFragmentScannerImpl
extends XMLScanner
implements XMLEntityHandler,
XMLComponent,
XMLDocumentScanner {
    protected static final int SCANNER_STATE_START_OF_MARKUP = 1;
    protected static final int SCANNER_STATE_COMMENT = 2;
    protected static final int SCANNER_STATE_PI = 3;
    protected static final int SCANNER_STATE_DOCTYPE = 4;
    protected static final int SCANNER_STATE_ROOT_ELEMENT = 6;
    protected static final int SCANNER_STATE_CONTENT = 7;
    protected static final int SCANNER_STATE_REFERENCE = 8;
    protected static final int SCANNER_STATE_END_OF_INPUT = 13;
    protected static final int SCANNER_STATE_TERMINATED = 14;
    protected static final int SCANNER_STATE_CDATA = 15;
    protected static final int SCANNER_STATE_TEXT_DECL = 16;
    protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    protected static final String NOTIFY_BUILTIN_REFS = "http://apache.org/xml/features/scanner/notify-builtin-refs";
    protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
    private static final String[] RECOGNIZED_FEATURES = new String[]{"http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/validation", "http://apache.org/xml/features/scanner/notify-builtin-refs", "http://apache.org/xml/features/scanner/notify-char-refs"};
    private static final Boolean[] FEATURE_DEFAULTS = new Boolean[]{null, null, Boolean.FALSE, Boolean.FALSE};
    private static final String[] RECOGNIZED_PROPERTIES = new String[]{"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-manager", "http://apache.org/xml/properties/internal/entity-resolver"};
    private static final Object[] PROPERTY_DEFAULTS = new Object[]{null, null, null, null};
    private static final boolean DEBUG_SCANNER_STATE = false;
    private static final boolean DEBUG_DISPATCHER = false;
    protected static final boolean DEBUG_CONTENT_SCANNING = false;
    protected XMLDocumentHandler fDocumentHandler;
    protected int[] fEntityStack = new int[4];
    protected int fMarkupDepth;
    protected int fScannerState;
    protected boolean fInScanContent = false;
    protected boolean fHasExternalDTD;
    protected boolean fStandalone;
    protected boolean fIsEntityDeclaredVC;
    protected ExternalSubsetResolver fExternalSubsetResolver;
    protected QName fCurrentElement;
    protected final ElementStack fElementStack = new ElementStack();
    protected boolean fNotifyBuiltInRefs = false;
    protected Dispatcher fDispatcher;
    protected final Dispatcher fContentDispatcher;
    protected final QName fElementQName;
    protected final QName fAttributeQName;
    protected final XMLAttributesImpl fAttributes;
    protected final XMLString fTempString;
    protected final XMLString fTempString2;
    private final String[] fStrings;
    private final XMLStringBuffer fStringBuffer;
    private final XMLStringBuffer fStringBuffer2;
    private final QName fQName;
    private final char[] fSingleChar;
    private boolean fSawSpace;
    private Augmentations fTempAugmentations;

    public XMLDocumentFragmentScannerImpl() {
        this.fContentDispatcher = this.createContentDispatcher();
        this.fElementQName = new QName();
        this.fAttributeQName = new QName();
        this.fAttributes = new XMLAttributesImpl();
        this.fTempString = new XMLString();
        this.fTempString2 = new XMLString();
        this.fStrings = new String[3];
        this.fStringBuffer = new XMLStringBuffer();
        this.fStringBuffer2 = new XMLStringBuffer();
        this.fQName = new QName();
        this.fSingleChar = new char[1];
        this.fTempAugmentations = null;
    }

    public void setInputSource(XMLInputSource xMLInputSource) throws IOException {
        this.fEntityManager.setEntityHandler(this);
        this.fEntityManager.startEntity("$fragment$", xMLInputSource, false, true);
    }

    public boolean scanDocument(boolean bl) throws IOException, XNIException {
        this.fEntityScanner = this.fEntityManager.getEntityScanner();
        this.fEntityManager.setEntityHandler(this);
        do {
            if (this.fDispatcher.dispatch(bl)) continue;
            return false;
        } while (bl);
        return true;
    }

    public void reset(XMLComponentManager xMLComponentManager) throws XMLConfigurationException {
        super.reset(xMLComponentManager);
        this.fAttributes.setNamespaces(this.fNamespaces);
        this.fMarkupDepth = 0;
        this.fCurrentElement = null;
        this.fElementStack.clear();
        this.fHasExternalDTD = false;
        this.fStandalone = false;
        this.fIsEntityDeclaredVC = false;
        this.fInScanContent = false;
        this.setScannerState(7);
        this.setDispatcher(this.fContentDispatcher);
        if (this.fParserSettings) {
            try {
                this.fNotifyBuiltInRefs = xMLComponentManager.getFeature("http://apache.org/xml/features/scanner/notify-builtin-refs");
            }
            catch (XMLConfigurationException xMLConfigurationException) {
                this.fNotifyBuiltInRefs = false;
            }
            try {
                Object object = xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
                this.fExternalSubsetResolver = object instanceof ExternalSubsetResolver ? (ExternalSubsetResolver)object : null;
            }
            catch (XMLConfigurationException xMLConfigurationException) {
                this.fExternalSubsetResolver = null;
            }
        }
    }

    public String[] getRecognizedFeatures() {
        return (String[])RECOGNIZED_FEATURES.clone();
    }

    public void setFeature(String string, boolean bl) throws XMLConfigurationException {
        int n2;
        super.setFeature(string, bl);
        if (string.startsWith("http://apache.org/xml/features/") && (n2 = string.length() - "http://apache.org/xml/features/".length()) == "scanner/notify-builtin-refs".length() && string.endsWith("scanner/notify-builtin-refs")) {
            this.fNotifyBuiltInRefs = bl;
        }
    }

    public String[] getRecognizedProperties() {
        return (String[])RECOGNIZED_PROPERTIES.clone();
    }

    public void setProperty(String string, Object object) throws XMLConfigurationException {
        super.setProperty(string, object);
        if (string.startsWith("http://apache.org/xml/properties/")) {
            int n2 = string.length() - "http://apache.org/xml/properties/".length();
            if (n2 == "internal/entity-manager".length() && string.endsWith("internal/entity-manager")) {
                this.fEntityManager = (XMLEntityManager)object;
                return;
            }
            if (n2 == "internal/entity-resolver".length() && string.endsWith("internal/entity-resolver")) {
                this.fExternalSubsetResolver = object instanceof ExternalSubsetResolver ? (ExternalSubsetResolver)object : null;
                return;
            }
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

    public void startEntity(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
        if (this.fEntityDepth == this.fEntityStack.length) {
            int[] arrn = new int[this.fEntityStack.length * 2];
            System.arraycopy(this.fEntityStack, 0, arrn, 0, this.fEntityStack.length);
            this.fEntityStack = arrn;
        }
        this.fEntityStack[this.fEntityDepth] = this.fMarkupDepth;
        super.startEntity(string, xMLResourceIdentifier, string2, augmentations);
        if (this.fStandalone && this.fEntityManager.isEntityDeclInExternalSubset(string)) {
            this.reportFatalError("MSG_REFERENCE_TO_EXTERNALLY_DECLARED_ENTITY_WHEN_STANDALONE", new Object[]{string});
        }
        if (this.fDocumentHandler != null && !this.fScanningAttribute && !string.equals("[xml]")) {
            this.fDocumentHandler.startGeneralEntity(string, xMLResourceIdentifier, string2, augmentations);
        }
    }

    public void endEntity(String string, Augmentations augmentations) throws XNIException {
        if (this.fInScanContent && this.fStringBuffer.length != 0 && this.fDocumentHandler != null) {
            this.fDocumentHandler.characters(this.fStringBuffer, null);
            this.fStringBuffer.length = 0;
        }
        super.endEntity(string, augmentations);
        if (this.fMarkupDepth != this.fEntityStack[this.fEntityDepth]) {
            this.reportFatalError("MarkupEntityMismatch", null);
        }
        if (this.fDocumentHandler != null && !this.fScanningAttribute && !string.equals("[xml]")) {
            this.fDocumentHandler.endGeneralEntity(string, augmentations);
        }
    }

    protected Dispatcher createContentDispatcher() {
        return new FragmentContentDispatcher(this);
    }

    protected void scanXMLDeclOrTextDecl(boolean bl) throws IOException, XNIException {
        super.scanXMLDeclOrTextDecl(bl, this.fStrings);
        --this.fMarkupDepth;
        String string = this.fStrings[0];
        String string2 = this.fStrings[1];
        String string3 = this.fStrings[2];
        this.fStandalone = string3 != null && string3.equals("yes");
        this.fEntityManager.setStandalone(this.fStandalone);
        this.fEntityScanner.setXMLVersion(string);
        if (this.fDocumentHandler != null) {
            if (bl) {
                this.fDocumentHandler.textDecl(string, string2, null);
            } else {
                this.fDocumentHandler.xmlDecl(string, string2, string3, null);
            }
        }
        if (string2 != null && !this.fEntityScanner.fCurrentEntity.isEncodingExternallySpecified()) {
            this.fEntityScanner.setEncoding(string2);
        }
    }

    protected void scanPIData(String string, XMLString xMLString) throws IOException, XNIException {
        super.scanPIData(string, xMLString);
        --this.fMarkupDepth;
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.processingInstruction(string, xMLString, null);
        }
    }

    protected void scanComment() throws IOException, XNIException {
        this.scanComment(this.fStringBuffer);
        --this.fMarkupDepth;
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.comment(this.fStringBuffer, null);
        }
    }

    protected boolean scanStartElement() throws IOException, XNIException {
        String string;
        if (this.fNamespaces) {
            this.fEntityScanner.scanQName(this.fElementQName);
        } else {
            string = this.fEntityScanner.scanName();
            this.fElementQName.setValues(null, string, string, null);
        }
        string = this.fElementQName.rawname;
        this.fCurrentElement = this.fElementStack.pushElement(this.fElementQName);
        boolean bl = false;
        this.fAttributes.removeAllAttributes();
        do {
            boolean bl2 = this.fEntityScanner.skipSpaces();
            int n2 = this.fEntityScanner.peekChar();
            if (n2 == 62) {
                this.fEntityScanner.scanChar();
                break;
            }
            if (n2 == 47) {
                this.fEntityScanner.scanChar();
                if (!this.fEntityScanner.skipChar(62)) {
                    this.reportFatalError("ElementUnterminated", new Object[]{string});
                }
                bl = true;
                break;
            }
            if (!(this.isValidNameStartChar(n2) && bl2 || this.isValidNameStartHighSurrogate(n2) && bl2)) {
                this.reportFatalError("ElementUnterminated", new Object[]{string});
            }
            this.scanAttribute(this.fAttributes);
        } while (true);
        if (this.fDocumentHandler != null) {
            if (bl) {
                --this.fMarkupDepth;
                if (this.fMarkupDepth < this.fEntityStack[this.fEntityDepth - 1]) {
                    this.reportFatalError("ElementEntityMismatch", new Object[]{this.fCurrentElement.rawname});
                }
                this.fDocumentHandler.emptyElement(this.fElementQName, this.fAttributes, null);
                this.fElementStack.popElement(this.fElementQName);
            } else {
                this.fDocumentHandler.startElement(this.fElementQName, this.fAttributes, null);
            }
        }
        return bl;
    }

    protected void scanStartElementName() throws IOException, XNIException {
        if (this.fNamespaces) {
            this.fEntityScanner.scanQName(this.fElementQName);
        } else {
            String string = this.fEntityScanner.scanName();
            this.fElementQName.setValues(null, string, string, null);
        }
        this.fSawSpace = this.fEntityScanner.skipSpaces();
    }

    protected boolean scanStartElementAfterName() throws IOException, XNIException {
        String string = this.fElementQName.rawname;
        this.fCurrentElement = this.fElementStack.pushElement(this.fElementQName);
        boolean bl = false;
        this.fAttributes.removeAllAttributes();
        do {
            int n2;
            if ((n2 = this.fEntityScanner.peekChar()) == 62) {
                this.fEntityScanner.scanChar();
                break;
            }
            if (n2 == 47) {
                this.fEntityScanner.scanChar();
                if (!this.fEntityScanner.skipChar(62)) {
                    this.reportFatalError("ElementUnterminated", new Object[]{string});
                }
                bl = true;
                break;
            }
            if (!(this.isValidNameStartChar(n2) && this.fSawSpace || this.isValidNameStartHighSurrogate(n2) && this.fSawSpace)) {
                this.reportFatalError("ElementUnterminated", new Object[]{string});
            }
            this.scanAttribute(this.fAttributes);
            this.fSawSpace = this.fEntityScanner.skipSpaces();
        } while (true);
        if (this.fDocumentHandler != null) {
            if (bl) {
                --this.fMarkupDepth;
                if (this.fMarkupDepth < this.fEntityStack[this.fEntityDepth - 1]) {
                    this.reportFatalError("ElementEntityMismatch", new Object[]{this.fCurrentElement.rawname});
                }
                this.fDocumentHandler.emptyElement(this.fElementQName, this.fAttributes, null);
                this.fElementStack.popElement(this.fElementQName);
            } else {
                this.fDocumentHandler.startElement(this.fElementQName, this.fAttributes, null);
            }
        }
        return bl;
    }

    protected void scanAttribute(XMLAttributes xMLAttributes) throws IOException, XNIException {
        if (this.fNamespaces) {
            this.fEntityScanner.scanQName(this.fAttributeQName);
        } else {
            String string = this.fEntityScanner.scanName();
            this.fAttributeQName.setValues(null, string, string, null);
        }
        this.fEntityScanner.skipSpaces();
        if (!this.fEntityScanner.skipChar(61)) {
            this.reportFatalError("EqRequiredInAttribute", new Object[]{this.fCurrentElement.rawname, this.fAttributeQName.rawname});
        }
        this.fEntityScanner.skipSpaces();
        int n2 = xMLAttributes.getLength();
        int n3 = xMLAttributes.addAttribute(this.fAttributeQName, XMLSymbols.fCDATASymbol, null);
        if (n2 == xMLAttributes.getLength()) {
            this.reportFatalError("AttributeNotUnique", new Object[]{this.fCurrentElement.rawname, this.fAttributeQName.rawname});
        }
        boolean bl = this.scanAttributeValue(this.fTempString, this.fTempString2, this.fAttributeQName.rawname, this.fIsEntityDeclaredVC, this.fCurrentElement.rawname);
        xMLAttributes.setValue(n3, this.fTempString.toString());
        if (!bl) {
            xMLAttributes.setNonNormalizedValue(n3, this.fTempString2.toString());
        }
        xMLAttributes.setSpecified(n3, true);
    }

    protected int scanContent() throws IOException, XNIException {
        XMLString xMLString = this.fTempString;
        int n2 = this.fEntityScanner.scanContent(xMLString);
        if (n2 == 13) {
            this.fEntityScanner.scanChar();
            this.fStringBuffer.clear();
            this.fStringBuffer.append(this.fTempString);
            this.fStringBuffer.append((char)n2);
            xMLString = this.fStringBuffer;
            n2 = -1;
        }
        if (this.fDocumentHandler != null && xMLString.length > 0) {
            this.fDocumentHandler.characters(xMLString, null);
        }
        if (n2 == 93 && this.fTempString.length == 0) {
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

    protected boolean scanCDATASection(boolean bl) throws IOException, XNIException {
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.startCDATA(null);
        }
        do {
            int n2;
            this.fStringBuffer.clear();
            if (!this.fEntityScanner.scanData("]]", this.fStringBuffer)) {
                if (this.fDocumentHandler != null && this.fStringBuffer.length > 0) {
                    this.fDocumentHandler.characters(this.fStringBuffer, null);
                }
                n2 = 0;
                while (this.fEntityScanner.skipChar(93)) {
                    ++n2;
                }
                if (this.fDocumentHandler != null && n2 > 0) {
                    int n3;
                    this.fStringBuffer.clear();
                    if (n2 > 2048) {
                        n3 = n2 / 2048;
                        int n4 = n2 % 2048;
                        int n5 = 0;
                        while (n5 < 2048) {
                            this.fStringBuffer.append(']');
                            ++n5;
                        }
                        int n6 = 0;
                        while (n6 < n3) {
                            this.fDocumentHandler.characters(this.fStringBuffer, null);
                            ++n6;
                        }
                        if (n4 != 0) {
                            this.fStringBuffer.length = n4;
                            this.fDocumentHandler.characters(this.fStringBuffer, null);
                        }
                    } else {
                        n3 = 0;
                        while (n3 < n2) {
                            this.fStringBuffer.append(']');
                            ++n3;
                        }
                        this.fDocumentHandler.characters(this.fStringBuffer, null);
                    }
                }
                if (this.fEntityScanner.skipChar(62)) break;
                if (this.fDocumentHandler == null) continue;
                this.fStringBuffer.clear();
                this.fStringBuffer.append("]]");
                this.fDocumentHandler.characters(this.fStringBuffer, null);
                continue;
            }
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.characters(this.fStringBuffer, null);
            }
            if ((n2 = this.fEntityScanner.peekChar()) == -1 || !this.isInvalidLiteral(n2)) continue;
            if (XMLChar.isHighSurrogate(n2)) {
                this.fStringBuffer.clear();
                this.scanSurrogates(this.fStringBuffer);
                if (this.fDocumentHandler == null) continue;
                this.fDocumentHandler.characters(this.fStringBuffer, null);
                continue;
            }
            this.reportFatalError("InvalidCharInCDSect", new Object[]{Integer.toString(n2, 16)});
            this.fEntityScanner.scanChar();
        } while (true);
        --this.fMarkupDepth;
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endCDATA(null);
        }
        return true;
    }

    protected int scanEndElement() throws IOException, XNIException {
        this.fElementStack.popElement(this.fElementQName);
        if (!this.fEntityScanner.skipString(this.fElementQName.rawname)) {
            this.reportFatalError("ETagRequired", new Object[]{this.fElementQName.rawname});
        }
        this.fEntityScanner.skipSpaces();
        if (!this.fEntityScanner.skipChar(62)) {
            this.reportFatalError("ETagUnterminated", new Object[]{this.fElementQName.rawname});
        }
        --this.fMarkupDepth;
        --this.fMarkupDepth;
        if (this.fMarkupDepth < this.fEntityStack[this.fEntityDepth - 1]) {
            this.reportFatalError("ElementEntityMismatch", new Object[]{this.fCurrentElement.rawname});
        }
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endElement(this.fElementQName, null);
        }
        return this.fMarkupDepth;
    }

    protected void scanCharReference() throws IOException, XNIException {
        this.fStringBuffer2.clear();
        int n2 = this.scanCharReferenceValue(this.fStringBuffer2, null);
        --this.fMarkupDepth;
        if (n2 != -1 && this.fDocumentHandler != null) {
            if (this.fNotifyCharRefs) {
                this.fDocumentHandler.startGeneralEntity(this.fCharRefLiteral, null, null, null);
            }
            Augmentations augmentations = null;
            if (this.fValidation && n2 <= 32) {
                if (this.fTempAugmentations != null) {
                    this.fTempAugmentations.removeAllItems();
                } else {
                    this.fTempAugmentations = new AugmentationsImpl();
                }
                augmentations = this.fTempAugmentations;
                augmentations.putItem("CHAR_REF_PROBABLE_WS", Boolean.TRUE);
            }
            this.fDocumentHandler.characters(this.fStringBuffer2, augmentations);
            if (this.fNotifyCharRefs) {
                this.fDocumentHandler.endGeneralEntity(this.fCharRefLiteral, null);
            }
        }
    }

    protected void scanEntityReference() throws IOException, XNIException {
        String string = this.fEntityScanner.scanName();
        if (string == null) {
            this.reportFatalError("NameRequiredInReference", null);
            return;
        }
        if (!this.fEntityScanner.skipChar(59)) {
            this.reportFatalError("SemicolonRequiredInReference", new Object[]{string});
        }
        --this.fMarkupDepth;
        if (string == XMLScanner.fAmpSymbol) {
            this.handleCharacter('&', XMLScanner.fAmpSymbol);
        } else if (string == XMLScanner.fLtSymbol) {
            this.handleCharacter('<', XMLScanner.fLtSymbol);
        } else if (string == XMLScanner.fGtSymbol) {
            this.handleCharacter('>', XMLScanner.fGtSymbol);
        } else if (string == XMLScanner.fQuotSymbol) {
            this.handleCharacter('\"', XMLScanner.fQuotSymbol);
        } else if (string == XMLScanner.fAposSymbol) {
            this.handleCharacter('\'', XMLScanner.fAposSymbol);
        } else if (this.fEntityManager.isUnparsedEntity(string)) {
            this.reportFatalError("ReferenceToUnparsedEntity", new Object[]{string});
        } else {
            if (!this.fEntityManager.isDeclaredEntity(string)) {
                if (this.fIsEntityDeclaredVC) {
                    if (this.fValidation) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EntityNotDeclared", new Object[]{string}, 1);
                    }
                } else {
                    this.reportFatalError("EntityNotDeclared", new Object[]{string});
                }
            }
            this.fEntityManager.startEntity(string, false);
        }
    }

    private void handleCharacter(char c2, String string) throws XNIException {
        if (this.fDocumentHandler != null) {
            if (this.fNotifyBuiltInRefs) {
                this.fDocumentHandler.startGeneralEntity(string, null, null, null);
            }
            this.fSingleChar[0] = c2;
            this.fTempString.setValues(this.fSingleChar, 0, 1);
            this.fDocumentHandler.characters(this.fTempString, null);
            if (this.fNotifyBuiltInRefs) {
                this.fDocumentHandler.endGeneralEntity(string, null);
            }
        }
    }

    protected int handleEndElement(QName qName, boolean bl) throws XNIException {
        --this.fMarkupDepth;
        if (this.fMarkupDepth < this.fEntityStack[this.fEntityDepth - 1]) {
            this.reportFatalError("ElementEntityMismatch", new Object[]{this.fCurrentElement.rawname});
        }
        QName qName2 = this.fQName;
        this.fElementStack.popElement(qName2);
        if (qName.rawname != qName2.rawname) {
            this.reportFatalError("ETagRequired", new Object[]{qName2.rawname});
        }
        if (this.fNamespaces) {
            qName.uri = qName2.uri;
        }
        if (this.fDocumentHandler != null && !bl) {
            this.fDocumentHandler.endElement(qName, null);
        }
        return this.fMarkupDepth;
    }

    protected final void setScannerState(int n2) {
        this.fScannerState = n2;
    }

    protected final void setDispatcher(Dispatcher dispatcher) {
        this.fDispatcher = dispatcher;
    }

    protected String getScannerStateName(int n2) {
        switch (n2) {
            case 4: {
                return "SCANNER_STATE_DOCTYPE";
            }
            case 6: {
                return "SCANNER_STATE_ROOT_ELEMENT";
            }
            case 1: {
                return "SCANNER_STATE_START_OF_MARKUP";
            }
            case 2: {
                return "SCANNER_STATE_COMMENT";
            }
            case 3: {
                return "SCANNER_STATE_PI";
            }
            case 7: {
                return "SCANNER_STATE_CONTENT";
            }
            case 8: {
                return "SCANNER_STATE_REFERENCE";
            }
            case 13: {
                return "SCANNER_STATE_END_OF_INPUT";
            }
            case 14: {
                return "SCANNER_STATE_TERMINATED";
            }
            case 15: {
                return "SCANNER_STATE_CDATA";
            }
            case 16: {
                return "SCANNER_STATE_TEXT_DECL";
            }
        }
        return "??? (" + n2 + ')';
    }

    public String getDispatcherName(Dispatcher dispatcher) {
        return "null";
    }

    static XMLStringBuffer access$000(XMLDocumentFragmentScannerImpl xMLDocumentFragmentScannerImpl) {
        return xMLDocumentFragmentScannerImpl.fStringBuffer;
    }

    protected class FragmentContentDispatcher
    implements Dispatcher {
        private final XMLDocumentFragmentScannerImpl this$0;

        protected FragmentContentDispatcher(XMLDocumentFragmentScannerImpl xMLDocumentFragmentScannerImpl) {
            this.this$0 = xMLDocumentFragmentScannerImpl;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        public boolean dispatch(boolean var1_1) throws IOException, XNIException {
            try {
                do {
                    var2_2 = false;
                    block1 : switch (this.this$0.fScannerState) {
                        case 7: {
                            if (this.this$0.fEntityScanner.skipChar(60)) {
                                this.this$0.setScannerState(1);
                                var2_2 = true;
                                break;
                            }
                            if (this.this$0.fEntityScanner.skipChar(38)) {
                                this.this$0.setScannerState(8);
                                var2_2 = true;
                                break;
                            }
                            do {
                                if ((var3_4 = this.this$0.scanContent()) == 60) {
                                    this.this$0.fEntityScanner.scanChar();
                                    this.this$0.setScannerState(1);
                                    break block1;
                                }
                                if (var3_4 == 38) {
                                    this.this$0.fEntityScanner.scanChar();
                                    this.this$0.setScannerState(8);
                                    break block1;
                                }
                                if (var3_4 == -1 || !this.this$0.isInvalidLiteral(var3_4)) continue;
                                if (XMLChar.isHighSurrogate(var3_4)) {
                                    XMLDocumentFragmentScannerImpl.access$000(this.this$0).clear();
                                    if (!this.this$0.scanSurrogates(XMLDocumentFragmentScannerImpl.access$000(this.this$0)) || this.this$0.fDocumentHandler == null) continue;
                                    this.this$0.fDocumentHandler.characters(XMLDocumentFragmentScannerImpl.access$000(this.this$0), null);
                                    continue;
                                }
                                this.this$0.reportFatalError("InvalidCharInContent", new Object[]{Integer.toString(var3_4, 16)});
                                this.this$0.fEntityScanner.scanChar();
                            } while (var1_1);
                            break;
                        }
                        case 1: {
                            ++this.this$0.fMarkupDepth;
                            if (this.this$0.fEntityScanner.skipChar(47)) {
                                if (this.this$0.scanEndElement() == 0 && this.elementDepthIsZeroHook()) {
                                    return true;
                                }
                                this.this$0.setScannerState(7);
                                break;
                            }
                            if (this.this$0.isValidNameStartChar(this.this$0.fEntityScanner.peekChar())) {
                                this.this$0.scanStartElement();
                                this.this$0.setScannerState(7);
                                break;
                            }
                            if (this.this$0.fEntityScanner.skipChar(33)) {
                                if (this.this$0.fEntityScanner.skipChar(45)) {
                                    if (!this.this$0.fEntityScanner.skipChar(45)) {
                                        this.this$0.reportFatalError("InvalidCommentStart", null);
                                    }
                                    this.this$0.setScannerState(2);
                                    var2_2 = true;
                                    break;
                                }
                                if (this.this$0.fEntityScanner.skipString("[CDATA[")) {
                                    this.this$0.setScannerState(15);
                                    var2_2 = true;
                                    break;
                                }
                                if (this.scanForDoctypeHook()) break;
                                this.this$0.reportFatalError("MarkupNotRecognizedInContent", null);
                                break;
                            }
                            if (this.this$0.fEntityScanner.skipChar(63)) {
                                this.this$0.setScannerState(3);
                                var2_2 = true;
                                break;
                            }
                            if (this.this$0.isValidNameStartHighSurrogate(this.this$0.fEntityScanner.peekChar())) {
                                this.this$0.scanStartElement();
                                this.this$0.setScannerState(7);
                                break;
                            }
                            this.this$0.reportFatalError("MarkupNotRecognizedInContent", null);
                            this.this$0.setScannerState(7);
                            break;
                        }
                        case 2: {
                            this.this$0.scanComment();
                            this.this$0.setScannerState(7);
                            break;
                        }
                        case 3: {
                            this.this$0.scanPI();
                            this.this$0.setScannerState(7);
                            break;
                        }
                        case 15: {
                            this.this$0.scanCDATASection(var1_1);
                            this.this$0.setScannerState(7);
                            break;
                        }
                        case 8: {
                            ++this.this$0.fMarkupDepth;
                            this.this$0.setScannerState(7);
                            if (this.this$0.fEntityScanner.skipChar(35)) {
                                this.this$0.scanCharReference();
                                break;
                            }
                            this.this$0.scanEntityReference();
                            break;
                        }
                        case 16: {
                            if (!this.this$0.fEntityScanner.skipString("<?xml")) ** GOTO lbl98
                            ++this.this$0.fMarkupDepth;
                            if (!this.this$0.isValidNameChar(this.this$0.fEntityScanner.peekChar())) ** GOTO lbl97
                            XMLDocumentFragmentScannerImpl.access$000(this.this$0).clear();
                            XMLDocumentFragmentScannerImpl.access$000(this.this$0).append("xml");
                            if (!this.this$0.fNamespaces) ** GOTO lbl124
                            ** GOTO lbl121
lbl97: // 1 sources:
                            this.this$0.scanXMLDeclOrTextDecl(true);
lbl98: // 3 sources:
                            this.this$0.fEntityManager.fCurrentEntity.mayReadChunks = true;
                            this.this$0.setScannerState(7);
                            break;
                        }
                        case 6: {
                            if (this.scanRootElementHook()) {
                                return true;
                            }
                            this.this$0.setScannerState(7);
                            break;
                        }
                        case 4: {
                            this.this$0.reportFatalError("DoctypeIllegalInContent", null);
                            this.this$0.setScannerState(7);
                        }
                    }
                } while (var1_1 || var2_2);
                return true;
            }
            catch (MalformedByteSequenceException var2_3) {
                this.this$0.fErrorReporter.reportError(var2_3.getDomain(), var2_3.getKey(), var2_3.getArguments(), 2, var2_3);
                return false;
            }
            catch (CharConversionException var3_6) {
                this.this$0.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "CharConversionFailure", null, 2, var3_6);
                return false;
            }
            catch (EOFException var4_7) {
                this.endOfFileHook(var4_7);
                return false;
            }
lbl-1000: // 1 sources:
            {
                XMLDocumentFragmentScannerImpl.access$000(this.this$0).append((char)this.this$0.fEntityScanner.scanChar());
lbl121: // 2 sources:
                ** while (this.this$0.isValidNCName((int)this.this$0.fEntityScanner.peekChar()))
            }
lbl122: // 1 sources:
            ** GOTO lbl125
lbl-1000: // 1 sources:
            {
                XMLDocumentFragmentScannerImpl.access$000(this.this$0).append((char)this.this$0.fEntityScanner.scanChar());
lbl124: // 2 sources:
                ** while (this.this$0.isValidNameChar((int)this.this$0.fEntityScanner.peekChar()))
            }
lbl125: // 2 sources:
            var3_5 = this.this$0.fSymbolTable.addSymbol(XMLDocumentFragmentScannerImpl.access$000((XMLDocumentFragmentScannerImpl)this.this$0).ch, XMLDocumentFragmentScannerImpl.access$000((XMLDocumentFragmentScannerImpl)this.this$0).offset, XMLDocumentFragmentScannerImpl.access$000((XMLDocumentFragmentScannerImpl)this.this$0).length);
            this.this$0.scanPIData(var3_5, this.this$0.fTempString);
            ** GOTO lbl98
        }

        protected boolean scanForDoctypeHook() throws IOException, XNIException {
            return false;
        }

        protected boolean elementDepthIsZeroHook() throws IOException, XNIException {
            return false;
        }

        protected boolean scanRootElementHook() throws IOException, XNIException {
            return false;
        }

        protected void endOfFileHook(EOFException eOFException) throws IOException, XNIException {
            if (this.this$0.fMarkupDepth != 0) {
                this.this$0.reportFatalError("PrematureEOF", null);
            }
        }
    }

    protected static interface Dispatcher {
        public boolean dispatch(boolean var1) throws IOException, XNIException;
    }

    protected static class ElementStack {
        protected QName[] fElements = new QName[10];
        protected int fSize;

        public ElementStack() {
            int n2 = 0;
            while (n2 < this.fElements.length) {
                this.fElements[n2] = new QName();
                ++n2;
            }
        }

        public QName pushElement(QName qName) {
            if (this.fSize == this.fElements.length) {
                QName[] arrqName = new QName[this.fElements.length * 2];
                System.arraycopy(this.fElements, 0, arrqName, 0, this.fSize);
                this.fElements = arrqName;
                int n2 = this.fSize;
                while (n2 < this.fElements.length) {
                    this.fElements[n2] = new QName();
                    ++n2;
                }
            }
            this.fElements[this.fSize].setValues(qName);
            return this.fElements[this.fSize++];
        }

        public void popElement(QName qName) {
            qName.setValues(this.fElements[--this.fSize]);
        }

        public void clear() {
            this.fSize = 0;
        }
    }

}

