/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FunctionCall;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class ConcatCall
extends FunctionCall {
    public ConcatCall(QName qName, Vector vector) {
        super(qName, vector);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        for (int i2 = 0; i2 < this.argumentCount(); ++i2) {
            Expression expression = this.argument(i2);
            if (expression.typeCheck(symbolTable).identicalTo(Type.String)) continue;
            this.setArgument(i2, new CastExpr(expression, Type.String));
        }
        this._type = Type.String;
        return this._type;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n2 = this.argumentCount();
        switch (n2) {
            case 0: {
                instructionList.append(new PUSH(constantPoolGen, ""));
                break;
            }
            case 1: {
                this.argument().translate(classGenerator, methodGenerator);
                break;
            }
            default: {
                int n3 = constantPoolGen.addMethodref("java.lang.StringBuffer", "<init>", "()V");
                INVOKEVIRTUAL iNVOKEVIRTUAL = new INVOKEVIRTUAL(constantPoolGen.addMethodref("java.lang.StringBuffer", "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;"));
                int n4 = constantPoolGen.addMethodref("java.lang.StringBuffer", "toString", "()Ljava/lang/String;");
                instructionList.append(new NEW(constantPoolGen.addClass("java.lang.StringBuffer")));
                instructionList.append(DUP);
                instructionList.append(new INVOKESPECIAL(n3));
                for (int i2 = 0; i2 < n2; ++i2) {
                    this.argument(i2).translate(classGenerator, methodGenerator);
                    instructionList.append(iNVOKEVIRTUAL);
                }
                instructionList.append(new INVOKEVIRTUAL(n4));
            }
        }
    }
}

