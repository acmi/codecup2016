/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantCP;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.generic.CPInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.LoadClass;

public abstract class FieldOrMethod
extends CPInstruction
implements LoadClass {
    FieldOrMethod() {
    }

    protected FieldOrMethod(short s2, int n2) {
        super(s2, n2);
    }

    public String getSignature(ConstantPoolGen constantPoolGen) {
        ConstantPool constantPool = constantPoolGen.getConstantPool();
        ConstantCP constantCP = (ConstantCP)constantPool.getConstant(this.index);
        ConstantNameAndType constantNameAndType = (ConstantNameAndType)constantPool.getConstant(constantCP.getNameAndTypeIndex());
        return ((ConstantUtf8)constantPool.getConstant(constantNameAndType.getSignatureIndex())).getBytes();
    }
}

