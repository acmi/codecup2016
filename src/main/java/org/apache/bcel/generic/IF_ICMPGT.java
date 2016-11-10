/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.IF_ICMPLE;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.Visitor;

public class IF_ICMPGT
extends IfInstruction {
    IF_ICMPGT() {
    }

    public IF_ICMPGT(InstructionHandle instructionHandle) {
        super(163, instructionHandle);
    }

    public IfInstruction negate() {
        return new IF_ICMPLE(this.target);
    }

    public void accept(Visitor visitor) {
        visitor.visitStackConsumer(this);
        visitor.visitBranchInstruction(this);
        visitor.visitIfInstruction(this);
        visitor.visitIF_ICMPGT(this);
    }
}

