/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.internal.BytecodeGen;
import com.google.inject.internal.CircularDependencyProxy;
import com.google.inject.internal.DelegatingInvocationHandler;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InjectorImpl;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

final class ConstructionContext<T> {
    T currentReference;
    boolean constructing;
    List<DelegatingInvocationHandler<T>> invocationHandlers;

    ConstructionContext() {
    }

    public T getCurrentReference() {
        return this.currentReference;
    }

    public void removeCurrentReference() {
        this.currentReference = null;
    }

    public void setCurrentReference(T t2) {
        this.currentReference = t2;
    }

    public boolean isConstructing() {
        return this.constructing;
    }

    public void startConstruction() {
        this.constructing = true;
    }

    public void finishConstruction() {
        this.constructing = false;
        this.invocationHandlers = null;
    }

    public Object createProxy(Errors errors, InjectorImpl.InjectorOptions injectorOptions, Class<?> class_) throws ErrorsException {
        if (injectorOptions.disableCircularProxies) {
            throw errors.circularDependenciesDisabled(class_).toException();
        }
        if (!class_.isInterface()) {
            throw errors.cannotProxyClass(class_).toException();
        }
        if (this.invocationHandlers == null) {
            this.invocationHandlers = new ArrayList<DelegatingInvocationHandler<T>>();
        }
        DelegatingInvocationHandler delegatingInvocationHandler = new DelegatingInvocationHandler();
        this.invocationHandlers.add(delegatingInvocationHandler);
        ClassLoader classLoader = BytecodeGen.getClassLoader(class_);
        return class_.cast(Proxy.newProxyInstance(classLoader, new Class[]{class_, CircularDependencyProxy.class}, delegatingInvocationHandler));
    }

    public void setProxyDelegates(T t2) {
        if (this.invocationHandlers != null) {
            for (DelegatingInvocationHandler<T> delegatingInvocationHandler : this.invocationHandlers) {
                delegatingInvocationHandler.setDelegate(t2);
            }
            this.invocationHandlers = null;
        }
    }
}

