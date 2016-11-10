/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SubstitutionGroupHandler;
import org.apache.xerces.impl.xs.XMLSchemaException;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSGrammarBucket;
import org.apache.xerces.impl.xs.XSGroupDecl;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.XSWildcardDecl;
import org.apache.xerces.impl.xs.models.CMBuilder;
import org.apache.xerces.impl.xs.models.XSCMValidator;
import org.apache.xerces.impl.xs.util.SimpleLocator;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;

public class XSConstraints {
    static final int OCCURRENCE_UNKNOWN = -2;
    static final XSSimpleType STRING_TYPE = (XSSimpleType)SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl("string");
    private static XSParticleDecl fEmptyParticle = null;
    private static final Comparator ELEMENT_PARTICLE_COMPARATOR = new Comparator(){

        public int compare(Object object, Object object2) {
            XSParticleDecl xSParticleDecl = (XSParticleDecl)object;
            XSParticleDecl xSParticleDecl2 = (XSParticleDecl)object2;
            XSElementDecl xSElementDecl = (XSElementDecl)xSParticleDecl.fValue;
            XSElementDecl xSElementDecl2 = (XSElementDecl)xSParticleDecl2.fValue;
            String string = xSElementDecl.getNamespace();
            String string2 = xSElementDecl2.getNamespace();
            String string3 = xSElementDecl.getName();
            String string4 = xSElementDecl2.getName();
            boolean bl = string == string2;
            int n2 = 0;
            if (!bl) {
                n2 = string != null ? (string2 != null ? string.compareTo(string2) : 1) : -1;
            }
            return n2 != 0 ? n2 : string3.compareTo(string4);
        }
    };

    public static XSParticleDecl getEmptySequence() {
        if (fEmptyParticle == null) {
            XSModelGroupImpl xSModelGroupImpl = new XSModelGroupImpl();
            xSModelGroupImpl.fCompositor = 102;
            xSModelGroupImpl.fParticleCount = 0;
            xSModelGroupImpl.fParticles = null;
            xSModelGroupImpl.fAnnotations = XSObjectListImpl.EMPTY_LIST;
            XSParticleDecl xSParticleDecl = new XSParticleDecl();
            xSParticleDecl.fType = 3;
            xSParticleDecl.fValue = xSModelGroupImpl;
            xSParticleDecl.fAnnotations = XSObjectListImpl.EMPTY_LIST;
            fEmptyParticle = xSParticleDecl;
        }
        return fEmptyParticle;
    }

    public static boolean checkTypeDerivationOk(XSTypeDefinition xSTypeDefinition, XSTypeDefinition xSTypeDefinition2, short s2) {
        if (xSTypeDefinition == SchemaGrammar.fAnyType) {
            return xSTypeDefinition == xSTypeDefinition2;
        }
        if (xSTypeDefinition == SchemaGrammar.fAnySimpleType) {
            return xSTypeDefinition2 == SchemaGrammar.fAnyType || xSTypeDefinition2 == SchemaGrammar.fAnySimpleType;
        }
        if (xSTypeDefinition.getTypeCategory() == 16) {
            if (xSTypeDefinition2.getTypeCategory() == 15) {
                if (xSTypeDefinition2 == SchemaGrammar.fAnyType) {
                    xSTypeDefinition2 = SchemaGrammar.fAnySimpleType;
                } else {
                    return false;
                }
            }
            return XSConstraints.checkSimpleDerivation((XSSimpleType)xSTypeDefinition, (XSSimpleType)xSTypeDefinition2, s2);
        }
        return XSConstraints.checkComplexDerivation((XSComplexTypeDecl)xSTypeDefinition, xSTypeDefinition2, s2);
    }

    public static boolean checkSimpleDerivationOk(XSSimpleType xSSimpleType, XSTypeDefinition xSTypeDefinition, short s2) {
        if (xSSimpleType == SchemaGrammar.fAnySimpleType) {
            return xSTypeDefinition == SchemaGrammar.fAnyType || xSTypeDefinition == SchemaGrammar.fAnySimpleType;
        }
        if (xSTypeDefinition.getTypeCategory() == 15) {
            if (xSTypeDefinition == SchemaGrammar.fAnyType) {
                xSTypeDefinition = SchemaGrammar.fAnySimpleType;
            } else {
                return false;
            }
        }
        return XSConstraints.checkSimpleDerivation(xSSimpleType, (XSSimpleType)xSTypeDefinition, s2);
    }

    public static boolean checkComplexDerivationOk(XSComplexTypeDecl xSComplexTypeDecl, XSTypeDefinition xSTypeDefinition, short s2) {
        if (xSComplexTypeDecl == SchemaGrammar.fAnyType) {
            return xSComplexTypeDecl == xSTypeDefinition;
        }
        return XSConstraints.checkComplexDerivation(xSComplexTypeDecl, xSTypeDefinition, s2);
    }

    private static boolean checkSimpleDerivation(XSSimpleType xSSimpleType, XSSimpleType xSSimpleType2, short s2) {
        if (xSSimpleType == xSSimpleType2) {
            return true;
        }
        if ((s2 & 2) != 0 || (xSSimpleType.getBaseType().getFinal() & 2) != 0) {
            return false;
        }
        XSSimpleType xSSimpleType3 = (XSSimpleType)xSSimpleType.getBaseType();
        if (xSSimpleType3 == xSSimpleType2) {
            return true;
        }
        if (xSSimpleType3 != SchemaGrammar.fAnySimpleType && XSConstraints.checkSimpleDerivation(xSSimpleType3, xSSimpleType2, s2)) {
            return true;
        }
        if ((xSSimpleType.getVariety() == 2 || xSSimpleType.getVariety() == 3) && xSSimpleType2 == SchemaGrammar.fAnySimpleType) {
            return true;
        }
        if (xSSimpleType2.getVariety() == 3) {
            XSObjectList xSObjectList = xSSimpleType2.getMemberTypes();
            int n2 = xSObjectList.getLength();
            int n3 = 0;
            while (n3 < n2) {
                xSSimpleType2 = (XSSimpleType)xSObjectList.item(n3);
                if (XSConstraints.checkSimpleDerivation(xSSimpleType, xSSimpleType2, s2)) {
                    return true;
                }
                ++n3;
            }
        }
        return false;
    }

