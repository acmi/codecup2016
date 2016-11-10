/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.Constants;

public abstract class Constant
implements Cloneable {
    protected byte tag;

    Constant(byte by) {
        this.tag = by;
    }

    public abstract void dump(DataOutputStream var1) throws IOException;

    public final byte getTag() {
        return this.tag;
    }

    public String toString() {
        return Constants.CONSTANT_NAMES[this.tag] + "[" + this.tag + "]";
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

