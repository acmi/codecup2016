/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.traversers;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.validation.ValidationState;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaNamespaceSupport;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAttributeDecl;
import org.apache.xerces.impl.xs.XSGrammarBucket;
import org.apache.xerces.impl.xs.traversers.Container;
import org.apache.xerces.impl.xs.traversers.OneAttr;
import org.apache.xerces.impl.xs.traversers.XSDHandler;
import org.apache.xerces.impl.xs.traversers.XSDocumentInfo;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.impl.xs.util.XIntPool;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public class XSAttributeChecker {
    private static final String ELEMENT_N = "element_n";
    private static final String ELEMENT_R = "element_r";
    private static final String ATTRIBUTE_N = "attribute_n";
    private static final String ATTRIBUTE_R = "attribute_r";
    private static int ATTIDX_COUNT = 0;
    public static final int ATTIDX_ABSTRACT = ATTIDX_COUNT++;
    public static final int ATTIDX_AFORMDEFAULT = ATTIDX_COUNT++;
    public static final int ATTIDX_BASE = ATTIDX_COUNT++;
    public static final int ATTIDX_BLOCK = ATTIDX_COUNT++;
    public static final int ATTIDX_BLOCKDEFAULT = ATTIDX_COUNT++;
    public static final int ATTIDX_DEFAULT = ATTIDX_COUNT++;
    public static final int ATTIDX_EFORMDEFAULT = ATTIDX_COUNT++;
    public static final int ATTIDX_FINAL = ATTIDX_COUNT++;
    public static final int ATTIDX_FINALDEFAULT = ATTIDX_COUNT++;
    public static final int ATTIDX_FIXED = ATTIDX_COUNT++;
    public static final int ATTIDX_FORM = ATTIDX_COUNT++;
    public static final int ATTIDX_ID = ATTIDX_COUNT++;
    public static final int ATTIDX_ITEMTYPE = ATTIDX_COUNT++;
    public static final int ATTIDX_MAXOCCURS = ATTIDX_COUNT++;
    public static final int ATTIDX_MEMBERTYPES = ATTIDX_COUNT++;
    public static final int ATTIDX_MINOCCURS = ATTIDX_COUNT++;
    public static final int ATTIDX_MIXED = ATTIDX_COUNT++;
    public static final int ATTIDX_NAME = ATTIDX_COUNT++;
    public static final int ATTIDX_NAMESPACE = ATTIDX_COUNT++;
    public static final int ATTIDX_NAMESPACE_LIST = ATTIDX_COUNT++;
    public static final int ATTIDX_NILLABLE = ATTIDX_COUNT++;
    public static final int ATTIDX_NONSCHEMA = ATTIDX_COUNT++;
    public static final int ATTIDX_PROCESSCONTENTS = ATTIDX_COUNT++;
    public static final int ATTIDX_PUBLIC = ATTIDX_COUNT++;
    public static final int ATTIDX_REF = ATTIDX_COUNT++;
    public static final int ATTIDX_REFER = ATTIDX_COUNT++;
    public static final int ATTIDX_SCHEMALOCATION = ATTIDX_COUNT++;
    public static final int ATTIDX_SOURCE = ATTIDX_COUNT++;
    public static final int ATTIDX_SUBSGROUP = ATTIDX_COUNT++;
    public static final int ATTIDX_SYSTEM = ATTIDX_COUNT++;
    public static final int ATTIDX_TARGETNAMESPACE = ATTIDX_COUNT++;
    public static final int ATTIDX_TYPE = ATTIDX_COUNT++;
    public static final int ATTIDX_USE = ATTIDX_COUNT++;
    public static final int ATTIDX_VALUE = ATTIDX_COUNT++;
    public static final int ATTIDX_ENUMNSDECLS = ATTIDX_COUNT++;
    public static final int ATTIDX_VERSION = ATTIDX_COUNT++;
    public static final int ATTIDX_XML_LANG = ATTIDX_COUNT++;
    public static final int ATTIDX_XPATH = ATTIDX_COUNT++;
    public static final int ATTIDX_FROMDEFAULT = ATTIDX_COUNT++;
    public static final int ATTIDX_ISRETURNED = ATTIDX_COUNT++;
    private static final XIntPool fXIntPool = new XIntPool();
    private static final XInt INT_QUALIFIED = fXIntPool.getXInt(1);
    private static final XInt INT_UNQUALIFIED = fXIntPool.getXInt(0);
    private static final XInt INT_EMPTY_SET = fXIntPool.getXInt(0);
    private static final XInt INT_ANY_STRICT = fXIntPool.getXInt(1);
    private static final XInt INT_ANY_LAX = fXIntPool.getXInt(3);
    private static final XInt INT_ANY_SKIP = fXIntPool.getXInt(2);
    private static final XInt INT_ANY_ANY = fXIntPool.getXInt(1);
    private static final XInt INT_ANY_LIST = fXIntPool.getXInt(3);
    private static final XInt INT_ANY_NOT = fXIntPool.getXInt(2);
    private static final XInt INT_USE_OPTIONAL = fXIntPool.getXInt(0);
    private static final XInt INT_USE_REQUIRED = fXIntPool.getXInt(1);
    private static final XInt INT_USE_PROHIBITED = fXIntPool.getXInt(2);
    private static final XInt INT_WS_PRESERVE = fXIntPool.getXInt(0);
    private static final XInt INT_WS_REPLACE = fXIntPool.getXInt(1);
    private static final XInt INT_WS_COLLAPSE = fXIntPool.getXInt(2);
    private static final XInt INT_UNBOUNDED = fXIntPool.getXInt(-1);
    private static final Hashtable fEleAttrsMapG = new Hashtable(29);
    private static final Hashtable fEleAttrsMapL = new Hashtable(79);
    protected static final int DT_ANYURI = 0;
    protected static final int DT_ID = 1;
    protected static final int DT_QNAME = 2;
    protected static final int DT_STRING = 3;
    protected static final int DT_TOKEN = 4;
    protected static final int DT_NCNAME = 5;
    protected static final int DT_XPATH = 6;
    protected static final int DT_XPATH1 = 7;
    protected static final int DT_LANGUAGE = 8;
    protected static final int DT_COUNT = 9;
    private static final XSSimpleType[] fExtraDVs = new XSSimpleType[9];
    protected static final int DT_BLOCK = -1;
    protected static final int DT_BLOCK1 = -2;
    protected static final int DT_FINAL = -3;
    protected static final int DT_FINAL1 = -4;
    protected static final int DT_FINAL2 = -5;
    protected static final int DT_FORM = -6;
    protected static final int DT_MAXOCCURS = -7;
    protected static final int DT_MAXOCCURS1 = -8;
    protected static final int DT_MEMBERTYPES = -9;
    protected static final int DT_MINOCCURS1 = -10;
    protected static final int DT_NAMESPACE = -11;
    protected static final int DT_PROCESSCONTENTS = -12;
    protected static final int DT_USE = -13;
    protected static final int DT_WHITESPACE = -14;
    protected static final int DT_BOOLEAN = -15;
    protected static final int DT_NONNEGINT = -16;
    protected static final int DT_POSINT = -17;
    protected XSDHandler fSchemaHandler = null;
    protected SymbolTable fSymbolTable = null;
    protected Hashtable fNonSchemaAttrs = new Hashtable();
    protected Vector fNamespaceList = new Vector();
    protected boolean[] fSeen = new boolean[ATTIDX_COUNT];
    private static boolean[] fSeenTemp;
    static final int INIT_POOL_SIZE = 10;
    static final int INC_POOL_SIZE = 10;
    Object[][] fArrayPool = new Object[10][ATTIDX_COUNT];
    private static Object[] fTempArray;
    int fPoolPos = 0;

    public XSAttributeChecker(XSDHandler xSDHandler) {
        this.fSchemaHandler = xSDHandler;
    }

    public void reset(SymbolTable symbolTable) {
        this.fSymbolTable = symbolTable;
        this.fNonSchemaAttrs.clear();
    }

    public Object[] checkAttributes(Element element, boolean bl, XSDocumentInfo xSDocumentInfo) {
        return this.checkAttributes(element, bl, xSDocumentInfo, false);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public Object[] checkAttributes(Element var1_1, boolean var2_2, XSDocumentInfo var3_3, boolean var4_4) {
        if (var1_1 == null) {
            return null;
        }
        var5_5 = DOMUtil.getAttrs(var1_1);
        this.resolveNamespace(var1_1, var5_5, var3_3.fNamespaceSupport);
        var6_6 = DOMUtil.getNamespaceURI(var1_1);
        var7_7 = DOMUtil.getLocalName(var1_1);
        if (!SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(var6_6)) {
            this.reportSchemaError("s4s-elt-schema-ns", new Object[]{var7_7}, var1_1);
        }
        var8_8 = XSAttributeChecker.fEleAttrsMapG;
        var9_9 = var7_7;
        if (!var2_2) {
            var8_8 = XSAttributeChecker.fEleAttrsMapL;
            if (var7_7.equals(SchemaSymbols.ELT_ELEMENT)) {
                var9_9 = DOMUtil.getAttr(var1_1, SchemaSymbols.ATT_REF) != null ? "element_r" : "element_n";
            } else if (var7_7.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
                var9_9 = DOMUtil.getAttr(var1_1, SchemaSymbols.ATT_REF) != null ? "attribute_r" : "attribute_n";
            }
        }
        if ((var10_10 = (Container)var8_8.get(var9_9)) == null) {
            this.reportSchemaError("s4s-elt-invalid", new Object[]{var7_7}, var1_1);
            return null;
        }
        var11_11 = this.getAvailableArray();
        var12_12 = 0;
        System.arraycopy(XSAttributeChecker.fSeenTemp, 0, this.fSeen, 0, XSAttributeChecker.ATTIDX_COUNT);
        var14_13 = var5_5.length;
        var15_14 = null;
        var16_15 = 0;
        while (var16_15 < var14_13) {
            var15_14 = var5_5[var16_15];
            var17_16 = var15_14.getName();
            var18_17 = DOMUtil.getNamespaceURI(var15_14);
            var19_19 = DOMUtil.getValue(var15_14);
            if (!var17_16.startsWith("xml")) ** GOTO lbl37
            var20_21 = DOMUtil.getPrefix(var15_14);
            if (!"xmlns".equals(var20_21) && !"xmlns".equals(var17_16)) {
                if (SchemaSymbols.ATT_XML_LANG.equals(var17_16) && (SchemaSymbols.ELT_SCHEMA.equals(var7_7) || SchemaSymbols.ELT_DOCUMENTATION.equals(var7_7))) {
                    var18_17 = null;
                }
lbl37: // 4 sources:
                if (var18_17 != null && var18_17.length() != 0) {
                    if (var18_17.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) {
                        this.reportSchemaError("s4s-att-not-allowed", new Object[]{var7_7, var17_16}, var1_1);
                    } else {
                        if (var11_11[XSAttributeChecker.ATTIDX_NONSCHEMA] == null) {
                            var11_11[XSAttributeChecker.ATTIDX_NONSCHEMA] = new Vector<E>(4, 2);
                        }
                        ((Vector)var11_11[XSAttributeChecker.ATTIDX_NONSCHEMA]).addElement(var17_16);
                        ((Vector)var11_11[XSAttributeChecker.ATTIDX_NONSCHEMA]).addElement(var19_19);
                    }
                } else {
                    var20_21 = var10_10.get((String)var17_16);
                    if (var20_21 == null) {
                        this.reportSchemaError("s4s-att-not-allowed", new Object[]{var7_7, var17_16}, var1_1);
                    } else {
                        block28 : {
                            this.fSeen[var20_21.valueIndex] = true;
                            try {
                                if (var20_21.dvIndex >= 0) {
                                    if (var20_21.dvIndex != 3 && var20_21.dvIndex != 6 && var20_21.dvIndex != 7) {
                                        var21_23 = XSAttributeChecker.fExtraDVs[var20_21.dvIndex];
                                        var22_25 = var21_23.validate((String)var19_19, (ValidationContext)var3_3.fValidationContext, null);
                                        if (var20_21.dvIndex == 2) {
                                            var23_26 = (QName)var22_25;
                                            if (var23_26.prefix == XMLSymbols.EMPTY_STRING && var23_26.uri == null && var3_3.fIsChameleonSchema) {
                                                var23_26.uri = var3_3.fTargetNamespace;
                                            }
                                        }
                                        var11_11[var20_21.valueIndex] = var22_25;
                                    } else {
                                        var11_11[var20_21.valueIndex] = var19_19;
                                    }
                                } else {
                                    var11_11[var20_21.valueIndex] = this.validate(var11_11, (String)var17_16, (String)var19_19, var20_21.dvIndex, var3_3);
                                }
                            }
                            catch (InvalidDatatypeValueException var21_24) {
                                this.reportSchemaError("s4s-att-invalid-value", new Object[]{var7_7, var17_16, var21_24.getMessage()}, var1_1);
                                if (var20_21.dfltValue == null) break block28;
                                var11_11[var20_21.valueIndex] = var20_21.dfltValue;
                            }
                        }
                        if (var7_7.equals(SchemaSymbols.ELT_ENUMERATION) && var4_4) {
                            var11_11[XSAttributeChecker.ATTIDX_ENUMNSDECLS] = new SchemaNamespaceSupport(var3_3.fNamespaceSupport);
                        }
                    }
                }
            }
            ++var16_15;
        }
        var17_16 = var10_10.values;
        var18_18 = 0;
        while (var18_18 < var17_16.length) {
            var19_19 = var17_16[var18_18];
            if (var19_19.dfltValue != null && !this.fSeen[var19_19.valueIndex]) {
                var11_11[var19_19.valueIndex] = var19_19.dfltValue;
                var12_12 |= (long)(1 << var19_19.valueIndex);
            }
            ++var18_18;
        }
        var11_11[XSAttributeChecker.ATTIDX_FROMDEFAULT] = new Long(var12_12);
        if (var11_11[XSAttributeChecker.ATTIDX_MAXOCCURS] == null) return var11_11;
        var19_20 = ((XInt)var11_11[XSAttributeChecker.ATTIDX_MINOCCURS]).intValue();
        var20_22 = ((XInt)var11_11[XSAttributeChecker.ATTIDX_MAXOCCURS]).intValue();
        if (var20_22 == -1) return var11_11;
        if (var19_20 <= var20_22) return var11_11;
        this.reportSchemaError("p-props-correct.2.1", new Object[]{var7_7, var11_11[XSAttributeChecker.ATTIDX_MINOCCURS], var11_11[XSAttributeChecker.ATTIDX_MAXOCCURS]}, var1_1);
        var11_11[XSAttributeChecker.ATTIDX_MINOCCURS] = var11_11[XSAttributeChecker.ATTIDX_MAXOCCURS];
        return var11_11;
    }

    private Object validate(Object[] arrobject, String string, String string2, int n2, XSDocumentInfo xSDocumentInfo) throws InvalidDatatypeValueException {
        if (string2 == null) {
            return null;
        }
        String string3 = XMLChar.trim(string2);
        Vector<QName> vector = null;
        switch (n2) {
            case -15: {
                if (string3.equals("false") || string3.equals("0")) {
                    vector = Boolean.FALSE;
                    break;
                }
                if (string3.equals("true") || string3.equals("1")) {
                    vector = Boolean.TRUE;
                    break;
                }
                throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{string3, "boolean"});
            }
            case -16: {
                try {
                    if (string3.length() > 0 && string3.charAt(0) == '+') {
                        string3 = string3.substring(1);
                    }
                    vector = fXIntPool.getXInt(Integer.parseInt(string3));
                }
                catch (NumberFormatException numberFormatException) {
                    throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{string3, "nonNegativeInteger"});
                }
                if (((XInt)((Object)vector)).intValue() >= 0) break;
                throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{string3, "nonNegativeInteger"});
            }
            case -17: {
                try {
                    if (string3.length() > 0 && string3.charAt(0) == '+') {
                        string3 = string3.substring(1);
                    }
                    vector = fXIntPool.getXInt(Integer.parseInt(string3));
                }
                catch (NumberFormatException numberFormatException) {
                    throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{string3, "positiveInteger"});
                }
                if (((XInt)((Object)vector)).intValue() > 0) break;
                throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{string3, "positiveInteger"});
            }
            case -1: {
                int n3 = 0;
                if (string3.equals("#all")) {
                    n3 = 31;
                } else {
                    StringTokenizer stringTokenizer = new StringTokenizer(string3, " \n\t\r");
                    while (stringTokenizer.hasMoreTokens()) {
                        String string4 = stringTokenizer.nextToken();
                        if (string4.equals("extension")) {
                            n3 |= 1;
                            continue;
                        }
                        if (string4.equals("restriction")) {
                            n3 |= 2;
                            continue;
                        }
                        if (string4.equals("substitution")) {
                            n3 |= 4;
                            continue;
                        }
                        throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[]{string3, "(#all | List of (extension | restriction | substitution))"});
                    }
                }
                vector = fXIntPool.getXInt(n3);
                break;
            }
            case -3: 
            case -2: {
                int n4 = 0;
                if (string3.equals("#all")) {
                    n4 = 31;
                } else {
                    StringTokenizer stringTokenizer = new StringTokenizer(string3, " \n\t\r");
                    while (stringTokenizer.hasMoreTokens()) {
                        String string5 = stringTokenizer.nextToken();
                        if (string5.equals("extension")) {
                            n4 |= 1;
                            continue;
                        }
                        if (string5.equals("restriction")) {
                            n4 |= 2;
                            continue;
                        }
                        throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[]{string3, "(#all | List of (extension | restriction))"});
                    }
                }
                vector = fXIntPool.getXInt(n4);
                break;
            }
            case -4: {
                int n5 = 0;
                if (string3.equals("#all")) {
                    n5 = 31;
                } else {
                    StringTokenizer stringTokenizer = new StringTokenizer(string3, " \n\t\r");
                    while (stringTokenizer.hasMoreTokens()) {
                        String string6 = stringTokenizer.nextToken();
                        if (string6.equals("list")) {
                            n5 |= 16;
                            continue;
                        }
                        if (string6.equals("union")) {
                            n5 |= 8;
                            continue;
                        }
                        if (string6.equals("restriction")) {
                            n5 |= 2;
                            continue;
                        }
                        throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[]{string3, "(#all | List of (list | union | restriction))"});
                    }
                }
                vector = fXIntPool.getXInt(n5);
                break;
            }
            case -5: {
                int n6 = 0;
                if (string3.equals("#all")) {
                    n6 = 31;
                } else {
                    StringTokenizer stringTokenizer = new StringTokenizer(string3, " \n\t\r");
                    while (stringTokenizer.hasMoreTokens()) {
                        String string7 = stringTokenizer.nextToken();
                        if (string7.equals("extension")) {
                            n6 |= 1;
                            continue;
                        }
                        if (string7.equals("restriction")) {
                            n6 |= 2;
                            continue;
                        }
                        if (string7.equals("list")) {
                            n6 |= 16;
                            continue;
                        }
                        if (string7.equals("union")) {
                            n6 |= 8;
                            continue;
                        }
                        throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[]{string3, "(#all | List of (extension | restriction | list | union))"});
                    }
                }
                vector = fXIntPool.getXInt(n6);
                break;
            }
            case -6: {
                if (string3.equals("qualified")) {
                    vector = INT_QUALIFIED;
                    break;
                }
                if (string3.equals("unqualified")) {
                    vector = INT_UNQUALIFIED;
                    break;
                }
                throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[]{string3, "(qualified | unqualified)"});
            }
            case -7: {
                if (string3.equals("unbounded")) {
                    vector = INT_UNBOUNDED;
                    break;
                }
                try {
                    vector = this.validate(arrobject, string, string3, -16, xSDocumentInfo);
                    break;
                }
                catch (NumberFormatException numberFormatException) {
                    throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[]{string3, "(nonNegativeInteger | unbounded)"});
                }
            }
            case -8: {
                if (string3.equals("1")) {
                    vector = fXIntPool.getXInt(1);
                    break;
                }
                throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[]{string3, "(1)"});
            }
            case -9: {
                Vector<QName> vector2 = new Vector<QName>();
                try {
                    StringTokenizer stringTokenizer = new StringTokenizer(string3, " \n\t\r");
                    while (stringTokenizer.hasMoreTokens()) {
                        String string8 = stringTokenizer.nextToken();
                        QName qName = (QName)fExtraDVs[2].validate(string8, (ValidationContext)xSDocumentInfo.fValidationContext, null);
                        if (qName.prefix == XMLSymbols.EMPTY_STRING && qName.uri == null && xSDocumentInfo.fIsChameleonSchema) {
                            qName.uri = xSDocumentInfo.fTargetNamespace;
                        }
                        vector2.addElement(qName);
                    }
                    vector = vector2;
                    break;
                }
                catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                    throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.2", new Object[]{string3, "(List of QName)"});
                }
            }
            case -10: {
                if (string3.equals("0")) {
                    vector = fXIntPool.getXInt(0);
                    break;
                }
                if (string3.equals("1")) {
                    vector = fXIntPool.getXInt(1);
                    break;
                }
                throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[]{string3, "(0 | 1)"});
            }
            case -11: {
                if (string3.equals("##any")) {
                    vector = INT_ANY_ANY;
                    break;
                }
                if (string3.equals("##other")) {
                    vector = INT_ANY_NOT;
                    String[] arrstring = new String[]{xSDocumentInfo.fTargetNamespace, null};
                    arrobject[XSAttributeChecker.ATTIDX_NAMESPACE_LIST] = arrstring;
                    break;
                }
                vector = INT_ANY_LIST;
                this.fNamespaceList.removeAllElements();
                StringTokenizer stringTokenizer = new StringTokenizer(string3, " \n\t\r");
                try {
                    while (stringTokenizer.hasMoreTokens()) {
                        String string9;
                        String string10 = stringTokenizer.nextToken();
                        if (string10.equals("##local")) {
                            string9 = null;
                        } else if (string10.equals("##targetNamespace")) {
                            string9 = xSDocumentInfo.fTargetNamespace;
                        } else {
                            fExtraDVs[0].validate(string10, (ValidationContext)xSDocumentInfo.fValidationContext, null);
                            string9 = this.fSymbolTable.addSymbol(string10);
                        }
                        if (this.fNamespaceList.contains(string9)) continue;
                        this.fNamespaceList.addElement(string9);
                    }
                }
                catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                    throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[]{string3, "((##any | ##other) | List of (anyURI | (##targetNamespace | ##local)) )"});
                }
                int n7 = this.fNamespaceList.size();
                Object[] arrobject2 = new String[n7];
                this.fNamespaceList.copyInto(arrobject2);
                arrobject[XSAttributeChecker.ATTIDX_NAMESPACE_LIST] = arrobject2;
                break;
            }
            case -12: {
                if (string3.equals("strict")) {
                    vector = INT_ANY_STRICT;
                    break;
                }
                if (string3.equals("lax")) {
                    vector = INT_ANY_LAX;
                    break;
                }
                if (string3.equals("skip")) {
                    vector = INT_ANY_SKIP;
                    break;
                }
                throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[]{string3, "(lax | skip | strict)"});
            }
            case -13: {
                if (string3.equals("optional")) {
                    vector = INT_USE_OPTIONAL;
                    break;
                }
                if (string3.equals("required")) {
                    vector = INT_USE_REQUIRED;
                    break;
                }
                if (string3.equals("prohibited")) {
                    vector = INT_USE_PROHIBITED;
                    break;
                }
                throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[]{string3, "(optional | prohibited | required)"});
            }
            case -14: {
                if (string3.equals("preserve")) {
                    vector = INT_WS_PRESERVE;
                    break;
                }
                if (string3.equals("replace")) {
                    vector = INT_WS_REPLACE;
                    break;
                }
                if (string3.equals("collapse")) {
                    vector = INT_WS_COLLAPSE;
                    break;
                }
                throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[]{string3, "(preserve | replace | collapse)"});
            }
        }
        return vector;
    }

    void reportSchemaError(String string, Object[] arrobject, Element element) {
        this.fSchemaHandler.reportSchemaError(string, arrobject, element);
    }

    public void checkNonSchemaAttributes(XSGrammarBucket xSGrammarBucket) {
        Iterator iterator = this.fNonSchemaAttrs.entrySet().iterator();
        while (iterator.hasNext()) {
            XSSimpleType xSSimpleType;
            XSAttributeDecl xSAttributeDecl;
            Map.Entry entry = iterator.next();
            String string = (String)entry.getKey();
            String string2 = string.substring(0, string.indexOf(44));
            String string3 = string.substring(string.indexOf(44) + 1);
            SchemaGrammar schemaGrammar = xSGrammarBucket.getGrammar(string2);
            if (schemaGrammar == null || (xSAttributeDecl = schemaGrammar.getGlobalAttributeDecl(string3)) == null || (xSSimpleType = (XSSimpleType)xSAttributeDecl.getTypeDefinition()) == null) continue;
            Vector vector = (Vector)entry.getValue();
            String string4 = (String)vector.elementAt(0);
            int n2 = vector.size();
            int n3 = 1;
            while (n3 < n2) {
                String string5 = (String)vector.elementAt(n3);
                try {
                    xSSimpleType.validate((String)vector.elementAt(n3 + 1), null, null);
                }
                catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                    this.reportSchemaError("s4s-att-invalid-value", new Object[]{string5, string4, invalidDatatypeValueException.getMessage()}, null);
                }
                n3 += 2;
            }
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static String normalize(String var0, short var1_1) {
        if (var0 == null) {
            return var0;
        }
        v0 = var0.length();
        var2_2 = v0;
        if (var2_2 == 0) return var0;
        if (var1_1 == 0) {
            return var0;
        }
        var3_3 = new StringBuffer();
        if (var1_1 != 1) ** GOTO lbl12
        var5_4 = 0;
        ** GOTO lbl21
lbl12: // 1 sources:
        var6_8 = true;
        var5_5 = 0;
        ** GOTO lbl34
lbl-1000: // 1 sources:
        {
            var4_6 = var0.charAt(var5_4);
            if (var4_6 != '\t' && var4_6 != '\n' && var4_6 != '\r') {
                var3_3.append(var4_6);
            } else {
                var3_3.append(' ');
            }
            ++var5_4;
lbl21: // 2 sources:
            ** while (var5_4 < var2_2)
        }
lbl22: // 1 sources:
        return var3_3.toString();
lbl-1000: // 1 sources:
        {
            var4_7 = var0.charAt(var5_5);
            if (var4_7 == '\t' || var4_7 == '\n' || var4_7 == '\r' || var4_7 == ' ') ** GOTO lbl30
            var3_3.append(var4_7);
            var6_8 = false;
            ** GOTO lbl33
            while ((var4_7 = var0.charAt(var5_5 + 1)) == '\t' || var4_7 == '\n' || var4_7 == '\r' || var4_7 == ' ') {
                ++var5_5;
lbl30: // 2 sources:
                if (var5_5 < var2_2 - 1) continue;
            }
            if (var5_5 < var2_2 - 1 && !var6_8) {
                var3_3.append(' ');
            }
lbl33: // 4 sources:
            ++var5_5;
lbl34: // 2 sources:
            ** while (var5_5 < var2_2)
        }
lbl35: // 1 sources:
        return var3_3.toString();
    }

    protected Object[] getAvailableArray() {
        if (this.fArrayPool.length == this.fPoolPos) {
            this.fArrayPool = new Object[this.fPoolPos + 10][];
            int n2 = this.fPoolPos;
            while (n2 < this.fArrayPool.length) {
                this.fArrayPool[n2] = new Object[ATTIDX_COUNT];
                ++n2;
            }
        }
        Object[] arrobject = this.fArrayPool[this.fPoolPos];
        this.fArrayPool[this.fPoolPos++] = null;
        System.arraycopy(fTempArray, 0, arrobject, 0, ATTIDX_COUNT - 1);
        arrobject[XSAttributeChecker.ATTIDX_ISRETURNED] = Boolean.FALSE;
        return arrobject;
    }

    public void returnAttrArray(Object[] arrobject, XSDocumentInfo xSDocumentInfo) {
        if (xSDocumentInfo != null) {
            xSDocumentInfo.fNamespaceSupport.popContext();
        }
        if (this.fPoolPos == 0 || arrobject == null || arrobject.length != ATTIDX_COUNT || ((Boolean)arrobject[ATTIDX_ISRETURNED]).booleanValue()) {
            return;
        }
        arrobject[XSAttributeChecker.ATTIDX_ISRETURNED] = Boolean.TRUE;
        if (arrobject[ATTIDX_NONSCHEMA] != null) {
            ((Vector)arrobject[ATTIDX_NONSCHEMA]).clear();
        }
        this.fArrayPool[--this.fPoolPos] = arrobject;
    }

    public void resolveNamespace(Element element, Attr[] arrattr, SchemaNamespaceSupport schemaNamespaceSupport) {
        schemaNamespaceSupport.pushContext();
        int n2 = arrattr.length;
        Attr attr = null;
        int n3 = 0;
        while (n3 < n2) {
            attr = arrattr[n3];
            String string = DOMUtil.getName(attr);
            String string2 = null;
            if (string.equals(XMLSymbols.PREFIX_XMLNS)) {
                string2 = XMLSymbols.EMPTY_STRING;
            } else if (string.startsWith("xmlns:")) {
                string2 = this.fSymbolTable.addSymbol(DOMUtil.getLocalName(attr));
            }
            if (string2 != null) {
                String string3 = this.fSymbolTable.addSymbol(DOMUtil.getValue(attr));
                schemaNamespaceSupport.declarePrefix(string2, string3.length() != 0 ? string3 : null);
            }
            ++n3;
        }
    }

    static {
        SchemaGrammar.BuiltinSchemaGrammar builtinSchemaGrammar = SchemaGrammar.SG_SchemaNS;
        XSAttributeChecker.fExtraDVs[0] = (XSSimpleType)builtinSchemaGrammar.getGlobalTypeDecl("anyURI");
        XSAttributeChecker.fExtraDVs[1] = (XSSimpleType)builtinSchemaGrammar.getGlobalTypeDecl("ID");
        XSAttributeChecker.fExtraDVs[2] = (XSSimpleType)builtinSchemaGrammar.getGlobalTypeDecl("QName");
        XSAttributeChecker.fExtraDVs[3] = (XSSimpleType)builtinSchemaGrammar.getGlobalTypeDecl("string");
        XSAttributeChecker.fExtraDVs[4] = (XSSimpleType)builtinSchemaGrammar.getGlobalTypeDecl("token");
        XSAttributeChecker.fExtraDVs[5] = (XSSimpleType)builtinSchemaGrammar.getGlobalTypeDecl("NCName");
        XSAttributeChecker.fExtraDVs[6] = fExtraDVs[3];
        XSAttributeChecker.fExtraDVs[6] = fExtraDVs[3];
        XSAttributeChecker.fExtraDVs[8] = (XSSimpleType)builtinSchemaGrammar.getGlobalTypeDecl("language");
        int n2 = 0;
        int n3 = n2++;
        int n4 = n2++;
        int n5 = n2++;
        int n6 = n2++;
        int n7 = n2++;
        int n8 = n2++;
        int n9 = n2++;
        int n10 = n2++;
        int n11 = n2++;
        int n12 = n2++;
        int n13 = n2++;
        int n14 = n2++;
        int n15 = n2++;
        int n16 = n2++;
        int n17 = n2++;
        int n18 = n2++;
        int n19 = n2++;
        int n20 = n2++;
        int n21 = n2++;
        int n22 = n2++;
        int n23 = n2++;
        int n24 = n2++;
        int n25 = n2++;
        int n26 = n2++;
        int n27 = n2++;
        int n28 = n2++;
        int n29 = n2++;
        int n30 = n2++;
        int n31 = n2++;
        int n32 = n2++;
        int n33 = n2++;
        int n34 = n2++;
        int n35 = n2++;
        int n36 = n2++;
        int n37 = n2++;
        int n38 = n2++;
        int n39 = n2++;
        int n40 = n2++;
        int n41 = n2++;
        int n42 = n2++;
        int n43 = n2++;
        int n44 = n2++;
        int n45 = n2++;
        int n46 = n2++;
        int n47 = n2++;
        int n48 = n2++;
        int n49 = n2++;
        int n50 = n2++;
        OneAttr[] arroneAttr = new OneAttr[n2];
        arroneAttr[n3] = new OneAttr(SchemaSymbols.ATT_ABSTRACT, -15, ATTIDX_ABSTRACT, Boolean.FALSE);
        arroneAttr[n4] = new OneAttr(SchemaSymbols.ATT_ATTRIBUTEFORMDEFAULT, -6, ATTIDX_AFORMDEFAULT, INT_UNQUALIFIED);
        arroneAttr[n5] = new OneAttr(SchemaSymbols.ATT_BASE, 2, ATTIDX_BASE, null);
        arroneAttr[n6] = new OneAttr(SchemaSymbols.ATT_BASE, 2, ATTIDX_BASE, null);
        arroneAttr[n7] = new OneAttr(SchemaSymbols.ATT_BLOCK, -1, ATTIDX_BLOCK, null);
        arroneAttr[n8] = new OneAttr(SchemaSymbols.ATT_BLOCK, -2, ATTIDX_BLOCK, null);
        arroneAttr[n9] = new OneAttr(SchemaSymbols.ATT_BLOCKDEFAULT, -1, ATTIDX_BLOCKDEFAULT, INT_EMPTY_SET);
        arroneAttr[n10] = new OneAttr(SchemaSymbols.ATT_DEFAULT, 3, ATTIDX_DEFAULT, null);
        arroneAttr[n11] = new OneAttr(SchemaSymbols.ATT_ELEMENTFORMDEFAULT, -6, ATTIDX_EFORMDEFAULT, INT_UNQUALIFIED);
        arroneAttr[n12] = new OneAttr(SchemaSymbols.ATT_FINAL, -3, ATTIDX_FINAL, null);
        arroneAttr[n13] = new OneAttr(SchemaSymbols.ATT_FINAL, -4, ATTIDX_FINAL, null);
        arroneAttr[n14] = new OneAttr(SchemaSymbols.ATT_FINALDEFAULT, -5, ATTIDX_FINALDEFAULT, INT_EMPTY_SET);
        arroneAttr[n15] = new OneAttr(SchemaSymbols.ATT_FIXED, 3, ATTIDX_FIXED, null);
        arroneAttr[n16] = new OneAttr(SchemaSymbols.ATT_FIXED, -15, ATTIDX_FIXED, Boolean.FALSE);
        arroneAttr[n17] = new OneAttr(SchemaSymbols.ATT_FORM, -6, ATTIDX_FORM, null);
        arroneAttr[n18] = new OneAttr(SchemaSymbols.ATT_ID, 1, ATTIDX_ID, null);
        arroneAttr[n19] = new OneAttr(SchemaSymbols.ATT_ITEMTYPE, 2, ATTIDX_ITEMTYPE, null);
        arroneAttr[n20] = new OneAttr(SchemaSymbols.ATT_MAXOCCURS, -7, ATTIDX_MAXOCCURS, fXIntPool.getXInt(1));
        arroneAttr[n21] = new OneAttr(SchemaSymbols.ATT_MAXOCCURS, -8, ATTIDX_MAXOCCURS, fXIntPool.getXInt(1));
        arroneAttr[n22] = new OneAttr(SchemaSymbols.ATT_MEMBERTYPES, -9, ATTIDX_MEMBERTYPES, null);
        arroneAttr[n23] = new OneAttr(SchemaSymbols.ATT_MINOCCURS, -16, ATTIDX_MINOCCURS, fXIntPool.getXInt(1));
        arroneAttr[n24] = new OneAttr(SchemaSymbols.ATT_MINOCCURS, -10, ATTIDX_MINOCCURS, fXIntPool.getXInt(1));
        arroneAttr[n25] = new OneAttr(SchemaSymbols.ATT_MIXED, -15, ATTIDX_MIXED, Boolean.FALSE);
        arroneAttr[n26] = new OneAttr(SchemaSymbols.ATT_MIXED, -15, ATTIDX_MIXED, null);
        arroneAttr[n27] = new OneAttr(SchemaSymbols.ATT_NAME, 5, ATTIDX_NAME, null);
        arroneAttr[n28] = new OneAttr(SchemaSymbols.ATT_NAMESPACE, -11, ATTIDX_NAMESPACE, INT_ANY_ANY);
        arroneAttr[n29] = new OneAttr(SchemaSymbols.ATT_NAMESPACE, 0, ATTIDX_NAMESPACE, null);
        arroneAttr[n30] = new OneAttr(SchemaSymbols.ATT_NILLABLE, -15, ATTIDX_NILLABLE, Boolean.FALSE);
        arroneAttr[n31] = new OneAttr(SchemaSymbols.ATT_PROCESSCONTENTS, -12, ATTIDX_PROCESSCONTENTS, INT_ANY_STRICT);
        arroneAttr[n32] = new OneAttr(SchemaSymbols.ATT_PUBLIC, 4, ATTIDX_PUBLIC, null);
        arroneAttr[n33] = new OneAttr(SchemaSymbols.ATT_REF, 2, ATTIDX_REF, null);
        arroneAttr[n34] = new OneAttr(SchemaSymbols.ATT_REFER, 2, ATTIDX_REFER, null);
        arroneAttr[n35] = new OneAttr(SchemaSymbols.ATT_SCHEMALOCATION, 0, ATTIDX_SCHEMALOCATION, null);
        arroneAttr[n36] = new OneAttr(SchemaSymbols.ATT_SCHEMALOCATION, 0, ATTIDX_SCHEMALOCATION, null);
        arroneAttr[n37] = new OneAttr(SchemaSymbols.ATT_SOURCE, 0, ATTIDX_SOURCE, null);
        arroneAttr[n38] = new OneAttr(SchemaSymbols.ATT_SUBSTITUTIONGROUP, 2, ATTIDX_SUBSGROUP, null);
        arroneAttr[n39] = new OneAttr(SchemaSymbols.ATT_SYSTEM, 0, ATTIDX_SYSTEM, null);
        arroneAttr[n40] = new OneAttr(SchemaSymbols.ATT_TARGETNAMESPACE, 0, ATTIDX_TARGETNAMESPACE, null);
        arroneAttr[n41] = new OneAttr(SchemaSymbols.ATT_TYPE, 2, ATTIDX_TYPE, null);
        arroneAttr[n42] = new OneAttr(SchemaSymbols.ATT_USE, -13, ATTIDX_USE, INT_USE_OPTIONAL);
        arroneAttr[n43] = new OneAttr(SchemaSymbols.ATT_VALUE, -16, ATTIDX_VALUE, null);
        arroneAttr[n44] = new OneAttr(SchemaSymbols.ATT_VALUE, -17, ATTIDX_VALUE, null);
        arroneAttr[n45] = new OneAttr(SchemaSymbols.ATT_VALUE, 3, ATTIDX_VALUE, null);
        arroneAttr[n46] = new OneAttr(SchemaSymbols.ATT_VALUE, -14, ATTIDX_VALUE, null);
        arroneAttr[n47] = new OneAttr(SchemaSymbols.ATT_VERSION, 4, ATTIDX_VERSION, null);
        arroneAttr[n48] = new OneAttr(SchemaSymbols.ATT_XML_LANG, 8, ATTIDX_XML_LANG, null);
        arroneAttr[n49] = new OneAttr(SchemaSymbols.ATT_XPATH, 6, ATTIDX_XPATH, null);
        arroneAttr[n50] = new OneAttr(SchemaSymbols.ATT_XPATH, 7, ATTIDX_XPATH, null);
        Container container = Container.getContainer(5);
        container.put(SchemaSymbols.ATT_DEFAULT, arroneAttr[n10]);
        container.put(SchemaSymbols.ATT_FIXED, arroneAttr[n15]);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_NAME, arroneAttr[n27]);
        container.put(SchemaSymbols.ATT_TYPE, arroneAttr[n41]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_ATTRIBUTE, container);
        container = Container.getContainer(7);
        container.put(SchemaSymbols.ATT_DEFAULT, arroneAttr[n10]);
        container.put(SchemaSymbols.ATT_FIXED, arroneAttr[n15]);
        container.put(SchemaSymbols.ATT_FORM, arroneAttr[n17]);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_NAME, arroneAttr[n27]);
        container.put(SchemaSymbols.ATT_TYPE, arroneAttr[n41]);
        container.put(SchemaSymbols.ATT_USE, arroneAttr[n42]);
        fEleAttrsMapL.put("attribute_n", container);
        container = Container.getContainer(5);
        container.put(SchemaSymbols.ATT_DEFAULT, arroneAttr[n10]);
        container.put(SchemaSymbols.ATT_FIXED, arroneAttr[n15]);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_REF, arroneAttr[n33]);
        container.put(SchemaSymbols.ATT_USE, arroneAttr[n42]);
        fEleAttrsMapL.put("attribute_r", container);
        container = Container.getContainer(10);
        container.put(SchemaSymbols.ATT_ABSTRACT, arroneAttr[n3]);
        container.put(SchemaSymbols.ATT_BLOCK, arroneAttr[n7]);
        container.put(SchemaSymbols.ATT_DEFAULT, arroneAttr[n10]);
        container.put(SchemaSymbols.ATT_FINAL, arroneAttr[n12]);
        container.put(SchemaSymbols.ATT_FIXED, arroneAttr[n15]);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_NAME, arroneAttr[n27]);
        container.put(SchemaSymbols.ATT_NILLABLE, arroneAttr[n30]);
        container.put(SchemaSymbols.ATT_SUBSTITUTIONGROUP, arroneAttr[n38]);
        container.put(SchemaSymbols.ATT_TYPE, arroneAttr[n41]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_ELEMENT, container);
        container = Container.getContainer(10);
        container.put(SchemaSymbols.ATT_BLOCK, arroneAttr[n7]);
        container.put(SchemaSymbols.ATT_DEFAULT, arroneAttr[n10]);
        container.put(SchemaSymbols.ATT_FIXED, arroneAttr[n15]);
        container.put(SchemaSymbols.ATT_FORM, arroneAttr[n17]);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_MAXOCCURS, arroneAttr[n20]);
        container.put(SchemaSymbols.ATT_MINOCCURS, arroneAttr[n23]);
        container.put(SchemaSymbols.ATT_NAME, arroneAttr[n27]);
        container.put(SchemaSymbols.ATT_NILLABLE, arroneAttr[n30]);
        container.put(SchemaSymbols.ATT_TYPE, arroneAttr[n41]);
        fEleAttrsMapL.put("element_n", container);
        container = Container.getContainer(4);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_MAXOCCURS, arroneAttr[n20]);
        container.put(SchemaSymbols.ATT_MINOCCURS, arroneAttr[n23]);
        container.put(SchemaSymbols.ATT_REF, arroneAttr[n33]);
        fEleAttrsMapL.put("element_r", container);
        container = Container.getContainer(6);
        container.put(SchemaSymbols.ATT_ABSTRACT, arroneAttr[n3]);
        container.put(SchemaSymbols.ATT_BLOCK, arroneAttr[n8]);
        container.put(SchemaSymbols.ATT_FINAL, arroneAttr[n12]);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_MIXED, arroneAttr[n25]);
        container.put(SchemaSymbols.ATT_NAME, arroneAttr[n27]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_COMPLEXTYPE, container);
        container = Container.getContainer(4);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_NAME, arroneAttr[n27]);
        container.put(SchemaSymbols.ATT_PUBLIC, arroneAttr[n32]);
        container.put(SchemaSymbols.ATT_SYSTEM, arroneAttr[n39]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_NOTATION, container);
        container = Container.getContainer(2);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_MIXED, arroneAttr[n25]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_COMPLEXTYPE, container);
        container = Container.getContainer(1);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_SIMPLECONTENT, container);
        container = Container.getContainer(2);
        container.put(SchemaSymbols.ATT_BASE, arroneAttr[n6]);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_RESTRICTION, container);
        container = Container.getContainer(2);
        container.put(SchemaSymbols.ATT_BASE, arroneAttr[n5]);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_EXTENSION, container);
        container = Container.getContainer(2);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_REF, arroneAttr[n33]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_ATTRIBUTEGROUP, container);
        container = Container.getContainer(3);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_NAMESPACE, arroneAttr[n28]);
        container.put(SchemaSymbols.ATT_PROCESSCONTENTS, arroneAttr[n31]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_ANYATTRIBUTE, container);
        container = Container.getContainer(2);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_MIXED, arroneAttr[n26]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_COMPLEXCONTENT, container);
        container = Container.getContainer(2);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_NAME, arroneAttr[n27]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_ATTRIBUTEGROUP, container);
        container = Container.getContainer(2);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_NAME, arroneAttr[n27]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_GROUP, container);
        container = Container.getContainer(4);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_MAXOCCURS, arroneAttr[n20]);
        container.put(SchemaSymbols.ATT_MINOCCURS, arroneAttr[n23]);
        container.put(SchemaSymbols.ATT_REF, arroneAttr[n33]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_GROUP, container);
        container = Container.getContainer(3);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_MAXOCCURS, arroneAttr[n21]);
        container.put(SchemaSymbols.ATT_MINOCCURS, arroneAttr[n24]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_ALL, container);
        container = Container.getContainer(3);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_MAXOCCURS, arroneAttr[n20]);
        container.put(SchemaSymbols.ATT_MINOCCURS, arroneAttr[n23]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_CHOICE, container);
        fEleAttrsMapL.put(SchemaSymbols.ELT_SEQUENCE, container);
        container = Container.getContainer(5);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_MAXOCCURS, arroneAttr[n20]);
        container.put(SchemaSymbols.ATT_MINOCCURS, arroneAttr[n23]);
        container.put(SchemaSymbols.ATT_NAMESPACE, arroneAttr[n28]);
        container.put(SchemaSymbols.ATT_PROCESSCONTENTS, arroneAttr[n31]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_ANY, container);
        container = Container.getContainer(2);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_NAME, arroneAttr[n27]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_UNIQUE, container);
        fEleAttrsMapL.put(SchemaSymbols.ELT_KEY, container);
        container = Container.getContainer(3);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_NAME, arroneAttr[n27]);
        container.put(SchemaSymbols.ATT_REFER, arroneAttr[n34]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_KEYREF, container);
        container = Container.getContainer(2);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_XPATH, arroneAttr[n49]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_SELECTOR, container);
        container = Container.getContainer(2);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_XPATH, arroneAttr[n50]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_FIELD, container);
        container = Container.getContainer(1);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_ANNOTATION, container);
        fEleAttrsMapL.put(SchemaSymbols.ELT_ANNOTATION, container);
        container = Container.getContainer(1);
        container.put(SchemaSymbols.ATT_SOURCE, arroneAttr[n37]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_APPINFO, container);
        fEleAttrsMapL.put(SchemaSymbols.ELT_APPINFO, container);
        container = Container.getContainer(2);
        container.put(SchemaSymbols.ATT_SOURCE, arroneAttr[n37]);
        container.put(SchemaSymbols.ATT_XML_LANG, arroneAttr[n48]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_DOCUMENTATION, container);
        fEleAttrsMapL.put(SchemaSymbols.ELT_DOCUMENTATION, container);
        container = Container.getContainer(3);
        container.put(SchemaSymbols.ATT_FINAL, arroneAttr[n13]);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_NAME, arroneAttr[n27]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_SIMPLETYPE, container);
        container = Container.getContainer(2);
        container.put(SchemaSymbols.ATT_FINAL, arroneAttr[n13]);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_SIMPLETYPE, container);
        container = Container.getContainer(2);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_ITEMTYPE, arroneAttr[n19]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_LIST, container);
        container = Container.getContainer(2);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_MEMBERTYPES, arroneAttr[n22]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_UNION, container);
        container = Container.getContainer(8);
        container.put(SchemaSymbols.ATT_ATTRIBUTEFORMDEFAULT, arroneAttr[n4]);
        container.put(SchemaSymbols.ATT_BLOCKDEFAULT, arroneAttr[n9]);
        container.put(SchemaSymbols.ATT_ELEMENTFORMDEFAULT, arroneAttr[n11]);
        container.put(SchemaSymbols.ATT_FINALDEFAULT, arroneAttr[n14]);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_TARGETNAMESPACE, arroneAttr[n40]);
        container.put(SchemaSymbols.ATT_VERSION, arroneAttr[n47]);
        container.put(SchemaSymbols.ATT_XML_LANG, arroneAttr[n48]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_SCHEMA, container);
        container = Container.getContainer(2);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_SCHEMALOCATION, arroneAttr[n35]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_INCLUDE, container);
        fEleAttrsMapG.put(SchemaSymbols.ELT_REDEFINE, container);
        container = Container.getContainer(3);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_NAMESPACE, arroneAttr[n29]);
        container.put(SchemaSymbols.ATT_SCHEMALOCATION, arroneAttr[n36]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_IMPORT, container);
        container = Container.getContainer(3);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_VALUE, arroneAttr[n43]);
        container.put(SchemaSymbols.ATT_FIXED, arroneAttr[n16]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_LENGTH, container);
        fEleAttrsMapL.put(SchemaSymbols.ELT_MINLENGTH, container);
        fEleAttrsMapL.put(SchemaSymbols.ELT_MAXLENGTH, container);
        fEleAttrsMapL.put(SchemaSymbols.ELT_FRACTIONDIGITS, container);
        container = Container.getContainer(3);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_VALUE, arroneAttr[n44]);
        container.put(SchemaSymbols.ATT_FIXED, arroneAttr[n16]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_TOTALDIGITS, container);
        container = Container.getContainer(2);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_VALUE, arroneAttr[n45]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_PATTERN, container);
        container = Container.getContainer(2);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_VALUE, arroneAttr[n45]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_ENUMERATION, container);
        container = Container.getContainer(3);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_VALUE, arroneAttr[n46]);
        container.put(SchemaSymbols.ATT_FIXED, arroneAttr[n16]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_WHITESPACE, container);
        container = Container.getContainer(3);
        container.put(SchemaSymbols.ATT_ID, arroneAttr[n18]);
        container.put(SchemaSymbols.ATT_VALUE, arroneAttr[n45]);
        container.put(SchemaSymbols.ATT_FIXED, arroneAttr[n16]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_MAXINCLUSIVE, container);
        fEleAttrsMapL.put(SchemaSymbols.ELT_MAXEXCLUSIVE, container);
        fEleAttrsMapL.put(SchemaSymbols.ELT_MININCLUSIVE, container);
        fEleAttrsMapL.put(SchemaSymbols.ELT_MINEXCLUSIVE, container);
        fSeenTemp = new boolean[ATTIDX_COUNT];
        fTempArray = new Object[ATTIDX_COUNT];
    }
}

