/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.StringTokenizer;
import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.SourceFile;
import org.apache.bcel.classfile.Utility;

public class JavaClass
extends AccessFlags
implements Cloneable {
    private String file_name;
    private String package_name;
    private String source_file_name = "<Unknown>";
    private int class_name_index;
    private int superclass_name_index;
    private String class_name;
    private String superclass_name;
    private int major;
    private int minor;
    private ConstantPool constant_pool;
    private int[] interfaces;
    private String[] interface_names;
    private Field[] fields;
    private Method[] methods;
    private Attribute[] attributes;
    private byte source = 1;
    static boolean debug;
    static char sep;

    public JavaClass(int n2, int n3, String string, int n4, int n5, int n6, ConstantPool constantPool, int[] arrn, Field[] arrfield, Method[] arrmethod, Attribute[] arrattribute, byte by) {
        if (arrn == null) {
            arrn = new int[]{};
        }
        if (arrattribute == null) {
            this.attributes = new Attribute[0];
        }
        if (arrfield == null) {
            arrfield = new Field[]{};
        }
        if (arrmethod == null) {
            arrmethod = new Method[]{};
        }
        this.class_name_index = n2;
        this.superclass_name_index = n3;
        this.file_name = string;
        this.major = n4;
        this.minor = n5;
        this.access_flags = n6;
        this.constant_pool = constantPool;
        this.interfaces = arrn;
        this.fields = arrfield;
        this.methods = arrmethod;
        this.attributes = arrattribute;
        this.source = by;
        int n7 = 0;
        while (n7 < arrattribute.length) {
            if (arrattribute[n7] instanceof SourceFile) {
                this.source_file_name = ((SourceFile)arrattribute[n7]).getSourceFileName();
                break;
            }
            ++n7;
        }
        this.class_name = constantPool.getConstantString(n2, 7);
        this.class_name = Utility.compactClassName(this.class_name, false);
        int n8 = this.class_name.lastIndexOf(46);
        this.package_name = n8 < 0 ? "" : this.class_name.substring(0, n8);
        if (n3 > 0) {
            this.superclass_name = constantPool.getConstantString(n3, 7);
            this.superclass_name = Utility.compactClassName(this.superclass_name, false);
        } else {
            this.superclass_name = "java.lang.Object";
        }
        this.interface_names = new String[arrn.length];
        int n9 = 0;
        while (n9 < arrn.length) {
            String string2 = constantPool.getConstantString(arrn[n9], 7);
            this.interface_names[n9] = Utility.compactClassName(string2, false);
            ++n9;
        }
    }

    public JavaClass(int n2, int n3, String string, int n4, int n5, int n6, ConstantPool constantPool, int[] arrn, Field[] arrfield, Method[] arrmethod, Attribute[] arrattribute) {
        this(n2, n3, string, n4, n5, n6, constantPool, arrn, arrfield, arrmethod, arrattribute, 1);
    }

    public void dump(OutputStream outputStream) throws IOException {
        this.dump(new DataOutputStream(outputStream));
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(-889275714);
        dataOutputStream.writeShort(this.minor);
        dataOutputStream.writeShort(this.major);
        this.constant_pool.dump(dataOutputStream);
        dataOutputStream.writeShort(this.access_flags);
        dataOutputStream.writeShort(this.class_name_index);
        dataOutputStream.writeShort(this.superclass_name_index);
        dataOutputStream.writeShort(this.interfaces.length);
        int n2 = 0;
        while (n2 < this.interfaces.length) {
            dataOutputStream.writeShort(this.interfaces[n2]);
            ++n2;
        }
        dataOutputStream.writeShort(this.fields.length);
        int n3 = 0;
        while (n3 < this.fields.length) {
            this.fields[n3].dump(dataOutputStream);
            ++n3;
        }
        dataOutputStream.writeShort(this.methods.length);
        int n4 = 0;
        while (n4 < this.methods.length) {
            this.methods[n4].dump(dataOutputStream);
            ++n4;
        }
        if (this.attributes != null) {
            dataOutputStream.writeShort(this.attributes.length);
            int n5 = 0;
            while (n5 < this.attributes.length) {
                this.attributes[n5].dump(dataOutputStream);
                ++n5;
            }
        } else {
            dataOutputStream.writeShort(0);
        }
        dataOutputStream.close();
    }

    public Attribute[] getAttributes() {
        return this.attributes;
    }

    public String getClassName() {
        return this.class_name;
    }

    public int getClassNameIndex() {
        return this.class_name_index;
    }

    public ConstantPool getConstantPool() {
        return this.constant_pool;
    }

    public Field[] getFields() {
        return this.fields;
    }

    public String[] getInterfaceNames() {
        return this.interface_names;
    }

    public int getMajor() {
        return this.major;
    }

    public Method[] getMethods() {
        return this.methods;
    }

    public int getMinor() {
        return this.minor;
    }

    public String getSourceFileName() {
        return this.source_file_name;
    }

    public String getSuperclassName() {
        return this.superclass_name;
    }

    public int getSuperclassNameIndex() {
        return this.superclass_name_index;
    }

    public String toString() {
        int n2;
        String string = Utility.accessToString(this.access_flags, true);
        string = string.equals("") ? "" : string + " ";
        StringBuffer stringBuffer = new StringBuffer(string + Utility.classOrInterface(this.access_flags) + " " + this.class_name + " extends " + Utility.compactClassName(this.superclass_name, false) + '\n');
        int n3 = this.interfaces.length;
        if (n3 > 0) {
            stringBuffer.append("implements\t\t");
            n2 = 0;
            while (n2 < n3) {
                stringBuffer.append(this.interface_names[n2]);
                if (n2 < n3 - 1) {
                    stringBuffer.append(", ");
                }
                ++n2;
            }
            stringBuffer.append('\n');
        }
        stringBuffer.append("filename\t\t" + this.file_name + '\n');
        stringBuffer.append("compiled from\t\t" + this.source_file_name + '\n');
        stringBuffer.append("compiler version\t" + this.major + "." + this.minor + '\n');
        stringBuffer.append("access flags\t\t" + this.access_flags + '\n');
        stringBuffer.append("constant pool\t\t" + this.constant_pool.getLength() + " entries\n");
        stringBuffer.append("ACC_SUPER flag\t\t" + this.isSuper() + "\n");
        if (this.attributes.length > 0) {
            stringBuffer.append("\nAttribute(s):\n");
            n2 = 0;
            while (n2 < this.attributes.length) {
                stringBuffer.append(JavaClass.indent(this.attributes[n2]));
                ++n2;
            }
        }
        if (this.fields.length > 0) {
            stringBuffer.append("\n" + this.fields.length + " fields:\n");
            n2 = 0;
            while (n2 < this.fields.length) {
                stringBuffer.append("\t" + this.fields[n2] + '\n');
                ++n2;
            }
        }
        if (this.methods.length > 0) {
            stringBuffer.append("\n" + this.methods.length + " methods:\n");
            n2 = 0;
            while (n2 < this.methods.length) {
                stringBuffer.append("\t" + this.methods[n2] + '\n');
                ++n2;
            }
        }
        return stringBuffer.toString();
    }

    private static final String indent(Object object) {
        StringTokenizer stringTokenizer = new StringTokenizer(object.toString(), "\n");
        StringBuffer stringBuffer = new StringBuffer();
        while (stringTokenizer.hasMoreTokens()) {
            stringBuffer.append("\t" + stringTokenizer.nextToken() + "\n");
        }
        return stringBuffer.toString();
    }

    public final boolean isSuper() {
        return (this.access_flags & 32) != 0;
    }

    static {
        String string;
        debug = false;
        sep = 47;
        String string2 = System.getProperty("JavaClass.debug");
        if (string2 != null) {
            debug = new Boolean(string2);
        }
        if ((string = System.getProperty("file.separator")) != null) {
            try {
                sep = string.charAt(0);
            }
            catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
                // empty catch block
            }
        }
    }
}

