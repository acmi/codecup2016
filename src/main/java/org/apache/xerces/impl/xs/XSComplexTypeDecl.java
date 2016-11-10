/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAttributeGroupDecl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.models.CMBuilder;
import org.apache.xerces.impl.xs.models.XSCMValidator;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.xs.XSWildcard;
import org.w3c.dom.TypeInfo;

public class XSComplexTypeDecl
implements XSComplexTypeDefinition,
TypeInfo {
    String fName = null;
    String fTargetNamespace = null;
    XSTypeDefinition fBaseType = null;
    short fDerivedBy = 2;
    short fFinal = 0;
    short fBlock = 0;
    short fMiscFlags = 0;
    XSAttributeGroupDecl fAttrGrp = null;
    short fContentType = 0;
    XSSimpleType fXSSimpleType = null;
    XSParticleDecl fParticle = null;
    XSCMValidator fCMValidator = null;
    XSCMValidator fUPACMValidator = null;
    XSObjectListImpl fAnnotations = null;
    private XSNamespaceItem fNamespaceItem = null;
    static final int DERIVATION_ANY = 0;
    static final int DERIVATION_RESTRICTION = 1;
    static final int DERIVATION_EXTENSION = 2;
    static final int DERIVATION_UNION = 4;
    static final int DERIVATION_LIST = 8;
    private static final short CT_IS_ABSTRACT = 1;
    private static final short CT_HAS_TYPE_ID = 2;
    private static final short CT_IS_ANONYMOUS = 4;

    public void setValues(String string, String string2, XSTypeDefinition xSTypeDefinition, short s2, short s3, short s4, short s5, boolean bl, XSAttributeGroupDecl xSAttributeGroupDecl, XSSimpleType xSSimpleType, XSParticleDecl xSParticleDecl, XSObjectListImpl xSObjectListImpl) {
        this.fTargetNamespace = string2;
        this.fBaseType = xSTypeDefinition;
        this.fDerivedBy = s2;
        this.fFinal = s3;
        this.fBlock = s4;
        this.fContentType = s5;
        if (bl) {
            this.fMiscFlags = (short)(this.fMiscFlags | 1);
        }
        this.fAttrGrp = xSAttributeGroupDecl;
        this.fXSSimpleType = xSSimpleType;
        this.fParticle = xSParticleDecl;
        this.fAnnotations = xSObjectListImpl;
    }

    public void setName(String string) {
        this.fName = string;
    }

    public short getTypeCategory() {
        return 15;
    }

    public String getTypeName() {
        return this.fName;
    }

    public short getFinalSet() {
        return this.fFinal;
    }

    public String getTargetNamespace() {
        return this.fTargetNamespace;
    }

    public boolean containsTypeID() {
        return (this.fMiscFlags & 2) != 0;
    }

    public void setIsAbstractType() {
        this.fMiscFlags = (short)(this.fMiscFlags | 1);
    }

    public void setContainsTypeID() {
        this.fMiscFlags = (short)(this.fMiscFlags | 2);
    }

    public void setIsAnonymous() {
        this.fMiscFlags = (short)(this.fMiscFlags | 4);
    }

    public XSCMValidator getContentModel(CMBuilder cMBuilder) {
        return this.getContentModel(cMBuilder, false);
    }

    public synchronized XSCMValidator getContentModel(CMBuilder cMBuilder, boolean bl) {
        if (this.fCMValidator == null) {
            if (bl) {
                if (this.fUPACMValidator == null) {
                    this.fUPACMValidator = cMBuilder.getContentModel(this, true);
                    if (this.fUPACMValidator != null && !this.fUPACMValidator.isCompactedForUPA()) {
                        this.fCMValidator = this.fUPACMValidator;
                    }
                }
                return this.fUPACMValidator;
            }
            this.fCMValidator = cMBuilder.getContentModel(this, false);
        }
        return this.fCMValidator;
    }

    public XSAttributeGroupDecl getAttrGrp() {
        return this.fAttrGrp;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        this.appendTypeInfo(stringBuffer);
        return stringBuffer.toString();
    }

    void appendTypeInfo(StringBuffer stringBuffer) {
        String[] arrstring = new String[]{"EMPTY", "SIMPLE", "ELEMENT", "MIXED"};
        String[] arrstring2 = new String[]{"EMPTY", "EXTENSION", "RESTRICTION"};
        stringBuffer.append("Complex type name='").append(this.fTargetNamespace).append(",").append(this.getTypeName()).append("', ");
        if (this.fBaseType != null) {
            stringBuffer.append(" base type name='").append(this.fBaseType.getName()).append("', ");
        }
        stringBuffer.append(" content type='").append(arrstring[this.fContentType]).append("', ");
        stringBuffer.append(" isAbstract='").append(this.getAbstract()).append("', ");
        stringBuffer.append(" hasTypeId='").append(this.containsTypeID()).append("', ");
        stringBuffer.append(" final='").append(this.fFinal).append("', ");
        stringBuffer.append(" block='").append(this.fBlock).append("', ");
        if (this.fParticle != null) {
            stringBuffer.append(" particle='").append(this.fParticle.toString()).append("', ");
        }
        stringBuffer.append(" derivedBy='").append(arrstring2[this.fDerivedBy]).append("'. ");
    }

    public boolean derivedFromType(XSTypeDefinition xSTypeDefinition, short s2) {
        if (xSTypeDefinition == null) {
            return false;
        }
        if (xSTypeDefinition == SchemaGrammar.fAnyType) {
            return true;
        }
        XSTypeDefinition xSTypeDefinition2 = this;
        while (xSTypeDefinition2 != xSTypeDefinition && xSTypeDefinition2 != SchemaGrammar.fAnySimpleType && xSTypeDefinition2 != SchemaGrammar.fAnyType) {
            xSTypeDefinition2 = xSTypeDefinition2.getBaseType();
        }
        return xSTypeDefinition2 == xSTypeDefinition;
    }

    public boolean derivedFrom(String string, String string2, short s2) {
        if (string2 == null) {
            return false;
        }
        if (string != null && string.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && string2.equals("anyType")) {
            return true;
        }
        XSTypeDefinition xSTypeDefinition = this;
        while (!(string2.equals(xSTypeDefinition.getName()) && (string == null && xSTypeDefinition.getNamespace() == null || string != null && string.equals(xSTypeDefinition.getNamespace())) || xSTypeDefinition == SchemaGrammar.fAnySimpleType || xSTypeDefinition == SchemaGrammar.fAnyType)) {
            xSTypeDefinition = xSTypeDefinition.getBaseType();
        }
        return xSTypeDefinition != SchemaGrammar.fAnySimpleType && xSTypeDefinition != SchemaGrammar.fAnyType;
    }

    public boolean isDOMDerivedFrom(String string, String string2, int n2) {
        if (string2 == null) {
            return false;
        }
        if (string != null && string.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && string2.equals("anyType") && n2 == 1 && n2 == 2) {
            return true;
        }
        if ((n2 & 1) != 0 && this.isDerivedByRestriction(string, string2, n2, this)) {
            return true;
        }
        if ((n2 & 2) != 0 && this.isDerivedByExtension(string, string2, n2, this)) {
            return true;
        }
        if (((n2 & 8) != 0 || (n2 & 4) != 0) && (n2 & 1) == 0 && (n2 & 2) == 0) {
            if (string.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && string2.equals("anyType")) {
                string2 = "anySimpleType";
            }
            if (!this.fName.equals("anyType") || !this.fTargetNamespace.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) {
                if (this.fBaseType != null && this.fBaseType instanceof XSSimpleTypeDecl) {
                    return ((XSSimpleTypeDecl)this.fBaseType).isDOMDerivedFrom(string, string2, n2);
                }
                if (this.fBaseType != null && this.fBaseType instanceof XSComplexTypeDecl) {
                    return ((XSComplexTypeDecl)this.fBaseType).isDOMDerivedFrom(string, string2, n2);
                }
            }
        }
        if ((n2 & 2) == 0 && (n2 & 1) == 0 && (n2 & 8) == 0 && (n2 & 4) == 0) {
            return this.isDerivedByAny(string, string2, n2, this);
        }
        return false;
    }

    private boolean isDerivedByAny(String string, String string2, int n2, XSTypeDefinition xSTypeDefinition) {
        XSTypeDefinition xSTypeDefinition2 = null;
        boolean bl = false;
        while (xSTypeDefinition != null && xSTypeDefinition != xSTypeDefinition2) {
            if (string2.equals(xSTypeDefinition.getName()) && (string == null && xSTypeDefinition.getNamespace() == null || string != null && string.equals(xSTypeDefinition.getNamespace()))) {
                bl = true;
                break;
            }
            if (this.isDerivedByRestriction(string, string2, n2, xSTypeDefinition)) {
                return true;
            }
            if (!this.isDerivedByExtension(string, string2, n2, xSTypeDefinition)) {
                return true;
            }
            xSTypeDefinition2 = xSTypeDefinition;
            xSTypeDefinition = xSTypeDefinition.getBaseType();
        }
        return bl;
    }

    private boolean isDerivedByRestriction(String string, String string2, int n2, XSTypeDefinition xSTypeDefinition) {
        XSTypeDefinition xSTypeDefinition2 = null;
        while (xSTypeDefinition != null && xSTypeDefinition != xSTypeDefinition2) {
            if (string != null && string.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && string2.equals("anySimpleType")) {
                return false;
            }
            if (string2.equals(xSTypeDefinition.getName()) && string != null && string.equals(xSTypeDefinition.getNamespace()) || xSTypeDefinition.getNamespace() == null && string == null) {
                return true;
            }
            if (xSTypeDefinition instanceof XSSimpleTypeDecl) {
                if (string.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && string2.equals("anyType")) {
                    string2 = "anySimpleType";
                }
                return ((XSSimpleTypeDecl)xSTypeDefinition).isDOMDerivedFrom(string, string2, n2);
            }
            if (((XSComplexTypeDecl)xSTypeDefinition).getDerivationMethod() != 2) {
                return false;
            }
            xSTypeDefinition2 = xSTypeDefinition;
            xSTypeDefinition = xSTypeDefinition.getBaseType();
        }
        return false;
    }

    private boolean isDerivedByExtension(String string, String string2, int n2, XSTypeDefinition xSTypeDefinition) {
        boolean bl = false;
        XSTypeDefinition xSTypeDefinition2 = null;
        while (xSTypeDefinition != null && xSTypeDefinition != xSTypeDefinition2) {
            if (string != null && string.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && string2.equals("anySimpleType") && SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(xSTypeDefinition.getNamespace()) && "anyType".equals(xSTypeDefinition.getName())) break;
            if (string2.equals(xSTypeDefinition.getName()) && (string == null && xSTypeDefinition.getNamespace() == null || string != null && string.equals(xSTypeDefinition.getNamespace()))) {
                return bl;
            }
            if (xSTypeDefinition instanceof XSSimpleTypeDecl) {
                if (string.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && string2.equals("anyType")) {
                    string2 = "anySimpleType";
                }
                if ((n2 & 2) != 0) {
                    return bl & ((XSSimpleTypeDecl)xSTypeDefinition).isDOMDerivedFrom(string, string2, n2 & 1);
                }
                return bl & ((XSSimpleTypeDecl)xSTypeDefinition).isDOMDerivedFrom(string, string2, n2);
            }
            if (((XSComplexTypeDecl)xSTypeDefinition).getDerivationMethod() == 1) {
                bl |= true;
            }
            xSTypeDefinition2 = xSTypeDefinition;
            xSTypeDefinition = xSTypeDefinition.getBaseType();
        }
        return false;
    }

    public void reset() {
        this.fName = null;
        this.fTargetNamespace = null;
        this.fBaseType = null;
        this.fDerivedBy = 2;
        this.fFinal = 0;
        this.fBlock = 0;
        this.fMiscFlags = 0;
        this.fAttrGrp.reset();
        this.fContentType = 0;
        this.fXSSimpleType = null;
        this.fParticle = null;
        this.fCMValidator = null;
        this.fUPACMValidator = null;
        if (this.fAnnotations != null) {
            this.fAnnotations.clearXSObjectList();
        }
        this.fAnnotations = null;
    }

    public short getType() {
        return 3;
    }

    public String getName() {
        return this.getAnonymous() ? null : this.fName;
    }

    public boolean getAnonymous() {
        return (this.fMiscFlags & 4) != 0;
    }

    public String getNamespace() {
        return this.fTargetNamespace;
    }

    public XSTypeDefinition getBaseType() {
        return this.fBaseType;
    }

    public short getDerivationMethod() {
        return this.fDerivedBy;
    }

    public boolean isFinal(short s2) {
        return (this.fFinal & s2) != 0;
    }

    public short getFinal() {
        return this.fFinal;
    }

    public boolean getAbstract() {
        return (this.fMiscFlags & 1) != 0;
    }

    public XSObjectList getAttributeUses() {
        return this.fAttrGrp.getAttributeUses();
    }

    public XSWildcard getAttributeWildcard() {
        return this.fAttrGrp.getAttributeWildcard();
    }

    public short getContentType() {
        return this.fContentType;
    }

    public XSSimpleTypeDefinition getSimpleType() {
        return this.fXSSimpleType;
    }

    public XSParticle getParticle() {
        return this.fParticle;
    }

    public boolean isProhibitedSubstitution(short s2) {
        return (this.fBlock & s2) != 0;
    }

    public short getProhibitedSubstitutions() {
        return this.fBlock;
    }

    public XSObjectList getAnnotations() {
        return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
    }

    public XSNamespaceItem getNamespaceItem() {
        return this.fNamespaceItem;
    }

    void setNamespaceItem(XSNamespaceItem xSNamespaceItem) {
        this.fNamespaceItem = xSNamespaceItem;
    }

    public XSAttributeUse getAttributeUse(String string, String string2) {
        return this.fAttrGrp.getAttributeUse(string, string2);
    }

    public String getTypeNamespace() {
        return this.getNamespace();
    }

    public boolean isDerivedFrom(String string, String string2, int n2) {
        return this.isDOMDerivedFrom(string, string2, n2);
    }
}

