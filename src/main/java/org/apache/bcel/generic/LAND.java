/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.ArithmeticInstruction;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.TypedInstruction;
import org.apache.bcel.generic.Visitor;

public class LAND
extends ArithmeticInstruction {
    public LAND() {
        super(127);
    }

    public void accept(Visitor visitor) {
        visitor.visitTypedInstruction(this);
        visitor.visitStackProducer(this);
        visitor.visitStackConsumer(this);
        visitor.visitArithmeticInstruction(this);
        visitor.visitLAND(this);
    }
}
