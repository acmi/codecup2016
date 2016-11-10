/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ConstantPushInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.PushInstruction;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.TypedInstruction;
import org.apache.bcel.generic.Visitor;
import org.apache.bcel.util.ByteSequence;

public class SIPUSH
extends Instruction
implements ConstantPushInstruction {
    private short b;

    SIPUSH() {
    }

    public SIPUSH(short s2) {
        super(17, 3);
        this.b = s2;
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        dataOutputStream.writeShort(this.b);
    }

    public String toString(boolean bl) {
        return super.toString(bl) + " " + this.b;
    }

    protected void initFromFile(ByteSequence byteSequence, boolean bl) throws IOException {
        this.length = 3;
        this.b = byteSequence.readShort();
    }

    public Type getType(ConstantPoolGen constantPoolGen) {
        return Type.SHORT;
    }

    public void accept(Visitor visitor) {
        visitor.visitPushInstruction(this);
        visitor.visitStackProducer(this);
        visitor.visitTypedInstruction(this);
        visitor.visitConstantPushInstruction(this);
        visitor.visitSIPUSH(this);
    }
}

