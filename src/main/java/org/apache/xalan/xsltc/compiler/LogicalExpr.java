/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.NotCall;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class LogicalExpr
extends Expression {
    public static final int OR = 0;
    public static final int AND = 1;
    private final int _op;
    private Expression _left;
    private Expression _right;
    private static final String[] Ops = new String[]{"or", "and"};

    public LogicalExpr(int n2, Expression expression, Expression expression2) {
        this._op = n2;
        this._left = expression;
        this._left.setParent(this);
        this._right = expression2;
        this._right.setParent(this);
    }

    public boolean hasPositionCall() {
        return this._left.hasPositionCall() || this._right.hasPositionCall();
    }

    public boolean hasLastCall() {
        return this._left.hasLastCall() || this._right.hasLastCall();
    }

    public Object evaluateAtCompileTime() {
        Object object = this._left.evaluateAtCompileTime();
        Object object2 = this._right.evaluateAtCompileTime();
        if (object == null || object2 == null) {
            return null;
        }
        if (this._op == 1) {
            return object == Boolean.TRUE && object2 == Boolean.TRUE ? Boolean.TRUE : Boolean.FALSE;
        }
        return object == Boolean.TRUE || object2 == Boolean.TRUE ? Boolean.TRUE : Boolean.FALSE;
    }

    public int getOp() {
        return this._op;
    }

    public void setParser(Parser parser) {
        super.setParser(parser);
        this._left.setParser(parser);
        this._right.setParser(parser);
    }

    public String toString() {
        return Ops[this._op] + '(' + this._left + ", " + this._right + ')';
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        Type type;
        Type type2 = this._left.typeCheck(symbolTable);
        MethodType methodType = new MethodType(Type.Void, type2, type = this._right.typeCheck(symbolTable));
        MethodType methodType2 = this.lookupPrimop(symbolTable, Ops[this._op], methodType);
        if (methodType2 != null) {
            Type type3;
            Type type4 = (Type)methodType2.argsType().elementAt(0);
            if (!type4.identicalTo(type2)) {
                this._left = new CastExpr(this._left, type4);
            }
            if (!(type3 = (Type)methodType2.argsType().elementAt(1)).identicalTo(type)) {
                this._right = new CastExpr(this._right, type4);
            }
            this._type = methodType2.resultType();
            return this._type;
        }
        throw new TypeCheckError(this);
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        this.translateDesynthesized(classGenerator, methodGenerator);
        this.synthesize(classGenerator, methodGenerator);
    }

    public void translateDesynthesized(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        SyntaxTreeNode syntaxTreeNode = this.getParent();
        if (this._op == 1) {
            this._left.translateDesynthesized(classGenerator, methodGenerator);
            InstructionHandle instructionHandle = instructionList.append(NOP);
            this._right.translateDesynthesized(classGenerator, methodGenerator);
            InstructionHandle instructionHandle2 = instructionList.append(NOP);
            this._falseList.append(this._right._falseList.append(this._left._falseList));
            if (this._left instanceof LogicalExpr && ((LogicalExpr)this._left).getOp() == 0) {
                this._left.backPatchTrueList(instructionHandle);
            } else if (this._left instanceof NotCall) {
                this._left.backPatchTrueList(instructionHandle);
            } else {
                this._trueList.append(this._left._trueList);
            }
            if (this._right instanceof LogicalExpr && ((LogicalExpr)this._right).getOp() == 0) {
                this._right.backPatchTrueList(instructionHandle2);
            } else if (this._right instanceof NotCall) {
                this._right.backPatchTrueList(instructionHandle2);
            } else {
                this._trueList.append(this._right._trueList);
            }
        } else {
            this._left.translateDesynthesized(classGenerator, methodGenerator);
            BranchHandle branchHandle = instructionList.append(new GOTO(null));
            this._right.translateDesynthesized(classGenerator, methodGenerator);
            this._left._trueList.backPatch(branchHandle);
            this._left._falseList.backPatch(branchHandle.getNext());
            this._falseList.append(this._right._falseList);
            this._trueList.add(branchHandle).append(this._right._trueList);
        }
    }
}

