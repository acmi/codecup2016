/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ConversionInstruction;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFGE;
import org.apache.bcel.generic.IFGT;
import org.apache.bcel.generic.IFLE;
import org.apache.bcel.generic.IFLT;
import org.apache.bcel.generic.IF_ICMPGE;
import org.apache.bcel.generic.IF_ICMPGT;
import org.apache.bcel.generic.IF_ICMPLE;
import org.apache.bcel.generic.IF_ICMPLT;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.RealType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;

public final class BooleanType
extends Type {
    static Class class$java$lang$Boolean;

    protected BooleanType() {
    }

    public String toString() {
        return "boolean";
    }

    public boolean identicalTo(Type type) {
        return this == type;
    }

    public String toSignature() {
        return "Z";
    }

    public boolean isSimple() {
        return true;
    }

    public org.apache.bcel.generic.Type toJCType() {
        return org.apache.bcel.generic.Type.BOOLEAN;
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, Type type) {
        if (type == Type.String) {
            this.translateTo(classGenerator, methodGenerator, (StringType)type);
        } else if (type == Type.Real) {
            this.translateTo(classGenerator, methodGenerator, (RealType)type);
        } else if (type == Type.Reference) {
            this.translateTo(classGenerator, methodGenerator, (ReferenceType)type);
        } else {
            ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)type.toString());
            classGenerator.getParser().reportError(2, errorMsg);
        }
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, StringType stringType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        BranchHandle branchHandle = instructionList.append(new IFEQ(null));
        instructionList.append(new PUSH(constantPoolGen, "true"));
        BranchHandle branchHandle2 = instructionList.append(new GOTO(null));
        branchHandle.setTarget(instructionList.append(new PUSH(constantPoolGen, "false")));
        branchHandle2.setTarget(instructionList.append(NOP));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, RealType realType) {
        methodGenerator.getInstructionList().append(I2D);
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, ReferenceType referenceType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(new NEW(constantPoolGen.addClass("java.lang.Boolean")));
        instructionList.append(DUP_X1);
        instructionList.append(SWAP);
        instructionList.append(new INVOKESPECIAL(constantPoolGen.addMethodref("java.lang.Boolean", "<init>", "(Z)V")));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, Class class_) {
        if (class_ == Boolean.TYPE) {
            methodGenerator.getInstructionList().append(NOP);
        } else {
            Class class_2 = class$java$lang$Boolean == null ? (BooleanType.class$java$lang$Boolean = BooleanType.class$("java.lang.Boolean")) : class$java$lang$Boolean;
            if (class_.isAssignableFrom(class_2)) {
                this.translateTo(classGenerator, methodGenerator, Type.Reference);
            } else {
                ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)class_.getName());
                classGenerator.getParser().reportError(2, errorMsg);
            }
        }
    }

    public void translateFrom(ClassGenerator classGenerator, MethodGenerator methodGenerator, Class class_) {
        this.translateTo(classGenerator, methodGenerator, class_);
    }

    public void translateBox(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        this.translateTo(classGenerator, methodGenerator, Type.Reference);
    }

    public void translateUnBox(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(new CHECKCAST(constantPoolGen.addClass("java.lang.Boolean")));
        instructionList.append(new INVOKEVIRTUAL(constantPoolGen.addMethodref("java.lang.Boolean", "booleanValue", "()Z")));
    }

    public Instruction LOAD(int n2) {
        return new ILOAD(n2);
    }

    public Instruction STORE(int n2) {
        return new ISTORE(n2);
    }

    public BranchInstruction GT(boolean bl) {
        return bl ? new IFGT(null) : new IF_ICMPGT(null);
    }

    public BranchInstruction GE(boolean bl) {
        return bl ? new IFGE(null) : new IF_ICMPGE(null);
    }

    public BranchInstruction LT(boolean bl) {
        return bl ? new IFLT(null) : new IF_ICMPLT(null);
    }

    public BranchInstruction LE(boolean bl) {
        return bl ? new IFLE(null) : new IF_ICMPLE(null);
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

