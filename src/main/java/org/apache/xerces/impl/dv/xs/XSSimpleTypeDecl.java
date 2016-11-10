/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.xs;

import java.util.AbstractList;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.xerces.impl.dv.DatatypeException;
import org.apache.xerces.impl.dv.InvalidDatatypeFacetException;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.XSFacets;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.dv.xs.AnyAtomicDV;
import org.apache.xerces.impl.dv.xs.AnySimpleDV;
import org.apache.xerces.impl.dv.xs.AnyURIDV;
import org.apache.xerces.impl.dv.xs.Base64BinaryDV;
import org.apache.xerces.impl.dv.xs.BooleanDV;
import org.apache.xerces.impl.dv.xs.DateDV;
import org.apache.xerces.impl.dv.xs.DateTimeDV;
import org.apache.xerces.impl.dv.xs.DayDV;
import org.apache.xerces.impl.dv.xs.DayTimeDurationDV;
import org.apache.xerces.impl.dv.xs.DecimalDV;
import org.apache.xerces.impl.dv.xs.DoubleDV;
import org.apache.xerces.impl.dv.xs.DurationDV;
import org.apache.xerces.impl.dv.xs.EntityDV;
import org.apache.xerces.impl.dv.xs.FloatDV;
import org.apache.xerces.impl.dv.xs.HexBinaryDV;
import org.apache.xerces.impl.dv.xs.IDDV;
import org.apache.xerces.impl.dv.xs.IDREFDV;
import org.apache.xerces.impl.dv.xs.IntegerDV;
import org.apache.xerces.impl.dv.xs.ListDV;
import org.apache.xerces.impl.dv.xs.MonthDV;
import org.apache.xerces.impl.dv.xs.MonthDayDV;
import org.apache.xerces.impl.dv.xs.PrecisionDecimalDV;
import org.apache.xerces.impl.dv.xs.QNameDV;
import org.apache.xerces.impl.dv.xs.StringDV;
import org.apache.xerces.impl.dv.xs.TimeDV;
import org.apache.xerces.impl.dv.xs.TypeValidator;
import org.apache.xerces.impl.dv.xs.UnionDV;
import org.apache.xerces.impl.dv.xs.XSSimpleTypeDelegate;
import org.apache.xerces.impl.dv.xs.YearDV;
import org.apache.xerces.impl.dv.xs.YearMonthDV;
import org.apache.xerces.impl.dv.xs.YearMonthDurationDV;
import org.apache.xerces.impl.xpath.regex.RegularExpression;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.util.ShortListImpl;
import org.apache.xerces.impl.xs.util.StringListImpl;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSFacet;
import org.apache.xerces.xs.XSMultiValueFacet;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.xs.datatypes.ObjectList;
import org.w3c.dom.TypeInfo;

