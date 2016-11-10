/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.KeyCall;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.Predicate;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

class FilterExpr
extends Expression {
    private Expression _primary;
    private final Vector _predicates;

    public FilterExpr(Expression expression, Vector vector) {
        this._primary = expression;
        this._predicates = vector;
        expression.setParent(this);
    }

    protected Expression getExpr() {
        if (this._primary instanceof CastExpr) {
            return ((CastExpr)this._primary).getExpr();
        }
        return this._primary;
    }

    public void setParser(Parser parser) {
        super.setParser(parser);
        this._primary.setParser(parser);
        if (this._predicates != null) {
            int n2 = this._predicates.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Expression expression = (Expression)this._predicates.elementAt(i2);
                expression.setParser(parser);
                expression.setParent(this);
            }
        }
    }

    public String toString() {
        return "filter-expr(" + this._primary + ", " + this._predicates + ")";
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        Type type = this._primary.typeCheck(symbolTable);
        boolean bl = this._primary instanceof KeyCall;
        if (!(type instanceof NodeSetType)) {
            if (type instanceof ReferenceType) {
                this._primary = new CastExpr(this._primary, Type.NodeSet);
            } else {
                throw new TypeCheckError(this);
            }
        }
        int n2 = this._predicates.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Predicate predicate = (Predicate)this._predicates.elementAt(i2);
            if (!bl) {
                predicate.dontOptimize();
            }
            predicate.typeCheck(symbolTable);
        }
        this._type = Type.NodeSet;
        return this._type;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        if (this._predicates.size() > 0) {
            this.translatePredicates(classGenerator, methodGenerator);
        } else {
            this._primary.translate(classGenerator, methodGenerator);
        }
    }

    public void translatePredicates(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (this._predicates.size() == 0) {
            this.translate(classGenerator, methodGenerator);
        } else {
            Predicate predicate = (Predicate)this._predicates.lastElement();
            this._predicates.remove(predicate);
            this.translatePredicates(classGenerator, methodGenerator);
            if (predicate.isNthPositionFilter()) {
                int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.NthIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;I)V");
                LocalVariableGen localVariableGen = methodGenerator.addLocalVariable("filter_expr_tmp1", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
                localVariableGen.setStart(instructionList.append(new ASTORE(localVariableGen.getIndex())));
                predicate.translate(classGenerator, methodGenerator);
                LocalVariableGen localVariableGen2 = methodGenerator.addLocalVariable("filter_expr_tmp2", Util.getJCRefType("I"), null, null);
                localVariableGen2.setStart(instructionList.append(new ISTORE(localVariableGen2.getIndex())));
                instructionList.append(new NEW(constantPoolGen.addClass("org.apache.xalan.xsltc.dom.NthIterator")));
                instructionList.append(DUP);
                localVariableGen.setEnd(instructionList.append(new ALOAD(localVariableGen.getIndex())));
                localVariableGen2.setEnd(instructionList.append(new ILOAD(localVariableGen2.getIndex())));
                instructionList.append(new INVOKESPECIAL(n2));
            } else {
                int n3 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.CurrentNodeListIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;ZLorg/apache/xalan/xsltc/dom/CurrentNodeListFilter;ILorg/apache/xalan/xsltc/runtime/AbstractTranslet;)V");
                LocalVariableGen localVariableGen = methodGenerator.addLocalVariable("filter_expr_tmp1", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
                localVariableGen.setStart(instructionList.append(new ASTORE(localVariableGen.getIndex())));
                predicate.translate(classGenerator, methodGenerator);
                LocalVariableGen localVariableGen3 = methodGenerator.addLocalVariable("filter_expr_tmp2", Util.getJCRefType("Lorg/apache/xalan/xsltc/dom/CurrentNodeListFilter;"), null, null);
                localVariableGen3.setStart(instructionList.append(new ASTORE(localVariableGen3.getIndex())));
                instructionList.append(new NEW(constantPoolGen.addClass("org.apache.xalan.xsltc.dom.CurrentNodeListIterator")));
                instructionList.append(DUP);
                localVariableGen.setEnd(instructionList.append(new ALOAD(localVariableGen.getIndex())));
                instructionList.append(ICONST_1);
                localVariableGen3.setEnd(instructionList.append(new ALOAD(localVariableGen3.getIndex())));
                instructionList.append(methodGenerator.loadCurrentNode());
                instructionList.append(classGenerator.loadTranslet());
                instructionList.append(new INVOKESPECIAL(n3));
            }
        }
    }
}

