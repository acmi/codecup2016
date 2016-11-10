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
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalFactory;
import com.google.inject.internal.Scoping;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.LinkedKeyBinding;
import java.util.Set;

public final class LinkedBindingImpl<T>
extends BindingImpl<T>
implements HasDependencies,
LinkedKeyBinding<T> {
    final Key<? extends T> targetKey;

    public LinkedBindingImpl(InjectorImpl injectorImpl, Key<T> key, Object object, InternalFactory<? extends T> internalFactory, Scoping scoping, Key<? extends T> key2) {
        super(injectorImpl, key, object, internalFactory, scoping);
        this.targetKey = key2;
    }

    public LinkedBindingImpl(Object object, Key<T> key, Scoping scoping, Key<? extends T> key2) {
        super(object, key, scoping);
        this.targetKey = key2;
    }

    @Override
    public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> bindingTargetVisitor) {
        return bindingTargetVisitor.visit(this);
    }

    @Override
    public Key<? extends T> getLinkedKey() {
        return this.targetKey;
    }

    @Override
    public Set<Dependency<?>> getDependencies() {
        return ImmutableSet.of(Dependency.get(this.targetKey));
    }

    @Override
    public BindingImpl<T> withScoping(Scoping scoping) {
        return new LinkedBindingImpl<T>(this.getSource(), this.getKey(), scoping, this.targetKey);
    }

    @Override
    public BindingImpl<T> withKey(Key<T> key) {
        return new LinkedBindingImpl<T>(this.getSource(), key, this.getScoping(), this.targetKey);
    }

    @Override
    public void applyTo(Binder binder) {
        this.getScoping().applyTo(binder.withSource(this.getSource()).bind(this.getKey()).to(this.getLinkedKey()));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(LinkedKeyBinding.class).add("key", this.getKey()).add("source", this.getSource()).add("scope", this.getScoping()).add("target", this.targetKey).toString();
    }

    public boolean equals(Object object) {
        if (object instanceof LinkedBindingImpl) {
            LinkedBindingImpl linkedBindingImpl = (LinkedBindingImpl)object;
            return this.getKey().equals(linkedBindingImpl.getKey()) && this.getScoping().equals(linkedBindingImpl.getScoping()) && Objects.equal(this.targetKey, linkedBindingImpl.targetKey);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(this.getKey(), this.getScoping(), this.targetKey);
    }
}

