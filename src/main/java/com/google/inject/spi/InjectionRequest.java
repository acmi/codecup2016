/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.ConfigurationException;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Element;
import com.google.inject.spi.ElementVisitor;
import com.google.inject.spi.InjectionPoint;
import java.util.Set;

public final class InjectionRequest<T>
implements Element {
    private final Object source;
    private final TypeLiteral<T> type;
    private final T instance;

    public InjectionRequest(Object object, TypeLiteral<T> typeLiteral, T t2) {
        this.source = Preconditions.checkNotNull(object, "source");
        this.type = Preconditions.checkNotNull(typeLiteral, "type");
        this.instance = Preconditions.checkNotNull(t2, "instance");
    }

    @Override
    public Object getSource() {
        return this.source;
    }

    public T getInstance() {
        return this.instance;
    }

    public TypeLiteral<T> getType() {
        return this.type;
    }

    public Set<InjectionPoint> getInjectionPoints() throws ConfigurationException {
        return InjectionPoint.forInstanceMethodsAndFields(this.instance.getClass());
    }

    public <R> R acceptVisitor(ElementVisitor<R> elementVisitor) {
        return elementVisitor.visit(this);
    }

    @Override
    public void applyTo(Binder binder) {
        binder.withSource(this.getSource()).requestInjection(this.type, this.instance);
    }
}

