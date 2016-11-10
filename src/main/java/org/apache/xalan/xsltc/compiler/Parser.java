/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import b.a.a;
import b.a.b;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.xalan.xsltc.compiler.CompilerException;
import org.apache.xalan.xsltc.compiler.Constants;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.LiteralElement;
import org.apache.xalan.xsltc.compiler.ObjectFactory;
import org.apache.xalan.xsltc.compiler.Output;
import org.apache.xalan.xsltc.compiler.Param;
import org.apache.xalan.xsltc.compiler.Pattern;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.Template;
import org.apache.xalan.xsltc.compiler.Text;
import org.apache.xalan.xsltc.compiler.UnsupportedElement;
import org.apache.xalan.xsltc.compiler.Variable;
import org.apache.xalan.xsltc.compiler.VariableBase;
import org.apache.xalan.xsltc.compiler.XPathLexer;
import org.apache.xalan.xsltc.compiler.XPathParser;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.runtime.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

public class Parser
implements Constants,
ContentHandler {
    private static final String XSL = "xsl";
    private static final String TRANSLET = "translet";
    private Locator _locator = null;
    private XSLTC _xsltc;
    private XPathParser _xpathParser;
    private Vector _errors;
    private Vector _warnings;
    private Hashtable _instructionClasses;
    private Hashtable _instructionAttrs;
    private Hashtable _qNames;
    private Hashtable _namespaces;
    private QName _useAttributeSets;
    private QName _excludeResultPrefixes;
    private QName _extensionElementPrefixes;
    private Hashtable _variableScope;
    private Stylesheet _currentStylesheet;
    private SymbolTable _symbolTable;
    private Output _output;
    private Template _template;
    private boolean _rootNamespaceDef;
    private SyntaxTreeNode _root;
    private String _target;
    private int _currentImportPrecedence;
    private String _PImedia = null;
    private String _PItitle = null;
    private String _PIcharset = null;
    private int _templateIndex = 0;
    private boolean versionIsOne = true;
    private Stack _parentStack = null;
    private Hashtable _prefixMapping = null;

    public Parser(XSLTC xSLTC) {
        this._xsltc = xSLTC;
    }

    public void init() {
        this._qNames = new Hashtable(512);
        this._namespaces = new Hashtable();
        this._instructionClasses = new Hashtable();
        this._instructionAttrs = new Hashtable();
        this._variableScope = new Hashtable();
        this._template = null;
        this._errors = new Vector();
        this._warnings = new Vector();
        this._symbolTable = new SymbolTable();
        this._xpathParser = new XPathParser(this);
        this._currentStylesheet = null;
        this._output = null;
        this._root = null;
        this._rootNamespaceDef = false;
        this._currentImportPrecedence = 1;
        this.initStdClasses();
        this.initInstructionAttrs();
        this.initExtClasses();
        this.initSymbolTable();
        this._useAttributeSets = this.getQName("http://www.w3.org/1999/XSL/Transform", "xsl", "use-attribute-sets");
        this._excludeResultPrefixes = this.getQName("http://www.w3.org/1999/XSL/Transform", "xsl", "exclude-result-prefixes");
        this._extensionElementPrefixes = this.getQName("http://www.w3.org/1999/XSL/Transform", "xsl", "extension-element-prefixes");
    }

    public void setOutput(Output output) {
        if (this._output != null) {
            if (this._output.getImportPrecedence() <= output.getImportPrecedence()) {
                String string = this._output.getCdata();
                output.mergeOutput(this._output);
                this._output.disable();
                this._output = output;
            } else {
                output.disable();
            }
        } else {
            this._output = output;
        }
    }

    public Output getOutput() {
        return this._output;
    }

    public Properties getOutputProperties() {
        return this.getTopLevelStylesheet().getOutputProperties();
    }

    public void addVariable(Variable variable) {
        this.addVariableOrParam(variable);
    }

    public void addParameter(Param param) {
        this.addVariableOrParam(param);
    }

    private void addVariableOrParam(VariableBase variableBase) {
        Object v2 = this._variableScope.get(variableBase.getName());
        if (v2 != null) {
            if (v2 instanceof Stack) {
                Stack stack = (Stack)v2;
                stack.push(variableBase);
            } else if (v2 instanceof VariableBase) {
                Stack<Object> stack = new Stack<Object>();
                stack.push(v2);
                stack.push(variableBase);
                this._variableScope.put(variableBase.getName(), stack);
            }
        } else {
            this._variableScope.put(variableBase.getName(), variableBase);
        }
    }

    public void removeVariable(QName qName) {
        Object v2 = this._variableScope.get(qName);
        if (v2 instanceof Stack) {
            Stack stack = (Stack)v2;
            if (!stack.isEmpty()) {
                stack.pop();
            }
            if (!stack.isEmpty()) {
                return;
            }
        }
        this._variableScope.remove(qName);
    }

    public VariableBase lookupVariable(QName qName) {
        Object v2 = this._variableScope.get(qName);
        if (v2 instanceof VariableBase) {
            return (VariableBase)v2;
        }
        if (v2 instanceof Stack) {
            Stack stack = (Stack)v2;
            return (VariableBase)stack.peek();
        }
        return null;
    }

    public void setXSLTC(XSLTC xSLTC) {
        this._xsltc = xSLTC;
    }

    public XSLTC getXSLTC() {
        return this._xsltc;
    }

    public int getCurrentImportPrecedence() {
        return this._currentImportPrecedence;
    }

    public int getNextImportPrecedence() {
        return ++this._currentImportPrecedence;
    }

    public void setCurrentStylesheet(Stylesheet stylesheet) {
        this._currentStylesheet = stylesheet;
    }

    public Stylesheet getCurrentStylesheet() {
        return this._currentStylesheet;
    }

    public Stylesheet getTopLevelStylesheet() {
        return this._xsltc.getStylesheet();
    }

    public QName getQNameSafe(String string) {
        int n2 = string.lastIndexOf(58);
        if (n2 != -1) {
            String string2 = string.substring(0, n2);
            String string3 = string.substring(n2 + 1);
            String string4 = null;
            if (!string2.equals("xmlns") && (string4 = this._symbolTable.lookupNamespace(string2)) == null) {
                string4 = "";
            }
            return this.getQName(string4, string2, string3);
        }
        String string5 = string.equals("xmlns") ? null : this._symbolTable.lookupNamespace("");
        return this.getQName(string5, null, string);
    }

    public QName getQName(String string) {
        return this.getQName(string, true, false);
    }

    public QName getQNameIgnoreDefaultNs(String string) {
        return this.getQName(string, true, true);
    }

    public QName getQName(String string, boolean bl) {
        return this.getQName(string, bl, false);
    }

    private QName getQName(String string, boolean bl, boolean bl2) {
        int n2 = string.lastIndexOf(58);
        if (n2 != -1) {
            String string2 = string.substring(0, n2);
            String string3 = string.substring(n2 + 1);
            String string4 = null;
            if (!string2.equals("xmlns") && (string4 = this._symbolTable.lookupNamespace(string2)) == null && bl) {
                int n3 = this.getLineNumber();
                ErrorMsg errorMsg = new ErrorMsg("NAMESPACE_UNDEF_ERR", n3, (Object)string2);
                this.reportError(3, errorMsg);
            }
            return this.getQName(string4, string2, string3);
        }
        if (string.equals("xmlns")) {
            bl2 = true;
        }
        String string5 = bl2 ? null : this._symbolTable.lookupNamespace("");
        return this.getQName(string5, null, string);
    }

    public QName getQName(String string, String string2, String string3) {
        String string4;
        if (string == null || string.equals("")) {
            QName qName = (QName)this._qNames.get(string3);
            if (qName == null) {
                qName = new QName(null, string2, string3);
                this._qNames.put(string3, qName);
            }
            return qName;
        }
        Hashtable<String, QName> hashtable = (Hashtable<String, QName>)this._namespaces.get(string);
        String string5 = string4 = string2 == null || string2.length() == 0 ? string3 : string2 + ':' + string3;
        if (hashtable == null) {
            QName qName = new QName(string, string2, string3);
            hashtable = new Hashtable<String, QName>();
            this._namespaces.put(string, hashtable);
            hashtable.put(string4, qName);
            return qName;
        }
        QName qName = (QName)hashtable.get(string4);
        if (qName == null) {
            qName = new QName(string, string2, string3);
            hashtable.put(string4, qName);
        }
        return qName;
    }

    public QName getQName(String string, String string2) {
        return this.getQName(string + string2);
    }

    public QName getQName(QName qName, QName qName2) {
        return this.getQName(qName.toString() + qName2.toString());
    }

    public QName getUseAttributeSets() {
        return this._useAttributeSets;
    }

    public QName getExtensionElementPrefixes() {
        return this._extensionElementPrefixes;
    }

    public QName getExcludeResultPrefixes() {
        return this._excludeResultPrefixes;
    }

    public Stylesheet makeStylesheet(SyntaxTreeNode syntaxTreeNode) throws CompilerException {
        try {
            Stylesheet stylesheet;
            if (syntaxTreeNode instanceof Stylesheet) {
                stylesheet = (Stylesheet)syntaxTreeNode;
            } else {
                stylesheet = new Stylesheet();
                stylesheet.setSimplified();
                stylesheet.addElement(syntaxTreeNode);
                stylesheet.setAttributes((AttributeList)syntaxTreeNode.getAttributes());
                if (syntaxTreeNode.lookupNamespace("") == null) {
                    syntaxTreeNode.addPrefixMapping("", "");
                }
            }
            stylesheet.setParser(this);
            return stylesheet;
        }
        catch (ClassCastException classCastException) {
            ErrorMsg errorMsg = new ErrorMsg("NOT_STYLESHEET_ERR", syntaxTreeNode);
            throw new CompilerException(errorMsg.toString());
        }
    }

    public void createAST(Stylesheet stylesheet) {
        try {
            if (stylesheet != null) {
                stylesheet.parseContents(this);
                int n2 = stylesheet.getImportPrecedence();
                Enumeration enumeration = stylesheet.elements();
                while (enumeration.hasMoreElements()) {
                    Object e2 = enumeration.nextElement();
                    if (!(e2 instanceof Text)) continue;
                    int n3 = this.getLineNumber();
                    ErrorMsg errorMsg = new ErrorMsg("ILLEGAL_TEXT_NODE_ERR", n3, (Object)null);
                    this.reportError(3, errorMsg);
                }
                if (!this.errorsFound()) {
                    stylesheet.typeCheck(this._symbolTable);
                }
            }
        }
        catch (TypeCheckError typeCheckError) {
            this.reportError(3, new ErrorMsg(typeCheckError));
        }
    }

    public SyntaxTreeNode parse(XMLReader xMLReader, InputSource inputSource) {
        try {
            xMLReader.setContentHandler(this);
            xMLReader.parse(inputSource);
            return this.getStylesheet(this._root);
        }
        catch (IOException iOException) {
            if (this._xsltc.debug()) {
                iOException.printStackTrace();
            }
            this.reportError(3, new ErrorMsg(iOException));
        }
        catch (SAXException sAXException) {
            Exception exception = sAXException.getException();
            if (this._xsltc.debug()) {
                sAXException.printStackTrace();
                if (exception != null) {
                    exception.printStackTrace();
                }
            }
            this.reportError(3, new ErrorMsg(sAXException));
        }
        catch (CompilerException compilerException) {
            if (this._xsltc.debug()) {
                compilerException.printStackTrace();
            }
            this.reportError(3, new ErrorMsg(compilerException));
        }
        catch (Exception exception) {
            if (this._xsltc.debug()) {
                exception.printStackTrace();
            }
            this.reportError(3, new ErrorMsg(exception));
        }
        return null;
    }

    public SyntaxTreeNode parse(InputSource inputSource) {
        try {
            SAXParserFactory sAXParserFactory = SAXParserFactory.newInstance();
            if (this._xsltc.isSecureProcessing()) {
                try {
                    sAXParserFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                }
                catch (SAXException sAXException) {
                    // empty catch block
                }
            }
            try {
                sAXParserFactory.setFeature("http://xml.org/sax/features/namespaces", true);
            }
            catch (Exception exception) {
                sAXParserFactory.setNamespaceAware(true);
            }
            SAXParser sAXParser = sAXParserFactory.newSAXParser();
            XMLReader xMLReader = sAXParser.getXMLReader();
            return this.parse(xMLReader, inputSource);
        }
        catch (ParserConfigurationException parserConfigurationException) {
            ErrorMsg errorMsg = new ErrorMsg("SAX_PARSER_CONFIG_ERR");
            this.reportError(3, errorMsg);
        }
        catch (SAXParseException sAXParseException) {
            this.reportError(3, new ErrorMsg(sAXParseException.getMessage(), sAXParseException.getLineNumber()));
        }
        catch (SAXException sAXException) {
            this.reportError(3, new ErrorMsg(sAXException.getMessage()));
        }
        return null;
    }

    public SyntaxTreeNode getDocumentRoot() {
        return this._root;
    }

    protected void setPIParameters(String string, String string2, String string3) {
        this._PImedia = string;
        this._PItitle = string2;
        this._PIcharset = string3;
    }

    private SyntaxTreeNode getStylesheet(SyntaxTreeNode syntaxTreeNode) throws CompilerException {
        if (this._target == null) {
            if (!this._rootNamespaceDef) {
                ErrorMsg errorMsg = new ErrorMsg("MISSING_XSLT_URI_ERR");
                throw new CompilerException(errorMsg.toString());
            }
            return syntaxTreeNode;
        }
        if (this._target.charAt(0) == '#') {
            SyntaxTreeNode syntaxTreeNode2 = this.findStylesheet(syntaxTreeNode, this._target.substring(1));
            if (syntaxTreeNode2 == null) {
                ErrorMsg errorMsg = new ErrorMsg("MISSING_XSLT_TARGET_ERR", (Object)this._target, syntaxTreeNode);
                throw new CompilerException(errorMsg.toString());
            }
            return syntaxTreeNode2;
        }
        return this.loadExternalStylesheet(this._target);
    }

    private SyntaxTreeNode findStylesheet(SyntaxTreeNode syntaxTreeNode, String string) {
        Object object;
        if (syntaxTreeNode == null) {
            return null;
        }
        if (syntaxTreeNode instanceof Stylesheet && (object = syntaxTreeNode.getAttribute("id")).equals(string)) {
            return syntaxTreeNode;
        }
        object = syntaxTreeNode.getContents();
        if (object != null) {
            int n2 = object.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                SyntaxTreeNode syntaxTreeNode2 = (SyntaxTreeNode)object.elementAt(i2);
                SyntaxTreeNode syntaxTreeNode3 = this.findStylesheet(syntaxTreeNode2, string);
                if (syntaxTreeNode3 == null) continue;
                return syntaxTreeNode3;
            }
        }
        return null;
    }

    private SyntaxTreeNode loadExternalStylesheet(String string) throws CompilerException {
        InputSource inputSource = new File(string).exists() ? new InputSource("file:" + string) : new InputSource(string);
        SyntaxTreeNode syntaxTreeNode = this.parse(inputSource);
        return syntaxTreeNode;
    }

    private void initAttrTable(String string, String[] arrstring) {
        this._instructionAttrs.put(this.getQName("http://www.w3.org/1999/XSL/Transform", "xsl", string), arrstring);
    }

    private void initInstructionAttrs() {
        this.initAttrTable("template", new String[]{"match", "name", "priority", "mode"});
        this.initAttrTable("stylesheet", new String[]{"id", "version", "extension-element-prefixes", "exclude-result-prefixes"});
        this.initAttrTable("transform", new String[]{"id", "version", "extension-element-prefixes", "exclude-result-prefixes"});
        this.initAttrTable("text", new String[]{"disable-output-escaping"});
        this.initAttrTable("if", new String[]{"test"});
        this.initAttrTable("choose", new String[0]);
        this.initAttrTable("when", new String[]{"test"});
        this.initAttrTable("otherwise", new String[0]);
        this.initAttrTable("for-each", new String[]{"select"});
        this.initAttrTable("message", new String[]{"terminate"});
        this.initAttrTable("number", new String[]{"level", "count", "from", "value", "format", "lang", "letter-value", "grouping-separator", "grouping-size"});
        this.initAttrTable("comment", new String[0]);
        this.initAttrTable("copy", new String[]{"use-attribute-sets"});
        this.initAttrTable("copy-of", new String[]{"select"});
        this.initAttrTable("param", new String[]{"name", "select"});
        this.initAttrTable("with-param", new String[]{"name", "select"});
        this.initAttrTable("variable", new String[]{"name", "select"});
        this.initAttrTable("output", new String[]{"method", "version", "encoding", "omit-xml-declaration", "standalone", "doctype-public", "doctype-system", "cdata-section-elements", "indent", "media-type"});
        this.initAttrTable("sort", new String[]{"select", "order", "case-order", "lang", "data-type"});
        this.initAttrTable("key", new String[]{"name", "match", "use"});
        this.initAttrTable("fallback", new String[0]);
        this.initAttrTable("attribute", new String[]{"name", "namespace"});
        this.initAttrTable("attribute-set", new String[]{"name", "use-attribute-sets"});
        this.initAttrTable("value-of", new String[]{"select", "disable-output-escaping"});
        this.initAttrTable("element", new String[]{"name", "namespace", "use-attribute-sets"});
        this.initAttrTable("call-template", new String[]{"name"});
        this.initAttrTable("apply-templates", new String[]{"select", "mode"});
        this.initAttrTable("apply-imports", new String[0]);
        this.initAttrTable("decimal-format", new String[]{"name", "decimal-separator", "grouping-separator", "infinity", "minus-sign", "NaN", "percent", "per-mille", "zero-digit", "digit", "pattern-separator"});
        this.initAttrTable("import", new String[]{"href"});
        this.initAttrTable("include", new String[]{"href"});
        this.initAttrTable("strip-space", new String[]{"elements"});
        this.initAttrTable("preserve-space", new String[]{"elements"});
        this.initAttrTable("processing-instruction", new String[]{"name"});
        this.initAttrTable("namespace-alias", new String[]{"stylesheet-prefix", "result-prefix"});
    }

    private void initStdClasses() {
        this.initStdClass("template", "Template");
        this.initStdClass("stylesheet", "Stylesheet");
        this.initStdClass("transform", "Stylesheet");
        this.initStdClass("text", "Text");
        this.initStdClass("if", "If");
        this.initStdClass("choose", "Choose");
        this.initStdClass("when", "When");
        this.initStdClass("otherwise", "Otherwise");
        this.initStdClass("for-each", "ForEach");
        this.initStdClass("message", "Message");
        this.initStdClass("number", "Number");
        this.initStdClass("comment", "Comment");
        this.initStdClass("copy", "Copy");
        this.initStdClass("copy-of", "CopyOf");
        this.initStdClass("param", "Param");
        this.initStdClass("with-param", "WithParam");
        this.initStdClass("variable", "Variable");
        this.initStdClass("output", "Output");
        this.initStdClass("sort", "Sort");
        this.initStdClass("key", "Key");
        this.initStdClass("fallback", "Fallback");
        this.initStdClass("attribute", "XslAttribute");
        this.initStdClass("attribute-set", "AttributeSet");
        this.initStdClass("value-of", "ValueOf");
        this.initStdClass("element", "XslElement");
        this.initStdClass("call-template", "CallTemplate");
        this.initStdClass("apply-templates", "ApplyTemplates");
        this.initStdClass("apply-imports", "ApplyImports");
        this.initStdClass("decimal-format", "DecimalFormatting");
        this.initStdClass("import", "Import");
        this.initStdClass("include", "Include");
        this.initStdClass("strip-space", "Whitespace");
        this.initStdClass("preserve-space", "Whitespace");
        this.initStdClass("processing-instruction", "ProcessingInstruction");
        this.initStdClass("namespace-alias", "NamespaceAlias");
    }

    private void initStdClass(String string, String string2) {
        this._instructionClasses.put(this.getQName("http://www.w3.org/1999/XSL/Transform", "xsl", string), "org.apache.xalan.xsltc.compiler." + string2);
    }

    public boolean elementSupported(String string, String string2) {
        return this._instructionClasses.get(this.getQName(string, "xsl", string2)) != null;
    }

    public boolean functionSupported(String string) {
        return this._symbolTable.lookupPrimop(string) != null;
    }

    private void initExtClasses() {
        this.initExtClass("output", "TransletOutput");
        this.initExtClass("http://xml.apache.org/xalan/redirect", "write", "TransletOutput");
    }

    private void initExtClass(String string, String string2) {
        this._instructionClasses.put(this.getQName("http://xml.apache.org/xalan/xsltc", "translet", string), "org.apache.xalan.xsltc.compiler." + string2);
    }

    private void initExtClass(String string, String string2, String string3) {
        this._instructionClasses.put(this.getQName(string, "translet", string2), "org.apache.xalan.xsltc.compiler." + string3);
    }

    private void initSymbolTable() {
        MethodType methodType = new MethodType(Type.Int, Type.Void);
        MethodType methodType2 = new MethodType(Type.Int, Type.Real);
        MethodType methodType3 = new MethodType(Type.Int, Type.String);
        MethodType methodType4 = new MethodType(Type.Int, Type.NodeSet);
        MethodType methodType5 = new MethodType(Type.Real, Type.Int);
        MethodType methodType6 = new MethodType(Type.Real, Type.Void);
        MethodType methodType7 = new MethodType(Type.Real, Type.Real);
        MethodType methodType8 = new MethodType(Type.Real, Type.NodeSet);
        MethodType methodType9 = new MethodType(Type.Real, Type.Reference);
        MethodType methodType10 = new MethodType(Type.Int, Type.Int);
        MethodType methodType11 = new MethodType(Type.NodeSet, Type.Reference);
        MethodType methodType12 = new MethodType(Type.NodeSet, Type.Void);
        MethodType methodType13 = new MethodType(Type.NodeSet, Type.String);
        MethodType methodType14 = new MethodType(Type.NodeSet, Type.NodeSet);
        MethodType methodType15 = new MethodType(Type.Node, Type.Void);
        MethodType methodType16 = new MethodType(Type.String, Type.Void);
        MethodType methodType17 = new MethodType(Type.String, Type.String);
        MethodType methodType18 = new MethodType(Type.String, Type.Node);
        MethodType methodType19 = new MethodType(Type.String, Type.NodeSet);
        MethodType methodType20 = new MethodType(Type.String, Type.Reference);
        MethodType methodType21 = new MethodType(Type.Boolean, Type.Reference);
        MethodType methodType22 = new MethodType(Type.Boolean, Type.Void);
        MethodType methodType23 = new MethodType(Type.Boolean, Type.Boolean);
        MethodType methodType24 = new MethodType(Type.Boolean, Type.String);
        MethodType methodType25 = new MethodType(Type.NodeSet, Type.Object);
        MethodType methodType26 = new MethodType(Type.Real, Type.Real, Type.Real);
        MethodType methodType27 = new MethodType(Type.Int, Type.Int, Type.Int);
        MethodType methodType28 = new MethodType(Type.Boolean, Type.Real, Type.Real);
        MethodType methodType29 = new MethodType(Type.Boolean, Type.Int, Type.Int);
        MethodType methodType30 = new MethodType(Type.String, Type.String, Type.String);
        MethodType methodType31 = new MethodType(Type.String, Type.Real, Type.String);
        MethodType methodType32 = new MethodType(Type.String, Type.String, Type.Real);
        MethodType methodType33 = new MethodType(Type.Reference, Type.String, Type.Reference);
        MethodType methodType34 = new MethodType(Type.NodeSet, Type.String, Type.String);
        MethodType methodType35 = new MethodType(Type.NodeSet, Type.String, Type.NodeSet);
        MethodType methodType36 = new MethodType(Type.Boolean, Type.Boolean, Type.Boolean);
        MethodType methodType37 = new MethodType(Type.Boolean, Type.String, Type.String);
        MethodType methodType38 = new MethodType(Type.String, Type.String, Type.NodeSet);
        MethodType methodType39 = new MethodType(Type.String, Type.Real, Type.String, Type.String);
        MethodType methodType40 = new MethodType(Type.String, Type.String, Type.Real, Type.Real);
        MethodType methodType41 = new MethodType(Type.String, Type.String, Type.String, Type.String);
        this._symbolTable.addPrimop("current", methodType15);
        this._symbolTable.addPrimop("last", methodType);
        this._symbolTable.addPrimop("position", methodType);
        this._symbolTable.addPrimop("true", methodType22);
        this._symbolTable.addPrimop("false", methodType22);
        this._symbolTable.addPrimop("not", methodType23);
        this._symbolTable.addPrimop("name", methodType16);
        this._symbolTable.addPrimop("name", methodType18);
        this._symbolTable.addPrimop("generate-id", methodType16);
        this._symbolTable.addPrimop("generate-id", methodType18);
        this._symbolTable.addPrimop("ceiling", methodType7);
        this._symbolTable.addPrimop("floor", methodType7);
        this._symbolTable.addPrimop("round", methodType7);
        this._symbolTable.addPrimop("contains", methodType37);
        this._symbolTable.addPrimop("number", methodType9);
        this._symbolTable.addPrimop("number", methodType6);
        this._symbolTable.addPrimop("boolean", methodType21);
        this._symbolTable.addPrimop("string", methodType20);
        this._symbolTable.addPrimop("string", methodType16);
        this._symbolTable.addPrimop("translate", methodType41);
        this._symbolTable.addPrimop("string-length", methodType);
        this._symbolTable.addPrimop("string-length", methodType3);
        this._symbolTable.addPrimop("starts-with", methodType37);
        this._symbolTable.addPrimop("format-number", methodType31);
        this._symbolTable.addPrimop("format-number", methodType39);
        this._symbolTable.addPrimop("unparsed-entity-uri", methodType17);
        this._symbolTable.addPrimop("key", methodType34);
        this._symbolTable.addPrimop("key", methodType35);
        this._symbolTable.addPrimop("id", methodType13);
        this._symbolTable.addPrimop("id", methodType14);
        this._symbolTable.addPrimop("namespace-uri", methodType16);
        this._symbolTable.addPrimop("function-available", methodType24);
        this._symbolTable.addPrimop("element-available", methodType24);
        this._symbolTable.addPrimop("document", methodType13);
        this._symbolTable.addPrimop("document", methodType12);
        this._symbolTable.addPrimop("count", methodType4);
        this._symbolTable.addPrimop("sum", methodType8);
        this._symbolTable.addPrimop("local-name", methodType16);
        this._symbolTable.addPrimop("local-name", methodType19);
        this._symbolTable.addPrimop("namespace-uri", methodType16);
        this._symbolTable.addPrimop("namespace-uri", methodType19);
        this._symbolTable.addPrimop("substring", methodType32);
        this._symbolTable.addPrimop("substring", methodType40);
        this._symbolTable.addPrimop("substring-after", methodType30);
        this._symbolTable.addPrimop("substring-before", methodType30);
        this._symbolTable.addPrimop("normalize-space", methodType16);
        this._symbolTable.addPrimop("normalize-space", methodType17);
        this._symbolTable.addPrimop("system-property", methodType17);
        this._symbolTable.addPrimop("nodeset", methodType11);
        this._symbolTable.addPrimop("objectType", methodType20);
        this._symbolTable.addPrimop("cast", methodType33);
        this._symbolTable.addPrimop("+", methodType26);
        this._symbolTable.addPrimop("-", methodType26);
        this._symbolTable.addPrimop("*", methodType26);
        this._symbolTable.addPrimop("/", methodType26);
        this._symbolTable.addPrimop("%", methodType26);
        this._symbolTable.addPrimop("+", methodType27);
        this._symbolTable.addPrimop("-", methodType27);
        this._symbolTable.addPrimop("*", methodType27);
        this._symbolTable.addPrimop("<", methodType28);
        this._symbolTable.addPrimop("<=", methodType28);
        this._symbolTable.addPrimop(">", methodType28);
        this._symbolTable.addPrimop(">=", methodType28);
        this._symbolTable.addPrimop("<", methodType29);
        this._symbolTable.addPrimop("<=", methodType29);
        this._symbolTable.addPrimop(">", methodType29);
        this._symbolTable.addPrimop(">=", methodType29);
        this._symbolTable.addPrimop("<", methodType36);
        this._symbolTable.addPrimop("<=", methodType36);
        this._symbolTable.addPrimop(">", methodType36);
        this._symbolTable.addPrimop(">=", methodType36);
        this._symbolTable.addPrimop("or", methodType36);
        this._symbolTable.addPrimop("and", methodType36);
        this._symbolTable.addPrimop("u-", methodType7);
        this._symbolTable.addPrimop("u-", methodType10);
    }

    public SymbolTable getSymbolTable() {
        return this._symbolTable;
    }

    public Template getTemplate() {
        return this._template;
    }

    public void setTemplate(Template template) {
        this._template = template;
    }

    public int getTemplateIndex() {
        return this._templateIndex++;
    }

    public SyntaxTreeNode makeInstance(String string, String string2, String string3, Attributes attributes) {
        SyntaxTreeNode syntaxTreeNode = null;
        QName qName = this.getQName(string, string2, string3);
        String string4 = (String)this._instructionClasses.get(qName);
        if (string4 != null) {
            try {
                Class class_ = ObjectFactory.findProviderClass(string4, ObjectFactory.findClassLoader(), true);
                syntaxTreeNode = (SyntaxTreeNode)class_.newInstance();
                syntaxTreeNode.setQName(qName);
                syntaxTreeNode.setParser(this);
                if (this._locator != null) {
                    syntaxTreeNode.setLineNumber(this.getLineNumber());
                }
                if (syntaxTreeNode instanceof Stylesheet) {
                    this._xsltc.setStylesheet((Stylesheet)syntaxTreeNode);
                }
                this.checkForSuperfluousAttributes(syntaxTreeNode, attributes);
            }
            catch (ClassNotFoundException classNotFoundException) {
                ErrorMsg errorMsg = new ErrorMsg("CLASS_NOT_FOUND_ERR", syntaxTreeNode);
                this.reportError(3, errorMsg);
            }
            catch (Exception exception) {
                ErrorMsg errorMsg = new ErrorMsg("INTERNAL_ERR", (Object)exception.getMessage(), syntaxTreeNode);
                this.reportError(2, errorMsg);
            }
        } else {
            if (string != null) {
                if (string.equals("http://www.w3.org/1999/XSL/Transform")) {
                    syntaxTreeNode = new UnsupportedElement(string, string2, string3, false);
                    UnsupportedElement unsupportedElement = (UnsupportedElement)syntaxTreeNode;
                    ErrorMsg errorMsg = new ErrorMsg("UNSUPPORTED_XSL_ERR", this.getLineNumber(), (Object)string3);
                    unsupportedElement.setErrorMessage(errorMsg);
                    if (this.versionIsOne) {
                        this.reportError(1, errorMsg);
                    }
                } else if (string.equals("http://xml.apache.org/xalan/xsltc")) {
                    syntaxTreeNode = new UnsupportedElement(string, string2, string3, true);
                    UnsupportedElement unsupportedElement = (UnsupportedElement)syntaxTreeNode;
                    ErrorMsg errorMsg = new ErrorMsg("UNSUPPORTED_EXT_ERR", this.getLineNumber(), (Object)string3);
                    unsupportedElement.setErrorMessage(errorMsg);
                } else {
                    Stylesheet stylesheet = this._xsltc.getStylesheet();
                    if (stylesheet != null && stylesheet.isExtension(string) && stylesheet != (SyntaxTreeNode)this._parentStack.peek()) {
                        syntaxTreeNode = new UnsupportedElement(string, string2, string3, true);
                        UnsupportedElement unsupportedElement = (UnsupportedElement)syntaxTreeNode;
                        ErrorMsg errorMsg = new ErrorMsg("UNSUPPORTED_EXT_ERR", this.getLineNumber(), (Object)(string2 + ":" + string3));
                        unsupportedElement.setErrorMessage(errorMsg);
                    }
                }
            }
            if (syntaxTreeNode == null) {
                syntaxTreeNode = new LiteralElement();
                syntaxTreeNode.setLineNumber(this.getLineNumber());
            }
            syntaxTreeNode.setParser(this);
        }
        if (syntaxTreeNode != null && syntaxTreeNode instanceof LiteralElement) {
            ((LiteralElement)syntaxTreeNode).setQName(qName);
        }
        return syntaxTreeNode;
    }

    private void checkForSuperfluousAttributes(SyntaxTreeNode syntaxTreeNode, Attributes attributes) {
        QName qName = syntaxTreeNode.getQName();
        boolean bl = syntaxTreeNode instanceof Stylesheet;
        String[] arrstring = (String[])this._instructionAttrs.get(qName);
        if (this.versionIsOne && arrstring != null) {
            int n2 = attributes.getLength();
            for (int i2 = 0; i2 < n2; ++i2) {
                int n3;
                String string = attributes.getQName(i2);
                if (bl && string.equals("version")) {
                    this.versionIsOne = attributes.getValue(i2).equals("1.0");
                }
                if (string.startsWith("xml") || string.indexOf(58) > 0) continue;
                for (n3 = 0; n3 < arrstring.length && !string.equalsIgnoreCase(arrstring[n3]); ++n3) {
                }
                if (n3 != arrstring.length) continue;
                ErrorMsg errorMsg = new ErrorMsg("ILLEGAL_ATTRIBUTE_ERR", (Object)string, syntaxTreeNode);
                errorMsg.setWarningError(true);
                this.reportError(4, errorMsg);
            }
        }
    }

    public Expression parseExpression(SyntaxTreeNode syntaxTreeNode, String string) {
        return (Expression)this.parseTopLevel(syntaxTreeNode, "<EXPRESSION>" + string, null);
    }

    public Expression parseExpression(SyntaxTreeNode syntaxTreeNode, String string, String string2) {
        String string3 = syntaxTreeNode.getAttribute(string);
        if (string3.length() == 0 && string2 != null) {
            string3 = string2;
        }
        return (Expression)this.parseTopLevel(syntaxTreeNode, "<EXPRESSION>" + string3, string3);
    }

    public Pattern parsePattern(SyntaxTreeNode syntaxTreeNode, String string) {
        return (Pattern)this.parseTopLevel(syntaxTreeNode, "<PATTERN>" + string, string);
    }

    public Pattern parsePattern(SyntaxTreeNode syntaxTreeNode, String string, String string2) {
        String string3 = syntaxTreeNode.getAttribute(string);
        if (string3.length() == 0 && string2 != null) {
            string3 = string2;
        }
        return (Pattern)this.parseTopLevel(syntaxTreeNode, "<PATTERN>" + string3, string3);
    }

    private SyntaxTreeNode parseTopLevel(SyntaxTreeNode syntaxTreeNode, String string, String string2) {
        int n2 = this.getLineNumber();
        try {
            SyntaxTreeNode syntaxTreeNode2;
            this._xpathParser.setScanner(new XPathLexer(new StringReader(string)));
            b b2 = this._xpathParser.parse(string2, n2);
            if (b2 != null && (syntaxTreeNode2 = (SyntaxTreeNode)b2.f) != null) {
                syntaxTreeNode2.setParser(this);
                syntaxTreeNode2.setParent(syntaxTreeNode);
                syntaxTreeNode2.setLineNumber(n2);
                return syntaxTreeNode2;
            }
            this.reportError(3, new ErrorMsg("XPATH_PARSER_ERR", (Object)string2, syntaxTreeNode));
        }
        catch (Exception exception) {
            if (this._xsltc.debug()) {
                exception.printStackTrace();
            }
            this.reportError(3, new ErrorMsg("XPATH_PARSER_ERR", (Object)string2, syntaxTreeNode));
        }
        SyntaxTreeNode.Dummy.setParser(this);
        return SyntaxTreeNode.Dummy;
    }

    public boolean errorsFound() {
        return this._errors.size() > 0;
    }

    public void printErrors() {
        int n2 = this._errors.size();
        if (n2 > 0) {
            System.err.println(new ErrorMsg("COMPILER_ERROR_KEY"));
            for (int i2 = 0; i2 < n2; ++i2) {
                System.err.println("  " + this._errors.elementAt(i2));
            }
        }
    }

    public void printWarnings() {
        int n2 = this._warnings.size();
        if (n2 > 0) {
            System.err.println(new ErrorMsg("COMPILER_WARNING_KEY"));
            for (int i2 = 0; i2 < n2; ++i2) {
                System.err.println("  " + this._warnings.elementAt(i2));
            }
        }
    }

    public void reportError(int n2, ErrorMsg errorMsg) {
        switch (n2) {
            case 0: {
                this._errors.addElement(errorMsg);
                break;
            }
            case 1: {
                this._errors.addElement(errorMsg);
                break;
            }
            case 2: {
                this._errors.addElement(errorMsg);
                break;
            }
            case 3: {
                this._errors.addElement(errorMsg);
                break;
            }
            case 4: {
                this._warnings.addElement(errorMsg);
            }
        }
    }

    public Vector getErrors() {
        return this._errors;
    }

    public Vector getWarnings() {
        return this._warnings;
    }

    public void startDocument() {
        this._root = null;
        this._target = null;
        this._prefixMapping = null;
        this._parentStack = new Stack();
    }

    public void endDocument() {
    }

    public void startPrefixMapping(String string, String string2) {
        if (this._prefixMapping == null) {
            this._prefixMapping = new Hashtable();
        }
        this._prefixMapping.put(string, string2);
    }

    public void endPrefixMapping(String string) {
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        int n2 = string3.lastIndexOf(58);
        String string4 = n2 == -1 ? null : string3.substring(0, n2);
        SyntaxTreeNode syntaxTreeNode = this.makeInstance(string, string4, string2, attributes);
        if (syntaxTreeNode == null) {
            ErrorMsg errorMsg = new ErrorMsg("ELEMENT_PARSE_ERR", string4 + ':' + string2);
            throw new SAXException(errorMsg.toString());
        }
        if (this._root == null) {
            this._rootNamespaceDef = this._prefixMapping != null && this._prefixMapping.containsValue("http://www.w3.org/1999/XSL/Transform");
            this._root = syntaxTreeNode;
        } else {
            SyntaxTreeNode syntaxTreeNode2 = (SyntaxTreeNode)this._parentStack.peek();
            syntaxTreeNode2.addElement(syntaxTreeNode);
            syntaxTreeNode.setParent(syntaxTreeNode2);
        }
        syntaxTreeNode.setAttributes(new AttributeList(attributes));
        syntaxTreeNode.setPrefixMapping(this._prefixMapping);
        if (syntaxTreeNode instanceof Stylesheet) {
            this.getSymbolTable().setCurrentNode(syntaxTreeNode);
            ((Stylesheet)syntaxTreeNode).declareExtensionPrefixes(this);
        }
        this._prefixMapping = null;
        this._parentStack.push(syntaxTreeNode);
    }

    public void endElement(String string, String string2, String string3) {
        this._parentStack.pop();
    }

    public void characters(char[] arrc, int n2, int n3) {
        Text text;
        String string = new String(arrc, n2, n3);
        SyntaxTreeNode syntaxTreeNode = (SyntaxTreeNode)this._parentStack.peek();
        if (string.length() == 0) {
            return;
        }
        if (syntaxTreeNode instanceof Text) {
            ((Text)syntaxTreeNode).setText(string);
            return;
        }
        if (syntaxTreeNode instanceof Stylesheet) {
            return;
        }
        SyntaxTreeNode syntaxTreeNode2 = syntaxTreeNode.lastChild();
        if (syntaxTreeNode2 != null && syntaxTreeNode2 instanceof Text && !(text = (Text)syntaxTreeNode2).isTextElement() && (n3 > 1 || arrc[0] < '\u0100')) {
            text.setText(string);
            return;
        }
        syntaxTreeNode.addElement(new Text(string));
    }

    private String getTokenValue(String string) {
        int n2 = string.indexOf(34);
        int n3 = string.lastIndexOf(34);
        return string.substring(n2 + 1, n3);
    }

    public void processingInstruction(String string, String string2) {
        if (this._target == null && string.equals("xml-stylesheet")) {
            String string3 = null;
            String string4 = null;
            String string5 = null;
            String string6 = null;
            StringTokenizer stringTokenizer = new StringTokenizer(string2);
            while (stringTokenizer.hasMoreElements()) {
                String string7 = (String)stringTokenizer.nextElement();
                if (string7.startsWith("href")) {
                    string3 = this.getTokenValue(string7);
                    continue;
                }
                if (string7.startsWith("media")) {
                    string4 = this.getTokenValue(string7);
                    continue;
                }
                if (string7.startsWith("title")) {
                    string5 = this.getTokenValue(string7);
                    continue;
                }
                if (!string7.startsWith("charset")) continue;
                string6 = this.getTokenValue(string7);
            }
            if (!(this._PImedia != null && !this._PImedia.equals(string4) || this._PItitle != null && !this._PImedia.equals(string5) || this._PIcharset != null && !this._PImedia.equals(string6))) {
                this._target = string3;
            }
        }
    }

    public void ignorableWhitespace(char[] arrc, int n2, int n3) {
    }

    public void skippedEntity(String string) {
    }

    public void setDocumentLocator(Locator locator) {
        this._locator = locator;
    }

    private int getLineNumber() {
        int n2 = 0;
        if (this._locator != null) {
            n2 = this._locator.getLineNumber();
        }
        return n2;
    }
}

