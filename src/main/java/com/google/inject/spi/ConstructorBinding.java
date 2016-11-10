/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.Binding;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.InjectionPoint;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aopalliance.intercept.MethodInterceptor;

public interface ConstructorBinding<T>
extends Binding<T>,
HasDependencies {
    public InjectionPoint getConstructor();

    public Set<InjectionPoint> getInjectableMembers();

    public Map<Method, List<MethodInterceptor>> getMethodInterceptors();
}

