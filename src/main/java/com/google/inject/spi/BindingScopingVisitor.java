/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.Scope;
import java.lang.annotation.Annotation;

public interface BindingScopingVisitor<V> {
    public V visitEagerSingleton();

    public V visitScope(Scope var1);

    public V visitScopeAnnotation(Class<? extends Annotation> var1);

    public V visitNoScoping();
}

