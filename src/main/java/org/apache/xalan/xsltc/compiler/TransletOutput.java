/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.AttributeValue;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class TransletOutput
extends Instruction {
    private Expression _filename;
    private boolean _append;

    TransletOutput() {
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("TransletOutput: " + this._filename);
    }

    public void parseContents(Parser parser) {
        String string = this.getAttribute("file");
        String string2 = this.getAttribute("append");
        if (string == null || string.equals("")) {
            this.reportError(this, parser, "REQUIRED_ATTR_ERR", "file");
        }
        this._filename = AttributeValue.create(this, string, parser);
        this._append = string2 != null && (string2.toLowerCase().equals("yes") || string2.toLowerCase().equals("true"));
        this.parseChildren(parser);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        Type type = this._filename.typeCheck(symbolTable);
        if (!(type instanceof StringType)) {
            this._filename = new CastExpr(this._filename, Type.String);
        }
        this.typeCheckContents(symbolTable);
        return Type.Void;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        boolean bl = classGenerator.getParser().getXSLTC().isSecureProcessing();
        if (bl) {
            int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "unallowed_extension_elementF", "(Ljava/lang/String;)V");
            instructionList.append(new PUSH(constantPoolGen, "redirect"));
            instructionList.append(new INVOKESTATIC(n2));
            return;
        }
        instructionList.append(methodGenerator.loadHandler());
        int n3 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "openOutputHandler", "(Ljava/lang/String;Z)" + TRANSLET_OUTPUT_SIG);
        int n4 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "closeOutputHandler", "(" + TRANSLET_OUTPUT_SIG + ")V");
        instructionList.append(classGenerator.loadTranslet());
        this._filename.translate(classGenerator, methodGenerator);
        instructionList.append(new PUSH(constantPoolGen, this._append));
        instructionList.append(new INVOKEVIRTUAL(n3));
        instructionList.append(methodGenerator.storeHandler());
        this.translateContents(classGenerator, methodGenerator);
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(methodGenerator.loadHandler());
        instructionList.append(new INVOKEVIRTUAL(n4));
        instructionList.append(methodGenerator.storeHandler());
    }
}

