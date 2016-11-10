/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.Element;
import com.google.inject.spi.ElementVisitor;
import com.google.inject.spi.TypeListener;

public final class TypeListenerBinding
implements Element {
    private final Object source;
    private final Matcher<? super TypeLiteral<?>> typeMatcher;
    private final TypeListener listener;

    TypeListenerBinding(Object object, TypeListener typeListener, Matcher<? super TypeLiteral<?>> matcher) {
        this.source = object;
        this.listener = typeListener;
        this.typeMatcher = matcher;
    }

    public TypeListener getListener() {
        return this.listener;
    }

    public Matcher<? super TypeLiteral<?>> getTypeMatcher() {
        return this.typeMatcher;
    }

    @Override
    public Object getSource() {
        return this.source;
    }

    @Override
    public <T> T acceptVisitor(ElementVisitor<T> elementVisitor) {
        return elementVisitor.visit(this);
    }

    @Override
    public void applyTo(Binder binder) {
        binder.withSource(this.getSource()).bindListener(this.typeMatcher, this.listener);
    }
}

