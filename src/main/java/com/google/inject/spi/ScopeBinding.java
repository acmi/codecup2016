/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.Scope;
import com.google.inject.spi.Element;
import com.google.inject.spi.ElementVisitor;
import java.lang.annotation.Annotation;

public final class ScopeBinding
implements Element {
    private final Object source;
    private final Class<? extends Annotation> annotationType;
    private final Scope scope;

    ScopeBinding(Object object, Class<? extends Annotation> class_, Scope scope) {
        this.source = Preconditions.checkNotNull(object, "source");
        this.annotationType = Preconditions.checkNotNull(class_, "annotationType");
        this.scope = Preconditions.checkNotNull(scope, "scope");
    }

    @Override
    public Object getSource() {
        return this.source;
    }

    public Class<? extends Annotation> getAnnotationType() {
        return this.annotationType;
    }

    public Scope getScope() {
        return this.scope;
    }

    @Override
    public <T> T acceptVisitor(ElementVisitor<T> elementVisitor) {
        return elementVisitor.visit(this);
    }

    @Override
    public void applyTo(Binder binder) {
        binder.withSource(this.getSource()).bindScope(this.annotationType, this.scope);
    }
}

