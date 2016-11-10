/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Objects;
import java.util.Map;

abstract class AbstractMapEntry<K, V>
implements Map.Entry<K, V> {
    AbstractMapEntry() {
    }

    @Override
    public abstract K getKey();

    @Override
    public abstract V getValue();

    @Override
    public V setValue(V v2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry)object;
            return Objects.equal(this.getKey(), entry.getKey()) && Objects.equal(this.getValue(), entry.getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        K k2 = this.getKey();
        V v2 = this.getValue();
        return (k2 == null ? 0 : k2.hashCode()) ^ (v2 == null ? 0 : v2.hashCode());
    }

    public String toString() {
        return this.getKey() + "=" + this.getValue();
    }
}

