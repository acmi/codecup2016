/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO_W;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.VariableRefBase;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodType;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

abstract class Expression
extends SyntaxTreeNode {
    protected Type _type;
    protected FlowList _trueList = new FlowList();
    protected FlowList _falseList = new FlowList();

    Expression() {
    }

    public Type getType() {
        return this._type;
    }

    public abstract String toString();

    public boolean hasPositionCall() {
        return false;
    }

    public boolean hasLastCall() {
        return false;
    }

    public Object evaluateAtCompileTime() {
        return null;
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        return this.typeCheckContents(symbolTable);
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ErrorMsg errorMsg = new ErrorMsg("NOT_IMPLEMENTED_ERR", this.getClass(), this);
        this.getParser().reportError(2, errorMsg);
    }

    public final InstructionList compile(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        InstructionList instructionList2 = new InstructionList();
        methodGenerator.setInstructionList(instructionList2);
        this.translate(classGenerator, methodGenerator);
        methodGenerator.setInstructionList(instructionList);
        return instructionList2;
    }

    public void translateDesynthesized(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        this.translate(classGenerator, methodGenerator);
        if (this._type instanceof BooleanType) {
            this.desynthesize(classGenerator, methodGenerator);
        }
    }

    public void startIterator(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        if (!(this._type instanceof NodeSetType)) {
            return;
        }
        Expression expression = this;
        if (expression instanceof CastExpr) {
            expression = ((CastExpr)expression).getExpr();
        }
        if (!(expression instanceof VariableRefBase)) {
            InstructionList instructionList = methodGenerator.getInstructionList();
            instructionList.append(methodGenerator.loadContextNode());
            instructionList.append(methodGenerator.setStartNode());
        }
    }

    public void synthesize(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        this._trueList.backPatch(instructionList.append(ICONST_1));
        BranchHandle branchHandle = instructionList.append(new GOTO_W(null));
        this._falseList.backPatch(instructionList.append(ICONST_0));
        branchHandle.setTarget(instructionList.append(NOP));
    }

    public void desynthesize(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        this._falseList.add(instructionList.append(new IFEQ(null)));
    }

    public FlowList getFalseList() {
        return this._falseList;
    }

    public FlowList getTrueList() {
        return this._trueList;
    }

    public void backPatchFalseList(InstructionHandle instructionHandle) {
        this._falseList.backPatch(instructionHandle);
    }

    public void backPatchTrueList(InstructionHandle instructionHandle) {
        this._trueList.backPatch(instructionHandle);
    }

    public MethodType lookupPrimop(SymbolTable symbolTable, String string, MethodType methodType) {
        MethodType methodType2 = null;
        Vector vector = symbolTable.lookupPrimop(string);
        if (vector != null) {
            int n2 = vector.size();
            int n3 = Integer.MAX_VALUE;
            for (int i2 = 0; i2 < n2; ++i2) {
                int n4;
                MethodType methodType3 = (MethodType)vector.elementAt(i2);
                if (methodType3.argsCount() != methodType.argsCount()) continue;
                if (methodType2 == null) {
                    methodType2 = methodType3;
                }
                if ((n4 = methodType.distanceTo(methodType3)) >= n3) continue;
                n3 = n4;
                methodType2 = methodType3;
            }
        }
        return methodType2;
    }
}

