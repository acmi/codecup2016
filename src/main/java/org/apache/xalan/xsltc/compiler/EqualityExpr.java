/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ArithmeticInstruction;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFNE;
import org.apache.bcel.generic.IF_ICMPEQ;
import org.apache.bcel.generic.IF_ICMPNE;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.IntType;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.NumberType;
import org.apache.xalan.xsltc.compiler.util.RealType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.ResultTreeType;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.runtime.Operators;

final class EqualityExpr
extends Expression {
    private final int _op;
    private Expression _left;
    private Expression _right;

    public EqualityExpr(int n2, Expression expression, Expression expression2) {
        this._op = n2;
        this._left = expression;
        this._left.setParent(this);
        this._right = expression2;
        this._right.setParent(this);
    }

    public void setParser(Parser parser) {
        super.setParser(parser);
        this._left.setParser(parser);
        this._right.setParser(parser);
    }

    public String toString() {
        return Operators.getOpNames(this._op) + '(' + this._left + ", " + this._right + ')';
    }

    public Expression getLeft() {
        return this._left;
    }

    public Expression getRight() {
        return this._right;
    }

    public boolean getOp() {
        return this._op != 1;
    }

    public boolean hasPositionCall() {
        if (this._left.hasPositionCall()) {
            return true;
        }
        if (this._right.hasPositionCall()) {
            return true;
        }
        return false;
    }

    public boolean hasLastCall() {
        if (this._left.hasLastCall()) {
            return true;
        }
        if (this._right.hasLastCall()) {
            return true;
        }
        return false;
    }

    private void swapArguments() {
        Expression expression = this._left;
        this._left = this._right;
        this._right = expression;
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        Type type = this._left.typeCheck(symbolTable);
        Type type2 = this._right.typeCheck(symbolTable);
        if (type.isSimple() && type2.isSimple()) {
            if (type != type2) {
                if (type instanceof BooleanType) {
                    this._right = new CastExpr(this._right, Type.Boolean);
                } else if (type2 instanceof BooleanType) {
                    this._left = new CastExpr(this._left, Type.Boolean);
                } else if (type instanceof NumberType || type2 instanceof NumberType) {
                    this._left = new CastExpr(this._left, Type.Real);
                    this._right = new CastExpr(this._right, Type.Real);
                } else {
                    this._left = new CastExpr(this._left, Type.String);
                    this._right = new CastExpr(this._right, Type.String);
                }
            }
        } else if (type instanceof ReferenceType) {
            this._right = new CastExpr(this._right, Type.Reference);
        } else if (type2 instanceof ReferenceType) {
            this._left = new CastExpr(this._left, Type.Reference);
        } else if (type instanceof NodeType && type2 == Type.String) {
            this._left = new CastExpr(this._left, Type.String);
        } else if (type == Type.String && type2 instanceof NodeType) {
            this._right = new CastExpr(this._right, Type.String);
        } else if (type instanceof NodeType && type2 instanceof NodeType) {
            this._left = new CastExpr(this._left, Type.String);
            this._right = new CastExpr(this._right, Type.String);
        } else if (!(type instanceof NodeType) || !(type2 instanceof NodeSetType)) {
            if (type instanceof NodeSetType && type2 instanceof NodeType) {
                this.swapArguments();
            } else {
                if (type instanceof NodeType) {
                    this._left = new CastExpr(this._left, Type.NodeSet);
                }
                if (type2 instanceof NodeType) {
                    this._right = new CastExpr(this._right, Type.NodeSet);
                }
                if (type.isSimple() || type instanceof ResultTreeType && type2 instanceof NodeSetType) {
                    this.swapArguments();
                }
                if (this._right.getType() instanceof IntType) {
                    this._right = new CastExpr(this._right, Type.Real);
                }
            }
        }
        this._type = Type.Boolean;
        return this._type;
    }

    public void translateDesynthesized(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Type type = this._left.getType();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (type instanceof BooleanType) {
            this._left.translate(classGenerator, methodGenerator);
            this._right.translate(classGenerator, methodGenerator);
            this._falseList.add(instructionList.append(this._op == 0 ? new IF_ICMPNE(null) : new IF_ICMPEQ(null)));
        } else if (type instanceof NumberType) {
            this._left.translate(classGenerator, methodGenerator);
            this._right.translate(classGenerator, methodGenerator);
            if (type instanceof RealType) {
                instructionList.append(DCMPG);
                this._falseList.add(instructionList.append(this._op == 0 ? new IFNE(null) : new IFEQ(null)));
            } else {
                this._falseList.add(instructionList.append(this._op == 0 ? new IF_ICMPNE(null) : new IF_ICMPEQ(null)));
            }
        } else {
            this.translate(classGenerator, methodGenerator);
            this.desynthesize(classGenerator, methodGenerator);
        }
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        Type type = this._left.getType();
        Type type2 = this._right.getType();
        if (type instanceof BooleanType || type instanceof NumberType) {
            this.translateDesynthesized(classGenerator, methodGenerator);
            this.synthesize(classGenerator, methodGenerator);
            return;
        }
        if (type instanceof StringType) {
            int n2 = constantPoolGen.addMethodref("java.lang.String", "equals", "(Ljava/lang/Object;)Z");
            this._left.translate(classGenerator, methodGenerator);
            this._right.translate(classGenerator, methodGenerator);
            instructionList.append(new INVOKEVIRTUAL(n2));
            if (this._op == 1) {
                instructionList.append(ICONST_1);
                instructionList.append(IXOR);
            }
            return;
        }
        if (type instanceof ResultTreeType) {
            if (type2 instanceof BooleanType) {
                this._right.translate(classGenerator, methodGenerator);
                if (this._op == 1) {
                    instructionList.append(ICONST_1);
                    instructionList.append(IXOR);
                }
                return;
            }
            if (type2 instanceof RealType) {
                this._left.translate(classGenerator, methodGenerator);
                type.translateTo(classGenerator, methodGenerator, Type.Real);
                this._right.translate(classGenerator, methodGenerator);
                instructionList.append(DCMPG);
                BranchHandle branchHandle = instructionList.append(this._op == 0 ? new IFNE(null) : new IFEQ(null));
                instructionList.append(ICONST_1);
                BranchHandle branchHandle2 = instructionList.append(new GOTO(null));
                branchHandle.setTarget(instructionList.append(ICONST_0));
                branchHandle2.setTarget(instructionList.append(NOP));
                return;
            }
            this._left.translate(classGenerator, methodGenerator);
            type.translateTo(classGenerator, methodGenerator, Type.String);
            this._right.translate(classGenerator, methodGenerator);
            if (type2 instanceof ResultTreeType) {
                type2.translateTo(classGenerator, methodGenerator, Type.String);
            }
            int n3 = constantPoolGen.addMethodref("java.lang.String", "equals", "(Ljava/lang/Object;)Z");
            instructionList.append(new INVOKEVIRTUAL(n3));
            if (this._op == 1) {
                instructionList.append(ICONST_1);
                instructionList.append(IXOR);
            }
            return;
        }
        if (type instanceof NodeSetType && type2 instanceof BooleanType) {
            this._left.translate(classGenerator, methodGenerator);
            this._left.startIterator(classGenerator, methodGenerator);
            Type.NodeSet.translateTo(classGenerator, methodGenerator, Type.Boolean);
            this._right.translate(classGenerator, methodGenerator);
            instructionList.append(IXOR);
            if (this._op == 0) {
                instructionList.append(ICONST_1);
                instructionList.append(IXOR);
            }
            return;
        }
        if (type instanceof NodeSetType && type2 instanceof StringType) {
            this._left.translate(classGenerator, methodGenerator);
            this._left.startIterator(classGenerator, methodGenerator);
            this._right.translate(classGenerator, methodGenerator);
            instructionList.append(new PUSH(constantPoolGen, this._op));
            instructionList.append(methodGenerator.loadDOM());
            int n4 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "compare", "(" + type.toSignature() + type2.toSignature() + "I" + "Lorg/apache/xalan/xsltc/DOM;" + ")Z");
            instructionList.append(new INVOKESTATIC(n4));
            return;
        }
        this._left.translate(classGenerator, methodGenerator);
        this._left.startIterator(classGenerator, methodGenerator);
        this._right.translate(classGenerator, methodGenerator);
        this._right.startIterator(classGenerator, methodGenerator);
        if (type2 instanceof ResultTreeType) {
            type2.translateTo(classGenerator, methodGenerator, Type.String);
            type2 = Type.String;
        }
        instructionList.append(new PUSH(constantPoolGen, this._op));
        instructionList.append(methodGenerator.loadDOM());
        int n5 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "compare", "(" + type.toSignature() + type2.toSignature() + "I" + "Lorg/apache/xalan/xsltc/DOM;" + ")Z");
        instructionList.append(new INVOKESTATIC(n5));
    }
}

