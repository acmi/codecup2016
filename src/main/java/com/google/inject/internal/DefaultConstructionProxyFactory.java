/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.inject.internal.BytecodeGen;
import com.google.inject.internal.ConstructionProxy;
import com.google.inject.internal.ConstructionProxyFactory;
import com.google.inject.internal.cglib.core.$CodeGenerationException;
import com.google.inject.internal.cglib.reflect.$FastClass;
import com.google.inject.spi.InjectionPoint;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import org.aopalliance.intercept.MethodInterceptor;

final class DefaultConstructionProxyFactory<T>
implements ConstructionProxyFactory<T> {
    private final InjectionPoint injectionPoint;

    DefaultConstructionProxyFactory(InjectionPoint injectionPoint) {
        this.injectionPoint = injectionPoint;
    }

    @Override
    public ConstructionProxy<T> create() {
        Constructor constructor = (Constructor)this.injectionPoint.getMember();
        try {
            $FastClass $FastClass = BytecodeGen.newFastClassForMember(constructor);
            if ($FastClass != null) {
                int n2 = $FastClass.getIndex(constructor.getParameterTypes());
                Preconditions.checkArgument(n2 >= 0, "Could not find constructor %s in fast class", constructor);
                return new FastClassProxy(this.injectionPoint, constructor, $FastClass, n2);
            }
        }
        catch ($CodeGenerationException $CodeGenerationException) {
            // empty catch block
        }
        return new ReflectiveProxy(this.injectionPoint, constructor);
    }

    private static final class ReflectiveProxy<T>
    implements ConstructionProxy<T> {
        final Constructor<T> constructor;
        final InjectionPoint injectionPoint;

        ReflectiveProxy(InjectionPoint injectionPoint, Constructor<T> constructor) {
            if (!Modifier.isPublic(constructor.getDeclaringClass().getModifiers()) || !Modifier.isPublic(constructor.getModifiers())) {
                constructor.setAccessible(true);
            }
            this.injectionPoint = injectionPoint;
            this.constructor = constructor;
        }

        @Override
        public /* varargs */ T newInstance(Object ... arrobject) throws InvocationTargetException {
            try {
                return this.constructor.newInstance(arrobject);
            }
            catch (InstantiationException instantiationException) {
                throw new AssertionError(instantiationException);
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new AssertionError(illegalAccessException);
            }
        }

        @Override
        public InjectionPoint getInjectionPoint() {
            return this.injectionPoint;
        }

        @Override
        public Constructor<T> getConstructor() {
            return this.constructor;
        }

        @Override
        public ImmutableMap<Method, List<MethodInterceptor>> getMethodInterceptors() {
            return ImmutableMap.of();
        }
    }

    private static final class FastClassProxy<T>
    implements ConstructionProxy<T> {
        final InjectionPoint injectionPoint;
        final Constructor<T> constructor;
        final $FastClass fc;
        final int index;

        private FastClassProxy(InjectionPoint injectionPoint, Constructor<T> constructor, $FastClass $FastClass, int n2) {
            this.injectionPoint = injectionPoint;
            this.constructor = constructor;
            this.fc = $FastClass;
            this.index = n2;
        }

        @Override
        public /* varargs */ T newInstance(Object ... arrobject) throws InvocationTargetException {
            return (T)this.fc.newInstance(this.index, arrobject);
        }

        @Override
        public InjectionPoint getInjectionPoint() {
            return this.injectionPoint;
        }

        @Override
        public Constructor<T> getConstructor() {
            return this.constructor;
        }

        @Override
        public ImmutableMap<Method, List<MethodInterceptor>> getMethodInterceptors() {
            return ImmutableMap.of();
        }
    }

}

