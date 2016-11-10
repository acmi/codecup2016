/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.TypeConverterBinding;
import java.util.Set;

public interface ConvertedConstantBinding<T>
extends Binding<T>,
HasDependencies {
    public T getValue();

    public TypeConverterBinding getTypeConverterBinding();

    public Key<String> getSourceKey();

    @Override
    public Set<Dependency<?>> getDependencies();
}

