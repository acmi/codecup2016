/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dtd.models;

import org.apache.xerces.impl.dtd.models.CMNode;
import org.apache.xerces.impl.dtd.models.CMStateSet;

public class CMUniOp
extends CMNode {
    private final CMNode fChild;

    public CMUniOp(int n2, CMNode cMNode) {
        super(n2);
        if (this.type() != 1 && this.type() != 2 && this.type() != 3) {
            throw new RuntimeException("ImplementationMessages.VAL_UST");
        }
        this.fChild = cMNode;
    }

    final CMNode getChild() {
        return this.fChild;
    }

    public boolean isNullable() {
        if (this.type() == 3) {
            return this.fChild.isNullable();
        }
        return true;
    }

    protected void calcFirstPos(CMStateSet cMStateSet) {
        cMStateSet.setTo(this.fChild.firstPos());
    }

    protected void calcLastPos(CMStateSet cMStateSet) {
        cMStateSet.setTo(this.fChild.lastPos());
    }
}

