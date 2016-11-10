/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IF_ICMPEQ;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.Predicate;
import org.apache.xalan.xsltc.compiler.StepPattern;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class ProcessingInstructionPattern
extends StepPattern {
    private String _name = null;
    private boolean _typeChecked = false;

    public ProcessingInstructionPattern(String string) {
        super(3, 7, null);
        this._name = string;
    }

    public double getDefaultPriority() {
        return this._name != null ? 0.0 : -0.5;
    }

    public String toString() {
        if (this._predicates == null) {
            return "processing-instruction(" + this._name + ")";
        }
        return "processing-instruction(" + this._name + ")" + this._predicates;
    }

    public void reduceKernelPattern() {
        this._typeChecked = true;
    }

    public boolean isWildcard() {
        return false;
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (this.hasPredicates()) {
            int n2 = this._predicates.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Predicate predicate = (Predicate)this._predicates.elementAt(i2);
                predicate.typeCheck(symbolTable);
            }
        }
        return Type.NodeSet;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        int n2;
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n3 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNodeName", "(I)Ljava/lang/String;");
        int n4 = constantPoolGen.addMethodref("java.lang.String", "equals", "(Ljava/lang/Object;)Z");
        instructionList.append(methodGenerator.loadCurrentNode());
        instructionList.append(SWAP);
        instructionList.append(methodGenerator.storeCurrentNode());
        if (!this._typeChecked) {
            instructionList.append(methodGenerator.loadCurrentNode());
            n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getExpandedTypeID", "(I)I");
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(methodGenerator.loadCurrentNode());
            instructionList.append(new INVOKEINTERFACE(n2, 2));
            instructionList.append(new PUSH(constantPoolGen, 7));
            this._falseList.add(instructionList.append(new IF_ICMPEQ(null)));
        }
        instructionList.append(new PUSH(constantPoolGen, this._name));
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(methodGenerator.loadCurrentNode());
        instructionList.append(new INVOKEINTERFACE(n3, 2));
        instructionList.append(new INVOKEVIRTUAL(n4));
        this._falseList.add(instructionList.append(new IFEQ(null)));
        if (this.hasPredicates()) {
            n2 = this._predicates.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Predicate predicate = (Predicate)this._predicates.elementAt(i2);
                Expression expression = predicate.getExpr();
                expression.translateDesynthesized(classGenerator, methodGenerator);
                this._trueList.append(expression._trueList);
                this._falseList.append(expression._falseList);
            }
        }
        InstructionHandle instructionHandle = instructionList.append(methodGenerator.storeCurrentNode());
        this.backPatchTrueList(instructionHandle);
        BranchHandle branchHandle = instructionList.append(new GOTO(null));
        instructionHandle = instructionList.append(methodGenerator.storeCurrentNode());
        this.backPatchFalseList(instructionHandle);
        this._falseList.add(instructionList.append(new GOTO(null)));
        branchHandle.setTarget(instructionList.append(NOP));
    }
}

