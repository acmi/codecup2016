/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO_W;
import org.apache.bcel.generic.IF_ICMPEQ;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.AncestorPattern;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.LocationPathPattern;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.RelativePathPattern;
import org.apache.xalan.xsltc.compiler.StepPattern;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class AbsolutePathPattern
extends LocationPathPattern {
    private final RelativePathPattern _left;

    public AbsolutePathPattern(RelativePathPattern relativePathPattern) {
        this._left = relativePathPattern;
        if (relativePathPattern != null) {
            relativePathPattern.setParent(this);
        }
    }

    public void setParser(Parser parser) {
        super.setParser(parser);
        if (this._left != null) {
            this._left.setParser(parser);
        }
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        return this._left == null ? Type.Root : this._left.typeCheck(symbolTable);
    }

    public boolean isWildcard() {
        return false;
    }

    public StepPattern getKernelPattern() {
        return this._left != null ? this._left.getKernelPattern() : null;
    }

    public void reduceKernelPattern() {
        this._left.reduceKernelPattern();
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (this._left != null) {
            if (this._left instanceof StepPattern) {
                LocalVariableGen localVariableGen = methodGenerator.addLocalVariable2("apptmp", Util.getJCRefType("I"), null);
                instructionList.append(DUP);
                localVariableGen.setStart(instructionList.append(new ISTORE(localVariableGen.getIndex())));
                this._left.translate(classGenerator, methodGenerator);
                instructionList.append(methodGenerator.loadDOM());
                localVariableGen.setEnd(instructionList.append(new ILOAD(localVariableGen.getIndex())));
                methodGenerator.removeLocalVariable(localVariableGen);
            } else {
                this._left.translate(classGenerator, methodGenerator);
            }
        }
        int n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getParent", "(I)I");
        int n3 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getExpandedTypeID", "(I)I");
        InstructionHandle instructionHandle = instructionList.append(methodGenerator.loadDOM());
        instructionList.append(SWAP);
        instructionList.append(new INVOKEINTERFACE(n2, 2));
        if (this._left instanceof AncestorPattern) {
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(SWAP);
        }
        instructionList.append(new INVOKEINTERFACE(n3, 2));
        instructionList.append(new PUSH(constantPoolGen, 9));
        BranchHandle branchHandle = instructionList.append(new IF_ICMPEQ(null));
        this._falseList.add(instructionList.append(new GOTO_W(null)));
        branchHandle.setTarget(instructionList.append(NOP));
        if (this._left != null) {
            this._left.backPatchTrueList(instructionHandle);
            if (this._left instanceof AncestorPattern) {
                AncestorPattern ancestorPattern = (AncestorPattern)this._left;
                this._falseList.backPatch(ancestorPattern.getLoopHandle());
            }
            this._falseList.append(this._left._falseList);
        }
    }

    public String toString() {
        return "absolutePathPattern(" + (this._left != null ? this._left.toString() : ")");
    }
}

