/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.internal.BytecodeGen;
import com.google.inject.internal.ConstructionProxy;
import com.google.inject.internal.ConstructionProxyFactory;
import com.google.inject.internal.DefaultConstructionProxyFactory;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InterceptorStackCallback;
import com.google.inject.internal.MethodAspect;
import com.google.inject.internal.cglib.core.$MethodWrapper;
import com.google.inject.internal.cglib.proxy.$Callback;
import com.google.inject.internal.cglib.proxy.$CallbackFilter;
import com.google.inject.internal.cglib.proxy.$Enhancer;
import com.google.inject.internal.cglib.proxy.$MethodInterceptor;
import com.google.inject.internal.cglib.proxy.$NoOp;
import com.google.inject.internal.cglib.reflect.$FastClass;
import com.google.inject.spi.InjectionPoint;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.aopalliance.intercept.MethodInterceptor;

final class ProxyFactory<T>
implements ConstructionProxyFactory<T> {
    private static final Logger logger = Logger.getLogger(ProxyFactory.class.getName());
    private final InjectionPoint injectionPoint;
    private final ImmutableMap<Method, List<MethodInterceptor>> interceptors;
    private final Class<T> declaringClass;
    private final List<Method> methods;
    private final $Callback[] callbacks;
    private BytecodeGen.Visibility visibility = BytecodeGen.Visibility.PUBLIC;

    ProxyFactory(InjectionPoint injectionPoint, Iterable<MethodAspect> iterable) {
        Object object;
        boolean bl;
        void var7_13;
        Object object2;
        this.injectionPoint = injectionPoint;
        Constructor constructor = (Constructor)injectionPoint.getMember();
        this.declaringClass = constructor.getDeclaringClass();
        ArrayList<MethodAspect> arrayList = Lists.newArrayList();
        for (MethodAspect object3 : iterable) {
            if (!object3.matches(this.declaringClass)) continue;
            arrayList.add(object3);
        }
        if (arrayList.isEmpty()) {
            this.interceptors = ImmutableMap.of();
            this.methods = ImmutableList.of();
            this.callbacks = null;
            return;
        }
        this.methods = Lists.newArrayList();
        $Enhancer.getMethods(this.declaringClass, null, this.methods);
        ArrayList arrayList2 = Lists.newArrayList();
        for (Method method : this.methods) {
            arrayList2.add(new MethodInterceptorsPair(method));
        }
        boolean bl2 = false;
        for (MethodAspect methodAspect : arrayList) {
            object2 = arrayList2.iterator();
            while (object2.hasNext()) {
                object = (MethodInterceptorsPair)object2.next();
                if (!methodAspect.matches(object.method)) continue;
                if (object.method.isSynthetic()) {
                    logger.log(Level.WARNING, "Method [{0}] is synthetic and is being intercepted by {1}. This could indicate a bug.  The method may be intercepted twice, or may not be intercepted at all.", new Object[]{object.method, methodAspect.interceptors()});
                }
                this.visibility = this.visibility.and(BytecodeGen.Visibility.forMember(object.method));
                object.addAll(methodAspect.interceptors());
                bl = true;
            }
        }
        if (!bl) {
            this.interceptors = ImmutableMap.of();
            this.callbacks = null;
            return;
        }
        Object var7_12 = null;
        this.callbacks = new $Callback[this.methods.size()];
        for (int i2 = 0; i2 < this.methods.size(); ++i2) {
            object2 = (MethodInterceptorsPair)arrayList2.get(i2);
            if (!object2.hasInterceptors()) {
                this.callbacks[i2] = $NoOp.INSTANCE;
                continue;
            }
            if (var7_13 == null) {
                ImmutableMap.Builder builder = ImmutableMap.builder();
            }
            object = ImmutableSet.copyOf(object2.interceptors).asList();
            var7_13.put(object2.method, object);
            this.callbacks[i2] = new InterceptorStackCallback(object2.method, (List<MethodInterceptor>)object);
        }
        this.interceptors = var7_13 != null ? var7_13.build() : ImmutableMap.of();
    }

    public ImmutableMap<Method, List<MethodInterceptor>> getInterceptors() {
        return this.interceptors;
    }

    @Override
    public ConstructionProxy<T> create() throws ErrorsException {
        if (this.interceptors.isEmpty()) {
            return new DefaultConstructionProxyFactory(this.injectionPoint).create();
        }
        Class[] arrclass = new Class[this.callbacks.length];
        for (int i2 = 0; i2 < this.callbacks.length; ++i2) {
            arrclass[i2] = this.callbacks[i2] == $NoOp.INSTANCE ? $NoOp.class : $MethodInterceptor.class;
        }
        try {
            $Enhancer $Enhancer = BytecodeGen.newEnhancer(this.declaringClass, this.visibility);
            $Enhancer.setCallbackFilter(new IndicesCallbackFilter(this.methods));
            $Enhancer.setCallbackTypes(arrclass);
            return new ProxyConstructor($Enhancer, this.injectionPoint, this.callbacks, this.interceptors);
        }
        catch (Throwable throwable) {
            throw new Errors().errorEnhancingClass(this.declaringClass, throwable).toException();
        }
    }

    private static class ProxyConstructor<T>
    implements ConstructionProxy<T> {
        final Class<?> enhanced;
        final InjectionPoint injectionPoint;
        final Constructor<T> constructor;
        final $Callback[] callbacks;
        final int constructorIndex;
        final ImmutableMap<Method, List<MethodInterceptor>> methodInterceptors;
        final $FastClass fastClass;

        ProxyConstructor($Enhancer $Enhancer, InjectionPoint injectionPoint, $Callback[] arr$Callback, ImmutableMap<Method, List<MethodInterceptor>> immutableMap) {
            this.enhanced = $Enhancer.createClass();
            this.injectionPoint = injectionPoint;
            this.constructor = (Constructor)injectionPoint.getMember();
            this.callbacks = arr$Callback;
            this.methodInterceptors = immutableMap;
            this.fastClass = BytecodeGen.newFastClassForMember(this.enhanced, this.constructor);
            this.constructorIndex = this.fastClass.getIndex(this.constructor.getParameterTypes());
        }

        @Override
        public /* varargs */ T newInstance(Object ... arrobject) throws InvocationTargetException {
            $Enhancer.registerCallbacks(this.enhanced, this.callbacks);
            try {
                Object object = this.fastClass.newInstance(this.constructorIndex, arrobject);
                return (T)object;
            }
            finally {
                $Enhancer.registerCallbacks(this.enhanced, null);
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
            return this.methodInterceptors;
        }
    }

    private static class IndicesCallbackFilter
    implements $CallbackFilter {
        final Map<Object, Integer> indices;
        final int hashCode;

        IndicesCallbackFilter(List<Method> list) {
            HashMap<Object, Integer> hashMap = Maps.newHashMap();
            for (int i2 = 0; i2 < list.size(); ++i2) {
                hashMap.put($MethodWrapper.create(list.get(i2)), i2);
            }
            this.indices = hashMap;
            this.hashCode = hashMap.hashCode();
        }

        @Override
        public int accept(Method method) {
            return this.indices.get($MethodWrapper.create(method));
        }

        @Override
        public boolean equals(Object object) {
            return object instanceof IndicesCallbackFilter && ((IndicesCallbackFilter)object).indices.equals(this.indices);
        }

        public int hashCode() {
            return this.hashCode;
        }
    }

    private static class MethodInterceptorsPair {
        final Method method;
        List<MethodInterceptor> interceptors;

        MethodInterceptorsPair(Method method) {
            this.method = method;
        }

        void addAll(List<MethodInterceptor> list) {
            if (this.interceptors == null) {
                this.interceptors = Lists.newArrayList();
            }
            this.interceptors.addAll(list);
        }

        boolean hasInterceptors() {
            return this.interceptors != null;
        }
    }

}

