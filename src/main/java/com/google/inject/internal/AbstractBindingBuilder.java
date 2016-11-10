/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Scope;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.Scoping;
import com.google.inject.internal.UntargettedBindingImpl;
import com.google.inject.spi.Element;
import com.google.inject.spi.InstanceBinding;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

public abstract class AbstractBindingBuilder<T> {
    public static final String IMPLEMENTATION_ALREADY_SET = "Implementation is set more than once.";
    public static final String SINGLE_INSTANCE_AND_SCOPE = "Setting the scope is not permitted when binding to a single instance.";
    public static final String SCOPE_ALREADY_SET = "Scope is set more than once.";
    public static final String BINDING_TO_NULL = "Binding to null instances is not allowed. Use toProvider(Providers.of(null)) if this is your intended behaviour.";
    public static final String CONSTANT_VALUE_ALREADY_SET = "Constant value is set more than once.";
    public static final String ANNOTATION_ALREADY_SPECIFIED = "More than one annotation is specified for this binding.";
    protected static final Key<?> NULL_KEY = Key.get(Void.class);
    protected List<Element> elements;
    protected int position;
    protected final Binder binder;
    private BindingImpl<T> binding;

    public AbstractBindingBuilder(Binder binder, List<Element> list, Object object, Key<T> key) {
        this.binder = binder;
        this.elements = list;
        this.position = list.size();
        this.binding = new UntargettedBindingImpl<T>(object, key, Scoping.UNSCOPED);
        list.add(this.position, this.binding);
    }

    protected BindingImpl<T> getBinding() {
        return this.binding;
    }

    protected BindingImpl<T> setBinding(BindingImpl<T> bindingImpl) {
        this.binding = bindingImpl;
        this.elements.set(this.position, bindingImpl);
        return bindingImpl;
    }

    protected BindingImpl<T> annotatedWithInternal(Class<? extends Annotation> class_) {
        Preconditions.checkNotNull(class_, "annotationType");
        this.checkNotAnnotated();
        return this.setBinding(this.binding.withKey(Key.get(this.binding.getKey().getTypeLiteral(), class_)));
    }

    protected BindingImpl<T> annotatedWithInternal(Annotation annotation) {
        Preconditions.checkNotNull(annotation, "annotation");
        this.checkNotAnnotated();
        return this.setBinding(this.binding.withKey(Key.get(this.binding.getKey().getTypeLiteral(), annotation)));
    }

    public void in(Class<? extends Annotation> class_) {
        Preconditions.checkNotNull(class_, "scopeAnnotation");
        this.checkNotScoped();
        this.setBinding(this.getBinding().withScoping(Scoping.forAnnotation(class_)));
    }

    public void in(Scope scope) {
        Preconditions.checkNotNull(scope, "scope");
        this.checkNotScoped();
        this.setBinding(this.getBinding().withScoping(Scoping.forInstance(scope)));
    }

    public void asEagerSingleton() {
        this.checkNotScoped();
        this.setBinding(this.getBinding().withScoping(Scoping.EAGER_SINGLETON));
    }

    protected boolean keyTypeIsSet() {
        return !Void.class.equals((Object)this.binding.getKey().getTypeLiteral().getType());
    }

    protected void checkNotTargetted() {
        if (!(this.binding instanceof UntargettedBindingImpl)) {
            this.binder.addError("Implementation is set more than once.", new Object[0]);
        }
    }

    protected void checkNotAnnotated() {
        if (this.binding.getKey().getAnnotationType() != null) {
            this.binder.addError("More than one annotation is specified for this binding.", new Object[0]);
        }
    }

    protected void checkNotScoped() {
        if (this.binding instanceof InstanceBinding) {
            this.binder.addError("Setting the scope is not permitted when binding to a single instance.", new Object[0]);
            return;
        }
        if (this.binding.getScoping().isExplicitlyScoped()) {
            this.binder.addError("Scope is set more than once.", new Object[0]);
        }
    }
}

