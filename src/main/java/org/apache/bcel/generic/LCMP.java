/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.Visitor;

public class LCMP
extends Instruction {
    public LCMP() {
        super(148, 1);
    }

    public void accept(Visitor visitor) {
        visitor.visitLCMP(this);
    }
}

