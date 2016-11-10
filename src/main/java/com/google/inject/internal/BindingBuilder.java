/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.ConfigurationException;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.internal.AbstractBindingBuilder;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.ConstructorBindingImpl;
import com.google.inject.internal.InstanceBindingImpl;
import com.google.inject.internal.LinkedBindingImpl;
import com.google.inject.internal.LinkedProviderBindingImpl;
import com.google.inject.internal.ProviderInstanceBindingImpl;
import com.google.inject.internal.Scoping;
import com.google.inject.spi.Element;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.Message;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class BindingBuilder<T>
extends AbstractBindingBuilder<T>
implements AnnotatedBindingBuilder<T> {
    public BindingBuilder(Binder binder, List<Element> list, Object object, Key<T> key) {
        super(binder, list, object, key);
    }

    @Override
    public BindingBuilder<T> annotatedWith(Class<? extends Annotation> class_) {
        this.annotatedWithInternal(class_);
        return this;
    }

    @Override
    public BindingBuilder<T> annotatedWith(Annotation annotation) {
        this.annotatedWithInternal(annotation);
        return this;
    }

    @Override
    public BindingBuilder<T> to(Class<? extends T> class_) {
        return this.to((Key)Key.get(class_));
    }

    @Override
    public BindingBuilder<T> to(TypeLiteral<? extends T> typeLiteral) {
        return this.to((Key)Key.get(typeLiteral));
    }

    @Override
    public BindingBuilder<T> to(Key<? extends T> key) {
        Preconditions.checkNotNull(key, "linkedKey");
        this.checkNotTargetted();
        BindingImpl bindingImpl = this.getBinding();
        this.setBinding(new LinkedBindingImpl<T>(bindingImpl.getSource(), bindingImpl.getKey(), bindingImpl.getScoping(), key));
        return this;
    }

    @Override
    public void toInstance(T t2) {
        Set set;
        this.checkNotTargetted();
        if (t2 != null) {
            try {
                set = InjectionPoint.forInstanceMethodsAndFields(t2.getClass());
            }
            catch (ConfigurationException configurationException) {
                this.copyErrorsToBinder(configurationException);
                set = (Set)configurationException.getPartialValue();
            }
        } else {
            this.binder.addError("Binding to null instances is not allowed. Use toProvider(Providers.of(null)) if this is your intended behaviour.", new Object[0]);
            set = ImmutableSet.of();
        }
        BindingImpl bindingImpl = this.getBinding();
        this.setBinding(new InstanceBindingImpl(bindingImpl.getSource(), bindingImpl.getKey(), Scoping.EAGER_SINGLETON, set, t2));
    }

    @Override
    public BindingBuilder<T> toProvider(Provider<? extends T> provider) {
        return this.toProvider((javax.inject.Provider)provider);
    }

    @Override
    public BindingBuilder<T> toProvider(javax.inject.Provider<? extends T> provider) {
        Set set;
        Preconditions.checkNotNull(provider, "provider");
        this.checkNotTargetted();
        try {
            set = InjectionPoint.forInstanceMethodsAndFields(provider.getClass());
        }
        catch (ConfigurationException configurationException) {
            this.copyErrorsToBinder(configurationException);
            set = (Set)configurationException.getPartialValue();
        }
        BindingImpl bindingImpl = this.getBinding();
        this.setBinding(new ProviderInstanceBindingImpl<T>(bindingImpl.getSource(), bindingImpl.getKey(), bindingImpl.getScoping(), set, provider));
        return this;
    }

    @Override
    public BindingBuilder<T> toProvider(Class<? extends javax.inject.Provider<? extends T>> class_) {
        return this.toProvider((Key)Key.get(class_));
    }

    @Override
    public BindingBuilder<T> toProvider(TypeLiteral<? extends javax.inject.Provider<? extends T>> typeLiteral) {
        return this.toProvider((Key)Key.get(typeLiteral));
    }

    @Override
    public BindingBuilder<T> toProvider(Key<? extends javax.inject.Provider<? extends T>> key) {
        Preconditions.checkNotNull(key, "providerKey");
        this.checkNotTargetted();
        BindingImpl bindingImpl = this.getBinding();
        this.setBinding(new LinkedProviderBindingImpl(bindingImpl.getSource(), bindingImpl.getKey(), bindingImpl.getScoping(), key));
        return this;
    }

    @Override
    public <S extends T> ScopedBindingBuilder toConstructor(Constructor<S> constructor) {
        return this.toConstructor(constructor, TypeLiteral.get(constructor.getDeclaringClass()));
    }

    @Override
    public <S extends T> ScopedBindingBuilder toConstructor(Constructor<S> constructor, TypeLiteral<? extends S> typeLiteral) {
        Set set;
        Preconditions.checkNotNull(constructor, "constructor");
        Preconditions.checkNotNull(typeLiteral, "type");
        this.checkNotTargetted();
        BindingImpl bindingImpl = this.getBinding();
        try {
            set = InjectionPoint.forInstanceMethodsAndFields(typeLiteral);
        }
        catch (ConfigurationException configurationException) {
            this.copyErrorsToBinder(configurationException);
            set = (Set)configurationException.getPartialValue();
        }
        try {
            InjectionPoint injectionPoint = InjectionPoint.forConstructor(constructor, typeLiteral);
            this.setBinding(new ConstructorBindingImpl(bindingImpl.getKey(), bindingImpl.getSource(), bindingImpl.getScoping(), injectionPoint, set));
        }
        catch (ConfigurationException configurationException) {
            this.copyErrorsToBinder(configurationException);
        }
        return this;
    }

    public String toString() {
        return "BindingBuilder<" + this.getBinding().getKey().getTypeLiteral() + ">";
    }

    private void copyErrorsToBinder(ConfigurationException configurationException) {
        for (Message message : configurationException.getErrorMessages()) {
            this.binder.addError(message);
        }
    }
}

