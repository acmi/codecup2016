/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;

public abstract class FieldOrMethod
extends AccessFlags
implements Cloneable {
    protected int name_index;
    protected int signature_index;
    protected int attributes_count;
    protected Attribute[] attributes;
    protected ConstantPool constant_pool;

    FieldOrMethod() {
    }

    protected FieldOrMethod(int n2, int n3, int n4, Attribute[] arrattribute, ConstantPool constantPool) {
        this.access_flags = n2;
        this.name_index = n3;
        this.signature_index = n4;
        this.constant_pool = constantPool;
        this.setAttributes(arrattribute);
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(this.access_flags);
        dataOutputStream.writeShort(this.name_index);
        dataOutputStream.writeShort(this.signature_index);
        dataOutputStream.writeShort(this.attributes_count);
        int n2 = 0;
        while (n2 < this.attributes_count) {
            this.attributes[n2].dump(dataOutputStream);
            ++n2;
        }
    }

    public final Attribute[] getAttributes() {
        return this.attributes;
    }

    public final void setAttributes(Attribute[] arrattribute) {
        this.attributes = arrattribute;
        this.attributes_count = arrattribute == null ? 0 : arrattribute.length;
    }

    public final ConstantPool getConstantPool() {
        return this.constant_pool;
    }

    public final String getName() {
        ConstantUtf8 constantUtf8 = (ConstantUtf8)this.constant_pool.getConstant(this.name_index, 1);
        return constantUtf8.getBytes();
    }

    public final String getSignature() {
        ConstantUtf8 constantUtf8 = (ConstantUtf8)this.constant_pool.getConstant(this.signature_index, 1);
        return constantUtf8.getBytes();
    }
}

