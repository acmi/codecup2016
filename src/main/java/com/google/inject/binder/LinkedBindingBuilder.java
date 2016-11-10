/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.binder;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.ScopedBindingBuilder;
import java.lang.reflect.Constructor;

public interface LinkedBindingBuilder<T>
extends ScopedBindingBuilder {
    public ScopedBindingBuilder to(Class<? extends T> var1);

    public ScopedBindingBuilder to(TypeLiteral<? extends T> var1);

    public ScopedBindingBuilder to(Key<? extends T> var1);

    public void toInstance(T var1);

    public ScopedBindingBuilder toProvider(Provider<? extends T> var1);

    public ScopedBindingBuilder toProvider(javax.inject.Provider<? extends T> var1);

    public ScopedBindingBuilder toProvider(Class<? extends javax.inject.Provider<? extends T>> var1);

    public ScopedBindingBuilder toProvider(TypeLiteral<? extends javax.inject.Provider<? extends T>> var1);

    public ScopedBindingBuilder toProvider(Key<? extends javax.inject.Provider<? extends T>> var1);

    public <S extends T> ScopedBindingBuilder toConstructor(Constructor<S> var1);

    public <S extends T> ScopedBindingBuilder toConstructor(Constructor<S> var1, TypeLiteral<? extends S> var2);
}