    private static boolean checkComplexDerivation(XSComplexTypeDecl xSComplexTypeDecl, XSTypeDefinition xSTypeDefinition, short s2) {
        if (xSComplexTypeDecl == xSTypeDefinition) {
            return true;
        }
        if ((xSComplexTypeDecl.fDerivedBy & s2) != 0) {
            return false;
        }
        XSTypeDefinition xSTypeDefinition2 = xSComplexTypeDecl.fBaseType;
        if (xSTypeDefinition2 == xSTypeDefinition) {
            return true;
        }
        if (xSTypeDefinition2 == SchemaGrammar.fAnyType || xSTypeDefinition2 == SchemaGrammar.fAnySimpleType) {
            return false;
        }
        if (xSTypeDefinition2.getTypeCategory() == 15) {
            return XSConstraints.checkComplexDerivation((XSComplexTypeDecl)xSTypeDefinition2, xSTypeDefinition, s2);
        }
        if (xSTypeDefinition2.getTypeCategory() == 16) {
            if (xSTypeDefinition.getTypeCategory() == 15) {
                if (xSTypeDefinition == SchemaGrammar.fAnyType) {
                    xSTypeDefinition = SchemaGrammar.fAnySimpleType;
                } else {
                    return false;
                }
            }
            return XSConstraints.checkSimpleDerivation((XSSimpleType)xSTypeDefinition2, (XSSimpleType)xSTypeDefinition, s2);
        }
        return false;
    }

    public static Object ElementDefaultValidImmediate(XSTypeDefinition xSTypeDefinition, String string, ValidationContext validationContext, ValidatedInfo validatedInfo) {
        Object object;
        XSSimpleType xSSimpleType = null;
        if (xSTypeDefinition.getTypeCategory() == 16) {
            xSSimpleType = (XSSimpleType)xSTypeDefinition;
        } else {
            object = (XSComplexTypeDecl)xSTypeDefinition;
            if (object.fContentType == 1) {
                xSSimpleType = object.fXSSimpleType;
            } else if (object.fContentType == 3) {
                if (!((XSParticleDecl)object.getParticle()).emptiable()) {
                    return null;
                }
            } else {
                return null;
            }
        }
        object = null;
        if (xSSimpleType == null) {
            xSSimpleType = STRING_TYPE;
        }
        try {
            object = xSSimpleType.validate(string, validationContext, validatedInfo);
            if (validatedInfo != null) {
                object = xSSimpleType.validate(validatedInfo.stringValue(), validationContext, validatedInfo);
            }
        }
        catch (InvalidDatatypeValueException invalidDatatypeValueException) {
            return null;
        }
        return object;
    }

    static void reportSchemaError(XMLErrorReporter xMLErrorReporter, SimpleLocator simpleLocator, String string, Object[] arrobject) {
        if (simpleLocator != null) {
            xMLErrorReporter.reportError(simpleLocator, "http://www.w3.org/TR/xml-schema-1", string, arrobject, 1);
        } else {
            xMLErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", string, arrobject, 1);
        }
    }

