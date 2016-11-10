/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Mode;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.Sort;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.ResultTreeType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.XML11Char;

final class ApplyTemplates
extends Instruction {
    private Expression _select;
    private Type _type = null;
    private QName _modeName;
    private String _functionName;

    ApplyTemplates() {
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("ApplyTemplates");
        this.indent(n2 + 4);
        Util.println("select " + this._select.toString());
        if (this._modeName != null) {
            this.indent(n2 + 4);
            Util.println("mode " + this._modeName);
        }
    }

    public boolean hasWithParams() {
        return this.hasContents();
    }

    public void parseContents(Parser parser) {
        String string = this.getAttribute("select");
        String string2 = this.getAttribute("mode");
        if (string.length() > 0) {
            this._select = parser.parseExpression(this, "select", null);
        }
        if (string2.length() > 0) {
            if (!XML11Char.isXML11ValidQName(string2)) {
                ErrorMsg errorMsg = new ErrorMsg("INVALID_QNAME_ERR", (Object)string2, this);
                parser.reportError(3, errorMsg);
            }
            this._modeName = parser.getQNameIgnoreDefaultNs(string2);
        }
        this._functionName = parser.getTopLevelStylesheet().getMode(this._modeName).functionName();
        this.parseChildren(parser);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (this._select != null) {
            this._type = this._select.typeCheck(symbolTable);
            if (this._type instanceof NodeType || this._type instanceof ReferenceType) {
                this._select = new CastExpr(this._select, Type.NodeSet);
                this._type = Type.NodeSet;
            }
            if (this._type instanceof NodeSetType || this._type instanceof ResultTreeType) {
                this.typeCheckContents(symbolTable);
                return Type.Void;
            }
            throw new TypeCheckError(this);
        }
        this.typeCheckContents(symbolTable);
        return Type.Void;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Object object;
        boolean bl = false;
        Stylesheet stylesheet = classGenerator.getStylesheet();
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n2 = methodGenerator.getLocalIndex("current");
        Vector vector = new Vector();
        Enumeration enumeration = this.elements();
        while (enumeration.hasMoreElements()) {
            object = enumeration.nextElement();
            if (!(object instanceof Sort)) continue;
            vector.addElement(object);
        }
        if (stylesheet.hasLocalParams() || this.hasContents()) {
            instructionList.append(classGenerator.loadTranslet());
            int n3 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "pushParamFrame", "()V");
            instructionList.append(new INVOKEVIRTUAL(n3));
            this.translateContents(classGenerator, methodGenerator);
        }
        instructionList.append(classGenerator.loadTranslet());
        if (this._type != null && this._type instanceof ResultTreeType) {
            if (vector.size() > 0) {
                ErrorMsg errorMsg = new ErrorMsg("RESULT_TREE_SORT_ERR", this);
                this.getParser().reportError(4, errorMsg);
            }
            this._select.translate(classGenerator, methodGenerator);
            this._type.translateTo(classGenerator, methodGenerator, Type.NodeSet);
        } else {
            instructionList.append(methodGenerator.loadDOM());
            if (vector.size() > 0) {
                Sort.translateSortIterator(classGenerator, methodGenerator, this._select, vector);
                object = constantPoolGen.addInterfaceMethodref("org.apache.xml.dtm.DTMAxisIterator", "setStartNode", "(I)Lorg/apache/xml/dtm/DTMAxisIterator;");
                instructionList.append(methodGenerator.loadCurrentNode());
                instructionList.append(new INVOKEINTERFACE((int)object, 2));
                bl = true;
            } else if (this._select == null) {
                Mode.compileGetChildren(classGenerator, methodGenerator, n2);
            } else {
                this._select.translate(classGenerator, methodGenerator);
            }
        }
        if (this._select != null && !bl) {
            this._select.startIterator(classGenerator, methodGenerator);
        }
        String string = classGenerator.getStylesheet().getClassName();
        instructionList.append(methodGenerator.loadHandler());
        String string2 = classGenerator.getApplyTemplatesSig();
        int n4 = constantPoolGen.addMethodref(string, this._functionName, string2);
        instructionList.append(new INVOKEVIRTUAL(n4));
        if (stylesheet.hasLocalParams() || this.hasContents()) {
            instructionList.append(classGenerator.loadTranslet());
            int n5 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "popParamFrame", "()V");
            instructionList.append(new INVOKEVIRTUAL(n5));
        }
    }
}

