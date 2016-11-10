/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.dv.xs.SchemaDVFactoryImpl;
import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.xerces.impl.xs.XSAttributeDecl;
import org.apache.xerces.impl.xs.XSAttributeUseImpl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.impl.xs.XSParticleDecl;

public final class XSDeclarationPool {
    private static final int CHUNK_SHIFT = 8;
    private static final int CHUNK_SIZE = 256;
    private static final int CHUNK_MASK = 255;
    private static final int INITIAL_CHUNK_COUNT = 4;
    private XSElementDecl[][] fElementDecl = new XSElementDecl[4][];
    private int fElementDeclIndex = 0;
    private XSParticleDecl[][] fParticleDecl = new XSParticleDecl[4][];
    private int fParticleDeclIndex = 0;
    private XSModelGroupImpl[][] fModelGroup = new XSModelGroupImpl[4][];
    private int fModelGroupIndex = 0;
    private XSAttributeDecl[][] fAttrDecl = new XSAttributeDecl[4][];
    private int fAttrDeclIndex = 0;
    private XSComplexTypeDecl[][] fCTDecl = new XSComplexTypeDecl[4][];
    private int fCTDeclIndex = 0;
    private XSSimpleTypeDecl[][] fSTDecl = new XSSimpleTypeDecl[4][];
    private int fSTDeclIndex = 0;
    private XSAttributeUseImpl[][] fAttributeUse = new XSAttributeUseImpl[4][];
    private int fAttributeUseIndex = 0;
    private SchemaDVFactoryImpl dvFactory;

    public void setDVFactory(SchemaDVFactoryImpl schemaDVFactoryImpl) {
        this.dvFactory = schemaDVFactoryImpl;
    }

    public final XSElementDecl getElementDecl() {
        int n2 = this.fElementDeclIndex >> 8;
        int n3 = this.fElementDeclIndex & 255;
        this.ensureElementDeclCapacity(n2);
        if (this.fElementDecl[n2][n3] == null) {
            this.fElementDecl[n2][n3] = new XSElementDecl();
        } else {
            this.fElementDecl[n2][n3].reset();
        }
        ++this.fElementDeclIndex;
        return this.fElementDecl[n2][n3];
    }

    public final XSAttributeDecl getAttributeDecl() {
        int n2 = this.fAttrDeclIndex >> 8;
        int n3 = this.fAttrDeclIndex & 255;
        this.ensureAttrDeclCapacity(n2);
        if (this.fAttrDecl[n2][n3] == null) {
            this.fAttrDecl[n2][n3] = new XSAttributeDecl();
        } else {
            this.fAttrDecl[n2][n3].reset();
        }
        ++this.fAttrDeclIndex;
        return this.fAttrDecl[n2][n3];
    }

    public final XSAttributeUseImpl getAttributeUse() {
        int n2 = this.fAttributeUseIndex >> 8;
        int n3 = this.fAttributeUseIndex & 255;
        this.ensureAttributeUseCapacity(n2);
        if (this.fAttributeUse[n2][n3] == null) {
            this.fAttributeUse[n2][n3] = new XSAttributeUseImpl();
        } else {
            this.fAttributeUse[n2][n3].reset();
        }
        ++this.fAttributeUseIndex;
        return this.fAttributeUse[n2][n3];
    }

    public final XSComplexTypeDecl getComplexTypeDecl() {
        int n2 = this.fCTDeclIndex >> 8;
        int n3 = this.fCTDeclIndex & 255;
        this.ensureCTDeclCapacity(n2);
        if (this.fCTDecl[n2][n3] == null) {
            this.fCTDecl[n2][n3] = new XSComplexTypeDecl();
        } else {
            this.fCTDecl[n2][n3].reset();
        }
        ++this.fCTDeclIndex;
        return this.fCTDecl[n2][n3];
    }

