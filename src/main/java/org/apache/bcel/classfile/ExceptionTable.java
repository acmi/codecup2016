/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Utility;

public final class ExceptionTable
extends Attribute {
    private int number_of_exceptions;
    private int[] exception_index_table;

    public ExceptionTable(int n2, int n3, int[] arrn, ConstantPool constantPool) {
        super(3, n2, n3, constantPool);
        this.setExceptionIndexTable(arrn);
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        dataOutputStream.writeShort(this.number_of_exceptions);
        int n2 = 0;
        while (n2 < this.number_of_exceptions) {
            dataOutputStream.writeShort(this.exception_index_table[n2]);
            ++n2;
        }
    }

    public final String[] getExceptionNames() {
        String[] arrstring = new String[this.number_of_exceptions];
        int n2 = 0;
        while (n2 < this.number_of_exceptions) {
            arrstring[n2] = this.constant_pool.getConstantString(this.exception_index_table[n2], 7).replace('/', '.');
            ++n2;
        }
        return arrstring;
    }

    public final void setExceptionIndexTable(int[] arrn) {
        this.exception_index_table = arrn;
        this.number_of_exceptions = arrn == null ? 0 : arrn.length;
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer("");
        int n2 = 0;
        while (n2 < this.number_of_exceptions) {
            String string = this.constant_pool.getConstantString(this.exception_index_table[n2], 7);
            stringBuffer.append(Utility.compactClassName(string, false));
            if (n2 < this.number_of_exceptions - 1) {
                stringBuffer.append(", ");
            }
            ++n2;
        }
        return stringBuffer.toString();
    }
}

