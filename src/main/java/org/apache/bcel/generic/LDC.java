/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.CPInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ExceptionThrower;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PushInstruction;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.TypedInstruction;
import org.apache.bcel.generic.Visitor;
import org.apache.bcel.util.ByteSequence;

public class LDC
extends CPInstruction
implements ExceptionThrower,
PushInstruction,
TypedInstruction {
    LDC() {
    }

    public LDC(int n2) {
        super(19, n2);
        this.setSize();
    }

    protected final void setSize() {
        if (this.index <= 255) {
            this.opcode = 18;
            this.length = 2;
        } else {
            this.opcode = 19;
            this.length = 3;
        }
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.opcode);
        if (this.length == 2) {
            dataOutputStream.writeByte(this.index);
        } else {
            dataOutputStream.writeShort(this.index);
        }
    }

    public final void setIndex(int n2) {
        super.setIndex(n2);
        this.setSize();
    }

    protected void initFromFile(ByteSequence byteSequence, boolean bl) throws IOException {
        this.length = 2;
        this.index = byteSequence.readUnsignedByte();
    }

    public Type getType(ConstantPoolGen constantPoolGen) {
        switch (constantPoolGen.getConstantPool().getConstant(this.index).getTag()) {
            case 8: {
                return Type.STRING;
            }
            case 4: {
                return Type.FLOAT;
            }
            case 3: {
                return Type.INT;
            }
        }
        throw new RuntimeException("Unknown or invalid constant type at " + this.index);
    }

    public void accept(Visitor visitor) {
        visitor.visitStackProducer(this);
        visitor.visitPushInstruction(this);
        visitor.visitExceptionThrower(this);
        visitor.visitTypedInstruction(this);
        visitor.visitCPInstruction(this);
        visitor.visitLDC(this);
    }
}

