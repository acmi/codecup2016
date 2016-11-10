/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFGE;
import org.apache.bcel.generic.IFGT;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.Pattern;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.TopLevelElement;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.XML11Char;

final class Key
extends TopLevelElement {
    private QName _name;
    private Pattern _match;
    private Expression _use;
    private Type _useType;

    Key() {
    }

    public void parseContents(Parser parser) {
        String string = this.getAttribute("name");
        if (!XML11Char.isXML11ValidQName(string)) {
            ErrorMsg errorMsg = new ErrorMsg("INVALID_QNAME_ERR", (Object)string, this);
            parser.reportError(3, errorMsg);
        }
        this._name = parser.getQNameIgnoreDefaultNs(string);
        this.getSymbolTable().addKey(this._name, this);
        this._match = parser.parsePattern(this, "match", null);
        this._use = parser.parseExpression(this, "use", null);
        if (this._name == null) {
            this.reportError(this, parser, "REQUIRED_ATTR_ERR", "name");
            return;
        }
        if (this._match.isDummy()) {
            this.reportError(this, parser, "REQUIRED_ATTR_ERR", "match");
            return;
        }
        if (this._use.isDummy()) {
            this.reportError(this, parser, "REQUIRED_ATTR_ERR", "use");
            return;
        }
    }

    public String getName() {
        return this._name.toString();
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        this._match.typeCheck(symbolTable);
        this._useType = this._use.typeCheck(symbolTable);
        if (!(this._useType instanceof StringType) && !(this._useType instanceof NodeSetType)) {
            this._use = new CastExpr(this._use, Type.String);
        }
        return Type.Void;
    }

    public void traverseNodeSet(ClassGenerator classGenerator, MethodGenerator methodGenerator, int n2) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n3 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getStringValueX", "(I)Ljava/lang/String;");
        int n4 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNodeIdent", "(I)I");
        int n5 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "setKeyIndexDom", "(Ljava/lang/String;Lorg/apache/xalan/xsltc/DOM;)V");
        LocalVariableGen localVariableGen = methodGenerator.addLocalVariable("parentNode", Util.getJCRefType("I"), null, null);
        localVariableGen.setStart(instructionList.append(new ISTORE(localVariableGen.getIndex())));
        instructionList.append(methodGenerator.loadCurrentNode());
        instructionList.append(methodGenerator.loadIterator());
        this._use.translate(classGenerator, methodGenerator);
        this._use.startIterator(classGenerator, methodGenerator);
        instructionList.append(methodGenerator.storeIterator());
        BranchHandle branchHandle = instructionList.append(new GOTO(null));
        InstructionHandle instructionHandle = instructionList.append(NOP);
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(new PUSH(constantPoolGen, this._name.toString()));
        localVariableGen.setEnd(instructionList.append(new ILOAD(localVariableGen.getIndex())));
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(methodGenerator.loadCurrentNode());
        instructionList.append(new INVOKEINTERFACE(n3, 2));
        instructionList.append(new INVOKEVIRTUAL(n2));
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(new PUSH(constantPoolGen, this.getName()));
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(new INVOKEVIRTUAL(n5));
        branchHandle.setTarget(instructionList.append(methodGenerator.loadIterator()));
        instructionList.append(methodGenerator.nextNode());
        instructionList.append(DUP);
        instructionList.append(methodGenerator.storeCurrentNode());
        instructionList.append(new IFGE(instructionHandle));
        instructionList.append(methodGenerator.storeIterator());
        instructionList.append(methodGenerator.storeCurrentNode());
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n2 = methodGenerator.getLocalIndex("current");
        int n3 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "buildKeyIndex", "(Ljava/lang/String;ILjava/lang/Object;)V");
        int n4 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "setKeyIndexDom", "(Ljava/lang/String;Lorg/apache/xalan/xsltc/DOM;)V");
        int n5 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNodeIdent", "(I)I");
        int n6 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getAxisIterator", "(I)Lorg/apache/xml/dtm/DTMAxisIterator;");
        instructionList.append(methodGenerator.loadCurrentNode());
        instructionList.append(methodGenerator.loadIterator());
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(new PUSH(constantPoolGen, 4));
        instructionList.append(new INVOKEINTERFACE(n6, 2));
        instructionList.append(methodGenerator.loadCurrentNode());
        instructionList.append(methodGenerator.setStartNode());
        instructionList.append(methodGenerator.storeIterator());
        BranchHandle branchHandle = instructionList.append(new GOTO(null));
        InstructionHandle instructionHandle = instructionList.append(NOP);
        instructionList.append(methodGenerator.loadCurrentNode());
        this._match.translate(classGenerator, methodGenerator);
        this._match.synthesize(classGenerator, methodGenerator);
        BranchHandle branchHandle2 = instructionList.append(new IFEQ(null));
        if (this._useType instanceof NodeSetType) {
            instructionList.append(methodGenerator.loadCurrentNode());
            this.traverseNodeSet(classGenerator, methodGenerator, n3);
        } else {
            instructionList.append(classGenerator.loadTranslet());
            instructionList.append(DUP);
            instructionList.append(new PUSH(constantPoolGen, this._name.toString()));
            instructionList.append(DUP_X1);
            instructionList.append(methodGenerator.loadCurrentNode());
            this._use.translate(classGenerator, methodGenerator);
            instructionList.append(new INVOKEVIRTUAL(n3));
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(new INVOKEVIRTUAL(n4));
        }
        InstructionHandle instructionHandle2 = instructionList.append(NOP);
        instructionList.append(methodGenerator.loadIterator());
        instructionList.append(methodGenerator.nextNode());
        instructionList.append(DUP);
        instructionList.append(methodGenerator.storeCurrentNode());
        instructionList.append(new IFGT(instructionHandle));
        instructionList.append(methodGenerator.storeIterator());
        instructionList.append(methodGenerator.storeCurrentNode());
        branchHandle.setTarget(instructionHandle2);
        branchHandle2.setTarget(instructionHandle2);
    }
}

