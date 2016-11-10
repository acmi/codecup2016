/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.AbstractProcessor;
import com.google.inject.internal.Annotations;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.ExposedBindingImpl;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.ProcessedBindingData;
import com.google.inject.internal.Scoping;
import com.google.inject.internal.State;
import com.google.inject.internal.UntargettedBindingImpl;
import com.google.inject.spi.DefaultBindingTargetVisitor;
import com.google.inject.spi.PrivateElements;
import java.util.Map;
import java.util.Set;

abstract class AbstractBindingProcessor
extends AbstractProcessor {
    private static final Set<Class<?>> FORBIDDEN_TYPES = ImmutableSet.of(AbstractModule.class, Binder.class, Binding.class, Injector.class, Key.class, MembersInjector.class, Module.class, Provider.class, Scope.class, Stage.class, TypeLiteral.class);
    protected final ProcessedBindingData bindingData;

    AbstractBindingProcessor(Errors errors, ProcessedBindingData processedBindingData) {
        super(errors);
        this.bindingData = processedBindingData;
    }

    protected <T> UntargettedBindingImpl<T> invalidBinding(InjectorImpl injectorImpl, Key<T> key, Object object) {
        return new UntargettedBindingImpl<T>(injectorImpl, key, object);
    }

    protected void putBinding(BindingImpl<?> bindingImpl) {
        Key key;
        block6 : {
            key = bindingImpl.getKey();
            Class class_ = key.getTypeLiteral().getRawType();
            if (FORBIDDEN_TYPES.contains(class_)) {
                this.errors.cannotBindToGuiceType(class_.getSimpleName());
                return;
            }
            Binding binding = this.injector.getExistingBinding(key);
            if (binding != null) {
                if (this.injector.state.getExplicitBinding(key) != null) {
                    try {
                        if (!this.isOkayDuplicate(binding, bindingImpl, this.injector.state)) {
                            this.errors.bindingAlreadySet(key, binding.getSource());
                            return;
                        }
                        break block6;
                    }
                    catch (Throwable throwable) {
                        this.errors.errorCheckingDuplicateBinding(key, binding.getSource(), throwable);
                        return;
                    }
                }
                this.errors.jitBindingAlreadySet(key);
                return;
            }
        }
        this.injector.state.parent().blacklist(key, this.injector.state, bindingImpl.getSource());
        this.injector.state.putBinding(key, bindingImpl);
    }

    private boolean isOkayDuplicate(BindingImpl<?> bindingImpl, BindingImpl<?> bindingImpl2, State state) {
        if (bindingImpl instanceof ExposedBindingImpl) {
            ExposedBindingImpl exposedBindingImpl = (ExposedBindingImpl)bindingImpl;
            InjectorImpl injectorImpl = (InjectorImpl)exposedBindingImpl.getPrivateElements().getInjector();
            return injectorImpl == bindingImpl2.getInjector();
        }
        bindingImpl = (BindingImpl)state.getExplicitBindingsThisLevel().get(bindingImpl2.getKey());
        if (bindingImpl == null) {
            return false;
        }
        return bindingImpl.equals(bindingImpl2);
    }

    private <T> void validateKey(Object object, Key<T> key) {
        Annotations.checkForMisplacedScopeAnnotations(key.getTypeLiteral().getRawType(), object, this.errors);
    }

    abstract class Processor<T, V>
    extends DefaultBindingTargetVisitor<T, V> {
        final Object source;
        final Key<T> key;
        final Class<? super T> rawType;
        Scoping scoping;

        Processor(BindingImpl<T> bindingImpl) {
            this.source = bindingImpl.getSource();
            this.key = bindingImpl.getKey();
            this.rawType = this.key.getTypeLiteral().getRawType();
            this.scoping = bindingImpl.getScoping();
        }

        protected void prepareBinding() {
            AbstractBindingProcessor.this.validateKey(this.source, this.key);
            this.scoping = Scoping.makeInjectable(this.scoping, AbstractBindingProcessor.this.injector, AbstractBindingProcessor.this.errors);
        }

        protected void scheduleInitialization(final BindingImpl<?> bindingImpl) {
            AbstractBindingProcessor.this.bindingData.addUninitializedBinding(new Runnable(){

                @Override
                public void run() {
                    try {
                        bindingImpl.getInjector().initializeBinding(bindingImpl, AbstractBindingProcessor.this.errors.withSource(Processor.this.source));
                    }
                    catch (ErrorsException errorsException) {
                        AbstractBindingProcessor.this.errors.merge(errorsException.getErrors());
                    }
                }
            });
        }

    }

}

