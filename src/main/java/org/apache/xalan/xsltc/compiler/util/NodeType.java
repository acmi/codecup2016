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
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.ObjectType;
import org.apache.xalan.xsltc.compiler.util.RealType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;

public final class NodeType
extends Type {
    private final int _type;

    protected NodeType() {
        this(-1);
    }

    protected NodeType(int n2) {
        this._type = n2;
    }

    public int getType() {
        return this._type;
    }

    public String toString() {
        return "node-type";
    }

    public boolean identicalTo(Type type) {
        return type instanceof NodeType;
    }

    public int hashCode() {
        return this._type;
    }

    public String toSignature() {
        return "I";
    }

    public org.apache.bcel.generic.Type toJCType() {
        return org.apache.bcel.generic.Type.INT;
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, Type type) {
        if (type == Type.String) {
            this.translateTo(classGenerator, methodGenerator, (StringType)type);
        } else if (type == Type.Boolean) {
            this.translateTo(classGenerator, methodGenerator, (BooleanType)type);
        } else if (type == Type.Real) {
            this.translateTo(classGenerator, methodGenerator, (RealType)type);
        } else if (type == Type.NodeSet) {
            this.translateTo(classGenerator, methodGenerator, (NodeSetType)type);
        } else if (type == Type.Reference) {
            this.translateTo(classGenerator, methodGenerator, (ReferenceType)type);
        } else if (type == Type.Object) {
            this.translateTo(classGenerator, methodGenerator, (ObjectType)type);
        } else {
            ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)type.toString());
            classGenerator.getParser().reportError(2, errorMsg);
        }
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, StringType stringType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        switch (this._type) {
            case 1: 
            case 9: {
                instructionList.append(methodGenerator.loadDOM());
                instructionList.append(SWAP);
                int n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getElementValue", "(I)Ljava/lang/String;");
                instructionList.append(new INVOKEINTERFACE(n2, 2));
                break;
            }
            case -1: 
            case 2: 
            case 7: 
            case 8: {
                instructionList.append(methodGenerator.loadDOM());
                instructionList.append(SWAP);
                int n3 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getStringValueX", "(I)Ljava/lang/String;");
                instructionList.append(new INVOKEINTERFACE(n3, 2));
                break;
            }
            default: {
                ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)stringType.toString());
                classGenerator.getParser().reportError(2, errorMsg);
            }
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
        this.translateTo(classGenerator, methodGenerator, Type.String);
        Type.String.translateTo(classGenerator, methodGenerator, Type.Real);
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, NodeSetType nodeSetType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(new NEW(constantPoolGen.addClass("org.apache.xalan.xsltc.dom.SingletonIterator")));
        instructionList.append(DUP_X1);
        instructionList.append(SWAP);
        int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.SingletonIterator", "<init>", "(I)V");
        instructionList.append(new INVOKESPECIAL(n2));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, ObjectType objectType) {
        methodGenerator.getInstructionList().append(NOP);
    }

    public FlowList translateToDesynthesized(ClassGenerator classGenerator, MethodGenerator methodGenerator, BooleanType booleanType) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        return new FlowList(instructionList.append(new IFEQ(null)));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, ReferenceType referenceType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(new NEW(constantPoolGen.addClass("org.apache.xalan.xsltc.runtime.Node")));
        instructionList.append(DUP_X1);
        instructionList.append(SWAP);
        instructionList.append(new PUSH(constantPoolGen, this._type));
        instructionList.append(new INVOKESPECIAL(constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.Node", "<init>", "(II)V")));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, Class class_) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        String string = class_.getName();
        if (string.equals("java.lang.String")) {
            this.translateTo(classGenerator, methodGenerator, Type.String);
            return;
        }
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(SWAP);
        if (string.equals("org.w3c.dom.Node") || string.equals("java.lang.Object")) {
            int n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "makeNode", "(I)Lorg/w3c/dom/Node;");
            instructionList.append(new INVOKEINTERFACE(n2, 2));
        } else if (string.equals("org.w3c.dom.NodeList")) {
            int n3 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "makeNodeList", "(I)Lorg/w3c/dom/NodeList;");
            instructionList.append(new INVOKEINTERFACE(n3, 2));
        } else {
            ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)string);
            classGenerator.getParser().reportError(2, errorMsg);
        }
    }

    public void translateBox(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        this.translateTo(classGenerator, methodGenerator, Type.Reference);
    }

    public void translateUnBox(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(new CHECKCAST(constantPoolGen.addClass("org.apache.xalan.xsltc.runtime.Node")));
        instructionList.append(new GETFIELD(constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.Node", "node", "I")));
    }

    public String getClassName() {
        return "org.apache.xalan.xsltc.runtime.Node";
    }

    public Instruction LOAD(int n2) {
        return new ILOAD(n2);
    }

    public Instruction STORE(int n2) {
        return new ISTORE(n2);
    }
}

