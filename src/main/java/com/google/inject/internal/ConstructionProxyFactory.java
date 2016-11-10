/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.internal.ConstructionProxy;
import com.google.inject.internal.ErrorsException;

interface ConstructionProxyFactory<T> {
    public ConstructionProxy<T> create() throws ErrorsException;
}

