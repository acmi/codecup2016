/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.cache;

public interface Weigher<K, V> {
    public int weigh(K var1, V var2);
}

