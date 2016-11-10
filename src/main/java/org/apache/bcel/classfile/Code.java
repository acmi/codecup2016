/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.CodeException;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Utility;

public final class Code
extends Attribute {
    private int max_stack;
    private int max_locals;
    private int code_length;
    private byte[] code;
    private int exception_table_length;
    private CodeException[] exception_table;
    private int attributes_count;
    private Attribute[] attributes;

    public Code(int n2, int n3, int n4, int n5, byte[] arrby, CodeException[] arrcodeException, Attribute[] arrattribute, ConstantPool constantPool) {
        super(2, n2, n3, constantPool);
        this.max_stack = n4;
        this.max_locals = n5;
        this.setCode(arrby);
        this.setExceptionTable(arrcodeException);
        this.setAttributes(arrattribute);
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        dataOutputStream.writeShort(this.max_stack);
        dataOutputStream.writeShort(this.max_locals);
        dataOutputStream.writeInt(this.code_length);
        dataOutputStream.write(this.code, 0, this.code_length);
        dataOutputStream.writeShort(this.exception_table_length);
        int n2 = 0;
        while (n2 < this.exception_table_length) {
            this.exception_table[n2].dump(dataOutputStream);
            ++n2;
        }
        dataOutputStream.writeShort(this.attributes_count);
        int n3 = 0;
        while (n3 < this.attributes_count) {
            this.attributes[n3].dump(dataOutputStream);
            ++n3;
        }
    }

    public final Attribute[] getAttributes() {
        return this.attributes;
    }

    public LocalVariableTable getLocalVariableTable() {
        int n2 = 0;
        while (n2 < this.attributes_count) {
            if (this.attributes[n2] instanceof LocalVariableTable) {
                return (LocalVariableTable)this.attributes[n2];
            }
            ++n2;
        }
        return null;
    }

    public final byte[] getCode() {
        return this.code;
    }

    public final CodeException[] getExceptionTable() {
        return this.exception_table;
    }

    public final int getMaxLocals() {
        return this.max_locals;
    }

    public final int getMaxStack() {
        return this.max_stack;
    }

    private final int getInternalLength() {
        return 8 + this.code_length + 2 + 8 * this.exception_table_length + 2;
    }

    private final int calculateLength() {
        int n2 = 0;
        int n3 = 0;
        while (n3 < this.attributes_count) {
            n2 += this.attributes[n3].length + 6;
            ++n3;
        }
        return n2 + this.getInternalLength();
    }

    public final void setAttributes(Attribute[] arrattribute) {
        this.attributes = arrattribute;
        this.attributes_count = arrattribute == null ? 0 : arrattribute.length;
        this.length = this.calculateLength();
    }

    public final void setCode(byte[] arrby) {
        this.code = arrby;
        this.code_length = arrby == null ? 0 : arrby.length;
    }

    public final void setExceptionTable(CodeException[] arrcodeException) {
        this.exception_table = arrcodeException;
        this.exception_table_length = arrcodeException == null ? 0 : arrcodeException.length;
    }

    public final String toString(boolean bl) {
        int n2;
        StringBuffer stringBuffer = new StringBuffer("Code(max_stack = " + this.max_stack + ", max_locals = " + this.max_locals + ", code_length = " + this.code_length + ")\n" + Utility.codeToString(this.code, this.constant_pool, 0, -1, bl));
        if (this.exception_table_length > 0) {
            stringBuffer.append("\nException handler(s) = \nFrom\tTo\tHandler\tType\n");
            n2 = 0;
            while (n2 < this.exception_table_length) {
                stringBuffer.append(this.exception_table[n2].toString(this.constant_pool, bl) + "\n");
                ++n2;
            }
        }
        if (this.attributes_count > 0) {
            stringBuffer.append("\nAttribute(s) = \n");
            n2 = 0;
            while (n2 < this.attributes_count) {
                stringBuffer.append(this.attributes[n2].toString() + "\n");
                ++n2;
            }
        }
        return stringBuffer.toString();
    }

    public final String toString() {
        return this.toString(true);
    }
}

