/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.Binding;
import com.google.inject.spi.DependencyAndSource;
import java.util.List;

public interface ProvisionListener {
    public <T> void onProvision(ProvisionInvocation<T> var1);

    public static abstract class ProvisionInvocation<T> {
        public abstract Binding<T> getBinding();

        public abstract T provision();

        public abstract List<DependencyAndSource> getDependencyChain();
    }

}

