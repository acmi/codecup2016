/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.Provider;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.ProviderInstanceBinding;

public interface ProviderWithExtensionVisitor<T>
extends Provider<T> {
    public <B, V> V acceptExtensionVisitor(BindingTargetVisitor<B, V> var1, ProviderInstanceBinding<? extends B> var2);
}

