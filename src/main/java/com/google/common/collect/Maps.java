/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  com.google.j2objc.annotations.Weak
 */
package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractMapEntry;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableEntry;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Platform;
import com.google.common.collect.Sets;
import com.google.common.collect.TransformedIterator;
import com.google.common.collect.UnmodifiableIterator;
import com.google.j2objc.annotations.Weak;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentMap;

public final class Maps {
    static final Joiner.MapJoiner STANDARD_JOINER = Collections2.STANDARD_JOINER.withKeyValueSeparator("=");

    static <K> Function<Map.Entry<K, ?>, K> keyFunction() {
        return EntryFunction.KEY;
    }

    static <V> Function<Map.Entry<?, V>, V> valueFunction() {
        return EntryFunction.VALUE;
    }

    static <K, V> Iterator<K> keyIterator(Iterator<Map.Entry<K, V>> iterator) {
        return Iterators.transform(iterator, Maps.<K>keyFunction());
    }

    static <K, V> Iterator<V> valueIterator(Iterator<Map.Entry<K, V>> iterator) {
        return Iterators.transform(iterator, Maps.<V>valueFunction());
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap();
    }

    static int capacity(int n2) {
        if (n2 < 3) {
            CollectPreconditions.checkNonnegative(n2, "expectedSize");
            return n2 + 1;
        }
        if (n2 < 1073741824) {
            return (int)((float)n2 / 0.75f + 1.0f);
        }
        return Integer.MAX_VALUE;
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap();
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<? extends K, ? extends V> map) {
        return new LinkedHashMap<K, V>(map);
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
        return new MapMaker().makeMap();
    }

