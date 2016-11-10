/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.LocalVariable;

public class LocalVariableTable
extends Attribute {
    private int local_variable_table_length;
    private LocalVariable[] local_variable_table;

    public LocalVariableTable(int n2, int n3, LocalVariable[] arrlocalVariable, ConstantPool constantPool) {
        super(5, n2, n3, constantPool);
        this.setLocalVariableTable(arrlocalVariable);
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        dataOutputStream.writeShort(this.local_variable_table_length);
        int n2 = 0;
        while (n2 < this.local_variable_table_length) {
            this.local_variable_table[n2].dump(dataOutputStream);
            ++n2;
        }
    }

    public final LocalVariable[] getLocalVariableTable() {
        return this.local_variable_table;
    }

    public final LocalVariable getLocalVariable(int n2) {
        int n3 = 0;
        while (n3 < this.local_variable_table_length) {
            if (this.local_variable_table[n3].getIndex() == n2) {
                return this.local_variable_table[n3];
            }
            ++n3;
        }
        return null;
    }

    public final void setLocalVariableTable(LocalVariable[] arrlocalVariable) {
        this.local_variable_table = arrlocalVariable;
        this.local_variable_table_length = arrlocalVariable == null ? 0 : arrlocalVariable.length;
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer("");
        int n2 = 0;
        while (n2 < this.local_variable_table_length) {
            stringBuffer.append(this.local_variable_table[n2].toString());
            if (n2 < this.local_variable_table_length - 1) {
                stringBuffer.append('\n');
            }
            ++n2;
        }
        return stringBuffer.toString();
    }
}

