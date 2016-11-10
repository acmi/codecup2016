/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
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

final class StringCall
extends FunctionCall {
    public StringCall(QName qName, Vector vector) {
        super(qName, vector);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        int n2 = this.argumentCount();
        if (n2 > 1) {
            ErrorMsg errorMsg = new ErrorMsg("ILLEGAL_ARG_ERR", this);
            throw new TypeCheckError(errorMsg);
        }
        if (n2 > 0) {
            this.argument().typeCheck(symbolTable);
        }
        this._type = Type.String;
        return this._type;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Type type;
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (this.argumentCount() == 0) {
            instructionList.append(methodGenerator.loadContextNode());
            type = Type.Node;
        } else {
            Expression expression = this.argument();
            expression.translate(classGenerator, methodGenerator);
            expression.startIterator(classGenerator, methodGenerator);
            type = expression.getType();
        }
        if (!type.identicalTo(Type.String)) {
            type.translateTo(classGenerator, methodGenerator, Type.String);
        }
    }
}

