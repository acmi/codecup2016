/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFNE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.IdPattern;
import org.apache.xalan.xsltc.compiler.LocationPathPattern;
import org.apache.xalan.xsltc.compiler.RelativePathPattern;
import org.apache.xalan.xsltc.compiler.StepPattern;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

abstract class IdKeyPattern
extends LocationPathPattern {
    protected RelativePathPattern _left = null;
    private String _index = null;
    private String _value = null;

    public IdKeyPattern(String string, String string2) {
        this._index = string;
        this._value = string2;
    }

    public String getIndexName() {
        return this._index;
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        return Type.NodeSet;
    }

    public boolean isWildcard() {
        return false;
    }

    public void setLeft(RelativePathPattern relativePathPattern) {
        this._left = relativePathPattern;
    }

    public StepPattern getKernelPattern() {
        return null;
    }

    public void reduceKernelPattern() {
    }

    public String toString() {
        return "id/keyPattern(" + this._index + ", " + this._value + ')';
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "getKeyIndex", "(Ljava/lang/String;)Lorg/apache/xalan/xsltc/dom/KeyIndex;");
        int n3 = constantPoolGen.addMethodref("org/apache/xalan/xsltc/dom/KeyIndex", "containsID", "(ILjava/lang/Object;)I");
        int n4 = constantPoolGen.addMethodref("org/apache/xalan/xsltc/dom/KeyIndex", "containsKey", "(ILjava/lang/Object;)I");
        int n5 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNodeIdent", "(I)I");
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(new PUSH(constantPoolGen, this._index));
        instructionList.append(new INVOKEVIRTUAL(n2));
        instructionList.append(SWAP);
        instructionList.append(new PUSH(constantPoolGen, this._value));
        if (this instanceof IdPattern) {
            instructionList.append(new INVOKEVIRTUAL(n3));
        } else {
            instructionList.append(new INVOKEVIRTUAL(n4));
        }
        this._trueList.add(instructionList.append(new IFNE(null)));
        this._falseList.add(instructionList.append(new GOTO(null)));
    }
}

