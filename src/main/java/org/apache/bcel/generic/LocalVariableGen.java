/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.io.PrintStream;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionTargeter;
import org.apache.bcel.generic.Type;

public class LocalVariableGen
implements Cloneable,
InstructionTargeter {
    private int index;
    private String name;
    private Type type;
    private InstructionHandle start;
    private InstructionHandle end;

    public LocalVariableGen(int n2, String string, Type type, InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        if (n2 < 0 || n2 > 65535) {
            throw new ClassGenException("Invalid index index: " + n2);
        }
        this.name = string;
        this.type = type;
        this.index = n2;
        this.setStart(instructionHandle);
        this.setEnd(instructionHandle2);
    }

    public LocalVariable getLocalVariable(ConstantPoolGen constantPoolGen) {
        int n2 = this.start.getPosition();
        int n3 = this.end.getPosition() - n2;
        if (n3 > 0) {
            n3 += this.end.getInstruction().getLength();
        }
        int n4 = constantPoolGen.addUtf8(this.name);
        int n5 = constantPoolGen.addUtf8(this.type.getSignature());
        return new LocalVariable(n2, n3, n4, n5, this.index, constantPoolGen.getConstantPool());
    }

    public int getIndex() {
        return this.index;
    }

    public String getName() {
        return this.name;
    }

    public Type getType() {
        return this.type;
    }

    public InstructionHandle getStart() {
        return this.start;
    }

    public InstructionHandle getEnd() {
        return this.end;
    }

    public void setStart(InstructionHandle instructionHandle) {
        BranchInstruction.notifyTarget(this.start, instructionHandle, this);
        this.start = instructionHandle;
    }

    public void setEnd(InstructionHandle instructionHandle) {
        BranchInstruction.notifyTarget(this.end, instructionHandle, this);
        this.end = instructionHandle;
    }

    public void updateTarget(InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        boolean bl = false;
        if (this.start == instructionHandle) {
            bl = true;
            this.setStart(instructionHandle2);
        }
        if (this.end == instructionHandle) {
            bl = true;
            this.setEnd(instructionHandle2);
        }
        if (!bl) {
            throw new ClassGenException("Not targeting " + instructionHandle + ", but {" + this.start + ", " + this.end + "}");
        }
    }

    public boolean equals(Object object) {
        if (!(object instanceof LocalVariableGen)) {
            return false;
        }
        LocalVariableGen localVariableGen = (LocalVariableGen)object;
        return localVariableGen.index == this.index && localVariableGen.start == this.start && localVariableGen.end == this.end;
    }

    public String toString() {
        return "LocalVariableGen(" + this.name + ", " + this.type + ", " + this.start + ", " + this.end + ")";
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

