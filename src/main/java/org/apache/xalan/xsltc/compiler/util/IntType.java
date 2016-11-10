/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ArithmeticInstruction;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CHECKCAST;
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
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NumberType;
import org.apache.xalan.xsltc.compiler.util.RealType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;

public final class IntType
extends NumberType {
    static Class class$java$lang$Double;

    protected IntType() {
    }

    public String toString() {
        return "int";
    }

    public boolean identicalTo(Type type) {
        return this == type;
    }

    public String toSignature() {
        return "I";
    }

    public org.apache.bcel.generic.Type toJCType() {
        return org.apache.bcel.generic.Type.INT;
    }

    public int distanceTo(Type type) {
        if (type == this) {
            return 0;
        }
        if (type == Type.Real) {
            return 1;
        }
        return Integer.MAX_VALUE;
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, Type type) {
        if (type == Type.Real) {
            this.translateTo(classGenerator, methodGenerator, (RealType)type);
        } else if (type == Type.String) {
            this.translateTo(classGenerator, methodGenerator, (StringType)type);
        } else if (type == Type.Boolean) {
            this.translateTo(classGenerator, methodGenerator, (BooleanType)type);
        } else if (type == Type.Reference) {
            this.translateTo(classGenerator, methodGenerator, (ReferenceType)type);
        } else {
            ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)type.toString());
            classGenerator.getParser().reportError(2, errorMsg);
        }
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, RealType realType) {
        methodGenerator.getInstructionList().append(I2D);
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, StringType stringType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(new INVOKESTATIC(constantPoolGen.addMethodref("java.lang.Integer", "toString", "(I)Ljava/lang/String;")));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, BooleanType booleanType) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        BranchHandle branchHandle = instructionList.append(new IFEQ(null));
        instructionList.append(ICONST_1);
        BranchHandle branchHandle2 = instructionList.append(new GOTO(null));
        branchHandle.setTarget(instructionList.append(ICONST_0));
        branchHandle2.setTarget(instructionList.append(NOP));
    }

    public FlowList translateToDesynthesized(ClassGenerator classGenerator, MethodGenerator methodGenerator, BooleanType booleanType) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        return new FlowList(instructionList.append(new IFEQ(null)));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, ReferenceType referenceType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(new NEW(constantPoolGen.addClass("java.lang.Integer")));
        instructionList.append(DUP_X1);
        instructionList.append(SWAP);
        instructionList.append(new INVOKESPECIAL(constantPoolGen.addMethodref("java.lang.Integer", "<init>", "(I)V")));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, Class class_) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (class_ == Character.TYPE) {
            instructionList.append(I2C);
        } else if (class_ == Byte.TYPE) {
            instructionList.append(I2B);
        } else if (class_ == Short.TYPE) {
            instructionList.append(I2S);
        } else if (class_ == Integer.TYPE) {
            instructionList.append(NOP);
        } else if (class_ == Long.TYPE) {
            instructionList.append(I2L);
        } else if (class_ == Float.TYPE) {
            instructionList.append(I2F);
        } else if (class_ == Double.TYPE) {
            instructionList.append(I2D);
        } else {
            Class class_2 = class$java$lang$Double == null ? (IntType.class$java$lang$Double = IntType.class$("java.lang.Double")) : class$java$lang$Double;
            if (class_.isAssignableFrom(class_2)) {
                instructionList.append(I2D);
                Type.Real.translateTo(classGenerator, methodGenerator, Type.Reference);
            } else {
                ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)class_.getName());
                classGenerator.getParser().reportError(2, errorMsg);
            }
        }
    }

    public void translateBox(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        this.translateTo(classGenerator, methodGenerator, Type.Reference);
    }

    public void translateUnBox(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(new CHECKCAST(constantPoolGen.addClass("java.lang.Integer")));
        int n2 = constantPoolGen.addMethodref("java.lang.Integer", "intValue", "()I");
        instructionList.append(new INVOKEVIRTUAL(n2));
    }

    public Instruction ADD() {
        return InstructionConstants.IADD;
    }

    public Instruction SUB() {
        return InstructionConstants.ISUB;
    }

    public Instruction MUL() {
        return InstructionConstants.IMUL;
    }

    public Instruction DIV() {
        return InstructionConstants.IDIV;
    }

    public Instruction REM() {
        return InstructionConstants.IREM;
    }

    public Instruction NEG() {
        return InstructionConstants.INEG;
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

