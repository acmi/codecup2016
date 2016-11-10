/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantDouble;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.ConstantUtf8;

public final class ConstantValue
extends Attribute {
    private int constantvalue_index;

    public ConstantValue(int n2, int n3, int n4, ConstantPool constantPool) {
        super(1, n2, n3, constantPool);
        this.constantvalue_index = n4;
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        dataOutputStream.writeShort(this.constantvalue_index);
    }

    public final String toString() throws InternalError {
        String string;
        Constant constant = this.constant_pool.getConstant(this.constantvalue_index);
        switch (constant.getTag()) {
            case 5: {
                string = "" + ((ConstantLong)constant).getBytes();
                break;
            }
            case 4: {
                string = "" + ((ConstantFloat)constant).getBytes();
                break;
            }
            case 6: {
                string = "" + ((ConstantDouble)constant).getBytes();
                break;
            }
            case 3: {
                string = "" + ((ConstantInteger)constant).getBytes();
                break;
            }
            case 8: {
                int n2 = ((ConstantString)constant).getStringIndex();
                constant = this.constant_pool.getConstant(n2, 1);
                string = "\"" + ConstantValue.convertString(((ConstantUtf8)constant).getBytes()) + "\"";
                break;
            }
            default: {
                throw new InternalError("Type of ConstValue invalid: " + constant);
            }
        }
        return string;
    }

    private static final String convertString(String string) {
        char[] arrc = string.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        int n2 = 0;
        while (n2 < arrc.length) {
            switch (arrc[n2]) {
                case '\n': {
                    stringBuffer.append("\\n");
                    break;
                }
                case '\r': {
                    stringBuffer.append("\\r");
                    break;
                }
                case '\"': {
                    stringBuffer.append("\\\"");
                    break;
                }
                case '\'': {
                    stringBuffer.append("\\'");
                    break;
                }
                case '\\': {
                    stringBuffer.append("\\\\");
                    break;
                }
                default: {
                    stringBuffer.append(arrc[n2]);
                }
            }
            ++n2;
        }
        return stringBuffer.toString();
    }
}

