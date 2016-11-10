/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionTargeter;

public class InstructionHandle
implements Serializable {
    InstructionHandle next;
    InstructionHandle prev;
    Instruction instruction;
    protected int i_position = -1;
    private HashSet targeters;
    private HashMap attributes;
    private static InstructionHandle ih_list = null;
    static Class class$org$apache$bcel$generic$BranchHandle;

    public final InstructionHandle getNext() {
        return this.next;
    }

    public final InstructionHandle getPrev() {
        return this.prev;
    }

    public final Instruction getInstruction() {
        return this.instruction;
    }

    public void setInstruction(Instruction instruction) {
        if (instruction == null) {
            throw new ClassGenException("Assigning null to handle");
        }
        Class class_ = class$org$apache$bcel$generic$BranchHandle == null ? (InstructionHandle.class$org$apache$bcel$generic$BranchHandle = InstructionHandle.class$("org.apache.bcel.generic.BranchHandle")) : class$org$apache$bcel$generic$BranchHandle;
        if (this.getClass() != class_ && instruction instanceof BranchInstruction) {
            throw new ClassGenException("Assigning branch instruction " + instruction + " to plain handle");
        }
        if (this.instruction != null) {
            this.instruction.dispose();
        }
        this.instruction = instruction;
    }

    protected InstructionHandle(Instruction instruction) {
        this.setInstruction(instruction);
    }

    static final InstructionHandle getInstructionHandle(Instruction instruction) {
        if (ih_list == null) {
            return new InstructionHandle(instruction);
        }
        InstructionHandle instructionHandle = ih_list;
        ih_list = instructionHandle.next;
        instructionHandle.setInstruction(instruction);
        return instructionHandle;
    }

    protected int updatePosition(int n2, int n3) {
        this.i_position += n2;
        return 0;
    }

    public int getPosition() {
        return this.i_position;
    }

    void setPosition(int n2) {
        this.i_position = n2;
    }

    protected void addHandle() {
        this.next = ih_list;
        ih_list = this;
    }

    void dispose() {
        this.prev = null;
        this.next = null;
        this.instruction.dispose();
        this.instruction = null;
        this.i_position = -1;
        this.attributes = null;
        this.removeAllTargeters();
        this.addHandle();
    }

    public void removeAllTargeters() {
        if (this.targeters != null) {
            this.targeters.clear();
        }
    }

    public void removeTargeter(InstructionTargeter instructionTargeter) {
        this.targeters.remove(instructionTargeter);
    }

    public void addTargeter(InstructionTargeter instructionTargeter) {
        if (this.targeters == null) {
            this.targeters = new HashSet();
        }
        this.targeters.add(instructionTargeter);
    }

    public boolean hasTargeters() {
        return this.targeters != null && this.targeters.size() > 0;
    }

    public InstructionTargeter[] getTargeters() {
        if (!this.hasTargeters()) {
            return null;
        }
        InstructionTargeter[] arrinstructionTargeter = new InstructionTargeter[this.targeters.size()];
        this.targeters.toArray(arrinstructionTargeter);
        return arrinstructionTargeter;
    }

    public String toString(boolean bl) {
        return Utility.format(this.i_position, 4, false, ' ') + ": " + this.instruction.toString(bl);
    }

    public String toString() {
        return this.toString(true);
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }
}

