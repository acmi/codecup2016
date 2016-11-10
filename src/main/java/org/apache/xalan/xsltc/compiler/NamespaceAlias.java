/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.TopLevelElement;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class NamespaceAlias
extends TopLevelElement {
    private String sPrefix;
    private String rPrefix;

    NamespaceAlias() {
    }

    public void parseContents(Parser parser) {
        this.sPrefix = this.getAttribute("stylesheet-prefix");
        this.rPrefix = this.getAttribute("result-prefix");
        parser.getSymbolTable().addPrefixAlias(this.sPrefix, this.rPrefix);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        return Type.Void;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
    }
}

