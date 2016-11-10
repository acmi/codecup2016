/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.ApplyTemplates;
import org.apache.xalan.xsltc.compiler.AttributeValue;
import org.apache.xalan.xsltc.compiler.AttributeValueTemplate;
import org.apache.xalan.xsltc.compiler.CallTemplate;
import org.apache.xalan.xsltc.compiler.Choose;
import org.apache.xalan.xsltc.compiler.Comment;
import org.apache.xalan.xsltc.compiler.Copy;
import org.apache.xalan.xsltc.compiler.CopyOf;
import org.apache.xalan.xsltc.compiler.ForEach;
import org.apache.xalan.xsltc.compiler.If;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.LiteralAttribute;
import org.apache.xalan.xsltc.compiler.Number;
import org.apache.xalan.xsltc.compiler.Otherwise;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.ProcessingInstruction;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SimpleAttributeValue;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.Text;
import org.apache.xalan.xsltc.compiler.UseAttributeSets;
import org.apache.xalan.xsltc.compiler.ValueOf;
import org.apache.xalan.xsltc.compiler.Variable;
import org.apache.xalan.xsltc.compiler.When;
import org.apache.xalan.xsltc.compiler.XslAttribute;
import org.apache.xalan.xsltc.compiler.XslElement;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xalan.xsltc.runtime.AttributeList;
import org.apache.xml.serializer.ElemDesc;
import org.apache.xml.serializer.ToHTMLStream;

