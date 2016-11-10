/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class Fallback
extends Instruction {
    private boolean _active = false;

    Fallback() {
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (this._active) {
            return this.typeCheckContents(symbolTable);
        }
        return Type.Void;
    }

    public void activate() {
        this._active = true;
    }

    public String toString() {
        return "fallback";
    }

    public void parseContents(Parser parser) {
        if (this._active) {
            this.parseChildren(parser);
        }
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (this._active) {
            this.translateContents(classGenerator, methodGenerator);
        }
    }
}

