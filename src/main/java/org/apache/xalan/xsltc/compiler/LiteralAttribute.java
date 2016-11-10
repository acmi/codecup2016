/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.AttributeValue;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.LiteralElement;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SimpleAttributeValue;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.serializer.ElemDesc;

final class LiteralAttribute
extends Instruction {
    private final String _name;
    private final AttributeValue _value;

    public LiteralAttribute(String string, String string2, Parser parser, SyntaxTreeNode syntaxTreeNode) {
        this._name = string;
        this.setParent(syntaxTreeNode);
        this._value = AttributeValue.create(this, string2, parser);
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("LiteralAttribute name=" + this._name + " value=" + this._value);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        this._value.typeCheck(symbolTable);
        this.typeCheckContents(symbolTable);
        return Type.Void;
    }

    protected boolean contextDependent() {
        return this._value.contextDependent();
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(methodGenerator.loadHandler());
        instructionList.append(new PUSH(constantPoolGen, this._name));
        this._value.translate(classGenerator, methodGenerator);
        SyntaxTreeNode syntaxTreeNode = this.getParent();
        if (syntaxTreeNode instanceof LiteralElement && ((LiteralElement)syntaxTreeNode).allAttributesUnique()) {
            String string;
            int n2 = 0;
            boolean bl = false;
            ElemDesc elemDesc = ((LiteralElement)syntaxTreeNode).getElemDesc();
            if (elemDesc != null) {
                if (elemDesc.isAttrFlagSet(this._name, 4)) {
                    n2 |= 2;
                    bl = true;
                } else if (elemDesc.isAttrFlagSet(this._name, 2)) {
                    n2 |= 4;
                }
            }
            if (this._value instanceof SimpleAttributeValue && !this.hasBadChars(string = ((SimpleAttributeValue)this._value).toString()) && !bl) {
                n2 |= 1;
            }
            instructionList.append(new PUSH(constantPoolGen, n2));
            instructionList.append(methodGenerator.uniqueAttribute());
        } else {
            instructionList.append(methodGenerator.attribute());
        }
    }

    private boolean hasBadChars(String string) {
        char[] arrc = string.toCharArray();
        int n2 = arrc.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            char c2 = arrc[i2];
            if (c2 >= ' ' && '~' >= c2 && c2 != '<' && c2 != '>' && c2 != '&' && c2 != '\"') continue;
            return true;
        }
        return false;
    }

    public String getName() {
        return this._name;
    }

    public AttributeValue getValue() {
        return this._value;
    }
}

