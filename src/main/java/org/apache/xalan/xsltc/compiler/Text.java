/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.LiteralElement;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Util;

final class Text
extends Instruction {
    private String _text;
    private boolean _escaping = true;
    private boolean _ignore = false;
    private boolean _textElement = false;

    public Text() {
        this._textElement = true;
    }

    public Text(String string) {
        this._text = string;
    }

    protected String getText() {
        return this._text;
    }

    protected void setText(String string) {
        this._text = this._text == null ? string : this._text + string;
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("Text");
        this.indent(n2 + 4);
        Util.println(this._text);
    }

    public void parseContents(Parser parser) {
        String string = this.getAttribute("disable-output-escaping");
        if (string != null && string.equals("yes")) {
            this._escaping = false;
        }
        this.parseChildren(parser);
        if (this._text == null) {
            if (this._textElement) {
                this._text = "";
            } else {
                this._ignore = true;
            }
        } else if (this._textElement) {
            if (this._text.length() == 0) {
                this._ignore = true;
            }
        } else if (this.getParent() instanceof LiteralElement) {
            LiteralElement literalElement = (LiteralElement)this.getParent();
            String string2 = literalElement.getAttribute("xml:space");
            if (string2 == null || !string2.equals("preserve")) {
                char c2;
                int n2;
                int n3 = this._text.length();
                for (n2 = 0; n2 < n3 && Text.isWhitespace(c2 = this._text.charAt(n2)); ++n2) {
                }
                if (n2 == n3) {
                    this._ignore = true;
                }
            }
        } else {
            int n4;
            char c3;
            int n5 = this._text.length();
            for (n4 = 0; n4 < n5 && Text.isWhitespace(c3 = this._text.charAt(n4)); ++n4) {
            }
            if (n4 == n5) {
                this._ignore = true;
            }
        }
    }

    public void ignore() {
        this._ignore = true;
    }

    public boolean isIgnore() {
        return this._ignore;
    }

    public boolean isTextElement() {
        return this._textElement;
    }

    protected boolean contextDependent() {
        return false;
    }

    private static boolean isWhitespace(char c2) {
        return c2 == ' ' || c2 == '\t' || c2 == '\n' || c2 == '\r';
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (!this._ignore) {
            int n2 = constantPoolGen.addInterfaceMethodref(OUTPUT_HANDLER, "setEscaping", "(Z)Z");
            if (!this._escaping) {
                instructionList.append(methodGenerator.loadHandler());
                instructionList.append(new PUSH(constantPoolGen, false));
                instructionList.append(new INVOKEINTERFACE(n2, 2));
            }
            instructionList.append(methodGenerator.loadHandler());
            if (!this.canLoadAsArrayOffsetLength()) {
                int n3 = constantPoolGen.addInterfaceMethodref(OUTPUT_HANDLER, "characters", "(Ljava/lang/String;)V");
                instructionList.append(new PUSH(constantPoolGen, this._text));
                instructionList.append(new INVOKEINTERFACE(n3, 2));
            } else {
                int n4 = constantPoolGen.addInterfaceMethodref(OUTPUT_HANDLER, "characters", "([CII)V");
                this.loadAsArrayOffsetLength(classGenerator, methodGenerator);
                instructionList.append(new INVOKEINTERFACE(n4, 4));
            }
            if (!this._escaping) {
                instructionList.append(methodGenerator.loadHandler());
                instructionList.append(SWAP);
                instructionList.append(new INVOKEINTERFACE(n2, 2));
                instructionList.append(POP);
            }
        }
        this.translateContents(classGenerator, methodGenerator);
    }

    public boolean canLoadAsArrayOffsetLength() {
        return this._text.length() <= 21845;
    }

    public void loadAsArrayOffsetLength(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        XSLTC xSLTC = classGenerator.getParser().getXSLTC();
        int n2 = xSLTC.addCharacterData(this._text);
        int n3 = this._text.length();
        String string = "_scharData" + (xSLTC.getCharacterDataCount() - 1);
        instructionList.append(new GETSTATIC(constantPoolGen.addFieldref(xSLTC.getClassName(), string, "[C")));
        instructionList.append(new PUSH(constantPoolGen, n2));
        instructionList.append(new PUSH(constantPoolGen, this._text.length()));
    }
}

