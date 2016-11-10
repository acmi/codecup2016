/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.ResultTreeType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class CopyOf
extends Instruction {
    private Expression _select;

    CopyOf() {
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("CopyOf");
        this.indent(n2 + 4);
        Util.println("select " + this._select.toString());
    }

    public void parseContents(Parser parser) {
        this._select = parser.parseExpression(this, "select", null);
        if (this._select.isDummy()) {
            this.reportError(this, parser, "REQUIRED_ATTR_ERR", "select");
            return;
        }
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        Type type = this._select.typeCheck(symbolTable);
        if (!(type instanceof NodeType || type instanceof NodeSetType || type instanceof ReferenceType || type instanceof ResultTreeType)) {
            this._select = new CastExpr(this._select, Type.String);
        }
        return Type.Void;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        Type type = this._select.getType();
        String string = "(Lorg/apache/xml/dtm/DTMAxisIterator;" + TRANSLET_OUTPUT_SIG + ")V";
        int n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "copy", string);
        String string2 = "(I" + TRANSLET_OUTPUT_SIG + ")V";
        int n3 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "copy", string2);
        String string3 = "()I";
        int n4 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getDocument", "()I");
        if (type instanceof NodeSetType) {
            instructionList.append(methodGenerator.loadDOM());
            this._select.translate(classGenerator, methodGenerator);
            this._select.startIterator(classGenerator, methodGenerator);
            instructionList.append(methodGenerator.loadHandler());
            instructionList.append(new INVOKEINTERFACE(n2, 3));
        } else if (type instanceof NodeType) {
            instructionList.append(methodGenerator.loadDOM());
            this._select.translate(classGenerator, methodGenerator);
            instructionList.append(methodGenerator.loadHandler());
            instructionList.append(new INVOKEINTERFACE(n3, 3));
        } else if (type instanceof ResultTreeType) {
            this._select.translate(classGenerator, methodGenerator);
            instructionList.append(DUP);
            instructionList.append(new INVOKEINTERFACE(n4, 1));
            instructionList.append(methodGenerator.loadHandler());
            instructionList.append(new INVOKEINTERFACE(n3, 3));
        } else if (type instanceof ReferenceType) {
            this._select.translate(classGenerator, methodGenerator);
            instructionList.append(methodGenerator.loadHandler());
            instructionList.append(methodGenerator.loadCurrentNode());
            instructionList.append(methodGenerator.loadDOM());
            int n5 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "copy", "(Ljava/lang/Object;" + TRANSLET_OUTPUT_SIG + "I" + "Lorg/apache/xalan/xsltc/DOM;" + ")V");
            instructionList.append(new INVOKESTATIC(n5));
        } else {
            instructionList.append(classGenerator.loadTranslet());
            this._select.translate(classGenerator, methodGenerator);
            instructionList.append(methodGenerator.loadHandler());
            instructionList.append(new INVOKEVIRTUAL(constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "characters", CHARACTERSW_SIG)));
        }
    }
}