    public static void fullSchemaChecking(XSGrammarBucket xSGrammarBucket, SubstitutionGroupHandler substitutionGroupHandler, CMBuilder cMBuilder, XMLErrorReporter xMLErrorReporter) {
        int n2;
        SimpleLocator[] arrsimpleLocator;
        Object object;
        Object object2;
        XSGroupDecl[] arrxSGroupDecl;
        SchemaGrammar[] arrschemaGrammar = xSGrammarBucket.getGrammars();
        int n3 = arrschemaGrammar.length - 1;
        while (n3 >= 0) {
            substitutionGroupHandler.addSubstitutionGroup(arrschemaGrammar[n3].getSubstitutionGroups());
            --n3;
        }
        XSParticleDecl xSParticleDecl = new XSParticleDecl();
        XSParticleDecl xSParticleDecl2 = new XSParticleDecl();
        xSParticleDecl.fType = 3;
        xSParticleDecl2.fType = 3;
        int n4 = arrschemaGrammar.length - 1;
        while (n4 >= 0) {
            arrxSGroupDecl = arrschemaGrammar[n4].getRedefinedGroupDecls();
            arrsimpleLocator = arrschemaGrammar[n4].getRGLocators();
            n2 = 0;
            while (n2 < arrxSGroupDecl.length) {
                XSGroupDecl xSGroupDecl = arrxSGroupDecl[n2++];
                XSModelGroupImpl xSModelGroupImpl = xSGroupDecl.fModelGroup;
                object2 = arrxSGroupDecl[n2++];
                XSModelGroupImpl xSModelGroupImpl2 = object2.fModelGroup;
                xSParticleDecl.fValue = xSModelGroupImpl;
                xSParticleDecl2.fValue = xSModelGroupImpl2;
                if (xSModelGroupImpl2 == null) {
                    if (xSModelGroupImpl == null) continue;
                    XSConstraints.reportSchemaError(xMLErrorReporter, arrsimpleLocator[n2 / 2 - 1], "src-redefine.6.2.2", new Object[]{xSGroupDecl.fName, "rcase-Recurse.2"});
                    continue;
                }
                if (xSModelGroupImpl == null) {
                    if (xSParticleDecl2.emptiable()) continue;
                    XSConstraints.reportSchemaError(xMLErrorReporter, arrsimpleLocator[n2 / 2 - 1], "src-redefine.6.2.2", new Object[]{xSGroupDecl.fName, "rcase-Recurse.2"});
                    continue;
                }
                try {
                    XSConstraints.particleValidRestriction(xSParticleDecl, substitutionGroupHandler, xSParticleDecl2, substitutionGroupHandler);
                    continue;
                }
                catch (XMLSchemaException xMLSchemaException) {
                    object = xMLSchemaException.getKey();
                    XSConstraints.reportSchemaError(xMLErrorReporter, arrsimpleLocator[n2 / 2 - 1], (String)object, xMLSchemaException.getArgs());
                    XSConstraints.reportSchemaError(xMLErrorReporter, arrsimpleLocator[n2 / 2 - 1], "src-redefine.6.2.2", new Object[]{xSGroupDecl.fName, object});
                }
            }
            --n4;
        }
        object2 = new SymbolHash();
        int n5 = arrschemaGrammar.length - 1;
        while (n5 >= 0) {
            int n6 = 0;
            boolean bl = arrschemaGrammar[n5].fFullChecked;
            arrxSGroupDecl = arrschemaGrammar[n5].getUncheckedComplexTypeDecls();
            arrsimpleLocator = arrschemaGrammar[n5].getUncheckedCTLocators();
            int n7 = 0;
            while (n7 < arrxSGroupDecl.length) {
                if (!bl && arrxSGroupDecl[n7].fParticle != null) {
                    object2.clear();
                    try {
                        XSConstraints.checkElementDeclsConsistent((XSComplexTypeDecl)((Object)arrxSGroupDecl[n7]), arrxSGroupDecl[n7].fParticle, (SymbolHash)object2, substitutionGroupHandler);
                    }
                    catch (XMLSchemaException xMLSchemaException) {
                        XSConstraints.reportSchemaError(xMLErrorReporter, arrsimpleLocator[n7], xMLSchemaException.getKey(), xMLSchemaException.getArgs());
                    }
                }
                if (arrxSGroupDecl[n7].fBaseType != null && arrxSGroupDecl[n7].fBaseType != SchemaGrammar.fAnyType && arrxSGroupDecl[n7].fDerivedBy == 2 && arrxSGroupDecl[n7].fBaseType instanceof XSComplexTypeDecl) {
                    object = arrxSGroupDecl[n7].fParticle;
                    XSParticleDecl xSParticleDecl3 = ((XSComplexTypeDecl)arrxSGroupDecl[n7].fBaseType).fParticle;
                    if (object == null) {
                        if (xSParticleDecl3 != null && !xSParticleDecl3.emptiable()) {
                            XSConstraints.reportSchemaError(xMLErrorReporter, arrsimpleLocator[n7], "derivation-ok-restriction.5.3.2", new Object[]{arrxSGroupDecl[n7].fName, arrxSGroupDecl[n7].fBaseType.getName()});
                        }
                    } else if (xSParticleDecl3 != null) {
                        try {
                            XSConstraints.particleValidRestriction(arrxSGroupDecl[n7].fParticle, substitutionGroupHandler, ((XSComplexTypeDecl)arrxSGroupDecl[n7].fBaseType).fParticle, substitutionGroupHandler);
                        }
                        catch (XMLSchemaException xMLSchemaException) {
                            XSConstraints.reportSchemaError(xMLErrorReporter, arrsimpleLocator[n7], xMLSchemaException.getKey(), xMLSchemaException.getArgs());
                            XSConstraints.reportSchemaError(xMLErrorReporter, arrsimpleLocator[n7], "derivation-ok-restriction.5.4.2", new Object[]{arrxSGroupDecl[n7].fName});
                        }
                    } else {
                        XSConstraints.reportSchemaError(xMLErrorReporter, arrsimpleLocator[n7], "derivation-ok-restriction.5.4.2", new Object[]{arrxSGroupDecl[n7].fName});
                    }
                }
                object = arrxSGroupDecl[n7].getContentModel(cMBuilder, true);
                n2 = 0;
                if (object != null) {
                    try {
                        n2 = (int)object.checkUniqueParticleAttribution(substitutionGroupHandler) ? 1 : 0;
                    }
                    catch (XMLSchemaException xMLSchemaException) {
                        XSConstraints.reportSchemaError(xMLErrorReporter, arrsimpleLocator[n7], xMLSchemaException.getKey(), xMLSchemaException.getArgs());
                    }
                }
                if (!bl && n2 != 0) {
                    arrxSGroupDecl[n6++] = arrxSGroupDecl[n7];
                }
                ++n7;
            }
            if (!bl) {
                arrschemaGrammar[n5].setUncheckedTypeNum(n6);
                arrschemaGrammar[n5].fFullChecked = true;
            }
            --n5;
        }
    }

    public static void checkElementDeclsConsistent(XSComplexTypeDecl xSComplexTypeDecl, XSParticleDecl xSParticleDecl, SymbolHash symbolHash, SubstitutionGroupHandler substitutionGroupHandler) throws XMLSchemaException {
        short s2 = xSParticleDecl.fType;
        if (s2 == 2) {
            return;
        }
        if (s2 == 1) {
            XSElementDecl xSElementDecl = (XSElementDecl)xSParticleDecl.fValue;
            XSConstraints.findElemInTable(xSComplexTypeDecl, xSElementDecl, symbolHash);
            if (xSElementDecl.fScope == 1) {
                XSElementDecl[] arrxSElementDecl = substitutionGroupHandler.getSubstitutionGroup(xSElementDecl);
                int n2 = 0;
                while (n2 < arrxSElementDecl.length) {
                    XSConstraints.findElemInTable(xSComplexTypeDecl, arrxSElementDecl[n2], symbolHash);
                    ++n2;
                }
            }
            return;
        }
        XSModelGroupImpl xSModelGroupImpl = (XSModelGroupImpl)xSParticleDecl.fValue;
        int n3 = 0;
        while (n3 < xSModelGroupImpl.fParticleCount) {
            XSConstraints.checkElementDeclsConsistent(xSComplexTypeDecl, xSModelGroupImpl.fParticles[n3], symbolHash, substitutionGroupHandler);
            ++n3;
        }
    }

    public static void findElemInTable(XSComplexTypeDecl xSComplexTypeDecl, XSElementDecl xSElementDecl, SymbolHash symbolHash) throws XMLSchemaException {
        String string = xSElementDecl.fName + "," + xSElementDecl.fTargetNamespace;
        XSElementDecl xSElementDecl2 = null;
        xSElementDecl2 = (XSElementDecl)symbolHash.get(string);
        if (xSElementDecl2 == null) {
            symbolHash.put(string, xSElementDecl);
        } else {
            if (xSElementDecl == xSElementDecl2) {
                return;
            }
            if (xSElementDecl.fType != xSElementDecl2.fType) {
                throw new XMLSchemaException("cos-element-consistent", new Object[]{xSComplexTypeDecl.fName, xSElementDecl.fName});
            }
        }
    }

