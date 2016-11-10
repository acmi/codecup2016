/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.AttributeValue;
import org.apache.xalan.xsltc.compiler.AttributeValueTemplate;
import org.apache.xalan.xsltc.compiler.Choose;
import org.apache.xalan.xsltc.compiler.CopyOf;
import org.apache.xalan.xsltc.compiler.If;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.LiteralAttribute;
import org.apache.xalan.xsltc.compiler.LiteralElement;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SimpleAttributeValue;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.Text;
import org.apache.xalan.xsltc.compiler.UseAttributeSets;
import org.apache.xalan.xsltc.compiler.VariableBase;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.serializer.ElemDesc;
import org.apache.xml.utils.XML11Char;

final class XslAttribute
extends Instruction {
    private String _prefix;
    private AttributeValue _name;
    private AttributeValueTemplate _namespace = null;
    private boolean _ignore = false;
    private boolean _isLiteral = false;

    XslAttribute() {
    }

    public AttributeValue getName() {
        return this._name;
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("Attribute " + this._name);
        this.displayContents(n2 + 4);
    }

    public void parseContents(Parser parser) {
        SyntaxTreeNode syntaxTreeNode;
        boolean bl = false;
        SymbolTable symbolTable = parser.getSymbolTable();
        String string = this.getAttribute("name");
        String string2 = this.getAttribute("namespace");
        QName qName = parser.getQName(string, false);
        String string3 = qName.getPrefix();
        if (string3 != null && string3.equals("xmlns") || string.equals("xmlns")) {
            this.reportError(this, parser, "ILLEGAL_ATTR_NAME_ERR", string);
            return;
        }
        this._isLiteral = Util.isLiteral(string);
        if (this._isLiteral && !XML11Char.isXML11ValidQName(string)) {
            this.reportError(this, parser, "ILLEGAL_ATTR_NAME_ERR", string);
            return;
        }
        SyntaxTreeNode syntaxTreeNode2 = this.getParent();
        Vector vector = syntaxTreeNode2.getContents();
        for (int i2 = 0; i2 < syntaxTreeNode2.elementCount() && (syntaxTreeNode = (SyntaxTreeNode)vector.elementAt(i2)) != this; ++i2) {
            if (syntaxTreeNode instanceof XslAttribute || syntaxTreeNode instanceof UseAttributeSets || syntaxTreeNode instanceof LiteralAttribute || syntaxTreeNode instanceof Text || syntaxTreeNode instanceof If || syntaxTreeNode instanceof Choose || syntaxTreeNode instanceof CopyOf || syntaxTreeNode instanceof VariableBase) continue;
            this.reportWarning(this, parser, "STRAY_ATTRIBUTE_ERR", string);
        }
        if (string2 != null && string2 != "") {
            this._prefix = this.lookupPrefix(string2);
            this._namespace = new AttributeValueTemplate(string2, parser, this);
        } else if (string3 != null && string3 != "") {
            this._prefix = string3;
            string2 = this.lookupNamespace(string3);
            if (string2 != null) {
                this._namespace = new AttributeValueTemplate(string2, parser, this);
            }
        }
        if (this._namespace != null) {
            if (this._prefix == null || this._prefix == "") {
                if (string3 != null) {
                    this._prefix = string3;
                } else {
                    this._prefix = symbolTable.generateNamespacePrefix();
                    bl = true;
                }
            } else if (string3 != null && !string3.equals(this._prefix)) {
                this._prefix = string3;
            }
            string = this._prefix + ":" + qName.getLocalPart();
            if (syntaxTreeNode2 instanceof LiteralElement && !bl) {
                ((LiteralElement)syntaxTreeNode2).registerNamespace(this._prefix, string2, symbolTable, false);
            }
        }
        if (syntaxTreeNode2 instanceof LiteralElement) {
            ((LiteralElement)syntaxTreeNode2).addAttribute(this);
        }
        this._name = AttributeValue.create(this, string, parser);
        this.parseChildren(parser);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (!this._ignore) {
            this._name.typeCheck(symbolTable);
            if (this._namespace != null) {
                this._namespace.typeCheck(symbolTable);
            }
            this.typeCheckContents(symbolTable);
        }
        return Type.Void;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        int n2;
        Object object;
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (this._ignore) {
            return;
        }
        this._ignore = true;
        if (this._namespace != null) {
            instructionList.append(methodGenerator.loadHandler());
            instructionList.append(new PUSH(constantPoolGen, this._prefix));
            this._namespace.translate(classGenerator, methodGenerator);
            instructionList.append(methodGenerator.namespace());
        }
        if (!this._isLiteral) {
            object = methodGenerator.addLocalVariable2("nameValue", Util.getJCRefType("Ljava/lang/String;"), null);
            this._name.translate(classGenerator, methodGenerator);
            object.setStart(instructionList.append(new ASTORE(object.getIndex())));
            instructionList.append(new ALOAD(object.getIndex()));
            n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "checkAttribQName", "(Ljava/lang/String;)V");
            instructionList.append(new INVOKESTATIC(n2));
            instructionList.append(methodGenerator.loadHandler());
            instructionList.append(DUP);
            object.setEnd(instructionList.append(new ALOAD(object.getIndex())));
        } else {
            instructionList.append(methodGenerator.loadHandler());
            instructionList.append(DUP);
            this._name.translate(classGenerator, methodGenerator);
        }
        if (this.elementCount() == 1 && this.elementAt(0) instanceof Text) {
            instructionList.append(new PUSH(constantPoolGen, ((Text)this.elementAt(0)).getText()));
        } else {
            instructionList.append(classGenerator.loadTranslet());
            instructionList.append(new GETFIELD(constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "stringValueHandler", "Lorg/apache/xalan/xsltc/runtime/StringValueHandler;")));
            instructionList.append(DUP);
            instructionList.append(methodGenerator.storeHandler());
            this.translateContents(classGenerator, methodGenerator);
            instructionList.append(new INVOKEVIRTUAL(constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.StringValueHandler", "getValue", "()Ljava/lang/String;")));
        }
        object = this.getParent();
        if (object instanceof LiteralElement && ((LiteralElement)object).allAttributesUnique()) {
            n2 = 0;
            ElemDesc elemDesc = ((LiteralElement)object).getElemDesc();
            if (elemDesc != null && this._name instanceof SimpleAttributeValue) {
                String string = ((SimpleAttributeValue)this._name).toString();
                if (elemDesc.isAttrFlagSet(string, 4)) {
                    n2 |= 2;
                } else if (elemDesc.isAttrFlagSet(string, 2)) {
                    n2 |= 4;
                }
            }
            instructionList.append(new PUSH(constantPoolGen, n2));
            instructionList.append(methodGenerator.uniqueAttribute());
        } else {
            instructionList.append(methodGenerator.attribute());
        }
        instructionList.append(methodGenerator.storeHandler());
    }
}

