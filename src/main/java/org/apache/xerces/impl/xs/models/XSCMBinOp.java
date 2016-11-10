/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.models;

import org.apache.xerces.impl.dtd.models.CMNode;
import org.apache.xerces.impl.dtd.models.CMStateSet;

public class XSCMBinOp
extends CMNode {
    private CMNode fLeftChild;
    private CMNode fRightChild;

    public XSCMBinOp(int n2, CMNode cMNode, CMNode cMNode2) {
        super(n2);
        if (this.type() != 101 && this.type() != 102) {
            throw new RuntimeException("ImplementationMessages.VAL_BST");
        }
        this.fLeftChild = cMNode;
        this.fRightChild = cMNode2;
    }

    final CMNode getLeft() {
        return this.fLeftChild;
    }

    final CMNode getRight() {
        return this.fRightChild;
    }

    public boolean isNullable() {
        if (this.type() == 101) {
            return this.fLeftChild.isNullable() || this.fRightChild.isNullable();
        }
        if (this.type() == 102) {
            return this.fLeftChild.isNullable() && this.fRightChild.isNullable();
        }
        throw new RuntimeException("ImplementationMessages.VAL_BST");
    }

    protected void calcFirstPos(CMStateSet cMStateSet) {
        if (this.type() == 101) {
            cMStateSet.setTo(this.fLeftChild.firstPos());
            cMStateSet.union(this.fRightChild.firstPos());
        } else if (this.type() == 102) {
            cMStateSet.setTo(this.fLeftChild.firstPos());
            if (this.fLeftChild.isNullable()) {
                cMStateSet.union(this.fRightChild.firstPos());
            }
        } else {
            throw new RuntimeException("ImplementationMessages.VAL_BST");
        }
    }

    protected void calcLastPos(CMStateSet cMStateSet) {
        if (this.type() == 101) {
            cMStateSet.setTo(this.fLeftChild.lastPos());
            cMStateSet.union(this.fRightChild.lastPos());
        } else if (this.type() == 102) {
            cMStateSet.setTo(this.fRightChild.lastPos());
            if (this.fRightChild.isNullable()) {
                cMStateSet.union(this.fLeftChild.lastPos());
            }
        } else {
            throw new RuntimeException("ImplementationMessages.VAL_BST");
        }
    }
}

