/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.CPInstruction;
import org.apache.bcel.generic.ExceptionThrower;
import org.apache.bcel.generic.LoadClass;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.TypedInstruction;
import org.apache.bcel.generic.Visitor;

public class CHECKCAST
extends CPInstruction
implements ExceptionThrower,
LoadClass,
StackConsumer,
StackProducer {
    CHECKCAST() {
    }

    public CHECKCAST(int n2) {
        super(192, n2);
    }

    public void accept(Visitor visitor) {
        visitor.visitLoadClass(this);
        visitor.visitExceptionThrower(this);
        visitor.visitStackProducer(this);
        visitor.visitStackConsumer(this);
        visitor.visitTypedInstruction(this);
        visitor.visitCPInstruction(this);
        visitor.visitCHECKCAST(this);
    }
}

