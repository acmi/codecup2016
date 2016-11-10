/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.AllocationInstruction;
import org.apache.bcel.generic.CPInstruction;
import org.apache.bcel.generic.ExceptionThrower;
import org.apache.bcel.generic.LoadClass;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.TypedInstruction;
import org.apache.bcel.generic.Visitor;

public class ANEWARRAY
extends CPInstruction
implements AllocationInstruction,
ExceptionThrower,
LoadClass,
StackProducer {
    ANEWARRAY() {
    }

    public ANEWARRAY(int n2) {
        super(189, n2);
    }

    public void accept(Visitor visitor) {
        visitor.visitLoadClass(this);
        visitor.visitAllocationInstruction(this);
        visitor.visitExceptionThrower(this);
        visitor.visitStackProducer(this);
        visitor.visitTypedInstruction(this);
        visitor.visitCPInstruction(this);
        visitor.visitANEWARRAY(this);
    }
}

