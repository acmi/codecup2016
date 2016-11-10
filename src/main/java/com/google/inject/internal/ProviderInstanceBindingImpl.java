/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalFactory;
import com.google.inject.internal.Scoping;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.ProviderInstanceBinding;
import com.google.inject.spi.ProviderWithExtensionVisitor;
import com.google.inject.util.Providers;
import java.util.Set;

class ProviderInstanceBindingImpl<T>
extends BindingImpl<T>
implements ProviderInstanceBinding<T> {
    final javax.inject.Provider<? extends T> providerInstance;
    final ImmutableSet<InjectionPoint> injectionPoints;

    public ProviderInstanceBindingImpl(InjectorImpl injectorImpl, Key<T> key, Object object, InternalFactory<? extends T> internalFactory, Scoping scoping, javax.inject.Provider<? extends T> provider, Set<InjectionPoint> set) {
        super(injectorImpl, key, object, internalFactory, scoping);
        this.providerInstance = provider;
        this.injectionPoints = ImmutableSet.copyOf(set);
    }

    public ProviderInstanceBindingImpl(Object object, Key<T> key, Scoping scoping, Set<InjectionPoint> set, javax.inject.Provider<? extends T> provider) {
        super(object, key, scoping);
        this.injectionPoints = ImmutableSet.copyOf(set);
        this.providerInstance = provider;
    }

    @Override
    public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> bindingTargetVisitor) {
        if (this.providerInstance instanceof ProviderWithExtensionVisitor) {
            return ((ProviderWithExtensionVisitor)this.providerInstance).acceptExtensionVisitor(bindingTargetVisitor, this);
        }
        return bindingTargetVisitor.visit(this);
    }

    @Override
    public Provider<? extends T> getProviderInstance() {
        return Providers.guicify(this.providerInstance);
    }

    @Override
    public javax.inject.Provider<? extends T> getUserSuppliedProvider() {
        return this.providerInstance;
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return this.injectionPoints;
    }

    @Override
    public Set<Dependency<?>> getDependencies() {
        return this.providerInstance instanceof HasDependencies ? ImmutableSet.copyOf(((HasDependencies)((Object)this.providerInstance)).getDependencies()) : Dependency.forInjectionPoints(this.injectionPoints);
    }

    @Override
    public BindingImpl<T> withScoping(Scoping scoping) {
        return new ProviderInstanceBindingImpl<T>(this.getSource(), this.getKey(), scoping, this.injectionPoints, this.providerInstance);
    }

    @Override
    public BindingImpl<T> withKey(Key<T> key) {
        return new ProviderInstanceBindingImpl<T>(this.getSource(), key, this.getScoping(), this.injectionPoints, this.providerInstance);
    }

    @Override
    public void applyTo(Binder binder) {
        this.getScoping().applyTo(binder.withSource(this.getSource()).bind(this.getKey()).toProvider(this.getUserSuppliedProvider()));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(ProviderInstanceBinding.class).add("key", this.getKey()).add("source", this.getSource()).add("scope", this.getScoping()).add("provider", this.providerInstance).toString();
    }

    public boolean equals(Object object) {
        if (object instanceof ProviderInstanceBindingImpl) {
            ProviderInstanceBindingImpl providerInstanceBindingImpl = (ProviderInstanceBindingImpl)object;
            return this.getKey().equals(providerInstanceBindingImpl.getKey()) && this.getScoping().equals(providerInstanceBindingImpl.getScoping()) && Objects.equal(this.providerInstance, providerInstanceBindingImpl.providerInstance);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(this.getKey(), this.getScoping());
    }
}

