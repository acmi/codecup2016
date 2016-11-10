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
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class BinOpExpr
extends Expression {
    public static final int PLUS = 0;
    public static final int MINUS = 1;
    public static final int TIMES = 2;
    public static final int DIV = 3;
    public static final int MOD = 4;
    private static final String[] Ops = new String[]{"+", "-", "*", "/", "%"};
    private int _op;
    private Expression _left;
    private Expression _right;

    public BinOpExpr(int n2, Expression expression, Expression expression2) {
        this._op = n2;
        this._left = expression;
        this._left.setParent(this);
        this._right = expression2;
        this._right.setParent(this);
    }

    public boolean hasPositionCall() {
        if (this._left.hasPositionCall()) {
            return true;
        }
        if (this._right.hasPositionCall()) {
            return true;
        }
        return false;
    }

    public boolean hasLastCall() {
        return this._left.hasLastCall() || this._right.hasLastCall();
    }

    public void setParser(Parser parser) {
        super.setParser(parser);
        this._left.setParser(parser);
        this._right.setParser(parser);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        Type type;
        Type type2 = this._left.typeCheck(symbolTable);
        MethodType methodType = this.lookupPrimop(symbolTable, Ops[this._op], new MethodType(Type.Void, type2, type = this._right.typeCheck(symbolTable)));
        if (methodType != null) {
            Type type3;
            Type type4 = (Type)methodType.argsType().elementAt(0);
            if (!type4.identicalTo(type2)) {
                this._left = new CastExpr(this._left, type4);
            }
            if (!(type3 = (Type)methodType.argsType().elementAt(1)).identicalTo(type)) {
                this._right = new CastExpr(this._right, type4);
            }
            this._type = methodType.resultType();
            return this._type;
        }
        throw new TypeCheckError(this);
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        this._left.translate(classGenerator, methodGenerator);
        this._right.translate(classGenerator, methodGenerator);
        switch (this._op) {
            case 0: {
                instructionList.append(this._type.ADD());
                break;
            }
            case 1: {
                instructionList.append(this._type.SUB());
                break;
            }
            case 2: {
                instructionList.append(this._type.MUL());
                break;
            }
            case 3: {
                instructionList.append(this._type.DIV());
                break;
            }
            case 4: {
                instructionList.append(this._type.REM());
                break;
            }
            default: {
                ErrorMsg errorMsg = new ErrorMsg("ILLEGAL_BINARY_OP_ERR", this);
                this.getParser().reportError(3, errorMsg);
            }
        }
    }

    public String toString() {
        return Ops[this._op] + '(' + this._left + ", " + this._right + ')';
    }
}

