/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.AbstractBindingProcessor;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.BoundProviderFactory;
import com.google.inject.internal.ConstantFactory;
import com.google.inject.internal.ConstructorBindingImpl;
import com.google.inject.internal.CreationListener;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.ExposedBindingImpl;
import com.google.inject.internal.ExposedKeyFactory;
import com.google.inject.internal.FactoryProxy;
import com.google.inject.internal.Initializable;
import com.google.inject.internal.Initializer;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InstanceBindingImpl;
import com.google.inject.internal.InternalFactory;
import com.google.inject.internal.InternalFactoryToInitializableAdapter;
import com.google.inject.internal.LinkedBindingImpl;
import com.google.inject.internal.LinkedProviderBindingImpl;
import com.google.inject.internal.ProcessedBindingData;
import com.google.inject.internal.ProviderInstanceBindingImpl;
import com.google.inject.internal.ProviderMethod;
import com.google.inject.internal.ProvisionListenerCallbackStore;
import com.google.inject.internal.ProvisionListenerStackCallback;
import com.google.inject.internal.Scoping;
import com.google.inject.internal.UntargettedBindingImpl;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.ConstructorBinding;
import com.google.inject.spi.ConvertedConstantBinding;
import com.google.inject.spi.ExposedBinding;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.InstanceBinding;
import com.google.inject.spi.LinkedKeyBinding;
import com.google.inject.spi.PrivateElements;
import com.google.inject.spi.ProviderBinding;
import com.google.inject.spi.ProviderInstanceBinding;
import com.google.inject.spi.ProviderKeyBinding;
import com.google.inject.spi.UntargettedBinding;
import java.util.Set;

