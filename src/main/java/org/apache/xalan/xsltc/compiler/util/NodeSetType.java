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
import org.apache.bcel.generic.IFLT;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.ObjectType;
import org.apache.xalan.xsltc.compiler.util.RealType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;

public final class NodeSetType
extends Type {
    protected NodeSetType() {
    }

    public String toString() {
        return "node-set";
    }

    public boolean identicalTo(Type type) {
        return this == type;
    }

    public String toSignature() {
        return "Lorg/apache/xml/dtm/DTMAxisIterator;";
    }

    public org.apache.bcel.generic.Type toJCType() {
        return new org.apache.bcel.generic.ObjectType("org.apache.xml.dtm.DTMAxisIterator");
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, Type type) {
        if (type == Type.String) {
            this.translateTo(classGenerator, methodGenerator, (StringType)type);
        } else if (type == Type.Boolean) {
            this.translateTo(classGenerator, methodGenerator, (BooleanType)type);
        } else if (type == Type.Real) {
            this.translateTo(classGenerator, methodGenerator, (RealType)type);
        } else if (type == Type.Node) {
            this.translateTo(classGenerator, methodGenerator, (NodeType)type);
        } else if (type == Type.Reference) {
            this.translateTo(classGenerator, methodGenerator, (ReferenceType)type);
        } else if (type == Type.Object) {
            this.translateTo(classGenerator, methodGenerator, (ObjectType)type);
        } else {
            ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)type.toString());
            classGenerator.getParser().reportError(2, errorMsg);
        }
    }

    public void translateFrom(ClassGenerator classGenerator, MethodGenerator methodGenerator, Class class_) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        if (class_.getName().equals("org.w3c.dom.NodeList")) {
            instructionList.append(classGenerator.loadTranslet());
            instructionList.append(methodGenerator.loadDOM());
            int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "nodeList2Iterator", "(Lorg/w3c/dom/NodeList;Lorg/apache/xalan/xsltc/Translet;Lorg/apache/xalan/xsltc/DOM;)Lorg/apache/xml/dtm/DTMAxisIterator;");
            instructionList.append(new INVOKESTATIC(n2));
        } else if (class_.getName().equals("org.w3c.dom.Node")) {
            instructionList.append(classGenerator.loadTranslet());
            instructionList.append(methodGenerator.loadDOM());
            int n3 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "node2Iterator", "(Lorg/w3c/dom/Node;Lorg/apache/xalan/xsltc/Translet;Lorg/apache/xalan/xsltc/DOM;)Lorg/apache/xml/dtm/DTMAxisIterator;");
            instructionList.append(new INVOKESTATIC(n3));
        } else {
            ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)class_.getName());
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

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, StringType stringType) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        this.getFirstNode(classGenerator, methodGenerator);
        instructionList.append(DUP);
        BranchHandle branchHandle = instructionList.append(new IFLT(null));
        Type.Node.translateTo(classGenerator, methodGenerator, stringType);
        BranchHandle branchHandle2 = instructionList.append(new GOTO(null));
        branchHandle.setTarget(instructionList.append(POP));
        instructionList.append(new PUSH(classGenerator.getConstantPool(), ""));
        branchHandle2.setTarget(instructionList.append(NOP));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, RealType realType) {
        this.translateTo(classGenerator, methodGenerator, Type.String);
        Type.String.translateTo(classGenerator, methodGenerator, Type.Real);
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, NodeType nodeType) {
        this.getFirstNode(classGenerator, methodGenerator);
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, ObjectType objectType) {
        methodGenerator.getInstructionList().append(NOP);
    }

    public FlowList translateToDesynthesized(ClassGenerator classGenerator, MethodGenerator methodGenerator, BooleanType booleanType) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        this.getFirstNode(classGenerator, methodGenerator);
        return new FlowList(instructionList.append(new IFLT(null)));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, ReferenceType referenceType) {
        methodGenerator.getInstructionList().append(NOP);
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, Class class_) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        String string = class_.getName();
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(SWAP);
        if (string.equals("org.w3c.dom.Node")) {
            int n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "makeNode", "(Lorg/apache/xml/dtm/DTMAxisIterator;)Lorg/w3c/dom/Node;");
            instructionList.append(new INVOKEINTERFACE(n2, 2));
        } else if (string.equals("org.w3c.dom.NodeList") || string.equals("java.lang.Object")) {
            int n3 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "makeNodeList", "(Lorg/apache/xml/dtm/DTMAxisIterator;)Lorg/w3c/dom/NodeList;");
            instructionList.append(new INVOKEINTERFACE(n3, 2));
        } else if (string.equals("java.lang.String")) {
            int n4 = constantPoolGen.addInterfaceMethodref("org.apache.xml.dtm.DTMAxisIterator", "next", "()I");
            int n5 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getStringValueX", "(I)Ljava/lang/String;");
            instructionList.append(new INVOKEINTERFACE(n4, 1));
            instructionList.append(new INVOKEINTERFACE(n5, 2));
        } else {
            ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)string);
            classGenerator.getParser().reportError(2, errorMsg);
        }
    }

    private void getFirstNode(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(new INVOKEINTERFACE(constantPoolGen.addInterfaceMethodref("org.apache.xml.dtm.DTMAxisIterator", "next", "()I"), 1));
    }

    public void translateBox(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        this.translateTo(classGenerator, methodGenerator, Type.Reference);
    }

    public void translateUnBox(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        methodGenerator.getInstructionList().append(NOP);
    }

    public String getClassName() {
        return "org.apache.xml.dtm.DTMAxisIterator";
    }

    public Instruction LOAD(int n2) {
        return new ALOAD(n2);
    }

    public Instruction STORE(int n2) {
        return new ASTORE(n2);
    }
}

