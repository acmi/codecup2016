/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.Errors;
import com.google.inject.internal.Lookups;
import com.google.inject.internal.MethodAspect;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.Message;
import com.google.inject.spi.TypeEncounter;
import java.lang.reflect.Method;
import java.util.List;
import org.aopalliance.intercept.MethodInterceptor;

final class EncounterImpl<T>
implements TypeEncounter<T> {
    private final Errors errors;
    private final Lookups lookups;
    private List<MembersInjector<? super T>> membersInjectors;
    private List<InjectionListener<? super T>> injectionListeners;
    private List<MethodAspect> aspects;
    private boolean valid = true;

    EncounterImpl(Errors errors, Lookups lookups) {
        this.errors = errors;
        this.lookups = lookups;
    }

    void invalidate() {
        this.valid = false;
    }

    ImmutableList<MethodAspect> getAspects() {
        return this.aspects == null ? ImmutableList.of() : ImmutableList.copyOf(this.aspects);
    }

    @Override
    public /* varargs */ void bindInterceptor(Matcher<? super Method> matcher, MethodInterceptor ... arrmethodInterceptor) {
        Preconditions.checkState(this.valid, "Encounters may not be used after hear() returns.");
        if (this.aspects == null) {
            this.aspects = Lists.newArrayList();
        }
        this.aspects.add(new MethodAspect(Matchers.any(), matcher, arrmethodInterceptor));
    }

    ImmutableSet<MembersInjector<? super T>> getMembersInjectors() {
        return this.membersInjectors == null ? ImmutableSet.of() : ImmutableSet.copyOf(this.membersInjectors);
    }

    ImmutableSet<InjectionListener<? super T>> getInjectionListeners() {
        return this.injectionListeners == null ? ImmutableSet.of() : ImmutableSet.copyOf(this.injectionListeners);
    }

    @Override
    public void register(MembersInjector<? super T> membersInjector) {
        Preconditions.checkState(this.valid, "Encounters may not be used after hear() returns.");
        if (this.membersInjectors == null) {
            this.membersInjectors = Lists.newArrayList();
        }
        this.membersInjectors.add(membersInjector);
    }

    @Override
    public void register(InjectionListener<? super T> injectionListener) {
        Preconditions.checkState(this.valid, "Encounters may not be used after hear() returns.");
        if (this.injectionListeners == null) {
            this.injectionListeners = Lists.newArrayList();
        }
        this.injectionListeners.add(injectionListener);
    }

    @Override
    public /* varargs */ void addError(String string, Object ... arrobject) {
        Preconditions.checkState(this.valid, "Encounters may not be used after hear() returns.");
        this.errors.addMessage(string, arrobject);
    }

    @Override
    public void addError(Throwable throwable) {
        Preconditions.checkState(this.valid, "Encounters may not be used after hear() returns.");
        this.errors.errorInUserCode(throwable, "An exception was caught and reported. Message: %s", throwable.getMessage());
    }

    @Override
    public void addError(Message message) {
        Preconditions.checkState(this.valid, "Encounters may not be used after hear() returns.");
        this.errors.addMessage(message);
    }

    @Override
    public <T> Provider<T> getProvider(Key<T> key) {
        Preconditions.checkState(this.valid, "Encounters may not be used after hear() returns.");
        return this.lookups.getProvider(key);
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> class_) {
        return this.getProvider(Key.get(class_));
    }

    @Override
    public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
        Preconditions.checkState(this.valid, "Encounters may not be used after hear() returns.");
        return this.lookups.getMembersInjector(typeLiteral);
    }

    @Override
    public <T> MembersInjector<T> getMembersInjector(Class<T> class_) {
        return this.getMembersInjector(TypeLiteral.get(class_));
    }
}

