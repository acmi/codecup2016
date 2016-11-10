/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.spi.InjectionPoint;
import java.lang.annotation.Annotation;
import java.util.Set;

public abstract class ModuleAnnotatedMethodScanner {
    public abstract Set<? extends Class<? extends Annotation>> annotationClasses();

    public abstract <T> Key<T> prepareMethod(Binder var1, Annotation var2, Key<T> var3, InjectionPoint var4);
}

