/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.spi.Dependency;
import java.util.Set;

public interface HasDependencies {
    public Set<Dependency<?>> getDependencies();
}

