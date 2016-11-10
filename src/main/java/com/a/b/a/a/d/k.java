/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.d;

import java.io.IOException;

class k {
    static void a(Throwable throwable) throws IOException {
        if (throwable == null) {
            return;
        }
        if (throwable instanceof IOException) {
            throw (IOException)throwable;
        }
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException)throwable;
        }
        if (throwable instanceof Error) {
            throw (Error)throwable;
        }
        throw new IllegalStateException("Got unexpected async. throwable.", throwable);
    }
}

