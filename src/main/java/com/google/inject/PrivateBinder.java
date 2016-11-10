/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedElementBuilder;

public interface PrivateBinder
extends Binder {
    public void expose(Key<?> var1);

    public AnnotatedElementBuilder expose(Class<?> var1);

    public AnnotatedElementBuilder expose(TypeLiteral<?> var1);

    @Override
    public PrivateBinder withSource(Object var1);

    @Override
    public /* varargs */ PrivateBinder skipSources(Class ... var1);
}

