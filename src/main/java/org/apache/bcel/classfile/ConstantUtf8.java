/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.Utility;

public final class ConstantUtf8
extends Constant {
    private String bytes;

    public ConstantUtf8(String string) {
        super(1);
        this.bytes = string;
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeUTF(this.bytes);
    }

    public final String getBytes() {
        return this.bytes;
    }

    public final String toString() {
        return super.toString() + "(\"" + Utility.replace(this.bytes, "\n", "\\n") + "\")";
    }
}

