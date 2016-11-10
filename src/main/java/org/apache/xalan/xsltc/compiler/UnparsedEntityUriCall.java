/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FunctionCall;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class UnparsedEntityUriCall
extends FunctionCall {
    private Expression _entity;

    public UnparsedEntityUriCall(QName qName, Vector vector) {
        super(qName, vector);
        this._entity = this.argument();
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        Type type = this._entity.typeCheck(symbolTable);
        if (!(type instanceof StringType)) {
            this._entity = new CastExpr(this._entity, Type.String);
        }
        this._type = Type.String;
        return this._type;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(methodGenerator.loadDOM());
        this._entity.translate(classGenerator, methodGenerator);
        instructionList.append(new INVOKEINTERFACE(constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getUnparsedEntityURI", "(Ljava/lang/String;)Ljava/lang/String;"), 2));
    }
}

