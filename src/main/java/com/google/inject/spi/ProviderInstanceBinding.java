/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.Binding;
import com.google.inject.Provider;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.InjectionPoint;
import java.util.Set;

public interface ProviderInstanceBinding<T>
extends Binding<T>,
HasDependencies {
    @Deprecated
    public Provider<? extends T> getProviderInstance();

    public javax.inject.Provider<? extends T> getUserSuppliedProvider();

    public Set<InjectionPoint> getInjectionPoints();
}

