/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.TopLevelElement;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.runtime.AttributeList;
import org.apache.xml.utils.XML11Char;

final class DecimalFormatting
extends TopLevelElement {
    private static final String DFS_CLASS = "java.text.DecimalFormatSymbols";
    private static final String DFS_SIG = "Ljava/text/DecimalFormatSymbols;";
    private QName _name = null;

    DecimalFormatting() {
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        return Type.Void;
    }

    public void parseContents(Parser parser) {
        Object object;
        String string = this.getAttribute("name");
        if (string.length() > 0 && !XML11Char.isXML11ValidQName(string)) {
            object = new ErrorMsg("INVALID_QNAME_ERR", (Object)string, this);
            parser.reportError(3, (ErrorMsg)object);
        }
        this._name = parser.getQNameIgnoreDefaultNs(string);
        if (this._name == null) {
            this._name = parser.getQNameIgnoreDefaultNs("");
        }
        if ((object = parser.getSymbolTable()).getDecimalFormatting(this._name) != null) {
            this.reportWarning(this, parser, "SYMBOLS_REDEF_ERR", this._name.toString());
        } else {
            object.addDecimalFormatting(this._name, this);
        }
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        int n2;
        int n3;
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n4 = constantPoolGen.addMethodref("java.text.DecimalFormatSymbols", "<init>", "(Ljava/util/Locale;)V");
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(new PUSH(constantPoolGen, this._name.toString()));
        instructionList.append(new NEW(constantPoolGen.addClass("java.text.DecimalFormatSymbols")));
        instructionList.append(DUP);
        instructionList.append(new GETSTATIC(constantPoolGen.addFieldref("java.util.Locale", "US", "Ljava/util/Locale;")));
        instructionList.append(new INVOKESPECIAL(n4));
        String string = this.getAttribute("NaN");
        if (string == null || string.equals("")) {
            n2 = constantPoolGen.addMethodref("java.text.DecimalFormatSymbols", "setNaN", "(Ljava/lang/String;)V");
            instructionList.append(DUP);
            instructionList.append(new PUSH(constantPoolGen, "NaN"));
            instructionList.append(new INVOKEVIRTUAL(n2));
        }
        if ((string = this.getAttribute("infinity")) == null || string.equals("")) {
            n2 = constantPoolGen.addMethodref("java.text.DecimalFormatSymbols", "setInfinity", "(Ljava/lang/String;)V");
            instructionList.append(DUP);
            instructionList.append(new PUSH(constantPoolGen, "Infinity"));
            instructionList.append(new INVOKEVIRTUAL(n2));
        }
        n2 = this._attributes.getLength();
        for (n3 = 0; n3 < n2; ++n3) {
            String string2 = this._attributes.getQName(n3);
            String string3 = this._attributes.getValue(n3);
            boolean bl = true;
            int n5 = 0;
            if (string2.equals("decimal-separator")) {
                n5 = constantPoolGen.addMethodref("java.text.DecimalFormatSymbols", "setDecimalSeparator", "(C)V");
            } else if (string2.equals("grouping-separator")) {
                n5 = constantPoolGen.addMethodref("java.text.DecimalFormatSymbols", "setGroupingSeparator", "(C)V");
            } else if (string2.equals("minus-sign")) {
                n5 = constantPoolGen.addMethodref("java.text.DecimalFormatSymbols", "setMinusSign", "(C)V");
            } else if (string2.equals("percent")) {
                n5 = constantPoolGen.addMethodref("java.text.DecimalFormatSymbols", "setPercent", "(C)V");
            } else if (string2.equals("per-mille")) {
                n5 = constantPoolGen.addMethodref("java.text.DecimalFormatSymbols", "setPerMill", "(C)V");
            } else if (string2.equals("zero-digit")) {
                n5 = constantPoolGen.addMethodref("java.text.DecimalFormatSymbols", "setZeroDigit", "(C)V");
            } else if (string2.equals("digit")) {
                n5 = constantPoolGen.addMethodref("java.text.DecimalFormatSymbols", "setDigit", "(C)V");
            } else if (string2.equals("pattern-separator")) {
                n5 = constantPoolGen.addMethodref("java.text.DecimalFormatSymbols", "setPatternSeparator", "(C)V");
            } else if (string2.equals("NaN")) {
                n5 = constantPoolGen.addMethodref("java.text.DecimalFormatSymbols", "setNaN", "(Ljava/lang/String;)V");
                instructionList.append(DUP);
                instructionList.append(new PUSH(constantPoolGen, string3));
                instructionList.append(new INVOKEVIRTUAL(n5));
                bl = false;
            } else if (string2.equals("infinity")) {
                n5 = constantPoolGen.addMethodref("java.text.DecimalFormatSymbols", "setInfinity", "(Ljava/lang/String;)V");
                instructionList.append(DUP);
                instructionList.append(new PUSH(constantPoolGen, string3));
                instructionList.append(new INVOKEVIRTUAL(n5));
                bl = false;
            } else {
                bl = false;
            }
            if (!bl) continue;
            instructionList.append(DUP);
            instructionList.append(new PUSH(constantPoolGen, string3.charAt(0)));
            instructionList.append(new INVOKEVIRTUAL(n5));
        }
        n3 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "addDecimalFormat", "(Ljava/lang/String;Ljava/text/DecimalFormatSymbols;)V");
        instructionList.append(new INVOKEVIRTUAL(n3));
    }

    public static void translateDefaultDFS(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n2 = constantPoolGen.addMethodref("java.text.DecimalFormatSymbols", "<init>", "(Ljava/util/Locale;)V");
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(new PUSH(constantPoolGen, ""));
        instructionList.append(new NEW(constantPoolGen.addClass("java.text.DecimalFormatSymbols")));
        instructionList.append(DUP);
        instructionList.append(new GETSTATIC(constantPoolGen.addFieldref("java.util.Locale", "US", "Ljava/util/Locale;")));
        instructionList.append(new INVOKESPECIAL(n2));
        int n3 = constantPoolGen.addMethodref("java.text.DecimalFormatSymbols", "setNaN", "(Ljava/lang/String;)V");
        instructionList.append(DUP);
        instructionList.append(new PUSH(constantPoolGen, "NaN"));
        instructionList.append(new INVOKEVIRTUAL(n3));
        int n4 = constantPoolGen.addMethodref("java.text.DecimalFormatSymbols", "setInfinity", "(Ljava/lang/String;)V");
        instructionList.append(DUP);
        instructionList.append(new PUSH(constantPoolGen, "Infinity"));
        instructionList.append(new INVOKEVIRTUAL(n4));
        int n5 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "addDecimalFormat", "(Ljava/lang/String;Ljava/text/DecimalFormatSymbols;)V");
        instructionList.append(new INVOKEVIRTUAL(n5));
    }
}

