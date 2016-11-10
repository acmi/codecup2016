/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.Stage;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.BindingProcessor;
import com.google.inject.internal.ConstantFactory;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InheritingState;
import com.google.inject.internal.Initializable;
import com.google.inject.internal.Initializables;
import com.google.inject.internal.Initializer;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InjectorOptionsProcessor;
import com.google.inject.internal.InstanceBindingImpl;
import com.google.inject.internal.InterceptorBindingProcessor;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.InternalFactory;
import com.google.inject.internal.ListenerBindingProcessor;
import com.google.inject.internal.MembersInjectorStore;
import com.google.inject.internal.MessageProcessor;
import com.google.inject.internal.ModuleAnnotatedMethodScannerProcessor;
import com.google.inject.internal.PrivateElementProcessor;
import com.google.inject.internal.PrivateElementsImpl;
import com.google.inject.internal.ProcessedBindingData;
import com.google.inject.internal.ProviderInstanceBindingImpl;
import com.google.inject.internal.ProvisionListenerCallbackStore;
import com.google.inject.internal.ScopeBindingProcessor;
import com.google.inject.internal.Scoping;
import com.google.inject.internal.State;
import com.google.inject.internal.TypeConverterBindingProcessor;
import com.google.inject.internal.UntargettedBindingProcessor;
import com.google.inject.internal.util.SourceProvider;
import com.google.inject.internal.util.Stopwatch;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.Element;
import com.google.inject.spi.Elements;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.ModuleAnnotatedMethodScannerBinding;
import com.google.inject.spi.PrivateElements;
import com.google.inject.spi.ProvisionListenerBinding;
import com.google.inject.spi.TypeListenerBinding;
import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

final class InjectorShell {
    private final List<Element> elements;
    private final InjectorImpl injector;

    private InjectorShell(Builder builder, List<Element> list, InjectorImpl injectorImpl) {
        this.elements = list;
        this.injector = injectorImpl;
    }

    InjectorImpl getInjector() {
        return this.injector;
    }

    List<Element> getElements() {
        return this.elements;
    }

    private static void bindInjector(InjectorImpl injectorImpl) {
        Key<Injector> key = Key.get(Injector.class);
        InjectorFactory injectorFactory = new InjectorFactory(injectorImpl);
        injectorImpl.state.putBinding(key, new ProviderInstanceBindingImpl<Injector>(injectorImpl, key, SourceProvider.UNKNOWN_SOURCE, injectorFactory, Scoping.UNSCOPED, injectorFactory, ImmutableSet.<InjectionPoint>of()));
    }

    private static void bindLogger(InjectorImpl injectorImpl) {
        Key<Logger> key = Key.get(Logger.class);
        LoggerFactory loggerFactory = new LoggerFactory();
        injectorImpl.state.putBinding(key, new ProviderInstanceBindingImpl<Logger>(injectorImpl, key, SourceProvider.UNKNOWN_SOURCE, loggerFactory, Scoping.UNSCOPED, loggerFactory, ImmutableSet.<InjectionPoint>of()));
    }

    private static void bindStage(InjectorImpl injectorImpl, Stage stage) {
        Key<Stage> key = Key.get(Stage.class);
        InstanceBindingImpl<Stage> instanceBindingImpl = new InstanceBindingImpl<Stage>(injectorImpl, key, SourceProvider.UNKNOWN_SOURCE, new ConstantFactory<Stage>(Initializables.of(stage)), ImmutableSet.<InjectionPoint>of(), stage);
        injectorImpl.state.putBinding(key, instanceBindingImpl);
    }

    private static class InheritedScannersModule
    implements Module {
        private final State state;

        InheritedScannersModule(State state) {
            this.state = state;
        }

        @Override
        public void configure(Binder binder) {
            for (ModuleAnnotatedMethodScannerBinding moduleAnnotatedMethodScannerBinding : this.state.getScannerBindings()) {
                moduleAnnotatedMethodScannerBinding.applyTo(binder);
            }
        }
    }

    private static class RootModule
    implements Module {
        private RootModule() {
        }

        @Override
        public void configure(Binder binder) {
            binder = binder.withSource(SourceProvider.UNKNOWN_SOURCE);
            binder.bindScope(Singleton.class, Scopes.SINGLETON);
            binder.bindScope(javax.inject.Singleton.class, Scopes.SINGLETON);
        }
    }

    private static class LoggerFactory
    implements Provider<Logger>,
    InternalFactory<Logger> {
        private LoggerFactory() {
        }

        @Override
        public Logger get(Errors errors, InternalContext internalContext, Dependency<?> dependency, boolean bl) {
            InjectionPoint injectionPoint = dependency.getInjectionPoint();
            return injectionPoint == null ? Logger.getAnonymousLogger() : Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
        }

        @Override
        public Logger get() {
            return Logger.getAnonymousLogger();
        }

        public String toString() {
            return "Provider<Logger>";
        }
    }

