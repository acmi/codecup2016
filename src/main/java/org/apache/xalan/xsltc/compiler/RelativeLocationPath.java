/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.Expression;

abstract class RelativeLocationPath
extends Expression {
    RelativeLocationPath() {
    }

    public abstract int getAxis();

    public abstract void setAxis(int var1);
}

