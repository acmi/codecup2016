/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.util.HashMap;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantCP;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantDouble;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.generic.ObjectType;

public class ConstantPoolGen {
    protected int size = 1024;
    protected Constant[] constants = new Constant[this.size];
    protected int index = 1;
    private HashMap string_table = new HashMap();
    private HashMap class_table = new HashMap();
    private HashMap utf8_table = new HashMap();
    private HashMap n_a_t_table = new HashMap();
    private HashMap cp_table = new HashMap();

    public ConstantPoolGen(Constant[] arrconstant) {
        if (arrconstant.length > this.size) {
            this.size = arrconstant.length;
            this.constants = new Constant[this.size];
        }
        System.arraycopy(arrconstant, 0, this.constants, 0, arrconstant.length);
        if (arrconstant.length > 0) {
            this.index = arrconstant.length;
        }
        int n2 = 1;
        while (n2 < this.index) {
            ConstantUtf8 constantUtf8;
            ConstantUtf8 constantUtf82;
            Constant constant = this.constants[n2];
            if (constant instanceof ConstantString) {
                ConstantString constantString = (ConstantString)constant;
                constantUtf8 = (ConstantUtf8)this.constants[constantString.getStringIndex()];
                this.string_table.put(constantUtf8.getBytes(), new Index(n2));
            } else if (constant instanceof ConstantClass) {
                ConstantClass constantClass = (ConstantClass)constant;
                constantUtf8 = (ConstantUtf8)this.constants[constantClass.getNameIndex()];
                this.class_table.put(constantUtf8.getBytes(), new Index(n2));
            } else if (constant instanceof ConstantNameAndType) {
                ConstantNameAndType constantNameAndType = (ConstantNameAndType)constant;
                constantUtf8 = (ConstantUtf8)this.constants[constantNameAndType.getNameIndex()];
                constantUtf82 = (ConstantUtf8)this.constants[constantNameAndType.getSignatureIndex()];
                this.n_a_t_table.put(constantUtf8.getBytes() + "%" + constantUtf82.getBytes(), new Index(n2));
            } else if (constant instanceof ConstantUtf8) {
                ConstantUtf8 constantUtf83 = (ConstantUtf8)constant;
                this.utf8_table.put(constantUtf83.getBytes(), new Index(n2));
            } else if (constant instanceof ConstantCP) {
                ConstantCP constantCP = (ConstantCP)constant;
                constantUtf8 = (ConstantClass)this.constants[constantCP.getClassIndex()];
                constantUtf82 = (ConstantNameAndType)this.constants[constantCP.getNameAndTypeIndex()];
                ConstantUtf8 constantUtf84 = (ConstantUtf8)this.constants[constantUtf8.getNameIndex()];
                String string = constantUtf84.getBytes().replace('/', '.');
                constantUtf84 = (ConstantUtf8)this.constants[constantUtf82.getNameIndex()];
                String string2 = constantUtf84.getBytes();
                constantUtf84 = (ConstantUtf8)this.constants[constantUtf82.getSignatureIndex()];
                String string3 = constantUtf84.getBytes();
                String string4 = ":";
                if (constant instanceof ConstantInterfaceMethodref) {
                    string4 = "#";
                } else if (constant instanceof ConstantFieldref) {
                    string4 = "&";
                }
                this.cp_table.put(string + string4 + string2 + string4 + string3, new Index(n2));
            }
            ++n2;
        }
    }

    public ConstantPoolGen(ConstantPool constantPool) {
        this(constantPool.getConstantPool());
    }

    public ConstantPoolGen() {
    }

    protected void adjustSize() {
        if (this.index + 3 >= this.size) {
            Constant[] arrconstant = this.constants;
            this.size *= 2;
            this.constants = new Constant[this.size];
            System.arraycopy(arrconstant, 0, this.constants, 0, this.index);
        }
    }

    public int lookupString(String string) {
        Index index = (Index)this.string_table.get(string);
        return index != null ? index.index : -1;
    }

    public int addString(String string) {
        int n2 = this.lookupString(string);
        if (n2 != -1) {
            return n2;
        }
        this.adjustSize();
        ConstantUtf8 constantUtf8 = new ConstantUtf8(string);
        ConstantString constantString = new ConstantString(this.index);
        this.constants[this.index++] = constantUtf8;
        n2 = this.index;
        this.constants[this.index++] = constantString;
        this.string_table.put(string, new Index(n2));
        return n2;
    }

    public int lookupClass(String string) {
        Index index = (Index)this.class_table.get(string.replace('.', '/'));
        return index != null ? index.index : -1;
    }

    private int addClass_(String string) {
        int n2 = this.lookupClass(string);
        if (n2 != -1) {
            return n2;
        }
        this.adjustSize();
        ConstantClass constantClass = new ConstantClass(this.addUtf8(string));
        n2 = this.index;
        this.constants[this.index++] = constantClass;
        this.class_table.put(string, new Index(n2));
        return n2;
    }

