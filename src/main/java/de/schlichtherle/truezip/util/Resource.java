/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.util;

public abstract class Resource<X extends Exception> {
    private boolean closed;

    public final void close() throws Exception {
        if (!this.closed) {
            this.onClose();
            this.closed = true;
        }
    }

    protected abstract void onClose() throws Exception;
}

