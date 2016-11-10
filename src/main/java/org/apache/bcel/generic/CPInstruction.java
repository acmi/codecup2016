/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.IndexedInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.TypedInstruction;
import org.apache.bcel.util.ByteSequence;

public abstract class CPInstruction
extends Instruction
implements IndexedInstruction,
TypedInstruction {
    protected int index;

    CPInstruction() {
    }

    protected CPInstruction(short s2, int n2) {
        super(s2, 3);
        this.setIndex(n2);
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.opcode);
        dataOutputStream.writeShort(this.index);
    }

    public String toString(boolean bl) {
        return super.toString(bl) + " " + this.index;
    }

    public String toString(ConstantPool constantPool) {
        Constant constant = constantPool.getConstant(this.index);
        String string = constantPool.constantToString(constant);
        if (constant instanceof ConstantClass) {
            string = string.replace('.', '/');
        }
        return Constants.OPCODE_NAMES[this.opcode] + " " + string;
    }

    protected void initFromFile(ByteSequence byteSequence, boolean bl) throws IOException {
        this.setIndex(byteSequence.readUnsignedShort());
        this.length = 3;
    }

    public final int getIndex() {
        return this.index;
    }

    public void setIndex(int n2) {
        if (n2 < 0) {
            throw new ClassGenException("Negative index value: " + n2);
        }
        this.index = n2;
    }

    public Type getType(ConstantPoolGen constantPoolGen) {
        ConstantPool constantPool = constantPoolGen.getConstantPool();
        String string = constantPool.getConstantString(this.index, 7);
        if (!string.startsWith("[")) {
            string = "L" + string + ";";
        }
        return Type.getType(string);
    }
}

