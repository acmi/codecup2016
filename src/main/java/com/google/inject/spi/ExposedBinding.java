/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.PrivateElements;

public interface ExposedBinding<T>
extends Binding<T>,
HasDependencies {
    public PrivateElements getPrivateElements();

    @Override
    public void applyTo(Binder var1);
}

