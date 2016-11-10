/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.IF_ICMPNE;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.SIPUSH;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.Step;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.MultiHashtable;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.ResultTreeType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class CastExpr
extends Expression {
    private final Expression _left;
    private static MultiHashtable InternalTypeMap = new MultiHashtable();
    private boolean _typeTest = false;

    public CastExpr(Expression expression, Type type) throws TypeCheckError {
        Step step;
        this._left = expression;
        this._type = type;
        if (this._left instanceof Step && this._type == Type.Boolean && (step = (Step)this._left).getAxis() == 13 && step.getNodeType() != -1) {
            this._typeTest = true;
        }
        this.setParser(expression.getParser());
        this.setParent(expression.getParent());
        expression.setParent(this);
        this.typeCheck(expression.getParser().getSymbolTable());
    }

    public Expression getExpr() {
        return this._left;
    }

    public boolean hasPositionCall() {
        return this._left.hasPositionCall();
    }

    public boolean hasLastCall() {
        return this._left.hasLastCall();
    }

    public String toString() {
        return "cast(" + this._left + ", " + this._type + ")";
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        Type type = this._left.getType();
        if (type == null) {
            type = this._left.typeCheck(symbolTable);
        }
        if (type instanceof NodeType) {
            type = Type.Node;
        } else if (type instanceof ResultTreeType) {
            type = Type.ResultTree;
        }
        if (InternalTypeMap.maps(type, this._type) != null) {
            return this._type;
        }
        throw new TypeCheckError(new ErrorMsg("DATA_CONVERSION_ERR", (Object)type.toString(), (Object)this._type.toString()));
    }

    public void translateDesynthesized(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Type type = this._left.getType();
        if (this._typeTest) {
            ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
            InstructionList instructionList = methodGenerator.getInstructionList();
            int n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getExpandedTypeID", "(I)I");
            instructionList.append(new SIPUSH((short)((Step)this._left).getNodeType()));
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(methodGenerator.loadContextNode());
            instructionList.append(new INVOKEINTERFACE(n2, 2));
            this._falseList.add(instructionList.append(new IF_ICMPNE(null)));
        } else {
            this._left.translate(classGenerator, methodGenerator);
            if (this._type != type) {
                this._left.startIterator(classGenerator, methodGenerator);
                if (this._type instanceof BooleanType) {
                    FlowList flowList = type.translateToDesynthesized(classGenerator, methodGenerator, this._type);
                    if (flowList != null) {
                        this._falseList.append(flowList);
                    }
                } else {
                    type.translateTo(classGenerator, methodGenerator, this._type);
                }
            }
        }
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Type type = this._left.getType();
        this._left.translate(classGenerator, methodGenerator);
        if (!this._type.identicalTo(type)) {
            this._left.startIterator(classGenerator, methodGenerator);
            type.translateTo(classGenerator, methodGenerator, this._type);
        }
    }

    static {
        InternalTypeMap.put(Type.Boolean, Type.Boolean);
        InternalTypeMap.put(Type.Boolean, Type.Real);
        InternalTypeMap.put(Type.Boolean, Type.String);
        InternalTypeMap.put(Type.Boolean, Type.Reference);
        InternalTypeMap.put(Type.Boolean, Type.Object);
        InternalTypeMap.put(Type.Real, Type.Real);
        InternalTypeMap.put(Type.Real, Type.Int);
        InternalTypeMap.put(Type.Real, Type.Boolean);
        InternalTypeMap.put(Type.Real, Type.String);
        InternalTypeMap.put(Type.Real, Type.Reference);
        InternalTypeMap.put(Type.Real, Type.Object);
        InternalTypeMap.put(Type.Int, Type.Int);
        InternalTypeMap.put(Type.Int, Type.Real);
        InternalTypeMap.put(Type.Int, Type.Boolean);
        InternalTypeMap.put(Type.Int, Type.String);
        InternalTypeMap.put(Type.Int, Type.Reference);
        InternalTypeMap.put(Type.Int, Type.Object);
        InternalTypeMap.put(Type.String, Type.String);
        InternalTypeMap.put(Type.String, Type.Boolean);
        InternalTypeMap.put(Type.String, Type.Real);
        InternalTypeMap.put(Type.String, Type.Reference);
        InternalTypeMap.put(Type.String, Type.Object);
        InternalTypeMap.put(Type.NodeSet, Type.NodeSet);
        InternalTypeMap.put(Type.NodeSet, Type.Boolean);
        InternalTypeMap.put(Type.NodeSet, Type.Real);
        InternalTypeMap.put(Type.NodeSet, Type.String);
        InternalTypeMap.put(Type.NodeSet, Type.Node);
        InternalTypeMap.put(Type.NodeSet, Type.Reference);
        InternalTypeMap.put(Type.NodeSet, Type.Object);
        InternalTypeMap.put(Type.Node, Type.Node);
        InternalTypeMap.put(Type.Node, Type.Boolean);
        InternalTypeMap.put(Type.Node, Type.Real);
        InternalTypeMap.put(Type.Node, Type.String);
        InternalTypeMap.put(Type.Node, Type.NodeSet);
        InternalTypeMap.put(Type.Node, Type.Reference);
        InternalTypeMap.put(Type.Node, Type.Object);
        InternalTypeMap.put(Type.ResultTree, Type.ResultTree);
        InternalTypeMap.put(Type.ResultTree, Type.Boolean);
        InternalTypeMap.put(Type.ResultTree, Type.Real);
        InternalTypeMap.put(Type.ResultTree, Type.String);
        InternalTypeMap.put(Type.ResultTree, Type.NodeSet);
        InternalTypeMap.put(Type.ResultTree, Type.Reference);
        InternalTypeMap.put(Type.ResultTree, Type.Object);
        InternalTypeMap.put(Type.Reference, Type.Reference);
        InternalTypeMap.put(Type.Reference, Type.Boolean);
        InternalTypeMap.put(Type.Reference, Type.Int);
        InternalTypeMap.put(Type.Reference, Type.Real);
        InternalTypeMap.put(Type.Reference, Type.String);
        InternalTypeMap.put(Type.Reference, Type.Node);
        InternalTypeMap.put(Type.Reference, Type.NodeSet);
        InternalTypeMap.put(Type.Reference, Type.ResultTree);
        InternalTypeMap.put(Type.Reference, Type.Object);
        InternalTypeMap.put(Type.Object, Type.String);
        InternalTypeMap.put(Type.Void, Type.String);
    }
}

