/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.internal.AbstractProcessor;
import com.google.inject.internal.Errors;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.State;
import com.google.inject.spi.ProvisionListenerBinding;
import com.google.inject.spi.TypeListenerBinding;

final class ListenerBindingProcessor
extends AbstractProcessor {
    ListenerBindingProcessor(Errors errors) {
        super(errors);
    }

    @Override
    public Boolean visit(TypeListenerBinding typeListenerBinding) {
        this.injector.state.addTypeListener(typeListenerBinding);
        return true;
    }

    @Override
    public Boolean visit(ProvisionListenerBinding provisionListenerBinding) {
        this.injector.state.addProvisionListener(provisionListenerBinding);
        return true;
    }
}

