/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.Exposed;
import com.google.inject.Key;
import com.google.inject.PrivateBinder;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.BytecodeGen;
import com.google.inject.internal.ConstructionContext;
import com.google.inject.internal.DelayedInitialize;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.Exceptions;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.InternalFactory;
import com.google.inject.internal.ProviderInstanceBindingImpl;
import com.google.inject.internal.ProvisionListenerCallbackStore;
import com.google.inject.internal.ProvisionListenerStackCallback;
import com.google.inject.internal.Scoping;
import com.google.inject.internal.SingleParameterInjector;
import com.google.inject.internal.cglib.core.$CodeGenerationException;
import com.google.inject.internal.cglib.reflect.$FastClass;
import com.google.inject.internal.cglib.reflect.$FastMethod;
import com.google.inject.internal.util.StackTraceElements;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.ProviderInstanceBinding;
import com.google.inject.spi.ProviderWithExtensionVisitor;
import com.google.inject.spi.ProvidesMethodBinding;
import com.google.inject.spi.ProvidesMethodTargetVisitor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

public abstract class ProviderMethod<T>
implements HasDependencies,
ProviderWithExtensionVisitor<T>,
ProvidesMethodBinding<T> {
    protected final Object instance;
    protected final Method method;
    private final Key<T> key;
    private final Class<? extends Annotation> scopeAnnotation;
    private final ImmutableSet<Dependency<?>> dependencies;
    private final List<Provider<?>> parameterProviders;
    private final boolean exposed;
    private final Annotation annotation;

    static <T> ProviderMethod<T> create(Key<T> key, Method method, Object object, ImmutableSet<Dependency<?>> immutableSet, List<Provider<?>> list, Class<? extends Annotation> class_, boolean bl, Annotation annotation) {
        int n2 = method.getModifiers();
        if (!bl) {
            try {
                $FastClass $FastClass = BytecodeGen.newFastClassForMember(method);
                if ($FastClass != null) {
                    return new FastClassProviderMethod<T>(key, $FastClass, method, object, immutableSet, list, class_, annotation);
                }
            }
            catch ($CodeGenerationException $CodeGenerationException) {
                // empty catch block
            }
        }
        if (!Modifier.isPublic(n2) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
            method.setAccessible(true);
        }
        return new ReflectionProviderMethod<T>(key, method, object, immutableSet, list, class_, annotation);
    }

    private ProviderMethod(Key<T> key, Method method, Object object, ImmutableSet<Dependency<?>> immutableSet, List<Provider<?>> list, Class<? extends Annotation> class_, Annotation annotation) {
        this.key = key;
        this.scopeAnnotation = class_;
        this.instance = object;
        this.dependencies = immutableSet;
        this.method = method;
        this.parameterProviders = list;
        this.exposed = method.isAnnotationPresent(Exposed.class);
        this.annotation = annotation;
    }

    @Override
    public Key<T> getKey() {
        return this.key;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    public Object getInstance() {
        return this.instance;
    }

    @Override
    public Object getEnclosingInstance() {
        return this.instance;
    }

    @Override
    public Annotation getAnnotation() {
        return this.annotation;
    }

    public void configure(Binder binder) {
        binder = binder.withSource(this.method);
        if (this.scopeAnnotation != null) {
            binder.bind(this.key).toProvider(this).in(this.scopeAnnotation);
        } else {
            binder.bind(this.key).toProvider(this);
        }
        if (this.exposed) {
            ((PrivateBinder)binder).expose(this.key);
        }
    }

    @Override
    public T get() {
        Object[] arrobject = new Object[this.parameterProviders.size()];
        for (int i2 = 0; i2 < arrobject.length; ++i2) {
            arrobject[i2] = this.parameterProviders.get(i2).get();
        }
        try {
            return this.doProvision(arrobject);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new AssertionError(illegalAccessException);
        }
        catch (InvocationTargetException invocationTargetException) {
            throw Exceptions.rethrowCause(invocationTargetException);
        }
    }

    abstract T doProvision(Object[] var1) throws IllegalAccessException, InvocationTargetException;

    @Override
    public Set<Dependency<?>> getDependencies() {
        return this.dependencies;
    }

    @Override
    public <B, V> V acceptExtensionVisitor(BindingTargetVisitor<B, V> bindingTargetVisitor, ProviderInstanceBinding<? extends B> providerInstanceBinding) {
        if (bindingTargetVisitor instanceof ProvidesMethodTargetVisitor) {
            return ((ProvidesMethodTargetVisitor)bindingTargetVisitor).visit(this);
        }
        return bindingTargetVisitor.visit(providerInstanceBinding);
    }

    public String toString() {
        String string = this.annotation.toString();
        if (this.annotation.annotationType() == Provides.class) {
            string = "@Provides";
        } else if (string.endsWith("()")) {
            string = string.substring(0, string.length() - 2);
        }
        return string + " " + StackTraceElements.forMember(this.method);
    }

    public boolean equals(Object object) {
        if (object instanceof ProviderMethod) {
            ProviderMethod providerMethod = (ProviderMethod)object;
            return this.method.equals(providerMethod.method) && this.instance.equals(providerMethod.instance) && this.annotation.equals(providerMethod.annotation);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(this.method, this.annotation);
    }

    static <T> BindingImpl<T> createBinding(InjectorImpl injectorImpl, Key<T> key, ProviderMethod<T> providerMethod, Object object, Scoping scoping) {
        Factory<T> factory = new Factory<T>(object, providerMethod);
        InternalFactory<T> internalFactory = Scoping.scope(key, injectorImpl, factory, object, scoping);
        return new ProviderMethodProviderInstanceBindingImpl<T>(injectorImpl, key, object, internalFactory, scoping, providerMethod, factory);
    }

    private static final class Factory<T>
    implements InternalFactory<T> {
        private final Object source;
        private final ProviderMethod<T> providerMethod;
        private ProvisionListenerStackCallback<T> provisionCallback;
        private SingleParameterInjector<?>[] parameterInjectors;

        Factory(Object object, ProviderMethod<T> providerMethod) {
            this.source = object;
            this.providerMethod = providerMethod;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public T get(final Errors errors, final InternalContext internalContext, final Dependency<?> dependency, boolean bl) throws ErrorsException {
            final ConstructionContext constructionContext = internalContext.getConstructionContext(this);
            if (constructionContext.isConstructing()) {
                Class class_ = dependency.getKey().getTypeLiteral().getRawType();
                Object object = constructionContext.createProxy(errors, internalContext.getInjectorOptions(), class_);
                return (T)object;
            }
            constructionContext.startConstruction();
            try {
                if (!this.provisionCallback.hasListeners()) {
                    Object t2 = this.provision(errors, dependency, internalContext, constructionContext);
                    return t2;
                }
                T t3 = this.provisionCallback.provision(errors, internalContext, new ProvisionListenerStackCallback.ProvisionCallback<T>(){

                    @Override
                    public T call() throws ErrorsException {
                        return Factory.this.provision(errors, dependency, internalContext, constructionContext);
                    }
                });
                return t3;
            }
            finally {
                constructionContext.removeCurrentReference();
                constructionContext.finishConstruction();
            }
        }

        T provision(Errors errors, Dependency<?> dependency, InternalContext internalContext, ConstructionContext<T> constructionContext) throws ErrorsException {
            try {
                T t2 = this.providerMethod.doProvision(SingleParameterInjector.getAll(errors, internalContext, this.parameterInjectors));
                errors.checkForNull(t2, this.providerMethod.getMethod(), dependency);
                constructionContext.setProxyDelegates(t2);
                return t2;
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new AssertionError(illegalAccessException);
            }
            catch (InvocationTargetException invocationTargetException /* !! */ ) {
                Throwable throwable = invocationTargetException /* !! */ .getCause() != null ? invocationTargetException /* !! */ .getCause() : invocationTargetException /* !! */ ;
                throw errors.withSource(this.source).errorInProvider(throwable).toException();
            }
        }

        public String toString() {
            return this.providerMethod.toString();
        }

    }

    private static final class ProviderMethodProviderInstanceBindingImpl<T>
    extends ProviderInstanceBindingImpl<T>
    implements DelayedInitialize {
        final Factory<T> factory;

        ProviderMethodProviderInstanceBindingImpl(InjectorImpl injectorImpl, Key<T> key, Object object, InternalFactory<? extends T> internalFactory, Scoping scoping, ProviderMethod<T> providerMethod, Factory<T> factory) {
            super(injectorImpl, key, object, internalFactory, scoping, providerMethod, ImmutableSet.<InjectionPoint>of());
            this.factory = factory;
        }

        @Override
        public void initialize(InjectorImpl injectorImpl, Errors errors) throws ErrorsException {
            this.factory.parameterInjectors = injectorImpl.getParametersInjectors(this.factory.providerMethod.dependencies.asList(), errors);
            this.factory.provisionCallback = injectorImpl.provisionListenerStore.get(this);
        }
    }

    private static final class ReflectionProviderMethod<T>
    extends ProviderMethod<T> {
        ReflectionProviderMethod(Key<T> key, Method method, Object object, ImmutableSet<Dependency<?>> immutableSet, List<Provider<?>> list, Class<? extends Annotation> class_, Annotation annotation) {
            super(key, method, object, immutableSet, list, class_, annotation);
        }

        @Override
        T doProvision(Object[] arrobject) throws IllegalAccessException, InvocationTargetException {
            return (T)this.method.invoke(this.instance, arrobject);
        }
    }

    private static final class FastClassProviderMethod<T>
    extends ProviderMethod<T> {
        final $FastClass fastClass;
        final int methodIndex;

        FastClassProviderMethod(Key<T> key, $FastClass $FastClass, Method method, Object object, ImmutableSet<Dependency<?>> immutableSet, List<Provider<?>> list, Class<? extends Annotation> class_, Annotation annotation) {
            super(key, method, object, immutableSet, list, class_, annotation);
            this.fastClass = $FastClass;
            this.methodIndex = $FastClass.getMethod(method).getIndex();
        }

        @Override
        public T doProvision(Object[] arrobject) throws IllegalAccessException, InvocationTargetException {
            return (T)this.fastClass.invoke(this.methodIndex, this.instance, arrobject);
        }
    }

}

