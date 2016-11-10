/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InternalContext;
import com.google.inject.spi.Dependency;

interface InternalFactory<T> {
    public T get(Errors var1, InternalContext var2, Dependency<?> var3, boolean var4) throws ErrorsException;
}

