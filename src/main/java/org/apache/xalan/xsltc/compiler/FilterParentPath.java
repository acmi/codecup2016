/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.CurrentCall;
import org.apache.xalan.xsltc.compiler.DocumentCall;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.KeyCall;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.RelativeLocationPath;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class FilterParentPath
extends Expression {
    private Expression _filterExpr;
    private Expression _path;
    private boolean _hasDescendantAxis = false;

    public FilterParentPath(Expression expression, Expression expression2) {
        this._path = expression2;
        this._path.setParent(this);
        this._filterExpr = expression;
        this._filterExpr.setParent(this);
    }

    public void setParser(Parser parser) {
        super.setParser(parser);
        this._filterExpr.setParser(parser);
        this._path.setParser(parser);
    }

    public String toString() {
        return "FilterParentPath(" + this._filterExpr + ", " + this._path + ')';
    }

    public void setDescendantAxis() {
        this._hasDescendantAxis = true;
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        Type type;
        Type type2 = this._filterExpr.typeCheck(symbolTable);
        if (!(type2 instanceof NodeSetType)) {
            if (type2 instanceof ReferenceType) {
                this._filterExpr = new CastExpr(this._filterExpr, Type.NodeSet);
            } else if (type2 instanceof NodeType) {
                this._filterExpr = new CastExpr(this._filterExpr, Type.NodeSet);
            } else {
                throw new TypeCheckError(this);
            }
        }
        if (!((type = this._path.typeCheck(symbolTable)) instanceof NodeSetType)) {
            this._path = new CastExpr(this._path, Type.NodeSet);
        }
        this._type = Type.NodeSet;
        return this._type;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        boolean bl;
        SyntaxTreeNode syntaxTreeNode;
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.StepIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;Lorg/apache/xml/dtm/DTMAxisIterator;)V");
        this._filterExpr.translate(classGenerator, methodGenerator);
        LocalVariableGen localVariableGen = methodGenerator.addLocalVariable("filter_parent_path_tmp1", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
        localVariableGen.setStart(instructionList.append(new ASTORE(localVariableGen.getIndex())));
        this._path.translate(classGenerator, methodGenerator);
        LocalVariableGen localVariableGen2 = methodGenerator.addLocalVariable("filter_parent_path_tmp2", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
        localVariableGen2.setStart(instructionList.append(new ASTORE(localVariableGen2.getIndex())));
        instructionList.append(new NEW(constantPoolGen.addClass("org.apache.xalan.xsltc.dom.StepIterator")));
        instructionList.append(DUP);
        localVariableGen.setEnd(instructionList.append(new ALOAD(localVariableGen.getIndex())));
        localVariableGen2.setEnd(instructionList.append(new ALOAD(localVariableGen2.getIndex())));
        instructionList.append(new INVOKESPECIAL(n2));
        if (this._hasDescendantAxis) {
            int n3 = constantPoolGen.addMethodref("org.apache.xml.dtm.ref.DTMAxisIteratorBase", "includeSelf", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
            instructionList.append(new INVOKEVIRTUAL(n3));
        }
        boolean bl2 = bl = (syntaxTreeNode = this.getParent()) instanceof RelativeLocationPath || syntaxTreeNode instanceof FilterParentPath || syntaxTreeNode instanceof KeyCall || syntaxTreeNode instanceof CurrentCall || syntaxTreeNode instanceof DocumentCall;
        if (!bl) {
            int n4 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "orderNodes", "(Lorg/apache/xml/dtm/DTMAxisIterator;I)Lorg/apache/xml/dtm/DTMAxisIterator;");
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(SWAP);
            instructionList.append(methodGenerator.loadContextNode());
            instructionList.append(new INVOKEINTERFACE(n4, 3));
        }
    }
}

