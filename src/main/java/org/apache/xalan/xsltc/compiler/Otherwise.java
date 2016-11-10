/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class Otherwise
extends Instruction {
    Otherwise() {
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("Otherwise");
        this.indent(n2 + 4);
        this.displayContents(n2 + 4);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        this.typeCheckContents(symbolTable);
        return Type.Void;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Parser parser = this.getParser();
        ErrorMsg errorMsg = new ErrorMsg("STRAY_OTHERWISE_ERR", this);
        parser.reportError(3, errorMsg);
    }
}

