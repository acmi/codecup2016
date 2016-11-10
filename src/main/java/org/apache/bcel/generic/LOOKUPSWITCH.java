/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.Select;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.VariableLengthInstruction;
import org.apache.bcel.generic.Visitor;
import org.apache.bcel.util.ByteSequence;

public class LOOKUPSWITCH
extends Select {
    LOOKUPSWITCH() {
    }

    public LOOKUPSWITCH(int[] arrn, InstructionHandle[] arrinstructionHandle, InstructionHandle instructionHandle) {
        super(171, arrn, arrinstructionHandle, instructionHandle);
        this.length = (short)(9 + this.match_length * 8);
        this.fixed_length = this.length;
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        dataOutputStream.writeInt(this.match_length);
        int n2 = 0;
        while (n2 < this.match_length) {
            dataOutputStream.writeInt(this.match[n2]);
            this.indices[n2] = this.getTargetOffset(this.targets[n2]);
            dataOutputStream.writeInt(this.indices[n2]);
            ++n2;
        }
    }

    protected void initFromFile(ByteSequence byteSequence, boolean bl) throws IOException {
        super.initFromFile(byteSequence, bl);
        this.match_length = byteSequence.readInt();
        this.fixed_length = (short)(9 + this.match_length * 8);
        this.length = (short)(this.fixed_length + this.padding);
        this.match = new int[this.match_length];
        this.indices = new int[this.match_length];
        this.targets = new InstructionHandle[this.match_length];
        int n2 = 0;
        while (n2 < this.match_length) {
            this.match[n2] = byteSequence.readInt();
            this.indices[n2] = byteSequence.readInt();
            ++n2;
        }
    }

    public void accept(Visitor visitor) {
        visitor.visitVariableLengthInstruction(this);
        visitor.visitStackProducer(this);
        visitor.visitBranchInstruction(this);
        visitor.visitSelect(this);
        visitor.visitLOOKUPSWITCH(this);
    }
}