    public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
        return new IdentityHashMap();
    }

    public static <K, V> Map.Entry<K, V> immutableEntry(K k2, V v2) {
        return new ImmutableEntry<K, V>(k2, v2);
    }

    static <K, V> Map.Entry<K, V> unmodifiableEntry(final Map.Entry<? extends K, ? extends V> entry) {
        Preconditions.checkNotNull(entry);
        return new AbstractMapEntry<K, V>(){

            @Override
            public K getKey() {
                return entry.getKey();
            }

            @Override
            public V getValue() {
                return entry.getValue();
            }
        };
    }

    static <K, V> UnmodifiableIterator<Map.Entry<K, V>> unmodifiableEntryIterator(final Iterator<Map.Entry<K, V>> iterator) {
        return new UnmodifiableIterator<Map.Entry<K, V>>(){

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Map.Entry<K, V> next() {
                return Maps.unmodifiableEntry((Map.Entry)iterator.next());
            }
        };
    }

    public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> map, Function<? super V1, V2> function) {
        return Maps.transformEntries(map, Maps.asEntryTransformer(function));
    }

    public static <K, V1, V2> Map<K, V2> transformEntries(Map<K, V1> map, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
        if (map instanceof SortedMap) {
            return Maps.transformEntries((SortedMap)map, entryTransformer);
        }
        return new TransformedEntriesMap<K, V1, V2>(map, entryTransformer);
    }

    public static <K, V1, V2> SortedMap<K, V2> transformEntries(SortedMap<K, V1> sortedMap, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
        return Platform.mapsTransformEntriesSortedMap(sortedMap, entryTransformer);
    }

    public static <K, V1, V2> NavigableMap<K, V2> transformEntries(NavigableMap<K, V1> navigableMap, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
        return new TransformedEntriesNavigableMap<K, V1, V2>(navigableMap, entryTransformer);
    }

    static <K, V1, V2> SortedMap<K, V2> transformEntriesIgnoreNavigable(SortedMap<K, V1> sortedMap, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
        return new TransformedEntriesSortedMap<K, V1, V2>(sortedMap, entryTransformer);
    }

    static <K, V1, V2> EntryTransformer<K, V1, V2> asEntryTransformer(final Function<? super V1, V2> function) {
        Preconditions.checkNotNull(function);
        return new EntryTransformer<K, V1, V2>(){

            @Override
            public V2 transformEntry(K k2, V1 V1) {
                return (V2)function.apply(V1);
            }
        };
    }

    static <V2, K, V1> Map.Entry<K, V2> transformEntry(final EntryTransformer<? super K, ? super V1, V2> entryTransformer, final Map.Entry<K, V1> entry) {
        Preconditions.checkNotNull(entryTransformer);
        Preconditions.checkNotNull(entry);
        return new AbstractMapEntry<K, V2>(){

            @Override
            public K getKey() {
                return entry.getKey();
            }

            @Override
            public V2 getValue() {
                return entryTransformer.transformEntry(entry.getKey(), entry.getValue());
            }
        };
    }

    static <K, V1, V2> Function<Map.Entry<K, V1>, Map.Entry<K, V2>> asEntryToEntryFunction(final EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
        Preconditions.checkNotNull(entryTransformer);
        return new Function<Map.Entry<K, V1>, Map.Entry<K, V2>>(){

            @Override
            public Map.Entry<K, V2> apply(Map.Entry<K, V1> entry) {
                return Maps.transformEntry(entryTransformer, entry);
            }
        };
    }

    static <V> V safeGet(Map<?, V> map, Object object) {
        Preconditions.checkNotNull(map);
        try {
            return map.get(object);
        }
        catch (ClassCastException classCastException) {
            return null;
        }
        catch (NullPointerException nullPointerException) {
            return null;
        }
    }

    static boolean safeContainsKey(Map<?, ?> map, Object object) {
        Preconditions.checkNotNull(map);
        try {
            return map.containsKey(object);
        }
        catch (ClassCastException classCastException) {
            return false;
        }
        catch (NullPointerException nullPointerException) {
            return false;
        }
    }

    static <V> V safeRemove(Map<?, V> map, Object object) {
        Preconditions.checkNotNull(map);
        try {
            return map.remove(object);
        }
        catch (ClassCastException classCastException) {
            return null;
        }
        catch (NullPointerException nullPointerException) {
            return null;
        }
    }

    static boolean equalsImpl(Map<?, ?> map, Object object) {
        if (map == object) {
            return true;
        }
        if (object instanceof Map) {
            Map map2 = (Map)object;
            return map.entrySet().equals(map2.entrySet());
        }
        return false;
    }

    static String toStringImpl(Map<?, ?> map) {
        StringBuilder stringBuilder = Collections2.newStringBuilderForCollection(map.size()).append('{');
        STANDARD_JOINER.appendTo(stringBuilder, map);
        return stringBuilder.append('}').toString();
    }

    static <K> K keyOrNull(Map.Entry<K, ?> entry) {
        return entry == null ? null : (K)entry.getKey();
    }

    static <E> ImmutableMap<E, Integer> indexMap(Collection<E> collection) {
        ImmutableMap.Builder<E, Integer> builder = new ImmutableMap.Builder<E, Integer>(collection.size());
        int n2 = 0;
        for (E e2 : collection) {
            builder.put(e2, n2++);
        }
        return builder.build();
    }

    static abstract class EntrySet<K, V>
    extends Sets.ImprovedAbstractSet<Map.Entry<K, V>> {
        EntrySet() {
        }

        abstract Map<K, V> map();

        @Override
        public int size() {
            return this.map().size();
        }

        @Override
        public void clear() {
            this.map().clear();
        }

        @Override
        public boolean contains(Object object) {
            if (object instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry)object;
                K k2 = entry.getKey();
                V v2 = Maps.safeGet(this.map(), k2);
                return Objects.equal(v2, entry.getValue()) && (v2 != null || this.map().containsKey(k2));
            }
            return false;
        }

        @Override
        public boolean isEmpty() {
            return this.map().isEmpty();
        }

        @Override
        public boolean remove(Object object) {
            if (this.contains(object)) {
                Map.Entry entry = (Map.Entry)object;
                return this.map().keySet().remove(entry.getKey());
            }
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            try {
                return super.removeAll(Preconditions.checkNotNull(collection));
            }
            catch (UnsupportedOperationException unsupportedOperationException) {
                return Sets.removeAllImpl(this, collection.iterator());
            }
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            try {
                return super.retainAll(Preconditions.checkNotNull(collection));
            }
            catch (UnsupportedOperationException unsupportedOperationException) {
                HashSet<K> hashSet = Sets.newHashSetWithExpectedSize(collection.size());
                for (? obj : collection) {
                    if (!this.contains(obj)) continue;
                    Map.Entry entry = (Map.Entry)obj;
                    hashSet.add(entry.getKey());
                }
                return this.map().keySet().retainAll(hashSet);
            }
        }
    }

    static class Values<K, V>
    extends AbstractCollection<V> {
        @Weak
        final Map<K, V> map;

        Values(Map<K, V> map) {
            this.map = Preconditions.checkNotNull(map);
        }

        final Map<K, V> map() {
            return this.map;
        }

        @Override
        public Iterator<V> iterator() {
            return Maps.valueIterator(this.map().entrySet().iterator());
        }

        @Override
        public boolean remove(Object object) {
            try {
                return super.remove(object);
            }
            catch (UnsupportedOperationException unsupportedOperationException) {
                for (Map.Entry<K, V> entry : this.map().entrySet()) {
                    if (!Objects.equal(object, entry.getValue())) continue;
                    this.map().remove(entry.getKey());
                    return true;
                }
                return false;
            }
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            try {
                return super.removeAll(Preconditions.checkNotNull(collection));
            }
            catch (UnsupportedOperationException unsupportedOperationException) {
                HashSet<K> hashSet = Sets.newHashSet();
                for (Map.Entry<K, V> entry : this.map().entrySet()) {
                    if (!collection.contains(entry.getValue())) continue;
                    hashSet.add(entry.getKey());
                }
                return this.map().keySet().removeAll(hashSet);
            }
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            try {
                return super.retainAll(Preconditions.checkNotNull(collection));
            }
            catch (UnsupportedOperationException unsupportedOperationException) {
                HashSet<K> hashSet = Sets.newHashSet();
                for (Map.Entry<K, V> entry : this.map().entrySet()) {
                    if (!collection.contains(entry.getValue())) continue;
                    hashSet.add(entry.getKey());
                }
                return this.map().keySet().retainAll(hashSet);
            }
        }

        @Override
        public int size() {
            return this.map().size();
        }

        @Override
        public boolean isEmpty() {
            return this.map().isEmpty();
        }

        @Override
        public boolean contains(Object object) {
            return this.map().containsValue(object);
        }

        @Override
        public void clear() {
            this.map().clear();
        }
    }

    static class KeySet<K, V>
    extends Sets.ImprovedAbstractSet<K> {
        @Weak
        final Map<K, V> map;

        KeySet(Map<K, V> map) {
            this.map = Preconditions.checkNotNull(map);
        }

        Map<K, V> map() {
            return this.map;
        }

        @Override
        public Iterator<K> iterator() {
            return Maps.keyIterator(this.map().entrySet().iterator());
        }

        @Override
        public int size() {
            return this.map().size();
        }

        @Override
        public boolean isEmpty() {
            return this.map().isEmpty();
        }

        @Override
        public boolean contains(Object object) {
            return this.map().containsKey(object);
        }

        @Override
        public boolean remove(Object object) {
            if (this.contains(object)) {
                this.map().remove(object);
                return true;
            }
            return false;
        }

        @Override
        public void clear() {
            this.map().clear();
        }
    }

    static abstract class IteratorBasedAbstractMap<K, V>
    extends AbstractMap<K, V> {
        IteratorBasedAbstractMap() {
        }

        abstract Iterator<Map.Entry<K, V>> entryIterator();

        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            return new EntrySet<K, V>(){

                @Override
                Map<K, V> map() {
                    return IteratorBasedAbstractMap.this;
                }

                @Override
                public Iterator<Map.Entry<K, V>> iterator() {
                    return IteratorBasedAbstractMap.this.entryIterator();
                }
            };
        }

        @Override
        public void clear() {
            Iterators.clear(this.entryIterator());
        }

    }

    static abstract class ViewCachingAbstractMap<K, V>
    extends AbstractMap<K, V> {
        private transient Set<Map.Entry<K, V>> entrySet;
        private transient Set<K> keySet;
        private transient Collection<V> values;

        ViewCachingAbstractMap() {
        }

        abstract Set<Map.Entry<K, V>> createEntrySet();

        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> set = this.entrySet;
            Set<Map.Entry<K, V>> set2 = set == null ? (this.entrySet = this.createEntrySet()) : set;
            return set2;
        }

        @Override
        public Set<K> keySet() {
            Set<K> set = this.keySet;
            Set<K> set2 = set == null ? (this.keySet = this.createKeySet()) : set;
            return set2;
        }

        Set<K> createKeySet() {
            return new KeySet<K, V>(this);
        }

        @Override
        public Collection<V> values() {
            Collection<V> collection = this.values;
            Collection<V> collection2 = collection == null ? (this.values = this.createValues()) : collection;
            return collection2;
        }

        Collection<V> createValues() {
            return new Values<K, V>(this);
        }
    }

    private static class TransformedEntriesNavigableMap<K, V1, V2>
    extends TransformedEntriesSortedMap<K, V1, V2>
    implements NavigableMap<K, V2> {
        TransformedEntriesNavigableMap(NavigableMap<K, V1> navigableMap, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
            super(navigableMap, entryTransformer);
        }

        @Override
        public Map.Entry<K, V2> ceilingEntry(K k2) {
            return this.transformEntry(this.fromMap().ceilingEntry(k2));
        }

        @Override
        public K ceilingKey(K k2) {
            return this.fromMap().ceilingKey(k2);
        }

        @Override
        public NavigableSet<K> descendingKeySet() {
            return this.fromMap().descendingKeySet();
        }

        @Override
        public NavigableMap<K, V2> descendingMap() {
            return Maps.transformEntries(this.fromMap().descendingMap(), this.transformer);
        }

        @Override
        public Map.Entry<K, V2> firstEntry() {
            return this.transformEntry(this.fromMap().firstEntry());
        }

        @Override
        public Map.Entry<K, V2> floorEntry(K k2) {
            return this.transformEntry(this.fromMap().floorEntry(k2));
        }

        @Override
        public K floorKey(K k2) {
            return this.fromMap().floorKey(k2);
        }

        @Override
        public NavigableMap<K, V2> headMap(K k2) {
            return this.headMap(k2, false);
        }

        @Override
        public NavigableMap<K, V2> headMap(K k2, boolean bl) {
            return Maps.transformEntries(this.fromMap().headMap(k2, bl), this.transformer);
        }

        @Override
        public Map.Entry<K, V2> higherEntry(K k2) {
            return this.transformEntry(this.fromMap().higherEntry(k2));
        }

        @Override
        public K higherKey(K k2) {
            return this.fromMap().higherKey(k2);
        }

        @Override
        public Map.Entry<K, V2> lastEntry() {
            return this.transformEntry(this.fromMap().lastEntry());
        }

        @Override
        public Map.Entry<K, V2> lowerEntry(K k2) {
            return this.transformEntry(this.fromMap().lowerEntry(k2));
        }

        @Override
        public K lowerKey(K k2) {
            return this.fromMap().lowerKey(k2);
        }

        @Override
        public NavigableSet<K> navigableKeySet() {
            return this.fromMap().navigableKeySet();
        }

        @Override
        public Map.Entry<K, V2> pollFirstEntry() {
            return this.transformEntry(this.fromMap().pollFirstEntry());
        }

        @Override
        public Map.Entry<K, V2> pollLastEntry() {
            return this.transformEntry(this.fromMap().pollLastEntry());
        }

        @Override
        public NavigableMap<K, V2> subMap(K k2, boolean bl, K k3, boolean bl2) {
            return Maps.transformEntries(this.fromMap().subMap(k2, bl, k3, bl2), this.transformer);
        }

        @Override
        public NavigableMap<K, V2> subMap(K k2, K k3) {
            return this.subMap(k2, true, k3, false);
        }

        @Override
        public NavigableMap<K, V2> tailMap(K k2) {
            return this.tailMap(k2, true);
        }

        @Override
        public NavigableMap<K, V2> tailMap(K k2, boolean bl) {
            return Maps.transformEntries(this.fromMap().tailMap(k2, bl), this.transformer);
        }

        private Map.Entry<K, V2> transformEntry(Map.Entry<K, V1> entry) {
            return entry == null ? null : Maps.transformEntry(this.transformer, entry);
        }

        @Override
        protected NavigableMap<K, V1> fromMap() {
            return (NavigableMap)super.fromMap();
        }
    }

    static class TransformedEntriesSortedMap<K, V1, V2>
    extends TransformedEntriesMap<K, V1, V2>
    implements SortedMap<K, V2> {
        protected SortedMap<K, V1> fromMap() {
            return (SortedMap)this.fromMap;
        }

        TransformedEntriesSortedMap(SortedMap<K, V1> sortedMap, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
            super(sortedMap, entryTransformer);
        }

        @Override
        public Comparator<? super K> comparator() {
            return this.fromMap().comparator();
        }

        @Override
        public K firstKey() {
            return this.fromMap().firstKey();
        }

        @Override
        public SortedMap<K, V2> headMap(K k2) {
            return Maps.transformEntries(this.fromMap().headMap(k2), this.transformer);
        }

        @Override
        public K lastKey() {
            return this.fromMap().lastKey();
        }

        @Override
        public SortedMap<K, V2> subMap(K k2, K k3) {
            return Maps.transformEntries(this.fromMap().subMap(k2, k3), this.transformer);
        }

        @Override
        public SortedMap<K, V2> tailMap(K k2) {
            return Maps.transformEntries(this.fromMap().tailMap(k2), this.transformer);
        }
    }

    static class TransformedEntriesMap<K, V1, V2>
    extends IteratorBasedAbstractMap<K, V2> {
        final Map<K, V1> fromMap;
        final EntryTransformer<? super K, ? super V1, V2> transformer;

        TransformedEntriesMap(Map<K, V1> map, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
            this.fromMap = Preconditions.checkNotNull(map);
            this.transformer = Preconditions.checkNotNull(entryTransformer);
        }

        @Override
        public int size() {
            return this.fromMap.size();
        }

        @Override
        public boolean containsKey(Object object) {
            return this.fromMap.containsKey(object);
        }

        @Override
        public V2 get(Object object) {
            V1 V1 = this.fromMap.get(object);
            return V1 != null || this.fromMap.containsKey(object) ? this.transformer.transformEntry(object, V1) : null;
        }

        @Override
        public V2 remove(Object object) {
            return this.fromMap.containsKey(object) ? (V2)this.transformer.transformEntry((K)object, (V1)this.fromMap.remove(object)) : null;
        }

        @Override
        public void clear() {
            this.fromMap.clear();
        }

        @Override
        public Set<K> keySet() {
            return this.fromMap.keySet();
        }

        @Override
        Iterator<Map.Entry<K, V2>> entryIterator() {
            return Iterators.transform(this.fromMap.entrySet().iterator(), Maps.asEntryToEntryFunction(this.transformer));
        }

        @Override
        public Collection<V2> values() {
            return new Values(this);
        }
    }

    public static interface EntryTransformer<K, V1, V2> {
        public V2 transformEntry(K var1, V1 var2);
    }

    private static abstract class EntryFunction
    extends Enum<EntryFunction>
    implements Function<Map.Entry<?, ?>, Object> {
        public static final /* enum */ EntryFunction KEY = new EntryFunction("KEY", 0){

            @Override
            public Object apply(Map.Entry<?, ?> entry) {
                return entry.getKey();
            }
        };
        public static final /* enum */ EntryFunction VALUE = new EntryFunction("VALUE", 1){

            @Override
            public Object apply(Map.Entry<?, ?> entry) {
                return entry.getValue();
            }
        };
        private static final /* synthetic */ EntryFunction[] $VALUES;

        public static EntryFunction[] values() {
            return (EntryFunction[])$VALUES.clone();
        }

        private EntryFunction() {
            super(string, n2);
        }

        static {
            $VALUES = new EntryFunction[]{KEY, VALUE};
        }

    }

}