    private static boolean particleValidRestriction(XSParticleDecl xSParticleDecl, SubstitutionGroupHandler substitutionGroupHandler, XSParticleDecl xSParticleDecl2, SubstitutionGroupHandler substitutionGroupHandler2) throws XMLSchemaException {
        return XSConstraints.particleValidRestriction(xSParticleDecl, substitutionGroupHandler, xSParticleDecl2, substitutionGroupHandler2, true);
    }

    private static boolean particleValidRestriction(XSParticleDecl xSParticleDecl, SubstitutionGroupHandler substitutionGroupHandler, XSParticleDecl arrxSElementDecl, SubstitutionGroupHandler substitutionGroupHandler2, boolean bl) throws XMLSchemaException {
        int n2;
        XSElementDecl[] arrxSElementDecl2;
        int n3;
        Vector<XSParticleDecl> vector = null;
        Vector vector2 = null;
        int n4 = -2;
        int n5 = -2;
        boolean bl2 = false;
        if (xSParticleDecl.isEmpty() && !arrxSElementDecl.emptiable()) {
            throw new XMLSchemaException("cos-particle-restrict.a", null);
        }
        if (!xSParticleDecl.isEmpty() && arrxSElementDecl.isEmpty()) {
            throw new XMLSchemaException("cos-particle-restrict.b", null);
        }
        int n6 = xSParticleDecl.fType;
        if (n6 == 3) {
            n6 = ((XSModelGroupImpl)xSParticleDecl.fValue).fCompositor;
            XSParticleDecl xSParticleDecl2 = XSConstraints.getNonUnaryGroup(xSParticleDecl);
            if (xSParticleDecl2 != xSParticleDecl) {
                xSParticleDecl = xSParticleDecl2;
                n6 = xSParticleDecl.fType;
                if (n6 == 3) {
                    n6 = ((XSModelGroupImpl)xSParticleDecl.fValue).fCompositor;
                }
            }
            vector = XSConstraints.removePointlessChildren(xSParticleDecl);
        }
        int n7 = xSParticleDecl.fMinOccurs;
        int n8 = xSParticleDecl.fMaxOccurs;
        if (substitutionGroupHandler != null && n6 == 1) {
            XSElementDecl xSElementDecl = (XSElementDecl)xSParticleDecl.fValue;
            if (xSElementDecl.fScope == 1 && (arrxSElementDecl2 = substitutionGroupHandler.getSubstitutionGroup(xSElementDecl)).length > 0) {
                n6 = 101;
                n4 = n7;
                n5 = n8;
                vector = new Vector(arrxSElementDecl2.length + 1);
                n2 = 0;
                while (n2 < arrxSElementDecl2.length) {
                    XSConstraints.addElementToParticleVector(vector, arrxSElementDecl2[n2]);
                    ++n2;
                }
                XSConstraints.addElementToParticleVector(vector, xSElementDecl);
                Collections.sort(vector, ELEMENT_PARTICLE_COMPARATOR);
                substitutionGroupHandler = null;
            }
        }
        if ((n3 = arrxSElementDecl.fType) == 3) {
            n3 = ((XSModelGroupImpl)arrxSElementDecl.fValue).fCompositor;
            arrxSElementDecl2 = XSConstraints.getNonUnaryGroup((XSParticleDecl)arrxSElementDecl);
            if (arrxSElementDecl2 != arrxSElementDecl) {
                arrxSElementDecl = arrxSElementDecl2;
                n3 = arrxSElementDecl.fType;
                if (n3 == 3) {
                    n3 = ((XSModelGroupImpl)arrxSElementDecl.fValue).fCompositor;
                }
            }
            vector2 = XSConstraints.removePointlessChildren((XSParticleDecl)arrxSElementDecl);
        }
        int n9 = arrxSElementDecl.fMinOccurs;
        n2 = arrxSElementDecl.fMaxOccurs;
        if (substitutionGroupHandler2 != null && n3 == 1) {
            XSElementDecl[] arrxSElementDecl3;
            XSElementDecl xSElementDecl = (XSElementDecl)arrxSElementDecl.fValue;
            if (xSElementDecl.fScope == 1 && (arrxSElementDecl3 = substitutionGroupHandler2.getSubstitutionGroup(xSElementDecl)).length > 0) {
                n3 = 101;
                vector2 = new Vector(arrxSElementDecl3.length + 1);
                int n10 = 0;
                while (n10 < arrxSElementDecl3.length) {
                    XSConstraints.addElementToParticleVector(vector2, arrxSElementDecl3[n10]);
                    ++n10;
                }
                XSConstraints.addElementToParticleVector(vector2, xSElementDecl);
                Collections.sort(vector2, ELEMENT_PARTICLE_COMPARATOR);
                substitutionGroupHandler2 = null;
                bl2 = true;
            }
        }
        switch (n6) {
            case 1: {
                switch (n3) {
                    case 1: {
                        XSConstraints.checkNameAndTypeOK((XSElementDecl)xSParticleDecl.fValue, n7, n8, (XSElementDecl)arrxSElementDecl.fValue, n9, n2);
                        return bl2;
                    }
                    case 2: {
                        XSConstraints.checkNSCompat((XSElementDecl)xSParticleDecl.fValue, n7, n8, (XSWildcardDecl)arrxSElementDecl.fValue, n9, n2, bl);
                        return bl2;
                    }
                    case 101: {
                        vector = new Vector<XSParticleDecl>();
                        vector.addElement(xSParticleDecl);
                        XSConstraints.checkRecurseLax(vector, 1, 1, substitutionGroupHandler, vector2, n9, n2, substitutionGroupHandler2);
                        return bl2;
                    }
                    case 102: 
                    case 103: {
                        vector = new Vector();
                        vector.addElement(xSParticleDecl);
                        XSConstraints.checkRecurse(vector, 1, 1, substitutionGroupHandler, vector2, n9, n2, substitutionGroupHandler2);
                        return bl2;
                    }
                }
                throw new XMLSchemaException("Internal-Error", new Object[]{"in particleValidRestriction"});
            }
            case 2: {
                switch (n3) {
                    case 2: {
                        XSConstraints.checkNSSubset((XSWildcardDecl)xSParticleDecl.fValue, n7, n8, (XSWildcardDecl)arrxSElementDecl.fValue, n9, n2);
                        return bl2;
                    }
                    case 1: 
                    case 101: 
                    case 102: 
                    case 103: {
                        throw new XMLSchemaException("cos-particle-restrict.2", new Object[]{"any:choice,sequence,all,elt"});
                    }
                }
                throw new XMLSchemaException("Internal-Error", new Object[]{"in particleValidRestriction"});
            }
            case 103: {
                switch (n3) {
                    case 2: {
                        if (n4 == -2) {
                            n4 = xSParticleDecl.minEffectiveTotalRange();
                        }
                        if (n5 == -2) {
                            n5 = xSParticleDecl.maxEffectiveTotalRange();
                        }
                        XSConstraints.checkNSRecurseCheckCardinality(vector, n4, n5, substitutionGroupHandler, (XSParticleDecl)arrxSElementDecl, n9, n2, bl);
                        return bl2;
                    }
                    case 103: {
                        XSConstraints.checkRecurse(vector, n7, n8, substitutionGroupHandler, vector2, n9, n2, substitutionGroupHandler2);
                        return bl2;
                    }
                    case 1: 
                    case 101: 
                    case 102: {
                        throw new XMLSchemaException("cos-particle-restrict.2", new Object[]{"all:choice,sequence,elt"});
                    }
                }
                throw new XMLSchemaException("Internal-Error", new Object[]{"in particleValidRestriction"});
            }
            case 101: {
                switch (n3) {
                    case 2: {
                        if (n4 == -2) {
                            n4 = xSParticleDecl.minEffectiveTotalRange();
                        }
                        if (n5 == -2) {
                            n5 = xSParticleDecl.maxEffectiveTotalRange();
                        }
                        XSConstraints.checkNSRecurseCheckCardinality(vector, n4, n5, substitutionGroupHandler, (XSParticleDecl)arrxSElementDecl, n9, n2, bl);
                        return bl2;
                    }
                    case 101: {
                        XSConstraints.checkRecurseLax(vector, n7, n8, substitutionGroupHandler, vector2, n9, n2, substitutionGroupHandler2);
                        return bl2;
                    }
                    case 1: 
                    case 102: 
                    case 103: {
                        throw new XMLSchemaException("cos-particle-restrict.2", new Object[]{"choice:all,sequence,elt"});
                    }
                }
                throw new XMLSchemaException("Internal-Error", new Object[]{"in particleValidRestriction"});
            }
            case 102: {
                switch (n3) {
                    case 2: {
                        if (n4 == -2) {
                            n4 = xSParticleDecl.minEffectiveTotalRange();
                        }
                        if (n5 == -2) {
                            n5 = xSParticleDecl.maxEffectiveTotalRange();
                        }
                        XSConstraints.checkNSRecurseCheckCardinality(vector, n4, n5, substitutionGroupHandler, (XSParticleDecl)arrxSElementDecl, n9, n2, bl);
                        return bl2;
                    }
                    case 103: {
                        XSConstraints.checkRecurseUnordered(vector, n7, n8, substitutionGroupHandler, vector2, n9, n2, substitutionGroupHandler2);
                        return bl2;
                    }
                    case 102: {
                        XSConstraints.checkRecurse(vector, n7, n8, substitutionGroupHandler, vector2, n9, n2, substitutionGroupHandler2);
                        return bl2;
                    }
                    case 101: {
                        int n11 = n7 * vector.size();
                        int n12 = n8 == -1 ? n8 : n8 * vector.size();
                        XSConstraints.checkMapAndSum(vector, n11, n12, substitutionGroupHandler, vector2, n9, n2, substitutionGroupHandler2);
                        return bl2;
                    }
                    case 1: {
                        throw new XMLSchemaException("cos-particle-restrict.2", new Object[]{"seq:elt"});
                    }
                }
                throw new XMLSchemaException("Internal-Error", new Object[]{"in particleValidRestriction"});
            }
        }
        return bl2;
    }

