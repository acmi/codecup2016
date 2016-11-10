/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject;

public final class OutOfScopeException
extends RuntimeException {
    public OutOfScopeException(String string) {
        super(string);
    }

    public OutOfScopeException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public OutOfScopeException(Throwable throwable) {
        super(throwable);
    }
}

