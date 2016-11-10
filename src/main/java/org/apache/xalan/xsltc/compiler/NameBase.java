/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FunctionCall;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

class NameBase
extends FunctionCall {
    private Expression _param = null;
    private Type _paramType = Type.Node;

    public NameBase(QName qName) {
        super(qName);
    }

    public NameBase(QName qName, Vector vector) {
        super(qName, vector);
        this._param = this.argument(0);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        switch (this.argumentCount()) {
            case 0: {
                this._paramType = Type.Node;
                break;
            }
            case 1: {
                this._paramType = this._param.typeCheck(symbolTable);
                break;
            }
            default: {
                throw new TypeCheckError(this);
            }
        }
        if (this._paramType != Type.NodeSet && this._paramType != Type.Node && this._paramType != Type.Reference) {
            throw new TypeCheckError(this);
        }
        this._type = Type.String;
        return this._type;
    }

    public Type getType() {
        return this._type;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(methodGenerator.loadDOM());
        if (this.argumentCount() == 0) {
            instructionList.append(methodGenerator.loadContextNode());
        } else if (this._paramType == Type.Node) {
            this._param.translate(classGenerator, methodGenerator);
        } else if (this._paramType == Type.Reference) {
            this._param.translate(classGenerator, methodGenerator);
            instructionList.append(new INVOKESTATIC(constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "referenceToNodeSet", "(Ljava/lang/Object;)Lorg/apache/xml/dtm/DTMAxisIterator;")));
            instructionList.append(methodGenerator.nextNode());
        } else {
            this._param.translate(classGenerator, methodGenerator);
            this._param.startIterator(classGenerator, methodGenerator);
            instructionList.append(methodGenerator.nextNode());
        }
    }
}

