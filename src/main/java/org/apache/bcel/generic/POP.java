/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.PopInstruction;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.StackInstruction;
import org.apache.bcel.generic.Visitor;

public class POP
extends StackInstruction
implements PopInstruction {
    public POP() {
        super(87);
    }

    public void accept(Visitor visitor) {
        visitor.visitStackConsumer(this);
        visitor.visitPopInstruction(this);
        visitor.visitStackInstruction(this);
        visitor.visitPOP(this);
    }
}

