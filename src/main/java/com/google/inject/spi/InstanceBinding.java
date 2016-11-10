/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.Binding;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.InjectionPoint;
import java.util.Set;

public interface InstanceBinding<T>
extends Binding<T>,
HasDependencies {
    public T getInstance();

    public Set<InjectionPoint> getInjectionPoints();
}

