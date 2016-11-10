/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.collect.Lists;
import com.google.inject.internal.cglib.proxy.$MethodInterceptor;
import com.google.inject.internal.cglib.proxy.$MethodProxy;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

final class InterceptorStackCallback
implements $MethodInterceptor {
    private static final Set<String> AOP_INTERNAL_CLASSES = new HashSet<String>(Arrays.asList(InterceptorStackCallback.class.getName(), InterceptedMethodInvocation.class.getName(), $MethodProxy.class.getName()));
    final MethodInterceptor[] interceptors;
    final Method method;

    public InterceptorStackCallback(Method method, List<MethodInterceptor> list) {
        this.method = method;
        this.interceptors = list.toArray(new MethodInterceptor[list.size()]);
    }

    @Override
    public Object intercept(Object object, Method method, Object[] arrobject, $MethodProxy $MethodProxy) throws Throwable {
        return new InterceptedMethodInvocation(object, $MethodProxy, arrobject, 0).proceed();
    }

    private void pruneStacktrace(Throwable throwable) {
        for (Throwable throwable2 = throwable; throwable2 != null; throwable2 = throwable2.getCause()) {
            StackTraceElement[] arrstackTraceElement = throwable2.getStackTrace();
            ArrayList<StackTraceElement> arrayList = Lists.newArrayList();
            for (StackTraceElement stackTraceElement : arrstackTraceElement) {
                String string = stackTraceElement.getClassName();
                if (AOP_INTERNAL_CLASSES.contains(string) || string.contains("$EnhancerByGuice$")) continue;
                arrayList.add(stackTraceElement);
            }
            throwable2.setStackTrace(arrayList.toArray(new StackTraceElement[arrayList.size()]));
        }
    }

    private class InterceptedMethodInvocation
    implements MethodInvocation {
        final Object proxy;
        final Object[] arguments;
        final $MethodProxy methodProxy;
        final int index;

        public InterceptedMethodInvocation(Object object, $MethodProxy $MethodProxy, Object[] arrobject, int n2) {
            this.proxy = object;
            this.methodProxy = $MethodProxy;
            this.arguments = arrobject;
            this.index = n2;
        }

        public Object proceed() throws Throwable {
            try {
                return this.index == InterceptorStackCallback.this.interceptors.length ? this.methodProxy.invokeSuper(this.proxy, this.arguments) : InterceptorStackCallback.this.interceptors[this.index].invoke(new InterceptedMethodInvocation(this.proxy, this.methodProxy, this.arguments, this.index + 1));
            }
            catch (Throwable throwable) {
                InterceptorStackCallback.this.pruneStacktrace(throwable);
                throw throwable;
            }
        }

        public Method getMethod() {
            return InterceptorStackCallback.this.method;
        }

        public Object[] getArguments() {
            return this.arguments;
        }

        public Object getThis() {
            return this.proxy;
        }

        public AccessibleObject getStaticPart() {
            return this.getMethod();
        }
    }

}