final class LiteralElement
extends Instruction {
    private String _name;
    private LiteralElement _literalElemParent = null;
    private Vector _attributeElements = null;
    private Hashtable _accessedPrefixes = null;
    private boolean _allAttributesUnique = false;
    private static final String XMLNS_STRING = "xmlns";

    LiteralElement() {
    }

    public QName getName() {
        return this._qname;
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("LiteralElement name = " + this._name);
        this.displayContents(n2 + 4);
    }

    private String accessedNamespace(String string) {
        String string2;
        if (this._literalElemParent != null && (string2 = this._literalElemParent.accessedNamespace(string)) != null) {
            return string2;
        }
        return this._accessedPrefixes != null ? (String)this._accessedPrefixes.get(string) : null;
    }

    public void registerNamespace(String string, String string2, SymbolTable symbolTable, boolean bl) {
        String string3;
        if (this._literalElemParent != null && (string3 = this._literalElemParent.accessedNamespace(string)) != null && string3.equals(string2)) {
            return;
        }
        if (this._accessedPrefixes == null) {
            this._accessedPrefixes = new Hashtable();
        } else if (!bl && (string3 = (String)this._accessedPrefixes.get(string)) != null) {
            if (string3.equals(string2)) {
                return;
            }
            string = symbolTable.generateNamespacePrefix();
        }
        if (!string.equals("xml")) {
            this._accessedPrefixes.put(string, string2);
        }
    }

    private String translateQName(QName qName, SymbolTable symbolTable) {
        String string;
        String string2 = qName.getLocalPart();
        String string3 = qName.getPrefix();
        if (string3 == null) {
            string3 = "";
        } else if (string3.equals("xmlns")) {
            return "xmlns";
        }
        String string4 = symbolTable.lookupPrefixAlias(string3);
        if (string4 != null) {
            symbolTable.excludeNamespaces(string3);
            string3 = string4;
        }
        if ((string = this.lookupNamespace(string3)) == null) {
            return string2;
        }
        this.registerNamespace(string3, string, symbolTable, false);
        if (string3 != "") {
            return string3 + ":" + string2;
        }
        return string2;
    }

    public void addAttribute(SyntaxTreeNode syntaxTreeNode) {
        if (this._attributeElements == null) {
            this._attributeElements = new Vector(2);
        }
        this._attributeElements.add(syntaxTreeNode);
    }

    public void setFirstAttribute(SyntaxTreeNode syntaxTreeNode) {
        if (this._attributeElements == null) {
            this._attributeElements = new Vector(2);
        }
        this._attributeElements.insertElementAt(syntaxTreeNode, 0);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (this._attributeElements != null) {
            int n2 = this._attributeElements.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                SyntaxTreeNode syntaxTreeNode = (SyntaxTreeNode)this._attributeElements.elementAt(i2);
                syntaxTreeNode.typeCheck(symbolTable);
            }
        }
        this.typeCheckContents(symbolTable);
        return Type.Void;
    }

    public Enumeration getNamespaceScope(SyntaxTreeNode syntaxTreeNode) {
        Hashtable hashtable = new Hashtable();
        while (syntaxTreeNode != null) {
            Hashtable hashtable2 = syntaxTreeNode.getPrefixMapping();
            if (hashtable2 != null) {
                Enumeration enumeration = hashtable2.keys();
                while (enumeration.hasMoreElements()) {
                    String string = (String)enumeration.nextElement();
                    if (hashtable.containsKey(string)) continue;
                    hashtable.put(string, hashtable2.get(string));
                }
            }
            syntaxTreeNode = syntaxTreeNode.getParent();
        }
        return hashtable.keys();
    }

    public void parseContents(Parser parser) {
        Object object;
        String string;
        Object object2;
        SymbolTable symbolTable = parser.getSymbolTable();
        symbolTable.setCurrentNode(this);
        SyntaxTreeNode syntaxTreeNode = this.getParent();
        if (syntaxTreeNode != null && syntaxTreeNode instanceof LiteralElement) {
            this._literalElemParent = (LiteralElement)syntaxTreeNode;
        }
        this._name = this.translateQName(this._qname, symbolTable);
        int n2 = this._attributes.getLength();
        for (int i2 = 0; i2 < n2; ++i2) {
            Object object3;
            object = parser.getQName(this._attributes.getQName(i2));
            object2 = object.getNamespace();
            string = this._attributes.getValue(i2);
            if (object.equals(parser.getUseAttributeSets())) {
                if (!Util.isValidQNames(string)) {
                    object3 = new ErrorMsg("INVALID_QNAME_ERR", (Object)string, this);
                    parser.reportError(3, (ErrorMsg)object3);
                }
                this.setFirstAttribute(new UseAttributeSets(string, parser));
                continue;
            }
            if (object.equals(parser.getExtensionElementPrefixes())) {
                symbolTable.excludeNamespaces(string);
                continue;
            }
            if (object.equals(parser.getExcludeResultPrefixes())) {
                symbolTable.excludeNamespaces(string);
                continue;
            }
            object3 = object.getPrefix();
            if (object3 != null && object3.equals("xmlns") || object3 == null && object.getLocalPart().equals("xmlns") || object2 != null && object2.equals("http://www.w3.org/1999/XSL/Transform")) continue;
            String string2 = this.translateQName((QName)object, symbolTable);
            LiteralAttribute literalAttribute = new LiteralAttribute(string2, string, parser, this);
            this.addAttribute(literalAttribute);
            literalAttribute.setParent(this);
            literalAttribute.parseContents(parser);
        }
        Enumeration enumeration = this.getNamespaceScope(this);
        while (enumeration.hasMoreElements()) {
            object = (String)enumeration.nextElement();
            if (object.equals("xml") || (object2 = this.lookupNamespace((String)object)) == null || symbolTable.isExcludedNamespace((String)object2)) continue;
            this.registerNamespace((String)object, (String)object2, symbolTable, true);
        }
        this.parseChildren(parser);
        for (int i3 = 0; i3 < n2; ++i3) {
            object2 = parser.getQName(this._attributes.getQName(i3));
            string = this._attributes.getValue(i3);
            if (object2.equals(parser.getExtensionElementPrefixes())) {
                symbolTable.unExcludeNamespaces(string);
                continue;
            }
            if (!object2.equals(parser.getExcludeResultPrefixes())) continue;
            symbolTable.unExcludeNamespaces(string);
        }
    }

    protected boolean contextDependent() {
        return this.dependentContents();
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        SyntaxTreeNode syntaxTreeNode /* !! */ ;
        Object object;
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        this._allAttributesUnique = this.checkAttributesUnique();
        instructionList.append(methodGenerator.loadHandler());
        instructionList.append(new PUSH(constantPoolGen, this._name));
        instructionList.append(DUP2);
        instructionList.append(methodGenerator.startElement());
        for (int i2 = 0; i2 < this.elementCount(); ++i2) {
            syntaxTreeNode /* !! */  = (SyntaxTreeNode)this.elementAt(i2);
            if (!(syntaxTreeNode /* !! */  instanceof Variable)) continue;
            syntaxTreeNode /* !! */ .translate(classGenerator, methodGenerator);
        }
        if (this._accessedPrefixes != null) {
            boolean bl;
            boolean bl2 = false;
            Enumeration i3 = this._accessedPrefixes.keys();
            while (i3.hasMoreElements()) {
                object = (String)i3.nextElement();
                String string = (String)this._accessedPrefixes.get(object);
                if (string == "" && object == "") continue;
                if (object == "") {
                    bl = true;
                }
                instructionList.append(methodGenerator.loadHandler());
                instructionList.append(new PUSH(constantPoolGen, (String)object));
                instructionList.append(new PUSH(constantPoolGen, string));
                instructionList.append(methodGenerator.namespace());
            }
            if (!bl && this._parent instanceof XslElement && ((XslElement)this._parent).declaresDefaultNS()) {
                instructionList.append(methodGenerator.loadHandler());
                instructionList.append(new PUSH(constantPoolGen, ""));
                instructionList.append(new PUSH(constantPoolGen, ""));
                instructionList.append(methodGenerator.namespace());
            }
        }
        if (this._attributeElements != null) {
            syntaxTreeNode /* !! */  = (SyntaxTreeNode)this._attributeElements.size();
            for (int i2 = 0; i2 < syntaxTreeNode /* !! */ ; ++i2) {
                object = (SyntaxTreeNode)this._attributeElements.elementAt(i2);
                if (object instanceof XslAttribute) continue;
                object.translate(classGenerator, methodGenerator);
            }
        }
        this.translateContents(classGenerator, methodGenerator);
        instructionList.append(methodGenerator.endElement());
    }

    private boolean isHTMLOutput() {
        return this.getStylesheet().getOutputMethod() == 2;
    }

    public ElemDesc getElemDesc() {
        if (this.isHTMLOutput()) {
            return ToHTMLStream.getElemDesc(this._name);
        }
        return null;
    }

    public boolean allAttributesUnique() {
        return this._allAttributesUnique;
    }

    private boolean checkAttributesUnique() {
        boolean bl = this.canProduceAttributeNodes(this, true);
        if (bl) {
            return false;
        }
        if (this._attributeElements != null) {
            int n2 = this._attributeElements.size();
            Hashtable<String, Instruction> hashtable = null;
            for (int i2 = 0; i2 < n2; ++i2) {
                XslAttribute xslAttribute;
                SyntaxTreeNode syntaxTreeNode32;
                SyntaxTreeNode syntaxTreeNode2 = (SyntaxTreeNode)this._attributeElements.elementAt(i2);
                if (syntaxTreeNode2 instanceof UseAttributeSets) {
                    return false;
                }
                if (!(syntaxTreeNode2 instanceof XslAttribute)) continue;
                if (hashtable == null) {
                    hashtable = new Hashtable<String, Instruction>();
                    for (int i3 = 0; i3 < i2; ++i3) {
                        SyntaxTreeNode syntaxTreeNode32 = (SyntaxTreeNode)this._attributeElements.elementAt(i3);
                        if (!(syntaxTreeNode32 instanceof LiteralAttribute)) continue;
                        LiteralAttribute literalAttribute = (LiteralAttribute)syntaxTreeNode32;
                        hashtable.put(literalAttribute.getName(), literalAttribute);
                    }
                }
                if ((syntaxTreeNode32 = (xslAttribute = (XslAttribute)syntaxTreeNode2).getName()) instanceof AttributeValueTemplate) {
                    return false;
                }
                if (!(syntaxTreeNode32 instanceof SimpleAttributeValue)) continue;
                SimpleAttributeValue simpleAttributeValue = (SimpleAttributeValue)syntaxTreeNode32;
                String string = simpleAttributeValue.toString();
                if (string != null && hashtable.get(string) != null) {
                    return false;
                }
                if (string == null) continue;
                hashtable.put(string, xslAttribute);
            }
        }
        return true;
    }

    private boolean canProduceAttributeNodes(SyntaxTreeNode syntaxTreeNode, boolean bl) {
        Vector vector = syntaxTreeNode.getContents();
        int n2 = vector.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Object object;
            SyntaxTreeNode syntaxTreeNode2 = (SyntaxTreeNode)vector.elementAt(i2);
            if (syntaxTreeNode2 instanceof Text) {
                object = (Text)syntaxTreeNode2;
                if (object.isIgnore()) continue;
                return false;
            }
            if (syntaxTreeNode2 instanceof LiteralElement || syntaxTreeNode2 instanceof ValueOf || syntaxTreeNode2 instanceof XslElement || syntaxTreeNode2 instanceof Comment || syntaxTreeNode2 instanceof Number || syntaxTreeNode2 instanceof ProcessingInstruction) {
                return false;
            }
            if (syntaxTreeNode2 instanceof XslAttribute) {
                if (bl) continue;
                return true;
            }
            if (syntaxTreeNode2 instanceof CallTemplate || syntaxTreeNode2 instanceof ApplyTemplates || syntaxTreeNode2 instanceof Copy || syntaxTreeNode2 instanceof CopyOf) {
                return true;
            }
            if ((syntaxTreeNode2 instanceof If || syntaxTreeNode2 instanceof ForEach) && this.canProduceAttributeNodes(syntaxTreeNode2, false)) {
                return true;
            }
            if (!(syntaxTreeNode2 instanceof Choose)) continue;
            object = syntaxTreeNode2.getContents();
            int n3 = object.size();
            for (int i3 = 0; i3 < n3; ++i3) {
                SyntaxTreeNode syntaxTreeNode3 = (SyntaxTreeNode)object.elementAt(i3);
                if (!(syntaxTreeNode3 instanceof When) && !(syntaxTreeNode3 instanceof Otherwise) || !this.canProduceAttributeNodes(syntaxTreeNode3, false)) continue;
                return true;
            }
        }
        return false;
    }
}