    private static void addElementToParticleVector(Vector vector, XSElementDecl xSElementDecl) {
        XSParticleDecl xSParticleDecl = new XSParticleDecl();
        xSParticleDecl.fValue = xSElementDecl;
        xSParticleDecl.fType = 1;
        vector.addElement(xSParticleDecl);
    }

    private static XSParticleDecl getNonUnaryGroup(XSParticleDecl xSParticleDecl) {
        if (xSParticleDecl.fType == 1 || xSParticleDecl.fType == 2) {
            return xSParticleDecl;
        }
        if (xSParticleDecl.fMinOccurs == 1 && xSParticleDecl.fMaxOccurs == 1 && xSParticleDecl.fValue != null && ((XSModelGroupImpl)xSParticleDecl.fValue).fParticleCount == 1) {
            return XSConstraints.getNonUnaryGroup(((XSModelGroupImpl)xSParticleDecl.fValue).fParticles[0]);
        }
        return xSParticleDecl;
    }

    private static Vector removePointlessChildren(XSParticleDecl xSParticleDecl) {
        if (xSParticleDecl.fType == 1 || xSParticleDecl.fType == 2) {
            return null;
        }
        Vector vector = new Vector();
        XSModelGroupImpl xSModelGroupImpl = (XSModelGroupImpl)xSParticleDecl.fValue;
        int n2 = 0;
        while (n2 < xSModelGroupImpl.fParticleCount) {
            XSConstraints.gatherChildren(xSModelGroupImpl.fCompositor, xSModelGroupImpl.fParticles[n2], vector);
            ++n2;
        }
        return vector;
    }

