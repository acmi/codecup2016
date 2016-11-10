/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalFactory;
import com.google.inject.internal.Scoping;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.ExposedBinding;
import com.google.inject.spi.PrivateElements;
import java.util.Set;

public final class ExposedBindingImpl<T>
extends BindingImpl<T>
implements ExposedBinding<T> {
    private final PrivateElements privateElements;

    public ExposedBindingImpl(InjectorImpl injectorImpl, Object object, Key<T> key, InternalFactory<T> internalFactory, PrivateElements privateElements) {
        super(injectorImpl, key, object, internalFactory, Scoping.UNSCOPED);
        this.privateElements = privateElements;
    }

    @Override
    public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> bindingTargetVisitor) {
        return bindingTargetVisitor.visit(this);
    }

    @Override
    public Set<Dependency<?>> getDependencies() {
        return ImmutableSet.of(Dependency.get(Key.get(Injector.class)));
    }

    @Override
    public PrivateElements getPrivateElements() {
        return this.privateElements;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(ExposedBinding.class).add("key", this.getKey()).add("source", this.getSource()).add("privateElements", this.privateElements).toString();
    }

    @Override
    public void applyTo(Binder binder) {
        throw new UnsupportedOperationException("This element represents a synthetic binding.");
    }
}

