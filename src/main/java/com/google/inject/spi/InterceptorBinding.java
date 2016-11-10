/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.inject.Binder;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.Element;
import com.google.inject.spi.ElementVisitor;
import java.lang.reflect.Method;
import java.util.List;
import org.aopalliance.intercept.MethodInterceptor;

public final class InterceptorBinding
implements Element {
    private final Object source;
    private final Matcher<? super Class<?>> classMatcher;
    private final Matcher<? super Method> methodMatcher;
    private final ImmutableList<MethodInterceptor> interceptors;

    InterceptorBinding(Object object, Matcher<? super Class<?>> matcher, Matcher<? super Method> matcher2, MethodInterceptor[] arrmethodInterceptor) {
        this.source = Preconditions.checkNotNull(object, "source");
        this.classMatcher = Preconditions.checkNotNull(matcher, "classMatcher");
        this.methodMatcher = Preconditions.checkNotNull(matcher2, "methodMatcher");
        this.interceptors = ImmutableList.copyOf(arrmethodInterceptor);
    }

    @Override
    public Object getSource() {
        return this.source;
    }

    public Matcher<? super Class<?>> getClassMatcher() {
        return this.classMatcher;
    }

    public Matcher<? super Method> getMethodMatcher() {
        return this.methodMatcher;
    }

    public List<MethodInterceptor> getInterceptors() {
        return this.interceptors;
    }

    @Override
    public <T> T acceptVisitor(ElementVisitor<T> elementVisitor) {
        return elementVisitor.visit(this);
    }

    @Override
    public void applyTo(Binder binder) {
        binder.withSource(this.getSource()).bindInterceptor(this.classMatcher, this.methodMatcher, this.interceptors.toArray(new MethodInterceptor[this.interceptors.size()]));
    }
}

