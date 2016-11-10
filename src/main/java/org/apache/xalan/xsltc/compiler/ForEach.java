/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import java.util.Vector;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFGT;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.Sort;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.Variable;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.ResultTreeType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class ForEach
extends Instruction {
    private Expression _select;
    private Type _type;

    ForEach() {
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("ForEach");
        this.indent(n2 + 4);
        Util.println("select " + this._select.toString());
        this.displayContents(n2 + 4);
    }

    public void parseContents(Parser parser) {
        this._select = parser.parseExpression(this, "select", null);
        this.parseChildren(parser);
        if (this._select.isDummy()) {
            this.reportError(this, parser, "REQUIRED_ATTR_ERR", "select");
        }
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        this._type = this._select.typeCheck(symbolTable);
        if (this._type instanceof ReferenceType || this._type instanceof NodeType) {
            this._select = new CastExpr(this._select, Type.NodeSet);
            this.typeCheckContents(symbolTable);
            return Type.Void;
        }
        if (this._type instanceof NodeSetType || this._type instanceof ResultTreeType) {
            this.typeCheckContents(symbolTable);
            return Type.Void;
        }
        throw new TypeCheckError(this);
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Object object;
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        instructionList.append(methodGenerator.loadCurrentNode());
        instructionList.append(methodGenerator.loadIterator());
        Vector vector = new Vector();
        Enumeration enumeration = this.elements();
        while (enumeration.hasMoreElements()) {
            object = enumeration.nextElement();
            if (!(object instanceof Sort)) continue;
            vector.addElement(object);
        }
        if (this._type != null && this._type instanceof ResultTreeType) {
            instructionList.append(methodGenerator.loadDOM());
            if (vector.size() > 0) {
                object = new ErrorMsg("RESULT_TREE_SORT_ERR", this);
                this.getParser().reportError(4, (ErrorMsg)object);
            }
            this._select.translate(classGenerator, methodGenerator);
            this._type.translateTo(classGenerator, methodGenerator, Type.NodeSet);
            instructionList.append(SWAP);
            instructionList.append(methodGenerator.storeDOM());
        } else {
            if (vector.size() > 0) {
                Sort.translateSortIterator(classGenerator, methodGenerator, this._select, vector);
            } else {
                this._select.translate(classGenerator, methodGenerator);
            }
            if (!(this._type instanceof ReferenceType)) {
                instructionList.append(methodGenerator.loadContextNode());
                instructionList.append(methodGenerator.setStartNode());
            }
        }
        instructionList.append(methodGenerator.storeIterator());
        this.initializeVariables(classGenerator, methodGenerator);
        object = instructionList.append(new GOTO(null));
        InstructionHandle instructionHandle = instructionList.append(NOP);
        this.translateContents(classGenerator, methodGenerator);
        object.setTarget(instructionList.append(methodGenerator.loadIterator()));
        instructionList.append(methodGenerator.nextNode());
        instructionList.append(DUP);
        instructionList.append(methodGenerator.storeCurrentNode());
        instructionList.append(new IFGT(instructionHandle));
        if (this._type != null && this._type instanceof ResultTreeType) {
            instructionList.append(methodGenerator.storeDOM());
        }
        instructionList.append(methodGenerator.storeIterator());
        instructionList.append(methodGenerator.storeCurrentNode());
    }

    public void initializeVariables(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        int n2 = this.elementCount();
        for (int i2 = 0; i2 < n2; ++i2) {
            Object e2 = this.getContents().elementAt(i2);
            if (!(e2 instanceof Variable)) continue;
            Variable variable = (Variable)e2;
            variable.initialize(classGenerator, methodGenerator);
        }
    }
}

