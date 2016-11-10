/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.ConfigurationException;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.EncounterImpl;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.FailableCache;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.Lookups;
import com.google.inject.internal.MembersInjectorImpl;
import com.google.inject.internal.SingleFieldInjector;
import com.google.inject.internal.SingleMemberInjector;
import com.google.inject.internal.SingleMethodInjector;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.Message;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.google.inject.spi.TypeListenerBinding;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final class MembersInjectorStore {
    private final InjectorImpl injector;
    private final ImmutableList<TypeListenerBinding> typeListenerBindings;
    private final FailableCache<TypeLiteral<?>, MembersInjectorImpl<?>> cache;

    MembersInjectorStore(InjectorImpl injectorImpl, List<TypeListenerBinding> list) {
        this.cache = new FailableCache<TypeLiteral<?>, MembersInjectorImpl<?>>(){

            @Override
            protected MembersInjectorImpl<?> create(TypeLiteral<?> typeLiteral, Errors errors) throws ErrorsException {
                return MembersInjectorStore.this.createWithListeners(typeLiteral, errors);
            }
        };
        this.injector = injectorImpl;
        this.typeListenerBindings = ImmutableList.copyOf(list);
    }

    public boolean hasTypeListeners() {
        return !this.typeListenerBindings.isEmpty();
    }

    public <T> MembersInjectorImpl<T> get(TypeLiteral<T> typeLiteral, Errors errors) throws ErrorsException {
        return this.cache.get(typeLiteral, errors);
    }

    boolean remove(TypeLiteral<?> typeLiteral) {
        return this.cache.remove(typeLiteral);
    }

    private <T> MembersInjectorImpl<T> createWithListeners(TypeLiteral<T> typeLiteral, Errors errors) throws ErrorsException {
        Set set;
        int n2 = errors.size();
        try {
            set = InjectionPoint.forInstanceMethodsAndFields(typeLiteral);
        }
        catch (ConfigurationException configurationException) {
            errors.merge(configurationException.getErrorMessages());
            set = (Set)configurationException.getPartialValue();
        }
        ImmutableList<SingleMemberInjector> immutableList = this.getInjectors(set, errors);
        errors.throwIfNewErrors(n2);
        EncounterImpl encounterImpl = new EncounterImpl(errors, this.injector.lookups);
        HashSet<TypeListener> hashSet = Sets.newHashSet();
        for (TypeListenerBinding typeListenerBinding : this.typeListenerBindings) {
            TypeListener typeListener = typeListenerBinding.getListener();
            if (hashSet.contains(typeListener) || !typeListenerBinding.getTypeMatcher().matches(typeLiteral)) continue;
            hashSet.add(typeListener);
            try {
                typeListener.hear(typeLiteral, encounterImpl);
            }
            catch (RuntimeException runtimeException) {
                errors.errorNotifyingTypeListener(typeListenerBinding, typeLiteral, runtimeException);
            }
        }
        encounterImpl.invalidate();
        errors.throwIfNewErrors(n2);
        return new MembersInjectorImpl<T>(this.injector, typeLiteral, encounterImpl, immutableList);
    }

    ImmutableList<SingleMemberInjector> getInjectors(Set<InjectionPoint> set, Errors errors) {
        ArrayList<SingleFieldInjector> arrayList = Lists.newArrayList();
        for (InjectionPoint injectionPoint : set) {
            try {
                Errors errors2 = injectionPoint.isOptional() ? new Errors(injectionPoint) : errors.withSource(injectionPoint);
                SingleMemberInjector singleMemberInjector = injectionPoint.getMember() instanceof Field ? new SingleFieldInjector(this.injector, injectionPoint, errors2) : new SingleMethodInjector(this.injector, injectionPoint, errors2);
                arrayList.add((SingleFieldInjector)singleMemberInjector);
            }
            catch (ErrorsException errorsException) {}
        }
        return ImmutableList.copyOf(arrayList);
    }

}

