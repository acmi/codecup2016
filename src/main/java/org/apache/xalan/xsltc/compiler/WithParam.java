/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.XML11Char;

final class WithParam
extends Instruction {
    private QName _name;
    protected String _escapedName;
    private Expression _select;
    private boolean _doParameterOptimization = false;

    WithParam() {
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("with-param " + this._name);
        if (this._select != null) {
            this.indent(n2 + 4);
            Util.println("select " + this._select.toString());
        }
        this.displayContents(n2 + 4);
    }

    public String getEscapedName() {
        return this._escapedName;
    }

    public QName getName() {
        return this._name;
    }

    public void setName(QName qName) {
        this._name = qName;
        this._escapedName = Util.escape(qName.getStringRep());
    }

    public void setDoParameterOptimization(boolean bl) {
        this._doParameterOptimization = bl;
    }

    public void parseContents(Parser parser) {
        Object object;
        String string = this.getAttribute("name");
        if (string.length() > 0) {
            if (!XML11Char.isXML11ValidQName(string)) {
                object = new ErrorMsg("INVALID_QNAME_ERR", (Object)string, this);
                parser.reportError(3, (ErrorMsg)object);
            }
            this.setName(parser.getQNameIgnoreDefaultNs(string));
        } else {
            this.reportError(this, parser, "REQUIRED_ATTR_ERR", "name");
        }
        object = this.getAttribute("select");
        if (object.length() > 0) {
            this._select = parser.parseExpression(this, "select", null);
        }
        this.parseChildren(parser);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (this._select != null) {
            Type type = this._select.typeCheck(symbolTable);
            if (!(type instanceof ReferenceType)) {
                this._select = new CastExpr(this._select, Type.Reference);
            }
        } else {
            this.typeCheckContents(symbolTable);
        }
        return Type.Void;
    }

    public void translateValue(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        if (this._select != null) {
            this._select.translate(classGenerator, methodGenerator);
            this._select.startIterator(classGenerator, methodGenerator);
        } else if (this.hasContents()) {
            this.compileResultTree(classGenerator, methodGenerator);
        } else {
            ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
            InstructionList instructionList = methodGenerator.getInstructionList();
            instructionList.append(new PUSH(constantPoolGen, ""));
        }
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (this._doParameterOptimization) {
            this.translateValue(classGenerator, methodGenerator);
            return;
        }
        String string = Util.escape(this.getEscapedName());
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(new PUSH(constantPoolGen, string));
        this.translateValue(classGenerator, methodGenerator);
        instructionList.append(new PUSH(constantPoolGen, false));
        instructionList.append(new INVOKEVIRTUAL(constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "addParameter", "(Ljava/lang/String;Ljava/lang/Object;Z)Ljava/lang/Object;")));
        instructionList.append(POP);
    }
}

