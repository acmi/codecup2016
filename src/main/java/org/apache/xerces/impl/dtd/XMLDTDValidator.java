/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dtd;

import java.io.IOException;
import java.io.PrintStream;
import org.apache.xerces.impl.Constants;
import org.apache.xerces.impl.RevalidationHandler;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.dtd.BalancedDTDGrammar;
import org.apache.xerces.impl.dtd.DTDGrammar;
import org.apache.xerces.impl.dtd.DTDGrammarBucket;
import org.apache.xerces.impl.dtd.XMLAttributeDecl;
import org.apache.xerces.impl.dtd.XMLDTDDescription;
import org.apache.xerces.impl.dtd.XMLDTDLoader;
import org.apache.xerces.impl.dtd.XMLDTDValidatorFilter;
import org.apache.xerces.impl.dtd.XMLElementDecl;
import org.apache.xerces.impl.dtd.XMLEntityDecl;
import org.apache.xerces.impl.dtd.XMLSimpleType;
import org.apache.xerces.impl.dtd.models.ContentModelValidator;
import org.apache.xerces.impl.dv.DTDDVFactory;
import org.apache.xerces.impl.dv.DatatypeValidator;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.validation.EntityState;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.validation.ValidationState;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLDocumentSource;

public class XMLDTDValidator
implements RevalidationHandler,
XMLDTDValidatorFilter,
XMLComponent,
XMLDocumentFilter {
    private static final int TOP_LEVEL_SCOPE = -1;
    protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    protected static final String VALIDATION = "http://xml.org/sax/features/validation";
    protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
    protected static final String BALANCE_SYNTAX_TREES = "http://apache.org/xml/features/validation/balance-syntax-trees";
    protected static final String WARN_ON_DUPLICATE_ATTDEF = "http://apache.org/xml/features/validation/warn-on-duplicate-attdef";
    protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
    protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    protected static final String GRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
    protected static final String DATATYPE_VALIDATOR_FACTORY = "http://apache.org/xml/properties/internal/datatype-validator-factory";
    protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
    private static final String[] RECOGNIZED_FEATURES = new String[]{"http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/validation", "http://apache.org/xml/features/validation/dynamic", "http://apache.org/xml/features/validation/balance-syntax-trees"};
    private static final Boolean[] FEATURE_DEFAULTS = new Boolean[]{null, null, Boolean.FALSE, Boolean.FALSE};
    private static final String[] RECOGNIZED_PROPERTIES = new String[]{"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/grammar-pool", "http://apache.org/xml/properties/internal/datatype-validator-factory", "http://apache.org/xml/properties/internal/validation-manager"};
    private static final Object[] PROPERTY_DEFAULTS = new Object[]{null, null, null, null, null};
    private static final boolean DEBUG_ATTRIBUTES = false;
    private static final boolean DEBUG_ELEMENT_CHILDREN = false;
    protected ValidationManager fValidationManager = null;
    protected final ValidationState fValidationState = new ValidationState();
    protected boolean fNamespaces;
    protected boolean fValidation;
    protected boolean fDTDValidation;
    protected boolean fDynamicValidation;
    protected boolean fBalanceSyntaxTrees;
    protected boolean fWarnDuplicateAttdef;
    protected SymbolTable fSymbolTable;
    protected XMLErrorReporter fErrorReporter;
    protected XMLGrammarPool fGrammarPool;
    protected DTDGrammarBucket fGrammarBucket;
    protected XMLLocator fDocLocation;
    protected NamespaceContext fNamespaceContext = null;
    protected DTDDVFactory fDatatypeValidatorFactory;
    protected XMLDocumentHandler fDocumentHandler;
    protected XMLDocumentSource fDocumentSource;
    protected DTDGrammar fDTDGrammar;
    protected boolean fSeenDoctypeDecl = false;
    private boolean fPerformValidation;
    private String fSchemaType;
    private final QName fCurrentElement = new QName();
    private int fCurrentElementIndex = -1;
    private int fCurrentContentSpecType = -1;
    private final QName fRootElement = new QName();
    private boolean fInCDATASection = false;
    private int[] fElementIndexStack = new int[8];
    private int[] fContentSpecTypeStack = new int[8];
    private QName[] fElementQNamePartsStack = new QName[8];
    private QName[] fElementChildren = new QName[32];
    private int fElementChildrenLength = 0;
    private int[] fElementChildrenOffsetStack = new int[32];
    private int fElementDepth = -1;
    private boolean fSeenRootElement = false;
    private boolean fInElementContent = false;
    private XMLElementDecl fTempElementDecl = new XMLElementDecl();
    private final XMLAttributeDecl fTempAttDecl = new XMLAttributeDecl();
    private final XMLEntityDecl fEntityDecl = new XMLEntityDecl();
    private final QName fTempQName = new QName();
    private final StringBuffer fBuffer = new StringBuffer();
    protected DatatypeValidator fValID;
    protected DatatypeValidator fValIDRef;
    protected DatatypeValidator fValIDRefs;
    protected DatatypeValidator fValENTITY;
    protected DatatypeValidator fValENTITIES;
    protected DatatypeValidator fValNMTOKEN;
    protected DatatypeValidator fValNMTOKENS;
    protected DatatypeValidator fValNOTATION;

    public XMLDTDValidator() {
        int n2 = 0;
        while (n2 < this.fElementQNamePartsStack.length) {
            this.fElementQNamePartsStack[n2] = new QName();
            ++n2;
        }
        this.fGrammarBucket = new DTDGrammarBucket();
    }

    DTDGrammarBucket getGrammarBucket() {
        return this.fGrammarBucket;
    }

    public void reset(XMLComponentManager xMLComponentManager) throws XMLConfigurationException {
        boolean bl;
        this.fDTDGrammar = null;
        this.fSeenDoctypeDecl = false;
        this.fInCDATASection = false;
        this.fSeenRootElement = false;
        this.fInElementContent = false;
        this.fCurrentElementIndex = -1;
        this.fCurrentContentSpecType = -1;
        this.fRootElement.clear();
        this.fValidationState.resetIDTables();
        this.fGrammarBucket.clear();
        this.fElementDepth = -1;
        this.fElementChildrenLength = 0;
        try {
            bl = xMLComponentManager.getFeature("http://apache.org/xml/features/internal/parser-settings");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            bl = true;
        }
        if (!bl) {
            this.fValidationManager.addValidationState(this.fValidationState);
            return;
        }
        try {
            this.fNamespaces = xMLComponentManager.getFeature("http://xml.org/sax/features/namespaces");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fNamespaces = true;
        }
        try {
            this.fValidation = xMLComponentManager.getFeature("http://xml.org/sax/features/validation");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fValidation = false;
        }
        try {
            this.fDTDValidation = !xMLComponentManager.getFeature("http://apache.org/xml/features/validation/schema");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fDTDValidation = true;
        }
        try {
            this.fDynamicValidation = xMLComponentManager.getFeature("http://apache.org/xml/features/validation/dynamic");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fDynamicValidation = false;
        }
        try {
            this.fBalanceSyntaxTrees = xMLComponentManager.getFeature("http://apache.org/xml/features/validation/balance-syntax-trees");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fBalanceSyntaxTrees = false;
        }
        try {
            this.fWarnDuplicateAttdef = xMLComponentManager.getFeature("http://apache.org/xml/features/validation/warn-on-duplicate-attdef");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fWarnDuplicateAttdef = false;
        }
        try {
            this.fSchemaType = (String)xMLComponentManager.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fSchemaType = null;
        }
        this.fValidationManager = (ValidationManager)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager");
        this.fValidationManager.addValidationState(this.fValidationState);
        this.fValidationState.setUsingNamespaces(this.fNamespaces);
        this.fErrorReporter = (XMLErrorReporter)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        this.fSymbolTable = (SymbolTable)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        try {
            this.fGrammarPool = (XMLGrammarPool)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/grammar-pool");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fGrammarPool = null;
        }
        this.fDatatypeValidatorFactory = (DTDDVFactory)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/datatype-validator-factory");
        this.init();
    }

    public String[] getRecognizedFeatures() {
        return (String[])RECOGNIZED_FEATURES.clone();
    }

    public void setFeature(String string, boolean bl) throws XMLConfigurationException {
    }

    public String[] getRecognizedProperties() {
        return (String[])RECOGNIZED_PROPERTIES.clone();
    }

    public void setProperty(String string, Object object) throws XMLConfigurationException {
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

    public void setDocumentSource(XMLDocumentSource xMLDocumentSource) {
        this.fDocumentSource = xMLDocumentSource;
    }

    public XMLDocumentSource getDocumentSource() {
        return this.fDocumentSource;
    }

    public void startDocument(XMLLocator xMLLocator, String string, NamespaceContext namespaceContext, Augmentations augmentations) throws XNIException {
        if (this.fGrammarPool != null) {
            Grammar[] arrgrammar = this.fGrammarPool.retrieveInitialGrammarSet("http://www.w3.org/TR/REC-xml");
            int n2 = arrgrammar != null ? arrgrammar.length : 0;
            int n3 = 0;
            while (n3 < n2) {
                this.fGrammarBucket.putGrammar((DTDGrammar)arrgrammar[n3]);
                ++n3;
            }
        }
        this.fDocLocation = xMLLocator;
        this.fNamespaceContext = namespaceContext;
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.startDocument(xMLLocator, string, namespaceContext, augmentations);
        }
    }

    public void xmlDecl(String string, String string2, String string3, Augmentations augmentations) throws XNIException {
        this.fGrammarBucket.setStandalone(string3 != null && string3.equals("yes"));
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.xmlDecl(string, string2, string3, augmentations);
        }
    }

    public void doctypeDecl(String string, String string2, String string3, Augmentations augmentations) throws XNIException {
        this.fSeenDoctypeDecl = true;
        this.fRootElement.setValues(null, string, string, null);
        String string4 = null;
        try {
            string4 = XMLEntityManager.expandSystemId(string3, this.fDocLocation.getExpandedSystemId(), false);
        }
        catch (IOException iOException) {
            // empty catch block
        }
        XMLDTDDescription xMLDTDDescription = new XMLDTDDescription(string2, string3, this.fDocLocation.getExpandedSystemId(), string4, string);
        this.fDTDGrammar = this.fGrammarBucket.getGrammar(xMLDTDDescription);
        if (this.fDTDGrammar == null && this.fGrammarPool != null && (string3 != null || string2 != null)) {
            this.fDTDGrammar = (DTDGrammar)this.fGrammarPool.retrieveGrammar(xMLDTDDescription);
        }
        if (this.fDTDGrammar == null) {
            this.fDTDGrammar = !this.fBalanceSyntaxTrees ? new DTDGrammar(this.fSymbolTable, xMLDTDDescription) : new BalancedDTDGrammar(this.fSymbolTable, xMLDTDDescription);
        } else {
            this.fValidationManager.setCachedDTD(true);
        }
        this.fGrammarBucket.setActiveGrammar(this.fDTDGrammar);
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.doctypeDecl(string, string2, string3, augmentations);
        }
    }

    public void startElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        this.handleStartElement(qName, xMLAttributes, augmentations);
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.startElement(qName, xMLAttributes, augmentations);
        }
    }

    public void emptyElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        boolean bl = this.handleStartElement(qName, xMLAttributes, augmentations);
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.emptyElement(qName, xMLAttributes, augmentations);
        }
        if (!bl) {
            this.handleEndElement(qName, augmentations, true);
        }
    }

    public void characters(XMLString xMLString, Augmentations augmentations) throws XNIException {
        boolean bl = true;
        boolean bl2 = true;
        int n2 = xMLString.offset;
        while (n2 < xMLString.offset + xMLString.length) {
            if (!this.isSpace(xMLString.ch[n2])) {
                bl2 = false;
                break;
            }
            ++n2;
        }
        if (this.fInElementContent && bl2 && !this.fInCDATASection && this.fDocumentHandler != null) {
            this.fDocumentHandler.ignorableWhitespace(xMLString, augmentations);
            bl = false;
        }
        if (this.fPerformValidation) {
            if (this.fInElementContent) {
                if (this.fGrammarBucket.getStandalone() && this.fDTDGrammar.getElementDeclIsExternal(this.fCurrentElementIndex) && bl2) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_WHITE_SPACE_IN_ELEMENT_CONTENT_WHEN_STANDALONE", null, 1);
                }
                if (!bl2) {
                    this.charDataInContent();
                }
                if (augmentations != null && augmentations.getItem("CHAR_REF_PROBABLE_WS") == Boolean.TRUE) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID_SPECIFIED", new Object[]{this.fCurrentElement.rawname, this.fDTDGrammar.getContentSpecAsString(this.fElementDepth), "character reference"}, 1);
                }
            }
            if (this.fCurrentContentSpecType == 1) {
                this.charDataInContent();
            }
        }
        if (bl && this.fDocumentHandler != null) {
            this.fDocumentHandler.characters(xMLString, augmentations);
        }
    }

    public void ignorableWhitespace(XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.ignorableWhitespace(xMLString, augmentations);
        }
    }

    public void endElement(QName qName, Augmentations augmentations) throws XNIException {
        this.handleEndElement(qName, augmentations, false);
    }

    public void startCDATA(Augmentations augmentations) throws XNIException {
        if (this.fPerformValidation && this.fInElementContent) {
            this.charDataInContent();
        }
        this.fInCDATASection = true;
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.startCDATA(augmentations);
        }
    }

    public void endCDATA(Augmentations augmentations) throws XNIException {
        this.fInCDATASection = false;
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endCDATA(augmentations);
        }
    }

    public void endDocument(Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endDocument(augmentations);
        }
    }

    public void comment(XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (this.fPerformValidation && this.fElementDepth >= 0 && this.fDTDGrammar != null) {
            this.fDTDGrammar.getElementDecl(this.fCurrentElementIndex, this.fTempElementDecl);
            if (this.fTempElementDecl.type == 1) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID_SPECIFIED", new Object[]{this.fCurrentElement.rawname, "EMPTY", "comment"}, 1);
            }
        }
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.comment(xMLString, augmentations);
        }
    }

    public void processingInstruction(String string, XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (this.fPerformValidation && this.fElementDepth >= 0 && this.fDTDGrammar != null) {
            this.fDTDGrammar.getElementDecl(this.fCurrentElementIndex, this.fTempElementDecl);
            if (this.fTempElementDecl.type == 1) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID_SPECIFIED", new Object[]{this.fCurrentElement.rawname, "EMPTY", "processing instruction"}, 1);
            }
        }
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.processingInstruction(string, xMLString, augmentations);
        }
    }

    public void startGeneralEntity(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
        if (this.fPerformValidation && this.fElementDepth >= 0 && this.fDTDGrammar != null) {
            this.fDTDGrammar.getElementDecl(this.fCurrentElementIndex, this.fTempElementDecl);
            if (this.fTempElementDecl.type == 1) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID_SPECIFIED", new Object[]{this.fCurrentElement.rawname, "EMPTY", "ENTITY"}, 1);
            }
            if (this.fGrammarBucket.getStandalone()) {
                XMLDTDLoader.checkStandaloneEntityRef(string, this.fDTDGrammar, this.fEntityDecl, this.fErrorReporter);
            }
        }
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.startGeneralEntity(string, xMLResourceIdentifier, string2, augmentations);
        }
    }

    public void endGeneralEntity(String string, Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endGeneralEntity(string, augmentations);
        }
    }

    public void textDecl(String string, String string2, Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.textDecl(string, string2, augmentations);
        }
    }

    public final boolean hasGrammar() {
        return this.fDTDGrammar != null;
    }

    public final boolean validate() {
        return this.fSchemaType != Constants.NS_XMLSCHEMA && (!this.fDynamicValidation && this.fValidation || this.fDynamicValidation && this.fSeenDoctypeDecl) && (this.fDTDValidation || this.fSeenDoctypeDecl);
    }

    protected void addDTDDefaultAttrsAndValidate(QName qName, int n2, XMLAttributes xMLAttributes) throws XNIException {
        String string;
        int n3;
        boolean bl;
        if (n2 == -1 || this.fDTDGrammar == null) {
            return;
        }
        int n4 = this.fDTDGrammar.getFirstAttributeDeclIndex(n2);
        while (n4 != -1) {
            boolean bl2;
            int n5;
            this.fDTDGrammar.getAttributeDecl(n4, this.fTempAttDecl);
            String string2 = this.fTempAttDecl.name.prefix;
            String string3 = this.fTempAttDecl.name.localpart;
            string = this.fTempAttDecl.name.rawname;
            String string4 = this.getAttributeTypeName(this.fTempAttDecl);
            n3 = this.fTempAttDecl.simpleType.defaultType;
            String string5 = null;
            if (this.fTempAttDecl.simpleType.defaultValue != null) {
                string5 = this.fTempAttDecl.simpleType.defaultValue;
            }
            boolean bl3 = false;
            bl = n3 == 2;
            boolean bl4 = bl2 = string4 == XMLSymbols.fCDATASymbol;
            if (!bl2 || bl || string5 != null) {
                n5 = xMLAttributes.getLength();
                int n6 = 0;
                while (n6 < n5) {
                    if (xMLAttributes.getQName(n6) == string) {
                        bl3 = true;
                        break;
                    }
                    ++n6;
                }
            }
            if (!bl3) {
                if (bl) {
                    if (this.fPerformValidation) {
                        Object[] arrobject = new Object[]{qName.localpart, string};
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_REQUIRED_ATTRIBUTE_NOT_SPECIFIED", arrobject, 1);
                    }
                } else if (string5 != null) {
                    if (this.fPerformValidation && this.fGrammarBucket.getStandalone() && this.fDTDGrammar.getAttributeDeclIsExternal(n4)) {
                        Object[] arrobject = new Object[]{qName.localpart, string};
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DEFAULTED_ATTRIBUTE_NOT_SPECIFIED", arrobject, 1);
                    }
                    if (this.fNamespaces && (n5 = string.indexOf(58)) != -1) {
                        string2 = string.substring(0, n5);
                        string2 = this.fSymbolTable.addSymbol(string2);
                        string3 = string.substring(n5 + 1);
                        string3 = this.fSymbolTable.addSymbol(string3);
                    }
                    this.fTempQName.setValues(string2, string3, string, this.fTempAttDecl.name.uri);
                    n5 = xMLAttributes.addAttribute(this.fTempQName, string4, string5);
                }
            }
            n4 = this.fDTDGrammar.getNextAttributeDeclIndex(n4);
        }
        int n7 = xMLAttributes.getLength();
        int n8 = 0;
        while (n8 < n7) {
            String string6;
            String string7;
            string = xMLAttributes.getQName(n8);
            boolean bl5 = false;
            if (this.fPerformValidation && this.fGrammarBucket.getStandalone() && (string7 = xMLAttributes.getNonNormalizedValue(n8)) != null && (string6 = this.getExternalEntityRefInAttrValue(string7)) != null) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_REFERENCE_TO_EXTERNALLY_DECLARED_ENTITY_WHEN_STANDALONE", new Object[]{string6}, 1);
            }
            n3 = -1;
            int n9 = this.fDTDGrammar.getFirstAttributeDeclIndex(n2);
            while (n9 != -1) {
                this.fDTDGrammar.getAttributeDecl(n9, this.fTempAttDecl);
                if (this.fTempAttDecl.name.rawname == string) {
                    n3 = n9;
                    bl5 = true;
                    break;
                }
                n9 = this.fDTDGrammar.getNextAttributeDeclIndex(n9);
            }
            if (!bl5) {
                if (this.fPerformValidation) {
                    Object[] arrobject = new Object[]{qName.rawname, string};
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ATTRIBUTE_NOT_DECLARED", arrobject, 1);
                }
            } else {
                String string8;
                String string9 = this.getAttributeTypeName(this.fTempAttDecl);
                xMLAttributes.setType(n8, string9);
                xMLAttributes.getAugmentations(n8).putItem("ATTRIBUTE_DECLARED", Boolean.TRUE);
                bl = false;
                String string10 = string8 = xMLAttributes.getValue(n8);
                if (xMLAttributes.isSpecified(n8) && string9 != XMLSymbols.fCDATASymbol) {
                    bl = this.normalizeAttrValue(xMLAttributes, n8);
                    string10 = xMLAttributes.getValue(n8);
                    if (this.fPerformValidation && this.fGrammarBucket.getStandalone() && bl && this.fDTDGrammar.getAttributeDeclIsExternal(n9)) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ATTVALUE_CHANGED_DURING_NORMALIZATION_WHEN_STANDALONE", new Object[]{string, string8, string10}, 1);
                    }
                }
                if (this.fPerformValidation) {
                    String string11;
                    if (this.fTempAttDecl.simpleType.defaultType == 1 && !string10.equals(string11 = this.fTempAttDecl.simpleType.defaultValue)) {
                        Object[] arrobject = new Object[]{qName.localpart, string, string10, string11};
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_FIXED_ATTVALUE_INVALID", arrobject, 1);
                    }
                    if (this.fTempAttDecl.simpleType.type == 1 || this.fTempAttDecl.simpleType.type == 2 || this.fTempAttDecl.simpleType.type == 3 || this.fTempAttDecl.simpleType.type == 4 || this.fTempAttDecl.simpleType.type == 5 || this.fTempAttDecl.simpleType.type == 6) {
                        this.validateDTDattribute(qName, string10, this.fTempAttDecl);
                    }
                }
            }
            ++n8;
        }
    }

    protected String getExternalEntityRefInAttrValue(String string) {
        int n2 = string.length();
        int n3 = string.indexOf(38);
        while (n3 != -1) {
            if (n3 + 1 < n2 && string.charAt(n3 + 1) != '#') {
                int n4 = string.indexOf(59, n3 + 1);
                String string2 = string.substring(n3 + 1, n4);
                int n5 = this.fDTDGrammar.getEntityDeclIndex(string2 = this.fSymbolTable.addSymbol(string2));
                if (n5 > -1) {
                    this.fDTDGrammar.getEntityDecl(n5, this.fEntityDecl);
                    if (this.fEntityDecl.inExternal || (string2 = this.getExternalEntityRefInAttrValue(this.fEntityDecl.value)) != null) {
                        return string2;
                    }
                }
            }
            n3 = string.indexOf(38, n3 + 1);
        }
        return null;
    }

    protected void validateDTDattribute(QName qName, String string, XMLAttributeDecl xMLAttributeDecl) throws XNIException {
        switch (xMLAttributeDecl.simpleType.type) {
            case 1: {
                boolean bl = xMLAttributeDecl.simpleType.list;
                try {
                    if (bl) {
                        this.fValENTITIES.validate(string, this.fValidationState);
                        break;
                    }
                    this.fValENTITY.validate(string, this.fValidationState);
                }
                catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", invalidDatatypeValueException.getKey(), invalidDatatypeValueException.getArgs(), 1);
                }
                break;
            }
            case 2: 
            case 6: {
                boolean bl = false;
                String[] arrstring = xMLAttributeDecl.simpleType.enumeration;
                if (arrstring == null) {
                    bl = false;
                } else {
                    int n2 = 0;
                    while (n2 < arrstring.length) {
                        if (string == arrstring[n2] || string.equals(arrstring[n2])) {
                            bl = true;
                            break;
                        }
                        ++n2;
                    }
                }
                if (bl) break;
                StringBuffer stringBuffer = new StringBuffer();
                if (arrstring != null) {
                    int n3 = 0;
                    while (n3 < arrstring.length) {
                        stringBuffer.append(arrstring[n3] + " ");
                        ++n3;
                    }
                }
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ATTRIBUTE_VALUE_NOT_IN_LIST", new Object[]{xMLAttributeDecl.name.rawname, string, stringBuffer}, 1);
                break;
            }
            case 3: {
                try {
                    this.fValID.validate(string, this.fValidationState);
                }
                catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", invalidDatatypeValueException.getKey(), invalidDatatypeValueException.getArgs(), 1);
                }
                break;
            }
            case 4: {
                boolean bl = xMLAttributeDecl.simpleType.list;
                try {
                    if (bl) {
                        this.fValIDRefs.validate(string, this.fValidationState);
                        break;
                    }
                    this.fValIDRef.validate(string, this.fValidationState);
                }
                catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                    if (bl) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "IDREFSInvalid", new Object[]{string}, 1);
                        break;
                    }
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", invalidDatatypeValueException.getKey(), invalidDatatypeValueException.getArgs(), 1);
                }
                break;
            }
            case 5: {
                boolean bl = xMLAttributeDecl.simpleType.list;
                try {
                    if (bl) {
                        this.fValNMTOKENS.validate(string, this.fValidationState);
                        break;
                    }
                    this.fValNMTOKEN.validate(string, this.fValidationState);
                    break;
                }
                catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                    if (bl) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "NMTOKENSInvalid", new Object[]{string}, 1);
                        break;
                    }
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "NMTOKENInvalid", new Object[]{string}, 1);
                }
            }
        }
    }

    protected boolean invalidStandaloneAttDef(QName qName, QName qName2) {
        boolean bl = true;
        return bl;
    }

    private boolean normalizeAttrValue(XMLAttributes xMLAttributes, int n2) {
        boolean bl = true;
        boolean bl2 = false;
        boolean bl3 = false;
        int n3 = 0;
        int n4 = 0;
        String string = xMLAttributes.getValue(n2);
        char[] arrc = new char[string.length()];
        this.fBuffer.setLength(0);
        string.getChars(0, string.length(), arrc, 0);
        int n5 = 0;
        while (n5 < arrc.length) {
            if (arrc[n5] == ' ') {
                if (bl3) {
                    bl2 = true;
                    bl3 = false;
                }
                if (bl2 && !bl) {
                    bl2 = false;
                    this.fBuffer.append(arrc[n5]);
                    ++n3;
                } else if (bl || !bl2) {
                    ++n4;
                }
            } else {
                bl3 = true;
                bl2 = false;
                bl = false;
                this.fBuffer.append(arrc[n5]);
                ++n3;
            }
            ++n5;
        }
        if (n3 > 0 && this.fBuffer.charAt(n3 - 1) == ' ') {
            this.fBuffer.setLength(n3 - 1);
        }
        String string2 = this.fBuffer.toString();
        xMLAttributes.setValue(n2, string2);
        return !string.equals(string2);
    }

    private final void rootElementSpecified(QName qName) throws XNIException {
        if (this.fPerformValidation) {
            String string = this.fRootElement.rawname;
            String string2 = qName.rawname;
            if (string == null || !string.equals(string2)) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "RootElementTypeMustMatchDoctypedecl", new Object[]{string, string2}, 1);
            }
        }
    }

    private int checkContent(int n2, QName[] arrqName, int n3, int n4) throws XNIException {
        this.fDTDGrammar.getElementDecl(n2, this.fTempElementDecl);
        String string = this.fCurrentElement.rawname;
        int n5 = this.fCurrentContentSpecType;
        if (n5 == 1) {
            if (n4 != 0) {
                return 0;
            }
        } else if (n5 != 0) {
            if (n5 == 2 || n5 == 3) {
                ContentModelValidator contentModelValidator = null;
                contentModelValidator = this.fTempElementDecl.contentModelValidator;
                int n6 = contentModelValidator.validate(arrqName, n3, n4);
                return n6;
            }
            if (n5 == -1 || n5 == 4) {
                // empty if block
            }
        }
        return -1;
    }

    private int getContentSpecType(int n2) {
        int n3 = -1;
        if (n2 > -1 && this.fDTDGrammar.getElementDecl(n2, this.fTempElementDecl)) {
            n3 = this.fTempElementDecl.type;
        }
        return n3;
    }

    private void charDataInContent() {
        Object object;
        if (this.fElementChildren.length <= this.fElementChildrenLength) {
            object = new QName[this.fElementChildren.length * 2];
            System.arraycopy(this.fElementChildren, 0, object, 0, this.fElementChildren.length);
            this.fElementChildren = object;
        }
        if ((object = this.fElementChildren[this.fElementChildrenLength]) == null) {
            int n2 = this.fElementChildrenLength;
            while (n2 < this.fElementChildren.length) {
                this.fElementChildren[n2] = new QName();
                ++n2;
            }
            object = this.fElementChildren[this.fElementChildrenLength];
        }
        object.clear();
        ++this.fElementChildrenLength;
    }

    private String getAttributeTypeName(XMLAttributeDecl xMLAttributeDecl) {
        switch (xMLAttributeDecl.simpleType.type) {
            case 1: {
                return xMLAttributeDecl.simpleType.list ? XMLSymbols.fENTITIESSymbol : XMLSymbols.fENTITYSymbol;
            }
            case 2: {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append('(');
                int n2 = 0;
                while (n2 < xMLAttributeDecl.simpleType.enumeration.length) {
                    if (n2 > 0) {
                        stringBuffer.append("|");
                    }
                    stringBuffer.append(xMLAttributeDecl.simpleType.enumeration[n2]);
                    ++n2;
                }
                stringBuffer.append(')');
                return this.fSymbolTable.addSymbol(stringBuffer.toString());
            }
            case 3: {
                return XMLSymbols.fIDSymbol;
            }
            case 4: {
                return xMLAttributeDecl.simpleType.list ? XMLSymbols.fIDREFSSymbol : XMLSymbols.fIDREFSymbol;
            }
            case 5: {
                return xMLAttributeDecl.simpleType.list ? XMLSymbols.fNMTOKENSSymbol : XMLSymbols.fNMTOKENSymbol;
            }
            case 6: {
                return XMLSymbols.fNOTATIONSymbol;
            }
        }
        return XMLSymbols.fCDATASymbol;
    }

    protected void init() {
        if (this.fValidation || this.fDynamicValidation) {
            try {
                this.fValID = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fIDSymbol);
                this.fValIDRef = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fIDREFSymbol);
                this.fValIDRefs = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fIDREFSSymbol);
                this.fValENTITY = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fENTITYSymbol);
                this.fValENTITIES = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fENTITIESSymbol);
                this.fValNMTOKEN = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fNMTOKENSymbol);
                this.fValNMTOKENS = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fNMTOKENSSymbol);
                this.fValNOTATION = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fNOTATIONSymbol);
            }
            catch (Exception exception) {
                exception.printStackTrace(System.err);
            }
        }
    }

    private void ensureStackCapacity(int n2) {
        if (n2 == this.fElementQNamePartsStack.length) {
            QName[] arrqName = new QName[n2 * 2];
            System.arraycopy(this.fElementQNamePartsStack, 0, arrqName, 0, n2);
            this.fElementQNamePartsStack = arrqName;
            QName qName = this.fElementQNamePartsStack[n2];
            if (qName == null) {
                int n3 = n2;
                while (n3 < this.fElementQNamePartsStack.length) {
                    this.fElementQNamePartsStack[n3] = new QName();
                    ++n3;
                }
            }
            int[] arrn = new int[n2 * 2];
            System.arraycopy(this.fElementIndexStack, 0, arrn, 0, n2);
            this.fElementIndexStack = arrn;
            arrn = new int[n2 * 2];
            System.arraycopy(this.fContentSpecTypeStack, 0, arrn, 0, n2);
            this.fContentSpecTypeStack = arrn;
        }
    }

    protected boolean handleStartElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        if (!this.fSeenRootElement) {
            this.fPerformValidation = this.validate();
            this.fSeenRootElement = true;
            this.fValidationManager.setEntityState(this.fDTDGrammar);
            this.fValidationManager.setGrammarFound(this.fSeenDoctypeDecl);
            this.rootElementSpecified(qName);
        }
        if (this.fDTDGrammar == null) {
            if (!this.fPerformValidation) {
                this.fCurrentElementIndex = -1;
                this.fCurrentContentSpecType = -1;
                this.fInElementContent = false;
            }
            if (this.fPerformValidation) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_GRAMMAR_NOT_FOUND", new Object[]{qName.rawname}, 1);
            }
            if (this.fDocumentSource != null) {
                this.fDocumentSource.setDocumentHandler(this.fDocumentHandler);
                if (this.fDocumentHandler != null) {
                    this.fDocumentHandler.setDocumentSource(this.fDocumentSource);
                }
                return true;
            }
        } else {
            this.fCurrentElementIndex = this.fDTDGrammar.getElementDeclIndex(qName);
            this.fCurrentContentSpecType = this.fDTDGrammar.getContentSpecType(this.fCurrentElementIndex);
            if (this.fCurrentContentSpecType == -1 && this.fPerformValidation) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ELEMENT_NOT_DECLARED", new Object[]{qName.rawname}, 1);
            }
            this.addDTDDefaultAttrsAndValidate(qName, this.fCurrentElementIndex, xMLAttributes);
        }
        this.fInElementContent = this.fCurrentContentSpecType == 3;
        ++this.fElementDepth;
        if (this.fPerformValidation) {
            Object object;
            if (this.fElementChildrenOffsetStack.length <= this.fElementDepth) {
                object = new int[this.fElementChildrenOffsetStack.length * 2];
                System.arraycopy(this.fElementChildrenOffsetStack, 0, object, 0, this.fElementChildrenOffsetStack.length);
                this.fElementChildrenOffsetStack = object;
            }
            this.fElementChildrenOffsetStack[this.fElementDepth] = this.fElementChildrenLength;
            if (this.fElementChildren.length <= this.fElementChildrenLength) {
                object = new QName[this.fElementChildrenLength * 2];
                System.arraycopy(this.fElementChildren, 0, object, 0, this.fElementChildren.length);
                this.fElementChildren = object;
            }
            if ((object = this.fElementChildren[this.fElementChildrenLength]) == null) {
                int n2 = this.fElementChildrenLength;
                while (n2 < this.fElementChildren.length) {
                    this.fElementChildren[n2] = new QName();
                    ++n2;
                }
                object = this.fElementChildren[this.fElementChildrenLength];
            }
            object.setValues(qName);
            ++this.fElementChildrenLength;
        }
        this.fCurrentElement.setValues(qName);
        this.ensureStackCapacity(this.fElementDepth);
        this.fElementQNamePartsStack[this.fElementDepth].setValues(this.fCurrentElement);
        this.fElementIndexStack[this.fElementDepth] = this.fCurrentElementIndex;
        this.fContentSpecTypeStack[this.fElementDepth] = this.fCurrentContentSpecType;
        this.startNamespaceScope(qName, xMLAttributes, augmentations);
        return false;
    }

    protected void startNamespaceScope(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) {
    }

    protected void handleEndElement(QName qName, Augmentations augmentations, boolean bl) throws XNIException {
        --this.fElementDepth;
        if (this.fPerformValidation) {
            int n2;
            int n3;
            int n4;
            QName[] arrqName;
            int n5 = this.fCurrentElementIndex;
            if (n5 != -1 && this.fCurrentContentSpecType != -1 && (n3 = this.checkContent(n5, arrqName = this.fElementChildren, n2 = this.fElementChildrenOffsetStack[this.fElementDepth + 1] + 1, n4 = this.fElementChildrenLength - n2)) != -1) {
                this.fDTDGrammar.getElementDecl(n5, this.fTempElementDecl);
                if (this.fTempElementDecl.type == 1) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID", new Object[]{qName.rawname, "EMPTY"}, 1);
                } else {
                    String string = n3 != n4 ? "MSG_CONTENT_INVALID" : "MSG_CONTENT_INCOMPLETE";
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", string, new Object[]{qName.rawname, this.fDTDGrammar.getContentSpecAsString(n5)}, 1);
                }
            }
            this.fElementChildrenLength = this.fElementChildrenOffsetStack[this.fElementDepth + 1] + 1;
        }
        this.endNamespaceScope(this.fCurrentElement, augmentations, bl);
        if (this.fElementDepth < -1) {
            throw new RuntimeException("FWK008 Element stack underflow");
        }
        if (this.fElementDepth < 0) {
            String string;
            this.fCurrentElement.clear();
            this.fCurrentElementIndex = -1;
            this.fCurrentContentSpecType = -1;
            this.fInElementContent = false;
            if (this.fPerformValidation && (string = this.fValidationState.checkIDRefID()) != null) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ELEMENT_WITH_ID_REQUIRED", new Object[]{string}, 1);
            }
            return;
        }
        this.fCurrentElement.setValues(this.fElementQNamePartsStack[this.fElementDepth]);
        this.fCurrentElementIndex = this.fElementIndexStack[this.fElementDepth];
        this.fCurrentContentSpecType = this.fContentSpecTypeStack[this.fElementDepth];
        this.fInElementContent = this.fCurrentContentSpecType == 3;
    }

    protected void endNamespaceScope(QName qName, Augmentations augmentations, boolean bl) {
        if (this.fDocumentHandler != null && !bl) {
            this.fDocumentHandler.endElement(this.fCurrentElement, augmentations);
        }
    }

    protected boolean isSpace(int n2) {
        return XMLChar.isSpace(n2);
    }

    public boolean characterData(String string, Augmentations augmentations) {
        this.characters(new XMLString(string.toCharArray(), 0, string.length()), augmentations);
        return true;
    }
}

