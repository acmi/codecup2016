/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.IFNONNULL;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.Visitor;

public class IFNULL
extends IfInstruction {
    IFNULL() {
    }

    public IFNULL(InstructionHandle instructionHandle) {
        super(198, instructionHandle);
    }

    public IfInstruction negate() {
        return new IFNONNULL(this.target);
    }

    public void accept(Visitor visitor) {
        visitor.visitStackConsumer(this);
        visitor.visitBranchInstruction(this);
        visitor.visitIfInstruction(this);
        visitor.visitIFNULL(this);
    }
}

