/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.ExceptionThrower;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.TypedInstruction;
import org.apache.bcel.generic.Visitor;

public class IRETURN
extends ReturnInstruction {
    public IRETURN() {
        super(172);
    }

    public void accept(Visitor visitor) {
        visitor.visitExceptionThrower(this);
        visitor.visitTypedInstruction(this);
        visitor.visitStackConsumer(this);
        visitor.visitReturnInstruction(this);
        visitor.visitIRETURN(this);
    }
}

