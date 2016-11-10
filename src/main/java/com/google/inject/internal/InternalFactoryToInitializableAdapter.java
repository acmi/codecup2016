/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.inject.internal.ConstructionContext;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.Initializable;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.ProviderInternalFactory;
import com.google.inject.internal.ProvisionListenerStackCallback;
import com.google.inject.spi.Dependency;
import javax.inject.Provider;

final class InternalFactoryToInitializableAdapter<T>
extends ProviderInternalFactory<T> {
    private final ProvisionListenerStackCallback<T> provisionCallback;
    private final Initializable<? extends Provider<? extends T>> initializable;

    public InternalFactoryToInitializableAdapter(Initializable<? extends Provider<? extends T>> initializable, Object object, ProvisionListenerStackCallback<T> provisionListenerStackCallback) {
        super(object);
        this.provisionCallback = Preconditions.checkNotNull(provisionListenerStackCallback, "provisionCallback");
        this.initializable = Preconditions.checkNotNull(initializable, "provider");
    }

    @Override
    public T get(Errors errors, InternalContext internalContext, Dependency<?> dependency, boolean bl) throws ErrorsException {
        return this.circularGet(this.initializable.get(errors), errors, internalContext, dependency, this.provisionCallback);
    }

    @Override
    protected T provision(Provider<? extends T> provider, Errors errors, Dependency<?> dependency, ConstructionContext<T> constructionContext) throws ErrorsException {
        try {
            return super.provision(provider, errors, dependency, constructionContext);
        }
        catch (RuntimeException runtimeException) {
            throw errors.withSource(this.source).errorInProvider(runtimeException).toException();
        }
    }

    public String toString() {
        return this.initializable.toString();
    }
}

