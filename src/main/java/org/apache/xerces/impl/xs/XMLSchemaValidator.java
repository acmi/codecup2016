/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;
import javax.xml.namespace.QName;
import org.apache.xerces.impl.RevalidationHandler;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.dv.DatatypeException;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.xerces.impl.validation.ConfigurableValidationState;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.validation.ValidationState;
import org.apache.xerces.impl.xs.AttributePSVImpl;
import org.apache.xerces.impl.xs.ElementPSVImpl;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.SubstitutionGroupHandler;
import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.impl.xs.XSAttributeDecl;
import org.apache.xerces.impl.xs.XSAttributeGroupDecl;
import org.apache.xerces.impl.xs.XSAttributeUseImpl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSConstraints;
import org.apache.xerces.impl.xs.XSDDescription;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSGrammarBucket;
import org.apache.xerces.impl.xs.XSNotationDecl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.XSWildcardDecl;
import org.apache.xerces.impl.xs.identity.Field;
import org.apache.xerces.impl.xs.identity.FieldActivator;
import org.apache.xerces.impl.xs.identity.IdentityConstraint;
import org.apache.xerces.impl.xs.identity.KeyRef;
import org.apache.xerces.impl.xs.identity.Selector;
import org.apache.xerces.impl.xs.identity.UniqueOrKey;
import org.apache.xerces.impl.xs.identity.ValueStore;
import org.apache.xerces.impl.xs.identity.XPathMatcher;
import org.apache.xerces.impl.xs.models.CMBuilder;
import org.apache.xerces.impl.xs.models.CMNodeFactory;
import org.apache.xerces.impl.xs.models.XSCMValidator;
import org.apache.xerces.util.AugmentationsImpl;
import org.apache.xerces.util.IntStack;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.URI;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
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
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNotationDeclaration;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;

