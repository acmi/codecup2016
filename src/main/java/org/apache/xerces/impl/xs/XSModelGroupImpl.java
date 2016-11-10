/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;

public class XSModelGroupImpl
implements XSModelGroup {
    public static final short MODELGROUP_CHOICE = 101;
    public static final short MODELGROUP_SEQUENCE = 102;
    public static final short MODELGROUP_ALL = 103;
    public short fCompositor;
    public XSParticleDecl[] fParticles = null;
    public int fParticleCount = 0;
    public XSObjectList fAnnotations = null;
    private String fDescription = null;

    public boolean isEmpty() {
        int n2 = 0;
        while (n2 < this.fParticleCount) {
            if (!this.fParticles[n2].isEmpty()) {
                return false;
            }
            ++n2;
        }
        return true;
    }

    public int minEffectiveTotalRange() {
        if (this.fCompositor == 101) {
            return this.minEffectiveTotalRangeChoice();
        }
        return this.minEffectiveTotalRangeAllSeq();
    }

    private int minEffectiveTotalRangeAllSeq() {
        int n2 = 0;
        int n3 = 0;
        while (n3 < this.fParticleCount) {
            n2 += this.fParticles[n3].minEffectiveTotalRange();
            ++n3;
        }
        return n2;
    }

    private int minEffectiveTotalRangeChoice() {
        int n2 = 0;
        if (this.fParticleCount > 0) {
            n2 = this.fParticles[0].minEffectiveTotalRange();
        }
        int n3 = 1;
        while (n3 < this.fParticleCount) {
            int n4 = this.fParticles[n3].minEffectiveTotalRange();
            if (n4 < n2) {
                n2 = n4;
            }
            ++n3;
        }
        return n2;
    }

    public int maxEffectiveTotalRange() {
        if (this.fCompositor == 101) {
            return this.maxEffectiveTotalRangeChoice();
        }
        return this.maxEffectiveTotalRangeAllSeq();
    }

    private int maxEffectiveTotalRangeAllSeq() {
        int n2 = 0;
        int n3 = 0;
        while (n3 < this.fParticleCount) {
            int n4 = this.fParticles[n3].maxEffectiveTotalRange();
            if (n4 == -1) {
                return -1;
            }
            n2 += n4;
            ++n3;
        }
        return n2;
    }

    private int maxEffectiveTotalRangeChoice() {
        int n2 = 0;
        if (this.fParticleCount > 0 && (n2 = this.fParticles[0].maxEffectiveTotalRange()) == -1) {
            return -1;
        }
        int n3 = 1;
        while (n3 < this.fParticleCount) {
            int n4 = this.fParticles[n3].maxEffectiveTotalRange();
            if (n4 == -1) {
                return -1;
            }
            if (n4 > n2) {
                n2 = n4;
            }
            ++n3;
        }
        return n2;
    }

    public String toString() {
        if (this.fDescription == null) {
            StringBuffer stringBuffer = new StringBuffer();
            if (this.fCompositor == 103) {
                stringBuffer.append("all(");
            } else {
                stringBuffer.append('(');
            }
            if (this.fParticleCount > 0) {
                stringBuffer.append(this.fParticles[0].toString());
            }
            int n2 = 1;
            while (n2 < this.fParticleCount) {
                if (this.fCompositor == 101) {
                    stringBuffer.append('|');
                } else {
                    stringBuffer.append(',');
                }
                stringBuffer.append(this.fParticles[n2].toString());
                ++n2;
            }
            stringBuffer.append(')');
            this.fDescription = stringBuffer.toString();
        }
        return this.fDescription;
    }

    public void reset() {
        this.fCompositor = 102;
        this.fParticles = null;
        this.fParticleCount = 0;
        this.fDescription = null;
        this.fAnnotations = null;
    }

    public short getType() {
        return 7;
    }

    public String getName() {
        return null;
    }

    public String getNamespace() {
        return null;
    }

    public short getCompositor() {
        if (this.fCompositor == 101) {
            return 2;
        }
        if (this.fCompositor == 102) {
            return 1;
        }
        return 3;
    }

    public XSObjectList getParticles() {
        return new XSObjectListImpl(this.fParticles, this.fParticleCount);
    }

    public XSAnnotation getAnnotation() {
        return this.fAnnotations != null ? (XSAnnotation)this.fAnnotations.item(0) : null;
    }

    public XSObjectList getAnnotations() {
        return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
    }

    public XSNamespaceItem getNamespaceItem() {
        return null;
    }
}

