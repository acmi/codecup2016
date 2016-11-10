/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.RegularImmutableBiMap;
import com.google.common.collect.SingletonImmutableBiMap;
import java.util.Collection;
import java.util.Set;

public abstract class ImmutableBiMap<K, V>
extends ImmutableMap<K, V>
implements BiMap<K, V> {
    public static <K, V> ImmutableBiMap<K, V> of() {
        return RegularImmutableBiMap.EMPTY;
    }

    public static <K, V> ImmutableBiMap<K, V> of(K k2, V v2) {
        return new SingletonImmutableBiMap<K, V>(k2, v2);
    }

    ImmutableBiMap() {
    }

    public abstract ImmutableBiMap<V, K> inverse();

    @Override
    public ImmutableSet<V> values() {
        return this.inverse().keySet();
    }
}

