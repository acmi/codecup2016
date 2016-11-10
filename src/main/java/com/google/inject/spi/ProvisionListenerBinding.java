/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.common.collect.ImmutableList;
import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.Element;
import com.google.inject.spi.ElementVisitor;
import com.google.inject.spi.ProvisionListener;
import java.util.List;

public final class ProvisionListenerBinding
implements Element {
    private final Object source;
    private final Matcher<? super Binding<?>> bindingMatcher;
    private final List<ProvisionListener> listeners;

    ProvisionListenerBinding(Object object, Matcher<? super Binding<?>> matcher, ProvisionListener[] arrprovisionListener) {
        this.source = object;
        this.bindingMatcher = matcher;
        this.listeners = ImmutableList.copyOf(arrprovisionListener);
    }

    public List<ProvisionListener> getListeners() {
        return this.listeners;
    }

    public Matcher<? super Binding<?>> getBindingMatcher() {
        return this.bindingMatcher;
    }

    @Override
    public Object getSource() {
        return this.source;
    }

    public <R> R acceptVisitor(ElementVisitor<R> elementVisitor) {
        return elementVisitor.visit(this);
    }

    @Override
    public void applyTo(Binder binder) {
        binder.withSource(this.getSource()).bindListener(this.bindingMatcher, this.listeners.toArray(new ProvisionListener[this.listeners.size()]));
    }
}