final class BindingProcessor
extends AbstractBindingProcessor {
    private final Initializer initializer;

    BindingProcessor(Errors errors, Initializer initializer, ProcessedBindingData processedBindingData) {
        super(errors, processedBindingData);
        this.initializer = initializer;
    }

    @Override
    public <T> Boolean visit(Binding<T> binding) {
        Class<T> class_ = binding.getKey().getTypeLiteral().getRawType();
        if (Void.class.equals(class_)) {
            if (binding instanceof ProviderInstanceBinding && ((ProviderInstanceBinding)binding).getUserSuppliedProvider() instanceof ProviderMethod) {
                this.errors.voidProviderMethod();
            } else {
                this.errors.missingConstantValues();
            }
            return true;
        }
        if (class_ == Provider.class) {
            this.errors.bindingToProvider();
            return true;
        }
        return (Boolean)binding.acceptTargetVisitor(new AbstractBindingProcessor.Processor<T, Boolean>((BindingImpl)binding){

            @Override
            public Boolean visit(ConstructorBinding<? extends T> constructorBinding) {
                this.prepareBinding();
                try {
                    ConstructorBindingImpl constructorBindingImpl = ConstructorBindingImpl.create(BindingProcessor.this.injector, this.key, constructorBinding.getConstructor(), this.source, this.scoping, BindingProcessor.this.errors, false, false);
                    this.scheduleInitialization(constructorBindingImpl);
                    BindingProcessor.this.putBinding(constructorBindingImpl);
                }
                catch (ErrorsException errorsException) {
                    BindingProcessor.this.errors.merge(errorsException.getErrors());
                    BindingProcessor.this.putBinding(BindingProcessor.this.invalidBinding(BindingProcessor.this.injector, this.key, this.source));
                }
                return true;
            }

            @Override
            public Boolean visit(InstanceBinding<? extends T> instanceBinding) {
                this.prepareBinding();
                Set<InjectionPoint> set = instanceBinding.getInjectionPoints();
                T t2 = instanceBinding.getInstance();
                Initializable<? extends T> initializable = BindingProcessor.this.initializer.requestInjection(BindingProcessor.this.injector, t2, instanceBinding, this.source, set);
                ConstantFactory<? extends T> constantFactory = new ConstantFactory<T>(initializable);
                InternalFactory<? extends T> internalFactory = Scoping.scope(this.key, BindingProcessor.this.injector, constantFactory, this.source, this.scoping);
                BindingProcessor.this.putBinding(new InstanceBindingImpl<T>(BindingProcessor.this.injector, this.key, this.source, internalFactory, set, (T)t2));
                return true;
            }

            @Override
            public Boolean visit(ProviderInstanceBinding<? extends T> providerInstanceBinding) {
                this.prepareBinding();
                javax.inject.Provider<? extends T> provider = providerInstanceBinding.getUserSuppliedProvider();
                if (provider instanceof ProviderMethod) {
                    ProviderMethod providerMethod = (ProviderMethod)provider;
                    return this.visitProviderMethod(providerMethod);
                }
                Set<InjectionPoint> set = providerInstanceBinding.getInjectionPoints();
                Initializable<javax.inject.Provider<? extends T>> initializable = BindingProcessor.this.initializer.requestInjection(BindingProcessor.this.injector, provider, null, this.source, set);
                InternalFactoryToInitializableAdapter<? extends T> internalFactoryToInitializableAdapter = new InternalFactoryToInitializableAdapter<T>(initializable, this.source, BindingProcessor.this.injector.provisionListenerStore.get(providerInstanceBinding));
                InternalFactory<? extends T> internalFactory = Scoping.scope(this.key, BindingProcessor.this.injector, internalFactoryToInitializableAdapter, this.source, this.scoping);
                BindingProcessor.this.putBinding(new ProviderInstanceBindingImpl<T>(BindingProcessor.this.injector, this.key, this.source, internalFactory, this.scoping, provider, set));
                return true;
            }

            @Override
            public Boolean visit(ProviderKeyBinding<? extends T> providerKeyBinding) {
                this.prepareBinding();
                Key<javax.inject.Provider<? extends T>> key = providerKeyBinding.getProviderKey();
                BoundProviderFactory<? extends T> boundProviderFactory = new BoundProviderFactory<T>(BindingProcessor.this.injector, key, this.source, BindingProcessor.this.injector.provisionListenerStore.get(providerKeyBinding));
                BindingProcessor.this.bindingData.addCreationListener(boundProviderFactory);
                InternalFactory<? extends T> internalFactory = Scoping.scope(this.key, BindingProcessor.this.injector, boundProviderFactory, this.source, this.scoping);
                BindingProcessor.this.putBinding(new LinkedProviderBindingImpl<T>(BindingProcessor.this.injector, this.key, this.source, internalFactory, this.scoping, key));
                return true;
            }

            @Override
            public Boolean visit(LinkedKeyBinding<? extends T> linkedKeyBinding) {
                this.prepareBinding();
                Key<? extends T> key = linkedKeyBinding.getLinkedKey();
                if (this.key.equals(key)) {
                    BindingProcessor.this.errors.recursiveBinding();
                }
                FactoryProxy<? extends T> factoryProxy = new FactoryProxy<T>(BindingProcessor.this.injector, this.key, key, this.source);
                BindingProcessor.this.bindingData.addCreationListener(factoryProxy);
                InternalFactory<? extends T> internalFactory = Scoping.scope(this.key, BindingProcessor.this.injector, factoryProxy, this.source, this.scoping);
                BindingProcessor.this.putBinding(new LinkedBindingImpl<T>(BindingProcessor.this.injector, this.key, this.source, internalFactory, this.scoping, key));
                return true;
            }

            private Boolean visitProviderMethod(ProviderMethod<T> providerMethod) {
                BindingImpl<T> bindingImpl = ProviderMethod.createBinding(BindingProcessor.this.injector, this.key, providerMethod, this.source, this.scoping);
                this.scheduleInitialization(bindingImpl);
                BindingProcessor.this.putBinding(bindingImpl);
                return true;
            }

            @Override
            public Boolean visit(UntargettedBinding<? extends T> untargettedBinding) {
                return false;
            }

            @Override
            public Boolean visit(ExposedBinding<? extends T> exposedBinding) {
                throw new IllegalArgumentException("Cannot apply a non-module element");
            }

            @Override
            public Boolean visit(ConvertedConstantBinding<? extends T> convertedConstantBinding) {
                throw new IllegalArgumentException("Cannot apply a non-module element");
            }

            @Override
            public Boolean visit(ProviderBinding<? extends T> providerBinding) {
                throw new IllegalArgumentException("Cannot apply a non-module element");
            }

            @Override
            protected Boolean visitOther(Binding<? extends T> binding) {
                throw new IllegalStateException("BindingProcessor should override all visitations");
            }
        });
    }

    @Override
    public Boolean visit(PrivateElements privateElements) {
        for (Key key : privateElements.getExposedKeys()) {
            this.bindExposed(privateElements, key);
        }
        return false;
    }

    private <T> void bindExposed(PrivateElements privateElements, Key<T> key) {
        ExposedKeyFactory<T> exposedKeyFactory = new ExposedKeyFactory<T>(key, privateElements);
        this.bindingData.addCreationListener(exposedKeyFactory);
        this.putBinding(new ExposedBindingImpl<T>(this.injector, privateElements.getExposedSource(key), key, exposedKeyFactory, privateElements));
    }

}

