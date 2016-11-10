/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;

public final class LineNumber
implements Cloneable {
    private int start_pc;
    private int line_number;

    public LineNumber(int n2, int n3) {
        this.start_pc = n2;
        this.line_number = n3;
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(this.start_pc);
        dataOutputStream.writeShort(this.line_number);
    }

    public final int getLineNumber() {
        return this.line_number;
    }

    public final int getStartPC() {
        return this.start_pc;
    }

    public final String toString() {
        return "LineNumber(" + this.start_pc + ", " + this.line_number + ")";
    }
}

