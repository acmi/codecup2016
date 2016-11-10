/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.IndexedInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.ReturnaddressType;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.TypedInstruction;
import org.apache.bcel.generic.Visitor;
import org.apache.bcel.util.ByteSequence;

public class RET
extends Instruction
implements IndexedInstruction,
TypedInstruction {
    private boolean wide;
    private int index;

    RET() {
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        if (this.wide) {
            dataOutputStream.writeByte(196);
        }
        dataOutputStream.writeByte(this.opcode);
        if (this.wide) {
            dataOutputStream.writeShort(this.index);
        } else {
            dataOutputStream.writeByte(this.index);
        }
    }

    private final void setWide() {
        this.wide = this.index > 255;
        this.length = this.wide ? 4 : 2;
    }

    protected void initFromFile(ByteSequence byteSequence, boolean bl) throws IOException {
        this.wide = bl;
        if (bl) {
            this.index = byteSequence.readUnsignedShort();
            this.length = 4;
        } else {
            this.index = byteSequence.readUnsignedByte();
            this.length = 2;
        }
    }

    public final int getIndex() {
        return this.index;
    }

    public final void setIndex(int n2) {
        if (n2 < 0) {
            throw new ClassGenException("Negative index value: " + n2);
        }
        this.index = n2;
        this.setWide();
    }

    public String toString(boolean bl) {
        return super.toString(bl) + " " + this.index;
    }

    public Type getType(ConstantPoolGen constantPoolGen) {
        return ReturnaddressType.NO_TARGET;
    }

    public void accept(Visitor visitor) {
        visitor.visitRET(this);
    }
}

