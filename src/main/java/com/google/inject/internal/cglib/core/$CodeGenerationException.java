/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

public class $CodeGenerationException
extends RuntimeException {
    private Throwable cause;

    public $CodeGenerationException(Throwable throwable) {
        String string = String.valueOf(throwable.getClass().getName());
        String string2 = String.valueOf(throwable.getMessage());
        super(new StringBuilder(3 + String.valueOf(string).length() + String.valueOf(string2).length()).append(string).append("-->").append(string2).toString());
        this.cause = throwable;
    }

    public Throwable getCause() {
        return this.cause;
    }
}

