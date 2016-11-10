/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.generic.CPInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldOrMethod;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.TypedInstruction;

public abstract class FieldInstruction
extends FieldOrMethod
implements TypedInstruction {
    FieldInstruction() {
    }

    protected FieldInstruction(short s2, int n2) {
        super(s2, n2);
    }

    public String toString(ConstantPool constantPool) {
        return Constants.OPCODE_NAMES[this.opcode] + " " + constantPool.constantToString(this.index, 9);
    }

    protected int getFieldSize(ConstantPoolGen constantPoolGen) {
        return this.getType(constantPoolGen).getSize();
    }

    public Type getType(ConstantPoolGen constantPoolGen) {
        return this.getFieldType(constantPoolGen);
    }

    public Type getFieldType(ConstantPoolGen constantPoolGen) {
        return Type.getType(this.getSignature(constantPoolGen));
    }
}

