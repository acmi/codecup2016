/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.spi.Element;
import com.google.inject.spi.ElementVisitor;

public final class RequireExactBindingAnnotationsOption
implements Element {
    private final Object source;

    RequireExactBindingAnnotationsOption(Object object) {
        this.source = Preconditions.checkNotNull(object, "source");
    }

    @Override
    public Object getSource() {
        return this.source;
    }

    @Override
    public void applyTo(Binder binder) {
        binder.withSource(this.getSource()).requireExactBindingAnnotations();
    }

    @Override
    public <T> T acceptVisitor(ElementVisitor<T> elementVisitor) {
        return elementVisitor.visit(this);
    }
}
