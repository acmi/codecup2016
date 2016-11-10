/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.AncestorPattern;
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

final class ParentPattern
extends RelativePathPattern {
    private final Pattern _left;
    private final RelativePathPattern _right;

    public ParentPattern(Pattern pattern, RelativePathPattern relativePathPattern) {
        this._left = pattern;
        this._left.setParent(this);
        this._right = relativePathPattern;
        this._right.setParent(this);
    }

    public void setParser(Parser parser) {
        super.setParser(parser);
        this._left.setParser(parser);
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
        this._left.typeCheck(symbolTable);
        return this._right.typeCheck(symbolTable);
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Object object;
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        LocalVariableGen localVariableGen = methodGenerator.addLocalVariable2("ppt", Util.getJCRefType("I"), null);
        ILOAD iLOAD = new ILOAD(localVariableGen.getIndex());
        ISTORE iSTORE = new ISTORE(localVariableGen.getIndex());
        if (this._right.isWildcard()) {
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(SWAP);
        } else if (this._right instanceof StepPattern) {
            instructionList.append(DUP);
            localVariableGen.setStart(instructionList.append(iSTORE));
            this._right.translate(classGenerator, methodGenerator);
            instructionList.append(methodGenerator.loadDOM());
            localVariableGen.setEnd(instructionList.append(iLOAD));
        } else {
            this._right.translate(classGenerator, methodGenerator);
            if (this._right instanceof AncestorPattern) {
                instructionList.append(methodGenerator.loadDOM());
                instructionList.append(SWAP);
            }
        }
        int n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getParent", "(I)I");
        instructionList.append(new INVOKEINTERFACE(n2, 2));
        SyntaxTreeNode syntaxTreeNode = this.getParent();
        if (syntaxTreeNode == null || syntaxTreeNode instanceof Instruction || syntaxTreeNode instanceof TopLevelElement) {
            this._left.translate(classGenerator, methodGenerator);
        } else {
            instructionList.append(DUP);
            object = instructionList.append(iSTORE);
            if (localVariableGen.getStart() == null) {
                localVariableGen.setStart((InstructionHandle)object);
            }
            this._left.translate(classGenerator, methodGenerator);
            instructionList.append(methodGenerator.loadDOM());
            localVariableGen.setEnd(instructionList.append(iLOAD));
        }
        methodGenerator.removeLocalVariable(localVariableGen);
        if (this._right instanceof AncestorPattern) {
            object = (AncestorPattern)this._right;
            this._left.backPatchFalseList(object.getLoopHandle());
        }
        this._trueList.append(this._right._trueList.append(this._left._trueList));
        this._falseList.append(this._right._falseList.append(this._left._falseList));
    }

    public String toString() {
        return "Parent(" + this._left + ", " + this._right + ')';
    }
}

