/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.ContextualCallable;
import com.google.inject.internal.DeferredLookups;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.Initializer;
import com.google.inject.internal.InjectionRequestProcessor;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InjectorShell;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.InternalFactory;
import com.google.inject.internal.LinkedBindingImpl;
import com.google.inject.internal.LookupProcessor;
import com.google.inject.internal.Lookups;
import com.google.inject.internal.ProcessedBindingData;
import com.google.inject.internal.Scoping;
import com.google.inject.internal.State;
import com.google.inject.internal.util.Stopwatch;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.Element;
import com.google.inject.spi.TypeConverterBinding;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class InternalInjectorCreator {
    private final Stopwatch stopwatch = new Stopwatch();
    private final Errors errors = new Errors();
    private final Initializer initializer = new Initializer();
    private final ProcessedBindingData bindingData = new ProcessedBindingData();
    private final InjectionRequestProcessor injectionRequestProcessor = new InjectionRequestProcessor(this.errors, this.initializer);
    private final InjectorShell.Builder shellBuilder = new InjectorShell.Builder();
    private List<InjectorShell> shells;

    public InternalInjectorCreator stage(Stage stage) {
        this.shellBuilder.stage(stage);
        return this;
    }

    public InternalInjectorCreator parentInjector(InjectorImpl injectorImpl) {
        this.shellBuilder.parent(injectorImpl);
        return this;
    }

    public InternalInjectorCreator addModules(Iterable<? extends Module> iterable) {
        this.shellBuilder.addModules(iterable);
        return this;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Injector build() {
        if (this.shellBuilder == null) {
            throw new AssertionError((Object)"Already built, builders are not reusable.");
        }
        Object object = this.shellBuilder.lock();
        synchronized (object) {
            this.shells = this.shellBuilder.build(this.initializer, this.bindingData, this.stopwatch, this.errors);
            this.stopwatch.resetAndLog("Injector construction");
            this.initializeStatically();
        }
        this.injectDynamically();
        if (this.shellBuilder.getStage() == Stage.TOOL) {
            return new ToolStageInjector(this.primaryInjector());
        }
        return this.primaryInjector();
    }

    private void initializeStatically() {
        this.bindingData.initializeBindings();
        this.stopwatch.resetAndLog("Binding initialization");
        for (InjectorShell injectorShell : this.shells) {
            injectorShell.getInjector().index();
        }
        this.stopwatch.resetAndLog("Binding indexing");
        this.injectionRequestProcessor.process(this.shells);
        this.stopwatch.resetAndLog("Collecting injection requests");
        this.bindingData.runCreationListeners(this.errors);
        this.stopwatch.resetAndLog("Binding validation");
        this.injectionRequestProcessor.validate();
        this.stopwatch.resetAndLog("Static validation");
        this.initializer.validateOustandingInjections(this.errors);
        this.stopwatch.resetAndLog("Instance member validation");
        new LookupProcessor(this.errors).process(this.shells);
        for (InjectorShell injectorShell : this.shells) {
            ((DeferredLookups)injectorShell.getInjector().lookups).initialize(this.errors);
        }
        this.stopwatch.resetAndLog("Provider verification");
        for (InjectorShell injectorShell : this.shells) {
            if (!injectorShell.getElements().isEmpty()) {
                throw new AssertionError((Object)("Failed to execute " + injectorShell.getElements()));
            }
        }
        this.errors.throwCreationExceptionIfErrorsExist();
    }

    private Injector primaryInjector() {
        return this.shells.get(0).getInjector();
    }

    private void injectDynamically() {
        this.injectionRequestProcessor.injectMembers();
        this.stopwatch.resetAndLog("Static member injection");
        this.initializer.injectAll(this.errors);
        this.stopwatch.resetAndLog("Instance injection");
        this.errors.throwCreationExceptionIfErrorsExist();
        if (this.shellBuilder.getStage() != Stage.TOOL) {
            for (InjectorShell injectorShell : this.shells) {
                this.loadEagerSingletons(injectorShell.getInjector(), this.shellBuilder.getStage(), this.errors);
            }
            this.stopwatch.resetAndLog("Preloading singletons");
        }
        this.errors.throwCreationExceptionIfErrorsExist();
    }

    void loadEagerSingletons(InjectorImpl injectorImpl, Stage stage, final Errors errors) {
        ImmutableList immutableList = ImmutableList.copyOf(Iterables.concat(injectorImpl.state.getExplicitBindingsThisLevel().values(), injectorImpl.jitBindings.values()));
        for (final BindingImpl bindingImpl : immutableList) {
            if (!this.isEagerSingleton(injectorImpl, bindingImpl, stage)) continue;
            try {
                injectorImpl.callInContext(new ContextualCallable<Void>(){
                    Dependency<?> dependency;

                    /*
                     * WARNING - Removed try catching itself - possible behaviour change.
                     */
                    @Override
                    public Void call(InternalContext internalContext) {
                        Dependency dependency = internalContext.pushDependency(this.dependency, bindingImpl.getSource());
                        Errors errors2 = errors.withSource(this.dependency);
                        try {
                            bindingImpl.getInternalFactory().get(errors2, internalContext, this.dependency, false);
                        }
                        catch (ErrorsException errorsException) {
                            errors2.merge(errorsException.getErrors());
                        }
                        finally {
                            internalContext.popStateAndSetDependency(dependency);
                        }
                        return null;
                    }
                });
                continue;
            }
            catch (ErrorsException errorsException) {
                throw new AssertionError();
            }
        }
    }

    private boolean isEagerSingleton(InjectorImpl injectorImpl, BindingImpl<?> bindingImpl, Stage stage) {
        if (bindingImpl.getScoping().isEagerSingleton(stage)) {
            return true;
        }
        if (bindingImpl instanceof LinkedBindingImpl) {
            Key key = ((LinkedBindingImpl)bindingImpl).getLinkedKey();
            return this.isEagerSingleton(injectorImpl, injectorImpl.getBinding(key), stage);
        }
        return false;
    }

    static class ToolStageInjector
    implements Injector {
        private final Injector delegateInjector;

        ToolStageInjector(Injector injector) {
            this.delegateInjector = injector;
        }

        @Override
        public void injectMembers(Object object) {
            throw new UnsupportedOperationException("Injector.injectMembers(Object) is not supported in Stage.TOOL");
        }

        @Override
        public Map<Key<?>, Binding<?>> getBindings() {
            return this.delegateInjector.getBindings();
        }

        @Override
        public Map<Key<?>, Binding<?>> getAllBindings() {
            return this.delegateInjector.getAllBindings();
        }

        @Override
        public <T> Binding<T> getBinding(Key<T> key) {
            return this.delegateInjector.getBinding(key);
        }

        @Override
        public <T> Binding<T> getBinding(Class<T> class_) {
            return this.delegateInjector.getBinding(class_);
        }

        @Override
        public <T> Binding<T> getExistingBinding(Key<T> key) {
            return this.delegateInjector.getExistingBinding(key);
        }

        @Override
        public <T> List<Binding<T>> findBindingsByType(TypeLiteral<T> typeLiteral) {
            return this.delegateInjector.findBindingsByType(typeLiteral);
        }

        @Override
        public Injector getParent() {
            return this.delegateInjector.getParent();
        }

        @Override
        public Injector createChildInjector(Iterable<? extends Module> iterable) {
            return this.delegateInjector.createChildInjector(iterable);
        }

        @Override
        public /* varargs */ Injector createChildInjector(Module ... arrmodule) {
            return this.delegateInjector.createChildInjector(arrmodule);
        }

        @Override
        public Map<Class<? extends Annotation>, Scope> getScopeBindings() {
            return this.delegateInjector.getScopeBindings();
        }

        @Override
        public Set<TypeConverterBinding> getTypeConverterBindings() {
            return this.delegateInjector.getTypeConverterBindings();
        }

        @Override
        public <T> Provider<T> getProvider(Key<T> key) {
            throw new UnsupportedOperationException("Injector.getProvider(Key<T>) is not supported in Stage.TOOL");
        }

        @Override
        public <T> Provider<T> getProvider(Class<T> class_) {
            throw new UnsupportedOperationException("Injector.getProvider(Class<T>) is not supported in Stage.TOOL");
        }

        @Override
        public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
            throw new UnsupportedOperationException("Injector.getMembersInjector(TypeLiteral<T>) is not supported in Stage.TOOL");
        }

        @Override
        public <T> MembersInjector<T> getMembersInjector(Class<T> class_) {
            throw new UnsupportedOperationException("Injector.getMembersInjector(Class<T>) is not supported in Stage.TOOL");
        }

        @Override
        public <T> T getInstance(Key<T> key) {
            throw new UnsupportedOperationException("Injector.getInstance(Key<T>) is not supported in Stage.TOOL");
        }

        @Override
        public <T> T getInstance(Class<T> class_) {
            throw new UnsupportedOperationException("Injector.getInstance(Class<T>) is not supported in Stage.TOOL");
        }
    }

}

