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
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFNONNULL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.RealType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.Type;

public class StringType
extends Type {
    static Class class$java$lang$String;

    protected StringType() {
    }

    public String toString() {
        return "string";
    }

    public boolean identicalTo(Type type) {
        return this == type;
    }

    public String toSignature() {
        return "Ljava/lang/String;";
    }

    public boolean isSimple() {
        return true;
    }

    public org.apache.bcel.generic.Type toJCType() {
        return org.apache.bcel.generic.Type.STRING;
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, Type type) {
        if (type == Type.Boolean) {
            this.translateTo(classGenerator, methodGenerator, (BooleanType)type);
        } else if (type == Type.Real) {
            this.translateTo(classGenerator, methodGenerator, (RealType)type);
        } else if (type == Type.Reference) {
            this.translateTo(classGenerator, methodGenerator, (ReferenceType)type);
        } else {
            ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)type.toString());
            classGenerator.getParser().reportError(2, errorMsg);
        }
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, BooleanType booleanType) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        FlowList flowList = this.translateToDesynthesized(classGenerator, methodGenerator, booleanType);
        instructionList.append(ICONST_1);
        BranchHandle branchHandle = instructionList.append(new GOTO(null));
        flowList.backPatch(instructionList.append(ICONST_0));
        branchHandle.setTarget(instructionList.append(NOP));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, RealType realType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(new INVOKESTATIC(constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "stringToReal", "(Ljava/lang/String;)D")));
    }

    public FlowList translateToDesynthesized(ClassGenerator classGenerator, MethodGenerator methodGenerator, BooleanType booleanType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(new INVOKEVIRTUAL(constantPoolGen.addMethodref("java.lang.String", "length", "()I")));
        return new FlowList(instructionList.append(new IFEQ(null)));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, ReferenceType referenceType) {
        methodGenerator.getInstructionList().append(NOP);
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, Class class_) {
        Class class_2 = class$java$lang$String == null ? (StringType.class$java$lang$String = StringType.class$("java.lang.String")) : class$java$lang$String;
        if (class_.isAssignableFrom(class_2)) {
            methodGenerator.getInstructionList().append(NOP);
        } else {
            ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)class_.getName());
            classGenerator.getParser().reportError(2, errorMsg);
        }
    }

    public void translateFrom(ClassGenerator classGenerator, MethodGenerator methodGenerator, Class class_) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (class_.getName().equals("java.lang.String")) {
            instructionList.append(DUP);
            BranchHandle branchHandle = instructionList.append(new IFNONNULL(null));
            instructionList.append(POP);
            instructionList.append(new PUSH(constantPoolGen, ""));
            branchHandle.setTarget(instructionList.append(NOP));
        } else {
            ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)class_.getName());
            classGenerator.getParser().reportError(2, errorMsg);
        }
    }

    public void translateBox(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        this.translateTo(classGenerator, methodGenerator, Type.Reference);
    }

    public void translateUnBox(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        methodGenerator.getInstructionList().append(NOP);
    }

    public String getClassName() {
        return "java.lang.String";
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

