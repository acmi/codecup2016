/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.classfile.Constant;

public final class ConstantLong
extends Constant {
    private long bytes;

    public ConstantLong(long l2) {
        super(5);
        this.bytes = l2;
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeLong(this.bytes);
    }

    public final long getBytes() {
        return this.bytes;
    }

    public final String toString() {
        return super.toString() + "(bytes = " + this.bytes + ")";
    }
}

