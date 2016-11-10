/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dtd.models;

import java.io.PrintStream;
import java.util.HashMap;
import org.apache.xerces.impl.dtd.models.CMAny;
import org.apache.xerces.impl.dtd.models.CMBinOp;
import org.apache.xerces.impl.dtd.models.CMLeaf;
import org.apache.xerces.impl.dtd.models.CMNode;
import org.apache.xerces.impl.dtd.models.CMStateSet;
import org.apache.xerces.impl.dtd.models.CMUniOp;
import org.apache.xerces.impl.dtd.models.ContentModelValidator;
import org.apache.xerces.xni.QName;

public class DFAContentModel
implements ContentModelValidator {
    private static String fEpsilonString = "<<CMNODE_EPSILON>>";
    private static String fEOCString = "<<CMNODE_EOC>>";
    private static final boolean DEBUG_VALIDATE_CONTENT = false;
    private QName[] fElemMap = null;
    private int[] fElemMapType = null;
    private int fElemMapSize = 0;
    private boolean fMixed;
    private int fEOCPos = 0;
    private boolean[] fFinalStateFlags = null;
    private CMStateSet[] fFollowList = null;
    private CMNode fHeadNode = null;
    private int fLeafCount = 0;
    private CMLeaf[] fLeafList = null;
    private int[] fLeafListType = null;
    private int[][] fTransTable = null;
    private int fTransTableSize = 0;
    private boolean fEmptyContentIsValid = false;
    private final QName fQName = new QName();

    public DFAContentModel(CMNode cMNode, int n2, boolean bl) {
        this.fLeafCount = n2;
        this.fMixed = bl;
        this.buildDFA(cMNode);
    }

    public int validate(QName[] arrqName, int n2, int n3) {
        if (n3 == 0) {
            return this.fEmptyContentIsValid ? -1 : 0;
        }
        int n4 = 0;
        int n5 = 0;
        while (n5 < n3) {
            QName qName = arrqName[n2 + n5];
            if (!this.fMixed || qName.localpart != null) {
                int n6 = 0;
                while (n6 < this.fElemMapSize) {
                    String string;
                    int n7 = this.fElemMapType[n6] & 15;
                    if (n7 == 0 ? this.fElemMap[n6].rawname == qName.rawname : (n7 == 6 ? (string = this.fElemMap[n6].uri) == null || string == qName.uri : (n7 == 8 ? qName.uri == null : n7 == 7 && this.fElemMap[n6].uri != qName.uri))) break;
                    ++n6;
                }
                if (n6 == this.fElemMapSize) {
                    return n5;
                }
                if ((n4 = this.fTransTable[n4][n6]) == -1) {
                    return n5;
                }
            }
            ++n5;
        }
        if (!this.fFinalStateFlags[n4]) {
            return n3;
        }
        return -1;
    }

    private void buildDFA(CMNode cMNode) {
        int n2;
        Object object;
        CMStateSet[] arrcMStateSet;
        Object object2;
        int n3;
        this.fQName.setValues(null, fEOCString, fEOCString, null);
        CMLeaf cMLeaf = new CMLeaf(this.fQName);
        this.fHeadNode = new CMBinOp(5, cMNode, cMLeaf);
        this.fEOCPos = this.fLeafCount;
        cMLeaf.setPosition(this.fLeafCount++);
        this.fLeafList = new CMLeaf[this.fLeafCount];
        this.fLeafListType = new int[this.fLeafCount];
        this.postTreeBuildInit(this.fHeadNode, 0);
        this.fFollowList = new CMStateSet[this.fLeafCount];
        int n4 = 0;
        while (n4 < this.fLeafCount) {
            this.fFollowList[n4] = new CMStateSet(this.fLeafCount);
            ++n4;
        }
        this.calcFollowList(this.fHeadNode);
        this.fElemMap = new QName[this.fLeafCount];
        this.fElemMapType = new int[this.fLeafCount];
        this.fElemMapSize = 0;
        int n5 = 0;
        while (n5 < this.fLeafCount) {
            this.fElemMap[n5] = new QName();
            object2 = this.fLeafList[n5].getElement();
            n2 = 0;
            while (n2 < this.fElemMapSize) {
                if (this.fElemMap[n2].rawname == object2.rawname) break;
                ++n2;
            }
            if (n2 == this.fElemMapSize) {
                this.fElemMap[this.fElemMapSize].setValues((QName)object2);
                this.fElemMapType[this.fElemMapSize] = this.fLeafListType[n5];
                ++this.fElemMapSize;
            }
            ++n5;
        }
        object2 = new int[this.fLeafCount + this.fElemMapSize];
        n2 = 0;
        int n6 = 0;
        while (n6 < this.fElemMapSize) {
            n3 = 0;
            while (n3 < this.fLeafCount) {
                arrcMStateSet = this.fLeafList[n3].getElement();
                object = this.fElemMap[n6];
                if (arrcMStateSet.rawname == object.rawname) {
                    object2[n2++] = n3;
                }
                ++n3;
            }
            object2[n2++] = -1;
            ++n6;
        }
        n3 = this.fLeafCount * 4;
        arrcMStateSet = new CMStateSet[n3];
        this.fFinalStateFlags = new boolean[n3];
        this.fTransTable = new int[n3][];
        object = this.fHeadNode.firstPos();
        int n7 = 0;
        int n8 = 0;
        this.fTransTable[n8] = this.makeDefStateList();
        arrcMStateSet[n8] = object;
        HashMap<CMStateSet, Integer> hashMap = new HashMap<CMStateSet, Integer>();
        while (n7 < ++n8) {
            object = arrcMStateSet[n7];
            int[] arrn = this.fTransTable[n7];
            this.fFinalStateFlags[n7] = object.getBit(this.fEOCPos);
            ++n7;
            CMStateSet cMStateSet = null;
            int n9 = 0;
            int n10 = 0;
            while (n10 < this.fElemMapSize) {
                if (cMStateSet == null) {
                    cMStateSet = new CMStateSet(this.fLeafCount);
                } else {
                    cMStateSet.zeroBits();
                }
                Object object3 = object2[n9++];
                while (object3 != -1) {
                    if (object.getBit((int)object3)) {
                        cMStateSet.union(this.fFollowList[object3]);
                    }
                    object3 = object2[n9++];
                }
                if (!cMStateSet.isEmpty()) {
                    int n11;
                    Integer n12 = (Integer)hashMap.get(cMStateSet);
                    int n13 = n11 = n12 == null ? n8 : n12;
                    if (n11 == n8) {
                        arrcMStateSet[n8] = cMStateSet;
                        this.fTransTable[n8] = this.makeDefStateList();
                        hashMap.put(cMStateSet, new Integer(n8));
                        ++n8;
                        cMStateSet = null;
                    }
                    arrn[n10] = n11;
                    if (n8 == n3) {
                        int n14 = (int)((double)n3 * 1.5);
                        CMStateSet[] arrcMStateSet2 = new CMStateSet[n14];
                        boolean[] arrbl = new boolean[n14];
                        int[][] arrarrn = new int[n14][];
                        System.arraycopy(arrcMStateSet, 0, arrcMStateSet2, 0, n3);
                        System.arraycopy(this.fFinalStateFlags, 0, arrbl, 0, n3);
                        System.arraycopy(this.fTransTable, 0, arrarrn, 0, n3);
                        n3 = n14;
                        arrcMStateSet = arrcMStateSet2;
                        this.fFinalStateFlags = arrbl;
                        this.fTransTable = arrarrn;
                    }
                }
                ++n10;
            }
        }
        this.fEmptyContentIsValid = ((CMBinOp)this.fHeadNode).getLeft().isNullable();
        this.fHeadNode = null;
        this.fLeafList = null;
        this.fFollowList = null;
    }

    private void calcFollowList(CMNode cMNode) {
        if (cMNode.type() == 4) {
            this.calcFollowList(((CMBinOp)cMNode).getLeft());
            this.calcFollowList(((CMBinOp)cMNode).getRight());
        } else if (cMNode.type() == 5) {
            this.calcFollowList(((CMBinOp)cMNode).getLeft());
            this.calcFollowList(((CMBinOp)cMNode).getRight());
            CMStateSet cMStateSet = ((CMBinOp)cMNode).getLeft().lastPos();
            CMStateSet cMStateSet2 = ((CMBinOp)cMNode).getRight().firstPos();
            int n2 = 0;
            while (n2 < this.fLeafCount) {
                if (cMStateSet.getBit(n2)) {
                    this.fFollowList[n2].union(cMStateSet2);
                }
                ++n2;
            }
        } else if (cMNode.type() == 2 || cMNode.type() == 3) {
            this.calcFollowList(((CMUniOp)cMNode).getChild());
            CMStateSet cMStateSet = cMNode.firstPos();
            CMStateSet cMStateSet3 = cMNode.lastPos();
            int n3 = 0;
            while (n3 < this.fLeafCount) {
                if (cMStateSet3.getBit(n3)) {
                    this.fFollowList[n3].union(cMStateSet);
                }
                ++n3;
            }
        } else if (cMNode.type() == 1) {
            this.calcFollowList(((CMUniOp)cMNode).getChild());
        }
    }

    private void dumpTree(CMNode cMNode, int n2) {
        int n3 = 0;
        while (n3 < n2) {
            System.out.print("   ");
            ++n3;
        }
        int n4 = cMNode.type();
        if (n4 == 4 || n4 == 5) {
            if (n4 == 4) {
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
            this.dumpTree(((CMBinOp)cMNode).getLeft(), n2 + 1);
            this.dumpTree(((CMBinOp)cMNode).getRight(), n2 + 1);
        } else if (cMNode.type() == 2) {
            System.out.print("Rep Node ");
            if (cMNode.isNullable()) {
                System.out.print("Nullable ");
            }
            System.out.print("firstPos=");
            System.out.print(cMNode.firstPos().toString());
            System.out.print(" lastPos=");
            System.out.println(cMNode.lastPos().toString());
            this.dumpTree(((CMUniOp)cMNode).getChild(), n2 + 1);
        } else if (cMNode.type() == 0) {
            System.out.print("Leaf: (pos=" + ((CMLeaf)cMNode).getPosition() + "), " + ((CMLeaf)cMNode).getElement() + "(elemIndex=" + ((CMLeaf)cMNode).getElement() + ") ");
            if (cMNode.isNullable()) {
                System.out.print(" Nullable ");
            }
            System.out.print("firstPos=");
            System.out.print(cMNode.firstPos().toString());
            System.out.print(" lastPos=");
            System.out.println(cMNode.lastPos().toString());
        } else {
            throw new RuntimeException("ImplementationMessages.VAL_NIICM");
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

    private int postTreeBuildInit(CMNode cMNode, int n2) {
        cMNode.setMaxStates(this.fLeafCount);
        if ((cMNode.type() & 15) == 6 || (cMNode.type() & 15) == 8 || (cMNode.type() & 15) == 7) {
            QName qName = new QName(null, null, null, ((CMAny)cMNode).getURI());
            this.fLeafList[n2] = new CMLeaf(qName, ((CMAny)cMNode).getPosition());
            this.fLeafListType[n2] = cMNode.type();
            ++n2;
        } else if (cMNode.type() == 4 || cMNode.type() == 5) {
            n2 = this.postTreeBuildInit(((CMBinOp)cMNode).getLeft(), n2);
            n2 = this.postTreeBuildInit(((CMBinOp)cMNode).getRight(), n2);
        } else if (cMNode.type() == 2 || cMNode.type() == 3 || cMNode.type() == 1) {
            n2 = this.postTreeBuildInit(((CMUniOp)cMNode).getChild(), n2);
        } else if (cMNode.type() == 0) {
            QName qName = ((CMLeaf)cMNode).getElement();
            if (qName.localpart != fEpsilonString) {
                this.fLeafList[n2] = (CMLeaf)cMNode;
                this.fLeafListType[n2] = 0;
                ++n2;
            }
        } else {
            throw new RuntimeException("ImplementationMessages.VAL_NIICM: type=" + cMNode.type());
        }
        return n2;
    }

    static {
        fEpsilonString = fEpsilonString.intern();
        fEOCString = fEOCString.intern();
    }
}

