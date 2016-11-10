/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantCP;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantDouble;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.Utility;

public class ConstantPool
implements Cloneable {
    private int constant_pool_count;
    private Constant[] constant_pool;

    public ConstantPool(Constant[] arrconstant) {
        this.setConstantPool(arrconstant);
    }

    public String constantToString(Constant constant) throws ClassFormatError {
        String string;
        byte by = constant.getTag();
        switch (by) {
            case 7: {
                int n2 = ((ConstantClass)constant).getNameIndex();
                constant = this.getConstant(n2, 1);
                string = Utility.compactClassName(((ConstantUtf8)constant).getBytes(), false);
                break;
            }
            case 8: {
                int n3 = ((ConstantString)constant).getStringIndex();
                constant = this.getConstant(n3, 1);
                string = "\"" + ConstantPool.escape(((ConstantUtf8)constant).getBytes()) + "\"";
                break;
            }
            case 1: {
                string = ((ConstantUtf8)constant).getBytes();
                break;
            }
            case 6: {
                string = "" + ((ConstantDouble)constant).getBytes();
                break;
            }
            case 4: {
                string = "" + ((ConstantFloat)constant).getBytes();
                break;
            }
            case 5: {
                string = "" + ((ConstantLong)constant).getBytes();
                break;
            }
            case 3: {
                string = "" + ((ConstantInteger)constant).getBytes();
                break;
            }
            case 12: {
                string = this.constantToString(((ConstantNameAndType)constant).getNameIndex(), 1) + " " + this.constantToString(((ConstantNameAndType)constant).getSignatureIndex(), 1);
                break;
            }
            case 9: 
            case 10: 
            case 11: {
                string = this.constantToString(((ConstantCP)constant).getClassIndex(), 7) + "." + this.constantToString(((ConstantCP)constant).getNameAndTypeIndex(), 12);
                break;
            }
            default: {
                throw new RuntimeException("Unknown constant type " + by);
            }
        }
        return string;
    }

    private static final String escape(String string) {
        int n2 = string.length();
        StringBuffer stringBuffer = new StringBuffer(n2 + 5);
        char[] arrc = string.toCharArray();
        int n3 = 0;
        while (n3 < n2) {
            switch (arrc[n3]) {
                case '\n': {
                    stringBuffer.append("\\n");
                    break;
                }
                case '\r': {
                    stringBuffer.append("\\r");
                    break;
                }
                case '\t': {
                    stringBuffer.append("\\t");
                    break;
                }
                case '\b': {
                    stringBuffer.append("\\b");
                    break;
                }
                case '\"': {
                    stringBuffer.append("\\\"");
                    break;
                }
                default: {
                    stringBuffer.append(arrc[n3]);
                }
            }
            ++n3;
        }
        return stringBuffer.toString();
    }

    public String constantToString(int n2, byte by) throws ClassFormatError {
        Constant constant = this.getConstant(n2, by);
        return this.constantToString(constant);
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(this.constant_pool_count);
        int n2 = 1;
        while (n2 < this.constant_pool_count) {
            if (this.constant_pool[n2] != null) {
                this.constant_pool[n2].dump(dataOutputStream);
            }
            ++n2;
        }
    }

    public Constant getConstant(int n2) {
        if (n2 >= this.constant_pool.length || n2 < 0) {
            throw new ClassFormatError("Invalid constant pool reference: " + n2 + ". Constant pool size is: " + this.constant_pool.length);
        }
        return this.constant_pool[n2];
    }

    public Constant getConstant(int n2, byte by) throws ClassFormatError {
        Constant constant = this.getConstant(n2);
        if (constant == null) {
            throw new ClassFormatError("Constant pool at index " + n2 + " is null.");
        }
        if (constant.getTag() == by) {
            return constant;
        }
        throw new ClassFormatError("Expected class `" + Constants.CONSTANT_NAMES[by] + "' at index " + n2 + " and got " + constant);
    }

    public Constant[] getConstantPool() {
        return this.constant_pool;
    }

    public String getConstantString(int n2, byte by) throws ClassFormatError {
        int n3;
        Constant constant = this.getConstant(n2, by);
        switch (by) {
            case 7: {
                n3 = ((ConstantClass)constant).getNameIndex();
                break;
            }
            case 8: {
                n3 = ((ConstantString)constant).getStringIndex();
                break;
            }
            default: {
                throw new RuntimeException("getConstantString called with illegal tag " + by);
            }
        }
        constant = this.getConstant(n3, 1);
        return ((ConstantUtf8)constant).getBytes();
    }

    public int getLength() {
        return this.constant_pool_count;
    }

    public void setConstantPool(Constant[] arrconstant) {
        this.constant_pool = arrconstant;
        this.constant_pool_count = arrconstant == null ? 0 : arrconstant.length;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        int n2 = 1;
        while (n2 < this.constant_pool_count) {
            stringBuffer.append("" + n2 + ")" + this.constant_pool[n2] + "\n");
            ++n2;
        }
        return stringBuffer.toString();
    }
}

