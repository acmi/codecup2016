/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

abstract class AbstractMultimap<K, V>
implements Multimap<K, V> {
    private transient Collection<Map.Entry<K, V>> entries;
    private transient Set<K> keySet;
    private transient Collection<V> values;
    private transient Map<K, Collection<V>> asMap;

    AbstractMultimap() {
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    public boolean containsValue(Object object) {
        for (Collection<V> collection : this.asMap().values()) {
            if (!collection.contains(object)) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean containsEntry(Object object, Object object2) {
        Collection<V> collection = this.asMap().get(object);
        return collection != null && collection.contains(object2);
    }

    @Override
    public boolean remove(Object object, Object object2) {
        Collection<V> collection = this.asMap().get(object);
        return collection != null && collection.remove(object2);
    }

    @Override
    public boolean put(K k2, V v2) {
        return this.get(k2).add(v2);
    }

    @Override
    public boolean putAll(K k2, Iterable<? extends V> iterable) {
        Preconditions.checkNotNull(iterable);
        if (iterable instanceof Collection) {
            Collection collection = (Collection)iterable;
            return !collection.isEmpty() && this.get(k2).addAll(collection);
        }
        Iterator<V> iterator = iterable.iterator();
        return iterator.hasNext() && Iterators.addAll(this.get(k2), iterator);
    }

    public Collection<Map.Entry<K, V>> entries() {
        Collection<Map.Entry<K, V>> collection = this.entries;
        Collection<Map.Entry<K, V>> collection2 = collection == null ? (this.entries = this.createEntries()) : collection;
        return collection2;
    }

    Collection<Map.Entry<K, V>> createEntries() {
        if (this instanceof SetMultimap) {
            return new EntrySet();
        }
        return new Entries();
    }

    abstract Iterator<Map.Entry<K, V>> entryIterator();

    @Override
    public Set<K> keySet() {
        Set<K> set = this.keySet;
        Set<K> set2 = set == null ? (this.keySet = this.createKeySet()) : set;
        return set2;
    }

    Set<K> createKeySet() {
        return new Maps.KeySet<K, Collection<V>>(this.asMap());
    }

    @Override
    public Collection<V> values() {
        Collection<V> collection = this.values;
        Collection<V> collection2 = collection == null ? (this.values = this.createValues()) : collection;
        return collection2;
    }

    Collection<V> createValues() {
        return new Values();
    }

    Iterator<V> valueIterator() {
        return Maps.valueIterator(this.entries().iterator());
    }

    @Override
    public Map<K, Collection<V>> asMap() {
        Map<K, Collection<Collection<V>>> map = this.asMap;
        Map<K, Collection<Collection<V>>> map2 = map == null ? (this.asMap = this.createAsMap()) : map;
        return map2;
    }

    abstract Map<K, Collection<V>> createAsMap();

    public boolean equals(Object object) {
        return Multimaps.equalsImpl(this, object);
    }

    public int hashCode() {
        return this.asMap().hashCode();
    }

    public String toString() {
        return this.asMap().toString();
    }

    class Values
    extends AbstractCollection<V> {
        Values() {
        }

        @Override
        public Iterator<V> iterator() {
            return AbstractMultimap.this.valueIterator();
        }

        @Override
        public int size() {
            return AbstractMultimap.this.size();
        }

        @Override
        public boolean contains(Object object) {
            return AbstractMultimap.this.containsValue(object);
        }

        @Override
        public void clear() {
            AbstractMultimap.this.clear();
        }
    }

    private class EntrySet
    extends AbstractMultimap<K, V>
    implements Set<Map.Entry<K, V>> {
        private EntrySet() {
            super();
        }

        @Override
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }

        @Override
        public boolean equals(Object object) {
            return Sets.equalsImpl(this, object);
        }
    }

    private class Entries
    extends Multimaps.Entries<K, V> {
        private Entries() {
        }

        @Override
        Multimap<K, V> multimap() {
            return AbstractMultimap.this;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return AbstractMultimap.this.entryIterator();
        }
    }

}

