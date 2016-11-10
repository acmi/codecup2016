/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FunctionCall;
import org.apache.xalan.xsltc.compiler.Key;
import org.apache.xalan.xsltc.compiler.LiteralExpr;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.TopLevelElement;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class KeyCall
extends FunctionCall {
    private Expression _name;
    private Expression _value;
    private Type _valueType;
    private QName _resolvedQName = null;

    public KeyCall(QName qName, Vector vector) {
        super(qName, vector);
        switch (this.argumentCount()) {
            case 1: {
                this._name = null;
                this._value = this.argument(0);
                break;
            }
            case 2: {
                this._name = this.argument(0);
                this._value = this.argument(1);
                break;
            }
            default: {
                this._value = null;
                this._name = null;
            }
        }
    }

    public void addParentDependency() {
        SyntaxTreeNode syntaxTreeNode;
        if (this._resolvedQName == null) {
            return;
        }
        for (syntaxTreeNode = this; syntaxTreeNode != null && !(syntaxTreeNode instanceof TopLevelElement); syntaxTreeNode = syntaxTreeNode.getParent()) {
        }
        TopLevelElement topLevelElement = (TopLevelElement)syntaxTreeNode;
        if (topLevelElement != null) {
            topLevelElement.addDependency(this.getSymbolTable().getKey(this._resolvedQName));
        }
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        Type type = super.typeCheck(symbolTable);
        if (this._name != null) {
            Type type2 = this._name.typeCheck(symbolTable);
            if (this._name instanceof LiteralExpr) {
                LiteralExpr literalExpr = (LiteralExpr)this._name;
                this._resolvedQName = this.getParser().getQNameIgnoreDefaultNs(literalExpr.getValue());
            } else if (!(type2 instanceof StringType)) {
                this._name = new CastExpr(this._name, Type.String);
            }
        }
        this._valueType = this._value.typeCheck(symbolTable);
        if (this._valueType != Type.NodeSet && this._valueType != Type.Reference && this._valueType != Type.String) {
            this._value = new CastExpr(this._value, Type.String);
            this._valueType = this._value.typeCheck(symbolTable);
        }
        this.addParentDependency();
        return type;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "getKeyIndex", "(Ljava/lang/String;)Lorg/apache/xalan/xsltc/dom/KeyIndex;");
        int n3 = constantPoolGen.addMethodref("org/apache/xalan/xsltc/dom/KeyIndex", "setDom", "(Lorg/apache/xalan/xsltc/DOM;)V");
        int n4 = constantPoolGen.addMethodref("org/apache/xalan/xsltc/dom/KeyIndex", "getKeyIndexIterator", "(" + this._valueType.toSignature() + "Z)" + "Lorg/apache/xalan/xsltc/dom/KeyIndex$KeyIndexIterator;");
        instructionList.append(classGenerator.loadTranslet());
        if (this._name == null) {
            instructionList.append(new PUSH(constantPoolGen, "##id"));
        } else if (this._resolvedQName != null) {
            instructionList.append(new PUSH(constantPoolGen, this._resolvedQName.toString()));
        } else {
            this._name.translate(classGenerator, methodGenerator);
        }
        instructionList.append(new INVOKEVIRTUAL(n2));
        instructionList.append(DUP);
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(new INVOKEVIRTUAL(n3));
        this._value.translate(classGenerator, methodGenerator);
        instructionList.append(this._name != null ? ICONST_1 : ICONST_0);
        instructionList.append(new INVOKEVIRTUAL(n4));
    }
}

