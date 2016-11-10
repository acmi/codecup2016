/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.classfile.Constant;

public final class ConstantString
extends Constant {
    private int string_index;

    public ConstantString(int n2) {
        super(8);
        this.string_index = n2;
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeShort(this.string_index);
    }

    public final int getStringIndex() {
        return this.string_index;
    }

    public final String toString() {
        return super.toString() + "(string_index = " + this.string_index + ")";
    }
}

