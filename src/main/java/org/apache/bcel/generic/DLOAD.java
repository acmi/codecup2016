/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.LoadInstruction;
import org.apache.bcel.generic.Visitor;

public class DLOAD
extends LoadInstruction {
    DLOAD() {
        super(24, 38);
    }

    public DLOAD(int n2) {
        super(24, 38, n2);
    }

    public void accept(Visitor visitor) {
        super.accept(visitor);
        visitor.visitDLOAD(this);
    }
}

