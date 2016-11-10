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

public class ICONST
extends Instruction
implements ConstantPushInstruction,
TypedInstruction {
    private int value;

    ICONST() {
    }

    public ICONST(int n2) {
        super(3, 1);
        if (n2 < -1 || n2 > 5) {
            throw new ClassGenException("ICONST can be used only for value between -1 and 5: " + n2);
        }
        this.opcode = (short)(3 + n2);
        this.value = n2;
    }

    public Type getType(ConstantPoolGen constantPoolGen) {
        return Type.INT;
    }

    public void accept(Visitor visitor) {
        visitor.visitPushInstruction(this);
        visitor.visitStackProducer(this);
        visitor.visitTypedInstruction(this);
        visitor.visitConstantPushInstruction(this);
        visitor.visitICONST(this);
    }
}

