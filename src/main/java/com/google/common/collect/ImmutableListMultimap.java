/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.collect.EmptyImmutableListMultimap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ListMultimap;
import java.util.Collection;
import java.util.List;

public class ImmutableListMultimap<K, V>
extends ImmutableMultimap<K, V>
implements ListMultimap<K, V> {
    public static <K, V> ImmutableListMultimap<K, V> of() {
        return EmptyImmutableListMultimap.INSTANCE;
    }

    ImmutableListMultimap(ImmutableMap<K, ImmutableList<V>> immutableMap, int n2) {
        super(immutableMap, n2);
    }

    @Override
    public ImmutableList<V> get(K k2) {
        ImmutableList immutableList = (ImmutableList)this.map.get(k2);
        return immutableList == null ? ImmutableList.of() : immutableList;
    }

    @Deprecated
    @Override
    public ImmutableList<V> removeAll(Object object) {
        throw new UnsupportedOperationException();
    }
}

