/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESTATIC;
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
import org.apache.xalan.xsltc.compiler.VariableBase;
import org.apache.xalan.xsltc.compiler.VariableRefBase;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.IntType;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodType;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.RealType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.ResultTreeType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.runtime.Operators;

final class RelationalExpr
extends Expression {
    private int _op;
    private Expression _left;
    private Expression _right;

    public RelationalExpr(int n2, Expression expression, Expression expression2) {
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
        return this._left.hasLastCall() || this._right.hasLastCall();
    }

    public boolean hasReferenceArgs() {
        return this._left.getType() instanceof ReferenceType || this._right.getType() instanceof ReferenceType;
    }

    public boolean hasNodeArgs() {
        return this._left.getType() instanceof NodeType || this._right.getType() instanceof NodeType;
    }

    public boolean hasNodeSetArgs() {
        return this._left.getType() instanceof NodeSetType || this._right.getType() instanceof NodeSetType;
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        MethodType methodType;
        Type type = this._left.typeCheck(symbolTable);
        Type type2 = this._right.typeCheck(symbolTable);
        if (type instanceof ResultTreeType && type2 instanceof ResultTreeType) {
            this._right = new CastExpr(this._right, Type.Real);
            this._left = new CastExpr(this._left, Type.Real);
            this._type = Type.Boolean;
            return this._type;
        }
        if (this.hasReferenceArgs()) {
            VariableBase variableBase;
            VariableRefBase variableRefBase;
            Type type3 = null;
            Type type4 = null;
            Type type5 = null;
            if (type instanceof ReferenceType && this._left instanceof VariableRefBase) {
                variableRefBase = (VariableRefBase)this._left;
                variableBase = variableRefBase.getVariable();
                type4 = variableBase.getType();
            }
            if (type2 instanceof ReferenceType && this._right instanceof VariableRefBase) {
                variableRefBase = (VariableRefBase)this._right;
                variableBase = variableRefBase.getVariable();
                type5 = variableBase.getType();
            }
            type3 = type4 == null ? type5 : (type5 == null ? type4 : Type.Real);
            if (type3 == null) {
                type3 = Type.Real;
            }
            this._right = new CastExpr(this._right, type3);
            this._left = new CastExpr(this._left, type3);
            this._type = Type.Boolean;
            return this._type;
        }
        if (this.hasNodeSetArgs()) {
            if (type2 instanceof NodeSetType) {
                Expression expression = this._right;
                this._right = this._left;
                this._left = expression;
                this._op = this._op == 2 ? 3 : (this._op == 3 ? 2 : (this._op == 4 ? 5 : 4));
                type2 = this._right.getType();
            }
            if (type2 instanceof NodeType) {
                this._right = new CastExpr(this._right, Type.NodeSet);
            }
            if (type2 instanceof IntType) {
                this._right = new CastExpr(this._right, Type.Real);
            }
            if (type2 instanceof ResultTreeType) {
                this._right = new CastExpr(this._right, Type.String);
            }
            this._type = Type.Boolean;
            return this._type;
        }
        if (this.hasNodeArgs()) {
            if (type instanceof BooleanType) {
                this._right = new CastExpr(this._right, Type.Boolean);
                type2 = Type.Boolean;
            }
            if (type2 instanceof BooleanType) {
                this._left = new CastExpr(this._left, Type.Boolean);
                type = Type.Boolean;
            }
        }
        if ((methodType = this.lookupPrimop(symbolTable, Operators.getOpNames(this._op), new MethodType(Type.Void, type, type2))) != null) {
            Type type6;
            Type type7 = (Type)methodType.argsType().elementAt(0);
            if (!type7.identicalTo(type)) {
                this._left = new CastExpr(this._left, type7);
            }
            if (!(type6 = (Type)methodType.argsType().elementAt(1)).identicalTo(type2)) {
                this._right = new CastExpr(this._right, type7);
            }
            this._type = methodType.resultType();
            return this._type;
        }
        throw new TypeCheckError(this);
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        if (this.hasNodeSetArgs() || this.hasReferenceArgs()) {
            ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
            InstructionList instructionList = methodGenerator.getInstructionList();
            this._left.translate(classGenerator, methodGenerator);
            this._left.startIterator(classGenerator, methodGenerator);
            this._right.translate(classGenerator, methodGenerator);
            this._right.startIterator(classGenerator, methodGenerator);
            instructionList.append(new PUSH(constantPoolGen, this._op));
            instructionList.append(methodGenerator.loadDOM());
            int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "compare", "(" + this._left.getType().toSignature() + this._right.getType().toSignature() + "I" + "Lorg/apache/xalan/xsltc/DOM;" + ")Z");
            instructionList.append(new INVOKESTATIC(n2));
        } else {
            this.translateDesynthesized(classGenerator, methodGenerator);
            this.synthesize(classGenerator, methodGenerator);
        }
    }

    public void translateDesynthesized(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        if (this.hasNodeSetArgs() || this.hasReferenceArgs()) {
            this.translate(classGenerator, methodGenerator);
            this.desynthesize(classGenerator, methodGenerator);
        } else {
            BranchInstruction branchInstruction = null;
            InstructionList instructionList = methodGenerator.getInstructionList();
            this._left.translate(classGenerator, methodGenerator);
            this._right.translate(classGenerator, methodGenerator);
            boolean bl = false;
            Type type = this._left.getType();
            if (type instanceof RealType) {
                instructionList.append(type.CMP(this._op == 3 || this._op == 5));
                type = Type.Int;
                bl = true;
            }
            switch (this._op) {
                case 3: {
                    branchInstruction = type.GE(bl);
                    break;
                }
                case 2: {
                    branchInstruction = type.LE(bl);
                    break;
                }
                case 5: {
                    branchInstruction = type.GT(bl);
                    break;
                }
                case 4: {
                    branchInstruction = type.LT(bl);
                    break;
                }
                default: {
                    ErrorMsg errorMsg = new ErrorMsg("ILLEGAL_RELAT_OP_ERR", this);
                    this.getParser().reportError(2, errorMsg);
                }
            }
            this._falseList.add(instructionList.append(branchInstruction));
        }
    }

    public String toString() {
        return Operators.getOpNames(this._op) + '(' + this._left + ", " + this._right + ')';
    }
}

