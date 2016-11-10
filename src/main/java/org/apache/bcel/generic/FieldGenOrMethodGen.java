/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.io.PrintStream;
import java.util.ArrayList;
import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Type;

public abstract class FieldGenOrMethodGen
extends AccessFlags
implements Cloneable {
    protected String name;
    protected Type type;
    protected ConstantPoolGen cp;
    private ArrayList attribute_vec = new ArrayList();

    protected FieldGenOrMethodGen() {
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String string) {
        this.name = string;
    }

    public ConstantPoolGen getConstantPool() {
        return this.cp;
    }

    public void setConstantPool(ConstantPoolGen constantPoolGen) {
        this.cp = constantPoolGen;
    }

    public void addAttribute(Attribute attribute) {
        this.attribute_vec.add(attribute);
    }

    public void removeAttribute(Attribute attribute) {
        this.attribute_vec.remove(attribute);
    }

    public void removeAttributes() {
        this.attribute_vec.clear();
    }

    public Attribute[] getAttributes() {
        Attribute[] arrattribute = new Attribute[this.attribute_vec.size()];
        this.attribute_vec.toArray(arrattribute);
        return arrattribute;
    }

    public abstract String getSignature();

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

