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

final class LastCall
extends FunctionCall {
    public LastCall(QName qName) {
        super(qName);
    }

    public boolean hasPositionCall() {
        return true;
    }

    public boolean hasLastCall() {
        return true;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (methodGenerator instanceof CompareGenerator) {
            instructionList.append(((CompareGenerator)methodGenerator).loadLastNode());
        } else if (methodGenerator instanceof TestGenerator) {
            instructionList.append(new ILOAD(3));
        } else {
            ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
            int n2 = constantPoolGen.addInterfaceMethodref("org.apache.xml.dtm.DTMAxisIterator", "getLast", "()I");
            instructionList.append(methodGenerator.loadIterator());
            instructionList.append(new INVOKEINTERFACE(n2, 1));
        }
    }
}
