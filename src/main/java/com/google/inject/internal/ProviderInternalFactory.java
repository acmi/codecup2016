/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.ConstructionContext;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.InternalFactory;
import com.google.inject.internal.ProvisionListenerStackCallback;
import com.google.inject.spi.Dependency;
import javax.inject.Provider;

abstract class ProviderInternalFactory<T>
implements InternalFactory<T> {
    protected final Object source;

    ProviderInternalFactory(Object object) {
        this.source = Preconditions.checkNotNull(object, "source");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected T circularGet(final Provider<? extends T> provider, final Errors errors, InternalContext internalContext, final Dependency<?> dependency, ProvisionListenerStackCallback<T> provisionListenerStackCallback) throws ErrorsException {
        final ConstructionContext constructionContext = internalContext.getConstructionContext(this);
        if (constructionContext.isConstructing()) {
            Class class_ = dependency.getKey().getTypeLiteral().getRawType();
            Object object = constructionContext.createProxy(errors, internalContext.getInjectorOptions(), class_);
            return (T)object;
        }
        constructionContext.startConstruction();
        try {
            if (!provisionListenerStackCallback.hasListeners()) {
                T t2 = this.provision(provider, errors, dependency, constructionContext);
                return t2;
            }
            T t3 = provisionListenerStackCallback.provision(errors, internalContext, new ProvisionListenerStackCallback.ProvisionCallback<T>(){

                @Override
                public T call() throws ErrorsException {
                    return ProviderInternalFactory.this.provision(provider, errors, dependency, constructionContext);
                }
            });
            return t3;
        }
        finally {
            constructionContext.removeCurrentReference();
            constructionContext.finishConstruction();
        }
    }

    protected T provision(Provider<? extends T> provider, Errors errors, Dependency<?> dependency, ConstructionContext<T> constructionContext) throws ErrorsException {
        T t2 = errors.checkForNull(provider.get(), this.source, dependency);
        constructionContext.setProxyDelegates(t2);
        return t2;
    }

}

