/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.CPInstruction;
import org.apache.bcel.generic.ExceptionThrower;
import org.apache.bcel.generic.FieldOrMethod;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LoadClass;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.TypedInstruction;
import org.apache.bcel.generic.Visitor;

public class INVOKESPECIAL
extends InvokeInstruction {
    INVOKESPECIAL() {
    }

    public INVOKESPECIAL(int n2) {
        super(183, n2);
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
        visitor.visitINVOKESPECIAL(this);
    }
}

