/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;

public final class BranchHandle
extends InstructionHandle {
    private BranchInstruction bi;
    private static BranchHandle bh_list = null;

    private BranchHandle(BranchInstruction branchInstruction) {
        super(branchInstruction);
        this.bi = branchInstruction;
    }

    static final BranchHandle getBranchHandle(BranchInstruction branchInstruction) {
        if (bh_list == null) {
            return new BranchHandle(branchInstruction);
        }
        BranchHandle branchHandle = bh_list;
        bh_list = (BranchHandle)branchHandle.next;
        branchHandle.setInstruction(branchInstruction);
        return branchHandle;
    }

    protected void addHandle() {
        this.next = bh_list;
        bh_list = this;
    }

    public int getPosition() {
        return this.bi.position;
    }

    void setPosition(int n2) {
        this.i_position = this.bi.position = n2;
    }

    protected int updatePosition(int n2, int n3) {
        int n4 = this.bi.updatePosition(n2, n3);
        this.i_position = this.bi.position;
        return n4;
    }

    public void setTarget(InstructionHandle instructionHandle) {
        this.bi.setTarget(instructionHandle);
    }

    public void updateTarget(InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        this.bi.updateTarget(instructionHandle, instructionHandle2);
    }

    public void setInstruction(Instruction instruction) {
        super.setInstruction(instruction);
        if (!(instruction instanceof BranchInstruction)) {
            throw new ClassGenException("Assigning " + instruction + " to branch handle which is not a branch instruction");
        }
        this.bi = (BranchInstruction)instruction;
    }
}

