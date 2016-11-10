/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.LiteralElement;
import org.apache.xalan.xsltc.compiler.Param;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.Pattern;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.TopLevelElement;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NamedMethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.XML11Char;

public final class Template
extends TopLevelElement {
    private QName _name;
    private QName _mode;
    private Pattern _pattern;
    private double _priority;
    private int _position;
    private boolean _disabled = false;
    private boolean _compiled = false;
    private boolean _simplified = false;
    private boolean _isSimpleNamedTemplate = false;
    private Vector _parameters = new Vector();
    private Stylesheet _stylesheet = null;

    public boolean hasParams() {
        return this._parameters.size() > 0;
    }

    public boolean isSimplified() {
        return this._simplified;
    }

    public void setSimplified() {
        this._simplified = true;
    }

    public boolean isSimpleNamedTemplate() {
        return this._isSimpleNamedTemplate;
    }

    public void addParameter(Param param) {
        this._parameters.addElement(param);
    }

    public Vector getParameters() {
        return this._parameters;
    }

    public void disable() {
        this._disabled = true;
    }

    public boolean disabled() {
        return this._disabled;
    }

    public double getPriority() {
        return this._priority;
    }

    public int getPosition() {
        return this._position;
    }

    public boolean isNamed() {
        return this._name != null;
    }

    public Pattern getPattern() {
        return this._pattern;
    }

    public QName getName() {
        return this._name;
    }

    public void setName(QName qName) {
        if (this._name == null) {
            this._name = qName;
        }
    }

    public QName getModeName() {
        return this._mode;
    }

    public int compareTo(Object object) {
        Template template = (Template)object;
        if (this._priority > template._priority) {
            return 1;
        }
        if (this._priority < template._priority) {
            return -1;
        }
        if (this._position > template._position) {
            return 1;
        }
        if (this._position < template._position) {
            return -1;
        }
        return 0;
    }

    public void display(int n2) {
        Util.println('\n');
        this.indent(n2);
        if (this._name != null) {
            this.indent(n2);
            Util.println("name = " + this._name);
        } else if (this._pattern != null) {
            this.indent(n2);
            Util.println("match = " + this._pattern.toString());
        }
        if (this._mode != null) {
            this.indent(n2);
            Util.println("mode = " + this._mode);
        }
        this.displayContents(n2 + 4);
    }

    private boolean resolveNamedTemplates(Template template, Parser parser) {
        int n2;
        if (template == null) {
            return true;
        }
        SymbolTable symbolTable = parser.getSymbolTable();
        int n3 = this.getImportPrecedence();
        if (n3 > (n2 = template.getImportPrecedence())) {
            template.disable();
            return true;
        }
        if (n3 < n2) {
            symbolTable.addTemplate(template);
            this.disable();
            return true;
        }
        return false;
    }

    public Stylesheet getStylesheet() {
        return this._stylesheet;
    }

    public void parseContents(Parser parser) {
        Object object;
        String string = this.getAttribute("name");
        String string2 = this.getAttribute("mode");
        String string3 = this.getAttribute("match");
        String string4 = this.getAttribute("priority");
        this._stylesheet = super.getStylesheet();
        if (string.length() > 0) {
            if (!XML11Char.isXML11ValidQName(string)) {
                object = new ErrorMsg("INVALID_QNAME_ERR", (Object)string, this);
                parser.reportError(3, (ErrorMsg)object);
            }
            this._name = parser.getQNameIgnoreDefaultNs(string);
        }
        if (string2.length() > 0) {
            if (!XML11Char.isXML11ValidQName(string2)) {
                object = new ErrorMsg("INVALID_QNAME_ERR", (Object)string2, this);
                parser.reportError(3, (ErrorMsg)object);
            }
            this._mode = parser.getQNameIgnoreDefaultNs(string2);
        }
        if (string3.length() > 0) {
            this._pattern = parser.parsePattern(this, "match", null);
        }
        this._priority = string4.length() > 0 ? Double.parseDouble(string4) : (this._pattern != null ? this._pattern.getPriority() : Double.NaN);
        this._position = parser.getTemplateIndex();
        if (this._name != null) {
            object = parser.getSymbolTable().addTemplate(this);
            if (!this.resolveNamedTemplates((Template)object, parser)) {
                ErrorMsg errorMsg = new ErrorMsg("TEMPLATE_REDEF_ERR", (Object)this._name, this);
                parser.reportError(3, errorMsg);
            }
            if (this._pattern == null && this._mode == null) {
                this._isSimpleNamedTemplate = true;
            }
        }
        if (this._parent instanceof Stylesheet) {
            ((Stylesheet)this._parent).addTemplate(this);
        }
        parser.setTemplate(this);
        this.parseChildren(parser);
        parser.setTemplate(null);
    }

    public void parseSimplified(Stylesheet stylesheet, Parser parser) {
        this._stylesheet = stylesheet;
        this.setParent(stylesheet);
        this._name = null;
        this._mode = null;
        this._priority = Double.NaN;
        this._pattern = parser.parsePattern(this, "/");
        Vector vector = this._stylesheet.getContents();
        SyntaxTreeNode syntaxTreeNode = (SyntaxTreeNode)vector.elementAt(0);
        if (syntaxTreeNode instanceof LiteralElement) {
            this.addElement(syntaxTreeNode);
            syntaxTreeNode.setParent(this);
            vector.set(0, this);
            parser.setTemplate(this);
            syntaxTreeNode.parseContents(parser);
            parser.setTemplate(null);
        }
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (this._pattern != null) {
            this._pattern.typeCheck(symbolTable);
        }
        return this.typeCheckContents(symbolTable);
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (this._disabled) {
            return;
        }
        String string = classGenerator.getClassName();
        if (this._compiled && this.isNamed()) {
            String string2 = Util.escape(this._name.toString());
            instructionList.append(classGenerator.loadTranslet());
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(methodGenerator.loadIterator());
            instructionList.append(methodGenerator.loadHandler());
            instructionList.append(methodGenerator.loadCurrentNode());
            instructionList.append(new INVOKEVIRTUAL(constantPoolGen.addMethodref(string, string2, "(Lorg/apache/xalan/xsltc/DOM;Lorg/apache/xml/dtm/DTMAxisIterator;" + TRANSLET_OUTPUT_SIG + "I)V")));
            return;
        }
        if (this._compiled) {
            return;
        }
        this._compiled = true;
        if (this._isSimpleNamedTemplate && methodGenerator instanceof NamedMethodGenerator) {
            int n2 = this._parameters.size();
            NamedMethodGenerator namedMethodGenerator = (NamedMethodGenerator)methodGenerator;
            for (int i2 = 0; i2 < n2; ++i2) {
                Param param = (Param)this._parameters.elementAt(i2);
                param.setLoadInstruction(namedMethodGenerator.loadParameter(i2));
                param.setStoreInstruction(namedMethodGenerator.storeParameter(i2));
            }
        }
        this.translateContents(classGenerator, methodGenerator);
        instructionList.setPositions(true);
    }
}

