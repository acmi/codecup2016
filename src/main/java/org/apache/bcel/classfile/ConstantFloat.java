/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.classfile.Constant;

public final class ConstantFloat
extends Constant {
    private float bytes;

    public ConstantFloat(float f2) {
        super(4);
        this.bytes = f2;
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeFloat(this.bytes);
    }

    public final float getBytes() {
        return this.bytes;
    }

    public final String toString() {
        return super.toString() + "(bytes = " + this.bytes + ")";
    }
}

