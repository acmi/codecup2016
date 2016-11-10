/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.RelativeLocationPath;
import org.apache.xalan.xsltc.compiler.Step;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class ParentLocationPath
extends RelativeLocationPath {
    private Expression _step;
    private final RelativeLocationPath _path;
    private Type stype;
    private boolean _orderNodes = false;
    private boolean _axisMismatch = false;

    public ParentLocationPath(RelativeLocationPath relativeLocationPath, Expression expression) {
        this._path = relativeLocationPath;
        this._step = expression;
        this._path.setParent(this);
        this._step.setParent(this);
        if (this._step instanceof Step) {
            this._axisMismatch = this.checkAxisMismatch();
        }
    }

    public void setAxis(int n2) {
        this._path.setAxis(n2);
    }

    public int getAxis() {
        return this._path.getAxis();
    }

    public RelativeLocationPath getPath() {
        return this._path;
    }

    public Expression getStep() {
        return this._step;
    }

    public void setParser(Parser parser) {
        super.setParser(parser);
        this._step.setParser(parser);
        this._path.setParser(parser);
    }

    public String toString() {
        return "ParentLocationPath(" + this._path + ", " + this._step + ')';
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        this.stype = this._step.typeCheck(symbolTable);
        this._path.typeCheck(symbolTable);
        if (this._axisMismatch) {
            this.enableNodeOrdering();
        }
        this._type = Type.NodeSet;
        return this._type;
    }

    public void enableNodeOrdering() {
        SyntaxTreeNode syntaxTreeNode = this.getParent();
        if (syntaxTreeNode instanceof ParentLocationPath) {
            ((ParentLocationPath)syntaxTreeNode).enableNodeOrdering();
        } else {
            this._orderNodes = true;
        }
    }

    public boolean checkAxisMismatch() {
        int n2;
        int n3 = this._path.getAxis();
        int n4 = ((Step)this._step).getAxis();
        if (!(n3 != 0 && n3 != 1 || n4 != 3 && n4 != 4 && n4 != 5 && n4 != 10 && n4 != 11 && n4 != 12)) {
            return true;
        }
        if (n3 == 3 && n4 == 0 || n4 == 1 || n4 == 10 || n4 == 11) {
            return true;
        }
        if (n3 == 4 || n3 == 5) {
            return true;
        }
        if (!(n3 != 6 && n3 != 7 || n4 != 6 && n4 != 10 && n4 != 11 && n4 != 12)) {
            return true;
        }
        if (!(n3 != 11 && n3 != 12 || n4 != 4 && n4 != 5 && n4 != 6 && n4 != 7 && n4 != 10 && n4 != 11 && n4 != 12)) {
            return true;
        }
        if (n4 == 6 && n3 == 3 && this._path instanceof Step && (n2 = ((Step)this._path).getNodeType()) == 2) {
            return true;
        }
        return false;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        int n2;
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        this._path.translate(classGenerator, methodGenerator);
        LocalVariableGen localVariableGen = methodGenerator.addLocalVariable("parent_location_path_tmp1", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
        localVariableGen.setStart(instructionList.append(new ASTORE(localVariableGen.getIndex())));
        this._step.translate(classGenerator, methodGenerator);
        LocalVariableGen localVariableGen2 = methodGenerator.addLocalVariable("parent_location_path_tmp2", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
        localVariableGen2.setStart(instructionList.append(new ASTORE(localVariableGen2.getIndex())));
        int n3 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.StepIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;Lorg/apache/xml/dtm/DTMAxisIterator;)V");
        instructionList.append(new NEW(constantPoolGen.addClass("org.apache.xalan.xsltc.dom.StepIterator")));
        instructionList.append(DUP);
        localVariableGen.setEnd(instructionList.append(new ALOAD(localVariableGen.getIndex())));
        localVariableGen2.setEnd(instructionList.append(new ALOAD(localVariableGen2.getIndex())));
        instructionList.append(new INVOKESPECIAL(n3));
        Expression expression = this._step;
        if (expression instanceof ParentLocationPath) {
            expression = ((ParentLocationPath)expression).getStep();
        }
        if (this._path instanceof Step && expression instanceof Step) {
            n2 = ((Step)this._path).getAxis();
            int n4 = ((Step)expression).getAxis();
            if (n2 == 5 && n4 == 3 || n2 == 11 && n4 == 10) {
                int n5 = constantPoolGen.addMethodref("org.apache.xml.dtm.ref.DTMAxisIteratorBase", "includeSelf", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
                instructionList.append(new INVOKEVIRTUAL(n5));
            }
        }
        if (this._orderNodes) {
            n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "orderNodes", "(Lorg/apache/xml/dtm/DTMAxisIterator;I)Lorg/apache/xml/dtm/DTMAxisIterator;");
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(SWAP);
            instructionList.append(methodGenerator.loadContextNode());
            instructionList.append(new INVOKEINTERFACE(n2, 3));
        }
    }
}

