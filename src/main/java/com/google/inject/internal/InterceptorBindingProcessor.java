/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.internal.AbstractProcessor;
import com.google.inject.internal.Errors;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.MethodAspect;
import com.google.inject.internal.State;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.InterceptorBinding;
import java.lang.reflect.Method;
import java.util.List;
import org.aopalliance.intercept.MethodInterceptor;

final class InterceptorBindingProcessor
extends AbstractProcessor {
    InterceptorBindingProcessor(Errors errors) {
        super(errors);
    }

    @Override
    public Boolean visit(InterceptorBinding interceptorBinding) {
        this.injector.state.addMethodAspect(new MethodAspect(interceptorBinding.getClassMatcher(), interceptorBinding.getMethodMatcher(), interceptorBinding.getInterceptors()));
        return true;
    }
}

