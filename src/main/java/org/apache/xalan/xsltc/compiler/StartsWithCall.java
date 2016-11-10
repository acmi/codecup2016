/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FunctionCall;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class StartsWithCall
extends FunctionCall {
    private Expression _base = null;
    private Expression _token = null;

    public StartsWithCall(QName qName, Vector vector) {
        super(qName, vector);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (this.argumentCount() != 2) {
            ErrorMsg errorMsg = new ErrorMsg("ILLEGAL_ARG_ERR", (Object)this.getName(), this);
            throw new TypeCheckError(errorMsg);
        }
        this._base = this.argument(0);
        Type type = this._base.typeCheck(symbolTable);
        if (type != Type.String) {
            this._base = new CastExpr(this._base, Type.String);
        }
        this._token = this.argument(1);
        Type type2 = this._token.typeCheck(symbolTable);
        if (type2 != Type.String) {
            this._token = new CastExpr(this._token, Type.String);
        }
        this._type = Type.Boolean;
        return this._type;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        this._base.translate(classGenerator, methodGenerator);
        this._token.translate(classGenerator, methodGenerator);
        instructionList.append(new INVOKEVIRTUAL(constantPoolGen.addMethodref("java.lang.String", "startsWith", "(Ljava/lang/String;)Z")));
    }
}

