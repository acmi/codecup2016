/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.xs.XSAttributeDecl;
import org.apache.xerces.impl.xs.XSAttributeUseImpl;
import org.apache.xerces.impl.xs.XSConstraints;
import org.apache.xerces.impl.xs.XSWildcardDecl;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSAttributeGroupDefinition;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSWildcard;

public class XSAttributeGroupDecl
implements XSAttributeGroupDefinition {
    public String fName = null;
    public String fTargetNamespace = null;
    int fAttrUseNum = 0;
    private static final int INITIAL_SIZE = 5;
    XSAttributeUseImpl[] fAttributeUses = new XSAttributeUseImpl[5];
    public XSWildcardDecl fAttributeWC = null;
    public String fIDAttrName = null;
    public XSObjectList fAnnotations;
    protected XSObjectListImpl fAttrUses = null;
    private XSNamespaceItem fNamespaceItem = null;

    public String addAttributeUse(XSAttributeUseImpl xSAttributeUseImpl) {
        if (xSAttributeUseImpl.fUse != 2 && xSAttributeUseImpl.fAttrDecl.fType.isIDType()) {
            if (this.fIDAttrName == null) {
                this.fIDAttrName = xSAttributeUseImpl.fAttrDecl.fName;
            } else {
                return this.fIDAttrName;
            }
        }
        if (this.fAttrUseNum == this.fAttributeUses.length) {
            this.fAttributeUses = XSAttributeGroupDecl.resize(this.fAttributeUses, this.fAttrUseNum * 2);
        }
        this.fAttributeUses[this.fAttrUseNum++] = xSAttributeUseImpl;
        return null;
    }

    public void replaceAttributeUse(XSAttributeUse xSAttributeUse, XSAttributeUseImpl xSAttributeUseImpl) {
        int n2 = 0;
        while (n2 < this.fAttrUseNum) {
            if (this.fAttributeUses[n2] == xSAttributeUse) {
                this.fAttributeUses[n2] = xSAttributeUseImpl;
            }
            ++n2;
        }
    }

    public XSAttributeUse getAttributeUse(String string, String string2) {
        int n2 = 0;
        while (n2 < this.fAttrUseNum) {
            if (this.fAttributeUses[n2].fAttrDecl.fTargetNamespace == string && this.fAttributeUses[n2].fAttrDecl.fName == string2) {
                return this.fAttributeUses[n2];
            }
            ++n2;
        }
        return null;
    }

    public XSAttributeUse getAttributeUseNoProhibited(String string, String string2) {
        int n2 = 0;
        while (n2 < this.fAttrUseNum) {
            if (this.fAttributeUses[n2].fAttrDecl.fTargetNamespace == string && this.fAttributeUses[n2].fAttrDecl.fName == string2 && this.fAttributeUses[n2].fUse != 2) {
                return this.fAttributeUses[n2];
            }
            ++n2;
        }
        return null;
    }

    public void removeProhibitedAttrs() {
        if (this.fAttrUseNum == 0) {
            return;
        }
        int n2 = 0;
        XSAttributeUseImpl[] arrxSAttributeUseImpl = new XSAttributeUseImpl[this.fAttrUseNum];
        int n3 = 0;
        while (n3 < this.fAttrUseNum) {
            if (this.fAttributeUses[n3].fUse != 2) {
                arrxSAttributeUseImpl[n2++] = this.fAttributeUses[n3];
            }
            ++n3;
        }
        this.fAttributeUses = arrxSAttributeUseImpl;
        this.fAttrUseNum = n2;
    }

    public Object[] validRestrictionOf(String string, XSAttributeGroupDecl xSAttributeGroupDecl) {
        int n2;
        Object[] arrobject = null;
        XSAttributeUseImpl xSAttributeUseImpl = null;
        XSAttributeDecl xSAttributeDecl = null;
        XSAttributeUseImpl xSAttributeUseImpl2 = null;
        XSAttributeDecl xSAttributeDecl2 = null;
        int n3 = 0;
        while (n3 < this.fAttrUseNum) {
            xSAttributeUseImpl = this.fAttributeUses[n3];
            xSAttributeDecl = xSAttributeUseImpl.fAttrDecl;
            xSAttributeUseImpl2 = (XSAttributeUseImpl)xSAttributeGroupDecl.getAttributeUse(xSAttributeDecl.fTargetNamespace, xSAttributeDecl.fName);
            if (xSAttributeUseImpl2 != null) {
                if (xSAttributeUseImpl2.getRequired() && !xSAttributeUseImpl.getRequired()) {
                    Object[] arrobject2 = new Object[4];
                    arrobject2[0] = string;
                    arrobject2[1] = xSAttributeDecl.fName;
                    arrobject2[2] = xSAttributeUseImpl.fUse == 0 ? "optional" : "prohibited";
                    arrobject2[3] = "derivation-ok-restriction.2.1.1";
                    arrobject = arrobject2;
                    return arrobject;
                }
                if (xSAttributeUseImpl.fUse != 2) {
                    short s2;
                    xSAttributeDecl2 = xSAttributeUseImpl2.fAttrDecl;
                    if (!XSConstraints.checkSimpleDerivationOk(xSAttributeDecl.fType, xSAttributeDecl2.fType, xSAttributeDecl2.fType.getFinal())) {
                        arrobject = new Object[]{string, xSAttributeDecl.fName, xSAttributeDecl.fType.getName(), xSAttributeDecl2.fType.getName(), "derivation-ok-restriction.2.1.2"};
                        return arrobject;
                    }
                    n2 = xSAttributeUseImpl2.fConstraintType != 0 ? xSAttributeUseImpl2.fConstraintType : xSAttributeDecl2.getConstraintType();
                    short s3 = s2 = xSAttributeUseImpl.fConstraintType != 0 ? xSAttributeUseImpl.fConstraintType : xSAttributeDecl.getConstraintType();
                    if (n2 == 2) {
                        ValidatedInfo validatedInfo;
                        if (s2 != 2) {
                            arrobject = new Object[]{string, xSAttributeDecl.fName, "derivation-ok-restriction.2.1.3.a"};
                            return arrobject;
                        }
                        ValidatedInfo validatedInfo2 = xSAttributeUseImpl2.fDefault != null ? xSAttributeUseImpl2.fDefault : xSAttributeDecl2.fDefault;
                        ValidatedInfo validatedInfo3 = validatedInfo = xSAttributeUseImpl.fDefault != null ? xSAttributeUseImpl.fDefault : xSAttributeDecl.fDefault;
                        if (!validatedInfo2.actualValue.equals(validatedInfo.actualValue)) {
                            arrobject = new Object[]{string, xSAttributeDecl.fName, validatedInfo.stringValue(), validatedInfo2.stringValue(), "derivation-ok-restriction.2.1.3.b"};
                            return arrobject;
                        }
                    }
                }
            } else {
                if (xSAttributeGroupDecl.fAttributeWC == null) {
                    arrobject = new Object[]{string, xSAttributeDecl.fName, "derivation-ok-restriction.2.2.a"};
                    return arrobject;
                }
                if (!xSAttributeGroupDecl.fAttributeWC.allowNamespace(xSAttributeDecl.fTargetNamespace)) {
                    Object[] arrobject3 = new Object[4];
                    arrobject3[0] = string;
                    arrobject3[1] = xSAttributeDecl.fName;
                    arrobject3[2] = xSAttributeDecl.fTargetNamespace == null ? "" : xSAttributeDecl.fTargetNamespace;
                    arrobject3[3] = "derivation-ok-restriction.2.2.b";
                    arrobject = arrobject3;
                    return arrobject;
                }
            }
            ++n3;
        }
        n2 = 0;
        while (n2 < xSAttributeGroupDecl.fAttrUseNum) {
            xSAttributeUseImpl2 = xSAttributeGroupDecl.fAttributeUses[n2];
            if (xSAttributeUseImpl2.fUse == 1) {
                xSAttributeDecl2 = xSAttributeUseImpl2.fAttrDecl;
                if (this.getAttributeUse(xSAttributeDecl2.fTargetNamespace, xSAttributeDecl2.fName) == null) {
                    arrobject = new Object[]{string, xSAttributeUseImpl2.fAttrDecl.fName, "derivation-ok-restriction.3"};
                    return arrobject;
                }
            }
            ++n2;
        }
        if (this.fAttributeWC != null) {
            if (xSAttributeGroupDecl.fAttributeWC == null) {
                arrobject = new Object[]{string, "derivation-ok-restriction.4.1"};
                return arrobject;
            }
            if (!this.fAttributeWC.isSubsetOf(xSAttributeGroupDecl.fAttributeWC)) {
                arrobject = new Object[]{string, "derivation-ok-restriction.4.2"};
                return arrobject;
            }
            if (this.fAttributeWC.weakerProcessContents(xSAttributeGroupDecl.fAttributeWC)) {
                arrobject = new Object[]{string, this.fAttributeWC.getProcessContentsAsString(), xSAttributeGroupDecl.fAttributeWC.getProcessContentsAsString(), "derivation-ok-restriction.4.3"};
                return arrobject;
            }
        }
        return null;
    }

    static final XSAttributeUseImpl[] resize(XSAttributeUseImpl[] arrxSAttributeUseImpl, int n2) {
        XSAttributeUseImpl[] arrxSAttributeUseImpl2 = new XSAttributeUseImpl[n2];
        System.arraycopy(arrxSAttributeUseImpl, 0, arrxSAttributeUseImpl2, 0, Math.min(arrxSAttributeUseImpl.length, n2));
        return arrxSAttributeUseImpl2;
    }

    public void reset() {
        this.fName = null;
        this.fTargetNamespace = null;
        int n2 = 0;
        while (n2 < this.fAttrUseNum) {
            this.fAttributeUses[n2] = null;
            ++n2;
        }
        this.fAttrUseNum = 0;
        this.fAttributeWC = null;
        this.fAnnotations = null;
        this.fIDAttrName = null;
    }

    public short getType() {
        return 5;
    }

    public String getName() {
        return this.fName;
    }

    public String getNamespace() {
        return this.fTargetNamespace;
    }

    public XSObjectList getAttributeUses() {
        if (this.fAttrUses == null) {
            this.fAttrUses = new XSObjectListImpl(this.fAttributeUses, this.fAttrUseNum);
        }
        return this.fAttrUses;
    }

    public XSWildcard getAttributeWildcard() {
        return this.fAttributeWC;
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
}

