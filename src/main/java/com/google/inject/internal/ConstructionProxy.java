/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.collect.ImmutableMap;
import com.google.inject.spi.InjectionPoint;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.aopalliance.intercept.MethodInterceptor;

interface ConstructionProxy<T> {
    public /* varargs */ T newInstance(Object ... var1) throws InvocationTargetException;

    public InjectionPoint getInjectionPoint();

    public Constructor<T> getConstructor();

    public ImmutableMap<Method, List<MethodInterceptor>> getMethodInterceptors();
}

