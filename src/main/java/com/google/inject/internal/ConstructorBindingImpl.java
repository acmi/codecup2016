/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.internal.Annotations;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.ConstructionProxy;
import com.google.inject.internal.ConstructorInjector;
import com.google.inject.internal.ConstructorInjectorStore;
import com.google.inject.internal.DefaultConstructionProxyFactory;
import com.google.inject.internal.DelayedInitialize;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.InternalFactory;
import com.google.inject.internal.MembersInjectorImpl;
import com.google.inject.internal.ProvisionListenerCallbackStore;
import com.google.inject.internal.ProvisionListenerStackCallback;
import com.google.inject.internal.Scoping;
import com.google.inject.internal.SingleParameterInjector;
import com.google.inject.internal.util.Classes;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.ConstructorBinding;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.Message;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aopalliance.intercept.MethodInterceptor;

final class ConstructorBindingImpl<T>
extends BindingImpl<T>
implements DelayedInitialize,
ConstructorBinding<T> {
    private final Factory<T> factory;
    private final InjectionPoint constructorInjectionPoint;

    private ConstructorBindingImpl(InjectorImpl injectorImpl, Key<T> key, Object object, InternalFactory<? extends T> internalFactory, Scoping scoping, Factory<T> factory, InjectionPoint injectionPoint) {
        super(injectorImpl, key, object, internalFactory, scoping);
        this.factory = factory;
        this.constructorInjectionPoint = injectionPoint;
    }

    public ConstructorBindingImpl(Key<T> key, Object object, Scoping scoping, InjectionPoint injectionPoint, Set<InjectionPoint> set) {
        super(object, key, scoping);
        this.factory = new Factory(false, key);
        ConstructionProxy constructionProxy = new DefaultConstructionProxyFactory(injectionPoint).create();
        this.constructorInjectionPoint = injectionPoint;
        this.factory.constructorInjector = new ConstructorInjector(set, constructionProxy, null, null);
    }

    static <T> ConstructorBindingImpl<T> create(InjectorImpl injectorImpl, Key<T> key, InjectionPoint injectionPoint, Object object, Scoping scoping, Errors errors, boolean bl, boolean bl2) throws ErrorsException {
        Class class_;
        Object object2;
        Object object3;
        int n2 = errors.size();
        Class class_2 = class_ = injectionPoint == null ? key.getTypeLiteral().getRawType() : injectionPoint.getDeclaringType().getRawType();
        if (Modifier.isAbstract(class_.getModifiers())) {
            errors.missingImplementation(key);
        }
        if (Classes.isInnerClass(class_)) {
            errors.cannotInjectInnerClass(class_);
        }
        errors.throwIfNewErrors(n2);
        if (injectionPoint == null) {
            try {
                injectionPoint = InjectionPoint.forConstructorOf(key.getTypeLiteral());
                if (bl2 && !ConstructorBindingImpl.hasAtInject((Constructor)injectionPoint.getMember())) {
                    errors.atInjectRequired(class_);
                }
            }
            catch (ConfigurationException configurationException) {
                throw errors.merge(configurationException.getErrorMessages()).toException();
            }
        }
        if (!scoping.isExplicitlyScoped() && (object3 = Annotations.findScopeAnnotation(errors, object2 = injectionPoint.getMember().getDeclaringClass())) != null) {
            scoping = Scoping.makeInjectable(Scoping.forAnnotation(object3), injectorImpl, errors.withSource(class_));
        }
        errors.throwIfNewErrors(n2);
        object2 = new Factory(bl, key);
        object3 = Scoping.scope(key, injectorImpl, object2, object, scoping);
        return new ConstructorBindingImpl<T>(injectorImpl, key, object, (InternalFactory<T>)object3, scoping, (Factory<T>)object2, injectionPoint);
    }

    private static boolean hasAtInject(Constructor constructor) {
        return constructor.isAnnotationPresent(Inject.class) || constructor.isAnnotationPresent(javax.inject.Inject.class);
    }

    @Override
    public void initialize(InjectorImpl injectorImpl, Errors errors) throws ErrorsException {
        this.factory.constructorInjector = injectorImpl.constructors.get(this.constructorInjectionPoint, errors);
        this.factory.provisionCallback = injectorImpl.provisionListenerStore.get(this);
    }

    boolean isInitialized() {
        return this.factory.constructorInjector != null;
    }

    InjectionPoint getInternalConstructor() {
        if (this.factory.constructorInjector != null) {
            return this.factory.constructorInjector.getConstructionProxy().getInjectionPoint();
        }
        return this.constructorInjectionPoint;
    }

    Set<Dependency<?>> getInternalDependencies() {
        ImmutableSet.Builder builder = ImmutableSet.builder();
        if (this.factory.constructorInjector == null) {
            builder.add(this.constructorInjectionPoint);
            try {
                builder.addAll(InjectionPoint.forInstanceMethodsAndFields(this.constructorInjectionPoint.getDeclaringType()));
            }
            catch (ConfigurationException configurationException) {}
        } else {
            builder.add(this.getConstructor()).addAll(this.getInjectableMembers());
        }
        return Dependency.forInjectionPoints(builder.build());
    }

    @Override
    public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> bindingTargetVisitor) {
        Preconditions.checkState(this.factory.constructorInjector != null, "not initialized");
        return bindingTargetVisitor.visit(this);
    }

    @Override
    public InjectionPoint getConstructor() {
        Preconditions.checkState(this.factory.constructorInjector != null, "Binding is not ready");
        return this.factory.constructorInjector.getConstructionProxy().getInjectionPoint();
    }

    @Override
    public Set<InjectionPoint> getInjectableMembers() {
        Preconditions.checkState(this.factory.constructorInjector != null, "Binding is not ready");
        return this.factory.constructorInjector.getInjectableMembers();
    }

    @Override
    public Map<Method, List<MethodInterceptor>> getMethodInterceptors() {
        Preconditions.checkState(this.factory.constructorInjector != null, "Binding is not ready");
        return this.factory.constructorInjector.getConstructionProxy().getMethodInterceptors();
    }

    @Override
    public Set<Dependency<?>> getDependencies() {
        return Dependency.forInjectionPoints(new ImmutableSet.Builder().add(this.getConstructor()).addAll(this.getInjectableMembers()).build());
    }

    @Override
    protected BindingImpl<T> withScoping(Scoping scoping) {
        return new ConstructorBindingImpl(null, this.getKey(), this.getSource(), this.factory, scoping, this.factory, this.constructorInjectionPoint);
    }

    @Override
    protected BindingImpl<T> withKey(Key<T> key) {
        return new ConstructorBindingImpl<T>(null, key, this.getSource(), this.factory, this.getScoping(), this.factory, this.constructorInjectionPoint);
    }

    @Override
    public void applyTo(Binder binder) {
        InjectionPoint injectionPoint = this.getConstructor();
        this.getScoping().applyTo(binder.withSource(this.getSource()).bind(this.getKey()).toConstructor((Constructor)this.getConstructor().getMember(), injectionPoint.getDeclaringType()));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(ConstructorBinding.class).add("key", this.getKey()).add("source", this.getSource()).add("scope", this.getScoping()).toString();
    }

    public boolean equals(Object object) {
        if (object instanceof ConstructorBindingImpl) {
            ConstructorBindingImpl constructorBindingImpl = (ConstructorBindingImpl)object;
            return this.getKey().equals(constructorBindingImpl.getKey()) && this.getScoping().equals(constructorBindingImpl.getScoping()) && Objects.equal(this.constructorInjectionPoint, constructorBindingImpl.constructorInjectionPoint);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(this.getKey(), this.getScoping(), this.constructorInjectionPoint);
    }

    private static class Factory<T>
    implements InternalFactory<T> {
        private final boolean failIfNotLinked;
        private final Key<?> key;
        private ConstructorInjector<T> constructorInjector;
        private ProvisionListenerStackCallback<T> provisionCallback;

        Factory(boolean bl, Key<?> key) {
            this.failIfNotLinked = bl;
            this.key = key;
        }

        @Override
        public T get(Errors errors, InternalContext internalContext, Dependency<?> dependency, boolean bl) throws ErrorsException {
            Preconditions.checkState(this.constructorInjector != null, "Constructor not ready");
            if (this.failIfNotLinked && !bl) {
                throw errors.jitDisabled(this.key).toException();
            }
            return (T)this.constructorInjector.construct(errors, internalContext, dependency.getKey().getTypeLiteral().getRawType(), this.provisionCallback);
        }
    }

}

