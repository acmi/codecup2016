/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
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
import org.apache.xalan.xsltc.compiler.util.Util;

public final class ResultTreeType
extends Type {
    private final String _methodName;

    protected ResultTreeType() {
        this._methodName = null;
    }

    public ResultTreeType(String string) {
        this._methodName = string;
    }

    public String toString() {
        return "result-tree";
    }

    public boolean identicalTo(Type type) {
        return type instanceof ResultTreeType;
    }

    public String toSignature() {
        return "Lorg/apache/xalan/xsltc/DOM;";
    }

    public org.apache.bcel.generic.Type toJCType() {
        return Util.getJCRefType(this.toSignature());
    }

    public String getMethodName() {
        return this._methodName;
    }

    public boolean implementedAsMethod() {
        return this._methodName != null;
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

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, BooleanType booleanType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(POP);
        instructionList.append(ICONST_1);
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, StringType stringType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (this._methodName == null) {
            int n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getStringValue", "()Ljava/lang/String;");
            instructionList.append(new INVOKEINTERFACE(n2, 1));
        } else {
            String string = classGenerator.getClassName();
            int n3 = methodGenerator.getLocalIndex("current");
            instructionList.append(classGenerator.loadTranslet());
            if (classGenerator.isExternal()) {
                instructionList.append(new CHECKCAST(constantPoolGen.addClass(string)));
            }
            instructionList.append(DUP);
            instructionList.append(new GETFIELD(constantPoolGen.addFieldref(string, "_dom", "Lorg/apache/xalan/xsltc/DOM;")));
            int n4 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.StringValueHandler", "<init>", "()V");
            instructionList.append(new NEW(constantPoolGen.addClass("org.apache.xalan.xsltc.runtime.StringValueHandler")));
            instructionList.append(DUP);
            instructionList.append(DUP);
            instructionList.append(new INVOKESPECIAL(n4));
            LocalVariableGen localVariableGen = methodGenerator.addLocalVariable("rt_to_string_handler", Util.getJCRefType("Lorg/apache/xalan/xsltc/runtime/StringValueHandler;"), null, null);
            localVariableGen.setStart(instructionList.append(new ASTORE(localVariableGen.getIndex())));
            n4 = constantPoolGen.addMethodref(string, this._methodName, "(Lorg/apache/xalan/xsltc/DOM;" + TRANSLET_OUTPUT_SIG + ")V");
            instructionList.append(new INVOKEVIRTUAL(n4));
            localVariableGen.setEnd(instructionList.append(new ALOAD(localVariableGen.getIndex())));
            n4 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.StringValueHandler", "getValue", "()Ljava/lang/String;");
            instructionList.append(new INVOKEVIRTUAL(n4));
        }
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, RealType realType) {
        this.translateTo(classGenerator, methodGenerator, Type.String);
        Type.String.translateTo(classGenerator, methodGenerator, Type.Real);
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, ReferenceType referenceType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (this._methodName == null) {
            instructionList.append(NOP);
        } else {
            String string = classGenerator.getClassName();
            int n2 = methodGenerator.getLocalIndex("current");
            instructionList.append(classGenerator.loadTranslet());
            if (classGenerator.isExternal()) {
                instructionList.append(new CHECKCAST(constantPoolGen.addClass(string)));
            }
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(methodGenerator.loadDOM());
            int n3 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getResultTreeFrag", "(IZ)Lorg/apache/xalan/xsltc/DOM;");
            instructionList.append(new PUSH(constantPoolGen, 32));
            instructionList.append(new PUSH(constantPoolGen, false));
            instructionList.append(new INVOKEINTERFACE(n3, 3));
            instructionList.append(DUP);
            LocalVariableGen localVariableGen = methodGenerator.addLocalVariable("rt_to_reference_dom", Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), null, null);
            instructionList.append(new CHECKCAST(constantPoolGen.addClass("Lorg/apache/xalan/xsltc/DOM;")));
            localVariableGen.setStart(instructionList.append(new ASTORE(localVariableGen.getIndex())));
            n3 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getOutputDomBuilder", "()" + TRANSLET_OUTPUT_SIG);
            instructionList.append(new INVOKEINTERFACE(n3, 1));
            instructionList.append(DUP);
            instructionList.append(DUP);
            LocalVariableGen localVariableGen2 = methodGenerator.addLocalVariable("rt_to_reference_handler", Util.getJCRefType(TRANSLET_OUTPUT_SIG), null, null);
            localVariableGen2.setStart(instructionList.append(new ASTORE(localVariableGen2.getIndex())));
            n3 = constantPoolGen.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE, "startDocument", "()V");
            instructionList.append(new INVOKEINTERFACE(n3, 1));
            n3 = constantPoolGen.addMethodref(string, this._methodName, "(Lorg/apache/xalan/xsltc/DOM;" + TRANSLET_OUTPUT_SIG + ")V");
            instructionList.append(new INVOKEVIRTUAL(n3));
            localVariableGen2.setEnd(instructionList.append(new ALOAD(localVariableGen2.getIndex())));
            n3 = constantPoolGen.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE, "endDocument", "()V");
            instructionList.append(new INVOKEINTERFACE(n3, 1));
            localVariableGen.setEnd(instructionList.append(new ALOAD(localVariableGen.getIndex())));
        }
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, NodeSetType nodeSetType) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(DUP);
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(new GETFIELD(constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "namesArray", "[Ljava/lang/String;")));
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(new GETFIELD(constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "urisArray", "[Ljava/lang/String;")));
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(new GETFIELD(constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "typesArray", "[I")));
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(new GETFIELD(constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "namespaceArray", "[Ljava/lang/String;")));
        int n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "setupMapping", "([Ljava/lang/String;[Ljava/lang/String;[I[Ljava/lang/String;)V");
        instructionList.append(new INVOKEINTERFACE(n2, 5));
        instructionList.append(DUP);
        int n3 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getIterator", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
        instructionList.append(new INVOKEINTERFACE(n3, 1));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, ObjectType objectType) {
        methodGenerator.getInstructionList().append(NOP);
    }

    public FlowList translateToDesynthesized(ClassGenerator classGenerator, MethodGenerator methodGenerator, BooleanType booleanType) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        this.translateTo(classGenerator, methodGenerator, Type.Boolean);
        return new FlowList(instructionList.append(new IFEQ(null)));
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, Class class_) {
        String string = class_.getName();
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (string.equals("org.w3c.dom.Node")) {
            this.translateTo(classGenerator, methodGenerator, Type.NodeSet);
            int n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "makeNode", "(Lorg/apache/xml/dtm/DTMAxisIterator;)Lorg/w3c/dom/Node;");
            instructionList.append(new INVOKEINTERFACE(n2, 2));
        } else if (string.equals("org.w3c.dom.NodeList")) {
            this.translateTo(classGenerator, methodGenerator, Type.NodeSet);
            int n3 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "makeNodeList", "(Lorg/apache/xml/dtm/DTMAxisIterator;)Lorg/w3c/dom/NodeList;");
            instructionList.append(new INVOKEINTERFACE(n3, 2));
        } else if (string.equals("java.lang.Object")) {
            instructionList.append(NOP);
        } else if (string.equals("java.lang.String")) {
            this.translateTo(classGenerator, methodGenerator, Type.String);
        } else {
            ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)string);
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
        return "org.apache.xalan.xsltc.DOM";
    }

    public Instruction LOAD(int n2) {
        return new ALOAD(n2);
    }

    public Instruction STORE(int n2) {
        return new ASTORE(n2);
    }
}

