/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FunctionCall;
import org.apache.xalan.xsltc.compiler.LiteralExpr;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class ElementAvailableCall
extends FunctionCall {
    public ElementAvailableCall(QName qName, Vector vector) {
        super(qName, vector);
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (this.argument() instanceof LiteralExpr) {
            this._type = Type.Boolean;
            return this._type;
        }
        ErrorMsg errorMsg = new ErrorMsg("NEED_LITERAL_ERR", (Object)"element-available", this);
        throw new TypeCheckError(errorMsg);
    }

    public Object evaluateAtCompileTime() {
        return this.getResult() ? Boolean.TRUE : Boolean.FALSE;
    }

    public boolean getResult() {
        try {
            LiteralExpr literalExpr = (LiteralExpr)this.argument();
            String string = literalExpr.getValue();
            int n2 = string.indexOf(58);
            String string2 = n2 > 0 ? string.substring(n2 + 1) : string;
            return this.getParser().elementSupported(literalExpr.getNamespace(), string2);
        }
        catch (ClassCastException classCastException) {
            return false;
        }
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        boolean bl = this.getResult();
        methodGenerator.getInstructionList().append(new PUSH(constantPoolGen, bl));
    }
}

