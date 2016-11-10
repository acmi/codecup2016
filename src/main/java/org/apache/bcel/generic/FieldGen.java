/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantValue;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldGenOrMethodGen;
import org.apache.bcel.generic.Type;

public class FieldGen
extends FieldGenOrMethodGen {
    private Object value = null;

    public FieldGen(int n2, Type type, String string, ConstantPoolGen constantPoolGen) {
        this.setAccessFlags(n2);
        this.setType(type);
        this.setName(string);
        this.setConstantPool(constantPoolGen);
    }

    private void checkType(Type type) {
        if (this.type == null) {
            throw new ClassGenException("You haven't defined the type of the field yet");
        }
        if (!this.isFinal()) {
            throw new ClassGenException("Only final fields may have an initial value!");
        }
        if (!this.type.equals(type)) {
            throw new ClassGenException("Types are not compatible: " + this.type + " vs. " + type);
        }
    }

    public Field getField() {
        String string = this.getSignature();
        int n2 = this.cp.addUtf8(this.name);
        int n3 = this.cp.addUtf8(string);
        if (this.value != null) {
            this.checkType(this.type);
            int n4 = this.addConstant();
            this.addAttribute(new ConstantValue(this.cp.addUtf8("ConstantValue"), 2, n4, this.cp.getConstantPool()));
        }
        return new Field(this.access_flags, n2, n3, this.getAttributes(), this.cp.getConstantPool());
    }

    private int addConstant() {
        switch (this.type.getType()) {
            case 4: 
            case 5: 
            case 8: 
            case 9: 
            case 10: {
                return this.cp.addInteger((Integer)this.value);
            }
            case 6: {
                return this.cp.addFloat(((Float)this.value).floatValue());
            }
            case 7: {
                return this.cp.addDouble((Double)this.value);
            }
            case 11: {
                return this.cp.addLong((Long)this.value);
            }
            case 14: {
                return this.cp.addString((String)this.value);
            }
        }
        throw new RuntimeException("Oops: Unhandled : " + this.type.getType());
    }

    public String getSignature() {
        return this.type.getSignature();
    }

    public String getInitValue() {
        if (this.value != null) {
            return this.value.toString();
        }
        return null;
    }

    public final String toString() {
        String string = Utility.accessToString(this.access_flags);
        string = string.equals("") ? "" : string + " ";
        String string2 = this.type.toString();
        String string3 = this.getName();
        StringBuffer stringBuffer = new StringBuffer(string + string2 + " " + string3);
        String string4 = this.getInitValue();
        if (string4 != null) {
            stringBuffer.append(" = " + string4);
        }
        return stringBuffer.toString();
    }
}

