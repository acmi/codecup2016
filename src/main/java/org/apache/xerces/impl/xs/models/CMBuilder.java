/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.models;

import org.apache.xerces.impl.dtd.models.CMNode;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSDeclarationPool;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.models.CMNodeFactory;
import org.apache.xerces.impl.xs.models.XSAllCM;
import org.apache.xerces.impl.xs.models.XSCMBinOp;
import org.apache.xerces.impl.xs.models.XSCMLeaf;
import org.apache.xerces.impl.xs.models.XSCMUniOp;
import org.apache.xerces.impl.xs.models.XSCMValidator;
import org.apache.xerces.impl.xs.models.XSDFACM;
import org.apache.xerces.impl.xs.models.XSEmptyCM;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTerm;

public class CMBuilder {
    private XSDeclarationPool fDeclPool = null;
    private static final XSEmptyCM fEmptyCM = new XSEmptyCM();
    private int fLeafCount;
    private int fParticleCount;
    private final CMNodeFactory fNodeFactory;

    public CMBuilder(CMNodeFactory cMNodeFactory) {
        this.fNodeFactory = cMNodeFactory;
    }

    public void setDeclPool(XSDeclarationPool xSDeclarationPool) {
        this.fDeclPool = xSDeclarationPool;
    }

    public XSCMValidator getContentModel(XSComplexTypeDecl xSComplexTypeDecl, boolean bl) {
        short s2 = xSComplexTypeDecl.getContentType();
        if (s2 == 1 || s2 == 0) {
            return null;
        }
        XSParticleDecl xSParticleDecl = (XSParticleDecl)xSComplexTypeDecl.getParticle();
        if (xSParticleDecl == null) {
            return fEmptyCM;
        }
        XSCMValidator xSCMValidator = null;
        xSCMValidator = xSParticleDecl.fType == 3 && ((XSModelGroupImpl)xSParticleDecl.fValue).fCompositor == 103 ? this.createAllCM(xSParticleDecl) : this.createDFACM(xSParticleDecl, bl);
        this.fNodeFactory.resetNodeCount();
        if (xSCMValidator == null) {
            xSCMValidator = fEmptyCM;
        }
        return xSCMValidator;
    }

    XSCMValidator createAllCM(XSParticleDecl xSParticleDecl) {
        if (xSParticleDecl.fMaxOccurs == 0) {
            return null;
        }
        XSModelGroupImpl xSModelGroupImpl = (XSModelGroupImpl)xSParticleDecl.fValue;
        XSAllCM xSAllCM = new XSAllCM(xSParticleDecl.fMinOccurs == 0, xSModelGroupImpl.fParticleCount);
        int n2 = 0;
        while (n2 < xSModelGroupImpl.fParticleCount) {
            xSAllCM.addElement((XSElementDecl)xSModelGroupImpl.fParticles[n2].fValue, xSModelGroupImpl.fParticles[n2].fMinOccurs == 0);
            ++n2;
        }
        return xSAllCM;
    }

    XSCMValidator createDFACM(XSParticleDecl xSParticleDecl, boolean bl) {
        CMNode cMNode;
        this.fLeafCount = 0;
        this.fParticleCount = 0;
        CMNode cMNode2 = cMNode = this.useRepeatingLeafNodes(xSParticleDecl) ? this.buildCompactSyntaxTree(xSParticleDecl) : this.buildSyntaxTree(xSParticleDecl, bl);
        if (cMNode == null) {
            return null;
        }
        return new XSDFACM(cMNode, this.fLeafCount);
    }

