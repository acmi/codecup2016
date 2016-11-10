/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject;

import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.AnnotatedConstantBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.Message;
import com.google.inject.spi.ProvisionListener;
import com.google.inject.spi.TypeConverter;
import com.google.inject.spi.TypeListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInterceptor;

public abstract class AbstractModule
implements Module {
    Binder binder;

    @Override
    public final synchronized void configure(Binder binder) {
        Preconditions.checkState(this.binder == null, "Re-entry is not allowed.");
        this.binder = Preconditions.checkNotNull(binder, "builder");
        try {
            this.configure();
        }
        finally {
            this.binder = null;
        }
    }

    protected abstract void configure();

    protected Binder binder() {
        Preconditions.checkState(this.binder != null, "The binder can only be used inside configure()");
        return this.binder;
    }

    protected void bindScope(Class<? extends Annotation> class_, Scope scope) {
        this.binder().bindScope(class_, scope);
    }

    protected <T> LinkedBindingBuilder<T> bind(Key<T> key) {
        return this.binder().bind(key);
    }

    protected <T> AnnotatedBindingBuilder<T> bind(TypeLiteral<T> typeLiteral) {
        return this.binder().bind(typeLiteral);
    }

    protected <T> AnnotatedBindingBuilder<T> bind(Class<T> class_) {
        return this.binder().bind(class_);
    }

    protected AnnotatedConstantBindingBuilder bindConstant() {
        return this.binder().bindConstant();
    }

    protected void install(Module module) {
        this.binder().install(module);
    }

    protected /* varargs */ void addError(String string, Object ... arrobject) {
        this.binder().addError(string, arrobject);
    }

    protected void addError(Throwable throwable) {
        this.binder().addError(throwable);
    }

    protected void addError(Message message) {
        this.binder().addError(message);
    }

    protected void requestInjection(Object object) {
        this.binder().requestInjection(object);
    }

    protected /* varargs */ void requestStaticInjection(Class<?> ... arrclass) {
        this.binder().requestStaticInjection(arrclass);
    }

    protected /* varargs */ void bindInterceptor(Matcher<? super Class<?>> matcher, Matcher<? super Method> matcher2, MethodInterceptor ... arrmethodInterceptor) {
        this.binder().bindInterceptor(matcher, matcher2, arrmethodInterceptor);
    }

    protected void requireBinding(Key<?> key) {
        this.binder().getProvider(key);
    }

    protected void requireBinding(Class<?> class_) {
        this.binder().getProvider(class_);
    }

    protected <T> Provider<T> getProvider(Key<T> key) {
        return this.binder().getProvider(key);
    }

    protected <T> Provider<T> getProvider(Class<T> class_) {
        return this.binder().getProvider(class_);
    }

    protected void convertToTypes(Matcher<? super TypeLiteral<?>> matcher, TypeConverter typeConverter) {
        this.binder().convertToTypes(matcher, typeConverter);
    }

    protected Stage currentStage() {
        return this.binder().currentStage();
    }

    protected <T> MembersInjector<T> getMembersInjector(Class<T> class_) {
        return this.binder().getMembersInjector(class_);
    }

    protected <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
        return this.binder().getMembersInjector(typeLiteral);
    }

    protected void bindListener(Matcher<? super TypeLiteral<?>> matcher, TypeListener typeListener) {
        this.binder().bindListener(matcher, typeListener);
    }

    protected /* varargs */ void bindListener(Matcher<? super Binding<?>> matcher, ProvisionListener ... arrprovisionListener) {
        this.binder().bindListener(matcher, arrprovisionListener);
    }
}

