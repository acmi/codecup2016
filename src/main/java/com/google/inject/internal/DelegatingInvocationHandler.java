/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Preconditions;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class DelegatingInvocationHandler<T>
implements InvocationHandler {
    private volatile boolean initialized;
    private T delegate;

    DelegatingInvocationHandler() {
    }

    @Override
    public Object invoke(Object object, Method method, Object[] arrobject) throws Throwable {
        try {
            Preconditions.checkState(this.initialized, "This is a proxy used to support circular references. The object we're proxying is not constructed yet. Please wait until after injection has completed to use this object.");
            Preconditions.checkNotNull(this.delegate, "This is a proxy used to support circular references. The object we're  proxying is initialized to null. No methods can be called.");
            return method.invoke(this.delegate, arrobject);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new RuntimeException(illegalAccessException);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new RuntimeException(illegalArgumentException);
        }
        catch (InvocationTargetException invocationTargetException) {
            throw invocationTargetException.getTargetException();
        }
    }

    void setDelegate(T t2) {
        this.delegate = t2;
        this.initialized = true;
    }
}