    private CMNode buildSyntaxTree(XSParticleDecl xSParticleDecl, boolean bl) {
        int n2 = xSParticleDecl.fMaxOccurs;
        int n3 = xSParticleDecl.fMinOccurs;
        boolean bl2 = false;
        if (bl) {
            if (n3 > 1) {
                if (n2 > n3 || xSParticleDecl.getMaxOccursUnbounded()) {
                    n3 = 1;
                    bl2 = true;
                } else {
                    n3 = 2;
                    bl2 = true;
                }
            }
            if (n2 > 1) {
                n2 = 2;
                bl2 = true;
            }
        }
        short s2 = xSParticleDecl.fType;
        CMNode cMNode = null;
        if (s2 == 2 || s2 == 1) {
            cMNode = this.fNodeFactory.getCMLeafNode(xSParticleDecl.fType, xSParticleDecl.fValue, this.fParticleCount++, this.fLeafCount++);
            if ((cMNode = this.expandContentModel(cMNode, n3, n2)) != null) {
                cMNode.setIsCompactUPAModel(bl2);
            }
        } else if (s2 == 3) {
            XSModelGroupImpl xSModelGroupImpl = (XSModelGroupImpl)xSParticleDecl.fValue;
            CMNode cMNode2 = null;
            int n4 = 0;
            int n5 = 0;
            while (n5 < xSModelGroupImpl.fParticleCount) {
                cMNode2 = this.buildSyntaxTree(xSModelGroupImpl.fParticles[n5], bl);
                if (cMNode2 != null) {
                    bl2 |= cMNode2.isCompactedForUPA();
                    ++n4;
                    cMNode = cMNode == null ? cMNode2 : this.fNodeFactory.getCMBinOpNode(xSModelGroupImpl.fCompositor, cMNode, cMNode2);
                }
                ++n5;
            }
            if (cMNode != null) {
                if (xSModelGroupImpl.fCompositor == 101 && n4 < xSModelGroupImpl.fParticleCount) {
                    cMNode = this.fNodeFactory.getCMUniOpNode(5, cMNode);
                }
                cMNode = this.expandContentModel(cMNode, n3, n2);
                cMNode.setIsCompactUPAModel(bl2);
            }
        }
        return cMNode;
    }

    private CMNode expandContentModel(CMNode cMNode, int n2, int n3) {
        CMNode cMNode2 = null;
        if (n2 == 1 && n3 == 1) {
            cMNode2 = cMNode;
        } else if (n2 == 0 && n3 == 1) {
            cMNode2 = this.fNodeFactory.getCMUniOpNode(5, cMNode);
        } else if (n2 == 0 && n3 == -1) {
            cMNode2 = this.fNodeFactory.getCMUniOpNode(4, cMNode);
        } else if (n2 == 1 && n3 == -1) {
            cMNode2 = this.fNodeFactory.getCMUniOpNode(6, cMNode);
        } else if (n3 == -1) {
            cMNode2 = this.fNodeFactory.getCMUniOpNode(6, cMNode);
            cMNode2 = this.fNodeFactory.getCMBinOpNode(102, this.multiNodes(cMNode, n2 - 1, true), cMNode2);
        } else {
            if (n2 > 0) {
                cMNode2 = this.multiNodes(cMNode, n2, false);
            }
            if (n3 > n2) {
                cMNode = this.fNodeFactory.getCMUniOpNode(5, cMNode);
                cMNode2 = cMNode2 == null ? this.multiNodes(cMNode, n3 - n2, false) : this.fNodeFactory.getCMBinOpNode(102, cMNode2, this.multiNodes(cMNode, n3 - n2, true));
            }
        }
        return cMNode2;
    }

    private CMNode multiNodes(CMNode cMNode, int n2, boolean bl) {
        if (n2 == 0) {
            return null;
        }
        if (n2 == 1) {
            return bl ? this.copyNode(cMNode) : cMNode;
        }
        int n3 = n2 / 2;
        return this.fNodeFactory.getCMBinOpNode(102, this.multiNodes(cMNode, n3, bl), this.multiNodes(cMNode, n2 - n3, true));
    }

    private CMNode copyNode(CMNode cMNode) {
        int n2 = cMNode.type();
        if (n2 == 101 || n2 == 102) {
            XSCMBinOp xSCMBinOp = (XSCMBinOp)cMNode;
            cMNode = this.fNodeFactory.getCMBinOpNode(n2, this.copyNode(xSCMBinOp.getLeft()), this.copyNode(xSCMBinOp.getRight()));
        } else if (n2 == 4 || n2 == 6 || n2 == 5) {
            XSCMUniOp xSCMUniOp = (XSCMUniOp)cMNode;
            cMNode = this.fNodeFactory.getCMUniOpNode(n2, this.copyNode(xSCMUniOp.getChild()));
        } else if (n2 == 1 || n2 == 2) {
            XSCMLeaf xSCMLeaf = (XSCMLeaf)cMNode;
            cMNode = this.fNodeFactory.getCMLeafNode(xSCMLeaf.type(), xSCMLeaf.getLeaf(), xSCMLeaf.getParticleId(), this.fLeafCount++);
        }
        return cMNode;
    }

