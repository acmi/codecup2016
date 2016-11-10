/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ArrayInstruction;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.NEWARRAY;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.PUTSTATIC;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.StackInstruction;
import org.apache.bcel.generic.TargetLostException;
import org.apache.bcel.util.InstructionFinder;
import org.apache.xalan.xsltc.compiler.AttributeSet;
import org.apache.xalan.xsltc.compiler.DecimalFormatting;
import org.apache.xalan.xsltc.compiler.Include;
import org.apache.xalan.xsltc.compiler.Key;
import org.apache.xalan.xsltc.compiler.Mode;
import org.apache.xalan.xsltc.compiler.NamespaceAlias;
import org.apache.xalan.xsltc.compiler.Output;
import org.apache.xalan.xsltc.compiler.Param;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SourceLoader;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.Template;
import org.apache.xalan.xsltc.compiler.TopLevelElement;
import org.apache.xalan.xsltc.compiler.Variable;
import org.apache.xalan.xsltc.compiler.VariableBase;
import org.apache.xalan.xsltc.compiler.Whitespace;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.SystemIDResolver;

public final class Stylesheet
extends SyntaxTreeNode {
    private String _version;
    private QName _name;
    private String _systemId;
    private Stylesheet _parentStylesheet;
    private Vector _globals = new Vector();
    private Boolean _hasLocalParams = null;
    private String _className;
    private final Vector _templates = new Vector();
    private Vector _allValidTemplates = null;
    private Vector _elementsWithNamespacesUsedDynamically = null;
    private int _nextModeSerial = 1;
    private final Hashtable _modes = new Hashtable();
    private Mode _defaultMode;
    private final Hashtable _extensions = new Hashtable();
    public Stylesheet _importedFrom = null;
    public Stylesheet _includedFrom = null;
    private Vector _includedStylesheets = null;
    private int _importPrecedence = 1;
    private int _minimumDescendantPrecedence = -1;
    private Hashtable _keys = new Hashtable();
    private SourceLoader _loader = null;
    private boolean _numberFormattingUsed = false;
    private boolean _simplified = false;
    private boolean _multiDocument = false;
    private boolean _callsNodeset = false;
    private boolean _hasIdCall = false;
    private boolean _templateInlining = false;
    private Output _lastOutputElement = null;
    private Properties _outputProperties = null;
    private int _outputMethod = 0;
    public static final int UNKNOWN_OUTPUT = 0;
    public static final int XML_OUTPUT = 1;
    public static final int HTML_OUTPUT = 2;
    public static final int TEXT_OUTPUT = 3;

    public int getOutputMethod() {
        return this._outputMethod;
    }

    private void checkOutputMethod() {
        String string;
        if (this._lastOutputElement != null && (string = this._lastOutputElement.getOutputMethod()) != null) {
            if (string.equals("xml")) {
                this._outputMethod = 1;
            } else if (string.equals("html")) {
                this._outputMethod = 2;
            } else if (string.equals("text")) {
                this._outputMethod = 3;
            }
        }
    }

    public boolean getTemplateInlining() {
        return this._templateInlining;
    }

    public void setTemplateInlining(boolean bl) {
        this._templateInlining = bl;
    }

    public boolean isSimplified() {
        return this._simplified;
    }

    public void setSimplified() {
        this._simplified = true;
    }

    public void setHasIdCall(boolean bl) {
        this._hasIdCall = bl;
    }

    public void setOutputProperty(String string, String string2) {
        if (this._outputProperties == null) {
            this._outputProperties = new Properties();
        }
        this._outputProperties.setProperty(string, string2);
    }

    public void setOutputProperties(Properties properties) {
        this._outputProperties = properties;
    }

    public Properties getOutputProperties() {
        return this._outputProperties;
    }

    public Output getLastOutputElement() {
        return this._lastOutputElement;
    }

    public void setMultiDocument(boolean bl) {
        this._multiDocument = bl;
    }

    public boolean isMultiDocument() {
        return this._multiDocument;
    }

    public void setCallsNodeset(boolean bl) {
        if (bl) {
            this.setMultiDocument(bl);
        }
        this._callsNodeset = bl;
    }

    public boolean callsNodeset() {
        return this._callsNodeset;
    }

    public void numberFormattingUsed() {
        this._numberFormattingUsed = true;
        Stylesheet stylesheet = this.getParentStylesheet();
        if (null != stylesheet) {
            stylesheet.numberFormattingUsed();
        }
    }

    public void setImportPrecedence(int n2) {
        this._importPrecedence = n2;
        Enumeration enumeration = this.elements();
        while (enumeration.hasMoreElements()) {
            Stylesheet n3;
            SyntaxTreeNode syntaxTreeNode = (SyntaxTreeNode)enumeration.nextElement();
            if (!(syntaxTreeNode instanceof Include) || (n3 = ((Include)syntaxTreeNode).getIncludedStylesheet()) == null || n3._includedFrom != this) continue;
            n3.setImportPrecedence(n2);
        }
        if (this._importedFrom != null) {
            if (this._importedFrom.getImportPrecedence() < n2) {
                Parser parser = this.getParser();
                int n3 = parser.getNextImportPrecedence();
                this._importedFrom.setImportPrecedence(n3);
            }
        } else if (this._includedFrom != null && this._includedFrom.getImportPrecedence() != n2) {
            this._includedFrom.setImportPrecedence(n2);
        }
    }

    public int getImportPrecedence() {
        return this._importPrecedence;
    }

    public int getMinimumDescendantPrecedence() {
        if (this._minimumDescendantPrecedence == -1) {
            int n2 = this.getImportPrecedence();
            int n3 = this._includedStylesheets != null ? this._includedStylesheets.size() : 0;
            for (int i2 = 0; i2 < n3; ++i2) {
                int n4 = ((Stylesheet)this._includedStylesheets.elementAt(i2)).getMinimumDescendantPrecedence();
                if (n4 >= n2) continue;
                n2 = n4;
            }
            this._minimumDescendantPrecedence = n2;
        }
        return this._minimumDescendantPrecedence;
    }

    public boolean checkForLoop(String string) {
        if (this._systemId != null && this._systemId.equals(string)) {
            return true;
        }
        if (this._parentStylesheet != null) {
            return this._parentStylesheet.checkForLoop(string);
        }
        return false;
    }

    public void setParser(Parser parser) {
        super.setParser(parser);
        this._name = this.makeStylesheetName("__stylesheet_");
    }

    public void setParentStylesheet(Stylesheet stylesheet) {
        this._parentStylesheet = stylesheet;
    }

    public Stylesheet getParentStylesheet() {
        return this._parentStylesheet;
    }

    public void setImportingStylesheet(Stylesheet stylesheet) {
        this._importedFrom = stylesheet;
        stylesheet.addIncludedStylesheet(this);
    }

    public void setIncludingStylesheet(Stylesheet stylesheet) {
        this._includedFrom = stylesheet;
        stylesheet.addIncludedStylesheet(this);
    }

    public void addIncludedStylesheet(Stylesheet stylesheet) {
        if (this._includedStylesheets == null) {
            this._includedStylesheets = new Vector();
        }
        this._includedStylesheets.addElement(stylesheet);
    }

    public void setSystemId(String string) {
        if (string != null) {
            this._systemId = SystemIDResolver.getAbsoluteURI(string);
        }
    }

    public String getSystemId() {
        return this._systemId;
    }

    public void setSourceLoader(SourceLoader sourceLoader) {
        this._loader = sourceLoader;
    }

    public SourceLoader getSourceLoader() {
        return this._loader;
    }

    private QName makeStylesheetName(String string) {
        return this.getParser().getQName(string + this.getXSLTC().nextStylesheetSerial());
    }

    public boolean hasGlobals() {
        return this._globals.size() > 0;
    }

    public boolean hasLocalParams() {
        if (this._hasLocalParams == null) {
            Vector vector = this.getAllValidTemplates();
            int n2 = vector.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Template template = (Template)vector.elementAt(i2);
                if (!template.hasParams()) continue;
                this._hasLocalParams = Boolean.TRUE;
                return true;
            }
            this._hasLocalParams = Boolean.FALSE;
            return false;
        }
        return this._hasLocalParams;
    }

    protected void addPrefixMapping(String string, String string2) {
        if (string.equals("") && string2.equals("http://www.w3.org/1999/xhtml")) {
            return;
        }
        super.addPrefixMapping(string, string2);
    }

    private void extensionURI(String string, SymbolTable symbolTable) {
        if (string != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(string);
            while (stringTokenizer.hasMoreTokens()) {
                String string2 = stringTokenizer.nextToken();
                String string3 = this.lookupNamespace(string2);
                if (string3 == null) continue;
                this._extensions.put(string3, string2);
            }
        }
    }

    public boolean isExtension(String string) {
        return this._extensions.get(string) != null;
    }

    public void declareExtensionPrefixes(Parser parser) {
        SymbolTable symbolTable = parser.getSymbolTable();
        String string = this.getAttribute("extension-element-prefixes");
        this.extensionURI(string, symbolTable);
    }

    public void parseContents(Parser parser) {
        Object object;
        SymbolTable symbolTable = parser.getSymbolTable();
        this.addPrefixMapping("xml", "http://www.w3.org/XML/1998/namespace");
        Stylesheet stylesheet = symbolTable.addStylesheet(this._name, this);
        if (stylesheet != null) {
            object = new ErrorMsg("MULTIPLE_STYLESHEET_ERR", this);
            parser.reportError(3, (ErrorMsg)object);
        }
        if (this._simplified) {
            symbolTable.excludeURI("http://www.w3.org/1999/XSL/Transform");
            object = new Template();
            object.parseSimplified(this, parser);
        } else {
            this.parseOwnChildren(parser);
        }
    }

    public final void parseOwnChildren(Parser parser) {
        int n2;
        SyntaxTreeNode syntaxTreeNode;
        SymbolTable symbolTable = parser.getSymbolTable();
        String string = this.getAttribute("exclude-result-prefixes");
        String string2 = this.getAttribute("extension-element-prefixes");
        symbolTable.pushExcludedNamespacesContext();
        symbolTable.excludeURI("http://www.w3.org/1999/XSL/Transform");
        symbolTable.excludeNamespaces(string);
        symbolTable.excludeNamespaces(string2);
        Vector vector = this.getContents();
        int n3 = vector.size();
        for (n2 = 0; n2 < n3; ++n2) {
            syntaxTreeNode = (SyntaxTreeNode)vector.elementAt(n2);
            if (!(syntaxTreeNode instanceof VariableBase) && !(syntaxTreeNode instanceof NamespaceAlias)) continue;
            parser.getSymbolTable().setCurrentNode(syntaxTreeNode);
            syntaxTreeNode.parseContents(parser);
        }
        for (n2 = 0; n2 < n3; ++n2) {
            syntaxTreeNode = (SyntaxTreeNode)vector.elementAt(n2);
            if (!(syntaxTreeNode instanceof VariableBase) && !(syntaxTreeNode instanceof NamespaceAlias)) {
                parser.getSymbolTable().setCurrentNode(syntaxTreeNode);
                syntaxTreeNode.parseContents(parser);
            }
            if (this._templateInlining || !(syntaxTreeNode instanceof Template)) continue;
            Template template = (Template)syntaxTreeNode;
            String string3 = "template$dot$" + template.getPosition();
            template.setName(parser.getQName(string3));
        }
        symbolTable.popExcludedNamespacesContext();
    }

    public void processModes() {
        if (this._defaultMode == null) {
            this._defaultMode = new Mode(null, this, "");
        }
        this._defaultMode.processPatterns(this._keys);
        Enumeration enumeration = this._modes.elements();
        while (enumeration.hasMoreElements()) {
            Mode mode = (Mode)enumeration.nextElement();
            mode.processPatterns(this._keys);
        }
    }

    private void compileModes(ClassGenerator classGenerator) {
        this._defaultMode.compileApplyTemplates(classGenerator);
        Enumeration enumeration = this._modes.elements();
        while (enumeration.hasMoreElements()) {
            Mode mode = (Mode)enumeration.nextElement();
            mode.compileApplyTemplates(classGenerator);
        }
    }

    public Mode getMode(QName qName) {
        if (qName == null) {
            if (this._defaultMode == null) {
                this._defaultMode = new Mode(null, this, "");
            }
            return this._defaultMode;
        }
        Mode mode = (Mode)this._modes.get(qName);
        if (mode == null) {
            String string = Integer.toString(this._nextModeSerial++);
            mode = new Mode(qName, this, string);
            this._modes.put(qName, mode);
        }
        return mode;
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        int n2 = this._globals.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            VariableBase variableBase = (VariableBase)this._globals.elementAt(i2);
            variableBase.typeCheck(symbolTable);
        }
        return this.typeCheckContents(symbolTable);
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        this.translate();
    }

    private void addDOMField(ClassGenerator classGenerator) {
        FieldGen fieldGen = new FieldGen(1, Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), "_dom", classGenerator.getConstantPool());
        classGenerator.addField(fieldGen.getField());
    }

    private void addStaticField(ClassGenerator classGenerator, String string, String string2) {
        FieldGen fieldGen = new FieldGen(12, Util.getJCRefType(string), string2, classGenerator.getConstantPool());
        classGenerator.addField(fieldGen.getField());
    }

    public void translate() {
        this._className = this.getXSLTC().getClassName();
        ClassGenerator classGenerator = new ClassGenerator(this._className, "org.apache.xalan.xsltc.runtime.AbstractTranslet", "", 33, null, this);
        this.addDOMField(classGenerator);
        this.compileTransform(classGenerator);
        Enumeration enumeration = this.elements();
        while (enumeration.hasMoreElements()) {
            TopLevelElement topLevelElement2;
            Object e2 = enumeration.nextElement();
            if (e2 instanceof Template) {
                TopLevelElement topLevelElement2 = (Template)e2;
                this.getMode(topLevelElement2.getModeName()).addTemplate((Template)topLevelElement2);
                continue;
            }
            if (e2 instanceof AttributeSet) {
                ((AttributeSet)e2).translate(classGenerator, null);
                continue;
            }
            if (!(e2 instanceof Output) || !(topLevelElement2 = (Output)e2).enabled()) continue;
            this._lastOutputElement = topLevelElement2;
        }
        this.checkOutputMethod();
        this.processModes();
        this.compileModes(classGenerator);
        this.compileStaticInitializer(classGenerator);
        this.compileConstructor(classGenerator, this._lastOutputElement);
        if (!this.getParser().errorsFound()) {
            this.getXSLTC().dumpClass(classGenerator.getJavaClass());
        }
    }

    private void compileStaticInitializer(ClassGenerator classGenerator) {
        int n2;
        int n3;
        Vector vector;
        int n4;
        int n5;
        int n6;
        int n7;
        Vector vector2;
        int n8;
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = new InstructionList();
        MethodGenerator methodGenerator = new MethodGenerator(9, org.apache.bcel.generic.Type.VOID, null, null, "<clinit>", this._className, instructionList, constantPoolGen);
        this.addStaticField(classGenerator, "[Ljava/lang/String;", "_sNamesArray");
        this.addStaticField(classGenerator, "[Ljava/lang/String;", "_sUrisArray");
        this.addStaticField(classGenerator, "[I", "_sTypesArray");
        this.addStaticField(classGenerator, "[Ljava/lang/String;", "_sNamespaceArray");
        int n9 = this.getXSLTC().getCharacterDataCount();
        for (int i2 = 0; i2 < n9; ++i2) {
            this.addStaticField(classGenerator, "[C", "_scharData" + i2);
        }
        Vector vector3 = this.getXSLTC().getNamesIndex();
        int n10 = vector3.size();
        String[] arrstring = new String[n10];
        String[] arrstring2 = new String[n10];
        int[] arrn = new int[n10];
        for (n3 = 0; n3 < n10; ++n3) {
            String string = (String)vector3.elementAt(n3);
            int n11 = string.lastIndexOf(58);
            if (n11 > -1) {
                arrstring2[n3] = string.substring(0, n11);
            }
            if (string.charAt(++n11) == '@') {
                arrn[n3] = 2;
                ++n11;
            } else if (string.charAt(n11) == '?') {
                arrn[n3] = 13;
                ++n11;
            } else {
                arrn[n3] = 1;
            }
            arrstring[n3] = n11 == 0 ? string : string.substring(n11);
        }
        methodGenerator.markChunkStart();
        instructionList.append(new PUSH(constantPoolGen, n10));
        instructionList.append(new ANEWARRAY(constantPoolGen.addClass("java.lang.String")));
        n3 = constantPoolGen.addFieldref(this._className, "_sNamesArray", "[Ljava/lang/String;");
        instructionList.append(new PUTSTATIC(n3));
        methodGenerator.markChunkEnd();
        for (n5 = 0; n5 < n10; ++n5) {
            String string = arrstring[n5];
            methodGenerator.markChunkStart();
            instructionList.append(new GETSTATIC(n3));
            instructionList.append(new PUSH(constantPoolGen, n5));
            instructionList.append(new PUSH(constantPoolGen, string));
            instructionList.append(AASTORE);
            methodGenerator.markChunkEnd();
        }
        methodGenerator.markChunkStart();
        instructionList.append(new PUSH(constantPoolGen, n10));
        instructionList.append(new ANEWARRAY(constantPoolGen.addClass("java.lang.String")));
        n5 = constantPoolGen.addFieldref(this._className, "_sUrisArray", "[Ljava/lang/String;");
        instructionList.append(new PUTSTATIC(n5));
        methodGenerator.markChunkEnd();
        for (n7 = 0; n7 < n10; ++n7) {
            String string = arrstring2[n7];
            methodGenerator.markChunkStart();
            instructionList.append(new GETSTATIC(n5));
            instructionList.append(new PUSH(constantPoolGen, n7));
            instructionList.append(new PUSH(constantPoolGen, string));
            instructionList.append(AASTORE);
            methodGenerator.markChunkEnd();
        }
        methodGenerator.markChunkStart();
        instructionList.append(new PUSH(constantPoolGen, n10));
        instructionList.append(new NEWARRAY(BasicType.INT));
        n7 = constantPoolGen.addFieldref(this._className, "_sTypesArray", "[I");
        instructionList.append(new PUTSTATIC(n7));
        methodGenerator.markChunkEnd();
        for (int i3 = 0; i3 < n10; ++i3) {
            n4 = arrn[i3];
            methodGenerator.markChunkStart();
            instructionList.append(new GETSTATIC(n7));
            instructionList.append(new PUSH(constantPoolGen, i3));
            instructionList.append(new PUSH(constantPoolGen, n4));
            instructionList.append(IASTORE);
            methodGenerator.markChunkEnd();
        }
        Vector vector4 = this.getXSLTC().getNamespaceIndex();
        methodGenerator.markChunkStart();
        instructionList.append(new PUSH(constantPoolGen, vector4.size()));
        instructionList.append(new ANEWARRAY(constantPoolGen.addClass("java.lang.String")));
        n4 = constantPoolGen.addFieldref(this._className, "_sNamespaceArray", "[Ljava/lang/String;");
        instructionList.append(new PUTSTATIC(n4));
        methodGenerator.markChunkEnd();
        for (int i4 = 0; i4 < vector4.size(); ++i4) {
            String string = (String)vector4.elementAt(i4);
            methodGenerator.markChunkStart();
            instructionList.append(new GETSTATIC(n4));
            instructionList.append(new PUSH(constantPoolGen, i4));
            instructionList.append(new PUSH(constantPoolGen, string));
            instructionList.append(AASTORE);
            methodGenerator.markChunkEnd();
        }
        Vector vector5 = this.getXSLTC().getNSAncestorPointers();
        if (vector5 != null && vector5.size() != 0) {
            this.addStaticField(classGenerator, "[I", "_sNamespaceAncestorsArray");
            methodGenerator.markChunkStart();
            instructionList.append(new PUSH(constantPoolGen, vector5.size()));
            instructionList.append(new NEWARRAY(BasicType.INT));
            int n12 = constantPoolGen.addFieldref(this._className, "_sNamespaceAncestorsArray", "[I");
            instructionList.append(new PUTSTATIC(n12));
            methodGenerator.markChunkEnd();
            for (n6 = 0; n6 < vector5.size(); ++n6) {
                n8 = (Integer)vector5.get(n6);
                methodGenerator.markChunkStart();
                instructionList.append(new GETSTATIC(n12));
                instructionList.append(new PUSH(constantPoolGen, n6));
                instructionList.append(new PUSH(constantPoolGen, n8));
                instructionList.append(IASTORE);
                methodGenerator.markChunkEnd();
            }
        }
        if ((vector2 = this.getXSLTC().getPrefixURIPairsIdx()) != null && vector2.size() != 0) {
            this.addStaticField(classGenerator, "[I", "_sPrefixURIsIdxArray");
            methodGenerator.markChunkStart();
            instructionList.append(new PUSH(constantPoolGen, vector2.size()));
            instructionList.append(new NEWARRAY(BasicType.INT));
            n6 = constantPoolGen.addFieldref(this._className, "_sPrefixURIsIdxArray", "[I");
            instructionList.append(new PUTSTATIC(n6));
            methodGenerator.markChunkEnd();
            for (n8 = 0; n8 < vector2.size(); ++n8) {
                n2 = (Integer)vector2.get(n8);
                methodGenerator.markChunkStart();
                instructionList.append(new GETSTATIC(n6));
                instructionList.append(new PUSH(constantPoolGen, n8));
                instructionList.append(new PUSH(constantPoolGen, n2));
                instructionList.append(IASTORE);
                methodGenerator.markChunkEnd();
            }
        }
        if ((vector = this.getXSLTC().getPrefixURIPairs()) != null && vector.size() != 0) {
            this.addStaticField(classGenerator, "[Ljava/lang/String;", "_sPrefixURIPairsArray");
            methodGenerator.markChunkStart();
            instructionList.append(new PUSH(constantPoolGen, vector.size()));
            instructionList.append(new ANEWARRAY(constantPoolGen.addClass("java.lang.String")));
            n8 = constantPoolGen.addFieldref(this._className, "_sPrefixURIPairsArray", "[Ljava/lang/String;");
            instructionList.append(new PUTSTATIC(n8));
            methodGenerator.markChunkEnd();
            for (n2 = 0; n2 < vector.size(); ++n2) {
                String string = (String)vector.get(n2);
                methodGenerator.markChunkStart();
                instructionList.append(new GETSTATIC(n8));
                instructionList.append(new PUSH(constantPoolGen, n2));
                instructionList.append(new PUSH(constantPoolGen, string));
                instructionList.append(AASTORE);
                methodGenerator.markChunkEnd();
            }
        }
        n8 = this.getXSLTC().getCharacterDataCount();
        n2 = constantPoolGen.addMethodref("java.lang.String", "toCharArray", "()[C");
        for (int i5 = 0; i5 < n8; ++i5) {
            methodGenerator.markChunkStart();
            instructionList.append(new PUSH(constantPoolGen, this.getXSLTC().getCharacterData(i5)));
            instructionList.append(new INVOKEVIRTUAL(n2));
            instructionList.append(new PUTSTATIC(constantPoolGen.addFieldref(this._className, "_scharData" + i5, "[C")));
            methodGenerator.markChunkEnd();
        }
        instructionList.append(RETURN);
        classGenerator.addMethod(methodGenerator);
    }

    private void compileConstructor(ClassGenerator classGenerator, Output output) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = new InstructionList();
        MethodGenerator methodGenerator = new MethodGenerator(1, org.apache.bcel.generic.Type.VOID, null, null, "<init>", this._className, instructionList, constantPoolGen);
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(new INVOKESPECIAL(constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "<init>", "()V")));
        methodGenerator.markChunkStart();
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(new GETSTATIC(constantPoolGen.addFieldref(this._className, "_sNamesArray", "[Ljava/lang/String;")));
        instructionList.append(new PUTFIELD(constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "namesArray", "[Ljava/lang/String;")));
        methodGenerator.markChunkEnd();
        methodGenerator.markChunkStart();
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(new GETSTATIC(constantPoolGen.addFieldref(this._className, "_sUrisArray", "[Ljava/lang/String;")));
        instructionList.append(new PUTFIELD(constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "urisArray", "[Ljava/lang/String;")));
        methodGenerator.markChunkEnd();
        methodGenerator.markChunkStart();
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(new GETSTATIC(constantPoolGen.addFieldref(this._className, "_sTypesArray", "[I")));
        instructionList.append(new PUTFIELD(constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "typesArray", "[I")));
        methodGenerator.markChunkEnd();
        methodGenerator.markChunkStart();
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(new GETSTATIC(constantPoolGen.addFieldref(this._className, "_sNamespaceArray", "[Ljava/lang/String;")));
        instructionList.append(new PUTFIELD(constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "namespaceArray", "[Ljava/lang/String;")));
        methodGenerator.markChunkEnd();
        methodGenerator.markChunkStart();
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(new PUSH(constantPoolGen, 101));
        instructionList.append(new PUTFIELD(constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "transletVersion", "I")));
        methodGenerator.markChunkEnd();
        if (this._hasIdCall) {
            methodGenerator.markChunkStart();
            instructionList.append(classGenerator.loadTranslet());
            instructionList.append(new PUSH(constantPoolGen, Boolean.TRUE));
            instructionList.append(new PUTFIELD(constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_hasIdCall", "Z")));
            methodGenerator.markChunkEnd();
        }
        if (output != null) {
            methodGenerator.markChunkStart();
            output.translate(classGenerator, methodGenerator);
            methodGenerator.markChunkEnd();
        }
        if (this._numberFormattingUsed) {
            methodGenerator.markChunkStart();
            DecimalFormatting.translateDefaultDFS(classGenerator, methodGenerator);
            methodGenerator.markChunkEnd();
        }
        instructionList.append(RETURN);
        classGenerator.addMethod(methodGenerator);
    }

    private String compileTopLevel(ClassGenerator classGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        org.apache.bcel.generic.Type[] arrtype = new org.apache.bcel.generic.Type[]{Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), Util.getJCRefType(TRANSLET_OUTPUT_SIG)};
        String[] arrstring = new String[]{"document", "iterator", "handler"};
        InstructionList instructionList = new InstructionList();
        MethodGenerator methodGenerator = new MethodGenerator(1, org.apache.bcel.generic.Type.VOID, arrtype, arrstring, "topLevel", this._className, instructionList, classGenerator.getConstantPool());
        methodGenerator.addException("org.apache.xalan.xsltc.TransletException");
        LocalVariableGen localVariableGen = methodGenerator.addLocalVariable("current", org.apache.bcel.generic.Type.INT, null, null);
        int n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "setFilter", "(Lorg/apache/xalan/xsltc/StripFilter;)V");
        int n3 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getIterator", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(new INVOKEINTERFACE(n3, 1));
        instructionList.append(methodGenerator.nextNode());
        localVariableGen.setStart(instructionList.append(new ISTORE(localVariableGen.getIndex())));
        Vector vector = new Vector(this._globals);
        Enumeration enumeration = this.elements();
        while (enumeration.hasMoreElements()) {
            Object e2 = enumeration.nextElement();
            if (!(e2 instanceof Key)) continue;
            vector.add(e2);
        }
        vector = this.resolveDependencies(vector);
        int n4 = vector.size();
        for (int i2 = 0; i2 < n4; ++i2) {
            TopLevelElement topLevelElement = (TopLevelElement)vector.elementAt(i2);
            topLevelElement.translate(classGenerator, methodGenerator);
            if (!(topLevelElement instanceof Key)) continue;
            Key key = (Key)topLevelElement;
            this._keys.put(key.getName(), key);
        }
        Vector vector2 = new Vector();
        enumeration = this.elements();
        while (enumeration.hasMoreElements()) {
            Object e2 = enumeration.nextElement();
            if (e2 instanceof DecimalFormatting) {
                ((DecimalFormatting)e2).translate(classGenerator, methodGenerator);
                continue;
            }
            if (!(e2 instanceof Whitespace)) continue;
            vector2.addAll(((Whitespace)e2).getRules());
        }
        if (vector2.size() > 0) {
            Whitespace.translateRules(vector2, classGenerator);
        }
        if (classGenerator.containsMethod("stripSpace", "(Lorg/apache/xalan/xsltc/DOM;II)Z") != null) {
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(classGenerator.loadTranslet());
            instructionList.append(new INVOKEINTERFACE(n2, 2));
        }
        instructionList.append(RETURN);
        classGenerator.addMethod(methodGenerator);
        return "(Lorg/apache/xalan/xsltc/DOM;Lorg/apache/xml/dtm/DTMAxisIterator;" + TRANSLET_OUTPUT_SIG + ")V";
    }

    private Vector resolveDependencies(Vector vector) {
        Vector<TopLevelElement> vector2 = new Vector<TopLevelElement>();
        while (vector.size() > 0) {
            boolean bl = false;
            int n2 = 0;
            while (n2 < vector.size()) {
                TopLevelElement topLevelElement = (TopLevelElement)vector.elementAt(n2);
                Vector vector3 = topLevelElement.getDependencies();
                if (vector3 == null || vector2.containsAll(vector3)) {
                    vector2.addElement(topLevelElement);
                    vector.remove(n2);
                    bl = true;
                    continue;
                }
                ++n2;
            }
            if (bl) continue;
            ErrorMsg errorMsg = new ErrorMsg("CIRCULAR_VARIABLE_ERR", (Object)vector.toString(), this);
            this.getParser().reportError(3, errorMsg);
            return vector2;
        }
        return vector2;
    }

    private String compileBuildKeys(ClassGenerator classGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        org.apache.bcel.generic.Type[] arrtype = new org.apache.bcel.generic.Type[]{Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), Util.getJCRefType(TRANSLET_OUTPUT_SIG), org.apache.bcel.generic.Type.INT};
        String[] arrstring = new String[]{"document", "iterator", "handler", "current"};
        InstructionList instructionList = new InstructionList();
        MethodGenerator methodGenerator = new MethodGenerator(1, org.apache.bcel.generic.Type.VOID, arrtype, arrstring, "buildKeys", this._className, instructionList, classGenerator.getConstantPool());
        methodGenerator.addException("org.apache.xalan.xsltc.TransletException");
        Enumeration enumeration = this.elements();
        while (enumeration.hasMoreElements()) {
            Object e2 = enumeration.nextElement();
            if (!(e2 instanceof Key)) continue;
            Key key = (Key)e2;
            key.translate(classGenerator, methodGenerator);
            this._keys.put(key.getName(), key);
        }
        instructionList.append(RETURN);
        classGenerator.addMethod(methodGenerator);
        return "(Lorg/apache/xalan/xsltc/DOM;Lorg/apache/xml/dtm/DTMAxisIterator;" + TRANSLET_OUTPUT_SIG + "I)V";
    }

    private void compileTransform(ClassGenerator classGenerator) {
        int n2;
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        org.apache.bcel.generic.Type[] arrtype = new org.apache.bcel.generic.Type[]{Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), Util.getJCRefType(TRANSLET_OUTPUT_SIG)};
        String[] arrstring = new String[]{"document", "iterator", "handler"};
        InstructionList instructionList = new InstructionList();
        MethodGenerator methodGenerator = new MethodGenerator(1, org.apache.bcel.generic.Type.VOID, arrtype, arrstring, "transform", this._className, instructionList, classGenerator.getConstantPool());
        methodGenerator.addException("org.apache.xalan.xsltc.TransletException");
        LocalVariableGen localVariableGen = methodGenerator.addLocalVariable("current", org.apache.bcel.generic.Type.INT, null, null);
        String string = classGenerator.getApplyTemplatesSig();
        int n3 = constantPoolGen.addMethodref(this.getClassName(), "applyTemplates", string);
        int n4 = constantPoolGen.addFieldref(this.getClassName(), "_dom", "Lorg/apache/xalan/xsltc/DOM;");
        instructionList.append(classGenerator.loadTranslet());
        if (this.isMultiDocument()) {
            instructionList.append(new NEW(constantPoolGen.addClass("org.apache.xalan.xsltc.dom.MultiDOM")));
            instructionList.append(DUP);
        }
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(new INVOKEVIRTUAL(constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "makeDOMAdapter", "(Lorg/apache/xalan/xsltc/DOM;)Lorg/apache/xalan/xsltc/dom/DOMAdapter;")));
        if (this.isMultiDocument()) {
            n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.MultiDOM", "<init>", "(Lorg/apache/xalan/xsltc/DOM;)V");
            instructionList.append(new INVOKESPECIAL(n2));
        }
        instructionList.append(new PUTFIELD(n4));
        n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getIterator", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(new INVOKEINTERFACE(n2, 1));
        instructionList.append(methodGenerator.nextNode());
        localVariableGen.setStart(instructionList.append(new ISTORE(localVariableGen.getIndex())));
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(methodGenerator.loadHandler());
        int n5 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "transferOutputSettings", "(" + OUTPUT_HANDLER_SIG + ")V");
        instructionList.append(new INVOKEVIRTUAL(n5));
        String string2 = this.compileBuildKeys(classGenerator);
        int n6 = constantPoolGen.addMethodref(this.getClassName(), "buildKeys", string2);
        Enumeration enumeration = this.elements();
        if (this._globals.size() > 0 || enumeration.hasMoreElements()) {
            String string3 = this.compileTopLevel(classGenerator);
            int n7 = constantPoolGen.addMethodref(this.getClassName(), "topLevel", string3);
            instructionList.append(classGenerator.loadTranslet());
            instructionList.append(classGenerator.loadTranslet());
            instructionList.append(new GETFIELD(n4));
            instructionList.append(methodGenerator.loadIterator());
            instructionList.append(methodGenerator.loadHandler());
            instructionList.append(new INVOKEVIRTUAL(n7));
        }
        instructionList.append(methodGenerator.loadHandler());
        instructionList.append(methodGenerator.startDocument());
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(new GETFIELD(n4));
        instructionList.append(methodGenerator.loadIterator());
        instructionList.append(methodGenerator.loadHandler());
        instructionList.append(new INVOKEVIRTUAL(n3));
        instructionList.append(methodGenerator.loadHandler());
        instructionList.append(methodGenerator.endDocument());
        instructionList.append(RETURN);
        classGenerator.addMethod(methodGenerator);
    }

    private void peepHoleOptimization(MethodGenerator methodGenerator) {
        String string = "`aload'`pop'`instruction'";
        InstructionList instructionList = methodGenerator.getInstructionList();
        InstructionFinder instructionFinder = new InstructionFinder(instructionList);
        Iterator iterator = instructionFinder.search("`aload'`pop'`instruction'");
        while (iterator.hasNext()) {
            InstructionHandle[] arrinstructionHandle = (InstructionHandle[])iterator.next();
            try {
                instructionList.delete(arrinstructionHandle[0], arrinstructionHandle[1]);
            }
            catch (TargetLostException targetLostException) {}
        }
    }

    public int addParam(Param param) {
        this._globals.addElement(param);
        return this._globals.size() - 1;
    }

    public int addVariable(Variable variable) {
        this._globals.addElement(variable);
        return this._globals.size() - 1;
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("Stylesheet");
        this.displayContents(n2 + 4);
    }

    public String getNamespace(String string) {
        return this.lookupNamespace(string);
    }

    public String getClassName() {
        return this._className;
    }

    public Vector getTemplates() {
        return this._templates;
    }

    public Vector getAllValidTemplates() {
        if (this._includedStylesheets == null) {
            return this._templates;
        }
        if (this._allValidTemplates == null) {
            Vector vector = new Vector();
            int n2 = this._includedStylesheets.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Stylesheet stylesheet = (Stylesheet)this._includedStylesheets.elementAt(i2);
                vector.addAll(stylesheet.getAllValidTemplates());
            }
            vector.addAll(this._templates);
            if (this._parentStylesheet != null) {
                return vector;
            }
            this._allValidTemplates = vector;
        }
        return this._allValidTemplates;
    }

    protected void addTemplate(Template template) {
        this._templates.addElement(template);
    }
}

