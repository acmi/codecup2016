/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.ArrayList;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ArithmeticInstruction;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFNONNULL;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.AttributeValueTemplate;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Closure;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.Pattern;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.VariableBase;
import org.apache.xalan.xsltc.compiler.VariableRefBase;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MatchGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeCounterGenerator;
import org.apache.xalan.xsltc.compiler.util.RealType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xalan.xsltc.runtime.AttributeList;

final class Number
extends Instruction
implements Closure {
    private static final int LEVEL_SINGLE = 0;
    private static final int LEVEL_MULTIPLE = 1;
    private static final int LEVEL_ANY = 2;
    private static final String[] ClassNames = new String[]{"org.apache.xalan.xsltc.dom.SingleNodeCounter", "org.apache.xalan.xsltc.dom.MultipleNodeCounter", "org.apache.xalan.xsltc.dom.AnyNodeCounter"};
    private static final String[] FieldNames = new String[]{"___single_node_counter", "___multiple_node_counter", "___any_node_counter"};
    private Pattern _from = null;
    private Pattern _count = null;
    private Expression _value = null;
    private AttributeValueTemplate _lang = null;
    private AttributeValueTemplate _format = null;
    private AttributeValueTemplate _letterValue = null;
    private AttributeValueTemplate _groupingSeparator = null;
    private AttributeValueTemplate _groupingSize = null;
    private int _level = 0;
    private boolean _formatNeeded = false;
    private String _className = null;
    private ArrayList _closureVars = null;

    Number() {
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
        }
    }

    public void parseContents(Parser parser) {
        int n2 = this._attributes.getLength();
        for (int i2 = 0; i2 < n2; ++i2) {
            String string = this._attributes.getQName(i2);
            String string2 = this._attributes.getValue(i2);
            if (string.equals("value")) {
                this._value = parser.parseExpression(this, string, null);
                continue;
            }
            if (string.equals("count")) {
                this._count = parser.parsePattern(this, string, null);
                continue;
            }
            if (string.equals("from")) {
                this._from = parser.parsePattern(this, string, null);
                continue;
            }
            if (string.equals("level")) {
                if (string2.equals("single")) {
                    this._level = 0;
                    continue;
                }
                if (string2.equals("multiple")) {
                    this._level = 1;
                    continue;
                }
                if (!string2.equals("any")) continue;
                this._level = 2;
                continue;
            }
            if (string.equals("format")) {
                this._format = new AttributeValueTemplate(string2, parser, this);
                this._formatNeeded = true;
                continue;
            }
            if (string.equals("lang")) {
                this._lang = new AttributeValueTemplate(string2, parser, this);
                this._formatNeeded = true;
                continue;
            }
            if (string.equals("letter-value")) {
                this._letterValue = new AttributeValueTemplate(string2, parser, this);
                this._formatNeeded = true;
                continue;
            }
            if (string.equals("grouping-separator")) {
                this._groupingSeparator = new AttributeValueTemplate(string2, parser, this);
                this._formatNeeded = true;
                continue;
            }
            if (!string.equals("grouping-size")) continue;
            this._groupingSize = new AttributeValueTemplate(string2, parser, this);
            this._formatNeeded = true;
        }
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        Type type;
        if (this._value != null && !((type = this._value.typeCheck(symbolTable)) instanceof RealType)) {
            this._value = new CastExpr(this._value, Type.Real);
        }
        if (this._count != null) {
            this._count.typeCheck(symbolTable);
        }
        if (this._from != null) {
            this._from.typeCheck(symbolTable);
        }
        if (this._format != null) {
            this._format.typeCheck(symbolTable);
        }
        if (this._lang != null) {
            this._lang.typeCheck(symbolTable);
        }
        if (this._letterValue != null) {
            this._letterValue.typeCheck(symbolTable);
        }
        if (this._groupingSeparator != null) {
            this._groupingSeparator.typeCheck(symbolTable);
        }
        if (this._groupingSize != null) {
            this._groupingSize.typeCheck(symbolTable);
        }
        return Type.Void;
    }

    public boolean hasValue() {
        return this._value != null;
    }

    public boolean isDefault() {
        return this._from == null && this._count == null;
    }

    private void compileDefault(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Object object;
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int[] arrn = this.getXSLTC().getNumberFieldIndexes();
        if (arrn[this._level] == -1) {
            object = new Field(2, constantPoolGen.addUtf8(FieldNames[this._level]), constantPoolGen.addUtf8("Lorg/apache/xalan/xsltc/dom/NodeCounter;"), null, constantPoolGen.getConstantPool());
            classGenerator.addField((Field)object);
            arrn[this._level] = constantPoolGen.addFieldref(classGenerator.getClassName(), FieldNames[this._level], "Lorg/apache/xalan/xsltc/dom/NodeCounter;");
        }
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(new GETFIELD(arrn[this._level]));
        object = instructionList.append(new IFNONNULL(null));
        int n2 = constantPoolGen.addMethodref(ClassNames[this._level], "getDefaultNodeCounter", "(Lorg/apache/xalan/xsltc/Translet;Lorg/apache/xalan/xsltc/DOM;Lorg/apache/xml/dtm/DTMAxisIterator;)Lorg/apache/xalan/xsltc/dom/NodeCounter;");
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(methodGenerator.loadIterator());
        instructionList.append(new INVOKESTATIC(n2));
        instructionList.append(DUP);
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(SWAP);
        instructionList.append(new PUTFIELD(arrn[this._level]));
        BranchHandle branchHandle = instructionList.append(new GOTO(null));
        object.setTarget(instructionList.append(classGenerator.loadTranslet()));
        instructionList.append(new GETFIELD(arrn[this._level]));
        branchHandle.setTarget(instructionList.append(NOP));
    }

    private void compileConstructor(ClassGenerator classGenerator) {
        InstructionList instructionList = new InstructionList();
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        MethodGenerator methodGenerator = new MethodGenerator(1, org.apache.bcel.generic.Type.VOID, new org.apache.bcel.generic.Type[]{Util.getJCRefType("Lorg/apache/xalan/xsltc/Translet;"), Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;")}, new String[]{"dom", "translet", "iterator"}, "<init>", this._className, instructionList, constantPoolGen);
        instructionList.append(ALOAD_0);
        instructionList.append(ALOAD_1);
        instructionList.append(ALOAD_2);
        instructionList.append(new ALOAD(3));
        int n2 = constantPoolGen.addMethodref(ClassNames[this._level], "<init>", "(Lorg/apache/xalan/xsltc/Translet;Lorg/apache/xalan/xsltc/DOM;Lorg/apache/xml/dtm/DTMAxisIterator;)V");
        instructionList.append(new INVOKESPECIAL(n2));
        instructionList.append(RETURN);
        classGenerator.addMethod(methodGenerator);
    }

    private void compileLocals(NodeCounterGenerator nodeCounterGenerator, MatchGenerator matchGenerator, InstructionList instructionList) {
        ConstantPoolGen constantPoolGen = nodeCounterGenerator.getConstantPool();
        LocalVariableGen localVariableGen = matchGenerator.addLocalVariable("iterator", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
        int n2 = constantPoolGen.addFieldref("org.apache.xalan.xsltc.dom.NodeCounter", "_iterator", "Lorg/apache/xml/dtm/DTMAxisIterator;");
        instructionList.append(ALOAD_0);
        instructionList.append(new GETFIELD(n2));
        localVariableGen.setStart(instructionList.append(new ASTORE(localVariableGen.getIndex())));
        matchGenerator.setIteratorIndex(localVariableGen.getIndex());
        localVariableGen = matchGenerator.addLocalVariable("translet", Util.getJCRefType("Lorg/apache/xalan/xsltc/runtime/AbstractTranslet;"), null, null);
        n2 = constantPoolGen.addFieldref("org.apache.xalan.xsltc.dom.NodeCounter", "_translet", "Lorg/apache/xalan/xsltc/Translet;");
        instructionList.append(ALOAD_0);
        instructionList.append(new GETFIELD(n2));
        instructionList.append(new CHECKCAST(constantPoolGen.addClass("org.apache.xalan.xsltc.runtime.AbstractTranslet")));
        localVariableGen.setStart(instructionList.append(new ASTORE(localVariableGen.getIndex())));
        nodeCounterGenerator.setTransletIndex(localVariableGen.getIndex());
        localVariableGen = matchGenerator.addLocalVariable("document", Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), null, null);
        n2 = constantPoolGen.addFieldref(this._className, "_document", "Lorg/apache/xalan/xsltc/DOM;");
        instructionList.append(ALOAD_0);
        instructionList.append(new GETFIELD(n2));
        localVariableGen.setStart(instructionList.append(new ASTORE(localVariableGen.getIndex())));
        matchGenerator.setDomIndex(localVariableGen.getIndex());
    }

    private void compilePatterns(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        int n2;
        MatchGenerator matchGenerator;
        this._className = this.getXSLTC().getHelperClassName();
        NodeCounterGenerator nodeCounterGenerator = new NodeCounterGenerator(this._className, ClassNames[this._level], this.toString(), 33, null, classGenerator.getStylesheet());
        InstructionList instructionList = null;
        ConstantPoolGen constantPoolGen = nodeCounterGenerator.getConstantPool();
        int n3 = this._closureVars == null ? 0 : this._closureVars.size();
        for (n2 = 0; n2 < n3; ++n2) {
            VariableBase variableBase = ((VariableRefBase)this._closureVars.get(n2)).getVariable();
            nodeCounterGenerator.addField(new Field(1, constantPoolGen.addUtf8(variableBase.getEscapedName()), constantPoolGen.addUtf8(variableBase.getType().toSignature()), null, constantPoolGen.getConstantPool()));
        }
        this.compileConstructor(nodeCounterGenerator);
        if (this._from != null) {
            instructionList = new InstructionList();
            matchGenerator = new MatchGenerator(17, org.apache.bcel.generic.Type.BOOLEAN, new org.apache.bcel.generic.Type[]{org.apache.bcel.generic.Type.INT}, new String[]{"node"}, "matchesFrom", this._className, instructionList, constantPoolGen);
            this.compileLocals(nodeCounterGenerator, matchGenerator, instructionList);
            instructionList.append(matchGenerator.loadContextNode());
            this._from.translate(nodeCounterGenerator, matchGenerator);
            this._from.synthesize(nodeCounterGenerator, matchGenerator);
            instructionList.append(IRETURN);
            nodeCounterGenerator.addMethod(matchGenerator);
        }
        if (this._count != null) {
            instructionList = new InstructionList();
            matchGenerator = new MatchGenerator(17, org.apache.bcel.generic.Type.BOOLEAN, new org.apache.bcel.generic.Type[]{org.apache.bcel.generic.Type.INT}, new String[]{"node"}, "matchesCount", this._className, instructionList, constantPoolGen);
            this.compileLocals(nodeCounterGenerator, matchGenerator, instructionList);
            instructionList.append(matchGenerator.loadContextNode());
            this._count.translate(nodeCounterGenerator, matchGenerator);
            this._count.synthesize(nodeCounterGenerator, matchGenerator);
            instructionList.append(IRETURN);
            nodeCounterGenerator.addMethod(matchGenerator);
        }
        this.getXSLTC().dumpClass(nodeCounterGenerator.getJavaClass());
        constantPoolGen = classGenerator.getConstantPool();
        instructionList = methodGenerator.getInstructionList();
        n2 = constantPoolGen.addMethodref(this._className, "<init>", "(Lorg/apache/xalan/xsltc/Translet;Lorg/apache/xalan/xsltc/DOM;Lorg/apache/xml/dtm/DTMAxisIterator;)V");
        instructionList.append(new NEW(constantPoolGen.addClass(this._className)));
        instructionList.append(DUP);
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(methodGenerator.loadIterator());
        instructionList.append(new INVOKESPECIAL(n2));
        for (int i2 = 0; i2 < n3; ++i2) {
            VariableRefBase variableRefBase = (VariableRefBase)this._closureVars.get(i2);
            VariableBase variableBase = variableRefBase.getVariable();
            Type type = variableBase.getType();
            instructionList.append(DUP);
            instructionList.append(variableBase.loadInstruction());
            instructionList.append(new PUTFIELD(constantPoolGen.addFieldref(this._className, variableBase.getEscapedName(), type.toSignature())));
        }
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        int n2;
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(classGenerator.loadTranslet());
        if (this.hasValue()) {
            this.compileDefault(classGenerator, methodGenerator);
            this._value.translate(classGenerator, methodGenerator);
            instructionList.append(new PUSH(constantPoolGen, 0.5));
            instructionList.append(DADD);
            n2 = constantPoolGen.addMethodref("java.lang.Math", "floor", "(D)D");
            instructionList.append(new INVOKESTATIC(n2));
            n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.NodeCounter", "setValue", "(D)Lorg/apache/xalan/xsltc/dom/NodeCounter;");
            instructionList.append(new INVOKEVIRTUAL(n2));
        } else if (this.isDefault()) {
            this.compileDefault(classGenerator, methodGenerator);
        } else {
            this.compilePatterns(classGenerator, methodGenerator);
        }
        if (!this.hasValue()) {
            instructionList.append(methodGenerator.loadContextNode());
            n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.NodeCounter", "setStartNode", "(I)Lorg/apache/xalan/xsltc/dom/NodeCounter;");
            instructionList.append(new INVOKEVIRTUAL(n2));
        }
        if (this._formatNeeded) {
            if (this._format != null) {
                this._format.translate(classGenerator, methodGenerator);
            } else {
                instructionList.append(new PUSH(constantPoolGen, "1"));
            }
            if (this._lang != null) {
                this._lang.translate(classGenerator, methodGenerator);
            } else {
                instructionList.append(new PUSH(constantPoolGen, "en"));
            }
            if (this._letterValue != null) {
                this._letterValue.translate(classGenerator, methodGenerator);
            } else {
                instructionList.append(new PUSH(constantPoolGen, ""));
            }
            if (this._groupingSeparator != null) {
                this._groupingSeparator.translate(classGenerator, methodGenerator);
            } else {
                instructionList.append(new PUSH(constantPoolGen, ""));
            }
            if (this._groupingSize != null) {
                this._groupingSize.translate(classGenerator, methodGenerator);
            } else {
                instructionList.append(new PUSH(constantPoolGen, "0"));
            }
            n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.NodeCounter", "getCounter", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
            instructionList.append(new INVOKEVIRTUAL(n2));
        } else {
            n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.NodeCounter", "setDefaultFormatting", "()Lorg/apache/xalan/xsltc/dom/NodeCounter;");
            instructionList.append(new INVOKEVIRTUAL(n2));
            n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.NodeCounter", "getCounter", "()Ljava/lang/String;");
            instructionList.append(new INVOKEVIRTUAL(n2));
        }
        instructionList.append(methodGenerator.loadHandler());
        n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "characters", CHARACTERSW_SIG);
        instructionList.append(new INVOKEVIRTUAL(n2));
    }
}

