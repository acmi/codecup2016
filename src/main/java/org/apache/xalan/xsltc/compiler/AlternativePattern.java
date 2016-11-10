/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.Pattern;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class AlternativePattern
extends Pattern {
    private final Pattern _left;
    private final Pattern _right;

    public AlternativePattern(Pattern pattern, Pattern pattern2) {
        this._left = pattern;
        this._right = pattern2;
    }

    public void setParser(Parser parser) {
        super.setParser(parser);
        this._left.setParser(parser);
        this._right.setParser(parser);
    }

    public Pattern getLeft() {
        return this._left;
    }

    public Pattern getRight() {
        return this._right;
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        this._left.typeCheck(symbolTable);
        this._right.typeCheck(symbolTable);
        return null;
    }

    public double getPriority() {
        double d2;
        double d3 = this._left.getPriority();
        if (d3 < (d2 = this._right.getPriority())) {
            return d3;
        }
        return d2;
    }

    public String toString() {
        return "alternative(" + this._left + ", " + this._right + ')';
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        this._left.translate(classGenerator, methodGenerator);
        BranchHandle branchHandle = instructionList.append(new GOTO(null));
        instructionList.append(methodGenerator.loadContextNode());
        this._right.translate(classGenerator, methodGenerator);
        this._left._trueList.backPatch(branchHandle);
        this._left._falseList.backPatch(branchHandle.getNext());
        this._trueList.append(this._right._trueList.add(branchHandle));
        this._falseList.append(this._right._falseList);
    }
}

