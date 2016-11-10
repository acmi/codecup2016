/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.traversers;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Stack;
import java.util.Vector;
import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.dv.SchemaDVFactory;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaNamespaceSupport;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XMLSchemaException;
import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSAttributeDecl;
import org.apache.xerces.impl.xs.XSAttributeGroupDecl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSDDescription;
import org.apache.xerces.impl.xs.XSDeclarationPool;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSGrammarBucket;
import org.apache.xerces.impl.xs.XSGroupDecl;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.impl.xs.XSNotationDecl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.identity.IdentityConstraint;
import org.apache.xerces.impl.xs.opti.ElementImpl;
import org.apache.xerces.impl.xs.opti.SchemaDOM;
import org.apache.xerces.impl.xs.opti.SchemaDOMParser;
import org.apache.xerces.impl.xs.opti.SchemaParsingConfig;
import org.apache.xerces.impl.xs.traversers.SchemaContentHandler;
import org.apache.xerces.impl.xs.traversers.StAXSchemaParser;
import org.apache.xerces.impl.xs.traversers.XSAnnotationInfo;
import org.apache.xerces.impl.xs.traversers.XSAttributeChecker;
import org.apache.xerces.impl.xs.traversers.XSDAttributeGroupTraverser;
import org.apache.xerces.impl.xs.traversers.XSDAttributeTraverser;
import org.apache.xerces.impl.xs.traversers.XSDComplexTypeTraverser;
import org.apache.xerces.impl.xs.traversers.XSDElementTraverser;
import org.apache.xerces.impl.xs.traversers.XSDGroupTraverser;
import org.apache.xerces.impl.xs.traversers.XSDKeyrefTraverser;
import org.apache.xerces.impl.xs.traversers.XSDNotationTraverser;
import org.apache.xerces.impl.xs.traversers.XSDSimpleTypeTraverser;
import org.apache.xerces.impl.xs.traversers.XSDUniqueOrKeyTraverser;
import org.apache.xerces.impl.xs.traversers.XSDWildcardTraverser;
import org.apache.xerces.impl.xs.traversers.XSDocumentInfo;
import org.apache.xerces.impl.xs.util.SimpleLocator;
import org.apache.xerces.impl.xs.util.XSInputSource;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.parsers.XML11Configuration;
import org.apache.xerces.util.DOMInputSource;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.DefaultErrorHandler;
import org.apache.xerces.util.ErrorHandlerWrapper;
import org.apache.xerces.util.SAXInputSource;
import org.apache.xerces.util.StAXInputSource;
import org.apache.xerces.util.StAXLocationWrapper;
import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.URI;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.grammars.XMLSchemaDescription;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParseException;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeGroupDefinition;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSModelGroupDefinition;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.xs.datatypes.ObjectList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XSDHandler {
    protected static final String VALIDATION = "http://xml.org/sax/features/validation";
    protected static final String XMLSCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
    protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
    protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
    protected static final String STANDARD_URI_CONFORMANT_FEATURE = "http://apache.org/xml/features/standard-uri-conformant";
    protected static final String DISALLOW_DOCTYPE = "http://apache.org/xml/features/disallow-doctype-decl";
    protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = "http://apache.org/xml/features/generate-synthetic-annotations";
    protected static final String VALIDATE_ANNOTATIONS = "http://apache.org/xml/features/validate-annotations";
    protected static final String HONOUR_ALL_SCHEMALOCATIONS = "http://apache.org/xml/features/honour-all-schemaLocations";
    protected static final String NAMESPACE_GROWTH = "http://apache.org/xml/features/namespace-growth";
    protected static final String TOLERATE_DUPLICATES = "http://apache.org/xml/features/internal/tolerate-duplicates";
    private static final String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
    protected static final String STRING_INTERNING = "http://xml.org/sax/features/string-interning";
    protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
    protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    public static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
    protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
    public static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    public static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
    public static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    protected static final String LOCALE = "http://apache.org/xml/properties/locale";
    protected static final boolean DEBUG_NODE_POOL = false;
    static final int ATTRIBUTE_TYPE = 1;
    static final int ATTRIBUTEGROUP_TYPE = 2;
    static final int ELEMENT_TYPE = 3;
    static final int GROUP_TYPE = 4;
    static final int IDENTITYCONSTRAINT_TYPE = 5;
    static final int NOTATION_TYPE = 6;
    static final int TYPEDECL_TYPE = 7;
    public static final String REDEF_IDENTIFIER = "_fn3dktizrknc9pi";
    protected Hashtable fNotationRegistry = new Hashtable();
    protected XSDeclarationPool fDeclPool = null;
    private Hashtable fUnparsedAttributeRegistry = new Hashtable();
    private Hashtable fUnparsedAttributeGroupRegistry = new Hashtable();
    private Hashtable fUnparsedElementRegistry = new Hashtable();
    private Hashtable fUnparsedGroupRegistry = new Hashtable();
    private Hashtable fUnparsedIdentityConstraintRegistry = new Hashtable();
    private Hashtable fUnparsedNotationRegistry = new Hashtable();
    private Hashtable fUnparsedTypeRegistry = new Hashtable();
    private Hashtable fUnparsedAttributeRegistrySub = new Hashtable();
    private Hashtable fUnparsedAttributeGroupRegistrySub = new Hashtable();
    private Hashtable fUnparsedElementRegistrySub = new Hashtable();
    private Hashtable fUnparsedGroupRegistrySub = new Hashtable();
    private Hashtable fUnparsedIdentityConstraintRegistrySub = new Hashtable();
    private Hashtable fUnparsedNotationRegistrySub = new Hashtable();
    private Hashtable fUnparsedTypeRegistrySub = new Hashtable();
    private Hashtable[] fUnparsedRegistriesExt = new Hashtable[]{null, new Hashtable(), new Hashtable(), new Hashtable(), new Hashtable(), new Hashtable(), new Hashtable(), new Hashtable()};
    private Hashtable fXSDocumentInfoRegistry = new Hashtable();
    private Hashtable fDependencyMap = new Hashtable();
    private Hashtable fImportMap = new Hashtable();
    private Vector fAllTNSs = new Vector();
    private Hashtable fLocationPairs = null;
    private static final Hashtable EMPTY_TABLE = new Hashtable();
    Hashtable fHiddenNodes = new Hashtable();
    private Hashtable fTraversed = new Hashtable();
    private Hashtable fDoc2SystemId = new Hashtable();
    private XSDocumentInfo fRoot = null;
    private Hashtable fDoc2XSDocumentMap = new Hashtable();
    private Hashtable fRedefine2XSDMap = new Hashtable();
    private Hashtable fRedefine2NSSupport = new Hashtable();
    private Hashtable fRedefinedRestrictedAttributeGroupRegistry = new Hashtable();
    private Hashtable fRedefinedRestrictedGroupRegistry = new Hashtable();
    private boolean fLastSchemaWasDuplicate;
    private boolean fValidateAnnotations = false;
    private boolean fHonourAllSchemaLocations = false;
    boolean fNamespaceGrowth = false;
    boolean fTolerateDuplicates = false;
    private XMLErrorReporter fErrorReporter;
    private XMLEntityResolver fEntityResolver;
    private XSAttributeChecker fAttributeChecker;
    private SymbolTable fSymbolTable;
    private XSGrammarBucket fGrammarBucket;
    private XSDDescription fSchemaGrammarDescription;
    private XMLGrammarPool fGrammarPool;
    XSDAttributeGroupTraverser fAttributeGroupTraverser;
    XSDAttributeTraverser fAttributeTraverser;
    XSDComplexTypeTraverser fComplexTypeTraverser;
    XSDElementTraverser fElementTraverser;
    XSDGroupTraverser fGroupTraverser;
    XSDKeyrefTraverser fKeyrefTraverser;
    XSDNotationTraverser fNotationTraverser;
    XSDSimpleTypeTraverser fSimpleTypeTraverser;
    XSDUniqueOrKeyTraverser fUniqueOrKeyTraverser;
    XSDWildcardTraverser fWildCardTraverser;
    SchemaDVFactory fDVFactory;
    SchemaDOMParser fSchemaParser = new SchemaDOMParser(new SchemaParsingConfig());
    SchemaContentHandler fXSContentHandler;
    StAXSchemaParser fStAXSchemaParser;
    XML11Configuration fAnnotationValidator;
    XSAnnotationGrammarPool fGrammarBucketAdapter;
    private static final int INIT_STACK_SIZE = 30;
    private static final int INC_STACK_SIZE = 10;
    private int fLocalElemStackPos = 0;
    private XSParticleDecl[] fParticle = new XSParticleDecl[30];
    private Element[] fLocalElementDecl = new Element[30];
    private XSDocumentInfo[] fLocalElementDecl_schema = new XSDocumentInfo[30];
    private int[] fAllContext = new int[30];
    private XSObject[] fParent = new XSObject[30];
    private String[][] fLocalElemNamespaceContext = new String[30][1];
    private static final int INIT_KEYREF_STACK = 2;
    private static final int INC_KEYREF_STACK_AMOUNT = 2;
    private int fKeyrefStackPos = 0;
    private Element[] fKeyrefs = new Element[2];
    private XSDocumentInfo[] fKeyrefsMapXSDocumentInfo = new XSDocumentInfo[2];
    private XSElementDecl[] fKeyrefElems = new XSElementDecl[2];
    private String[][] fKeyrefNamespaceContext = new String[2][1];
    SymbolHash fGlobalAttrDecls = new SymbolHash();
    SymbolHash fGlobalAttrGrpDecls = new SymbolHash();
    SymbolHash fGlobalElemDecls = new SymbolHash();
    SymbolHash fGlobalGroupDecls = new SymbolHash();
    SymbolHash fGlobalNotationDecls = new SymbolHash();
    SymbolHash fGlobalIDConstraintDecls = new SymbolHash();
    SymbolHash fGlobalTypeDecls = new SymbolHash();
    private static final String[][] NS_ERROR_CODES = new String[][]{{"src-include.2.1", "src-include.2.1"}, {"src-redefine.3.1", "src-redefine.3.1"}, {"src-import.3.1", "src-import.3.2"}, null, {"TargetNamespace.1", "TargetNamespace.2"}, {"TargetNamespace.1", "TargetNamespace.2"}, {"TargetNamespace.1", "TargetNamespace.2"}, {"TargetNamespace.1", "TargetNamespace.2"}};
    private static final String[] ELE_ERROR_CODES = new String[]{"src-include.1", "src-redefine.2", "src-import.2", "schema_reference.4", "schema_reference.4", "schema_reference.4", "schema_reference.4", "schema_reference.4"};
    private Vector fReportedTNS = null;
    private static final String[] COMP_TYPE = new String[]{null, "attribute declaration", "attribute group", "element declaration", "group", "identity constraint", "notation", "type definition"};
    private static final String[] CIRCULAR_CODES = new String[]{"Internal-Error", "Internal-Error", "src-attribute_group.3", "e-props-correct.6", "mg-props-correct.2", "Internal-Error", "Internal-Error", "st-props-correct.2"};
    private SimpleLocator xl = new SimpleLocator();

    private String null2EmptyString(String string) {
        return string == null ? XMLSymbols.EMPTY_STRING : string;
    }

    private String emptyString2Null(String string) {
        return string == XMLSymbols.EMPTY_STRING ? null : string;
    }

    private String doc2SystemId(Element element) {
        String string = null;
        if (element.getOwnerDocument() instanceof SchemaDOM) {
            string = ((SchemaDOM)element.getOwnerDocument()).getDocumentURI();
        }
        return string != null ? string : (String)this.fDoc2SystemId.get(element);
    }

    public XSDHandler() {
    }

    public XSDHandler(XSGrammarBucket xSGrammarBucket) {
        this();
        this.fGrammarBucket = xSGrammarBucket;
        this.fSchemaGrammarDescription = new XSDDescription();
    }

    public SchemaGrammar parseSchema(XMLInputSource xMLInputSource, XSDDescription xSDDescription, Hashtable hashtable) throws IOException {
        Object object;
        Object object2;
        this.fLocationPairs = hashtable;
        this.fSchemaParser.resetNodePool();
        SchemaGrammar schemaGrammar = null;
        String string = null;
        short s2 = xSDDescription.getContextType();
        if (s2 != 3) {
            schemaGrammar = this.fHonourAllSchemaLocations && s2 == 2 && this.isExistingGrammar(xSDDescription, this.fNamespaceGrowth) ? this.fGrammarBucket.getGrammar(xSDDescription.getTargetNamespace()) : this.findGrammar(xSDDescription, this.fNamespaceGrowth);
            if (schemaGrammar != null) {
                if (!this.fNamespaceGrowth) {
                    return schemaGrammar;
                }
                try {
                    if (schemaGrammar.getDocumentLocations().contains(XMLEntityManager.expandSystemId(xMLInputSource.getSystemId(), xMLInputSource.getBaseSystemId(), false))) {
                        return schemaGrammar;
                    }
                }
                catch (URI.MalformedURIException malformedURIException) {
                    // empty catch block
                }
            }
            if ((string = xSDDescription.getTargetNamespace()) != null) {
                string = this.fSymbolTable.addSymbol(string);
            }
        }
        this.prepareForParse();
        Element element = null;
        element = xMLInputSource instanceof DOMInputSource ? this.getSchemaDocument(string, (DOMInputSource)xMLInputSource, s2 == 3, s2, null) : (xMLInputSource instanceof SAXInputSource ? this.getSchemaDocument(string, (SAXInputSource)xMLInputSource, s2 == 3, s2, null) : (xMLInputSource instanceof StAXInputSource ? this.getSchemaDocument(string, (StAXInputSource)xMLInputSource, s2 == 3, s2, null) : (xMLInputSource instanceof XSInputSource ? this.getSchemaDocument((XSInputSource)xMLInputSource, xSDDescription) : this.getSchemaDocument(string, xMLInputSource, s2 == 3, s2, null))));
        if (element == null) {
            if (xMLInputSource instanceof XSInputSource) {
                return this.fGrammarBucket.getGrammar(xSDDescription.getTargetNamespace());
            }
            return schemaGrammar;
        }
        if (s2 == 3) {
            object = element;
            string = DOMUtil.getAttrValue((Element)object, SchemaSymbols.ATT_TARGETNAMESPACE);
            if (string != null && string.length() > 0) {
                string = this.fSymbolTable.addSymbol(string);
                xSDDescription.setTargetNamespace(string);
            } else {
                string = null;
            }
            schemaGrammar = this.findGrammar(xSDDescription, this.fNamespaceGrowth);
            String string2 = XMLEntityManager.expandSystemId(xMLInputSource.getSystemId(), xMLInputSource.getBaseSystemId(), false);
            if (schemaGrammar != null && (!this.fNamespaceGrowth || string2 != null && schemaGrammar.getDocumentLocations().contains(string2))) {
                return schemaGrammar;
            }
            object2 = new XSDKey(string2, s2, string);
            this.fTraversed.put(object2, element);
            if (string2 != null) {
                this.fDoc2SystemId.put(element, string2);
            }
        }
        this.prepareForTraverse();
        this.fRoot = this.constructTrees(element, xMLInputSource.getSystemId(), xSDDescription, schemaGrammar != null);
        if (this.fRoot == null) {
            return null;
        }
        this.buildGlobalNameRegistries();
        object = this.fValidateAnnotations ? new ArrayList() : null;
        this.traverseSchemas((ArrayList)object);
        this.traverseLocalElements();
        this.resolveKeyRefs();
        int n2 = this.fAllTNSs.size() - 1;
        while (n2 >= 0) {
            object2 = (String)this.fAllTNSs.elementAt(n2);
            Vector vector = (Vector)this.fImportMap.get(object2);
            SchemaGrammar schemaGrammar2 = this.fGrammarBucket.getGrammar(this.emptyString2Null((String)object2));
            if (schemaGrammar2 != null) {
                int n3 = 0;
                int n4 = 0;
                while (n4 < vector.size()) {
                    SchemaGrammar schemaGrammar3 = this.fGrammarBucket.getGrammar((String)vector.elementAt(n4));
                    if (schemaGrammar3 != null) {
                        vector.setElementAt(schemaGrammar3, n3++);
                    }
                    ++n4;
                }
                vector.setSize(n3);
                schemaGrammar2.setImportedGrammars(vector);
            }
            --n2;
        }
        if (this.fValidateAnnotations && object.size() > 0) {
            this.validateAnnotations((ArrayList)object);
        }
        return this.fGrammarBucket.getGrammar(this.fRoot.fTargetNamespace);
    }

    private void validateAnnotations(ArrayList arrayList) {
        if (this.fAnnotationValidator == null) {
            this.createAnnotationValidator();
        }
        int n2 = arrayList.size();
        XMLInputSource xMLInputSource = new XMLInputSource(null, null, null);
        this.fGrammarBucketAdapter.refreshGrammars(this.fGrammarBucket);
        int n3 = 0;
        while (n3 < n2) {
            xMLInputSource.setSystemId((String)arrayList.get(n3));
            XSAnnotationInfo xSAnnotationInfo = (XSAnnotationInfo)arrayList.get(n3 + 1);
            while (xSAnnotationInfo != null) {
                xMLInputSource.setCharacterStream(new StringReader(xSAnnotationInfo.fAnnotation));
                try {
                    this.fAnnotationValidator.parse(xMLInputSource);
                }
                catch (IOException iOException) {
                    // empty catch block
                }
                xSAnnotationInfo = xSAnnotationInfo.next;
            }
            n3 += 2;
        }
    }

    private void createAnnotationValidator() {
        this.fAnnotationValidator = new XML11Configuration();
        this.fGrammarBucketAdapter = new XSAnnotationGrammarPool(null);
        this.fAnnotationValidator.setFeature("http://xml.org/sax/features/validation", true);
        this.fAnnotationValidator.setFeature("http://apache.org/xml/features/validation/schema", true);
        this.fAnnotationValidator.setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarBucketAdapter);
        XMLErrorHandler xMLErrorHandler = this.fErrorReporter.getErrorHandler();
        this.fAnnotationValidator.setProperty("http://apache.org/xml/properties/internal/error-handler", xMLErrorHandler != null ? xMLErrorHandler : new DefaultErrorHandler());
        Locale locale = this.fErrorReporter.getLocale();
        this.fAnnotationValidator.setProperty("http://apache.org/xml/properties/locale", locale);
    }

    SchemaGrammar getGrammar(String string) {
        return this.fGrammarBucket.getGrammar(string);
    }

    protected SchemaGrammar findGrammar(XSDDescription xSDDescription, boolean bl) {
        SchemaGrammar schemaGrammar = this.fGrammarBucket.getGrammar(xSDDescription.getTargetNamespace());
        if (schemaGrammar == null && this.fGrammarPool != null && (schemaGrammar = (SchemaGrammar)this.fGrammarPool.retrieveGrammar(xSDDescription)) != null && !this.fGrammarBucket.putGrammar(schemaGrammar, true, bl)) {
            this.reportSchemaWarning("GrammarConflict", null, null);
            schemaGrammar = null;
        }
        return schemaGrammar;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected XSDocumentInfo constructTrees(Element var1_1, String var2_2, XSDDescription var3_3, boolean var4_4) {
        if (var1_1 == null) {
            return null;
        }
        var5_5 = var3_3.getTargetNamespace();
        var6_6 = var3_3.getContextType();
        var7_7 = null;
        try {
            var7_7 = new XSDocumentInfo(var1_1, this.fAttributeChecker, this.fSymbolTable);
        }
        catch (XMLSchemaException var8_8) {
            this.reportSchemaError(XSDHandler.ELE_ERROR_CODES[var6_6], new Object[]{var2_2}, var1_1);
            return null;
        }
        if (var7_7.fTargetNamespace != null && var7_7.fTargetNamespace.length() == 0) {
            this.reportSchemaWarning("EmptyTargetNamespace", new Object[]{var2_2}, var1_1);
            var7_7.fTargetNamespace = null;
        }
        if (var5_5 != null) {
            var8_9 = 0;
            if (var6_6 == 0 || var6_6 == 1) {
                if (var7_7.fTargetNamespace == null) {
                    var7_7.fTargetNamespace = var5_5;
                    var7_7.fIsChameleonSchema = true;
                } else if (var5_5 != var7_7.fTargetNamespace) {
                    this.reportSchemaError(XSDHandler.NS_ERROR_CODES[var6_6][var8_9], new Object[]{var5_5, var7_7.fTargetNamespace}, var1_1);
                    return null;
                }
            } else if (var6_6 != 3 && var5_5 != var7_7.fTargetNamespace) {
                this.reportSchemaError(XSDHandler.NS_ERROR_CODES[var6_6][var8_9], new Object[]{var5_5, var7_7.fTargetNamespace}, var1_1);
                return null;
            }
        } else if (var7_7.fTargetNamespace != null) {
            if (var6_6 != 3) {
                var8_10 = 1;
                this.reportSchemaError(XSDHandler.NS_ERROR_CODES[var6_6][var8_10], new Object[]{var5_5, var7_7.fTargetNamespace}, var1_1);
                return null;
            }
            var3_3.setTargetNamespace(var7_7.fTargetNamespace);
            var5_5 = var7_7.fTargetNamespace;
        }
        var7_7.addAllowedNS(var7_7.fTargetNamespace);
        var8_11 = null;
        if (var4_4) {
            var9_12 = this.fGrammarBucket.getGrammar(var7_7.fTargetNamespace);
            if (var9_12.isImmutable()) {
                var8_11 = new SchemaGrammar((SchemaGrammar)var9_12);
                this.fGrammarBucket.putGrammar(var8_11);
                this.updateImportListWith(var8_11);
            } else {
                var8_11 = var9_12;
            }
            this.updateImportListFor(var8_11);
        } else if (var6_6 == 0 || var6_6 == 1) {
            var8_11 = this.fGrammarBucket.getGrammar(var7_7.fTargetNamespace);
        } else if (this.fHonourAllSchemaLocations && var6_6 == 2) {
            var8_11 = this.findGrammar(var3_3, false);
            if (var8_11 == null) {
                var8_11 = new SchemaGrammar(var7_7.fTargetNamespace, var3_3.makeClone(), this.fSymbolTable);
                this.fGrammarBucket.putGrammar(var8_11);
            }
        } else {
            var8_11 = new SchemaGrammar(var7_7.fTargetNamespace, var3_3.makeClone(), this.fSymbolTable);
            this.fGrammarBucket.putGrammar(var8_11);
        }
        var8_11.addDocument(null, (String)this.fDoc2SystemId.get(var7_7.fSchemaElement));
        this.fDoc2XSDocumentMap.put(var1_1, var7_7);
        var9_12 = new Vector<E>();
        var10_13 = var1_1;
        var11_14 = null;
        var12_15 = DOMUtil.getFirstChildElement(var10_13);
        while (var12_15 != null) {
            block55 : {
                var13_16 = null;
                var14_17 = null;
                var15_18 = DOMUtil.getLocalName(var12_15);
                var16_19 = -1;
                var17_20 = false;
                if (var15_18.equals(SchemaSymbols.ELT_ANNOTATION)) ** GOTO lbl194
                if (!var15_18.equals(SchemaSymbols.ELT_IMPORT)) ** GOTO lbl127
                var16_19 = 2;
                var18_21 = this.fAttributeChecker.checkAttributes(var12_15, true, var7_7);
                var14_17 = (String)var18_21[XSAttributeChecker.ATTIDX_SCHEMALOCATION];
                var13_16 = (String)var18_21[XSAttributeChecker.ATTIDX_NAMESPACE];
                if (var13_16 != null) {
                    var13_16 = this.fSymbolTable.addSymbol(var13_16);
                }
                if ((var19_22 = DOMUtil.getFirstChildElement(var12_15)) != null) {
                    var20_24 = DOMUtil.getLocalName(var19_22);
                    if (var20_24.equals(SchemaSymbols.ELT_ANNOTATION)) {
                        var8_11.addAnnotation(this.fElementTraverser.traverseAnnotationDecl(var19_22, (Object[])var18_21, true, var7_7));
                    } else {
                        this.reportSchemaError("s4s-elt-must-match.1", new Object[]{var15_18, "annotation?", var20_24}, var12_15);
                    }
                    if (DOMUtil.getNextSiblingElement(var19_22) != null) {
                        this.reportSchemaError("s4s-elt-must-match.1", new Object[]{var15_18, "annotation?", DOMUtil.getLocalName(DOMUtil.getNextSiblingElement(var19_22))}, var12_15);
                    }
                } else {
                    var20_24 = DOMUtil.getSyntheticAnnotation(var12_15);
                    if (var20_24 != null) {
                        var8_11.addAnnotation(this.fElementTraverser.traverseSyntheticAnnotation(var12_15, var20_24, (Object[])var18_21, true, var7_7));
                    }
                }
                this.fAttributeChecker.returnAttrArray((Object[])var18_21, var7_7);
                if (var13_16 != var7_7.fTargetNamespace) ** GOTO lbl96
                this.reportSchemaError(var13_16 != null ? "src-import.1.1" : "src-import.1.2", new Object[]{var13_16}, var12_15);
                ** GOTO lbl194
lbl96: // 1 sources:
                if (!var7_7.isAllowedNS(var13_16)) ** GOTO lbl99
                if (this.fHonourAllSchemaLocations || this.fNamespaceGrowth) ** GOTO lbl100
                ** GOTO lbl194
lbl99: // 1 sources:
                var7_7.addAllowedNS(var13_16);
lbl100: // 2 sources:
                if ((var21_26 = (Vector)this.fImportMap.get(var20_24 = this.null2EmptyString(var7_7.fTargetNamespace))) == null) {
                    this.fAllTNSs.addElement(var20_24);
                    var21_26 = new Vector<String>();
                    this.fImportMap.put(var20_24, var21_26);
                    var21_26.addElement((String)var13_16);
                } else if (!var21_26.contains(var13_16)) {
                    var21_26.addElement(var13_16);
                }
                this.fSchemaGrammarDescription.reset();
                this.fSchemaGrammarDescription.setContextType(2);
                this.fSchemaGrammarDescription.setBaseSystemId(this.doc2SystemId(var1_1));
                this.fSchemaGrammarDescription.setLiteralSystemId(var14_17);
                this.fSchemaGrammarDescription.setLocationHints(new String[]{var14_17});
                this.fSchemaGrammarDescription.setTargetNamespace(var13_16);
                var22_27 = this.findGrammar(this.fSchemaGrammarDescription, this.fNamespaceGrowth);
                if (var22_27 == null) ** GOTO lbl125
                if (!this.fNamespaceGrowth) ** GOTO lbl124
                try {
                    if (!var22_27.getDocumentLocations().contains(XMLEntityManager.expandSystemId(var14_17, this.fSchemaGrammarDescription.getBaseSystemId(), false))) {
                        var17_20 = true;
                    }
                    break block55;
                }
                catch (URI.MalformedURIException var23_29) {}
                ** GOTO lbl125
lbl124: // 1 sources:
                if (!this.fHonourAllSchemaLocations || this.isExistingGrammar(this.fSchemaGrammarDescription, false)) ** GOTO lbl194
lbl125: // 4 sources:
                var11_14 = this.resolveSchema(this.fSchemaGrammarDescription, false, var12_15, var22_27 == null);
                ** GOTO lbl186
lbl127: // 1 sources:
                if (!var15_18.equals(SchemaSymbols.ELT_INCLUDE) && !var15_18.equals(SchemaSymbols.ELT_REDEFINE)) break;
                var18_21 = this.fAttributeChecker.checkAttributes(var12_15, true, var7_7);
                var14_17 = (String)var18_21[XSAttributeChecker.ATTIDX_SCHEMALOCATION];
                if (var15_18.equals(SchemaSymbols.ELT_REDEFINE)) {
                    this.fRedefine2NSSupport.put(var12_15, new SchemaNamespaceSupport(var7_7.fNamespaceSupport));
                }
                if (!var15_18.equals(SchemaSymbols.ELT_INCLUDE)) ** GOTO lbl147
                var19_22 = DOMUtil.getFirstChildElement(var12_15);
                if (var19_22 == null) ** GOTO lbl143
                var20_24 = DOMUtil.getLocalName(var19_22);
                if (var20_24.equals(SchemaSymbols.ELT_ANNOTATION)) {
                    var8_11.addAnnotation(this.fElementTraverser.traverseAnnotationDecl(var19_22, (Object[])var18_21, true, var7_7));
                } else {
                    this.reportSchemaError("s4s-elt-must-match.1", new Object[]{var15_18, "annotation?", var20_24}, var12_15);
                }
                if (DOMUtil.getNextSiblingElement(var19_22) != null) {
                    this.reportSchemaError("s4s-elt-must-match.1", new Object[]{var15_18, "annotation?", DOMUtil.getLocalName(DOMUtil.getNextSiblingElement(var19_22))}, var12_15);
                }
                ** GOTO lbl159
lbl143: // 1 sources:
                var20_24 = DOMUtil.getSyntheticAnnotation(var12_15);
                if (var20_24 == null) ** GOTO lbl159
                var8_11.addAnnotation(this.fElementTraverser.traverseSyntheticAnnotation(var12_15, var20_24, (Object[])var18_21, true, var7_7));
                ** GOTO lbl159
lbl147: // 1 sources:
                var19_22 = DOMUtil.getFirstChildElement(var12_15);
                while (var19_22 != null) {
                    var20_24 = DOMUtil.getLocalName(var19_22);
                    if (var20_24.equals(SchemaSymbols.ELT_ANNOTATION)) {
                        var8_11.addAnnotation(this.fElementTraverser.traverseAnnotationDecl(var19_22, (Object[])var18_21, true, var7_7));
                        DOMUtil.setHidden(var19_22, this.fHiddenNodes);
                    } else {
                        var21_26 = DOMUtil.getSyntheticAnnotation(var12_15);
                        if (var21_26 != null) {
                            var8_11.addAnnotation(this.fElementTraverser.traverseSyntheticAnnotation(var12_15, (String)var21_26, (Object[])var18_21, true, var7_7));
                        }
                    }
                    var19_22 = DOMUtil.getNextSiblingElement(var19_22);
                }
lbl159: // 4 sources:
                this.fAttributeChecker.returnAttrArray((Object[])var18_21, var7_7);
                if (var14_17 == null) {
                    this.reportSchemaError("s4s-att-must-appear", new Object[]{"<include> or <redefine>", "schemaLocation"}, var12_15);
                }
                var19_23 = false;
                var16_19 = 0;
                if (var15_18.equals(SchemaSymbols.ELT_REDEFINE)) {
                    var19_23 = this.nonAnnotationContent(var12_15);
                    var16_19 = 1;
                }
                this.fSchemaGrammarDescription.reset();
                this.fSchemaGrammarDescription.setContextType(var16_19);
                this.fSchemaGrammarDescription.setBaseSystemId(this.doc2SystemId(var1_1));
                this.fSchemaGrammarDescription.setLocationHints(new String[]{var14_17});
                this.fSchemaGrammarDescription.setTargetNamespace(var5_5);
                var20_25 = false;
                var21_26 = this.resolveSchemaSource(this.fSchemaGrammarDescription, var19_23, var12_15, true);
                if (this.fNamespaceGrowth && var16_19 == 0) {
                    try {
                        var22_27 = XMLEntityManager.expandSystemId(var21_26.getSystemId(), var21_26.getBaseSystemId(), false);
                        var20_25 = var8_11.getDocumentLocations().contains((String)var22_27);
                    }
                    catch (URI.MalformedURIException var22_28) {
                        // empty catch block
                    }
                }
                if (!var20_25) {
                    var11_14 = this.resolveSchema((XMLInputSource)var21_26, this.fSchemaGrammarDescription, var19_23, var12_15);
                    var13_16 = var7_7.fTargetNamespace;
                } else {
                    this.fLastSchemaWasDuplicate = true;
                }
lbl186: // 3 sources:
                var18_21 = null;
                var18_21 = this.fLastSchemaWasDuplicate != false ? (var11_14 == null ? null : (XSDocumentInfo)this.fDoc2XSDocumentMap.get(var11_14)) : this.constructTrees(var11_14, var14_17, this.fSchemaGrammarDescription, var17_20);
                if (var15_18.equals(SchemaSymbols.ELT_REDEFINE) && var18_21 != null) {
                    this.fRedefine2XSDMap.put(var12_15, var18_21);
                }
                if (var11_14 != null) {
                    if (var18_21 != null) {
                        var9_12.addElement(var18_21);
                    }
                    var11_14 = null;
                }
            }
            var12_15 = DOMUtil.getNextSiblingElement(var12_15);
        }
        this.fDependencyMap.put(var7_7, var9_12);
        return var7_7;
    }

    private boolean isExistingGrammar(XSDDescription xSDDescription, boolean bl) {
        SchemaGrammar schemaGrammar = this.fGrammarBucket.getGrammar(xSDDescription.getTargetNamespace());
        if (schemaGrammar == null) {
            return this.findGrammar(xSDDescription, bl) != null;
        }
        if (schemaGrammar.isImmutable()) {
            return true;
        }
        try {
            return schemaGrammar.getDocumentLocations().contains(XMLEntityManager.expandSystemId(xSDDescription.getLiteralSystemId(), xSDDescription.getBaseSystemId(), false));
        }
        catch (URI.MalformedURIException malformedURIException) {
            return false;
        }
    }

    private void updateImportListFor(SchemaGrammar schemaGrammar) {
        Vector vector = schemaGrammar.getImportedGrammars();
        if (vector != null) {
            int n2 = 0;
            while (n2 < vector.size()) {
                SchemaGrammar schemaGrammar2 = (SchemaGrammar)vector.elementAt(n2);
                SchemaGrammar schemaGrammar3 = this.fGrammarBucket.getGrammar(schemaGrammar2.getTargetNamespace());
                if (schemaGrammar3 != null && schemaGrammar2 != schemaGrammar3) {
                    vector.set(n2, schemaGrammar3);
                }
                ++n2;
            }
        }
    }

    private void updateImportListWith(SchemaGrammar schemaGrammar) {
        SchemaGrammar[] arrschemaGrammar = this.fGrammarBucket.getGrammars();
        int n2 = 0;
        while (n2 < arrschemaGrammar.length) {
            Vector vector;
            SchemaGrammar schemaGrammar2 = arrschemaGrammar[n2];
            if (schemaGrammar2 != schemaGrammar && (vector = schemaGrammar2.getImportedGrammars()) != null) {
                int n3 = 0;
                while (n3 < vector.size()) {
                    SchemaGrammar schemaGrammar3 = (SchemaGrammar)vector.elementAt(n3);
                    if (this.null2EmptyString(schemaGrammar3.getTargetNamespace()).equals(this.null2EmptyString(schemaGrammar.getTargetNamespace()))) {
                        if (schemaGrammar3 == schemaGrammar) break;
                        vector.set(n3, schemaGrammar);
                        break;
                    }
                    ++n3;
                }
            }
            ++n2;
        }
    }

    protected void buildGlobalNameRegistries() {
        Stack<XSDocumentInfo> stack = new Stack<XSDocumentInfo>();
        stack.push(this.fRoot);
        while (!stack.empty()) {
            Object object;
            XSDocumentInfo xSDocumentInfo = (XSDocumentInfo)stack.pop();
            Element element = xSDocumentInfo.fSchemaElement;
            if (DOMUtil.isHidden(element, this.fHiddenNodes)) continue;
            Element element2 = element;
            boolean bl = true;
            Element element3 = DOMUtil.getFirstChildElement(element2);
            while (element3 != null) {
                if (!DOMUtil.getLocalName(element3).equals(SchemaSymbols.ELT_ANNOTATION)) {
                    String string;
                    String string2;
                    if (DOMUtil.getLocalName(element3).equals(SchemaSymbols.ELT_INCLUDE) || DOMUtil.getLocalName(element3).equals(SchemaSymbols.ELT_IMPORT)) {
                        if (!bl) {
                            this.reportSchemaError("s4s-elt-invalid-content.3", new Object[]{DOMUtil.getLocalName(element3)}, element3);
                        }
                        DOMUtil.setHidden(element3, this.fHiddenNodes);
                    } else if (DOMUtil.getLocalName(element3).equals(SchemaSymbols.ELT_REDEFINE)) {
                        if (!bl) {
                            this.reportSchemaError("s4s-elt-invalid-content.3", new Object[]{DOMUtil.getLocalName(element3)}, element3);
                        }
                        object = DOMUtil.getFirstChildElement(element3);
                        while (object != null) {
                            string = DOMUtil.getAttrValue((Element)object, SchemaSymbols.ATT_NAME);
                            if (string.length() != 0) {
                                String string3;
                                string2 = xSDocumentInfo.fTargetNamespace == null ? "," + string : xSDocumentInfo.fTargetNamespace + "," + string;
                                String string4 = DOMUtil.getLocalName((Node)object);
                                if (string4.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                                    this.checkForDuplicateNames(string2, 2, this.fUnparsedAttributeGroupRegistry, this.fUnparsedAttributeGroupRegistrySub, (Element)object, xSDocumentInfo);
                                    string3 = DOMUtil.getAttrValue((Element)object, SchemaSymbols.ATT_NAME) + "_fn3dktizrknc9pi";
                                    this.renameRedefiningComponents(xSDocumentInfo, (Element)object, SchemaSymbols.ELT_ATTRIBUTEGROUP, string, string3);
                                } else if (string4.equals(SchemaSymbols.ELT_COMPLEXTYPE) || string4.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                                    this.checkForDuplicateNames(string2, 7, this.fUnparsedTypeRegistry, this.fUnparsedTypeRegistrySub, (Element)object, xSDocumentInfo);
                                    string3 = DOMUtil.getAttrValue((Element)object, SchemaSymbols.ATT_NAME) + "_fn3dktizrknc9pi";
                                    if (string4.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                                        this.renameRedefiningComponents(xSDocumentInfo, (Element)object, SchemaSymbols.ELT_COMPLEXTYPE, string, string3);
                                    } else {
                                        this.renameRedefiningComponents(xSDocumentInfo, (Element)object, SchemaSymbols.ELT_SIMPLETYPE, string, string3);
                                    }
                                } else if (string4.equals(SchemaSymbols.ELT_GROUP)) {
                                    this.checkForDuplicateNames(string2, 4, this.fUnparsedGroupRegistry, this.fUnparsedGroupRegistrySub, (Element)object, xSDocumentInfo);
                                    string3 = DOMUtil.getAttrValue((Element)object, SchemaSymbols.ATT_NAME) + "_fn3dktizrknc9pi";
                                    this.renameRedefiningComponents(xSDocumentInfo, (Element)object, SchemaSymbols.ELT_GROUP, string, string3);
                                }
                            }
                            object = DOMUtil.getNextSiblingElement((Node)object);
                        }
                    } else {
                        bl = false;
                        object = DOMUtil.getAttrValue(element3, SchemaSymbols.ATT_NAME);
                        if (object.length() != 0) {
                            string = xSDocumentInfo.fTargetNamespace == null ? "," + (String)object : xSDocumentInfo.fTargetNamespace + "," + (String)object;
                            string2 = DOMUtil.getLocalName(element3);
                            if (string2.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
                                this.checkForDuplicateNames(string, 1, this.fUnparsedAttributeRegistry, this.fUnparsedAttributeRegistrySub, element3, xSDocumentInfo);
                            } else if (string2.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                                this.checkForDuplicateNames(string, 2, this.fUnparsedAttributeGroupRegistry, this.fUnparsedAttributeGroupRegistrySub, element3, xSDocumentInfo);
                            } else if (string2.equals(SchemaSymbols.ELT_COMPLEXTYPE) || string2.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                                this.checkForDuplicateNames(string, 7, this.fUnparsedTypeRegistry, this.fUnparsedTypeRegistrySub, element3, xSDocumentInfo);
                            } else if (string2.equals(SchemaSymbols.ELT_ELEMENT)) {
                                this.checkForDuplicateNames(string, 3, this.fUnparsedElementRegistry, this.fUnparsedElementRegistrySub, element3, xSDocumentInfo);
                            } else if (string2.equals(SchemaSymbols.ELT_GROUP)) {
                                this.checkForDuplicateNames(string, 4, this.fUnparsedGroupRegistry, this.fUnparsedGroupRegistrySub, element3, xSDocumentInfo);
                            } else if (string2.equals(SchemaSymbols.ELT_NOTATION)) {
                                this.checkForDuplicateNames(string, 6, this.fUnparsedNotationRegistry, this.fUnparsedNotationRegistrySub, element3, xSDocumentInfo);
                            }
                        }
                    }
                }
                element3 = DOMUtil.getNextSiblingElement(element3);
            }
            DOMUtil.setHidden(element, this.fHiddenNodes);
            object = (Vector)this.fDependencyMap.get(xSDocumentInfo);
            int n2 = 0;
            while (n2 < object.size()) {
                stack.push((XSDocumentInfo)object.elementAt(n2));
                ++n2;
            }
        }
    }

    protected void traverseSchemas(ArrayList arrayList) {
        this.setSchemasVisible(this.fRoot);
        Stack<XSDocumentInfo> stack = new Stack<XSDocumentInfo>();
        stack.push(this.fRoot);
        while (!stack.empty()) {
            Object object;
            XSDocumentInfo xSDocumentInfo = (XSDocumentInfo)stack.pop();
            Element element = xSDocumentInfo.fSchemaElement;
            SchemaGrammar schemaGrammar = this.fGrammarBucket.getGrammar(xSDocumentInfo.fTargetNamespace);
            if (DOMUtil.isHidden(element, this.fHiddenNodes)) continue;
            Element element2 = element;
            boolean bl = false;
            Element element3 = DOMUtil.getFirstVisibleChildElement(element2, this.fHiddenNodes);
            while (element3 != null) {
                DOMUtil.setHidden(element3, this.fHiddenNodes);
                object = DOMUtil.getLocalName(element3);
                if (DOMUtil.getLocalName(element3).equals(SchemaSymbols.ELT_REDEFINE)) {
                    xSDocumentInfo.backupNSSupport((SchemaNamespaceSupport)this.fRedefine2NSSupport.get(element3));
                    Element element4 = DOMUtil.getFirstVisibleChildElement(element3, this.fHiddenNodes);
                    while (element4 != null) {
                        String string = DOMUtil.getLocalName(element4);
                        DOMUtil.setHidden(element4, this.fHiddenNodes);
                        if (string.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                            this.fAttributeGroupTraverser.traverseGlobal(element4, xSDocumentInfo, schemaGrammar);
                        } else if (string.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                            this.fComplexTypeTraverser.traverseGlobal(element4, xSDocumentInfo, schemaGrammar);
                        } else if (string.equals(SchemaSymbols.ELT_GROUP)) {
                            this.fGroupTraverser.traverseGlobal(element4, xSDocumentInfo, schemaGrammar);
                        } else if (string.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                            this.fSimpleTypeTraverser.traverseGlobal(element4, xSDocumentInfo, schemaGrammar);
                        } else {
                            this.reportSchemaError("s4s-elt-must-match.1", new Object[]{DOMUtil.getLocalName(element3), "(annotation | (simpleType | complexType | group | attributeGroup))*", string}, element4);
                        }
                        element4 = DOMUtil.getNextVisibleSiblingElement(element4, this.fHiddenNodes);
                    }
                    xSDocumentInfo.restoreNSSupport();
                } else if (object.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
                    this.fAttributeTraverser.traverseGlobal(element3, xSDocumentInfo, schemaGrammar);
                } else if (object.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                    this.fAttributeGroupTraverser.traverseGlobal(element3, xSDocumentInfo, schemaGrammar);
                } else if (object.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                    this.fComplexTypeTraverser.traverseGlobal(element3, xSDocumentInfo, schemaGrammar);
                } else if (object.equals(SchemaSymbols.ELT_ELEMENT)) {
                    this.fElementTraverser.traverseGlobal(element3, xSDocumentInfo, schemaGrammar);
                } else if (object.equals(SchemaSymbols.ELT_GROUP)) {
                    this.fGroupTraverser.traverseGlobal(element3, xSDocumentInfo, schemaGrammar);
                } else if (object.equals(SchemaSymbols.ELT_NOTATION)) {
                    this.fNotationTraverser.traverse(element3, xSDocumentInfo, schemaGrammar);
                } else if (object.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                    this.fSimpleTypeTraverser.traverseGlobal(element3, xSDocumentInfo, schemaGrammar);
                } else if (object.equals(SchemaSymbols.ELT_ANNOTATION)) {
                    schemaGrammar.addAnnotation(this.fElementTraverser.traverseAnnotationDecl(element3, xSDocumentInfo.getSchemaAttrs(), true, xSDocumentInfo));
                    bl = true;
                } else {
                    this.reportSchemaError("s4s-elt-invalid-content.1", new Object[]{SchemaSymbols.ELT_SCHEMA, DOMUtil.getLocalName(element3)}, element3);
                }
                element3 = DOMUtil.getNextVisibleSiblingElement(element3, this.fHiddenNodes);
            }
            if (!bl && (object = DOMUtil.getSyntheticAnnotation(element2)) != null) {
                schemaGrammar.addAnnotation(this.fElementTraverser.traverseSyntheticAnnotation(element2, (String)object, xSDocumentInfo.getSchemaAttrs(), true, xSDocumentInfo));
            }
            if (arrayList != null && (object = xSDocumentInfo.getAnnotations()) != null) {
                arrayList.add(this.doc2SystemId(element));
                arrayList.add(object);
            }
            xSDocumentInfo.returnSchemaAttrs();
            DOMUtil.setHidden(element, this.fHiddenNodes);
            object = (Vector)this.fDependencyMap.get(xSDocumentInfo);
            int n2 = 0;
            while (n2 < object.size()) {
                stack.push((XSDocumentInfo)object.elementAt(n2));
                ++n2;
            }
        }
    }

    private final boolean needReportTNSError(String string) {
        if (this.fReportedTNS == null) {
            this.fReportedTNS = new Vector();
        } else if (this.fReportedTNS.contains(string)) {
            return false;
        }
        this.fReportedTNS.addElement(string);
        return true;
    }

    void addGlobalAttributeDecl(XSAttributeDecl xSAttributeDecl) {
        String string;
        String string2 = xSAttributeDecl.getNamespace();
        String string3 = string = string2 == null || string2.length() == 0 ? "," + xSAttributeDecl.getName() : string2 + "," + xSAttributeDecl.getName();
        if (this.fGlobalAttrDecls.get(string) == null) {
            this.fGlobalAttrDecls.put(string, xSAttributeDecl);
        }
    }

    void addGlobalAttributeGroupDecl(XSAttributeGroupDecl xSAttributeGroupDecl) {
        String string;
        String string2 = xSAttributeGroupDecl.getNamespace();
        String string3 = string = string2 == null || string2.length() == 0 ? "," + xSAttributeGroupDecl.getName() : string2 + "," + xSAttributeGroupDecl.getName();
        if (this.fGlobalAttrGrpDecls.get(string) == null) {
            this.fGlobalAttrGrpDecls.put(string, xSAttributeGroupDecl);
        }
    }

    void addGlobalElementDecl(XSElementDecl xSElementDecl) {
        String string;
        String string2 = xSElementDecl.getNamespace();
        String string3 = string = string2 == null || string2.length() == 0 ? "," + xSElementDecl.getName() : string2 + "," + xSElementDecl.getName();
        if (this.fGlobalElemDecls.get(string) == null) {
            this.fGlobalElemDecls.put(string, xSElementDecl);
        }
    }

    void addGlobalGroupDecl(XSGroupDecl xSGroupDecl) {
        String string;
        String string2 = xSGroupDecl.getNamespace();
        String string3 = string = string2 == null || string2.length() == 0 ? "," + xSGroupDecl.getName() : string2 + "," + xSGroupDecl.getName();
        if (this.fGlobalGroupDecls.get(string) == null) {
            this.fGlobalGroupDecls.put(string, xSGroupDecl);
        }
    }

    void addGlobalNotationDecl(XSNotationDecl xSNotationDecl) {
        String string;
        String string2 = xSNotationDecl.getNamespace();
        String string3 = string = string2 == null || string2.length() == 0 ? "," + xSNotationDecl.getName() : string2 + "," + xSNotationDecl.getName();
        if (this.fGlobalNotationDecls.get(string) == null) {
            this.fGlobalNotationDecls.put(string, xSNotationDecl);
        }
    }

    void addGlobalTypeDecl(XSTypeDefinition xSTypeDefinition) {
        String string;
        String string2 = xSTypeDefinition.getNamespace();
        String string3 = string = string2 == null || string2.length() == 0 ? "," + xSTypeDefinition.getName() : string2 + "," + xSTypeDefinition.getName();
        if (this.fGlobalTypeDecls.get(string) == null) {
            this.fGlobalTypeDecls.put(string, xSTypeDefinition);
        }
    }

    void addIDConstraintDecl(IdentityConstraint identityConstraint) {
        String string;
        String string2 = identityConstraint.getNamespace();
        String string3 = string = string2 == null || string2.length() == 0 ? "," + identityConstraint.getIdentityConstraintName() : string2 + "," + identityConstraint.getIdentityConstraintName();
        if (this.fGlobalIDConstraintDecls.get(string) == null) {
            this.fGlobalIDConstraintDecls.put(string, identityConstraint);
        }
    }

    private XSAttributeDecl getGlobalAttributeDecl(String string) {
        return (XSAttributeDecl)this.fGlobalAttrDecls.get(string);
    }

    private XSAttributeGroupDecl getGlobalAttributeGroupDecl(String string) {
        return (XSAttributeGroupDecl)this.fGlobalAttrGrpDecls.get(string);
    }

    private XSElementDecl getGlobalElementDecl(String string) {
        return (XSElementDecl)this.fGlobalElemDecls.get(string);
    }

    private XSGroupDecl getGlobalGroupDecl(String string) {
        return (XSGroupDecl)this.fGlobalGroupDecls.get(string);
    }

    private XSNotationDecl getGlobalNotationDecl(String string) {
        return (XSNotationDecl)this.fGlobalNotationDecls.get(string);
    }

    private XSTypeDefinition getGlobalTypeDecl(String string) {
        return (XSTypeDefinition)this.fGlobalTypeDecls.get(string);
    }

    private IdentityConstraint getIDConstraintDecl(String string) {
        return (IdentityConstraint)this.fGlobalIDConstraintDecls.get(string);
    }

    protected Object getGlobalDecl(XSDocumentInfo xSDocumentInfo, int n2, QName qName, Element element) {
        String string;
        Object object;
        Object object2;
        if (qName.uri != null && qName.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA && n2 == 7 && (object = SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(qName.localpart)) != null) {
            return object;
        }
        if (!xSDocumentInfo.isAllowedNS(qName.uri) && xSDocumentInfo.needReportTNSError(qName.uri)) {
            object = qName.uri == null ? "src-resolve.4.1" : "src-resolve.4.2";
            this.reportSchemaError((String)object, new Object[]{this.fDoc2SystemId.get(xSDocumentInfo.fSchemaElement), qName.uri, qName.rawname}, element);
        }
        if ((object = this.fGrammarBucket.getGrammar(qName.uri)) == null) {
            if (this.needReportTNSError(qName.uri)) {
                this.reportSchemaError("src-resolve", new Object[]{qName.rawname, COMP_TYPE[n2]}, element);
            }
            return null;
        }
        Object object3 = this.getGlobalDeclFromGrammar((SchemaGrammar)object, n2, qName.localpart);
        String string2 = string = qName.uri == null ? "," + qName.localpart : qName.uri + "," + qName.localpart;
        if (!this.fTolerateDuplicates) {
            if (object3 != null) {
                return object3;
            }
        } else {
            object2 = this.getGlobalDecl(string, n2);
            if (object2 != null) {
                return object2;
            }
        }
        object2 = null;
        Element element2 = null;
        XSDocumentInfo xSDocumentInfo2 = null;
        switch (n2) {
            case 1: {
                element2 = (Element)this.fUnparsedAttributeRegistry.get(string);
                xSDocumentInfo2 = (XSDocumentInfo)this.fUnparsedAttributeRegistrySub.get(string);
                break;
            }
            case 2: {
                element2 = (Element)this.fUnparsedAttributeGroupRegistry.get(string);
                xSDocumentInfo2 = (XSDocumentInfo)this.fUnparsedAttributeGroupRegistrySub.get(string);
                break;
            }
            case 3: {
                element2 = (Element)this.fUnparsedElementRegistry.get(string);
                xSDocumentInfo2 = (XSDocumentInfo)this.fUnparsedElementRegistrySub.get(string);
                break;
            }
            case 4: {
                element2 = (Element)this.fUnparsedGroupRegistry.get(string);
                xSDocumentInfo2 = (XSDocumentInfo)this.fUnparsedGroupRegistrySub.get(string);
                break;
            }
            case 5: {
                element2 = (Element)this.fUnparsedIdentityConstraintRegistry.get(string);
                xSDocumentInfo2 = (XSDocumentInfo)this.fUnparsedIdentityConstraintRegistrySub.get(string);
                break;
            }
            case 6: {
                element2 = (Element)this.fUnparsedNotationRegistry.get(string);
                xSDocumentInfo2 = (XSDocumentInfo)this.fUnparsedNotationRegistrySub.get(string);
                break;
            }
            case 7: {
                element2 = (Element)this.fUnparsedTypeRegistry.get(string);
                xSDocumentInfo2 = (XSDocumentInfo)this.fUnparsedTypeRegistrySub.get(string);
                break;
            }
            default: {
                this.reportSchemaError("Internal-Error", new Object[]{"XSDHandler asked to locate component of type " + n2 + "; it does not recognize this type!"}, element);
            }
        }
        if (element2 == null) {
            if (object3 == null) {
                this.reportSchemaError("src-resolve", new Object[]{qName.rawname, COMP_TYPE[n2]}, element);
            }
            return object3;
        }
        object2 = this.findXSDocumentForDecl(xSDocumentInfo, element2, xSDocumentInfo2);
        if (object2 == null) {
            if (object3 == null) {
                String string3 = qName.uri == null ? "src-resolve.4.1" : "src-resolve.4.2";
                this.reportSchemaError(string3, new Object[]{this.fDoc2SystemId.get(xSDocumentInfo.fSchemaElement), qName.uri, qName.rawname}, element);
            }
            return object3;
        }
        if (DOMUtil.isHidden(element2, this.fHiddenNodes)) {
            if (object3 == null) {
                String string4 = CIRCULAR_CODES[n2];
                if (n2 == 7 && SchemaSymbols.ELT_COMPLEXTYPE.equals(DOMUtil.getLocalName(element2))) {
                    string4 = "ct-props-correct.3";
                }
                this.reportSchemaError(string4, new Object[]{qName.prefix + ":" + qName.localpart}, element);
            }
            return object3;
        }
        return this.traverseGlobalDecl(n2, element2, (XSDocumentInfo)object2, (SchemaGrammar)object);
    }

    protected Object getGlobalDecl(String string, int n2) {
        XSObject xSObject = null;
        switch (n2) {
            case 1: {
                xSObject = this.getGlobalAttributeDecl(string);
                break;
            }
            case 2: {
                xSObject = this.getGlobalAttributeGroupDecl(string);
                break;
            }
            case 3: {
                xSObject = this.getGlobalElementDecl(string);
                break;
            }
            case 4: {
                xSObject = this.getGlobalGroupDecl(string);
                break;
            }
            case 5: {
                xSObject = this.getIDConstraintDecl(string);
                break;
            }
            case 6: {
                xSObject = this.getGlobalNotationDecl(string);
                break;
            }
            case 7: {
                xSObject = this.getGlobalTypeDecl(string);
            }
        }
        return xSObject;
    }

    protected Object getGlobalDeclFromGrammar(SchemaGrammar schemaGrammar, int n2, String string) {
        XSObject xSObject = null;
        switch (n2) {
            case 1: {
                xSObject = schemaGrammar.getGlobalAttributeDecl(string);
                break;
            }
            case 2: {
                xSObject = schemaGrammar.getGlobalAttributeGroupDecl(string);
                break;
            }
            case 3: {
                xSObject = schemaGrammar.getGlobalElementDecl(string);
                break;
            }
            case 4: {
                xSObject = schemaGrammar.getGlobalGroupDecl(string);
                break;
            }
            case 5: {
                xSObject = schemaGrammar.getIDConstraintDecl(string);
                break;
            }
            case 6: {
                xSObject = schemaGrammar.getGlobalNotationDecl(string);
                break;
            }
            case 7: {
                xSObject = schemaGrammar.getGlobalTypeDecl(string);
            }
        }
        return xSObject;
    }

    protected Object getGlobalDeclFromGrammar(SchemaGrammar schemaGrammar, int n2, String string, String string2) {
        XSObject xSObject = null;
        switch (n2) {
            case 1: {
                xSObject = schemaGrammar.getGlobalAttributeDecl(string, string2);
                break;
            }
            case 2: {
                xSObject = schemaGrammar.getGlobalAttributeGroupDecl(string, string2);
                break;
            }
            case 3: {
                xSObject = schemaGrammar.getGlobalElementDecl(string, string2);
                break;
            }
            case 4: {
                xSObject = schemaGrammar.getGlobalGroupDecl(string, string2);
                break;
            }
            case 5: {
                xSObject = schemaGrammar.getIDConstraintDecl(string, string2);
                break;
            }
            case 6: {
                xSObject = schemaGrammar.getGlobalNotationDecl(string, string2);
                break;
            }
            case 7: {
                xSObject = schemaGrammar.getGlobalTypeDecl(string, string2);
            }
        }
        return xSObject;
    }

    protected Object traverseGlobalDecl(int n2, Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        XSObject xSObject = null;
        DOMUtil.setHidden(element, this.fHiddenNodes);
        SchemaNamespaceSupport schemaNamespaceSupport = null;
        Element element2 = DOMUtil.getParent(element);
        if (DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_REDEFINE)) {
            schemaNamespaceSupport = (SchemaNamespaceSupport)this.fRedefine2NSSupport.get(element2);
        }
        xSDocumentInfo.backupNSSupport(schemaNamespaceSupport);
        switch (n2) {
            case 7: {
                if (DOMUtil.getLocalName(element).equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                    xSObject = this.fComplexTypeTraverser.traverseGlobal(element, xSDocumentInfo, schemaGrammar);
                    break;
                }
                xSObject = this.fSimpleTypeTraverser.traverseGlobal(element, xSDocumentInfo, schemaGrammar);
                break;
            }
            case 1: {
                xSObject = this.fAttributeTraverser.traverseGlobal(element, xSDocumentInfo, schemaGrammar);
                break;
            }
            case 3: {
                xSObject = this.fElementTraverser.traverseGlobal(element, xSDocumentInfo, schemaGrammar);
                break;
            }
            case 2: {
                xSObject = this.fAttributeGroupTraverser.traverseGlobal(element, xSDocumentInfo, schemaGrammar);
                break;
            }
            case 4: {
                xSObject = this.fGroupTraverser.traverseGlobal(element, xSDocumentInfo, schemaGrammar);
                break;
            }
            case 6: {
                xSObject = this.fNotationTraverser.traverse(element, xSDocumentInfo, schemaGrammar);
                break;
            }
        }
        xSDocumentInfo.restoreNSSupport();
        return xSObject;
    }

    public String schemaDocument2SystemId(XSDocumentInfo xSDocumentInfo) {
        return (String)this.fDoc2SystemId.get(xSDocumentInfo.fSchemaElement);
    }

    Object getGrpOrAttrGrpRedefinedByRestriction(int n2, QName qName, XSDocumentInfo xSDocumentInfo, Element element) {
        int n3;
        String string = qName.uri != null ? qName.uri + "," + qName.localpart : "," + qName.localpart;
        String string2 = null;
        switch (n2) {
            case 2: {
                string2 = (String)this.fRedefinedRestrictedAttributeGroupRegistry.get(string);
                break;
            }
            case 4: {
                string2 = (String)this.fRedefinedRestrictedGroupRegistry.get(string);
                break;
            }
            default: {
                return null;
            }
        }
        if (string2 == null) {
            return null;
        }
        QName qName2 = new QName(XMLSymbols.EMPTY_STRING, string2.substring(n3 + 1), string2.substring(n3), (n3 = string2.indexOf(",")) == 0 ? null : string2.substring(0, n3));
        Object object = this.getGlobalDecl(xSDocumentInfo, n2, qName2, element);
        if (object == null) {
            switch (n2) {
                case 2: {
                    this.reportSchemaError("src-redefine.7.2.1", new Object[]{qName.localpart}, element);
                    break;
                }
                case 4: {
                    this.reportSchemaError("src-redefine.6.2.1", new Object[]{qName.localpart}, element);
                }
            }
            return null;
        }
        return object;
    }

    protected void resolveKeyRefs() {
        int n2 = 0;
        while (n2 < this.fKeyrefStackPos) {
            XSDocumentInfo xSDocumentInfo = this.fKeyrefsMapXSDocumentInfo[n2];
            xSDocumentInfo.fNamespaceSupport.makeGlobal();
            xSDocumentInfo.fNamespaceSupport.setEffectiveContext(this.fKeyrefNamespaceContext[n2]);
            SchemaGrammar schemaGrammar = this.fGrammarBucket.getGrammar(xSDocumentInfo.fTargetNamespace);
            DOMUtil.setHidden(this.fKeyrefs[n2], this.fHiddenNodes);
            this.fKeyrefTraverser.traverse(this.fKeyrefs[n2], this.fKeyrefElems[n2], xSDocumentInfo, schemaGrammar);
            ++n2;
        }
    }

    protected Hashtable getIDRegistry() {
        return this.fUnparsedIdentityConstraintRegistry;
    }

    protected Hashtable getIDRegistry_sub() {
        return this.fUnparsedIdentityConstraintRegistrySub;
    }

    protected void storeKeyRef(Element element, XSDocumentInfo xSDocumentInfo, XSElementDecl xSElementDecl) {
        Object object;
        String string = DOMUtil.getAttrValue(element, SchemaSymbols.ATT_NAME);
        if (string.length() != 0) {
            object = xSDocumentInfo.fTargetNamespace == null ? "," + string : xSDocumentInfo.fTargetNamespace + "," + string;
            this.checkForDuplicateNames((String)object, 5, this.fUnparsedIdentityConstraintRegistry, this.fUnparsedIdentityConstraintRegistrySub, element, xSDocumentInfo);
        }
        if (this.fKeyrefStackPos == this.fKeyrefs.length) {
            object = new Element[this.fKeyrefStackPos + 2];
            System.arraycopy(this.fKeyrefs, 0, object, 0, this.fKeyrefStackPos);
            this.fKeyrefs = object;
            XSElementDecl[] arrxSElementDecl = new XSElementDecl[this.fKeyrefStackPos + 2];
            System.arraycopy(this.fKeyrefElems, 0, arrxSElementDecl, 0, this.fKeyrefStackPos);
            this.fKeyrefElems = arrxSElementDecl;
            String[][] arrarrstring = new String[this.fKeyrefStackPos + 2][];
            System.arraycopy(this.fKeyrefNamespaceContext, 0, arrarrstring, 0, this.fKeyrefStackPos);
            this.fKeyrefNamespaceContext = arrarrstring;
            XSDocumentInfo[] arrxSDocumentInfo = new XSDocumentInfo[this.fKeyrefStackPos + 2];
            System.arraycopy(this.fKeyrefsMapXSDocumentInfo, 0, arrxSDocumentInfo, 0, this.fKeyrefStackPos);
            this.fKeyrefsMapXSDocumentInfo = arrxSDocumentInfo;
        }
        this.fKeyrefs[this.fKeyrefStackPos] = element;
        this.fKeyrefElems[this.fKeyrefStackPos] = xSElementDecl;
        this.fKeyrefNamespaceContext[this.fKeyrefStackPos] = xSDocumentInfo.fNamespaceSupport.getEffectiveLocalContext();
        this.fKeyrefsMapXSDocumentInfo[this.fKeyrefStackPos++] = xSDocumentInfo;
    }

    private Element resolveSchema(XSDDescription xSDDescription, boolean bl, Element element, boolean bl2) {
        XMLInputSource xMLInputSource = null;
        try {
            Hashtable hashtable = bl2 ? this.fLocationPairs : EMPTY_TABLE;
            xMLInputSource = XMLSchemaLoader.resolveDocument(xSDDescription, hashtable, this.fEntityResolver);
        }
        catch (IOException iOException) {
            if (bl) {
                this.reportSchemaError("schema_reference.4", new Object[]{xSDDescription.getLocationHints()[0]}, element);
            }
            this.reportSchemaWarning("schema_reference.4", new Object[]{xSDDescription.getLocationHints()[0]}, element);
        }
        if (xMLInputSource instanceof DOMInputSource) {
            return this.getSchemaDocument(xSDDescription.getTargetNamespace(), (DOMInputSource)xMLInputSource, bl, xSDDescription.getContextType(), element);
        }
        if (xMLInputSource instanceof SAXInputSource) {
            return this.getSchemaDocument(xSDDescription.getTargetNamespace(), (SAXInputSource)xMLInputSource, bl, xSDDescription.getContextType(), element);
        }
        if (xMLInputSource instanceof StAXInputSource) {
            return this.getSchemaDocument(xSDDescription.getTargetNamespace(), (StAXInputSource)xMLInputSource, bl, xSDDescription.getContextType(), element);
        }
        if (xMLInputSource instanceof XSInputSource) {
            return this.getSchemaDocument((XSInputSource)xMLInputSource, xSDDescription);
        }
        return this.getSchemaDocument(xSDDescription.getTargetNamespace(), xMLInputSource, bl, xSDDescription.getContextType(), element);
    }

    private Element resolveSchema(XMLInputSource xMLInputSource, XSDDescription xSDDescription, boolean bl, Element element) {
        if (xMLInputSource instanceof DOMInputSource) {
            return this.getSchemaDocument(xSDDescription.getTargetNamespace(), (DOMInputSource)xMLInputSource, bl, xSDDescription.getContextType(), element);
        }
        if (xMLInputSource instanceof SAXInputSource) {
            return this.getSchemaDocument(xSDDescription.getTargetNamespace(), (SAXInputSource)xMLInputSource, bl, xSDDescription.getContextType(), element);
        }
        if (xMLInputSource instanceof StAXInputSource) {
            return this.getSchemaDocument(xSDDescription.getTargetNamespace(), (StAXInputSource)xMLInputSource, bl, xSDDescription.getContextType(), element);
        }
        if (xMLInputSource instanceof XSInputSource) {
            return this.getSchemaDocument((XSInputSource)xMLInputSource, xSDDescription);
        }
        return this.getSchemaDocument(xSDDescription.getTargetNamespace(), xMLInputSource, bl, xSDDescription.getContextType(), element);
    }

    private XMLInputSource resolveSchemaSource(XSDDescription xSDDescription, boolean bl, Element element, boolean bl2) {
        XMLInputSource xMLInputSource = null;
        try {
            Hashtable hashtable = bl2 ? this.fLocationPairs : EMPTY_TABLE;
            xMLInputSource = XMLSchemaLoader.resolveDocument(xSDDescription, hashtable, this.fEntityResolver);
        }
        catch (IOException iOException) {
            if (bl) {
                this.reportSchemaError("schema_reference.4", new Object[]{xSDDescription.getLocationHints()[0]}, element);
            }
            this.reportSchemaWarning("schema_reference.4", new Object[]{xSDDescription.getLocationHints()[0]}, element);
        }
        return xMLInputSource;
    }

    private Element getSchemaDocument(String string, XMLInputSource xMLInputSource, boolean bl, short s2, Element element) {
        boolean bl2 = true;
        IOException iOException = null;
        Element element2 = null;
        try {
            if (xMLInputSource != null && (xMLInputSource.getSystemId() != null || xMLInputSource.getByteStream() != null || xMLInputSource.getCharacterStream() != null)) {
                XSDKey xSDKey = null;
                String string2 = null;
                if (s2 != 3 && (element2 = (Element)this.fTraversed.get(xSDKey = new XSDKey(string2 = XMLEntityManager.expandSystemId(xMLInputSource.getSystemId(), xMLInputSource.getBaseSystemId(), false), s2, string))) != null) {
                    this.fLastSchemaWasDuplicate = true;
                    return element2;
                }
                this.fSchemaParser.parse(xMLInputSource);
                Document document = this.fSchemaParser.getDocument();
                element2 = document != null ? DOMUtil.getRoot(document) : null;
                return this.getSchemaDocument0(xSDKey, string2, element2);
            }
            bl2 = false;
        }
        catch (IOException iOException2) {
            iOException = iOException2;
        }
        return this.getSchemaDocument1(bl, bl2, xMLInputSource, element, iOException);
    }

    private Element getSchemaDocument(String string, SAXInputSource sAXInputSource, boolean bl, short s2, Element element) {
        XMLReader xMLReader = sAXInputSource.getXMLReader();
        InputSource inputSource = sAXInputSource.getInputSource();
        boolean bl2 = true;
        IOException iOException = null;
        Element element2 = null;
        try {
            if (inputSource != null && (inputSource.getSystemId() != null || inputSource.getByteStream() != null || inputSource.getCharacterStream() != null)) {
                XSDKey xSDKey = null;
                String string2 = null;
                if (s2 != 3 && (element2 = (Element)this.fTraversed.get(xSDKey = new XSDKey(string2 = XMLEntityManager.expandSystemId(inputSource.getSystemId(), sAXInputSource.getBaseSystemId(), false), s2, string))) != null) {
                    this.fLastSchemaWasDuplicate = true;
                    return element2;
                }
                boolean bl3 = false;
                if (xMLReader != null) {
                    try {
                        bl3 = xMLReader.getFeature("http://xml.org/sax/features/namespace-prefixes");
                    }
                    catch (SAXException sAXException) {}
                } else {
                    try {
                        xMLReader = XMLReaderFactory.createXMLReader();
                    }
                    catch (SAXException sAXException) {
                        xMLReader = new SAXParser();
                    }
                    try {
                        Object object;
                        xMLReader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
                        bl3 = true;
                        if (xMLReader instanceof SAXParser && (object = this.fSchemaParser.getProperty("http://apache.org/xml/properties/security-manager")) != null) {
                            xMLReader.setProperty("http://apache.org/xml/properties/security-manager", object);
                        }
                    }
                    catch (SAXException sAXException) {
                        // empty catch block
                    }
                }
                boolean bl4 = false;
                try {
                    bl4 = xMLReader.getFeature("http://xml.org/sax/features/string-interning");
                }
                catch (SAXException sAXException) {
                    // empty catch block
                }
                if (this.fXSContentHandler == null) {
                    this.fXSContentHandler = new SchemaContentHandler();
                }
                this.fXSContentHandler.reset(this.fSchemaParser, this.fSymbolTable, bl3, bl4);
                xMLReader.setContentHandler(this.fXSContentHandler);
                xMLReader.setErrorHandler(this.fErrorReporter.getSAXErrorHandler());
                xMLReader.parse(inputSource);
                try {
                    xMLReader.setContentHandler(null);
                    xMLReader.setErrorHandler(null);
                }
                catch (Exception exception) {
                    // empty catch block
                }
                Document document = this.fXSContentHandler.getDocument();
                element2 = document != null ? DOMUtil.getRoot(document) : null;
                return this.getSchemaDocument0(xSDKey, string2, element2);
            }
            bl2 = false;
        }
        catch (SAXParseException sAXParseException) {
            throw SAX2XNIUtil.createXMLParseException0(sAXParseException);
        }
        catch (SAXException sAXException) {
            throw SAX2XNIUtil.createXNIException0(sAXException);
        }
        catch (IOException iOException2) {
            iOException = iOException2;
        }
        return this.getSchemaDocument1(bl, bl2, sAXInputSource, element, iOException);
    }

    private Element getSchemaDocument(String string, DOMInputSource dOMInputSource, boolean bl, short s2, Element element) {
        boolean bl2 = true;
        IOException iOException = null;
        Element element2 = null;
        Element element3 = null;
        Node node = dOMInputSource.getNode();
        int n2 = -1;
        if (node != null) {
            n2 = node.getNodeType();
            if (n2 == 9) {
                element3 = DOMUtil.getRoot((Document)node);
            } else if (n2 == 1) {
                element3 = (Element)node;
            }
        }
        try {
            if (element3 != null) {
                XSDKey xSDKey = null;
                String string2 = null;
                if (s2 != 3) {
                    Node node2;
                    boolean bl3;
                    string2 = XMLEntityManager.expandSystemId(dOMInputSource.getSystemId(), dOMInputSource.getBaseSystemId(), false);
                    boolean bl4 = bl3 = n2 == 9;
                    if (!bl3 && (node2 = element3.getParentNode()) != null) {
                        boolean bl5 = bl3 = node2.getNodeType() == 9;
                    }
                    if (bl3 && (element2 = (Element)this.fTraversed.get(xSDKey = new XSDKey(string2, s2, string))) != null) {
                        this.fLastSchemaWasDuplicate = true;
                        return element2;
                    }
                }
                element2 = element3;
                return this.getSchemaDocument0(xSDKey, string2, element2);
            }
            bl2 = false;
        }
        catch (IOException iOException2) {
            iOException = iOException2;
        }
        return this.getSchemaDocument1(bl, bl2, dOMInputSource, element, iOException);
    }

    private Element getSchemaDocument(String string, StAXInputSource stAXInputSource, boolean bl, short s2, Element element) {
        IOException iOException = null;
        Element element2 = null;
        try {
            Document document;
            boolean bl2 = stAXInputSource.shouldConsumeRemainingContent();
            XMLStreamReader xMLStreamReader = stAXInputSource.getXMLStreamReader();
            XMLEventReader xMLEventReader = stAXInputSource.getXMLEventReader();
            XSDKey xSDKey = null;
            String string2 = null;
            if (s2 != 3) {
                string2 = XMLEntityManager.expandSystemId(stAXInputSource.getSystemId(), stAXInputSource.getBaseSystemId(), false);
                boolean bl3 = bl2;
                if (!bl3) {
                    bl3 = xMLStreamReader != null ? xMLStreamReader.getEventType() == 7 : xMLEventReader.peek().isStartDocument();
                }
                if (bl3 && (element2 = (Element)this.fTraversed.get(xSDKey = new XSDKey(string2, s2, string))) != null) {
                    this.fLastSchemaWasDuplicate = true;
                    return element2;
                }
            }
            if (this.fStAXSchemaParser == null) {
                this.fStAXSchemaParser = new StAXSchemaParser();
            }
            this.fStAXSchemaParser.reset(this.fSchemaParser, this.fSymbolTable);
            if (xMLStreamReader != null) {
                this.fStAXSchemaParser.parse(xMLStreamReader);
                if (bl2) {
                    while (xMLStreamReader.hasNext()) {
                        xMLStreamReader.next();
                    }
                }
            } else {
                this.fStAXSchemaParser.parse(xMLEventReader);
                if (bl2) {
                    while (xMLEventReader.hasNext()) {
                        xMLEventReader.nextEvent();
                    }
                }
            }
            element2 = (document = this.fStAXSchemaParser.getDocument()) != null ? DOMUtil.getRoot(document) : null;
            return this.getSchemaDocument0(xSDKey, string2, element2);
        }
        catch (XMLStreamException xMLStreamException) {
            StAXLocationWrapper stAXLocationWrapper = new StAXLocationWrapper();
            stAXLocationWrapper.setLocation(xMLStreamException.getLocation());
            throw new XMLParseException(stAXLocationWrapper, xMLStreamException.getMessage(), xMLStreamException);
        }
        catch (IOException iOException2) {
            iOException = iOException2;
            return this.getSchemaDocument1(bl, true, stAXInputSource, element, iOException);
        }
    }

    private Element getSchemaDocument0(XSDKey xSDKey, String string, Element element) {
        if (xSDKey != null) {
            this.fTraversed.put(xSDKey, element);
        }
        if (string != null) {
            this.fDoc2SystemId.put(element, string);
        }
        this.fLastSchemaWasDuplicate = false;
        return element;
    }

    private Element getSchemaDocument1(boolean bl, boolean bl2, XMLInputSource xMLInputSource, Element element, IOException iOException) {
        if (bl) {
            if (bl2) {
                this.reportSchemaError("schema_reference.4", new Object[]{xMLInputSource.getSystemId()}, element, iOException);
            } else {
                Object[] arrobject = new Object[1];
                arrobject[0] = xMLInputSource == null ? "" : xMLInputSource.getSystemId();
                this.reportSchemaError("schema_reference.4", arrobject, element, iOException);
            }
        } else if (bl2) {
            this.reportSchemaWarning("schema_reference.4", new Object[]{xMLInputSource.getSystemId()}, element, iOException);
        }
        this.fLastSchemaWasDuplicate = false;
        return null;
    }

    private Element getSchemaDocument(XSInputSource xSInputSource, XSDDescription xSDDescription) {
        SchemaGrammar[] arrschemaGrammar = xSInputSource.getGrammars();
        short s2 = xSDDescription.getContextType();
        if (arrschemaGrammar != null && arrschemaGrammar.length > 0) {
            Vector vector = this.expandGrammars(arrschemaGrammar);
            if (this.fNamespaceGrowth || !this.existingGrammars(vector)) {
                this.addGrammars(vector);
                if (s2 == 3) {
                    xSDDescription.setTargetNamespace(arrschemaGrammar[0].getTargetNamespace());
                }
            }
        } else {
            XSObject[] arrxSObject = xSInputSource.getComponents();
            if (arrxSObject != null && arrxSObject.length > 0) {
                Hashtable hashtable = new Hashtable();
                Vector vector = this.expandComponents(arrxSObject, hashtable);
                if (this.fNamespaceGrowth || this.canAddComponents(vector)) {
                    this.addGlobalComponents(vector, hashtable);
                    if (s2 == 3) {
                        xSDDescription.setTargetNamespace(arrxSObject[0].getNamespace());
                    }
                }
            }
        }
        return null;
    }

    private Vector expandGrammars(SchemaGrammar[] arrschemaGrammar) {
        Vector<SchemaGrammar> vector = new Vector<SchemaGrammar>();
        int n2 = 0;
        while (n2 < arrschemaGrammar.length) {
            if (!vector.contains(arrschemaGrammar[n2])) {
                vector.add(arrschemaGrammar[n2]);
            }
            ++n2;
        }
        int n3 = 0;
        while (n3 < vector.size()) {
            SchemaGrammar schemaGrammar = (SchemaGrammar)vector.elementAt(n3);
            Vector vector2 = schemaGrammar.getImportedGrammars();
            if (vector2 != null) {
                int n4 = vector2.size() - 1;
                while (n4 >= 0) {
                    SchemaGrammar schemaGrammar2 = (SchemaGrammar)vector2.elementAt(n4);
                    if (!vector.contains(schemaGrammar2)) {
                        vector.addElement(schemaGrammar2);
                    }
                    --n4;
                }
            }
            ++n3;
        }
        return vector;
    }

    private boolean existingGrammars(Vector vector) {
        int n2 = vector.size();
        XSDDescription xSDDescription = new XSDDescription();
        int n3 = 0;
        while (n3 < n2) {
            SchemaGrammar schemaGrammar = (SchemaGrammar)vector.elementAt(n3);
            xSDDescription.setNamespace(schemaGrammar.getTargetNamespace());
            SchemaGrammar schemaGrammar2 = this.findGrammar(xSDDescription, false);
            if (schemaGrammar2 != null) {
                return true;
            }
            ++n3;
        }
        return false;
    }

    private boolean canAddComponents(Vector vector) {
        int n2 = vector.size();
        XSDDescription xSDDescription = new XSDDescription();
        int n3 = 0;
        while (n3 < n2) {
            XSObject xSObject = (XSObject)vector.elementAt(n3);
            if (!this.canAddComponent(xSObject, xSDDescription)) {
                return false;
            }
            ++n3;
        }
        return true;
    }

    private boolean canAddComponent(XSObject xSObject, XSDDescription xSDDescription) {
        xSDDescription.setNamespace(xSObject.getNamespace());
        SchemaGrammar schemaGrammar = this.findGrammar(xSDDescription, false);
        if (schemaGrammar == null) {
            return true;
        }
        if (schemaGrammar.isImmutable()) {
            return false;
        }
        short s2 = xSObject.getType();
        String string = xSObject.getName();
        switch (s2) {
            case 3: {
                if (schemaGrammar.getGlobalTypeDecl(string) != xSObject) break;
                return true;
            }
            case 1: {
                if (schemaGrammar.getGlobalAttributeDecl(string) != xSObject) break;
                return true;
            }
            case 5: {
                if (schemaGrammar.getGlobalAttributeDecl(string) != xSObject) break;
                return true;
            }
            case 2: {
                if (schemaGrammar.getGlobalElementDecl(string) != xSObject) break;
                return true;
            }
            case 6: {
                if (schemaGrammar.getGlobalGroupDecl(string) != xSObject) break;
                return true;
            }
            case 11: {
                if (schemaGrammar.getGlobalNotationDecl(string) != xSObject) break;
                return true;
            }
            default: {
                return true;
            }
        }
        return false;
    }

    private void addGrammars(Vector vector) {
        int n2 = vector.size();
        XSDDescription xSDDescription = new XSDDescription();
        int n3 = 0;
        while (n3 < n2) {
            SchemaGrammar schemaGrammar = (SchemaGrammar)vector.elementAt(n3);
            xSDDescription.setNamespace(schemaGrammar.getTargetNamespace());
            SchemaGrammar schemaGrammar2 = this.findGrammar(xSDDescription, this.fNamespaceGrowth);
            if (schemaGrammar != schemaGrammar2) {
                this.addGrammarComponents(schemaGrammar, schemaGrammar2);
            }
            ++n3;
        }
    }

    private void addGrammarComponents(SchemaGrammar schemaGrammar, SchemaGrammar schemaGrammar2) {
        if (schemaGrammar2 == null) {
            this.createGrammarFrom(schemaGrammar);
            return;
        }
        SchemaGrammar schemaGrammar3 = schemaGrammar2;
        if (schemaGrammar3.isImmutable()) {
            schemaGrammar3 = this.createGrammarFrom(schemaGrammar2);
        }
        this.addNewGrammarLocations(schemaGrammar, schemaGrammar3);
        this.addNewImportedGrammars(schemaGrammar, schemaGrammar3);
        this.addNewGrammarComponents(schemaGrammar, schemaGrammar3);
    }

    private SchemaGrammar createGrammarFrom(SchemaGrammar schemaGrammar) {
        SchemaGrammar schemaGrammar2 = new SchemaGrammar(schemaGrammar);
        this.fGrammarBucket.putGrammar(schemaGrammar2);
        this.updateImportListWith(schemaGrammar2);
        this.updateImportListFor(schemaGrammar2);
        return schemaGrammar2;
    }

    private void addNewGrammarLocations(SchemaGrammar schemaGrammar, SchemaGrammar schemaGrammar2) {
        StringList stringList = schemaGrammar.getDocumentLocations();
        int n2 = stringList.size();
        StringList stringList2 = schemaGrammar2.getDocumentLocations();
        int n3 = 0;
        while (n3 < n2) {
            String string = stringList.item(n3);
            if (!stringList2.contains(string)) {
                schemaGrammar2.addDocument(null, string);
            }
            ++n3;
        }
    }

    private void addNewImportedGrammars(SchemaGrammar schemaGrammar, SchemaGrammar schemaGrammar2) {
        Vector vector = schemaGrammar.getImportedGrammars();
        if (vector != null) {
            Vector vector2 = schemaGrammar2.getImportedGrammars();
            if (vector2 == null) {
                vector2 = (Vector)vector.clone();
                schemaGrammar2.setImportedGrammars(vector2);
            } else {
                this.updateImportList(vector, vector2);
            }
        }
    }

    private void updateImportList(Vector vector, Vector vector2) {
        int n2 = vector.size();
        int n3 = 0;
        while (n3 < n2) {
            SchemaGrammar schemaGrammar = (SchemaGrammar)vector.elementAt(n3);
            if (!this.containedImportedGrammar(vector2, schemaGrammar)) {
                vector2.add(schemaGrammar);
            }
            ++n3;
        }
    }

    private void addNewGrammarComponents(SchemaGrammar schemaGrammar, SchemaGrammar schemaGrammar2) {
        schemaGrammar2.resetComponents();
        this.addGlobalElementDecls(schemaGrammar, schemaGrammar2);
        this.addGlobalAttributeDecls(schemaGrammar, schemaGrammar2);
        this.addGlobalAttributeGroupDecls(schemaGrammar, schemaGrammar2);
        this.addGlobalGroupDecls(schemaGrammar, schemaGrammar2);
        this.addGlobalTypeDecls(schemaGrammar, schemaGrammar2);
        this.addGlobalNotationDecls(schemaGrammar, schemaGrammar2);
    }

    private void addGlobalElementDecls(SchemaGrammar schemaGrammar, SchemaGrammar schemaGrammar2) {
        XSElementDecl xSElementDecl;
        XSElementDecl xSElementDecl2;
        XSNamedMap xSNamedMap = schemaGrammar.getComponents(2);
        int n2 = xSNamedMap.getLength();
        int n3 = 0;
        while (n3 < n2) {
            xSElementDecl2 = (XSElementDecl)xSNamedMap.item(n3);
            xSElementDecl = schemaGrammar2.getGlobalElementDecl(xSElementDecl2.getName());
            if (xSElementDecl == null) {
                schemaGrammar2.addGlobalElementDecl(xSElementDecl2);
            } else if (xSElementDecl != xSElementDecl2) {
                // empty if block
            }
            ++n3;
        }
        ObjectList objectList = schemaGrammar.getComponentsExt(2);
        n2 = objectList.getLength();
        int n4 = 0;
        while (n4 < n2) {
            String string = (String)objectList.item(n4);
            int n5 = string.indexOf(44);
            String string2 = string.substring(0, n5);
            String string3 = string.substring(n5 + 1, string.length());
            xSElementDecl2 = (XSElementDecl)objectList.item(n4 + 1);
            xSElementDecl = schemaGrammar2.getGlobalElementDecl(string3, string2);
            if (xSElementDecl == null) {
                schemaGrammar2.addGlobalElementDecl(xSElementDecl2, string2);
            } else if (xSElementDecl != xSElementDecl2) {
                // empty if block
            }
            n4 += 2;
        }
    }

    private void addGlobalAttributeDecls(SchemaGrammar schemaGrammar, SchemaGrammar schemaGrammar2) {
        XSAttributeDecl xSAttributeDecl;
        XSAttributeDecl xSAttributeDecl2;
        XSNamedMap xSNamedMap = schemaGrammar.getComponents(1);
        int n2 = xSNamedMap.getLength();
        int n3 = 0;
        while (n3 < n2) {
            xSAttributeDecl = (XSAttributeDecl)xSNamedMap.item(n3);
            xSAttributeDecl2 = schemaGrammar2.getGlobalAttributeDecl(xSAttributeDecl.getName());
            if (xSAttributeDecl2 == null) {
                schemaGrammar2.addGlobalAttributeDecl(xSAttributeDecl);
            } else if (xSAttributeDecl2 != xSAttributeDecl && !this.fTolerateDuplicates) {
                this.reportSharingError(xSAttributeDecl.getNamespace(), xSAttributeDecl.getName());
            }
            ++n3;
        }
        ObjectList objectList = schemaGrammar.getComponentsExt(1);
        n2 = objectList.getLength();
        int n4 = 0;
        while (n4 < n2) {
            String string = (String)objectList.item(n4);
            int n5 = string.indexOf(44);
            String string2 = string.substring(0, n5);
            String string3 = string.substring(n5 + 1, string.length());
            xSAttributeDecl = (XSAttributeDecl)objectList.item(n4 + 1);
            xSAttributeDecl2 = schemaGrammar2.getGlobalAttributeDecl(string3, string2);
            if (xSAttributeDecl2 == null) {
                schemaGrammar2.addGlobalAttributeDecl(xSAttributeDecl, string2);
            } else if (xSAttributeDecl2 != xSAttributeDecl) {
                // empty if block
            }
            n4 += 2;
        }
    }

    private void addGlobalAttributeGroupDecls(SchemaGrammar schemaGrammar, SchemaGrammar schemaGrammar2) {
        XSAttributeGroupDecl xSAttributeGroupDecl;
        XSAttributeGroupDecl xSAttributeGroupDecl2;
        XSNamedMap xSNamedMap = schemaGrammar.getComponents(5);
        int n2 = xSNamedMap.getLength();
        int n3 = 0;
        while (n3 < n2) {
            xSAttributeGroupDecl = (XSAttributeGroupDecl)xSNamedMap.item(n3);
            xSAttributeGroupDecl2 = schemaGrammar2.getGlobalAttributeGroupDecl(xSAttributeGroupDecl.getName());
            if (xSAttributeGroupDecl2 == null) {
                schemaGrammar2.addGlobalAttributeGroupDecl(xSAttributeGroupDecl);
            } else if (xSAttributeGroupDecl2 != xSAttributeGroupDecl && !this.fTolerateDuplicates) {
                this.reportSharingError(xSAttributeGroupDecl.getNamespace(), xSAttributeGroupDecl.getName());
            }
            ++n3;
        }
        ObjectList objectList = schemaGrammar.getComponentsExt(5);
        n2 = objectList.getLength();
        int n4 = 0;
        while (n4 < n2) {
            String string = (String)objectList.item(n4);
            int n5 = string.indexOf(44);
            String string2 = string.substring(0, n5);
            String string3 = string.substring(n5 + 1, string.length());
            xSAttributeGroupDecl = (XSAttributeGroupDecl)objectList.item(n4 + 1);
            xSAttributeGroupDecl2 = schemaGrammar2.getGlobalAttributeGroupDecl(string3, string2);
            if (xSAttributeGroupDecl2 == null) {
                schemaGrammar2.addGlobalAttributeGroupDecl(xSAttributeGroupDecl, string2);
            } else if (xSAttributeGroupDecl2 != xSAttributeGroupDecl) {
                // empty if block
            }
            n4 += 2;
        }
    }

    private void addGlobalNotationDecls(SchemaGrammar schemaGrammar, SchemaGrammar schemaGrammar2) {
        XSNotationDecl xSNotationDecl;
        XSNotationDecl xSNotationDecl2;
        XSNamedMap xSNamedMap = schemaGrammar.getComponents(11);
        int n2 = xSNamedMap.getLength();
        int n3 = 0;
        while (n3 < n2) {
            xSNotationDecl = (XSNotationDecl)xSNamedMap.item(n3);
            xSNotationDecl2 = schemaGrammar2.getGlobalNotationDecl(xSNotationDecl.getName());
            if (xSNotationDecl2 == null) {
                schemaGrammar2.addGlobalNotationDecl(xSNotationDecl);
            } else if (xSNotationDecl2 != xSNotationDecl && !this.fTolerateDuplicates) {
                this.reportSharingError(xSNotationDecl.getNamespace(), xSNotationDecl.getName());
            }
            ++n3;
        }
        ObjectList objectList = schemaGrammar.getComponentsExt(11);
        n2 = objectList.getLength();
        int n4 = 0;
        while (n4 < n2) {
            String string = (String)objectList.item(n4);
            int n5 = string.indexOf(44);
            String string2 = string.substring(0, n5);
            String string3 = string.substring(n5 + 1, string.length());
            xSNotationDecl = (XSNotationDecl)objectList.item(n4 + 1);
            xSNotationDecl2 = schemaGrammar2.getGlobalNotationDecl(string3, string2);
            if (xSNotationDecl2 == null) {
                schemaGrammar2.addGlobalNotationDecl(xSNotationDecl, string2);
            } else if (xSNotationDecl2 != xSNotationDecl) {
                // empty if block
            }
            n4 += 2;
        }
    }

    private void addGlobalGroupDecls(SchemaGrammar schemaGrammar, SchemaGrammar schemaGrammar2) {
        XSGroupDecl xSGroupDecl;
        XSGroupDecl xSGroupDecl2;
        XSNamedMap xSNamedMap = schemaGrammar.getComponents(6);
        int n2 = xSNamedMap.getLength();
        int n3 = 0;
        while (n3 < n2) {
            xSGroupDecl = (XSGroupDecl)xSNamedMap.item(n3);
            xSGroupDecl2 = schemaGrammar2.getGlobalGroupDecl(xSGroupDecl.getName());
            if (xSGroupDecl2 == null) {
                schemaGrammar2.addGlobalGroupDecl(xSGroupDecl);
            } else if (xSGroupDecl != xSGroupDecl2 && !this.fTolerateDuplicates) {
                this.reportSharingError(xSGroupDecl.getNamespace(), xSGroupDecl.getName());
            }
            ++n3;
        }
        ObjectList objectList = schemaGrammar.getComponentsExt(6);
        n2 = objectList.getLength();
        int n4 = 0;
        while (n4 < n2) {
            String string = (String)objectList.item(n4);
            int n5 = string.indexOf(44);
            String string2 = string.substring(0, n5);
            String string3 = string.substring(n5 + 1, string.length());
            xSGroupDecl = (XSGroupDecl)objectList.item(n4 + 1);
            xSGroupDecl2 = schemaGrammar2.getGlobalGroupDecl(string3, string2);
            if (xSGroupDecl2 == null) {
                schemaGrammar2.addGlobalGroupDecl(xSGroupDecl, string2);
            } else if (xSGroupDecl2 != xSGroupDecl) {
                // empty if block
            }
            n4 += 2;
        }
    }

    private void addGlobalTypeDecls(SchemaGrammar schemaGrammar, SchemaGrammar schemaGrammar2) {
        XSTypeDefinition xSTypeDefinition;
        XSTypeDefinition xSTypeDefinition2;
        XSNamedMap xSNamedMap = schemaGrammar.getComponents(3);
        int n2 = xSNamedMap.getLength();
        int n3 = 0;
        while (n3 < n2) {
            xSTypeDefinition = (XSTypeDefinition)xSNamedMap.item(n3);
            xSTypeDefinition2 = schemaGrammar2.getGlobalTypeDecl(xSTypeDefinition.getName());
            if (xSTypeDefinition2 == null) {
                schemaGrammar2.addGlobalTypeDecl(xSTypeDefinition);
            } else if (xSTypeDefinition2 != xSTypeDefinition && !this.fTolerateDuplicates) {
                this.reportSharingError(xSTypeDefinition.getNamespace(), xSTypeDefinition.getName());
            }
            ++n3;
        }
        ObjectList objectList = schemaGrammar.getComponentsExt(3);
        n2 = objectList.getLength();
        int n4 = 0;
        while (n4 < n2) {
            String string = (String)objectList.item(n4);
            int n5 = string.indexOf(44);
            String string2 = string.substring(0, n5);
            String string3 = string.substring(n5 + 1, string.length());
            xSTypeDefinition = (XSTypeDefinition)objectList.item(n4 + 1);
            xSTypeDefinition2 = schemaGrammar2.getGlobalTypeDecl(string3, string2);
            if (xSTypeDefinition2 == null) {
                schemaGrammar2.addGlobalTypeDecl(xSTypeDefinition, string2);
            } else if (xSTypeDefinition2 != xSTypeDefinition) {
                // empty if block
            }
            n4 += 2;
        }
    }

    private Vector expandComponents(XSObject[] arrxSObject, Hashtable hashtable) {
        Vector<XSObject> vector = new Vector<XSObject>();
        int n2 = 0;
        while (n2 < arrxSObject.length) {
            if (!vector.contains(arrxSObject[n2])) {
                vector.add(arrxSObject[n2]);
            }
            ++n2;
        }
        int n3 = 0;
        while (n3 < vector.size()) {
            XSObject xSObject = (XSObject)vector.elementAt(n3);
            this.expandRelatedComponents(xSObject, vector, hashtable);
            ++n3;
        }
        return vector;
    }

    private void expandRelatedComponents(XSObject xSObject, Vector vector, Hashtable hashtable) {
        short s2 = xSObject.getType();
        switch (s2) {
            case 3: {
                this.expandRelatedTypeComponents((XSTypeDefinition)xSObject, vector, xSObject.getNamespace(), hashtable);
                break;
            }
            case 1: {
                this.expandRelatedAttributeComponents((XSAttributeDeclaration)xSObject, vector, xSObject.getNamespace(), hashtable);
                break;
            }
            case 5: {
                this.expandRelatedAttributeGroupComponents((XSAttributeGroupDefinition)xSObject, vector, xSObject.getNamespace(), hashtable);
            }
            case 2: {
                this.expandRelatedElementComponents((XSElementDeclaration)xSObject, vector, xSObject.getNamespace(), hashtable);
                break;
            }
            case 6: {
                this.expandRelatedModelGroupDefinitionComponents((XSModelGroupDefinition)xSObject, vector, xSObject.getNamespace(), hashtable);
            }
        }
    }

    private void expandRelatedAttributeComponents(XSAttributeDeclaration xSAttributeDeclaration, Vector vector, String string, Hashtable hashtable) {
        this.addRelatedType(xSAttributeDeclaration.getTypeDefinition(), vector, string, hashtable);
    }

    private void expandRelatedElementComponents(XSElementDeclaration xSElementDeclaration, Vector vector, String string, Hashtable hashtable) {
        this.addRelatedType(xSElementDeclaration.getTypeDefinition(), vector, string, hashtable);
        XSElementDeclaration xSElementDeclaration2 = xSElementDeclaration.getSubstitutionGroupAffiliation();
        if (xSElementDeclaration2 != null) {
            this.addRelatedElement(xSElementDeclaration2, vector, string, hashtable);
        }
    }

    private void expandRelatedTypeComponents(XSTypeDefinition xSTypeDefinition, Vector vector, String string, Hashtable hashtable) {
        if (xSTypeDefinition instanceof XSComplexTypeDecl) {
            this.expandRelatedComplexTypeComponents((XSComplexTypeDecl)xSTypeDefinition, vector, string, hashtable);
        } else if (xSTypeDefinition instanceof XSSimpleTypeDecl) {
            this.expandRelatedSimpleTypeComponents((XSSimpleTypeDefinition)xSTypeDefinition, vector, string, hashtable);
        }
    }

    private void expandRelatedModelGroupDefinitionComponents(XSModelGroupDefinition xSModelGroupDefinition, Vector vector, String string, Hashtable hashtable) {
        this.expandRelatedModelGroupComponents(xSModelGroupDefinition.getModelGroup(), vector, string, hashtable);
    }

    private void expandRelatedAttributeGroupComponents(XSAttributeGroupDefinition xSAttributeGroupDefinition, Vector vector, String string, Hashtable hashtable) {
        this.expandRelatedAttributeUsesComponents(xSAttributeGroupDefinition.getAttributeUses(), vector, string, hashtable);
    }

    private void expandRelatedComplexTypeComponents(XSComplexTypeDecl xSComplexTypeDecl, Vector vector, String string, Hashtable hashtable) {
        this.addRelatedType(xSComplexTypeDecl.getBaseType(), vector, string, hashtable);
        this.expandRelatedAttributeUsesComponents(xSComplexTypeDecl.getAttributeUses(), vector, string, hashtable);
        XSParticle xSParticle = xSComplexTypeDecl.getParticle();
        if (xSParticle != null) {
            this.expandRelatedParticleComponents(xSParticle, vector, string, hashtable);
        }
    }

    private void expandRelatedSimpleTypeComponents(XSSimpleTypeDefinition xSSimpleTypeDefinition, Vector vector, String string, Hashtable hashtable) {
        XSObjectList xSObjectList;
        XSSimpleTypeDefinition xSSimpleTypeDefinition2;
        XSSimpleTypeDefinition xSSimpleTypeDefinition3;
        XSTypeDefinition xSTypeDefinition = xSSimpleTypeDefinition.getBaseType();
        if (xSTypeDefinition != null) {
            this.addRelatedType(xSTypeDefinition, vector, string, hashtable);
        }
        if ((xSSimpleTypeDefinition2 = xSSimpleTypeDefinition.getItemType()) != null) {
            this.addRelatedType(xSSimpleTypeDefinition2, vector, string, hashtable);
        }
        if ((xSSimpleTypeDefinition3 = xSSimpleTypeDefinition.getPrimitiveType()) != null) {
            this.addRelatedType(xSSimpleTypeDefinition3, vector, string, hashtable);
        }
        if ((xSObjectList = xSSimpleTypeDefinition.getMemberTypes()).size() > 0) {
            int n2 = 0;
            while (n2 < xSObjectList.size()) {
                this.addRelatedType((XSTypeDefinition)xSObjectList.item(n2), vector, string, hashtable);
                ++n2;
            }
        }
    }

    private void expandRelatedAttributeUsesComponents(XSObjectList xSObjectList, Vector vector, String string, Hashtable hashtable) {
        int n2 = xSObjectList == null ? 0 : xSObjectList.size();
        int n3 = 0;
        while (n3 < n2) {
            this.expandRelatedAttributeUseComponents((XSAttributeUse)xSObjectList.item(n3), vector, string, hashtable);
            ++n3;
        }
    }

    private void expandRelatedAttributeUseComponents(XSAttributeUse xSAttributeUse, Vector vector, String string, Hashtable hashtable) {
        this.addRelatedAttribute(xSAttributeUse.getAttrDeclaration(), vector, string, hashtable);
    }

    private void expandRelatedParticleComponents(XSParticle xSParticle, Vector vector, String string, Hashtable hashtable) {
        XSTerm xSTerm = xSParticle.getTerm();
        switch (xSTerm.getType()) {
            case 2: {
                this.addRelatedElement((XSElementDeclaration)xSTerm, vector, string, hashtable);
                break;
            }
            case 7: {
                this.expandRelatedModelGroupComponents((XSModelGroup)xSTerm, vector, string, hashtable);
                break;
            }
        }
    }

    private void expandRelatedModelGroupComponents(XSModelGroup xSModelGroup, Vector vector, String string, Hashtable hashtable) {
        XSObjectList xSObjectList = xSModelGroup.getParticles();
        int n2 = xSObjectList == null ? 0 : xSObjectList.getLength();
        int n3 = 0;
        while (n3 < n2) {
            this.expandRelatedParticleComponents((XSParticle)xSObjectList.item(n3), vector, string, hashtable);
            ++n3;
        }
    }

    private void addRelatedType(XSTypeDefinition xSTypeDefinition, Vector vector, String string, Hashtable hashtable) {
        if (!xSTypeDefinition.getAnonymous()) {
            if (!xSTypeDefinition.getNamespace().equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && !vector.contains(xSTypeDefinition)) {
                Vector vector2 = this.findDependentNamespaces(string, hashtable);
                this.addNamespaceDependency(string, xSTypeDefinition.getNamespace(), vector2);
                vector.add(xSTypeDefinition);
            }
        } else {
            this.expandRelatedTypeComponents(xSTypeDefinition, vector, string, hashtable);
        }
    }

    private void addRelatedElement(XSElementDeclaration xSElementDeclaration, Vector vector, String string, Hashtable hashtable) {
        if (xSElementDeclaration.getScope() == 1) {
            if (!vector.contains(xSElementDeclaration)) {
                Vector vector2 = this.findDependentNamespaces(string, hashtable);
                this.addNamespaceDependency(string, xSElementDeclaration.getNamespace(), vector2);
                vector.add(xSElementDeclaration);
            }
        } else {
            this.expandRelatedElementComponents(xSElementDeclaration, vector, string, hashtable);
        }
    }

    private void addRelatedAttribute(XSAttributeDeclaration xSAttributeDeclaration, Vector vector, String string, Hashtable hashtable) {
        if (xSAttributeDeclaration.getScope() == 1) {
            if (!vector.contains(xSAttributeDeclaration)) {
                Vector vector2 = this.findDependentNamespaces(string, hashtable);
                this.addNamespaceDependency(string, xSAttributeDeclaration.getNamespace(), vector2);
                vector.add(xSAttributeDeclaration);
            }
        } else {
            this.expandRelatedAttributeComponents(xSAttributeDeclaration, vector, string, hashtable);
        }
    }

    private void addGlobalComponents(Vector vector, Hashtable hashtable) {
        XSDDescription xSDDescription = new XSDDescription();
        int n2 = vector.size();
        int n3 = 0;
        while (n3 < n2) {
            this.addGlobalComponent((XSObject)vector.elementAt(n3), xSDDescription);
            ++n3;
        }
        this.updateImportDependencies(hashtable);
    }

    private void addGlobalComponent(XSObject xSObject, XSDDescription xSDDescription) {
        String string = xSObject.getNamespace();
        xSDDescription.setNamespace(string);
        SchemaGrammar schemaGrammar = this.getSchemaGrammar(xSDDescription);
        short s2 = xSObject.getType();
        String string2 = xSObject.getName();
        switch (s2) {
            case 3: {
                if (((XSTypeDefinition)xSObject).getAnonymous()) break;
                if (schemaGrammar.getGlobalTypeDecl(string2) == null) {
                    schemaGrammar.addGlobalTypeDecl((XSTypeDefinition)xSObject);
                }
                if (schemaGrammar.getGlobalTypeDecl(string2, "") != null) break;
                schemaGrammar.addGlobalTypeDecl((XSTypeDefinition)xSObject, "");
                break;
            }
            case 1: {
                if (((XSAttributeDecl)xSObject).getScope() != 1) break;
                if (schemaGrammar.getGlobalAttributeDecl(string2) == null) {
                    schemaGrammar.addGlobalAttributeDecl((XSAttributeDecl)xSObject);
                }
                if (schemaGrammar.getGlobalAttributeDecl(string2, "") != null) break;
                schemaGrammar.addGlobalAttributeDecl((XSAttributeDecl)xSObject, "");
                break;
            }
            case 5: {
                if (schemaGrammar.getGlobalAttributeDecl(string2) == null) {
                    schemaGrammar.addGlobalAttributeGroupDecl((XSAttributeGroupDecl)xSObject);
                }
                if (schemaGrammar.getGlobalAttributeDecl(string2, "") != null) break;
                schemaGrammar.addGlobalAttributeGroupDecl((XSAttributeGroupDecl)xSObject, "");
                break;
            }
            case 2: {
                if (((XSElementDecl)xSObject).getScope() != 1) break;
                schemaGrammar.addGlobalElementDeclAll((XSElementDecl)xSObject);
                if (schemaGrammar.getGlobalElementDecl(string2) == null) {
                    schemaGrammar.addGlobalElementDecl((XSElementDecl)xSObject);
                }
                if (schemaGrammar.getGlobalElementDecl(string2, "") != null) break;
                schemaGrammar.addGlobalElementDecl((XSElementDecl)xSObject, "");
                break;
            }
            case 6: {
                if (schemaGrammar.getGlobalGroupDecl(string2) == null) {
                    schemaGrammar.addGlobalGroupDecl((XSGroupDecl)xSObject);
                }
                if (schemaGrammar.getGlobalGroupDecl(string2, "") != null) break;
                schemaGrammar.addGlobalGroupDecl((XSGroupDecl)xSObject, "");
                break;
            }
            case 11: {
                if (schemaGrammar.getGlobalNotationDecl(string2) == null) {
                    schemaGrammar.addGlobalNotationDecl((XSNotationDecl)xSObject);
                }
                if (schemaGrammar.getGlobalNotationDecl(string2, "") != null) break;
                schemaGrammar.addGlobalNotationDecl((XSNotationDecl)xSObject, "");
                break;
            }
        }
    }

    private void updateImportDependencies(Hashtable hashtable) {
        Enumeration enumeration = hashtable.keys();
        while (enumeration.hasMoreElements()) {
            String string = (String)enumeration.nextElement();
            Vector vector = (Vector)hashtable.get(this.null2EmptyString(string));
            if (vector.size() <= 0) continue;
            this.expandImportList(string, vector);
        }
    }

    private void expandImportList(String string, Vector vector) {
        SchemaGrammar schemaGrammar = this.fGrammarBucket.getGrammar(string);
        if (schemaGrammar != null) {
            Vector vector2 = schemaGrammar.getImportedGrammars();
            if (vector2 == null) {
                vector2 = new Vector();
                this.addImportList(schemaGrammar, vector2, vector);
                schemaGrammar.setImportedGrammars(vector2);
            } else {
                this.updateImportList(schemaGrammar, vector2, vector);
            }
        }
    }

    private void addImportList(SchemaGrammar schemaGrammar, Vector vector, Vector vector2) {
        int n2 = vector2.size();
        int n3 = 0;
        while (n3 < n2) {
            SchemaGrammar schemaGrammar2 = this.fGrammarBucket.getGrammar((String)vector2.elementAt(n3));
            if (schemaGrammar2 != null) {
                vector.add(schemaGrammar2);
            }
            ++n3;
        }
    }

    private void updateImportList(SchemaGrammar schemaGrammar, Vector vector, Vector vector2) {
        int n2 = vector2.size();
        int n3 = 0;
        while (n3 < n2) {
            SchemaGrammar schemaGrammar2 = this.fGrammarBucket.getGrammar((String)vector2.elementAt(n3));
            if (schemaGrammar2 != null && !this.containedImportedGrammar(vector, schemaGrammar2)) {
                vector.add(schemaGrammar2);
            }
            ++n3;
        }
    }

    private boolean containedImportedGrammar(Vector vector, SchemaGrammar schemaGrammar) {
        int n2 = vector.size();
        int n3 = 0;
        while (n3 < n2) {
            SchemaGrammar schemaGrammar2 = (SchemaGrammar)vector.elementAt(n3);
            if (this.null2EmptyString(schemaGrammar2.getTargetNamespace()).equals(this.null2EmptyString(schemaGrammar.getTargetNamespace()))) {
                return true;
            }
            ++n3;
        }
        return false;
    }

    private SchemaGrammar getSchemaGrammar(XSDDescription xSDDescription) {
        SchemaGrammar schemaGrammar = this.findGrammar(xSDDescription, this.fNamespaceGrowth);
        if (schemaGrammar == null) {
            schemaGrammar = new SchemaGrammar(xSDDescription.getNamespace(), xSDDescription.makeClone(), this.fSymbolTable);
            this.fGrammarBucket.putGrammar(schemaGrammar);
        } else if (schemaGrammar.isImmutable()) {
            schemaGrammar = this.createGrammarFrom(schemaGrammar);
        }
        return schemaGrammar;
    }

    private Vector findDependentNamespaces(String string, Hashtable hashtable) {
        String string2 = this.null2EmptyString(string);
        Vector vector = (Vector)hashtable.get(string2);
        if (vector == null) {
            vector = new Vector();
            hashtable.put(string2, vector);
        }
        return vector;
    }

    private void addNamespaceDependency(String string, String string2, Vector vector) {
        String string3;
        String string4 = this.null2EmptyString(string);
        if (!string4.equals(string3 = this.null2EmptyString(string2)) && !vector.contains(string3)) {
            vector.add(string3);
        }
    }

    private void reportSharingError(String string, String string2) {
        String string3 = string == null ? "," + string2 : string + "," + string2;
        this.reportSchemaError("sch-props-correct.2", new Object[]{string3}, null);
    }

    private void createTraversers() {
        this.fAttributeChecker = new XSAttributeChecker(this);
        this.fAttributeGroupTraverser = new XSDAttributeGroupTraverser(this, this.fAttributeChecker);
        this.fAttributeTraverser = new XSDAttributeTraverser(this, this.fAttributeChecker);
        this.fComplexTypeTraverser = new XSDComplexTypeTraverser(this, this.fAttributeChecker);
        this.fElementTraverser = new XSDElementTraverser(this, this.fAttributeChecker);
        this.fGroupTraverser = new XSDGroupTraverser(this, this.fAttributeChecker);
        this.fKeyrefTraverser = new XSDKeyrefTraverser(this, this.fAttributeChecker);
        this.fNotationTraverser = new XSDNotationTraverser(this, this.fAttributeChecker);
        this.fSimpleTypeTraverser = new XSDSimpleTypeTraverser(this, this.fAttributeChecker);
        this.fUniqueOrKeyTraverser = new XSDUniqueOrKeyTraverser(this, this.fAttributeChecker);
        this.fWildCardTraverser = new XSDWildcardTraverser(this, this.fAttributeChecker);
    }

    void prepareForParse() {
        this.fTraversed.clear();
        this.fDoc2SystemId.clear();
        this.fHiddenNodes.clear();
        this.fLastSchemaWasDuplicate = false;
    }

    void prepareForTraverse() {
        this.fUnparsedAttributeRegistry.clear();
        this.fUnparsedAttributeGroupRegistry.clear();
        this.fUnparsedElementRegistry.clear();
        this.fUnparsedGroupRegistry.clear();
        this.fUnparsedIdentityConstraintRegistry.clear();
        this.fUnparsedNotationRegistry.clear();
        this.fUnparsedTypeRegistry.clear();
        this.fUnparsedAttributeRegistrySub.clear();
        this.fUnparsedAttributeGroupRegistrySub.clear();
        this.fUnparsedElementRegistrySub.clear();
        this.fUnparsedGroupRegistrySub.clear();
        this.fUnparsedIdentityConstraintRegistrySub.clear();
        this.fUnparsedNotationRegistrySub.clear();
        this.fUnparsedTypeRegistrySub.clear();
        int n2 = 1;
        while (n2 <= 7) {
            this.fUnparsedRegistriesExt[n2].clear();
            ++n2;
        }
        this.fXSDocumentInfoRegistry.clear();
        this.fDependencyMap.clear();
        this.fDoc2XSDocumentMap.clear();
        this.fRedefine2XSDMap.clear();
        this.fRedefine2NSSupport.clear();
        this.fAllTNSs.removeAllElements();
        this.fImportMap.clear();
        this.fRoot = null;
        int n3 = 0;
        while (n3 < this.fLocalElemStackPos) {
            this.fParticle[n3] = null;
            this.fLocalElementDecl[n3] = null;
            this.fLocalElementDecl_schema[n3] = null;
            this.fLocalElemNamespaceContext[n3] = null;
            ++n3;
        }
        this.fLocalElemStackPos = 0;
        int n4 = 0;
        while (n4 < this.fKeyrefStackPos) {
            this.fKeyrefs[n4] = null;
            this.fKeyrefElems[n4] = null;
            this.fKeyrefNamespaceContext[n4] = null;
            this.fKeyrefsMapXSDocumentInfo[n4] = null;
            ++n4;
        }
        this.fKeyrefStackPos = 0;
        if (this.fAttributeChecker == null) {
            this.createTraversers();
        }
        Locale locale = this.fErrorReporter.getLocale();
        this.fAttributeChecker.reset(this.fSymbolTable);
        this.fAttributeGroupTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
        this.fAttributeTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
        this.fComplexTypeTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
        this.fElementTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
        this.fGroupTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
        this.fKeyrefTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
        this.fNotationTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
        this.fSimpleTypeTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
        this.fUniqueOrKeyTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
        this.fWildCardTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
        this.fRedefinedRestrictedAttributeGroupRegistry.clear();
        this.fRedefinedRestrictedGroupRegistry.clear();
        this.fGlobalAttrDecls.clear();
        this.fGlobalAttrGrpDecls.clear();
        this.fGlobalElemDecls.clear();
        this.fGlobalGroupDecls.clear();
        this.fGlobalNotationDecls.clear();
        this.fGlobalIDConstraintDecls.clear();
        this.fGlobalTypeDecls.clear();
    }

    public void setDeclPool(XSDeclarationPool xSDeclarationPool) {
        this.fDeclPool = xSDeclarationPool;
    }

    public void setDVFactory(SchemaDVFactory schemaDVFactory) {
        this.fDVFactory = schemaDVFactory;
    }

    public void reset(XMLComponentManager xMLComponentManager) {
        Object object;
        this.fSymbolTable = (SymbolTable)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fEntityResolver = (XMLEntityResolver)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-manager");
        XMLEntityResolver xMLEntityResolver = (XMLEntityResolver)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
        if (xMLEntityResolver != null) {
            this.fSchemaParser.setEntityResolver(xMLEntityResolver);
        }
        this.fErrorReporter = (XMLErrorReporter)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        try {
            Locale locale;
            object = this.fErrorReporter.getErrorHandler();
            if (object != this.fSchemaParser.getProperty("http://apache.org/xml/properties/internal/error-handler")) {
                this.fSchemaParser.setProperty("http://apache.org/xml/properties/internal/error-handler", object != null ? object : new Object());
                if (this.fAnnotationValidator != null) {
                    this.fAnnotationValidator.setProperty("http://apache.org/xml/properties/internal/error-handler", object != null ? object : new Object());
                }
            }
            if ((locale = this.fErrorReporter.getLocale()) != this.fSchemaParser.getProperty("http://apache.org/xml/properties/locale")) {
                this.fSchemaParser.setProperty("http://apache.org/xml/properties/locale", locale);
                if (this.fAnnotationValidator != null) {
                    this.fAnnotationValidator.setProperty("http://apache.org/xml/properties/locale", locale);
                }
            }
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            // empty catch block
        }
        try {
            this.fValidateAnnotations = xMLComponentManager.getFeature("http://apache.org/xml/features/validate-annotations");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fValidateAnnotations = false;
        }
        try {
            this.fHonourAllSchemaLocations = xMLComponentManager.getFeature("http://apache.org/xml/features/honour-all-schemaLocations");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fHonourAllSchemaLocations = false;
        }
        try {
            this.fNamespaceGrowth = xMLComponentManager.getFeature("http://apache.org/xml/features/namespace-growth");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fNamespaceGrowth = false;
        }
        try {
            this.fTolerateDuplicates = xMLComponentManager.getFeature("http://apache.org/xml/features/internal/tolerate-duplicates");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fTolerateDuplicates = false;
        }
        try {
            this.fSchemaParser.setFeature("http://apache.org/xml/features/continue-after-fatal-error", this.fErrorReporter.getFeature("http://apache.org/xml/features/continue-after-fatal-error"));
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            // empty catch block
        }
        try {
            this.fSchemaParser.setFeature("http://apache.org/xml/features/allow-java-encodings", xMLComponentManager.getFeature("http://apache.org/xml/features/allow-java-encodings"));
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            // empty catch block
        }
        try {
            this.fSchemaParser.setFeature("http://apache.org/xml/features/standard-uri-conformant", xMLComponentManager.getFeature("http://apache.org/xml/features/standard-uri-conformant"));
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            // empty catch block
        }
        try {
            this.fGrammarPool = (XMLGrammarPool)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/grammar-pool");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fGrammarPool = null;
        }
        try {
            this.fSchemaParser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", xMLComponentManager.getFeature("http://apache.org/xml/features/disallow-doctype-decl"));
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            // empty catch block
        }
        try {
            object = xMLComponentManager.getProperty("http://apache.org/xml/properties/security-manager");
            if (object != null) {
                this.fSchemaParser.setProperty("http://apache.org/xml/properties/security-manager", object);
            }
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            // empty catch block
        }
    }

    void traverseLocalElements() {
        this.fElementTraverser.fDeferTraversingLocalElements = false;
        int n2 = 0;
        while (n2 < this.fLocalElemStackPos) {
            Element element = this.fLocalElementDecl[n2];
            XSDocumentInfo xSDocumentInfo = this.fLocalElementDecl_schema[n2];
            SchemaGrammar schemaGrammar = this.fGrammarBucket.getGrammar(xSDocumentInfo.fTargetNamespace);
            this.fElementTraverser.traverseLocal(this.fParticle[n2], element, xSDocumentInfo, schemaGrammar, this.fAllContext[n2], this.fParent[n2], this.fLocalElemNamespaceContext[n2]);
            if (this.fParticle[n2].fType == 0) {
                XSModelGroupImpl xSModelGroupImpl = null;
                if (this.fParent[n2] instanceof XSComplexTypeDecl) {
                    XSParticle xSParticle = ((XSComplexTypeDecl)this.fParent[n2]).getParticle();
                    if (xSParticle != null) {
                        xSModelGroupImpl = (XSModelGroupImpl)xSParticle.getTerm();
                    }
                } else {
                    xSModelGroupImpl = ((XSGroupDecl)this.fParent[n2]).fModelGroup;
                }
                if (xSModelGroupImpl != null) {
                    this.removeParticle(xSModelGroupImpl, this.fParticle[n2]);
                }
            }
            ++n2;
        }
    }

    private boolean removeParticle(XSModelGroupImpl xSModelGroupImpl, XSParticleDecl xSParticleDecl) {
        int n2 = 0;
        while (n2 < xSModelGroupImpl.fParticleCount) {
            XSParticleDecl xSParticleDecl2 = xSModelGroupImpl.fParticles[n2];
            if (xSParticleDecl2 == xSParticleDecl) {
                int n3 = n2;
                while (n3 < xSModelGroupImpl.fParticleCount - 1) {
                    xSModelGroupImpl.fParticles[n3] = xSModelGroupImpl.fParticles[n3 + 1];
                    ++n3;
                }
                --xSModelGroupImpl.fParticleCount;
                return true;
            }
            if (xSParticleDecl2.fType == 3 && this.removeParticle((XSModelGroupImpl)xSParticleDecl2.fValue, xSParticleDecl)) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    void fillInLocalElemInfo(Element element, XSDocumentInfo xSDocumentInfo, int n2, XSObject xSObject, XSParticleDecl xSParticleDecl) {
        if (this.fParticle.length == this.fLocalElemStackPos) {
            XSParticleDecl[] arrxSParticleDecl = new XSParticleDecl[this.fLocalElemStackPos + 10];
            System.arraycopy(this.fParticle, 0, arrxSParticleDecl, 0, this.fLocalElemStackPos);
            this.fParticle = arrxSParticleDecl;
            Element[] arrelement = new Element[this.fLocalElemStackPos + 10];
            System.arraycopy(this.fLocalElementDecl, 0, arrelement, 0, this.fLocalElemStackPos);
            this.fLocalElementDecl = arrelement;
            XSDocumentInfo[] arrxSDocumentInfo = new XSDocumentInfo[this.fLocalElemStackPos + 10];
            System.arraycopy(this.fLocalElementDecl_schema, 0, arrxSDocumentInfo, 0, this.fLocalElemStackPos);
            this.fLocalElementDecl_schema = arrxSDocumentInfo;
            int[] arrn = new int[this.fLocalElemStackPos + 10];
            System.arraycopy(this.fAllContext, 0, arrn, 0, this.fLocalElemStackPos);
            this.fAllContext = arrn;
            XSObject[] arrxSObject = new XSObject[this.fLocalElemStackPos + 10];
            System.arraycopy(this.fParent, 0, arrxSObject, 0, this.fLocalElemStackPos);
            this.fParent = arrxSObject;
            String[][] arrarrstring = new String[this.fLocalElemStackPos + 10][];
            System.arraycopy(this.fLocalElemNamespaceContext, 0, arrarrstring, 0, this.fLocalElemStackPos);
            this.fLocalElemNamespaceContext = arrarrstring;
        }
        this.fParticle[this.fLocalElemStackPos] = xSParticleDecl;
        this.fLocalElementDecl[this.fLocalElemStackPos] = element;
        this.fLocalElementDecl_schema[this.fLocalElemStackPos] = xSDocumentInfo;
        this.fAllContext[this.fLocalElemStackPos] = n2;
        this.fParent[this.fLocalElemStackPos] = xSObject;
        this.fLocalElemNamespaceContext[this.fLocalElemStackPos++] = xSDocumentInfo.fNamespaceSupport.getEffectiveLocalContext();
    }

    void checkForDuplicateNames(String string, int n2, Hashtable hashtable, Hashtable hashtable2, Element element, XSDocumentInfo xSDocumentInfo) {
        Object var7_7 = null;
        Object v2 = hashtable.get(string);
        var7_7 = v2;
        if (v2 == null) {
            if (this.fNamespaceGrowth && !this.fTolerateDuplicates) {
                this.checkForDuplicateNames(string, n2, element);
            }
            hashtable.put(string, element);
            hashtable2.put(string, xSDocumentInfo);
        } else {
            Element element2 = var7_7;
            XSDocumentInfo xSDocumentInfo2 = (XSDocumentInfo)hashtable2.get(string);
            if (element2 == element) {
                return;
            }
            Element element3 = null;
            XSDocumentInfo xSDocumentInfo3 = null;
            boolean bl = true;
            element3 = DOMUtil.getParent(element2);
            if (DOMUtil.getLocalName(element3).equals(SchemaSymbols.ELT_REDEFINE)) {
                xSDocumentInfo3 = (XSDocumentInfo)this.fRedefine2XSDMap.get(element3);
            } else if (DOMUtil.getLocalName(DOMUtil.getParent(element)).equals(SchemaSymbols.ELT_REDEFINE)) {
                xSDocumentInfo3 = xSDocumentInfo2;
                bl = false;
            }
            if (xSDocumentInfo3 != null) {
                if (xSDocumentInfo2 == xSDocumentInfo) {
                    this.reportSchemaError("sch-props-correct.2", new Object[]{string}, element);
                    return;
                }
                String string2 = string.substring(string.lastIndexOf(44) + 1) + "_fn3dktizrknc9pi";
                if (xSDocumentInfo3 == xSDocumentInfo) {
                    element.setAttribute(SchemaSymbols.ATT_NAME, string2);
                    if (xSDocumentInfo.fTargetNamespace == null) {
                        hashtable.put("," + string2, element);
                        hashtable2.put("," + string2, xSDocumentInfo);
                    } else {
                        hashtable.put(xSDocumentInfo.fTargetNamespace + "," + string2, element);
                        hashtable2.put(xSDocumentInfo.fTargetNamespace + "," + string2, xSDocumentInfo);
                    }
                    if (xSDocumentInfo.fTargetNamespace == null) {
                        this.checkForDuplicateNames("," + string2, n2, hashtable, hashtable2, element, xSDocumentInfo);
                    } else {
                        this.checkForDuplicateNames(xSDocumentInfo.fTargetNamespace + "," + string2, n2, hashtable, hashtable2, element, xSDocumentInfo);
                    }
                } else if (bl) {
                    if (xSDocumentInfo.fTargetNamespace == null) {
                        this.checkForDuplicateNames("," + string2, n2, hashtable, hashtable2, element, xSDocumentInfo);
                    } else {
                        this.checkForDuplicateNames(xSDocumentInfo.fTargetNamespace + "," + string2, n2, hashtable, hashtable2, element, xSDocumentInfo);
                    }
                } else {
                    this.reportSchemaError("sch-props-correct.2", new Object[]{string}, element);
                }
            } else if (!this.fTolerateDuplicates || this.fUnparsedRegistriesExt[n2].get(string) == xSDocumentInfo) {
                this.reportSchemaError("sch-props-correct.2", new Object[]{string}, element);
            }
        }
        if (this.fTolerateDuplicates) {
            this.fUnparsedRegistriesExt[n2].put(string, xSDocumentInfo);
        }
    }

    void checkForDuplicateNames(String string, int n2, Element element) {
        Object object;
        int n3 = string.indexOf(44);
        String string2 = string.substring(0, n3);
        SchemaGrammar schemaGrammar = this.fGrammarBucket.getGrammar(this.emptyString2Null(string2));
        if (schemaGrammar != null && (object = this.getGlobalDeclFromGrammar(schemaGrammar, n2, string.substring(n3 + 1))) != null) {
            this.reportSchemaError("sch-props-correct.2", new Object[]{string}, element);
        }
    }

    private void renameRedefiningComponents(XSDocumentInfo xSDocumentInfo, Element element, String string, String string2, String string3) {
        if (string.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
            Element element2 = DOMUtil.getFirstChildElement(element);
            if (element2 == null) {
                this.reportSchemaError("src-redefine.5.a.a", null, element);
            } else {
                String string4 = DOMUtil.getLocalName(element2);
                if (string4.equals(SchemaSymbols.ELT_ANNOTATION)) {
                    element2 = DOMUtil.getNextSiblingElement(element2);
                }
                if (element2 == null) {
                    this.reportSchemaError("src-redefine.5.a.a", null, element);
                } else {
                    string4 = DOMUtil.getLocalName(element2);
                    if (!string4.equals(SchemaSymbols.ELT_RESTRICTION)) {
                        this.reportSchemaError("src-redefine.5.a.b", new Object[]{string4}, element);
                    } else {
                        Object[] arrobject = this.fAttributeChecker.checkAttributes(element2, false, xSDocumentInfo);
                        QName qName = (QName)arrobject[XSAttributeChecker.ATTIDX_BASE];
                        if (qName == null || qName.uri != xSDocumentInfo.fTargetNamespace || !qName.localpart.equals(string2)) {
                            Object[] arrobject2 = new Object[2];
                            arrobject2[0] = string4;
                            arrobject2[1] = (xSDocumentInfo.fTargetNamespace == null ? "" : xSDocumentInfo.fTargetNamespace) + "," + string2;
                            this.reportSchemaError("src-redefine.5.a.c", arrobject2, element);
                        } else if (qName.prefix != null && qName.prefix.length() > 0) {
                            element2.setAttribute(SchemaSymbols.ATT_BASE, qName.prefix + ":" + string3);
                        } else {
                            element2.setAttribute(SchemaSymbols.ATT_BASE, string3);
                        }
                        this.fAttributeChecker.returnAttrArray(arrobject, xSDocumentInfo);
                    }
                }
            }
        } else if (string.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
            Element element3 = DOMUtil.getFirstChildElement(element);
            if (element3 == null) {
                this.reportSchemaError("src-redefine.5.b.a", null, element);
            } else {
                if (DOMUtil.getLocalName(element3).equals(SchemaSymbols.ELT_ANNOTATION)) {
                    element3 = DOMUtil.getNextSiblingElement(element3);
                }
                if (element3 == null) {
                    this.reportSchemaError("src-redefine.5.b.a", null, element);
                } else {
                    Element element4 = DOMUtil.getFirstChildElement(element3);
                    if (element4 == null) {
                        this.reportSchemaError("src-redefine.5.b.b", null, element3);
                    } else {
                        String string5 = DOMUtil.getLocalName(element4);
                        if (string5.equals(SchemaSymbols.ELT_ANNOTATION)) {
                            element4 = DOMUtil.getNextSiblingElement(element4);
                        }
                        if (element4 == null) {
                            this.reportSchemaError("src-redefine.5.b.b", null, element3);
                        } else {
                            string5 = DOMUtil.getLocalName(element4);
                            if (!string5.equals(SchemaSymbols.ELT_RESTRICTION) && !string5.equals(SchemaSymbols.ELT_EXTENSION)) {
                                this.reportSchemaError("src-redefine.5.b.c", new Object[]{string5}, element4);
                            } else {
                                Object[] arrobject = this.fAttributeChecker.checkAttributes(element4, false, xSDocumentInfo);
                                QName qName = (QName)arrobject[XSAttributeChecker.ATTIDX_BASE];
                                if (qName == null || qName.uri != xSDocumentInfo.fTargetNamespace || !qName.localpart.equals(string2)) {
                                    Object[] arrobject3 = new Object[2];
                                    arrobject3[0] = string5;
                                    arrobject3[1] = (xSDocumentInfo.fTargetNamespace == null ? "" : xSDocumentInfo.fTargetNamespace) + "," + string2;
                                    this.reportSchemaError("src-redefine.5.b.d", arrobject3, element4);
                                } else if (qName.prefix != null && qName.prefix.length() > 0) {
                                    element4.setAttribute(SchemaSymbols.ATT_BASE, qName.prefix + ":" + string3);
                                } else {
                                    element4.setAttribute(SchemaSymbols.ATT_BASE, string3);
                                }
                            }
                        }
                    }
                }
            }
        } else if (string.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
            String string6 = xSDocumentInfo.fTargetNamespace == null ? "," + string2 : xSDocumentInfo.fTargetNamespace + "," + string2;
            int n2 = this.changeRedefineGroup(string6, string, string3, element, xSDocumentInfo);
            if (n2 > 1) {
                this.reportSchemaError("src-redefine.7.1", new Object[]{new Integer(n2)}, element);
            } else if (n2 != 1) {
                if (xSDocumentInfo.fTargetNamespace == null) {
                    this.fRedefinedRestrictedAttributeGroupRegistry.put(string6, "," + string3);
                } else {
                    this.fRedefinedRestrictedAttributeGroupRegistry.put(string6, xSDocumentInfo.fTargetNamespace + "," + string3);
                }
            }
        } else if (string.equals(SchemaSymbols.ELT_GROUP)) {
            String string7 = xSDocumentInfo.fTargetNamespace == null ? "," + string2 : xSDocumentInfo.fTargetNamespace + "," + string2;
            int n3 = this.changeRedefineGroup(string7, string, string3, element, xSDocumentInfo);
            if (n3 > 1) {
                this.reportSchemaError("src-redefine.6.1.1", new Object[]{new Integer(n3)}, element);
            } else if (n3 != 1) {
                if (xSDocumentInfo.fTargetNamespace == null) {
                    this.fRedefinedRestrictedGroupRegistry.put(string7, "," + string3);
                } else {
                    this.fRedefinedRestrictedGroupRegistry.put(string7, xSDocumentInfo.fTargetNamespace + "," + string3);
                }
            }
        } else {
            this.reportSchemaError("Internal-Error", new Object[]{"could not handle this particular <redefine>; please submit your schemas and instance document in a bug report!"}, element);
        }
    }

    private String findQName(String string, XSDocumentInfo xSDocumentInfo) {
        String string2;
        SchemaNamespaceSupport schemaNamespaceSupport = xSDocumentInfo.fNamespaceSupport;
        int n2 = string.indexOf(58);
        String string3 = XMLSymbols.EMPTY_STRING;
        if (n2 > 0) {
            string3 = string.substring(0, n2);
        }
        String string4 = schemaNamespaceSupport.getURI(this.fSymbolTable.addSymbol(string3));
        String string5 = string2 = n2 == 0 ? string : string.substring(n2 + 1);
        if (string3 == XMLSymbols.EMPTY_STRING && string4 == null && xSDocumentInfo.fIsChameleonSchema) {
            string4 = xSDocumentInfo.fTargetNamespace;
        }
        if (string4 == null) {
            return "," + string2;
        }
        return string4 + "," + string2;
    }

    private int changeRedefineGroup(String string, String string2, String string3, Element element, XSDocumentInfo xSDocumentInfo) {
        int n2 = 0;
        Element element2 = DOMUtil.getFirstChildElement(element);
        while (element2 != null) {
            String string4 = DOMUtil.getLocalName(element2);
            if (!string4.equals(string2)) {
                n2 += this.changeRedefineGroup(string, string2, string3, element2, xSDocumentInfo);
            } else {
                String string5;
                String string6 = element2.getAttribute(SchemaSymbols.ATT_REF);
                if (string6.length() != 0 && string.equals(string5 = this.findQName(string6, xSDocumentInfo))) {
                    String string7 = XMLSymbols.EMPTY_STRING;
                    int n3 = string6.indexOf(":");
                    if (n3 > 0) {
                        string7 = string6.substring(0, n3);
                        element2.setAttribute(SchemaSymbols.ATT_REF, string7 + ":" + string3);
                    } else {
                        element2.setAttribute(SchemaSymbols.ATT_REF, string3);
                    }
                    ++n2;
                    if (string2.equals(SchemaSymbols.ELT_GROUP)) {
                        String string8 = element2.getAttribute(SchemaSymbols.ATT_MINOCCURS);
                        String string9 = element2.getAttribute(SchemaSymbols.ATT_MAXOCCURS);
                        if (string9.length() != 0 && !string9.equals("1") || string8.length() != 0 && !string8.equals("1")) {
                            this.reportSchemaError("src-redefine.6.1.2", new Object[]{string6}, element2);
                        }
                    }
                }
            }
            element2 = DOMUtil.getNextSiblingElement(element2);
        }
        return n2;
    }

    private XSDocumentInfo findXSDocumentForDecl(XSDocumentInfo xSDocumentInfo, Element element, XSDocumentInfo xSDocumentInfo2) {
        XSDocumentInfo xSDocumentInfo3 = xSDocumentInfo2;
        if (xSDocumentInfo3 == null) {
            return null;
        }
        XSDocumentInfo xSDocumentInfo4 = xSDocumentInfo3;
        return xSDocumentInfo4;
    }

    private boolean nonAnnotationContent(Element element) {
        Element element2 = DOMUtil.getFirstChildElement(element);
        while (element2 != null) {
            if (!DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_ANNOTATION)) {
                return true;
            }
            element2 = DOMUtil.getNextSiblingElement(element2);
        }
        return false;
    }

    private void setSchemasVisible(XSDocumentInfo xSDocumentInfo) {
        if (DOMUtil.isHidden(xSDocumentInfo.fSchemaElement, this.fHiddenNodes)) {
            DOMUtil.setVisible(xSDocumentInfo.fSchemaElement, this.fHiddenNodes);
            Vector vector = (Vector)this.fDependencyMap.get(xSDocumentInfo);
            int n2 = 0;
            while (n2 < vector.size()) {
                this.setSchemasVisible((XSDocumentInfo)vector.elementAt(n2));
                ++n2;
            }
        }
    }

    public SimpleLocator element2Locator(Element element) {
        if (!(element instanceof ElementImpl)) {
            return null;
        }
        SimpleLocator simpleLocator = new SimpleLocator();
        return this.element2Locator(element, simpleLocator) ? simpleLocator : null;
    }

    public boolean element2Locator(Element element, SimpleLocator simpleLocator) {
        if (simpleLocator == null) {
            return false;
        }
        if (element instanceof ElementImpl) {
            ElementImpl elementImpl = (ElementImpl)element;
            Document document = elementImpl.getOwnerDocument();
            String string = (String)this.fDoc2SystemId.get(DOMUtil.getRoot(document));
            int n2 = elementImpl.getLineNumber();
            int n3 = elementImpl.getColumnNumber();
            simpleLocator.setValues(string, string, n2, n3, elementImpl.getCharacterOffset());
            return true;
        }
        return false;
    }

    void reportSchemaError(String string, Object[] arrobject, Element element) {
        this.reportSchemaError(string, arrobject, element, null);
    }

    void reportSchemaError(String string, Object[] arrobject, Element element, Exception exception) {
        if (this.element2Locator(element, this.xl)) {
            this.fErrorReporter.reportError(this.xl, "http://www.w3.org/TR/xml-schema-1", string, arrobject, 1, exception);
        } else {
            this.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", string, arrobject, 1, exception);
        }
    }

    void reportSchemaWarning(String string, Object[] arrobject, Element element) {
        this.reportSchemaWarning(string, arrobject, element, null);
    }

    void reportSchemaWarning(String string, Object[] arrobject, Element element, Exception exception) {
        if (this.element2Locator(element, this.xl)) {
            this.fErrorReporter.reportError(this.xl, "http://www.w3.org/TR/xml-schema-1", string, arrobject, 0, exception);
        } else {
            this.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", string, arrobject, 0, exception);
        }
    }

    public void setGenerateSyntheticAnnotations(boolean bl) {
        this.fSchemaParser.setFeature("http://apache.org/xml/features/generate-synthetic-annotations", bl);
    }

    class 1 {
    }

    private static final class SAX2XNIUtil
    extends ErrorHandlerWrapper {
        private SAX2XNIUtil() {
        }

        public static XMLParseException createXMLParseException0(SAXParseException sAXParseException) {
            return ErrorHandlerWrapper.createXMLParseException(sAXParseException);
        }

        public static XNIException createXNIException0(SAXException sAXException) {
            return ErrorHandlerWrapper.createXNIException(sAXException);
        }
    }

    private static class XSDKey {
        String systemId;
        short referType;
        String referNS;

        XSDKey(String string, short s2, String string2) {
            this.systemId = string;
            this.referType = s2;
            this.referNS = string2;
        }

        public int hashCode() {
            return this.referNS == null ? 0 : this.referNS.hashCode();
        }

        public boolean equals(Object object) {
            if (!(object instanceof XSDKey)) {
                return false;
            }
            XSDKey xSDKey = (XSDKey)object;
            if (this.referNS != xSDKey.referNS) {
                return false;
            }
            if (this.systemId == null || !this.systemId.equals(xSDKey.systemId)) {
                return false;
            }
            return true;
        }
    }

    private static class XSAnnotationGrammarPool
    implements XMLGrammarPool {
        private XSGrammarBucket fGrammarBucket;
        private Grammar[] fInitialGrammarSet;

        private XSAnnotationGrammarPool() {
        }

        public Grammar[] retrieveInitialGrammarSet(String string) {
            if (string == "http://www.w3.org/2001/XMLSchema") {
                if (this.fInitialGrammarSet == null) {
                    if (this.fGrammarBucket == null) {
                        this.fInitialGrammarSet = new Grammar[]{SchemaGrammar.Schema4Annotations.INSTANCE};
                    } else {
                        SchemaGrammar[] arrschemaGrammar = this.fGrammarBucket.getGrammars();
                        int n2 = 0;
                        while (n2 < arrschemaGrammar.length) {
                            if (SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(arrschemaGrammar[n2].getTargetNamespace())) {
                                this.fInitialGrammarSet = arrschemaGrammar;
                                return this.fInitialGrammarSet;
                            }
                            ++n2;
                        }
                        Grammar[] arrgrammar = new Grammar[arrschemaGrammar.length + 1];
                        System.arraycopy(arrschemaGrammar, 0, arrgrammar, 0, arrschemaGrammar.length);
                        arrgrammar[arrgrammar.length - 1] = SchemaGrammar.Schema4Annotations.INSTANCE;
                        this.fInitialGrammarSet = arrgrammar;
                    }
                }
                return this.fInitialGrammarSet;
            }
            return new Grammar[0];
        }

        public void cacheGrammars(String string, Grammar[] arrgrammar) {
        }

        public Grammar retrieveGrammar(XMLGrammarDescription xMLGrammarDescription) {
            if (xMLGrammarDescription.getGrammarType() == "http://www.w3.org/2001/XMLSchema") {
                SchemaGrammar schemaGrammar;
                String string = ((XMLSchemaDescription)xMLGrammarDescription).getTargetNamespace();
                if (this.fGrammarBucket != null && (schemaGrammar = this.fGrammarBucket.getGrammar(string)) != null) {
                    return schemaGrammar;
                }
                if (SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(string)) {
                    return SchemaGrammar.Schema4Annotations.INSTANCE;
                }
            }
            return null;
        }

        public void refreshGrammars(XSGrammarBucket xSGrammarBucket) {
            this.fGrammarBucket = xSGrammarBucket;
            this.fInitialGrammarSet = null;
        }

        public void lockPool() {
        }

        public void unlockPool() {
        }

        public void clear() {
        }

        XSAnnotationGrammarPool(1 var1_1) {
            this();
        }
    }

}

