/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.IFGT;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.Visitor;

public class IFLE
extends IfInstruction {
    IFLE() {
    }

    public IFLE(InstructionHandle instructionHandle) {
        super(158, instructionHandle);
    }

    public IfInstruction negate() {
        return new IFGT(this.target);
    }

    public void accept(Visitor visitor) {
        visitor.visitStackConsumer(this);
        visitor.visitBranchInstruction(this);
        visitor.visitIfInstruction(this);
        visitor.visitIFLE(this);
    }
}

