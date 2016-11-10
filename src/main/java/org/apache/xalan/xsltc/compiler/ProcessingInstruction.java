/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.AttributeValue;
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
import org.apache.xml.utils.XML11Char;

final class ProcessingInstruction
extends Instruction {
    private AttributeValue _name;
    private boolean _isLiteral = false;

    ProcessingInstruction() {
    }

    public void parseContents(Parser parser) {
        String string = this.getAttribute("name");
        if (string.length() > 0) {
            this._isLiteral = Util.isLiteral(string);
            if (this._isLiteral && !XML11Char.isXML11ValidNCName(string)) {
                ErrorMsg errorMsg = new ErrorMsg("INVALID_NCNAME_ERR", (Object)string, this);
                parser.reportError(3, errorMsg);
            }
            this._name = AttributeValue.create(this, string, parser);
        } else {
            this.reportError(this, parser, "REQUIRED_ATTR_ERR", "name");
        }
        if (string.equals("xml")) {
            this.reportError(this, parser, "ILLEGAL_PI_ERR", "xml");
        }
        this.parseChildren(parser);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        this._name.typeCheck(symbolTable);
        this.typeCheckContents(symbolTable);
        return Type.Void;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (!this._isLiteral) {
            LocalVariableGen localVariableGen = methodGenerator.addLocalVariable2("nameValue", Util.getJCRefType("Ljava/lang/String;"), null);
            this._name.translate(classGenerator, methodGenerator);
            localVariableGen.setStart(instructionList.append(new ASTORE(localVariableGen.getIndex())));
            instructionList.append(new ALOAD(localVariableGen.getIndex()));
            int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "checkNCName", "(Ljava/lang/String;)V");
            instructionList.append(new INVOKESTATIC(n2));
            instructionList.append(methodGenerator.loadHandler());
            instructionList.append(DUP);
            localVariableGen.setEnd(instructionList.append(new ALOAD(localVariableGen.getIndex())));
        } else {
            instructionList.append(methodGenerator.loadHandler());
            instructionList.append(DUP);
            this._name.translate(classGenerator, methodGenerator);
        }
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(new GETFIELD(constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "stringValueHandler", "Lorg/apache/xalan/xsltc/runtime/StringValueHandler;")));
        instructionList.append(DUP);
        instructionList.append(methodGenerator.storeHandler());
        this.translateContents(classGenerator, methodGenerator);
        instructionList.append(new INVOKEVIRTUAL(constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.StringValueHandler", "getValueOfPI", "()Ljava/lang/String;")));
        int n3 = constantPoolGen.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE, "processingInstruction", "(Ljava/lang/String;Ljava/lang/String;)V");
        instructionList.append(new INVOKEINTERFACE(n3, 3));
        instructionList.append(methodGenerator.storeHandler());
    }
}

