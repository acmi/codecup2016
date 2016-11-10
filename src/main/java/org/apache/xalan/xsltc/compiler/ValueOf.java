/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class ValueOf
extends Instruction {
    private Expression _select;
    private boolean _escaping = true;
    private boolean _isString = false;

    ValueOf() {
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("ValueOf");
        this.indent(n2 + 4);
        Util.println("select " + this._select.toString());
    }

    public void parseContents(Parser parser) {
        this._select = parser.parseExpression(this, "select", null);
        if (this._select.isDummy()) {
            this.reportError(this, parser, "REQUIRED_ATTR_ERR", "select");
            return;
        }
        String string = this.getAttribute("disable-output-escaping");
        if (string != null && string.equals("yes")) {
            this._escaping = false;
        }
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        Type type = this._select.typeCheck(symbolTable);
        if (type != null && !type.identicalTo(Type.Node)) {
            if (type.identicalTo(Type.NodeSet)) {
                this._select = new CastExpr(this._select, Type.Node);
            } else {
                this._isString = true;
                if (!type.identicalTo(Type.String)) {
                    this._select = new CastExpr(this._select, Type.String);
                }
                this._isString = true;
            }
        }
        return Type.Void;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n2 = constantPoolGen.addInterfaceMethodref(OUTPUT_HANDLER, "setEscaping", "(Z)Z");
        if (!this._escaping) {
            instructionList.append(methodGenerator.loadHandler());
            instructionList.append(new PUSH(constantPoolGen, false));
            instructionList.append(new INVOKEINTERFACE(n2, 2));
        }
        if (this._isString) {
            int n3 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "characters", CHARACTERSW_SIG);
            instructionList.append(classGenerator.loadTranslet());
            this._select.translate(classGenerator, methodGenerator);
            instructionList.append(methodGenerator.loadHandler());
            instructionList.append(new INVOKEVIRTUAL(n3));
        } else {
            int n4 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "characters", CHARACTERS_SIG);
            instructionList.append(methodGenerator.loadDOM());
            this._select.translate(classGenerator, methodGenerator);
            instructionList.append(methodGenerator.loadHandler());
            instructionList.append(new INVOKEINTERFACE(n4, 3));
        }
        if (!this._escaping) {
            instructionList.append(methodGenerator.loadHandler());
            instructionList.append(SWAP);
            instructionList.append(new INVOKEINTERFACE(n2, 2));
            instructionList.append(POP);
        }
    }
}

