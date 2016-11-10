/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.collect.AbstractMapEntry;
import java.io.Serializable;

class ImmutableEntry<K, V>
extends AbstractMapEntry<K, V>
implements Serializable {
    final K key;
    final V value;

    ImmutableEntry(K k2, V v2) {
        this.key = k2;
        this.value = v2;
    }

    @Override
    public final K getKey() {
        return this.key;
    }

    @Override
    public final V getValue() {
        return this.value;
    }

    @Override
    public final V setValue(V v2) {
        throw new UnsupportedOperationException();
    }
}