    public final XSSimpleTypeDecl getSimpleTypeDecl() {
        int n2 = this.fSTDeclIndex >> 8;
        int n3 = this.fSTDeclIndex & 255;
        this.ensureSTDeclCapacity(n2);
        if (this.fSTDecl[n2][n3] == null) {
            this.fSTDecl[n2][n3] = this.dvFactory.newXSSimpleTypeDecl();
        } else {
            this.fSTDecl[n2][n3].reset();
        }
        ++this.fSTDeclIndex;
        return this.fSTDecl[n2][n3];
    }

    public final XSParticleDecl getParticleDecl() {
        int n2 = this.fParticleDeclIndex >> 8;
        int n3 = this.fParticleDeclIndex & 255;
        this.ensureParticleDeclCapacity(n2);
        if (this.fParticleDecl[n2][n3] == null) {
            this.fParticleDecl[n2][n3] = new XSParticleDecl();
        } else {
            this.fParticleDecl[n2][n3].reset();
        }
        ++this.fParticleDeclIndex;
        return this.fParticleDecl[n2][n3];
    }

    public final XSModelGroupImpl getModelGroup() {
        int n2 = this.fModelGroupIndex >> 8;
        int n3 = this.fModelGroupIndex & 255;
        this.ensureModelGroupCapacity(n2);
        if (this.fModelGroup[n2][n3] == null) {
            this.fModelGroup[n2][n3] = new XSModelGroupImpl();
        } else {
            this.fModelGroup[n2][n3].reset();
        }
        ++this.fModelGroupIndex;
        return this.fModelGroup[n2][n3];
    }

    private boolean ensureElementDeclCapacity(int n2) {
        if (n2 >= this.fElementDecl.length) {
            this.fElementDecl = XSDeclarationPool.resize(this.fElementDecl, this.fElementDecl.length * 2);
        } else if (this.fElementDecl[n2] != null) {
            return false;
        }
        this.fElementDecl[n2] = new XSElementDecl[256];
        return true;
    }

    private static XSElementDecl[][] resize(XSElementDecl[][] arrxSElementDecl, int n2) {
        XSElementDecl[][] arrxSElementDecl2 = new XSElementDecl[n2][];
        System.arraycopy(arrxSElementDecl, 0, arrxSElementDecl2, 0, arrxSElementDecl.length);
        return arrxSElementDecl2;
    }

    private boolean ensureParticleDeclCapacity(int n2) {
        if (n2 >= this.fParticleDecl.length) {
            this.fParticleDecl = XSDeclarationPool.resize(this.fParticleDecl, this.fParticleDecl.length * 2);
        } else if (this.fParticleDecl[n2] != null) {
            return false;
        }
        this.fParticleDecl[n2] = new XSParticleDecl[256];
        return true;
    }

    private boolean ensureModelGroupCapacity(int n2) {
        if (n2 >= this.fModelGroup.length) {
            this.fModelGroup = XSDeclarationPool.resize(this.fModelGroup, this.fModelGroup.length * 2);
        } else if (this.fModelGroup[n2] != null) {
            return false;
        }
        this.fModelGroup[n2] = new XSModelGroupImpl[256];
        return true;
    }

    private static XSParticleDecl[][] resize(XSParticleDecl[][] arrxSParticleDecl, int n2) {
        XSParticleDecl[][] arrxSParticleDecl2 = new XSParticleDecl[n2][];
        System.arraycopy(arrxSParticleDecl, 0, arrxSParticleDecl2, 0, arrxSParticleDecl.length);
        return arrxSParticleDecl2;
    }

    private static XSModelGroupImpl[][] resize(XSModelGroupImpl[][] arrxSModelGroupImpl, int n2) {
        XSModelGroupImpl[][] arrxSModelGroupImpl2 = new XSModelGroupImpl[n2][];
        System.arraycopy(arrxSModelGroupImpl, 0, arrxSModelGroupImpl2, 0, arrxSModelGroupImpl.length);
        return arrxSModelGroupImpl2;
    }

