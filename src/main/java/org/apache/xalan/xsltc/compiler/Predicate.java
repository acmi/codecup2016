/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.ArrayList;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.AbsoluteLocationPath;
import org.apache.xalan.xsltc.compiler.BooleanExpr;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Closure;
import org.apache.xalan.xsltc.compiler.EqualityExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.LiteralExpr;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.Pattern;
import org.apache.xalan.xsltc.compiler.PositionCall;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.Step;
import org.apache.xalan.xsltc.compiler.StepPattern;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.TopLevelElement;
import org.apache.xalan.xsltc.compiler.VariableBase;
import org.apache.xalan.xsltc.compiler.VariableRefBase;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.FilterGenerator;
import org.apache.xalan.xsltc.compiler.util.IntType;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NumberType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.ResultTreeType;
import org.apache.xalan.xsltc.compiler.util.TestGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class Predicate
extends Expression
implements Closure {
    private Expression _exp = null;
    private boolean _canOptimize = true;
    private boolean _nthPositionFilter = false;
    private boolean _nthDescendant = false;
    int _ptype = -1;
    private String _className = null;
    private ArrayList _closureVars = null;
    private Closure _parentClosure = null;
    private Expression _value = null;
    private Step _step = null;

    public Predicate(Expression expression) {
        this._exp = expression;
        this._exp.setParent(this);
    }

    public void setParser(Parser parser) {
        super.setParser(parser);
        this._exp.setParser(parser);
    }

    public boolean isNthPositionFilter() {
        return this._nthPositionFilter;
    }

    public boolean isNthDescendant() {
        return this._nthDescendant;
    }

    public void dontOptimize() {
        this._canOptimize = false;
    }

    public boolean hasPositionCall() {
        return this._exp.hasPositionCall();
    }

    public boolean hasLastCall() {
        return this._exp.hasLastCall();
    }

    public boolean inInnerClass() {
        return this._className != null;
    }

    public Closure getParentClosure() {
        if (this._parentClosure == null) {
            SyntaxTreeNode syntaxTreeNode = this.getParent();
            do {
                if (!(syntaxTreeNode instanceof Closure)) continue;
                this._parentClosure = (Closure)((Object)syntaxTreeNode);
                break;
            } while (!(syntaxTreeNode instanceof TopLevelElement) && (syntaxTreeNode = syntaxTreeNode.getParent()) != null);
        }
        return this._parentClosure;
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
            Closure closure = this.getParentClosure();
            if (closure != null) {
                closure.addVariable(variableRefBase);
            }
        }
    }

    public int getPosType() {
        if (this._ptype == -1) {
            SyntaxTreeNode syntaxTreeNode = this.getParent();
            if (syntaxTreeNode instanceof StepPattern) {
                this._ptype = ((StepPattern)syntaxTreeNode).getNodeType();
            } else if (syntaxTreeNode instanceof AbsoluteLocationPath) {
                AbsoluteLocationPath absoluteLocationPath = (AbsoluteLocationPath)syntaxTreeNode;
                Expression expression = absoluteLocationPath.getPath();
                if (expression instanceof Step) {
                    this._ptype = ((Step)expression).getNodeType();
                }
            } else if (syntaxTreeNode instanceof VariableRefBase) {
                VariableRefBase variableRefBase = (VariableRefBase)syntaxTreeNode;
                VariableBase variableBase = variableRefBase.getVariable();
                Expression expression = variableBase.getExpression();
                if (expression instanceof Step) {
                    this._ptype = ((Step)expression).getNodeType();
                }
            } else if (syntaxTreeNode instanceof Step) {
                this._ptype = ((Step)syntaxTreeNode).getNodeType();
            }
        }
        return this._ptype;
    }

    public boolean parentIsPattern() {
        return this.getParent() instanceof Pattern;
    }

    public Expression getExpr() {
        return this._exp;
    }

    public String toString() {
        return "pred(" + this._exp + ')';
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        Type type = this._exp.typeCheck(symbolTable);
        if (type instanceof ReferenceType) {
            type = Type.Real;
            this._exp = new CastExpr(this._exp, type);
        }
        if (type instanceof ResultTreeType) {
            this._exp = new CastExpr(this._exp, Type.Boolean);
            this._exp = new CastExpr(this._exp, Type.Real);
            type = this._exp.typeCheck(symbolTable);
        }
        if (type instanceof NumberType) {
            if (!(type instanceof IntType)) {
                this._exp = new CastExpr(this._exp, Type.Int);
            }
            if (this._canOptimize) {
                boolean bl = this._nthPositionFilter = !this._exp.hasLastCall() && !this._exp.hasPositionCall();
                if (this._nthPositionFilter) {
                    SyntaxTreeNode syntaxTreeNode = this.getParent();
                    this._nthDescendant = syntaxTreeNode instanceof Step && syntaxTreeNode.getParent() instanceof AbsoluteLocationPath;
                    this._type = Type.NodeSet;
                    return this._type;
                }
            }
            this._nthDescendant = false;
            this._nthPositionFilter = false;
            QName qName = this.getParser().getQNameIgnoreDefaultNs("position");
            PositionCall positionCall = new PositionCall(qName);
            positionCall.setParser(this.getParser());
            positionCall.setParent(this);
            this._exp = new EqualityExpr(0, positionCall, this._exp);
            if (this._exp.typeCheck(symbolTable) != Type.Boolean) {
                this._exp = new CastExpr(this._exp, Type.Boolean);
            }
            this._type = Type.Boolean;
            return this._type;
        }
        if (!(type instanceof BooleanType)) {
            this._exp = new CastExpr(this._exp, Type.Boolean);
        }
        this._type = Type.Boolean;
        return this._type;
    }

    private void compileFilter(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Object object;
        this._className = this.getXSLTC().getHelperClassName();
        FilterGenerator filterGenerator = new FilterGenerator(this._className, "java.lang.Object", this.toString(), 33, new String[]{"org.apache.xalan.xsltc.dom.CurrentNodeListFilter"}, classGenerator.getStylesheet());
        ConstantPoolGen constantPoolGen = filterGenerator.getConstantPool();
        int n2 = this._closureVars == null ? 0 : this._closureVars.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            object = ((VariableRefBase)this._closureVars.get(i2)).getVariable();
            filterGenerator.addField(new Field(1, constantPoolGen.addUtf8(object.getEscapedName()), constantPoolGen.addUtf8(object.getType().toSignature()), null, constantPoolGen.getConstantPool()));
        }
        InstructionList instructionList = new InstructionList();
        TestGenerator testGenerator = new TestGenerator(17, org.apache.bcel.generic.Type.BOOLEAN, new org.apache.bcel.generic.Type[]{org.apache.bcel.generic.Type.INT, org.apache.bcel.generic.Type.INT, org.apache.bcel.generic.Type.INT, org.apache.bcel.generic.Type.INT, Util.getJCRefType("Lorg/apache/xalan/xsltc/runtime/AbstractTranslet;"), Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;")}, new String[]{"node", "position", "last", "current", "translet", "iterator"}, "test", this._className, instructionList, constantPoolGen);
        LocalVariableGen localVariableGen = testGenerator.addLocalVariable("document", Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), null, null);
        object = classGenerator.getClassName();
        instructionList.append(filterGenerator.loadTranslet());
        instructionList.append(new CHECKCAST(constantPoolGen.addClass((String)object)));
        instructionList.append(new GETFIELD(constantPoolGen.addFieldref((String)object, "_dom", "Lorg/apache/xalan/xsltc/DOM;")));
        localVariableGen.setStart(instructionList.append(new ASTORE(localVariableGen.getIndex())));
        testGenerator.setDomIndex(localVariableGen.getIndex());
        this._exp.translate(filterGenerator, testGenerator);
        instructionList.append(IRETURN);
        filterGenerator.addEmptyConstructor(1);
        filterGenerator.addMethod(testGenerator);
        this.getXSLTC().dumpClass(filterGenerator.getJavaClass());
    }

    public boolean isBooleanTest() {
        return this._exp instanceof BooleanExpr;
    }

    public boolean isNodeValueTest() {
        if (!this._canOptimize) {
            return false;
        }
        return this.getStep() != null && this.getCompareValue() != null;
    }

    public Step getStep() {
        if (this._step != null) {
            return this._step;
        }
        if (this._exp == null) {
            return null;
        }
        if (this._exp instanceof EqualityExpr) {
            EqualityExpr equalityExpr = (EqualityExpr)this._exp;
            Expression expression = equalityExpr.getLeft();
            Expression expression2 = equalityExpr.getRight();
            if (expression instanceof CastExpr) {
                expression = ((CastExpr)expression).getExpr();
            }
            if (expression instanceof Step) {
                this._step = (Step)expression;
            }
            if (expression2 instanceof CastExpr) {
                expression2 = ((CastExpr)expression2).getExpr();
            }
            if (expression2 instanceof Step) {
                this._step = (Step)expression2;
            }
        }
        return this._step;
    }

    public Expression getCompareValue() {
        if (this._value != null) {
            return this._value;
        }
        if (this._exp == null) {
            return null;
        }
        if (this._exp instanceof EqualityExpr) {
            EqualityExpr equalityExpr = (EqualityExpr)this._exp;
            Expression expression = equalityExpr.getLeft();
            Expression expression2 = equalityExpr.getRight();
            if (expression instanceof LiteralExpr) {
                this._value = expression;
                return this._value;
            }
            if (expression instanceof VariableRefBase && expression.getType() == Type.String) {
                this._value = expression;
                return this._value;
            }
            if (expression2 instanceof LiteralExpr) {
                this._value = expression2;
                return this._value;
            }
            if (expression2 instanceof VariableRefBase && expression2.getType() == Type.String) {
                this._value = expression2;
                return this._value;
            }
        }
        return null;
    }

    public void translateFilter(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        this.compileFilter(classGenerator, methodGenerator);
        instructionList.append(new NEW(constantPoolGen.addClass(this._className)));
        instructionList.append(DUP);
        instructionList.append(new INVOKESPECIAL(constantPoolGen.addMethodref(this._className, "<init>", "()V")));
        int n2 = this._closureVars == null ? 0 : this._closureVars.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Closure closure;
            VariableRefBase variableRefBase = (VariableRefBase)this._closureVars.get(i2);
            VariableBase variableBase = variableRefBase.getVariable();
            Type type = variableBase.getType();
            instructionList.append(DUP);
            for (closure = this._parentClosure; closure != null && !closure.inInnerClass(); closure = closure.getParentClosure()) {
            }
            if (closure != null) {
                instructionList.append(ALOAD_0);
                instructionList.append(new GETFIELD(constantPoolGen.addFieldref(closure.getInnerClassName(), variableBase.getEscapedName(), type.toSignature())));
            } else {
                instructionList.append(variableBase.loadInstruction());
            }
            instructionList.append(new PUTFIELD(constantPoolGen.addFieldref(this._className, variableBase.getEscapedName(), type.toSignature())));
        }
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (this._nthPositionFilter || this._nthDescendant) {
            this._exp.translate(classGenerator, methodGenerator);
        } else if (this.isNodeValueTest() && this.getParent() instanceof Step) {
            this._value.translate(classGenerator, methodGenerator);
            instructionList.append(new CHECKCAST(constantPoolGen.addClass("java.lang.String")));
            instructionList.append(new PUSH(constantPoolGen, ((EqualityExpr)this._exp).getOp()));
        } else {
            this.translateFilter(classGenerator, methodGenerator);
        }
    }
}

