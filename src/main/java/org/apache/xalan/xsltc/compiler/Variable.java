/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.generic.ACONST_NULL;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.DCONST;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.VariableBase;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.IntType;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.RealType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class Variable
extends VariableBase {
    Variable() {
    }

    public int getIndex() {
        return this._local != null ? this._local.getIndex() : -1;
    }

    public void parseContents(Parser parser) {
        super.parseContents(parser);
        SyntaxTreeNode syntaxTreeNode = this.getParent();
        if (syntaxTreeNode instanceof Stylesheet) {
            this._isLocal = false;
            Variable variable = parser.getSymbolTable().lookupVariable(this._name);
            if (variable != null) {
                int n2;
                int n3 = this.getImportPrecedence();
                if (n3 == (n2 = variable.getImportPrecedence())) {
                    String string = this._name.toString();
                    this.reportError(this, parser, "VARIABLE_REDEF_ERR", string);
                } else {
                    if (n2 > n3) {
                        this._ignore = true;
                        return;
                    }
                    variable.disable();
                }
            }
            ((Stylesheet)syntaxTreeNode).addVariable(this);
            parser.getSymbolTable().addVariable(this);
        } else {
            this._isLocal = true;
        }
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (this._select != null) {
            this._type = this._select.typeCheck(symbolTable);
        } else if (this.hasContents()) {
            this.typeCheckContents(symbolTable);
            this._type = Type.ResultTree;
        } else {
            this._type = Type.Reference;
        }
        return Type.Void;
    }

    public void initialize(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (this.isLocal() && !this._refs.isEmpty()) {
            if (this._local == null) {
                this._local = methodGenerator.addLocalVariable2(this.getEscapedName(), this._type.toJCType(), null);
            }
            if (this._type instanceof IntType || this._type instanceof NodeType || this._type instanceof BooleanType) {
                instructionList.append(new ICONST(0));
            } else if (this._type instanceof RealType) {
                instructionList.append(new DCONST(0.0));
            } else {
                instructionList.append(new ACONST_NULL());
            }
            this._local.setStart(instructionList.append(this._type.STORE(this._local.getIndex())));
        }
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        if (this._refs.isEmpty()) {
            this._ignore = true;
        }
        if (this._ignore) {
            return;
        }
        this._ignore = true;
        String string = this.getEscapedName();
        if (this.isLocal()) {
            boolean bl;
            this.translateValue(classGenerator, methodGenerator);
            boolean bl2 = bl = this._local == null;
            if (bl) {
                this.mapRegister(methodGenerator);
            }
            InstructionHandle instructionHandle = instructionList.append(this._type.STORE(this._local.getIndex()));
            if (bl) {
                this._local.setStart(instructionHandle);
            }
        } else {
            String string2 = this._type.toSignature();
            if (classGenerator.containsField(string) == null) {
                classGenerator.addField(new Field(1, constantPoolGen.addUtf8(string), constantPoolGen.addUtf8(string2), null, constantPoolGen.getConstantPool()));
                instructionList.append(classGenerator.loadTranslet());
                this.translateValue(classGenerator, methodGenerator);
                instructionList.append(new PUTFIELD(constantPoolGen.addFieldref(classGenerator.getClassName(), string, string2)));
            }
        }
    }
}

