/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.GotoInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.UnconditionalBranch;
import org.apache.bcel.generic.VariableLengthInstruction;
import org.apache.bcel.generic.Visitor;

public class GOTO
extends GotoInstruction
implements VariableLengthInstruction {
    GOTO() {
    }

    public GOTO(InstructionHandle instructionHandle) {
        super(167, instructionHandle);
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        this.index = this.getTargetOffset();
        if (this.opcode == 167) {
            BranchInstruction.super.dump(dataOutputStream);
        } else {
            this.index = this.getTargetOffset();
            dataOutputStream.writeByte(this.opcode);
            dataOutputStream.writeInt(this.index);
        }
    }

    protected int updatePosition(int n2, int n3) {
        int n4 = this.getTargetOffset();
        this.position += n2;
        if (Math.abs(n4) >= 32767 - n3) {
            this.opcode = 200;
            this.length = 5;
            return 2;
        }
        return 0;
    }

    public void accept(Visitor visitor) {
        visitor.visitVariableLengthInstruction(this);
        visitor.visitUnconditionalBranch(this);
        visitor.visitBranchInstruction(this);
        visitor.visitGotoInstruction(this);
        visitor.visitGOTO(this);
    }
}

