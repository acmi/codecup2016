/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.FunctionCall;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.CompareGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.TestGenerator;

final class PositionCall
extends FunctionCall {
    public PositionCall(QName qName) {
        super(qName);
    }

    public boolean hasPositionCall() {
        return true;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (methodGenerator instanceof CompareGenerator) {
            instructionList.append(((CompareGenerator)methodGenerator).loadCurrentNode());
        } else if (methodGenerator instanceof TestGenerator) {
            instructionList.append(new ILOAD(2));
        } else {
            ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
            int n2 = constantPoolGen.addInterfaceMethodref("org.apache.xml.dtm.DTMAxisIterator", "getPosition", "()I");
            instructionList.append(methodGenerator.loadIterator());
            instructionList.append(new INVOKEINTERFACE(n2, 1));
        }
    }
}

