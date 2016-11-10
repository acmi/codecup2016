/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFNULL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.ObjectFactory;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.Util;

public final class ObjectType
extends Type {
    private String _javaClassName = "java.lang.Object";
    private Class _clazz;
    static Class class$java$lang$Object;

    protected ObjectType(String string) {
        Class class_ = class$java$lang$Object == null ? (ObjectType.class$java$lang$Object = ObjectType.class$("java.lang.Object")) : class$java$lang$Object;
        this._clazz = class_;
        this._javaClassName = string;
        try {
            this._clazz = ObjectFactory.findProviderClass(string, ObjectFactory.findClassLoader(), true);
        }
        catch (ClassNotFoundException classNotFoundException) {
            this._clazz = null;
        }
    }

    protected ObjectType(Class class_) {
        Class class_2 = class$java$lang$Object == null ? (ObjectType.class$java$lang$Object = ObjectType.class$("java.lang.Object")) : class$java$lang$Object;
        this._clazz = class_2;
        this._clazz = class_;
        this._javaClassName = class_.getName();
    }

    public int hashCode() {
        Class class_ = class$java$lang$Object == null ? (ObjectType.class$java$lang$Object = ObjectType.class$("java.lang.Object")) : class$java$lang$Object;
        return class_.hashCode();
    }

    public boolean equals(Object object) {
        return object instanceof ObjectType;
    }

    public String getJavaClassName() {
        return this._javaClassName;
    }

    public Class getJavaClass() {
        return this._clazz;
    }

    public String toString() {
        return this._javaClassName;
    }

    public boolean identicalTo(Type type) {
        return this == type;
    }

    public String toSignature() {
        StringBuffer stringBuffer = new StringBuffer("L");
        stringBuffer.append(this._javaClassName.replace('.', '/')).append(';');
        return stringBuffer.toString();
    }

    public org.apache.bcel.generic.Type toJCType() {
        return Util.getJCRefType(this.toSignature());
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, Type type) {
        if (type == Type.String) {
            this.translateTo(classGenerator, methodGenerator, (StringType)type);
        } else {
            ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)type.toString());
            classGenerator.getParser().reportError(2, errorMsg);
        }
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, StringType stringType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(DUP);
        BranchHandle branchHandle = instructionList.append(new IFNULL(null));
        instructionList.append(new INVOKEVIRTUAL(constantPoolGen.addMethodref(this._javaClassName, "toString", "()Ljava/lang/String;")));
        BranchHandle branchHandle2 = instructionList.append(new GOTO(null));
        branchHandle.setTarget(instructionList.append(POP));
        instructionList.append(new PUSH(constantPoolGen, ""));
        branchHandle2.setTarget(instructionList.append(NOP));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, Class class_) {
        if (class_.isAssignableFrom(this._clazz)) {
            methodGenerator.getInstructionList().append(NOP);
        } else {
            ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)class_.getClass().toString());
            classGenerator.getParser().reportError(2, errorMsg);
        }
    }

    public void translateFrom(ClassGenerator classGenerator, MethodGenerator methodGenerator, Class class_) {
        methodGenerator.getInstructionList().append(NOP);
    }

    public Instruction LOAD(int n2) {
        return new ALOAD(n2);
    }

    public Instruction STORE(int n2) {
        return new ASTORE(n2);
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }
}

