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

final class LiteralExpr
extends Expression {
    private final String _value;
    private final String _namespace;

    public LiteralExpr(String string) {
        this._value = string;
        this._namespace = null;
    }

    public LiteralExpr(String string, String string2) {
        this._value = string;
        this._namespace = string2.equals("") ? null : string2;
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        this._type = Type.String;
        return this._type;
    }

    public String toString() {
        return "literal-expr(" + this._value + ')';
    }

    protected boolean contextDependent() {
        return false;
    }

    protected String getValue() {
        return this._value;
    }

    protected String getNamespace() {
        return this._namespace;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(new PUSH(constantPoolGen, this._value));
    }
}

