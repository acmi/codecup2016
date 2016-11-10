/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.classfile.Constant;

public final class ConstantNameAndType
extends Constant {
    private int name_index;
    private int signature_index;

    public ConstantNameAndType(int n2, int n3) {
        super(12);
        this.name_index = n2;
        this.signature_index = n3;
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeShort(this.name_index);
        dataOutputStream.writeShort(this.signature_index);
    }

    public final int getNameIndex() {
        return this.name_index;
    }

    public final int getSignatureIndex() {
        return this.signature_index;
    }

    public final String toString() {
        return super.toString() + "(name_index = " + this.name_index + ", signature_index = " + this.signature_index + ")";
    }
}

