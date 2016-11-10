/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.LoadInstruction;
import org.apache.bcel.generic.Visitor;

public class ALOAD
extends LoadInstruction {
    ALOAD() {
        super(25, 42);
    }

    public ALOAD(int n2) {
        super(25, 42, n2);
    }

    public void accept(Visitor visitor) {
        super.accept(visitor);
        visitor.visitALOAD(this);
    }
}

