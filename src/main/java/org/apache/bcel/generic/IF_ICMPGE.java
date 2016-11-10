/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.IF_ICMPLT;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.Visitor;

public class IF_ICMPGE
extends IfInstruction {
    IF_ICMPGE() {
    }

    public IF_ICMPGE(InstructionHandle instructionHandle) {
        super(162, instructionHandle);
    }

    public IfInstruction negate() {
        return new IF_ICMPLT(this.target);
    }

    public void accept(Visitor visitor) {
        visitor.visitStackConsumer(this);
        visitor.visitBranchInstruction(this);
        visitor.visitIfInstruction(this);
        visitor.visitIF_ICMPGE(this);
    }
}

