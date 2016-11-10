/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.IF_ICMPGE;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.Visitor;

public class IF_ICMPLT
extends IfInstruction {
    IF_ICMPLT() {
    }

    public IF_ICMPLT(InstructionHandle instructionHandle) {
        super(161, instructionHandle);
    }

    public IfInstruction negate() {
        return new IF_ICMPGE(this.target);
    }

    public void accept(Visitor visitor) {
        visitor.visitStackConsumer(this);
        visitor.visitBranchInstruction(this);
        visitor.visitIfInstruction(this);
        visitor.visitIF_ICMPLT(this);
    }
}

