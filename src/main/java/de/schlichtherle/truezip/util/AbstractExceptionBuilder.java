/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.util;

public abstract class AbstractExceptionBuilder<C extends Exception, X extends Exception> {
    private X assembly;

    protected abstract X update(C var1, X var2);

    protected X post(X x2) {
        return x2;
    }

    public final void warn(C c2) {
        if (null == c2) {
            throw new NullPointerException();
        }
        this.assembly = this.update(c2, this.assembly);
    }

    public final void check() throws Exception {
        X x2 = this.assembly;
        if (null != x2) {
            this.assembly = null;
            throw this.post(x2);
        }
    }
}

