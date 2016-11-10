/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableAsList;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMapEntrySet;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMapFauxverideShim;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.RegularImmutableSortedSet;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;

public final class ImmutableSortedMap<K, V>
extends ImmutableSortedMapFauxverideShim<K, V>
implements NavigableMap<K, V> {
    private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
    private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP = new ImmutableSortedMap(ImmutableSortedSet.emptySet(Ordering.natural()), ImmutableList.of());
    private final transient RegularImmutableSortedSet<K> keySet;
    private final transient ImmutableList<V> valueList;
    private transient ImmutableSortedMap<K, V> descendingMap;

    static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> comparator) {
        if (Ordering.natural().equals(comparator)) {
            return ImmutableSortedMap.of();
        }
        return new ImmutableSortedMap(ImmutableSortedSet.emptySet(comparator), ImmutableList.of());
    }

    public static <K, V> ImmutableSortedMap<K, V> of() {
        return NATURAL_EMPTY_MAP;
    }

    ImmutableSortedMap(RegularImmutableSortedSet<K> regularImmutableSortedSet, ImmutableList<V> immutableList) {
        this(regularImmutableSortedSet, immutableList, null);
    }

    ImmutableSortedMap(RegularImmutableSortedSet<K> regularImmutableSortedSet, ImmutableList<V> immutableList, ImmutableSortedMap<K, V> immutableSortedMap) {
        this.keySet = regularImmutableSortedSet;
        this.valueList = immutableList;
        this.descendingMap = immutableSortedMap;
    }

    @Override
    public int size() {
        return this.valueList.size();
    }

    @Override
    public V get(Object object) {
        int n2 = this.keySet.indexOf(object);
        return n2 == -1 ? null : (V)this.valueList.get(n2);
    }

    @Override
    boolean isPartialView() {
        return this.keySet.isPartialView() || this.valueList.isPartialView();
    }

    @Override
    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        return super.entrySet();
    }

    @Override
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        class EntrySet
        extends ImmutableMapEntrySet<K, V> {
            EntrySet() {
            }

            @Override
            public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
                return this.asList().iterator();
            }

            @Override
            ImmutableList<Map.Entry<K, V>> createAsList() {
                return new ImmutableAsList<Map.Entry<K, V>>(){

                    @Override
                    public Map.Entry<K, V> get(int n2) {
                        return Maps.immutableEntry(ImmutableSortedMap.this.keySet.asList().get(n2), ImmutableSortedMap.this.valueList.get(n2));
                    }

                    @Override
                    ImmutableCollection<Map.Entry<K, V>> delegateCollection() {
                        return 1EntrySet.this;
                    }
                };
            }

            @Override
            ImmutableMap<K, V> map() {
                return ImmutableSortedMap.this;
            }

        }
        return this.isEmpty() ? ImmutableSet.of() : new EntrySet();
    }

    @Override
    public ImmutableSortedSet<K> keySet() {
        return this.keySet;
    }

    @Override
    public ImmutableCollection<V> values() {
        return this.valueList;
    }

    @Override
    public Comparator<? super K> comparator() {
        return this.keySet().comparator();
    }

    @Override
    public K firstKey() {
        return (K)this.keySet().first();
    }

    @Override
    public K lastKey() {
        return (K)this.keySet().last();
    }

    private ImmutableSortedMap<K, V> getSubMap(int n2, int n3) {
        if (n2 == 0 && n3 == this.size()) {
            return this;
        }
        if (n2 == n3) {
            return ImmutableSortedMap.emptyMap(this.comparator());
        }
        return new ImmutableSortedMap<K, V>(this.keySet.getSubSet(n2, n3), (ImmutableList<V>)this.valueList.subList(n2, n3));
    }

    @Override
    public ImmutableSortedMap<K, V> headMap(K k2) {
        return this.headMap((Object)k2, false);
    }

    @Override
    public ImmutableSortedMap<K, V> headMap(K k2, boolean bl) {
        return this.getSubMap(0, this.keySet.headIndex(Preconditions.checkNotNull(k2), bl));
    }

    @Override
    public ImmutableSortedMap<K, V> subMap(K k2, K k3) {
        return this.subMap((Object)k2, true, (Object)k3, false);
    }

    @Override
    public ImmutableSortedMap<K, V> subMap(K k2, boolean bl, K k3, boolean bl2) {
        Preconditions.checkNotNull(k2);
        Preconditions.checkNotNull(k3);
        Preconditions.checkArgument(this.comparator().compare(k2, k3) <= 0, "expected fromKey <= toKey but %s > %s", k2, k3);
        return this.headMap((Object)k3, bl2).tailMap((Object)k2, bl);
    }

    @Override
    public ImmutableSortedMap<K, V> tailMap(K k2) {
        return this.tailMap((Object)k2, true);
    }

    @Override
    public ImmutableSortedMap<K, V> tailMap(K k2, boolean bl) {
        return this.getSubMap(this.keySet.tailIndex(Preconditions.checkNotNull(k2), bl), this.size());
    }

    @Override
    public Map.Entry<K, V> lowerEntry(K k2) {
        return this.headMap((Object)k2, false).lastEntry();
    }

    @Override
    public K lowerKey(K k2) {
        return Maps.keyOrNull(this.lowerEntry(k2));
    }

    @Override
    public Map.Entry<K, V> floorEntry(K k2) {
        return this.headMap((Object)k2, true).lastEntry();
    }

    @Override
    public K floorKey(K k2) {
        return Maps.keyOrNull(this.floorEntry(k2));
    }

    @Override
    public Map.Entry<K, V> ceilingEntry(K k2) {
        return this.tailMap((Object)k2, true).firstEntry();
    }

    @Override
    public K ceilingKey(K k2) {
        return Maps.keyOrNull(this.ceilingEntry(k2));
    }

    @Override
    public Map.Entry<K, V> higherEntry(K k2) {
        return this.tailMap((Object)k2, false).firstEntry();
    }

    @Override
    public K higherKey(K k2) {
        return Maps.keyOrNull(this.higherEntry(k2));
    }

    @Override
    public Map.Entry<K, V> firstEntry() {
        return this.isEmpty() ? null : (Map.Entry)this.entrySet().asList().get(0);
    }

    @Override
    public Map.Entry<K, V> lastEntry() {
        return this.isEmpty() ? null : (Map.Entry)this.entrySet().asList().get(this.size() - 1);
    }

    @Deprecated
    @Override
    public final Map.Entry<K, V> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public final Map.Entry<K, V> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSortedMap<K, V> descendingMap() {
        ImmutableSortedMap immutableSortedMap = this.descendingMap;
        if (immutableSortedMap == null) {
            if (this.isEmpty()) {
                immutableSortedMap = ImmutableSortedMap.emptyMap(Ordering.from(this.comparator()).reverse());
                return immutableSortedMap;
            }
            immutableSortedMap = new ImmutableSortedMap<K, V>((RegularImmutableSortedSet)this.keySet.descendingSet(), this.valueList.reverse(), this);
            return immutableSortedMap;
        }
        return immutableSortedMap;
    }

    @Override
    public ImmutableSortedSet<K> navigableKeySet() {
        return this.keySet;
    }

    @Override
    public ImmutableSortedSet<K> descendingKeySet() {
        return this.keySet.descendingSet();
    }

}