public class XSSimpleTypeDecl
implements XSSimpleType,
TypeInfo {
    protected static final short DV_STRING = 1;
    protected static final short DV_BOOLEAN = 2;
    protected static final short DV_DECIMAL = 3;
    protected static final short DV_FLOAT = 4;
    protected static final short DV_DOUBLE = 5;
    protected static final short DV_DURATION = 6;
    protected static final short DV_DATETIME = 7;
    protected static final short DV_TIME = 8;
    protected static final short DV_DATE = 9;
    protected static final short DV_GYEARMONTH = 10;
    protected static final short DV_GYEAR = 11;
    protected static final short DV_GMONTHDAY = 12;
    protected static final short DV_GDAY = 13;
    protected static final short DV_GMONTH = 14;
    protected static final short DV_HEXBINARY = 15;
    protected static final short DV_BASE64BINARY = 16;
    protected static final short DV_ANYURI = 17;
    protected static final short DV_QNAME = 18;
    protected static final short DV_PRECISIONDECIMAL = 19;
    protected static final short DV_NOTATION = 20;
    protected static final short DV_ANYSIMPLETYPE = 0;
    protected static final short DV_ID = 21;
    protected static final short DV_IDREF = 22;
    protected static final short DV_ENTITY = 23;
    protected static final short DV_INTEGER = 24;
    protected static final short DV_LIST = 25;
    protected static final short DV_UNION = 26;
    protected static final short DV_YEARMONTHDURATION = 27;
    protected static final short DV_DAYTIMEDURATION = 28;
    protected static final short DV_ANYATOMICTYPE = 29;
    private static final TypeValidator[] gDVs = new TypeValidator[]{new AnySimpleDV(), new StringDV(), new BooleanDV(), new DecimalDV(), new FloatDV(), new DoubleDV(), new DurationDV(), new DateTimeDV(), new TimeDV(), new DateDV(), new YearMonthDV(), new YearDV(), new MonthDayDV(), new DayDV(), new MonthDV(), new HexBinaryDV(), new Base64BinaryDV(), new AnyURIDV(), new QNameDV(), new PrecisionDecimalDV(), new QNameDV(), new IDDV(), new IDREFDV(), new EntityDV(), new IntegerDV(), new ListDV(), new UnionDV(), new YearMonthDurationDV(), new DayTimeDurationDV(), new AnyAtomicDV()};
    static final short NORMALIZE_NONE = 0;
    static final short NORMALIZE_TRIM = 1;
    static final short NORMALIZE_FULL = 2;
    static final short[] fDVNormalizeType = new short[]{0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 1, 1, 0};
    static final short SPECIAL_PATTERN_NONE = 0;
    static final short SPECIAL_PATTERN_NMTOKEN = 1;
    static final short SPECIAL_PATTERN_NAME = 2;
    static final short SPECIAL_PATTERN_NCNAME = 3;
    static final String[] SPECIAL_PATTERN_STRING = new String[]{"NONE", "NMTOKEN", "Name", "NCName"};
    static final String[] WS_FACET_STRING = new String[]{"preserve", "replace", "collapse"};
    static final String URI_SCHEMAFORSCHEMA = "http://www.w3.org/2001/XMLSchema";
    static final String ANY_TYPE = "anyType";
    public static final short YEARMONTHDURATION_DT = 46;
    public static final short DAYTIMEDURATION_DT = 47;
    public static final short PRECISIONDECIMAL_DT = 48;
    public static final short ANYATOMICTYPE_DT = 49;
    static final int DERIVATION_ANY = 0;
    static final int DERIVATION_RESTRICTION = 1;
    static final int DERIVATION_EXTENSION = 2;
    static final int DERIVATION_UNION = 4;
    static final int DERIVATION_LIST = 8;
    static final ValidationContext fEmptyContext = new ValidationContext(){

        public boolean needFacetChecking() {
            return true;
        }

        public boolean needExtraChecking() {
            return false;
        }

        public boolean needToNormalize() {
            return true;
        }

        public boolean useNamespaces() {
            return true;
        }

        public boolean isEntityDeclared(String string) {
            return false;
        }

        public boolean isEntityUnparsed(String string) {
            return false;
        }

        public boolean isIdDeclared(String string) {
            return false;
        }

        public void addId(String string) {
        }

        public void addIdRef(String string) {
        }

        public String getSymbol(String string) {
            return string.intern();
        }

        public String getURI(String string) {
            return null;
        }

        public Locale getLocale() {
            return Locale.getDefault();
        }
    };
    private TypeValidator[] fDVs = gDVs;
    private boolean fIsImmutable = false;
    private XSSimpleTypeDecl fItemType;
    private XSSimpleTypeDecl[] fMemberTypes;
    private short fBuiltInKind;
    private String fTypeName;
    private String fTargetNamespace;
    private short fFinalSet = 0;
    private XSSimpleTypeDecl fBase;
    private short fVariety = -1;
    private short fValidationDV = -1;
    private short fFacetsDefined = 0;
    private short fFixedFacet = 0;
    private short fWhiteSpace = 0;
    private int fLength = -1;
    private int fMinLength = -1;
    private int fMaxLength = -1;
    private int fTotalDigits = -1;
    private int fFractionDigits = -1;
    private Vector fPattern;
    private Vector fPatternStr;
    private Vector fEnumeration;
    private short[] fEnumerationType;
    private ShortList[] fEnumerationItemType;
    private ShortList fEnumerationTypeList;
    private ObjectList fEnumerationItemTypeList;
    private StringList fLexicalPattern;
    private StringList fLexicalEnumeration;
    private ObjectList fActualEnumeration;
    private Object fMaxInclusive;
    private Object fMaxExclusive;
    private Object fMinExclusive;
    private Object fMinInclusive;
    public XSAnnotation lengthAnnotation;
    public XSAnnotation minLengthAnnotation;
    public XSAnnotation maxLengthAnnotation;
    public XSAnnotation whiteSpaceAnnotation;
    public XSAnnotation totalDigitsAnnotation;
    public XSAnnotation fractionDigitsAnnotation;
    public XSObjectListImpl patternAnnotations;
    public XSObjectList enumerationAnnotations;
    public XSAnnotation maxInclusiveAnnotation;
    public XSAnnotation maxExclusiveAnnotation;
    public XSAnnotation minInclusiveAnnotation;
    public XSAnnotation minExclusiveAnnotation;
    private XSObjectListImpl fFacets;
    private XSObjectListImpl fMultiValueFacets;
    private XSObjectList fAnnotations = null;
    private short fPatternType = 0;
    private short fOrdered;
    private boolean fFinite;
    private boolean fBounded;
    private boolean fNumeric;
    private XSNamespaceItem fNamespaceItem = null;
    static final XSSimpleTypeDecl fAnySimpleType = new XSSimpleTypeDecl(null, "anySimpleType", 0, 0, false, true, false, true, 1);
    static final XSSimpleTypeDecl fAnyAtomicType = new XSSimpleTypeDecl(fAnySimpleType, "anyAtomicType", 29, 0, false, true, false, true, 49);
    static final ValidationContext fDummyContext = new ValidationContext(){

        public boolean needFacetChecking() {
            return true;
        }

        public boolean needExtraChecking() {
            return false;
        }

        public boolean needToNormalize() {
            return false;
        }

        public boolean useNamespaces() {
            return true;
        }

        public boolean isEntityDeclared(String string) {
            return false;
        }

        public boolean isEntityUnparsed(String string) {
            return false;
        }

        public boolean isIdDeclared(String string) {
            return false;
        }

        public void addId(String string) {
        }

        public void addIdRef(String string) {
        }

        public String getSymbol(String string) {
            return string.intern();
        }

        public String getURI(String string) {
            return null;
        }

        public Locale getLocale() {
            return Locale.getDefault();
        }
    };
    private boolean fAnonymous = false;

    protected static TypeValidator[] getGDVs() {
        return (TypeValidator[])gDVs.clone();
    }

    protected void setDVs(TypeValidator[] arrtypeValidator) {
        this.fDVs = arrtypeValidator;
    }

    public XSSimpleTypeDecl() {
    }

    protected XSSimpleTypeDecl(XSSimpleTypeDecl xSSimpleTypeDecl, String string, short s2, short s3, boolean bl, boolean bl2, boolean bl3, boolean bl4, short s4) {
        this.fIsImmutable = bl4;
        this.fBase = xSSimpleTypeDecl;
        this.fTypeName = string;
        this.fTargetNamespace = "http://www.w3.org/2001/XMLSchema";
        this.fVariety = 1;
        this.fValidationDV = s2;
        this.fFacetsDefined = 16;
        if (s2 == 1) {
            this.fWhiteSpace = 0;
        } else {
            this.fWhiteSpace = 2;
            this.fFixedFacet = 16;
        }
        this.fOrdered = s3;
        this.fBounded = bl;
        this.fFinite = bl2;
        this.fNumeric = bl3;
        this.fAnnotations = null;
        this.fBuiltInKind = s4;
    }

    protected XSSimpleTypeDecl(XSSimpleTypeDecl xSSimpleTypeDecl, String string, String string2, short s2, boolean bl, XSObjectList xSObjectList, short s3) {
        this(xSSimpleTypeDecl, string, string2, s2, bl, xSObjectList);
        this.fBuiltInKind = s3;
    }

    protected XSSimpleTypeDecl(XSSimpleTypeDecl xSSimpleTypeDecl, String string, String string2, short s2, boolean bl, XSObjectList xSObjectList) {
        this.fBase = xSSimpleTypeDecl;
        this.fTypeName = string;
        this.fTargetNamespace = string2;
        this.fFinalSet = s2;
        this.fAnnotations = xSObjectList;
        this.fVariety = this.fBase.fVariety;
        this.fValidationDV = this.fBase.fValidationDV;
        switch (this.fVariety) {
            case 1: {
                break;
            }
            case 2: {
                this.fItemType = this.fBase.fItemType;
                break;
            }
            case 3: {
                this.fMemberTypes = this.fBase.fMemberTypes;
            }
        }
        this.fLength = this.fBase.fLength;
        this.fMinLength = this.fBase.fMinLength;
        this.fMaxLength = this.fBase.fMaxLength;
        this.fPattern = this.fBase.fPattern;
        this.fPatternStr = this.fBase.fPatternStr;
        this.fEnumeration = this.fBase.fEnumeration;
        this.fEnumerationType = this.fBase.fEnumerationType;
        this.fEnumerationItemType = this.fBase.fEnumerationItemType;
        this.fWhiteSpace = this.fBase.fWhiteSpace;
        this.fMaxExclusive = this.fBase.fMaxExclusive;
        this.fMaxInclusive = this.fBase.fMaxInclusive;
        this.fMinExclusive = this.fBase.fMinExclusive;
        this.fMinInclusive = this.fBase.fMinInclusive;
        this.fTotalDigits = this.fBase.fTotalDigits;
        this.fFractionDigits = this.fBase.fFractionDigits;
        this.fPatternType = this.fBase.fPatternType;
        this.fFixedFacet = this.fBase.fFixedFacet;
        this.fFacetsDefined = this.fBase.fFacetsDefined;
        this.lengthAnnotation = this.fBase.lengthAnnotation;
        this.minLengthAnnotation = this.fBase.minLengthAnnotation;
        this.maxLengthAnnotation = this.fBase.maxLengthAnnotation;
        this.patternAnnotations = this.fBase.patternAnnotations;
        this.enumerationAnnotations = this.fBase.enumerationAnnotations;
        this.whiteSpaceAnnotation = this.fBase.whiteSpaceAnnotation;
        this.maxExclusiveAnnotation = this.fBase.maxExclusiveAnnotation;
        this.maxInclusiveAnnotation = this.fBase.maxInclusiveAnnotation;
        this.minExclusiveAnnotation = this.fBase.minExclusiveAnnotation;
        this.minInclusiveAnnotation = this.fBase.minInclusiveAnnotation;
        this.totalDigitsAnnotation = this.fBase.totalDigitsAnnotation;
        this.fractionDigitsAnnotation = this.fBase.fractionDigitsAnnotation;
        this.calcFundamentalFacets();
        this.fIsImmutable = bl;
        this.fBuiltInKind = xSSimpleTypeDecl.fBuiltInKind;
    }

    protected XSSimpleTypeDecl(String string, String string2, short s2, XSSimpleTypeDecl xSSimpleTypeDecl, boolean bl, XSObjectList xSObjectList) {
        this.fBase = fAnySimpleType;
        this.fTypeName = string;
        this.fTargetNamespace = string2;
        this.fFinalSet = s2;
        this.fAnnotations = xSObjectList;
        this.fVariety = 2;
        this.fItemType = xSSimpleTypeDecl;
        this.fValidationDV = 25;
        this.fFacetsDefined = 16;
        this.fFixedFacet = 16;
        this.fWhiteSpace = 2;
        this.calcFundamentalFacets();
        this.fIsImmutable = bl;
        this.fBuiltInKind = 44;
    }

    protected XSSimpleTypeDecl(String string, String string2, short s2, XSSimpleTypeDecl[] arrxSSimpleTypeDecl, XSObjectList xSObjectList) {
        this.fBase = fAnySimpleType;
        this.fTypeName = string;
        this.fTargetNamespace = string2;
        this.fFinalSet = s2;
        this.fAnnotations = xSObjectList;
        this.fVariety = 3;
        this.fMemberTypes = arrxSSimpleTypeDecl;
        this.fValidationDV = 26;
        this.fFacetsDefined = 16;
        this.fWhiteSpace = 2;
        this.calcFundamentalFacets();
        this.fIsImmutable = false;
        this.fBuiltInKind = 45;
    }

    protected XSSimpleTypeDecl setRestrictionValues(XSSimpleTypeDecl xSSimpleTypeDecl, String string, String string2, short s2, XSObjectList xSObjectList) {
        if (this.fIsImmutable) {
            return null;
        }
        this.fBase = xSSimpleTypeDecl;
        this.fAnonymous = false;
        this.fTypeName = string;
        this.fTargetNamespace = string2;
        this.fFinalSet = s2;
        this.fAnnotations = xSObjectList;
        this.fVariety = this.fBase.fVariety;
        this.fValidationDV = this.fBase.fValidationDV;
        switch (this.fVariety) {
            case 1: {
                break;
            }
            case 2: {
                this.fItemType = this.fBase.fItemType;
                break;
            }
            case 3: {
                this.fMemberTypes = this.fBase.fMemberTypes;
            }
        }
        this.fLength = this.fBase.fLength;
        this.fMinLength = this.fBase.fMinLength;
        this.fMaxLength = this.fBase.fMaxLength;
        this.fPattern = this.fBase.fPattern;
        this.fPatternStr = this.fBase.fPatternStr;
        this.fEnumeration = this.fBase.fEnumeration;
        this.fEnumerationType = this.fBase.fEnumerationType;
        this.fEnumerationItemType = this.fBase.fEnumerationItemType;
        this.fWhiteSpace = this.fBase.fWhiteSpace;
        this.fMaxExclusive = this.fBase.fMaxExclusive;
        this.fMaxInclusive = this.fBase.fMaxInclusive;
        this.fMinExclusive = this.fBase.fMinExclusive;
        this.fMinInclusive = this.fBase.fMinInclusive;
        this.fTotalDigits = this.fBase.fTotalDigits;
        this.fFractionDigits = this.fBase.fFractionDigits;
        this.fPatternType = this.fBase.fPatternType;
        this.fFixedFacet = this.fBase.fFixedFacet;
        this.fFacetsDefined = this.fBase.fFacetsDefined;
        this.calcFundamentalFacets();
        this.fBuiltInKind = xSSimpleTypeDecl.fBuiltInKind;
        return this;
    }

    protected XSSimpleTypeDecl setListValues(String string, String string2, short s2, XSSimpleTypeDecl xSSimpleTypeDecl, XSObjectList xSObjectList) {
        if (this.fIsImmutable) {
            return null;
        }
        this.fBase = fAnySimpleType;
        this.fAnonymous = false;
        this.fTypeName = string;
        this.fTargetNamespace = string2;
        this.fFinalSet = s2;
        this.fAnnotations = xSObjectList;
        this.fVariety = 2;
        this.fItemType = xSSimpleTypeDecl;
        this.fValidationDV = 25;
        this.fFacetsDefined = 16;
        this.fFixedFacet = 16;
        this.fWhiteSpace = 2;
        this.calcFundamentalFacets();
        this.fBuiltInKind = 44;
        return this;
    }

    protected XSSimpleTypeDecl setUnionValues(String string, String string2, short s2, XSSimpleTypeDecl[] arrxSSimpleTypeDecl, XSObjectList xSObjectList) {
        if (this.fIsImmutable) {
            return null;
        }
        this.fBase = fAnySimpleType;
        this.fAnonymous = false;
        this.fTypeName = string;
        this.fTargetNamespace = string2;
        this.fFinalSet = s2;
        this.fAnnotations = xSObjectList;
        this.fVariety = 3;
        this.fMemberTypes = arrxSSimpleTypeDecl;
        this.fValidationDV = 26;
        this.fFacetsDefined = 16;
        this.fWhiteSpace = 2;
        this.calcFundamentalFacets();
        this.fBuiltInKind = 45;
        return this;
    }

    public short getType() {
        return 3;
    }

    public short getTypeCategory() {
        return 16;
    }

    public String getName() {
        return this.getAnonymous() ? null : this.fTypeName;
    }

    public String getTypeName() {
        return this.fTypeName;
    }

    public String getNamespace() {
        return this.fTargetNamespace;
    }

    public short getFinal() {
        return this.fFinalSet;
    }

    public boolean isFinal(short s2) {
        return (this.fFinalSet & s2) != 0;
    }

    public XSTypeDefinition getBaseType() {
        return this.fBase;
    }

    public boolean getAnonymous() {
        return this.fAnonymous || this.fTypeName == null;
    }

    public short getVariety() {
        return this.fValidationDV == 0 ? 0 : this.fVariety;
    }

    public boolean isIDType() {
        switch (this.fVariety) {
            case 1: {
                return this.fValidationDV == 21;
            }
            case 2: {
                return this.fItemType.isIDType();
            }
            case 3: {
                int n2 = 0;
                while (n2 < this.fMemberTypes.length) {
                    if (this.fMemberTypes[n2].isIDType()) {
                        return true;
                    }
                    ++n2;
                }
                break block0;
            }
        }
        return false;
    }

    public short getWhitespace() throws DatatypeException {
        if (this.fVariety == 3) {
            throw new DatatypeException("dt-whitespace", new Object[]{this.fTypeName});
        }
        return this.fWhiteSpace;
    }

    public short getPrimitiveKind() {
        if (this.fVariety == 1 && this.fValidationDV != 0) {
            if (this.fValidationDV == 21 || this.fValidationDV == 22 || this.fValidationDV == 23) {
                return 1;
            }
            if (this.fValidationDV == 24) {
                return 3;
            }
            return this.fValidationDV;
        }
        return 0;
    }

    public short getBuiltInKind() {
        return this.fBuiltInKind;
    }

    public XSSimpleTypeDefinition getPrimitiveType() {
        if (this.fVariety == 1 && this.fValidationDV != 0) {
            XSSimpleTypeDecl xSSimpleTypeDecl = this;
            while (xSSimpleTypeDecl.fBase != fAnySimpleType) {
                xSSimpleTypeDecl = xSSimpleTypeDecl.fBase;
            }
            return xSSimpleTypeDecl;
        }
        return null;
    }

    public XSSimpleTypeDefinition getItemType() {
        if (this.fVariety == 2) {
            return this.fItemType;
        }
        return null;
    }

    public XSObjectList getMemberTypes() {
        if (this.fVariety == 3) {
            return new XSObjectListImpl(this.fMemberTypes, this.fMemberTypes.length);
        }
        return XSObjectListImpl.EMPTY_LIST;
    }

    public void applyFacets(XSFacets xSFacets, short s2, short s3, ValidationContext validationContext) throws InvalidDatatypeFacetException {
        if (validationContext == null) {
            validationContext = fEmptyContext;
        }
        this.applyFacets(xSFacets, s2, s3, 0, validationContext);
    }

    void applyFacets1(XSFacets xSFacets, short s2, short s3) {
        try {
            this.applyFacets(xSFacets, s2, s3, 0, fDummyContext);
        }
        catch (InvalidDatatypeFacetException invalidDatatypeFacetException) {
            throw new RuntimeException("internal error");
        }
        this.fIsImmutable = true;
    }

    void applyFacets1(XSFacets xSFacets, short s2, short s3, short s4) {
        try {
            this.applyFacets(xSFacets, s2, s3, s4, fDummyContext);
        }
        catch (InvalidDatatypeFacetException invalidDatatypeFacetException) {
            throw new RuntimeException("internal error");
        }
        this.fIsImmutable = true;
    }

    void applyFacets(XSFacets xSFacets, short s2, short s3, short s4, ValidationContext validationContext) throws InvalidDatatypeFacetException {
        if (this.fIsImmutable) {
            return;
        }
        ValidatedInfo validatedInfo = new ValidatedInfo();
        this.fFacetsDefined = 0;
        this.fFixedFacet = 0;
        int n2 = 0;
        short s5 = this.fDVs[this.fValidationDV].getAllowedFacets();
        if ((s2 & 1) != 0) {
            if ((s5 & 1) == 0) {
                this.reportError("cos-applicable-facets", new Object[]{"length", this.fTypeName});
            } else {
                this.fLength = xSFacets.length;
                this.lengthAnnotation = xSFacets.lengthAnnotation;
                this.fFacetsDefined = (short)(this.fFacetsDefined | 1);
                if ((s3 & 1) != 0) {
                    this.fFixedFacet = (short)(this.fFixedFacet | 1);
                }
            }
        }
        if ((s2 & 2) != 0) {
            if ((s5 & 2) == 0) {
                this.reportError("cos-applicable-facets", new Object[]{"minLength", this.fTypeName});
            } else {
                this.fMinLength = xSFacets.minLength;
                this.minLengthAnnotation = xSFacets.minLengthAnnotation;
                this.fFacetsDefined = (short)(this.fFacetsDefined | 2);
                if ((s3 & 2) != 0) {
                    this.fFixedFacet = (short)(this.fFixedFacet | 2);
                }
            }
        }
        if ((s2 & 4) != 0) {
            if ((s5 & 4) == 0) {
                this.reportError("cos-applicable-facets", new Object[]{"maxLength", this.fTypeName});
            } else {
                this.fMaxLength = xSFacets.maxLength;
                this.maxLengthAnnotation = xSFacets.maxLengthAnnotation;
                this.fFacetsDefined = (short)(this.fFacetsDefined | 4);
                if ((s3 & 4) != 0) {
                    this.fFixedFacet = (short)(this.fFixedFacet | 4);
                }
            }
        }
        if ((s2 & 8) != 0) {
            if ((s5 & 8) == 0) {
                this.reportError("cos-applicable-facets", new Object[]{"pattern", this.fTypeName});
            } else {
                void invalidDatatypeValueException;
                this.patternAnnotations = xSFacets.patternAnnotations;
                Object regularExpression = null;
                try {
                    RegularExpression invalidDatatypeValueException2 = new RegularExpression(xSFacets.pattern, "X", validationContext.getLocale());
                }
                catch (Exception exception) {
                    this.reportError("InvalidRegex", new Object[]{xSFacets.pattern, exception.getLocalizedMessage()});
                }
                if (invalidDatatypeValueException != null) {
                    this.fPattern = new Vector();
                    this.fPattern.addElement(invalidDatatypeValueException);
                    this.fPatternStr = new Vector();
                    this.fPatternStr.addElement(xSFacets.pattern);
                    this.fFacetsDefined = (short)(this.fFacetsDefined | 8);
                    if ((s3 & 8) != 0) {
                        this.fFixedFacet = (short)(this.fFixedFacet | 8);
                    }
                }
            }
        }
        if ((s2 & 16) != 0) {
            if ((s5 & 16) == 0) {
                this.reportError("cos-applicable-facets", new Object[]{"whiteSpace", this.fTypeName});
            } else {
                this.fWhiteSpace = xSFacets.whiteSpace;
                this.whiteSpaceAnnotation = xSFacets.whiteSpaceAnnotation;
                this.fFacetsDefined = (short)(this.fFacetsDefined | 16);
                if ((s3 & 16) != 0) {
                    this.fFixedFacet = (short)(this.fFixedFacet | 16);
                }
            }
        }
        if ((s2 & 2048) != 0) {
            if ((s5 & 2048) == 0) {
                this.reportError("cos-applicable-facets", new Object[]{"enumeration", this.fTypeName});
            } else {
                this.fEnumeration = new Vector();
                Vector bl = xSFacets.enumeration;
                this.fEnumerationType = new short[bl.size()];
                this.fEnumerationItemType = new ShortList[bl.size()];
                Vector vector = xSFacets.enumNSDecls;
                ValidationContextImpl validationContextImpl = new ValidationContextImpl(validationContext);
                this.enumerationAnnotations = xSFacets.enumAnnotations;
                int n3 = 0;
                while (n3 < bl.size()) {
                    if (vector != null) {
                        validationContextImpl.setNSContext((NamespaceContext)vector.elementAt(n3));
                    }
                    try {
                        ValidatedInfo validatedInfo2 = this.getActualEnumValue((String)bl.elementAt(n3), validationContextImpl, validatedInfo);
                        this.fEnumeration.addElement(validatedInfo2.actualValue);
                        this.fEnumerationType[n3] = validatedInfo2.actualValueType;
                        this.fEnumerationItemType[n3] = validatedInfo2.itemValueTypes;
                    }
                    catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                        this.reportError("enumeration-valid-restriction", new Object[]{bl.elementAt(n3), this.getBaseType().getName()});
                    }
                    ++n3;
                }
                this.fFacetsDefined = (short)(this.fFacetsDefined | 2048);
                if ((s3 & 2048) != 0) {
                    this.fFixedFacet = (short)(this.fFixedFacet | 2048);
                }
            }
        }
        if ((s2 & 32) != 0) {
            if ((s5 & 32) == 0) {
                this.reportError("cos-applicable-facets", new Object[]{"maxInclusive", this.fTypeName});
            } else {
                this.maxInclusiveAnnotation = xSFacets.maxInclusiveAnnotation;
                try {
                    this.fMaxInclusive = this.fBase.getActualValue(xSFacets.maxInclusive, validationContext, validatedInfo, true);
                    this.fFacetsDefined = (short)(this.fFacetsDefined | 32);
                    if ((s3 & 32) != 0) {
                        this.fFixedFacet = (short)(this.fFixedFacet | 32);
                    }
                }
                catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                    this.reportError(invalidDatatypeValueException.getKey(), invalidDatatypeValueException.getArgs());
                    this.reportError("FacetValueFromBase", new Object[]{this.fTypeName, xSFacets.maxInclusive, "maxInclusive", this.fBase.getName()});
                }
                if ((this.fBase.fFacetsDefined & 32) != 0 && (this.fBase.fFixedFacet & 32) != 0 && this.fDVs[this.fValidationDV].compare(this.fMaxInclusive, this.fBase.fMaxInclusive) != 0) {
                    this.reportError("FixedFacetValue", new Object[]{"maxInclusive", this.fMaxInclusive, this.fBase.fMaxInclusive, this.fTypeName});
                }
                try {
                    this.fBase.validate(validationContext, validatedInfo);
                }
                catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                    this.reportError(invalidDatatypeValueException.getKey(), invalidDatatypeValueException.getArgs());
                    this.reportError("FacetValueFromBase", new Object[]{this.fTypeName, xSFacets.maxInclusive, "maxInclusive", this.fBase.getName()});
                }
            }
        }
        boolean bl = true;
        if ((s2 & 64) != 0) {
            if ((s5 & 64) == 0) {
                this.reportError("cos-applicable-facets", new Object[]{"maxExclusive", this.fTypeName});
            } else {
                boolean bl2;
                this.maxExclusiveAnnotation = xSFacets.maxExclusiveAnnotation;
                try {
                    this.fMaxExclusive = this.fBase.getActualValue(xSFacets.maxExclusive, validationContext, validatedInfo, true);
                    this.fFacetsDefined = (short)(this.fFacetsDefined | 64);
                    if ((s3 & 64) != 0) {
                        this.fFixedFacet = (short)(this.fFixedFacet | 64);
                    }
                }
                catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                    this.reportError(invalidDatatypeValueException.getKey(), invalidDatatypeValueException.getArgs());
                    this.reportError("FacetValueFromBase", new Object[]{this.fTypeName, xSFacets.maxExclusive, "maxExclusive", this.fBase.getName()});
                }
                if ((this.fBase.fFacetsDefined & 64) != 0) {
                    n2 = this.fDVs[this.fValidationDV].compare(this.fMaxExclusive, this.fBase.fMaxExclusive);
                    if ((this.fBase.fFixedFacet & 64) != 0 && n2 != 0) {
                        this.reportError("FixedFacetValue", new Object[]{"maxExclusive", xSFacets.maxExclusive, this.fBase.fMaxExclusive, this.fTypeName});
                    }
                    if (n2 == 0) {
                        bl2 = false;
                    }
                }
                if (bl2) {
                    try {
                        this.fBase.validate(validationContext, validatedInfo);
                    }
                    catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                        this.reportError(invalidDatatypeValueException.getKey(), invalidDatatypeValueException.getArgs());
                        this.reportError("FacetValueFromBase", new Object[]{this.fTypeName, xSFacets.maxExclusive, "maxExclusive", this.fBase.getName()});
                    }
                } else if ((this.fBase.fFacetsDefined & 32) != 0 && this.fDVs[this.fValidationDV].compare(this.fMaxExclusive, this.fBase.fMaxInclusive) > 0) {
                    this.reportError("maxExclusive-valid-restriction.2", new Object[]{xSFacets.maxExclusive, this.fBase.fMaxInclusive});
                }
            }
        }
        boolean bl3 = true;
        if ((s2 & 128) != 0) {
            if ((s5 & 128) == 0) {
                this.reportError("cos-applicable-facets", new Object[]{"minExclusive", this.fTypeName});
            } else {
                boolean bl4;
                this.minExclusiveAnnotation = xSFacets.minExclusiveAnnotation;
                try {
                    this.fMinExclusive = this.fBase.getActualValue(xSFacets.minExclusive, validationContext, validatedInfo, true);
                    this.fFacetsDefined = (short)(this.fFacetsDefined | 128);
                    if ((s3 & 128) != 0) {
                        this.fFixedFacet = (short)(this.fFixedFacet | 128);
                    }
                }
                catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                    this.reportError(invalidDatatypeValueException.getKey(), invalidDatatypeValueException.getArgs());
                    this.reportError("FacetValueFromBase", new Object[]{this.fTypeName, xSFacets.minExclusive, "minExclusive", this.fBase.getName()});
                }
                if ((this.fBase.fFacetsDefined & 128) != 0) {
                    n2 = this.fDVs[this.fValidationDV].compare(this.fMinExclusive, this.fBase.fMinExclusive);
                    if ((this.fBase.fFixedFacet & 128) != 0 && n2 != 0) {
                        this.reportError("FixedFacetValue", new Object[]{"minExclusive", xSFacets.minExclusive, this.fBase.fMinExclusive, this.fTypeName});
                    }
                    if (n2 == 0) {
                        bl4 = false;
                    }
                }
                if (bl4) {
                    try {
                        this.fBase.validate(validationContext, validatedInfo);
                    }
                    catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                        this.reportError(invalidDatatypeValueException.getKey(), invalidDatatypeValueException.getArgs());
                        this.reportError("FacetValueFromBase", new Object[]{this.fTypeName, xSFacets.minExclusive, "minExclusive", this.fBase.getName()});
                    }
                } else if ((this.fBase.fFacetsDefined & 256) != 0 && this.fDVs[this.fValidationDV].compare(this.fMinExclusive, this.fBase.fMinInclusive) < 0) {
                    this.reportError("minExclusive-valid-restriction.3", new Object[]{xSFacets.minExclusive, this.fBase.fMinInclusive});
                }
            }
        }
        if ((s2 & 256) != 0) {
            if ((s5 & 256) == 0) {
                this.reportError("cos-applicable-facets", new Object[]{"minInclusive", this.fTypeName});
            } else {
                this.minInclusiveAnnotation = xSFacets.minInclusiveAnnotation;
                try {
                    this.fMinInclusive = this.fBase.getActualValue(xSFacets.minInclusive, validationContext, validatedInfo, true);
                    this.fFacetsDefined = (short)(this.fFacetsDefined | 256);
                    if ((s3 & 256) != 0) {
                        this.fFixedFacet = (short)(this.fFixedFacet | 256);
                    }
                }
                catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                    this.reportError(invalidDatatypeValueException.getKey(), invalidDatatypeValueException.getArgs());
                    this.reportError("FacetValueFromBase", new Object[]{this.fTypeName, xSFacets.minInclusive, "minInclusive", this.fBase.getName()});
                }
                if ((this.fBase.fFacetsDefined & 256) != 0 && (this.fBase.fFixedFacet & 256) != 0 && this.fDVs[this.fValidationDV].compare(this.fMinInclusive, this.fBase.fMinInclusive) != 0) {
                    this.reportError("FixedFacetValue", new Object[]{"minInclusive", xSFacets.minInclusive, this.fBase.fMinInclusive, this.fTypeName});
                }
                try {
                    this.fBase.validate(validationContext, validatedInfo);
                }
                catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                    this.reportError(invalidDatatypeValueException.getKey(), invalidDatatypeValueException.getArgs());
                    this.reportError("FacetValueFromBase", new Object[]{this.fTypeName, xSFacets.minInclusive, "minInclusive", this.fBase.getName()});
                }
            }
        }
        if ((s2 & 512) != 0) {
            if ((s5 & 512) == 0) {
                this.reportError("cos-applicable-facets", new Object[]{"totalDigits", this.fTypeName});
            } else {
                this.totalDigitsAnnotation = xSFacets.totalDigitsAnnotation;
                this.fTotalDigits = xSFacets.totalDigits;
                this.fFacetsDefined = (short)(this.fFacetsDefined | 512);
                if ((s3 & 512) != 0) {
                    this.fFixedFacet = (short)(this.fFixedFacet | 512);
                }
            }
        }
        if ((s2 & 1024) != 0) {
            if ((s5 & 1024) == 0) {
                this.reportError("cos-applicable-facets", new Object[]{"fractionDigits", this.fTypeName});
            } else {
                this.fFractionDigits = xSFacets.fractionDigits;
                this.fractionDigitsAnnotation = xSFacets.fractionDigitsAnnotation;
                this.fFacetsDefined = (short)(this.fFacetsDefined | 1024);
                if ((s3 & 1024) != 0) {
                    this.fFixedFacet = (short)(this.fFixedFacet | 1024);
                }
            }
        }
        if (s4 != 0) {
            this.fPatternType = s4;
        }
        if (this.fFacetsDefined != 0) {
            if ((this.fFacetsDefined & 2) != 0 && (this.fFacetsDefined & 4) != 0 && this.fMinLength > this.fMaxLength) {
                this.reportError("minLength-less-than-equal-to-maxLength", new Object[]{Integer.toString(this.fMinLength), Integer.toString(this.fMaxLength), this.fTypeName});
            }
            if ((this.fFacetsDefined & 64) != 0 && (this.fFacetsDefined & 32) != 0) {
                this.reportError("maxInclusive-maxExclusive", new Object[]{this.fMaxInclusive, this.fMaxExclusive, this.fTypeName});
            }
            if ((this.fFacetsDefined & 128) != 0 && (this.fFacetsDefined & 256) != 0) {
                this.reportError("minInclusive-minExclusive", new Object[]{this.fMinInclusive, this.fMinExclusive, this.fTypeName});
            }
            if ((this.fFacetsDefined & 32) != 0 && (this.fFacetsDefined & 256) != 0 && (n2 = this.fDVs[this.fValidationDV].compare(this.fMinInclusive, this.fMaxInclusive)) != -1 && n2 != 0) {
                this.reportError("minInclusive-less-than-equal-to-maxInclusive", new Object[]{this.fMinInclusive, this.fMaxInclusive, this.fTypeName});
            }
            if ((this.fFacetsDefined & 64) != 0 && (this.fFacetsDefined & 128) != 0 && (n2 = this.fDVs[this.fValidationDV].compare(this.fMinExclusive, this.fMaxExclusive)) != -1 && n2 != 0) {
                this.reportError("minExclusive-less-than-equal-to-maxExclusive", new Object[]{this.fMinExclusive, this.fMaxExclusive, this.fTypeName});
            }
            if ((this.fFacetsDefined & 32) != 0 && (this.fFacetsDefined & 128) != 0 && this.fDVs[this.fValidationDV].compare(this.fMinExclusive, this.fMaxInclusive) != -1) {
                this.reportError("minExclusive-less-than-maxInclusive", new Object[]{this.fMinExclusive, this.fMaxInclusive, this.fTypeName});
            }
            if ((this.fFacetsDefined & 64) != 0 && (this.fFacetsDefined & 256) != 0 && this.fDVs[this.fValidationDV].compare(this.fMinInclusive, this.fMaxExclusive) != -1) {
                this.reportError("minInclusive-less-than-maxExclusive", new Object[]{this.fMinInclusive, this.fMaxExclusive, this.fTypeName});
            }
            if ((this.fFacetsDefined & 1024) != 0 && (this.fFacetsDefined & 512) != 0 && this.fFractionDigits > this.fTotalDigits) {
                this.reportError("fractionDigits-totalDigits", new Object[]{Integer.toString(this.fFractionDigits), Integer.toString(this.fTotalDigits), this.fTypeName});
            }
            if ((this.fFacetsDefined & 1) != 0) {
                if ((this.fBase.fFacetsDefined & 2) != 0 && this.fLength < this.fBase.fMinLength) {
                    this.reportError("length-minLength-maxLength.1.1", new Object[]{this.fTypeName, Integer.toString(this.fLength), Integer.toString(this.fBase.fMinLength)});
                }
                if ((this.fBase.fFacetsDefined & 4) != 0 && this.fLength > this.fBase.fMaxLength) {
                    this.reportError("length-minLength-maxLength.2.1", new Object[]{this.fTypeName, Integer.toString(this.fLength), Integer.toString(this.fBase.fMaxLength)});
                }
                if ((this.fBase.fFacetsDefined & 1) != 0 && this.fLength != this.fBase.fLength) {
                    this.reportError("length-valid-restriction", new Object[]{Integer.toString(this.fLength), Integer.toString(this.fBase.fLength), this.fTypeName});
                }
            }
            if ((this.fBase.fFacetsDefined & 1) != 0 || (this.fFacetsDefined & 1) != 0) {
                if ((this.fFacetsDefined & 2) != 0) {
                    if (this.fBase.fLength < this.fMinLength) {
                        this.reportError("length-minLength-maxLength.1.1", new Object[]{this.fTypeName, Integer.toString(this.fBase.fLength), Integer.toString(this.fMinLength)});
                    }
                    if ((this.fBase.fFacetsDefined & 2) == 0) {
                        this.reportError("length-minLength-maxLength.1.2.a", new Object[]{this.fTypeName});
                    }
                    if (this.fMinLength != this.fBase.fMinLength) {
                        this.reportError("length-minLength-maxLength.1.2.b", new Object[]{this.fTypeName, Integer.toString(this.fMinLength), Integer.toString(this.fBase.fMinLength)});
                    }
                }
                if ((this.fFacetsDefined & 4) != 0) {
                    if (this.fBase.fLength > this.fMaxLength) {
                        this.reportError("length-minLength-maxLength.2.1", new Object[]{this.fTypeName, Integer.toString(this.fBase.fLength), Integer.toString(this.fMaxLength)});
                    }
                    if ((this.fBase.fFacetsDefined & 4) == 0) {
                        this.reportError("length-minLength-maxLength.2.2.a", new Object[]{this.fTypeName});
                    }
                    if (this.fMaxLength != this.fBase.fMaxLength) {
                        this.reportError("length-minLength-maxLength.2.2.b", new Object[]{this.fTypeName, Integer.toString(this.fMaxLength), Integer.toString(this.fBase.fBase.fMaxLength)});
                    }
                }
            }
            if ((this.fFacetsDefined & 2) != 0) {
                if ((this.fBase.fFacetsDefined & 4) != 0) {
                    if (this.fMinLength > this.fBase.fMaxLength) {
                        this.reportError("minLength-less-than-equal-to-maxLength", new Object[]{Integer.toString(this.fMinLength), Integer.toString(this.fBase.fMaxLength), this.fTypeName});
                    }
                } else if ((this.fBase.fFacetsDefined & 2) != 0) {
                    if ((this.fBase.fFixedFacet & 2) != 0 && this.fMinLength != this.fBase.fMinLength) {
                        this.reportError("FixedFacetValue", new Object[]{"minLength", Integer.toString(this.fMinLength), Integer.toString(this.fBase.fMinLength), this.fTypeName});
                    }
                    if (this.fMinLength < this.fBase.fMinLength) {
                        this.reportError("minLength-valid-restriction", new Object[]{Integer.toString(this.fMinLength), Integer.toString(this.fBase.fMinLength), this.fTypeName});
                    }
                }
            }
            if ((this.fFacetsDefined & 4) != 0 && (this.fBase.fFacetsDefined & 2) != 0 && this.fMaxLength < this.fBase.fMinLength) {
                this.reportError("minLength-less-than-equal-to-maxLength", new Object[]{Integer.toString(this.fBase.fMinLength), Integer.toString(this.fMaxLength)});
            }
            if ((this.fFacetsDefined & 4) != 0 && (this.fBase.fFacetsDefined & 4) != 0) {
                if ((this.fBase.fFixedFacet & 4) != 0 && this.fMaxLength != this.fBase.fMaxLength) {
                    this.reportError("FixedFacetValue", new Object[]{"maxLength", Integer.toString(this.fMaxLength), Integer.toString(this.fBase.fMaxLength), this.fTypeName});
                }
                if (this.fMaxLength > this.fBase.fMaxLength) {
                    this.reportError("maxLength-valid-restriction", new Object[]{Integer.toString(this.fMaxLength), Integer.toString(this.fBase.fMaxLength), this.fTypeName});
                }
            }
            if ((this.fFacetsDefined & 512) != 0 && (this.fBase.fFacetsDefined & 512) != 0) {
                if ((this.fBase.fFixedFacet & 512) != 0 && this.fTotalDigits != this.fBase.fTotalDigits) {
                    this.reportError("FixedFacetValue", new Object[]{"totalDigits", Integer.toString(this.fTotalDigits), Integer.toString(this.fBase.fTotalDigits), this.fTypeName});
                }
                if (this.fTotalDigits > this.fBase.fTotalDigits) {
                    this.reportError("totalDigits-valid-restriction", new Object[]{Integer.toString(this.fTotalDigits), Integer.toString(this.fBase.fTotalDigits), this.fTypeName});
                }
            }
            if ((this.fFacetsDefined & 1024) != 0 && (this.fBase.fFacetsDefined & 512) != 0 && this.fFractionDigits > this.fBase.fTotalDigits) {
                this.reportError("fractionDigits-totalDigits", new Object[]{Integer.toString(this.fFractionDigits), Integer.toString(this.fTotalDigits), this.fTypeName});
            }
            if ((this.fFacetsDefined & 1024) != 0) {
                if ((this.fBase.fFacetsDefined & 1024) != 0) {
                    if ((this.fBase.fFixedFacet & 1024) != 0 && this.fFractionDigits != this.fBase.fFractionDigits || this.fValidationDV == 24 && this.fFractionDigits != 0) {
                        this.reportError("FixedFacetValue", new Object[]{"fractionDigits", Integer.toString(this.fFractionDigits), Integer.toString(this.fBase.fFractionDigits), this.fTypeName});
                    }
                    if (this.fFractionDigits > this.fBase.fFractionDigits) {
                        this.reportError("fractionDigits-valid-restriction", new Object[]{Integer.toString(this.fFractionDigits), Integer.toString(this.fBase.fFractionDigits), this.fTypeName});
                    }
                } else if (this.fValidationDV == 24 && this.fFractionDigits != 0) {
                    this.reportError("FixedFacetValue", new Object[]{"fractionDigits", Integer.toString(this.fFractionDigits), "0", this.fTypeName});
                }
            }
            if ((this.fFacetsDefined & 16) != 0 && (this.fBase.fFacetsDefined & 16) != 0) {
                if ((this.fBase.fFixedFacet & 16) != 0 && this.fWhiteSpace != this.fBase.fWhiteSpace) {
                    this.reportError("FixedFacetValue", new Object[]{"whiteSpace", this.whiteSpaceValue(this.fWhiteSpace), this.whiteSpaceValue(this.fBase.fWhiteSpace), this.fTypeName});
                }
                if (this.fWhiteSpace == 0 && this.fBase.fWhiteSpace == 2) {
                    this.reportError("whiteSpace-valid-restriction.1", new Object[]{this.fTypeName, "preserve"});
                }
                if (this.fWhiteSpace == 1 && this.fBase.fWhiteSpace == 2) {
                    this.reportError("whiteSpace-valid-restriction.1", new Object[]{this.fTypeName, "replace"});
                }
                if (this.fWhiteSpace == 0 && this.fBase.fWhiteSpace == 1) {
                    this.reportError("whiteSpace-valid-restriction.2", new Object[]{this.fTypeName});
                }
            }
        }
        if ((this.fFacetsDefined & 1) == 0 && (this.fBase.fFacetsDefined & 1) != 0) {
            this.fFacetsDefined = (short)(this.fFacetsDefined | 1);
            this.fLength = this.fBase.fLength;
            this.lengthAnnotation = this.fBase.lengthAnnotation;
        }
        if ((this.fFacetsDefined & 2) == 0 && (this.fBase.fFacetsDefined & 2) != 0) {
            this.fFacetsDefined = (short)(this.fFacetsDefined | 2);
            this.fMinLength = this.fBase.fMinLength;
            this.minLengthAnnotation = this.fBase.minLengthAnnotation;
        }
        if ((this.fFacetsDefined & 4) == 0 && (this.fBase.fFacetsDefined & 4) != 0) {
            this.fFacetsDefined = (short)(this.fFacetsDefined | 4);
            this.fMaxLength = this.fBase.fMaxLength;
            this.maxLengthAnnotation = this.fBase.maxLengthAnnotation;
        }
        if ((this.fBase.fFacetsDefined & 8) != 0) {
            if ((this.fFacetsDefined & 8) == 0) {
                this.fFacetsDefined = (short)(this.fFacetsDefined | 8);
                this.fPattern = this.fBase.fPattern;
                this.fPatternStr = this.fBase.fPatternStr;
                this.patternAnnotations = this.fBase.patternAnnotations;
            } else {
                int n4 = this.fBase.fPattern.size() - 1;
                while (n4 >= 0) {
                    this.fPattern.addElement(this.fBase.fPattern.elementAt(n4));
                    this.fPatternStr.addElement(this.fBase.fPatternStr.elementAt(n4));
                    --n4;
                }
                if (this.fBase.patternAnnotations != null) {
                    if (this.patternAnnotations != null) {
                        int n5 = this.fBase.patternAnnotations.getLength() - 1;
                        while (n5 >= 0) {
                            this.patternAnnotations.addXSObject(this.fBase.patternAnnotations.item(n5));
                            --n5;
                        }
                    } else {
                        this.patternAnnotations = this.fBase.patternAnnotations;
                    }
                }
            }
        }
        if ((this.fFacetsDefined & 16) == 0 && (this.fBase.fFacetsDefined & 16) != 0) {
            this.fFacetsDefined = (short)(this.fFacetsDefined | 16);
            this.fWhiteSpace = this.fBase.fWhiteSpace;
            this.whiteSpaceAnnotation = this.fBase.whiteSpaceAnnotation;
        }
        if ((this.fFacetsDefined & 2048) == 0 && (this.fBase.fFacetsDefined & 2048) != 0) {
            this.fFacetsDefined = (short)(this.fFacetsDefined | 2048);
            this.fEnumeration = this.fBase.fEnumeration;
            this.enumerationAnnotations = this.fBase.enumerationAnnotations;
        }
        if ((this.fBase.fFacetsDefined & 64) != 0 && (this.fFacetsDefined & 64) == 0 && (this.fFacetsDefined & 32) == 0) {
            this.fFacetsDefined = (short)(this.fFacetsDefined | 64);
            this.fMaxExclusive = this.fBase.fMaxExclusive;
            this.maxExclusiveAnnotation = this.fBase.maxExclusiveAnnotation;
        }
        if ((this.fBase.fFacetsDefined & 32) != 0 && (this.fFacetsDefined & 64) == 0 && (this.fFacetsDefined & 32) == 0) {
            this.fFacetsDefined = (short)(this.fFacetsDefined | 32);
            this.fMaxInclusive = this.fBase.fMaxInclusive;
            this.maxInclusiveAnnotation = this.fBase.maxInclusiveAnnotation;
        }
        if ((this.fBase.fFacetsDefined & 128) != 0 && (this.fFacetsDefined & 128) == 0 && (this.fFacetsDefined & 256) == 0) {
            this.fFacetsDefined = (short)(this.fFacetsDefined | 128);
            this.fMinExclusive = this.fBase.fMinExclusive;
            this.minExclusiveAnnotation = this.fBase.minExclusiveAnnotation;
        }
        if ((this.fBase.fFacetsDefined & 256) != 0 && (this.fFacetsDefined & 128) == 0 && (this.fFacetsDefined & 256) == 0) {
            this.fFacetsDefined = (short)(this.fFacetsDefined | 256);
            this.fMinInclusive = this.fBase.fMinInclusive;
            this.minInclusiveAnnotation = this.fBase.minInclusiveAnnotation;
        }
        if ((this.fBase.fFacetsDefined & 512) != 0 && (this.fFacetsDefined & 512) == 0) {
            this.fFacetsDefined = (short)(this.fFacetsDefined | 512);
            this.fTotalDigits = this.fBase.fTotalDigits;
            this.totalDigitsAnnotation = this.fBase.totalDigitsAnnotation;
        }
        if ((this.fBase.fFacetsDefined & 1024) != 0 && (this.fFacetsDefined & 1024) == 0) {
            this.fFacetsDefined = (short)(this.fFacetsDefined | 1024);
            this.fFractionDigits = this.fBase.fFractionDigits;
            this.fractionDigitsAnnotation = this.fBase.fractionDigitsAnnotation;
        }
        if (this.fPatternType == 0 && this.fBase.fPatternType != 0) {
            this.fPatternType = this.fBase.fPatternType;
        }
        this.fFixedFacet = (short)(this.fFixedFacet | this.fBase.fFixedFacet);
        this.calcFundamentalFacets();
    }

    public Object validate(String string, ValidationContext validationContext, ValidatedInfo validatedInfo) throws InvalidDatatypeValueException {
        if (validationContext == null) {
            validationContext = fEmptyContext;
        }
        if (validatedInfo == null) {
            validatedInfo = new ValidatedInfo();
        } else {
            validatedInfo.memberType = null;
        }
        boolean bl = validationContext == null || validationContext.needToNormalize();
        Object object = this.getActualValue(string, validationContext, validatedInfo, bl);
        this.validate(validationContext, validatedInfo);
        return object;
    }

    protected ValidatedInfo getActualEnumValue(String string, ValidationContext validationContext, ValidatedInfo validatedInfo) throws InvalidDatatypeValueException {
        return this.fBase.validateWithInfo(string, validationContext, validatedInfo);
    }

    public ValidatedInfo validateWithInfo(String string, ValidationContext validationContext, ValidatedInfo validatedInfo) throws InvalidDatatypeValueException {
        if (validationContext == null) {
            validationContext = fEmptyContext;
        }
        if (validatedInfo == null) {
            validatedInfo = new ValidatedInfo();
        } else {
            validatedInfo.memberType = null;
        }
        boolean bl = validationContext == null || validationContext.needToNormalize();
        this.getActualValue(string, validationContext, validatedInfo, bl);
        this.validate(validationContext, validatedInfo);
        return validatedInfo;
    }

    public Object validate(Object object, ValidationContext validationContext, ValidatedInfo validatedInfo) throws InvalidDatatypeValueException {
        if (validationContext == null) {
            validationContext = fEmptyContext;
        }
        if (validatedInfo == null) {
            validatedInfo = new ValidatedInfo();
        } else {
            validatedInfo.memberType = null;
        }
        boolean bl = validationContext == null || validationContext.needToNormalize();
        Object object2 = this.getActualValue(object, validationContext, validatedInfo, bl);
        this.validate(validationContext, validatedInfo);
        return object2;
    }

    public void validate(ValidationContext validationContext, ValidatedInfo validatedInfo) throws InvalidDatatypeValueException {
        if (validationContext == null) {
            validationContext = fEmptyContext;
        }
        if (validationContext.needFacetChecking() && this.fFacetsDefined != 0 && this.fFacetsDefined != 16) {
            this.checkFacets(validatedInfo);
        }
        if (validationContext.needExtraChecking()) {
            this.checkExtraRules(validationContext, validatedInfo);
        }
    }

    private void checkFacets(ValidatedInfo validatedInfo) throws InvalidDatatypeValueException {
        int n2;
        Object object = validatedInfo.actualValue;
        String string = validatedInfo.normalizedValue;
        short s2 = validatedInfo.actualValueType;
        ShortList shortList = validatedInfo.itemValueTypes;
        if (this.fValidationDV != 18 && this.fValidationDV != 20) {
            n2 = this.fDVs[this.fValidationDV].getDataLength(object);
            if ((this.fFacetsDefined & 4) != 0 && n2 > this.fMaxLength) {
                throw new InvalidDatatypeValueException("cvc-maxLength-valid", new Object[]{string, Integer.toString(n2), Integer.toString(this.fMaxLength), this.fTypeName});
            }
            if ((this.fFacetsDefined & 2) != 0 && n2 < this.fMinLength) {
                throw new InvalidDatatypeValueException("cvc-minLength-valid", new Object[]{string, Integer.toString(n2), Integer.toString(this.fMinLength), this.fTypeName});
            }
            if ((this.fFacetsDefined & 1) != 0 && n2 != this.fLength) {
                throw new InvalidDatatypeValueException("cvc-length-valid", new Object[]{string, Integer.toString(n2), Integer.toString(this.fLength), this.fTypeName});
            }
        }
        if ((this.fFacetsDefined & 2048) != 0) {
            n2 = 0;
            int n3 = this.fEnumeration.size();
            short s3 = this.convertToPrimitiveKind(s2);
            int n4 = 0;
            while (n4 < n3) {
                short s4 = this.convertToPrimitiveKind(this.fEnumerationType[n4]);
                if ((s3 == s4 || s3 == 1 && s4 == 2 || s3 == 2 && s4 == 1) && this.fEnumeration.elementAt(n4).equals(object)) {
                    if (s3 == 44 || s3 == 43) {
                        int n5;
                        ShortList shortList2 = this.fEnumerationItemType[n4];
                        int n6 = shortList != null ? shortList.getLength() : 0;
                        int n7 = n5 = shortList2 != null ? shortList2.getLength() : 0;
                        if (n6 == n5) {
                            int n8 = 0;
                            while (n8 < n6) {
                                short s5;
                                short s6 = this.convertToPrimitiveKind(shortList.item(n8));
                                if (s6 != (s5 = this.convertToPrimitiveKind(shortList2.item(n8))) && (s6 != 1 || s5 != 2) && (s6 != 2 || s5 != 1)) break;
                                ++n8;
                            }
                            if (n8 == n6) {
                                n2 = 1;
                                break;
                            }
                        }
                    } else {
                        n2 = 1;
                        break;
                    }
                }
                ++n4;
            }
            if (n2 == 0) {
                throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[]{string, this.fEnumeration.toString()});
            }
        }
        if ((this.fFacetsDefined & 1024) != 0 && (n2 = this.fDVs[this.fValidationDV].getFractionDigits(object)) > this.fFractionDigits) {
            throw new InvalidDatatypeValueException("cvc-fractionDigits-valid", new Object[]{string, Integer.toString(n2), Integer.toString(this.fFractionDigits)});
        }
        if ((this.fFacetsDefined & 512) != 0 && (n2 = this.fDVs[this.fValidationDV].getTotalDigits(object)) > this.fTotalDigits) {
            throw new InvalidDatatypeValueException("cvc-totalDigits-valid", new Object[]{string, Integer.toString(n2), Integer.toString(this.fTotalDigits)});
        }
        if ((this.fFacetsDefined & 32) != 0 && (n2 = this.fDVs[this.fValidationDV].compare(object, this.fMaxInclusive)) != -1 && n2 != 0) {
            throw new InvalidDatatypeValueException("cvc-maxInclusive-valid", new Object[]{string, this.fMaxInclusive, this.fTypeName});
        }
        if ((this.fFacetsDefined & 64) != 0 && (n2 = this.fDVs[this.fValidationDV].compare(object, this.fMaxExclusive)) != -1) {
            throw new InvalidDatatypeValueException("cvc-maxExclusive-valid", new Object[]{string, this.fMaxExclusive, this.fTypeName});
        }
        if ((this.fFacetsDefined & 256) != 0 && (n2 = this.fDVs[this.fValidationDV].compare(object, this.fMinInclusive)) != 1 && n2 != 0) {
            throw new InvalidDatatypeValueException("cvc-minInclusive-valid", new Object[]{string, this.fMinInclusive, this.fTypeName});
        }
        if ((this.fFacetsDefined & 128) != 0 && (n2 = this.fDVs[this.fValidationDV].compare(object, this.fMinExclusive)) != 1) {
            throw new InvalidDatatypeValueException("cvc-minExclusive-valid", new Object[]{string, this.fMinExclusive, this.fTypeName});
        }
    }

    private void checkExtraRules(ValidationContext validationContext, ValidatedInfo validatedInfo) throws InvalidDatatypeValueException {
        Object object = validatedInfo.actualValue;
        if (this.fVariety == 1) {
            this.fDVs[this.fValidationDV].checkExtraRules(object, validationContext);
        } else {
            if (this.fVariety == 2) {
                ListDV.ListData listData = (ListDV.ListData)object;
                XSSimpleType xSSimpleType = validatedInfo.memberType;
                int n2 = listData.getLength();
                try {
                    if (this.fItemType.fVariety == 3) {
                        XSSimpleTypeDecl[] arrxSSimpleTypeDecl = (XSSimpleTypeDecl[])validatedInfo.memberTypes;
                        int n3 = n2 - 1;
                        while (n3 >= 0) {
                            validatedInfo.actualValue = listData.item(n3);
                            validatedInfo.memberType = arrxSSimpleTypeDecl[n3];
                            this.fItemType.checkExtraRules(validationContext, validatedInfo);
                            --n3;
                        }
                    } else {
                        int n4 = n2 - 1;
                        while (n4 >= 0) {
                            validatedInfo.actualValue = listData.item(n4);
                            this.fItemType.checkExtraRules(validationContext, validatedInfo);
                            --n4;
                        }
                    }
                    Object var10_10 = null;
                    validatedInfo.actualValue = listData;
                    validatedInfo.memberType = xSSimpleType;
                }
                catch (Throwable throwable) {
                    Object var10_11 = null;
                    validatedInfo.actualValue = listData;
                    validatedInfo.memberType = xSSimpleType;
                    throw throwable;
                }
            }
            ((XSSimpleTypeDecl)validatedInfo.memberType).checkExtraRules(validationContext, validatedInfo);
        }
    }

    private Object getActualValue(Object object, ValidationContext validationContext, ValidatedInfo validatedInfo, boolean bl) throws InvalidDatatypeValueException {
        Object object2;
        int n2;
        String string = bl ? this.normalize(object, this.fWhiteSpace) : object.toString();
        if ((this.fFacetsDefined & 8) != 0) {
            n2 = this.fPattern.size() - 1;
            while (n2 >= 0) {
                object2 = (RegularExpression)this.fPattern.elementAt(n2);
                if (!object2.matches(string)) {
                    throw new InvalidDatatypeValueException("cvc-pattern-valid", new Object[]{object, this.fPatternStr.elementAt(n2), this.fTypeName});
                }
                --n2;
            }
        }
        if (this.fVariety == 1) {
            if (this.fPatternType != 0) {
                boolean bl2 = false;
                if (this.fPatternType == 1) {
                    bl2 = !XMLChar.isValidNmtoken(string);
                } else if (this.fPatternType == 2) {
                    bl2 = !XMLChar.isValidName(string);
                } else if (this.fPatternType == 3) {
                    boolean bl3 = bl2 = !XMLChar.isValidNCName(string);
                }
                if (bl2) {
                    throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{string, SPECIAL_PATTERN_STRING[this.fPatternType]});
                }
            }
            validatedInfo.normalizedValue = string;
            validatedInfo.actualValue = object2 = this.fDVs[this.fValidationDV].getActualValue(string, validationContext);
            validatedInfo.actualValueType = this.fBuiltInKind;
            return object2;
        }
        if (this.fVariety == 2) {
            object2 = new StringTokenizer(string, " ");
            n2 = object2.countTokens();
            Object[] arrobject = new Object[n2];
            boolean bl4 = this.fItemType.getVariety() == 3;
            short[] arrs = new short[bl4 ? n2 : 1];
            if (!bl4) {
                arrs[0] = this.fItemType.fBuiltInKind;
            }
            XSSimpleTypeDecl[] arrxSSimpleTypeDecl = new XSSimpleTypeDecl[n2];
            int n3 = 0;
            while (n3 < n2) {
                arrobject[n3] = this.fItemType.getActualValue(object2.nextToken(), validationContext, validatedInfo, false);
                if (validationContext.needFacetChecking() && this.fItemType.fFacetsDefined != 0 && this.fItemType.fFacetsDefined != 16) {
                    this.fItemType.checkFacets(validatedInfo);
                }
                arrxSSimpleTypeDecl[n3] = (XSSimpleTypeDecl)validatedInfo.memberType;
                if (bl4) {
                    arrs[n3] = arrxSSimpleTypeDecl[n3].fBuiltInKind;
                }
                ++n3;
            }
            ListDV.ListData listData = new ListDV.ListData(arrobject);
            validatedInfo.actualValue = listData;
            validatedInfo.actualValueType = bl4 ? 43 : 44;
            validatedInfo.memberType = null;
            validatedInfo.memberTypes = arrxSSimpleTypeDecl;
            validatedInfo.itemValueTypes = new ShortListImpl(arrs, arrs.length);
            validatedInfo.normalizedValue = string;
            return listData;
        }
        object2 = this.fMemberTypes.length > 1 && object != null ? object.toString() : object;
        n2 = 0;
        while (n2 < this.fMemberTypes.length) {
            try {
                Object object3 = this.fMemberTypes[n2].getActualValue(object2, validationContext, validatedInfo, true);
                if (validationContext.needFacetChecking() && this.fMemberTypes[n2].fFacetsDefined != 0 && this.fMemberTypes[n2].fFacetsDefined != 16) {
                    this.fMemberTypes[n2].checkFacets(validatedInfo);
                }
                validatedInfo.memberType = this.fMemberTypes[n2];
                return object3;
            }
            catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                ++n2;
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        int n4 = 0;
        while (n4 < this.fMemberTypes.length) {
            if (n4 != 0) {
                stringBuffer.append(" | ");
            }
            XSSimpleTypeDecl xSSimpleTypeDecl = this.fMemberTypes[n4];
            if (xSSimpleTypeDecl.fTargetNamespace != null) {
                stringBuffer.append('{');
                stringBuffer.append(xSSimpleTypeDecl.fTargetNamespace);
                stringBuffer.append('}');
            }
            stringBuffer.append(xSSimpleTypeDecl.fTypeName);
            if (xSSimpleTypeDecl.fEnumeration != null) {
                Vector vector = xSSimpleTypeDecl.fEnumeration;
                stringBuffer.append(" : [");
                int n5 = 0;
                while (n5 < vector.size()) {
                    if (n5 != 0) {
                        stringBuffer.append(',');
                    }
                    stringBuffer.append(vector.elementAt(n5));
                    ++n5;
                }
                stringBuffer.append(']');
            }
            ++n4;
        }
        throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[]{object, this.fTypeName, stringBuffer.toString()});
    }

    public boolean isEqual(Object object, Object object2) {
        if (object == null) {
            return false;
        }
        return object.equals(object2);
    }

    public boolean isIdentical(Object object, Object object2) {
        if (object == null) {
            return false;
        }
        return this.fDVs[this.fValidationDV].isIdentical(object, object2);
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

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    protected String normalize(Object var1_1, short var2_2) {
        if (var1_1 == null) {
            return null;
        }
        if ((this.fFacetsDefined & 8) == 0) {
            var3_3 = XSSimpleTypeDecl.fDVNormalizeType[this.fValidationDV];
            if (var3_3 == 0) {
                return var1_1.toString();
            }
            if (var3_3 == 1) {
                return XMLChar.trim(var1_1.toString());
            }
        }
        if (!(var1_1 instanceof StringBuffer)) {
            var3_4 = var1_1.toString();
            return XSSimpleTypeDecl.normalize(var3_4, var2_2);
        }
        var3_5 = (StringBuffer)var1_1;
        var4_6 = var3_5.length();
        if (var4_6 == 0) {
            return "";
        }
        if (var2_2 == 0) {
            return var3_5.toString();
        }
        if (var2_2 != 1) ** GOTO lbl21
        var6_7 = 0;
        ** GOTO lbl29
lbl21: // 1 sources:
        var7_11 = 0;
        var8_12 = true;
        var6_8 = 0;
        ** GOTO lbl42
lbl-1000: // 1 sources:
        {
            var5_9 = var3_5.charAt(var6_7);
            if (var5_9 == '\t' || var5_9 == '\n' || var5_9 == '\r') {
                var3_5.setCharAt(var6_7, ' ');
            }
            ++var6_7;
lbl29: // 2 sources:
            ** while (var6_7 < var4_6)
        }
lbl30: // 1 sources:
        return var3_5.toString();
lbl-1000: // 1 sources:
        {
            var5_10 = var3_5.charAt(var6_8);
            if (var5_10 == '\t' || var5_10 == '\n' || var5_10 == '\r' || var5_10 == ' ') ** GOTO lbl38
            var3_5.setCharAt(var7_11++, var5_10);
            var8_12 = false;
            ** GOTO lbl41
            while ((var5_10 = var3_5.charAt(var6_8 + 1)) == '\t' || var5_10 == '\n' || var5_10 == '\r' || var5_10 == ' ') {
                ++var6_8;
lbl38: // 2 sources:
                if (var6_8 < var4_6 - 1) continue;
            }
            if (var6_8 < var4_6 - 1 && !var8_12) {
                var3_5.setCharAt(var7_11++, ' ');
            }
lbl41: // 4 sources:
            ++var6_8;
lbl42: // 2 sources:
            ** while (var6_8 < var4_6)
        }
lbl43: // 1 sources:
        var3_5.setLength(var7_11);
        return var3_5.toString();
    }

    void reportError(String string, Object[] arrobject) throws InvalidDatatypeFacetException {
        throw new InvalidDatatypeFacetException(string, arrobject);
    }

    private String whiteSpaceValue(short s2) {
        return WS_FACET_STRING[s2];
    }

    public short getOrdered() {
        return this.fOrdered;
    }

    public boolean getBounded() {
        return this.fBounded;
    }

    public boolean getFinite() {
        return this.fFinite;
    }

    public boolean getNumeric() {
        return this.fNumeric;
    }

    public boolean isDefinedFacet(short s2) {
        if ((this.fFacetsDefined & s2) != 0) {
            return true;
        }
        if (this.fPatternType != 0) {
            return s2 == 8;
        }
        if (this.fValidationDV == 24) {
            return s2 == 8 || s2 == 1024;
        }
        return false;
    }

    public short getDefinedFacets() {
        if (this.fPatternType != 0) {
            return (short)(this.fFacetsDefined | 8);
        }
        if (this.fValidationDV == 24) {
            return (short)(this.fFacetsDefined | 8 | 1024);
        }
        return this.fFacetsDefined;
    }

    public boolean isFixedFacet(short s2) {
        if ((this.fFixedFacet & s2) != 0) {
            return true;
        }
        if (this.fValidationDV == 24) {
            return s2 == 1024;
        }
        return false;
    }

    public short getFixedFacets() {
        if (this.fValidationDV == 24) {
            return (short)(this.fFixedFacet | 1024);
        }
        return this.fFixedFacet;
    }

    public String getLexicalFacetValue(short s2) {
        switch (s2) {
            case 1: {
                return this.fLength == -1 ? null : Integer.toString(this.fLength);
            }
            case 2: {
                return this.fMinLength == -1 ? null : Integer.toString(this.fMinLength);
            }
            case 4: {
                return this.fMaxLength == -1 ? null : Integer.toString(this.fMaxLength);
            }
            case 16: {
                return WS_FACET_STRING[this.fWhiteSpace];
            }
            case 32: {
                return this.fMaxInclusive == null ? null : this.fMaxInclusive.toString();
            }
            case 64: {
                return this.fMaxExclusive == null ? null : this.fMaxExclusive.toString();
            }
            case 128: {
                return this.fMinExclusive == null ? null : this.fMinExclusive.toString();
            }
            case 256: {
                return this.fMinInclusive == null ? null : this.fMinInclusive.toString();
            }
            case 512: {
                return this.fTotalDigits == -1 ? null : Integer.toString(this.fTotalDigits);
            }
            case 1024: {
                if (this.fValidationDV == 24) {
                    return "0";
                }
                return this.fFractionDigits == -1 ? null : Integer.toString(this.fFractionDigits);
            }
        }
        return null;
    }

    public StringList getLexicalEnumeration() {
        if (this.fLexicalEnumeration == null) {
            if (this.fEnumeration == null) {
                return StringListImpl.EMPTY_LIST;
            }
            int n2 = this.fEnumeration.size();
            String[] arrstring = new String[n2];
            int n3 = 0;
            while (n3 < n2) {
                arrstring[n3] = this.fEnumeration.elementAt(n3).toString();
                ++n3;
            }
            this.fLexicalEnumeration = new StringListImpl(arrstring, n2);
        }
        return this.fLexicalEnumeration;
    }

    public ObjectList getActualEnumeration() {
        if (this.fActualEnumeration == null) {
            this.fActualEnumeration = new AbstractObjectList(this){
                private final XSSimpleTypeDecl this$0;

                public int getLength() {
                    return XSSimpleTypeDecl.access$100(this.this$0) != null ? XSSimpleTypeDecl.access$100(this.this$0).size() : 0;
                }

                public boolean contains(Object object) {
                    return XSSimpleTypeDecl.access$100(this.this$0) != null && XSSimpleTypeDecl.access$100(this.this$0).contains(object);
                }

                public Object item(int n2) {
                    if (n2 < 0 || n2 >= this.getLength()) {
                        return null;
                    }
                    return XSSimpleTypeDecl.access$100(this.this$0).elementAt(n2);
                }
            };
        }
        return this.fActualEnumeration;
    }

    public ObjectList getEnumerationItemTypeList() {
        if (this.fEnumerationItemTypeList == null) {
            if (this.fEnumerationItemType == null) {
                return null;
            }
            this.fEnumerationItemTypeList = new AbstractObjectList(this){
                private final XSSimpleTypeDecl this$0;

                public int getLength() {
                    return XSSimpleTypeDecl.access$200(this.this$0) != null ? XSSimpleTypeDecl.access$200(this.this$0).length : 0;
                }

                public boolean contains(Object object) {
                    if (XSSimpleTypeDecl.access$200(this.this$0) == null || !(object instanceof ShortList)) {
                        return false;
                    }
                    int n2 = 0;
                    while (n2 < XSSimpleTypeDecl.access$200(this.this$0).length) {
                        if (XSSimpleTypeDecl.access$200(this.this$0)[n2] == object) {
                            return true;
                        }
                        ++n2;
                    }
                    return false;
                }

                public Object item(int n2) {
                    if (n2 < 0 || n2 >= this.getLength()) {
                        return null;
                    }
                    return XSSimpleTypeDecl.access$200(this.this$0)[n2];
                }
            };
        }
        return this.fEnumerationItemTypeList;
    }

    public ShortList getEnumerationTypeList() {
        if (this.fEnumerationTypeList == null) {
            if (this.fEnumerationType == null) {
                return ShortListImpl.EMPTY_LIST;
            }
            this.fEnumerationTypeList = new ShortListImpl(this.fEnumerationType, this.fEnumerationType.length);
        }
        return this.fEnumerationTypeList;
    }

    public StringList getLexicalPattern() {
        if (this.fPatternType == 0 && this.fValidationDV != 24 && this.fPatternStr == null) {
            return StringListImpl.EMPTY_LIST;
        }
        if (this.fLexicalPattern == null) {
            int n2;
            String[] arrstring;
            int n3 = n2 = this.fPatternStr == null ? 0 : this.fPatternStr.size();
            if (this.fPatternType == 1) {
                arrstring = new String[n2 + 1];
                arrstring[n2] = "\\c+";
            } else if (this.fPatternType == 2) {
                arrstring = new String[n2 + 1];
                arrstring[n2] = "\\i\\c*";
            } else if (this.fPatternType == 3) {
                arrstring = new String[n2 + 2];
                arrstring[n2] = "\\i\\c*";
                arrstring[n2 + 1] = "[\\i-[:]][\\c-[:]]*";
            } else if (this.fValidationDV == 24) {
                arrstring = new String[n2 + 1];
                arrstring[n2] = "[\\-+]?[0-9]+";
            } else {
                arrstring = new String[n2];
            }
            int n4 = 0;
            while (n4 < n2) {
                arrstring[n4] = (String)this.fPatternStr.elementAt(n4);
                ++n4;
            }
            this.fLexicalPattern = new StringListImpl(arrstring, arrstring.length);
        }
        return this.fLexicalPattern;
    }

    public XSObjectList getAnnotations() {
        return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
    }

    private void calcFundamentalFacets() {
        this.setOrdered();
        this.setNumeric();
        this.setBounded();
        this.setCardinality();
    }

    private void setOrdered() {
        if (this.fVariety == 1) {
            this.fOrdered = this.fBase.fOrdered;
        } else if (this.fVariety == 2) {
            this.fOrdered = 0;
        } else if (this.fVariety == 3) {
            int n2 = this.fMemberTypes.length;
            if (n2 == 0) {
                this.fOrdered = 1;
                return;
            }
            short s2 = this.getPrimitiveDV(this.fMemberTypes[0].fValidationDV);
            boolean bl = s2 != 0;
            boolean bl2 = this.fMemberTypes[0].fOrdered == 0;
            int n3 = 1;
            while (n3 < this.fMemberTypes.length && (bl || bl2)) {
                if (bl) {
                    boolean bl3 = bl = s2 == this.getPrimitiveDV(this.fMemberTypes[n3].fValidationDV);
                }
                if (bl2) {
                    bl2 = this.fMemberTypes[n3].fOrdered == 0;
                }
                ++n3;
            }
            this.fOrdered = bl ? this.fMemberTypes[0].fOrdered : (bl2 ? 0 : 1);
        }
    }

    private void setNumeric() {
        if (this.fVariety == 1) {
            this.fNumeric = this.fBase.fNumeric;
        } else if (this.fVariety == 2) {
            this.fNumeric = false;
        } else if (this.fVariety == 3) {
            XSSimpleTypeDecl[] arrxSSimpleTypeDecl = this.fMemberTypes;
            int n2 = 0;
            while (n2 < arrxSSimpleTypeDecl.length) {
                if (!arrxSSimpleTypeDecl[n2].getNumeric()) {
                    this.fNumeric = false;
                    return;
                }
                ++n2;
            }
            this.fNumeric = true;
        }
    }

    private void setBounded() {
        if (this.fVariety == 1) {
            this.fBounded = !((this.fFacetsDefined & 256) == 0 && (this.fFacetsDefined & 128) == 0 || (this.fFacetsDefined & 32) == 0 && (this.fFacetsDefined & 64) == 0);
        } else if (this.fVariety == 2) {
            this.fBounded = (this.fFacetsDefined & 1) != 0 || (this.fFacetsDefined & 2) != 0 && (this.fFacetsDefined & 4) != 0;
        } else if (this.fVariety == 3) {
            XSSimpleTypeDecl[] arrxSSimpleTypeDecl = this.fMemberTypes;
            short s2 = 0;
            if (arrxSSimpleTypeDecl.length > 0) {
                s2 = this.getPrimitiveDV(arrxSSimpleTypeDecl[0].fValidationDV);
            }
            int n2 = 0;
            while (n2 < arrxSSimpleTypeDecl.length) {
                if (!arrxSSimpleTypeDecl[n2].getBounded() || s2 != this.getPrimitiveDV(arrxSSimpleTypeDecl[n2].fValidationDV)) {
                    this.fBounded = false;
                    return;
                }
                ++n2;
            }
            this.fBounded = true;
        }
    }

    private boolean specialCardinalityCheck() {
        if (this.fBase.fValidationDV == 9 || this.fBase.fValidationDV == 10 || this.fBase.fValidationDV == 11 || this.fBase.fValidationDV == 12 || this.fBase.fValidationDV == 13 || this.fBase.fValidationDV == 14) {
            return true;
        }
        return false;
    }

    private void setCardinality() {
        if (this.fVariety == 1) {
            this.fFinite = this.fBase.fFinite ? true : ((this.fFacetsDefined & 1) != 0 || (this.fFacetsDefined & 4) != 0 || (this.fFacetsDefined & 512) != 0 ? true : (!((this.fFacetsDefined & 256) == 0 && (this.fFacetsDefined & 128) == 0 || (this.fFacetsDefined & 32) == 0 && (this.fFacetsDefined & 64) == 0) ? (this.fFacetsDefined & 1024) != 0 || this.specialCardinalityCheck() : false));
        } else if (this.fVariety == 2) {
            this.fFinite = (this.fFacetsDefined & 1) != 0 || (this.fFacetsDefined & 2) != 0 && (this.fFacetsDefined & 4) != 0;
        } else if (this.fVariety == 3) {
            XSSimpleTypeDecl[] arrxSSimpleTypeDecl = this.fMemberTypes;
            int n2 = 0;
            while (n2 < arrxSSimpleTypeDecl.length) {
                if (!arrxSSimpleTypeDecl[n2].getFinite()) {
                    this.fFinite = false;
                    return;
                }
                ++n2;
            }
            this.fFinite = true;
        }
    }

    private short getPrimitiveDV(short s2) {
        if (s2 == 21 || s2 == 22 || s2 == 23) {
            return 1;
        }
        if (s2 == 24) {
            return 3;
        }
        return s2;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean derivedFromType(XSTypeDefinition var1_1, short var2_2) {
        if (var1_1 != null) ** GOTO lbl4
        return false;
lbl-1000: // 1 sources:
        {
            var1_1 = ((XSSimpleTypeDelegate)var1_1).type;
lbl4: // 2 sources:
            ** while (var1_1 instanceof XSSimpleTypeDelegate)
        }
lbl5: // 1 sources:
        if (var1_1.getBaseType() == var1_1) {
            return true;
        }
        var3_3 = this;
        while (var3_4 != var1_1 && var3_4 != XSSimpleTypeDecl.fAnySimpleType) {
            var3_5 = var3_4.getBaseType();
        }
        if (var3_4 != var1_1) return false;
        return true;
    }

    public boolean derivedFrom(String string, String string2, short s2) {
        if (string2 == null) {
            return false;
        }
        if ("http://www.w3.org/2001/XMLSchema".equals(string) && "anyType".equals(string2)) {
            return true;
        }
        XSTypeDefinition xSTypeDefinition = this;
        while (!(string2.equals(xSTypeDefinition.getName()) && (string == null && xSTypeDefinition.getNamespace() == null || string != null && string.equals(xSTypeDefinition.getNamespace())) || xSTypeDefinition == fAnySimpleType)) {
            xSTypeDefinition = xSTypeDefinition.getBaseType();
        }
        return xSTypeDefinition != fAnySimpleType;
    }

    public boolean isDOMDerivedFrom(String string, String string2, int n2) {
        if (string2 == null) {
            return false;
        }
        if (SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(string) && "anyType".equals(string2) && ((n2 & 1) != 0 || n2 == 0)) {
            return true;
        }
        if ((n2 & 1) != 0 && this.isDerivedByRestriction(string, string2, this)) {
            return true;
        }
        if ((n2 & 8) != 0 && this.isDerivedByList(string, string2, this)) {
            return true;
        }
        if ((n2 & 4) != 0 && this.isDerivedByUnion(string, string2, this)) {
            return true;
        }
        if ((n2 & 2) != 0 && (n2 & 1) == 0 && (n2 & 8) == 0 && (n2 & 4) == 0) {
            return false;
        }
        if ((n2 & 2) == 0 && (n2 & 1) == 0 && (n2 & 8) == 0 && (n2 & 4) == 0) {
            return this.isDerivedByAny(string, string2, this);
        }
        return false;
    }

    private boolean isDerivedByAny(String string, String string2, XSTypeDefinition xSTypeDefinition) {
        boolean bl = false;
        XSTypeDefinition xSTypeDefinition2 = null;
        while (xSTypeDefinition != null && xSTypeDefinition != xSTypeDefinition2) {
            if (string2.equals(xSTypeDefinition.getName()) && (string == null && xSTypeDefinition.getNamespace() == null || string != null && string.equals(xSTypeDefinition.getNamespace()))) {
                bl = true;
                break;
            }
            if (this.isDerivedByRestriction(string, string2, xSTypeDefinition)) {
                return true;
            }
            if (this.isDerivedByList(string, string2, xSTypeDefinition)) {
                return true;
            }
            if (this.isDerivedByUnion(string, string2, xSTypeDefinition)) {
                return true;
            }
            xSTypeDefinition2 = xSTypeDefinition;
            if (((XSSimpleTypeDecl)xSTypeDefinition).getVariety() == 0 || ((XSSimpleTypeDecl)xSTypeDefinition).getVariety() == 1) {
                xSTypeDefinition = xSTypeDefinition.getBaseType();
                continue;
            }
            if (((XSSimpleTypeDecl)xSTypeDefinition).getVariety() == 3) {
                int n2 = 0;
                if (n2 >= ((XSSimpleTypeDecl)xSTypeDefinition).getMemberTypes().getLength()) continue;
                return this.isDerivedByAny(string, string2, (XSTypeDefinition)((XSSimpleTypeDecl)xSTypeDefinition).getMemberTypes().item(n2));
            }
            if (((XSSimpleTypeDecl)xSTypeDefinition).getVariety() != 2) continue;
            xSTypeDefinition = ((XSSimpleTypeDecl)xSTypeDefinition).getItemType();
        }
        return bl;
    }

    private boolean isDerivedByRestriction(String string, String string2, XSTypeDefinition xSTypeDefinition) {
        XSTypeDefinition xSTypeDefinition2 = null;
        while (xSTypeDefinition != null && xSTypeDefinition != xSTypeDefinition2) {
            if (string2.equals(xSTypeDefinition.getName()) && (string != null && string.equals(xSTypeDefinition.getNamespace()) || xSTypeDefinition.getNamespace() == null && string == null)) {
                return true;
            }
            xSTypeDefinition2 = xSTypeDefinition;
            xSTypeDefinition = xSTypeDefinition.getBaseType();
        }
        return false;
    }

    private boolean isDerivedByList(String string, String string2, XSTypeDefinition xSTypeDefinition) {
        XSSimpleTypeDefinition xSSimpleTypeDefinition;
        if (xSTypeDefinition != null && ((XSSimpleTypeDefinition)xSTypeDefinition).getVariety() == 2 && (xSSimpleTypeDefinition = ((XSSimpleTypeDefinition)xSTypeDefinition).getItemType()) != null && this.isDerivedByRestriction(string, string2, xSSimpleTypeDefinition)) {
            return true;
        }
        return false;
    }

    private boolean isDerivedByUnion(String string, String string2, XSTypeDefinition xSTypeDefinition) {
        if (xSTypeDefinition != null && ((XSSimpleTypeDefinition)xSTypeDefinition).getVariety() == 3) {
            XSObjectList xSObjectList = ((XSSimpleTypeDefinition)xSTypeDefinition).getMemberTypes();
            int n2 = 0;
            while (n2 < xSObjectList.getLength()) {
                if (xSObjectList.item(n2) != null && this.isDerivedByRestriction(string, string2, (XSSimpleTypeDefinition)xSObjectList.item(n2))) {
                    return true;
                }
                ++n2;
            }
        }
        return false;
    }

    public void reset() {
        if (this.fIsImmutable) {
            return;
        }
        this.fItemType = null;
        this.fMemberTypes = null;
        this.fTypeName = null;
        this.fTargetNamespace = null;
        this.fFinalSet = 0;
        this.fBase = null;
        this.fVariety = -1;
        this.fValidationDV = -1;
        this.fFacetsDefined = 0;
        this.fFixedFacet = 0;
        this.fWhiteSpace = 0;
        this.fLength = -1;
        this.fMinLength = -1;
        this.fMaxLength = -1;
        this.fTotalDigits = -1;
        this.fFractionDigits = -1;
        this.fPattern = null;
        this.fPatternStr = null;
        this.fEnumeration = null;
        this.fEnumerationType = null;
        this.fEnumerationItemType = null;
        this.fLexicalPattern = null;
        this.fLexicalEnumeration = null;
        this.fMaxInclusive = null;
        this.fMaxExclusive = null;
        this.fMinExclusive = null;
        this.fMinInclusive = null;
        this.lengthAnnotation = null;
        this.minLengthAnnotation = null;
        this.maxLengthAnnotation = null;
        this.whiteSpaceAnnotation = null;
        this.totalDigitsAnnotation = null;
        this.fractionDigitsAnnotation = null;
        this.patternAnnotations = null;
        this.enumerationAnnotations = null;
        this.maxInclusiveAnnotation = null;
        this.maxExclusiveAnnotation = null;
        this.minInclusiveAnnotation = null;
        this.minExclusiveAnnotation = null;
        this.fPatternType = 0;
        this.fAnnotations = null;
        this.fFacets = null;
    }

    public XSNamespaceItem getNamespaceItem() {
        return this.fNamespaceItem;
    }

    public void setNamespaceItem(XSNamespaceItem xSNamespaceItem) {
        this.fNamespaceItem = xSNamespaceItem;
    }

    public String toString() {
        return this.fTargetNamespace + "," + this.fTypeName;
    }

    public XSObjectList getFacets() {
        if (this.fFacets == null && (this.fFacetsDefined != 0 || this.fValidationDV == 24)) {
            XSObject[] arrxSObject = new XSFacetImpl[10];
            int n2 = 0;
            if ((this.fFacetsDefined & 16) != 0) {
                arrxSObject[n2] = new XSFacetImpl(16, WS_FACET_STRING[this.fWhiteSpace], (this.fFixedFacet & 16) != 0, this.whiteSpaceAnnotation);
                ++n2;
            }
            if (this.fLength != -1) {
                arrxSObject[n2] = new XSFacetImpl(1, Integer.toString(this.fLength), (this.fFixedFacet & 1) != 0, this.lengthAnnotation);
                ++n2;
            }
            if (this.fMinLength != -1) {
                arrxSObject[n2] = new XSFacetImpl(2, Integer.toString(this.fMinLength), (this.fFixedFacet & 2) != 0, this.minLengthAnnotation);
                ++n2;
            }
            if (this.fMaxLength != -1) {
                arrxSObject[n2] = new XSFacetImpl(4, Integer.toString(this.fMaxLength), (this.fFixedFacet & 4) != 0, this.maxLengthAnnotation);
                ++n2;
            }
            if (this.fTotalDigits != -1) {
                arrxSObject[n2] = new XSFacetImpl(512, Integer.toString(this.fTotalDigits), (this.fFixedFacet & 512) != 0, this.totalDigitsAnnotation);
                ++n2;
            }
            if (this.fValidationDV == 24) {
                arrxSObject[n2] = new XSFacetImpl(1024, "0", true, this.fractionDigitsAnnotation);
                ++n2;
            } else if (this.fFractionDigits != -1) {
                arrxSObject[n2] = new XSFacetImpl(1024, Integer.toString(this.fFractionDigits), (this.fFixedFacet & 1024) != 0, this.fractionDigitsAnnotation);
                ++n2;
            }
            if (this.fMaxInclusive != null) {
                arrxSObject[n2] = new XSFacetImpl(32, this.fMaxInclusive.toString(), (this.fFixedFacet & 32) != 0, this.maxInclusiveAnnotation);
                ++n2;
            }
            if (this.fMaxExclusive != null) {
                arrxSObject[n2] = new XSFacetImpl(64, this.fMaxExclusive.toString(), (this.fFixedFacet & 64) != 0, this.maxExclusiveAnnotation);
                ++n2;
            }
            if (this.fMinExclusive != null) {
                arrxSObject[n2] = new XSFacetImpl(128, this.fMinExclusive.toString(), (this.fFixedFacet & 128) != 0, this.minExclusiveAnnotation);
                ++n2;
            }
            if (this.fMinInclusive != null) {
                arrxSObject[n2] = new XSFacetImpl(256, this.fMinInclusive.toString(), (this.fFixedFacet & 256) != 0, this.minInclusiveAnnotation);
                ++n2;
            }
            this.fFacets = new XSObjectListImpl(arrxSObject, n2);
        }
        return this.fFacets != null ? this.fFacets : XSObjectListImpl.EMPTY_LIST;
    }

    public XSObjectList getMultiValueFacets() {
        if (this.fMultiValueFacets == null && ((this.fFacetsDefined & 2048) != 0 || (this.fFacetsDefined & 8) != 0 || this.fPatternType != 0 || this.fValidationDV == 24)) {
            XSObject[] arrxSObject = new XSMVFacetImpl[2];
            int n2 = 0;
            if ((this.fFacetsDefined & 8) != 0 || this.fPatternType != 0 || this.fValidationDV == 24) {
                arrxSObject[n2] = new XSMVFacetImpl(8, this.getLexicalPattern(), this.patternAnnotations);
                ++n2;
            }
            if (this.fEnumeration != null) {
                arrxSObject[n2] = new XSMVFacetImpl(2048, this.getLexicalEnumeration(), this.enumerationAnnotations);
                ++n2;
            }
            this.fMultiValueFacets = new XSObjectListImpl(arrxSObject, n2);
        }
        return this.fMultiValueFacets != null ? this.fMultiValueFacets : XSObjectListImpl.EMPTY_LIST;
    }

    public Object getMinInclusiveValue() {
        return this.fMinInclusive;
    }

    public Object getMinExclusiveValue() {
        return this.fMinExclusive;
    }

    public Object getMaxInclusiveValue() {
        return this.fMaxInclusive;
    }

    public Object getMaxExclusiveValue() {
        return this.fMaxExclusive;
    }

    public void setAnonymous(boolean bl) {
        this.fAnonymous = bl;
    }

    public String getTypeNamespace() {
        return this.getNamespace();
    }

    public boolean isDerivedFrom(String string, String string2, int n2) {
        return this.isDOMDerivedFrom(string, string2, n2);
    }

    private short convertToPrimitiveKind(short s2) {
        if (s2 <= 20) {
            return s2;
        }
        if (s2 <= 29) {
            return 2;
        }
        if (s2 <= 42) {
            return 4;
        }
        return s2;
    }

    static Vector access$100(XSSimpleTypeDecl xSSimpleTypeDecl) {
        return xSSimpleTypeDecl.fEnumeration;
    }

    static ShortList[] access$200(XSSimpleTypeDecl xSSimpleTypeDecl) {
        return xSSimpleTypeDecl.fEnumerationItemType;
    }

    private static abstract class AbstractObjectList
    extends AbstractList
    implements ObjectList {
        private AbstractObjectList() {
        }

        public Object get(int n2) {
            if (n2 >= 0 && n2 < this.getLength()) {
                return this.item(n2);
            }
            throw new IndexOutOfBoundsException("Index: " + n2);
        }

        public int size() {
            return this.getLength();
        }

        public abstract Object item(int var1);

        public abstract int getLength();

        AbstractObjectList( var1_1) {
            this();
        }
    }

    private static final class XSMVFacetImpl
    implements XSMultiValueFacet {
        final short kind;
        final XSObjectList annotations;
        final StringList values;

        public XSMVFacetImpl(short s2, StringList stringList, XSObjectList xSObjectList) {
            this.kind = s2;
            this.values = stringList;
            this.annotations = xSObjectList != null ? xSObjectList : XSObjectListImpl.EMPTY_LIST;
        }

        public short getFacetKind() {
            return this.kind;
        }

        public XSObjectList getAnnotations() {
            return this.annotations;
        }

        public StringList getLexicalFacetValues() {
            return this.values;
        }

        public String getName() {
            return null;
        }

        public String getNamespace() {
            return null;
        }

        public XSNamespaceItem getNamespaceItem() {
            return null;
        }

        public short getType() {
            return 14;
        }
    }

    private static final class XSFacetImpl
    implements XSFacet {
        final short kind;
        final String value;
        final boolean fixed;
        final XSObjectList annotations;

        public XSFacetImpl(short s2, String string, boolean bl, XSAnnotation xSAnnotation) {
            this.kind = s2;
            this.value = string;
            this.fixed = bl;
            if (xSAnnotation != null) {
                this.annotations = new XSObjectListImpl();
                ((XSObjectListImpl)this.annotations).addXSObject(xSAnnotation);
            } else {
                this.annotations = XSObjectListImpl.EMPTY_LIST;
            }
        }

        public XSAnnotation getAnnotation() {
            return (XSAnnotation)this.annotations.item(0);
        }

        public XSObjectList getAnnotations() {
            return this.annotations;
        }

        public short getFacetKind() {
            return this.kind;
        }

        public String getLexicalFacetValue() {
            return this.value;
        }

        public boolean getFixed() {
            return this.fixed;
        }

        public String getName() {
            return null;
        }

        public String getNamespace() {
            return null;
        }

        public XSNamespaceItem getNamespaceItem() {
            return null;
        }

        public short getType() {
            return 13;
        }
    }

    static final class ValidationContextImpl
    implements ValidationContext {
        final ValidationContext fExternal;
        NamespaceContext fNSContext;

        ValidationContextImpl(ValidationContext validationContext) {
            this.fExternal = validationContext;
        }

        void setNSContext(NamespaceContext namespaceContext) {
            this.fNSContext = namespaceContext;
        }

        public boolean needFacetChecking() {
            return this.fExternal.needFacetChecking();
        }

        public boolean needExtraChecking() {
            return this.fExternal.needExtraChecking();
        }

        public boolean needToNormalize() {
            return this.fExternal.needToNormalize();
        }

        public boolean useNamespaces() {
            return true;
        }

        public boolean isEntityDeclared(String string) {
            return this.fExternal.isEntityDeclared(string);
        }

        public boolean isEntityUnparsed(String string) {
            return this.fExternal.isEntityUnparsed(string);
        }

        public boolean isIdDeclared(String string) {
            return this.fExternal.isIdDeclared(string);
        }

        public void addId(String string) {
            this.fExternal.addId(string);
        }

        public void addIdRef(String string) {
            this.fExternal.addIdRef(string);
        }

        public String getSymbol(String string) {
            return this.fExternal.getSymbol(string);
        }

        public String getURI(String string) {
            if (this.fNSContext == null) {
                return this.fExternal.getURI(string);
            }
            return this.fNSContext.getURI(string);
        }

        public Locale getLocale() {
            return this.fExternal.getLocale();
        }
    }

}

