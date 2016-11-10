/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.PopInstruction;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.TypedInstruction;
import org.apache.bcel.generic.Visitor;

public abstract class StoreInstruction
extends LocalVariableInstruction
implements PopInstruction {
    StoreInstruction(short s2, short s3) {
        super(s2, s3);
    }

    protected StoreInstruction(short s2, short s3, int n2) {
        super(s2, s3, n2);
    }

    public void accept(Visitor visitor) {
        visitor.visitStackConsumer(this);
        visitor.visitPopInstruction(this);
        visitor.visitStoreInstruction(this);
        visitor.visitTypedInstruction(this);
        visitor.visitLocalVariableInstruction(this);
        visitor.visitStoreInstruction(this);
    }
}

