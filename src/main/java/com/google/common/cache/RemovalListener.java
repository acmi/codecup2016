/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.cache;

import com.google.common.cache.RemovalNotification;

public interface RemovalListener<K, V> {
    public void onRemoval(RemovalNotification<K, V> var1);
}

