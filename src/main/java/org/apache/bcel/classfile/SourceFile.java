/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;

public final class SourceFile
extends Attribute {
    private int sourcefile_index;

    public SourceFile(int n2, int n3, int n4, ConstantPool constantPool) {
        super(0, n2, n3, constantPool);
        this.sourcefile_index = n4;
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        dataOutputStream.writeShort(this.sourcefile_index);
    }

    public final String getSourceFileName() {
        ConstantUtf8 constantUtf8 = (ConstantUtf8)this.constant_pool.getConstant(this.sourcefile_index, 1);
        return constantUtf8.getBytes();
    }

    public final String toString() {
        return "SourceFile(" + this.getSourceFileName() + ")";
    }
}

