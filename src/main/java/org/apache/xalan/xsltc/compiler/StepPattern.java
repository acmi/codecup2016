/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.GOTO_W;
import org.apache.bcel.generic.IFLT;
import org.apache.bcel.generic.IFNE;
import org.apache.bcel.generic.IFNONNULL;
import org.apache.bcel.generic.IF_ICMPEQ;
import org.apache.bcel.generic.IF_ICMPLT;
import org.apache.bcel.generic.IF_ICMPNE;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.Predicate;
import org.apache.xalan.xsltc.compiler.RelativePathPattern;
import org.apache.xalan.xsltc.compiler.Step;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.dtm.Axis;

class StepPattern
extends RelativePathPattern {
    private static final int NO_CONTEXT = 0;
    private static final int SIMPLE_CONTEXT = 1;
    private static final int GENERAL_CONTEXT = 2;
    protected final int _axis;
    protected final int _nodeType;
    protected Vector _predicates;
    private Step _step = null;
    private boolean _isEpsilon = false;
    private int _contextCase;
    private double _priority = Double.MAX_VALUE;

    public StepPattern(int n2, int n3, Vector vector) {
        this._axis = n2;
        this._nodeType = n3;
        this._predicates = vector;
    }

    public void setParser(Parser parser) {
        super.setParser(parser);
        if (this._predicates != null) {
            int n2 = this._predicates.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Predicate predicate = (Predicate)this._predicates.elementAt(i2);
                predicate.setParser(parser);
                predicate.setParent(this);
            }
        }
    }

    public int getNodeType() {
        return this._nodeType;
    }

    public void setPriority(double d2) {
        this._priority = d2;
    }

    public StepPattern getKernelPattern() {
        return this;
    }

    public boolean isWildcard() {
        return this._isEpsilon && !this.hasPredicates();
    }

    public StepPattern setPredicates(Vector vector) {
        this._predicates = vector;
        return this;
    }

    protected boolean hasPredicates() {
        return this._predicates != null && this._predicates.size() > 0;
    }

    public double getDefaultPriority() {
        if (this._priority != Double.MAX_VALUE) {
            return this._priority;
        }
        if (this.hasPredicates()) {
            return 0.5;
        }
        switch (this._nodeType) {
            case -1: {
                return -0.5;
            }
            case 0: {
                return 0.0;
            }
        }
        return this._nodeType >= 14 ? 0.0 : -0.5;
    }

    public int getAxis() {
        return this._axis;
    }

    public void reduceKernelPattern() {
        this._isEpsilon = true;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("stepPattern(\"");
        stringBuffer.append(Axis.getNames(this._axis)).append("\", ").append(this._isEpsilon ? "epsilon{" + Integer.toString(this._nodeType) + "}" : Integer.toString(this._nodeType));
        if (this._predicates != null) {
            stringBuffer.append(", ").append(this._predicates.toString());
        }
        return stringBuffer.append(')').toString();
    }

    private int analyzeCases() {
        boolean bl = true;
        int n2 = this._predicates.size();
        for (int i2 = 0; i2 < n2 && bl; ++i2) {
            Predicate predicate = (Predicate)this._predicates.elementAt(i2);
            if (!predicate.isNthPositionFilter() && !predicate.hasPositionCall() && !predicate.hasLastCall()) continue;
            bl = false;
        }
        if (bl) {
            return 0;
        }
        if (n2 == 1) {
            return 1;
        }
        return 2;
    }

    private String getNextFieldName() {
        return "__step_pattern_iter_" + this.getXSLTC().nextStepPatternSerial();
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (this.hasPredicates()) {
            Predicate predicate;
            int n2 = this._predicates.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                predicate = (Predicate)this._predicates.elementAt(i2);
                predicate.typeCheck(symbolTable);
            }
            this._contextCase = this.analyzeCases();
            Step step = null;
            if (this._contextCase == 1) {
                predicate = (Predicate)this._predicates.elementAt(0);
                if (predicate.isNthPositionFilter()) {
                    this._contextCase = 2;
                    step = new Step(this._axis, this._nodeType, this._predicates);
                } else {
                    step = new Step(this._axis, this._nodeType, null);
                }
            } else if (this._contextCase == 2) {
                int n3 = this._predicates.size();
                for (int i3 = 0; i3 < n3; ++i3) {
                    ((Predicate)this._predicates.elementAt(i3)).dontOptimize();
                }
                step = new Step(this._axis, this._nodeType, this._predicates);
            }
            if (step != null) {
                step.setParser(this.getParser());
                step.typeCheck(symbolTable);
                this._step = step;
            }
        }
        return this._axis == 3 ? Type.Element : Type.Attribute;
    }

    private void translateKernel(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (this._nodeType == 1) {
            int n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "isElement", "(I)Z");
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(SWAP);
            instructionList.append(new INVOKEINTERFACE(n2, 2));
            BranchHandle branchHandle = instructionList.append(new IFNE(null));
            this._falseList.add(instructionList.append(new GOTO_W(null)));
            branchHandle.setTarget(instructionList.append(NOP));
        } else if (this._nodeType == 2) {
            int n3 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "isAttribute", "(I)Z");
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(SWAP);
            instructionList.append(new INVOKEINTERFACE(n3, 2));
            BranchHandle branchHandle = instructionList.append(new IFNE(null));
            this._falseList.add(instructionList.append(new GOTO_W(null)));
            branchHandle.setTarget(instructionList.append(NOP));
        } else {
            int n4 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getExpandedTypeID", "(I)I");
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(SWAP);
            instructionList.append(new INVOKEINTERFACE(n4, 2));
            instructionList.append(new PUSH(constantPoolGen, this._nodeType));
            BranchHandle branchHandle = instructionList.append(new IF_ICMPEQ(null));
            this._falseList.add(instructionList.append(new GOTO_W(null)));
            branchHandle.setTarget(instructionList.append(NOP));
        }
    }

    private void translateNoContext(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Object object;
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(methodGenerator.loadCurrentNode());
        instructionList.append(SWAP);
        instructionList.append(methodGenerator.storeCurrentNode());
        if (!this._isEpsilon) {
            instructionList.append(methodGenerator.loadCurrentNode());
            this.translateKernel(classGenerator, methodGenerator);
        }
        int n2 = this._predicates.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            object = (Predicate)this._predicates.elementAt(i2);
            Expression expression = object.getExpr();
            expression.translateDesynthesized(classGenerator, methodGenerator);
            this._trueList.append(expression._trueList);
            this._falseList.append(expression._falseList);
        }
        InstructionHandle instructionHandle = instructionList.append(methodGenerator.storeCurrentNode());
        this.backPatchTrueList(instructionHandle);
        object = instructionList.append(new GOTO(null));
        instructionHandle = instructionList.append(methodGenerator.storeCurrentNode());
        this.backPatchFalseList(instructionHandle);
        this._falseList.add(instructionList.append(new GOTO(null)));
        object.setTarget(instructionList.append(NOP));
    }

    private void translateSimpleContext(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        LocalVariableGen localVariableGen = methodGenerator.addLocalVariable("step_pattern_tmp1", Util.getJCRefType("I"), null, null);
        localVariableGen.setStart(instructionList.append(new ISTORE(localVariableGen.getIndex())));
        if (!this._isEpsilon) {
            instructionList.append(new ILOAD(localVariableGen.getIndex()));
            this.translateKernel(classGenerator, methodGenerator);
        }
        instructionList.append(methodGenerator.loadCurrentNode());
        instructionList.append(methodGenerator.loadIterator());
        int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.MatchingIterator", "<init>", "(ILorg/apache/xml/dtm/DTMAxisIterator;)V");
        this._step.translate(classGenerator, methodGenerator);
        LocalVariableGen localVariableGen2 = methodGenerator.addLocalVariable("step_pattern_tmp2", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
        localVariableGen2.setStart(instructionList.append(new ASTORE(localVariableGen2.getIndex())));
        instructionList.append(new NEW(constantPoolGen.addClass("org.apache.xalan.xsltc.dom.MatchingIterator")));
        instructionList.append(DUP);
        instructionList.append(new ILOAD(localVariableGen.getIndex()));
        localVariableGen2.setEnd(instructionList.append(new ALOAD(localVariableGen2.getIndex())));
        instructionList.append(new INVOKESPECIAL(n2));
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(new ILOAD(localVariableGen.getIndex()));
        n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getParent", "(I)I");
        instructionList.append(new INVOKEINTERFACE(n2, 2));
        instructionList.append(methodGenerator.setStartNode());
        instructionList.append(methodGenerator.storeIterator());
        localVariableGen.setEnd(instructionList.append(new ILOAD(localVariableGen.getIndex())));
        instructionList.append(methodGenerator.storeCurrentNode());
        Predicate predicate = (Predicate)this._predicates.elementAt(0);
        Expression expression = predicate.getExpr();
        expression.translateDesynthesized(classGenerator, methodGenerator);
        InstructionHandle instructionHandle = instructionList.append(methodGenerator.storeIterator());
        instructionList.append(methodGenerator.storeCurrentNode());
        expression.backPatchTrueList(instructionHandle);
        BranchHandle branchHandle = instructionList.append(new GOTO(null));
        instructionHandle = instructionList.append(methodGenerator.storeIterator());
        instructionList.append(methodGenerator.storeCurrentNode());
        expression.backPatchFalseList(instructionHandle);
        this._falseList.add(instructionList.append(new GOTO(null)));
        branchHandle.setTarget(instructionList.append(NOP));
    }

    private void translateGeneralContext(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Object object;
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n2 = 0;
        BranchHandle branchHandle = null;
        String string = this.getNextFieldName();
        LocalVariableGen localVariableGen = methodGenerator.addLocalVariable("step_pattern_tmp1", Util.getJCRefType("I"), null, null);
        localVariableGen.setStart(instructionList.append(new ISTORE(localVariableGen.getIndex())));
        LocalVariableGen localVariableGen2 = methodGenerator.addLocalVariable("step_pattern_tmp2", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
        if (!classGenerator.isExternal()) {
            object = new Field(2, constantPoolGen.addUtf8(string), constantPoolGen.addUtf8("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, constantPoolGen.getConstantPool());
            classGenerator.addField((Field)object);
            n2 = constantPoolGen.addFieldref(classGenerator.getClassName(), string, "Lorg/apache/xml/dtm/DTMAxisIterator;");
            instructionList.append(classGenerator.loadTranslet());
            instructionList.append(new GETFIELD(n2));
            instructionList.append(DUP);
            localVariableGen2.setStart(instructionList.append(new ASTORE(localVariableGen2.getIndex())));
            branchHandle = instructionList.append(new IFNONNULL(null));
            instructionList.append(classGenerator.loadTranslet());
        }
        this._step.translate(classGenerator, methodGenerator);
        object = instructionList.append(new ASTORE(localVariableGen2.getIndex()));
        if (!classGenerator.isExternal()) {
            instructionList.append(new ALOAD(localVariableGen2.getIndex()));
            instructionList.append(new PUTFIELD(n2));
            branchHandle.setTarget(instructionList.append(NOP));
        } else {
            localVariableGen2.setStart((InstructionHandle)object);
        }
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(new ILOAD(localVariableGen.getIndex()));
        int n3 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getParent", "(I)I");
        instructionList.append(new INVOKEINTERFACE(n3, 2));
        instructionList.append(new ALOAD(localVariableGen2.getIndex()));
        instructionList.append(SWAP);
        instructionList.append(methodGenerator.setStartNode());
        LocalVariableGen localVariableGen3 = methodGenerator.addLocalVariable("step_pattern_tmp3", Util.getJCRefType("I"), null, null);
        BranchHandle branchHandle2 = instructionList.append(new GOTO(null));
        InstructionHandle instructionHandle = instructionList.append(new ALOAD(localVariableGen2.getIndex()));
        localVariableGen3.setStart(instructionHandle);
        InstructionHandle instructionHandle2 = instructionList.append(methodGenerator.nextNode());
        instructionList.append(DUP);
        instructionList.append(new ISTORE(localVariableGen3.getIndex()));
        this._falseList.add(instructionList.append(new IFLT(null)));
        instructionList.append(new ILOAD(localVariableGen3.getIndex()));
        instructionList.append(new ILOAD(localVariableGen.getIndex()));
        localVariableGen2.setEnd(instructionList.append(new IF_ICMPLT(instructionHandle)));
        localVariableGen3.setEnd(instructionList.append(new ILOAD(localVariableGen3.getIndex())));
        localVariableGen.setEnd(instructionList.append(new ILOAD(localVariableGen.getIndex())));
        this._falseList.add(instructionList.append(new IF_ICMPNE(null)));
        branchHandle2.setTarget(instructionHandle2);
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (this.hasPredicates()) {
            switch (this._contextCase) {
                case 0: {
                    this.translateNoContext(classGenerator, methodGenerator);
                    break;
                }
                case 1: {
                    this.translateSimpleContext(classGenerator, methodGenerator);
                    break;
                }
                default: {
                    this.translateGeneralContext(classGenerator, methodGenerator);
                    break;
                }
            }
        } else if (this.isWildcard()) {
            instructionList.append(POP);
        } else {
            this.translateKernel(classGenerator, methodGenerator);
        }
    }
}

