/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.Visitor;
import org.apache.bcel.util.ByteSequence;

public class IINC
extends LocalVariableInstruction {
    private boolean wide;
    private int c;

    IINC() {
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        if (this.wide) {
            dataOutputStream.writeByte(196);
        }
        dataOutputStream.writeByte(this.opcode);
        if (this.wide) {
            dataOutputStream.writeShort(this.n);
            dataOutputStream.writeShort(this.c);
        } else {
            dataOutputStream.writeByte(this.n);
            dataOutputStream.writeByte(this.c);
        }
    }

    private final void setWide() {
        this.wide = this.n > 65535 || Math.abs(this.c) > 127;
        this.length = this.wide ? 6 : 3;
    }

    protected void initFromFile(ByteSequence byteSequence, boolean bl) throws IOException {
        this.wide = bl;
        if (bl) {
            this.length = 6;
            this.n = byteSequence.readUnsignedShort();
            this.c = byteSequence.readShort();
        } else {
            this.length = 3;
            this.n = byteSequence.readUnsignedByte();
            this.c = byteSequence.readByte();
        }
    }

    public String toString(boolean bl) {
        return super.toString(bl) + " " + this.c;
    }

    public final void setIndex(int n2) {
        if (n2 < 0) {
            throw new ClassGenException("Negative index value: " + n2);
        }
        this.n = n2;
        this.setWide();
    }

    public Type getType(ConstantPoolGen constantPoolGen) {
        return Type.INT;
    }

    public void accept(Visitor visitor) {
        visitor.visitLocalVariableInstruction(this);
        visitor.visitIINC(this);
    }
}

