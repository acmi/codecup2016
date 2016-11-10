/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Collection;
import java.util.Vector;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ICONST;
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
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FilterParentPath;
import org.apache.xalan.xsltc.compiler.ParentLocationPath;
import org.apache.xalan.xsltc.compiler.ParentPattern;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.Predicate;
import org.apache.xalan.xsltc.compiler.RelativeLocationPath;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.UnionPathExpr;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.dtm.Axis;

final class Step
extends RelativeLocationPath {
    private int _axis;
    private Vector _predicates;
    private boolean _hadPredicates = false;
    private int _nodeType;

    public Step(int n2, int n3, Vector vector) {
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

    public int getAxis() {
        return this._axis;
    }

    public void setAxis(int n2) {
        this._axis = n2;
    }

    public int getNodeType() {
        return this._nodeType;
    }

    public Vector getPredicates() {
        return this._predicates;
    }

    public void addPredicates(Vector vector) {
        if (this._predicates == null) {
            this._predicates = vector;
        } else {
            this._predicates.addAll(vector);
        }
    }

    private boolean hasParentPattern() {
        SyntaxTreeNode syntaxTreeNode = this.getParent();
        return syntaxTreeNode instanceof ParentPattern || syntaxTreeNode instanceof ParentLocationPath || syntaxTreeNode instanceof UnionPathExpr || syntaxTreeNode instanceof FilterParentPath;
    }

    private boolean hasPredicates() {
        return this._predicates != null && this._predicates.size() > 0;
    }

    private boolean isPredicate() {
        SyntaxTreeNode syntaxTreeNode = this;
        while (syntaxTreeNode != null) {
            if (!((syntaxTreeNode = syntaxTreeNode.getParent()) instanceof Predicate)) continue;
            return true;
        }
        return false;
    }

    public boolean isAbbreviatedDot() {
        return this._nodeType == -1 && this._axis == 13;
    }

    public boolean isAbbreviatedDDot() {
        return this._nodeType == -1 && this._axis == 10;
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        this._hadPredicates = this.hasPredicates();
        this._type = this.isAbbreviatedDot() ? (this.hasParentPattern() || this.hasPredicates() ? Type.NodeSet : Type.Node) : Type.NodeSet;
        if (this._predicates != null) {
            int n2 = this._predicates.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Expression expression = (Expression)this._predicates.elementAt(i2);
                expression.typeCheck(symbolTable);
            }
        }
        return this._type;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (this.hasPredicates()) {
            this.translatePredicates(classGenerator, methodGenerator);
        } else {
            Object object;
            int n2 = 0;
            String string = null;
            XSLTC xSLTC = this.getParser().getXSLTC();
            if (this._nodeType >= 14) {
                object = xSLTC.getNamesIndex();
                string = (String)object.elementAt(this._nodeType - 14);
                n2 = string.lastIndexOf(42);
            }
            if (this._axis == 2 && this._nodeType != 2 && this._nodeType != -1 && !this.hasParentPattern() && n2 == 0) {
                int n3 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getTypedAxisIterator", "(II)Lorg/apache/xml/dtm/DTMAxisIterator;");
                instructionList.append(methodGenerator.loadDOM());
                instructionList.append(new PUSH(constantPoolGen, 2));
                instructionList.append(new PUSH(constantPoolGen, this._nodeType));
                instructionList.append(new INVOKEINTERFACE(n3, 3));
                return;
            }
            object = this.getParent();
            if (this.isAbbreviatedDot()) {
                if (this._type == Type.Node) {
                    instructionList.append(methodGenerator.loadContextNode());
                } else if (object instanceof ParentLocationPath) {
                    int n4 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.SingletonIterator", "<init>", "(I)V");
                    instructionList.append(new NEW(constantPoolGen.addClass("org.apache.xalan.xsltc.dom.SingletonIterator")));
                    instructionList.append(DUP);
                    instructionList.append(methodGenerator.loadContextNode());
                    instructionList.append(new INVOKESPECIAL(n4));
                } else {
                    int n5 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getAxisIterator", "(I)Lorg/apache/xml/dtm/DTMAxisIterator;");
                    instructionList.append(methodGenerator.loadDOM());
                    instructionList.append(new PUSH(constantPoolGen, this._axis));
                    instructionList.append(new INVOKEINTERFACE(n5, 2));
                }
                return;
            }
            if (object instanceof ParentLocationPath && object.getParent() instanceof ParentLocationPath && this._nodeType == 1 && !this._hadPredicates) {
                this._nodeType = -1;
            }
            switch (this._nodeType) {
                case 2: {
                    this._axis = 2;
                }
                case -1: {
                    int n6 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getAxisIterator", "(I)Lorg/apache/xml/dtm/DTMAxisIterator;");
                    instructionList.append(methodGenerator.loadDOM());
                    instructionList.append(new PUSH(constantPoolGen, this._axis));
                    instructionList.append(new INVOKEINTERFACE(n6, 2));
                    break;
                }
                default: {
                    if (n2 > 1) {
                        String string2 = this._axis == 2 ? string.substring(0, n2 - 2) : string.substring(0, n2 - 1);
                        int n7 = xSLTC.registerNamespace(string2);
                        int n8 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNamespaceAxisIterator", "(II)Lorg/apache/xml/dtm/DTMAxisIterator;");
                        instructionList.append(methodGenerator.loadDOM());
                        instructionList.append(new PUSH(constantPoolGen, this._axis));
                        instructionList.append(new PUSH(constantPoolGen, n7));
                        instructionList.append(new INVOKEINTERFACE(n8, 3));
                        break;
                    }
                }
                case 1: {
                    int n9 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getTypedAxisIterator", "(II)Lorg/apache/xml/dtm/DTMAxisIterator;");
                    instructionList.append(methodGenerator.loadDOM());
                    instructionList.append(new PUSH(constantPoolGen, this._axis));
                    instructionList.append(new PUSH(constantPoolGen, this._nodeType));
                    instructionList.append(new INVOKEINTERFACE(n9, 3));
                }
            }
        }
    }

    public void translatePredicates(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n2 = 0;
        if (this._predicates.size() == 0) {
            this.translate(classGenerator, methodGenerator);
        } else {
            Predicate predicate = (Predicate)this._predicates.lastElement();
            this._predicates.remove(predicate);
            if (predicate.isNodeValueTest()) {
                Step step = predicate.getStep();
                instructionList.append(methodGenerator.loadDOM());
                if (step.isAbbreviatedDot()) {
                    this.translate(classGenerator, methodGenerator);
                    instructionList.append(new ICONST(0));
                } else {
                    ParentLocationPath parentLocationPath = new ParentLocationPath(this, step);
                    try {
                        parentLocationPath.typeCheck(this.getParser().getSymbolTable());
                    }
                    catch (TypeCheckError typeCheckError) {
                        // empty catch block
                    }
                    parentLocationPath.translate(classGenerator, methodGenerator);
                    instructionList.append(new ICONST(1));
                }
                predicate.translate(classGenerator, methodGenerator);
                n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNodeValueIterator", "(Lorg/apache/xml/dtm/DTMAxisIterator;ILjava/lang/String;Z)Lorg/apache/xml/dtm/DTMAxisIterator;");
                instructionList.append(new INVOKEINTERFACE(n2, 5));
            } else if (predicate.isNthDescendant()) {
                instructionList.append(methodGenerator.loadDOM());
                instructionList.append(new ICONST(predicate.getPosType()));
                predicate.translate(classGenerator, methodGenerator);
                instructionList.append(new ICONST(0));
                n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNthDescendant", "(IIZ)Lorg/apache/xml/dtm/DTMAxisIterator;");
                instructionList.append(new INVOKEINTERFACE(n2, 4));
            } else if (predicate.isNthPositionFilter()) {
                n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.NthIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;I)V");
                this.translatePredicates(classGenerator, methodGenerator);
                LocalVariableGen localVariableGen = methodGenerator.addLocalVariable("step_tmp1", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
                localVariableGen.setStart(instructionList.append(new ASTORE(localVariableGen.getIndex())));
                predicate.translate(classGenerator, methodGenerator);
                LocalVariableGen localVariableGen2 = methodGenerator.addLocalVariable("step_tmp2", Util.getJCRefType("I"), null, null);
                localVariableGen2.setStart(instructionList.append(new ISTORE(localVariableGen2.getIndex())));
                instructionList.append(new NEW(constantPoolGen.addClass("org.apache.xalan.xsltc.dom.NthIterator")));
                instructionList.append(DUP);
                localVariableGen.setEnd(instructionList.append(new ALOAD(localVariableGen.getIndex())));
                localVariableGen2.setEnd(instructionList.append(new ILOAD(localVariableGen2.getIndex())));
                instructionList.append(new INVOKESPECIAL(n2));
            } else {
                n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.dom.CurrentNodeListIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;Lorg/apache/xalan/xsltc/dom/CurrentNodeListFilter;ILorg/apache/xalan/xsltc/runtime/AbstractTranslet;)V");
                this.translatePredicates(classGenerator, methodGenerator);
                LocalVariableGen localVariableGen = methodGenerator.addLocalVariable("step_tmp1", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
                localVariableGen.setStart(instructionList.append(new ASTORE(localVariableGen.getIndex())));
                predicate.translateFilter(classGenerator, methodGenerator);
                LocalVariableGen localVariableGen3 = methodGenerator.addLocalVariable("step_tmp2", Util.getJCRefType("Lorg/apache/xalan/xsltc/dom/CurrentNodeListFilter;"), null, null);
                localVariableGen3.setStart(instructionList.append(new ASTORE(localVariableGen3.getIndex())));
                instructionList.append(new NEW(constantPoolGen.addClass("org.apache.xalan.xsltc.dom.CurrentNodeListIterator")));
                instructionList.append(DUP);
                localVariableGen.setEnd(instructionList.append(new ALOAD(localVariableGen.getIndex())));
                localVariableGen3.setEnd(instructionList.append(new ALOAD(localVariableGen3.getIndex())));
                instructionList.append(methodGenerator.loadCurrentNode());
                instructionList.append(classGenerator.loadTranslet());
                if (classGenerator.isExternal()) {
                    String string = classGenerator.getClassName();
                    instructionList.append(new CHECKCAST(constantPoolGen.addClass(string)));
                }
                instructionList.append(new INVOKESPECIAL(n2));
            }
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("step(\"");
        stringBuffer.append(Axis.getNames(this._axis)).append("\", ").append(this._nodeType);
        if (this._predicates != null) {
            int n2 = this._predicates.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Predicate predicate = (Predicate)this._predicates.elementAt(i2);
                stringBuffer.append(", ").append(predicate.toString());
            }
        }
        return stringBuffer.append(')').toString();
    }
}

