/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Utility;

public final class CodeException
implements Cloneable,
Constants {
    private int start_pc;
    private int end_pc;
    private int handler_pc;
    private int catch_type;

    public CodeException(int n2, int n3, int n4, int n5) {
        this.start_pc = n2;
        this.end_pc = n3;
        this.handler_pc = n4;
        this.catch_type = n5;
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(this.start_pc);
        dataOutputStream.writeShort(this.end_pc);
        dataOutputStream.writeShort(this.handler_pc);
        dataOutputStream.writeShort(this.catch_type);
    }

    public final int getCatchType() {
        return this.catch_type;
    }

    public final int getEndPC() {
        return this.end_pc;
    }

    public final int getHandlerPC() {
        return this.handler_pc;
    }

    public final int getStartPC() {
        return this.start_pc;
    }

    public final String toString() {
        return "CodeException(start_pc = " + this.start_pc + ", end_pc = " + this.end_pc + ", handler_pc = " + this.handler_pc + ", catch_type = " + this.catch_type + ")";
    }

    public final String toString(ConstantPool constantPool, boolean bl) {
        String string = this.catch_type == 0 ? "<Any exception>(0)" : Utility.compactClassName(constantPool.getConstantString(this.catch_type, 7), false) + (bl ? new StringBuffer().append("(").append(this.catch_type).append(")").toString() : "");
        return "" + this.start_pc + "\t" + this.end_pc + "\t" + this.handler_pc + "\t" + string;
    }
}

