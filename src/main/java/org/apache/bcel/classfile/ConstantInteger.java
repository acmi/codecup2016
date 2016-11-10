/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.classfile.Constant;

public final class ConstantInteger
extends Constant {
    private int bytes;

    public ConstantInteger(int n2) {
        super(3);
        this.bytes = n2;
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeInt(this.bytes);
    }

    public final int getBytes() {
        return this.bytes;
    }

    public final String toString() {
        return super.toString() + "(bytes = " + this.bytes + ")";
    }
}

