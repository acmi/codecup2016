/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.io.PrintStream;
import org.apache.bcel.classfile.CodeException;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionTargeter;
import org.apache.bcel.generic.ObjectType;

public final class CodeExceptionGen
implements Cloneable,
InstructionTargeter {
    private InstructionHandle start_pc;
    private InstructionHandle end_pc;
    private InstructionHandle handler_pc;
    private ObjectType catch_type;

    public CodeExceptionGen(InstructionHandle instructionHandle, InstructionHandle instructionHandle2, InstructionHandle instructionHandle3, ObjectType objectType) {
        this.setStartPC(instructionHandle);
        this.setEndPC(instructionHandle2);
        this.setHandlerPC(instructionHandle3);
        this.catch_type = objectType;
    }

    public CodeException getCodeException(ConstantPoolGen constantPoolGen) {
        return new CodeException(this.start_pc.getPosition(), this.end_pc.getPosition() + this.end_pc.getInstruction().getLength(), this.handler_pc.getPosition(), this.catch_type == null ? 0 : constantPoolGen.addClass(this.catch_type));
    }

    public void setStartPC(InstructionHandle instructionHandle) {
        BranchInstruction.notifyTarget(this.start_pc, instructionHandle, this);
        this.start_pc = instructionHandle;
    }

    public void setEndPC(InstructionHandle instructionHandle) {
        BranchInstruction.notifyTarget(this.end_pc, instructionHandle, this);
        this.end_pc = instructionHandle;
    }

    public void setHandlerPC(InstructionHandle instructionHandle) {
        BranchInstruction.notifyTarget(this.handler_pc, instructionHandle, this);
        this.handler_pc = instructionHandle;
    }

    public void updateTarget(InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        boolean bl = false;
        if (this.start_pc == instructionHandle) {
            bl = true;
            this.setStartPC(instructionHandle2);
        }
        if (this.end_pc == instructionHandle) {
            bl = true;
            this.setEndPC(instructionHandle2);
        }
        if (this.handler_pc == instructionHandle) {
            bl = true;
            this.setHandlerPC(instructionHandle2);
        }
        if (!bl) {
            throw new ClassGenException("Not targeting " + instructionHandle + ", but {" + this.start_pc + ", " + this.end_pc + ", " + this.handler_pc + "}");
        }
    }

    public InstructionHandle getHandlerPC() {
        return this.handler_pc;
    }

    public String toString() {
        return "CodeExceptionGen(" + this.start_pc + ", " + this.end_pc + ", " + this.handler_pc + ")";
    }

    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            System.err.println(cloneNotSupportedException);
            return null;
        }
    }
}

