/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FunctionCall;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class BooleanCall
extends FunctionCall {
    private Expression _arg = null;

    public BooleanCall(QName qName, Vector vector) {
        super(qName, vector);
        this._arg = this.argument(0);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        this._arg.typeCheck(symbolTable);
        this._type = Type.Boolean;
        return this._type;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        this._arg.translate(classGenerator, methodGenerator);
        Type type = this._arg.getType();
        if (!type.identicalTo(Type.Boolean)) {
            this._arg.startIterator(classGenerator, methodGenerator);
            type.translateTo(classGenerator, methodGenerator, Type.Boolean);
        }
    }
}

