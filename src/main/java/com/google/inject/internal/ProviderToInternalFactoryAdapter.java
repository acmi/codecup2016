/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.google.inject.internal.ContextualCallable;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.InternalFactory;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.Message;
import java.util.List;

final class ProviderToInternalFactoryAdapter<T>
implements Provider<T> {
    private final InjectorImpl injector;
    private final InternalFactory<? extends T> internalFactory;

    public ProviderToInternalFactoryAdapter(InjectorImpl injectorImpl, InternalFactory<? extends T> internalFactory) {
        this.injector = injectorImpl;
        this.internalFactory = internalFactory;
    }

    @Override
    public T get() {
        final Errors errors = new Errors();
        try {
            Object t2 = this.injector.callInContext(new ContextualCallable<T>(){

                @Override
                public T call(InternalContext internalContext) throws ErrorsException {
                    Dependency dependency = internalContext.getDependency();
                    return ProviderToInternalFactoryAdapter.this.internalFactory.get(errors, internalContext, dependency, true);
                }
            });
            errors.throwIfNewErrors(0);
            return t2;
        }
        catch (ErrorsException errorsException) {
            throw new ProvisionException(errors.merge(errorsException.getErrors()).getMessages());
        }
    }

    public String toString() {
        return this.internalFactory.toString();
    }

}

