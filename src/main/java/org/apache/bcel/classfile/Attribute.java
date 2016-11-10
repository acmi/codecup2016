/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ConstantPool;

public abstract class Attribute
implements Cloneable {
    protected int name_index;
    protected int length;
    protected byte tag;
    protected ConstantPool constant_pool;
    private static HashMap readers = new HashMap();

    Attribute(byte by, int n2, int n3, ConstantPool constantPool) {
        this.tag = by;
        this.name_index = n2;
        this.length = n3;
        this.constant_pool = constantPool;
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(this.name_index);
        dataOutputStream.writeInt(this.length);
    }

    public final int getLength() {
        return this.length;
    }

    public final byte getTag() {
        return this.tag;
    }

    public Object clone() {
        Object object = null;
        try {
            object = super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            cloneNotSupportedException.printStackTrace();
        }
        return object;
    }

    public String toString() {
        return Constants.ATTRIBUTE_NAMES[this.tag];
    }
}

