/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.classfile.Constant;

public final class ConstantClass
extends Constant {
    private int name_index;

    public ConstantClass(int n2) {
        super(7);
        this.name_index = n2;
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeShort(this.name_index);
    }

    public final int getNameIndex() {
        return this.name_index;
    }

    public final String toString() {
        return super.toString() + "(name_index = " + this.name_index + ")";
    }
}

