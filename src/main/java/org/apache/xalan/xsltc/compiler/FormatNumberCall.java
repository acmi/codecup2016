/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FunctionCall;
import org.apache.xalan.xsltc.compiler.LiteralExpr;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.RealType;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class FormatNumberCall
extends FunctionCall {
    private Expression _value;
    private Expression _format;
    private Expression _name;
    private QName _resolvedQName = null;

    public FormatNumberCall(QName qName, Vector vector) {
        super(qName, vector);
        this._value = this.argument(0);
        this._format = this.argument(1);
        this._name = this.argumentCount() == 3 ? this.argument(2) : null;
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        Type type;
        this.getStylesheet().numberFormattingUsed();
        Type type2 = this._value.typeCheck(symbolTable);
        if (!(type2 instanceof RealType)) {
            this._value = new CastExpr(this._value, Type.Real);
        }
        if (!((type = this._format.typeCheck(symbolTable)) instanceof StringType)) {
            this._format = new CastExpr(this._format, Type.String);
        }
        if (this.argumentCount() == 3) {
            Type type3 = this._name.typeCheck(symbolTable);
            if (this._name instanceof LiteralExpr) {
                LiteralExpr literalExpr = (LiteralExpr)this._name;
                this._resolvedQName = this.getParser().getQNameIgnoreDefaultNs(literalExpr.getValue());
            } else if (!(type3 instanceof StringType)) {
                this._name = new CastExpr(this._name, Type.String);
            }
        }
        this._type = Type.String;
        return this._type;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        this._value.translate(classGenerator, methodGenerator);
        this._format.translate(classGenerator, methodGenerator);
        int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "formatNumber", "(DLjava/lang/String;Ljava/text/DecimalFormat;)Ljava/lang/String;");
        int n3 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "getDecimalFormat", "(Ljava/lang/String;)Ljava/text/DecimalFormat;");
        instructionList.append(classGenerator.loadTranslet());
        if (this._name == null) {
            instructionList.append(new PUSH(constantPoolGen, ""));
        } else if (this._resolvedQName != null) {
            instructionList.append(new PUSH(constantPoolGen, this._resolvedQName.toString()));
        } else {
            this._name.translate(classGenerator, methodGenerator);
        }
        instructionList.append(new INVOKEVIRTUAL(n3));
        instructionList.append(new INVOKESTATIC(n2));
    }
}

