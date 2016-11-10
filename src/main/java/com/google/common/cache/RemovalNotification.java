/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.cache;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.cache.RemovalCause;
import java.util.Map;

public final class RemovalNotification<K, V>
implements Map.Entry<K, V> {
    private final K key;
    private final V value;
    private final RemovalCause cause;

    public static <K, V> RemovalNotification<K, V> create(K k2, V v2, RemovalCause removalCause) {
        return new RemovalNotification<K, V>(k2, v2, removalCause);
    }

    private RemovalNotification(K k2, V v2, RemovalCause removalCause) {
        this.key = k2;
        this.value = v2;
        this.cause = Preconditions.checkNotNull(removalCause);
    }

    public RemovalCause getCause() {
        return this.cause;
    }

    @Override
    public K getKey() {
        return this.key;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public final V setValue(V v2) {
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

