/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.Expression;

final class ArgumentList {
    private final Expression _arg;
    private final ArgumentList _rest;

    public ArgumentList(Expression expression, ArgumentList argumentList) {
        this._arg = expression;
        this._rest = argumentList;
    }

    public String toString() {
        return this._rest == null ? this._arg.toString() : this._arg.toString() + ", " + this._rest.toString();
    }
}

