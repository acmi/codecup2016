/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl;

import java.io.CharConversionException;
import java.io.EOFException;
import java.io.IOException;
import org.apache.xerces.impl.ExternalSubsetResolver;
import org.apache.xerces.impl.XMLDocumentFragmentScannerImpl;
import org.apache.xerces.impl.XMLEntityHandler;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLEntityScanner;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.dtd.XMLDTDDescription;
import org.apache.xerces.impl.io.MalformedByteSequenceException;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDTDScanner;
import org.apache.xerces.xni.parser.XMLInputSource;

public class XMLDocumentScannerImpl
extends XMLDocumentFragmentScannerImpl {
    protected static final int SCANNER_STATE_XML_DECL = 0;
    protected static final int SCANNER_STATE_PROLOG = 5;
    protected static final int SCANNER_STATE_TRAILING_MISC = 12;
    protected static final int SCANNER_STATE_DTD_INTERNAL_DECLS = 17;
    protected static final int SCANNER_STATE_DTD_EXTERNAL = 18;
    protected static final int SCANNER_STATE_DTD_EXTERNAL_DECLS = 19;
    protected static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
    protected static final String DISALLOW_DOCTYPE_DECL_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
    protected static final String DTD_SCANNER = "http://apache.org/xml/properties/internal/dtd-scanner";
    protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
    protected static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
    private static final String[] RECOGNIZED_FEATURES = new String[]{"http://apache.org/xml/features/nonvalidating/load-external-dtd", "http://apache.org/xml/features/disallow-doctype-decl"};
    private static final Boolean[] FEATURE_DEFAULTS = new Boolean[]{Boolean.TRUE, Boolean.FALSE};
    private static final String[] RECOGNIZED_PROPERTIES = new String[]{"http://apache.org/xml/properties/internal/dtd-scanner", "http://apache.org/xml/properties/internal/validation-manager", "http://apache.org/xml/properties/internal/namespace-context"};
    private static final Object[] PROPERTY_DEFAULTS = new Object[]{null, null, null};
    protected XMLDTDScanner fDTDScanner;
    protected ValidationManager fValidationManager;
    protected boolean fScanningDTD;
    protected String fDoctypeName;
    protected String fDoctypePublicId;
    protected String fDoctypeSystemId;
    protected NamespaceContext fNamespaceContext = new NamespaceSupport();
    protected boolean fLoadExternalDTD = true;
    protected boolean fDisallowDoctype = false;
    protected boolean fSeenDoctypeDecl;
    protected final XMLDocumentFragmentScannerImpl.Dispatcher fXMLDeclDispatcher;
    protected final XMLDocumentFragmentScannerImpl.Dispatcher fPrologDispatcher;
    protected final XMLDocumentFragmentScannerImpl.Dispatcher fDTDDispatcher;
    protected final XMLDocumentFragmentScannerImpl.Dispatcher fTrailingMiscDispatcher;
    private final String[] fStrings;
    private final XMLString fString;
    private final XMLStringBuffer fStringBuffer;
    private XMLInputSource fExternalSubsetSource;
    private final XMLDTDDescription fDTDDescription;

    public XMLDocumentScannerImpl() {
        this.fXMLDeclDispatcher = new XMLDeclDispatcher(this);
        this.fPrologDispatcher = new PrologDispatcher(this);
        this.fDTDDispatcher = new DTDDispatcher(this);
        this.fTrailingMiscDispatcher = new TrailingMiscDispatcher(this);
        this.fStrings = new String[3];
        this.fString = new XMLString();
        this.fStringBuffer = new XMLStringBuffer();
        this.fExternalSubsetSource = null;
        this.fDTDDescription = new XMLDTDDescription(null, null, null, null, null);
    }

    public void setInputSource(XMLInputSource xMLInputSource) throws IOException {
        this.fEntityManager.setEntityHandler(this);
        this.fEntityManager.startDocumentEntity(xMLInputSource);
    }

    public void reset(XMLComponentManager xMLComponentManager) throws XMLConfigurationException {
        super.reset(xMLComponentManager);
        this.fDoctypeName = null;
        this.fDoctypePublicId = null;
        this.fDoctypeSystemId = null;
        this.fSeenDoctypeDecl = false;
        this.fScanningDTD = false;
        this.fExternalSubsetSource = null;
        if (!this.fParserSettings) {
            this.fNamespaceContext.reset();
            this.setScannerState(0);
            this.setDispatcher(this.fXMLDeclDispatcher);
            return;
        }
        try {
            this.fLoadExternalDTD = xMLComponentManager.getFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fLoadExternalDTD = true;
        }
        try {
            this.fDisallowDoctype = xMLComponentManager.getFeature("http://apache.org/xml/features/disallow-doctype-decl");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fDisallowDoctype = false;
        }
        this.fDTDScanner = (XMLDTDScanner)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/dtd-scanner");
        try {
            this.fValidationManager = (ValidationManager)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fValidationManager = null;
        }
        try {
            this.fNamespaceContext = (NamespaceContext)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/namespace-context");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            // empty catch block
        }
        if (this.fNamespaceContext == null) {
            this.fNamespaceContext = new NamespaceSupport();
        }
        this.fNamespaceContext.reset();
        this.setScannerState(0);
        this.setDispatcher(this.fXMLDeclDispatcher);
    }

    public String[] getRecognizedFeatures() {
        String[] arrstring = super.getRecognizedFeatures();
        int n2 = arrstring != null ? arrstring.length : 0;
        String[] arrstring2 = new String[n2 + RECOGNIZED_FEATURES.length];
        if (arrstring != null) {
            System.arraycopy(arrstring, 0, arrstring2, 0, arrstring.length);
        }
        System.arraycopy(RECOGNIZED_FEATURES, 0, arrstring2, n2, RECOGNIZED_FEATURES.length);
        return arrstring2;
    }

    public void setFeature(String string, boolean bl) throws XMLConfigurationException {
        super.setFeature(string, bl);
        if (string.startsWith("http://apache.org/xml/features/")) {
            int n2 = string.length() - "http://apache.org/xml/features/".length();
            if (n2 == "nonvalidating/load-external-dtd".length() && string.endsWith("nonvalidating/load-external-dtd")) {
                this.fLoadExternalDTD = bl;
                return;
            }
            if (n2 == "disallow-doctype-decl".length() && string.endsWith("disallow-doctype-decl")) {
                this.fDisallowDoctype = bl;
                return;
            }
        }
    }

    public String[] getRecognizedProperties() {
        String[] arrstring = super.getRecognizedProperties();
        int n2 = arrstring != null ? arrstring.length : 0;
        String[] arrstring2 = new String[n2 + RECOGNIZED_PROPERTIES.length];
        if (arrstring != null) {
            System.arraycopy(arrstring, 0, arrstring2, 0, arrstring.length);
        }
        System.arraycopy(RECOGNIZED_PROPERTIES, 0, arrstring2, n2, RECOGNIZED_PROPERTIES.length);
        return arrstring2;
    }

    public void setProperty(String string, Object object) throws XMLConfigurationException {
        super.setProperty(string, object);
        if (string.startsWith("http://apache.org/xml/properties/")) {
            int n2 = string.length() - "http://apache.org/xml/properties/".length();
            if (n2 == "internal/dtd-scanner".length() && string.endsWith("internal/dtd-scanner")) {
                this.fDTDScanner = (XMLDTDScanner)object;
            }
            if (n2 == "internal/namespace-context".length() && string.endsWith("internal/namespace-context") && object != null) {
                this.fNamespaceContext = (NamespaceContext)object;
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
        return super.getFeatureDefault(string);
    }

    public Object getPropertyDefault(String string) {
        int n2 = 0;
        while (n2 < RECOGNIZED_PROPERTIES.length) {
            if (RECOGNIZED_PROPERTIES[n2].equals(string)) {
                return PROPERTY_DEFAULTS[n2];
            }
            ++n2;
        }
        return super.getPropertyDefault(string);
    }

    public void startEntity(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
        super.startEntity(string, xMLResourceIdentifier, string2, augmentations);
        if (!string.equals("[xml]") && this.fEntityScanner.isExternal()) {
            this.setScannerState(16);
        }
        if (this.fDocumentHandler != null && string.equals("[xml]")) {
            this.fDocumentHandler.startDocument(this.fEntityScanner, string2, this.fNamespaceContext, null);
        }
    }

    public void endEntity(String string, Augmentations augmentations) throws XNIException {
        super.endEntity(string, augmentations);
        if (this.fDocumentHandler != null && string.equals("[xml]")) {
            this.fDocumentHandler.endDocument(null);
        }
    }

    protected XMLDocumentFragmentScannerImpl.Dispatcher createContentDispatcher() {
        return new ContentDispatcher(this);
    }

    protected boolean scanDoctypeDecl() throws IOException, XNIException {
        if (!this.fEntityScanner.skipSpaces()) {
            this.reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ROOT_ELEMENT_TYPE_IN_DOCTYPEDECL", null);
        }
        this.fDoctypeName = this.fEntityScanner.scanName();
        if (this.fDoctypeName == null) {
            this.reportFatalError("MSG_ROOT_ELEMENT_TYPE_REQUIRED", null);
        }
        if (this.fEntityScanner.skipSpaces()) {
            this.scanExternalID(this.fStrings, false);
            this.fDoctypeSystemId = this.fStrings[0];
            this.fDoctypePublicId = this.fStrings[1];
            this.fEntityScanner.skipSpaces();
        }
        boolean bl = this.fHasExternalDTD = this.fDoctypeSystemId != null;
        if (!this.fHasExternalDTD && this.fExternalSubsetResolver != null) {
            this.fDTDDescription.setValues(null, null, this.fEntityManager.getCurrentResourceIdentifier().getExpandedSystemId(), null);
            this.fDTDDescription.setRootName(this.fDoctypeName);
            this.fExternalSubsetSource = this.fExternalSubsetResolver.getExternalSubset(this.fDTDDescription);
            boolean bl2 = this.fHasExternalDTD = this.fExternalSubsetSource != null;
        }
        if (this.fDocumentHandler != null) {
            if (this.fExternalSubsetSource == null) {
                this.fDocumentHandler.doctypeDecl(this.fDoctypeName, this.fDoctypePublicId, this.fDoctypeSystemId, null);
            } else {
                this.fDocumentHandler.doctypeDecl(this.fDoctypeName, this.fExternalSubsetSource.getPublicId(), this.fExternalSubsetSource.getSystemId(), null);
            }
        }
        boolean bl3 = true;
        if (!this.fEntityScanner.skipChar(91)) {
            bl3 = false;
            this.fEntityScanner.skipSpaces();
            if (!this.fEntityScanner.skipChar(62)) {
                this.reportFatalError("DoctypedeclUnterminated", new Object[]{this.fDoctypeName});
            }
            --this.fMarkupDepth;
        }
        return bl3;
    }

    protected String getScannerStateName(int n2) {
        switch (n2) {
            case 0: {
                return "SCANNER_STATE_XML_DECL";
            }
            case 5: {
                return "SCANNER_STATE_PROLOG";
            }
            case 12: {
                return "SCANNER_STATE_TRAILING_MISC";
            }
            case 17: {
                return "SCANNER_STATE_DTD_INTERNAL_DECLS";
            }
            case 18: {
                return "SCANNER_STATE_DTD_EXTERNAL";
            }
            case 19: {
                return "SCANNER_STATE_DTD_EXTERNAL_DECLS";
            }
        }
        return super.getScannerStateName(n2);
    }

    static XMLStringBuffer access$000(XMLDocumentScannerImpl xMLDocumentScannerImpl) {
        return xMLDocumentScannerImpl.fStringBuffer;
    }

    static XMLString access$100(XMLDocumentScannerImpl xMLDocumentScannerImpl) {
        return xMLDocumentScannerImpl.fString;
    }

    static XMLInputSource access$200(XMLDocumentScannerImpl xMLDocumentScannerImpl) {
        return xMLDocumentScannerImpl.fExternalSubsetSource;
    }

    static XMLInputSource access$202(XMLDocumentScannerImpl xMLDocumentScannerImpl, XMLInputSource xMLInputSource) {
        xMLDocumentScannerImpl.fExternalSubsetSource = xMLInputSource;
        return xMLDocumentScannerImpl.fExternalSubsetSource;
    }

    static XMLDTDDescription access$300(XMLDocumentScannerImpl xMLDocumentScannerImpl) {
        return xMLDocumentScannerImpl.fDTDDescription;
    }

    protected final class TrailingMiscDispatcher
    implements XMLDocumentFragmentScannerImpl.Dispatcher {
        private final XMLDocumentScannerImpl this$0;

        protected TrailingMiscDispatcher(XMLDocumentScannerImpl xMLDocumentScannerImpl) {
            this.this$0 = xMLDocumentScannerImpl;
        }

        public boolean dispatch(boolean bl) throws IOException, XNIException {
            try {
                boolean bl2;
                do {
                    bl2 = false;
                    switch (this.this$0.fScannerState) {
                        case 12: {
                            this.this$0.fEntityScanner.skipSpaces();
                            if (this.this$0.fEntityScanner.skipChar(60)) {
                                this.this$0.setScannerState(1);
                                bl2 = true;
                                break;
                            }
                            this.this$0.setScannerState(7);
                            bl2 = true;
                            break;
                        }
                        case 1: {
                            ++this.this$0.fMarkupDepth;
                            if (this.this$0.fEntityScanner.skipChar(63)) {
                                this.this$0.setScannerState(3);
                                bl2 = true;
                                break;
                            }
                            if (this.this$0.fEntityScanner.skipChar(33)) {
                                this.this$0.setScannerState(2);
                                bl2 = true;
                                break;
                            }
                            if (this.this$0.fEntityScanner.skipChar(47)) {
                                this.this$0.reportFatalError("MarkupNotRecognizedInMisc", null);
                                bl2 = true;
                                break;
                            }
                            if (this.this$0.isValidNameStartChar(this.this$0.fEntityScanner.peekChar())) {
                                this.this$0.reportFatalError("MarkupNotRecognizedInMisc", null);
                                this.this$0.scanStartElement();
                                this.this$0.setScannerState(7);
                                break;
                            }
                            if (this.this$0.isValidNameStartHighSurrogate(this.this$0.fEntityScanner.peekChar())) {
                                this.this$0.reportFatalError("MarkupNotRecognizedInMisc", null);
                                this.this$0.scanStartElement();
                                this.this$0.setScannerState(7);
                                break;
                            }
                            this.this$0.reportFatalError("MarkupNotRecognizedInMisc", null);
                            break;
                        }
                        case 3: {
                            this.this$0.scanPI();
                            this.this$0.setScannerState(12);
                            break;
                        }
                        case 2: {
                            if (!this.this$0.fEntityScanner.skipString("--")) {
                                this.this$0.reportFatalError("InvalidCommentStart", null);
                            }
                            this.this$0.scanComment();
                            this.this$0.setScannerState(12);
                            break;
                        }
                        case 7: {
                            int n2 = this.this$0.fEntityScanner.peekChar();
                            if (n2 == -1) {
                                this.this$0.setScannerState(14);
                                return false;
                            }
                            this.this$0.reportFatalError("ContentIllegalInTrailingMisc", null);
                            this.this$0.fEntityScanner.scanChar();
                            this.this$0.setScannerState(12);
                            break;
                        }
                        case 8: {
                            this.this$0.reportFatalError("ReferenceIllegalInTrailingMisc", null);
                            this.this$0.setScannerState(12);
                            break;
                        }
                        case 14: {
                            return false;
                        }
                    }
                } while (bl || bl2);
            }
            catch (MalformedByteSequenceException malformedByteSequenceException) {
                this.this$0.fErrorReporter.reportError(malformedByteSequenceException.getDomain(), malformedByteSequenceException.getKey(), malformedByteSequenceException.getArguments(), 2, malformedByteSequenceException);
                return false;
            }
            catch (CharConversionException charConversionException) {
                this.this$0.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "CharConversionFailure", null, 2, charConversionException);
                return false;
            }
            catch (EOFException eOFException) {
                if (this.this$0.fMarkupDepth != 0) {
                    this.this$0.reportFatalError("PrematureEOF", null);
                    return false;
                }
                this.this$0.setScannerState(14);
                return false;
            }
            return true;
        }
    }

    protected class ContentDispatcher
    extends XMLDocumentFragmentScannerImpl.FragmentContentDispatcher {
        private final XMLDocumentScannerImpl this$0;

        protected ContentDispatcher(XMLDocumentScannerImpl xMLDocumentScannerImpl) {
            super(xMLDocumentScannerImpl);
            this.this$0 = xMLDocumentScannerImpl;
        }

        protected boolean scanForDoctypeHook() throws IOException, XNIException {
            if (this.this$0.fEntityScanner.skipString("DOCTYPE")) {
                this.this$0.setScannerState(4);
                return true;
            }
            return false;
        }

        protected boolean elementDepthIsZeroHook() throws IOException, XNIException {
            this.this$0.setScannerState(12);
            this.this$0.setDispatcher(this.this$0.fTrailingMiscDispatcher);
            return true;
        }

        protected boolean scanRootElementHook() throws IOException, XNIException {
            if (this.this$0.fExternalSubsetResolver != null && !this.this$0.fSeenDoctypeDecl && !this.this$0.fDisallowDoctype && (this.this$0.fValidation || this.this$0.fLoadExternalDTD)) {
                this.this$0.scanStartElementName();
                this.resolveExternalSubsetAndRead();
                if (this.this$0.scanStartElementAfterName()) {
                    this.this$0.setScannerState(12);
                    this.this$0.setDispatcher(this.this$0.fTrailingMiscDispatcher);
                    return true;
                }
            } else if (this.this$0.scanStartElement()) {
                this.this$0.setScannerState(12);
                this.this$0.setDispatcher(this.this$0.fTrailingMiscDispatcher);
                return true;
            }
            return false;
        }

        protected void endOfFileHook(EOFException eOFException) throws IOException, XNIException {
            this.this$0.reportFatalError("PrematureEOF", null);
        }

        protected void resolveExternalSubsetAndRead() throws IOException, XNIException {
            XMLDocumentScannerImpl.access$300(this.this$0).setValues(null, null, this.this$0.fEntityManager.getCurrentResourceIdentifier().getExpandedSystemId(), null);
            XMLDocumentScannerImpl.access$300(this.this$0).setRootName(this.this$0.fElementQName.rawname);
            XMLInputSource xMLInputSource = this.this$0.fExternalSubsetResolver.getExternalSubset(XMLDocumentScannerImpl.access$300(this.this$0));
            if (xMLInputSource != null) {
                this.this$0.fDoctypeName = this.this$0.fElementQName.rawname;
                this.this$0.fDoctypePublicId = xMLInputSource.getPublicId();
                this.this$0.fDoctypeSystemId = xMLInputSource.getSystemId();
                if (this.this$0.fDocumentHandler != null) {
                    this.this$0.fDocumentHandler.doctypeDecl(this.this$0.fDoctypeName, this.this$0.fDoctypePublicId, this.this$0.fDoctypeSystemId, null);
                }
                try {
                    if (this.this$0.fValidationManager == null || !this.this$0.fValidationManager.isCachedDTD()) {
                        this.this$0.fDTDScanner.setInputSource(xMLInputSource);
                        while (this.this$0.fDTDScanner.scanDTDExternalSubset(true)) {
                        }
                    } else {
                        this.this$0.fDTDScanner.setInputSource(null);
                    }
                    Object var3_2 = null;
                    this.this$0.fEntityManager.setEntityHandler(this.this$0);
                }
                catch (Throwable throwable) {
                    Object var3_3 = null;
                    this.this$0.fEntityManager.setEntityHandler(this.this$0);
                    throw throwable;
                }
            }
        }
    }

    protected final class DTDDispatcher
    implements XMLDocumentFragmentScannerImpl.Dispatcher {
        private final XMLDocumentScannerImpl this$0;

        protected DTDDispatcher(XMLDocumentScannerImpl xMLDocumentScannerImpl) {
            this.this$0 = xMLDocumentScannerImpl;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        public boolean dispatch(boolean var1_1) throws IOException, XNIException {
            block20 : {
                this.this$0.fEntityManager.setEntityHandler(null);
                try {
                    ** try [egrp 1[TRYBLOCK] [0, 1, 2 : 11->663)] { 
lbl4: // 1 sources:
                    break block20;
lbl5: // 1 sources:
                    catch (MalformedByteSequenceException var2_3) {
                        this.this$0.fErrorReporter.reportError(var2_3.getDomain(), var2_3.getKey(), var2_3.getArguments(), 2, var2_3);
                        var3_6 = false;
                        var8_17 = null;
                        this.this$0.fEntityManager.setEntityHandler(this.this$0);
                        return var3_6;
                    }
                }
                catch (Throwable var7_21) {
                    var8_20 = null;
                    this.this$0.fEntityManager.setEntityHandler(this.this$0);
                    throw var7_21;
                }
            }
            block11 : do {
                var2_2 = false;
                switch (this.this$0.fScannerState) {
                    case 17: {
                        var3_4 = true;
                        var4_8 = !(this.this$0.fValidation == false && this.this$0.fLoadExternalDTD == false || this.this$0.fValidationManager != null && this.this$0.fValidationManager.isCachedDTD() != false);
                        var5_11 = this.this$0.fDTDScanner.scanDTDInternalSubset(var3_4, this.this$0.fStandalone, this.this$0.fHasExternalDTD != false && var4_8 != false);
                        if (var5_11) continue block11;
                        if (!this.this$0.fEntityScanner.skipChar(93)) {
                            this.this$0.reportFatalError("EXPECTED_SQUARE_BRACKET_TO_CLOSE_INTERNAL_SUBSET", null);
                        }
                        this.this$0.fEntityScanner.skipSpaces();
                        if (!this.this$0.fEntityScanner.skipChar(62)) {
                            this.this$0.reportFatalError("DoctypedeclUnterminated", new Object[]{this.this$0.fDoctypeName});
                        }
                        --this.this$0.fMarkupDepth;
                        if (this.this$0.fDoctypeSystemId != null) {
                            v0 = this.this$0.fIsEntityDeclaredVC = this.this$0.fStandalone == false;
                            if (var4_8) {
                                this.this$0.setScannerState(18);
                                break;
                            }
                        } else if (XMLDocumentScannerImpl.access$200(this.this$0) != null) {
                            v1 = this.this$0.fIsEntityDeclaredVC = this.this$0.fStandalone == false;
                            if (var4_8) {
                                this.this$0.fDTDScanner.setInputSource(XMLDocumentScannerImpl.access$200(this.this$0));
                                XMLDocumentScannerImpl.access$202(this.this$0, null);
                                this.this$0.setScannerState(19);
                                break;
                            }
                        } else {
                            this.this$0.fIsEntityDeclaredVC = this.this$0.fEntityManager.hasPEReferences() != false && this.this$0.fStandalone == false;
                        }
                        this.this$0.setScannerState(5);
                        this.this$0.setDispatcher(this.this$0.fPrologDispatcher);
                        this.this$0.fEntityManager.setEntityHandler(this.this$0);
                        var6_13 = true;
                        var8_14 = null;
                        this.this$0.fEntityManager.setEntityHandler(this.this$0);
                        return var6_13;
                    }
                    case 18: {
                        XMLDocumentScannerImpl.access$300(this.this$0).setValues(this.this$0.fDoctypePublicId, this.this$0.fDoctypeSystemId, null, null);
                        XMLDocumentScannerImpl.access$300(this.this$0).setRootName(this.this$0.fDoctypeName);
                        var3_5 = this.this$0.fEntityManager.resolveEntity(XMLDocumentScannerImpl.access$300(this.this$0));
                        this.this$0.fDTDScanner.setInputSource(var3_5);
                        this.this$0.setScannerState(19);
                        var2_2 = true;
                        break;
                    }
                    case 19: {
                        var3_4 = true;
                        var4_8 = this.this$0.fDTDScanner.scanDTDExternalSubset(var3_4);
                        if (var4_8) continue block11;
                        this.this$0.setScannerState(5);
                        this.this$0.setDispatcher(this.this$0.fPrologDispatcher);
                        this.this$0.fEntityManager.setEntityHandler(this.this$0);
                        var5_11 = true;
                        var8_15 = null;
                        this.this$0.fEntityManager.setEntityHandler(this.this$0);
                        return var5_11;
                    }
                    default: {
                        throw new XNIException("DTDDispatcher#dispatch: scanner state=" + this.this$0.fScannerState + " (" + this.this$0.getScannerStateName(this.this$0.fScannerState) + ')');
                    }
                }
            } while (var1_1 || var2_2);
            var8_16 = null;
            this.this$0.fEntityManager.setEntityHandler(this.this$0);
            return true;
lbl82: // 1 sources:
            catch (CharConversionException var3_7) {
                this.this$0.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "CharConversionFailure", null, 2, var3_7);
                var4_9 = false;
                var8_18 = null;
                this.this$0.fEntityManager.setEntityHandler(this.this$0);
                return var4_9;
            }
lbl89: // 1 sources:
            catch (EOFException var4_10) {
                this.this$0.reportFatalError("PrematureEOF", null);
                var5_12 = false;
                var8_19 = null;
                this.this$0.fEntityManager.setEntityHandler(this.this$0);
                return var5_12;
            }
        }
    }

    protected final class PrologDispatcher
    implements XMLDocumentFragmentScannerImpl.Dispatcher {
        private final XMLDocumentScannerImpl this$0;

        protected PrologDispatcher(XMLDocumentScannerImpl xMLDocumentScannerImpl) {
            this.this$0 = xMLDocumentScannerImpl;
        }

        public boolean dispatch(boolean bl) throws IOException, XNIException {
            try {
                boolean bl2;
                do {
                    bl2 = false;
                    switch (this.this$0.fScannerState) {
                        case 5: {
                            this.this$0.fEntityScanner.skipSpaces();
                            if (this.this$0.fEntityScanner.skipChar(60)) {
                                this.this$0.setScannerState(1);
                                bl2 = true;
                                break;
                            }
                            if (this.this$0.fEntityScanner.skipChar(38)) {
                                this.this$0.setScannerState(8);
                                bl2 = true;
                                break;
                            }
                            this.this$0.setScannerState(7);
                            bl2 = true;
                            break;
                        }
                        case 1: {
                            ++this.this$0.fMarkupDepth;
                            if (this.this$0.fEntityScanner.skipChar(33)) {
                                if (this.this$0.fEntityScanner.skipChar(45)) {
                                    if (!this.this$0.fEntityScanner.skipChar(45)) {
                                        this.this$0.reportFatalError("InvalidCommentStart", null);
                                    }
                                    this.this$0.setScannerState(2);
                                    bl2 = true;
                                    break;
                                }
                                if (this.this$0.fEntityScanner.skipString("DOCTYPE")) {
                                    this.this$0.setScannerState(4);
                                    bl2 = true;
                                    break;
                                }
                                this.this$0.reportFatalError("MarkupNotRecognizedInProlog", null);
                                break;
                            }
                            if (this.this$0.isValidNameStartChar(this.this$0.fEntityScanner.peekChar())) {
                                this.this$0.setScannerState(6);
                                this.this$0.setDispatcher(this.this$0.fContentDispatcher);
                                return true;
                            }
                            if (this.this$0.fEntityScanner.skipChar(63)) {
                                this.this$0.setScannerState(3);
                                bl2 = true;
                                break;
                            }
                            if (this.this$0.isValidNameStartHighSurrogate(this.this$0.fEntityScanner.peekChar())) {
                                this.this$0.setScannerState(6);
                                this.this$0.setDispatcher(this.this$0.fContentDispatcher);
                                return true;
                            }
                            this.this$0.reportFatalError("MarkupNotRecognizedInProlog", null);
                            break;
                        }
                        case 2: {
                            this.this$0.scanComment();
                            this.this$0.setScannerState(5);
                            break;
                        }
                        case 3: {
                            this.this$0.scanPI();
                            this.this$0.setScannerState(5);
                            break;
                        }
                        case 4: {
                            if (this.this$0.fDisallowDoctype) {
                                this.this$0.reportFatalError("DoctypeNotAllowed", null);
                            }
                            if (this.this$0.fSeenDoctypeDecl) {
                                this.this$0.reportFatalError("AlreadySeenDoctype", null);
                            }
                            this.this$0.fSeenDoctypeDecl = true;
                            if (this.this$0.scanDoctypeDecl()) {
                                this.this$0.setScannerState(17);
                                this.this$0.setDispatcher(this.this$0.fDTDDispatcher);
                                return true;
                            }
                            if (this.this$0.fDoctypeSystemId != null) {
                                boolean bl3 = this.this$0.fIsEntityDeclaredVC = !this.this$0.fStandalone;
                                if (!(!this.this$0.fValidation && !this.this$0.fLoadExternalDTD || this.this$0.fValidationManager != null && this.this$0.fValidationManager.isCachedDTD())) {
                                    this.this$0.setScannerState(18);
                                    this.this$0.setDispatcher(this.this$0.fDTDDispatcher);
                                    return true;
                                }
                            } else if (XMLDocumentScannerImpl.access$200(this.this$0) != null) {
                                boolean bl4 = this.this$0.fIsEntityDeclaredVC = !this.this$0.fStandalone;
                                if (!(!this.this$0.fValidation && !this.this$0.fLoadExternalDTD || this.this$0.fValidationManager != null && this.this$0.fValidationManager.isCachedDTD())) {
                                    this.this$0.fDTDScanner.setInputSource(XMLDocumentScannerImpl.access$200(this.this$0));
                                    XMLDocumentScannerImpl.access$202(this.this$0, null);
                                    this.this$0.setScannerState(19);
                                    this.this$0.setDispatcher(this.this$0.fDTDDispatcher);
                                    return true;
                                }
                            }
                            this.this$0.fDTDScanner.setInputSource(null);
                            this.this$0.setScannerState(5);
                            break;
                        }
                        case 7: {
                            this.this$0.reportFatalError("ContentIllegalInProlog", null);
                            this.this$0.fEntityScanner.scanChar();
                        }
                        case 8: {
                            this.this$0.reportFatalError("ReferenceIllegalInProlog", null);
                        }
                    }
                } while (bl || bl2);
                if (bl) {
                    if (this.this$0.fEntityScanner.scanChar() != 60) {
                        this.this$0.reportFatalError("RootElementRequired", null);
                    }
                    this.this$0.setScannerState(6);
                    this.this$0.setDispatcher(this.this$0.fContentDispatcher);
                }
            }
            catch (MalformedByteSequenceException malformedByteSequenceException) {
                this.this$0.fErrorReporter.reportError(malformedByteSequenceException.getDomain(), malformedByteSequenceException.getKey(), malformedByteSequenceException.getArguments(), 2, malformedByteSequenceException);
                return false;
            }
            catch (CharConversionException charConversionException) {
                this.this$0.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "CharConversionFailure", null, 2, charConversionException);
                return false;
            }
            catch (EOFException eOFException) {
                this.this$0.reportFatalError("PrematureEOF", null);
                return false;
            }
            return true;
        }
    }

    protected final class XMLDeclDispatcher
    implements XMLDocumentFragmentScannerImpl.Dispatcher {
        private final XMLDocumentScannerImpl this$0;

        protected XMLDeclDispatcher(XMLDocumentScannerImpl xMLDocumentScannerImpl) {
            this.this$0 = xMLDocumentScannerImpl;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        public boolean dispatch(boolean var1_1) throws IOException, XNIException {
            this.this$0.setScannerState(5);
            this.this$0.setDispatcher(this.this$0.fPrologDispatcher);
            try {
                if (!this.this$0.fEntityScanner.skipString("<?xml")) ** GOTO lbl12
                ++this.this$0.fMarkupDepth;
                if (!XMLChar.isName(this.this$0.fEntityScanner.peekChar())) ** GOTO lbl11
                XMLDocumentScannerImpl.access$000(this.this$0).clear();
                XMLDocumentScannerImpl.access$000(this.this$0).append("xml");
                if (!this.this$0.fNamespaces) ** GOTO lbl28
                ** GOTO lbl25
lbl11: // 1 sources:
                this.this$0.scanXMLDeclOrTextDecl(false);
lbl12: // 3 sources:
                do {
                    this.this$0.fEntityManager.fCurrentEntity.mayReadChunks = true;
                    return true;
                    break;
                } while (true);
            }
            catch (MalformedByteSequenceException var2_3) {
                this.this$0.fErrorReporter.reportError(var2_3.getDomain(), var2_3.getKey(), var2_3.getArguments(), 2, var2_3);
                return false;
            }
            catch (CharConversionException var3_4) {
                this.this$0.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "CharConversionFailure", null, 2, var3_4);
                return false;
            }
            catch (EOFException var4_5) {
                this.this$0.reportFatalError("PrematureEOF", null);
                return false;
            }
lbl-1000: // 1 sources:
            {
                XMLDocumentScannerImpl.access$000(this.this$0).append((char)this.this$0.fEntityScanner.scanChar());
lbl25: // 2 sources:
                ** while (XMLChar.isNCName((int)this.this$0.fEntityScanner.peekChar()))
            }
lbl26: // 1 sources:
            ** GOTO lbl29
lbl-1000: // 1 sources:
            {
                XMLDocumentScannerImpl.access$000(this.this$0).append((char)this.this$0.fEntityScanner.scanChar());
lbl28: // 2 sources:
                ** while (XMLChar.isName((int)this.this$0.fEntityScanner.peekChar()))
            }
lbl29: // 2 sources:
            var2_2 = this.this$0.fSymbolTable.addSymbol(XMLDocumentScannerImpl.access$000((XMLDocumentScannerImpl)this.this$0).ch, XMLDocumentScannerImpl.access$000((XMLDocumentScannerImpl)this.this$0).offset, XMLDocumentScannerImpl.access$000((XMLDocumentScannerImpl)this.this$0).length);
            this.this$0.scanPIData(var2_2, XMLDocumentScannerImpl.access$100(this.this$0));
            ** while (true)
        }
    }

}