    public int addClass(String string) {
        return this.addClass_(string.replace('.', '/'));
    }

    public int addClass(ObjectType objectType) {
        return this.addClass(objectType.getClassName());
    }

    public int lookupInteger(int n2) {
        int n3 = 1;
        while (n3 < this.index) {
            ConstantInteger constantInteger;
            if (this.constants[n3] instanceof ConstantInteger && (constantInteger = (ConstantInteger)this.constants[n3]).getBytes() == n2) {
                return n3;
            }
            ++n3;
        }
        return -1;
    }

    public int addInteger(int n2) {
        int n3 = this.lookupInteger(n2);
        if (n3 != -1) {
            return n3;
        }
        this.adjustSize();
        n3 = this.index;
        this.constants[this.index++] = new ConstantInteger(n2);
        return n3;
    }

    public int lookupFloat(float f2) {
        int n2 = 1;
        while (n2 < this.index) {
            ConstantFloat constantFloat;
            if (this.constants[n2] instanceof ConstantFloat && (constantFloat = (ConstantFloat)this.constants[n2]).getBytes() == f2) {
                return n2;
            }
            ++n2;
        }
        return -1;
    }

    public int addFloat(float f2) {
        int n2 = this.lookupFloat(f2);
        if (n2 != -1) {
            return n2;
        }
        this.adjustSize();
        n2 = this.index;
        this.constants[this.index++] = new ConstantFloat(f2);
        return n2;
    }

    public int lookupUtf8(String string) {
        Index index = (Index)this.utf8_table.get(string);
        return index != null ? index.index : -1;
    }

    public int addUtf8(String string) {
        int n2 = this.lookupUtf8(string);
        if (n2 != -1) {
            return n2;
        }
        this.adjustSize();
        n2 = this.index;
        this.constants[this.index++] = new ConstantUtf8(string);
        this.utf8_table.put(string, new Index(n2));
        return n2;
    }

    public int lookupLong(long l2) {
        int n2 = 1;
        while (n2 < this.index) {
            ConstantLong constantLong;
            if (this.constants[n2] instanceof ConstantLong && (constantLong = (ConstantLong)this.constants[n2]).getBytes() == l2) {
                return n2;
            }
            ++n2;
        }
        return -1;
    }

    public int addLong(long l2) {
        int n2 = this.lookupLong(l2);
        if (n2 != -1) {
            return n2;
        }
        this.adjustSize();
        n2 = this.index;
        this.constants[this.index] = new ConstantLong(l2);
        this.index += 2;
        return n2;
    }

    public int lookupDouble(double d2) {
        int n2 = 1;
        while (n2 < this.index) {
            ConstantDouble constantDouble;
            if (this.constants[n2] instanceof ConstantDouble && (constantDouble = (ConstantDouble)this.constants[n2]).getBytes() == d2) {
                return n2;
            }
            ++n2;
        }
        return -1;
    }

    public int addDouble(double d2) {
        int n2 = this.lookupDouble(d2);
        if (n2 != -1) {
            return n2;
        }
        this.adjustSize();
        n2 = this.index;
        this.constants[this.index] = new ConstantDouble(d2);
        this.index += 2;
        return n2;
    }

    public int lookupNameAndType(String string, String string2) {
        Index index = (Index)this.n_a_t_table.get(string + "%" + string2);
        return index != null ? index.index : -1;
    }

    public int addNameAndType(String string, String string2) {
        int n2 = this.lookupNameAndType(string, string2);
        if (n2 != -1) {
            return n2;
        }
        this.adjustSize();
        int n3 = this.addUtf8(string);
        int n4 = this.addUtf8(string2);
        n2 = this.index;
        this.constants[this.index++] = new ConstantNameAndType(n3, n4);
        this.n_a_t_table.put(string + "%" + string2, new Index(n2));
        return n2;
    }

    public int lookupMethodref(String string, String string2, String string3) {
        Index index = (Index)this.cp_table.get(string + ":" + string2 + ":" + string3);
        return index != null ? index.index : -1;
    }

    public int addMethodref(String string, String string2, String string3) {
        int n2 = this.lookupMethodref(string, string2, string3);
        if (n2 != -1) {
            return n2;
        }
        this.adjustSize();
        int n3 = this.addNameAndType(string2, string3);
        int n4 = this.addClass(string);
        n2 = this.index;
        this.constants[this.index++] = new ConstantMethodref(n4, n3);
        this.cp_table.put(string + ":" + string2 + ":" + string3, new Index(n2));
        return n2;
    }

    public int lookupInterfaceMethodref(String string, String string2, String string3) {
        Index index = (Index)this.cp_table.get(string + "#" + string2 + "#" + string3);
        return index != null ? index.index : -1;
    }

