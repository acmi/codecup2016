/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FunctionCall;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.FilterGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class LangCall
extends FunctionCall {
    private Expression _lang;
    private Type _langType;

    public LangCall(QName qName, Vector vector) {
        super(qName, vector);
        this._lang = this.argument(0);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        this._langType = this._lang.typeCheck(symbolTable);
        if (!(this._langType instanceof StringType)) {
            this._lang = new CastExpr(this._lang, Type.String);
        }
        return Type.Boolean;
    }

    public Type getType() {
        return Type.Boolean;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "testLanguage", "(Ljava/lang/String;Lorg/apache/xalan/xsltc/DOM;I)Z");
        this._lang.translate(classGenerator, methodGenerator);
        instructionList.append(methodGenerator.loadDOM());
        if (classGenerator instanceof FilterGenerator) {
            instructionList.append(new ILOAD(1));
        } else {
            instructionList.append(methodGenerator.loadContextNode());
        }
        instructionList.append(new INVOKESTATIC(n2));
    }
}

