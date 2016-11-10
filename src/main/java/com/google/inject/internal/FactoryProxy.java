/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.MoreObjects;
import com.google.inject.Key;
import com.google.inject.internal.CreationListener;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.InternalFactory;
import com.google.inject.spi.Dependency;

final class FactoryProxy<T>
implements CreationListener,
InternalFactory<T> {
    private final InjectorImpl injector;
    private final Key<T> key;
    private final Key<? extends T> targetKey;
    private final Object source;
    private InternalFactory<? extends T> targetFactory;

    FactoryProxy(InjectorImpl injectorImpl, Key<T> key, Key<? extends T> key2, Object object) {
        this.injector = injectorImpl;
        this.key = key;
        this.targetKey = key2;
        this.source = object;
    }

    @Override
    public void notify(Errors errors) {
        try {
            this.targetFactory = this.injector.getInternalFactory(this.targetKey, errors.withSource(this.source), InjectorImpl.JitLimitation.NEW_OR_EXISTING_JIT);
        }
        catch (ErrorsException errorsException) {
            errors.merge(errorsException.getErrors());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public T get(Errors errors, InternalContext internalContext, Dependency<?> dependency, boolean bl) throws ErrorsException {
        internalContext.pushState(this.targetKey, this.source);
        try {
            T t2 = this.targetFactory.get(errors.withSource(this.targetKey), internalContext, dependency, true);
            return t2;
        }
        finally {
            internalContext.popState();
        }
    }

    public String toString() {
        return MoreObjects.toStringHelper(FactoryProxy.class).add("key", this.key).add("provider", this.targetFactory).toString();
    }
}

