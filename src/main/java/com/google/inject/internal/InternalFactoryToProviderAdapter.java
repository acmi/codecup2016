/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.inject.Provider;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.InternalFactory;
import com.google.inject.spi.Dependency;

final class InternalFactoryToProviderAdapter<T>
implements InternalFactory<T> {
    private final Provider<? extends T> provider;
    private final Object source;

    public InternalFactoryToProviderAdapter(Provider<? extends T> provider, Object object) {
        this.provider = Preconditions.checkNotNull(provider, "provider");
        this.source = Preconditions.checkNotNull(object, "source");
    }

    @Override
    public T get(Errors errors, InternalContext internalContext, Dependency<?> dependency, boolean bl) throws ErrorsException {
        try {
            return errors.checkForNull(this.provider.get(), this.source, dependency);
        }
        catch (RuntimeException runtimeException) {
            throw errors.withSource(this.source).errorInProvider(runtimeException).toException();
        }
    }

    public String toString() {
        return this.provider.toString();
    }
}

