/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.io.PrintStream;
import java.util.Vector;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.TopLevelElement;
import org.apache.xalan.xsltc.compiler.VariableRefBase;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.XML11Char;

class VariableBase
extends TopLevelElement {
    protected QName _name;
    protected String _escapedName;
    protected Type _type;
    protected boolean _isLocal;
    protected LocalVariableGen _local;
    protected Instruction _loadInstruction;
    protected Instruction _storeInstruction;
    protected Expression _select;
    protected String select;
    protected Vector _refs = new Vector(2);
    protected Vector _dependencies = null;
    protected boolean _ignore = false;

    VariableBase() {
    }

    public void disable() {
        this._ignore = true;
    }

    public void addReference(VariableRefBase variableRefBase) {
        this._refs.addElement(variableRefBase);
    }

    public void mapRegister(MethodGenerator methodGenerator) {
        if (this._local == null) {
            String string = this.getEscapedName();
            org.apache.bcel.generic.Type type = this._type.toJCType();
            this._local = methodGenerator.addLocalVariable2(string, type, null);
        }
    }

    public void unmapRegister(MethodGenerator methodGenerator) {
        if (this._local != null) {
            this._local.setEnd(methodGenerator.getInstructionList().getEnd());
            methodGenerator.removeLocalVariable(this._local);
            this._refs = null;
            this._local = null;
        }
    }

    public Instruction loadInstruction() {
        Instruction instruction = this._loadInstruction;
        if (this._loadInstruction == null) {
            this._loadInstruction = this._type.LOAD(this._local.getIndex());
        }
        return this._loadInstruction;
    }

    public Instruction storeInstruction() {
        Instruction instruction = this._storeInstruction;
        if (this._storeInstruction == null) {
            this._storeInstruction = this._type.STORE(this._local.getIndex());
        }
        return this._storeInstruction;
    }

    public Expression getExpression() {
        return this._select;
    }

    public String toString() {
        return "variable(" + this._name + ")";
    }

    public void display(int n2) {
        this.indent(n2);
        System.out.println("Variable " + this._name);
        if (this._select != null) {
            this.indent(n2 + 4);
            System.out.println("select " + this._select.toString());
        }
        this.displayContents(n2 + 4);
    }

    public Type getType() {
        return this._type;
    }

    public QName getName() {
        return this._name;
    }

    public String getEscapedName() {
        return this._escapedName;
    }

    public void setName(QName qName) {
        this._name = qName;
        this._escapedName = Util.escape(qName.getStringRep());
    }

    public boolean isLocal() {
        return this._isLocal;
    }

    public void parseContents(Parser parser) {
        Object object;
        String string = this.getAttribute("name");
        if (string.length() > 0) {
            if (!XML11Char.isXML11ValidQName(string)) {
                object = new ErrorMsg("INVALID_QNAME_ERR", (Object)string, this);
                parser.reportError(3, (ErrorMsg)object);
            }
            this.setName(parser.getQNameIgnoreDefaultNs(string));
        } else {
            this.reportError(this, parser, "REQUIRED_ATTR_ERR", "name");
        }
        object = parser.lookupVariable(this._name);
        if (object != null && object.getParent() == this.getParent()) {
            this.reportError(this, parser, "VARIABLE_REDEF_ERR", string);
        }
        this.select = this.getAttribute("select");
        if (this.select.length() > 0) {
            this._select = this.getParser().parseExpression(this, "select", null);
            if (this._select.isDummy()) {
                this.reportError(this, parser, "REQUIRED_ATTR_ERR", "select");
                return;
            }
        }
        this.parseChildren(parser);
    }

    public void translateValue(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        if (this._select != null) {
            this._select.translate(classGenerator, methodGenerator);
            if (this._select.getType() instanceof NodeSetType) {
                ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
                InstructionList instructionList = methodGenerator.getInstructionList();
                int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.CachedNodeListIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;)V");
                instructionList.append(new NEW(constantPoolGen.addClass("org.apache.xalan.xsltc.dom.CachedNodeListIterator")));
                instructionList.append(DUP_X1);
                instructionList.append(SWAP);
                instructionList.append(new INVOKESPECIAL(n2));
            }
            this._select.startIterator(classGenerator, methodGenerator);
        } else if (this.hasContents()) {
            this.compileResultTree(classGenerator, methodGenerator);
        } else {
            ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
            InstructionList instructionList = methodGenerator.getInstructionList();
            instructionList.append(new PUSH(constantPoolGen, ""));
        }
    }
}

