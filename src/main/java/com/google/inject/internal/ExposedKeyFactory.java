/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.CreationListener;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.InternalFactory;
import com.google.inject.internal.State;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.PrivateElements;

final class ExposedKeyFactory<T>
implements CreationListener,
InternalFactory<T> {
    private final Key<T> key;
    private final PrivateElements privateElements;
    private BindingImpl<T> delegate;

    ExposedKeyFactory(Key<T> key, PrivateElements privateElements) {
        this.key = key;
        this.privateElements = privateElements;
    }

    @Override
    public void notify(Errors errors) {
        InjectorImpl injectorImpl = (InjectorImpl)this.privateElements.getInjector();
        BindingImpl<T> bindingImpl = injectorImpl.state.getExplicitBinding(this.key);
        if (bindingImpl.getInternalFactory() == this) {
            errors.withSource(bindingImpl.getSource()).exposedButNotBound(this.key);
            return;
        }
        this.delegate = bindingImpl;
    }

    @Override
    public T get(Errors errors, InternalContext internalContext, Dependency<?> dependency, boolean bl) throws ErrorsException {
        return this.delegate.getInternalFactory().get(errors, internalContext, dependency, bl);
    }
}

