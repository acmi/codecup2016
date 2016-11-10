/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.cache;

public interface Cache<K, V> {
    public V getIfPresent(Object var1);

    public void put(K var1, V var2);

    public void cleanUp();
}

