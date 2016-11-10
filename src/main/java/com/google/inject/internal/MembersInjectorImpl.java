/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.ContextualCallable;
import com.google.inject.internal.EncounterImpl;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.MethodAspect;
import com.google.inject.internal.ProvisionListenerStackCallback;
import com.google.inject.internal.SingleMemberInjector;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.InjectionPoint;

final class MembersInjectorImpl<T>
implements MembersInjector<T> {
    private final TypeLiteral<T> typeLiteral;
    private final InjectorImpl injector;
    private final ImmutableList<SingleMemberInjector> memberInjectors;
    private final ImmutableSet<MembersInjector<? super T>> userMembersInjectors;
    private final ImmutableSet<InjectionListener<? super T>> injectionListeners;
    private final ImmutableList<MethodAspect> addedAspects;

    MembersInjectorImpl(InjectorImpl injectorImpl, TypeLiteral<T> typeLiteral, EncounterImpl<T> encounterImpl, ImmutableList<SingleMemberInjector> immutableList) {
        this.injector = injectorImpl;
        this.typeLiteral = typeLiteral;
        this.memberInjectors = immutableList;
        this.userMembersInjectors = encounterImpl.getMembersInjectors();
        this.injectionListeners = encounterImpl.getInjectionListeners();
        this.addedAspects = encounterImpl.getAspects();
    }

    public ImmutableList<SingleMemberInjector> getMemberInjectors() {
        return this.memberInjectors;
    }

    @Override
    public void injectMembers(T t2) {
        Errors errors = new Errors(this.typeLiteral);
        try {
            this.injectAndNotify(t2, errors, null, null, this.typeLiteral, false);
        }
        catch (ErrorsException errorsException) {
            errors.merge(errorsException.getErrors());
        }
        errors.throwProvisionExceptionIfErrorsExist();
    }

    void injectAndNotify(final T t2, final Errors errors, final Key<T> key, final ProvisionListenerStackCallback<T> provisionListenerStackCallback, final Object object, final boolean bl) throws ErrorsException {
        if (t2 == null) {
            return;
        }
        this.injector.callInContext(new ContextualCallable<Void>(){

            @Override
            public Void call(final InternalContext internalContext) throws ErrorsException {
                block4 : {
                    internalContext.pushState(key, object);
                    try {
                        if (provisionListenerStackCallback != null && provisionListenerStackCallback.hasListeners()) {
                            provisionListenerStackCallback.provision(errors, internalContext, new ProvisionListenerStackCallback.ProvisionCallback<T>(){

                                @Override
                                public T call() {
                                    MembersInjectorImpl.this.injectMembers(t2, errors, internalContext, bl);
                                    return (T)t2;
                                }
                            });
                            break block4;
                        }
                        MembersInjectorImpl.this.injectMembers(t2, errors, internalContext, bl);
                    }
                    finally {
                        internalContext.popState();
                    }
                }
                return null;
            }

        });
        if (!bl) {
            this.notifyListeners(t2, errors);
        }
    }

    void notifyListeners(T t2, Errors errors) throws ErrorsException {
        int n2 = errors.size();
        for (InjectionListener injectionListener : this.injectionListeners) {
            try {
                injectionListener.afterInjection(t2);
            }
            catch (RuntimeException runtimeException) {
                errors.errorNotifyingInjectionListener(injectionListener, this.typeLiteral, runtimeException);
            }
        }
        errors.throwIfNewErrors(n2);
    }

    void injectMembers(T t2, Errors errors, InternalContext internalContext, boolean bl) {
        int n2 = this.memberInjectors.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            SingleMemberInjector singleMemberInjector = this.memberInjectors.get(i2);
            if (bl && !singleMemberInjector.getInjectionPoint().isToolable()) continue;
            singleMemberInjector.inject(errors, internalContext, t2);
        }
        if (!bl) {
            for (MembersInjector membersInjector : this.userMembersInjectors) {
                try {
                    membersInjector.injectMembers(t2);
                }
                catch (RuntimeException runtimeException) {
                    errors.errorInUserInjector(membersInjector, this.typeLiteral, runtimeException);
                }
            }
        }
    }

    public String toString() {
        return "MembersInjector<" + this.typeLiteral + ">";
    }

    public ImmutableSet<InjectionPoint> getInjectionPoints() {
        ImmutableSet.Builder builder = ImmutableSet.builder();
        for (SingleMemberInjector singleMemberInjector : this.memberInjectors) {
            builder.add(singleMemberInjector.getInjectionPoint());
        }
        return builder.build();
    }

    public ImmutableList<MethodAspect> getAddedAspects() {
        return this.addedAspects;
    }

}

