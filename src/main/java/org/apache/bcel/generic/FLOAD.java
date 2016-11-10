/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.LoadInstruction;
import org.apache.bcel.generic.Visitor;

public class FLOAD
extends LoadInstruction {
    FLOAD() {
        super(23, 34);
    }

    public FLOAD(int n2) {
        super(23, 34, n2);
    }

    public void accept(Visitor visitor) {
        super.accept(visitor);
        visitor.visitFLOAD(this);
    }
}