    private static void gatherChildren(int n2, XSParticleDecl xSParticleDecl, Vector vector) {
        int n3 = xSParticleDecl.fMinOccurs;
        int n4 = xSParticleDecl.fMaxOccurs;
        short s2 = xSParticleDecl.fType;
        if (s2 == 3) {
            s2 = ((XSModelGroupImpl)xSParticleDecl.fValue).fCompositor;
        }
        if (s2 == 1 || s2 == 2) {
            vector.addElement(xSParticleDecl);
            return;
        }
        if (n3 != 1 || n4 != 1) {
            vector.addElement(xSParticleDecl);
        } else if (n2 == s2) {
            XSModelGroupImpl xSModelGroupImpl = (XSModelGroupImpl)xSParticleDecl.fValue;
            int n5 = 0;
            while (n5 < xSModelGroupImpl.fParticleCount) {
                XSConstraints.gatherChildren(s2, xSModelGroupImpl.fParticles[n5], vector);
                ++n5;
            }
        } else if (!xSParticleDecl.isEmpty()) {
            vector.addElement(xSParticleDecl);
        }
    }

    private static void checkNameAndTypeOK(XSElementDecl xSElementDecl, int n2, int n3, XSElementDecl xSElementDecl2, int n4, int n5) throws XMLSchemaException {
        short s2;
        if (xSElementDecl.fName != xSElementDecl2.fName || xSElementDecl.fTargetNamespace != xSElementDecl2.fTargetNamespace) {
            throw new XMLSchemaException("rcase-NameAndTypeOK.1", new Object[]{xSElementDecl.fName, xSElementDecl.fTargetNamespace, xSElementDecl2.fName, xSElementDecl2.fTargetNamespace});
        }
        if (!xSElementDecl2.getNillable() && xSElementDecl.getNillable()) {
            throw new XMLSchemaException("rcase-NameAndTypeOK.2", new Object[]{xSElementDecl.fName});
        }
        if (!XSConstraints.checkOccurrenceRange(n2, n3, n4, n5)) {
            Object[] arrobject = new Object[5];
            arrobject[0] = xSElementDecl.fName;
            arrobject[1] = Integer.toString(n2);
            arrobject[2] = n3 == -1 ? "unbounded" : Integer.toString(n3);
            arrobject[3] = Integer.toString(n4);
            arrobject[4] = n5 == -1 ? "unbounded" : Integer.toString(n5);
            throw new XMLSchemaException("rcase-NameAndTypeOK.3", arrobject);
        }
        if (xSElementDecl2.getConstraintType() == 2) {
            if (xSElementDecl.getConstraintType() != 2) {
                throw new XMLSchemaException("rcase-NameAndTypeOK.4.a", new Object[]{xSElementDecl.fName, xSElementDecl2.fDefault.stringValue()});
            }
            s2 = 0;
            if (xSElementDecl.fType.getTypeCategory() == 16 || ((XSComplexTypeDecl)xSElementDecl.fType).fContentType == 1) {
                s2 = 1;
            }
            if (s2 == 0 && !xSElementDecl2.fDefault.normalizedValue.equals(xSElementDecl.fDefault.normalizedValue) || s2 != 0 && !xSElementDecl2.fDefault.actualValue.equals(xSElementDecl.fDefault.actualValue)) {
                throw new XMLSchemaException("rcase-NameAndTypeOK.4.b", new Object[]{xSElementDecl.fName, xSElementDecl.fDefault.stringValue(), xSElementDecl2.fDefault.stringValue()});
            }
        }
        XSConstraints.checkIDConstraintRestriction(xSElementDecl, xSElementDecl2);
        s2 = xSElementDecl.fBlock;
        short s3 = xSElementDecl2.fBlock;
        if ((s2 & s3) != s3 || s2 == 0 && s3 != 0) {
            throw new XMLSchemaException("rcase-NameAndTypeOK.6", new Object[]{xSElementDecl.fName});
        }
        if (!XSConstraints.checkTypeDerivationOk(xSElementDecl.fType, xSElementDecl2.fType, 25)) {
            throw new XMLSchemaException("rcase-NameAndTypeOK.7", new Object[]{xSElementDecl.fName, xSElementDecl.fType.getName(), xSElementDecl2.fType.getName()});
        }
    }

    private static void checkIDConstraintRestriction(XSElementDecl xSElementDecl, XSElementDecl xSElementDecl2) throws XMLSchemaException {
    }

    private static boolean checkOccurrenceRange(int n2, int n3, int n4, int n5) {
        if (n2 >= n4 && (n5 == -1 || n3 != -1 && n3 <= n5)) {
            return true;
        }
        return false;
    }

    private static void checkNSCompat(XSElementDecl xSElementDecl, int n2, int n3, XSWildcardDecl xSWildcardDecl, int n4, int n5, boolean bl) throws XMLSchemaException {
        if (bl && !XSConstraints.checkOccurrenceRange(n2, n3, n4, n5)) {
            Object[] arrobject = new Object[5];
            arrobject[0] = xSElementDecl.fName;
            arrobject[1] = Integer.toString(n2);
            arrobject[2] = n3 == -1 ? "unbounded" : Integer.toString(n3);
            arrobject[3] = Integer.toString(n4);
            arrobject[4] = n5 == -1 ? "unbounded" : Integer.toString(n5);
            throw new XMLSchemaException("rcase-NSCompat.2", arrobject);
        }
        if (!xSWildcardDecl.allowNamespace(xSElementDecl.fTargetNamespace)) {
            throw new XMLSchemaException("rcase-NSCompat.1", new Object[]{xSElementDecl.fName, xSElementDecl.fTargetNamespace});
        }
    }

    private static void checkNSSubset(XSWildcardDecl xSWildcardDecl, int n2, int n3, XSWildcardDecl xSWildcardDecl2, int n4, int n5) throws XMLSchemaException {
        if (!XSConstraints.checkOccurrenceRange(n2, n3, n4, n5)) {
            Object[] arrobject = new Object[4];
            arrobject[0] = Integer.toString(n2);
            arrobject[1] = n3 == -1 ? "unbounded" : Integer.toString(n3);
            arrobject[2] = Integer.toString(n4);
            arrobject[3] = n5 == -1 ? "unbounded" : Integer.toString(n5);
            throw new XMLSchemaException("rcase-NSSubset.2", arrobject);
        }
        if (!xSWildcardDecl.isSubsetOf(xSWildcardDecl2)) {
            throw new XMLSchemaException("rcase-NSSubset.1", null);
        }
        if (xSWildcardDecl.weakerProcessContents(xSWildcardDecl2)) {
            throw new XMLSchemaException("rcase-NSSubset.3", new Object[]{xSWildcardDecl.getProcessContentsAsString(), xSWildcardDecl2.getProcessContentsAsString()});
        }
    }

