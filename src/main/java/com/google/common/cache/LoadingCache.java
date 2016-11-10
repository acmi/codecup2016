/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.cache;

import com.google.common.base.Function;
import com.google.common.cache.Cache;
import java.util.concurrent.ConcurrentMap;

public interface LoadingCache<K, V>
extends Function<K, V>,
Cache<K, V> {
    public V getUnchecked(K var1);

    public ConcurrentMap<K, V> asMap();
}

