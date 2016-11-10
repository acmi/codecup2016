/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

interface Lookups {
    public <T> Provider<T> getProvider(Key<T> var1);

    public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> var1);
}

