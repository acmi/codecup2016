/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.collection;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MapBuilder<K, V> {
    private final Map<K, V> map;

    public MapBuilder() {
        this.map = new HashMap();
    }

    public MapBuilder(Map<K, V> map) {
        this.map = map;
    }

    public MapBuilder<K, V> put(K k2, V v2) {
        this.map.put(k2, v2);
        return this;
    }

    public Map<K, V> buildUnmodifiable() {
        return Collections.unmodifiableMap(this.map);
    }
}

