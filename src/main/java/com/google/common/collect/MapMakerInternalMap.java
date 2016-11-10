/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  com.google.j2objc.annotations.Weak
 */
package com.google.common.collect;

import com.google.common.base.Equivalence;
import com.google.common.base.Preconditions;
import com.google.common.base.Ticker;
import com.google.common.collect.AbstractMapEntry;
import com.google.common.collect.AbstractSequentialIterator;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.GenericMapMaker;
import com.google.common.collect.Iterators;
import com.google.common.collect.MapMaker;
import com.google.common.primitives.Ints;
import com.google.j2objc.annotations.Weak;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractQueue;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

class MapMakerInternalMap<K, V>
extends AbstractMap<K, V>
implements Serializable,
ConcurrentMap<K, V> {
    private static final Logger logger = Logger.getLogger(MapMakerInternalMap.class.getName());
    final transient int segmentMask;
    final transient int segmentShift;
    final transient Segment<K, V>[] segments;
    final int concurrencyLevel;
    final Equivalence<Object> keyEquivalence;
    final Equivalence<Object> valueEquivalence;
    final Strength keyStrength;
    final Strength valueStrength;
    final int maximumSize;
    final long expireAfterAccessNanos;
    final long expireAfterWriteNanos;
    final Queue<MapMaker.RemovalNotification<K, V>> removalNotificationQueue;
    final MapMaker.RemovalListener<K, V> removalListener;
    final transient EntryFactory entryFactory;
    final Ticker ticker;
    static final ValueReference<Object, Object> UNSET = new ValueReference<Object, Object>(){

        @Override
        public Object get() {
            return null;
        }

        @Override
        public ReferenceEntry<Object, Object> getEntry() {
            return null;
        }

        @Override
        public ValueReference<Object, Object> copyFor(ReferenceQueue<Object> referenceQueue, Object object, ReferenceEntry<Object, Object> referenceEntry) {
            return this;
        }

        @Override
        public boolean isComputingReference() {
            return false;
        }

        @Override
        public Object waitForValue() {
            return null;
        }

        @Override
        public void clear(ValueReference<Object, Object> valueReference) {
        }
    };
    static final Queue<? extends Object> DISCARDING_QUEUE = new AbstractQueue<Object>(){

        @Override
        public boolean offer(Object object) {
            return true;
        }

        @Override
        public Object peek() {
            return null;
        }

        @Override
        public Object poll() {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public Iterator<Object> iterator() {
            return Iterators.emptyIterator();
        }
    };
    transient Set<K> keySet;
    transient Collection<V> values;
    transient Set<Map.Entry<K, V>> entrySet;

    MapMakerInternalMap(MapMaker mapMaker) {
        int n2;
        int n3;
        this.concurrencyLevel = Math.min(mapMaker.getConcurrencyLevel(), 65536);
        this.keyStrength = mapMaker.getKeyStrength();
        this.valueStrength = mapMaker.getValueStrength();
        this.keyEquivalence = mapMaker.getKeyEquivalence();
        this.valueEquivalence = this.valueStrength.defaultEquivalence();
        this.maximumSize = mapMaker.maximumSize;
        this.expireAfterAccessNanos = mapMaker.getExpireAfterAccessNanos();
        this.expireAfterWriteNanos = mapMaker.getExpireAfterWriteNanos();
        this.entryFactory = EntryFactory.getFactory(this.keyStrength, this.expires(), this.evictsBySize());
        this.ticker = mapMaker.getTicker();
        this.removalListener = mapMaker.getRemovalListener();
        this.removalNotificationQueue = this.removalListener == GenericMapMaker.NullListener.INSTANCE ? MapMakerInternalMap.discardingQueue() : new ConcurrentLinkedQueue();
        int n4 = Math.min(mapMaker.getInitialCapacity(), 1073741824);
        if (this.evictsBySize()) {
            n4 = Math.min(n4, this.maximumSize);
        }
        int n5 = 0;
        for (n3 = 1; !(n3 >= this.concurrencyLevel || this.evictsBySize() && n3 * 2 > this.maximumSize); n3 <<= 1) {
            ++n5;
        }
        this.segmentShift = 32 - n5;
        this.segmentMask = n3 - 1;
        this.segments = this.newSegmentArray(n3);
        int n6 = n4 / n3;
        if (n6 * n3 < n4) {
            ++n6;
        }
        for (n2 = 1; n2 < n6; n2 <<= 1) {
        }
        if (this.evictsBySize()) {
            int n7 = this.maximumSize / n3 + 1;
            int n8 = this.maximumSize % n3;
            for (int i2 = 0; i2 < this.segments.length; ++i2) {
                if (i2 == n8) {
                    --n7;
                }
                this.segments[i2] = this.createSegment(n2, n7);
            }
        } else {
            for (int i3 = 0; i3 < this.segments.length; ++i3) {
                this.segments[i3] = this.createSegment(n2, -1);
            }
        }
    }

    boolean evictsBySize() {
        return this.maximumSize != -1;
    }

    boolean expires() {
        return this.expiresAfterWrite() || this.expiresAfterAccess();
    }

    boolean expiresAfterWrite() {
        return this.expireAfterWriteNanos > 0;
    }

    boolean expiresAfterAccess() {
        return this.expireAfterAccessNanos > 0;
    }

    boolean usesKeyReferences() {
        return this.keyStrength != Strength.STRONG;
    }

    boolean usesValueReferences() {
        return this.valueStrength != Strength.STRONG;
    }

    static <K, V> ValueReference<K, V> unset() {
        return UNSET;
    }

    static <K, V> ReferenceEntry<K, V> nullEntry() {
        return NullEntry.INSTANCE;
    }

    static <E> Queue<E> discardingQueue() {
        return DISCARDING_QUEUE;
    }

    static int rehash(int n2) {
        n2 += n2 << 15 ^ -12931;
        n2 ^= n2 >>> 10;
        n2 += n2 << 3;
        n2 ^= n2 >>> 6;
        n2 += (n2 << 2) + (n2 << 14);
        return n2 ^ n2 >>> 16;
    }

    int hash(Object object) {
        int n2 = this.keyEquivalence.hash(object);
        return MapMakerInternalMap.rehash(n2);
    }

    void reclaimValue(ValueReference<K, V> valueReference) {
        ReferenceEntry<K, V> referenceEntry = valueReference.getEntry();
        int n2 = referenceEntry.getHash();
        this.segmentFor(n2).reclaimValue(referenceEntry.getKey(), n2, valueReference);
    }

    void reclaimKey(ReferenceEntry<K, V> referenceEntry) {
        int n2 = referenceEntry.getHash();
        this.segmentFor(n2).reclaimKey(referenceEntry, n2);
    }

    Segment<K, V> segmentFor(int n2) {
        return this.segments[n2 >>> this.segmentShift & this.segmentMask];
    }

    Segment<K, V> createSegment(int n2, int n3) {
        return new Segment(this, n2, n3);
    }

    V getLiveValue(ReferenceEntry<K, V> referenceEntry) {
        if (referenceEntry.getKey() == null) {
            return null;
        }
        V v2 = referenceEntry.getValueReference().get();
        if (v2 == null) {
            return null;
        }
        if (this.expires() && this.isExpired(referenceEntry)) {
            return null;
        }
        return v2;
    }

    boolean isExpired(ReferenceEntry<K, V> referenceEntry) {
        return this.isExpired(referenceEntry, this.ticker.read());
    }

    boolean isExpired(ReferenceEntry<K, V> referenceEntry, long l2) {
        return l2 - referenceEntry.getExpirationTime() > 0;
    }

    static <K, V> void connectExpirables(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
        referenceEntry.setNextExpirable(referenceEntry2);
        referenceEntry2.setPreviousExpirable(referenceEntry);
    }

    static <K, V> void nullifyExpirable(ReferenceEntry<K, V> referenceEntry) {
        ReferenceEntry<K, V> referenceEntry2 = MapMakerInternalMap.nullEntry();
        referenceEntry.setNextExpirable(referenceEntry2);
        referenceEntry.setPreviousExpirable(referenceEntry2);
    }

    void processPendingNotifications() {
        MapMaker.RemovalNotification<K, V> removalNotification;
        while ((removalNotification = this.removalNotificationQueue.poll()) != null) {
            try {
                this.removalListener.onRemoval(removalNotification);
            }
            catch (Exception exception) {
                logger.log(Level.WARNING, "Exception thrown by removal listener", exception);
            }
        }
    }

    static <K, V> void connectEvictables(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
        referenceEntry.setNextEvictable(referenceEntry2);
        referenceEntry2.setPreviousEvictable(referenceEntry);
    }

    static <K, V> void nullifyEvictable(ReferenceEntry<K, V> referenceEntry) {
        ReferenceEntry<K, V> referenceEntry2 = MapMakerInternalMap.nullEntry();
        referenceEntry.setNextEvictable(referenceEntry2);
        referenceEntry.setPreviousEvictable(referenceEntry2);
    }

    final Segment<K, V>[] newSegmentArray(int n2) {
        return new Segment[n2];
    }

    @Override
    public boolean isEmpty() {
        int n2;
        long l2 = 0;
        Segment<K, V>[] arrsegment = this.segments;
        for (n2 = 0; n2 < arrsegment.length; ++n2) {
            if (arrsegment[n2].count != 0) {
                return false;
            }
            l2 += (long)arrsegment[n2].modCount;
        }
        if (l2 != 0) {
            for (n2 = 0; n2 < arrsegment.length; ++n2) {
                if (arrsegment[n2].count != 0) {
                    return false;
                }
                l2 -= (long)arrsegment[n2].modCount;
            }
            if (l2 != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int size() {
        Segment<K, V>[] arrsegment = this.segments;
        long l2 = 0;
        for (int i2 = 0; i2 < arrsegment.length; ++i2) {
            l2 += (long)arrsegment[i2].count;
        }
        return Ints.saturatedCast(l2);
    }

    @Override
    public V get(Object object) {
        if (object == null) {
            return null;
        }
        int n2 = this.hash(object);
        return this.segmentFor(n2).get(object, n2);
    }

    @Override
    public boolean containsKey(Object object) {
        if (object == null) {
            return false;
        }
        int n2 = this.hash(object);
        return this.segmentFor(n2).containsKey(object, n2);
    }

    @Override
    public boolean containsValue(Object object) {
        if (object == null) {
            return false;
        }
        Segment<K, V>[] arrsegment = this.segments;
        long l2 = -1;
        for (int i2 = 0; i2 < 3; ++i2) {
            long l3 = 0;
            for (Segment segment : arrsegment) {
                int n2 = segment.count;
                AtomicReferenceArray atomicReferenceArray = segment.table;
                for (int i3 = 0; i3 < atomicReferenceArray.length(); ++i3) {
                    for (ReferenceEntry referenceEntry = atomicReferenceArray.get((int)i3); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                        V v2 = segment.getLiveValue(referenceEntry);
                        if (v2 == null || !this.valueEquivalence.equivalent(object, v2)) continue;
                        return true;
                    }
                }
                l3 += (long)segment.modCount;
            }
            if (l3 == l2) break;
            l2 = l3;
        }
        return false;
    }

    @Override
    public V put(K k2, V v2) {
        Preconditions.checkNotNull(k2);
        Preconditions.checkNotNull(v2);
        int n2 = this.hash(k2);
        return this.segmentFor(n2).put(k2, n2, v2, false);
    }

    @Override
    public V putIfAbsent(K k2, V v2) {
        Preconditions.checkNotNull(k2);
        Preconditions.checkNotNull(v2);
        int n2 = this.hash(k2);
        return this.segmentFor(n2).put(k2, n2, v2, true);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public V remove(Object object) {
        if (object == null) {
            return null;
        }
        int n2 = this.hash(object);
        return this.segmentFor(n2).remove(object, n2);
    }

    @Override
    public boolean remove(Object object, Object object2) {
        if (object == null || object2 == null) {
            return false;
        }
        int n2 = this.hash(object);
        return this.segmentFor(n2).remove(object, n2, object2);
    }

    @Override
    public boolean replace(K k2, V v2, V v3) {
        Preconditions.checkNotNull(k2);
        Preconditions.checkNotNull(v3);
        if (v2 == null) {
            return false;
        }
        int n2 = this.hash(k2);
        return this.segmentFor(n2).replace(k2, n2, v2, v3);
    }

    @Override
    public V replace(K k2, V v2) {
        Preconditions.checkNotNull(k2);
        Preconditions.checkNotNull(v2);
        int n2 = this.hash(k2);
        return this.segmentFor(n2).replace(k2, n2, v2);
    }

    @Override
    public void clear() {
        for (Segment<K, V> segment : this.segments) {
            segment.clear();
        }
    }

    @Override
    public Set<K> keySet() {
        KeySet keySet = this.keySet;
        KeySet keySet2 = keySet != null ? keySet : (this.keySet = new KeySet());
        return keySet2;
    }

    @Override
    public Collection<V> values() {
        Values values = this.values;
        Values values2 = values != null ? values : (this.values = new Values());
        return values2;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        EntrySet entrySet = this.entrySet;
        EntrySet entrySet2 = entrySet != null ? entrySet : (this.entrySet = new EntrySet());
        return entrySet2;
    }

    private static <E> ArrayList<E> toArrayList(Collection<E> collection) {
        ArrayList arrayList = new ArrayList(collection.size());
        Iterators.addAll(arrayList, collection.iterator());
        return arrayList;
    }

    private static abstract class SafeToArraySet<E>
    extends AbstractSet<E> {
        private SafeToArraySet() {
        }

        @Override
        public Object[] toArray() {
            return MapMakerInternalMap.toArrayList(this).toArray();
        }

        @Override
        public <E> E[] toArray(E[] arrE) {
            return MapMakerInternalMap.toArrayList(this).toArray(arrE);
        }
    }

    final class EntrySet
    extends SafeToArraySet<Map.Entry<K, V>> {
        EntrySet() {
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public boolean contains(Object object) {
            if (!(object instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry)object;
            Object k2 = entry.getKey();
            if (k2 == null) {
                return false;
            }
            Object v2 = MapMakerInternalMap.this.get(k2);
            return v2 != null && MapMakerInternalMap.this.valueEquivalence.equivalent(entry.getValue(), v2);
        }

        @Override
        public boolean remove(Object object) {
            if (!(object instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry)object;
            Object k2 = entry.getKey();
            return k2 != null && MapMakerInternalMap.this.remove(k2, entry.getValue());
        }

        @Override
        public int size() {
            return MapMakerInternalMap.this.size();
        }

        @Override
        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        @Override
        public void clear() {
            MapMakerInternalMap.this.clear();
        }
    }

    final class Values
    extends AbstractCollection<V> {
        Values() {
        }

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            return MapMakerInternalMap.this.size();
        }

        @Override
        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        @Override
        public boolean contains(Object object) {
            return MapMakerInternalMap.this.containsValue(object);
        }

        @Override
        public void clear() {
            MapMakerInternalMap.this.clear();
        }

        @Override
        public Object[] toArray() {
            return MapMakerInternalMap.toArrayList(this).toArray();
        }

        @Override
        public <E> E[] toArray(E[] arrE) {
            return MapMakerInternalMap.toArrayList(this).toArray(arrE);
        }
    }

    final class KeySet
    extends SafeToArraySet<K> {
        KeySet() {
        }

        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public int size() {
            return MapMakerInternalMap.this.size();
        }

        @Override
        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        @Override
        public boolean contains(Object object) {
            return MapMakerInternalMap.this.containsKey(object);
        }

        @Override
        public boolean remove(Object object) {
            return MapMakerInternalMap.this.remove(object) != null;
        }

        @Override
        public void clear() {
            MapMakerInternalMap.this.clear();
        }
    }

    final class EntryIterator
    extends MapMakerInternalMap<K, V> {
        EntryIterator() {
            super();
        }

        public Map.Entry<K, V> next() {
            return this.nextEntry();
        }
    }

    final class WriteThroughEntry
    extends AbstractMapEntry<K, V> {
        final K key;
        V value;

        WriteThroughEntry(K k2, V v2) {
            this.key = k2;
            this.value = v2;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry)object;
                return this.key.equals(entry.getKey()) && this.value.equals(entry.getValue());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return this.key.hashCode() ^ this.value.hashCode();
        }

        @Override
        public V setValue(V v2) {
            V v3 = MapMakerInternalMap.this.put(this.key, v2);
            this.value = v2;
            return v3;
        }
    }

    final class ValueIterator
    extends MapMakerInternalMap<K, V> {
        ValueIterator() {
            super();
        }

        public V next() {
            return this.nextEntry().getValue();
        }
    }

    final class KeyIterator
    extends MapMakerInternalMap<K, V> {
        KeyIterator() {
            super();
        }

        public K next() {
            return this.nextEntry().getKey();
        }
    }

    abstract class HashIterator<E>
    implements Iterator<E> {
        int nextSegmentIndex;
        int nextTableIndex;
        Segment<K, V> currentSegment;
        AtomicReferenceArray<ReferenceEntry<K, V>> currentTable;
        ReferenceEntry<K, V> nextEntry;
        MapMakerInternalMap<K, V> nextExternal;
        MapMakerInternalMap<K, V> lastReturned;

        HashIterator() {
            this.nextSegmentIndex = MapMakerInternalMap.this.segments.length - 1;
            this.nextTableIndex = -1;
            this.advance();
        }

        final void advance() {
            this.nextExternal = null;
            if (this.nextInChain()) {
                return;
            }
            if (this.nextInTable()) {
                return;
            }
            while (this.nextSegmentIndex >= 0) {
                this.currentSegment = MapMakerInternalMap.this.segments[this.nextSegmentIndex--];
                if (this.currentSegment.count == 0) continue;
                this.currentTable = this.currentSegment.table;
                this.nextTableIndex = this.currentTable.length() - 1;
                if (!this.nextInTable()) continue;
                return;
            }
        }

        boolean nextInChain() {
            if (this.nextEntry != null) {
                this.nextEntry = this.nextEntry.getNext();
                while (this.nextEntry != null) {
                    if (this.advanceTo(this.nextEntry)) {
                        return true;
                    }
                    this.nextEntry = this.nextEntry.getNext();
                }
            }
            return false;
        }

        boolean nextInTable() {
            while (this.nextTableIndex >= 0) {
                if ((this.nextEntry = this.currentTable.get(this.nextTableIndex--)) == null || !this.advanceTo(this.nextEntry) && !this.nextInChain()) continue;
                return true;
            }
            return false;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        boolean advanceTo(ReferenceEntry<K, V> referenceEntry) {
            try {
                K k2 = referenceEntry.getKey();
                V v2 = MapMakerInternalMap.this.getLiveValue(referenceEntry);
                if (v2 != null) {
                    this.nextExternal = new WriteThroughEntry(k2, v2);
                    boolean bl = true;
                    return bl;
                }
                boolean bl = false;
                return bl;
            }
            finally {
                this.currentSegment.postReadCleanup();
            }
        }

        @Override
        public boolean hasNext() {
            return this.nextExternal != null;
        }

        MapMakerInternalMap<K, V> nextEntry() {
            if (this.nextExternal == null) {
                throw new NoSuchElementException();
            }
            this.lastReturned = this.nextExternal;
            this.advance();
            return this.lastReturned;
        }

        @Override
        public void remove() {
            CollectPreconditions.checkRemove(this.lastReturned != null);
            MapMakerInternalMap.this.remove(this.lastReturned.getKey());
            this.lastReturned = null;
        }
    }

    static final class ExpirationQueue<K, V>
    extends AbstractQueue<ReferenceEntry<K, V>> {
        final ReferenceEntry<K, V> head;

        ExpirationQueue() {
            this.head = new AbstractReferenceEntry<K, V>(){
                ReferenceEntry<K, V> nextExpirable;
                ReferenceEntry<K, V> previousExpirable;

                @Override
                public long getExpirationTime() {
                    return Long.MAX_VALUE;
                }

                @Override
                public void setExpirationTime(long l2) {
                }

                @Override
                public ReferenceEntry<K, V> getNextExpirable() {
                    return this.nextExpirable;
                }

                @Override
                public void setNextExpirable(ReferenceEntry<K, V> referenceEntry) {
                    this.nextExpirable = referenceEntry;
                }

                @Override
                public ReferenceEntry<K, V> getPreviousExpirable() {
                    return this.previousExpirable;
                }

                @Override
                public void setPreviousExpirable(ReferenceEntry<K, V> referenceEntry) {
                    this.previousExpirable = referenceEntry;
                }
            };
        }

        @Override
        public boolean offer(ReferenceEntry<K, V> referenceEntry) {
            MapMakerInternalMap.connectExpirables(referenceEntry.getPreviousExpirable(), referenceEntry.getNextExpirable());
            MapMakerInternalMap.connectExpirables(this.head.getPreviousExpirable(), referenceEntry);
            MapMakerInternalMap.connectExpirables(referenceEntry, this.head);
            return true;
        }

        @Override
        public ReferenceEntry<K, V> peek() {
            ReferenceEntry<K, V> referenceEntry = this.head.getNextExpirable();
            return referenceEntry == this.head ? null : referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> poll() {
            ReferenceEntry<K, V> referenceEntry = this.head.getNextExpirable();
            if (referenceEntry == this.head) {
                return null;
            }
            this.remove(referenceEntry);
            return referenceEntry;
        }

        @Override
        public boolean remove(Object object) {
            ReferenceEntry referenceEntry = (ReferenceEntry)object;
            ReferenceEntry referenceEntry2 = referenceEntry.getPreviousExpirable();
            ReferenceEntry referenceEntry3 = referenceEntry.getNextExpirable();
            MapMakerInternalMap.connectExpirables(referenceEntry2, referenceEntry3);
            MapMakerInternalMap.nullifyExpirable(referenceEntry);
            return referenceEntry3 != NullEntry.INSTANCE;
        }

        @Override
        public boolean contains(Object object) {
            ReferenceEntry referenceEntry = (ReferenceEntry)object;
            return referenceEntry.getNextExpirable() != NullEntry.INSTANCE;
        }

        @Override
        public boolean isEmpty() {
            return this.head.getNextExpirable() == this.head;
        }

        @Override
        public int size() {
            int n2 = 0;
            for (ReferenceEntry<K, V> referenceEntry = this.head.getNextExpirable(); referenceEntry != this.head; referenceEntry = referenceEntry.getNextExpirable()) {
                ++n2;
            }
            return n2;
        }

        @Override
        public void clear() {
            ReferenceEntry<K, V> referenceEntry = this.head.getNextExpirable();
            while (referenceEntry != this.head) {
                ReferenceEntry<K, V> referenceEntry2 = referenceEntry.getNextExpirable();
                MapMakerInternalMap.nullifyExpirable(referenceEntry);
                referenceEntry = referenceEntry2;
            }
            this.head.setNextExpirable(this.head);
            this.head.setPreviousExpirable(this.head);
        }

        @Override
        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>((ReferenceEntry)this.peek()){

                @Override
                protected ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> referenceEntry) {
                    ReferenceEntry<K, V> referenceEntry2 = referenceEntry.getNextExpirable();
                    return referenceEntry2 == ExpirationQueue.this.head ? null : referenceEntry2;
                }
            };
        }

    }

    static final class EvictionQueue<K, V>
    extends AbstractQueue<ReferenceEntry<K, V>> {
        final ReferenceEntry<K, V> head;

        EvictionQueue() {
            this.head = new AbstractReferenceEntry<K, V>(){
                ReferenceEntry<K, V> nextEvictable;
                ReferenceEntry<K, V> previousEvictable;

                @Override
                public ReferenceEntry<K, V> getNextEvictable() {
                    return this.nextEvictable;
                }

                @Override
                public void setNextEvictable(ReferenceEntry<K, V> referenceEntry) {
                    this.nextEvictable = referenceEntry;
                }

                @Override
                public ReferenceEntry<K, V> getPreviousEvictable() {
                    return this.previousEvictable;
                }

                @Override
                public void setPreviousEvictable(ReferenceEntry<K, V> referenceEntry) {
                    this.previousEvictable = referenceEntry;
                }
            };
        }

        @Override
        public boolean offer(ReferenceEntry<K, V> referenceEntry) {
            MapMakerInternalMap.connectEvictables(referenceEntry.getPreviousEvictable(), referenceEntry.getNextEvictable());
            MapMakerInternalMap.connectEvictables(this.head.getPreviousEvictable(), referenceEntry);
            MapMakerInternalMap.connectEvictables(referenceEntry, this.head);
            return true;
        }

        @Override
        public ReferenceEntry<K, V> peek() {
            ReferenceEntry<K, V> referenceEntry = this.head.getNextEvictable();
            return referenceEntry == this.head ? null : referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> poll() {
            ReferenceEntry<K, V> referenceEntry = this.head.getNextEvictable();
            if (referenceEntry == this.head) {
                return null;
            }
            this.remove(referenceEntry);
            return referenceEntry;
        }

        @Override
        public boolean remove(Object object) {
            ReferenceEntry referenceEntry = (ReferenceEntry)object;
            ReferenceEntry referenceEntry2 = referenceEntry.getPreviousEvictable();
            ReferenceEntry referenceEntry3 = referenceEntry.getNextEvictable();
            MapMakerInternalMap.connectEvictables(referenceEntry2, referenceEntry3);
            MapMakerInternalMap.nullifyEvictable(referenceEntry);
            return referenceEntry3 != NullEntry.INSTANCE;
        }

        @Override
        public boolean contains(Object object) {
            ReferenceEntry referenceEntry = (ReferenceEntry)object;
            return referenceEntry.getNextEvictable() != NullEntry.INSTANCE;
        }

        @Override
        public boolean isEmpty() {
            return this.head.getNextEvictable() == this.head;
        }

        @Override
        public int size() {
            int n2 = 0;
            for (ReferenceEntry<K, V> referenceEntry = this.head.getNextEvictable(); referenceEntry != this.head; referenceEntry = referenceEntry.getNextEvictable()) {
                ++n2;
            }
            return n2;
        }

        @Override
        public void clear() {
            ReferenceEntry<K, V> referenceEntry = this.head.getNextEvictable();
            while (referenceEntry != this.head) {
                ReferenceEntry<K, V> referenceEntry2 = referenceEntry.getNextEvictable();
                MapMakerInternalMap.nullifyEvictable(referenceEntry);
                referenceEntry = referenceEntry2;
            }
            this.head.setNextEvictable(this.head);
            this.head.setPreviousEvictable(this.head);
        }

        @Override
        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>((ReferenceEntry)this.peek()){

                @Override
                protected ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> referenceEntry) {
                    ReferenceEntry<K, V> referenceEntry2 = referenceEntry.getNextEvictable();
                    return referenceEntry2 == EvictionQueue.this.head ? null : referenceEntry2;
                }
            };
        }

    }

    static class Segment<K, V>
    extends ReentrantLock {
        @Weak
        final MapMakerInternalMap<K, V> map;
        volatile int count;
        int modCount;
        int threshold;
        volatile AtomicReferenceArray<ReferenceEntry<K, V>> table;
        final int maxSegmentSize;
        final ReferenceQueue<K> keyReferenceQueue;
        final ReferenceQueue<V> valueReferenceQueue;
        final Queue<ReferenceEntry<K, V>> recencyQueue;
        final AtomicInteger readCount = new AtomicInteger();
        final Queue<ReferenceEntry<K, V>> evictionQueue;
        final Queue<ReferenceEntry<K, V>> expirationQueue;

        Segment(MapMakerInternalMap<K, V> mapMakerInternalMap, int n2, int n3) {
            this.map = mapMakerInternalMap;
            this.maxSegmentSize = n3;
            this.initTable(this.newEntryArray(n2));
            this.keyReferenceQueue = mapMakerInternalMap.usesKeyReferences() ? new ReferenceQueue() : null;
            this.valueReferenceQueue = mapMakerInternalMap.usesValueReferences() ? new ReferenceQueue() : null;
            this.recencyQueue = mapMakerInternalMap.evictsBySize() || mapMakerInternalMap.expiresAfterAccess() ? new ConcurrentLinkedQueue() : MapMakerInternalMap.discardingQueue();
            this.evictionQueue = mapMakerInternalMap.evictsBySize() ? new EvictionQueue() : MapMakerInternalMap.discardingQueue();
            this.expirationQueue = mapMakerInternalMap.expires() ? new ExpirationQueue() : MapMakerInternalMap.discardingQueue();
        }

        AtomicReferenceArray<ReferenceEntry<K, V>> newEntryArray(int n2) {
            return new AtomicReferenceArray<ReferenceEntry<K, V>>(n2);
        }

        void initTable(AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray) {
            this.threshold = atomicReferenceArray.length() * 3 / 4;
            if (this.threshold == this.maxSegmentSize) {
                ++this.threshold;
            }
            this.table = atomicReferenceArray;
        }

        ReferenceEntry<K, V> newEntry(K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
            return this.map.entryFactory.newEntry(this, k2, n2, referenceEntry);
        }

        ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            if (referenceEntry.getKey() == null) {
                return null;
            }
            ValueReference<K, V> valueReference = referenceEntry.getValueReference();
            V v2 = valueReference.get();
            if (v2 == null && !valueReference.isComputingReference()) {
                return null;
            }
            ReferenceEntry<K, V> referenceEntry3 = this.map.entryFactory.copyEntry(this, referenceEntry, referenceEntry2);
            referenceEntry3.setValueReference(valueReference.copyFor(this.valueReferenceQueue, v2, referenceEntry3));
            return referenceEntry3;
        }

        void setValue(ReferenceEntry<K, V> referenceEntry, V v2) {
            ValueReference<K, V> valueReference = this.map.valueStrength.referenceValue(this, referenceEntry, v2);
            referenceEntry.setValueReference(valueReference);
            this.recordWrite(referenceEntry);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        void tryDrainReferenceQueues() {
            if (this.tryLock()) {
                try {
                    this.drainReferenceQueues();
                }
                finally {
                    this.unlock();
                }
            }
        }

        void drainReferenceQueues() {
            if (this.map.usesKeyReferences()) {
                this.drainKeyReferenceQueue();
            }
            if (this.map.usesValueReferences()) {
                this.drainValueReferenceQueue();
            }
        }

        void drainKeyReferenceQueue() {
            Reference<K> reference;
            int n2 = 0;
            while ((reference = this.keyReferenceQueue.poll()) != null) {
                ReferenceEntry referenceEntry = (ReferenceEntry)((Object)reference);
                this.map.reclaimKey(referenceEntry);
                if (++n2 != 16) continue;
                break;
            }
        }

        void drainValueReferenceQueue() {
            Reference<V> reference;
            int n2 = 0;
            while ((reference = this.valueReferenceQueue.poll()) != null) {
                ValueReference valueReference = (ValueReference)((Object)reference);
                this.map.reclaimValue(valueReference);
                if (++n2 != 16) continue;
                break;
            }
        }

        void clearReferenceQueues() {
            if (this.map.usesKeyReferences()) {
                this.clearKeyReferenceQueue();
            }
            if (this.map.usesValueReferences()) {
                this.clearValueReferenceQueue();
            }
        }

        void clearKeyReferenceQueue() {
            while (this.keyReferenceQueue.poll() != null) {
            }
        }

        void clearValueReferenceQueue() {
            while (this.valueReferenceQueue.poll() != null) {
            }
        }

        void recordRead(ReferenceEntry<K, V> referenceEntry) {
            if (this.map.expiresAfterAccess()) {
                this.recordExpirationTime(referenceEntry, this.map.expireAfterAccessNanos);
            }
            this.recencyQueue.add(referenceEntry);
        }

        void recordLockedRead(ReferenceEntry<K, V> referenceEntry) {
            this.evictionQueue.add(referenceEntry);
            if (this.map.expiresAfterAccess()) {
                this.recordExpirationTime(referenceEntry, this.map.expireAfterAccessNanos);
                this.expirationQueue.add(referenceEntry);
            }
        }

        void recordWrite(ReferenceEntry<K, V> referenceEntry) {
            this.drainRecencyQueue();
            this.evictionQueue.add(referenceEntry);
            if (this.map.expires()) {
                long l2 = this.map.expiresAfterAccess() ? this.map.expireAfterAccessNanos : this.map.expireAfterWriteNanos;
                this.recordExpirationTime(referenceEntry, l2);
                this.expirationQueue.add(referenceEntry);
            }
        }

        void drainRecencyQueue() {
            ReferenceEntry<K, V> referenceEntry;
            while ((referenceEntry = this.recencyQueue.poll()) != null) {
                if (this.evictionQueue.contains(referenceEntry)) {
                    this.evictionQueue.add(referenceEntry);
                }
                if (!this.map.expiresAfterAccess() || !this.expirationQueue.contains(referenceEntry)) continue;
                this.expirationQueue.add(referenceEntry);
            }
        }

        void recordExpirationTime(ReferenceEntry<K, V> referenceEntry, long l2) {
            referenceEntry.setExpirationTime(this.map.ticker.read() + l2);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        void tryExpireEntries() {
            if (this.tryLock()) {
                try {
                    this.expireEntries();
                }
                finally {
                    this.unlock();
                }
            }
        }

        void expireEntries() {
            ReferenceEntry<K, V> referenceEntry;
            this.drainRecencyQueue();
            if (this.expirationQueue.isEmpty()) {
                return;
            }
            long l2 = this.map.ticker.read();
            while ((referenceEntry = this.expirationQueue.peek()) != null && this.map.isExpired(referenceEntry, l2)) {
                if (!this.removeEntry(referenceEntry, referenceEntry.getHash(), MapMaker.RemovalCause.EXPIRED)) {
                    throw new AssertionError();
                }
            }
        }

        void enqueueNotification(ReferenceEntry<K, V> referenceEntry, MapMaker.RemovalCause removalCause) {
            this.enqueueNotification(referenceEntry.getKey(), referenceEntry.getHash(), referenceEntry.getValueReference().get(), removalCause);
        }

        void enqueueNotification(K k2, int n2, V v2, MapMaker.RemovalCause removalCause) {
            if (this.map.removalNotificationQueue != MapMakerInternalMap.DISCARDING_QUEUE) {
                MapMaker.RemovalNotification<K, V> removalNotification = new MapMaker.RemovalNotification<K, V>(k2, v2, removalCause);
                this.map.removalNotificationQueue.offer(removalNotification);
            }
        }

        boolean evictEntries() {
            if (this.map.evictsBySize() && this.count >= this.maxSegmentSize) {
                this.drainRecencyQueue();
                ReferenceEntry<K, V> referenceEntry = this.evictionQueue.remove();
                if (!this.removeEntry(referenceEntry, referenceEntry.getHash(), MapMaker.RemovalCause.SIZE)) {
                    throw new AssertionError();
                }
                return true;
            }
            return false;
        }

        ReferenceEntry<K, V> getFirst(int n2) {
            AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
            return atomicReferenceArray.get(n2 & atomicReferenceArray.length() - 1);
        }

        ReferenceEntry<K, V> getEntry(Object object, int n2) {
            if (this.count != 0) {
                for (ReferenceEntry<K, V> referenceEntry = this.getFirst((int)n2); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                    if (referenceEntry.getHash() != n2) continue;
                    K k2 = referenceEntry.getKey();
                    if (k2 == null) {
                        this.tryDrainReferenceQueues();
                        continue;
                    }
                    if (!this.map.keyEquivalence.equivalent(object, k2)) continue;
                    return referenceEntry;
                }
            }
            return null;
        }

        ReferenceEntry<K, V> getLiveEntry(Object object, int n2) {
            ReferenceEntry<K, V> referenceEntry = this.getEntry(object, n2);
            if (referenceEntry == null) {
                return null;
            }
            if (this.map.expires() && this.map.isExpired(referenceEntry)) {
                this.tryExpireEntries();
                return null;
            }
            return referenceEntry;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        V get(Object object, int n2) {
            try {
                ReferenceEntry<K, V> referenceEntry = this.getLiveEntry(object, n2);
                if (referenceEntry == null) {
                    V v2 = null;
                    return v2;
                }
                V v3 = referenceEntry.getValueReference().get();
                if (v3 != null) {
                    this.recordRead(referenceEntry);
                } else {
                    this.tryDrainReferenceQueues();
                }
                V v4 = v3;
                return v4;
            }
            finally {
                this.postReadCleanup();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        boolean containsKey(Object object, int n2) {
            try {
                if (this.count != 0) {
                    ReferenceEntry<K, V> referenceEntry = this.getLiveEntry(object, n2);
                    if (referenceEntry == null) {
                        boolean bl = false;
                        return bl;
                    }
                    boolean bl = referenceEntry.getValueReference().get() != null;
                    return bl;
                }
                boolean bl = false;
                return bl;
            }
            finally {
                this.postReadCleanup();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        V put(K k2, int n2, V v2, boolean bl) {
            this.lock();
            try {
                K k3;
                ReferenceEntry<K, V> referenceEntry;
                ReferenceEntry<K, V> referenceEntry2;
                this.preWriteCleanup();
                int n3 = this.count + 1;
                if (n3 > this.threshold) {
                    this.expand();
                    n3 = this.count + 1;
                }
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int n4 = n2 & atomicReferenceArray.length() - 1;
                for (referenceEntry = referenceEntry2 = atomicReferenceArray.get((int)n4); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                    k3 = referenceEntry.getKey();
                    if (referenceEntry.getHash() != n2 || k3 == null || !this.map.keyEquivalence.equivalent(k2, k3)) continue;
                    ValueReference<K, V> valueReference = referenceEntry.getValueReference();
                    V v3 = valueReference.get();
                    if (v3 == null) {
                        ++this.modCount;
                        this.setValue(referenceEntry, v2);
                        if (!valueReference.isComputingReference()) {
                            this.enqueueNotification(k2, n2, v3, MapMaker.RemovalCause.COLLECTED);
                            n3 = this.count;
                        } else if (this.evictEntries()) {
                            n3 = this.count + 1;
                        }
                        this.count = n3;
                        V v4 = null;
                        return v4;
                    }
                    if (bl) {
                        this.recordLockedRead(referenceEntry);
                        V v5 = v3;
                        return v5;
                    }
                    ++this.modCount;
                    this.enqueueNotification(k2, n2, v3, MapMaker.RemovalCause.REPLACED);
                    this.setValue(referenceEntry, v2);
                    V v6 = v3;
                    return v6;
                }
                ++this.modCount;
                referenceEntry = this.newEntry(k2, n2, referenceEntry2);
                this.setValue(referenceEntry, v2);
                atomicReferenceArray.set(n4, referenceEntry);
                if (this.evictEntries()) {
                    n3 = this.count + 1;
                }
                this.count = n3;
                k3 = null;
                return (V)k3;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }

        void expand() {
            AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
            int n2 = atomicReferenceArray.length();
            if (n2 >= 1073741824) {
                return;
            }
            int n3 = this.count;
            AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray2 = this.newEntryArray(n2 << 1);
            this.threshold = atomicReferenceArray2.length() * 3 / 4;
            int n4 = atomicReferenceArray2.length() - 1;
            for (int i2 = 0; i2 < n2; ++i2) {
                int n5;
                ReferenceEntry<K, V> referenceEntry;
                ReferenceEntry<K, V> referenceEntry2 = atomicReferenceArray.get(i2);
                if (referenceEntry2 == null) continue;
                ReferenceEntry<K, V> referenceEntry3 = referenceEntry2.getNext();
                int n6 = referenceEntry2.getHash() & n4;
                if (referenceEntry3 == null) {
                    atomicReferenceArray2.set(n6, referenceEntry2);
                    continue;
                }
                ReferenceEntry<K, V> referenceEntry4 = referenceEntry2;
                int n7 = n6;
                for (referenceEntry = referenceEntry3; referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                    n5 = referenceEntry.getHash() & n4;
                    if (n5 == n7) continue;
                    n7 = n5;
                    referenceEntry4 = referenceEntry;
                }
                atomicReferenceArray2.set(n7, referenceEntry4);
                for (referenceEntry = referenceEntry2; referenceEntry != referenceEntry4; referenceEntry = referenceEntry.getNext()) {
                    n5 = referenceEntry.getHash() & n4;
                    ReferenceEntry<K, V> referenceEntry5 = atomicReferenceArray2.get(n5);
                    ReferenceEntry<K, V> referenceEntry6 = this.copyEntry(referenceEntry, referenceEntry5);
                    if (referenceEntry6 != null) {
                        atomicReferenceArray2.set(n5, referenceEntry6);
                        continue;
                    }
                    this.removeCollectedEntry(referenceEntry);
                    --n3;
                }
            }
            this.table = atomicReferenceArray2;
            this.count = n3;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        boolean replace(K k2, int n2, V v2, V v3) {
            this.lock();
            try {
                this.preWriteCleanup();
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int n3 = n2 & atomicReferenceArray.length() - 1;
                for (ReferenceEntry<K, V> referenceEntry = referenceEntry2 = atomicReferenceArray.get((int)n3); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                    K k3 = referenceEntry.getKey();
                    if (referenceEntry.getHash() != n2 || k3 == null || !this.map.keyEquivalence.equivalent(k2, k3)) continue;
                    ValueReference<K, V> valueReference = referenceEntry.getValueReference();
                    V v4 = valueReference.get();
                    if (v4 == null) {
                        int n4;
                        if (this.isCollected(valueReference)) {
                            ReferenceEntry<K, V> referenceEntry2;
                            n4 = this.count - 1;
                            ++this.modCount;
                            this.enqueueNotification(k3, n2, v4, MapMaker.RemovalCause.COLLECTED);
                            ReferenceEntry<K, V> referenceEntry3 = this.removeFromChain(referenceEntry2, referenceEntry);
                            n4 = this.count - 1;
                            atomicReferenceArray.set(n3, referenceEntry3);
                            this.count = n4;
                        }
                        n4 = 0;
                        return (boolean)n4;
                    }
                    if (this.map.valueEquivalence.equivalent(v2, v4)) {
                        ++this.modCount;
                        this.enqueueNotification(k2, n2, v4, MapMaker.RemovalCause.REPLACED);
                        this.setValue(referenceEntry, v3);
                        boolean bl = true;
                        return bl;
                    }
                    this.recordLockedRead(referenceEntry);
                    boolean bl = false;
                    return bl;
                }
                boolean bl = false;
                return bl;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        V replace(K k2, int n2, V v2) {
            this.lock();
            try {
                ReferenceEntry<K, V> referenceEntry;
                this.preWriteCleanup();
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int n3 = n2 & atomicReferenceArray.length() - 1;
                for (referenceEntry = referenceEntry2 = atomicReferenceArray.get((int)n3); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                    K k3 = referenceEntry.getKey();
                    if (referenceEntry.getHash() != n2 || k3 == null || !this.map.keyEquivalence.equivalent(k2, k3)) continue;
                    ValueReference<K, V> valueReference = referenceEntry.getValueReference();
                    V v3 = valueReference.get();
                    if (v3 == null) {
                        if (this.isCollected(valueReference)) {
                            ReferenceEntry<K, V> referenceEntry2;
                            int n4 = this.count - 1;
                            ++this.modCount;
                            this.enqueueNotification(k3, n2, v3, MapMaker.RemovalCause.COLLECTED);
                            ReferenceEntry<K, V> referenceEntry3 = this.removeFromChain(referenceEntry2, referenceEntry);
                            n4 = this.count - 1;
                            atomicReferenceArray.set(n3, referenceEntry3);
                            this.count = n4;
                        }
                        V v4 = null;
                        return v4;
                    }
                    ++this.modCount;
                    this.enqueueNotification(k2, n2, v3, MapMaker.RemovalCause.REPLACED);
                    this.setValue(referenceEntry, v2);
                    V v5 = v3;
                    return v5;
                }
                referenceEntry = null;
                return (V)referenceEntry;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        V remove(Object object, int n2) {
            this.lock();
            try {
                ReferenceEntry<K, V> referenceEntry;
                this.preWriteCleanup();
                int n3 = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int n4 = n2 & atomicReferenceArray.length() - 1;
                for (referenceEntry = referenceEntry2 = atomicReferenceArray.get((int)n4); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                    MapMaker.RemovalCause removalCause;
                    ReferenceEntry<K, V> referenceEntry2;
                    K k2 = referenceEntry.getKey();
                    if (referenceEntry.getHash() != n2 || k2 == null || !this.map.keyEquivalence.equivalent(object, k2)) continue;
                    ValueReference<K, V> valueReference = referenceEntry.getValueReference();
                    V v2 = valueReference.get();
                    if (v2 != null) {
                        removalCause = MapMaker.RemovalCause.EXPLICIT;
                    } else if (this.isCollected(valueReference)) {
                        removalCause = MapMaker.RemovalCause.COLLECTED;
                    } else {
                        V v3 = null;
                        return v3;
                    }
                    ++this.modCount;
                    this.enqueueNotification(k2, n2, v2, removalCause);
                    ReferenceEntry<K, V> referenceEntry3 = this.removeFromChain(referenceEntry2, referenceEntry);
                    n3 = this.count - 1;
                    atomicReferenceArray.set(n4, referenceEntry3);
                    this.count = n3;
                    V v4 = v2;
                    return v4;
                }
                referenceEntry = null;
                return (V)referenceEntry;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        boolean remove(Object object, int n2, Object object2) {
            this.lock();
            try {
                this.preWriteCleanup();
                int n3 = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int n4 = n2 & atomicReferenceArray.length() - 1;
                for (ReferenceEntry<K, V> referenceEntry = referenceEntry2 = atomicReferenceArray.get((int)n4); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                    ReferenceEntry<K, V> referenceEntry2;
                    MapMaker.RemovalCause removalCause;
                    K k2 = referenceEntry.getKey();
                    if (referenceEntry.getHash() != n2 || k2 == null || !this.map.keyEquivalence.equivalent(object, k2)) continue;
                    ValueReference<K, V> valueReference = referenceEntry.getValueReference();
                    V v2 = valueReference.get();
                    if (this.map.valueEquivalence.equivalent(object2, v2)) {
                        removalCause = MapMaker.RemovalCause.EXPLICIT;
                    } else if (this.isCollected(valueReference)) {
                        removalCause = MapMaker.RemovalCause.COLLECTED;
                    } else {
                        boolean bl = false;
                        return bl;
                    }
                    ++this.modCount;
                    this.enqueueNotification(k2, n2, v2, removalCause);
                    ReferenceEntry<K, V> referenceEntry3 = this.removeFromChain(referenceEntry2, referenceEntry);
                    n3 = this.count - 1;
                    atomicReferenceArray.set(n4, referenceEntry3);
                    this.count = n3;
                    boolean bl = removalCause == MapMaker.RemovalCause.EXPLICIT;
                    return bl;
                }
                boolean bl = false;
                return bl;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        void clear() {
            if (this.count != 0) {
                this.lock();
                try {
                    int n2;
                    AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                    if (this.map.removalNotificationQueue != MapMakerInternalMap.DISCARDING_QUEUE) {
                        for (n2 = 0; n2 < atomicReferenceArray.length(); ++n2) {
                            for (ReferenceEntry<K, V> referenceEntry = atomicReferenceArray.get((int)n2); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                                if (referenceEntry.getValueReference().isComputingReference()) continue;
                                this.enqueueNotification(referenceEntry, MapMaker.RemovalCause.EXPLICIT);
                            }
                        }
                    }
                    for (n2 = 0; n2 < atomicReferenceArray.length(); ++n2) {
                        atomicReferenceArray.set(n2, null);
                    }
                    this.clearReferenceQueues();
                    this.evictionQueue.clear();
                    this.expirationQueue.clear();
                    this.readCount.set(0);
                    ++this.modCount;
                    this.count = 0;
                }
                finally {
                    this.unlock();
                    this.postWriteCleanup();
                }
            }
        }

        ReferenceEntry<K, V> removeFromChain(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            this.evictionQueue.remove(referenceEntry2);
            this.expirationQueue.remove(referenceEntry2);
            int n2 = this.count;
            ReferenceEntry<K, V> referenceEntry3 = referenceEntry2.getNext();
            for (ReferenceEntry<K, V> referenceEntry4 = referenceEntry; referenceEntry4 != referenceEntry2; referenceEntry4 = referenceEntry4.getNext()) {
                ReferenceEntry<K, V> referenceEntry5 = this.copyEntry(referenceEntry4, referenceEntry3);
                if (referenceEntry5 != null) {
                    referenceEntry3 = referenceEntry5;
                    continue;
                }
                this.removeCollectedEntry(referenceEntry4);
                --n2;
            }
            this.count = n2;
            return referenceEntry3;
        }

        void removeCollectedEntry(ReferenceEntry<K, V> referenceEntry) {
            this.enqueueNotification(referenceEntry, MapMaker.RemovalCause.COLLECTED);
            this.evictionQueue.remove(referenceEntry);
            this.expirationQueue.remove(referenceEntry);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        boolean reclaimKey(ReferenceEntry<K, V> referenceEntry, int n2) {
            this.lock();
            try {
                int n3 = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int n4 = n2 & atomicReferenceArray.length() - 1;
                for (ReferenceEntry<K, V> referenceEntry2 = referenceEntry3 = atomicReferenceArray.get((int)n4); referenceEntry2 != null; referenceEntry2 = referenceEntry2.getNext()) {
                    ReferenceEntry<K, V> referenceEntry3;
                    if (referenceEntry2 != referenceEntry) continue;
                    ++this.modCount;
                    this.enqueueNotification(referenceEntry2.getKey(), n2, referenceEntry2.getValueReference().get(), MapMaker.RemovalCause.COLLECTED);
                    ReferenceEntry<K, V> referenceEntry4 = this.removeFromChain(referenceEntry3, referenceEntry2);
                    n3 = this.count - 1;
                    atomicReferenceArray.set(n4, referenceEntry4);
                    this.count = n3;
                    boolean bl = true;
                    return bl;
                }
                boolean bl = false;
                return bl;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        boolean reclaimValue(K k2, int n2, ValueReference<K, V> valueReference) {
            this.lock();
            try {
                int n3 = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int n4 = n2 & atomicReferenceArray.length() - 1;
                for (ReferenceEntry<K, V> referenceEntry = referenceEntry2 = atomicReferenceArray.get((int)n4); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                    K k3 = referenceEntry.getKey();
                    if (referenceEntry.getHash() != n2 || k3 == null || !this.map.keyEquivalence.equivalent(k2, k3)) continue;
                    ValueReference<K, V> valueReference2 = referenceEntry.getValueReference();
                    if (valueReference2 == valueReference) {
                        ReferenceEntry<K, V> referenceEntry2;
                        ++this.modCount;
                        this.enqueueNotification(k2, n2, valueReference.get(), MapMaker.RemovalCause.COLLECTED);
                        ReferenceEntry<K, V> referenceEntry3 = this.removeFromChain(referenceEntry2, referenceEntry);
                        n3 = this.count - 1;
                        atomicReferenceArray.set(n4, referenceEntry3);
                        this.count = n3;
                        boolean bl = true;
                        return bl;
                    }
                    boolean bl = false;
                    return bl;
                }
                boolean bl = false;
                return bl;
            }
            finally {
                this.unlock();
                if (!this.isHeldByCurrentThread()) {
                    this.postWriteCleanup();
                }
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        boolean clearValue(K k2, int n2, ValueReference<K, V> valueReference) {
            this.lock();
            try {
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int n3 = n2 & atomicReferenceArray.length() - 1;
                for (ReferenceEntry<K, V> referenceEntry = referenceEntry2 = atomicReferenceArray.get((int)n3); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                    K k3 = referenceEntry.getKey();
                    if (referenceEntry.getHash() != n2 || k3 == null || !this.map.keyEquivalence.equivalent(k2, k3)) continue;
                    ValueReference<K, V> valueReference2 = referenceEntry.getValueReference();
                    if (valueReference2 == valueReference) {
                        ReferenceEntry<K, V> referenceEntry2;
                        ReferenceEntry<K, V> referenceEntry3 = this.removeFromChain(referenceEntry2, referenceEntry);
                        atomicReferenceArray.set(n3, referenceEntry3);
                        boolean bl = true;
                        return bl;
                    }
                    boolean bl = false;
                    return bl;
                }
                boolean bl = false;
                return bl;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }

        boolean removeEntry(ReferenceEntry<K, V> referenceEntry, int n2, MapMaker.RemovalCause removalCause) {
            int n3 = this.count - 1;
            AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
            int n4 = n2 & atomicReferenceArray.length() - 1;
            for (ReferenceEntry<K, V> referenceEntry2 = referenceEntry3 = atomicReferenceArray.get((int)n4); referenceEntry2 != null; referenceEntry2 = referenceEntry2.getNext()) {
                ReferenceEntry<K, V> referenceEntry3;
                if (referenceEntry2 != referenceEntry) continue;
                ++this.modCount;
                this.enqueueNotification(referenceEntry2.getKey(), n2, referenceEntry2.getValueReference().get(), removalCause);
                ReferenceEntry<K, V> referenceEntry4 = this.removeFromChain(referenceEntry3, referenceEntry2);
                n3 = this.count - 1;
                atomicReferenceArray.set(n4, referenceEntry4);
                this.count = n3;
                return true;
            }
            return false;
        }

        boolean isCollected(ValueReference<K, V> valueReference) {
            if (valueReference.isComputingReference()) {
                return false;
            }
            return valueReference.get() == null;
        }

        V getLiveValue(ReferenceEntry<K, V> referenceEntry) {
            if (referenceEntry.getKey() == null) {
                this.tryDrainReferenceQueues();
                return null;
            }
            V v2 = referenceEntry.getValueReference().get();
            if (v2 == null) {
                this.tryDrainReferenceQueues();
                return null;
            }
            if (this.map.expires() && this.map.isExpired(referenceEntry)) {
                this.tryExpireEntries();
                return null;
            }
            return v2;
        }

        void postReadCleanup() {
            if ((this.readCount.incrementAndGet() & 63) == 0) {
                this.runCleanup();
            }
        }

        void preWriteCleanup() {
            this.runLockedCleanup();
        }

        void postWriteCleanup() {
            this.runUnlockedCleanup();
        }

        void runCleanup() {
            this.runLockedCleanup();
            this.runUnlockedCleanup();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        void runLockedCleanup() {
            if (this.tryLock()) {
                try {
                    this.drainReferenceQueues();
                    this.expireEntries();
                    this.readCount.set(0);
                }
                finally {
                    this.unlock();
                }
            }
        }

        void runUnlockedCleanup() {
            if (!this.isHeldByCurrentThread()) {
                this.map.processPendingNotifications();
            }
        }
    }

    static final class StrongValueReference<K, V>
    implements ValueReference<K, V> {
        final V referent;

        StrongValueReference(V v2) {
            this.referent = v2;
        }

        @Override
        public V get() {
            return this.referent;
        }

        @Override
        public ReferenceEntry<K, V> getEntry() {
            return null;
        }

        @Override
        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v2, ReferenceEntry<K, V> referenceEntry) {
            return this;
        }

        @Override
        public boolean isComputingReference() {
            return false;
        }

        @Override
        public V waitForValue() {
            return this.get();
        }

        @Override
        public void clear(ValueReference<K, V> valueReference) {
        }
    }

    static final class SoftValueReference<K, V>
    extends SoftReference<V>
    implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        SoftValueReference(ReferenceQueue<V> referenceQueue, V v2, ReferenceEntry<K, V> referenceEntry) {
            super(v2, referenceQueue);
            this.entry = referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }

        @Override
        public void clear(ValueReference<K, V> valueReference) {
            this.clear();
        }

        @Override
        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v2, ReferenceEntry<K, V> referenceEntry) {
            return new SoftValueReference<K, V>(referenceQueue, v2, referenceEntry);
        }

        @Override
        public boolean isComputingReference() {
            return false;
        }

        @Override
        public V waitForValue() {
            return (V)this.get();
        }
    }

    static final class WeakValueReference<K, V>
    extends WeakReference<V>
    implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        WeakValueReference(ReferenceQueue<V> referenceQueue, V v2, ReferenceEntry<K, V> referenceEntry) {
            super(v2, referenceQueue);
            this.entry = referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }

        @Override
        public void clear(ValueReference<K, V> valueReference) {
            this.clear();
        }

        @Override
        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v2, ReferenceEntry<K, V> referenceEntry) {
            return new WeakValueReference<K, V>(referenceQueue, v2, referenceEntry);
        }

        @Override
        public boolean isComputingReference() {
            return false;
        }

        @Override
        public V waitForValue() {
            return (V)this.get();
        }
    }

    static final class WeakExpirableEvictableEntry<K, V>
    extends WeakEntry<K, V>
    implements ReferenceEntry<K, V> {
        volatile long time = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();

        WeakExpirableEvictableEntry(ReferenceQueue<K> referenceQueue, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k2, n2, referenceEntry);
        }

        @Override
        public long getExpirationTime() {
            return this.time;
        }

        @Override
        public void setExpirationTime(long l2) {
            this.time = l2;
        }

        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }

        @Override
        public void setNextExpirable(ReferenceEntry<K, V> referenceEntry) {
            this.nextExpirable = referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }

        @Override
        public void setPreviousExpirable(ReferenceEntry<K, V> referenceEntry) {
            this.previousExpirable = referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }

        @Override
        public void setNextEvictable(ReferenceEntry<K, V> referenceEntry) {
            this.nextEvictable = referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }

        @Override
        public void setPreviousEvictable(ReferenceEntry<K, V> referenceEntry) {
            this.previousEvictable = referenceEntry;
        }
    }

    static final class WeakEvictableEntry<K, V>
    extends WeakEntry<K, V>
    implements ReferenceEntry<K, V> {
        ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();

        WeakEvictableEntry(ReferenceQueue<K> referenceQueue, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k2, n2, referenceEntry);
        }

        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }

        @Override
        public void setNextEvictable(ReferenceEntry<K, V> referenceEntry) {
            this.nextEvictable = referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }

        @Override
        public void setPreviousEvictable(ReferenceEntry<K, V> referenceEntry) {
            this.previousEvictable = referenceEntry;
        }
    }

    static final class WeakExpirableEntry<K, V>
    extends WeakEntry<K, V>
    implements ReferenceEntry<K, V> {
        volatile long time = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();

        WeakExpirableEntry(ReferenceQueue<K> referenceQueue, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k2, n2, referenceEntry);
        }

        @Override
        public long getExpirationTime() {
            return this.time;
        }

        @Override
        public void setExpirationTime(long l2) {
            this.time = l2;
        }

        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }

        @Override
        public void setNextExpirable(ReferenceEntry<K, V> referenceEntry) {
            this.nextExpirable = referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }

        @Override
        public void setPreviousExpirable(ReferenceEntry<K, V> referenceEntry) {
            this.previousExpirable = referenceEntry;
        }
    }

    static class WeakEntry<K, V>
    extends WeakReference<K>
    implements ReferenceEntry<K, V> {
        final int hash;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference = MapMakerInternalMap.unset();

        WeakEntry(ReferenceQueue<K> referenceQueue, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
            super(k2, referenceQueue);
            this.hash = n2;
            this.next = referenceEntry;
        }

        @Override
        public K getKey() {
            return (K)this.get();
        }

        @Override
        public long getExpirationTime() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setExpirationTime(long l2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setNextExpirable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPreviousExpirable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setNextEvictable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPreviousEvictable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        @Override
        public void setValueReference(ValueReference<K, V> valueReference) {
            ValueReference<K, V> valueReference2 = this.valueReference;
            this.valueReference = valueReference;
            valueReference2.clear(valueReference);
        }

        @Override
        public int getHash() {
            return this.hash;
        }

        @Override
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }

    static final class StrongExpirableEvictableEntry<K, V>
    extends StrongEntry<K, V>
    implements ReferenceEntry<K, V> {
        volatile long time = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();

        StrongExpirableEvictableEntry(K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
            super(k2, n2, referenceEntry);
        }

        @Override
        public long getExpirationTime() {
            return this.time;
        }

        @Override
        public void setExpirationTime(long l2) {
            this.time = l2;
        }

        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }

        @Override
        public void setNextExpirable(ReferenceEntry<K, V> referenceEntry) {
            this.nextExpirable = referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }

        @Override
        public void setPreviousExpirable(ReferenceEntry<K, V> referenceEntry) {
            this.previousExpirable = referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }

        @Override
        public void setNextEvictable(ReferenceEntry<K, V> referenceEntry) {
            this.nextEvictable = referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }

        @Override
        public void setPreviousEvictable(ReferenceEntry<K, V> referenceEntry) {
            this.previousEvictable = referenceEntry;
        }
    }

    static final class StrongEvictableEntry<K, V>
    extends StrongEntry<K, V>
    implements ReferenceEntry<K, V> {
        ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();

        StrongEvictableEntry(K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
            super(k2, n2, referenceEntry);
        }

        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }

        @Override
        public void setNextEvictable(ReferenceEntry<K, V> referenceEntry) {
            this.nextEvictable = referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }

        @Override
        public void setPreviousEvictable(ReferenceEntry<K, V> referenceEntry) {
            this.previousEvictable = referenceEntry;
        }
    }

    static final class StrongExpirableEntry<K, V>
    extends StrongEntry<K, V>
    implements ReferenceEntry<K, V> {
        volatile long time = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();

        StrongExpirableEntry(K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
            super(k2, n2, referenceEntry);
        }

        @Override
        public long getExpirationTime() {
            return this.time;
        }

        @Override
        public void setExpirationTime(long l2) {
            this.time = l2;
        }

        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }

        @Override
        public void setNextExpirable(ReferenceEntry<K, V> referenceEntry) {
            this.nextExpirable = referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }

        @Override
        public void setPreviousExpirable(ReferenceEntry<K, V> referenceEntry) {
            this.previousExpirable = referenceEntry;
        }
    }

    static class StrongEntry<K, V>
    implements ReferenceEntry<K, V> {
        final K key;
        final int hash;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference = MapMakerInternalMap.unset();

        StrongEntry(K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
            this.key = k2;
            this.hash = n2;
            this.next = referenceEntry;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public long getExpirationTime() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setExpirationTime(long l2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setNextExpirable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPreviousExpirable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setNextEvictable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPreviousEvictable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        @Override
        public void setValueReference(ValueReference<K, V> valueReference) {
            ValueReference<K, V> valueReference2 = this.valueReference;
            this.valueReference = valueReference;
            valueReference2.clear(valueReference);
        }

        @Override
        public int getHash() {
            return this.hash;
        }

        @Override
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }

    static abstract class AbstractReferenceEntry<K, V>
    implements ReferenceEntry<K, V> {
        AbstractReferenceEntry() {
        }

        @Override
        public ValueReference<K, V> getValueReference() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setValueReference(ValueReference<K, V> valueReference) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getNext() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getHash() {
            throw new UnsupportedOperationException();
        }

        @Override
        public K getKey() {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getExpirationTime() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setExpirationTime(long l2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setNextExpirable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPreviousExpirable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setNextEvictable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPreviousEvictable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
    }

    private static final class NullEntry
    extends Enum<NullEntry>
    implements ReferenceEntry<Object, Object> {
        public static final /* enum */ NullEntry INSTANCE = new NullEntry();
        private static final /* synthetic */ NullEntry[] $VALUES;

        public static NullEntry[] values() {
            return (NullEntry[])$VALUES.clone();
        }

        private NullEntry() {
            super(string, n2);
        }

        @Override
        public ValueReference<Object, Object> getValueReference() {
            return null;
        }

        @Override
        public void setValueReference(ValueReference<Object, Object> valueReference) {
        }

        @Override
        public ReferenceEntry<Object, Object> getNext() {
            return null;
        }

        @Override
        public int getHash() {
            return 0;
        }

        @Override
        public Object getKey() {
            return null;
        }

        @Override
        public long getExpirationTime() {
            return 0;
        }

        @Override
        public void setExpirationTime(long l2) {
        }

        @Override
        public ReferenceEntry<Object, Object> getNextExpirable() {
            return this;
        }

        @Override
        public void setNextExpirable(ReferenceEntry<Object, Object> referenceEntry) {
        }

        @Override
        public ReferenceEntry<Object, Object> getPreviousExpirable() {
            return this;
        }

        @Override
        public void setPreviousExpirable(ReferenceEntry<Object, Object> referenceEntry) {
        }

        @Override
        public ReferenceEntry<Object, Object> getNextEvictable() {
            return this;
        }

        @Override
        public void setNextEvictable(ReferenceEntry<Object, Object> referenceEntry) {
        }

        @Override
        public ReferenceEntry<Object, Object> getPreviousEvictable() {
            return this;
        }

        @Override
        public void setPreviousEvictable(ReferenceEntry<Object, Object> referenceEntry) {
        }

        static {
            $VALUES = new NullEntry[]{INSTANCE};
        }
    }

    static interface ReferenceEntry<K, V> {
        public ValueReference<K, V> getValueReference();

        public void setValueReference(ValueReference<K, V> var1);

        public ReferenceEntry<K, V> getNext();

        public int getHash();

        public K getKey();

        public long getExpirationTime();

        public void setExpirationTime(long var1);

        public ReferenceEntry<K, V> getNextExpirable();

        public void setNextExpirable(ReferenceEntry<K, V> var1);

        public ReferenceEntry<K, V> getPreviousExpirable();

        public void setPreviousExpirable(ReferenceEntry<K, V> var1);

        public ReferenceEntry<K, V> getNextEvictable();

        public void setNextEvictable(ReferenceEntry<K, V> var1);

        public ReferenceEntry<K, V> getPreviousEvictable();

        public void setPreviousEvictable(ReferenceEntry<K, V> var1);
    }

    static interface ValueReference<K, V> {
        public V get();

        public V waitForValue() throws ExecutionException;

        public ReferenceEntry<K, V> getEntry();

        public ValueReference<K, V> copyFor(ReferenceQueue<V> var1, V var2, ReferenceEntry<K, V> var3);

        public void clear(ValueReference<K, V> var1);

        public boolean isComputingReference();
    }

    static abstract class EntryFactory
    extends Enum<EntryFactory> {
        public static final /* enum */ EntryFactory STRONG = new EntryFactory("STRONG", 0){

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
                return new StrongEntry<K, V>(k2, n2, referenceEntry);
            }
        };
        public static final /* enum */ EntryFactory STRONG_EXPIRABLE = new EntryFactory("STRONG_EXPIRABLE", 1){

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
                return new StrongExpirableEntry<K, V>(k2, n2, referenceEntry);
            }

            @Override
            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                ReferenceEntry<K, V> referenceEntry3 = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyExpirableEntry(referenceEntry, referenceEntry3);
                return referenceEntry3;
            }
        };
        public static final /* enum */ EntryFactory STRONG_EVICTABLE = new EntryFactory("STRONG_EVICTABLE", 2){

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
                return new StrongEvictableEntry<K, V>(k2, n2, referenceEntry);
            }

            @Override
            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                ReferenceEntry<K, V> referenceEntry3 = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyEvictableEntry(referenceEntry, referenceEntry3);
                return referenceEntry3;
            }
        };
        public static final /* enum */ EntryFactory STRONG_EXPIRABLE_EVICTABLE = new EntryFactory("STRONG_EXPIRABLE_EVICTABLE", 3){

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
                return new StrongExpirableEvictableEntry<K, V>(k2, n2, referenceEntry);
            }

            @Override
            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                ReferenceEntry<K, V> referenceEntry3 = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyExpirableEntry(referenceEntry, referenceEntry3);
                this.copyEvictableEntry(referenceEntry, referenceEntry3);
                return referenceEntry3;
            }
        };
        public static final /* enum */ EntryFactory WEAK = new EntryFactory("WEAK", 4){

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
                return new WeakEntry(segment.keyReferenceQueue, k2, n2, referenceEntry);
            }
        };
        public static final /* enum */ EntryFactory WEAK_EXPIRABLE = new EntryFactory("WEAK_EXPIRABLE", 5){

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
                return new WeakExpirableEntry(segment.keyReferenceQueue, k2, n2, referenceEntry);
            }

            @Override
            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                ReferenceEntry<K, V> referenceEntry3 = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyExpirableEntry(referenceEntry, referenceEntry3);
                return referenceEntry3;
            }
        };
        public static final /* enum */ EntryFactory WEAK_EVICTABLE = new EntryFactory("WEAK_EVICTABLE", 6){

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
                return new WeakEvictableEntry(segment.keyReferenceQueue, k2, n2, referenceEntry);
            }

            @Override
            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                ReferenceEntry<K, V> referenceEntry3 = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyEvictableEntry(referenceEntry, referenceEntry3);
                return referenceEntry3;
            }
        };
        public static final /* enum */ EntryFactory WEAK_EXPIRABLE_EVICTABLE = new EntryFactory("WEAK_EXPIRABLE_EVICTABLE", 7){

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
                return new WeakExpirableEvictableEntry(segment.keyReferenceQueue, k2, n2, referenceEntry);
            }

            @Override
            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                ReferenceEntry<K, V> referenceEntry3 = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyExpirableEntry(referenceEntry, referenceEntry3);
                this.copyEvictableEntry(referenceEntry, referenceEntry3);
                return referenceEntry3;
            }
        };
        static final EntryFactory[][] factories;
        private static final /* synthetic */ EntryFactory[] $VALUES;

        public static EntryFactory[] values() {
            return (EntryFactory[])$VALUES.clone();
        }

        private EntryFactory() {
            super(string, n2);
        }

        static EntryFactory getFactory(Strength strength, boolean bl, boolean bl2) {
            int n2 = (bl ? 1 : 0) | (bl2 ? 2 : 0);
            return factories[strength.ordinal()][n2];
        }

        abstract <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> var1, K var2, int var3, ReferenceEntry<K, V> var4);

        <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            return this.newEntry(segment, referenceEntry.getKey(), referenceEntry.getHash(), referenceEntry2);
        }

        <K, V> void copyExpirableEntry(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            referenceEntry2.setExpirationTime(referenceEntry.getExpirationTime());
            MapMakerInternalMap.connectExpirables(referenceEntry.getPreviousExpirable(), referenceEntry2);
            MapMakerInternalMap.connectExpirables(referenceEntry2, referenceEntry.getNextExpirable());
            MapMakerInternalMap.nullifyExpirable(referenceEntry);
        }

        <K, V> void copyEvictableEntry(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            MapMakerInternalMap.connectEvictables(referenceEntry.getPreviousEvictable(), referenceEntry2);
            MapMakerInternalMap.connectEvictables(referenceEntry2, referenceEntry.getNextEvictable());
            MapMakerInternalMap.nullifyEvictable(referenceEntry);
        }

        static {
            $VALUES = new EntryFactory[]{STRONG, STRONG_EXPIRABLE, STRONG_EVICTABLE, STRONG_EXPIRABLE_EVICTABLE, WEAK, WEAK_EXPIRABLE, WEAK_EVICTABLE, WEAK_EXPIRABLE_EVICTABLE};
            factories = new EntryFactory[][]{{STRONG, STRONG_EXPIRABLE, STRONG_EVICTABLE, STRONG_EXPIRABLE_EVICTABLE}, new EntryFactory[0], {WEAK, WEAK_EXPIRABLE, WEAK_EVICTABLE, WEAK_EXPIRABLE_EVICTABLE}};
        }

    }

    static abstract class Strength
    extends Enum<Strength> {
        public static final /* enum */ Strength STRONG = new Strength("STRONG", 0){

            @Override
            <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V v2) {
                return new StrongValueReference(v2);
            }

            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.equals();
            }
        };
        public static final /* enum */ Strength SOFT = new Strength("SOFT", 1){

            @Override
            <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V v2) {
                return new SoftValueReference(segment.valueReferenceQueue, v2, referenceEntry);
            }

            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
        };
        public static final /* enum */ Strength WEAK = new Strength("WEAK", 2){

            @Override
            <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V v2) {
                return new WeakValueReference(segment.valueReferenceQueue, v2, referenceEntry);
            }

            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
        };
        private static final /* synthetic */ Strength[] $VALUES;

        public static Strength[] values() {
            return (Strength[])$VALUES.clone();
        }

        private Strength() {
            super(string, n2);
        }

        abstract <K, V> ValueReference<K, V> referenceValue(Segment<K, V> var1, ReferenceEntry<K, V> var2, V var3);

        abstract Equivalence<Object> defaultEquivalence();

        static {
            $VALUES = new Strength[]{STRONG, SOFT, WEAK};
        }

    }

}

