/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.classfile.Constant;

public abstract class ConstantCP
extends Constant {
    protected int class_index;
    protected int name_and_type_index;

    protected ConstantCP(byte by, int n2, int n3) {
        super(by);
        this.class_index = n2;
        this.name_and_type_index = n3;
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeShort(this.class_index);
        dataOutputStream.writeShort(this.name_and_type_index);
    }

    public final int getClassIndex() {
        return this.class_index;
    }

    public final int getNameAndTypeIndex() {
        return this.name_and_type_index;
    }

    public final String toString() {
        return super.toString() + "(class_index = " + this.class_index + ", name_and_type_index = " + this.name_and_type_index + ")";
    }
}