    private static class InjectorFactory
    implements Provider<Injector>,
    InternalFactory<Injector> {
        private final Injector injector;

        private InjectorFactory(Injector injector) {
            this.injector = injector;
        }

        @Override
        public Injector get(Errors errors, InternalContext internalContext, Dependency<?> dependency, boolean bl) throws ErrorsException {
            return this.injector;
        }

        @Override
        public Injector get() {
            return this.injector;
        }

        public String toString() {
            return "Provider<Injector>";
        }
    }

    static class Builder {
        private final List<Element> elements = Lists.newArrayList();
        private final List<Module> modules = Lists.newArrayList();
        private State state;
        private InjectorImpl parent;
        private InjectorImpl.InjectorOptions options;
        private Stage stage;
        private PrivateElementsImpl privateElements;

        Builder() {
        }

        Builder stage(Stage stage) {
            this.stage = stage;
            return this;
        }

        Builder parent(InjectorImpl injectorImpl) {
            this.parent = injectorImpl;
            this.state = new InheritingState(injectorImpl.state);
            this.options = injectorImpl.options;
            this.stage = this.options.stage;
            return this;
        }

        Builder privateElements(PrivateElements privateElements) {
            this.privateElements = (PrivateElementsImpl)privateElements;
            this.elements.addAll(privateElements.getElements());
            return this;
        }

        void addModules(Iterable<? extends Module> iterable) {
            for (Module module : iterable) {
                this.modules.add(module);
            }
        }

        Stage getStage() {
            return this.options.stage;
        }

        Object lock() {
            return this.getState().lock();
        }

        List<InjectorShell> build(Initializer initializer, ProcessedBindingData processedBindingData, Stopwatch stopwatch, Errors errors) {
            Preconditions.checkState(this.stage != null, "Stage not initialized");
            Preconditions.checkState(this.privateElements == null || this.parent != null, "PrivateElements with no parent");
            Preconditions.checkState(this.state != null, "no state. Did you remember to lock() ?");
            if (this.parent == null) {
                this.modules.add(0, new RootModule());
            } else {
                this.modules.add(0, new InheritedScannersModule(this.parent.state));
            }
            this.elements.addAll(Elements.getElements(this.stage, this.modules));
            InjectorOptionsProcessor injectorOptionsProcessor = new InjectorOptionsProcessor(errors);
            injectorOptionsProcessor.process(null, this.elements);
            this.options = injectorOptionsProcessor.getOptions(this.stage, this.options);
            InjectorImpl injectorImpl = new InjectorImpl(this.parent, this.state, this.options);
            if (this.privateElements != null) {
                this.privateElements.initInjector(injectorImpl);
            }
            if (this.parent == null) {
                TypeConverterBindingProcessor.prepareBuiltInConverters(injectorImpl);
            }
            stopwatch.resetAndLog("Module execution");
            new MessageProcessor(errors).process(injectorImpl, this.elements);
            new InterceptorBindingProcessor(errors).process(injectorImpl, this.elements);
            stopwatch.resetAndLog("Interceptors creation");
            new ListenerBindingProcessor(errors).process(injectorImpl, this.elements);
            List<TypeListenerBinding> list = injectorImpl.state.getTypeListenerBindings();
            injectorImpl.membersInjectorStore = new MembersInjectorStore(injectorImpl, list);
            List<ProvisionListenerBinding> list2 = injectorImpl.state.getProvisionListenerBindings();
            injectorImpl.provisionListenerStore = new ProvisionListenerCallbackStore(list2);
            stopwatch.resetAndLog("TypeListeners & ProvisionListener creation");
            new ScopeBindingProcessor(errors).process(injectorImpl, this.elements);
            stopwatch.resetAndLog("Scopes creation");
            new TypeConverterBindingProcessor(errors).process(injectorImpl, this.elements);
            stopwatch.resetAndLog("Converters creation");
            InjectorShell.bindStage(injectorImpl, this.stage);
            InjectorShell.bindInjector(injectorImpl);
            InjectorShell.bindLogger(injectorImpl);
            new BindingProcessor(errors, initializer, processedBindingData).process(injectorImpl, this.elements);
            new UntargettedBindingProcessor(errors, processedBindingData).process(injectorImpl, this.elements);
            stopwatch.resetAndLog("Binding creation");
            new ModuleAnnotatedMethodScannerProcessor(errors).process(injectorImpl, this.elements);
            stopwatch.resetAndLog("Module annotated method scanners creation");
            ArrayList<InjectorShell> arrayList = Lists.newArrayList();
            arrayList.add(new InjectorShell(this, this.elements, injectorImpl));
            PrivateElementProcessor privateElementProcessor = new PrivateElementProcessor(errors);
            privateElementProcessor.process(injectorImpl, this.elements);
            for (Builder builder : privateElementProcessor.getInjectorShellBuilders()) {
                arrayList.addAll(builder.build(initializer, processedBindingData, stopwatch, errors));
            }
            stopwatch.resetAndLog("Private environment creation");
            return arrayList;
        }

        private State getState() {
            if (this.state == null) {
                this.state = new InheritingState(State.NONE);
            }
            return this.state;
        }
    }

}

