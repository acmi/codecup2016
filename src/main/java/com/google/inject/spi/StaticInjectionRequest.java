/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.ConfigurationException;
import com.google.inject.spi.Element;
import com.google.inject.spi.ElementVisitor;
import com.google.inject.spi.InjectionPoint;
import java.util.Set;

public final class StaticInjectionRequest
implements Element {
    private final Object source;
    private final Class<?> type;

    StaticInjectionRequest(Object object, Class<?> class_) {
        this.source = Preconditions.checkNotNull(object, "source");
        this.type = Preconditions.checkNotNull(class_, "type");
    }

    @Override
    public Object getSource() {
        return this.source;
    }

    public Class<?> getType() {
        return this.type;
    }

    public Set<InjectionPoint> getInjectionPoints() throws ConfigurationException {
        return InjectionPoint.forStaticMethodsAndFields(this.type);
    }

    @Override
    public void applyTo(Binder binder) {
        binder.withSource(this.getSource()).requestStaticInjection(this.type);
    }

    @Override
    public <T> T acceptVisitor(ElementVisitor<T> elementVisitor) {
        return elementVisitor.visit(this);
    }
}

