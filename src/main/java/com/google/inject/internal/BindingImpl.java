/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.MoreObjects;
import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalFactory;
import com.google.inject.internal.Scoping;
import com.google.inject.spi.BindingScopingVisitor;
import com.google.inject.spi.ElementVisitor;
import com.google.inject.spi.InstanceBinding;

public abstract class BindingImpl<T>
implements Binding<T> {
    private final InjectorImpl injector;
    private final Key<T> key;
    private final Object source;
    private final Scoping scoping;
    private final InternalFactory<? extends T> internalFactory;
    private volatile Provider<T> provider;

    public BindingImpl(InjectorImpl injectorImpl, Key<T> key, Object object, InternalFactory<? extends T> internalFactory, Scoping scoping) {
        this.injector = injectorImpl;
        this.key = key;
        this.source = object;
        this.internalFactory = internalFactory;
        this.scoping = scoping;
    }

    protected BindingImpl(Object object, Key<T> key, Scoping scoping) {
        this.internalFactory = null;
        this.injector = null;
        this.source = object;
        this.key = key;
        this.scoping = scoping;
    }

    @Override
    public Key<T> getKey() {
        return this.key;
    }

    @Override
    public Object getSource() {
        return this.source;
    }

    @Override
    public Provider<T> getProvider() {
        if (this.provider == null) {
            if (this.injector == null) {
                throw new UnsupportedOperationException("getProvider() not supported for module bindings");
            }
            this.provider = this.injector.getProvider(this.key);
        }
        return this.provider;
    }

    public InternalFactory<? extends T> getInternalFactory() {
        return this.internalFactory;
    }

    public Scoping getScoping() {
        return this.scoping;
    }

    public boolean isConstant() {
        return this instanceof InstanceBinding;
    }

    public <V> V acceptVisitor(ElementVisitor<V> elementVisitor) {
        return elementVisitor.visit(this);
    }

    @Override
    public <V> V acceptScopingVisitor(BindingScopingVisitor<V> bindingScopingVisitor) {
        return this.scoping.acceptVisitor(bindingScopingVisitor);
    }

    protected BindingImpl<T> withScoping(Scoping scoping) {
        throw new AssertionError();
    }

    protected BindingImpl<T> withKey(Key<T> key) {
        throw new AssertionError();
    }

    public String toString() {
        return MoreObjects.toStringHelper(Binding.class).add("key", this.key).add("scope", this.scoping).add("source", this.source).toString();
    }

    public InjectorImpl getInjector() {
        return this.injector;
    }
}

