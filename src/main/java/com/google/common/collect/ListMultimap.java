/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.collect.Multimap;
import java.util.List;

public interface ListMultimap<K, V>
extends Multimap<K, V> {
    @Override
    public List<V> get(K var1);
}

