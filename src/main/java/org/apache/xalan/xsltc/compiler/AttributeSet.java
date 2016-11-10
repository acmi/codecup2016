/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import java.util.Vector;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.Text;
import org.apache.xalan.xsltc.compiler.TopLevelElement;
import org.apache.xalan.xsltc.compiler.UseAttributeSets;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.XslAttribute;
import org.apache.xalan.xsltc.compiler.util.AttributeSetMethodGenerator;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.XML11Char;

final class AttributeSet
extends TopLevelElement {
    private static final String AttributeSetPrefix = "$as$";
    private QName _name;
    private UseAttributeSets _useSets;
    private AttributeSet _mergeSet;
    private String _method;
    private boolean _ignore = false;

    AttributeSet() {
    }

    public QName getName() {
        return this._name;
    }

    public String getMethodName() {
        return this._method;
    }

    public void ignore() {
        this._ignore = true;
    }

    public void parseContents(Parser parser) {
        Object object;
        Object object2;
        String string = this.getAttribute("name");
        if (!XML11Char.isXML11ValidQName(string)) {
            object2 = new ErrorMsg("INVALID_QNAME_ERR", (Object)string, this);
            parser.reportError(3, (ErrorMsg)object2);
        }
        this._name = parser.getQNameIgnoreDefaultNs(string);
        if (this._name == null || this._name.equals("")) {
            object2 = new ErrorMsg("UNNAMED_ATTRIBSET_ERR", this);
            parser.reportError(3, (ErrorMsg)object2);
        }
        if ((object2 = this.getAttribute("use-attribute-sets")).length() > 0) {
            if (!Util.isValidQNames((String)object2)) {
                object = new ErrorMsg("INVALID_QNAME_ERR", object2, this);
                parser.reportError(3, (ErrorMsg)object);
            }
            this._useSets = new UseAttributeSets((String)object2, parser);
        }
        object = this.getContents();
        int n2 = object.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            SyntaxTreeNode syntaxTreeNode = (SyntaxTreeNode)object.elementAt(i2);
            if (syntaxTreeNode instanceof XslAttribute) {
                parser.getSymbolTable().setCurrentNode(syntaxTreeNode);
                syntaxTreeNode.parseContents(parser);
                continue;
            }
            if (syntaxTreeNode instanceof Text) continue;
            ErrorMsg errorMsg = new ErrorMsg("ILLEGAL_CHILD_ERR", this);
            parser.reportError(3, errorMsg);
        }
        parser.getSymbolTable().setCurrentNode(this);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (this._ignore) {
            return Type.Void;
        }
        this._mergeSet = symbolTable.addAttributeSet(this);
        this._method = "$as$" + this.getXSLTC().nextAttributeSetSerial();
        if (this._useSets != null) {
            this._useSets.typeCheck(symbolTable);
        }
        this.typeCheckContents(symbolTable);
        return Type.Void;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Object object;
        Object object2;
        Object object3;
        if (this._ignore) {
            return;
        }
        methodGenerator = new AttributeSetMethodGenerator(this._method, classGenerator);
        if (this._mergeSet != null) {
            object3 = classGenerator.getConstantPool();
            object = methodGenerator.getInstructionList();
            object2 = this._mergeSet.getMethodName();
            object.append(classGenerator.loadTranslet());
            object.append(methodGenerator.loadDOM());
            object.append(methodGenerator.loadIterator());
            object.append(methodGenerator.loadHandler());
            int n2 = object3.addMethodref(classGenerator.getClassName(), (String)object2, ATTR_SET_SIG);
            object.append(new INVOKESPECIAL(n2));
        }
        if (this._useSets != null) {
            this._useSets.translate(classGenerator, methodGenerator);
        }
        object3 = this.elements();
        while (object3.hasMoreElements()) {
            object = (SyntaxTreeNode)object3.nextElement();
            if (!(object instanceof XslAttribute)) continue;
            object2 = (XslAttribute)object;
            object2.translate(classGenerator, methodGenerator);
        }
        object = methodGenerator.getInstructionList();
        object.append(RETURN);
        classGenerator.addMethod(methodGenerator);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("attribute-set: ");
        Enumeration enumeration = this.elements();
        while (enumeration.hasMoreElements()) {
            XslAttribute xslAttribute = (XslAttribute)enumeration.nextElement();
            stringBuffer.append(xslAttribute);
        }
        return stringBuffer.toString();
    }
}

