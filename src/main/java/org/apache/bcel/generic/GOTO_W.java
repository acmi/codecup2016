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
import org.apache.bcel.generic.Visitor;
import org.apache.bcel.util.ByteSequence;

public class GOTO_W
extends GotoInstruction {
    GOTO_W() {
    }

    public GOTO_W(InstructionHandle instructionHandle) {
        super(200, instructionHandle);
        this.length = 5;
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        this.index = this.getTargetOffset();
        dataOutputStream.writeByte(this.opcode);
        dataOutputStream.writeInt(this.index);
    }

    protected void initFromFile(ByteSequence byteSequence, boolean bl) throws IOException {
        this.index = byteSequence.readInt();
        this.length = 5;
    }

    public void accept(Visitor visitor) {
        visitor.visitUnconditionalBranch(this);
        visitor.visitBranchInstruction(this);
        visitor.visitGotoInstruction(this);
        visitor.visitGOTO_W(this);
    }
}