    private static void checkNSRecurseCheckCardinality(Vector vector, int n2, int n3, SubstitutionGroupHandler substitutionGroupHandler, XSParticleDecl xSParticleDecl, int n4, int n5, boolean bl) throws XMLSchemaException {
        if (bl && !XSConstraints.checkOccurrenceRange(n2, n3, n4, n5)) {
            Object[] arrobject = new Object[4];
            arrobject[0] = Integer.toString(n2);
            arrobject[1] = n3 == -1 ? "unbounded" : Integer.toString(n3);
            arrobject[2] = Integer.toString(n4);
            arrobject[3] = n5 == -1 ? "unbounded" : Integer.toString(n5);
            throw new XMLSchemaException("rcase-NSRecurseCheckCardinality.2", arrobject);
        }
        int n6 = vector.size();
        try {
            int n7 = 0;
            while (n7 < n6) {
                XSParticleDecl xSParticleDecl2 = (XSParticleDecl)vector.elementAt(n7);
                XSConstraints.particleValidRestriction(xSParticleDecl2, substitutionGroupHandler, xSParticleDecl, null, false);
                ++n7;
            }
        }
        catch (XMLSchemaException xMLSchemaException) {
            throw new XMLSchemaException("rcase-NSRecurseCheckCardinality.1", null);
        }
    }

    private static void checkRecurse(Vector vector, int n2, int n3, SubstitutionGroupHandler substitutionGroupHandler, Vector vector2, int n4, int n5, SubstitutionGroupHandler substitutionGroupHandler2) throws XMLSchemaException {
        if (!XSConstraints.checkOccurrenceRange(n2, n3, n4, n5)) {
            Object[] arrobject = new Object[4];
            arrobject[0] = Integer.toString(n2);
            arrobject[1] = n3 == -1 ? "unbounded" : Integer.toString(n3);
            arrobject[2] = Integer.toString(n4);
            arrobject[3] = n5 == -1 ? "unbounded" : Integer.toString(n5);
            throw new XMLSchemaException("rcase-Recurse.1", arrobject);
        }
        int n6 = vector.size();
        int n7 = vector2.size();
        int n8 = 0;
        int n9 = 0;
        while (n9 < n6) {
            block8 : {
                XSParticleDecl xSParticleDecl = (XSParticleDecl)vector.elementAt(n9);
                int n10 = n8;
                while (n10 < n7) {
                    XSParticleDecl xSParticleDecl2 = (XSParticleDecl)vector2.elementAt(n10);
                    ++n8;
                    try {
                        XSConstraints.particleValidRestriction(xSParticleDecl, substitutionGroupHandler, xSParticleDecl2, substitutionGroupHandler2);
                        break block8;
                    }
                    catch (XMLSchemaException xMLSchemaException) {
                        if (!xSParticleDecl2.emptiable()) {
                            throw new XMLSchemaException("rcase-Recurse.2", null);
                        }
                        ++n10;
                    }
                }
                throw new XMLSchemaException("rcase-Recurse.2", null);
            }
            ++n9;
        }
        int n11 = n8;
        while (n11 < n7) {
            XSParticleDecl xSParticleDecl = (XSParticleDecl)vector2.elementAt(n11);
            if (!xSParticleDecl.emptiable()) {
                throw new XMLSchemaException("rcase-Recurse.2", null);
            }
            ++n11;
        }
    }

    private static void checkRecurseUnordered(Vector vector, int n2, int n3, SubstitutionGroupHandler substitutionGroupHandler, Vector vector2, int n4, int n5, SubstitutionGroupHandler substitutionGroupHandler2) throws XMLSchemaException {
        if (!XSConstraints.checkOccurrenceRange(n2, n3, n4, n5)) {
            Object[] arrobject = new Object[4];
            arrobject[0] = Integer.toString(n2);
            arrobject[1] = n3 == -1 ? "unbounded" : Integer.toString(n3);
            arrobject[2] = Integer.toString(n4);
            arrobject[3] = n5 == -1 ? "unbounded" : Integer.toString(n5);
            throw new XMLSchemaException("rcase-RecurseUnordered.1", arrobject);
        }
        int n6 = vector.size();
        int n7 = vector2.size();
        boolean[] arrbl = new boolean[n7];
        int n8 = 0;
        while (n8 < n6) {
            block8 : {
                XSParticleDecl xSParticleDecl = (XSParticleDecl)vector.elementAt(n8);
                int n9 = 0;
                while (n9 < n7) {
                    XSParticleDecl xSParticleDecl2 = (XSParticleDecl)vector2.elementAt(n9);
                    try {
                        XSConstraints.particleValidRestriction(xSParticleDecl, substitutionGroupHandler, xSParticleDecl2, substitutionGroupHandler2);
                        if (arrbl[n9]) {
                            throw new XMLSchemaException("rcase-RecurseUnordered.2", null);
                        }
                        arrbl[n9] = true;
                        break block8;
                    }
                    catch (XMLSchemaException xMLSchemaException) {
                        ++n9;
                    }
                }
                throw new XMLSchemaException("rcase-RecurseUnordered.2", null);
            }
            ++n8;
        }
        int n10 = 0;
        while (n10 < n7) {
            XSParticleDecl xSParticleDecl = (XSParticleDecl)vector2.elementAt(n10);
            if (!arrbl[n10] && !xSParticleDecl.emptiable()) {
                throw new XMLSchemaException("rcase-RecurseUnordered.2", null);
            }
            ++n10;
        }
    }

