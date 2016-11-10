/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.inject.Key;
import com.google.inject.internal.ConstructionContext;
import com.google.inject.internal.CreationListener;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.InternalFactory;
import com.google.inject.internal.ProviderInternalFactory;
import com.google.inject.internal.ProvisionListenerStackCallback;
import com.google.inject.spi.Dependency;
import javax.inject.Provider;

final class BoundProviderFactory<T>
extends ProviderInternalFactory<T>
implements CreationListener {
    private final ProvisionListenerStackCallback<T> provisionCallback;
    private final InjectorImpl injector;
    final Key<? extends Provider<? extends T>> providerKey;
    private InternalFactory<? extends Provider<? extends T>> providerFactory;

    BoundProviderFactory(InjectorImpl injectorImpl, Key<? extends Provider<? extends T>> key, Object object, ProvisionListenerStackCallback<T> provisionListenerStackCallback) {
        super(object);
        this.provisionCallback = Preconditions.checkNotNull(provisionListenerStackCallback, "provisionCallback");
        this.injector = injectorImpl;
        this.providerKey = key;
    }

    @Override
    public void notify(Errors errors) {
        try {
            this.providerFactory = this.injector.getInternalFactory(this.providerKey, errors.withSource(this.source), InjectorImpl.JitLimitation.NEW_OR_EXISTING_JIT);
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
        internalContext.pushState(this.providerKey, this.source);
        try {
            errors = errors.withSource(this.providerKey);
            Provider<? extends T> provider = this.providerFactory.get(errors, internalContext, dependency, true);
            T t2 = this.circularGet(provider, errors, internalContext, dependency, this.provisionCallback);
            return t2;
        }
        finally {
            internalContext.popState();
        }
    }

    @Override
    protected T provision(Provider<? extends T> provider, Errors errors, Dependency<?> dependency, ConstructionContext<T> constructionContext) throws ErrorsException {
        try {
            return super.provision(provider, errors, dependency, constructionContext);
        }
        catch (RuntimeException runtimeException) {
            throw errors.errorInProvider(runtimeException).toException();
        }
    }

    public String toString() {
        return this.providerKey.toString();
    }
}

