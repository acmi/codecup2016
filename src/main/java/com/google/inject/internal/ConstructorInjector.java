/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.collect.ImmutableSet;
import com.google.inject.internal.ConstructionContext;
import com.google.inject.internal.ConstructionProxy;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.MembersInjectorImpl;
import com.google.inject.internal.ProvisionListenerStackCallback;
import com.google.inject.internal.SingleParameterInjector;
import com.google.inject.spi.InjectionPoint;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

final class ConstructorInjector<T> {
    private final ImmutableSet<InjectionPoint> injectableMembers;
    private final SingleParameterInjector<?>[] parameterInjectors;
    private final ConstructionProxy<T> constructionProxy;
    private final MembersInjectorImpl<T> membersInjector;

    ConstructorInjector(Set<InjectionPoint> set, ConstructionProxy<T> constructionProxy, SingleParameterInjector<?>[] arrsingleParameterInjector, MembersInjectorImpl<T> membersInjectorImpl) {
        this.injectableMembers = ImmutableSet.copyOf(set);
        this.constructionProxy = constructionProxy;
        this.parameterInjectors = arrsingleParameterInjector;
        this.membersInjector = membersInjectorImpl;
    }

    public ImmutableSet<InjectionPoint> getInjectableMembers() {
        return this.injectableMembers;
    }

    ConstructionProxy<T> getConstructionProxy() {
        return this.constructionProxy;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    Object construct(final Errors errors, final InternalContext internalContext, Class<?> class_, ProvisionListenerStackCallback<T> provisionListenerStackCallback) throws ErrorsException {
        final ConstructionContext constructionContext = internalContext.getConstructionContext(this);
        InjectorImpl.InjectorOptions injectorOptions = internalContext.getInjectorOptions();
        if (constructionContext.isConstructing()) {
            return constructionContext.createProxy(errors, injectorOptions, class_);
        }
        Object t2 = constructionContext.getCurrentReference();
        if (t2 != null) {
            if (injectorOptions.disableCircularProxies) {
                throw errors.circularDependenciesDisabled(class_).toException();
            }
            return t2;
        }
        constructionContext.startConstruction();
        try {
            if (!provisionListenerStackCallback.hasListeners()) {
                Object t3 = this.provision(errors, internalContext, constructionContext);
                return t3;
            }
            T t4 = provisionListenerStackCallback.provision(errors, internalContext, new ProvisionListenerStackCallback.ProvisionCallback<T>(){

                @Override
                public T call() throws ErrorsException {
                    return (T)ConstructorInjector.this.provision(errors, internalContext, constructionContext);
                }
            });
            return t4;
        }
        finally {
            constructionContext.finishConstruction();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private T provision(Errors errors, InternalContext internalContext, ConstructionContext<T> constructionContext) throws ErrorsException {
        try {
            Object object;
            T t2;
            try {
                object = SingleParameterInjector.getAll(errors, internalContext, this.parameterInjectors);
                t2 = this.constructionProxy.newInstance((Object[])object);
                constructionContext.setProxyDelegates(t2);
            }
            finally {
                constructionContext.finishConstruction();
            }
            constructionContext.setCurrentReference(t2);
            this.membersInjector.injectMembers(t2, errors, internalContext, false);
            this.membersInjector.notifyListeners(t2, errors);
            object = t2;
            return (T)object;
        }
        catch (InvocationTargetException invocationTargetException /* !! */ ) {
            Throwable throwable = invocationTargetException /* !! */ .getCause() != null ? invocationTargetException /* !! */ .getCause() : invocationTargetException /* !! */ ;
            throw errors.withSource(this.constructionProxy.getInjectionPoint()).errorInjectingConstructor(throwable).toException();
        }
        finally {
            constructionContext.removeCurrentReference();
        }
    }

}