    private static void checkRecurseLax(Vector vector, int n2, int n3, SubstitutionGroupHandler substitutionGroupHandler, Vector vector2, int n4, int n5, SubstitutionGroupHandler substitutionGroupHandler2) throws XMLSchemaException {
        if (!XSConstraints.checkOccurrenceRange(n2, n3, n4, n5)) {
            Object[] arrobject = new Object[4];
            arrobject[0] = Integer.toString(n2);
            arrobject[1] = n3 == -1 ? "unbounded" : Integer.toString(n3);
            arrobject[2] = Integer.toString(n4);
            arrobject[3] = n5 == -1 ? "unbounded" : Integer.toString(n5);
            throw new XMLSchemaException("rcase-RecurseLax.1", arrobject);
        }
        int n6 = vector.size();
        int n7 = vector2.size();
        int n8 = 0;
        int n9 = 0;
        while (n9 < n6) {
            block6 : {
                XSParticleDecl xSParticleDecl = (XSParticleDecl)vector.elementAt(n9);
                int n10 = n8;
                while (n10 < n7) {
                    XSParticleDecl xSParticleDecl2 = (XSParticleDecl)vector2.elementAt(n10);
                    ++n8;
                    try {
                        if (XSConstraints.particleValidRestriction(xSParticleDecl, substitutionGroupHandler, xSParticleDecl2, substitutionGroupHandler2)) {
                            --n8;
                        }
                        break block6;
                    }
                    catch (XMLSchemaException xMLSchemaException) {
                        ++n10;
                    }
                }
                throw new XMLSchemaException("rcase-RecurseLax.2", null);
            }
            ++n9;
        }
    }

    private static void checkMapAndSum(Vector vector, int n2, int n3, SubstitutionGroupHandler substitutionGroupHandler, Vector vector2, int n4, int n5, SubstitutionGroupHandler substitutionGroupHandler2) throws XMLSchemaException {
        if (!XSConstraints.checkOccurrenceRange(n2, n3, n4, n5)) {
            Object[] arrobject = new Object[4];
            arrobject[0] = Integer.toString(n2);
            arrobject[1] = n3 == -1 ? "unbounded" : Integer.toString(n3);
            arrobject[2] = Integer.toString(n4);
            arrobject[3] = n5 == -1 ? "unbounded" : Integer.toString(n5);
            throw new XMLSchemaException("rcase-MapAndSum.2", arrobject);
        }
        int n6 = vector.size();
        int n7 = vector2.size();
        int n8 = 0;
        while (n8 < n6) {
            block5 : {
                XSParticleDecl xSParticleDecl = (XSParticleDecl)vector.elementAt(n8);
                int n9 = 0;
                while (n9 < n7) {
                    XSParticleDecl xSParticleDecl2 = (XSParticleDecl)vector2.elementAt(n9);
                    try {
                        XSConstraints.particleValidRestriction(xSParticleDecl, substitutionGroupHandler, xSParticleDecl2, substitutionGroupHandler2);
                        break block5;
                    }
                    catch (XMLSchemaException xMLSchemaException) {
                        ++n9;
                    }
                }
                throw new XMLSchemaException("rcase-MapAndSum.1", null);
            }
            ++n8;
        }
    }

    public static boolean overlapUPA(XSElementDecl xSElementDecl, XSElementDecl xSElementDecl2, SubstitutionGroupHandler substitutionGroupHandler) {
        if (xSElementDecl.fName == xSElementDecl2.fName && xSElementDecl.fTargetNamespace == xSElementDecl2.fTargetNamespace) {
            return true;
        }
        XSElementDecl[] arrxSElementDecl = substitutionGroupHandler.getSubstitutionGroup(xSElementDecl);
        int n2 = arrxSElementDecl.length - 1;
        while (n2 >= 0) {
            if (arrxSElementDecl[n2].fName == xSElementDecl2.fName && arrxSElementDecl[n2].fTargetNamespace == xSElementDecl2.fTargetNamespace) {
                return true;
            }
            --n2;
        }
        arrxSElementDecl = substitutionGroupHandler.getSubstitutionGroup(xSElementDecl2);
        int n3 = arrxSElementDecl.length - 1;
        while (n3 >= 0) {
            if (arrxSElementDecl[n3].fName == xSElementDecl.fName && arrxSElementDecl[n3].fTargetNamespace == xSElementDecl.fTargetNamespace) {
                return true;
            }
            --n3;
        }
        return false;
    }

    public static boolean overlapUPA(XSElementDecl xSElementDecl, XSWildcardDecl xSWildcardDecl, SubstitutionGroupHandler substitutionGroupHandler) {
        if (xSWildcardDecl.allowNamespace(xSElementDecl.fTargetNamespace)) {
            return true;
        }
        XSElementDecl[] arrxSElementDecl = substitutionGroupHandler.getSubstitutionGroup(xSElementDecl);
        int n2 = arrxSElementDecl.length - 1;
        while (n2 >= 0) {
            if (xSWildcardDecl.allowNamespace(arrxSElementDecl[n2].fTargetNamespace)) {
                return true;
            }
            --n2;
        }
        return false;
    }

    public static boolean overlapUPA(XSWildcardDecl xSWildcardDecl, XSWildcardDecl xSWildcardDecl2) {
        XSWildcardDecl xSWildcardDecl3 = xSWildcardDecl.performIntersectionWith(xSWildcardDecl2, xSWildcardDecl.fProcessContents);
        if (xSWildcardDecl3 == null || xSWildcardDecl3.fType != 3 || xSWildcardDecl3.fNamespaceList.length != 0) {
            return true;
        }
        return false;
    }

    public static boolean overlapUPA(Object object, Object object2, SubstitutionGroupHandler substitutionGroupHandler) {
        if (object instanceof XSElementDecl) {
            if (object2 instanceof XSElementDecl) {
                return XSConstraints.overlapUPA((XSElementDecl)object, (XSElementDecl)object2, substitutionGroupHandler);
            }
            return XSConstraints.overlapUPA((XSElementDecl)object, (XSWildcardDecl)object2, substitutionGroupHandler);
        }
        if (object2 instanceof XSElementDecl) {
            return XSConstraints.overlapUPA((XSElementDecl)object2, (XSWildcardDecl)object, substitutionGroupHandler);
        }
        return XSConstraints.overlapUPA((XSWildcardDecl)object, (XSWildcardDecl)object2);
    }

}

