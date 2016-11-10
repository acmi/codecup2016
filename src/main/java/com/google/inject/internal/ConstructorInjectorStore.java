/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.ConstructionProxy;
import com.google.inject.internal.ConstructorInjector;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.FailableCache;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.MembersInjectorImpl;
import com.google.inject.internal.MembersInjectorStore;
import com.google.inject.internal.MethodAspect;
import com.google.inject.internal.ProxyFactory;
import com.google.inject.internal.SingleParameterInjector;
import com.google.inject.internal.State;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.InjectionPoint;
import java.util.List;
import java.util.Set;

final class ConstructorInjectorStore {
    private final InjectorImpl injector;
    private final FailableCache<InjectionPoint, ConstructorInjector<?>> cache;

    ConstructorInjectorStore(InjectorImpl injectorImpl) {
        this.cache = new FailableCache<InjectionPoint, ConstructorInjector<?>>(){

            @Override
            protected ConstructorInjector<?> create(InjectionPoint injectionPoint, Errors errors) throws ErrorsException {
                return ConstructorInjectorStore.this.createConstructor(injectionPoint, errors);
            }
        };
        this.injector = injectorImpl;
    }

    public ConstructorInjector<?> get(InjectionPoint injectionPoint, Errors errors) throws ErrorsException {
        return this.cache.get(injectionPoint, errors);
    }

    boolean remove(InjectionPoint injectionPoint) {
        return this.cache.remove(injectionPoint);
    }

    private <T> ConstructorInjector<T> createConstructor(InjectionPoint injectionPoint, Errors errors) throws ErrorsException {
        int n2 = errors.size();
        SingleParameterInjector<?>[] arrsingleParameterInjector = this.injector.getParametersInjectors(injectionPoint.getDependencies(), errors);
        MembersInjectorImpl membersInjectorImpl = this.injector.membersInjectorStore.get(injectionPoint.getDeclaringType(), errors);
        ImmutableList<MethodAspect> immutableList = this.injector.state.getMethodAspects();
        ImmutableList<MethodAspect> immutableList2 = membersInjectorImpl.getAddedAspects().isEmpty() ? immutableList : ImmutableList.copyOf(Iterables.concat(immutableList, membersInjectorImpl.getAddedAspects()));
        ProxyFactory proxyFactory = new ProxyFactory(injectionPoint, immutableList2);
        errors.throwIfNewErrors(n2);
        return new ConstructorInjector(membersInjectorImpl.getInjectionPoints(), proxyFactory.create(), arrsingleParameterInjector, membersInjectorImpl);
    }

}

