/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.io.PrintStream;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class If
extends Instruction {
    private Expression _test;
    private boolean _ignore = false;

    If() {
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("If");
        this.indent(n2 + 4);
        System.out.print("test ");
        Util.println(this._test.toString());
        this.displayContents(n2 + 4);
    }

    public void parseContents(Parser parser) {
        this._test = parser.parseExpression(this, "test", null);
        if (this._test.isDummy()) {
            this.reportError(this, parser, "REQUIRED_ATTR_ERR", "test");
            return;
        }
        Object object = this._test.evaluateAtCompileTime();
        if (object != null && object instanceof Boolean) {
            this._ignore = (Boolean)object == false;
        }
        this.parseChildren(parser);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (!(this._test.typeCheck(symbolTable) instanceof BooleanType)) {
            this._test = new CastExpr(this._test, Type.Boolean);
        }
        if (!this._ignore) {
            this.typeCheckContents(symbolTable);
        }
        return Type.Void;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        this._test.translateDesynthesized(classGenerator, methodGenerator);
        InstructionHandle instructionHandle = instructionList.getEnd();
        if (!this._ignore) {
            this.translateContents(classGenerator, methodGenerator);
        }
        this._test.backPatchFalseList(instructionList.append(NOP));
        this._test.backPatchTrueList(instructionHandle.getNext());
    }
}

