/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ArithmeticInstruction;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.FunctionCall;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;

final class NotCall
extends FunctionCall {
    public NotCall(QName qName, Vector vector) {
        super(qName, vector);
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        this.argument().translate(classGenerator, methodGenerator);
        instructionList.append(ICONST_1);
        instructionList.append(IXOR);
    }

    public void translateDesynthesized(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        Expression expression = this.argument();
        expression.translateDesynthesized(classGenerator, methodGenerator);
        BranchHandle branchHandle = instructionList.append(new GOTO(null));
        this._trueList = expression._falseList;
        this._falseList = expression._trueList;
        this._falseList.add(branchHandle);
    }
}

