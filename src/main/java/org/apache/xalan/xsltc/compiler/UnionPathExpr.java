/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.Step;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xml.dtm.Axis;

final class UnionPathExpr
extends Expression {
    private final Expression _pathExpr;
    private final Expression _rest;
    private boolean _reverse = false;
    private Expression[] _components;

    public UnionPathExpr(Expression expression, Expression expression2) {
        this._pathExpr = expression;
        this._rest = expression2;
    }

    public void setParser(Parser parser) {
        super.setParser(parser);
        Vector vector = new Vector();
        this.flatten(vector);
        int n2 = vector.size();
        this._components = vector.toArray(new Expression[n2]);
        for (int i2 = 0; i2 < n2; ++i2) {
            this._components[i2].setParser(parser);
            this._components[i2].setParent(this);
            if (!(this._components[i2] instanceof Step)) continue;
            Step step = (Step)this._components[i2];
            int n3 = step.getAxis();
            int n4 = step.getNodeType();
            if (n3 == 2 || n4 == 2) {
                this._components[i2] = this._components[0];
                this._components[0] = step;
            }
            if (!Axis.isReverse(n3)) continue;
            this._reverse = true;
        }
        if (this.getParent() instanceof Expression) {
            this._reverse = false;
        }
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        int n2 = this._components.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (this._components[i2].typeCheck(symbolTable) == Type.NodeSet) continue;
            this._components[i2] = new CastExpr(this._components[i2], Type.NodeSet);
        }
        this._type = Type.NodeSet;
        return this._type;
    }

    public String toString() {
        return "union(" + this._pathExpr + ", " + this._rest + ')';
    }

    private void flatten(Vector vector) {
        vector.addElement(this._pathExpr);
        if (this._rest != null) {
            if (this._rest instanceof UnionPathExpr) {
                ((UnionPathExpr)this._rest).flatten(vector);
            } else {
                vector.addElement(this._rest);
            }
        }
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        int n2;
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n3 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.UnionIterator", "<init>", "(Lorg/apache/xalan/xsltc/DOM;)V");
        int n4 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.UnionIterator", "addIterator", "(Lorg/apache/xml/dtm/DTMAxisIterator;)Lorg/apache/xalan/xsltc/dom/UnionIterator;");
        instructionList.append(new NEW(constantPoolGen.addClass("org.apache.xalan.xsltc.dom.UnionIterator")));
        instructionList.append(DUP);
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(new INVOKESPECIAL(n3));
        int n5 = this._components.length;
        for (n2 = 0; n2 < n5; ++n2) {
            this._components[n2].translate(classGenerator, methodGenerator);
            instructionList.append(new INVOKEVIRTUAL(n4));
        }
        if (this._reverse) {
            n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "orderNodes", "(Lorg/apache/xml/dtm/DTMAxisIterator;I)Lorg/apache/xml/dtm/DTMAxisIterator;");
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(SWAP);
            instructionList.append(methodGenerator.loadContextNode());
            instructionList.append(new INVOKEINTERFACE(n2, 3));
        }
    }
}

