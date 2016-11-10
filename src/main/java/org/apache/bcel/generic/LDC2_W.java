/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.CPInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.PushInstruction;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.TypedInstruction;
import org.apache.bcel.generic.Visitor;

public class LDC2_W
extends CPInstruction
implements PushInstruction,
TypedInstruction {
    LDC2_W() {
    }

    public LDC2_W(int n2) {
        super(20, n2);
    }

    public Type getType(ConstantPoolGen constantPoolGen) {
        switch (constantPoolGen.getConstantPool().getConstant(this.index).getTag()) {
            case 5: {
                return Type.LONG;
            }
            case 6: {
                return Type.DOUBLE;
            }
        }
        throw new RuntimeException("Unknown constant type " + this.opcode);
    }

    public void accept(Visitor visitor) {
        visitor.visitStackProducer(this);
        visitor.visitPushInstruction(this);
        visitor.visitTypedInstruction(this);
        visitor.visitCPInstruction(this);
        visitor.visitLDC2_W(this);
    }
}

