/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.IF_ICMPGT;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.Visitor;

public class IF_ICMPLE
extends IfInstruction {
    IF_ICMPLE() {
    }

    public IF_ICMPLE(InstructionHandle instructionHandle) {
        super(164, instructionHandle);
    }

    public IfInstruction negate() {
        return new IF_ICMPGT(this.target);
    }

    public void accept(Visitor visitor) {
        visitor.visitStackConsumer(this);
        visitor.visitBranchInstruction(this);
        visitor.visitIfInstruction(this);
        visitor.visitIF_ICMPLE(this);
    }
}

