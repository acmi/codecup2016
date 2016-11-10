/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.proxy;

import com.google.inject.internal.cglib.core.$CodeGenerationException;

public class $UndeclaredThrowableException
extends $CodeGenerationException {
    public $UndeclaredThrowableException(Throwable throwable) {
        super(throwable);
    }

    public Throwable getUndeclaredThrowable() {
        return this.getCause();
    }
}

