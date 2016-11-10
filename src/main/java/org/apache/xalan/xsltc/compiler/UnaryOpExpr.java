/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class UnaryOpExpr
extends Expression {
    private Expression _left;

    public UnaryOpExpr(Expression expression) {
        this._left = expression;
        this._left.setParent(this);
    }

    public boolean hasPositionCall() {
        return this._left.hasPositionCall();
    }

    public boolean hasLastCall() {
        return this._left.hasLastCall();
    }

    public void setParser(Parser parser) {
        super.setParser(parser);
        this._left.setParser(parser);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        Type type = this._left.typeCheck(symbolTable);
        MethodType methodType = this.lookupPrimop(symbolTable, "u-", new MethodType(Type.Void, type));
        if (methodType != null) {
            Type type2 = (Type)methodType.argsType().elementAt(0);
            if (!type2.identicalTo(type)) {
                this._left = new CastExpr(this._left, type2);
            }
            this._type = methodType.resultType();
            return this._type;
        }
        throw new TypeCheckError(this);
    }

    public String toString() {
        return "u-(" + this._left + ')';
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        this._left.translate(classGenerator, methodGenerator);
        instructionList.append(this._type.NEG());
    }
}

