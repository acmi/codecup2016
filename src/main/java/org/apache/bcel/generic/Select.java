/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionTargeter;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.VariableLengthInstruction;
import org.apache.bcel.util.ByteSequence;

public abstract class Select
extends BranchInstruction
implements StackProducer,
VariableLengthInstruction {
    protected int[] match;
    protected int[] indices;
    protected InstructionHandle[] targets;
    protected int fixed_length;
    protected int match_length;
    protected int padding = 0;

    Select() {
    }

    Select(short s2, int[] arrn, InstructionHandle[] arrinstructionHandle, InstructionHandle instructionHandle) {
        super(s2, instructionHandle);
        this.targets = arrinstructionHandle;
        int n2 = 0;
        while (n2 < arrinstructionHandle.length) {
            BranchInstruction.notifyTarget(null, arrinstructionHandle[n2], this);
            ++n2;
        }
        this.match = arrn;
        this.match_length = arrn.length;
        if (this.match_length != arrinstructionHandle.length) {
            throw new ClassGenException("Match and target array have not the same length");
        }
        this.indices = new int[this.match_length];
    }

    protected int updatePosition(int n2, int n3) {
        this.position += n2;
        short s2 = this.length;
        this.padding = (4 - (this.position + 1) % 4) % 4;
        this.length = (short)(this.fixed_length + this.padding);
        return this.length - s2;
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.opcode);
        int n2 = 0;
        while (n2 < this.padding) {
            dataOutputStream.writeByte(0);
            ++n2;
        }
        this.index = this.getTargetOffset();
        dataOutputStream.writeInt(this.index);
    }

    protected void initFromFile(ByteSequence byteSequence, boolean bl) throws IOException {
        this.padding = (4 - byteSequence.getIndex() % 4) % 4;
        int n2 = 0;
        while (n2 < this.padding) {
            byte by = byteSequence.readByte();
            if (by != 0) {
                throw new ClassGenException("Padding byte != 0: " + by);
            }
            ++n2;
        }
        this.index = byteSequence.readInt();
    }

    public String toString(boolean bl) {
        StringBuffer stringBuffer = new StringBuffer(super.toString(bl));
        if (bl) {
            int n2 = 0;
            while (n2 < this.match_length) {
                String string = "null";
                if (this.targets[n2] != null) {
                    string = this.targets[n2].getInstruction().toString();
                }
                stringBuffer.append("(" + this.match[n2] + ", " + string + " = {" + this.indices[n2] + "})");
                ++n2;
            }
        } else {
            stringBuffer.append(" ...");
        }
        return stringBuffer.toString();
    }

    public void setTarget(int n2, InstructionHandle instructionHandle) {
        BranchInstruction.notifyTarget(this.targets[n2], instructionHandle, this);
        this.targets[n2] = instructionHandle;
    }

    public void updateTarget(InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        boolean bl = false;
        if (this.target == instructionHandle) {
            bl = true;
            this.setTarget(instructionHandle2);
        }
        int n2 = 0;
        while (n2 < this.targets.length) {
            if (this.targets[n2] == instructionHandle) {
                bl = true;
                this.setTarget(n2, instructionHandle2);
            }
            ++n2;
        }
        if (!bl) {
            throw new ClassGenException("Not targeting " + instructionHandle);
        }
    }

    void dispose() {
        super.dispose();
        int n2 = 0;
        while (n2 < this.targets.length) {
            this.targets[n2].removeTargeter(this);
            ++n2;
        }
    }

    public int[] getIndices() {
        return this.indices;
    }

    public InstructionHandle[] getTargets() {
        return this.targets;
    }
}

