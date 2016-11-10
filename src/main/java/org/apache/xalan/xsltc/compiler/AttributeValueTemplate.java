/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.AttributeValue;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.LiteralExpr;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class AttributeValueTemplate
extends AttributeValue {
    static final int OUT_EXPR = 0;
    static final int IN_EXPR = 1;
    static final int IN_EXPR_SQUOTES = 2;
    static final int IN_EXPR_DQUOTES = 3;
    static final String DELIMITER = "\ufffe";

    public AttributeValueTemplate(String string, Parser parser, SyntaxTreeNode syntaxTreeNode) {
        this.setParent(syntaxTreeNode);
        this.setParser(parser);
        try {
            this.parseAVTemplate(string, parser);
        }
        catch (NoSuchElementException noSuchElementException) {
            this.reportError(syntaxTreeNode, parser, "ATTR_VAL_TEMPLATE_ERR", string);
        }
    }

    private void parseAVTemplate(String string, Parser parser) {
        StringTokenizer stringTokenizer = new StringTokenizer(string, "{}\"'", true);
        String string2 = null;
        String string3 = null;
        StringBuffer stringBuffer = new StringBuffer();
        int n2 = 0;
        block23 : while (stringTokenizer.hasMoreTokens()) {
            if (string3 != null) {
                string2 = string3;
                string3 = null;
            } else {
                string2 = stringTokenizer.nextToken();
            }
            if (string2.length() == 1) {
                switch (string2.charAt(0)) {
                    case '{': {
                        switch (n2) {
                            case 0: {
                                string3 = stringTokenizer.nextToken();
                                if (string3.equals("{")) {
                                    stringBuffer.append(string3);
                                    string3 = null;
                                    break;
                                }
                                stringBuffer.append("\ufffe");
                                n2 = 1;
                                break;
                            }
                            case 1: 
                            case 2: 
                            case 3: {
                                this.reportError(this.getParent(), parser, "ATTR_VAL_TEMPLATE_ERR", string);
                            }
                        }
                        continue block23;
                    }
                    case '}': {
                        switch (n2) {
                            case 0: {
                                string3 = stringTokenizer.nextToken();
                                if (string3.equals("}")) {
                                    stringBuffer.append(string3);
                                    string3 = null;
                                    break;
                                }
                                this.reportError(this.getParent(), parser, "ATTR_VAL_TEMPLATE_ERR", string);
                                break;
                            }
                            case 1: {
                                stringBuffer.append("\ufffe");
                                n2 = 0;
                                break;
                            }
                            case 2: 
                            case 3: {
                                stringBuffer.append(string2);
                            }
                        }
                        continue block23;
                    }
                    case '\'': {
                        switch (n2) {
                            case 1: {
                                n2 = 2;
                                break;
                            }
                            case 2: {
                                n2 = 1;
                                break;
                            }
                        }
                        stringBuffer.append(string2);
                        continue block23;
                    }
                    case '\"': {
                        switch (n2) {
                            case 1: {
                                n2 = 3;
                                break;
                            }
                            case 3: {
                                n2 = 1;
                                break;
                            }
                        }
                        stringBuffer.append(string2);
                        continue block23;
                    }
                }
                stringBuffer.append(string2);
                continue;
            }
            stringBuffer.append(string2);
        }
        if (n2 != 0) {
            this.reportError(this.getParent(), parser, "ATTR_VAL_TEMPLATE_ERR", string);
        }
        stringTokenizer = new StringTokenizer(stringBuffer.toString(), "\ufffe", true);
        while (stringTokenizer.hasMoreTokens()) {
            string2 = stringTokenizer.nextToken();
            if (string2.equals("\ufffe")) {
                this.addElement(parser.parseExpression(this, stringTokenizer.nextToken()));
                stringTokenizer.nextToken();
                continue;
            }
            this.addElement(new LiteralExpr(string2));
        }
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        Vector vector = this.getContents();
        int n2 = vector.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Expression expression = (Expression)vector.elementAt(i2);
            if (expression.typeCheck(symbolTable).identicalTo(Type.String)) continue;
            vector.setElementAt(new CastExpr(expression, Type.String), i2);
        }
        this._type = Type.String;
        return this._type;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("AVT:[");
        int n2 = this.elementCount();
        for (int i2 = 0; i2 < n2; ++i2) {
            stringBuffer.append(this.elementAt(i2).toString());
            if (i2 >= n2 - 1) continue;
            stringBuffer.append(' ');
        }
        return stringBuffer.append(']').toString();
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        if (this.elementCount() == 1) {
            Expression expression = (Expression)this.elementAt(0);
            expression.translate(classGenerator, methodGenerator);
        } else {
            ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
            InstructionList instructionList = methodGenerator.getInstructionList();
            int n2 = constantPoolGen.addMethodref("java.lang.StringBuffer", "<init>", "()V");
            INVOKEVIRTUAL iNVOKEVIRTUAL = new INVOKEVIRTUAL(constantPoolGen.addMethodref("java.lang.StringBuffer", "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;"));
            int n3 = constantPoolGen.addMethodref("java.lang.StringBuffer", "toString", "()Ljava/lang/String;");
            instructionList.append(new NEW(constantPoolGen.addClass("java.lang.StringBuffer")));
            instructionList.append(DUP);
            instructionList.append(new INVOKESPECIAL(n2));
            Enumeration enumeration = this.elements();
            while (enumeration.hasMoreElements()) {
                Expression expression = (Expression)enumeration.nextElement();
                expression.translate(classGenerator, methodGenerator);
                instructionList.append(iNVOKEVIRTUAL);
            }
            instructionList.append(new INVOKEVIRTUAL(n3));
        }
    }
}

