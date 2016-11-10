/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class AbsoluteLocationPath
extends Expression {
    private Expression _path;

    public AbsoluteLocationPath() {
        this._path = null;
    }

    public AbsoluteLocationPath(Expression expression) {
        this._path = expression;
        if (expression != null) {
            this._path.setParent(this);
        }
    }

    public void setParser(Parser parser) {
        super.setParser(parser);
        if (this._path != null) {
            this._path.setParser(parser);
        }
    }

    public Expression getPath() {
        return this._path;
    }

    public String toString() {
        return "AbsoluteLocationPath(" + (this._path != null ? this._path.toString() : "null") + ')';
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        Type type;
        if (this._path != null && (type = this._path.typeCheck(symbolTable)) instanceof NodeType) {
            this._path = new CastExpr(this._path, Type.NodeSet);
        }
        this._type = Type.NodeSet;
        return this._type;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (this._path != null) {
            int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.AbsoluteIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;)V");
            this._path.translate(classGenerator, methodGenerator);
            LocalVariableGen localVariableGen = methodGenerator.addLocalVariable("abs_location_path_tmp", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
            localVariableGen.setStart(instructionList.append(new ASTORE(localVariableGen.getIndex())));
            instructionList.append(new NEW(constantPoolGen.addClass("org.apache.xalan.xsltc.dom.AbsoluteIterator")));
            instructionList.append(DUP);
            localVariableGen.setEnd(instructionList.append(new ALOAD(localVariableGen.getIndex())));
            instructionList.append(new INVOKESPECIAL(n2));
        } else {
            int n3 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getIterator", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(new INVOKEINTERFACE(n3, 1));
        }
    }
}

