/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.MembersInjector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.AbstractProcessor;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.MembersInjectorImpl;
import com.google.inject.internal.MembersInjectorStore;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.MembersInjectorLookup;
import com.google.inject.spi.ProviderLookup;

final class LookupProcessor
extends AbstractProcessor {
    LookupProcessor(Errors errors) {
        super(errors);
    }

    @Override
    public <T> Boolean visit(MembersInjectorLookup<T> membersInjectorLookup) {
        try {
            MembersInjectorImpl<T> membersInjectorImpl = this.injector.membersInjectorStore.get(membersInjectorLookup.getType(), this.errors);
            membersInjectorLookup.initializeDelegate(membersInjectorImpl);
        }
        catch (ErrorsException errorsException) {
            this.errors.merge(errorsException.getErrors());
        }
        return true;
    }

    @Override
    public <T> Boolean visit(ProviderLookup<T> providerLookup) {
        try {
            Provider<T> provider = this.injector.getProviderOrThrow(providerLookup.getDependency(), this.errors);
            providerLookup.initializeDelegate(provider);
        }
        catch (ErrorsException errorsException) {
            this.errors.merge(errorsException.getErrors());
        }
        return true;
    }
}

