/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;

public class TypeCheckError
extends Exception {
    static final long serialVersionUID = 3246224233917854640L;
    ErrorMsg _error = null;
    SyntaxTreeNode _node = null;

    public TypeCheckError(SyntaxTreeNode syntaxTreeNode) {
        this._node = syntaxTreeNode;
    }

    public TypeCheckError(ErrorMsg errorMsg) {
        this._error = errorMsg;
    }

    public TypeCheckError(String string, Object object) {
        this._error = new ErrorMsg(string, object);
    }

    public TypeCheckError(String string, Object object, Object object2) {
        this._error = new ErrorMsg(string, object, object2);
    }

    public ErrorMsg getErrorMsg() {
        return this._error;
    }

    public String getMessage() {
        return this.toString();
    }

    public String toString() {
        if (this._error == null) {
            this._error = this._node != null ? new ErrorMsg("TYPE_CHECK_ERR", this._node.toString()) : new ErrorMsg("TYPE_CHECK_UNK_LOC_ERR");
        }
        return this._error.toString();
    }
}

