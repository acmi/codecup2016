/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.CycleDetectingLock;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.Initializable;
import com.google.inject.internal.Initializables;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.MembersInjectorImpl;
import com.google.inject.internal.MembersInjectorStore;
import com.google.inject.internal.ProvisionListenerCallbackStore;
import com.google.inject.internal.ProvisionListenerStackCallback;
import com.google.inject.spi.InjectionPoint;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

final class Initializer {
    private volatile boolean validationStarted = false;
    private final CycleDetectingLock.CycleDetectingLockFactory<Class<?>> cycleDetectingLockFactory = new CycleDetectingLock.CycleDetectingLockFactory();
    private final List<InjectableReference<?>> pendingInjections = Lists.newArrayList();
    private final IdentityHashMap<Object, InjectableReference<?>> initializablesCache = Maps.newIdentityHashMap();

    Initializer() {
    }

    <T> Initializable<T> requestInjection(InjectorImpl injectorImpl, T t2, Binding<T> binding, Object object, Set<InjectionPoint> set) {
        ProvisionListenerStackCallback<T> provisionListenerStackCallback;
        Preconditions.checkNotNull(object);
        Preconditions.checkState(!this.validationStarted, "Member injection could not be requested after validation is started");
        ProvisionListenerStackCallback<T> provisionListenerStackCallback2 = provisionListenerStackCallback = binding == null ? null : injectorImpl.provisionListenerStore.get(binding);
        if (t2 == null || set.isEmpty() && !injectorImpl.membersInjectorStore.hasTypeListeners() && (provisionListenerStackCallback == null || !provisionListenerStackCallback.hasListeners())) {
            return Initializables.of(t2);
        }
        if (this.initializablesCache.containsKey(t2)) {
            Initializable initializable = this.initializablesCache.get(t2);
            return initializable;
        }
        InjectableReference<T> injectableReference = new InjectableReference<T>(injectorImpl, t2, binding == null ? null : binding.getKey(), provisionListenerStackCallback, object, this.cycleDetectingLockFactory.create(t2.getClass()));
        this.initializablesCache.put(t2, injectableReference);
        this.pendingInjections.add(injectableReference);
        return injectableReference;
    }

    void validateOustandingInjections(Errors errors) {
        this.validationStarted = true;
        this.initializablesCache.clear();
        for (InjectableReference injectableReference : this.pendingInjections) {
            try {
                injectableReference.validate(errors);
            }
            catch (ErrorsException errorsException) {
                errors.merge(errorsException.getErrors());
            }
        }
    }

    void injectAll(Errors errors) {
        Preconditions.checkState(this.validationStarted, "Validation should be done before injection");
        for (InjectableReference injectableReference : this.pendingInjections) {
            try {
                injectableReference.get(errors);
            }
            catch (ErrorsException errorsException) {
                errors.merge(errorsException.getErrors());
            }
        }
        this.pendingInjections.clear();
    }

    private static class InjectableReference<T>
    implements Initializable<T> {
        private volatile InjectableReferenceState state = InjectableReferenceState.NEW;
        private volatile MembersInjectorImpl<T> membersInjector = null;
        private final InjectorImpl injector;
        private final T instance;
        private final Object source;
        private final Key<T> key;
        private final ProvisionListenerStackCallback<T> provisionCallback;
        private final CycleDetectingLock<?> lock;

        public InjectableReference(InjectorImpl injectorImpl, T t2, Key<T> key, ProvisionListenerStackCallback<T> provisionListenerStackCallback, Object object, CycleDetectingLock<?> cycleDetectingLock) {
            this.injector = injectorImpl;
            this.key = key;
            this.provisionCallback = provisionListenerStackCallback;
            this.instance = Preconditions.checkNotNull(t2, "instance");
            this.source = Preconditions.checkNotNull(object, "source");
            this.lock = Preconditions.checkNotNull(cycleDetectingLock, "lock");
        }

        public void validate(Errors errors) throws ErrorsException {
            TypeLiteral typeLiteral = TypeLiteral.get(this.instance.getClass());
            this.membersInjector = this.injector.membersInjectorStore.get(typeLiteral, errors.withSource(this.source));
            Preconditions.checkNotNull(this.membersInjector, "No membersInjector available for instance: %s, from key: %s", this.instance, this.key);
            this.state = InjectableReferenceState.VALIDATED;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public T get(Errors errors) throws ErrorsException {
            if (this.state == InjectableReferenceState.READY) {
                return this.instance;
            }
            ListMultimap listMultimap = this.lock.lockOrDetectPotentialLocksCycle();
            if (!listMultimap.isEmpty()) {
                return this.instance;
            }
            try {
                switch (this.state) {
                    case READY: {
                        T t2 = this.instance;
                        return t2;
                    }
                    case INJECTING: {
                        T t3 = this.instance;
                        return t3;
                    }
                    case VALIDATED: {
                        this.state = InjectableReferenceState.INJECTING;
                        break;
                    }
                    case NEW: {
                        throw new IllegalStateException("InjectableReference is not validated yet");
                    }
                    default: {
                        throw new IllegalStateException("Unknown state: " + (Object)((Object)this.state));
                    }
                }
                this.membersInjector.injectAndNotify(this.instance, errors.withSource(this.source), this.key, this.provisionCallback, this.source, this.injector.options.stage == Stage.TOOL);
                this.state = InjectableReferenceState.READY;
                T t4 = this.instance;
                return t4;
            }
            finally {
                this.lock.unlock();
            }
        }

        public String toString() {
            return this.instance.toString();
        }
    }

    private static enum InjectableReferenceState {
        NEW,
        VALIDATED,
        INJECTING,
        READY;
        

        private InjectableReferenceState() {
        }
    }

}

