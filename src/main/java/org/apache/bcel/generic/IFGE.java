/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.IFLT;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.Visitor;

public class IFGE
extends IfInstruction {
    IFGE() {
    }

    public IFGE(InstructionHandle instructionHandle) {
        super(156, instructionHandle);
    }

    public IfInstruction negate() {
        return new IFLT(this.target);
    }

    public void accept(Visitor visitor) {
        visitor.visitStackConsumer(this);
        visitor.visitBranchInstruction(this);
        visitor.visitIfInstruction(this);
        visitor.visitIFGE(this);
    }
}

