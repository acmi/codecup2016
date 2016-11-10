/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFLT;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.Pattern;
import org.apache.xalan.xsltc.compiler.RelativePathPattern;
import org.apache.xalan.xsltc.compiler.StepPattern;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.TopLevelElement;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class AncestorPattern
extends RelativePathPattern {
    private final Pattern _left;
    private final RelativePathPattern _right;
    private InstructionHandle _loop;

    public AncestorPattern(RelativePathPattern relativePathPattern) {
        this(null, relativePathPattern);
    }

    public AncestorPattern(Pattern pattern, RelativePathPattern relativePathPattern) {
        this._left = pattern;
        this._right = relativePathPattern;
        this._right.setParent(this);
        if (pattern != null) {
            pattern.setParent(this);
        }
    }

    public InstructionHandle getLoopHandle() {
        return this._loop;
    }

    public void setParser(Parser parser) {
        super.setParser(parser);
        if (this._left != null) {
            this._left.setParser(parser);
        }
        this._right.setParser(parser);
    }

    public boolean isWildcard() {
        return false;
    }

    public StepPattern getKernelPattern() {
        return this._right.getKernelPattern();
    }

    public void reduceKernelPattern() {
        this._right.reduceKernelPattern();
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (this._left != null) {
            this._left.typeCheck(symbolTable);
        }
        return this._right.typeCheck(symbolTable);
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        LocalVariableGen localVariableGen = methodGenerator.addLocalVariable2("app", Util.getJCRefType("I"), instructionList.getEnd());
        ILOAD iLOAD = new ILOAD(localVariableGen.getIndex());
        ISTORE iSTORE = new ISTORE(localVariableGen.getIndex());
        if (this._right instanceof StepPattern) {
            instructionList.append(DUP);
            instructionList.append(iSTORE);
            this._right.translate(classGenerator, methodGenerator);
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(iLOAD);
        } else {
            this._right.translate(classGenerator, methodGenerator);
            if (this._right instanceof AncestorPattern) {
                instructionList.append(methodGenerator.loadDOM());
                instructionList.append(SWAP);
            }
        }
        if (this._left != null) {
            int n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getParent", "(I)I");
            InstructionHandle instructionHandle = instructionList.append(new INVOKEINTERFACE(n2, 2));
            instructionList.append(DUP);
            instructionList.append(iSTORE);
            this._falseList.add(instructionList.append(new IFLT(null)));
            instructionList.append(iLOAD);
            this._left.translate(classGenerator, methodGenerator);
            SyntaxTreeNode syntaxTreeNode = this.getParent();
            if (syntaxTreeNode != null && !(syntaxTreeNode instanceof Instruction) && !(syntaxTreeNode instanceof TopLevelElement)) {
                instructionList.append(iLOAD);
            }
            BranchHandle branchHandle = instructionList.append(new GOTO(null));
            this._loop = instructionList.append(methodGenerator.loadDOM());
            instructionList.append(iLOAD);
            localVariableGen.setEnd(this._loop);
            instructionList.append(new GOTO(instructionHandle));
            branchHandle.setTarget(instructionList.append(NOP));
            this._left.backPatchFalseList(this._loop);
            this._trueList.append(this._left._trueList);
        } else {
            instructionList.append(POP2);
        }
        if (this._right instanceof AncestorPattern) {
            AncestorPattern ancestorPattern = (AncestorPattern)this._right;
            this._falseList.backPatch(ancestorPattern.getLoopHandle());
        }
        this._trueList.append(this._right._trueList);
        this._falseList.append(this._right._falseList);
    }

    public String toString() {
        return "AncestorPattern(" + this._left + ", " + this._right + ')';
    }
}

