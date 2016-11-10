/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.io.PrintStream;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class When
extends Instruction {
    private Expression _test;
    private boolean _ignore = false;

    When() {
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("When");
        this.indent(n2 + 4);
        System.out.print("test ");
        Util.println(this._test.toString());
        this.displayContents(n2 + 4);
    }

    public Expression getTest() {
        return this._test;
    }

    public boolean ignore() {
        return this._ignore;
    }

    public void parseContents(Parser parser) {
        this._test = parser.parseExpression(this, "test", null);
        Object object = this._test.evaluateAtCompileTime();
        if (object != null && object instanceof Boolean) {
            this._ignore = (Boolean)object == false;
        }
        this.parseChildren(parser);
        if (this._test.isDummy()) {
            this.reportError(this, parser, "REQUIRED_ATTR_ERR", "test");
        }
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
        ErrorMsg errorMsg = new ErrorMsg("STRAY_WHEN_ERR", this);
        this.getParser().reportError(3, errorMsg);
    }
}

