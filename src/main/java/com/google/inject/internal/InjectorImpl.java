/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.ConfigurationException;
import com.google.inject.ImplementedBy;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Module;
import com.google.inject.ProvidedBy;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.google.inject.Scope;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.Annotations;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.ConstantFactory;
import com.google.inject.internal.ConstructorBindingImpl;
import com.google.inject.internal.ConstructorInjectorStore;
import com.google.inject.internal.ContextualCallable;
import com.google.inject.internal.DeferredLookups;
import com.google.inject.internal.DelayedInitialize;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.Initializable;
import com.google.inject.internal.Initializables;
import com.google.inject.internal.InstanceBindingImpl;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.InternalFactory;
import com.google.inject.internal.InternalInjectorCreator;
import com.google.inject.internal.LinkedBindingImpl;
import com.google.inject.internal.LinkedProviderBindingImpl;
import com.google.inject.internal.Lookups;
import com.google.inject.internal.MembersInjectorImpl;
import com.google.inject.internal.MembersInjectorStore;
import com.google.inject.internal.MoreTypes;
import com.google.inject.internal.ProvidedByInternalFactory;
import com.google.inject.internal.ProvisionListenerCallbackStore;
import com.google.inject.internal.ProvisionListenerStackCallback;
import com.google.inject.internal.Scoping;
import com.google.inject.internal.SingleParameterInjector;
import com.google.inject.internal.State;
import com.google.inject.internal.util.SourceProvider;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.ConvertedConstantBinding;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.Message;
import com.google.inject.spi.ProviderBinding;
import com.google.inject.spi.TypeConverter;
import com.google.inject.spi.TypeConverterBinding;
import com.google.inject.util.Providers;
import java.lang.annotation.Annotation;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class InjectorImpl
implements Injector,
Lookups {
    public static final TypeLiteral<String> STRING_TYPE = TypeLiteral.get(String.class);
    final State state;
    final InjectorImpl parent;
    final BindingsMultimap bindingsMultimap = new BindingsMultimap();
    final InjectorOptions options;
    final Map<Key<?>, BindingImpl<?>> jitBindings = Maps.newHashMap();
    final Set<Key<?>> failedJitBindings = Sets.newHashSet();
    Lookups lookups;
    final ConstructorInjectorStore constructors;
    MembersInjectorStore membersInjectorStore;
    ProvisionListenerCallbackStore provisionListenerStore;
    private final ThreadLocal<Object[]> localContext;

    InjectorImpl(InjectorImpl injectorImpl, State state, InjectorOptions injectorOptions) {
        this.lookups = new DeferredLookups(this);
        this.constructors = new ConstructorInjectorStore(this);
        this.parent = injectorImpl;
        this.state = state;
        this.options = injectorOptions;
        this.localContext = injectorImpl != null ? injectorImpl.localContext : new ThreadLocal();
    }

    void index() {
        for (Binding binding : this.state.getExplicitBindingsThisLevel().values()) {
            this.index(binding);
        }
    }

    <T> void index(Binding<T> binding) {
        this.bindingsMultimap.put(binding.getKey().getTypeLiteral(), binding);
    }

    @Override
    public <T> List<Binding<T>> findBindingsByType(TypeLiteral<T> typeLiteral) {
        return this.bindingsMultimap.getAll(typeLiteral);
    }

    public <T> BindingImpl<T> getBinding(Key<T> key) {
        Errors errors = new Errors(key);
        try {
            BindingImpl<T> bindingImpl = this.getBindingOrThrow(key, errors, JitLimitation.EXISTING_JIT);
            errors.throwConfigurationExceptionIfErrorsExist();
            return bindingImpl;
        }
        catch (ErrorsException errorsException) {
            throw new ConfigurationException(errors.merge(errorsException.getErrors()).getMessages());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public <T> BindingImpl<T> getExistingBinding(Key<T> key) {
        BindingImpl<T> bindingImpl = this.state.getExplicitBinding(key);
        if (bindingImpl != null) {
            return bindingImpl;
        }
        Key<T> key2 = this.state.lock();
        synchronized (key2) {
            InjectorImpl injectorImpl = this;
            while (injectorImpl != null) {
                BindingImpl bindingImpl2 = injectorImpl.jitBindings.get(key);
                if (bindingImpl2 != null) {
                    return bindingImpl2;
                }
                injectorImpl = injectorImpl.parent;
            }
        }
        if (InjectorImpl.isProvider(key)) {
            try {
                key2 = InjectorImpl.getProvidedKey(key, new Errors());
                if (this.getExistingBinding((Key)key2) != null) {
                    return this.getBinding((Key)key);
                }
            }
            catch (ErrorsException errorsException) {
                throw new ConfigurationException(errorsException.getErrors().getMessages());
            }
        }
        return null;
    }

    <T> BindingImpl<T> getBindingOrThrow(Key<T> key, Errors errors, JitLimitation jitLimitation) throws ErrorsException {
        BindingImpl<T> bindingImpl = this.state.getExplicitBinding(key);
        if (bindingImpl != null) {
            return bindingImpl;
        }
        return this.getJustInTimeBinding(key, errors, jitLimitation);
    }

    @Override
    public <T> Binding<T> getBinding(Class<T> class_) {
        return this.getBinding((Key)Key.get(class_));
    }

    @Override
    public Injector getParent() {
        return this.parent;
    }

    @Override
    public Injector createChildInjector(Iterable<? extends Module> iterable) {
        return new InternalInjectorCreator().parentInjector(this).addModules(iterable).build();
    }

    @Override
    public /* varargs */ Injector createChildInjector(Module ... arrmodule) {
        return this.createChildInjector(ImmutableList.copyOf(arrmodule));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private <T> BindingImpl<T> getJustInTimeBinding(Key<T> key, Errors errors, JitLimitation jitLimitation) throws ErrorsException {
        boolean bl = InjectorImpl.isProvider(key) || InjectorImpl.isTypeLiteral(key) || InjectorImpl.isMembersInjector(key);
        Object object = this.state.lock();
        synchronized (object) {
            InjectorImpl injectorImpl = this;
            while (injectorImpl != null) {
                BindingImpl bindingImpl = injectorImpl.jitBindings.get(key);
                if (bindingImpl != null) {
                    if (this.options.jitDisabled && jitLimitation == JitLimitation.NO_JIT && !bl && !(bindingImpl instanceof ConvertedConstantBindingImpl)) {
                        throw errors.jitDisabled(key).toException();
                    }
                    return bindingImpl;
                }
                injectorImpl = injectorImpl.parent;
            }
            if (this.failedJitBindings.contains(key) && errors.hasErrors()) {
                throw errors.toException();
            }
            return this.createJustInTimeBindingRecursive(key, errors, this.options.jitDisabled, jitLimitation);
        }
    }

    private static boolean isProvider(Key<?> key) {
        return key.getTypeLiteral().getRawType().equals(Provider.class);
    }

    private static boolean isTypeLiteral(Key<?> key) {
        return key.getTypeLiteral().getRawType().equals(TypeLiteral.class);
    }

    private static <T> Key<T> getProvidedKey(Key<Provider<T>> key, Errors errors) throws ErrorsException {
        Type type = key.getTypeLiteral().getType();
        if (!(type instanceof ParameterizedType)) {
            throw errors.cannotInjectRawProvider().toException();
        }
        Type type2 = ((ParameterizedType)type).getActualTypeArguments()[0];
        Key key2 = key.ofType(type2);
        return key2;
    }

    private static boolean isMembersInjector(Key<?> key) {
        return key.getTypeLiteral().getRawType().equals(MembersInjector.class) && key.getAnnotationType() == null;
    }

    private <T> BindingImpl<MembersInjector<T>> createMembersInjectorBinding(Key<MembersInjector<T>> key, Errors errors) throws ErrorsException {
        Type type = key.getTypeLiteral().getType();
        if (!(type instanceof ParameterizedType)) {
            throw errors.cannotInjectRawMembersInjector().toException();
        }
        TypeLiteral typeLiteral = TypeLiteral.get(((ParameterizedType)type).getActualTypeArguments()[0]);
        MembersInjectorImpl membersInjectorImpl = this.membersInjectorStore.get(typeLiteral, errors);
        ConstantFactory constantFactory = new ConstantFactory(Initializables.of(membersInjectorImpl));
        return new InstanceBindingImpl<MembersInjector<T>>(this, key, SourceProvider.UNKNOWN_SOURCE, constantFactory, ImmutableSet.<InjectionPoint>of(), membersInjectorImpl);
    }

    private <T> BindingImpl<Provider<T>> createProviderBinding(Key<Provider<T>> key, Errors errors) throws ErrorsException {
        Key<T> key2 = InjectorImpl.getProvidedKey(key, errors);
        BindingImpl<T> bindingImpl = this.getBindingOrThrow(key2, errors, JitLimitation.NO_JIT);
        return new ProviderBindingImpl<T>(this, key, bindingImpl);
    }

    private <T> BindingImpl<T> convertConstantStringBinding(Key<T> key, Errors errors) throws ErrorsException {
        Key<String> key2 = key.ofType(STRING_TYPE);
        BindingImpl<String> bindingImpl = this.state.getExplicitBinding(key2);
        if (bindingImpl == null || !bindingImpl.isConstant()) {
            return null;
        }
        String string = bindingImpl.getProvider().get();
        Object object = bindingImpl.getSource();
        TypeLiteral<T> typeLiteral = key.getTypeLiteral();
        TypeConverterBinding typeConverterBinding = this.state.getConverter(string, typeLiteral, errors, object);
        if (typeConverterBinding == null) {
            return null;
        }
        try {
            Object object2 = typeConverterBinding.getTypeConverter().convert(string, typeLiteral);
            if (object2 == null) {
                throw errors.converterReturnedNull(string, object, typeLiteral, typeConverterBinding).toException();
            }
            if (!typeLiteral.getRawType().isInstance(object2)) {
                throw errors.conversionTypeError(string, object, typeLiteral, typeConverterBinding, object2).toException();
            }
            return new ConvertedConstantBindingImpl<Object>(this, key, object2, bindingImpl, typeConverterBinding);
        }
        catch (ErrorsException errorsException) {
            throw errorsException;
        }
        catch (RuntimeException runtimeException) {
            throw errors.conversionError(string, object, typeLiteral, typeConverterBinding, runtimeException).toException();
        }
    }

    <T> void initializeBinding(BindingImpl<T> bindingImpl, Errors errors) throws ErrorsException {
        if (bindingImpl instanceof DelayedInitialize) {
            ((DelayedInitialize)((Object)bindingImpl)).initialize(this, errors);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    <T> void initializeJitBinding(BindingImpl<T> bindingImpl, Errors errors) throws ErrorsException {
        if (bindingImpl instanceof DelayedInitialize) {
            Key<T> key = bindingImpl.getKey();
            this.jitBindings.put(key, bindingImpl);
            boolean bl = false;
            DelayedInitialize delayedInitialize = (DelayedInitialize)((Object)bindingImpl);
            try {
                delayedInitialize.initialize(this, errors);
                bl = true;
            }
            finally {
                if (!bl) {
                    this.removeFailedJitBinding(bindingImpl, null);
                    this.cleanup(bindingImpl, new HashSet<Key>());
                }
            }
        }
    }

    private boolean cleanup(BindingImpl<?> bindingImpl, Set<Key> set) {
        boolean bl = false;
        Set set2 = this.getInternalDependencies(bindingImpl);
        for (Dependency dependency : set2) {
            Key key = dependency.getKey();
            InjectionPoint injectionPoint = dependency.getInjectionPoint();
            if (!set.add(key)) continue;
            BindingImpl bindingImpl2 = this.jitBindings.get(key);
            if (bindingImpl2 != null) {
                boolean bl2 = this.cleanup(bindingImpl2, set);
                if (bindingImpl2 instanceof ConstructorBindingImpl) {
                    ConstructorBindingImpl constructorBindingImpl = (ConstructorBindingImpl)bindingImpl2;
                    injectionPoint = constructorBindingImpl.getInternalConstructor();
                    if (!constructorBindingImpl.isInitialized()) {
                        bl2 = true;
                    }
                }
                if (!bl2) continue;
                this.removeFailedJitBinding(bindingImpl2, injectionPoint);
                bl = true;
                continue;
            }
            if (this.state.getExplicitBinding(key) != null) continue;
            bl = true;
        }
        return bl;
    }

    private void removeFailedJitBinding(Binding<?> binding, InjectionPoint injectionPoint) {
        this.failedJitBindings.add(binding.getKey());
        this.jitBindings.remove(binding.getKey());
        this.membersInjectorStore.remove(binding.getKey().getTypeLiteral());
        this.provisionListenerStore.remove(binding);
        if (injectionPoint != null) {
            this.constructors.remove(injectionPoint);
        }
    }

    private Set<Dependency<?>> getInternalDependencies(BindingImpl<?> bindingImpl) {
        if (bindingImpl instanceof ConstructorBindingImpl) {
            return ((ConstructorBindingImpl)bindingImpl).getInternalDependencies();
        }
        if (bindingImpl instanceof HasDependencies) {
            return ((HasDependencies)((Object)bindingImpl)).getDependencies();
        }
        return ImmutableSet.of();
    }

    <T> BindingImpl<T> createUninitializedBinding(Key<T> key, Scoping scoping, Object object, Errors errors, boolean bl) throws ErrorsException {
        Class<T> class_ = key.getTypeLiteral().getRawType();
        ImplementedBy implementedBy = class_.getAnnotation(ImplementedBy.class);
        if (class_.isArray() || class_.isEnum() && implementedBy != null) {
            throw errors.missingImplementation(key).toException();
        }
        if (class_ == TypeLiteral.class) {
            BindingImpl<TypeLiteral<T>> bindingImpl = this.createTypeLiteralBinding(key, errors);
            return bindingImpl;
        }
        if (implementedBy != null) {
            Annotations.checkForMisplacedScopeAnnotations(class_, object, errors);
            return this.createImplementedByBinding(key, scoping, implementedBy, errors);
        }
        ProvidedBy providedBy = class_.getAnnotation(ProvidedBy.class);
        if (providedBy != null) {
            Annotations.checkForMisplacedScopeAnnotations(class_, object, errors);
            return this.createProvidedByBinding(key, scoping, providedBy, errors);
        }
        return ConstructorBindingImpl.create(this, key, null, object, scoping, errors, bl && this.options.jitDisabled, this.options.atInjectRequired);
    }

    private <T> BindingImpl<TypeLiteral<T>> createTypeLiteralBinding(Key<TypeLiteral<T>> key, Errors errors) throws ErrorsException {
        Type type = key.getTypeLiteral().getType();
        if (!(type instanceof ParameterizedType)) {
            throw errors.cannotInjectRawTypeLiteral().toException();
        }
        ParameterizedType parameterizedType = (ParameterizedType)type;
        Type type2 = parameterizedType.getActualTypeArguments()[0];
        if (!(type2 instanceof Class || type2 instanceof GenericArrayType || type2 instanceof ParameterizedType)) {
            throw errors.cannotInjectTypeLiteralOf(type2).toException();
        }
        TypeLiteral typeLiteral = TypeLiteral.get(type2);
        ConstantFactory constantFactory = new ConstantFactory(Initializables.of(typeLiteral));
        return new InstanceBindingImpl<TypeLiteral<T>>(this, key, SourceProvider.UNKNOWN_SOURCE, constantFactory, ImmutableSet.<InjectionPoint>of(), typeLiteral);
    }

    <T> BindingImpl<T> createProvidedByBinding(Key<T> key, Scoping scoping, ProvidedBy providedBy, Errors errors) throws ErrorsException {
        Class<T> class_ = key.getTypeLiteral().getRawType();
        Class class_2 = providedBy.value();
        if (class_2 == class_) {
            throw errors.recursiveProviderType().toException();
        }
        Key key2 = Key.get(class_2);
        ProvidedByInternalFactory<T> providedByInternalFactory = new ProvidedByInternalFactory<T>(class_, class_2, key2);
        Class<T> class_3 = class_;
        LinkedProviderBindingImpl<T> linkedProviderBindingImpl = LinkedProviderBindingImpl.createWithInitializer(this, key, class_3, Scoping.scope(key, this, providedByInternalFactory, class_3, scoping), scoping, key2, providedByInternalFactory);
        providedByInternalFactory.setProvisionListenerCallback(this.provisionListenerStore.get(linkedProviderBindingImpl));
        return linkedProviderBindingImpl;
    }

    private <T> BindingImpl<T> createImplementedByBinding(Key<T> key, Scoping scoping, ImplementedBy implementedBy, Errors errors) throws ErrorsException {
        Class<T> class_ = key.getTypeLiteral().getRawType();
        Class class_2 = implementedBy.value();
        if (class_2 == class_) {
            throw errors.recursiveImplementationType().toException();
        }
        if (!class_.isAssignableFrom(class_2)) {
            throw errors.notASubtype(class_2, class_).toException();
        }
        Class class_3 = class_2;
        final Key key2 = Key.get(class_3);
        final BindingImpl bindingImpl = this.getBindingOrThrow(key2, errors, JitLimitation.NEW_OR_EXISTING_JIT);
        InternalFactory internalFactory = new InternalFactory<T>(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public T get(Errors errors, InternalContext internalContext, Dependency<?> dependency, boolean bl) throws ErrorsException {
                internalContext.pushState(key2, bindingImpl.getSource());
                try {
                    Object t2 = bindingImpl.getInternalFactory().get(errors.withSource(key2), internalContext, dependency, true);
                    return t2;
                }
                finally {
                    internalContext.popState();
                }
            }
        };
        Class<T> class_4 = class_;
        return new LinkedBindingImpl<T>(this, key, class_4, Scoping.scope(key, this, internalFactory, class_4, scoping), scoping, key2);
    }

    private <T> BindingImpl<T> createJustInTimeBindingRecursive(Key<T> key, Errors errors, boolean bl, JitLimitation jitLimitation) throws ErrorsException {
        if (this.parent != null) {
            if (jitLimitation == JitLimitation.NEW_OR_EXISTING_JIT && bl && !this.parent.options.jitDisabled) {
                throw errors.jitDisabledInParent(key).toException();
            }
            try {
                return this.parent.createJustInTimeBindingRecursive(key, new Errors(), bl, this.parent.options.jitDisabled ? JitLimitation.NO_JIT : jitLimitation);
            }
            catch (ErrorsException errorsException) {
                // empty catch block
            }
        }
        Set<Object> set = this.state.getSourcesForBlacklistedKey(key);
        if (this.state.isBlacklisted(key)) {
            throw errors.childBindingAlreadySet(key, set).toException();
        }
        key = MoreTypes.canonicalizeKey(key);
        BindingImpl<T> bindingImpl = this.createJustInTimeBinding(key, errors, bl, jitLimitation);
        this.state.parent().blacklist(key, this.state, bindingImpl.getSource());
        this.jitBindings.put(key, bindingImpl);
        return bindingImpl;
    }

    private <T> BindingImpl<T> createJustInTimeBinding(Key<T> key, Errors errors, boolean bl, JitLimitation jitLimitation) throws ErrorsException {
        int n2 = errors.size();
        Set<Object> set = this.state.getSourcesForBlacklistedKey(key);
        if (this.state.isBlacklisted(key)) {
            throw errors.childBindingAlreadySet(key, set).toException();
        }
        if (InjectorImpl.isProvider(key)) {
            BindingImpl<Provider<T>> bindingImpl = this.createProviderBinding(key, errors);
            return bindingImpl;
        }
        if (InjectorImpl.isMembersInjector(key)) {
            BindingImpl<MembersInjector<T>> bindingImpl = this.createMembersInjectorBinding(key, errors);
            return bindingImpl;
        }
        BindingImpl<Provider<T>> bindingImpl = this.convertConstantStringBinding(key, errors);
        if (bindingImpl != null) {
            return bindingImpl;
        }
        if (!InjectorImpl.isTypeLiteral(key) && bl && jitLimitation != JitLimitation.NEW_OR_EXISTING_JIT) {
            throw errors.jitDisabled(key).toException();
        }
        if (key.getAnnotationType() != null) {
            if (key.hasAttributes() && !this.options.exactBindingAnnotationsRequired) {
                try {
                    Errors errors2 = new Errors();
                    return this.getBindingOrThrow(key.withoutAttributes(), errors2, JitLimitation.NO_JIT);
                }
                catch (ErrorsException errorsException) {
                    // empty catch block
                }
            }
            throw errors.missingImplementation(key).toException();
        }
        Class<Provider<T>> class_ = key.getTypeLiteral().getRawType();
        BindingImpl<Provider<T>> bindingImpl2 = this.createUninitializedBinding(key, Scoping.UNSCOPED, class_, errors, true);
        errors.throwIfNewErrors(n2);
        this.initializeJitBinding(bindingImpl2, errors);
        return bindingImpl2;
    }

    <T> InternalFactory<? extends T> getInternalFactory(Key<T> key, Errors errors, JitLimitation jitLimitation) throws ErrorsException {
        return this.getBindingOrThrow(key, errors, jitLimitation).getInternalFactory();
    }

    @Override
    public Map<Key<?>, Binding<?>> getBindings() {
        return this.state.getExplicitBindingsThisLevel();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Map<Key<?>, Binding<?>> getAllBindings() {
        Object object = this.state.lock();
        synchronized (object) {
            return new ImmutableMap.Builder().putAll(this.state.getExplicitBindingsThisLevel()).putAll(this.jitBindings).build();
        }
    }

    @Override
    public Map<Class<? extends Annotation>, Scope> getScopeBindings() {
        return ImmutableMap.copyOf(this.state.getScopes());
    }

    @Override
    public Set<TypeConverterBinding> getTypeConverterBindings() {
        return ImmutableSet.copyOf(this.state.getConvertersThisLevel());
    }

    SingleParameterInjector<?>[] getParametersInjectors(List<Dependency<?>> list, Errors errors) throws ErrorsException {
        if (list.isEmpty()) {
            return null;
        }
        int n2 = errors.size();
        SingleParameterInjector[] arrsingleParameterInjector = new SingleParameterInjector[list.size()];
        int n3 = 0;
        for (Dependency dependency : list) {
            try {
                arrsingleParameterInjector[n3++] = this.createParameterInjector(dependency, errors.withSource(dependency));
            }
            catch (ErrorsException errorsException) {}
        }
        errors.throwIfNewErrors(n2);
        return arrsingleParameterInjector;
    }

    <T> SingleParameterInjector<T> createParameterInjector(Dependency<T> dependency, Errors errors) throws ErrorsException {
        BindingImpl<T> bindingImpl = this.getBindingOrThrow(dependency.getKey(), errors, JitLimitation.NO_JIT);
        return new SingleParameterInjector<T>(dependency, bindingImpl);
    }

    @Override
    public void injectMembers(Object object) {
        MembersInjector membersInjector = this.getMembersInjector(object.getClass());
        membersInjector.injectMembers((Object)object);
    }

    @Override
    public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
        Errors errors = new Errors(typeLiteral);
        try {
            return this.membersInjectorStore.get(typeLiteral, errors);
        }
        catch (ErrorsException errorsException) {
            throw new ConfigurationException(errors.merge(errorsException.getErrors()).getMessages());
        }
    }

    @Override
    public <T> MembersInjector<T> getMembersInjector(Class<T> class_) {
        return this.getMembersInjector(TypeLiteral.get(class_));
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> class_) {
        return this.getProvider(Key.get(class_));
    }

    <T> Provider<T> getProviderOrThrow(final Dependency<T> dependency, Errors errors) throws ErrorsException {
        Key<T> key = dependency.getKey();
        final BindingImpl<T> bindingImpl = this.getBindingOrThrow(key, errors, JitLimitation.NO_JIT);
        return new Provider<T>(){

            @Override
            public T get() {
                final Errors errors = new Errors(dependency);
                try {
                    Object t2 = InjectorImpl.this.callInContext(new ContextualCallable<T>(){

                        /*
                         * WARNING - Removed try catching itself - possible behaviour change.
                         */
                        @Override
                        public T call(InternalContext internalContext) throws ErrorsException {
                            Dependency dependency = internalContext.pushDependency(dependency, bindingImpl.getSource());
                            try {
                                Object t2 = bindingImpl.getInternalFactory().get(errors, internalContext, dependency, false);
                                return t2;
                            }
                            finally {
                                internalContext.popStateAndSetDependency(dependency);
                            }
                        }
                    });
                    errors.throwIfNewErrors(0);
                    return t2;
                }
                catch (ErrorsException errorsException) {
                    throw new ProvisionException(errors.merge(errorsException.getErrors()).getMessages());
                }
            }

            public String toString() {
                return bindingImpl.getInternalFactory().toString();
            }

        };
    }

    @Override
    public <T> Provider<T> getProvider(Key<T> key) {
        Errors errors = new Errors(key);
        try {
            Provider<T> provider = this.getProviderOrThrow(Dependency.get(key), errors);
            errors.throwIfNewErrors(0);
            return provider;
        }
        catch (ErrorsException errorsException) {
            throw new ConfigurationException(errors.merge(errorsException.getErrors()).getMessages());
        }
    }

    @Override
    public <T> T getInstance(Key<T> key) {
        return this.getProvider(key).get();
    }

    @Override
    public <T> T getInstance(Class<T> class_) {
        return this.getProvider(class_).get();
    }

    InternalContext getLocalContext() {
        return (InternalContext)this.localContext.get()[0];
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    <T> T callInContext(ContextualCallable<T> contextualCallable) throws ErrorsException {
        Object[] arrobject = this.localContext.get();
        if (arrobject == null) {
            arrobject = new Object[1];
            this.localContext.set(arrobject);
        }
        if (arrobject[0] == null) {
            arrobject[0] = new InternalContext(this.options);
            try {
                T t2 = contextualCallable.call((InternalContext)arrobject[0]);
                return t2;
            }
            finally {
                arrobject[0] = null;
            }
        }
        return contextualCallable.call((InternalContext)arrobject[0]);
    }

    public String toString() {
        return MoreObjects.toStringHelper(Injector.class).add("bindings", this.state.getExplicitBindingsThisLevel().values()).toString();
    }

    static interface MethodInvoker {
        public /* varargs */ Object invoke(Object var1, Object ... var2) throws IllegalAccessException, InvocationTargetException;
    }

    private static class BindingsMultimap {
        final Map<TypeLiteral<?>, List<Binding<?>>> multimap = Maps.newHashMap();

        private BindingsMultimap() {
        }

        <T> void put(TypeLiteral<T> typeLiteral, Binding<T> binding) {
            List list = this.multimap.get(typeLiteral);
            if (list == null) {
                list = Lists.newArrayList();
                this.multimap.put(typeLiteral, list);
            }
            list.add(binding);
        }

        <T> List<Binding<T>> getAll(TypeLiteral<T> typeLiteral) {
            List list = this.multimap.get(typeLiteral);
            return list != null ? Collections.unmodifiableList(this.multimap.get(typeLiteral)) : ImmutableList.of();
        }
    }

    private static class ConvertedConstantBindingImpl<T>
    extends BindingImpl<T>
    implements ConvertedConstantBinding<T> {
        final T value;
        final Provider<T> provider;
        final Binding<String> originalBinding;
        final TypeConverterBinding typeConverterBinding;

        ConvertedConstantBindingImpl(InjectorImpl injectorImpl, Key<T> key, T t2, Binding<String> binding, TypeConverterBinding typeConverterBinding) {
            super(injectorImpl, key, binding.getSource(), new ConstantFactory<T>(Initializables.of(t2)), Scoping.UNSCOPED);
            this.value = t2;
            this.provider = Providers.of(t2);
            this.originalBinding = binding;
            this.typeConverterBinding = typeConverterBinding;
        }

        @Override
        public Provider<T> getProvider() {
            return this.provider;
        }

        @Override
        public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> bindingTargetVisitor) {
            return bindingTargetVisitor.visit(this);
        }

        @Override
        public T getValue() {
            return this.value;
        }

        @Override
        public TypeConverterBinding getTypeConverterBinding() {
            return this.typeConverterBinding;
        }

        @Override
        public Key<String> getSourceKey() {
            return this.originalBinding.getKey();
        }

        @Override
        public Set<Dependency<?>> getDependencies() {
            return ImmutableSet.of(Dependency.get(this.getSourceKey()));
        }

        @Override
        public void applyTo(Binder binder) {
            throw new UnsupportedOperationException("This element represents a synthetic binding.");
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(ConvertedConstantBinding.class).add("key", this.getKey()).add("sourceKey", this.getSourceKey()).add("value", this.value).toString();
        }

        public boolean equals(Object object) {
            if (object instanceof ConvertedConstantBindingImpl) {
                ConvertedConstantBindingImpl convertedConstantBindingImpl = (ConvertedConstantBindingImpl)object;
                return this.getKey().equals(convertedConstantBindingImpl.getKey()) && this.getScoping().equals(convertedConstantBindingImpl.getScoping()) && Objects.equal(this.value, convertedConstantBindingImpl.value);
            }
            return false;
        }

        public int hashCode() {
            return Objects.hashCode(this.getKey(), this.getScoping(), this.value);
        }
    }

    private static class ProviderBindingImpl<T>
    extends BindingImpl<Provider<T>>
    implements HasDependencies,
    ProviderBinding<Provider<T>> {
        final BindingImpl<T> providedBinding;

        ProviderBindingImpl(InjectorImpl injectorImpl, Key<Provider<T>> key, Binding<T> binding) {
            super(injectorImpl, key, binding.getSource(), ProviderBindingImpl.createInternalFactory(binding), Scoping.UNSCOPED);
            this.providedBinding = (BindingImpl)binding;
        }

        static <T> InternalFactory<Provider<T>> createInternalFactory(Binding<T> binding) {
            final Provider<T> provider = binding.getProvider();
            return new InternalFactory<Provider<T>>(){

                @Override
                public Provider<T> get(Errors errors, InternalContext internalContext, Dependency dependency, boolean bl) {
                    return provider;
                }
            };
        }

        @Override
        public Key<? extends T> getProvidedKey() {
            return this.providedBinding.getKey();
        }

        @Override
        public <V> V acceptTargetVisitor(BindingTargetVisitor<? super Provider<T>, V> bindingTargetVisitor) {
            return bindingTargetVisitor.visit(this);
        }

        @Override
        public void applyTo(Binder binder) {
            throw new UnsupportedOperationException("This element represents a synthetic binding.");
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(ProviderBinding.class).add("key", this.getKey()).add("providedKey", this.getProvidedKey()).toString();
        }

        @Override
        public Set<Dependency<?>> getDependencies() {
            return ImmutableSet.of(Dependency.get(this.getProvidedKey()));
        }

        public boolean equals(Object object) {
            if (object instanceof ProviderBindingImpl) {
                ProviderBindingImpl providerBindingImpl = (ProviderBindingImpl)object;
                return this.getKey().equals(providerBindingImpl.getKey()) && this.getScoping().equals(providerBindingImpl.getScoping()) && Objects.equal(this.providedBinding, providerBindingImpl.providedBinding);
            }
            return false;
        }

        public int hashCode() {
            return Objects.hashCode(this.getKey(), this.getScoping(), this.providedBinding);
        }

    }

    static enum JitLimitation {
        NO_JIT,
        EXISTING_JIT,
        NEW_OR_EXISTING_JIT;
        

        private JitLimitation() {
        }
    }

    static class InjectorOptions {
        final Stage stage;
        final boolean jitDisabled;
        final boolean disableCircularProxies;
        final boolean atInjectRequired;
        final boolean exactBindingAnnotationsRequired;

        InjectorOptions(Stage stage, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
            this.stage = stage;
            this.jitDisabled = bl;
            this.disableCircularProxies = bl2;
            this.atInjectRequired = bl3;
            this.exactBindingAnnotationsRequired = bl4;
        }

        public String toString() {
            return MoreObjects.toStringHelper(this.getClass()).add("stage", (Object)this.stage).add("jitDisabled", this.jitDisabled).add("disableCircularProxies", this.disableCircularProxies).add("atInjectRequired", this.atInjectRequired).add("exactBindingAnnotationsRequired", this.exactBindingAnnotationsRequired).toString();
        }
    }

}

