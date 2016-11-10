/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantValue;
import org.apache.bcel.classfile.FieldOrMethod;
import org.apache.bcel.classfile.Utility;

public final class Field
extends FieldOrMethod {
    public Field(int n2, int n3, int n4, Attribute[] arrattribute, ConstantPool constantPool) {
        super(n2, n3, n4, arrattribute, constantPool);
    }

    public final ConstantValue getConstantValue() {
        int n2 = 0;
        while (n2 < this.attributes_count) {
            if (this.attributes[n2].getTag() == 1) {
                return (ConstantValue)this.attributes[n2];
            }
            ++n2;
        }
        return null;
    }

    public final String toString() {
        String string = Utility.accessToString(this.access_flags);
        string = string.equals("") ? "" : string + " ";
        String string2 = Utility.signatureToString(this.getSignature());
        String string3 = this.getName();
        StringBuffer stringBuffer = new StringBuffer(string + string2 + " " + string3);
        ConstantValue constantValue = this.getConstantValue();
        if (constantValue != null) {
            stringBuffer.append(" = " + constantValue);
        }
        int n2 = 0;
        while (n2 < this.attributes_count) {
            Attribute attribute = this.attributes[n2];
            if (!(attribute instanceof ConstantValue)) {
                stringBuffer.append(" [" + attribute.toString() + "]");
            }
            ++n2;
        }
        return stringBuffer.toString();
    }
}

