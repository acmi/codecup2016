/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.LoadInstruction;
import org.apache.bcel.generic.Visitor;

public class ILOAD
extends LoadInstruction {
    ILOAD() {
        super(21, 26);
    }

    public ILOAD(int n2) {
        super(21, 26, n2);
    }

    public void accept(Visitor visitor) {
        super.accept(visitor);
        visitor.visitILOAD(this);
    }
}

