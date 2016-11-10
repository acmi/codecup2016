/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ConstantPushInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.PushInstruction;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.TypedInstruction;
import org.apache.bcel.generic.Visitor;

public class LCONST
extends Instruction
implements ConstantPushInstruction,
TypedInstruction {
    private long value;

    LCONST() {
    }

    public LCONST(long l2) {
        super(9, 1);
        if (l2 == 0) {
            this.opcode = 9;
        } else if (l2 == 1) {
            this.opcode = 10;
        } else {
            throw new ClassGenException("LCONST can be used only for 0 and 1: " + l2);
        }
        this.value = l2;
    }

    public Type getType(ConstantPoolGen constantPoolGen) {
        return Type.LONG;
    }

    public void accept(Visitor visitor) {
        visitor.visitPushInstruction(this);
        visitor.visitStackProducer(this);
        visitor.visitTypedInstruction(this);
        visitor.visitConstantPushInstruction(this);
        visitor.visitLCONST(this);
    }
}

