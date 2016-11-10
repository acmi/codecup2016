/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Hashing;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMapEntry;
import com.google.common.collect.ImmutableMapEntrySet;
import com.google.common.collect.ImmutableSet;
import java.util.Map;

final class RegularImmutableMap<K, V>
extends ImmutableMap<K, V> {
    private final transient Map.Entry<K, V>[] entries;
    private final transient ImmutableMapEntry<K, V>[] table;
    private final transient int mask;

    static /* varargs */ <K, V> RegularImmutableMap<K, V> fromEntries(Map.Entry<K, V> ... arrentry) {
        return RegularImmutableMap.fromEntryArray(arrentry.length, arrentry);
    }

    static <K, V> RegularImmutableMap<K, V> fromEntryArray(int n2, Map.Entry<K, V>[] arrentry) {
        Preconditions.checkPositionIndex(n2, arrentry.length);
        Map.Entry<K, V>[] arrentry2 = n2 == arrentry.length ? arrentry : ImmutableMapEntry.createEntryArray(n2);
        int n3 = Hashing.closedTableSize(n2, 1.2);
        ImmutableMapEntry<K, V>[] arrimmutableMapEntry = ImmutableMapEntry.createEntryArray(n3);
        int n4 = n3 - 1;
        for (int i2 = 0; i2 < n2; ++i2) {
            ImmutableMapEntry immutableMapEntry2;
            ImmutableMapEntry immutableMapEntry2;
            Map.Entry<K, V> entry = arrentry[i2];
            K k2 = entry.getKey();
            V v2 = entry.getValue();
            CollectPreconditions.checkEntryNotNull(k2, v2);
            int n5 = Hashing.smear(k2.hashCode()) & n4;
            ImmutableMapEntry immutableMapEntry3 = arrimmutableMapEntry[n5];
            if (immutableMapEntry3 == null) {
                boolean bl = entry instanceof ImmutableMapEntry && ((ImmutableMapEntry)entry).isReusable();
                immutableMapEntry2 = bl ? (ImmutableMapEntry)entry : new ImmutableMapEntry(k2, v2);
            } else {
                immutableMapEntry2 = new ImmutableMapEntry.NonTerminalImmutableMapEntry<K, V>(k2, v2, immutableMapEntry3);
            }
            arrimmutableMapEntry[n5] = immutableMapEntry2;
            arrentry2[i2] = immutableMapEntry2;
            RegularImmutableMap.checkNoConflictInKeyBucket(k2, immutableMapEntry2, immutableMapEntry3);
        }
        return new RegularImmutableMap<K, V>(arrentry2, arrimmutableMapEntry, n4);
    }

    private RegularImmutableMap(Map.Entry<K, V>[] arrentry, ImmutableMapEntry<K, V>[] arrimmutableMapEntry, int n2) {
        this.entries = arrentry;
        this.table = arrimmutableMapEntry;
        this.mask = n2;
    }

    static void checkNoConflictInKeyBucket(Object object, Map.Entry<?, ?> entry, ImmutableMapEntry<?, ?> immutableMapEntry) {
        while (immutableMapEntry != null) {
            RegularImmutableMap.checkNoConflict(!object.equals(immutableMapEntry.getKey()), "key", entry, immutableMapEntry);
            immutableMapEntry = immutableMapEntry.getNextInKeyBucket();
        }
    }

    @Override
    public V get(Object object) {
        return RegularImmutableMap.get(object, this.table, this.mask);
    }

    static <V> V get(Object object, ImmutableMapEntry<?, V>[] arrimmutableMapEntry, int n2) {
        if (object == null) {
            return null;
        }
        int n3 = Hashing.smear(object.hashCode()) & n2;
        for (ImmutableMapEntry immutableMapEntry = arrimmutableMapEntry[n3]; immutableMapEntry != null; immutableMapEntry = immutableMapEntry.getNextInKeyBucket()) {
            Object obj = immutableMapEntry.getKey();
            if (!object.equals(obj)) continue;
            return immutableMapEntry.getValue();
        }
        return null;
    }

    @Override
    public int size() {
        return this.entries.length;
    }

    @Override
    boolean isPartialView() {
        return false;
    }

    @Override
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return new ImmutableMapEntrySet.RegularEntrySet<K, V>(this, this.entries);
    }
}

