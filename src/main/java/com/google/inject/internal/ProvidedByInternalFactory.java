/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.inject.Key;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.ConstructionContext;
import com.google.inject.internal.DelayedInitialize;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.InternalFactory;
import com.google.inject.internal.ProviderInternalFactory;
import com.google.inject.internal.ProvisionListenerStackCallback;
import com.google.inject.spi.Dependency;
import javax.inject.Provider;

class ProvidedByInternalFactory<T>
extends ProviderInternalFactory<T>
implements DelayedInitialize {
    private final Class<?> rawType;
    private final Class<? extends Provider<?>> providerType;
    private final Key<? extends Provider<T>> providerKey;
    private BindingImpl<? extends Provider<T>> providerBinding;
    private ProvisionListenerStackCallback<T> provisionCallback;

    ProvidedByInternalFactory(Class<?> class_, Class<? extends Provider<?>> class_2, Key<? extends Provider<T>> key) {
        super(key);
        this.rawType = class_;
        this.providerType = class_2;
        this.providerKey = key;
    }

    void setProvisionListenerCallback(ProvisionListenerStackCallback<T> provisionListenerStackCallback) {
        this.provisionCallback = provisionListenerStackCallback;
    }

    @Override
    public void initialize(InjectorImpl injectorImpl, Errors errors) throws ErrorsException {
        this.providerBinding = injectorImpl.getBindingOrThrow(this.providerKey, errors, InjectorImpl.JitLimitation.NEW_OR_EXISTING_JIT);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public T get(Errors errors, InternalContext internalContext, Dependency dependency, boolean bl) throws ErrorsException {
        Preconditions.checkState(this.providerBinding != null, "not initialized");
        internalContext.pushState(this.providerKey, this.providerBinding.getSource());
        try {
            errors = errors.withSource(this.providerKey);
            Provider<T> provider = this.providerBinding.getInternalFactory().get(errors, internalContext, dependency, true);
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
            T t2 = super.provision(provider, errors, dependency, constructionContext);
            if (t2 != null && !this.rawType.isInstance(t2)) {
                throw errors.subtypeNotProvided(this.providerType, this.rawType).toException();
            }
            T t3 = t2;
            return t3;
        }
        catch (RuntimeException runtimeException) {
            throw errors.errorInProvider(runtimeException).toException();
        }
    }
}

