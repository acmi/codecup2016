/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.identity.IdentityConstraint;
import org.apache.xerces.impl.xs.util.XSNamedMapImpl;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTypeDefinition;

public class XSElementDecl
implements XSElementDeclaration {
    public static final short SCOPE_ABSENT = 0;
    public static final short SCOPE_GLOBAL = 1;
    public static final short SCOPE_LOCAL = 2;
    public String fName = null;
    public String fTargetNamespace = null;
    public XSTypeDefinition fType = null;
    public QName fUnresolvedTypeName = null;
    short fMiscFlags = 0;
    public short fScope = 0;
    XSComplexTypeDecl fEnclosingCT = null;
    public short fBlock = 0;
    public short fFinal = 0;
    public XSObjectList fAnnotations = null;
    public ValidatedInfo fDefault = null;
    public XSElementDecl fSubGroup = null;
    static final int INITIAL_SIZE = 2;
    int fIDCPos = 0;
    IdentityConstraint[] fIDConstraints = new IdentityConstraint[2];
    private XSNamespaceItem fNamespaceItem = null;
    private static final short CONSTRAINT_MASK = 3;
    private static final short NILLABLE = 4;
    private static final short ABSTRACT = 8;
    private String fDescription = null;

    public void setConstraintType(short s2) {
        this.fMiscFlags = (short)(this.fMiscFlags ^ this.fMiscFlags & 3);
        this.fMiscFlags = (short)(this.fMiscFlags | s2 & 3);
    }

    public void setIsNillable() {
        this.fMiscFlags = (short)(this.fMiscFlags | 4);
    }

    public void setIsAbstract() {
        this.fMiscFlags = (short)(this.fMiscFlags | 8);
    }

    public void setIsGlobal() {
        this.fScope = 1;
    }

    public void setIsLocal(XSComplexTypeDecl xSComplexTypeDecl) {
        this.fScope = 2;
        this.fEnclosingCT = xSComplexTypeDecl;
    }

    public void addIDConstraint(IdentityConstraint identityConstraint) {
        if (this.fIDCPos == this.fIDConstraints.length) {
            this.fIDConstraints = XSElementDecl.resize(this.fIDConstraints, this.fIDCPos * 2);
        }
        this.fIDConstraints[this.fIDCPos++] = identityConstraint;
    }

    public IdentityConstraint[] getIDConstraints() {
        if (this.fIDCPos == 0) {
            return null;
        }
        if (this.fIDCPos < this.fIDConstraints.length) {
            this.fIDConstraints = XSElementDecl.resize(this.fIDConstraints, this.fIDCPos);
        }
        return this.fIDConstraints;
    }

    static final IdentityConstraint[] resize(IdentityConstraint[] arridentityConstraint, int n2) {
        IdentityConstraint[] arridentityConstraint2 = new IdentityConstraint[n2];
        System.arraycopy(arridentityConstraint, 0, arridentityConstraint2, 0, Math.min(arridentityConstraint.length, n2));
        return arridentityConstraint2;
    }

    public String toString() {
        if (this.fDescription == null) {
            if (this.fTargetNamespace != null) {
                StringBuffer stringBuffer = new StringBuffer(this.fTargetNamespace.length() + (this.fName != null ? this.fName.length() : 4) + 3);
                stringBuffer.append('\"');
                stringBuffer.append(this.fTargetNamespace);
                stringBuffer.append('\"');
                stringBuffer.append(':');
                stringBuffer.append(this.fName);
                this.fDescription = stringBuffer.toString();
            } else {
                this.fDescription = this.fName;
            }
        }
        return this.fDescription;
    }

    public int hashCode() {
        int n2 = this.fName.hashCode();
        if (this.fTargetNamespace != null) {
            n2 = (n2 << 16) + this.fTargetNamespace.hashCode();
        }
        return n2;
    }

    public boolean equals(Object object) {
        return object == this;
    }

    public void reset() {
        this.fScope = 0;
        this.fName = null;
        this.fTargetNamespace = null;
        this.fType = null;
        this.fUnresolvedTypeName = null;
        this.fMiscFlags = 0;
        this.fBlock = 0;
        this.fFinal = 0;
        this.fDefault = null;
        this.fAnnotations = null;
        this.fSubGroup = null;
        int n2 = 0;
        while (n2 < this.fIDCPos) {
            this.fIDConstraints[n2] = null;
            ++n2;
        }
        this.fIDCPos = 0;
    }

    public short getType() {
        return 2;
    }

    public String getName() {
        return this.fName;
    }

    public String getNamespace() {
        return this.fTargetNamespace;
    }

    public XSTypeDefinition getTypeDefinition() {
        return this.fType;
    }

    public short getScope() {
        return this.fScope;
    }

    public XSComplexTypeDefinition getEnclosingCTDefinition() {
        return this.fEnclosingCT;
    }

    public short getConstraintType() {
        return (short)(this.fMiscFlags & 3);
    }

    public String getConstraintValue() {
        return this.getConstraintType() == 0 ? null : this.fDefault.stringValue();
    }

    public boolean getNillable() {
        return (this.fMiscFlags & 4) != 0;
    }

    public XSNamedMap getIdentityConstraints() {
        return new XSNamedMapImpl(this.fIDConstraints, this.fIDCPos);
    }

    public XSElementDeclaration getSubstitutionGroupAffiliation() {
        return this.fSubGroup;
    }

    public boolean isSubstitutionGroupExclusion(short s2) {
        return (this.fFinal & s2) != 0;
    }

    public short getSubstitutionGroupExclusions() {
        return this.fFinal;
    }

    public boolean isDisallowedSubstitution(short s2) {
        return (this.fBlock & s2) != 0;
    }

    public short getDisallowedSubstitutions() {
        return this.fBlock;
    }

    public boolean getAbstract() {
        return (this.fMiscFlags & 8) != 0;
    }

    public XSAnnotation getAnnotation() {
        return this.fAnnotations != null ? (XSAnnotation)this.fAnnotations.item(0) : null;
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

    public Object getActualVC() {
        return this.getConstraintType() == 0 ? null : this.fDefault.actualValue;
    }

    public short getActualVCType() {
        return this.getConstraintType() == 0 ? 45 : this.fDefault.actualValueType;
    }

    public ShortList getItemValueTypes() {
        return this.getConstraintType() == 0 ? null : this.fDefault.itemValueTypes;
    }
}

