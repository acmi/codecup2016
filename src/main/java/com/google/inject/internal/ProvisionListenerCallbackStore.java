/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Stage;
import com.google.inject.internal.ProvisionListenerStackCallback;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.ProvisionListener;
import com.google.inject.spi.ProvisionListenerBinding;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

final class ProvisionListenerCallbackStore {
    private static final Set<Key<?>> INTERNAL_BINDINGS = ImmutableSet.of(Key.get(Injector.class), Key.get(Stage.class), Key.get(Logger.class));
    private final ImmutableList<ProvisionListenerBinding> listenerBindings;
    private final LoadingCache<KeyBinding, ProvisionListenerStackCallback<?>> cache;

    ProvisionListenerCallbackStore(List<ProvisionListenerBinding> list) {
        this.cache = CacheBuilder.newBuilder().build(new CacheLoader<KeyBinding, ProvisionListenerStackCallback<?>>(){

            @Override
            public ProvisionListenerStackCallback<?> load(KeyBinding keyBinding) {
                return ProvisionListenerCallbackStore.this.create(keyBinding.binding);
            }
        });
        this.listenerBindings = ImmutableList.copyOf(list);
    }

    public <T> ProvisionListenerStackCallback<T> get(Binding<T> binding) {
        if (!INTERNAL_BINDINGS.contains(binding.getKey())) {
            return this.cache.getUnchecked(new KeyBinding(binding.getKey(), binding));
        }
        return ProvisionListenerStackCallback.emptyListener();
    }

    boolean remove(Binding<?> binding) {
        return this.cache.asMap().remove(binding) != null;
    }

    private <T> ProvisionListenerStackCallback<T> create(Binding<T> binding) {
        ArrayList<ProvisionListener> arrayList = null;
        for (ProvisionListenerBinding provisionListenerBinding : this.listenerBindings) {
            if (!provisionListenerBinding.getBindingMatcher().matches(binding)) continue;
            if (arrayList == null) {
                arrayList = Lists.newArrayList();
            }
            arrayList.addAll(provisionListenerBinding.getListeners());
        }
        if (arrayList == null || arrayList.isEmpty()) {
            return ProvisionListenerStackCallback.emptyListener();
        }
        return new ProvisionListenerStackCallback<T>(binding, arrayList);
    }

    private static class KeyBinding {
        final Key<?> key;
        final Binding<?> binding;

        KeyBinding(Key<?> key, Binding<?> binding) {
            this.key = key;
            this.binding = binding;
        }

        public boolean equals(Object object) {
            return object instanceof KeyBinding && this.key.equals(((KeyBinding)object).key);
        }

        public int hashCode() {
            return this.key.hashCode();
        }
    }

}

