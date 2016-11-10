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

public class FCONST
extends Instruction
implements ConstantPushInstruction,
TypedInstruction {
    private float value;

    FCONST() {
    }

    public FCONST(float f2) {
        super(11, 1);
        if ((double)f2 == 0.0) {
            this.opcode = 11;
        } else if ((double)f2 == 1.0) {
            this.opcode = 12;
        } else if ((double)f2 == 2.0) {
            this.opcode = 13;
        } else {
            throw new ClassGenException("FCONST can be used only for 0.0, 1.0 and 2.0: " + f2);
        }
        this.value = f2;
    }

    public Type getType(ConstantPoolGen constantPoolGen) {
        return Type.FLOAT;
    }

    public void accept(Visitor visitor) {
        visitor.visitPushInstruction(this);
        visitor.visitStackProducer(this);
        visitor.visitTypedInstruction(this);
        visitor.visitConstantPushInstruction(this);
        visitor.visitFCONST(this);
    }
}

