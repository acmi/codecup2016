/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.io.PrintStream;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionTargeter;

public class LineNumberGen
implements Cloneable,
InstructionTargeter {
    private InstructionHandle ih;
    private int src_line;

    public LineNumberGen(InstructionHandle instructionHandle, int n2) {
        this.setInstruction(instructionHandle);
        this.setSourceLine(n2);
    }

    public void updateTarget(InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        if (instructionHandle != this.ih) {
            throw new ClassGenException("Not targeting " + instructionHandle + ", but " + this.ih + "}");
        }
        this.setInstruction(instructionHandle2);
    }

    public LineNumber getLineNumber() {
        return new LineNumber(this.ih.getPosition(), this.src_line);
    }

    public void setInstruction(InstructionHandle instructionHandle) {
        BranchInstruction.notifyTarget(this.ih, instructionHandle, this);
        this.ih = instructionHandle;
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

    public void setSourceLine(int n2) {
        this.src_line = n2;
    }
}

