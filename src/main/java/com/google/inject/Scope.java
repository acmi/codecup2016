/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject;

import com.google.inject.Key;
import com.google.inject.Provider;

public interface Scope {
    public <T> Provider<T> scope(Key<T> var1, Provider<T> var2);

    public String toString();
}

