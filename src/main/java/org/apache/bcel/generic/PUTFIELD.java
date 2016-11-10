/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.CPInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ExceptionThrower;
import org.apache.bcel.generic.FieldInstruction;
import org.apache.bcel.generic.FieldOrMethod;
import org.apache.bcel.generic.LoadClass;
import org.apache.bcel.generic.PopInstruction;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.TypedInstruction;
import org.apache.bcel.generic.Visitor;

public class PUTFIELD
extends FieldInstruction
implements ExceptionThrower,
PopInstruction {
    PUTFIELD() {
    }

    public PUTFIELD(int n2) {
        super(181, n2);
    }

    public int consumeStack(ConstantPoolGen constantPoolGen) {
        return this.getFieldSize(constantPoolGen) + 1;
    }

    public void accept(Visitor visitor) {
        visitor.visitExceptionThrower(this);
        visitor.visitStackConsumer(this);
        visitor.visitPopInstruction(this);
        visitor.visitTypedInstruction(this);
        visitor.visitLoadClass(this);
        visitor.visitCPInstruction(this);
        visitor.visitFieldOrMethod(this);
        visitor.visitFieldInstruction(this);
        visitor.visitPUTFIELD(this);
    }
}

