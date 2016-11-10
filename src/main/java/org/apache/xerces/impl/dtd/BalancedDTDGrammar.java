/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dtd;

import org.apache.xerces.impl.dtd.DTDGrammar;
import org.apache.xerces.impl.dtd.XMLDTDDescription;
import org.apache.xerces.impl.dtd.XMLElementDecl;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XNIException;

final class BalancedDTDGrammar
extends DTDGrammar {
    private boolean fMixed;
    private int fDepth = 0;
    private short[] fOpStack = null;
    private int[][] fGroupIndexStack;
    private int[] fGroupIndexStackSizes;

    public BalancedDTDGrammar(SymbolTable symbolTable, XMLDTDDescription xMLDTDDescription) {
        super(symbolTable, xMLDTDDescription);
    }

    public final void startContentModel(String string, Augmentations augmentations) throws XNIException {
        this.fDepth = 0;
        this.initializeContentModelStacks();
        super.startContentModel(string, augmentations);
    }

    public final void startGroup(Augmentations augmentations) throws XNIException {
        ++this.fDepth;
        this.initializeContentModelStacks();
        this.fMixed = false;
    }

    public final void pcdata(Augmentations augmentations) throws XNIException {
        this.fMixed = true;
    }

    public final void element(String string, Augmentations augmentations) throws XNIException {
        this.addToCurrentGroup(this.addUniqueLeafNode(string));
    }

    public final void separator(short s2, Augmentations augmentations) throws XNIException {
        if (s2 == 0) {
            this.fOpStack[this.fDepth] = 4;
        } else if (s2 == 1) {
            this.fOpStack[this.fDepth] = 5;
        }
    }

    public final void occurrence(short s2, Augmentations augmentations) throws XNIException {
        if (!this.fMixed) {
            int n2 = this.fGroupIndexStackSizes[this.fDepth] - 1;
            if (s2 == 2) {
                this.fGroupIndexStack[this.fDepth][n2] = this.addContentSpecNode(1, this.fGroupIndexStack[this.fDepth][n2], -1);
            } else if (s2 == 3) {
                this.fGroupIndexStack[this.fDepth][n2] = this.addContentSpecNode(2, this.fGroupIndexStack[this.fDepth][n2], -1);
            } else if (s2 == 4) {
                this.fGroupIndexStack[this.fDepth][n2] = this.addContentSpecNode(3, this.fGroupIndexStack[this.fDepth][n2], -1);
            }
        }
    }

    public final void endGroup(Augmentations augmentations) throws XNIException {
        int n2 = this.fGroupIndexStackSizes[this.fDepth];
        int n3 = n2 > 0 ? this.addContentSpecNodes(0, n2 - 1) : this.addUniqueLeafNode(null);
        --this.fDepth;
        this.addToCurrentGroup(n3);
    }

    public final void endDTD(Augmentations augmentations) throws XNIException {
        super.endDTD(augmentations);
        this.fOpStack = null;
        this.fGroupIndexStack = null;
        this.fGroupIndexStackSizes = null;
    }

    protected final void addContentSpecToElement(XMLElementDecl xMLElementDecl) {
        int n2 = this.fGroupIndexStackSizes[0] > 0 ? this.fGroupIndexStack[0][0] : -1;
        this.setContentSpecIndex(this.fCurrentElementIndex, n2);
    }

    private int addContentSpecNodes(int n2, int n3) {
        if (n2 == n3) {
            return this.fGroupIndexStack[this.fDepth][n2];
        }
        int n4 = (n2 + n3) / 2;
        return this.addContentSpecNode(this.fOpStack[this.fDepth], this.addContentSpecNodes(n2, n4), this.addContentSpecNodes(n4 + 1, n3));
    }

    private void initializeContentModelStacks() {
        if (this.fOpStack == null) {
            this.fOpStack = new short[8];
            this.fGroupIndexStack = new int[8][];
            this.fGroupIndexStackSizes = new int[8];
        } else if (this.fDepth == this.fOpStack.length) {
            short[] arrs = new short[this.fDepth * 2];
            System.arraycopy(this.fOpStack, 0, arrs, 0, this.fDepth);
            this.fOpStack = arrs;
            int[][] arrarrn = new int[this.fDepth * 2][];
            System.arraycopy(this.fGroupIndexStack, 0, arrarrn, 0, this.fDepth);
            this.fGroupIndexStack = arrarrn;
            int[] arrn = new int[this.fDepth * 2];
            System.arraycopy(this.fGroupIndexStackSizes, 0, arrn, 0, this.fDepth);
            this.fGroupIndexStackSizes = arrn;
        }
        this.fOpStack[this.fDepth] = -1;
        this.fGroupIndexStackSizes[this.fDepth] = 0;
    }

    private void addToCurrentGroup(int n2) {
        int[] arrn = this.fGroupIndexStack[this.fDepth];
        int[] arrn2 = this.fGroupIndexStackSizes;
        int n3 = this.fDepth;
        int n4 = arrn2[n3];
        arrn2[n3] = n4 + 1;
        int n5 = n4;
        if (arrn == null) {
            arrn = new int[8];
            this.fGroupIndexStack[this.fDepth] = arrn;
        } else if (n5 == arrn.length) {
            int[] arrn3 = new int[arrn.length * 2];
            System.arraycopy(arrn, 0, arrn3, 0, arrn.length);
            arrn = arrn3;
            this.fGroupIndexStack[this.fDepth] = arrn;
        }
        arrn[n5] = n2;
    }
}

