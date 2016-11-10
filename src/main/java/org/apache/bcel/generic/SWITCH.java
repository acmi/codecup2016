/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LOOKUPSWITCH;
import org.apache.bcel.generic.Select;
import org.apache.bcel.generic.TABLESWITCH;

public final class SWITCH
implements CompoundInstruction {
    private int[] match;
    private InstructionHandle[] targets;
    private Select instruction;
    private int match_length;

    public SWITCH(int[] arrn, InstructionHandle[] arrinstructionHandle, InstructionHandle instructionHandle, int n2) {
        this.match = (int[])arrn.clone();
        this.targets = (InstructionHandle[])arrinstructionHandle.clone();
        this.match_length = arrn.length;
        if (this.match_length < 2) {
            this.instruction = new TABLESWITCH(arrn, arrinstructionHandle, instructionHandle);
        } else {
            this.sort(0, this.match_length - 1);
            if (this.matchIsOrdered(n2)) {
                this.fillup(n2, instructionHandle);
                this.instruction = new TABLESWITCH(this.match, this.targets, instructionHandle);
            } else {
                this.instruction = new LOOKUPSWITCH(this.match, this.targets, instructionHandle);
            }
        }
    }

    public SWITCH(int[] arrn, InstructionHandle[] arrinstructionHandle, InstructionHandle instructionHandle) {
        this(arrn, arrinstructionHandle, instructionHandle, 1);
    }

    private final void fillup(int n2, InstructionHandle instructionHandle) {
        int n3 = this.match_length + this.match_length * n2;
        int[] arrn = new int[n3];
        InstructionHandle[] arrinstructionHandle = new InstructionHandle[n3];
        int n4 = 1;
        arrn[0] = this.match[0];
        arrinstructionHandle[0] = this.targets[0];
        int n5 = 1;
        while (n5 < this.match_length) {
            int n6 = this.match[n5 - 1];
            int n7 = this.match[n5] - n6;
            int n8 = 1;
            while (n8 < n7) {
                arrn[n4] = n6 + n8;
                arrinstructionHandle[n4] = instructionHandle;
                ++n4;
                ++n8;
            }
            arrn[n4] = this.match[n5];
            arrinstructionHandle[n4] = this.targets[n5];
            ++n4;
            ++n5;
        }
        this.match = new int[n4];
        this.targets = new InstructionHandle[n4];
        System.arraycopy(arrn, 0, this.match, 0, n4);
        System.arraycopy(arrinstructionHandle, 0, this.targets, 0, n4);
    }

    private final void sort(int n2, int n3) {
        int n4 = n2;
        int n5 = n3;
        int n6 = this.match[(n2 + n3) / 2];
        do {
            if (this.match[n4] < n6) {
                ++n4;
                continue;
            }
            while (n6 < this.match[n5]) {
                --n5;
            }
            if (n4 <= n5) {
                int n7 = this.match[n4];
                this.match[n4] = this.match[n5];
                this.match[n5] = n7;
                InstructionHandle instructionHandle = this.targets[n4];
                this.targets[n4] = this.targets[n5];
                this.targets[n5] = instructionHandle;
                ++n4;
                --n5;
            }
            if (n4 > n5) break;
        } while (true);
        if (n2 < n5) {
            this.sort(n2, n5);
        }
        if (n4 < n3) {
            this.sort(n4, n3);
        }
    }

    private final boolean matchIsOrdered(int n2) {
        int n3 = 1;
        while (n3 < this.match_length) {
            if (this.match[n3] - this.match[n3 - 1] > n2) {
                return false;
            }
            ++n3;
        }
        return true;
    }

    public final InstructionList getInstructionList() {
        return new InstructionList(this.instruction);
    }
}

