/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.DUP_X1;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.NEWARRAY;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.AbsolutePathPattern;
import org.apache.xalan.xsltc.compiler.ApplyTemplates;
import org.apache.xalan.xsltc.compiler.CallTemplate;
import org.apache.xalan.xsltc.compiler.Choose;
import org.apache.xalan.xsltc.compiler.Constants;
import org.apache.xalan.xsltc.compiler.If;
import org.apache.xalan.xsltc.compiler.Number;
import org.apache.xalan.xsltc.compiler.Otherwise;
import org.apache.xalan.xsltc.compiler.Param;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.RelativePathPattern;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.Template;
import org.apache.xalan.xsltc.compiler.Text;
import org.apache.xalan.xsltc.compiler.ValueOf;
import org.apache.xalan.xsltc.compiler.Variable;
import org.apache.xalan.xsltc.compiler.VariableBase;
import org.apache.xalan.xsltc.compiler.When;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.runtime.AttributeList;
import org.xml.sax.Attributes;

public abstract class SyntaxTreeNode
implements Constants {
    private Parser _parser;
    protected SyntaxTreeNode _parent;
    private Stylesheet _stylesheet;
    private Template _template;
    private final Vector _contents = new Vector(2);
    protected QName _qname;
    private int _line;
    protected AttributeList _attributes = null;
    private Hashtable _prefixMapping = null;
    public static final int UNKNOWN_STYLESHEET_NODE_ID = -1;
    private int _nodeIDForStylesheetNSLookup = -1;
    static final SyntaxTreeNode Dummy = new AbsolutePathPattern(null);
    protected static final int IndentIncrement = 4;
    private static final char[] _spaces = "                                                       ".toCharArray();

    public SyntaxTreeNode() {
        this._line = 0;
        this._qname = null;
    }

    public SyntaxTreeNode(int n2) {
        this._line = n2;
        this._qname = null;
    }

    public SyntaxTreeNode(String string, String string2, String string3) {
        this._line = 0;
        this.setQName(string, string2, string3);
    }

    protected final void setLineNumber(int n2) {
        this._line = n2;
    }

    public final int getLineNumber() {
        if (this._line > 0) {
            return this._line;
        }
        SyntaxTreeNode syntaxTreeNode = this.getParent();
        return syntaxTreeNode != null ? syntaxTreeNode.getLineNumber() : 0;
    }

    protected void setQName(QName qName) {
        this._qname = qName;
    }

    protected void setQName(String string, String string2, String string3) {
        this._qname = new QName(string, string2, string3);
    }

    protected QName getQName() {
        return this._qname;
    }

    protected void setAttributes(AttributeList attributeList) {
        this._attributes = attributeList;
    }

    protected String getAttribute(String string) {
        if (this._attributes == null) {
            return "";
        }
        String string2 = this._attributes.getValue(string);
        return string2 == null || string2.equals("") ? "" : string2;
    }

    protected String getAttribute(String string, String string2) {
        return this.getAttribute(string + ':' + string2);
    }

    protected boolean hasAttribute(String string) {
        return this._attributes != null && this._attributes.getValue(string) != null;
    }

    protected void addAttribute(String string, String string2) {
        this._attributes.add(string, string2);
    }

    protected Attributes getAttributes() {
        return this._attributes;
    }

    protected void setPrefixMapping(Hashtable hashtable) {
        this._prefixMapping = hashtable;
    }

    protected Hashtable getPrefixMapping() {
        return this._prefixMapping;
    }

    protected void addPrefixMapping(String string, String string2) {
        if (this._prefixMapping == null) {
            this._prefixMapping = new Hashtable();
        }
        this._prefixMapping.put(string, string2);
    }

    protected String lookupNamespace(String string) {
        String string2 = null;
        if (this._prefixMapping != null) {
            string2 = (String)this._prefixMapping.get(string);
        }
        if (string2 == null && this._parent != null) {
            string2 = this._parent.lookupNamespace(string);
            if (string == "" && string2 == null) {
                string2 = "";
            }
        }
        return string2;
    }

    protected String lookupPrefix(String string) {
        String string2 = null;
        if (this._prefixMapping != null && this._prefixMapping.contains(string)) {
            Enumeration enumeration = this._prefixMapping.keys();
            while (enumeration.hasMoreElements()) {
                string2 = (String)enumeration.nextElement();
                String string3 = (String)this._prefixMapping.get(string2);
                if (!string3.equals(string)) continue;
                return string2;
            }
        } else if (this._parent != null) {
            string2 = this._parent.lookupPrefix(string);
            if (string == "" && string2 == null) {
                string2 = "";
            }
        }
        return string2;
    }

    protected void setParser(Parser parser) {
        this._parser = parser;
    }

    public final Parser getParser() {
        return this._parser;
    }

    protected void setParent(SyntaxTreeNode syntaxTreeNode) {
        if (this._parent == null) {
            this._parent = syntaxTreeNode;
        }
    }

    protected final SyntaxTreeNode getParent() {
        return this._parent;
    }

    protected final boolean isDummy() {
        return this == Dummy;
    }

    protected int getImportPrecedence() {
        Stylesheet stylesheet = this.getStylesheet();
        if (stylesheet == null) {
            return Integer.MIN_VALUE;
        }
        return stylesheet.getImportPrecedence();
    }

    public Stylesheet getStylesheet() {
        if (this._stylesheet == null) {
            SyntaxTreeNode syntaxTreeNode;
            for (syntaxTreeNode = this; syntaxTreeNode != null; syntaxTreeNode = syntaxTreeNode.getParent()) {
                if (!(syntaxTreeNode instanceof Stylesheet)) continue;
                return (Stylesheet)syntaxTreeNode;
            }
            this._stylesheet = (Stylesheet)syntaxTreeNode;
        }
        return this._stylesheet;
    }

    protected Template getTemplate() {
        if (this._template == null) {
            SyntaxTreeNode syntaxTreeNode;
            for (syntaxTreeNode = this; syntaxTreeNode != null && !(syntaxTreeNode instanceof Template); syntaxTreeNode = syntaxTreeNode.getParent()) {
            }
            this._template = (Template)syntaxTreeNode;
        }
        return this._template;
    }

    protected final XSLTC getXSLTC() {
        return this._parser.getXSLTC();
    }

    protected final SymbolTable getSymbolTable() {
        return this._parser == null ? null : this._parser.getSymbolTable();
    }

    public void parseContents(Parser parser) {
        this.parseChildren(parser);
    }

    protected final void parseChildren(Parser parser) {
        int n2;
        Vector<QName> vector = null;
        int n3 = this._contents.size();
        for (n2 = 0; n2 < n3; ++n2) {
            SyntaxTreeNode syntaxTreeNode = (SyntaxTreeNode)this._contents.elementAt(n2);
            parser.getSymbolTable().setCurrentNode(syntaxTreeNode);
            syntaxTreeNode.parseContents(parser);
            QName qName = this.updateScope(parser, syntaxTreeNode);
            if (qName == null) continue;
            if (vector == null) {
                vector = new Vector<QName>(2);
            }
            vector.addElement(qName);
        }
        parser.getSymbolTable().setCurrentNode(this);
        if (vector != null) {
            n2 = vector.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                parser.removeVariable((QName)vector.elementAt(i2));
            }
        }
    }

    protected QName updateScope(Parser parser, SyntaxTreeNode syntaxTreeNode) {
        if (syntaxTreeNode instanceof Variable) {
            Variable variable = (Variable)syntaxTreeNode;
            parser.addVariable(variable);
            return variable.getName();
        }
        if (syntaxTreeNode instanceof Param) {
            Param param = (Param)syntaxTreeNode;
            parser.addParameter(param);
            return param.getName();
        }
        return null;
    }

    public abstract Type typeCheck(SymbolTable var1) throws TypeCheckError;

    protected Type typeCheckContents(SymbolTable symbolTable) throws TypeCheckError {
        int n2 = this.elementCount();
        for (int i2 = 0; i2 < n2; ++i2) {
            SyntaxTreeNode syntaxTreeNode = (SyntaxTreeNode)this._contents.elementAt(i2);
            syntaxTreeNode.typeCheck(symbolTable);
        }
        return Type.Void;
    }

    public abstract void translate(ClassGenerator var1, MethodGenerator var2);

    protected void translateContents(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        SyntaxTreeNode syntaxTreeNode;
        int n2;
        int n3 = this.elementCount();
        for (n2 = 0; n2 < n3; ++n2) {
            methodGenerator.markChunkStart();
            syntaxTreeNode = (SyntaxTreeNode)this._contents.elementAt(n2);
            syntaxTreeNode.translate(classGenerator, methodGenerator);
            methodGenerator.markChunkEnd();
        }
        for (n2 = 0; n2 < n3; ++n2) {
            if (!(this._contents.elementAt(n2) instanceof VariableBase)) continue;
            syntaxTreeNode = (VariableBase)this._contents.elementAt(n2);
            syntaxTreeNode.unmapRegister(methodGenerator);
        }
    }

    private boolean isSimpleRTF(SyntaxTreeNode syntaxTreeNode) {
        Vector vector = syntaxTreeNode.getContents();
        for (int i2 = 0; i2 < vector.size(); ++i2) {
            SyntaxTreeNode syntaxTreeNode2 = (SyntaxTreeNode)vector.elementAt(i2);
            if (this.isTextElement(syntaxTreeNode2, false)) continue;
            return false;
        }
        return true;
    }

    private boolean isAdaptiveRTF(SyntaxTreeNode syntaxTreeNode) {
        Vector vector = syntaxTreeNode.getContents();
        for (int i2 = 0; i2 < vector.size(); ++i2) {
            SyntaxTreeNode syntaxTreeNode2 = (SyntaxTreeNode)vector.elementAt(i2);
            if (this.isTextElement(syntaxTreeNode2, true)) continue;
            return false;
        }
        return true;
    }

    private boolean isTextElement(SyntaxTreeNode syntaxTreeNode, boolean bl) {
        if (syntaxTreeNode instanceof ValueOf || syntaxTreeNode instanceof Number || syntaxTreeNode instanceof Text) {
            return true;
        }
        if (syntaxTreeNode instanceof If) {
            return bl ? this.isAdaptiveRTF(syntaxTreeNode) : this.isSimpleRTF(syntaxTreeNode);
        }
        if (syntaxTreeNode instanceof Choose) {
            Vector vector = syntaxTreeNode.getContents();
            for (int i2 = 0; i2 < vector.size(); ++i2) {
                SyntaxTreeNode syntaxTreeNode2 = (SyntaxTreeNode)vector.elementAt(i2);
                if (syntaxTreeNode2 instanceof Text || (syntaxTreeNode2 instanceof When || syntaxTreeNode2 instanceof Otherwise) && (bl && this.isAdaptiveRTF(syntaxTreeNode2) || !bl && this.isSimpleRTF(syntaxTreeNode2))) continue;
                return false;
            }
            return true;
        }
        if (bl && (syntaxTreeNode instanceof CallTemplate || syntaxTreeNode instanceof ApplyTemplates)) {
            return true;
        }
        return false;
    }

    protected void compileResultTree(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        Stylesheet stylesheet = classGenerator.getStylesheet();
        boolean bl = this.isSimpleRTF(this);
        boolean bl2 = false;
        if (!bl) {
            bl2 = this.isAdaptiveRTF(this);
        }
        int n2 = bl ? 0 : (bl2 ? 1 : 2);
        instructionList.append(methodGenerator.loadHandler());
        String string = classGenerator.getDOMClass();
        instructionList.append(methodGenerator.loadDOM());
        int n3 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getResultTreeFrag", "(IIZ)Lorg/apache/xalan/xsltc/DOM;");
        instructionList.append(new PUSH(constantPoolGen, 32));
        instructionList.append(new PUSH(constantPoolGen, n2));
        instructionList.append(new PUSH(constantPoolGen, stylesheet.callsNodeset()));
        instructionList.append(new INVOKEINTERFACE(n3, 4));
        instructionList.append(DUP);
        n3 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getOutputDomBuilder", "()" + TRANSLET_OUTPUT_SIG);
        instructionList.append(new INVOKEINTERFACE(n3, 1));
        instructionList.append(DUP);
        instructionList.append(methodGenerator.storeHandler());
        instructionList.append(methodGenerator.startDocument());
        this.translateContents(classGenerator, methodGenerator);
        instructionList.append(methodGenerator.loadHandler());
        instructionList.append(methodGenerator.endDocument());
        if (stylesheet.callsNodeset() && !string.equals("org/apache/xalan/xsltc/DOM")) {
            n3 = constantPoolGen.addMethodref("org/apache/xalan/xsltc/dom/DOMAdapter", "<init>", "(Lorg/apache/xalan/xsltc/DOM;[Ljava/lang/String;[Ljava/lang/String;[I[Ljava/lang/String;)V");
            instructionList.append(new NEW(constantPoolGen.addClass("org/apache/xalan/xsltc/dom/DOMAdapter")));
            instructionList.append(new DUP_X1());
            instructionList.append(SWAP);
            if (!stylesheet.callsNodeset()) {
                instructionList.append(new ICONST(0));
                instructionList.append(new ANEWARRAY(constantPoolGen.addClass("java.lang.String")));
                instructionList.append(DUP);
                instructionList.append(DUP);
                instructionList.append(new ICONST(0));
                instructionList.append(new NEWARRAY(BasicType.INT));
                instructionList.append(SWAP);
                instructionList.append(new INVOKESPECIAL(n3));
            } else {
                instructionList.append(ALOAD_0);
                instructionList.append(new GETFIELD(constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "namesArray", "[Ljava/lang/String;")));
                instructionList.append(ALOAD_0);
                instructionList.append(new GETFIELD(constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "urisArray", "[Ljava/lang/String;")));
                instructionList.append(ALOAD_0);
                instructionList.append(new GETFIELD(constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "typesArray", "[I")));
                instructionList.append(ALOAD_0);
                instructionList.append(new GETFIELD(constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "namespaceArray", "[Ljava/lang/String;")));
                instructionList.append(new INVOKESPECIAL(n3));
                instructionList.append(DUP);
                instructionList.append(methodGenerator.loadDOM());
                instructionList.append(new CHECKCAST(constantPoolGen.addClass(classGenerator.getDOMClass())));
                instructionList.append(SWAP);
                n3 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.MultiDOM", "addDOMAdapter", "(Lorg/apache/xalan/xsltc/dom/DOMAdapter;)I");
                instructionList.append(new INVOKEVIRTUAL(n3));
                instructionList.append(POP);
            }
        }
        instructionList.append(SWAP);
        instructionList.append(methodGenerator.storeHandler());
    }

    protected final int getNodeIDForStylesheetNSLookup() {
        if (this._nodeIDForStylesheetNSLookup == -1) {
            Hashtable hashtable = this.getPrefixMapping();
            int n2 = this._parent != null ? this._parent.getNodeIDForStylesheetNSLookup() : -1;
            this._nodeIDForStylesheetNSLookup = hashtable == null ? n2 : this.getXSLTC().registerStylesheetPrefixMappingForRuntime(hashtable, n2);
        }
        return this._nodeIDForStylesheetNSLookup;
    }

    protected boolean contextDependent() {
        return true;
    }

    protected boolean dependentContents() {
        int n2 = this.elementCount();
        for (int i2 = 0; i2 < n2; ++i2) {
            SyntaxTreeNode syntaxTreeNode = (SyntaxTreeNode)this._contents.elementAt(i2);
            if (!syntaxTreeNode.contextDependent()) continue;
            return true;
        }
        return false;
    }

    protected final void addElement(SyntaxTreeNode syntaxTreeNode) {
        this._contents.addElement(syntaxTreeNode);
        syntaxTreeNode.setParent(this);
    }

    protected final void setFirstElement(SyntaxTreeNode syntaxTreeNode) {
        this._contents.insertElementAt(syntaxTreeNode, 0);
        syntaxTreeNode.setParent(this);
    }

    protected final void removeElement(SyntaxTreeNode syntaxTreeNode) {
        this._contents.remove(syntaxTreeNode);
        syntaxTreeNode.setParent(null);
    }

    protected final Vector getContents() {
        return this._contents;
    }

    protected final boolean hasContents() {
        return this.elementCount() > 0;
    }

    protected final int elementCount() {
        return this._contents.size();
    }

    protected final Enumeration elements() {
        return this._contents.elements();
    }

    protected final Object elementAt(int n2) {
        return this._contents.elementAt(n2);
    }

    protected final SyntaxTreeNode lastChild() {
        if (this._contents.size() == 0) {
            return null;
        }
        return (SyntaxTreeNode)this._contents.lastElement();
    }

    public void display(int n2) {
        this.displayContents(n2);
    }

    protected void displayContents(int n2) {
        int n3 = this.elementCount();
        for (int i2 = 0; i2 < n3; ++i2) {
            SyntaxTreeNode syntaxTreeNode = (SyntaxTreeNode)this._contents.elementAt(i2);
            syntaxTreeNode.display(n2);
        }
    }

    protected final void indent(int n2) {
        System.out.print(new String(_spaces, 0, n2));
    }

    protected void reportError(SyntaxTreeNode syntaxTreeNode, Parser parser, String string, String string2) {
        ErrorMsg errorMsg = new ErrorMsg(string, (Object)string2, syntaxTreeNode);
        parser.reportError(3, errorMsg);
    }

    protected void reportWarning(SyntaxTreeNode syntaxTreeNode, Parser parser, String string, String string2) {
        ErrorMsg errorMsg = new ErrorMsg(string, (Object)string2, syntaxTreeNode);
        parser.reportError(4, errorMsg);
    }
}

