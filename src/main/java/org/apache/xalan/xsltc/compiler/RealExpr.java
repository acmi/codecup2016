/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class RealExpr
extends Expression {
    private double _value;

    public RealExpr(double d2) {
        this._value = d2;
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        this._type = Type.Real;
        return this._type;
    }

    public String toString() {
        return "real-expr(" + this._value + ')';
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(new PUSH(constantPoolGen, this._value));
    }
}
