/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  com.google.j2objc.annotations.Weak
 */
package com.google.common.collect;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.RegularImmutableAsList;
import com.google.common.collect.UnmodifiableIterator;
import com.google.j2objc.annotations.Weak;
import java.util.Iterator;
import java.util.Map;

abstract class ImmutableMapEntrySet<K, V>
extends ImmutableSet<Map.Entry<K, V>> {
    ImmutableMapEntrySet() {
    }

    abstract ImmutableMap<K, V> map();

    @Override
    public int size() {
        return this.map().size();
    }

    @Override
    public boolean contains(Object object) {
        if (object instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry)object;
            V v2 = this.map().get(entry.getKey());
            return v2 != null && v2.equals(entry.getValue());
        }
        return false;
    }

    @Override
    boolean isPartialView() {
        return this.map().isPartialView();
    }

    @Override
    boolean isHashCodeFast() {
        return this.map().isHashCodeFast();
    }

    @Override
    public int hashCode() {
        return this.map().hashCode();
    }

    static final class RegularEntrySet<K, V>
    extends ImmutableMapEntrySet<K, V> {
        @Weak
        private final transient ImmutableMap<K, V> map;
        private final transient Map.Entry<K, V>[] entries;

        RegularEntrySet(ImmutableMap<K, V> immutableMap, Map.Entry<K, V>[] arrentry) {
            this.map = immutableMap;
            this.entries = arrentry;
        }

        @Override
        ImmutableMap<K, V> map() {
            return this.map;
        }

        @Override
        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            return this.asList().iterator();
        }

        @Override
        ImmutableList<Map.Entry<K, V>> createAsList() {
            return new RegularImmutableAsList<Map.Entry<K, V>>(this, this.entries);
        }
    }

}

