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

public class TABLESWITCH
extends Select {
    TABLESWITCH() {
    }

    public TABLESWITCH(int[] arrn, InstructionHandle[] arrinstructionHandle, InstructionHandle instructionHandle) {
        super(170, arrn, arrinstructionHandle, instructionHandle);
        this.length = (short)(13 + this.match_length * 4);
        this.fixed_length = this.length;
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        int n2 = this.match_length > 0 ? this.match[0] : 0;
        dataOutputStream.writeInt(n2);
        int n3 = this.match_length > 0 ? this.match[this.match_length - 1] : 0;
        dataOutputStream.writeInt(n3);
        int n4 = 0;
        while (n4 < this.match_length) {
            this.indices[n4] = this.getTargetOffset(this.targets[n4]);
            dataOutputStream.writeInt(this.indices[n4]);
            ++n4;
        }
    }

    protected void initFromFile(ByteSequence byteSequence, boolean bl) throws IOException {
        super.initFromFile(byteSequence, bl);
        int n2 = byteSequence.readInt();
        int n3 = byteSequence.readInt();
        this.match_length = n3 - n2 + 1;
        this.fixed_length = (short)(13 + this.match_length * 4);
        this.length = (short)(this.fixed_length + this.padding);
        this.match = new int[this.match_length];
        this.indices = new int[this.match_length];
        this.targets = new InstructionHandle[this.match_length];
        int n4 = n2;
        while (n4 <= n3) {
            this.match[n4 - n2] = n4++;
        }
        int n5 = 0;
        while (n5 < this.match_length) {
            this.indices[n5] = byteSequence.readInt();
            ++n5;
        }
    }

    public void accept(Visitor visitor) {
        visitor.visitVariableLengthInstruction(this);
        visitor.visitStackProducer(this);
        visitor.visitBranchInstruction(this);
        visitor.visitSelect(this);
        visitor.visitTABLESWITCH(this);
    }
}

