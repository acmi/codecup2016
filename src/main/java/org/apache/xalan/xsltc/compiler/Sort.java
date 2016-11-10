/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.ArrayList;
import java.util.Vector;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ArrayInstruction;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.NOP;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.StackInstruction;
import org.apache.bcel.generic.TABLESWITCH;
import org.apache.xalan.xsltc.compiler.ApplyTemplates;
import org.apache.xalan.xsltc.compiler.AttributeValue;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Closure;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.ForEach;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.VariableBase;
import org.apache.xalan.xsltc.compiler.VariableRefBase;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.CompareGenerator;
import org.apache.xalan.xsltc.compiler.util.IntType;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSortRecordFactGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSortRecordGenerator;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class Sort
extends Instruction
implements Closure {
    private Expression _select;
    private AttributeValue _order;
    private AttributeValue _caseOrder;
    private AttributeValue _dataType;
    private AttributeValue _lang;
    private String _data = null;
    private String _className = null;
    private ArrayList _closureVars = null;
    private boolean _needsSortRecordFactory = false;

    Sort() {
    }

    public boolean inInnerClass() {
        return this._className != null;
    }

    public Closure getParentClosure() {
        return null;
    }

    public String getInnerClassName() {
        return this._className;
    }

    public void addVariable(VariableRefBase variableRefBase) {
        if (this._closureVars == null) {
            this._closureVars = new ArrayList();
        }
        if (!this._closureVars.contains(variableRefBase)) {
            this._closureVars.add(variableRefBase);
            this._needsSortRecordFactory = true;
        }
    }

    private void setInnerClassName(String string) {
        this._className = string;
    }

    public void parseContents(Parser parser) {
        SyntaxTreeNode syntaxTreeNode = this.getParent();
        if (!(syntaxTreeNode instanceof ApplyTemplates) && !(syntaxTreeNode instanceof ForEach)) {
            this.reportError(this, parser, "STRAY_SORT_ERR", null);
            return;
        }
        this._select = parser.parseExpression(this, "select", "string(.)");
        String string = this.getAttribute("order");
        if (string.length() == 0) {
            string = "ascending";
        }
        this._order = AttributeValue.create(this, string, parser);
        string = this.getAttribute("data-type");
        if (string.length() == 0) {
            try {
                Type type = this._select.typeCheck(parser.getSymbolTable());
                string = type instanceof IntType ? "number" : "text";
            }
            catch (TypeCheckError typeCheckError) {
                string = "text";
            }
        }
        this._dataType = AttributeValue.create(this, string, parser);
        string = this.getAttribute("lang");
        this._lang = AttributeValue.create(this, string, parser);
        string = this.getAttribute("case-order");
        this._caseOrder = AttributeValue.create(this, string, parser);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        Type type = this._select.typeCheck(symbolTable);
        if (!(type instanceof StringType)) {
            this._select = new CastExpr(this._select, Type.String);
        }
        this._order.typeCheck(symbolTable);
        this._caseOrder.typeCheck(symbolTable);
        this._dataType.typeCheck(symbolTable);
        this._lang.typeCheck(symbolTable);
        return Type.Void;
    }

    public void translateSortType(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        this._dataType.translate(classGenerator, methodGenerator);
    }

    public void translateSortOrder(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        this._order.translate(classGenerator, methodGenerator);
    }

    public void translateCaseOrder(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        this._caseOrder.translate(classGenerator, methodGenerator);
    }

    public void translateLang(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        this._lang.translate(classGenerator, methodGenerator);
    }

    public void translateSelect(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        this._select.translate(classGenerator, methodGenerator);
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
    }

    public static void translateSortIterator(ClassGenerator classGenerator, MethodGenerator methodGenerator, Expression expression, Vector vector) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.SortingIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;Lorg/apache/xalan/xsltc/dom/NodeSortRecordFactory;)V");
        LocalVariableGen localVariableGen = methodGenerator.addLocalVariable("sort_tmp1", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
        LocalVariableGen localVariableGen2 = methodGenerator.addLocalVariable("sort_tmp2", Util.getJCRefType("Lorg/apache/xalan/xsltc/dom/NodeSortRecordFactory;"), null, null);
        if (expression == null) {
            int n3 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getAxisIterator", "(I)Lorg/apache/xml/dtm/DTMAxisIterator;");
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(new PUSH(constantPoolGen, 3));
            instructionList.append(new INVOKEINTERFACE(n3, 2));
        } else {
            expression.translate(classGenerator, methodGenerator);
        }
        localVariableGen.setStart(instructionList.append(new ASTORE(localVariableGen.getIndex())));
        Sort.compileSortRecordFactory(vector, classGenerator, methodGenerator);
        localVariableGen2.setStart(instructionList.append(new ASTORE(localVariableGen2.getIndex())));
        instructionList.append(new NEW(constantPoolGen.addClass("org.apache.xalan.xsltc.dom.SortingIterator")));
        instructionList.append(DUP);
        localVariableGen.setEnd(instructionList.append(new ALOAD(localVariableGen.getIndex())));
        localVariableGen2.setEnd(instructionList.append(new ALOAD(localVariableGen2.getIndex())));
        instructionList.append(new INVOKESPECIAL(n2));
    }

    public static void compileSortRecordFactory(Vector vector, ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Object object;
        String string = Sort.compileSortRecord(vector, classGenerator, methodGenerator);
        boolean bl = false;
        int n2 = vector.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            object = (Sort)vector.elementAt(i2);
            bl |= object._needsSortRecordFactory;
        }
        String string2 = "org/apache/xalan/xsltc/dom/NodeSortRecordFactory";
        if (bl) {
            string2 = Sort.compileSortRecordFactory(vector, classGenerator, methodGenerator, string);
        }
        object = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        LocalVariableGen localVariableGen = methodGenerator.addLocalVariable("sort_order_tmp", Util.getJCRefType("[Ljava/lang/String;"), null, null);
        instructionList.append(new PUSH((ConstantPoolGen)object, n2));
        instructionList.append(new ANEWARRAY(object.addClass("java.lang.String")));
        for (int i3 = 0; i3 < n2; ++i3) {
            Sort sort = (Sort)vector.elementAt(i3);
            instructionList.append(DUP);
            instructionList.append(new PUSH((ConstantPoolGen)object, i3));
            sort.translateSortOrder(classGenerator, methodGenerator);
            instructionList.append(AASTORE);
        }
        localVariableGen.setStart(instructionList.append(new ASTORE(localVariableGen.getIndex())));
        LocalVariableGen localVariableGen2 = methodGenerator.addLocalVariable("sort_type_tmp", Util.getJCRefType("[Ljava/lang/String;"), null, null);
        instructionList.append(new PUSH((ConstantPoolGen)object, n2));
        instructionList.append(new ANEWARRAY(object.addClass("java.lang.String")));
        for (int i4 = 0; i4 < n2; ++i4) {
            Sort sort = (Sort)vector.elementAt(i4);
            instructionList.append(DUP);
            instructionList.append(new PUSH((ConstantPoolGen)object, i4));
            sort.translateSortType(classGenerator, methodGenerator);
            instructionList.append(AASTORE);
        }
        localVariableGen2.setStart(instructionList.append(new ASTORE(localVariableGen2.getIndex())));
        LocalVariableGen localVariableGen3 = methodGenerator.addLocalVariable("sort_lang_tmp", Util.getJCRefType("[Ljava/lang/String;"), null, null);
        instructionList.append(new PUSH((ConstantPoolGen)object, n2));
        instructionList.append(new ANEWARRAY(object.addClass("java.lang.String")));
        for (int i5 = 0; i5 < n2; ++i5) {
            Sort sort = (Sort)vector.elementAt(i5);
            instructionList.append(DUP);
            instructionList.append(new PUSH((ConstantPoolGen)object, i5));
            sort.translateLang(classGenerator, methodGenerator);
            instructionList.append(AASTORE);
        }
        localVariableGen3.setStart(instructionList.append(new ASTORE(localVariableGen3.getIndex())));
        LocalVariableGen localVariableGen4 = methodGenerator.addLocalVariable("sort_case_order_tmp", Util.getJCRefType("[Ljava/lang/String;"), null, null);
        instructionList.append(new PUSH((ConstantPoolGen)object, n2));
        instructionList.append(new ANEWARRAY(object.addClass("java.lang.String")));
        for (int i6 = 0; i6 < n2; ++i6) {
            Sort sort = (Sort)vector.elementAt(i6);
            instructionList.append(DUP);
            instructionList.append(new PUSH((ConstantPoolGen)object, i6));
            sort.translateCaseOrder(classGenerator, methodGenerator);
            instructionList.append(AASTORE);
        }
        localVariableGen4.setStart(instructionList.append(new ASTORE(localVariableGen4.getIndex())));
        instructionList.append(new NEW(object.addClass(string2)));
        instructionList.append(DUP);
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(new PUSH((ConstantPoolGen)object, string));
        instructionList.append(classGenerator.loadTranslet());
        localVariableGen.setEnd(instructionList.append(new ALOAD(localVariableGen.getIndex())));
        localVariableGen2.setEnd(instructionList.append(new ALOAD(localVariableGen2.getIndex())));
        localVariableGen3.setEnd(instructionList.append(new ALOAD(localVariableGen3.getIndex())));
        localVariableGen4.setEnd(instructionList.append(new ALOAD(localVariableGen4.getIndex())));
        instructionList.append(new INVOKESPECIAL(object.addMethodref(string2, "<init>", "(Lorg/apache/xalan/xsltc/DOM;Ljava/lang/String;Lorg/apache/xalan/xsltc/Translet;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)V")));
        ArrayList<VariableRefBase> arrayList = new ArrayList<VariableRefBase>();
        for (int i7 = 0; i7 < n2; ++i7) {
            Sort sort = (Sort)vector.get(i7);
            int n3 = sort._closureVars == null ? 0 : sort._closureVars.size();
            for (int i8 = 0; i8 < n3; ++i8) {
                VariableRefBase variableRefBase = (VariableRefBase)sort._closureVars.get(i8);
                if (arrayList.contains(variableRefBase)) continue;
                VariableBase variableBase = variableRefBase.getVariable();
                instructionList.append(DUP);
                instructionList.append(variableBase.loadInstruction());
                instructionList.append(new PUTFIELD(object.addFieldref(string2, variableBase.getEscapedName(), variableBase.getType().toSignature())));
                arrayList.add(variableRefBase);
            }
        }
    }

    public static String compileSortRecordFactory(Vector vector, ClassGenerator classGenerator, MethodGenerator methodGenerator, String string) {
        String[] arrstring;
        XSLTC xSLTC = ((Sort)vector.firstElement()).getXSLTC();
        String string2 = xSLTC.getHelperClassName();
        NodeSortRecordFactGenerator nodeSortRecordFactGenerator = new NodeSortRecordFactGenerator(string2, "org/apache/xalan/xsltc/dom/NodeSortRecordFactory", string2 + ".java", 49, new String[0], classGenerator.getStylesheet());
        ConstantPoolGen constantPoolGen = nodeSortRecordFactGenerator.getConstantPool();
        int n2 = vector.size();
        ArrayList<VariableRefBase> arrayList = new ArrayList<VariableRefBase>();
        for (int i2 = 0; i2 < n2; ++i2) {
            arrstring = (String[])vector.get(i2);
            int n3 = arrstring._closureVars == null ? 0 : arrstring._closureVars.size();
            for (int i3 = 0; i3 < n3; ++i3) {
                VariableRefBase variableRefBase = (VariableRefBase)arrstring._closureVars.get(i3);
                if (arrayList.contains(variableRefBase)) continue;
                VariableBase n4 = variableRefBase.getVariable();
                nodeSortRecordFactGenerator.addField(new Field(1, constantPoolGen.addUtf8(n4.getEscapedName()), constantPoolGen.addUtf8(n4.getType().toSignature()), null, constantPoolGen.getConstantPool()));
                arrayList.add(variableRefBase);
            }
        }
        org.apache.bcel.generic.Type[] arrtype = new org.apache.bcel.generic.Type[]{Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), Util.getJCRefType("Ljava/lang/String;"), Util.getJCRefType("Lorg/apache/xalan/xsltc/Translet;"), Util.getJCRefType("[Ljava/lang/String;"), Util.getJCRefType("[Ljava/lang/String;"), Util.getJCRefType("[Ljava/lang/String;"), Util.getJCRefType("[Ljava/lang/String;")};
        arrstring = new String[]{"document", "className", "translet", "order", "type", "lang", "case_order"};
        InstructionList instructionList = new InstructionList();
        MethodGenerator methodGenerator2 = new MethodGenerator(1, org.apache.bcel.generic.Type.VOID, arrtype, arrstring, "<init>", string2, instructionList, constantPoolGen);
        instructionList.append(ALOAD_0);
        instructionList.append(ALOAD_1);
        instructionList.append(ALOAD_2);
        instructionList.append(new ALOAD(3));
        instructionList.append(new ALOAD(4));
        instructionList.append(new ALOAD(5));
        instructionList.append(new ALOAD(6));
        instructionList.append(new ALOAD(7));
        instructionList.append(new INVOKESPECIAL(constantPoolGen.addMethodref("org/apache/xalan/xsltc/dom/NodeSortRecordFactory", "<init>", "(Lorg/apache/xalan/xsltc/DOM;Ljava/lang/String;Lorg/apache/xalan/xsltc/Translet;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)V")));
        instructionList.append(RETURN);
        instructionList = new InstructionList();
        MethodGenerator methodGenerator3 = new MethodGenerator(1, Util.getJCRefType("Lorg/apache/xalan/xsltc/dom/NodeSortRecord;"), new org.apache.bcel.generic.Type[]{org.apache.bcel.generic.Type.INT, org.apache.bcel.generic.Type.INT}, new String[]{"node", "last"}, "makeNodeSortRecord", string2, instructionList, constantPoolGen);
        instructionList.append(ALOAD_0);
        instructionList.append(ILOAD_1);
        instructionList.append(ILOAD_2);
        instructionList.append(new INVOKESPECIAL(constantPoolGen.addMethodref("org/apache/xalan/xsltc/dom/NodeSortRecordFactory", "makeNodeSortRecord", "(II)Lorg/apache/xalan/xsltc/dom/NodeSortRecord;")));
        instructionList.append(DUP);
        instructionList.append(new CHECKCAST(constantPoolGen.addClass(string)));
        int n3 = arrayList.size();
        for (int i2 = 0; i2 < n3; ++i2) {
            VariableRefBase variableRefBase = (VariableRefBase)arrayList.get(i2);
            VariableBase variableBase = variableRefBase.getVariable();
            Type type = variableBase.getType();
            instructionList.append(DUP);
            instructionList.append(ALOAD_0);
            instructionList.append(new GETFIELD(constantPoolGen.addFieldref(string2, variableBase.getEscapedName(), type.toSignature())));
            instructionList.append(new PUTFIELD(constantPoolGen.addFieldref(string, variableBase.getEscapedName(), type.toSignature())));
        }
        instructionList.append(POP);
        instructionList.append(ARETURN);
        methodGenerator2.setMaxLocals();
        methodGenerator2.setMaxStack();
        nodeSortRecordFactGenerator.addMethod(methodGenerator2);
        methodGenerator3.setMaxLocals();
        methodGenerator3.setMaxStack();
        nodeSortRecordFactGenerator.addMethod(methodGenerator3);
        xSLTC.dumpClass(nodeSortRecordFactGenerator.getJavaClass());
        return string2;
    }

    private static String compileSortRecord(Vector vector, ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        XSLTC xSLTC = ((Sort)vector.firstElement()).getXSLTC();
        String string = xSLTC.getHelperClassName();
        NodeSortRecordGenerator nodeSortRecordGenerator = new NodeSortRecordGenerator(string, "org.apache.xalan.xsltc.dom.NodeSortRecord", "sort$0.java", 49, new String[0], classGenerator.getStylesheet());
        ConstantPoolGen constantPoolGen = nodeSortRecordGenerator.getConstantPool();
        int n2 = vector.size();
        ArrayList<VariableRefBase> arrayList = new ArrayList<VariableRefBase>();
        for (int i2 = 0; i2 < n2; ++i2) {
            Sort sort = (Sort)vector.get(i2);
            sort.setInnerClassName(string);
            int n3 = sort._closureVars == null ? 0 : sort._closureVars.size();
            for (int i3 = 0; i3 < n3; ++i3) {
                VariableRefBase variableRefBase = (VariableRefBase)sort._closureVars.get(i3);
                if (arrayList.contains(variableRefBase)) continue;
                VariableBase variableBase = variableRefBase.getVariable();
                nodeSortRecordGenerator.addField(new Field(1, constantPoolGen.addUtf8(variableBase.getEscapedName()), constantPoolGen.addUtf8(variableBase.getType().toSignature()), null, constantPoolGen.getConstantPool()));
                arrayList.add(variableRefBase);
            }
        }
        MethodGenerator methodGenerator2 = Sort.compileInit(vector, nodeSortRecordGenerator, constantPoolGen, string);
        MethodGenerator methodGenerator3 = Sort.compileExtract(vector, nodeSortRecordGenerator, constantPoolGen, string);
        nodeSortRecordGenerator.addMethod(methodGenerator2);
        nodeSortRecordGenerator.addMethod(methodGenerator3);
        xSLTC.dumpClass(nodeSortRecordGenerator.getJavaClass());
        return string;
    }

    private static MethodGenerator compileInit(Vector vector, NodeSortRecordGenerator nodeSortRecordGenerator, ConstantPoolGen constantPoolGen, String string) {
        InstructionList instructionList = new InstructionList();
        MethodGenerator methodGenerator = new MethodGenerator(1, org.apache.bcel.generic.Type.VOID, null, null, "<init>", string, instructionList, constantPoolGen);
        instructionList.append(ALOAD_0);
        instructionList.append(new INVOKESPECIAL(constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.NodeSortRecord", "<init>", "()V")));
        instructionList.append(RETURN);
        return methodGenerator;
    }

    private static MethodGenerator compileExtract(Vector vector, NodeSortRecordGenerator nodeSortRecordGenerator, ConstantPoolGen constantPoolGen, String string) {
        InstructionList instructionList = new InstructionList();
        CompareGenerator compareGenerator = new CompareGenerator(17, org.apache.bcel.generic.Type.STRING, new org.apache.bcel.generic.Type[]{Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), org.apache.bcel.generic.Type.INT, org.apache.bcel.generic.Type.INT, Util.getJCRefType("Lorg/apache/xalan/xsltc/runtime/AbstractTranslet;"), org.apache.bcel.generic.Type.INT}, new String[]{"dom", "current", "level", "translet", "last"}, "extractValueFromDOM", string, instructionList, constantPoolGen);
        int n2 = vector.size();
        int[] arrn = new int[n2];
        InstructionHandle[] arrinstructionHandle = new InstructionHandle[n2];
        InstructionHandle instructionHandle = null;
        if (n2 > 1) {
            instructionList.append(new ILOAD(compareGenerator.getLocalIndex("level")));
            instructionHandle = instructionList.append(new NOP());
        }
        for (int i2 = 0; i2 < n2; ++i2) {
            arrn[i2] = i2;
            Sort sort = (Sort)vector.elementAt(i2);
            arrinstructionHandle[i2] = instructionList.append(NOP);
            sort.translateSelect(nodeSortRecordGenerator, compareGenerator);
            instructionList.append(ARETURN);
        }
        if (n2 > 1) {
            InstructionHandle instructionHandle2 = instructionList.append(new PUSH(constantPoolGen, ""));
            instructionList.insert(instructionHandle, new TABLESWITCH(arrn, arrinstructionHandle, instructionHandle2));
            instructionList.append(ARETURN);
        }
        return compareGenerator;
    }
}

