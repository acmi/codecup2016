/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.Key;
import com.google.inject.spi.HasDependencies;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface ProvidesMethodBinding<T>
extends HasDependencies {
    public Method getMethod();

    public Object getEnclosingInstance();

    public Key<T> getKey();

    public Annotation getAnnotation();
}

