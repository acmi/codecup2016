/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.util.concurrent;

public class ExecutionError
extends Error {
    protected ExecutionError() {
    }

    public ExecutionError(Error error) {
        super(error);
    }
}

