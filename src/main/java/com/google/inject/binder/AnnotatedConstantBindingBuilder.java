/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.binder;

import com.google.inject.binder.ConstantBindingBuilder;
import java.lang.annotation.Annotation;

public interface AnnotatedConstantBindingBuilder {
    public ConstantBindingBuilder annotatedWith(Class<? extends Annotation> var1);

    public ConstantBindingBuilder annotatedWith(Annotation var1);
}

