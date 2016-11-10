/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.PushInstruction;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.TypedInstruction;
import org.apache.bcel.generic.Visitor;

public abstract class LoadInstruction
extends LocalVariableInstruction
implements PushInstruction {
    LoadInstruction(short s2, short s3) {
        super(s2, s3);
    }

    protected LoadInstruction(short s2, short s3, int n2) {
        super(s2, s3, n2);
    }

    public void accept(Visitor visitor) {
        visitor.visitStackProducer(this);
        visitor.visitPushInstruction(this);
        visitor.visitTypedInstruction(this);
        visitor.visitLocalVariableInstruction(this);
        visitor.visitLoadInstruction(this);
    }
}

