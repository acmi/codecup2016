/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.Fallback;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class UnsupportedElement
extends SyntaxTreeNode {
    private Vector _fallbacks = null;
    private ErrorMsg _message = null;
    private boolean _isExtension = false;

    public UnsupportedElement(String string, String string2, String string3, boolean bl) {
        super(string, string2, string3);
        this._isExtension = bl;
    }

    public void setErrorMessage(ErrorMsg errorMsg) {
        this._message = errorMsg;
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("Unsupported element = " + this._qname.getNamespace() + ":" + this._qname.getLocalPart());
        this.displayContents(n2 + 4);
    }

    private void processFallbacks(Parser parser) {
        Vector vector = this.getContents();
        if (vector != null) {
            int n2 = vector.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                SyntaxTreeNode syntaxTreeNode = (SyntaxTreeNode)vector.elementAt(i2);
                if (!(syntaxTreeNode instanceof Fallback)) continue;
                Fallback fallback = (Fallback)syntaxTreeNode;
                fallback.activate();
                fallback.parseContents(parser);
                if (this._fallbacks == null) {
                    this._fallbacks = new Vector();
                }
                this._fallbacks.addElement(syntaxTreeNode);
            }
        }
    }

    public void parseContents(Parser parser) {
        this.processFallbacks(parser);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (this._fallbacks != null) {
            int n2 = this._fallbacks.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Fallback fallback = (Fallback)this._fallbacks.elementAt(i2);
                fallback.typeCheck(symbolTable);
            }
        }
        return Type.Void;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        if (this._fallbacks != null) {
            int n2 = this._fallbacks.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Fallback fallback = (Fallback)this._fallbacks.elementAt(i2);
                fallback.translate(classGenerator, methodGenerator);
            }
        } else {
            ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
            InstructionList instructionList = methodGenerator.getInstructionList();
            int n3 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "unsupported_ElementF", "(Ljava/lang/String;Z)V");
            instructionList.append(new PUSH(constantPoolGen, this.getQName().toString()));
            instructionList.append(new PUSH(constantPoolGen, this._isExtension));
            instructionList.append(new INVOKESTATIC(n3));
        }
    }
}

