/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.IFNULL;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.Visitor;

public class IFNONNULL
extends IfInstruction {
    IFNONNULL() {
    }

    public IFNONNULL(InstructionHandle instructionHandle) {
        super(199, instructionHandle);
    }

    public IfInstruction negate() {
        return new IFNULL(this.target);
    }

    public void accept(Visitor visitor) {
        visitor.visitStackConsumer(this);
        visitor.visitBranchInstruction(this);
        visitor.visitIfInstruction(this);
        visitor.visitIFNONNULL(this);
    }
}