public class XMLSchemaValidator
implements RevalidationHandler,
FieldActivator,
XMLComponent,
XMLDocumentFilter {
    private static final boolean DEBUG = false;
    protected static final String VALIDATION = "http://xml.org/sax/features/validation";
    protected static final String SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
    protected static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
    protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
    protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
    protected static final String SCHEMA_ELEMENT_DEFAULT = "http://apache.org/xml/features/validation/schema/element-default";
    protected static final String SCHEMA_AUGMENT_PSVI = "http://apache.org/xml/features/validation/schema/augment-psvi";
    protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
    protected static final String STANDARD_URI_CONFORMANT_FEATURE = "http://apache.org/xml/features/standard-uri-conformant";
    protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = "http://apache.org/xml/features/generate-synthetic-annotations";
    protected static final String VALIDATE_ANNOTATIONS = "http://apache.org/xml/features/validate-annotations";
    protected static final String HONOUR_ALL_SCHEMALOCATIONS = "http://apache.org/xml/features/honour-all-schemaLocations";
    protected static final String USE_GRAMMAR_POOL_ONLY = "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only";
    protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
    protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
    protected static final String NAMESPACE_GROWTH = "http://apache.org/xml/features/namespace-growth";
    protected static final String TOLERATE_DUPLICATES = "http://apache.org/xml/features/internal/tolerate-duplicates";
    protected static final String IGNORE_XSI_TYPE = "http://apache.org/xml/features/validation/schema/ignore-xsi-type-until-elemdecl";
    protected static final String ID_IDREF_CHECKING = "http://apache.org/xml/features/validation/id-idref-checking";
    protected static final String UNPARSED_ENTITY_CHECKING = "http://apache.org/xml/features/validation/unparsed-entity-checking";
    protected static final String IDENTITY_CONSTRAINT_CHECKING = "http://apache.org/xml/features/validation/identity-constraint-checking";
    public static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    public static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    public static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
    public static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
    protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
    protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
    protected static final String SCHEMA_LOCATION = "http://apache.org/xml/properties/schema/external-schemaLocation";
    protected static final String SCHEMA_NONS_LOCATION = "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation";
    protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    protected static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    protected static final String ROOT_TYPE_DEF = "http://apache.org/xml/properties/validation/schema/root-type-definition";
    protected static final String ROOT_ELEMENT_DECL = "http://apache.org/xml/properties/validation/schema/root-element-declaration";
    protected static final String SCHEMA_DV_FACTORY = "http://apache.org/xml/properties/internal/validation/schema/dv-factory";
    private static final String[] RECOGNIZED_FEATURES = new String[]{"http://xml.org/sax/features/validation", "http://apache.org/xml/features/validation/schema", "http://apache.org/xml/features/validation/dynamic", "http://apache.org/xml/features/validation/schema-full-checking", "http://apache.org/xml/features/allow-java-encodings", "http://apache.org/xml/features/continue-after-fatal-error", "http://apache.org/xml/features/standard-uri-conformant", "http://apache.org/xml/features/generate-synthetic-annotations", "http://apache.org/xml/features/validate-annotations", "http://apache.org/xml/features/honour-all-schemaLocations", "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only", "http://apache.org/xml/features/validation/schema/ignore-xsi-type-until-elemdecl", "http://apache.org/xml/features/validation/id-idref-checking", "http://apache.org/xml/features/validation/identity-constraint-checking", "http://apache.org/xml/features/validation/unparsed-entity-checking", "http://apache.org/xml/features/namespace-growth", "http://apache.org/xml/features/internal/tolerate-duplicates"};
    private static final Boolean[] FEATURE_DEFAULTS = new Boolean[]{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
    private static final String[] RECOGNIZED_PROPERTIES = new String[]{"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/validation-manager", "http://apache.org/xml/properties/schema/external-schemaLocation", "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", "http://java.sun.com/xml/jaxp/properties/schemaSource", "http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://apache.org/xml/properties/validation/schema/root-type-definition", "http://apache.org/xml/properties/validation/schema/root-element-declaration", "http://apache.org/xml/properties/internal/validation/schema/dv-factory"};
    private static final Object[] PROPERTY_DEFAULTS = new Object[]{null, null, null, null, null, null, null, null, null, null, null};
    protected static final int ID_CONSTRAINT_NUM = 1;
    static final XSAttributeDecl XSI_TYPE = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_TYPE);
    static final XSAttributeDecl XSI_NIL = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_NIL);
    static final XSAttributeDecl XSI_SCHEMALOCATION = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_SCHEMALOCATION);
    static final XSAttributeDecl XSI_NONAMESPACESCHEMALOCATION = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION);
    private static final Hashtable EMPTY_TABLE = new Hashtable();
    protected ElementPSVImpl fCurrentPSVI = new ElementPSVImpl();
    protected final AugmentationsImpl fAugmentations = new AugmentationsImpl();
    protected XMLString fDefaultValue;
    protected boolean fDynamicValidation = false;
    protected boolean fSchemaDynamicValidation = false;
    protected boolean fDoValidation = false;
    protected boolean fFullChecking = false;
    protected boolean fNormalizeData = true;
    protected boolean fSchemaElementDefault = true;
    protected boolean fAugPSVI = true;
    protected boolean fIdConstraint = false;
    protected boolean fUseGrammarPoolOnly = false;
    protected boolean fNamespaceGrowth = false;
    private String fSchemaType = null;
    protected boolean fEntityRef = false;
    protected boolean fInCDATA = false;
    protected SymbolTable fSymbolTable;
    private XMLLocator fLocator;
    protected final XSIErrorReporter fXSIErrorReporter;
    protected XMLEntityResolver fEntityResolver;
    protected ValidationManager fValidationManager;
    protected ConfigurableValidationState fValidationState;
    protected XMLGrammarPool fGrammarPool;
    protected String fExternalSchemas;
    protected String fExternalNoNamespaceSchema;
    protected Object fJaxpSchemaSource;
    protected final XSDDescription fXSDDescription;
    protected final Hashtable fLocationPairs;
    protected final Hashtable fExpandedLocationPairs;
    protected final ArrayList fUnparsedLocations;
    protected XMLDocumentHandler fDocumentHandler;
    protected XMLDocumentSource fDocumentSource;
    static final int INITIAL_STACK_SIZE = 8;
    static final int INC_STACK_SIZE = 8;
    private static final boolean DEBUG_NORMALIZATION = false;
    private final XMLString fEmptyXMLStr;
    private static final int BUFFER_SIZE = 20;
    private final XMLString fNormalizedStr;
    private boolean fFirstChunk;
    private boolean fTrailing;
    private short fWhiteSpace;
    private boolean fUnionType;
    private final XSGrammarBucket fGrammarBucket;
    private final SubstitutionGroupHandler fSubGroupHandler;
    private final XSSimpleType fQNameDV;
    private final CMNodeFactory nodeFactory;
    private final CMBuilder fCMBuilder;
    private final XMLSchemaLoader fSchemaLoader;
    private String fValidationRoot;
    private int fSkipValidationDepth;
    private int fNFullValidationDepth;
    private int fNNoneValidationDepth;
    private int fElementDepth;
    private boolean fSubElement;
    private boolean[] fSubElementStack;
    private XSElementDecl fCurrentElemDecl;
    private XSElementDecl[] fElemDeclStack;
    private boolean fNil;
    private boolean[] fNilStack;
    private XSNotationDecl fNotation;
    private XSNotationDecl[] fNotationStack;
    private XSTypeDefinition fCurrentType;
    private XSTypeDefinition[] fTypeStack;
    private XSCMValidator fCurrentCM;
    private XSCMValidator[] fCMStack;
    private int[] fCurrCMState;
    private int[][] fCMStateStack;
    private boolean fStrictAssess;
    private boolean[] fStrictAssessStack;
    private final StringBuffer fBuffer;
    private boolean fAppendBuffer;
    private boolean fSawText;
    private boolean[] fSawTextStack;
    private boolean fSawCharacters;
    private boolean[] fStringContent;
    private final org.apache.xerces.xni.QName fTempQName;
    private QName fRootTypeQName;
    private XSTypeDefinition fRootTypeDefinition;
    private QName fRootElementDeclQName;
    private XSElementDecl fRootElementDeclaration;
    private int fIgnoreXSITypeDepth;
    private boolean fIDCChecking;
    private ValidatedInfo fValidatedInfo;
    private ValidationState fState4XsiType;
    private ValidationState fState4ApplyDefault;
    protected XPathMatcherStack fMatcherStack;
    protected ValueStoreCache fValueStoreCache;

    public String[] getRecognizedFeatures() {
        return (String[])RECOGNIZED_FEATURES.clone();
    }

    public void setFeature(String string, boolean bl) throws XMLConfigurationException {
    }

    public String[] getRecognizedProperties() {
        return (String[])RECOGNIZED_PROPERTIES.clone();
    }

    public void setProperty(String string, Object object) throws XMLConfigurationException {
        if (string.equals("http://apache.org/xml/properties/validation/schema/root-type-definition")) {
            if (object == null) {
                this.fRootTypeQName = null;
                this.fRootTypeDefinition = null;
            } else if (object instanceof QName) {
                this.fRootTypeQName = (QName)object;
                this.fRootTypeDefinition = null;
            } else {
                this.fRootTypeDefinition = (XSTypeDefinition)object;
                this.fRootTypeQName = null;
            }
        } else if (string.equals("http://apache.org/xml/properties/validation/schema/root-element-declaration")) {
            if (object == null) {
                this.fRootElementDeclQName = null;
                this.fRootElementDeclaration = null;
            } else if (object instanceof QName) {
                this.fRootElementDeclQName = (QName)object;
                this.fRootElementDeclaration = null;
            } else {
                this.fRootElementDeclaration = (XSElementDecl)object;
                this.fRootElementDeclQName = null;
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

    public void setDocumentSource(XMLDocumentSource xMLDocumentSource) {
        this.fDocumentSource = xMLDocumentSource;
    }

    public XMLDocumentSource getDocumentSource() {
        return this.fDocumentSource;
    }

    public void startDocument(XMLLocator xMLLocator, String string, NamespaceContext namespaceContext, Augmentations augmentations) throws XNIException {
        this.fValidationState.setNamespaceSupport(namespaceContext);
        this.fState4XsiType.setNamespaceSupport(namespaceContext);
        this.fState4ApplyDefault.setNamespaceSupport(namespaceContext);
        this.fLocator = xMLLocator;
        this.handleStartDocument(xMLLocator, string);
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.startDocument(xMLLocator, string, namespaceContext, augmentations);
        }
    }

    public void xmlDecl(String string, String string2, String string3, Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.xmlDecl(string, string2, string3, augmentations);
        }
    }

    public void doctypeDecl(String string, String string2, String string3, Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.doctypeDecl(string, string2, string3, augmentations);
        }
    }

    public void startElement(org.apache.xerces.xni.QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        Augmentations augmentations2 = this.handleStartElement(qName, xMLAttributes, augmentations);
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.startElement(qName, xMLAttributes, augmentations2);
        }
    }

    public void emptyElement(org.apache.xerces.xni.QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        Augmentations augmentations2 = this.handleStartElement(qName, xMLAttributes, augmentations);
        this.fDefaultValue = null;
        if (this.fElementDepth != -2) {
            augmentations2 = this.handleEndElement(qName, augmentations2);
        }
        if (this.fDocumentHandler != null) {
            if (!this.fSchemaElementDefault || this.fDefaultValue == null) {
                this.fDocumentHandler.emptyElement(qName, xMLAttributes, augmentations2);
            } else {
                this.fDocumentHandler.startElement(qName, xMLAttributes, augmentations2);
                this.fDocumentHandler.characters(this.fDefaultValue, null);
                this.fDocumentHandler.endElement(qName, augmentations2);
            }
        }
    }

    public void characters(XMLString xMLString, Augmentations augmentations) throws XNIException {
        xMLString = this.handleCharacters(xMLString);
        if (this.fDocumentHandler != null) {
            if (this.fNormalizeData && this.fUnionType) {
                if (augmentations != null) {
                    this.fDocumentHandler.characters(this.fEmptyXMLStr, augmentations);
                }
            } else {
                this.fDocumentHandler.characters(xMLString, augmentations);
            }
        }
    }

    public void ignorableWhitespace(XMLString xMLString, Augmentations augmentations) throws XNIException {
        this.handleIgnorableWhitespace(xMLString);
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.ignorableWhitespace(xMLString, augmentations);
        }
    }

    public void endElement(org.apache.xerces.xni.QName qName, Augmentations augmentations) throws XNIException {
        this.fDefaultValue = null;
        Augmentations augmentations2 = this.handleEndElement(qName, augmentations);
        if (this.fDocumentHandler != null) {
            if (!this.fSchemaElementDefault || this.fDefaultValue == null) {
                this.fDocumentHandler.endElement(qName, augmentations2);
            } else {
                this.fDocumentHandler.characters(this.fDefaultValue, null);
                this.fDocumentHandler.endElement(qName, augmentations2);
            }
        }
    }

    public void startCDATA(Augmentations augmentations) throws XNIException {
        this.fInCDATA = true;
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.startCDATA(augmentations);
        }
    }

    public void endCDATA(Augmentations augmentations) throws XNIException {
        this.fInCDATA = false;
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endCDATA(augmentations);
        }
    }

    public void endDocument(Augmentations augmentations) throws XNIException {
        this.handleEndDocument();
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endDocument(augmentations);
        }
        this.fLocator = null;
    }

    public boolean characterData(String string, Augmentations augmentations) {
        boolean bl = this.fSawText = this.fSawText || string.length() > 0;
        if (this.fNormalizeData && this.fWhiteSpace != -1 && this.fWhiteSpace != 0) {
            this.normalizeWhitespace(string, this.fWhiteSpace == 2);
            this.fBuffer.append(this.fNormalizedStr.ch, this.fNormalizedStr.offset, this.fNormalizedStr.length);
        } else if (this.fAppendBuffer) {
            this.fBuffer.append(string);
        }
        boolean bl2 = true;
        if (this.fCurrentType != null && this.fCurrentType.getTypeCategory() == 15) {
            XSComplexTypeDecl xSComplexTypeDecl = (XSComplexTypeDecl)this.fCurrentType;
            if (xSComplexTypeDecl.fContentType == 2) {
                int n2 = 0;
                while (n2 < string.length()) {
                    if (!XMLChar.isSpace(string.charAt(n2))) {
                        bl2 = false;
                        this.fSawCharacters = true;
                        break;
                    }
                    ++n2;
                }
            }
        }
        return bl2;
    }

    public void elementDefault(String string) {
    }

    public void startGeneralEntity(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
        this.fEntityRef = true;
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.startGeneralEntity(string, xMLResourceIdentifier, string2, augmentations);
        }
    }

    public void textDecl(String string, String string2, Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.textDecl(string, string2, augmentations);
        }
    }

    public void comment(XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.comment(xMLString, augmentations);
        }
    }

    public void processingInstruction(String string, XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.processingInstruction(string, xMLString, augmentations);
        }
    }

    public void endGeneralEntity(String string, Augmentations augmentations) throws XNIException {
        this.fEntityRef = false;
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endGeneralEntity(string, augmentations);
        }
    }

    public XMLSchemaValidator() {
        this.fXSIErrorReporter = new XSIErrorReporter(this);
        this.fValidationManager = null;
        this.fValidationState = new ConfigurableValidationState();
        this.fExternalSchemas = null;
        this.fExternalNoNamespaceSchema = null;
        this.fJaxpSchemaSource = null;
        this.fXSDDescription = new XSDDescription();
        this.fLocationPairs = new Hashtable();
        this.fExpandedLocationPairs = new Hashtable();
        this.fUnparsedLocations = new ArrayList();
        this.fEmptyXMLStr = new XMLString(null, 0, -1);
        this.fNormalizedStr = new XMLString();
        this.fFirstChunk = true;
        this.fTrailing = false;
        this.fWhiteSpace = -1;
        this.fUnionType = false;
        this.fGrammarBucket = new XSGrammarBucket();
        this.fSubGroupHandler = new SubstitutionGroupHandler(this.fGrammarBucket);
        this.fQNameDV = (XSSimpleType)SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl("QName");
        this.nodeFactory = new CMNodeFactory();
        this.fCMBuilder = new CMBuilder(this.nodeFactory);
        this.fSchemaLoader = new XMLSchemaLoader(this.fXSIErrorReporter.fErrorReporter, this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder);
        this.fSubElementStack = new boolean[8];
        this.fElemDeclStack = new XSElementDecl[8];
        this.fNilStack = new boolean[8];
        this.fNotationStack = new XSNotationDecl[8];
        this.fTypeStack = new XSTypeDefinition[8];
        this.fCMStack = new XSCMValidator[8];
        this.fCMStateStack = new int[8][];
        this.fStrictAssess = true;
        this.fStrictAssessStack = new boolean[8];
        this.fBuffer = new StringBuffer();
        this.fAppendBuffer = true;
        this.fSawText = false;
        this.fSawTextStack = new boolean[8];
        this.fSawCharacters = false;
        this.fStringContent = new boolean[8];
        this.fTempQName = new org.apache.xerces.xni.QName();
        this.fRootTypeQName = null;
        this.fRootTypeDefinition = null;
        this.fRootElementDeclQName = null;
        this.fRootElementDeclaration = null;
        this.fValidatedInfo = new ValidatedInfo();
        this.fState4XsiType = new ValidationState();
        this.fState4ApplyDefault = new ValidationState();
        this.fMatcherStack = new XPathMatcherStack();
        this.fValueStoreCache = new ValueStoreCache(this);
        this.fState4XsiType.setExtraChecking(false);
        this.fState4ApplyDefault.setFacetChecking(false);
    }

    public void reset(XMLComponentManager xMLComponentManager) throws XMLConfigurationException {
        boolean bl;
        Object object;
        boolean bl2;
        this.fIdConstraint = false;
        this.fLocationPairs.clear();
        this.fExpandedLocationPairs.clear();
        this.fValidationState.resetIDTables();
        this.fSchemaLoader.reset(xMLComponentManager);
        this.fCurrentElemDecl = null;
        this.fCurrentCM = null;
        this.fCurrCMState = null;
        this.fSkipValidationDepth = -1;
        this.fNFullValidationDepth = -1;
        this.fNNoneValidationDepth = -1;
        this.fElementDepth = -1;
        this.fSubElement = false;
        this.fSchemaDynamicValidation = false;
        this.fEntityRef = false;
        this.fInCDATA = false;
        this.fMatcherStack.clear();
        this.fXSIErrorReporter.reset((XMLErrorReporter)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
        try {
            bl = xMLComponentManager.getFeature("http://apache.org/xml/features/internal/parser-settings");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            bl = true;
        }
        if (!bl) {
            this.fValidationManager.addValidationState(this.fValidationState);
            this.nodeFactory.reset();
            XMLSchemaLoader.processExternalHints(this.fExternalSchemas, this.fExternalNoNamespaceSchema, this.fLocationPairs, this.fXSIErrorReporter.fErrorReporter);
            return;
        }
        this.nodeFactory.reset(xMLComponentManager);
        SymbolTable symbolTable = (SymbolTable)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        if (symbolTable != this.fSymbolTable) {
            this.fSymbolTable = symbolTable;
        }
        try {
            this.fNamespaceGrowth = xMLComponentManager.getFeature("http://apache.org/xml/features/namespace-growth");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fNamespaceGrowth = false;
        }
        try {
            this.fDynamicValidation = xMLComponentManager.getFeature("http://apache.org/xml/features/validation/dynamic");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fDynamicValidation = false;
        }
        if (this.fDynamicValidation) {
            this.fDoValidation = true;
        } else {
            try {
                this.fDoValidation = xMLComponentManager.getFeature("http://xml.org/sax/features/validation");
            }
            catch (XMLConfigurationException xMLConfigurationException) {
                this.fDoValidation = false;
            }
        }
        if (this.fDoValidation) {
            try {
                this.fDoValidation = xMLComponentManager.getFeature("http://apache.org/xml/features/validation/schema");
            }
            catch (XMLConfigurationException xMLConfigurationException) {
                // empty catch block
            }
        }
        try {
            this.fFullChecking = xMLComponentManager.getFeature("http://apache.org/xml/features/validation/schema-full-checking");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fFullChecking = false;
        }
        try {
            this.fNormalizeData = xMLComponentManager.getFeature("http://apache.org/xml/features/validation/schema/normalized-value");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fNormalizeData = false;
        }
        try {
            this.fSchemaElementDefault = xMLComponentManager.getFeature("http://apache.org/xml/features/validation/schema/element-default");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fSchemaElementDefault = false;
        }
        try {
            this.fAugPSVI = xMLComponentManager.getFeature("http://apache.org/xml/features/validation/schema/augment-psvi");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fAugPSVI = true;
        }
        try {
            this.fSchemaType = (String)xMLComponentManager.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fSchemaType = null;
        }
        try {
            this.fUseGrammarPoolOnly = xMLComponentManager.getFeature("http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fUseGrammarPoolOnly = false;
        }
        this.fEntityResolver = (XMLEntityResolver)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-manager");
        this.fValidationManager = (ValidationManager)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager");
        this.fValidationManager.addValidationState(this.fValidationState);
        this.fValidationState.setSymbolTable(this.fSymbolTable);
        try {
            object = xMLComponentManager.getProperty("http://apache.org/xml/properties/validation/schema/root-type-definition");
            if (object == null) {
                this.fRootTypeQName = null;
                this.fRootTypeDefinition = null;
            } else if (object instanceof QName) {
                this.fRootTypeQName = (QName)object;
                this.fRootTypeDefinition = null;
            } else {
                this.fRootTypeDefinition = (XSTypeDefinition)object;
                this.fRootTypeQName = null;
            }
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fRootTypeQName = null;
            this.fRootTypeDefinition = null;
        }
        try {
            object = xMLComponentManager.getProperty("http://apache.org/xml/properties/validation/schema/root-element-declaration");
            if (object == null) {
                this.fRootElementDeclQName = null;
                this.fRootElementDeclaration = null;
            } else if (object instanceof QName) {
                this.fRootElementDeclQName = (QName)object;
                this.fRootElementDeclaration = null;
            } else {
                this.fRootElementDeclaration = (XSElementDecl)object;
                this.fRootElementDeclQName = null;
            }
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fRootElementDeclQName = null;
            this.fRootElementDeclaration = null;
        }
        try {
            bl2 = xMLComponentManager.getFeature("http://apache.org/xml/features/validation/schema/ignore-xsi-type-until-elemdecl");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            bl2 = false;
        }
        this.fIgnoreXSITypeDepth = bl2 ? 0 : -1;
        try {
            this.fIDCChecking = xMLComponentManager.getFeature("http://apache.org/xml/features/validation/identity-constraint-checking");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fIDCChecking = true;
        }
        try {
            this.fValidationState.setIdIdrefChecking(xMLComponentManager.getFeature("http://apache.org/xml/features/validation/id-idref-checking"));
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fValidationState.setIdIdrefChecking(true);
        }
        try {
            this.fValidationState.setUnparsedEntityChecking(xMLComponentManager.getFeature("http://apache.org/xml/features/validation/unparsed-entity-checking"));
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fValidationState.setUnparsedEntityChecking(true);
        }
        try {
            this.fExternalSchemas = (String)xMLComponentManager.getProperty("http://apache.org/xml/properties/schema/external-schemaLocation");
            this.fExternalNoNamespaceSchema = (String)xMLComponentManager.getProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fExternalSchemas = null;
            this.fExternalNoNamespaceSchema = null;
        }
        XMLSchemaLoader.processExternalHints(this.fExternalSchemas, this.fExternalNoNamespaceSchema, this.fLocationPairs, this.fXSIErrorReporter.fErrorReporter);
        try {
            this.fJaxpSchemaSource = xMLComponentManager.getProperty("http://java.sun.com/xml/jaxp/properties/schemaSource");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fJaxpSchemaSource = null;
        }
        try {
            this.fGrammarPool = (XMLGrammarPool)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/grammar-pool");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fGrammarPool = null;
        }
        this.fState4XsiType.setSymbolTable(symbolTable);
        this.fState4ApplyDefault.setSymbolTable(symbolTable);
    }

    public void startValueScopeFor(IdentityConstraint identityConstraint, int n2) {
        ValueStoreBase valueStoreBase = this.fValueStoreCache.getValueStoreFor(identityConstraint, n2);
        valueStoreBase.startValueScope();
    }

    public XPathMatcher activateField(Field field, int n2) {
        ValueStoreBase valueStoreBase = this.fValueStoreCache.getValueStoreFor(field.getIdentityConstraint(), n2);
        XPathMatcher xPathMatcher = field.createMatcher(valueStoreBase);
        this.fMatcherStack.addMatcher(xPathMatcher);
        xPathMatcher.startDocumentFragment();
        return xPathMatcher;
    }

    public void endValueScopeFor(IdentityConstraint identityConstraint, int n2) {
        ValueStoreBase valueStoreBase = this.fValueStoreCache.getValueStoreFor(identityConstraint, n2);
        valueStoreBase.endValueScope();
    }

    private void activateSelectorFor(IdentityConstraint identityConstraint) {
        Selector selector = identityConstraint.getSelector();
        XMLSchemaValidator xMLSchemaValidator = this;
        if (selector == null) {
            return;
        }
        XPathMatcher xPathMatcher = selector.createMatcher(xMLSchemaValidator, this.fElementDepth);
        this.fMatcherStack.addMatcher(xPathMatcher);
        xPathMatcher.startDocumentFragment();
    }

    void ensureStackCapacity() {
        if (this.fElementDepth == this.fElemDeclStack.length) {
            int n2 = this.fElementDepth + 8;
            boolean[] arrbl = new boolean[n2];
            System.arraycopy(this.fSubElementStack, 0, arrbl, 0, this.fElementDepth);
            this.fSubElementStack = arrbl;
            XSElementDecl[] arrxSElementDecl = new XSElementDecl[n2];
            System.arraycopy(this.fElemDeclStack, 0, arrxSElementDecl, 0, this.fElementDepth);
            this.fElemDeclStack = arrxSElementDecl;
            arrbl = new boolean[n2];
            System.arraycopy(this.fNilStack, 0, arrbl, 0, this.fElementDepth);
            this.fNilStack = arrbl;
            XSNotationDecl[] arrxSNotationDecl = new XSNotationDecl[n2];
            System.arraycopy(this.fNotationStack, 0, arrxSNotationDecl, 0, this.fElementDepth);
            this.fNotationStack = arrxSNotationDecl;
            XSTypeDefinition[] arrxSTypeDefinition = new XSTypeDefinition[n2];
            System.arraycopy(this.fTypeStack, 0, arrxSTypeDefinition, 0, this.fElementDepth);
            this.fTypeStack = arrxSTypeDefinition;
            XSCMValidator[] arrxSCMValidator = new XSCMValidator[n2];
            System.arraycopy(this.fCMStack, 0, arrxSCMValidator, 0, this.fElementDepth);
            this.fCMStack = arrxSCMValidator;
            arrbl = new boolean[n2];
            System.arraycopy(this.fSawTextStack, 0, arrbl, 0, this.fElementDepth);
            this.fSawTextStack = arrbl;
            arrbl = new boolean[n2];
            System.arraycopy(this.fStringContent, 0, arrbl, 0, this.fElementDepth);
            this.fStringContent = arrbl;
            arrbl = new boolean[n2];
            System.arraycopy(this.fStrictAssessStack, 0, arrbl, 0, this.fElementDepth);
            this.fStrictAssessStack = arrbl;
            int[][] arrarrn = new int[n2][];
            System.arraycopy(this.fCMStateStack, 0, arrarrn, 0, this.fElementDepth);
            this.fCMStateStack = arrarrn;
        }
    }

    void handleStartDocument(XMLLocator xMLLocator, String string) {
        if (this.fIDCChecking) {
            this.fValueStoreCache.startDocument();
        }
        if (this.fAugPSVI) {
            this.fCurrentPSVI.fGrammars = null;
            this.fCurrentPSVI.fSchemaInformation = null;
        }
    }

    void handleEndDocument() {
        if (this.fIDCChecking) {
            this.fValueStoreCache.endDocument();
        }
    }

    XMLString handleCharacters(XMLString xMLString) {
        if (this.fSkipValidationDepth >= 0) {
            return xMLString;
        }
        boolean bl = this.fSawText = this.fSawText || xMLString.length > 0;
        if (this.fNormalizeData && this.fWhiteSpace != -1 && this.fWhiteSpace != 0) {
            this.normalizeWhitespace(xMLString, this.fWhiteSpace == 2);
            xMLString = this.fNormalizedStr;
        }
        if (this.fAppendBuffer) {
            this.fBuffer.append(xMLString.ch, xMLString.offset, xMLString.length);
        }
        if (this.fCurrentType != null && this.fCurrentType.getTypeCategory() == 15) {
            XSComplexTypeDecl xSComplexTypeDecl = (XSComplexTypeDecl)this.fCurrentType;
            if (xSComplexTypeDecl.fContentType == 2) {
                int n2 = xMLString.offset;
                while (n2 < xMLString.offset + xMLString.length) {
                    if (!XMLChar.isSpace(xMLString.ch[n2])) {
                        this.fSawCharacters = true;
                        break;
                    }
                    ++n2;
                }
            }
        }
        return xMLString;
    }

    private void normalizeWhitespace(XMLString xMLString, boolean bl) {
        boolean bl2 = bl;
        boolean bl3 = false;
        boolean bl4 = false;
        boolean bl5 = false;
        int n2 = xMLString.offset + xMLString.length;
        if (this.fNormalizedStr.ch == null || this.fNormalizedStr.ch.length < xMLString.length + 1) {
            this.fNormalizedStr.ch = new char[xMLString.length + 1];
        }
        this.fNormalizedStr.offset = 1;
        this.fNormalizedStr.length = 1;
        int n3 = xMLString.offset;
        while (n3 < n2) {
            char c2 = xMLString.ch[n3];
            if (XMLChar.isSpace(c2)) {
                if (!bl2) {
                    this.fNormalizedStr.ch[this.fNormalizedStr.length++] = 32;
                    bl2 = bl;
                }
                if (!bl3) {
                    bl4 = true;
                }
            } else {
                this.fNormalizedStr.ch[this.fNormalizedStr.length++] = c2;
                bl2 = false;
                bl3 = true;
            }
            ++n3;
        }
        if (bl2) {
            if (this.fNormalizedStr.length > 1) {
                --this.fNormalizedStr.length;
                bl5 = true;
            } else if (bl4 && !this.fFirstChunk) {
                bl5 = true;
            }
        }
        if (this.fNormalizedStr.length > 1 && !this.fFirstChunk && this.fWhiteSpace == 2) {
            if (this.fTrailing) {
                this.fNormalizedStr.offset = 0;
                this.fNormalizedStr.ch[0] = 32;
            } else if (bl4) {
                this.fNormalizedStr.offset = 0;
                this.fNormalizedStr.ch[0] = 32;
            }
        }
        this.fNormalizedStr.length -= this.fNormalizedStr.offset;
        this.fTrailing = bl5;
        if (bl5 || bl3) {
            this.fFirstChunk = false;
        }
    }

    private void normalizeWhitespace(String string, boolean bl) {
        boolean bl2 = bl;
        int n2 = string.length();
        if (this.fNormalizedStr.ch == null || this.fNormalizedStr.ch.length < n2) {
            this.fNormalizedStr.ch = new char[n2];
        }
        this.fNormalizedStr.offset = 0;
        this.fNormalizedStr.length = 0;
        int n3 = 0;
        while (n3 < n2) {
            char c2 = string.charAt(n3);
            if (XMLChar.isSpace(c2)) {
                if (!bl2) {
                    this.fNormalizedStr.ch[this.fNormalizedStr.length++] = 32;
                    bl2 = bl;
                }
            } else {
                this.fNormalizedStr.ch[this.fNormalizedStr.length++] = c2;
                bl2 = false;
            }
            ++n3;
        }
        if (bl2 && this.fNormalizedStr.length != 0) {
            --this.fNormalizedStr.length;
        }
    }

    void handleIgnorableWhitespace(XMLString xMLString) {
        if (this.fSkipValidationDepth >= 0) {
            return;
        }
    }

    Augmentations handleStartElement(org.apache.xerces.xni.QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) {
        int n2;
        int[] datatypeException;
        Object object;
        Object object2;
        void var7_10;
        Object object3;
        if (this.fElementDepth == -1 && this.fValidationManager.isGrammarFound() && this.fSchemaType == null) {
            this.fSchemaDynamicValidation = true;
        }
        String string = xMLAttributes.getValue(SchemaSymbols.URI_XSI, SchemaSymbols.XSI_SCHEMALOCATION);
        String string2 = xMLAttributes.getValue(SchemaSymbols.URI_XSI, SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION);
        this.storeLocations(string, string2);
        if (this.fSkipValidationDepth >= 0) {
            ++this.fElementDepth;
            if (this.fAugPSVI) {
                augmentations = this.getEmptyAugs(augmentations);
            }
            return augmentations;
        }
        Object object4 = null;
        if (this.fCurrentCM != null) {
            object4 = this.fCurrentCM.oneTransition(qName, this.fCurrCMState, this.fSubGroupHandler);
            if (this.fCurrCMState[0] == -1) {
                XSComplexTypeDecl xSComplexTypeDecl = (XSComplexTypeDecl)this.fCurrentType;
                if (xSComplexTypeDecl.fParticle != null && (object = this.fCurrentCM.whatCanGoHere(this.fCurrCMState)).size() > 0) {
                    object3 = this.expectedStr((Vector)object);
                    datatypeException = this.fCurrentCM.occurenceInfo(this.fCurrCMState);
                    if (datatypeException != null) {
                        object2 = datatypeException[0];
                        n2 = datatypeException[1];
                        int n3 = datatypeException[2];
                        if (n3 < object2) {
                            reference var14_22 = object2 - n3;
                            if (var14_22 > true) {
                                this.reportSchemaError("cvc-complex-type.2.4.h", new Object[]{qName.rawname, this.fCurrentCM.getTermName(datatypeException[3]), Integer.toString((int)object2), Integer.toString((int)var14_22)});
                            } else {
                                this.reportSchemaError("cvc-complex-type.2.4.g", new Object[]{qName.rawname, this.fCurrentCM.getTermName(datatypeException[3]), Integer.toString((int)object2)});
                            }
                        } else if (n3 >= n2 && n2 != -1) {
                            this.reportSchemaError("cvc-complex-type.2.4.e", new Object[]{qName.rawname, object3, Integer.toString(n2)});
                        } else {
                            this.reportSchemaError("cvc-complex-type.2.4.a", new Object[]{qName.rawname, object3});
                        }
                    } else {
                        this.reportSchemaError("cvc-complex-type.2.4.a", new Object[]{qName.rawname, object3});
                    }
                } else {
                    object3 = this.fCurrentCM.occurenceInfo(this.fCurrCMState);
                    if (object3 != null) {
                        object2 = object3[2];
                        int n4 = object3[1];
                        if (object2 >= n4 && n4 != -1) {
                            this.reportSchemaError("cvc-complex-type.2.4.f", new Object[]{qName.rawname, Integer.toString(n4)});
                        } else {
                            this.reportSchemaError("cvc-complex-type.2.4.d", new Object[]{qName.rawname});
                        }
                    } else {
                        this.reportSchemaError("cvc-complex-type.2.4.d", new Object[]{qName.rawname});
                    }
                }
            }
        }
        if (this.fElementDepth != -1) {
            this.ensureStackCapacity();
            this.fSubElementStack[this.fElementDepth] = true;
            this.fSubElement = false;
            this.fElemDeclStack[this.fElementDepth] = this.fCurrentElemDecl;
            this.fNilStack[this.fElementDepth] = this.fNil;
            this.fNotationStack[this.fElementDepth] = this.fNotation;
            this.fTypeStack[this.fElementDepth] = this.fCurrentType;
            this.fStrictAssessStack[this.fElementDepth] = this.fStrictAssess;
            this.fCMStack[this.fElementDepth] = this.fCurrentCM;
            this.fCMStateStack[this.fElementDepth] = this.fCurrCMState;
            this.fSawTextStack[this.fElementDepth] = this.fSawText;
            this.fStringContent[this.fElementDepth] = this.fSawCharacters;
        }
        ++this.fElementDepth;
        this.fCurrentElemDecl = null;
        Object var7_8 = null;
        this.fCurrentType = null;
        this.fStrictAssess = true;
        this.fNil = false;
        this.fNotation = null;
        this.fBuffer.setLength(0);
        this.fSawText = false;
        this.fSawCharacters = false;
        if (object4 != null) {
            if (object4 instanceof XSElementDecl) {
                this.fCurrentElemDecl = (XSElementDecl)object4;
            } else {
                XSWildcardDecl xSWildcardDecl = (XSWildcardDecl)object4;
            }
        }
        if (var7_10 != null && var7_10.fProcessContents == 2) {
            this.fSkipValidationDepth = this.fElementDepth;
            if (this.fAugPSVI) {
                augmentations = this.getEmptyAugs(augmentations);
            }
            return augmentations;
        }
        if (this.fElementDepth == 0) {
            if (this.fRootElementDeclaration != null) {
                this.fCurrentElemDecl = this.fRootElementDeclaration;
                this.checkElementMatchesRootElementDecl(this.fCurrentElemDecl, qName);
            } else if (this.fRootElementDeclQName != null) {
                this.processRootElementDeclQName(this.fRootElementDeclQName, qName);
            } else if (this.fRootTypeDefinition != null) {
                this.fCurrentType = this.fRootTypeDefinition;
            } else if (this.fRootTypeQName != null) {
                this.processRootTypeQName(this.fRootTypeQName);
            }
        }
        if (this.fCurrentType == null) {
            if (this.fCurrentElemDecl == null && (object = this.findSchemaGrammar(5, qName.uri, null, qName, xMLAttributes)) != null) {
                this.fCurrentElemDecl = object.getGlobalElementDecl(qName.localpart);
            }
            if (this.fCurrentElemDecl != null) {
                this.fCurrentType = this.fCurrentElemDecl.fType;
            }
        }
        if (this.fElementDepth == this.fIgnoreXSITypeDepth && this.fCurrentElemDecl == null) {
            ++this.fIgnoreXSITypeDepth;
        }
        object = null;
        if (this.fElementDepth >= this.fIgnoreXSITypeDepth) {
            object = xMLAttributes.getValue(SchemaSymbols.URI_XSI, SchemaSymbols.XSI_TYPE);
        }
        if (this.fCurrentType == null && object == null) {
            if (this.fElementDepth == 0) {
                if (this.fDynamicValidation || this.fSchemaDynamicValidation) {
                    if (this.fDocumentSource != null) {
                        this.fDocumentSource.setDocumentHandler(this.fDocumentHandler);
                        if (this.fDocumentHandler != null) {
                            this.fDocumentHandler.setDocumentSource(this.fDocumentSource);
                        }
                        this.fElementDepth = -2;
                        return augmentations;
                    }
                    this.fSkipValidationDepth = this.fElementDepth;
                    if (this.fAugPSVI) {
                        augmentations = this.getEmptyAugs(augmentations);
                    }
                    return augmentations;
                }
                this.fXSIErrorReporter.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "cvc-elt.1.a", new Object[]{qName.rawname}, 1);
            } else if (var7_10 != null && var7_10.fProcessContents == 1) {
                this.reportSchemaError("cvc-complex-type.2.4.c", new Object[]{qName.rawname});
            }
            this.fCurrentType = SchemaGrammar.fAnyType;
            this.fStrictAssess = false;
            this.fNFullValidationDepth = this.fElementDepth;
            this.fAppendBuffer = false;
            this.fXSIErrorReporter.pushContext();
        } else {
            this.fXSIErrorReporter.pushContext();
            if (object != null) {
                object3 = this.fCurrentType;
                this.fCurrentType = this.getAndCheckXsiType(qName, (String)object, xMLAttributes);
                if (this.fCurrentType == null) {
                    this.fCurrentType = object3 == null ? SchemaGrammar.fAnyType : object3;
                }
            }
            this.fNNoneValidationDepth = this.fElementDepth;
            if (this.fCurrentElemDecl != null && this.fCurrentElemDecl.getConstraintType() == 2) {
                this.fAppendBuffer = true;
            } else if (this.fCurrentType.getTypeCategory() == 16) {
                this.fAppendBuffer = true;
            } else {
                object3 = (XSComplexTypeDecl)this.fCurrentType;
                boolean bl = this.fAppendBuffer = object3.fContentType == 1;
            }
        }
        if (this.fCurrentElemDecl != null && this.fCurrentElemDecl.getAbstract()) {
            this.reportSchemaError("cvc-elt.2", new Object[]{qName.rawname});
        }
        if (this.fElementDepth == 0) {
            this.fValidationRoot = qName.rawname;
        }
        if (this.fNormalizeData) {
            this.fFirstChunk = true;
            this.fTrailing = false;
            this.fUnionType = false;
            this.fWhiteSpace = -1;
        }
        if (this.fCurrentType.getTypeCategory() == 15) {
            object3 = (XSComplexTypeDecl)this.fCurrentType;
            if (object3.getAbstract()) {
                this.reportSchemaError("cvc-type.2", new Object[]{qName.rawname});
            }
            if (this.fNormalizeData && object3.fContentType == 1) {
                if (object3.fXSSimpleType.getVariety() == 3) {
                    this.fUnionType = true;
                } else {
                    try {
                        this.fWhiteSpace = object3.fXSSimpleType.getWhitespace();
                    }
                    catch (DatatypeException datatypeException2) {}
                }
            }
        } else if (this.fNormalizeData) {
            object3 = (XSSimpleType)this.fCurrentType;
            if (object3.getVariety() == 3) {
                this.fUnionType = true;
            } else {
                try {
                    this.fWhiteSpace = object3.getWhitespace();
                }
                catch (DatatypeException datatypeException3) {
                    // empty catch block
                }
            }
        }
        this.fCurrentCM = null;
        if (this.fCurrentType.getTypeCategory() == 15) {
            this.fCurrentCM = ((XSComplexTypeDecl)this.fCurrentType).getContentModel(this.fCMBuilder);
        }
        this.fCurrCMState = null;
        if (this.fCurrentCM != null) {
            this.fCurrCMState = this.fCurrentCM.startContentModel();
        }
        if ((object3 = xMLAttributes.getValue(SchemaSymbols.URI_XSI, SchemaSymbols.XSI_NIL)) != null && this.fCurrentElemDecl != null) {
            this.fNil = this.getXsiNil(qName, (String)object3);
        }
        datatypeException = null;
        if (this.fCurrentType.getTypeCategory() == 15) {
            XSComplexTypeDecl xSComplexTypeDecl = (XSComplexTypeDecl)this.fCurrentType;
            datatypeException = xSComplexTypeDecl.getAttrGrp();
        }
        if (this.fIDCChecking) {
            this.fValueStoreCache.startElement();
            this.fMatcherStack.pushContext();
            if (this.fCurrentElemDecl != null && this.fCurrentElemDecl.fIDCPos > 0) {
                this.fIdConstraint = true;
                this.fValueStoreCache.initValueStoresFor(this.fCurrentElemDecl, this);
            }
        }
        this.processAttributes(qName, xMLAttributes, (XSAttributeGroupDecl)datatypeException);
        if (datatypeException != null) {
            this.addDefaultAttributes(qName, xMLAttributes, (XSAttributeGroupDecl)datatypeException);
        }
        object2 = this.fMatcherStack.getMatcherCount();
        n2 = 0;
        while (n2 < object2) {
            XPathMatcher xPathMatcher = this.fMatcherStack.getMatcherAt(n2);
            xPathMatcher.startElement(qName, xMLAttributes);
            ++n2;
        }
        if (this.fAugPSVI) {
            augmentations = this.getEmptyAugs(augmentations);
            this.fCurrentPSVI.fValidationContext = this.fValidationRoot;
            this.fCurrentPSVI.fDeclaration = this.fCurrentElemDecl;
            this.fCurrentPSVI.fTypeDecl = this.fCurrentType;
            this.fCurrentPSVI.fNotation = this.fNotation;
            this.fCurrentPSVI.fNil = this.fNil;
        }
        return augmentations;
    }

    Augmentations handleEndElement(org.apache.xerces.xni.QName qName, Augmentations augmentations) {
        int n2;
        if (this.fSkipValidationDepth >= 0) {
            if (this.fSkipValidationDepth == this.fElementDepth && this.fSkipValidationDepth > 0) {
                this.fNFullValidationDepth = this.fSkipValidationDepth - 1;
                this.fSkipValidationDepth = -1;
                --this.fElementDepth;
                this.fSubElement = this.fSubElementStack[this.fElementDepth];
                this.fCurrentElemDecl = this.fElemDeclStack[this.fElementDepth];
                this.fNil = this.fNilStack[this.fElementDepth];
                this.fNotation = this.fNotationStack[this.fElementDepth];
                this.fCurrentType = this.fTypeStack[this.fElementDepth];
                this.fCurrentCM = this.fCMStack[this.fElementDepth];
                this.fStrictAssess = this.fStrictAssessStack[this.fElementDepth];
                this.fCurrCMState = this.fCMStateStack[this.fElementDepth];
                this.fSawText = this.fSawTextStack[this.fElementDepth];
                this.fSawCharacters = this.fStringContent[this.fElementDepth];
            } else {
                --this.fElementDepth;
            }
            if (this.fElementDepth == -1 && this.fFullChecking && !this.fUseGrammarPoolOnly) {
                XSConstraints.fullSchemaChecking(this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder, this.fXSIErrorReporter.fErrorReporter);
            }
            if (this.fAugPSVI) {
                augmentations = this.getEmptyAugs(augmentations);
            }
            return augmentations;
        }
        this.processElementContent(qName);
        if (this.fIDCChecking) {
            Object object;
            XPathMatcher xPathMatcher;
            int n3 = this.fMatcherStack.getMatcherCount();
            int n4 = n3 - 1;
            while (n4 >= 0) {
                XPathMatcher xPathMatcher2 = this.fMatcherStack.getMatcherAt(n4);
                if (this.fCurrentElemDecl == null) {
                    xPathMatcher2.endElement(qName, this.fCurrentType, false, this.fValidatedInfo.actualValue, this.fValidatedInfo.actualValueType, this.fValidatedInfo.itemValueTypes);
                } else {
                    xPathMatcher2.endElement(qName, this.fCurrentType, this.fCurrentElemDecl.getNillable(), this.fDefaultValue == null ? this.fValidatedInfo.actualValue : this.fCurrentElemDecl.fDefault.actualValue, this.fDefaultValue == null ? this.fValidatedInfo.actualValueType : this.fCurrentElemDecl.fDefault.actualValueType, this.fDefaultValue == null ? this.fValidatedInfo.itemValueTypes : this.fCurrentElemDecl.fDefault.itemValueTypes);
                }
                --n4;
            }
            if (this.fMatcherStack.size() > 0) {
                this.fMatcherStack.popContext();
            }
            n2 = this.fMatcherStack.getMatcherCount();
            int n5 = n3 - 1;
            while (n5 >= n2) {
                XPathMatcher xPathMatcher3 = this.fMatcherStack.getMatcherAt(n5);
                if (xPathMatcher3 instanceof Selector.Matcher && (object = (xPathMatcher = (Selector.Matcher)xPathMatcher3).getIdentityConstraint()) != null && object.getCategory() != 2) {
                    this.fValueStoreCache.transplant((IdentityConstraint)object, xPathMatcher.getInitialDepth());
                }
                --n5;
            }
            int n6 = n3 - 1;
            while (n6 >= n2) {
                ValueStoreBase valueStoreBase;
                IdentityConstraint identityConstraint;
                xPathMatcher = this.fMatcherStack.getMatcherAt(n6);
                if (xPathMatcher instanceof Selector.Matcher && (identityConstraint = (object = xPathMatcher).getIdentityConstraint()) != null && identityConstraint.getCategory() == 2 && (valueStoreBase = this.fValueStoreCache.getValueStoreFor(identityConstraint, object.getInitialDepth())) != null) {
                    valueStoreBase.endDocumentFragment();
                }
                --n6;
            }
            this.fValueStoreCache.endElement();
        }
        if (this.fElementDepth < this.fIgnoreXSITypeDepth) {
            --this.fIgnoreXSITypeDepth;
        }
        Grammar[] arrgrammar = null;
        if (this.fElementDepth == 0) {
            String string = this.fValidationState.checkIDRefID();
            this.fValidationState.resetIDTables();
            if (string != null) {
                this.reportSchemaError("cvc-id.1", new Object[]{string});
            }
            if (this.fFullChecking && !this.fUseGrammarPoolOnly) {
                XSConstraints.fullSchemaChecking(this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder, this.fXSIErrorReporter.fErrorReporter);
            }
            arrgrammar = this.fGrammarBucket.getGrammars();
            if (this.fGrammarPool != null) {
                n2 = 0;
                while (n2 < arrgrammar.length) {
                    arrgrammar[n2].setImmutable(true);
                    ++n2;
                }
                this.fGrammarPool.cacheGrammars("http://www.w3.org/2001/XMLSchema", arrgrammar);
            }
            augmentations = this.endElementPSVI(true, (SchemaGrammar[])arrgrammar, augmentations);
        } else {
            augmentations = this.endElementPSVI(false, (SchemaGrammar[])arrgrammar, augmentations);
            --this.fElementDepth;
            this.fSubElement = this.fSubElementStack[this.fElementDepth];
            this.fCurrentElemDecl = this.fElemDeclStack[this.fElementDepth];
            this.fNil = this.fNilStack[this.fElementDepth];
            this.fNotation = this.fNotationStack[this.fElementDepth];
            this.fCurrentType = this.fTypeStack[this.fElementDepth];
            this.fCurrentCM = this.fCMStack[this.fElementDepth];
            this.fStrictAssess = this.fStrictAssessStack[this.fElementDepth];
            this.fCurrCMState = this.fCMStateStack[this.fElementDepth];
            this.fSawText = this.fSawTextStack[this.fElementDepth];
            this.fSawCharacters = this.fStringContent[this.fElementDepth];
            this.fWhiteSpace = -1;
            this.fAppendBuffer = false;
            this.fUnionType = false;
        }
        return augmentations;
    }

    final Augmentations endElementPSVI(boolean bl, SchemaGrammar[] arrschemaGrammar, Augmentations augmentations) {
        if (this.fAugPSVI) {
            augmentations = this.getEmptyAugs(augmentations);
            this.fCurrentPSVI.fDeclaration = this.fCurrentElemDecl;
            this.fCurrentPSVI.fTypeDecl = this.fCurrentType;
            this.fCurrentPSVI.fNotation = this.fNotation;
            this.fCurrentPSVI.fValidationContext = this.fValidationRoot;
            this.fCurrentPSVI.fNil = this.fNil;
            this.fCurrentPSVI.fValidationAttempted = this.fElementDepth > this.fNFullValidationDepth ? 2 : (this.fElementDepth > this.fNNoneValidationDepth ? 0 : 1);
            if (this.fNFullValidationDepth == this.fElementDepth) {
                this.fNFullValidationDepth = this.fElementDepth - 1;
            }
            if (this.fNNoneValidationDepth == this.fElementDepth) {
                this.fNNoneValidationDepth = this.fElementDepth - 1;
            }
            if (this.fDefaultValue != null) {
                this.fCurrentPSVI.fSpecified = true;
            }
            this.fCurrentPSVI.fMemberType = this.fValidatedInfo.memberType;
            this.fCurrentPSVI.fNormalizedValue = this.fValidatedInfo.normalizedValue;
            this.fCurrentPSVI.fActualValue = this.fValidatedInfo.actualValue;
            this.fCurrentPSVI.fActualValueType = this.fValidatedInfo.actualValueType;
            this.fCurrentPSVI.fItemValueTypes = this.fValidatedInfo.itemValueTypes;
            if (this.fStrictAssess) {
                String[] arrstring = this.fXSIErrorReporter.mergeContext();
                this.fCurrentPSVI.fErrors = arrstring;
                this.fCurrentPSVI.fValidity = arrstring == null ? 2 : 1;
            } else {
                this.fCurrentPSVI.fValidity = 0;
                this.fXSIErrorReporter.popContext();
            }
            if (bl) {
                this.fCurrentPSVI.fGrammars = arrschemaGrammar;
                this.fCurrentPSVI.fSchemaInformation = null;
            }
        }
        return augmentations;
    }

    Augmentations getEmptyAugs(Augmentations augmentations) {
        if (augmentations == null) {
            augmentations = this.fAugmentations;
            augmentations.removeAllItems();
        }
        augmentations.putItem("ELEMENT_PSVI", this.fCurrentPSVI);
        this.fCurrentPSVI.reset();
        return augmentations;
    }

    void storeLocations(String string, String string2) {
        if (string != null && !XMLSchemaLoader.tokenizeSchemaLocationStr(string, this.fLocationPairs)) {
            this.fXSIErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "SchemaLocation", new Object[]{string}, 0);
        }
        if (string2 != null) {
            XMLSchemaLoader.LocationArray locationArray = (XMLSchemaLoader.LocationArray)this.fLocationPairs.get(XMLSymbols.EMPTY_STRING);
            if (locationArray == null) {
                locationArray = new XMLSchemaLoader.LocationArray();
                this.fLocationPairs.put(XMLSymbols.EMPTY_STRING, locationArray);
            }
            locationArray.addLocation(string2);
        }
    }

    SchemaGrammar findSchemaGrammar(short s2, String string, org.apache.xerces.xni.QName qName, org.apache.xerces.xni.QName qName2, XMLAttributes xMLAttributes) {
        SchemaGrammar schemaGrammar = null;
        schemaGrammar = this.fGrammarBucket.getGrammar(string);
        if (schemaGrammar == null) {
            this.fXSDDescription.setNamespace(string);
            if (this.fGrammarPool != null && (schemaGrammar = (SchemaGrammar)this.fGrammarPool.retrieveGrammar(this.fXSDDescription)) != null && !this.fGrammarBucket.putGrammar(schemaGrammar, true, this.fNamespaceGrowth)) {
                this.fXSIErrorReporter.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "GrammarConflict", null, 0);
                schemaGrammar = null;
            }
        }
        if (!this.fUseGrammarPoolOnly && (schemaGrammar == null || this.fNamespaceGrowth && !this.hasSchemaComponent(schemaGrammar, s2, qName2))) {
            Hashtable hashtable;
            String[] arrstring;
            Object v2;
            this.fXSDDescription.reset();
            this.fXSDDescription.fContextType = s2;
            this.fXSDDescription.setNamespace(string);
            this.fXSDDescription.fEnclosedElementName = qName;
            this.fXSDDescription.fTriggeringComponent = qName2;
            this.fXSDDescription.fAttributes = xMLAttributes;
            if (this.fLocator != null) {
                this.fXSDDescription.setBaseSystemId(this.fLocator.getExpandedSystemId());
            }
            if ((v2 = (hashtable = this.fLocationPairs).get(string == null ? XMLSymbols.EMPTY_STRING : string)) != null && (arrstring = ((XMLSchemaLoader.LocationArray)v2).getLocationArray()).length != 0) {
                this.setLocationHints(this.fXSDDescription, arrstring, schemaGrammar);
            }
            if (schemaGrammar == null || this.fXSDDescription.fLocationHints != null) {
                boolean bl = true;
                if (schemaGrammar != null) {
                    hashtable = EMPTY_TABLE;
                }
                try {
                    XMLInputSource xMLInputSource = XMLSchemaLoader.resolveDocument(this.fXSDDescription, hashtable, this.fEntityResolver);
                    if (schemaGrammar != null && this.fNamespaceGrowth) {
                        try {
                            if (schemaGrammar.getDocumentLocations().contains(XMLEntityManager.expandSystemId(xMLInputSource.getSystemId(), xMLInputSource.getBaseSystemId(), false))) {
                                bl = false;
                            }
                        }
                        catch (URI.MalformedURIException malformedURIException) {
                            // empty catch block
                        }
                    }
                    if (bl) {
                        schemaGrammar = this.fSchemaLoader.loadSchema(this.fXSDDescription, xMLInputSource, this.fLocationPairs);
                    }
                }
                catch (IOException iOException) {
                    String[] arrstring2 = this.fXSDDescription.getLocationHints();
                    Object[] arrobject = new Object[1];
                    arrobject[0] = arrstring2 != null ? arrstring2[0] : XMLSymbols.EMPTY_STRING;
                    this.fXSIErrorReporter.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "schema_reference.4", arrobject, 0, iOException);
                }
            }
        }
        return schemaGrammar;
    }

    private boolean hasSchemaComponent(SchemaGrammar schemaGrammar, short s2, org.apache.xerces.xni.QName qName) {
        String string;
        if (schemaGrammar != null && qName != null && (string = qName.localpart) != null && string.length() > 0) {
            switch (s2) {
                case 5: {
                    return schemaGrammar.getElementDeclaration(string) != null;
                }
                case 6: {
                    return schemaGrammar.getAttributeDeclaration(string) != null;
                }
                case 7: {
                    return schemaGrammar.getTypeDefinition(string) != null;
                }
            }
        }
        return false;
    }

    private void setLocationHints(XSDDescription xSDDescription, String[] arrstring, SchemaGrammar schemaGrammar) {
        int n2 = arrstring.length;
        if (schemaGrammar == null) {
            this.fXSDDescription.fLocationHints = new String[n2];
            System.arraycopy(arrstring, 0, this.fXSDDescription.fLocationHints, 0, n2);
        } else {
            this.setLocationHints(xSDDescription, arrstring, schemaGrammar.getDocumentLocations());
        }
    }

    private void setLocationHints(XSDDescription xSDDescription, String[] arrstring, StringList stringList) {
        int n2 = arrstring.length;
        String[] arrstring2 = new String[n2];
        int n3 = 0;
        int n4 = 0;
        while (n4 < n2) {
            try {
                String string = XMLEntityManager.expandSystemId(arrstring[n4], xSDDescription.getBaseSystemId(), false);
                if (!stringList.contains(string)) {
                    arrstring2[n3++] = arrstring[n4];
                }
            }
            catch (URI.MalformedURIException malformedURIException) {
                // empty catch block
            }
            ++n4;
        }
        if (n3 > 0) {
            if (n3 == n2) {
                this.fXSDDescription.fLocationHints = arrstring2;
            } else {
                this.fXSDDescription.fLocationHints = new String[n3];
                System.arraycopy(arrstring2, 0, this.fXSDDescription.fLocationHints, 0, n3);
            }
        }
    }

    XSTypeDefinition getAndCheckXsiType(org.apache.xerces.xni.QName qName, String string, XMLAttributes xMLAttributes) {
        SchemaGrammar schemaGrammar;
        org.apache.xerces.xni.QName qName2 = null;
        try {
            qName2 = (org.apache.xerces.xni.QName)this.fQNameDV.validate(string, (ValidationContext)this.fValidationState, null);
        }
        catch (InvalidDatatypeValueException invalidDatatypeValueException) {
            this.reportSchemaError(invalidDatatypeValueException.getKey(), invalidDatatypeValueException.getArgs());
            this.reportSchemaError("cvc-elt.4.1", new Object[]{qName.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_TYPE, string});
            return null;
        }
        XSTypeDefinition xSTypeDefinition = null;
        if (qName2.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA) {
            xSTypeDefinition = SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(qName2.localpart);
        }
        if (xSTypeDefinition == null && (schemaGrammar = this.findSchemaGrammar(7, qName2.uri, qName, qName2, xMLAttributes)) != null) {
            xSTypeDefinition = schemaGrammar.getGlobalTypeDecl(qName2.localpart);
        }
        if (xSTypeDefinition == null) {
            this.reportSchemaError("cvc-elt.4.2", new Object[]{qName.rawname, string});
            return null;
        }
        if (this.fCurrentType != null) {
            short s2 = 0;
            if (this.fCurrentElemDecl != null) {
                s2 = this.fCurrentElemDecl.fBlock;
            }
            if (this.fCurrentType.getTypeCategory() == 15) {
                s2 = (short)(s2 | ((XSComplexTypeDecl)this.fCurrentType).fBlock);
            }
            if (!XSConstraints.checkTypeDerivationOk(xSTypeDefinition, this.fCurrentType, s2)) {
                this.reportSchemaError("cvc-elt.4.3", new Object[]{qName.rawname, string, this.fCurrentType.getName()});
            }
        }
        return xSTypeDefinition;
    }

    boolean getXsiNil(org.apache.xerces.xni.QName qName, String string) {
        if (this.fCurrentElemDecl != null && !this.fCurrentElemDecl.getNillable()) {
            this.reportSchemaError("cvc-elt.3.1", new Object[]{qName.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL});
        } else {
            String string2 = XMLChar.trim(string);
            if (string2.equals("true") || string2.equals("1")) {
                if (this.fCurrentElemDecl != null && this.fCurrentElemDecl.getConstraintType() == 2) {
                    this.reportSchemaError("cvc-elt.3.2.2", new Object[]{qName.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL});
                }
                return true;
            }
        }
        return false;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    void processAttributes(org.apache.xerces.xni.QName var1_1, XMLAttributes var2_2, XSAttributeGroupDecl var3_3) {
        var4_4 = null;
        var5_5 = var2_2.getLength();
        var6_6 = null;
        var7_7 = null;
        var8_8 = this.fCurrentType == null || this.fCurrentType.getTypeCategory() == 16;
        var9_9 = null;
        var10_10 = 0;
        var11_11 = null;
        if (!var8_8) {
            var9_9 = var3_3.getAttributeUses();
            var10_10 = var9_9.getLength();
            var11_11 = var3_3.fAttributeWC;
        }
        var12_12 = 0;
        while (var12_12 < var5_5) {
            var2_2.getName(var12_12, this.fTempQName);
            if (this.fAugPSVI || this.fIdConstraint) {
                var6_6 = var2_2.getAugmentations(var12_12);
                var7_7 = (AttributePSVImpl)var6_6.getItem("ATTRIBUTE_PSVI");
                if (var7_7 != null) {
                    var7_7.reset();
                } else {
                    var7_7 = new AttributePSVImpl();
                    var6_6.putItem("ATTRIBUTE_PSVI", var7_7);
                }
                var7_7.fValidationContext = this.fValidationRoot;
            }
            if (this.fTempQName.uri != SchemaSymbols.URI_XSI) ** GOTO lbl41
            var13_14 = null;
            if (this.fTempQName.localpart == SchemaSymbols.XSI_TYPE) {
                var13_15 = XMLSchemaValidator.XSI_TYPE;
            } else if (this.fTempQName.localpart == SchemaSymbols.XSI_NIL) {
                var13_16 = XMLSchemaValidator.XSI_NIL;
            } else if (this.fTempQName.localpart == SchemaSymbols.XSI_SCHEMALOCATION) {
                var13_17 = XMLSchemaValidator.XSI_SCHEMALOCATION;
            } else if (this.fTempQName.localpart == SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION) {
                var13_18 = XMLSchemaValidator.XSI_NONAMESPACESCHEMALOCATION;
            }
            if (var13_19 == null) ** GOTO lbl41
            this.processOneAttribute(var1_1, var2_2, var12_12, (XSAttributeDecl)var13_19, null, var7_7);
            ** GOTO lbl76
lbl41: // 2 sources:
            if (this.fTempQName.rawname == XMLSymbols.PREFIX_XMLNS || this.fTempQName.rawname.startsWith("xmlns:")) ** GOTO lbl76
            if (!var8_8) ** GOTO lbl45
            this.reportSchemaError("cvc-type.3.1.1", new Object[]{var1_1.rawname, this.fTempQName.rawname});
            ** GOTO lbl76
lbl45: // 1 sources:
            var13_20 = null;
            var15_24 = 0;
            while (var15_24 < var10_10) {
                var14_23 = (XSAttributeUseImpl)var9_9.item(var15_24);
                if (var14_23.fAttrDecl.fName == this.fTempQName.localpart && var14_23.fAttrDecl.fTargetNamespace == this.fTempQName.uri) {
                    var13_21 = var14_23;
                    break;
                }
                ++var15_24;
            }
            if (var13_22 != null || var11_11 != null && var11_11.allowNamespace(this.fTempQName.uri)) ** GOTO lbl58
            this.reportSchemaError("cvc-complex-type.3.2.2", new Object[]{var1_1.rawname, this.fTempQName.rawname});
            this.fNFullValidationDepth = this.fElementDepth;
            ** GOTO lbl76
lbl58: // 1 sources:
            var16_25 = null;
            if (var13_22 == null) ** GOTO lbl62
            var16_25 = var13_22.fAttrDecl;
            ** GOTO lbl75
lbl62: // 1 sources:
            if (var11_11.fProcessContents != 2) {
                var17_26 = this.findSchemaGrammar(6, this.fTempQName.uri, var1_1, this.fTempQName, var2_2);
                if (var17_26 != null) {
                    var16_25 = var17_26.getGlobalAttributeDecl(this.fTempQName.localpart);
                }
                if (var16_25 == null) {
                    if (var11_11.fProcessContents == 1) {
                        this.reportSchemaError("cvc-complex-type.3.2.2", new Object[]{var1_1.rawname, this.fTempQName.rawname});
                    }
                } else {
                    if (var16_25.fType.getTypeCategory() == 16 && var16_25.fType.isIDType()) {
                        if (var4_4 != null) {
                            this.reportSchemaError("cvc-complex-type.5.1", new Object[]{var1_1.rawname, var16_25.fName, var4_4});
                        } else {
                            var4_4 = var16_25.fName;
                        }
                    }
lbl75: // 5 sources:
                    this.processOneAttribute(var1_1, var2_2, var12_12, var16_25, (XSAttributeUseImpl)var13_22, var7_7);
                }
            }
lbl76: // 8 sources:
            ++var12_12;
        }
        if (var8_8 != false) return;
        if (var3_3.fIDAttrName == null) return;
        if (var4_4 == null) return;
        this.reportSchemaError("cvc-complex-type.5.2", new Object[]{var1_1.rawname, var4_4, var3_3.fIDAttrName});
    }

    void processOneAttribute(org.apache.xerces.xni.QName qName, XMLAttributes xMLAttributes, int n2, XSAttributeDecl xSAttributeDecl, XSAttributeUseImpl xSAttributeUseImpl, AttributePSVImpl attributePSVImpl) {
        String[] arrstring;
        String string = xMLAttributes.getValue(n2);
        this.fXSIErrorReporter.pushContext();
        XSSimpleType xSSimpleType = xSAttributeDecl.fType;
        Object object = null;
        try {
            object = xSSimpleType.validate(string, (ValidationContext)this.fValidationState, this.fValidatedInfo);
            if (this.fNormalizeData) {
                xMLAttributes.setValue(n2, this.fValidatedInfo.normalizedValue);
            }
            if (xSSimpleType.getVariety() == 1 && xSSimpleType.getPrimitiveKind() == 20) {
                arrstring = (String[])object;
                SchemaGrammar schemaGrammar = this.fGrammarBucket.getGrammar(arrstring.uri);
                if (schemaGrammar != null) {
                    this.fNotation = schemaGrammar.getGlobalNotationDecl(arrstring.localpart);
                }
            }
        }
        catch (InvalidDatatypeValueException invalidDatatypeValueException) {
            this.reportSchemaError(invalidDatatypeValueException.getKey(), invalidDatatypeValueException.getArgs());
            Object[] arrobject = new Object[4];
            arrobject[0] = qName.rawname;
            arrobject[1] = this.fTempQName.rawname;
            arrobject[2] = string;
            arrobject[3] = xSSimpleType instanceof XSSimpleTypeDecl ? ((XSSimpleTypeDecl)xSSimpleType).getTypeName() : xSSimpleType.getName();
            this.reportSchemaError("cvc-attribute.3", arrobject);
        }
        if (!(object == null || xSAttributeDecl.getConstraintType() != 2 || ValidatedInfo.isComparable(this.fValidatedInfo, xSAttributeDecl.fDefault) && object.equals(xSAttributeDecl.fDefault.actualValue))) {
            this.reportSchemaError("cvc-attribute.4", new Object[]{qName.rawname, this.fTempQName.rawname, string, xSAttributeDecl.fDefault.stringValue()});
        }
        if (!(object == null || xSAttributeUseImpl == null || xSAttributeUseImpl.fConstraintType != 2 || ValidatedInfo.isComparable(this.fValidatedInfo, xSAttributeUseImpl.fDefault) && object.equals(xSAttributeUseImpl.fDefault.actualValue))) {
            this.reportSchemaError("cvc-complex-type.3.1", new Object[]{qName.rawname, this.fTempQName.rawname, string, xSAttributeUseImpl.fDefault.stringValue()});
        }
        if (this.fIdConstraint) {
            attributePSVImpl.fActualValue = object;
        }
        if (this.fAugPSVI) {
            attributePSVImpl.fDeclaration = xSAttributeDecl;
            attributePSVImpl.fTypeDecl = xSSimpleType;
            attributePSVImpl.fMemberType = this.fValidatedInfo.memberType;
            attributePSVImpl.fNormalizedValue = this.fValidatedInfo.normalizedValue;
            attributePSVImpl.fActualValue = this.fValidatedInfo.actualValue;
            attributePSVImpl.fActualValueType = this.fValidatedInfo.actualValueType;
            attributePSVImpl.fItemValueTypes = this.fValidatedInfo.itemValueTypes;
            attributePSVImpl.fValidationAttempted = 2;
            this.fNNoneValidationDepth = this.fElementDepth;
            arrstring = this.fXSIErrorReporter.mergeContext();
            attributePSVImpl.fErrors = arrstring;
            attributePSVImpl.fValidity = arrstring == null ? 2 : 1;
        }
    }

    void addDefaultAttributes(org.apache.xerces.xni.QName qName, XMLAttributes xMLAttributes, XSAttributeGroupDecl xSAttributeGroupDecl) {
        XSObjectList xSObjectList = xSAttributeGroupDecl.getAttributeUses();
        int n2 = xSObjectList.getLength();
        int n3 = 0;
        while (n3 < n2) {
            boolean bl;
            XSAttributeUseImpl xSAttributeUseImpl = (XSAttributeUseImpl)xSObjectList.item(n3);
            XSAttributeDecl xSAttributeDecl = xSAttributeUseImpl.fAttrDecl;
            short s2 = xSAttributeUseImpl.fConstraintType;
            ValidatedInfo validatedInfo = xSAttributeUseImpl.fDefault;
            if (s2 == 0) {
                s2 = xSAttributeDecl.getConstraintType();
                validatedInfo = xSAttributeDecl.fDefault;
            }
            boolean bl2 = bl = xMLAttributes.getValue(xSAttributeDecl.fTargetNamespace, xSAttributeDecl.fName) != null;
            if (xSAttributeUseImpl.fUse == 1 && !bl) {
                this.reportSchemaError("cvc-complex-type.4", new Object[]{qName.rawname, xSAttributeDecl.fName});
            }
            if (!bl && s2 != 0) {
                String string;
                int n4;
                Object object;
                org.apache.xerces.xni.QName qName2 = new org.apache.xerces.xni.QName(null, xSAttributeDecl.fName, xSAttributeDecl.fName, xSAttributeDecl.fTargetNamespace);
                String string2 = string = validatedInfo != null ? validatedInfo.stringValue() : "";
                if (xMLAttributes instanceof XMLAttributesImpl) {
                    object = (XMLAttributesImpl)xMLAttributes;
                    n4 = object.getLength();
                    object.addAttributeNS(qName2, "CDATA", string);
                } else {
                    n4 = xMLAttributes.addAttribute(qName2, "CDATA", string);
                }
                if (this.fAugPSVI) {
                    object = xMLAttributes.getAugmentations(n4);
                    AttributePSVImpl attributePSVImpl = new AttributePSVImpl();
                    object.putItem("ATTRIBUTE_PSVI", attributePSVImpl);
                    attributePSVImpl.fDeclaration = xSAttributeDecl;
                    attributePSVImpl.fTypeDecl = xSAttributeDecl.fType;
                    attributePSVImpl.fMemberType = validatedInfo.memberType;
                    attributePSVImpl.fNormalizedValue = string;
                    attributePSVImpl.fActualValue = validatedInfo.actualValue;
                    attributePSVImpl.fActualValueType = validatedInfo.actualValueType;
                    attributePSVImpl.fItemValueTypes = validatedInfo.itemValueTypes;
                    attributePSVImpl.fValidationContext = this.fValidationRoot;
                    attributePSVImpl.fValidity = 2;
                    attributePSVImpl.fValidationAttempted = 2;
                    attributePSVImpl.fSpecified = true;
                }
            }
            ++n3;
        }
    }

    void processElementContent(org.apache.xerces.xni.QName qName) {
        Object object;
        int n2;
        if (!(this.fCurrentElemDecl == null || this.fCurrentElemDecl.fDefault == null || this.fSawText || this.fSubElement || this.fNil)) {
            object = this.fCurrentElemDecl.fDefault.stringValue();
            n2 = object.length();
            if (this.fNormalizedStr.ch == null || this.fNormalizedStr.ch.length < n2) {
                this.fNormalizedStr.ch = new char[n2];
            }
            object.getChars(0, n2, this.fNormalizedStr.ch, 0);
            this.fNormalizedStr.offset = 0;
            this.fNormalizedStr.length = n2;
            this.fDefaultValue = this.fNormalizedStr;
        }
        this.fValidatedInfo.normalizedValue = null;
        if (this.fNil && (this.fSubElement || this.fSawText)) {
            this.reportSchemaError("cvc-elt.3.2.1", new Object[]{qName.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL});
        }
        this.fValidatedInfo.reset();
        if (!(this.fCurrentElemDecl == null || this.fCurrentElemDecl.getConstraintType() == 0 || this.fSubElement || this.fSawText || this.fNil)) {
            if (this.fCurrentType != this.fCurrentElemDecl.fType && XSConstraints.ElementDefaultValidImmediate(this.fCurrentType, this.fCurrentElemDecl.fDefault.stringValue(), this.fState4XsiType, null) == null) {
                this.reportSchemaError("cvc-elt.5.1.1", new Object[]{qName.rawname, this.fCurrentType.getName(), this.fCurrentElemDecl.fDefault.stringValue()});
            }
            this.elementLocallyValidType(qName, this.fCurrentElemDecl.fDefault.stringValue());
        } else {
            object = this.elementLocallyValidType(qName, this.fBuffer);
            if (this.fCurrentElemDecl != null && this.fCurrentElemDecl.getConstraintType() == 2 && !this.fNil) {
                String string = this.fBuffer.toString();
                if (this.fSubElement) {
                    this.reportSchemaError("cvc-elt.5.2.2.1", new Object[]{qName.rawname});
                }
                if (this.fCurrentType.getTypeCategory() == 15) {
                    XSComplexTypeDecl xSComplexTypeDecl = (XSComplexTypeDecl)this.fCurrentType;
                    if (xSComplexTypeDecl.fContentType == 3) {
                        if (!this.fCurrentElemDecl.fDefault.normalizedValue.equals(string)) {
                            this.reportSchemaError("cvc-elt.5.2.2.2.1", new Object[]{qName.rawname, string, this.fCurrentElemDecl.fDefault.normalizedValue});
                        }
                    } else if (!(xSComplexTypeDecl.fContentType != 1 || object == null || ValidatedInfo.isComparable(this.fValidatedInfo, this.fCurrentElemDecl.fDefault) && object.equals(this.fCurrentElemDecl.fDefault.actualValue))) {
                        this.reportSchemaError("cvc-elt.5.2.2.2.2", new Object[]{qName.rawname, string, this.fCurrentElemDecl.fDefault.stringValue()});
                    }
                } else if (!(this.fCurrentType.getTypeCategory() != 16 || object == null || ValidatedInfo.isComparable(this.fValidatedInfo, this.fCurrentElemDecl.fDefault) && object.equals(this.fCurrentElemDecl.fDefault.actualValue))) {
                    this.reportSchemaError("cvc-elt.5.2.2.2.2", new Object[]{qName.rawname, string, this.fCurrentElemDecl.fDefault.stringValue()});
                }
            }
        }
        if (this.fDefaultValue == null && this.fNormalizeData && this.fDocumentHandler != null && this.fUnionType) {
            object = this.fValidatedInfo.normalizedValue;
            if (object == null) {
                object = this.fBuffer.toString();
            }
            n2 = object.length();
            if (this.fNormalizedStr.ch == null || this.fNormalizedStr.ch.length < n2) {
                this.fNormalizedStr.ch = new char[n2];
            }
            object.getChars(0, n2, this.fNormalizedStr.ch, 0);
            this.fNormalizedStr.offset = 0;
            this.fNormalizedStr.length = n2;
            this.fDocumentHandler.characters(this.fNormalizedStr, null);
        }
    }

    Object elementLocallyValidType(org.apache.xerces.xni.QName qName, Object object) {
        if (this.fCurrentType == null) {
            return null;
        }
        Object object2 = null;
        if (this.fCurrentType.getTypeCategory() == 16) {
            if (this.fSubElement) {
                this.reportSchemaError("cvc-type.3.1.2", new Object[]{qName.rawname});
            }
            if (!this.fNil) {
                XSSimpleType xSSimpleType = (XSSimpleType)this.fCurrentType;
                try {
                    if (!this.fNormalizeData || this.fUnionType) {
                        this.fValidationState.setNormalizationRequired(true);
                    }
                    object2 = xSSimpleType.validate(object, (ValidationContext)this.fValidationState, this.fValidatedInfo);
                }
                catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                    this.reportSchemaError(invalidDatatypeValueException.getKey(), invalidDatatypeValueException.getArgs());
                    this.reportSchemaError("cvc-type.3.1.3", new Object[]{qName.rawname, object});
                }
            }
        } else {
            object2 = this.elementLocallyValidComplexType(qName, object);
        }
        return object2;
    }

    Object elementLocallyValidComplexType(org.apache.xerces.xni.QName qName, Object object) {
        Object object2 = null;
        XSComplexTypeDecl xSComplexTypeDecl = (XSComplexTypeDecl)this.fCurrentType;
        if (!this.fNil) {
            Object object3;
            if (xSComplexTypeDecl.fContentType == 0 && (this.fSubElement || this.fSawText)) {
                this.reportSchemaError("cvc-complex-type.2.1", new Object[]{qName.rawname});
            } else if (xSComplexTypeDecl.fContentType == 1) {
                if (this.fSubElement) {
                    this.reportSchemaError("cvc-complex-type.2.2", new Object[]{qName.rawname});
                }
                object3 = xSComplexTypeDecl.fXSSimpleType;
                try {
                    if (!this.fNormalizeData || this.fUnionType) {
                        this.fValidationState.setNormalizationRequired(true);
                    }
                    object2 = object3.validate(object, (ValidationContext)this.fValidationState, this.fValidatedInfo);
                }
                catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                    this.reportSchemaError(invalidDatatypeValueException.getKey(), invalidDatatypeValueException.getArgs());
                    this.reportSchemaError("cvc-complex-type.2.2", new Object[]{qName.rawname});
                }
            } else if (xSComplexTypeDecl.fContentType == 2 && this.fSawCharacters) {
                this.reportSchemaError("cvc-complex-type.2.3", new Object[]{qName.rawname});
            }
            if (!(xSComplexTypeDecl.fContentType != 2 && xSComplexTypeDecl.fContentType != 3 || this.fCurrCMState[0] < 0 || this.fCurrentCM.endContentModel(this.fCurrCMState))) {
                object3 = this.expectedStr(this.fCurrentCM.whatCanGoHere(this.fCurrCMState));
                int[] arrn = this.fCurrentCM.occurenceInfo(this.fCurrCMState);
                if (arrn != null) {
                    int n2 = arrn[2];
                    int n3 = arrn[0];
                    if (n2 < n3) {
                        int n4 = n3 - n2;
                        if (n4 > 1) {
                            this.reportSchemaError("cvc-complex-type.2.4.j", new Object[]{qName.rawname, this.fCurrentCM.getTermName(arrn[3]), Integer.toString(n3), Integer.toString(n4)});
                        } else {
                            this.reportSchemaError("cvc-complex-type.2.4.i", new Object[]{qName.rawname, this.fCurrentCM.getTermName(arrn[3]), Integer.toString(n3)});
                        }
                    } else {
                        this.reportSchemaError("cvc-complex-type.2.4.b", new Object[]{qName.rawname, object3});
                    }
                } else {
                    this.reportSchemaError("cvc-complex-type.2.4.b", new Object[]{qName.rawname, object3});
                }
            }
        }
        return object2;
    }

    void processRootTypeQName(QName qName) {
        Object object;
        String string = qName.getNamespaceURI();
        if (string != null && string.equals("")) {
            string = null;
        }
        if (SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(string)) {
            this.fCurrentType = SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(qName.getLocalPart());
        } else {
            object = this.findSchemaGrammar(5, string, null, null, null);
            if (object != null) {
                this.fCurrentType = object.getGlobalTypeDecl(qName.getLocalPart());
            }
        }
        if (this.fCurrentType == null) {
            object = qName.getPrefix().equals("") ? qName.getLocalPart() : qName.getPrefix() + ":" + qName.getLocalPart();
            this.reportSchemaError("cvc-type.1", new Object[]{object});
        }
    }

    void processRootElementDeclQName(QName qName, org.apache.xerces.xni.QName qName2) {
        SchemaGrammar schemaGrammar;
        String string = qName.getNamespaceURI();
        if (string != null && string.equals("")) {
            string = null;
        }
        if ((schemaGrammar = this.findSchemaGrammar(5, string, null, null, null)) != null) {
            this.fCurrentElemDecl = schemaGrammar.getGlobalElementDecl(qName.getLocalPart());
        }
        if (this.fCurrentElemDecl == null) {
            String string2 = qName.getPrefix().equals("") ? qName.getLocalPart() : qName.getPrefix() + ":" + qName.getLocalPart();
            this.reportSchemaError("cvc-elt.1.a", new Object[]{string2});
        } else {
            this.checkElementMatchesRootElementDecl(this.fCurrentElemDecl, qName2);
        }
    }

    void checkElementMatchesRootElementDecl(XSElementDecl xSElementDecl, org.apache.xerces.xni.QName qName) {
        if (qName.localpart != xSElementDecl.fName || qName.uri != xSElementDecl.fTargetNamespace) {
            this.reportSchemaError("cvc-elt.1.b", new Object[]{qName.rawname, xSElementDecl.fName});
        }
    }

    void reportSchemaError(String string, Object[] arrobject) {
        if (this.fDoValidation) {
            this.fXSIErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", string, arrobject, 1);
        }
    }

    private String expectedStr(Vector vector) {
        StringBuffer stringBuffer = new StringBuffer("{");
        int n2 = vector.size();
        int n3 = 0;
        while (n3 < n2) {
            if (n3 > 0) {
                stringBuffer.append(", ");
            }
            stringBuffer.append(vector.elementAt(n3).toString());
            ++n3;
        }
        stringBuffer.append('}');
        return stringBuffer.toString();
    }

    static int access$000(XMLSchemaValidator xMLSchemaValidator) {
        return xMLSchemaValidator.fElementDepth;
    }

    static void access$100(XMLSchemaValidator xMLSchemaValidator, IdentityConstraint identityConstraint) {
        xMLSchemaValidator.activateSelectorFor(identityConstraint);
    }

    protected static final class ShortVector {
        private int fLength;
        private short[] fData;

        public ShortVector() {
        }

        public ShortVector(int n2) {
            this.fData = new short[n2];
        }

        public int length() {
            return this.fLength;
        }

        public void add(short s2) {
            this.ensureCapacity(this.fLength + 1);
            this.fData[this.fLength++] = s2;
        }

        public short valueAt(int n2) {
            return this.fData[n2];
        }

        public void clear() {
            this.fLength = 0;
        }

        public boolean contains(short s2) {
            int n2 = 0;
            while (n2 < this.fLength) {
                if (this.fData[n2] == s2) {
                    return true;
                }
                ++n2;
            }
            return false;
        }

        private void ensureCapacity(int n2) {
            if (this.fData == null) {
                this.fData = new short[8];
            } else if (this.fData.length <= n2) {
                short[] arrs = new short[this.fData.length * 2];
                System.arraycopy(this.fData, 0, arrs, 0, this.fData.length);
                this.fData = arrs;
            }
        }
    }

    protected static final class LocalIDKey {
        public IdentityConstraint fId;
        public int fDepth;

        public LocalIDKey() {
        }

        public LocalIDKey(IdentityConstraint identityConstraint, int n2) {
            this.fId = identityConstraint;
            this.fDepth = n2;
        }

        public int hashCode() {
            return this.fId.hashCode() + this.fDepth;
        }

        public boolean equals(Object object) {
            if (object instanceof LocalIDKey) {
                LocalIDKey localIDKey = (LocalIDKey)object;
                return localIDKey.fId == this.fId && localIDKey.fDepth == this.fDepth;
            }
            return false;
        }
    }

    protected class ValueStoreCache {
        final LocalIDKey fLocalId;
        protected final ArrayList fValueStores;
        protected final HashMap fIdentityConstraint2ValueStoreMap;
        protected final Stack fGlobalMapStack;
        protected final HashMap fGlobalIDConstraintMap;
        private final XMLSchemaValidator this$0;

        public ValueStoreCache(XMLSchemaValidator xMLSchemaValidator) {
            this.this$0 = xMLSchemaValidator;
            this.fLocalId = new LocalIDKey();
            this.fValueStores = new ArrayList();
            this.fIdentityConstraint2ValueStoreMap = new HashMap();
            this.fGlobalMapStack = new Stack();
            this.fGlobalIDConstraintMap = new HashMap();
        }

        public void startDocument() {
            this.fValueStores.clear();
            this.fIdentityConstraint2ValueStoreMap.clear();
            this.fGlobalIDConstraintMap.clear();
            this.fGlobalMapStack.removeAllElements();
        }

        public void startElement() {
            if (this.fGlobalIDConstraintMap.size() > 0) {
                this.fGlobalMapStack.push(this.fGlobalIDConstraintMap.clone());
            } else {
                this.fGlobalMapStack.push(null);
            }
            this.fGlobalIDConstraintMap.clear();
        }

        public void endElement() {
            if (this.fGlobalMapStack.isEmpty()) {
                return;
            }
            HashMap hashMap = (HashMap)this.fGlobalMapStack.pop();
            if (hashMap == null) {
                return;
            }
            Iterator iterator = hashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                IdentityConstraint identityConstraint = (IdentityConstraint)entry.getKey();
                ValueStoreBase valueStoreBase = (ValueStoreBase)entry.getValue();
                if (valueStoreBase == null) continue;
                ValueStoreBase valueStoreBase2 = (ValueStoreBase)this.fGlobalIDConstraintMap.get(identityConstraint);
                if (valueStoreBase2 == null) {
                    this.fGlobalIDConstraintMap.put(identityConstraint, valueStoreBase);
                    continue;
                }
                if (valueStoreBase2 == valueStoreBase) continue;
                valueStoreBase2.append(valueStoreBase);
            }
        }

        public void initValueStoresFor(XSElementDecl xSElementDecl, FieldActivator fieldActivator) {
            IdentityConstraint[] arridentityConstraint = xSElementDecl.fIDConstraints;
            int n2 = xSElementDecl.fIDCPos;
            int n3 = 0;
            while (n3 < n2) {
                switch (arridentityConstraint[n3].getCategory()) {
                    LocalIDKey localIDKey;
                    case 3: {
                        UniqueOrKey uniqueOrKey = (UniqueOrKey)arridentityConstraint[n3];
                        localIDKey = new LocalIDKey(uniqueOrKey, XMLSchemaValidator.access$000(this.this$0));
                        UniqueValueStore uniqueValueStore = (UniqueValueStore)this.fIdentityConstraint2ValueStoreMap.get(localIDKey);
                        if (uniqueValueStore == null) {
                            uniqueValueStore = new UniqueValueStore(this.this$0, uniqueOrKey);
                            this.fIdentityConstraint2ValueStoreMap.put(localIDKey, uniqueValueStore);
                        } else {
                            uniqueValueStore.clear();
                        }
                        this.fValueStores.add(uniqueValueStore);
                        XMLSchemaValidator.access$100(this.this$0, arridentityConstraint[n3]);
                        break;
                    }
                    case 1: {
                        UniqueOrKey uniqueOrKey = (UniqueOrKey)arridentityConstraint[n3];
                        localIDKey = new LocalIDKey(uniqueOrKey, XMLSchemaValidator.access$000(this.this$0));
                        KeyValueStore keyValueStore = (KeyValueStore)this.fIdentityConstraint2ValueStoreMap.get(localIDKey);
                        if (keyValueStore == null) {
                            keyValueStore = new KeyValueStore(this.this$0, uniqueOrKey);
                            this.fIdentityConstraint2ValueStoreMap.put(localIDKey, keyValueStore);
                        } else {
                            keyValueStore.clear();
                        }
                        this.fValueStores.add(keyValueStore);
                        XMLSchemaValidator.access$100(this.this$0, arridentityConstraint[n3]);
                        break;
                    }
                    case 2: {
                        KeyRef keyRef = (KeyRef)arridentityConstraint[n3];
                        localIDKey = new LocalIDKey(keyRef, XMLSchemaValidator.access$000(this.this$0));
                        KeyRefValueStore keyRefValueStore = (KeyRefValueStore)this.fIdentityConstraint2ValueStoreMap.get(localIDKey);
                        if (keyRefValueStore == null) {
                            keyRefValueStore = new KeyRefValueStore(this.this$0, keyRef, null);
                            this.fIdentityConstraint2ValueStoreMap.put(localIDKey, keyRefValueStore);
                        } else {
                            keyRefValueStore.clear();
                        }
                        this.fValueStores.add(keyRefValueStore);
                        XMLSchemaValidator.access$100(this.this$0, arridentityConstraint[n3]);
                    }
                }
                ++n3;
            }
        }

        public ValueStoreBase getValueStoreFor(IdentityConstraint identityConstraint, int n2) {
            this.fLocalId.fDepth = n2;
            this.fLocalId.fId = identityConstraint;
            return (ValueStoreBase)this.fIdentityConstraint2ValueStoreMap.get(this.fLocalId);
        }

        public ValueStoreBase getGlobalValueStoreFor(IdentityConstraint identityConstraint) {
            return (ValueStoreBase)this.fGlobalIDConstraintMap.get(identityConstraint);
        }

        public void transplant(IdentityConstraint identityConstraint, int n2) {
            this.fLocalId.fDepth = n2;
            this.fLocalId.fId = identityConstraint;
            ValueStoreBase valueStoreBase = (ValueStoreBase)this.fIdentityConstraint2ValueStoreMap.get(this.fLocalId);
            if (identityConstraint.getCategory() == 2) {
                return;
            }
            ValueStoreBase valueStoreBase2 = (ValueStoreBase)this.fGlobalIDConstraintMap.get(identityConstraint);
            if (valueStoreBase2 != null) {
                valueStoreBase2.append(valueStoreBase);
                this.fGlobalIDConstraintMap.put(identityConstraint, valueStoreBase2);
            } else {
                this.fGlobalIDConstraintMap.put(identityConstraint, valueStoreBase);
            }
        }

        public void endDocument() {
            int n2 = this.fValueStores.size();
            int n3 = 0;
            while (n3 < n2) {
                ValueStoreBase valueStoreBase = (ValueStoreBase)this.fValueStores.get(n3);
                valueStoreBase.endDocument();
                ++n3;
            }
        }

        public String toString() {
            String string = super.toString();
            int n2 = string.lastIndexOf(36);
            if (n2 != -1) {
                return string.substring(n2 + 1);
            }
            int n3 = string.lastIndexOf(46);
            if (n3 != -1) {
                return string.substring(n3 + 1);
            }
            return string;
        }
    }

    protected class KeyRefValueStore
    extends ValueStoreBase {
        protected ValueStoreBase fKeyValueStore;
        private final XMLSchemaValidator this$0;

        public KeyRefValueStore(XMLSchemaValidator xMLSchemaValidator, KeyRef keyRef, KeyValueStore keyValueStore) {
            super(xMLSchemaValidator, keyRef);
            this.this$0 = xMLSchemaValidator;
            this.fKeyValueStore = keyValueStore;
        }

        public void endDocumentFragment() {
            super.endDocumentFragment();
            this.fKeyValueStore = (ValueStoreBase)this.this$0.fValueStoreCache.fGlobalIDConstraintMap.get(((KeyRef)this.fIdentityConstraint).getKey());
            if (this.fKeyValueStore == null) {
                String string = "KeyRefOutOfScope";
                String string2 = this.fIdentityConstraint.toString();
                this.this$0.reportSchemaError(string, new Object[]{string2});
                return;
            }
            int n2 = this.fKeyValueStore.contains(this);
            if (n2 != -1) {
                String string = "KeyNotFound";
                String string3 = this.toString(this.fValues, n2, this.fFieldCount);
                String string4 = this.fIdentityConstraint.getElementName();
                String string5 = this.fIdentityConstraint.getName();
                this.this$0.reportSchemaError(string, new Object[]{string5, string3, string4});
            }
        }

        public void endDocument() {
            super.endDocument();
        }
    }

    protected class KeyValueStore
    extends ValueStoreBase {
        private final XMLSchemaValidator this$0;

        public KeyValueStore(XMLSchemaValidator xMLSchemaValidator, UniqueOrKey uniqueOrKey) {
            super(xMLSchemaValidator, uniqueOrKey);
            this.this$0 = xMLSchemaValidator;
        }

        protected void checkDuplicateValues() {
            if (this.contains()) {
                String string = "DuplicateKey";
                String string2 = this.toString(this.fLocalValues);
                String string3 = this.fIdentityConstraint.getElementName();
                String string4 = this.fIdentityConstraint.getIdentityConstraintName();
                this.this$0.reportSchemaError(string, new Object[]{string2, string3, string4});
            }
        }
    }

    protected class UniqueValueStore
    extends ValueStoreBase {
        private final XMLSchemaValidator this$0;

        public UniqueValueStore(XMLSchemaValidator xMLSchemaValidator, UniqueOrKey uniqueOrKey) {
            super(xMLSchemaValidator, uniqueOrKey);
            this.this$0 = xMLSchemaValidator;
        }

        protected void checkDuplicateValues() {
            if (this.contains()) {
                String string = "DuplicateUnique";
                String string2 = this.toString(this.fLocalValues);
                String string3 = this.fIdentityConstraint.getElementName();
                String string4 = this.fIdentityConstraint.getIdentityConstraintName();
                this.this$0.reportSchemaError(string, new Object[]{string2, string3, string4});
            }
        }
    }

    protected abstract class ValueStoreBase
    implements ValueStore {
        protected IdentityConstraint fIdentityConstraint;
        protected int fFieldCount;
        protected Field[] fFields;
        protected Object[] fLocalValues;
        protected short[] fLocalValueTypes;
        protected ShortList[] fLocalItemValueTypes;
        protected int fValuesCount;
        public final Vector fValues;
        public ShortVector fValueTypes;
        public Vector fItemValueTypes;
        private boolean fUseValueTypeVector;
        private int fValueTypesLength;
        private short fValueType;
        private boolean fUseItemValueTypeVector;
        private int fItemValueTypesLength;
        private ShortList fItemValueType;
        final StringBuffer fTempBuffer;
        private final XMLSchemaValidator this$0;

        protected ValueStoreBase(XMLSchemaValidator xMLSchemaValidator, IdentityConstraint identityConstraint) {
            this.this$0 = xMLSchemaValidator;
            this.fFieldCount = 0;
            this.fFields = null;
            this.fLocalValues = null;
            this.fLocalValueTypes = null;
            this.fLocalItemValueTypes = null;
            this.fValues = new Vector();
            this.fValueTypes = null;
            this.fItemValueTypes = null;
            this.fUseValueTypeVector = false;
            this.fValueTypesLength = 0;
            this.fValueType = 0;
            this.fUseItemValueTypeVector = false;
            this.fItemValueTypesLength = 0;
            this.fItemValueType = null;
            this.fTempBuffer = new StringBuffer();
            this.fIdentityConstraint = identityConstraint;
            this.fFieldCount = this.fIdentityConstraint.getFieldCount();
            this.fFields = new Field[this.fFieldCount];
            this.fLocalValues = new Object[this.fFieldCount];
            this.fLocalValueTypes = new short[this.fFieldCount];
            this.fLocalItemValueTypes = new ShortList[this.fFieldCount];
            int n2 = 0;
            while (n2 < this.fFieldCount) {
                this.fFields[n2] = this.fIdentityConstraint.getFieldAt(n2);
                ++n2;
            }
        }

        public void clear() {
            this.fValuesCount = 0;
            this.fUseValueTypeVector = false;
            this.fValueTypesLength = 0;
            this.fValueType = 0;
            this.fUseItemValueTypeVector = false;
            this.fItemValueTypesLength = 0;
            this.fItemValueType = null;
            this.fValues.setSize(0);
            if (this.fValueTypes != null) {
                this.fValueTypes.clear();
            }
            if (this.fItemValueTypes != null) {
                this.fItemValueTypes.setSize(0);
            }
        }

        public void append(ValueStoreBase valueStoreBase) {
            int n2 = 0;
            while (n2 < valueStoreBase.fValues.size()) {
                this.fValues.addElement(valueStoreBase.fValues.elementAt(n2));
                ++n2;
            }
        }

        public void startValueScope() {
            this.fValuesCount = 0;
            int n2 = 0;
            while (n2 < this.fFieldCount) {
                this.fLocalValues[n2] = null;
                this.fLocalValueTypes[n2] = 0;
                this.fLocalItemValueTypes[n2] = null;
                ++n2;
            }
        }

        public void endValueScope() {
            if (this.fValuesCount == 0) {
                if (this.fIdentityConstraint.getCategory() == 1) {
                    String string = "AbsentKeyValue";
                    String string2 = this.fIdentityConstraint.getElementName();
                    String string3 = this.fIdentityConstraint.getIdentityConstraintName();
                    this.this$0.reportSchemaError(string, new Object[]{string2, string3});
                }
                return;
            }
            if (this.fValuesCount != this.fFieldCount) {
                if (this.fIdentityConstraint.getCategory() == 1) {
                    String string = "KeyNotEnoughValues";
                    UniqueOrKey uniqueOrKey = (UniqueOrKey)this.fIdentityConstraint;
                    String string4 = this.fIdentityConstraint.getElementName();
                    String string5 = uniqueOrKey.getIdentityConstraintName();
                    this.this$0.reportSchemaError(string, new Object[]{string4, string5});
                }
                return;
            }
        }

        public void endDocumentFragment() {
        }

        public void endDocument() {
        }

        public void reportError(String string, Object[] arrobject) {
            this.this$0.reportSchemaError(string, arrobject);
        }

        public void addValue(Field field, boolean bl, Object object, short s2, ShortList shortList) {
            int n2 = this.fFieldCount - 1;
            while (n2 > -1) {
                if (this.fFields[n2] == field) break;
                --n2;
            }
            if (n2 == -1) {
                String string = "UnknownField";
                String string2 = this.fIdentityConstraint.getElementName();
                String string3 = this.fIdentityConstraint.getIdentityConstraintName();
                this.this$0.reportSchemaError(string, new Object[]{field.toString(), string2, string3});
                return;
            }
            if (!bl) {
                String string = "FieldMultipleMatch";
                String string4 = this.fIdentityConstraint.getIdentityConstraintName();
                this.this$0.reportSchemaError(string, new Object[]{field.toString(), string4});
            } else {
                ++this.fValuesCount;
            }
            this.fLocalValues[n2] = object;
            this.fLocalValueTypes[n2] = s2;
            this.fLocalItemValueTypes[n2] = shortList;
            if (this.fValuesCount == this.fFieldCount) {
                this.checkDuplicateValues();
                n2 = 0;
                while (n2 < this.fFieldCount) {
                    this.fValues.addElement(this.fLocalValues[n2]);
                    this.addValueType(this.fLocalValueTypes[n2]);
                    this.addItemValueType(this.fLocalItemValueTypes[n2]);
                    ++n2;
                }
            }
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public boolean contains() {
            var1_1 = 0;
            var2_2 = this.fValues.size();
            var3_3 = 0;
            while (var3_3 < var2_2) {
                block3 : {
                    var1_1 = var3_3 + this.fFieldCount;
                    var4_4 = 0;
                    while (var4_4 < this.fFieldCount) {
                        var5_5 = this.fLocalValues[var4_4];
                        var6_6 = this.fValues.elementAt(var3_3);
                        var7_7 = this.fLocalValueTypes[var4_4];
                        var8_8 = this.getValueTypeAt(var3_3);
                        if (var5_5 == null || var6_6 == null || var7_7 != var8_8 || !var5_5.equals(var6_6)) break block3;
                        if (var7_7 != 44 && var7_7 != 43) ** GOTO lbl-1000
                        var9_9 = this.fLocalItemValueTypes[var4_4];
                        var10_10 = this.getItemValueTypeAt(var3_3);
                        if (var9_9 != null && var10_10 != null && var9_9.equals(var10_10)) lbl-1000: // 2 sources:
                        {
                            ++var3_3;
                            ++var4_4;
                            continue;
                        }
                        break block3;
                    }
                    return true;
                }
                var3_3 = var1_1;
            }
            return false;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public int contains(ValueStoreBase var1_1) {
            var2_2 = var1_1.fValues;
            var3_3 = var2_2.size();
            if (this.fFieldCount > 1) ** GOTO lbl6
            var4_4 = 0;
            ** GOTO lbl16
lbl6: // 1 sources:
            var4_5 = this.fValues.size();
            var5_7 = 0;
            ** GOTO lbl40
lbl-1000: // 1 sources:
            {
                var5_6 = var1_1.getValueTypeAt(var4_4);
                if (this.valueTypeContains(var5_6) == false) return var4_4;
                if (!this.fValues.contains(var2_2.elementAt(var4_4))) {
                    return var4_4;
                }
                if (!(var5_6 != 44 && var5_6 != 43 || this.itemValueTypeContains(var6_8 = var1_1.getItemValueTypeAt(var4_4)))) {
                    return var4_4;
                }
                ++var4_4;
lbl16: // 2 sources:
                ** while (var4_4 < var3_3)
            }
lbl17: // 1 sources:
            return -1;
lbl-1000: // 1 sources:
            {
                var6_9 = 0;
                while (var6_9 < var4_5) {
                    block7 : {
                        var7_10 = 0;
                        while (var7_10 < this.fFieldCount) {
                            var8_11 = var2_2.elementAt(var5_7 + var7_10);
                            var9_12 = this.fValues.elementAt(var6_9 + var7_10);
                            var10_13 = var1_1.getValueTypeAt(var5_7 + var7_10);
                            var11_14 = this.getValueTypeAt(var6_9 + var7_10);
                            if (var8_11 != var9_12 && (var10_13 != var11_14 || var8_11 == null || !var8_11.equals(var9_12))) break block7;
                            if (var10_13 != 44 && var10_13 != 43) ** GOTO lbl-1000
                            var12_15 = var1_1.getItemValueTypeAt(var5_7 + var7_10);
                            var13_16 = this.getItemValueTypeAt(var6_9 + var7_10);
                            if (var12_15 != null && var13_16 != null && var12_15.equals(var13_16)) lbl-1000: // 2 sources:
                            {
                                ++var7_10;
                                continue;
                            }
                            break block7;
                        }
                        var5_7 += this.fFieldCount;
                        continue block1;
                    }
                    var6_9 += this.fFieldCount;
                }
                return var5_7;
lbl40: // 2 sources:
                ** while (var5_7 < var3_3)
            }
lbl41: // 1 sources:
            return -1;
        }

        protected void checkDuplicateValues() {
        }

        protected String toString(Object[] arrobject) {
            int n2 = arrobject.length;
            if (n2 == 0) {
                return "";
            }
            this.fTempBuffer.setLength(0);
            int n3 = 0;
            while (n3 < n2) {
                if (n3 > 0) {
                    this.fTempBuffer.append(',');
                }
                this.fTempBuffer.append(arrobject[n3]);
                ++n3;
            }
            return this.fTempBuffer.toString();
        }

        protected String toString(Vector vector, int n2, int n3) {
            if (n3 == 0) {
                return "";
            }
            if (n3 == 1) {
                return String.valueOf(vector.elementAt(n2));
            }
            StringBuffer stringBuffer = new StringBuffer();
            int n4 = 0;
            while (n4 < n3) {
                if (n4 > 0) {
                    stringBuffer.append(',');
                }
                stringBuffer.append(vector.elementAt(n2 + n4));
                ++n4;
            }
            return stringBuffer.toString();
        }

        public String toString() {
            int n2;
            String string = super.toString();
            int n3 = string.lastIndexOf(36);
            if (n3 != -1) {
                string = string.substring(n3 + 1);
            }
            if ((n2 = string.lastIndexOf(46)) != -1) {
                string = string.substring(n2 + 1);
            }
            return string + '[' + this.fIdentityConstraint + ']';
        }

        private void addValueType(short s2) {
            if (this.fUseValueTypeVector) {
                this.fValueTypes.add(s2);
            } else if (this.fValueTypesLength++ == 0) {
                this.fValueType = s2;
            } else if (this.fValueType != s2) {
                this.fUseValueTypeVector = true;
                if (this.fValueTypes == null) {
                    this.fValueTypes = new ShortVector(this.fValueTypesLength * 2);
                }
                int n2 = 1;
                while (n2 < this.fValueTypesLength) {
                    this.fValueTypes.add(this.fValueType);
                    ++n2;
                }
                this.fValueTypes.add(s2);
            }
        }

        private short getValueTypeAt(int n2) {
            if (this.fUseValueTypeVector) {
                return this.fValueTypes.valueAt(n2);
            }
            return this.fValueType;
        }

        private boolean valueTypeContains(short s2) {
            if (this.fUseValueTypeVector) {
                return this.fValueTypes.contains(s2);
            }
            return this.fValueType == s2;
        }

        private void addItemValueType(ShortList shortList) {
            if (this.fUseItemValueTypeVector) {
                this.fItemValueTypes.add(shortList);
            } else if (this.fItemValueTypesLength++ == 0) {
                this.fItemValueType = shortList;
            } else if (!(this.fItemValueType == shortList || this.fItemValueType != null && this.fItemValueType.equals(shortList))) {
                this.fUseItemValueTypeVector = true;
                if (this.fItemValueTypes == null) {
                    this.fItemValueTypes = new Vector(this.fItemValueTypesLength * 2);
                }
                int n2 = 1;
                while (n2 < this.fItemValueTypesLength) {
                    this.fItemValueTypes.add(this.fItemValueType);
                    ++n2;
                }
                this.fItemValueTypes.add(shortList);
            }
        }

        private ShortList getItemValueTypeAt(int n2) {
            if (this.fUseItemValueTypeVector) {
                return (ShortList)this.fItemValueTypes.elementAt(n2);
            }
            return this.fItemValueType;
        }

        private boolean itemValueTypeContains(ShortList shortList) {
            if (this.fUseItemValueTypeVector) {
                return this.fItemValueTypes.contains(shortList);
            }
            return this.fItemValueType == shortList || this.fItemValueType != null && this.fItemValueType.equals(shortList);
        }
    }

    protected static class XPathMatcherStack {
        protected XPathMatcher[] fMatchers = new XPathMatcher[4];
        protected int fMatchersCount;
        protected IntStack fContextStack = new IntStack();

        public void clear() {
            int n2 = 0;
            while (n2 < this.fMatchersCount) {
                this.fMatchers[n2] = null;
                ++n2;
            }
            this.fMatchersCount = 0;
            this.fContextStack.clear();
        }

        public int size() {
            return this.fContextStack.size();
        }

        public int getMatcherCount() {
            return this.fMatchersCount;
        }

        public void addMatcher(XPathMatcher xPathMatcher) {
            this.ensureMatcherCapacity();
            this.fMatchers[this.fMatchersCount++] = xPathMatcher;
        }

        public XPathMatcher getMatcherAt(int n2) {
            return this.fMatchers[n2];
        }

        public void pushContext() {
            this.fContextStack.push(this.fMatchersCount);
        }

        public void popContext() {
            this.fMatchersCount = this.fContextStack.pop();
        }

        private void ensureMatcherCapacity() {
            if (this.fMatchersCount == this.fMatchers.length) {
                XPathMatcher[] arrxPathMatcher = new XPathMatcher[this.fMatchers.length * 2];
                System.arraycopy(this.fMatchers, 0, arrxPathMatcher, 0, this.fMatchers.length);
                this.fMatchers = arrxPathMatcher;
            }
        }
    }

    protected final class XSIErrorReporter {
        XMLErrorReporter fErrorReporter;
        Vector fErrors;
        int[] fContext;
        int fContextCount;
        private final XMLSchemaValidator this$0;

        protected XSIErrorReporter(XMLSchemaValidator xMLSchemaValidator) {
            this.this$0 = xMLSchemaValidator;
            this.fErrors = new Vector();
            this.fContext = new int[8];
        }

        public void reset(XMLErrorReporter xMLErrorReporter) {
            this.fErrorReporter = xMLErrorReporter;
            this.fErrors.removeAllElements();
            this.fContextCount = 0;
        }

        public void pushContext() {
            if (!this.this$0.fAugPSVI) {
                return;
            }
            if (this.fContextCount == this.fContext.length) {
                int n2 = this.fContextCount + 8;
                int[] arrn = new int[n2];
                System.arraycopy(this.fContext, 0, arrn, 0, this.fContextCount);
                this.fContext = arrn;
            }
            this.fContext[this.fContextCount++] = this.fErrors.size();
        }

        public String[] popContext() {
            if (!this.this$0.fAugPSVI) {
                return null;
            }
            int n2 = this.fContext[--this.fContextCount];
            int n3 = this.fErrors.size() - n2;
            if (n3 == 0) {
                return null;
            }
            String[] arrstring = new String[n3];
            int n4 = 0;
            while (n4 < n3) {
                arrstring[n4] = (String)this.fErrors.elementAt(n2 + n4);
                ++n4;
            }
            this.fErrors.setSize(n2);
            return arrstring;
        }

        public String[] mergeContext() {
            if (!this.this$0.fAugPSVI) {
                return null;
            }
            int n2 = this.fContext[--this.fContextCount];
            int n3 = this.fErrors.size() - n2;
            if (n3 == 0) {
                return null;
            }
            String[] arrstring = new String[n3];
            int n4 = 0;
            while (n4 < n3) {
                arrstring[n4] = (String)this.fErrors.elementAt(n2 + n4);
                ++n4;
            }
            return arrstring;
        }

        public void reportError(String string, String string2, Object[] arrobject, short s2) throws XNIException {
            String string3 = this.fErrorReporter.reportError(string, string2, arrobject, s2);
            if (this.this$0.fAugPSVI) {
                this.fErrors.addElement(string2);
                this.fErrors.addElement(string3);
            }
        }

        public void reportError(XMLLocator xMLLocator, String string, String string2, Object[] arrobject, short s2) throws XNIException {
            String string3 = this.fErrorReporter.reportError(xMLLocator, string, string2, arrobject, s2);
            if (this.this$0.fAugPSVI) {
                this.fErrors.addElement(string2);
                this.fErrors.addElement(string3);
            }
        }
    }

}

