/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.PushInstruction;
import org.apache.bcel.generic.ReferenceType;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.TypedInstruction;
import org.apache.bcel.generic.Visitor;

public class ACONST_NULL
extends Instruction
implements PushInstruction,
TypedInstruction {
    public ACONST_NULL() {
        super(1, 1);
    }

    public Type getType(ConstantPoolGen constantPoolGen) {
        return Type.NULL;
    }

    public void accept(Visitor visitor) {
        visitor.visitStackProducer(this);
        visitor.visitPushInstruction(this);
        visitor.visitTypedInstruction(this);
        visitor.visitACONST_NULL(this);
    }
}

