/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.Param;
import org.apache.xalan.xsltc.compiler.ParameterRef;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.Variable;
import org.apache.xalan.xsltc.compiler.VariableBase;
import org.apache.xalan.xsltc.compiler.VariableRef;
import org.apache.xalan.xsltc.compiler.VariableRefBase;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class UnresolvedRef
extends VariableRefBase {
    private QName _variableName = null;
    private VariableRefBase _ref = null;

    public UnresolvedRef(QName qName) {
        this._variableName = qName;
    }

    public QName getName() {
        return this._variableName;
    }

    private ErrorMsg reportError() {
        ErrorMsg errorMsg = new ErrorMsg("VARIABLE_UNDEF_ERR", (Object)this._variableName, this);
        this.getParser().reportError(3, errorMsg);
        return errorMsg;
    }

    private VariableRefBase resolve(Parser parser, SymbolTable symbolTable) {
        VariableBase variableBase = parser.lookupVariable(this._variableName);
        if (variableBase == null) {
            variableBase = (VariableBase)symbolTable.lookupName(this._variableName);
        }
        if (variableBase == null) {
            this.reportError();
            return null;
        }
        this._variable = variableBase;
        this.addParentDependency();
        if (variableBase instanceof Variable) {
            return new VariableRef((Variable)variableBase);
        }
        if (variableBase instanceof Param) {
            return new ParameterRef((Param)variableBase);
        }
        return null;
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (this._ref != null) {
            String string = this._variableName.toString();
            ErrorMsg errorMsg = new ErrorMsg("CIRCULAR_VARIABLE_ERR", (Object)string, this);
        }
        if ((this._ref = this.resolve(this.getParser(), symbolTable)) != null) {
            this._type = this._ref.typeCheck(symbolTable);
            return this._type;
        }
        throw new TypeCheckError(this.reportError());
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        if (this._ref != null) {
            this._ref.translate(classGenerator, methodGenerator);
        } else {
            this.reportError();
        }
    }

    public String toString() {
        return "unresolved-ref()";
    }
}

