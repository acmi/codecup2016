/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.ExceptionThrower;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.Visitor;

public class ARRAYLENGTH
extends Instruction
implements ExceptionThrower,
StackProducer {
    public ARRAYLENGTH() {
        super(190, 1);
    }

    public void accept(Visitor visitor) {
        visitor.visitExceptionThrower(this);
        visitor.visitStackProducer(this);
        visitor.visitARRAYLENGTH(this);
    }
}

