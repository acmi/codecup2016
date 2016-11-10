/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.util.StringTokenizer;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.generic.CPInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ExceptionThrower;
import org.apache.bcel.generic.FieldOrMethod;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.TypedInstruction;

public abstract class InvokeInstruction
extends FieldOrMethod
implements ExceptionThrower,
StackConsumer,
StackProducer,
TypedInstruction {
    InvokeInstruction() {
    }

    protected InvokeInstruction(short s2, int n2) {
        super(s2, n2);
    }

    public String toString(ConstantPool constantPool) {
        Constant constant = constantPool.getConstant(this.index);
        StringTokenizer stringTokenizer = new StringTokenizer(constantPool.constantToString(constant));
        return Constants.OPCODE_NAMES[this.opcode] + " " + stringTokenizer.nextToken().replace('.', '/') + stringTokenizer.nextToken();
    }

    public int consumeStack(ConstantPoolGen constantPoolGen) {
        String string = this.getSignature(constantPoolGen);
        Type[] arrtype = Type.getArgumentTypes(string);
        int n2 = this.opcode == 184 ? 0 : 1;
        int n3 = arrtype.length;
        int n4 = 0;
        while (n4 < n3) {
            n2 += arrtype[n4].getSize();
            ++n4;
        }
        return n2;
    }

    public int produceStack(ConstantPoolGen constantPoolGen) {
        return this.getReturnType(constantPoolGen).getSize();
    }

    public Type getType(ConstantPoolGen constantPoolGen) {
        return this.getReturnType(constantPoolGen);
    }

    public Type getReturnType(ConstantPoolGen constantPoolGen) {
        return Type.getReturnType(this.getSignature(constantPoolGen));
    }
}