    private CMNode buildCompactSyntaxTree(XSParticleDecl xSParticleDecl) {
        int n2 = xSParticleDecl.fMaxOccurs;
        int n3 = xSParticleDecl.fMinOccurs;
        short s2 = xSParticleDecl.fType;
        CMNode cMNode = null;
        if (s2 == 2 || s2 == 1) {
            return this.buildCompactSyntaxTree2(xSParticleDecl, n3, n2);
        }
        if (s2 == 3) {
            XSModelGroupImpl xSModelGroupImpl = (XSModelGroupImpl)xSParticleDecl.fValue;
            if (xSModelGroupImpl.fParticleCount == 1 && (n3 != 1 || n2 != 1)) {
                return this.buildCompactSyntaxTree2(xSModelGroupImpl.fParticles[0], n3, n2);
            }
            CMNode cMNode2 = null;
            int n4 = 0;
            int n5 = 0;
            while (n5 < xSModelGroupImpl.fParticleCount) {
                cMNode2 = this.buildCompactSyntaxTree(xSModelGroupImpl.fParticles[n5]);
                if (cMNode2 != null) {
                    ++n4;
                    cMNode = cMNode == null ? cMNode2 : this.fNodeFactory.getCMBinOpNode(xSModelGroupImpl.fCompositor, cMNode, cMNode2);
                }
                ++n5;
            }
            if (cMNode != null && xSModelGroupImpl.fCompositor == 101 && n4 < xSModelGroupImpl.fParticleCount) {
                cMNode = this.fNodeFactory.getCMUniOpNode(5, cMNode);
            }
        }
        return cMNode;
    }

    private CMNode buildCompactSyntaxTree2(XSParticleDecl xSParticleDecl, int n2, int n3) {
        CMNode cMNode = null;
        if (n2 == 1 && n3 == 1) {
            cMNode = this.fNodeFactory.getCMLeafNode(xSParticleDecl.fType, xSParticleDecl.fValue, this.fParticleCount++, this.fLeafCount++);
        } else if (n2 == 0 && n3 == 1) {
            cMNode = this.fNodeFactory.getCMLeafNode(xSParticleDecl.fType, xSParticleDecl.fValue, this.fParticleCount++, this.fLeafCount++);
            cMNode = this.fNodeFactory.getCMUniOpNode(5, cMNode);
        } else if (n2 == 0 && n3 == -1) {
            cMNode = this.fNodeFactory.getCMLeafNode(xSParticleDecl.fType, xSParticleDecl.fValue, this.fParticleCount++, this.fLeafCount++);
            cMNode = this.fNodeFactory.getCMUniOpNode(4, cMNode);
        } else if (n2 == 1 && n3 == -1) {
            cMNode = this.fNodeFactory.getCMLeafNode(xSParticleDecl.fType, xSParticleDecl.fValue, this.fParticleCount++, this.fLeafCount++);
            cMNode = this.fNodeFactory.getCMUniOpNode(6, cMNode);
        } else {
            cMNode = this.fNodeFactory.getCMRepeatingLeafNode(xSParticleDecl.fType, xSParticleDecl.fValue, n2, n3, this.fParticleCount++, this.fLeafCount++);
            cMNode = n2 == 0 ? this.fNodeFactory.getCMUniOpNode(4, cMNode) : this.fNodeFactory.getCMUniOpNode(6, cMNode);
        }
        return cMNode;
    }

    private boolean useRepeatingLeafNodes(XSParticleDecl xSParticleDecl) {
        int n2 = xSParticleDecl.fMaxOccurs;
        int n3 = xSParticleDecl.fMinOccurs;
        short s2 = xSParticleDecl.fType;
        if (s2 == 3) {
            XSModelGroupImpl xSModelGroupImpl = (XSModelGroupImpl)xSParticleDecl.fValue;
            if (n3 != 1 || n2 != 1) {
                if (xSModelGroupImpl.fParticleCount == 1) {
                    XSParticleDecl xSParticleDecl2 = xSModelGroupImpl.fParticles[0];
                    short s3 = xSParticleDecl2.fType;
                    return (s3 == 1 || s3 == 2) && xSParticleDecl2.fMinOccurs == 1 && xSParticleDecl2.fMaxOccurs == 1;
                }
                return xSModelGroupImpl.fParticleCount == 0;
            }
            int n4 = 0;
            while (n4 < xSModelGroupImpl.fParticleCount) {
                if (!this.useRepeatingLeafNodes(xSModelGroupImpl.fParticles[n4])) {
                    return false;
                }
                ++n4;
            }
        }
        return true;
    }
}

