/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionTargeter;
import org.apache.bcel.util.ByteSequence;

public abstract class BranchInstruction
extends Instruction
implements InstructionTargeter {
    protected int index;
    protected InstructionHandle target;
    protected int position;

    BranchInstruction() {
    }

    protected BranchInstruction(short s2, InstructionHandle instructionHandle) {
        super(s2, 3);
        this.setTarget(instructionHandle);
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.opcode);
        this.index = this.getTargetOffset();
        if (Math.abs(this.index) >= 32767) {
            throw new ClassGenException("Branch target offset too large for short");
        }
        dataOutputStream.writeShort(this.index);
    }

    protected int getTargetOffset(InstructionHandle instructionHandle) {
        if (instructionHandle == null) {
            throw new ClassGenException("Target of " + super.toString(true) + " is invalid null handle");
        }
        int n2 = instructionHandle.getPosition();
        if (n2 < 0) {
            throw new ClassGenException("Invalid branch target position offset for " + super.toString(true) + ":" + n2 + ":" + instructionHandle);
        }
        return n2 - this.position;
    }

    protected int getTargetOffset() {
        return this.getTargetOffset(this.target);
    }

    protected int updatePosition(int n2, int n3) {
        this.position += n2;
        return 0;
    }

    public String toString(boolean bl) {
        String string = super.toString(bl);
        String string2 = "null";
        if (bl) {
            if (this.target != null) {
                string2 = this.target.getInstruction() == this ? "<points to itself>" : (this.target.getInstruction() == null ? "<null instruction!!!?>" : this.target.getInstruction().toString(false));
            }
        } else if (this.target != null) {
            this.index = this.getTargetOffset();
            string2 = "" + (this.index + this.position);
        }
        return string + " -> " + string2;
    }

    protected void initFromFile(ByteSequence byteSequence, boolean bl) throws IOException {
        this.length = 3;
        this.index = byteSequence.readShort();
    }

    public final int getIndex() {
        return this.index;
    }

    public InstructionHandle getTarget() {
        return this.target;
    }

    public void setTarget(InstructionHandle instructionHandle) {
        BranchInstruction.notifyTarget(this.target, instructionHandle, this);
        this.target = instructionHandle;
    }

    static final void notifyTarget(InstructionHandle instructionHandle, InstructionHandle instructionHandle2, InstructionTargeter instructionTargeter) {
        if (instructionHandle != null) {
            instructionHandle.removeTargeter(instructionTargeter);
        }
        if (instructionHandle2 != null) {
            instructionHandle2.addTargeter(instructionTargeter);
        }
    }

    public void updateTarget(InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        if (this.target != instructionHandle) {
            throw new ClassGenException("Not targeting " + instructionHandle + ", but " + this.target);
        }
        this.setTarget(instructionHandle2);
    }

    void dispose() {
        this.setTarget(null);
        this.index = -1;
        this.position = -1;
    }
}

