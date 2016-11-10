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
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalFactory;
import com.google.inject.internal.Scoping;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.InstanceBinding;
import com.google.inject.util.Providers;
import java.util.Set;

final class InstanceBindingImpl<T>
extends BindingImpl<T>
implements InstanceBinding<T> {
    final T instance;
    final Provider<T> provider;
    final ImmutableSet<InjectionPoint> injectionPoints;

    public InstanceBindingImpl(InjectorImpl injectorImpl, Key<T> key, Object object, InternalFactory<? extends T> internalFactory, Set<InjectionPoint> set, T t2) {
        super(injectorImpl, key, object, internalFactory, Scoping.EAGER_SINGLETON);
        this.injectionPoints = ImmutableSet.copyOf(set);
        this.instance = t2;
        this.provider = Providers.of(t2);
    }

    public InstanceBindingImpl(Object object, Key<T> key, Scoping scoping, Set<InjectionPoint> set, T t2) {
        super(object, key, scoping);
        this.injectionPoints = ImmutableSet.copyOf(set);
        this.instance = t2;
        this.provider = Providers.of(t2);
    }

    @Override
    public Provider<T> getProvider() {
        return this.provider;
    }

    @Override
    public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> bindingTargetVisitor) {
        return bindingTargetVisitor.visit(this);
    }

    @Override
    public T getInstance() {
        return this.instance;
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return this.injectionPoints;
    }

    @Override
    public Set<Dependency<?>> getDependencies() {
        return this.instance instanceof HasDependencies ? ImmutableSet.copyOf(((HasDependencies)this.instance).getDependencies()) : Dependency.forInjectionPoints(this.injectionPoints);
    }

    @Override
    public BindingImpl<T> withScoping(Scoping scoping) {
        return new InstanceBindingImpl(this.getSource(), this.getKey(), scoping, this.injectionPoints, this.instance);
    }

    @Override
    public BindingImpl<T> withKey(Key<T> key) {
        return new InstanceBindingImpl<T>(this.getSource(), key, this.getScoping(), this.injectionPoints, this.instance);
    }

    @Override
    public void applyTo(Binder binder) {
        binder.withSource(this.getSource()).bind(this.getKey()).toInstance(this.instance);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(InstanceBinding.class).add("key", this.getKey()).add("source", this.getSource()).add("instance", this.instance).toString();
    }

    public boolean equals(Object object) {
        if (object instanceof InstanceBindingImpl) {
            InstanceBindingImpl instanceBindingImpl = (InstanceBindingImpl)object;
            return this.getKey().equals(instanceBindingImpl.getKey()) && this.getScoping().equals(instanceBindingImpl.getScoping()) && Objects.equal(this.instance, instanceBindingImpl.instance);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(this.getKey(), this.getScoping());
    }
}

