/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.Visitor;

public class IFNE
extends IfInstruction {
    IFNE() {
    }

    public IFNE(InstructionHandle instructionHandle) {
        super(154, instructionHandle);
    }

    public IfInstruction negate() {
        return new IFEQ(this.target);
    }

    public void accept(Visitor visitor) {
        visitor.visitStackConsumer(this);
        visitor.visitBranchInstruction(this);
        visitor.visitIfInstruction(this);
        visitor.visitIFNE(this);
    }
}

