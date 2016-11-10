/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.io.PrintStream;
import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Param;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.Template;
import org.apache.xalan.xsltc.compiler.WithParam;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.XML11Char;

final class CallTemplate
extends Instruction {
    private QName _name;
    private Object[] _parameters = null;
    private Template _calleeTemplate = null;

    CallTemplate() {
    }

    public void display(int n2) {
        this.indent(n2);
        System.out.print("CallTemplate");
        Util.println(" name " + this._name);
        this.displayContents(n2 + 4);
    }

    public boolean hasWithParams() {
        return this.elementCount() > 0;
    }

    public void parseContents(Parser parser) {
        String string = this.getAttribute("name");
        if (string.length() > 0) {
            if (!XML11Char.isXML11ValidQName(string)) {
                ErrorMsg errorMsg = new ErrorMsg("INVALID_QNAME_ERR", (Object)string, this);
                parser.reportError(3, errorMsg);
            }
            this._name = parser.getQNameIgnoreDefaultNs(string);
        } else {
            this.reportError(this, parser, "REQUIRED_ATTR_ERR", "name");
        }
        this.parseChildren(parser);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        Template template = symbolTable.lookupTemplate(this._name);
        if (template == null) {
            ErrorMsg errorMsg = new ErrorMsg("TEMPLATE_UNDEF_ERR", (Object)this._name, this);
            throw new TypeCheckError(errorMsg);
        }
        this.typeCheckContents(symbolTable);
        return Type.Void;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Stylesheet stylesheet = classGenerator.getStylesheet();
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (stylesheet.hasLocalParams() || this.hasContents()) {
            this._calleeTemplate = this.getCalleeTemplate();
            if (this._calleeTemplate != null) {
                this.buildParameterList();
            } else {
                int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "pushParamFrame", "()V");
                instructionList.append(classGenerator.loadTranslet());
                instructionList.append(new INVOKEVIRTUAL(n2));
                this.translateContents(classGenerator, methodGenerator);
            }
        }
        String string = stylesheet.getClassName();
        String string2 = Util.escape(this._name.toString());
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(methodGenerator.loadIterator());
        instructionList.append(methodGenerator.loadHandler());
        instructionList.append(methodGenerator.loadCurrentNode());
        StringBuffer stringBuffer = new StringBuffer("(Lorg/apache/xalan/xsltc/DOM;Lorg/apache/xml/dtm/DTMAxisIterator;" + TRANSLET_OUTPUT_SIG + "I");
        if (this._calleeTemplate != null) {
            Vector vector = this._calleeTemplate.getParameters();
            int n3 = this._parameters.length;
            for (int i2 = 0; i2 < n3; ++i2) {
                SyntaxTreeNode syntaxTreeNode = (SyntaxTreeNode)this._parameters[i2];
                stringBuffer.append("Ljava/lang/Object;");
                if (syntaxTreeNode instanceof Param) {
                    instructionList.append(ACONST_NULL);
                    continue;
                }
                syntaxTreeNode.translate(classGenerator, methodGenerator);
            }
        }
        stringBuffer.append(")V");
        instructionList.append(new INVOKEVIRTUAL(constantPoolGen.addMethodref(string, string2, stringBuffer.toString())));
        if (this._calleeTemplate == null && (stylesheet.hasLocalParams() || this.hasContents())) {
            int n4 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "popParamFrame", "()V");
            instructionList.append(classGenerator.loadTranslet());
            instructionList.append(new INVOKEVIRTUAL(n4));
        }
    }

    public Template getCalleeTemplate() {
        Template template = this.getXSLTC().getParser().getSymbolTable().lookupTemplate(this._name);
        return template.isSimpleNamedTemplate() ? template : null;
    }

    private void buildParameterList() {
        int n2;
        Vector vector = this._calleeTemplate.getParameters();
        int n3 = vector.size();
        this._parameters = new Object[n3];
        for (n2 = 0; n2 < n3; ++n2) {
            this._parameters[n2] = vector.elementAt(n2);
        }
        n2 = this.elementCount();
        block1 : for (int i2 = 0; i2 < n2; ++i2) {
            Object object = this.elementAt(i2);
            if (!(object instanceof WithParam)) continue;
            WithParam withParam = (WithParam)object;
            QName qName = withParam.getName();
            for (int i3 = 0; i3 < n3; ++i3) {
                Object object2 = this._parameters[i3];
                if (object2 instanceof Param && ((Param)object2).getName().equals(qName)) {
                    withParam.setDoParameterOptimization(true);
                    this._parameters[i3] = withParam;
                    continue block1;
                }
                if (!(object2 instanceof WithParam) || !((WithParam)object2).getName().equals(qName)) continue;
                withParam.setDoParameterOptimization(true);
                this._parameters[i3] = withParam;
                continue block1;
            }
        }
    }
}

