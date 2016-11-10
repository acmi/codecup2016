/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FunctionCall;
import org.apache.xalan.xsltc.compiler.LiteralExpr;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.ObjectType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class CastCall
extends FunctionCall {
    private String _className;
    private Expression _right;

    public CastCall(QName qName, Vector vector) {
        super(qName, vector);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (this.argumentCount() != 2) {
            throw new TypeCheckError(new ErrorMsg("ILLEGAL_ARG_ERR", (Object)this.getName(), this));
        }
        Expression expression = this.argument(0);
        if (!(expression instanceof LiteralExpr)) {
            throw new TypeCheckError(new ErrorMsg("NEED_LITERAL_ERR", (Object)this.getName(), this));
        }
        this._className = ((LiteralExpr)expression).getValue();
        this._type = Type.newObjectType(this._className);
        this._right = this.argument(1);
        Type type = this._right.typeCheck(symbolTable);
        if (type != Type.Reference && !(type instanceof ObjectType)) {
            throw new TypeCheckError(new ErrorMsg("DATA_CONVERSION_ERR", type, this._type, this));
        }
        return this._type;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        this._right.translate(classGenerator, methodGenerator);
        instructionList.append(new CHECKCAST(constantPoolGen.addClass(this._className)));
    }
}

