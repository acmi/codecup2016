/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.binder.AnnotatedConstantBindingBuilder;
import com.google.inject.binder.ConstantBindingBuilder;
import com.google.inject.internal.AbstractBindingBuilder;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.InstanceBindingImpl;
import com.google.inject.internal.Scoping;
import com.google.inject.spi.Element;
import com.google.inject.spi.InjectionPoint;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

public final class ConstantBindingBuilderImpl<T>
extends AbstractBindingBuilder<T>
implements AnnotatedConstantBindingBuilder,
ConstantBindingBuilder {
    public ConstantBindingBuilderImpl(Binder binder, List<Element> list, Object object) {
        super(binder, list, object, NULL_KEY);
    }

    @Override
    public ConstantBindingBuilder annotatedWith(Class<? extends Annotation> class_) {
        this.annotatedWithInternal(class_);
        return this;
    }

    @Override
    public ConstantBindingBuilder annotatedWith(Annotation annotation) {
        this.annotatedWithInternal(annotation);
        return this;
    }

    @Override
    public void to(String string) {
        this.toConstant(String.class, string);
    }

    @Override
    public void to(int n2) {
        this.toConstant(Integer.class, n2);
    }

    @Override
    public void to(long l2) {
        this.toConstant(Long.class, l2);
    }

    @Override
    public void to(boolean bl) {
        this.toConstant(Boolean.class, bl);
    }

    @Override
    public void to(double d2) {
        this.toConstant(Double.class, d2);
    }

    @Override
    public void to(float f2) {
        this.toConstant(Float.class, Float.valueOf(f2));
    }

    @Override
    public void to(short s2) {
        this.toConstant(Short.class, s2);
    }

    @Override
    public void to(char c2) {
        this.toConstant(Character.class, Character.valueOf(c2));
    }

    @Override
    public void to(byte by) {
        this.toConstant(Byte.class, Byte.valueOf(by));
    }

    @Override
    public void to(Class<?> class_) {
        this.toConstant(Class.class, class_);
    }

    @Override
    public <E extends Enum<E>> void to(E e2) {
        this.toConstant(e2.getDeclaringClass(), e2);
    }

    private void toConstant(Class<?> class_, Object object) {
        Class class_2 = class_;
        Object object2 = object;
        if (this.keyTypeIsSet()) {
            this.binder.addError("Constant value is set more than once.", new Object[0]);
            return;
        }
        BindingImpl bindingImpl = this.getBinding();
        Key key = bindingImpl.getKey().getAnnotation() != null ? Key.get(class_2, bindingImpl.getKey().getAnnotation()) : (bindingImpl.getKey().getAnnotationType() != null ? Key.get(class_2, bindingImpl.getKey().getAnnotationType()) : Key.get(class_2));
        if (object2 == null) {
            this.binder.addError("Binding to null instances is not allowed. Use toProvider(Providers.of(null)) if this is your intended behaviour.", new Object[0]);
        }
        this.setBinding(new InstanceBindingImpl<Object>(bindingImpl.getSource(), key, bindingImpl.getScoping(), ImmutableSet.<InjectionPoint>of(), object2));
    }

    public String toString() {
        return "ConstantBindingBuilder";
    }
}

