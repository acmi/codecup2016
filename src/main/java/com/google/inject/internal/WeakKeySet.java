/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import com.google.inject.Key;
import com.google.inject.internal.Errors;
import com.google.inject.internal.State;
import com.google.inject.internal.util.SourceProvider;
import java.util.Map;
import java.util.Set;

final class WeakKeySet {
    private Map<Key<?>, Multiset<Object>> backingMap;
    private final Object lock;
    private final Cache<State, Set<KeyAndSource>> evictionCache;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void cleanUpForCollectedState(Set<KeyAndSource> set) {
        Object object = this.lock;
        synchronized (object) {
            for (KeyAndSource keyAndSource : set) {
                Multiset<Object> multiset = this.backingMap.get(keyAndSource.key);
                if (multiset == null) continue;
                multiset.remove(keyAndSource.source);
                if (!multiset.isEmpty()) continue;
                this.backingMap.remove(keyAndSource.key);
            }
        }
    }

    WeakKeySet(Object object) {
        this.evictionCache = CacheBuilder.newBuilder().weakKeys().removalListener(new RemovalListener<State, Set<KeyAndSource>>(){

            @Override
            public void onRemoval(RemovalNotification<State, Set<KeyAndSource>> removalNotification) {
                Preconditions.checkState(RemovalCause.COLLECTED.equals((Object)removalNotification.getCause()));
                WeakKeySet.this.cleanUpForCollectedState(removalNotification.getValue());
            }
        }).build();
        this.lock = object;
    }

    public void add(Key<?> key, State state, Object object) {
        Multiset<Object> multiset;
        if (this.backingMap == null) {
            this.backingMap = Maps.newHashMap();
        }
        if (object instanceof Class || object == SourceProvider.UNKNOWN_SOURCE) {
            object = null;
        }
        if ((multiset = this.backingMap.get(key)) == null) {
            multiset = LinkedHashMultiset.create();
            this.backingMap.put(key, multiset);
        }
        Object object2 = Errors.convert(object);
        multiset.add(object2);
        if (state.parent() != State.NONE) {
            Set<KeyAndSource> set = this.evictionCache.getIfPresent(state);
            if (set == null) {
                set = Sets.newHashSet();
                this.evictionCache.put(state, set);
            }
            set.add(new KeyAndSource(key, object2));
        }
    }

    public boolean contains(Key<?> key) {
        this.evictionCache.cleanUp();
        return this.backingMap != null && this.backingMap.containsKey(key);
    }

    public Set<Object> getSources(Key<?> key) {
        this.evictionCache.cleanUp();
        Multiset<Object> multiset = this.backingMap == null ? null : this.backingMap.get(key);
        return multiset == null ? null : multiset.elementSet();
    }

    private static final class KeyAndSource {
        final Key<?> key;
        final Object source;

        KeyAndSource(Key<?> key, Object object) {
            this.key = key;
            this.source = object;
        }

        public int hashCode() {
            return Objects.hashCode(this.key, this.source);
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof KeyAndSource)) {
                return false;
            }
            KeyAndSource keyAndSource = (KeyAndSource)object;
            return Objects.equal(this.key, keyAndSource.key) && Objects.equal(this.source, keyAndSource.source);
        }
    }

}