    public int addInterfaceMethodref(String string, String string2, String string3) {
        int n2 = this.lookupInterfaceMethodref(string, string2, string3);
        if (n2 != -1) {
            return n2;
        }
        this.adjustSize();
        int n3 = this.addClass(string);
        int n4 = this.addNameAndType(string2, string3);
        n2 = this.index;
        this.constants[this.index++] = new ConstantInterfaceMethodref(n3, n4);
        this.cp_table.put(string + "#" + string2 + "#" + string3, new Index(n2));
        return n2;
    }

    public int lookupFieldref(String string, String string2, String string3) {
        Index index = (Index)this.cp_table.get(string + "&" + string2 + "&" + string3);
        return index != null ? index.index : -1;
    }

    public int addFieldref(String string, String string2, String string3) {
        int n2 = this.lookupFieldref(string, string2, string3);
        if (n2 != -1) {
            return n2;
        }
        this.adjustSize();
        int n3 = this.addClass(string);
        int n4 = this.addNameAndType(string2, string3);
        n2 = this.index;
        this.constants[this.index++] = new ConstantFieldref(n3, n4);
        this.cp_table.put(string + "&" + string2 + "&" + string3, new Index(n2));
        return n2;
    }

    public Constant getConstant(int n2) {
        return this.constants[n2];
    }

    public ConstantPool getConstantPool() {
        return new ConstantPool(this.constants);
    }

    public ConstantPool getFinalConstantPool() {
        Constant[] arrconstant = new Constant[this.index];
        System.arraycopy(this.constants, 0, arrconstant, 0, this.index);
        return new ConstantPool(arrconstant);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        int n2 = 1;
        while (n2 < this.index) {
            stringBuffer.append("" + n2 + ")" + this.constants[n2] + "\n");
            ++n2;
        }
        return stringBuffer.toString();
    }

    public int addConstant(Constant constant, ConstantPoolGen constantPoolGen) {
        Constant[] arrconstant = constantPoolGen.getConstantPool().getConstantPool();
        switch (constant.getTag()) {
            case 8: {
                ConstantString constantString = (ConstantString)constant;
                ConstantUtf8 constantUtf8 = (ConstantUtf8)arrconstant[constantString.getStringIndex()];
                return this.addString(constantUtf8.getBytes());
            }
            case 7: {
                ConstantClass constantClass = (ConstantClass)constant;
                ConstantUtf8 constantUtf8 = (ConstantUtf8)arrconstant[constantClass.getNameIndex()];
                return this.addClass(constantUtf8.getBytes());
            }
            case 12: {
                ConstantNameAndType constantNameAndType = (ConstantNameAndType)constant;
                ConstantUtf8 constantUtf8 = (ConstantUtf8)arrconstant[constantNameAndType.getNameIndex()];
                ConstantUtf8 constantUtf82 = (ConstantUtf8)arrconstant[constantNameAndType.getSignatureIndex()];
                return this.addNameAndType(constantUtf8.getBytes(), constantUtf82.getBytes());
            }
            case 1: {
                return this.addUtf8(((ConstantUtf8)constant).getBytes());
            }
            case 6: {
                return this.addDouble(((ConstantDouble)constant).getBytes());
            }
            case 4: {
                return this.addFloat(((ConstantFloat)constant).getBytes());
            }
            case 5: {
                return this.addLong(((ConstantLong)constant).getBytes());
            }
            case 3: {
                return this.addInteger(((ConstantInteger)constant).getBytes());
            }
            case 9: 
            case 10: 
            case 11: {
                ConstantCP constantCP = (ConstantCP)constant;
                ConstantClass constantClass = (ConstantClass)arrconstant[constantCP.getClassIndex()];
                ConstantNameAndType constantNameAndType = (ConstantNameAndType)arrconstant[constantCP.getNameAndTypeIndex()];
                ConstantUtf8 constantUtf8 = (ConstantUtf8)arrconstant[constantClass.getNameIndex()];
                String string = constantUtf8.getBytes().replace('/', '.');
                constantUtf8 = (ConstantUtf8)arrconstant[constantNameAndType.getNameIndex()];
                String string2 = constantUtf8.getBytes();
                constantUtf8 = (ConstantUtf8)arrconstant[constantNameAndType.getSignatureIndex()];
                String string3 = constantUtf8.getBytes();
                switch (constant.getTag()) {
                    case 11: {
                        return this.addInterfaceMethodref(string, string2, string3);
                    }
                    case 10: {
                        return this.addMethodref(string, string2, string3);
                    }
                    case 9: {
                        return this.addFieldref(string, string2, string3);
                    }
                }
                throw new RuntimeException("Unknown constant type " + constant);
            }
        }
        throw new RuntimeException("Unknown constant type " + constant);
    }

    private static class Index {
        int index;

        Index(int n2) {
            this.index = n2;
        }
    }

}

