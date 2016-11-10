/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.Text;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class Comment
extends Instruction {
    Comment() {
    }

    public void parseContents(Parser parser) {
        this.parseChildren(parser);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        this.typeCheckContents(symbolTable);
        return Type.String;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Object object;
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        Text text = null;
        if (this.elementCount() == 1 && (object = this.elementAt(0)) instanceof Text) {
            text = (Text)object;
        }
        if (text != null) {
            instructionList.append(methodGenerator.loadHandler());
            if (text.canLoadAsArrayOffsetLength()) {
                text.loadAsArrayOffsetLength(classGenerator, methodGenerator);
                int n2 = constantPoolGen.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE, "comment", "([CII)V");
                instructionList.append(new INVOKEINTERFACE(n2, 4));
            } else {
                instructionList.append(new PUSH(constantPoolGen, text.getText()));
                int n3 = constantPoolGen.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE, "comment", "(Ljava/lang/String;)V");
                instructionList.append(new INVOKEINTERFACE(n3, 2));
            }
        } else {
            instructionList.append(methodGenerator.loadHandler());
            instructionList.append(DUP);
            instructionList.append(classGenerator.loadTranslet());
            instructionList.append(new GETFIELD(constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "stringValueHandler", "Lorg/apache/xalan/xsltc/runtime/StringValueHandler;")));
            instructionList.append(DUP);
            instructionList.append(methodGenerator.storeHandler());
            this.translateContents(classGenerator, methodGenerator);
            instructionList.append(new INVOKEVIRTUAL(constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.StringValueHandler", "getValue", "()Ljava/lang/String;")));
            int n4 = constantPoolGen.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE, "comment", "(Ljava/lang/String;)V");
            instructionList.append(new INVOKEINTERFACE(n4, 2));
            instructionList.append(methodGenerator.storeHandler());
        }
    }
}

