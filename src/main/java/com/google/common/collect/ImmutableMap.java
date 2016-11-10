/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableEnumMap;
import com.google.common.collect.ImmutableMapEntry;
import com.google.common.collect.ImmutableMapEntrySet;
import com.google.common.collect.ImmutableMapKeySet;
import com.google.common.collect.ImmutableMapValues;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Ordering;
import com.google.common.collect.RegularImmutableMap;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public abstract class ImmutableMap<K, V>
implements Serializable,
Map<K, V> {
    static final Map.Entry<?, ?>[] EMPTY_ENTRY_ARRAY = new Map.Entry[0];
    private transient ImmutableSet<Map.Entry<K, V>> entrySet;
    private transient ImmutableSet<K> keySet;
    private transient ImmutableCollection<V> values;

    public static <K, V> ImmutableMap<K, V> of() {
        return ImmutableBiMap.of();
    }

    public static <K, V> ImmutableMap<K, V> of(K k2, V v2) {
        return ImmutableBiMap.of(k2, v2);
    }

    static <K, V> ImmutableMapEntry<K, V> entryOf(K k2, V v2) {
        return new ImmutableMapEntry<K, V>(k2, v2);
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    static void checkNoConflict(boolean bl, String string, Map.Entry<?, ?> entry, Map.Entry<?, ?> entry2) {
        if (!bl) {
            throw new IllegalArgumentException("Multiple entries with same " + string + ": " + entry + " and " + entry2);
        }
    }

    public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        if (map instanceof ImmutableMap && !(map instanceof ImmutableSortedMap)) {
            ImmutableMap immutableMap = (ImmutableMap)map;
            if (!immutableMap.isPartialView()) {
                return immutableMap;
            }
        } else if (map instanceof EnumMap) {
            ImmutableMap<K, V> immutableMap = ImmutableMap.copyOfEnumMap((EnumMap)map);
            return immutableMap;
        }
        return ImmutableMap.copyOf(map.entrySet());
    }

    public static <K, V> ImmutableMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> iterable) {
        Map.Entry<?, ?>[] arrentry = Iterables.toArray(iterable, EMPTY_ENTRY_ARRAY);
        switch (arrentry.length) {
            case 0: {
                return ImmutableMap.of();
            }
            case 1: {
                Map.Entry entry = arrentry[0];
                return ImmutableMap.of(entry.getKey(), entry.getValue());
            }
        }
        return RegularImmutableMap.fromEntries(arrentry);
    }

    private static <K extends Enum<K>, V> ImmutableMap<K, V> copyOfEnumMap(EnumMap<K, ? extends V> enumMap) {
        EnumMap<K, V> enumMap2 = new EnumMap<K, V>(enumMap);
        for (Map.Entry<K, V> entry : enumMap2.entrySet()) {
            CollectPreconditions.checkEntryNotNull(entry.getKey(), entry.getValue());
        }
        return ImmutableEnumMap.asImmutable(enumMap2);
    }

    ImmutableMap() {
    }

    @Deprecated
    @Override
    public final V put(K k2, V v2) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public final V remove(Object object) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public final void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean containsKey(Object object) {
        return this.get(object) != null;
    }

    @Override
    public boolean containsValue(Object object) {
        return this.values().contains(object);
    }

    @Override
    public abstract V get(Object var1);

    @Override
    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        ImmutableSet<Map.Entry<K, V>> immutableSet = this.entrySet;
        ImmutableSet<Map.Entry<K, V>> immutableSet2 = immutableSet == null ? (this.entrySet = this.createEntrySet()) : immutableSet;
        return immutableSet2;
    }

    abstract ImmutableSet<Map.Entry<K, V>> createEntrySet();

    @Override
    public ImmutableSet<K> keySet() {
        ImmutableSet<K> immutableSet = this.keySet;
        ImmutableSet<K> immutableSet2 = immutableSet == null ? (this.keySet = this.createKeySet()) : immutableSet;
        return immutableSet2;
    }

    ImmutableSet<K> createKeySet() {
        return this.isEmpty() ? ImmutableSet.of() : new ImmutableMapKeySet<K, V>(this);
    }

    UnmodifiableIterator<K> keyIterator() {
        Iterator iterator = this.entrySet().iterator();
        return new UnmodifiableIterator<K>((UnmodifiableIterator)iterator){
            final /* synthetic */ UnmodifiableIterator val$entryIterator;

            @Override
            public boolean hasNext() {
                return this.val$entryIterator.hasNext();
            }

            @Override
            public K next() {
                return ((Map.Entry)this.val$entryIterator.next()).getKey();
            }
        };
    }

    @Override
    public ImmutableCollection<V> values() {
        ImmutableCollection<V> immutableCollection = this.values;
        ImmutableCollection<V> immutableCollection2 = immutableCollection == null ? (this.values = new ImmutableMapValues<K, V>(this)) : immutableCollection;
        return immutableCollection2;
    }

    @Override
    public boolean equals(Object object) {
        return Maps.equalsImpl(this, object);
    }

    abstract boolean isPartialView();

    @Override
    public int hashCode() {
        return Sets.hashCodeImpl(this.entrySet());
    }

    boolean isHashCodeFast() {
        return false;
    }

    public String toString() {
        return Maps.toStringImpl(this);
    }

    static abstract class IteratorBasedImmutableMap<K, V>
    extends ImmutableMap<K, V> {
        IteratorBasedImmutableMap() {
        }

        abstract UnmodifiableIterator<Map.Entry<K, V>> entryIterator();

        @Override
        ImmutableSet<Map.Entry<K, V>> createEntrySet() {
            class EntrySetImpl
            extends ImmutableMapEntrySet<K, V> {
                EntrySetImpl() {
                }

                @Override
                ImmutableMap<K, V> map() {
                    return IteratorBasedImmutableMap.this;
                }

                @Override
                public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
                    return IteratorBasedImmutableMap.this.entryIterator();
                }
            }
            return new EntrySetImpl();
        }

    }

    public static class Builder<K, V> {
        Comparator<? super V> valueComparator;
        ImmutableMapEntry<K, V>[] entries;
        int size;
        boolean entriesUsed;

        public Builder() {
            this(4);
        }

        Builder(int n2) {
            this.entries = new ImmutableMapEntry[n2];
            this.size = 0;
            this.entriesUsed = false;
        }

        private void ensureCapacity(int n2) {
            if (n2 > this.entries.length) {
                this.entries = ObjectArrays.arraysCopyOf(this.entries, ImmutableCollection.Builder.expandedCapacity(this.entries.length, n2));
                this.entriesUsed = false;
            }
        }

        public Builder<K, V> put(K k2, V v2) {
            this.ensureCapacity(this.size + 1);
            ImmutableMapEntry<K, V> immutableMapEntry = ImmutableMap.entryOf(k2, v2);
            this.entries[this.size++] = immutableMapEntry;
            return this;
        }

        public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
            return this.put(entry.getKey(), entry.getValue());
        }

        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            return this.putAll(map.entrySet());
        }

        public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> iterable) {
            if (iterable instanceof Collection) {
                this.ensureCapacity(this.size + ((Collection)iterable).size());
            }
            for (Map.Entry<K, V> entry : iterable) {
                this.put(entry);
            }
            return this;
        }

        public ImmutableMap<K, V> build() {
            switch (this.size) {
                case 0: {
                    return ImmutableMap.of();
                }
                case 1: {
                    return ImmutableMap.of(this.entries[0].getKey(), this.entries[0].getValue());
                }
            }
            if (this.valueComparator != null) {
                if (this.entriesUsed) {
                    this.entries = ObjectArrays.arraysCopyOf(this.entries, this.size);
                }
                Arrays.sort(this.entries, 0, this.size, Ordering.from(this.valueComparator).onResultOf(Maps.valueFunction()));
            }
            this.entriesUsed = this.size == this.entries.length;
            return RegularImmutableMap.fromEntryArray(this.size, this.entries);
        }
    }

}

