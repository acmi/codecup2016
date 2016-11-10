/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.LineNumber;

public final class LineNumberTable
extends Attribute {
    private int line_number_table_length;
    private LineNumber[] line_number_table;

    public LineNumberTable(int n2, int n3, LineNumber[] arrlineNumber, ConstantPool constantPool) {
        super(4, n2, n3, constantPool);
        this.setLineNumberTable(arrlineNumber);
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        dataOutputStream.writeShort(this.line_number_table_length);
        int n2 = 0;
        while (n2 < this.line_number_table_length) {
            this.line_number_table[n2].dump(dataOutputStream);
            ++n2;
        }
    }

    public final LineNumber[] getLineNumberTable() {
        return this.line_number_table;
    }

    public final void setLineNumberTable(LineNumber[] arrlineNumber) {
        this.line_number_table = arrlineNumber;
        this.line_number_table_length = arrlineNumber == null ? 0 : arrlineNumber.length;
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        int n2 = 0;
        while (n2 < this.line_number_table_length) {
            stringBuffer2.append(this.line_number_table[n2].toString());
            if (n2 < this.line_number_table_length - 1) {
                stringBuffer2.append(", ");
            }
            if (stringBuffer2.length() > 72) {
                stringBuffer2.append('\n');
                stringBuffer.append((Object)stringBuffer2);
                stringBuffer2.setLength(0);
            }
            ++n2;
        }
        stringBuffer.append((Object)stringBuffer2);
        return stringBuffer.toString();
    }
}

