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
import org.apache.bcel.generic.ConversionInstruction;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.ObjectType;
import org.apache.xalan.xsltc.compiler.util.RealType;
import org.apache.xalan.xsltc.compiler.util.ResultTreeType;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;

public final class ReferenceType
extends Type {
    protected ReferenceType() {
    }

    public String toString() {
        return "reference";
    }

    public boolean identicalTo(Type type) {
        return this == type;
    }

    public String toSignature() {
        return "Ljava/lang/Object;";
    }

    public org.apache.bcel.generic.Type toJCType() {
        return org.apache.bcel.generic.Type.OBJECT;
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, Type type) {
        if (type == Type.String) {
            this.translateTo(classGenerator, methodGenerator, (StringType)type);
        } else if (type == Type.Real) {
            this.translateTo(classGenerator, methodGenerator, (RealType)type);
        } else if (type == Type.Boolean) {
            this.translateTo(classGenerator, methodGenerator, (BooleanType)type);
        } else if (type == Type.NodeSet) {
            this.translateTo(classGenerator, methodGenerator, (NodeSetType)type);
        } else if (type == Type.Node) {
            this.translateTo(classGenerator, methodGenerator, (NodeType)type);
        } else if (type == Type.ResultTree) {
            this.translateTo(classGenerator, methodGenerator, (ResultTreeType)type);
        } else if (type == Type.Object) {
            this.translateTo(classGenerator, methodGenerator, (ObjectType)type);
        } else if (type != Type.Reference) {
            ErrorMsg errorMsg = new ErrorMsg("INTERNAL_ERR", type.toString());
            classGenerator.getParser().reportError(2, errorMsg);
        }
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, StringType stringType) {
        int n2 = methodGenerator.getLocalIndex("current");
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (n2 < 0) {
            instructionList.append(new PUSH(constantPoolGen, 0));
        } else {
            instructionList.append(new ILOAD(n2));
        }
        instructionList.append(methodGenerator.loadDOM());
        int n3 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "stringF", "(Ljava/lang/Object;ILorg/apache/xalan/xsltc/DOM;)Ljava/lang/String;");
        instructionList.append(new INVOKESTATIC(n3));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, RealType realType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(methodGenerator.loadDOM());
        int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "numberF", "(Ljava/lang/Object;Lorg/apache/xalan/xsltc/DOM;)D");
        instructionList.append(new INVOKESTATIC(n2));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, BooleanType booleanType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "booleanF", "(Ljava/lang/Object;)Z");
        instructionList.append(new INVOKESTATIC(n2));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, NodeSetType nodeSetType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "referenceToNodeSet", "(Ljava/lang/Object;)Lorg/apache/xml/dtm/DTMAxisIterator;");
        instructionList.append(new INVOKESTATIC(n2));
        n2 = constantPoolGen.addInterfaceMethodref("org.apache.xml.dtm.DTMAxisIterator", "reset", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
        instructionList.append(new INVOKEINTERFACE(n2, 1));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, NodeType nodeType) {
        this.translateTo(classGenerator, methodGenerator, Type.NodeSet);
        Type.NodeSet.translateTo(classGenerator, methodGenerator, nodeType);
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, ResultTreeType resultTreeType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "referenceToResultTree", "(Ljava/lang/Object;)Lorg/apache/xalan/xsltc/DOM;");
        instructionList.append(new INVOKESTATIC(n2));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, ObjectType objectType) {
        methodGenerator.getInstructionList().append(NOP);
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, Class class_) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "referenceToLong", "(Ljava/lang/Object;)J");
        int n3 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "referenceToDouble", "(Ljava/lang/Object;)D");
        int n4 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "referenceToBoolean", "(Ljava/lang/Object;)Z");
        if (class_.getName().equals("java.lang.Object")) {
            instructionList.append(NOP);
        } else if (class_ == Double.TYPE) {
            instructionList.append(new INVOKESTATIC(n3));
        } else if (class_.getName().equals("java.lang.Double")) {
            instructionList.append(new INVOKESTATIC(n3));
            Type.Real.translateTo(classGenerator, methodGenerator, Type.Reference);
        } else if (class_ == Float.TYPE) {
            instructionList.append(new INVOKESTATIC(n3));
            instructionList.append(D2F);
        } else if (class_.getName().equals("java.lang.String")) {
            int n5 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "referenceToString", "(Ljava/lang/Object;Lorg/apache/xalan/xsltc/DOM;)Ljava/lang/String;");
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(new INVOKESTATIC(n5));
        } else if (class_.getName().equals("org.w3c.dom.Node")) {
            int n6 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "referenceToNode", "(Ljava/lang/Object;Lorg/apache/xalan/xsltc/DOM;)Lorg/w3c/dom/Node;");
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(new INVOKESTATIC(n6));
        } else if (class_.getName().equals("org.w3c.dom.NodeList")) {
            int n7 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "referenceToNodeList", "(Ljava/lang/Object;Lorg/apache/xalan/xsltc/DOM;)Lorg/w3c/dom/NodeList;");
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(new INVOKESTATIC(n7));
        } else if (class_.getName().equals("org.apache.xalan.xsltc.DOM")) {
            this.translateTo(classGenerator, methodGenerator, Type.ResultTree);
        } else if (class_ == Long.TYPE) {
            instructionList.append(new INVOKESTATIC(n2));
        } else if (class_ == Integer.TYPE) {
            instructionList.append(new INVOKESTATIC(n2));
            instructionList.append(L2I);
        } else if (class_ == Short.TYPE) {
            instructionList.append(new INVOKESTATIC(n2));
            instructionList.append(L2I);
            instructionList.append(I2S);
        } else if (class_ == Byte.TYPE) {
            instructionList.append(new INVOKESTATIC(n2));
            instructionList.append(L2I);
            instructionList.append(I2B);
        } else if (class_ == Character.TYPE) {
            instructionList.append(new INVOKESTATIC(n2));
            instructionList.append(L2I);
            instructionList.append(I2C);
        } else if (class_ == Boolean.TYPE) {
            instructionList.append(new INVOKESTATIC(n4));
        } else if (class_.getName().equals("java.lang.Boolean")) {
            instructionList.append(new INVOKESTATIC(n4));
            Type.Boolean.translateTo(classGenerator, methodGenerator, Type.Reference);
        } else {
            ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)class_.getName());
            classGenerator.getParser().reportError(2, errorMsg);
        }
    }

    public void translateFrom(ClassGenerator classGenerator, MethodGenerator methodGenerator, Class class_) {
        if (class_.getName().equals("java.lang.Object")) {
            methodGenerator.getInstructionList().append(NOP);
        } else {
            ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)class_.getName());
            classGenerator.getParser().reportError(2, errorMsg);
        }
    }

    public FlowList translateToDesynthesized(ClassGenerator classGenerator, MethodGenerator methodGenerator, BooleanType booleanType) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        this.translateTo(classGenerator, methodGenerator, booleanType);
        return new FlowList(instructionList.append(new IFEQ(null)));
    }

    public void translateBox(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
    }

    public void translateUnBox(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
    }

    public Instruction LOAD(int n2) {
        return new ALOAD(n2);
    }

    public Instruction STORE(int n2) {
        return new ASTORE(n2);
    }
}

