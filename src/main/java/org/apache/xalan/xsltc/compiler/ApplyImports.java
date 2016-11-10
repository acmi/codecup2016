/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Mode;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.Template;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class ApplyImports
extends Instruction {
    private QName _modeName;
    private int _precedence;

    ApplyImports() {
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("ApplyTemplates");
        this.indent(n2 + 4);
        if (this._modeName != null) {
            this.indent(n2 + 4);
            Util.println("mode " + this._modeName);
        }
    }

    public boolean hasWithParams() {
        return this.hasContents();
    }

    private int getMinPrecedence(int n2) {
        Stylesheet stylesheet = this.getStylesheet();
        while (stylesheet._includedFrom != null) {
            stylesheet = stylesheet._includedFrom;
        }
        return stylesheet.getMinimumDescendantPrecedence();
    }

    public void parseContents(Parser parser) {
        Stylesheet stylesheet = this.getStylesheet();
        stylesheet.setTemplateInlining(false);
        Template template = this.getTemplate();
        this._modeName = template.getModeName();
        this._precedence = template.getImportPrecedence();
        this.parseChildren(parser);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        this.typeCheckContents(symbolTable);
        return Type.Void;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        int n2;
        Stylesheet stylesheet = classGenerator.getStylesheet();
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n3 = methodGenerator.getLocalIndex("current");
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(methodGenerator.loadIterator());
        instructionList.append(methodGenerator.loadHandler());
        instructionList.append(methodGenerator.loadCurrentNode());
        if (stylesheet.hasLocalParams()) {
            instructionList.append(classGenerator.loadTranslet());
            n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "pushParamFrame", "()V");
            instructionList.append(new INVOKEVIRTUAL(n2));
        }
        n2 = this._precedence;
        int n4 = this.getMinPrecedence(n2);
        Mode mode = stylesheet.getMode(this._modeName);
        String string = mode.functionName(n4, n2);
        String string2 = classGenerator.getStylesheet().getClassName();
        String string3 = classGenerator.getApplyTemplatesSigForImport();
        int n5 = constantPoolGen.addMethodref(string2, string, string3);
        instructionList.append(new INVOKEVIRTUAL(n5));
        if (stylesheet.hasLocalParams()) {
            instructionList.append(classGenerator.loadTranslet());
            int n6 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "popParamFrame", "()V");
            instructionList.append(new INVOKEVIRTUAL(n6));
        }
    }
}

