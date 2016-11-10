/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InternalContext;

interface ContextualCallable<T> {
    public T call(InternalContext var1) throws ErrorsException;
}

