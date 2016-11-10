/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.models;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Vector;
import org.apache.xerces.impl.dtd.models.CMNode;
import org.apache.xerces.impl.dtd.models.CMStateSet;
import org.apache.xerces.impl.xs.SubstitutionGroupHandler;
import org.apache.xerces.impl.xs.XMLSchemaException;
import org.apache.xerces.impl.xs.XSConstraints;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSWildcardDecl;
import org.apache.xerces.impl.xs.models.XSCMBinOp;
import org.apache.xerces.impl.xs.models.XSCMLeaf;
import org.apache.xerces.impl.xs.models.XSCMRepeatingLeaf;
import org.apache.xerces.impl.xs.models.XSCMUniOp;
import org.apache.xerces.impl.xs.models.XSCMValidator;
import org.apache.xerces.xni.QName;

public class XSDFACM
implements XSCMValidator {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_VALIDATE_CONTENT = false;
    private Object[] fElemMap = null;
    private int[] fElemMapType = null;
    private int[] fElemMapId = null;
    private int fElemMapSize = 0;
    private boolean[] fFinalStateFlags = null;
    private CMStateSet[] fFollowList = null;
    private CMNode fHeadNode = null;
    private int fLeafCount = 0;
    private XSCMLeaf[] fLeafList = null;
    private int[] fLeafListType = null;
    private int[][] fTransTable = null;
    private Occurence[] fCountingStates = null;
    private int fTransTableSize = 0;
    private boolean fIsCompactedForUPA;
    private static long time = 0;

    public XSDFACM(CMNode cMNode, int n2) {
        this.fLeafCount = n2;
        this.fIsCompactedForUPA = cMNode.isCompactedForUPA();
        this.buildDFA(cMNode);
    }

    public boolean isFinalState(int n2) {
        return n2 < 0 ? false : this.fFinalStateFlags[n2];
    }

    public Object oneTransition(QName qName, int[] arrn, SubstitutionGroupHandler substitutionGroupHandler) {
        int n2 = arrn[0];
        if (n2 == -1 || n2 == -2) {
            if (n2 == -1) {
                arrn[0] = -2;
            }
            return this.findMatchingDecl(qName, substitutionGroupHandler);
        }
        int n3 = 0;
        int n4 = 0;
        Object object = null;
        while (n4 < this.fElemMapSize) {
            n3 = this.fTransTable[n2][n4];
            if (n3 != -1) {
                int n5 = this.fElemMapType[n4];
                if (n5 == 1) {
                    object = substitutionGroupHandler.getMatchingElemDecl(qName, (XSElementDecl)this.fElemMap[n4]);
                    if (object != null) {
                        break;
                    }
                } else if (n5 == 2 && ((XSWildcardDecl)this.fElemMap[n4]).allowNamespace(qName.uri)) {
                    object = this.fElemMap[n4];
                    break;
                }
            }
            ++n4;
        }
        if (n4 == this.fElemMapSize) {
            arrn[1] = arrn[0];
            arrn[0] = -1;
            return this.findMatchingDecl(qName, substitutionGroupHandler);
        }
        if (this.fCountingStates != null) {
            Occurence occurence = this.fCountingStates[n2];
            if (occurence != null) {
                if (n2 == n3) {
                    arrn[2] = arrn[2] + 1;
                    if (arrn[2] > occurence.maxOccurs && occurence.maxOccurs != -1) {
                        return this.findMatchingDecl(qName, arrn, substitutionGroupHandler, n4);
                    }
                } else {
                    if (arrn[2] < occurence.minOccurs) {
                        arrn[1] = arrn[0];
                        arrn[0] = -1;
                        return this.findMatchingDecl(qName, substitutionGroupHandler);
                    }
                    occurence = this.fCountingStates[n3];
                    if (occurence != null) {
                        arrn[2] = n4 == occurence.elemIndex ? 1 : 0;
                    }
                }
            } else {
                occurence = this.fCountingStates[n3];
                if (occurence != null) {
                    arrn[2] = n4 == occurence.elemIndex ? 1 : 0;
                }
            }
        }
        arrn[0] = n3;
        return object;
    }

    Object findMatchingDecl(QName qName, SubstitutionGroupHandler substitutionGroupHandler) {
        XSElementDecl xSElementDecl = null;
        int n2 = 0;
        while (n2 < this.fElemMapSize) {
            int n3 = this.fElemMapType[n2];
            if (n3 == 1) {
                xSElementDecl = substitutionGroupHandler.getMatchingElemDecl(qName, (XSElementDecl)this.fElemMap[n2]);
                if (xSElementDecl != null) {
                    return xSElementDecl;
                }
            } else if (n3 == 2 && ((XSWildcardDecl)this.fElemMap[n2]).allowNamespace(qName.uri)) {
                return this.fElemMap[n2];
            }
            ++n2;
        }
        return null;
    }

    Object findMatchingDecl(QName qName, int[] arrn, SubstitutionGroupHandler substitutionGroupHandler, int n2) {
        int n3 = arrn[0];
        int n4 = 0;
        Object object = null;
        while (++n2 < this.fElemMapSize) {
            n4 = this.fTransTable[n3][n2];
            if (n4 == -1) continue;
            int n5 = this.fElemMapType[n2];
            if (n5 == 1) {
                object = substitutionGroupHandler.getMatchingElemDecl(qName, (XSElementDecl)this.fElemMap[n2]);
                if (object == null) continue;
                break;
            }
            if (n5 != 2 || !((XSWildcardDecl)this.fElemMap[n2]).allowNamespace(qName.uri)) continue;
            object = this.fElemMap[n2];
            break;
        }
        if (n2 == this.fElemMapSize) {
            arrn[1] = arrn[0];
            arrn[0] = -1;
            return this.findMatchingDecl(qName, substitutionGroupHandler);
        }
        arrn[0] = n4;
        Occurence occurence = this.fCountingStates[n4];
        if (occurence != null) {
            arrn[2] = n2 == occurence.elemIndex ? 1 : 0;
        }
        return object;
    }

    public int[] startContentModel() {
        return new int[3];
    }

    public boolean endContentModel(int[] arrn) {
        int n2 = arrn[0];
        if (this.fFinalStateFlags[n2]) {
            Occurence occurence;
            if (this.fCountingStates != null && (occurence = this.fCountingStates[n2]) != null && arrn[2] < occurence.minOccurs) {
                return false;
            }
            return true;
        }
        return false;
    }

    private void buildDFA(CMNode cMNode) {
        int n2;
        Object object;
        int n3;
        int n4;
        int n5 = this.fLeafCount;
        XSCMLeaf xSCMLeaf = new XSCMLeaf(1, null, -1, this.fLeafCount++);
        this.fHeadNode = new XSCMBinOp(102, cMNode, xSCMLeaf);
        this.fLeafList = new XSCMLeaf[this.fLeafCount];
        this.fLeafListType = new int[this.fLeafCount];
        this.postTreeBuildInit(this.fHeadNode);
        this.fFollowList = new CMStateSet[this.fLeafCount];
        int n6 = 0;
        while (n6 < this.fLeafCount) {
            this.fFollowList[n6] = new CMStateSet(this.fLeafCount);
            ++n6;
        }
        this.calcFollowList(this.fHeadNode);
        this.fElemMap = new Object[this.fLeafCount];
        this.fElemMapType = new int[this.fLeafCount];
        this.fElemMapId = new int[this.fLeafCount];
        this.fElemMapSize = 0;
        Occurence[] arroccurence = null;
        int n7 = 0;
        while (n7 < this.fLeafCount) {
            this.fElemMap[n7] = null;
            int n8 = 0;
            n4 = this.fLeafList[n7].getParticleId();
            while (n8 < this.fElemMapSize) {
                if (n4 == this.fElemMapId[n8]) break;
                ++n8;
            }
            if (n8 == this.fElemMapSize) {
                XSCMLeaf xSCMLeaf2 = this.fLeafList[n7];
                this.fElemMap[this.fElemMapSize] = xSCMLeaf2.getLeaf();
                if (xSCMLeaf2 instanceof XSCMRepeatingLeaf) {
                    if (arroccurence == null) {
                        arroccurence = new Occurence[this.fLeafCount];
                    }
                    arroccurence[this.fElemMapSize] = new Occurence((XSCMRepeatingLeaf)xSCMLeaf2, this.fElemMapSize);
                }
                this.fElemMapType[this.fElemMapSize] = this.fLeafListType[n7];
                this.fElemMapId[this.fElemMapSize] = n4;
                ++this.fElemMapSize;
            }
            ++n7;
        }
        --this.fElemMapSize;
        int[] arrn = new int[this.fLeafCount + this.fElemMapSize];
        n4 = 0;
        int n9 = 0;
        while (n9 < this.fElemMapSize) {
            n2 = this.fElemMapId[n9];
            int n10 = 0;
            while (n10 < this.fLeafCount) {
                if (n2 == this.fLeafList[n10].getParticleId()) {
                    arrn[n4++] = n10;
                }
                ++n10;
            }
            arrn[n4++] = -1;
            ++n9;
        }
        n2 = this.fLeafCount * 4;
        CMStateSet[] arrcMStateSet = new CMStateSet[n2];
        this.fFinalStateFlags = new boolean[n2];
        this.fTransTable = new int[n2][];
        CMStateSet cMStateSet = this.fHeadNode.firstPos();
        int n11 = 0;
        int n12 = 0;
        this.fTransTable[n12] = this.makeDefStateList();
        arrcMStateSet[n12] = cMStateSet;
        HashMap<Object, Integer> hashMap = new HashMap<Object, Integer>();
        while (n11 < ++n12) {
            cMStateSet = arrcMStateSet[n11];
            int[] arrn2 = this.fTransTable[n11];
            this.fFinalStateFlags[n11] = cMStateSet.getBit(n5);
            ++n11;
            object = null;
            n3 = 0;
            int n13 = 0;
            while (n13 < this.fElemMapSize) {
                if (object == null) {
                    object = new CMStateSet(this.fLeafCount);
                } else {
                    object.zeroBits();
                }
                int n14 = arrn[n3++];
                while (n14 != -1) {
                    if (cMStateSet.getBit(n14)) {
                        object.union(this.fFollowList[n14]);
                    }
                    n14 = arrn[n3++];
                }
                if (!object.isEmpty()) {
                    int n15;
                    Integer n16 = (Integer)hashMap.get(object);
                    int n17 = n15 = n16 == null ? n12 : n16;
                    if (n15 == n12) {
                        arrcMStateSet[n12] = object;
                        this.fTransTable[n12] = this.makeDefStateList();
                        hashMap.put(object, new Integer(n12));
                        ++n12;
                        object = null;
                    }
                    arrn2[n13] = n15;
                    if (n12 == n2) {
                        int n18 = (int)((double)n2 * 1.5);
                        CMStateSet[] arrcMStateSet2 = new CMStateSet[n18];
                        boolean[] arrbl = new boolean[n18];
                        int[][] arrarrn = new int[n18][];
                        System.arraycopy(arrcMStateSet, 0, arrcMStateSet2, 0, n2);
                        System.arraycopy(this.fFinalStateFlags, 0, arrbl, 0, n2);
                        System.arraycopy(this.fTransTable, 0, arrarrn, 0, n2);
                        n2 = n18;
                        arrcMStateSet = arrcMStateSet2;
                        this.fFinalStateFlags = arrbl;
                        this.fTransTable = arrarrn;
                    }
                }
                ++n13;
            }
        }
        if (arroccurence != null) {
            this.fCountingStates = new Occurence[n12];
            int n19 = 0;
            while (n19 < n12) {
                object = this.fTransTable[n19];
                n3 = 0;
                while (n3 < object.length) {
                    if (n19 == object[n3]) {
                        this.fCountingStates[n19] = arroccurence[n3];
                        break;
                    }
                    ++n3;
                }
                ++n19;
            }
        }
        this.fHeadNode = null;
        this.fLeafList = null;
        this.fFollowList = null;
        this.fLeafListType = null;
        this.fElemMapId = null;
    }

    private void calcFollowList(CMNode cMNode) {
        if (cMNode.type() == 101) {
            this.calcFollowList(((XSCMBinOp)cMNode).getLeft());
            this.calcFollowList(((XSCMBinOp)cMNode).getRight());
        } else if (cMNode.type() == 102) {
            this.calcFollowList(((XSCMBinOp)cMNode).getLeft());
            this.calcFollowList(((XSCMBinOp)cMNode).getRight());
            CMStateSet cMStateSet = ((XSCMBinOp)cMNode).getLeft().lastPos();
            CMStateSet cMStateSet2 = ((XSCMBinOp)cMNode).getRight().firstPos();
            int n2 = 0;
            while (n2 < this.fLeafCount) {
                if (cMStateSet.getBit(n2)) {
                    this.fFollowList[n2].union(cMStateSet2);
                }
                ++n2;
            }
        } else if (cMNode.type() == 4 || cMNode.type() == 6) {
            this.calcFollowList(((XSCMUniOp)cMNode).getChild());
            CMStateSet cMStateSet = cMNode.firstPos();
            CMStateSet cMStateSet3 = cMNode.lastPos();
            int n3 = 0;
            while (n3 < this.fLeafCount) {
                if (cMStateSet3.getBit(n3)) {
                    this.fFollowList[n3].union(cMStateSet);
                }
                ++n3;
            }
        } else if (cMNode.type() == 5) {
            this.calcFollowList(((XSCMUniOp)cMNode).getChild());
        }
    }

    private void dumpTree(CMNode cMNode, int n2) {
        int n3 = 0;
        while (n3 < n2) {
            System.out.print("   ");
            ++n3;
        }
        int n4 = cMNode.type();
        switch (n4) {
            case 101: 
            case 102: {
                if (n4 == 101) {
                    System.out.print("Choice Node ");
                } else {
                    System.out.print("Seq Node ");
                }
                if (cMNode.isNullable()) {
                    System.out.print("Nullable ");
                }
                System.out.print("firstPos=");
                System.out.print(cMNode.firstPos().toString());
                System.out.print(" lastPos=");
                System.out.println(cMNode.lastPos().toString());
                this.dumpTree(((XSCMBinOp)cMNode).getLeft(), n2 + 1);
                this.dumpTree(((XSCMBinOp)cMNode).getRight(), n2 + 1);
                break;
            }
            case 4: 
            case 5: 
            case 6: {
                System.out.print("Rep Node ");
                if (cMNode.isNullable()) {
                    System.out.print("Nullable ");
                }
                System.out.print("firstPos=");
                System.out.print(cMNode.firstPos().toString());
                System.out.print(" lastPos=");
                System.out.println(cMNode.lastPos().toString());
                this.dumpTree(((XSCMUniOp)cMNode).getChild(), n2 + 1);
                break;
            }
            case 1: {
                System.out.print("Leaf: (pos=" + ((XSCMLeaf)cMNode).getPosition() + "), " + "(elemIndex=" + ((XSCMLeaf)cMNode).getLeaf() + ") ");
                if (cMNode.isNullable()) {
                    System.out.print(" Nullable ");
                }
                System.out.print("firstPos=");
                System.out.print(cMNode.firstPos().toString());
                System.out.print(" lastPos=");
                System.out.println(cMNode.lastPos().toString());
                break;
            }
            case 2: {
                System.out.print("Any Node: ");
                System.out.print("firstPos=");
                System.out.print(cMNode.firstPos().toString());
                System.out.print(" lastPos=");
                System.out.println(cMNode.lastPos().toString());
                break;
            }
            default: {
                throw new RuntimeException("ImplementationMessages.VAL_NIICM");
            }
        }
    }

    private int[] makeDefStateList() {
        int[] arrn = new int[this.fElemMapSize];
        int n2 = 0;
        while (n2 < this.fElemMapSize) {
            arrn[n2] = -1;
            ++n2;
        }
        return arrn;
    }

    private void postTreeBuildInit(CMNode cMNode) throws RuntimeException {
        cMNode.setMaxStates(this.fLeafCount);
        XSCMLeaf xSCMLeaf = null;
        int n2 = 0;
        if (cMNode.type() == 2) {
            xSCMLeaf = (XSCMLeaf)cMNode;
            n2 = xSCMLeaf.getPosition();
            this.fLeafList[n2] = xSCMLeaf;
            this.fLeafListType[n2] = 2;
        } else if (cMNode.type() == 101 || cMNode.type() == 102) {
            this.postTreeBuildInit(((XSCMBinOp)cMNode).getLeft());
            this.postTreeBuildInit(((XSCMBinOp)cMNode).getRight());
        } else if (cMNode.type() == 4 || cMNode.type() == 6 || cMNode.type() == 5) {
            this.postTreeBuildInit(((XSCMUniOp)cMNode).getChild());
        } else if (cMNode.type() == 1) {
            xSCMLeaf = (XSCMLeaf)cMNode;
            n2 = xSCMLeaf.getPosition();
            this.fLeafList[n2] = xSCMLeaf;
            this.fLeafListType[n2] = 1;
        } else {
            throw new RuntimeException("ImplementationMessages.VAL_NIICM");
        }
    }

    public boolean checkUniqueParticleAttribution(SubstitutionGroupHandler substitutionGroupHandler) throws XMLSchemaException {
        int n2;
        Object object;
        int n3;
        byte[][] arrby = new byte[this.fElemMapSize][this.fElemMapSize];
        int n4 = 0;
        while (n4 < this.fTransTable.length && this.fTransTable[n4] != null) {
            n2 = 0;
            while (n2 < this.fElemMapSize) {
                n3 = n2 + 1;
                while (n3 < this.fElemMapSize) {
                    if (this.fTransTable[n4][n2] != -1 && this.fTransTable[n4][n3] != -1 && arrby[n2][n3] == 0) {
                        arrby[n2][n3] = XSConstraints.overlapUPA(this.fElemMap[n2], this.fElemMap[n3], substitutionGroupHandler) ? (this.fCountingStates != null && (object = this.fCountingStates[n4]) != null && this.fTransTable[n4][n2] == n4 ^ this.fTransTable[n4][n3] == n4 && object.minOccurs == object.maxOccurs ? -1 : 1) : -1;
                    }
                    ++n3;
                }
                ++n2;
            }
            ++n4;
        }
        n2 = 0;
        while (n2 < this.fElemMapSize) {
            n3 = 0;
            while (n3 < this.fElemMapSize) {
                if (arrby[n2][n3] == 1) {
                    throw new XMLSchemaException("cos-nonambig", new Object[]{this.fElemMap[n2].toString(), this.fElemMap[n3].toString()});
                }
                ++n3;
            }
            ++n2;
        }
        n3 = 0;
        while (n3 < this.fElemMapSize) {
            if (this.fElemMapType[n3] == 2) {
                object = (XSWildcardDecl)this.fElemMap[n3];
                if (object.fType == 3 || object.fType == 2) {
                    return true;
                }
            }
            ++n3;
        }
        return false;
    }

    public Vector whatCanGoHere(int[] arrn) {
        int n2 = arrn[0];
        if (n2 < 0) {
            n2 = arrn[1];
        }
        Occurence occurence = this.fCountingStates != null ? this.fCountingStates[n2] : null;
        int n3 = arrn[2];
        Vector<Object> vector = new Vector<Object>();
        int n4 = 0;
        while (n4 < this.fElemMapSize) {
            int n5 = this.fTransTable[n2][n4];
            if (!(n5 == -1 || occurence != null && (n2 == n5 ? n3 >= occurence.maxOccurs && occurence.maxOccurs != -1 : n3 < occurence.minOccurs))) {
                vector.addElement(this.fElemMap[n4]);
            }
            ++n4;
        }
        return vector;
    }

    public int[] occurenceInfo(int[] arrn) {
        if (this.fCountingStates != null) {
            Occurence occurence;
            int n2 = arrn[0];
            if (n2 < 0) {
                n2 = arrn[1];
            }
            if ((occurence = this.fCountingStates[n2]) != null) {
                int[] arrn2 = new int[]{occurence.minOccurs, occurence.maxOccurs, arrn[2], occurence.elemIndex};
                return arrn2;
            }
        }
        return null;
    }

    public String getTermName(int n2) {
        Object object = this.fElemMap[n2];
        return object != null ? object.toString() : null;
    }

    public boolean isCompactedForUPA() {
        return this.fIsCompactedForUPA;
    }

    static final class Occurence {
        final int minOccurs;
        final int maxOccurs;
        final int elemIndex;

        public Occurence(XSCMRepeatingLeaf xSCMRepeatingLeaf, int n2) {
            this.minOccurs = xSCMRepeatingLeaf.getMinOccurs();
            this.maxOccurs = xSCMRepeatingLeaf.getMaxOccurs();
            this.elemIndex = n2;
        }

        public String toString() {
            return "minOccurs=" + this.minOccurs + ";maxOccurs=" + (this.maxOccurs != -1 ? Integer.toString(this.maxOccurs) : "unbounded");
        }
    }

}

