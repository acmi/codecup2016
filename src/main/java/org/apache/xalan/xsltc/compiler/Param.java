/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.io.PrintStream;
import java.util.Vector;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.IFNONNULL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.Template;
import org.apache.xalan.xsltc.compiler.VariableBase;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.ObjectType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.runtime.BasisLibrary;

final class Param
extends VariableBase {
    private boolean _isInSimpleNamedTemplate = false;

    Param() {
    }

    public String toString() {
        return "param(" + this._name + ")";
    }

    public Instruction setLoadInstruction(Instruction instruction) {
        Instruction instruction2 = this._loadInstruction;
        this._loadInstruction = instruction;
        return instruction2;
    }

    public Instruction setStoreInstruction(Instruction instruction) {
        Instruction instruction2 = this._storeInstruction;
        this._storeInstruction = instruction;
        return instruction2;
    }

    public void display(int n2) {
        this.indent(n2);
        System.out.println("param " + this._name);
        if (this._select != null) {
            this.indent(n2 + 4);
            System.out.println("select " + this._select.toString());
        }
        this.displayContents(n2 + 4);
    }

    public void parseContents(Parser parser) {
        super.parseContents(parser);
        SyntaxTreeNode syntaxTreeNode = this.getParent();
        if (syntaxTreeNode instanceof Stylesheet) {
            this._isLocal = false;
            Param param = parser.getSymbolTable().lookupParam(this._name);
            if (param != null) {
                int n2;
                int n3 = this.getImportPrecedence();
                if (n3 == (n2 = param.getImportPrecedence())) {
                    String string = this._name.toString();
                    this.reportError(this, parser, "VARIABLE_REDEF_ERR", string);
                } else {
                    if (n2 > n3) {
                        this._ignore = true;
                        return;
                    }
                    param.disable();
                }
            }
            ((Stylesheet)syntaxTreeNode).addParam(this);
            parser.getSymbolTable().addParam(this);
        } else if (syntaxTreeNode instanceof Template) {
            Template template = (Template)syntaxTreeNode;
            this._isLocal = true;
            template.addParameter(this);
            if (template.isSimpleNamedTemplate()) {
                this._isInSimpleNamedTemplate = true;
            }
        }
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (this._select != null) {
            this._type = this._select.typeCheck(symbolTable);
            if (!(this._type instanceof ReferenceType) && !(this._type instanceof ObjectType)) {
                this._select = new CastExpr(this._select, Type.Reference);
            }
        } else if (this.hasContents()) {
            this.typeCheckContents(symbolTable);
        }
        this._type = Type.Reference;
        return Type.Void;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (this._ignore) {
            return;
        }
        this._ignore = true;
        String string = BasisLibrary.mapQNameToJavaName(this._name.toString());
        String string2 = this._type.toSignature();
        String string3 = this._type.getClassName();
        if (this.isLocal()) {
            if (this._isInSimpleNamedTemplate) {
                instructionList.append(this.loadInstruction());
                BranchHandle branchHandle = instructionList.append(new IFNONNULL(null));
                this.translateValue(classGenerator, methodGenerator);
                instructionList.append(this.storeInstruction());
                branchHandle.setTarget(instructionList.append(NOP));
                return;
            }
            instructionList.append(classGenerator.loadTranslet());
            instructionList.append(new PUSH(constantPoolGen, string));
            this.translateValue(classGenerator, methodGenerator);
            instructionList.append(new PUSH(constantPoolGen, true));
            instructionList.append(new INVOKEVIRTUAL(constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "addParameter", "(Ljava/lang/String;Ljava/lang/Object;Z)Ljava/lang/Object;")));
            if (string3 != "") {
                instructionList.append(new CHECKCAST(constantPoolGen.addClass(string3)));
            }
            this._type.translateUnBox(classGenerator, methodGenerator);
            if (this._refs.isEmpty()) {
                instructionList.append(this._type.POP());
                this._local = null;
            } else {
                this._local = methodGenerator.addLocalVariable2(string, this._type.toJCType(), null);
                this._local.setStart(instructionList.append(this._type.STORE(this._local.getIndex())));
            }
        } else if (classGenerator.containsField(string) == null) {
            classGenerator.addField(new Field(1, constantPoolGen.addUtf8(string), constantPoolGen.addUtf8(string2), null, constantPoolGen.getConstantPool()));
            instructionList.append(classGenerator.loadTranslet());
            instructionList.append(DUP);
            instructionList.append(new PUSH(constantPoolGen, string));
            this.translateValue(classGenerator, methodGenerator);
            instructionList.append(new PUSH(constantPoolGen, true));
            instructionList.append(new INVOKEVIRTUAL(constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "addParameter", "(Ljava/lang/String;Ljava/lang/Object;Z)Ljava/lang/Object;")));
            this._type.translateUnBox(classGenerator, methodGenerator);
            if (string3 != "") {
                instructionList.append(new CHECKCAST(constantPoolGen.addClass(string3)));
            }
            instructionList.append(new PUTFIELD(constantPoolGen.addFieldref(classGenerator.getClassName(), string, string2)));
        }
    }
}

