/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.Text;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class Message
extends Instruction {
    private boolean _terminate = false;

    Message() {
    }

    public void parseContents(Parser parser) {
        String string = this.getAttribute("terminate");
        if (string != null) {
            this._terminate = string.equals("yes");
        }
        this.parseChildren(parser);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        this.typeCheckContents(symbolTable);
        return Type.Void;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(classGenerator.loadTranslet());
        switch (this.elementCount()) {
            case 0: {
                instructionList.append(new PUSH(constantPoolGen, ""));
                break;
            }
            case 1: {
                SyntaxTreeNode syntaxTreeNode = (SyntaxTreeNode)this.elementAt(0);
                if (syntaxTreeNode instanceof Text) {
                    instructionList.append(new PUSH(constantPoolGen, ((Text)syntaxTreeNode).getText()));
                    break;
                }
            }
            default: {
                instructionList.append(methodGenerator.loadHandler());
                instructionList.append(new NEW(constantPoolGen.addClass(STREAM_XML_OUTPUT)));
                instructionList.append(methodGenerator.storeHandler());
                instructionList.append(new NEW(constantPoolGen.addClass("java.io.StringWriter")));
                instructionList.append(DUP);
                instructionList.append(DUP);
                instructionList.append(new INVOKESPECIAL(constantPoolGen.addMethodref("java.io.StringWriter", "<init>", "()V")));
                instructionList.append(methodGenerator.loadHandler());
                instructionList.append(new INVOKESPECIAL(constantPoolGen.addMethodref(STREAM_XML_OUTPUT, "<init>", "()V")));
                instructionList.append(methodGenerator.loadHandler());
                instructionList.append(SWAP);
                instructionList.append(new INVOKEINTERFACE(constantPoolGen.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE, "setWriter", "(Ljava/io/Writer;)V"), 2));
                instructionList.append(methodGenerator.loadHandler());
                instructionList.append(new PUSH(constantPoolGen, "UTF-8"));
                instructionList.append(new INVOKEINTERFACE(constantPoolGen.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE, "setEncoding", "(Ljava/lang/String;)V"), 2));
                instructionList.append(methodGenerator.loadHandler());
                instructionList.append(ICONST_1);
                instructionList.append(new INVOKEINTERFACE(constantPoolGen.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE, "setOmitXMLDeclaration", "(Z)V"), 2));
                instructionList.append(methodGenerator.loadHandler());
                instructionList.append(new INVOKEINTERFACE(constantPoolGen.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE, "startDocument", "()V"), 1));
                this.translateContents(classGenerator, methodGenerator);
                instructionList.append(methodGenerator.loadHandler());
                instructionList.append(new INVOKEINTERFACE(constantPoolGen.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE, "endDocument", "()V"), 1));
                instructionList.append(new INVOKEVIRTUAL(constantPoolGen.addMethodref("java.io.StringWriter", "toString", "()Ljava/lang/String;")));
                instructionList.append(SWAP);
                instructionList.append(methodGenerator.storeHandler());
            }
        }
        instructionList.append(new INVOKEVIRTUAL(constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "displayMessage", "(Ljava/lang/String;)V")));
        if (this._terminate) {
            int n2 = constantPoolGen.addMethodref("java.lang.RuntimeException", "<init>", "(Ljava/lang/String;)V");
            instructionList.append(new NEW(constantPoolGen.addClass("java.lang.RuntimeException")));
            instructionList.append(DUP);
            instructionList.append(new PUSH(constantPoolGen, "Termination forced by an xsl:message instruction"));
            instructionList.append(new INVOKESPECIAL(n2));
            instructionList.append(ATHROW);
        }
    }
}

