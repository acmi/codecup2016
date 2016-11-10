/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.IndexedInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.TypedInstruction;
import org.apache.bcel.util.ByteSequence;

public abstract class LocalVariableInstruction
extends Instruction
implements IndexedInstruction,
TypedInstruction {
    protected int n = -1;
    private short c_tag = -1;
    private short canon_tag = -1;

    private final boolean wide() {
        return this.n > 255;
    }

    LocalVariableInstruction(short s2, short s3) {
        this.canon_tag = s2;
        this.c_tag = s3;
    }

    LocalVariableInstruction() {
    }

    protected LocalVariableInstruction(short s2, short s3, int n2) {
        super(s2, 2);
        this.c_tag = s3;
        this.canon_tag = s2;
        this.setIndex(n2);
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        if (this.wide()) {
            dataOutputStream.writeByte(196);
        }
        dataOutputStream.writeByte(this.opcode);
        if (this.length > 1) {
            if (this.wide()) {
                dataOutputStream.writeShort(this.n);
            } else {
                dataOutputStream.writeByte(this.n);
            }
        }
    }

    public String toString(boolean bl) {
        if (this.opcode >= 26 && this.opcode <= 45 || this.opcode >= 59 && this.opcode <= 78) {
            return super.toString(bl);
        }
        return super.toString(bl) + " " + this.n;
    }

    protected void initFromFile(ByteSequence byteSequence, boolean bl) throws IOException {
        if (bl) {
            this.n = byteSequence.readUnsignedShort();
            this.length = 4;
        } else if (this.opcode >= 21 && this.opcode <= 25 || this.opcode >= 54 && this.opcode <= 58) {
            this.n = byteSequence.readUnsignedByte();
            this.length = 2;
        } else if (this.opcode <= 45) {
            this.n = (this.opcode - 26) % 4;
            this.length = 1;
        } else {
            this.n = (this.opcode - 59) % 4;
            this.length = 1;
        }
    }

    public final int getIndex() {
        return this.n;
    }

    public void setIndex(int n2) {
        if (n2 < 0 || n2 > 65535) {
            throw new ClassGenException("Illegal value: " + n2);
        }
        this.n = n2;
        if (n2 >= 0 && n2 <= 3) {
            this.opcode = (short)(this.c_tag + n2);
            this.length = 1;
        } else {
            this.opcode = this.canon_tag;
            this.length = this.wide() ? 4 : 2;
        }
    }

    public Type getType(ConstantPoolGen constantPoolGen) {
        switch (this.canon_tag) {
            case 21: 
            case 54: {
                return Type.INT;
            }
            case 22: 
            case 55: {
                return Type.LONG;
            }
            case 24: 
            case 57: {
                return Type.DOUBLE;
            }
            case 23: 
            case 56: {
                return Type.FLOAT;
            }
            case 25: 
            case 58: {
                return Type.OBJECT;
            }
        }
        throw new ClassGenException("Oops: unknown case in switch" + this.canon_tag);
    }
}

