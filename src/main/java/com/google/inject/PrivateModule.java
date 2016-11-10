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
import com.google.inject.PrivateBinder;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.AnnotatedConstantBindingBuilder;
import com.google.inject.binder.AnnotatedElementBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.Message;
import com.google.inject.spi.ProvisionListener;
import com.google.inject.spi.TypeConverter;
import com.google.inject.spi.TypeListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInterceptor;

public abstract class PrivateModule
implements Module {
    private PrivateBinder binder;

    @Override
    public final synchronized void configure(Binder binder) {
        Preconditions.checkState(this.binder == null, "Re-entry is not allowed.");
        this.binder = (PrivateBinder)binder.skipSources(PrivateModule.class);
        try {
            this.configure();
        }
        finally {
            this.binder = null;
        }
    }

    protected abstract void configure();

    protected final <T> void expose(Key<T> key) {
        this.binder().expose(key);
    }

    protected final AnnotatedElementBuilder expose(Class<?> class_) {
        return this.binder().expose(class_);
    }

    protected final AnnotatedElementBuilder expose(TypeLiteral<?> typeLiteral) {
        return this.binder().expose(typeLiteral);
    }

    protected final PrivateBinder binder() {
        Preconditions.checkState(this.binder != null, "The binder can only be used inside configure()");
        return this.binder;
    }

    protected final void bindScope(Class<? extends Annotation> class_, Scope scope) {
        this.binder().bindScope(class_, scope);
    }

    protected final <T> LinkedBindingBuilder<T> bind(Key<T> key) {
        return this.binder().bind(key);
    }

    protected final <T> AnnotatedBindingBuilder<T> bind(TypeLiteral<T> typeLiteral) {
        return this.binder().bind(typeLiteral);
    }

    protected final <T> AnnotatedBindingBuilder<T> bind(Class<T> class_) {
        return this.binder().bind(class_);
    }

    protected final AnnotatedConstantBindingBuilder bindConstant() {
        return this.binder().bindConstant();
    }

    protected final void install(Module module) {
        this.binder().install(module);
    }

    protected final /* varargs */ void addError(String string, Object ... arrobject) {
        this.binder().addError(string, arrobject);
    }

    protected final void addError(Throwable throwable) {
        this.binder().addError(throwable);
    }

    protected final void addError(Message message) {
        this.binder().addError(message);
    }

    protected final void requestInjection(Object object) {
        this.binder().requestInjection(object);
    }

    protected final /* varargs */ void requestStaticInjection(Class<?> ... arrclass) {
        this.binder().requestStaticInjection(arrclass);
    }

    protected final /* varargs */ void bindInterceptor(Matcher<? super Class<?>> matcher, Matcher<? super Method> matcher2, MethodInterceptor ... arrmethodInterceptor) {
        this.binder().bindInterceptor(matcher, matcher2, arrmethodInterceptor);
    }

    protected final void requireBinding(Key<?> key) {
        this.binder().getProvider(key);
    }

    protected final void requireBinding(Class<?> class_) {
        this.binder().getProvider(class_);
    }

    protected final <T> Provider<T> getProvider(Key<T> key) {
        return this.binder().getProvider(key);
    }

    protected final <T> Provider<T> getProvider(Class<T> class_) {
        return this.binder().getProvider(class_);
    }

    protected final void convertToTypes(Matcher<? super TypeLiteral<?>> matcher, TypeConverter typeConverter) {
        this.binder().convertToTypes(matcher, typeConverter);
    }

    protected final Stage currentStage() {
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

