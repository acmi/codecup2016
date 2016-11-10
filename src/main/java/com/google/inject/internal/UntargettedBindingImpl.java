/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.Errors;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.InternalFactory;
import com.google.inject.internal.Scoping;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.UntargettedBinding;

final class UntargettedBindingImpl<T>
extends BindingImpl<T>
implements UntargettedBinding<T> {
    UntargettedBindingImpl(InjectorImpl injectorImpl, Key<T> key, Object object) {
        super(injectorImpl, key, object, new InternalFactory<T>(){

            @Override
            public T get(Errors errors, InternalContext internalContext, Dependency<?> dependency, boolean bl) {
                throw new AssertionError();
            }
        }, Scoping.UNSCOPED);
    }

    public UntargettedBindingImpl(Object object, Key<T> key, Scoping scoping) {
        super(object, key, scoping);
    }

    @Override
    public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> bindingTargetVisitor) {
        return bindingTargetVisitor.visit(this);
    }

    @Override
    public BindingImpl<T> withScoping(Scoping scoping) {
        return new UntargettedBindingImpl(this.getSource(), this.getKey(), scoping);
    }

    @Override
    public BindingImpl<T> withKey(Key<T> key) {
        return new UntargettedBindingImpl<T>(this.getSource(), key, this.getScoping());
    }

    @Override
    public void applyTo(Binder binder) {
        this.getScoping().applyTo(binder.withSource(this.getSource()).bind(this.getKey()));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(UntargettedBinding.class).add("key", this.getKey()).add("source", this.getSource()).toString();
    }

    public boolean equals(Object object) {
        if (object instanceof UntargettedBindingImpl) {
            UntargettedBindingImpl untargettedBindingImpl = (UntargettedBindingImpl)object;
            return this.getKey().equals(untargettedBindingImpl.getKey()) && this.getScoping().equals(untargettedBindingImpl.getScoping());
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(this.getKey(), this.getScoping());
    }

}

