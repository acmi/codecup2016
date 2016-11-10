/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.SourceFile;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.ClassObserver;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.Type;

public class ClassGen
extends AccessFlags
implements Cloneable {
    private String class_name;
    private String super_class_name;
    private String file_name;
    private int class_name_index = -1;
    private int superclass_name_index = -1;
    private int major = 45;
    private int minor = 3;
    private ConstantPoolGen cp;
    private ArrayList field_vec = new ArrayList();
    private ArrayList method_vec = new ArrayList();
    private ArrayList attribute_vec = new ArrayList();
    private ArrayList interface_vec = new ArrayList();
    private ArrayList observers;

    public ClassGen(String string, String string2, String string3, int n2, String[] arrstring) {
        this.class_name = string;
        this.super_class_name = string2;
        this.file_name = string3;
        this.access_flags = n2;
        this.cp = new ConstantPoolGen();
        this.addAttribute(new SourceFile(this.cp.addUtf8("SourceFile"), 2, this.cp.addUtf8(string3), this.cp.getConstantPool()));
        this.class_name_index = this.cp.addClass(string);
        this.superclass_name_index = this.cp.addClass(string2);
        if (arrstring != null) {
            int n3 = 0;
            while (n3 < arrstring.length) {
                this.addInterface(arrstring[n3]);
                ++n3;
            }
        }
    }

    public ClassGen(JavaClass javaClass) {
        this.class_name_index = javaClass.getClassNameIndex();
        this.superclass_name_index = javaClass.getSuperclassNameIndex();
        this.class_name = javaClass.getClassName();
        this.super_class_name = javaClass.getSuperclassName();
        this.file_name = javaClass.getSourceFileName();
        this.access_flags = javaClass.getAccessFlags();
        this.cp = new ConstantPoolGen(javaClass.getConstantPool());
        this.major = javaClass.getMajor();
        this.minor = javaClass.getMinor();
        Attribute[] arrattribute = javaClass.getAttributes();
        Method[] arrmethod = javaClass.getMethods();
        Field[] arrfield = javaClass.getFields();
        String[] arrstring = javaClass.getInterfaceNames();
        int n2 = 0;
        while (n2 < arrstring.length) {
            this.addInterface(arrstring[n2]);
            ++n2;
        }
        int n3 = 0;
        while (n3 < arrattribute.length) {
            this.addAttribute(arrattribute[n3]);
            ++n3;
        }
        int n4 = 0;
        while (n4 < arrmethod.length) {
            this.addMethod(arrmethod[n4]);
            ++n4;
        }
        int n5 = 0;
        while (n5 < arrfield.length) {
            this.addField(arrfield[n5]);
            ++n5;
        }
    }

    public JavaClass getJavaClass() {
        int[] arrn = this.getInterfaces();
        Field[] arrfield = this.getFields();
        Method[] arrmethod = this.getMethods();
        Attribute[] arrattribute = this.getAttributes();
        ConstantPool constantPool = this.cp.getFinalConstantPool();
        return new JavaClass(this.class_name_index, this.superclass_name_index, this.file_name, this.major, this.minor, this.access_flags, constantPool, arrn, arrfield, arrmethod, arrattribute);
    }

    public void addInterface(String string) {
        this.interface_vec.add(string);
    }

    public void removeInterface(String string) {
        this.interface_vec.remove(string);
    }

    public int getMajor() {
        return this.major;
    }

    public void setMajor(int n2) {
        this.major = n2;
    }

    public void setMinor(int n2) {
        this.minor = n2;
    }

    public int getMinor() {
        return this.minor;
    }

    public void addAttribute(Attribute attribute) {
        this.attribute_vec.add(attribute);
    }

    public void addMethod(Method method) {
        this.method_vec.add(method);
    }

    public void addEmptyConstructor(int n2) {
        InstructionList instructionList = new InstructionList();
        instructionList.append(InstructionConstants.THIS);
        instructionList.append(new INVOKESPECIAL(this.cp.addMethodref(this.super_class_name, "<init>", "()V")));
        instructionList.append(InstructionConstants.RETURN);
        MethodGen methodGen = new MethodGen(n2, Type.VOID, Type.NO_ARGS, null, "<init>", this.class_name, instructionList, this.cp);
        methodGen.setMaxStack(1);
        this.addMethod(methodGen.getMethod());
    }

    public void addField(Field field) {
        this.field_vec.add(field);
    }

    public boolean containsField(Field field) {
        return this.field_vec.contains(field);
    }

    public Field containsField(String string) {
        Iterator iterator = this.field_vec.iterator();
        while (iterator.hasNext()) {
            Field field = (Field)iterator.next();
            if (!field.getName().equals(string)) continue;
            return field;
        }
        return null;
    }

    public Method containsMethod(String string, String string2) {
        Iterator iterator = this.method_vec.iterator();
        while (iterator.hasNext()) {
            Method method = (Method)iterator.next();
            if (!method.getName().equals(string) || !method.getSignature().equals(string2)) continue;
            return method;
        }
        return null;
    }

    public void removeAttribute(Attribute attribute) {
        this.attribute_vec.remove(attribute);
    }

    public void removeMethod(Method method) {
        this.method_vec.remove(method);
    }

    public void replaceMethod(Method method, Method method2) {
        if (method2 == null) {
            throw new ClassGenException("Replacement method must not be null");
        }
        int n2 = this.method_vec.indexOf(method);
        if (n2 < 0) {
            this.method_vec.add(method2);
        } else {
            this.method_vec.set(n2, method2);
        }
    }

    public void replaceField(Field field, Field field2) {
        if (field2 == null) {
            throw new ClassGenException("Replacement method must not be null");
        }
        int n2 = this.field_vec.indexOf(field);
        if (n2 < 0) {
            this.field_vec.add(field2);
        } else {
            this.field_vec.set(n2, field2);
        }
    }

    public void removeField(Field field) {
        this.field_vec.remove(field);
    }

    public String getClassName() {
        return this.class_name;
    }

    public String getSuperclassName() {
        return this.super_class_name;
    }

    public String getFileName() {
        return this.file_name;
    }

    public void setClassName(String string) {
        this.class_name = string.replace('/', '.');
        this.class_name_index = this.cp.addClass(string);
    }

    public void setSuperclassName(String string) {
        this.super_class_name = string.replace('/', '.');
        this.superclass_name_index = this.cp.addClass(string);
    }

    public Method[] getMethods() {
        Method[] arrmethod = new Method[this.method_vec.size()];
        this.method_vec.toArray(arrmethod);
        return arrmethod;
    }

    public void setMethods(Method[] arrmethod) {
        this.method_vec.clear();
        int n2 = 0;
        while (n2 < arrmethod.length) {
            this.addMethod(arrmethod[n2]);
            ++n2;
        }
    }

    public void setMethodAt(Method method, int n2) {
        this.method_vec.set(n2, method);
    }

    public Method getMethodAt(int n2) {
        return (Method)this.method_vec.get(n2);
    }

    public String[] getInterfaceNames() {
        int n2 = this.interface_vec.size();
        String[] arrstring = new String[n2];
        this.interface_vec.toArray(arrstring);
        return arrstring;
    }

    public int[] getInterfaces() {
        int n2 = this.interface_vec.size();
        int[] arrn = new int[n2];
        int n3 = 0;
        while (n3 < n2) {
            arrn[n3] = this.cp.addClass((String)this.interface_vec.get(n3));
            ++n3;
        }
        return arrn;
    }

    public Field[] getFields() {
        Field[] arrfield = new Field[this.field_vec.size()];
        this.field_vec.toArray(arrfield);
        return arrfield;
    }

    public Attribute[] getAttributes() {
        Attribute[] arrattribute = new Attribute[this.attribute_vec.size()];
        this.attribute_vec.toArray(arrattribute);
        return arrattribute;
    }

    public ConstantPoolGen getConstantPool() {
        return this.cp;
    }

    public void setConstantPool(ConstantPoolGen constantPoolGen) {
        this.cp = constantPoolGen;
    }

    public void setClassNameIndex(int n2) {
        this.class_name_index = n2;
        this.class_name = this.cp.getConstantPool().getConstantString(n2, 7).replace('/', '.');
    }

    public void setSuperclassNameIndex(int n2) {
        this.superclass_name_index = n2;
        this.super_class_name = this.cp.getConstantPool().getConstantString(n2, 7).replace('/', '.');
    }

    public int getSuperclassNameIndex() {
        return this.superclass_name_index;
    }

    public int getClassNameIndex() {
        return this.class_name_index;
    }

    public void addObserver(ClassObserver classObserver) {
        if (this.observers == null) {
            this.observers = new ArrayList();
        }
        this.observers.add(classObserver);
    }

    public void removeObserver(ClassObserver classObserver) {
        if (this.observers != null) {
            this.observers.remove(classObserver);
        }
    }

    public void update() {
        if (this.observers != null) {
            Iterator iterator = this.observers.iterator();
            while (iterator.hasNext()) {
                ((ClassObserver)iterator.next()).notify(this);
            }
        }
    }

    public Object clone() {
        try {
            return Object.super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            System.err.println(cloneNotSupportedException);
            return null;
        }
    }
}

