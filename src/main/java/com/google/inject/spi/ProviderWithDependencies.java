/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.Provider;
import com.google.inject.spi.HasDependencies;

public interface ProviderWithDependencies<T>
extends Provider<T>,
HasDependencies {
}

