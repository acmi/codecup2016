/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.util;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.ProviderWithDependencies;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Providers {
    private Providers() {
    }

    public static <T> Provider<T> of(T t2) {
        return new ConstantProvider(t2);
    }

    public static <T> Provider<T> guicify(javax.inject.Provider<T> provider) {
        if (provider instanceof Provider) {
            return (Provider)provider;
        }
        javax.inject.Provider<T> provider2 = Preconditions.checkNotNull(provider, "provider");
        Set<InjectionPoint> set = InjectionPoint.forInstanceMethodsAndFields(provider.getClass());
        if (set.isEmpty()) {
            return new GuicifiedProvider(provider2);
        }
        HashSet hashSet = Sets.newHashSet();
        for (InjectionPoint injectionPoint : set) {
            hashSet.addAll(injectionPoint.getDependencies());
        }
        ImmutableSet immutableSet = ImmutableSet.copyOf(hashSet);
        return new GuicifiedProviderWithDependencies(immutableSet, provider2);
    }

    private static final class GuicifiedProviderWithDependencies<T>
    extends GuicifiedProvider<T>
    implements ProviderWithDependencies<T> {
        private final Set<Dependency<?>> dependencies;

        private GuicifiedProviderWithDependencies(Set<Dependency<?>> set, javax.inject.Provider<T> provider) {
            super(provider);
            this.dependencies = set;
        }

        @Inject
        void initialize(Injector injector) {
            injector.injectMembers(this.delegate);
        }

        @Override
        public Set<Dependency<?>> getDependencies() {
            return this.dependencies;
        }
    }

    private static class GuicifiedProvider<T>
    implements Provider<T> {
        protected final javax.inject.Provider<T> delegate;

        private GuicifiedProvider(javax.inject.Provider<T> provider) {
            this.delegate = provider;
        }

        @Override
        public T get() {
            return this.delegate.get();
        }

        public String toString() {
            return "guicified(" + this.delegate + ")";
        }

        public boolean equals(Object object) {
            return object instanceof GuicifiedProvider && Objects.equal(this.delegate, ((GuicifiedProvider)object).delegate);
        }

        public int hashCode() {
            return Objects.hashCode(this.delegate);
        }
    }

    private static final class ConstantProvider<T>
    implements Provider<T> {
        private final T instance;

        private ConstantProvider(T t2) {
            this.instance = t2;
        }

        @Override
        public T get() {
            return this.instance;
        }

        public String toString() {
            return "of(" + this.instance + ")";
        }

        public boolean equals(Object object) {
            return object instanceof ConstantProvider && Objects.equal(this.instance, ((ConstantProvider)object).instance);
        }

        public int hashCode() {
            return Objects.hashCode(this.instance);
        }
    }

}

