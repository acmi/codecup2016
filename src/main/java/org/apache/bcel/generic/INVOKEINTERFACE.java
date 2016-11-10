/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.generic.CPInstruction;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ExceptionThrower;
import org.apache.bcel.generic.FieldOrMethod;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LoadClass;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.TypedInstruction;
import org.apache.bcel.generic.Visitor;
import org.apache.bcel.util.ByteSequence;

public final class INVOKEINTERFACE
extends InvokeInstruction {
    private int nargs;

    INVOKEINTERFACE() {
    }

    public INVOKEINTERFACE(int n2, int n3) {
        super(185, n2);
        this.length = 5;
        if (n3 < 1) {
            throw new ClassGenException("Number of arguments must be > 0 " + n3);
        }
        this.nargs = n3;
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.opcode);
        dataOutputStream.writeShort(this.index);
        dataOutputStream.writeByte(this.nargs);
        dataOutputStream.writeByte(0);
    }

    protected void initFromFile(ByteSequence byteSequence, boolean bl) throws IOException {
        CPInstruction.super.initFromFile(byteSequence, bl);
        this.length = 5;
        this.nargs = byteSequence.readUnsignedByte();
        byteSequence.readByte();
    }

    public String toString(ConstantPool constantPool) {
        return super.toString(constantPool) + " " + this.nargs;
    }

    public int consumeStack(ConstantPoolGen constantPoolGen) {
        return this.nargs;
    }

    public void accept(Visitor visitor) {
        visitor.visitExceptionThrower(this);
        visitor.visitTypedInstruction(this);
        visitor.visitStackConsumer(this);
        visitor.visitStackProducer(this);
        visitor.visitLoadClass(this);
        visitor.visitCPInstruction(this);
        visitor.visitFieldOrMethod(this);
        visitor.visitInvokeInstruction(this);
        visitor.visitINVOKEINTERFACE(this);
    }
}

