/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.ProvisionException;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InternalContext;
import com.google.inject.spi.DependencyAndSource;
import com.google.inject.spi.Message;
import com.google.inject.spi.ProvisionListener;
import java.util.LinkedHashSet;
import java.util.List;

final class ProvisionListenerStackCallback<T> {
    private static final ProvisionListener[] EMPTY_LISTENER = new ProvisionListener[0];
    private static final ProvisionListenerStackCallback<?> EMPTY_CALLBACK = new ProvisionListenerStackCallback<T>(null, ImmutableList.<ProvisionListener>of());
    private final ProvisionListener[] listeners;
    private final Binding<T> binding;

    public static <T> ProvisionListenerStackCallback<T> emptyListener() {
        return EMPTY_CALLBACK;
    }

    public ProvisionListenerStackCallback(Binding<T> binding, List<ProvisionListener> list) {
        this.binding = binding;
        if (list.isEmpty()) {
            this.listeners = EMPTY_LISTENER;
        } else {
            LinkedHashSet<ProvisionListener> linkedHashSet = Sets.newLinkedHashSet(list);
            this.listeners = linkedHashSet.toArray(new ProvisionListener[linkedHashSet.size()]);
        }
    }

    public boolean hasListeners() {
        return this.listeners.length > 0;
    }

    public T provision(Errors errors, InternalContext internalContext, ProvisionCallback<T> provisionCallback) throws ErrorsException {
        Provision provision = new Provision(errors, internalContext, provisionCallback);
        RuntimeException runtimeException = null;
        try {
            provision.provision();
        }
        catch (RuntimeException runtimeException2) {
            runtimeException = runtimeException2;
        }
        if (provision.exceptionDuringProvision != null) {
            throw provision.exceptionDuringProvision;
        }
        if (runtimeException != null) {
            String string = provision.erredListener != null ? provision.erredListener.getClass() : "(unknown)";
            throw errors.errorInUserCode(runtimeException, "Error notifying ProvisionListener %s of %s.%n Reason: %s", string, this.binding.getKey(), runtimeException).toException();
        }
        return provision.result;
    }

    private class Provision
    extends ProvisionListener.ProvisionInvocation<T> {
        final Errors errors;
        final int numErrorsBefore;
        final InternalContext context;
        final ProvisionCallback<T> callable;
        int index;
        T result;
        ErrorsException exceptionDuringProvision;
        ProvisionListener erredListener;

        public Provision(Errors errors, InternalContext internalContext, ProvisionCallback<T> provisionCallback) {
            this.index = -1;
            this.callable = provisionCallback;
            this.context = internalContext;
            this.errors = errors;
            this.numErrorsBefore = errors.size();
        }

        @Override
        public T provision() {
            ++this.index;
            if (this.index == ProvisionListenerStackCallback.this.listeners.length) {
                try {
                    this.result = this.callable.call();
                    this.errors.throwIfNewErrors(this.numErrorsBefore);
                }
                catch (ErrorsException errorsException) {
                    this.exceptionDuringProvision = errorsException;
                    throw new ProvisionException(this.errors.merge(errorsException.getErrors()).getMessages());
                }
            }
            if (this.index < ProvisionListenerStackCallback.this.listeners.length) {
                int n2 = this.index;
                try {
                    ProvisionListenerStackCallback.this.listeners[this.index].onProvision(this);
                }
                catch (RuntimeException runtimeException) {
                    this.erredListener = ProvisionListenerStackCallback.this.listeners[n2];
                    throw runtimeException;
                }
                if (n2 == this.index) {
                    this.provision();
                }
            } else {
                throw new IllegalStateException("Already provisioned in this listener.");
            }
            return this.result;
        }

        @Override
        public Binding<T> getBinding() {
            return ProvisionListenerStackCallback.this.binding;
        }

        @Override
        public List<DependencyAndSource> getDependencyChain() {
            return this.context.getDependencyChain();
        }
    }

    public static interface ProvisionCallback<T> {
        public T call() throws ErrorsException;
    }

}

