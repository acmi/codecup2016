/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.binder;

import com.google.inject.binder.LinkedBindingBuilder;
import java.lang.annotation.Annotation;

public interface AnnotatedBindingBuilder<T>
extends LinkedBindingBuilder<T> {
    public LinkedBindingBuilder<T> annotatedWith(Class<? extends Annotation> var1);

    public LinkedBindingBuilder<T> annotatedWith(Annotation var1);
}

