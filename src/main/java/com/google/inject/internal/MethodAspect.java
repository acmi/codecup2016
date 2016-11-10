/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.inject.matcher.Matcher;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.aopalliance.intercept.MethodInterceptor;

final class MethodAspect {
    private final Matcher<? super Class<?>> classMatcher;
    private final Matcher<? super Method> methodMatcher;
    private final List<MethodInterceptor> interceptors;

    MethodAspect(Matcher<? super Class<?>> matcher, Matcher<? super Method> matcher2, List<MethodInterceptor> list) {
        this.classMatcher = Preconditions.checkNotNull(matcher, "class matcher");
        this.methodMatcher = Preconditions.checkNotNull(matcher2, "method matcher");
        this.interceptors = Preconditions.checkNotNull(list, "interceptors");
    }

    /* varargs */ MethodAspect(Matcher<? super Class<?>> matcher, Matcher<? super Method> matcher2, MethodInterceptor ... arrmethodInterceptor) {
        this(matcher, matcher2, Arrays.asList(arrmethodInterceptor));
    }

    boolean matches(Class<?> class_) {
        return this.classMatcher.matches(class_);
    }

    boolean matches(Method method) {
        return this.methodMatcher.matches(method);
    }

    List<MethodInterceptor> interceptors() {
        return this.interceptors;
    }
}

