/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.classfile.Constant;

public final class ConstantDouble
extends Constant {
    private double bytes;

    public ConstantDouble(double d2) {
        super(6);
        this.bytes = d2;
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeDouble(this.bytes);
    }

    public final double getBytes() {
        return this.bytes;
    }

    public final String toString() {
        return super.toString() + "(bytes = " + this.bytes + ")";
    }
}

