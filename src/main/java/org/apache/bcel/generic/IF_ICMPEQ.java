/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.IF_ICMPNE;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.Visitor;

public class IF_ICMPEQ
extends IfInstruction {
    IF_ICMPEQ() {
    }

    public IF_ICMPEQ(InstructionHandle instructionHandle) {
        super(159, instructionHandle);
    }

    public IfInstruction negate() {
        return new IF_ICMPNE(this.target);
    }

    public void accept(Visitor visitor) {
        visitor.visitStackConsumer(this);
        visitor.visitBranchInstruction(this);
        visitor.visitIfInstruction(this);
        visitor.visitIF_ICMPEQ(this);
    }
}

