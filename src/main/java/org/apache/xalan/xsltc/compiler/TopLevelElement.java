/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

class TopLevelElement
extends SyntaxTreeNode {
    protected Vector _dependencies = null;

    TopLevelElement() {
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        return this.typeCheckContents(symbolTable);
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ErrorMsg errorMsg = new ErrorMsg("NOT_IMPLEMENTED_ERR", this.getClass(), this);
        this.getParser().reportError(2, errorMsg);
    }

    public InstructionList compile(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        InstructionList instructionList = methodGenerator.getInstructionList();
        InstructionList instructionList2 = new InstructionList();
        methodGenerator.setInstructionList(instructionList2);
        this.translate(classGenerator, methodGenerator);
        methodGenerator.setInstructionList(instructionList);
        return instructionList2;
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("TopLevelElement");
        this.displayContents(n2 + 4);
    }

    public void addDependency(TopLevelElement topLevelElement) {
        if (this._dependencies == null) {
            this._dependencies = new Vector();
        }
        if (!this._dependencies.contains(topLevelElement)) {
            this._dependencies.addElement(topLevelElement);
        }
    }

    public Vector getDependencies() {
        return this._dependencies;
    }
}

