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
import org.apache.bcel.generic.DLOAD;
import org.apache.bcel.generic.DSTORE;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFNE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.IntType;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NumberType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;

public final class RealType
extends NumberType {
    static Class class$java$lang$Double;

    protected RealType() {
    }

    public String toString() {
        return "real";
    }

    public boolean identicalTo(Type type) {
        return this == type;
    }

    public String toSignature() {
        return "D";
    }

    public org.apache.bcel.generic.Type toJCType() {
        return org.apache.bcel.generic.Type.DOUBLE;
    }

    public int distanceTo(Type type) {
        if (type == this) {
            return 0;
        }
        if (type == Type.Int) {
            return 1;
        }
        return Integer.MAX_VALUE;
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, Type type) {
        if (type == Type.String) {
            this.translateTo(classGenerator, methodGenerator, (StringType)type);
        } else if (type == Type.Boolean) {
            this.translateTo(classGenerator, methodGenerator, (BooleanType)type);
        } else if (type == Type.Reference) {
            this.translateTo(classGenerator, methodGenerator, (ReferenceType)type);
        } else if (type == Type.Int) {
            this.translateTo(classGenerator, methodGenerator, (IntType)type);
        } else {
            ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)type.toString());
            classGenerator.getParser().reportError(2, errorMsg);
        }
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, StringType stringType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(new INVOKESTATIC(constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "realToString", "(D)Ljava/lang/String;")));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, BooleanType booleanType) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        FlowList flowList = this.translateToDesynthesized(classGenerator, methodGenerator, booleanType);
        instructionList.append(ICONST_1);
        BranchHandle branchHandle = instructionList.append(new GOTO(null));
        flowList.backPatch(instructionList.append(ICONST_0));
        branchHandle.setTarget(instructionList.append(NOP));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, IntType intType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(new INVOKESTATIC(constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "realToInt", "(D)I")));
    }

    public FlowList translateToDesynthesized(ClassGenerator classGenerator, MethodGenerator methodGenerator, BooleanType booleanType) {
        FlowList flowList = new FlowList();
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(DUP2);
        LocalVariableGen localVariableGen = methodGenerator.addLocalVariable("real_to_boolean_tmp", org.apache.bcel.generic.Type.DOUBLE, null, null);
        localVariableGen.setStart(instructionList.append(new DSTORE(localVariableGen.getIndex())));
        instructionList.append(DCONST_0);
        instructionList.append(DCMPG);
        flowList.add(instructionList.append(new IFEQ(null)));
        instructionList.append(new DLOAD(localVariableGen.getIndex()));
        localVariableGen.setEnd(instructionList.append(new DLOAD(localVariableGen.getIndex())));
        instructionList.append(DCMPG);
        flowList.add(instructionList.append(new IFNE(null)));
        return flowList;
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, ReferenceType referenceType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(new NEW(constantPoolGen.addClass("java.lang.Double")));
        instructionList.append(DUP_X2);
        instructionList.append(DUP_X2);
        instructionList.append(POP);
        instructionList.append(new INVOKESPECIAL(constantPoolGen.addMethodref("java.lang.Double", "<init>", "(D)V")));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, Class class_) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (class_ == Character.TYPE) {
            instructionList.append(D2I);
            instructionList.append(I2C);
        } else if (class_ == Byte.TYPE) {
            instructionList.append(D2I);
            instructionList.append(I2B);
        } else if (class_ == Short.TYPE) {
            instructionList.append(D2I);
            instructionList.append(I2S);
        } else if (class_ == Integer.TYPE) {
            instructionList.append(D2I);
        } else if (class_ == Long.TYPE) {
            instructionList.append(D2L);
        } else if (class_ == Float.TYPE) {
            instructionList.append(D2F);
        } else if (class_ == Double.TYPE) {
            instructionList.append(NOP);
        } else {
            Class class_2 = class$java$lang$Double == null ? (RealType.class$java$lang$Double = RealType.class$("java.lang.Double")) : class$java$lang$Double;
            if (class_.isAssignableFrom(class_2)) {
                this.translateTo(classGenerator, methodGenerator, Type.Reference);
            } else {
                ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)class_.getName());
                classGenerator.getParser().reportError(2, errorMsg);
            }
        }
    }

    public void translateFrom(ClassGenerator classGenerator, MethodGenerator methodGenerator, Class class_) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (class_ == Character.TYPE || class_ == Byte.TYPE || class_ == Short.TYPE || class_ == Integer.TYPE) {
            instructionList.append(I2D);
        } else if (class_ == Long.TYPE) {
            instructionList.append(L2D);
        } else if (class_ == Float.TYPE) {
            instructionList.append(F2D);
        } else if (class_ == Double.TYPE) {
            instructionList.append(NOP);
        } else {
            ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)class_.getName());
            classGenerator.getParser().reportError(2, errorMsg);
        }
    }

    public void translateBox(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        this.translateTo(classGenerator, methodGenerator, Type.Reference);
    }

    public void translateUnBox(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(new CHECKCAST(constantPoolGen.addClass("java.lang.Double")));
        instructionList.append(new INVOKEVIRTUAL(constantPoolGen.addMethodref("java.lang.Double", "doubleValue", "()D")));
    }

    public Instruction ADD() {
        return InstructionConstants.DADD;
    }

    public Instruction SUB() {
        return InstructionConstants.DSUB;
    }

    public Instruction MUL() {
        return InstructionConstants.DMUL;
    }

    public Instruction DIV() {
        return InstructionConstants.DDIV;
    }

    public Instruction REM() {
        return InstructionConstants.DREM;
    }

    public Instruction NEG() {
        return InstructionConstants.DNEG;
    }

    public Instruction LOAD(int n2) {
        return new DLOAD(n2);
    }

    public Instruction STORE(int n2) {
        return new DSTORE(n2);
    }

    public Instruction POP() {
        return POP2;
    }

    public Instruction CMP(boolean bl) {
        return bl ? InstructionConstants.DCMPG : InstructionConstants.DCMPL;
    }

    public Instruction DUP() {
        return DUP2;
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

