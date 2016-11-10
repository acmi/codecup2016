/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.collect.Lists;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.Errors;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.LookupProcessor;
import com.google.inject.internal.Lookups;
import com.google.inject.spi.Element;
import com.google.inject.spi.MembersInjectorLookup;
import com.google.inject.spi.ProviderLookup;
import java.util.List;

final class DeferredLookups
implements Lookups {
    private final InjectorImpl injector;
    private final List<Element> lookups = Lists.newArrayList();

    DeferredLookups(InjectorImpl injectorImpl) {
        this.injector = injectorImpl;
    }

    void initialize(Errors errors) {
        this.injector.lookups = this.injector;
        new LookupProcessor(errors).process(this.injector, this.lookups);
    }

    @Override
    public <T> Provider<T> getProvider(Key<T> key) {
        ProviderLookup<T> providerLookup = new ProviderLookup<T>(key, key);
        this.lookups.add(providerLookup);
        return providerLookup.getProvider();
    }

    @Override
    public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
        MembersInjectorLookup<T> membersInjectorLookup = new MembersInjectorLookup<T>(typeLiteral, typeLiteral);
        this.lookups.add(membersInjectorLookup);
        return membersInjectorLookup.getMembersInjector();
    }
}

