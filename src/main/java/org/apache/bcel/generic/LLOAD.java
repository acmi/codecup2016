/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.LoadInstruction;
import org.apache.bcel.generic.Visitor;

public class LLOAD
extends LoadInstruction {
    LLOAD() {
        super(22, 30);
    }

    public LLOAD(int n2) {
        super(22, 30, n2);
    }

    public void accept(Visitor visitor) {
        super.accept(visitor);
        visitor.visitLLOAD(this);
    }
}

