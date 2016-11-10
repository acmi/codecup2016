/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.Visitor;

public class NOP
extends Instruction {
    public NOP() {
        super(0, 1);
    }

    public void accept(Visitor visitor) {
        visitor.visitNOP(this);
    }
}

