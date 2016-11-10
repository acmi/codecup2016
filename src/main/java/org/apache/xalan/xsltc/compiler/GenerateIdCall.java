/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FunctionCall;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;

final class GenerateIdCall
extends FunctionCall {
    public GenerateIdCall(QName qName, Vector vector) {
        super(qName, vector);
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (this.argumentCount() == 0) {
            instructionList.append(methodGenerator.loadContextNode());
        } else {
            this.argument().translate(classGenerator, methodGenerator);
        }
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        instructionList.append(new INVOKESTATIC(constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "generate_idF", "(I)Ljava/lang/String;")));
    }
}

