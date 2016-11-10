/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

public final class CompilerException
extends Exception {
    static final long serialVersionUID = 1732939618562742663L;
    private String _msg;

    public CompilerException() {
    }

    public CompilerException(Exception exception) {
        super(exception.toString());
        this._msg = exception.toString();
    }

    public CompilerException(String string) {
        super(string);
        this._msg = string;
    }

    public String getMessage() {
        int n2 = this._msg.indexOf(58);
        if (n2 > -1) {
            return this._msg.substring(n2);
        }
        return this._msg;
    }
}

