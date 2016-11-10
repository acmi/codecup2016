/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.DelayedInitialize;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalFactory;
import com.google.inject.internal.Scoping;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.ProviderKeyBinding;
import java.util.Set;
import javax.inject.Provider;

final class LinkedProviderBindingImpl<T>
extends BindingImpl<T>
implements DelayedInitialize,
HasDependencies,
ProviderKeyBinding<T> {
    final Key<? extends Provider<? extends T>> providerKey;
    final DelayedInitialize delayedInitializer;

    private LinkedProviderBindingImpl(InjectorImpl injectorImpl, Key<T> key, Object object, InternalFactory<? extends T> internalFactory, Scoping scoping, Key<? extends Provider<? extends T>> key2, DelayedInitialize delayedInitialize) {
        super(injectorImpl, key, object, internalFactory, scoping);
        this.providerKey = key2;
        this.delayedInitializer = delayedInitialize;
    }

    public LinkedProviderBindingImpl(InjectorImpl injectorImpl, Key<T> key, Object object, InternalFactory<? extends T> internalFactory, Scoping scoping, Key<? extends Provider<? extends T>> key2) {
        this(injectorImpl, key, object, internalFactory, scoping, key2, null);
    }

    LinkedProviderBindingImpl(Object object, Key<T> key, Scoping scoping, Key<? extends Provider<? extends T>> key2) {
        super(object, key, scoping);
        this.providerKey = key2;
        this.delayedInitializer = null;
    }

    static <T> LinkedProviderBindingImpl<T> createWithInitializer(InjectorImpl injectorImpl, Key<T> key, Object object, InternalFactory<? extends T> internalFactory, Scoping scoping, Key<? extends Provider<? extends T>> key2, DelayedInitialize delayedInitialize) {
        return new LinkedProviderBindingImpl<T>(injectorImpl, key, object, internalFactory, scoping, key2, delayedInitialize);
    }

    @Override
    public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> bindingTargetVisitor) {
        return bindingTargetVisitor.visit(this);
    }

    @Override
    public Key<? extends Provider<? extends T>> getProviderKey() {
        return this.providerKey;
    }

    @Override
    public void initialize(InjectorImpl injectorImpl, Errors errors) throws ErrorsException {
        if (this.delayedInitializer != null) {
            this.delayedInitializer.initialize(injectorImpl, errors);
        }
    }

    @Override
    public Set<Dependency<?>> getDependencies() {
        return ImmutableSet.of(Dependency.get(this.providerKey));
    }

    @Override
    public BindingImpl<T> withScoping(Scoping scoping) {
        return new LinkedProviderBindingImpl(this.getSource(), this.getKey(), scoping, this.providerKey);
    }

    @Override
    public BindingImpl<T> withKey(Key<T> key) {
        return new LinkedProviderBindingImpl<T>(this.getSource(), key, this.getScoping(), this.providerKey);
    }

    @Override
    public void applyTo(Binder binder) {
        this.getScoping().applyTo(binder.withSource(this.getSource()).bind(this.getKey()).toProvider(this.getProviderKey()));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(ProviderKeyBinding.class).add("key", this.getKey()).add("source", this.getSource()).add("scope", this.getScoping()).add("provider", this.providerKey).toString();
    }

    public boolean equals(Object object) {
        if (object instanceof LinkedProviderBindingImpl) {
            LinkedProviderBindingImpl linkedProviderBindingImpl = (LinkedProviderBindingImpl)object;
            return this.getKey().equals(linkedProviderBindingImpl.getKey()) && this.getScoping().equals(linkedProviderBindingImpl.getScoping()) && Objects.equal(this.providerKey, linkedProviderBindingImpl.providerKey);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(this.getKey(), this.getScoping(), this.providerKey);
    }
}

