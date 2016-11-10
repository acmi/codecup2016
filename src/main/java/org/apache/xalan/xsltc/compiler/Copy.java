/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFNULL;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.LiteralElement;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.UseAttributeSets;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class Copy
extends Instruction {
    private UseAttributeSets _useSets;

    Copy() {
    }

    public void parseContents(Parser parser) {
        String string = this.getAttribute("use-attribute-sets");
        if (string.length() > 0) {
            if (!Util.isValidQNames(string)) {
                ErrorMsg errorMsg = new ErrorMsg("INVALID_QNAME_ERR", (Object)string, this);
                parser.reportError(3, errorMsg);
            }
            this._useSets = new UseAttributeSets(string, parser);
        }
        this.parseChildren(parser);
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("Copy");
        this.indent(n2 + 4);
        this.displayContents(n2 + 4);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (this._useSets != null) {
            this._useSets.typeCheck(symbolTable);
        }
        this.typeCheckContents(symbolTable);
        return Type.Void;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Object object;
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        LocalVariableGen localVariableGen = methodGenerator.addLocalVariable2("name", Util.getJCRefType("Ljava/lang/String;"), null);
        LocalVariableGen localVariableGen2 = methodGenerator.addLocalVariable2("length", Util.getJCRefType("I"), null);
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(methodGenerator.loadCurrentNode());
        instructionList.append(methodGenerator.loadHandler());
        int n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "shallowCopy", "(I" + TRANSLET_OUTPUT_SIG + ")" + "Ljava/lang/String;");
        instructionList.append(new INVOKEINTERFACE(n2, 3));
        instructionList.append(DUP);
        localVariableGen.setStart(instructionList.append(new ASTORE(localVariableGen.getIndex())));
        BranchHandle branchHandle2 = instructionList.append(new IFNULL(null));
        instructionList.append(new ALOAD(localVariableGen.getIndex()));
        int n3 = constantPoolGen.addMethodref("java.lang.String", "length", "()I");
        instructionList.append(new INVOKEVIRTUAL(n3));
        localVariableGen2.setStart(instructionList.append(new ISTORE(localVariableGen2.getIndex())));
        if (this._useSets != null) {
            object = this.getParent();
            if (object instanceof LiteralElement || object instanceof LiteralElement) {
                this._useSets.translate(classGenerator, methodGenerator);
            } else {
                instructionList.append(new ILOAD(localVariableGen2.getIndex()));
                BranchHandle branchHandle = instructionList.append(new IFEQ(null));
                this._useSets.translate(classGenerator, methodGenerator);
                branchHandle.setTarget(instructionList.append(NOP));
            }
        }
        this.translateContents(classGenerator, methodGenerator);
        localVariableGen2.setEnd(instructionList.append(new ILOAD(localVariableGen2.getIndex())));
        object = instructionList.append(new IFEQ(null));
        instructionList.append(methodGenerator.loadHandler());
        localVariableGen.setEnd(instructionList.append(new ALOAD(localVariableGen.getIndex())));
        instructionList.append(methodGenerator.endElement());
        InstructionHandle instructionHandle = instructionList.append(NOP);
        branchHandle2.setTarget(instructionHandle);
        object.setTarget(instructionHandle);
        methodGenerator.removeLocalVariable(localVariableGen);
        methodGenerator.removeLocalVariable(localVariableGen2);
    }
}