    private boolean ensureAttrDeclCapacity(int n2) {
        if (n2 >= this.fAttrDecl.length) {
            this.fAttrDecl = XSDeclarationPool.resize(this.fAttrDecl, this.fAttrDecl.length * 2);
        } else if (this.fAttrDecl[n2] != null) {
            return false;
        }
        this.fAttrDecl[n2] = new XSAttributeDecl[256];
        return true;
    }

    private static XSAttributeDecl[][] resize(XSAttributeDecl[][] arrxSAttributeDecl, int n2) {
        XSAttributeDecl[][] arrxSAttributeDecl2 = new XSAttributeDecl[n2][];
        System.arraycopy(arrxSAttributeDecl, 0, arrxSAttributeDecl2, 0, arrxSAttributeDecl.length);
        return arrxSAttributeDecl2;
    }

    private boolean ensureAttributeUseCapacity(int n2) {
        if (n2 >= this.fAttributeUse.length) {
            this.fAttributeUse = XSDeclarationPool.resize(this.fAttributeUse, this.fAttributeUse.length * 2);
        } else if (this.fAttributeUse[n2] != null) {
            return false;
        }
        this.fAttributeUse[n2] = new XSAttributeUseImpl[256];
        return true;
    }

    private static XSAttributeUseImpl[][] resize(XSAttributeUseImpl[][] arrxSAttributeUseImpl, int n2) {
        XSAttributeUseImpl[][] arrxSAttributeUseImpl2 = new XSAttributeUseImpl[n2][];
        System.arraycopy(arrxSAttributeUseImpl, 0, arrxSAttributeUseImpl2, 0, arrxSAttributeUseImpl.length);
        return arrxSAttributeUseImpl2;
    }

    private boolean ensureSTDeclCapacity(int n2) {
        if (n2 >= this.fSTDecl.length) {
            this.fSTDecl = XSDeclarationPool.resize(this.fSTDecl, this.fSTDecl.length * 2);
        } else if (this.fSTDecl[n2] != null) {
            return false;
        }
        this.fSTDecl[n2] = new XSSimpleTypeDecl[256];
        return true;
    }

    private static XSSimpleTypeDecl[][] resize(XSSimpleTypeDecl[][] arrxSSimpleTypeDecl, int n2) {
        XSSimpleTypeDecl[][] arrxSSimpleTypeDecl2 = new XSSimpleTypeDecl[n2][];
        System.arraycopy(arrxSSimpleTypeDecl, 0, arrxSSimpleTypeDecl2, 0, arrxSSimpleTypeDecl.length);
        return arrxSSimpleTypeDecl2;
    }

    private boolean ensureCTDeclCapacity(int n2) {
        if (n2 >= this.fCTDecl.length) {
            this.fCTDecl = XSDeclarationPool.resize(this.fCTDecl, this.fCTDecl.length * 2);
        } else if (this.fCTDecl[n2] != null) {
            return false;
        }
        this.fCTDecl[n2] = new XSComplexTypeDecl[256];
        return true;
    }

    private static XSComplexTypeDecl[][] resize(XSComplexTypeDecl[][] arrxSComplexTypeDecl, int n2) {
        XSComplexTypeDecl[][] arrxSComplexTypeDecl2 = new XSComplexTypeDecl[n2][];
        System.arraycopy(arrxSComplexTypeDecl, 0, arrxSComplexTypeDecl2, 0, arrxSComplexTypeDecl.length);
        return arrxSComplexTypeDecl2;
    }

    public void reset() {
        this.fElementDeclIndex = 0;
        this.fParticleDeclIndex = 0;
        this.fModelGroupIndex = 0;
        this.fSTDeclIndex = 0;
        this.fCTDeclIndex = 0;
        this.fAttrDeclIndex = 0;
        this.fAttributeUseIndex = 0;
    }
}

