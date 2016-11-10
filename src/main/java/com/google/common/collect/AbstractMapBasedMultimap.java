/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractMultimap;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

abstract class AbstractMapBasedMultimap<K, V>
extends AbstractMultimap<K, V>
implements Serializable {
    private transient Map<K, Collection<V>> map;
    private transient int totalSize;

    protected AbstractMapBasedMultimap(Map<K, Collection<V>> map) {
        Preconditions.checkArgument(map.isEmpty());
        this.map = map;
    }

    Collection<V> createUnmodifiableEmptyCollection() {
        return this.unmodifiableCollectionSubclass(this.createCollection());
    }

    abstract Collection<V> createCollection();

    Collection<V> createCollection(K k2) {
        return this.createCollection();
    }

    @Override
    public int size() {
        return this.totalSize;
    }

    @Override
    public boolean put(K k2, V v2) {
        Collection<V> collection = this.map.get(k2);
        if (collection == null) {
            collection = this.createCollection(k2);
            if (collection.add(v2)) {
                ++this.totalSize;
                this.map.put(k2, collection);
                return true;
            }
            throw new AssertionError((Object)"New Collection violated the Collection spec");
        }
        if (collection.add(v2)) {
            ++this.totalSize;
            return true;
        }
        return false;
    }

    @Override
    public Collection<V> removeAll(Object object) {
        Collection<V> collection = this.map.remove(object);
        if (collection == null) {
            return this.createUnmodifiableEmptyCollection();
        }
        Collection<V> collection2 = this.createCollection();
        collection2.addAll(collection);
        this.totalSize -= collection.size();
        collection.clear();
        return this.unmodifiableCollectionSubclass(collection2);
    }

    Collection<V> unmodifiableCollectionSubclass(Collection<V> collection) {
        if (collection instanceof SortedSet) {
            return Collections.unmodifiableSortedSet((SortedSet)collection);
        }
        if (collection instanceof Set) {
            return Collections.unmodifiableSet((Set)collection);
        }
        if (collection instanceof List) {
            return Collections.unmodifiableList((List)collection);
        }
        return Collections.unmodifiableCollection(collection);
    }

    @Override
    public void clear() {
        for (Collection<V> collection : this.map.values()) {
            collection.clear();
        }
        this.map.clear();
        this.totalSize = 0;
    }

    @Override
    public Collection<V> get(K k2) {
        Collection<V> collection = this.map.get(k2);
        if (collection == null) {
            collection = this.createCollection(k2);
        }
        return this.wrapCollection(k2, collection);
    }

    Collection<V> wrapCollection(K k2, Collection<V> collection) {
        if (collection instanceof SortedSet) {
            return new WrappedSortedSet(this, k2, (SortedSet)collection, null);
        }
        if (collection instanceof Set) {
            return new WrappedSet(k2, (Set)collection);
        }
        if (collection instanceof List) {
            return this.wrapList(k2, (List)collection, null);
        }
        return new WrappedCollection(this, k2, collection, null);
    }

    private List<V> wrapList(K k2, List<V> list, AbstractMapBasedMultimap<K, V> abstractMapBasedMultimap) {
        return list instanceof RandomAccess ? new RandomAccessWrappedList(this, k2, list, abstractMapBasedMultimap) : new WrappedList(this, k2, list, abstractMapBasedMultimap);
    }

    private Iterator<V> iteratorOrListIterator(Collection<V> collection) {
        return collection instanceof List ? ((List)collection).listIterator() : collection.iterator();
    }

    @Override
    Set<K> createKeySet() {
        return this.map instanceof SortedMap ? new SortedKeySet((SortedMap)this.map) : new KeySet(this.map);
    }

    private int removeValuesForKey(Object object) {
        Collection<V> collection = Maps.safeRemove(this.map, object);
        int n2 = 0;
        if (collection != null) {
            n2 = collection.size();
            collection.clear();
            this.totalSize -= n2;
        }
        return n2;
    }

    @Override
    public Collection<V> values() {
        return super.values();
    }

    @Override
    Iterator<V> valueIterator() {
        return new AbstractMapBasedMultimap<K, V>(){

            V output(K k2, V v2) {
                return v2;
            }
        };
    }

    @Override
    public Collection<Map.Entry<K, V>> entries() {
        return super.entries();
    }

    @Override
    Iterator<Map.Entry<K, V>> entryIterator() {
        return new AbstractMapBasedMultimap<K, V>(){

            Map.Entry<K, V> output(K k2, V v2) {
                return Maps.immutableEntry(k2, v2);
            }
        };
    }

    @Override
    Map<K, Collection<V>> createAsMap() {
        return this.map instanceof SortedMap ? new SortedAsMap((SortedMap)this.map) : new AsMap(this.map);
    }

    static /* synthetic */ int access$212(AbstractMapBasedMultimap abstractMapBasedMultimap, int n2) {
        return abstractMapBasedMultimap.totalSize += n2;
    }

    static /* synthetic */ int access$220(AbstractMapBasedMultimap abstractMapBasedMultimap, int n2) {
        return abstractMapBasedMultimap.totalSize -= n2;
    }

    private class SortedAsMap
    extends AbstractMapBasedMultimap<K, V>
    implements SortedMap<K, Collection<V>> {
        SortedSet<K> sortedKeySet;

        SortedAsMap(SortedMap<K, Collection<V>> sortedMap) {
            super(sortedMap);
        }

        SortedMap<K, Collection<V>> sortedMap() {
            return (SortedMap)this.submap;
        }

        @Override
        public Comparator<? super K> comparator() {
            return this.sortedMap().comparator();
        }

        @Override
        public K firstKey() {
            return this.sortedMap().firstKey();
        }

        @Override
        public K lastKey() {
            return this.sortedMap().lastKey();
        }

        @Override
        public SortedMap<K, Collection<V>> headMap(K k2) {
            return new SortedAsMap(this.sortedMap().headMap(k2));
        }

        @Override
        public SortedMap<K, Collection<V>> subMap(K k2, K k3) {
            return new SortedAsMap(this.sortedMap().subMap(k2, k3));
        }

        @Override
        public SortedMap<K, Collection<V>> tailMap(K k2) {
            return new SortedAsMap(this.sortedMap().tailMap(k2));
        }

        @Override
        public SortedSet<K> keySet() {
            SortedSet<K> sortedSet = this.sortedKeySet;
            Set set = sortedSet == null ? (this.sortedKeySet = this.createKeySet()) : sortedSet;
            return set;
        }

        @Override
        SortedSet<K> createKeySet() {
            return new SortedKeySet(this.sortedMap());
        }
    }

    private class AsMap
    extends Maps.ViewCachingAbstractMap<K, Collection<V>> {
        final transient Map<K, Collection<V>> submap;

        AsMap(Map<K, Collection<V>> map) {
            this.submap = map;
        }

        @Override
        protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
            return new AsMapEntries();
        }

        @Override
        public boolean containsKey(Object object) {
            return Maps.safeContainsKey(this.submap, object);
        }

        @Override
        public Collection<V> get(Object object) {
            Collection<V> collection = Maps.safeGet(this.submap, object);
            if (collection == null) {
                return null;
            }
            Object object2 = object;
            return AbstractMapBasedMultimap.this.wrapCollection(object2, collection);
        }

        @Override
        public Set<K> keySet() {
            return AbstractMapBasedMultimap.this.keySet();
        }

        @Override
        public int size() {
            return this.submap.size();
        }

        @Override
        public Collection<V> remove(Object object) {
            Collection<V> collection = this.submap.remove(object);
            if (collection == null) {
                return null;
            }
            Collection collection2 = AbstractMapBasedMultimap.this.createCollection();
            collection2.addAll(collection);
            AbstractMapBasedMultimap.access$220(AbstractMapBasedMultimap.this, collection.size());
            collection.clear();
            return collection2;
        }

        @Override
        public boolean equals(Object object) {
            return this == object || this.submap.equals(object);
        }

        @Override
        public int hashCode() {
            return this.submap.hashCode();
        }

        @Override
        public String toString() {
            return this.submap.toString();
        }

        @Override
        public void clear() {
            if (this.submap == AbstractMapBasedMultimap.this.map) {
                AbstractMapBasedMultimap.this.clear();
            } else {
                Iterators.clear(new AsMapIterator());
            }
        }

        Map.Entry<K, Collection<V>> wrapEntry(Map.Entry<K, Collection<V>> entry) {
            K k2 = entry.getKey();
            return Maps.immutableEntry(k2, AbstractMapBasedMultimap.this.wrapCollection(k2, entry.getValue()));
        }

        class AsMapIterator
        implements Iterator<Map.Entry<K, Collection<V>>> {
            final Iterator<Map.Entry<K, Collection<V>>> delegateIterator;
            Collection<V> collection;

            AsMapIterator() {
                this.delegateIterator = AsMap.this.submap.entrySet().iterator();
            }

            @Override
            public boolean hasNext() {
                return this.delegateIterator.hasNext();
            }

            @Override
            public Map.Entry<K, Collection<V>> next() {
                Map.Entry<K, Collection<V>> entry = this.delegateIterator.next();
                this.collection = entry.getValue();
                return AsMap.this.wrapEntry(entry);
            }

            @Override
            public void remove() {
                this.delegateIterator.remove();
                AbstractMapBasedMultimap.access$220(AbstractMapBasedMultimap.this, this.collection.size());
                this.collection.clear();
            }
        }

        class AsMapEntries
        extends Maps.EntrySet<K, Collection<V>> {
            AsMapEntries() {
            }

            @Override
            Map<K, Collection<V>> map() {
                return AsMap.this;
            }

            @Override
            public Iterator<Map.Entry<K, Collection<V>>> iterator() {
                return new AsMapIterator();
            }

            @Override
            public boolean contains(Object object) {
                return Collections2.safeContains(AsMap.this.submap.entrySet(), object);
            }

            @Override
            public boolean remove(Object object) {
                if (!this.contains(object)) {
                    return false;
                }
                Map.Entry entry = (Map.Entry)object;
                AbstractMapBasedMultimap.this.removeValuesForKey(entry.getKey());
                return true;
            }
        }

    }

    private abstract class Itr<T>
    implements Iterator<T> {
        final Iterator<Map.Entry<K, Collection<V>>> keyIterator;
        K key;
        Collection<V> collection;
        Iterator<V> valueIterator;

        Itr() {
            this.keyIterator = AbstractMapBasedMultimap.this.map.entrySet().iterator();
            this.key = null;
            this.collection = null;
            this.valueIterator = Iterators.emptyModifiableIterator();
        }

        abstract T output(K var1, V var2);

        @Override
        public boolean hasNext() {
            return this.keyIterator.hasNext() || this.valueIterator.hasNext();
        }

        @Override
        public T next() {
            if (!this.valueIterator.hasNext()) {
                Map.Entry<K, Collection<V>> entry = this.keyIterator.next();
                this.key = entry.getKey();
                this.collection = entry.getValue();
                this.valueIterator = this.collection.iterator();
            }
            return this.output(this.key, this.valueIterator.next());
        }

        @Override
        public void remove() {
            this.valueIterator.remove();
            if (this.collection.isEmpty()) {
                this.keyIterator.remove();
            }
            AbstractMapBasedMultimap.this.totalSize--;
        }
    }

    private class SortedKeySet
    extends AbstractMapBasedMultimap<K, V>
    implements SortedSet<K> {
        SortedKeySet(SortedMap<K, Collection<V>> sortedMap) {
            super(sortedMap);
        }

        SortedMap<K, Collection<V>> sortedMap() {
            return (SortedMap)KeySet.super.map();
        }

        @Override
        public Comparator<? super K> comparator() {
            return this.sortedMap().comparator();
        }

        @Override
        public K first() {
            return this.sortedMap().firstKey();
        }

        @Override
        public SortedSet<K> headSet(K k2) {
            return new SortedKeySet(this.sortedMap().headMap(k2));
        }

        @Override
        public K last() {
            return this.sortedMap().lastKey();
        }

        @Override
        public SortedSet<K> subSet(K k2, K k3) {
            return new SortedKeySet(this.sortedMap().subMap(k2, k3));
        }

        @Override
        public SortedSet<K> tailSet(K k2) {
            return new SortedKeySet(this.sortedMap().tailMap(k2));
        }
    }

    private class KeySet
    extends Maps.KeySet<K, Collection<V>> {
        KeySet(Map<K, Collection<V>> map) {
            super(map);
        }

        @Override
        public Iterator<K> iterator() {
            final Iterator iterator = this.map().entrySet().iterator();
            return new Iterator<K>(){
                Map.Entry<K, Collection<V>> entry;

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public K next() {
                    this.entry = (Map.Entry)iterator.next();
                    return this.entry.getKey();
                }

                @Override
                public void remove() {
                    CollectPreconditions.checkRemove(this.entry != null);
                    Collection<V> collection = this.entry.getValue();
                    iterator.remove();
                    AbstractMapBasedMultimap.access$220(AbstractMapBasedMultimap.this, collection.size());
                    collection.clear();
                }
            };
        }

        @Override
        public boolean remove(Object object) {
            int n2 = 0;
            Collection collection = (Collection)this.map().remove(object);
            if (collection != null) {
                n2 = collection.size();
                collection.clear();
                AbstractMapBasedMultimap.access$220(AbstractMapBasedMultimap.this, n2);
            }
            return n2 > 0;
        }

        @Override
        public void clear() {
            Iterators.clear(this.iterator());
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return this.map().keySet().containsAll(collection);
        }

        @Override
        public boolean equals(Object object) {
            return this == object || this.map().keySet().equals(object);
        }

        @Override
        public int hashCode() {
            return this.map().keySet().hashCode();
        }

    }

    private class RandomAccessWrappedList
    extends AbstractMapBasedMultimap<K, V>
    implements RandomAccess {
        RandomAccessWrappedList(List<V> k2, AbstractMapBasedMultimap<K, V> list) {
            super((AbstractMapBasedMultimap)this$0, k2, list, abstractMapBasedMultimap);
        }
    }

    private class WrappedList
    extends AbstractMapBasedMultimap<K, V>
    implements List<V> {
        WrappedList(List<V> k2, AbstractMapBasedMultimap<K, V> list) {
            super((AbstractMapBasedMultimap)this$0, k2, list, abstractMapBasedMultimap);
        }

        List<V> getListDelegate() {
            return (List)this.getDelegate();
        }

        @Override
        public boolean addAll(int n2, Collection<? extends V> collection) {
            if (collection.isEmpty()) {
                return false;
            }
            int n3 = this.size();
            boolean bl = this.getListDelegate().addAll(n2, collection);
            if (bl) {
                int n4 = this.getDelegate().size();
                AbstractMapBasedMultimap.access$212(this$0, n4 - n3);
                if (n3 == 0) {
                    this.addToMap();
                }
            }
            return bl;
        }

        @Override
        public V get(int n2) {
            this.refreshIfEmpty();
            return this.getListDelegate().get(n2);
        }

        @Override
        public V set(int n2, V v2) {
            this.refreshIfEmpty();
            return this.getListDelegate().set(n2, v2);
        }

        @Override
        public void add(int n2, V v2) {
            this.refreshIfEmpty();
            boolean bl = this.getDelegate().isEmpty();
            this.getListDelegate().add(n2, v2);
            this$0.totalSize++;
            if (bl) {
                this.addToMap();
            }
        }

        @Override
        public V remove(int n2) {
            this.refreshIfEmpty();
            V v2 = this.getListDelegate().remove(n2);
            this$0.totalSize--;
            this.removeIfEmpty();
            return v2;
        }

        @Override
        public int indexOf(Object object) {
            this.refreshIfEmpty();
            return this.getListDelegate().indexOf(object);
        }

        @Override
        public int lastIndexOf(Object object) {
            this.refreshIfEmpty();
            return this.getListDelegate().lastIndexOf(object);
        }

        @Override
        public ListIterator<V> listIterator() {
            this.refreshIfEmpty();
            return new WrappedListIterator(this);
        }

        @Override
        public ListIterator<V> listIterator(int n2) {
            this.refreshIfEmpty();
            return new WrappedListIterator(this, n2);
        }

        @Override
        public List<V> subList(int n2, int n3) {
            this.refreshIfEmpty();
            return this$0.wrapList(this.getKey(), this.getListDelegate().subList(n2, n3), (WrappedCollection)((Object)(this.getAncestor() == null ? this : this.getAncestor())));
        }

        private class WrappedListIterator
        extends AbstractMapBasedMultimap<K, V>
        implements ListIterator<V> {
            final /* synthetic */ WrappedList this$1;

            WrappedListIterator(WrappedList wrappedList) {
                this.this$1 = wrappedList;
                super((WrappedCollection)((Object)wrappedList));
            }

            public WrappedListIterator(WrappedList wrappedList, int n2) {
                this.this$1 = wrappedList;
                super((WrappedCollection)((Object)wrappedList), wrappedList.getListDelegate().listIterator(n2));
            }

            private ListIterator<V> getDelegateListIterator() {
                return (ListIterator)this.getDelegateIterator();
            }

            @Override
            public boolean hasPrevious() {
                return this.getDelegateListIterator().hasPrevious();
            }

            @Override
            public V previous() {
                return this.getDelegateListIterator().previous();
            }

            @Override
            public int nextIndex() {
                return this.getDelegateListIterator().nextIndex();
            }

            @Override
            public int previousIndex() {
                return this.getDelegateListIterator().previousIndex();
            }

            @Override
            public void set(V v2) {
                this.getDelegateListIterator().set(v2);
            }

            @Override
            public void add(V v2) {
                boolean bl = this.this$1.isEmpty();
                this.getDelegateListIterator().add(v2);
                this.this$1.this$0.totalSize++;
                if (bl) {
                    this.this$1.addToMap();
                }
            }
        }

    }

    private class WrappedSortedSet
    extends AbstractMapBasedMultimap<K, V>
    implements SortedSet<V> {
        WrappedSortedSet(SortedSet<V> k2, AbstractMapBasedMultimap<K, V> sortedSet) {
            super((AbstractMapBasedMultimap)this$0, k2, sortedSet, abstractMapBasedMultimap);
        }

        SortedSet<V> getSortedSetDelegate() {
            return (SortedSet)this.getDelegate();
        }

        @Override
        public Comparator<? super V> comparator() {
            return this.getSortedSetDelegate().comparator();
        }

        @Override
        public V first() {
            this.refreshIfEmpty();
            return this.getSortedSetDelegate().first();
        }

        @Override
        public V last() {
            this.refreshIfEmpty();
            return this.getSortedSetDelegate().last();
        }

        @Override
        public SortedSet<V> headSet(V v2) {
            this.refreshIfEmpty();
            return new WrappedSortedSet(this$0, this.getKey(), this.getSortedSetDelegate().headSet(v2), (WrappedCollection)((Object)(this.getAncestor() == null ? this : this.getAncestor())));
        }

        @Override
        public SortedSet<V> subSet(V v2, V v3) {
            this.refreshIfEmpty();
            return new WrappedSortedSet(this$0, this.getKey(), this.getSortedSetDelegate().subSet(v2, v3), (WrappedCollection)((Object)(this.getAncestor() == null ? this : this.getAncestor())));
        }

        @Override
        public SortedSet<V> tailSet(V v2) {
            this.refreshIfEmpty();
            return new WrappedSortedSet(this$0, this.getKey(), this.getSortedSetDelegate().tailSet(v2), (WrappedCollection)((Object)(this.getAncestor() == null ? this : this.getAncestor())));
        }
    }

    private class WrappedSet
    extends AbstractMapBasedMultimap<K, V>
    implements Set<V> {
        WrappedSet(K k2, Set<V> set) {
            super(AbstractMapBasedMultimap.this, k2, set, null);
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            if (collection.isEmpty()) {
                return false;
            }
            int n2 = this.size();
            boolean bl = Sets.removeAllImpl((Set)this.delegate, collection);
            if (bl) {
                int n3 = this.delegate.size();
                AbstractMapBasedMultimap.access$212(AbstractMapBasedMultimap.this, n3 - n2);
                this.removeIfEmpty();
            }
            return bl;
        }
    }

    private class WrappedCollection
    extends AbstractCollection<V> {
        final K key;
        Collection<V> delegate;
        final AbstractMapBasedMultimap<K, V> ancestor;
        final Collection<V> ancestorDelegate;

        WrappedCollection(Collection<V> k2, AbstractMapBasedMultimap<K, V> collection) {
            this.key = k2;
            this.delegate = collection;
            this.ancestor = abstractMapBasedMultimap;
            this.ancestorDelegate = abstractMapBasedMultimap == null ? null : abstractMapBasedMultimap.getDelegate();
        }

        void refreshIfEmpty() {
            Collection collection;
            if (this.ancestor != null) {
                this.ancestor.refreshIfEmpty();
                if (this.ancestor.getDelegate() != this.ancestorDelegate) {
                    throw new ConcurrentModificationException();
                }
            } else if (this.delegate.isEmpty() && (collection = (Collection)this$0.map.get(this.key)) != null) {
                this.delegate = collection;
            }
        }

        void removeIfEmpty() {
            if (this.ancestor != null) {
                this.ancestor.removeIfEmpty();
            } else if (this.delegate.isEmpty()) {
                this$0.map.remove(this.key);
            }
        }

        K getKey() {
            return this.key;
        }

        void addToMap() {
            if (this.ancestor != null) {
                this.ancestor.addToMap();
            } else {
                this$0.map.put(this.key, this.delegate);
            }
        }

        @Override
        public int size() {
            this.refreshIfEmpty();
            return this.delegate.size();
        }

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            this.refreshIfEmpty();
            return this.delegate.equals(object);
        }

        @Override
        public int hashCode() {
            this.refreshIfEmpty();
            return this.delegate.hashCode();
        }

        @Override
        public String toString() {
            this.refreshIfEmpty();
            return this.delegate.toString();
        }

        Collection<V> getDelegate() {
            return this.delegate;
        }

        @Override
        public Iterator<V> iterator() {
            this.refreshIfEmpty();
            return new WrappedIterator(this);
        }

        @Override
        public boolean add(V v2) {
            this.refreshIfEmpty();
            boolean bl = this.delegate.isEmpty();
            boolean bl2 = this.delegate.add(v2);
            if (bl2) {
                this$0.totalSize++;
                if (bl) {
                    this.addToMap();
                }
            }
            return bl2;
        }

        AbstractMapBasedMultimap<K, V> getAncestor() {
            return this.ancestor;
        }

        @Override
        public boolean addAll(Collection<? extends V> collection) {
            if (collection.isEmpty()) {
                return false;
            }
            int n2 = this.size();
            boolean bl = this.delegate.addAll(collection);
            if (bl) {
                int n3 = this.delegate.size();
                AbstractMapBasedMultimap.access$212(this$0, n3 - n2);
                if (n2 == 0) {
                    this.addToMap();
                }
            }
            return bl;
        }

        @Override
        public boolean contains(Object object) {
            this.refreshIfEmpty();
            return this.delegate.contains(object);
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            this.refreshIfEmpty();
            return this.delegate.containsAll(collection);
        }

        @Override
        public void clear() {
            int n2 = this.size();
            if (n2 == 0) {
                return;
            }
            this.delegate.clear();
            AbstractMapBasedMultimap.access$220(this$0, n2);
            this.removeIfEmpty();
        }

        @Override
        public boolean remove(Object object) {
            this.refreshIfEmpty();
            boolean bl = this.delegate.remove(object);
            if (bl) {
                this$0.totalSize--;
                this.removeIfEmpty();
            }
            return bl;
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            if (collection.isEmpty()) {
                return false;
            }
            int n2 = this.size();
            boolean bl = this.delegate.removeAll(collection);
            if (bl) {
                int n3 = this.delegate.size();
                AbstractMapBasedMultimap.access$212(this$0, n3 - n2);
                this.removeIfEmpty();
            }
            return bl;
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            Preconditions.checkNotNull(collection);
            int n2 = this.size();
            boolean bl = this.delegate.retainAll(collection);
            if (bl) {
                int n3 = this.delegate.size();
                AbstractMapBasedMultimap.access$212(this$0, n3 - n2);
                this.removeIfEmpty();
            }
            return bl;
        }

        class WrappedIterator
        implements Iterator<V> {
            final Iterator<V> delegateIterator;
            final Collection<V> originalDelegate;
            final /* synthetic */ WrappedCollection this$1;

            WrappedIterator(WrappedCollection wrappedCollection) {
                this.this$1 = wrappedCollection;
                this.originalDelegate = this.this$1.delegate;
                this.delegateIterator = wrappedCollection.this$0.iteratorOrListIterator(wrappedCollection.delegate);
            }

            WrappedIterator(WrappedCollection wrappedCollection, Iterator<V> iterator) {
                this.this$1 = wrappedCollection;
                this.originalDelegate = this.this$1.delegate;
                this.delegateIterator = iterator;
            }

            void validateIterator() {
                this.this$1.refreshIfEmpty();
                if (this.this$1.delegate != this.originalDelegate) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override
            public boolean hasNext() {
                this.validateIterator();
                return this.delegateIterator.hasNext();
            }

            @Override
            public V next() {
                this.validateIterator();
                return this.delegateIterator.next();
            }

            @Override
            public void remove() {
                this.delegateIterator.remove();
                this.this$1.this$0.totalSize--;
                this.this$1.removeIfEmpty();
            }

            Iterator<V> getDelegateIterator() {
                this.validateIterator();
                return this.delegateIterator;
            }
        }

    }

}

