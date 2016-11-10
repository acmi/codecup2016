/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.Closure;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.Param;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.TopLevelElement;
import org.apache.xalan.xsltc.compiler.Variable;
import org.apache.xalan.xsltc.compiler.VariableBase;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

class VariableRefBase
extends Expression {
    protected VariableBase _variable;
    protected Closure _closure = null;

    public VariableRefBase(VariableBase variableBase) {
        this._variable = variableBase;
        variableBase.addReference(this);
    }

    public VariableRefBase() {
        this._variable = null;
    }

    public VariableBase getVariable() {
        return this._variable;
    }

    public void addParentDependency() {
        SyntaxTreeNode syntaxTreeNode;
        for (syntaxTreeNode = this; syntaxTreeNode != null && !(syntaxTreeNode instanceof TopLevelElement); syntaxTreeNode = syntaxTreeNode.getParent()) {
        }
        TopLevelElement topLevelElement = (TopLevelElement)syntaxTreeNode;
        if (topLevelElement != null) {
            VariableBase variableBase = this._variable;
            if (this._variable._ignore) {
                if (this._variable instanceof Variable) {
                    variableBase = topLevelElement.getSymbolTable().lookupVariable(this._variable._name);
                } else if (this._variable instanceof Param) {
                    variableBase = topLevelElement.getSymbolTable().lookupParam(this._variable._name);
                }
            }
            topLevelElement.addDependency(variableBase);
        }
    }

    public boolean equals(Object object) {
        try {
            return this._variable == ((VariableRefBase)object)._variable;
        }
        catch (ClassCastException classCastException) {
            return false;
        }
    }

    public String toString() {
        return "variable-ref(" + this._variable.getName() + '/' + this._variable.getType() + ')';
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (this._type != null) {
            return this._type;
        }
        if (this._variable.isLocal()) {
            SyntaxTreeNode syntaxTreeNode = this.getParent();
            do {
                if (!(syntaxTreeNode instanceof Closure)) continue;
                this._closure = (Closure)((Object)syntaxTreeNode);
                break;
            } while (!(syntaxTreeNode instanceof TopLevelElement) && (syntaxTreeNode = syntaxTreeNode.getParent()) != null);
            if (this._closure != null) {
                this._closure.addVariable(this);
            }
        }
        this._type = this._variable.getType();
        if (this._type == null) {
            this._variable.typeCheck(symbolTable);
            this._type = this._variable.getType();
        }
        this.addParentDependency();
        return this._type;
    }
}

