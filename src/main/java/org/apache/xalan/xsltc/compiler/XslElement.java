/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.AttributeValueTemplate;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.UseAttributeSets;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.XslAttribute;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.XML11Char;

final class XslElement
extends Instruction {
    private String _prefix;
    private boolean _ignore = false;
    private boolean _isLiteralName = true;
    private AttributeValueTemplate _name;
    private AttributeValueTemplate _namespace;

    XslElement() {
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("Element " + this._name);
        this.displayContents(n2 + 4);
    }

    public boolean declaresDefaultNS() {
        return false;
    }

    public void parseContents(Parser parser) {
        Object object;
        Object object2;
        SymbolTable symbolTable = parser.getSymbolTable();
        String string = this.getAttribute("name");
        if (string == "") {
            ErrorMsg errorMsg = new ErrorMsg("ILLEGAL_ELEM_NAME_ERR", (Object)string, this);
            parser.reportError(4, errorMsg);
            this.parseChildren(parser);
            this._ignore = true;
            return;
        }
        String string2 = this.getAttribute("namespace");
        this._isLiteralName = Util.isLiteral(string);
        if (this._isLiteralName) {
            if (!XML11Char.isXML11ValidQName(string)) {
                ErrorMsg errorMsg = new ErrorMsg("ILLEGAL_ELEM_NAME_ERR", (Object)string, this);
                parser.reportError(4, errorMsg);
                this.parseChildren(parser);
                this._ignore = true;
                return;
            }
            object = parser.getQNameSafe(string);
            object2 = object.getPrefix();
            String string3 = object.getLocalPart();
            if (object2 == null) {
                object2 = "";
            }
            if (!this.hasAttribute("namespace")) {
                string2 = this.lookupNamespace((String)object2);
                if (string2 == null) {
                    ErrorMsg errorMsg = new ErrorMsg("NAMESPACE_UNDEF_ERR", object2, this);
                    parser.reportError(4, errorMsg);
                    this.parseChildren(parser);
                    this._ignore = true;
                    return;
                }
                this._prefix = object2;
                this._namespace = new AttributeValueTemplate(string2, parser, this);
            } else {
                if (object2 == "") {
                    if (Util.isLiteral(string2) && (object2 = this.lookupPrefix(string2)) == null) {
                        object2 = symbolTable.generateNamespacePrefix();
                    }
                    StringBuffer stringBuffer = new StringBuffer((String)object2);
                    if (object2 != "") {
                        stringBuffer.append(':');
                    }
                    string = stringBuffer.append(string3).toString();
                }
                this._prefix = object2;
                this._namespace = new AttributeValueTemplate(string2, parser, this);
            }
        } else {
            this._namespace = string2 == "" ? null : new AttributeValueTemplate(string2, parser, this);
        }
        this._name = new AttributeValueTemplate(string, parser, this);
        object = this.getAttribute("use-attribute-sets");
        if (object.length() > 0) {
            if (!Util.isValidQNames((String)object)) {
                object2 = new ErrorMsg("INVALID_QNAME_ERR", object, this);
                parser.reportError(3, (ErrorMsg)object2);
            }
            this.setFirstElement(new UseAttributeSets((String)object, parser));
        }
        this.parseChildren(parser);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (!this._ignore) {
            this._name.typeCheck(symbolTable);
            if (this._namespace != null) {
                this._namespace.typeCheck(symbolTable);
            }
        }
        this.typeCheckContents(symbolTable);
        return Type.Void;
    }

    public void translateLiteral(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (!this._ignore) {
            instructionList.append(methodGenerator.loadHandler());
            this._name.translate(classGenerator, methodGenerator);
            instructionList.append(DUP2);
            instructionList.append(methodGenerator.startElement());
            if (this._namespace != null) {
                instructionList.append(methodGenerator.loadHandler());
                instructionList.append(new PUSH(constantPoolGen, this._prefix));
                this._namespace.translate(classGenerator, methodGenerator);
                instructionList.append(methodGenerator.namespace());
            }
        }
        this.translateContents(classGenerator, methodGenerator);
        if (!this._ignore) {
            instructionList.append(methodGenerator.endElement());
        }
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Object var3_3 = null;
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (this._isLiteralName) {
            this.translateLiteral(classGenerator, methodGenerator);
            return;
        }
        if (!this._ignore) {
            LocalVariableGen localVariableGen = methodGenerator.addLocalVariable2("nameValue", Util.getJCRefType("Ljava/lang/String;"), null);
            this._name.translate(classGenerator, methodGenerator);
            localVariableGen.setStart(instructionList.append(new ASTORE(localVariableGen.getIndex())));
            instructionList.append(new ALOAD(localVariableGen.getIndex()));
            int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "checkQName", "(Ljava/lang/String;)V");
            instructionList.append(new INVOKESTATIC(n2));
            instructionList.append(methodGenerator.loadHandler());
            localVariableGen.setEnd(instructionList.append(new ALOAD(localVariableGen.getIndex())));
            if (this._namespace != null) {
                this._namespace.translate(classGenerator, methodGenerator);
            } else {
                String string = this.getXSLTC().getClassName();
                instructionList.append(DUP);
                instructionList.append(new PUSH(constantPoolGen, this.getNodeIDForStylesheetNSLookup()));
                instructionList.append(new GETSTATIC(constantPoolGen.addFieldref(string, "_sNamespaceAncestorsArray", "[I")));
                instructionList.append(new GETSTATIC(constantPoolGen.addFieldref(string, "_sPrefixURIsIdxArray", "[I")));
                instructionList.append(new GETSTATIC(constantPoolGen.addFieldref(string, "_sPrefixURIPairsArray", "[Ljava/lang/String;")));
                instructionList.append(ICONST_0);
                instructionList.append(new INVOKESTATIC(constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "lookupStylesheetQNameNamespace", "(Ljava/lang/String;I[I[I[Ljava/lang/String;Z)Ljava/lang/String;")));
            }
            instructionList.append(methodGenerator.loadHandler());
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(methodGenerator.loadCurrentNode());
            instructionList.append(new INVOKESTATIC(constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "startXslElement", "(Ljava/lang/String;Ljava/lang/String;" + TRANSLET_OUTPUT_SIG + "Lorg/apache/xalan/xsltc/DOM;" + "I)" + "Ljava/lang/String;")));
        }
        this.translateContents(classGenerator, methodGenerator);
        if (!this._ignore) {
            instructionList.append(methodGenerator.endElement());
        }
    }

    public void translateContents(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        int n2 = this.elementCount();
        for (int i2 = 0; i2 < n2; ++i2) {
            SyntaxTreeNode syntaxTreeNode = (SyntaxTreeNode)this.getContents().elementAt(i2);
            if (this._ignore && syntaxTreeNode instanceof XslAttribute) continue;
            syntaxTreeNode.translate(classGenerator, methodGenerator);
        }
    }
}

