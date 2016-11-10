/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.util.Map;

final class SingletonImmutableBiMap<K, V>
extends ImmutableBiMap<K, V> {
    final transient K singleKey;
    final transient V singleValue;
    transient ImmutableBiMap<V, K> inverse;

    SingletonImmutableBiMap(K k2, V v2) {
        CollectPreconditions.checkEntryNotNull(k2, v2);
        this.singleKey = k2;
        this.singleValue = v2;
    }

    private SingletonImmutableBiMap(K k2, V v2, ImmutableBiMap<V, K> immutableBiMap) {
        this.singleKey = k2;
        this.singleValue = v2;
        this.inverse = immutableBiMap;
    }

    @Override
    public V get(Object object) {
        return this.singleKey.equals(object) ? (V)this.singleValue : null;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean containsKey(Object object) {
        return this.singleKey.equals(object);
    }

    @Override
    public boolean containsValue(Object object) {
        return this.singleValue.equals(object);
    }

    @Override
    boolean isPartialView() {
        return false;
    }

    @Override
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return ImmutableSet.of(Maps.immutableEntry(this.singleKey, this.singleValue));
    }

    @Override
    ImmutableSet<K> createKeySet() {
        return ImmutableSet.of(this.singleKey);
    }

    @Override
    public ImmutableBiMap<V, K> inverse() {
        ImmutableBiMap<K, V> immutableBiMap = this.inverse;
        if (immutableBiMap == null) {
            this.inverse = new SingletonImmutableBiMap<K, V>(this.singleValue, this.singleKey, this);
            return this.inverse;
        }
        return immutableBiMap;
    }
}

