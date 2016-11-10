/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;

interface Initializable<T> {
    public T get(Errors var1) throws ErrorsException;
}

