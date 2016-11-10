/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.ProvidesMethodBinding;

public interface ProvidesMethodTargetVisitor<T, V>
extends BindingTargetVisitor<T, V> {
    @Override
    public V visit(ProvidesMethodBinding<? extends T> var1);
}

