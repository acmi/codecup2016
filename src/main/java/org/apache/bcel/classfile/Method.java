/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.ExceptionTable;
import org.apache.bcel.classfile.FieldOrMethod;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Utility;

public final class Method
extends FieldOrMethod {
    public Method() {
    }

    public Method(int n2, int n3, int n4, Attribute[] arrattribute, ConstantPool constantPool) {
        super(n2, n3, n4, arrattribute, constantPool);
    }

    public final Code getCode() {
        int n2 = 0;
        while (n2 < this.attributes_count) {
            if (this.attributes[n2] instanceof Code) {
                return (Code)this.attributes[n2];
            }
            ++n2;
        }
        return null;
    }

    public final ExceptionTable getExceptionTable() {
        int n2 = 0;
        while (n2 < this.attributes_count) {
            if (this.attributes[n2] instanceof ExceptionTable) {
                return (ExceptionTable)this.attributes[n2];
            }
            ++n2;
        }
        return null;
    }

    public final LocalVariableTable getLocalVariableTable() {
        Code code = this.getCode();
        if (code != null) {
            return code.getLocalVariableTable();
        }
        return null;
    }

    public final String toString() {
        String string;
        String string2 = Utility.accessToString(this.access_flags);
        ConstantUtf8 constantUtf8 = (ConstantUtf8)this.constant_pool.getConstant(this.signature_index, 1);
        String string3 = constantUtf8.getBytes();
        constantUtf8 = (ConstantUtf8)this.constant_pool.getConstant(this.name_index, 1);
        String string4 = constantUtf8.getBytes();
        string3 = Utility.methodSignatureToString(string3, string4, string2, true, this.getLocalVariableTable());
        StringBuffer stringBuffer = new StringBuffer(string3);
        int n2 = 0;
        while (n2 < this.attributes_count) {
            Attribute exceptionTable = this.attributes[n2];
            if (!(exceptionTable instanceof Code) && !(exceptionTable instanceof ExceptionTable)) {
                stringBuffer.append(" [" + exceptionTable.toString() + "]");
            }
            ++n2;
        }
        ExceptionTable exceptionTable = this.getExceptionTable();
        if (exceptionTable != null && !(string = exceptionTable.toString()).equals("")) {
            stringBuffer.append("\n\t\tthrows " + string);
        }
        return stringBuffer.toString();
    }
}

