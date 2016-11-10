/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.Utility;

public final class LocalVariable
implements Cloneable,
Constants {
    private int start_pc;
    private int length;
    private int name_index;
    private int signature_index;
    private int index;
    private ConstantPool constant_pool;

    public LocalVariable(int n2, int n3, int n4, int n5, int n6, ConstantPool constantPool) {
        this.start_pc = n2;
        this.length = n3;
        this.name_index = n4;
        this.signature_index = n5;
        this.index = n6;
        this.constant_pool = constantPool;
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(this.start_pc);
        dataOutputStream.writeShort(this.length);
        dataOutputStream.writeShort(this.name_index);
        dataOutputStream.writeShort(this.signature_index);
        dataOutputStream.writeShort(this.index);
    }

    public final int getLength() {
        return this.length;
    }

    public final String getName() {
        ConstantUtf8 constantUtf8 = (ConstantUtf8)this.constant_pool.getConstant(this.name_index, 1);
        return constantUtf8.getBytes();
    }

    public final String getSignature() {
        ConstantUtf8 constantUtf8 = (ConstantUtf8)this.constant_pool.getConstant(this.signature_index, 1);
        return constantUtf8.getBytes();
    }

    public final int getIndex() {
        return this.index;
    }

    public final int getStartPC() {
        return this.start_pc;
    }

    public final String toString() {
        String string = this.getName();
        String string2 = Utility.signatureToString(this.getSignature());
        return "LocalVariable(start_pc = " + this.start_pc + ", length = " + this.length + ", index = " + this.index + ":" + string2 + " " + string + ")";
    }
}

