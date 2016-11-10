/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FunctionCall;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class DocumentCall
extends FunctionCall {
    private Expression _arg1 = null;
    private Expression _arg2 = null;
    private Type _arg1Type;

    public DocumentCall(QName qName, Vector vector) {
        super(qName, vector);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        int n2 = this.argumentCount();
        if (n2 < 1 || n2 > 2) {
            ErrorMsg errorMsg = new ErrorMsg("ILLEGAL_ARG_ERR", this);
            throw new TypeCheckError(errorMsg);
        }
        if (this.getStylesheet() == null) {
            ErrorMsg errorMsg = new ErrorMsg("ILLEGAL_ARG_ERR", this);
            throw new TypeCheckError(errorMsg);
        }
        this._arg1 = this.argument(0);
        if (this._arg1 == null) {
            ErrorMsg errorMsg = new ErrorMsg("DOCUMENT_ARG_ERR", this);
            throw new TypeCheckError(errorMsg);
        }
        this._arg1Type = this._arg1.typeCheck(symbolTable);
        if (this._arg1Type != Type.NodeSet && this._arg1Type != Type.String) {
            this._arg1 = new CastExpr(this._arg1, Type.String);
        }
        if (n2 == 2) {
            this._arg2 = this.argument(1);
            if (this._arg2 == null) {
                ErrorMsg errorMsg = new ErrorMsg("DOCUMENT_ARG_ERR", this);
                throw new TypeCheckError(errorMsg);
            }
            Type type = this._arg2.typeCheck(symbolTable);
            if (type.identicalTo(Type.Node)) {
                this._arg2 = new CastExpr(this._arg2, Type.NodeSet);
            } else if (!type.identicalTo(Type.NodeSet)) {
                ErrorMsg errorMsg = new ErrorMsg("DOCUMENT_ARG_ERR", this);
                throw new TypeCheckError(errorMsg);
            }
        }
        this._type = Type.NodeSet;
        return this._type;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n2 = this.argumentCount();
        int n3 = constantPoolGen.addFieldref(classGenerator.getClassName(), "_dom", "Lorg/apache/xalan/xsltc/DOM;");
        String string = null;
        string = n2 == 1 ? "(Ljava/lang/Object;Ljava/lang/String;Lorg/apache/xalan/xsltc/runtime/AbstractTranslet;Lorg/apache/xalan/xsltc/DOM;)Lorg/apache/xml/dtm/DTMAxisIterator;" : "(Ljava/lang/Object;Lorg/apache/xml/dtm/DTMAxisIterator;Ljava/lang/String;Lorg/apache/xalan/xsltc/runtime/AbstractTranslet;Lorg/apache/xalan/xsltc/DOM;)Lorg/apache/xml/dtm/DTMAxisIterator;";
        int n4 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.LoadDocument", "documentF", string);
        this._arg1.translate(classGenerator, methodGenerator);
        if (this._arg1Type == Type.NodeSet) {
            this._arg1.startIterator(classGenerator, methodGenerator);
        }
        if (n2 == 2) {
            this._arg2.translate(classGenerator, methodGenerator);
            this._arg2.startIterator(classGenerator, methodGenerator);
        }
        instructionList.append(new PUSH(constantPoolGen, this.getStylesheet().getSystemId()));
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(DUP);
        instructionList.append(new GETFIELD(n3));
        instructionList.append(new INVOKESTATIC(n4));
    }
}

