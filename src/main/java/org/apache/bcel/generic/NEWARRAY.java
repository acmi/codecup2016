/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.Constants;
import org.apache.bcel.generic.AllocationInstruction;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.ExceptionThrower;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.Visitor;
import org.apache.bcel.util.ByteSequence;

public class NEWARRAY
extends Instruction
implements AllocationInstruction,
ExceptionThrower,
StackProducer {
    private byte type;

    NEWARRAY() {
    }

    public NEWARRAY(byte by) {
        super(188, 2);
        this.type = by;
    }

    public NEWARRAY(BasicType basicType) {
        this(basicType.getType());
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.opcode);
        dataOutputStream.writeByte(this.type);
    }

    public String toString(boolean bl) {
        return super.toString(bl) + " " + Constants.TYPE_NAMES[this.type];
    }

    protected void initFromFile(ByteSequence byteSequence, boolean bl) throws IOException {
        this.type = byteSequence.readByte();
        this.length = 2;
    }

    public void accept(Visitor visitor) {
        visitor.visitAllocationInstruction(this);
        visitor.visitExceptionThrower(this);
        visitor.visitStackProducer(this);
        visitor.visitNEWARRAY(this);
    }
}

