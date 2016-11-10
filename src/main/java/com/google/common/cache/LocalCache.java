/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  com.google.common.cache.LocalCache.AbstractCacheSet
 *  com.google.common.cache.LocalCache.HashIterator
 *  com.google.j2objc.annotations.Weak
 */
package com.google.common.cache;

import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.base.Supplier;
import com.google.common.base.Ticker;
import com.google.common.cache.AbstractCache;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.cache.Weigher;
import com.google.common.collect.AbstractSequentialIterator;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.primitives.Ints;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.common.util.concurrent.Uninterruptibles;
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
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

class LocalCache<K, V>
extends AbstractMap<K, V>
implements ConcurrentMap<K, V> {
    static final Logger logger = Logger.getLogger(LocalCache.class.getName());
    final int segmentMask;
    final int segmentShift;
    final Segment<K, V>[] segments;
    final int concurrencyLevel;
    final Equivalence<Object> keyEquivalence;
    final Equivalence<Object> valueEquivalence;
    final Strength keyStrength;
    final Strength valueStrength;
    final long maxWeight;
    final Weigher<K, V> weigher;
    final long expireAfterAccessNanos;
    final long expireAfterWriteNanos;
    final long refreshNanos;
    final Queue<RemovalNotification<K, V>> removalNotificationQueue;
    final RemovalListener<K, V> removalListener;
    final Ticker ticker;
    final EntryFactory entryFactory;
    final AbstractCache.StatsCounter globalStatsCounter;
    final CacheLoader<? super K, V> defaultLoader;
    static final ValueReference<Object, Object> UNSET = new ValueReference<Object, Object>(){

        @Override
        public Object get() {
            return null;
        }

        @Override
        public int getWeight() {
            return 0;
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
        public boolean isLoading() {
            return false;
        }

        @Override
        public boolean isActive() {
            return false;
        }

        @Override
        public Object waitForValue() {
            return null;
        }

        @Override
        public void notifyNewValue(Object object) {
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
            return ImmutableSet.of().iterator();
        }
    };
    Set<K> keySet;
    Collection<V> values;
    Set<Map.Entry<K, V>> entrySet;

    LocalCache(CacheBuilder<? super K, ? super V> cacheBuilder, CacheLoader<? super K, V> cacheLoader) {
        int n2;
        int n3;
        this.concurrencyLevel = Math.min(cacheBuilder.getConcurrencyLevel(), 65536);
        this.keyStrength = cacheBuilder.getKeyStrength();
        this.valueStrength = cacheBuilder.getValueStrength();
        this.keyEquivalence = cacheBuilder.getKeyEquivalence();
        this.valueEquivalence = cacheBuilder.getValueEquivalence();
        this.maxWeight = cacheBuilder.getMaximumWeight();
        this.weigher = cacheBuilder.getWeigher();
        this.expireAfterAccessNanos = cacheBuilder.getExpireAfterAccessNanos();
        this.expireAfterWriteNanos = cacheBuilder.getExpireAfterWriteNanos();
        this.refreshNanos = cacheBuilder.getRefreshNanos();
        this.removalListener = cacheBuilder.getRemovalListener();
        this.removalNotificationQueue = this.removalListener == CacheBuilder.NullListener.INSTANCE ? LocalCache.discardingQueue() : new ConcurrentLinkedQueue();
        this.ticker = cacheBuilder.getTicker(this.recordsTime());
        this.entryFactory = EntryFactory.getFactory(this.keyStrength, this.usesAccessEntries(), this.usesWriteEntries());
        this.globalStatsCounter = cacheBuilder.getStatsCounterSupplier().get();
        this.defaultLoader = cacheLoader;
        int n4 = Math.min(cacheBuilder.getInitialCapacity(), 1073741824);
        if (this.evictsBySize() && !this.customWeigher()) {
            n4 = Math.min(n4, (int)this.maxWeight);
        }
        int n5 = 0;
        for (n3 = 1; !(n3 >= this.concurrencyLevel || this.evictsBySize() && (long)(n3 * 20) > this.maxWeight); n3 <<= 1) {
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
            long l2 = this.maxWeight / (long)n3 + 1;
            long l3 = this.maxWeight % (long)n3;
            for (int i2 = 0; i2 < this.segments.length; ++i2) {
                if ((long)i2 == l3) {
                    --l2;
                }
                this.segments[i2] = this.createSegment(n2, l2, cacheBuilder.getStatsCounterSupplier().get());
            }
        } else {
            for (int i3 = 0; i3 < this.segments.length; ++i3) {
                this.segments[i3] = this.createSegment(n2, -1, cacheBuilder.getStatsCounterSupplier().get());
            }
        }
    }

    boolean evictsBySize() {
        return this.maxWeight >= 0;
    }

    boolean customWeigher() {
        return this.weigher != CacheBuilder.OneWeigher.INSTANCE;
    }

    boolean expiresAfterWrite() {
        return this.expireAfterWriteNanos > 0;
    }

    boolean expiresAfterAccess() {
        return this.expireAfterAccessNanos > 0;
    }

    boolean refreshes() {
        return this.refreshNanos > 0;
    }

    boolean usesAccessQueue() {
        return this.expiresAfterAccess() || this.evictsBySize();
    }

    boolean usesWriteQueue() {
        return this.expiresAfterWrite();
    }

    boolean recordsWrite() {
        return this.expiresAfterWrite() || this.refreshes();
    }

    boolean recordsAccess() {
        return this.expiresAfterAccess();
    }

    boolean recordsTime() {
        return this.recordsWrite() || this.recordsAccess();
    }

    boolean usesWriteEntries() {
        return this.usesWriteQueue() || this.recordsWrite();
    }

    boolean usesAccessEntries() {
        return this.usesAccessQueue() || this.recordsAccess();
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
        return LocalCache.rehash(n2);
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

    Segment<K, V> createSegment(int n2, long l2, AbstractCache.StatsCounter statsCounter) {
        return new Segment(this, n2, l2, statsCounter);
    }

    V getLiveValue(ReferenceEntry<K, V> referenceEntry, long l2) {
        if (referenceEntry.getKey() == null) {
            return null;
        }
        V v2 = referenceEntry.getValueReference().get();
        if (v2 == null) {
            return null;
        }
        if (this.isExpired(referenceEntry, l2)) {
            return null;
        }
        return v2;
    }

    boolean isExpired(ReferenceEntry<K, V> referenceEntry, long l2) {
        Preconditions.checkNotNull(referenceEntry);
        if (this.expiresAfterAccess() && l2 - referenceEntry.getAccessTime() >= this.expireAfterAccessNanos) {
            return true;
        }
        if (this.expiresAfterWrite() && l2 - referenceEntry.getWriteTime() >= this.expireAfterWriteNanos) {
            return true;
        }
        return false;
    }

    static <K, V> void connectAccessOrder(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
        referenceEntry.setNextInAccessQueue(referenceEntry2);
        referenceEntry2.setPreviousInAccessQueue(referenceEntry);
    }

    static <K, V> void nullifyAccessOrder(ReferenceEntry<K, V> referenceEntry) {
        ReferenceEntry<K, V> referenceEntry2 = LocalCache.nullEntry();
        referenceEntry.setNextInAccessQueue(referenceEntry2);
        referenceEntry.setPreviousInAccessQueue(referenceEntry2);
    }

    static <K, V> void connectWriteOrder(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
        referenceEntry.setNextInWriteQueue(referenceEntry2);
        referenceEntry2.setPreviousInWriteQueue(referenceEntry);
    }

    static <K, V> void nullifyWriteOrder(ReferenceEntry<K, V> referenceEntry) {
        ReferenceEntry<K, V> referenceEntry2 = LocalCache.nullEntry();
        referenceEntry.setNextInWriteQueue(referenceEntry2);
        referenceEntry.setPreviousInWriteQueue(referenceEntry2);
    }

    void processPendingNotifications() {
        RemovalNotification<K, V> removalNotification;
        while ((removalNotification = this.removalNotificationQueue.poll()) != null) {
            try {
                this.removalListener.onRemoval(removalNotification);
            }
            catch (Throwable throwable) {
                logger.log(Level.WARNING, "Exception thrown by removal listener", throwable);
            }
        }
    }

    final Segment<K, V>[] newSegmentArray(int n2) {
        return new Segment[n2];
    }

    public void cleanUp() {
        for (Segment<K, V> segment : this.segments) {
            segment.cleanUp();
        }
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

    long longSize() {
        Segment<K, V>[] arrsegment = this.segments;
        long l2 = 0;
        for (int i2 = 0; i2 < arrsegment.length; ++i2) {
            l2 += (long)Math.max(0, arrsegment[i2].count);
        }
        return l2;
    }

    @Override
    public int size() {
        return Ints.saturatedCast(this.longSize());
    }

    @Override
    public V get(Object object) {
        if (object == null) {
            return null;
        }
        int n2 = this.hash(object);
        return this.segmentFor(n2).get(object, n2);
    }

    public V getIfPresent(Object object) {
        int n2 = this.hash(Preconditions.checkNotNull(object));
        V v2 = this.segmentFor(n2).get(object, n2);
        if (v2 == null) {
            this.globalStatsCounter.recordMisses(1);
        } else {
            this.globalStatsCounter.recordHits(1);
        }
        return v2;
    }

    V get(K k2, CacheLoader<? super K, V> cacheLoader) throws ExecutionException {
        int n2 = this.hash(Preconditions.checkNotNull(k2));
        return this.segmentFor(n2).get((K)k2, n2, cacheLoader);
    }

    V getOrLoad(K k2) throws ExecutionException {
        return this.get(k2, this.defaultLoader);
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
        long l2 = this.ticker.read();
        Segment<K, V>[] arrsegment = this.segments;
        long l3 = -1;
        for (int i2 = 0; i2 < 3; ++i2) {
            long l4 = 0;
            for (Segment segment : arrsegment) {
                int n2 = segment.count;
                AtomicReferenceArray atomicReferenceArray = segment.table;
                for (int i3 = 0; i3 < atomicReferenceArray.length(); ++i3) {
                    for (ReferenceEntry referenceEntry = atomicReferenceArray.get((int)i3); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                        V v2 = segment.getLiveValue(referenceEntry, l2);
                        if (v2 == null || !this.valueEquivalence.equivalent(object, v2)) continue;
                        return true;
                    }
                }
                l4 += (long)segment.modCount;
            }
            if (l4 == l3) break;
            l3 = l4;
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
        Set<K> set = this.keySet;
        Set<K> set2 = set != null ? set : (this.keySet = new KeySet(this));
        return set2;
    }

    @Override
    public Collection<V> values() {
        Values values = this.values;
        Values values2 = values != null ? values : (this.values = new Values(this));
        return values2;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = this.entrySet;
        Set<Map.Entry<K, V>> set2 = set != null ? set : (this.entrySet = new EntrySet(this));
        return set2;
    }

    private static <E> ArrayList<E> toArrayList(Collection<E> collection) {
        ArrayList arrayList = new ArrayList(collection.size());
        Iterators.addAll(arrayList, collection.iterator());
        return arrayList;
    }

    static class LocalLoadingCache<K, V>
    extends LocalManualCache<K, V>
    implements LoadingCache<K, V> {
        LocalLoadingCache(CacheBuilder<? super K, ? super V> cacheBuilder, CacheLoader<? super K, V> cacheLoader) {
            super(new LocalCache<K, V>(cacheBuilder, Preconditions.checkNotNull(cacheLoader)));
        }

        public V get(K k2) throws ExecutionException {
            return this.localCache.getOrLoad(k2);
        }

        @Override
        public V getUnchecked(K k2) {
            try {
                return this.get(k2);
            }
            catch (ExecutionException executionException) {
                throw new UncheckedExecutionException(executionException.getCause());
            }
        }

        @Override
        public final V apply(K k2) {
            return this.getUnchecked(k2);
        }
    }

    static class LocalManualCache<K, V>
    implements Cache<K, V>,
    Serializable {
        final LocalCache<K, V> localCache;

        LocalManualCache(CacheBuilder<? super K, ? super V> cacheBuilder) {
            this(new LocalCache<K, V>(cacheBuilder, null));
        }

        private LocalManualCache(LocalCache<K, V> localCache) {
            this.localCache = localCache;
        }

        @Override
        public V getIfPresent(Object object) {
            return this.localCache.getIfPresent(object);
        }

        @Override
        public void put(K k2, V v2) {
            this.localCache.put(k2, v2);
        }

        public ConcurrentMap<K, V> asMap() {
            return this.localCache;
        }

        @Override
        public void cleanUp() {
            this.localCache.cleanUp();
        }
    }

    final class EntrySet
    extends com.google.common.cache.LocalCache.AbstractCacheSet<Map.Entry<K, V>> {
        EntrySet(ConcurrentMap<?, ?> concurrentMap) {
            super(concurrentMap);
        }

        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        public boolean contains(Object object) {
            if (!(object instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry)object;
            Object k2 = entry.getKey();
            if (k2 == null) {
                return false;
            }
            Object v2 = LocalCache.this.get(k2);
            return v2 != null && LocalCache.this.valueEquivalence.equivalent(entry.getValue(), v2);
        }

        public boolean remove(Object object) {
            if (!(object instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry)object;
            Object k2 = entry.getKey();
            return k2 != null && LocalCache.this.remove(k2, entry.getValue());
        }
    }

    final class Values
    extends AbstractCollection<V> {
        private final ConcurrentMap<?, ?> map;

        Values(ConcurrentMap<?, ?> concurrentMap) {
            this.map = concurrentMap;
        }

        @Override
        public int size() {
            return this.map.size();
        }

        @Override
        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override
        public void clear() {
            this.map.clear();
        }

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public boolean contains(Object object) {
            return this.map.containsValue(object);
        }

        @Override
        public Object[] toArray() {
            return LocalCache.toArrayList(this).toArray();
        }

        @Override
        public <E> E[] toArray(E[] arrE) {
            return LocalCache.toArrayList(this).toArray(arrE);
        }
    }

    final class KeySet
    extends LocalCache<K, V> {
        KeySet(ConcurrentMap<?, ?> concurrentMap) {
            super(concurrentMap);
        }

        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        public boolean contains(Object object) {
            return this.map.containsKey(object);
        }

        @Override
        public boolean remove(Object object) {
            return this.map.remove(object) != null;
        }
    }

    abstract class AbstractCacheSet<T>
    extends AbstractSet<T> {
        @Weak
        final ConcurrentMap<?, ?> map;

        AbstractCacheSet(ConcurrentMap<?, ?> concurrentMap) {
            this.map = concurrentMap;
        }

        @Override
        public int size() {
            return this.map.size();
        }

        @Override
        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override
        public void clear() {
            this.map.clear();
        }

        @Override
        public Object[] toArray() {
            return LocalCache.toArrayList(this).toArray();
        }

        @Override
        public <E> E[] toArray(E[] arrE) {
            return LocalCache.toArrayList(this).toArray(arrE);
        }
    }

    final class EntryIterator
    extends com.google.common.cache.LocalCache.HashIterator<Map.Entry<K, V>> {
        EntryIterator() {
            super();
        }

        public Map.Entry<K, V> next() {
            return this.nextEntry();
        }
    }

    final class WriteThroughEntry
    implements Map.Entry<K, V> {
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
            throw new UnsupportedOperationException();
        }

        public String toString() {
            return this.getKey() + "=" + this.getValue();
        }
    }

    final class ValueIterator
    extends LocalCache<K, V> {
        ValueIterator() {
            super();
        }

        public V next() {
            return this.nextEntry().getValue();
        }
    }

    final class KeyIterator
    extends LocalCache<K, V> {
        KeyIterator() {
            super();
        }

        public K next() {
            return this.nextEntry().getKey();
        }
    }

    abstract class HashIterator<T>
    implements Iterator<T> {
        int nextSegmentIndex;
        int nextTableIndex;
        Segment<K, V> currentSegment;
        AtomicReferenceArray<ReferenceEntry<K, V>> currentTable;
        ReferenceEntry<K, V> nextEntry;
        LocalCache<K, V> nextExternal;
        LocalCache<K, V> lastReturned;

        HashIterator() {
            this.nextSegmentIndex = LocalCache.this.segments.length - 1;
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
                this.currentSegment = LocalCache.this.segments[this.nextSegmentIndex--];
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
                long l2 = LocalCache.this.ticker.read();
                K k2 = referenceEntry.getKey();
                V v2 = LocalCache.this.getLiveValue(referenceEntry, l2);
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

        LocalCache<K, V> nextEntry() {
            if (this.nextExternal == null) {
                throw new NoSuchElementException();
            }
            this.lastReturned = this.nextExternal;
            this.advance();
            return this.lastReturned;
        }

        @Override
        public void remove() {
            Preconditions.checkState(this.lastReturned != null);
            LocalCache.this.remove(this.lastReturned.getKey());
            this.lastReturned = null;
        }
    }

    static final class AccessQueue<K, V>
    extends AbstractQueue<ReferenceEntry<K, V>> {
        final ReferenceEntry<K, V> head;

        AccessQueue() {
            this.head = new AbstractReferenceEntry<K, V>(){
                ReferenceEntry<K, V> nextAccess;
                ReferenceEntry<K, V> previousAccess;

                @Override
                public long getAccessTime() {
                    return Long.MAX_VALUE;
                }

                @Override
                public void setAccessTime(long l2) {
                }

                @Override
                public ReferenceEntry<K, V> getNextInAccessQueue() {
                    return this.nextAccess;
                }

                @Override
                public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
                    this.nextAccess = referenceEntry;
                }

                @Override
                public ReferenceEntry<K, V> getPreviousInAccessQueue() {
                    return this.previousAccess;
                }

                @Override
                public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
                    this.previousAccess = referenceEntry;
                }
            };
        }

        @Override
        public boolean offer(ReferenceEntry<K, V> referenceEntry) {
            LocalCache.connectAccessOrder(referenceEntry.getPreviousInAccessQueue(), referenceEntry.getNextInAccessQueue());
            LocalCache.connectAccessOrder(this.head.getPreviousInAccessQueue(), referenceEntry);
            LocalCache.connectAccessOrder(referenceEntry, this.head);
            return true;
        }

        @Override
        public ReferenceEntry<K, V> peek() {
            ReferenceEntry<K, V> referenceEntry = this.head.getNextInAccessQueue();
            return referenceEntry == this.head ? null : referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> poll() {
            ReferenceEntry<K, V> referenceEntry = this.head.getNextInAccessQueue();
            if (referenceEntry == this.head) {
                return null;
            }
            this.remove(referenceEntry);
            return referenceEntry;
        }

        @Override
        public boolean remove(Object object) {
            ReferenceEntry referenceEntry = (ReferenceEntry)object;
            ReferenceEntry referenceEntry2 = referenceEntry.getPreviousInAccessQueue();
            ReferenceEntry referenceEntry3 = referenceEntry.getNextInAccessQueue();
            LocalCache.connectAccessOrder(referenceEntry2, referenceEntry3);
            LocalCache.nullifyAccessOrder(referenceEntry);
            return referenceEntry3 != NullEntry.INSTANCE;
        }

        @Override
        public boolean contains(Object object) {
            ReferenceEntry referenceEntry = (ReferenceEntry)object;
            return referenceEntry.getNextInAccessQueue() != NullEntry.INSTANCE;
        }

        @Override
        public boolean isEmpty() {
            return this.head.getNextInAccessQueue() == this.head;
        }

        @Override
        public int size() {
            int n2 = 0;
            for (ReferenceEntry<K, V> referenceEntry = this.head.getNextInAccessQueue(); referenceEntry != this.head; referenceEntry = referenceEntry.getNextInAccessQueue()) {
                ++n2;
            }
            return n2;
        }

        @Override
        public void clear() {
            ReferenceEntry<K, V> referenceEntry = this.head.getNextInAccessQueue();
            while (referenceEntry != this.head) {
                ReferenceEntry<K, V> referenceEntry2 = referenceEntry.getNextInAccessQueue();
                LocalCache.nullifyAccessOrder(referenceEntry);
                referenceEntry = referenceEntry2;
            }
            this.head.setNextInAccessQueue(this.head);
            this.head.setPreviousInAccessQueue(this.head);
        }

        @Override
        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>((ReferenceEntry)this.peek()){

                @Override
                protected ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> referenceEntry) {
                    ReferenceEntry<K, V> referenceEntry2 = referenceEntry.getNextInAccessQueue();
                    return referenceEntry2 == AccessQueue.this.head ? null : referenceEntry2;
                }
            };
        }

    }

    static final class WriteQueue<K, V>
    extends AbstractQueue<ReferenceEntry<K, V>> {
        final ReferenceEntry<K, V> head;

        WriteQueue() {
            this.head = new AbstractReferenceEntry<K, V>(){
                ReferenceEntry<K, V> nextWrite;
                ReferenceEntry<K, V> previousWrite;

                @Override
                public long getWriteTime() {
                    return Long.MAX_VALUE;
                }

                @Override
                public void setWriteTime(long l2) {
                }

                @Override
                public ReferenceEntry<K, V> getNextInWriteQueue() {
                    return this.nextWrite;
                }

                @Override
                public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
                    this.nextWrite = referenceEntry;
                }

                @Override
                public ReferenceEntry<K, V> getPreviousInWriteQueue() {
                    return this.previousWrite;
                }

                @Override
                public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
                    this.previousWrite = referenceEntry;
                }
            };
        }

        @Override
        public boolean offer(ReferenceEntry<K, V> referenceEntry) {
            LocalCache.connectWriteOrder(referenceEntry.getPreviousInWriteQueue(), referenceEntry.getNextInWriteQueue());
            LocalCache.connectWriteOrder(this.head.getPreviousInWriteQueue(), referenceEntry);
            LocalCache.connectWriteOrder(referenceEntry, this.head);
            return true;
        }

        @Override
        public ReferenceEntry<K, V> peek() {
            ReferenceEntry<K, V> referenceEntry = this.head.getNextInWriteQueue();
            return referenceEntry == this.head ? null : referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> poll() {
            ReferenceEntry<K, V> referenceEntry = this.head.getNextInWriteQueue();
            if (referenceEntry == this.head) {
                return null;
            }
            this.remove(referenceEntry);
            return referenceEntry;
        }

        @Override
        public boolean remove(Object object) {
            ReferenceEntry referenceEntry = (ReferenceEntry)object;
            ReferenceEntry referenceEntry2 = referenceEntry.getPreviousInWriteQueue();
            ReferenceEntry referenceEntry3 = referenceEntry.getNextInWriteQueue();
            LocalCache.connectWriteOrder(referenceEntry2, referenceEntry3);
            LocalCache.nullifyWriteOrder(referenceEntry);
            return referenceEntry3 != NullEntry.INSTANCE;
        }

        @Override
        public boolean contains(Object object) {
            ReferenceEntry referenceEntry = (ReferenceEntry)object;
            return referenceEntry.getNextInWriteQueue() != NullEntry.INSTANCE;
        }

        @Override
        public boolean isEmpty() {
            return this.head.getNextInWriteQueue() == this.head;
        }

        @Override
        public int size() {
            int n2 = 0;
            for (ReferenceEntry<K, V> referenceEntry = this.head.getNextInWriteQueue(); referenceEntry != this.head; referenceEntry = referenceEntry.getNextInWriteQueue()) {
                ++n2;
            }
            return n2;
        }

        @Override
        public void clear() {
            ReferenceEntry<K, V> referenceEntry = this.head.getNextInWriteQueue();
            while (referenceEntry != this.head) {
                ReferenceEntry<K, V> referenceEntry2 = referenceEntry.getNextInWriteQueue();
                LocalCache.nullifyWriteOrder(referenceEntry);
                referenceEntry = referenceEntry2;
            }
            this.head.setNextInWriteQueue(this.head);
            this.head.setPreviousInWriteQueue(this.head);
        }

        @Override
        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>((ReferenceEntry)this.peek()){

                @Override
                protected ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> referenceEntry) {
                    ReferenceEntry<K, V> referenceEntry2 = referenceEntry.getNextInWriteQueue();
                    return referenceEntry2 == WriteQueue.this.head ? null : referenceEntry2;
                }
            };
        }

    }

    static class LoadingValueReference<K, V>
    implements ValueReference<K, V> {
        volatile ValueReference<K, V> oldValue;
        final SettableFuture<V> futureValue = SettableFuture.create();
        final Stopwatch stopwatch = Stopwatch.createUnstarted();

        public LoadingValueReference() {
            this(LocalCache.unset());
        }

        public LoadingValueReference(ValueReference<K, V> valueReference) {
            this.oldValue = valueReference;
        }

        @Override
        public boolean isLoading() {
            return true;
        }

        @Override
        public boolean isActive() {
            return this.oldValue.isActive();
        }

        @Override
        public int getWeight() {
            return this.oldValue.getWeight();
        }

        public boolean set(V v2) {
            return this.futureValue.set(v2);
        }

        public boolean setException(Throwable throwable) {
            return this.futureValue.setException(throwable);
        }

        private ListenableFuture<V> fullyFailedFuture(Throwable throwable) {
            return Futures.immediateFailedFuture(throwable);
        }

        @Override
        public void notifyNewValue(V v2) {
            if (v2 != null) {
                this.set(v2);
            } else {
                this.oldValue = LocalCache.unset();
            }
        }

        public ListenableFuture<V> loadFuture(K k2, CacheLoader<? super K, V> cacheLoader) {
            try {
                this.stopwatch.start();
                V v2 = this.oldValue.get();
                if (v2 == null) {
                    V v3 = cacheLoader.load(k2);
                    return this.set(v3) ? this.futureValue : Futures.immediateFuture(v3);
                }
                ListenableFuture<V> listenableFuture = cacheLoader.reload(k2, v2);
                if (listenableFuture == null) {
                    return Futures.immediateFuture(null);
                }
                return Futures.transform(listenableFuture, new Function<V, V>(){

                    @Override
                    public V apply(V v2) {
                        LoadingValueReference.this.set(v2);
                        return v2;
                    }
                });
            }
            catch (Throwable throwable) {
                ListenableFuture listenableFuture;
                ListenableFuture listenableFuture2 = listenableFuture = this.setException(throwable) ? this.futureValue : this.fullyFailedFuture(throwable);
                if (throwable instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                return listenableFuture;
            }
        }

        public long elapsedNanos() {
            return this.stopwatch.elapsed(TimeUnit.NANOSECONDS);
        }

        @Override
        public V waitForValue() throws ExecutionException {
            return Uninterruptibles.getUninterruptibly(this.futureValue);
        }

        @Override
        public V get() {
            return this.oldValue.get();
        }

        public ValueReference<K, V> getOldValue() {
            return this.oldValue;
        }

        @Override
        public ReferenceEntry<K, V> getEntry() {
            return null;
        }

        @Override
        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v2, ReferenceEntry<K, V> referenceEntry) {
            return this;
        }

    }

    static class Segment<K, V>
    extends ReentrantLock {
        @Weak
        final LocalCache<K, V> map;
        volatile int count;
        long totalWeight;
        int modCount;
        int threshold;
        volatile AtomicReferenceArray<ReferenceEntry<K, V>> table;
        final long maxSegmentWeight;
        final ReferenceQueue<K> keyReferenceQueue;
        final ReferenceQueue<V> valueReferenceQueue;
        final Queue<ReferenceEntry<K, V>> recencyQueue;
        final AtomicInteger readCount = new AtomicInteger();
        final Queue<ReferenceEntry<K, V>> writeQueue;
        final Queue<ReferenceEntry<K, V>> accessQueue;
        final AbstractCache.StatsCounter statsCounter;

        Segment(LocalCache<K, V> localCache, int n2, long l2, AbstractCache.StatsCounter statsCounter) {
            this.map = localCache;
            this.maxSegmentWeight = l2;
            this.statsCounter = Preconditions.checkNotNull(statsCounter);
            this.initTable(this.newEntryArray(n2));
            this.keyReferenceQueue = localCache.usesKeyReferences() ? new ReferenceQueue() : null;
            this.valueReferenceQueue = localCache.usesValueReferences() ? new ReferenceQueue() : null;
            this.recencyQueue = localCache.usesAccessQueue() ? new ConcurrentLinkedQueue() : LocalCache.discardingQueue();
            this.writeQueue = localCache.usesWriteQueue() ? new WriteQueue() : LocalCache.discardingQueue();
            this.accessQueue = localCache.usesAccessQueue() ? new AccessQueue() : LocalCache.discardingQueue();
        }

        AtomicReferenceArray<ReferenceEntry<K, V>> newEntryArray(int n2) {
            return new AtomicReferenceArray<ReferenceEntry<K, V>>(n2);
        }

        void initTable(AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray) {
            this.threshold = atomicReferenceArray.length() * 3 / 4;
            if (!this.map.customWeigher() && (long)this.threshold == this.maxSegmentWeight) {
                ++this.threshold;
            }
            this.table = atomicReferenceArray;
        }

        ReferenceEntry<K, V> newEntry(K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
            return this.map.entryFactory.newEntry(this, Preconditions.checkNotNull(k2), n2, referenceEntry);
        }

        ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            if (referenceEntry.getKey() == null) {
                return null;
            }
            ValueReference<K, V> valueReference = referenceEntry.getValueReference();
            V v2 = valueReference.get();
            if (v2 == null && valueReference.isActive()) {
                return null;
            }
            ReferenceEntry<K, V> referenceEntry3 = this.map.entryFactory.copyEntry(this, referenceEntry, referenceEntry2);
            referenceEntry3.setValueReference(valueReference.copyFor(this.valueReferenceQueue, v2, referenceEntry3));
            return referenceEntry3;
        }

        void setValue(ReferenceEntry<K, V> referenceEntry, K k2, V v2, long l2) {
            ValueReference<K, V> valueReference = referenceEntry.getValueReference();
            int n2 = this.map.weigher.weigh(k2, v2);
            Preconditions.checkState(n2 >= 0, "Weights must be non-negative");
            ValueReference<K, V> valueReference2 = this.map.valueStrength.referenceValue(this, referenceEntry, v2, n2);
            referenceEntry.setValueReference(valueReference2);
            this.recordWrite(referenceEntry, n2, l2);
            valueReference.notifyNewValue(v2);
        }

        V get(K k2, int n2, CacheLoader<? super K, V> cacheLoader) throws ExecutionException {
            Preconditions.checkNotNull(k2);
            Preconditions.checkNotNull(cacheLoader);
            try {
                ReferenceEntry referenceEntry;
                if (this.count != 0 && (referenceEntry = this.getEntry(k2, n2)) != null) {
                    long l2 = this.map.ticker.read();
                    V v2 = this.getLiveValue(referenceEntry, l2);
                    if (v2 != null) {
                        this.recordRead(referenceEntry, l2);
                        this.statsCounter.recordHits(1);
                        V v3 = this.scheduleRefresh(referenceEntry, k2, n2, v2, l2, cacheLoader);
                        return v3;
                    }
                    ValueReference valueReference = referenceEntry.getValueReference();
                    if (valueReference.isLoading()) {
                        Object v4 = this.waitForLoadingValue(referenceEntry, k2, valueReference);
                        return v4;
                    }
                }
                referenceEntry = this.lockedGetOrLoad(k2, n2, cacheLoader);
                return (V)referenceEntry;
            }
            catch (ExecutionException executionException) {
                Throwable throwable = executionException.getCause();
                if (throwable instanceof Error) {
                    throw new ExecutionError((Error)throwable);
                }
                if (throwable instanceof RuntimeException) {
                    throw new UncheckedExecutionException(throwable);
                }
                throw executionException;
            }
            finally {
                this.postReadCleanup();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        V lockedGetOrLoad(K k2, int n2, CacheLoader<? super K, V> cacheLoader) throws ExecutionException {
            boolean bl;
            ValueReference<K, V> valueReference;
            ReferenceEntry referenceEntry;
            LoadingValueReference loadingValueReference;
            block19 : {
                valueReference = null;
                loadingValueReference = null;
                bl = true;
                this.lock();
                try {
                    long l2 = this.map.ticker.read();
                    this.preWriteCleanup(l2);
                    int n3 = this.count - 1;
                    AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                    int n4 = n2 & atomicReferenceArray.length() - 1;
                    for (referenceEntry = referenceEntry2 = atomicReferenceArray.get((int)n4); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                        K k3 = referenceEntry.getKey();
                        if (referenceEntry.getHash() != n2 || k3 == null || !this.map.keyEquivalence.equivalent(k2, k3)) continue;
                        valueReference = referenceEntry.getValueReference();
                        if (valueReference.isLoading()) {
                            bl = false;
                            break;
                        }
                        V v2 = valueReference.get();
                        if (v2 == null) {
                            this.enqueueNotification(k3, n2, valueReference, RemovalCause.COLLECTED);
                        } else if (this.map.isExpired(referenceEntry, l2)) {
                            this.enqueueNotification(k3, n2, valueReference, RemovalCause.EXPIRED);
                        } else {
                            this.recordLockedRead(referenceEntry, l2);
                            this.statsCounter.recordHits(1);
                            V v3 = v2;
                            return v3;
                        }
                        this.writeQueue.remove(referenceEntry);
                        this.accessQueue.remove(referenceEntry);
                        this.count = n3;
                        break;
                    }
                    if (!bl) break block19;
                    loadingValueReference = new LoadingValueReference();
                    if (referenceEntry == null) {
                        ReferenceEntry referenceEntry2;
                        referenceEntry = this.newEntry(k2, n2, referenceEntry2);
                        referenceEntry.setValueReference(loadingValueReference);
                        atomicReferenceArray.set(n4, referenceEntry);
                        break block19;
                    }
                    referenceEntry.setValueReference(loadingValueReference);
                }
                finally {
                    this.unlock();
                    this.postWriteCleanup();
                }
            }
            if (bl) {
                try {
                    ReferenceEntry referenceEntry3 = referenceEntry;
                    synchronized (referenceEntry3) {
                        Object v4 = this.loadSync(k2, n2, loadingValueReference, cacheLoader);
                        return v4;
                    }
                }
                finally {
                    this.statsCounter.recordMisses(1);
                }
            }
            return this.waitForLoadingValue(referenceEntry, k2, valueReference);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        V waitForLoadingValue(ReferenceEntry<K, V> referenceEntry, K k2, ValueReference<K, V> valueReference) throws ExecutionException {
            if (!valueReference.isLoading()) {
                throw new AssertionError();
            }
            Preconditions.checkState(!Thread.holdsLock(referenceEntry), "Recursive load of: %s", k2);
            try {
                V v2 = valueReference.waitForValue();
                if (v2 == null) {
                    throw new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + k2 + ".");
                }
                long l2 = this.map.ticker.read();
                this.recordRead(referenceEntry, l2);
                V v3 = v2;
                return v3;
            }
            finally {
                this.statsCounter.recordMisses(1);
            }
        }

        V loadSync(K k2, int n2, LoadingValueReference<K, V> loadingValueReference, CacheLoader<? super K, V> cacheLoader) throws ExecutionException {
            ListenableFuture<V> listenableFuture = loadingValueReference.loadFuture((K)k2, cacheLoader);
            return this.getAndRecordStats(k2, n2, loadingValueReference, listenableFuture);
        }

        ListenableFuture<V> loadAsync(final K k2, final int n2, final LoadingValueReference<K, V> loadingValueReference, CacheLoader<? super K, V> cacheLoader) {
            final ListenableFuture<V> listenableFuture = loadingValueReference.loadFuture((K)k2, cacheLoader);
            listenableFuture.addListener(new Runnable(){

                @Override
                public void run() {
                    try {
                        Object v2 = Segment.this.getAndRecordStats(k2, n2, loadingValueReference, listenableFuture);
                    }
                    catch (Throwable throwable) {
                        LocalCache.logger.log(Level.WARNING, "Exception thrown during refresh", throwable);
                        loadingValueReference.setException(throwable);
                    }
                }
            }, MoreExecutors.directExecutor());
            return listenableFuture;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        V getAndRecordStats(K k2, int n2, LoadingValueReference<K, V> loadingValueReference, ListenableFuture<V> listenableFuture) throws ExecutionException {
            V v2 = null;
            try {
                v2 = Uninterruptibles.getUninterruptibly(listenableFuture);
                if (v2 == null) {
                    throw new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + k2 + ".");
                }
                this.statsCounter.recordLoadSuccess(loadingValueReference.elapsedNanos());
                this.storeLoadedValue(k2, n2, loadingValueReference, v2);
                V v3 = v2;
                return v3;
            }
            finally {
                if (v2 == null) {
                    this.statsCounter.recordLoadException(loadingValueReference.elapsedNanos());
                    this.removeLoadingValue(k2, n2, loadingValueReference);
                }
            }
        }

        V scheduleRefresh(ReferenceEntry<K, V> referenceEntry, K k2, int n2, V v2, long l2, CacheLoader<? super K, V> cacheLoader) {
            V v3;
            if (this.map.refreshes() && l2 - referenceEntry.getWriteTime() > this.map.refreshNanos && !referenceEntry.getValueReference().isLoading() && (v3 = this.refresh(k2, n2, cacheLoader, true)) != null) {
                return v3;
            }
            return v2;
        }

        V refresh(K k2, int n2, CacheLoader<? super K, V> cacheLoader, boolean bl) {
            LoadingValueReference<K, V> loadingValueReference = this.insertLoadingValueReference(k2, n2, bl);
            if (loadingValueReference == null) {
                return null;
            }
            ListenableFuture<V> listenableFuture = this.loadAsync(k2, n2, loadingValueReference, cacheLoader);
            if (listenableFuture.isDone()) {
                try {
                    return Uninterruptibles.getUninterruptibly(listenableFuture);
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
            return null;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        LoadingValueReference<K, V> insertLoadingValueReference(K k2, int n2, boolean bl) {
            ReferenceEntry<K, V> referenceEntry = null;
            this.lock();
            try {
                Object object;
                ReferenceEntry<K, V> referenceEntry2;
                long l2 = this.map.ticker.read();
                this.preWriteCleanup(l2);
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int n3 = n2 & atomicReferenceArray.length() - 1;
                for (referenceEntry = referenceEntry2 = atomicReferenceArray.get((int)n3); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                    object = referenceEntry.getKey();
                    if (referenceEntry.getHash() != n2 || object == null || !this.map.keyEquivalence.equivalent(k2, object)) continue;
                    ValueReference<K, V> valueReference = referenceEntry.getValueReference();
                    if (valueReference.isLoading() || bl && l2 - referenceEntry.getWriteTime() < this.map.refreshNanos) {
                        LoadingValueReference<K, V> loadingValueReference = null;
                        return loadingValueReference;
                    }
                    ++this.modCount;
                    LoadingValueReference<K, V> loadingValueReference = new LoadingValueReference<K, V>(valueReference);
                    referenceEntry.setValueReference(loadingValueReference);
                    LoadingValueReference<K, V> loadingValueReference2 = loadingValueReference;
                    return loadingValueReference2;
                }
                ++this.modCount;
                object = new LoadingValueReference();
                referenceEntry = this.newEntry(k2, n2, referenceEntry2);
                referenceEntry.setValueReference((ValueReference<K, V>)object);
                atomicReferenceArray.set(n3, referenceEntry);
                Object object2 = object;
                return object2;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
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

        void recordRead(ReferenceEntry<K, V> referenceEntry, long l2) {
            if (this.map.recordsAccess()) {
                referenceEntry.setAccessTime(l2);
            }
            this.recencyQueue.add(referenceEntry);
        }

        void recordLockedRead(ReferenceEntry<K, V> referenceEntry, long l2) {
            if (this.map.recordsAccess()) {
                referenceEntry.setAccessTime(l2);
            }
            this.accessQueue.add(referenceEntry);
        }

        void recordWrite(ReferenceEntry<K, V> referenceEntry, int n2, long l2) {
            this.drainRecencyQueue();
            this.totalWeight += (long)n2;
            if (this.map.recordsAccess()) {
                referenceEntry.setAccessTime(l2);
            }
            if (this.map.recordsWrite()) {
                referenceEntry.setWriteTime(l2);
            }
            this.accessQueue.add(referenceEntry);
            this.writeQueue.add(referenceEntry);
        }

        void drainRecencyQueue() {
            ReferenceEntry<K, V> referenceEntry;
            while ((referenceEntry = this.recencyQueue.poll()) != null) {
                if (!this.accessQueue.contains(referenceEntry)) continue;
                this.accessQueue.add(referenceEntry);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        void tryExpireEntries(long l2) {
            if (this.tryLock()) {
                try {
                    this.expireEntries(l2);
                }
                finally {
                    this.unlock();
                }
            }
        }

        void expireEntries(long l2) {
            ReferenceEntry<K, V> referenceEntry;
            this.drainRecencyQueue();
            while ((referenceEntry = this.writeQueue.peek()) != null && this.map.isExpired(referenceEntry, l2)) {
                if (!this.removeEntry(referenceEntry, referenceEntry.getHash(), RemovalCause.EXPIRED)) {
                    throw new AssertionError();
                }
            }
            while ((referenceEntry = this.accessQueue.peek()) != null && this.map.isExpired(referenceEntry, l2)) {
                if (!this.removeEntry(referenceEntry, referenceEntry.getHash(), RemovalCause.EXPIRED)) {
                    throw new AssertionError();
                }
            }
        }

        void enqueueNotification(ReferenceEntry<K, V> referenceEntry, RemovalCause removalCause) {
            this.enqueueNotification(referenceEntry.getKey(), referenceEntry.getHash(), referenceEntry.getValueReference(), removalCause);
        }

        void enqueueNotification(K k2, int n2, ValueReference<K, V> valueReference, RemovalCause removalCause) {
            this.totalWeight -= (long)valueReference.getWeight();
            if (removalCause.wasEvicted()) {
                this.statsCounter.recordEviction();
            }
            if (this.map.removalNotificationQueue != LocalCache.DISCARDING_QUEUE) {
                V v2 = valueReference.get();
                RemovalNotification<K, V> removalNotification = RemovalNotification.create(k2, v2, removalCause);
                this.map.removalNotificationQueue.offer(removalNotification);
            }
        }

        void evictEntries(ReferenceEntry<K, V> referenceEntry) {
            if (!this.map.evictsBySize()) {
                return;
            }
            this.drainRecencyQueue();
            if ((long)referenceEntry.getValueReference().getWeight() > this.maxSegmentWeight && !this.removeEntry(referenceEntry, referenceEntry.getHash(), RemovalCause.SIZE)) {
                throw new AssertionError();
            }
            while (this.totalWeight > this.maxSegmentWeight) {
                ReferenceEntry<K, V> referenceEntry2 = this.getNextEvictable();
                if (!this.removeEntry(referenceEntry2, referenceEntry2.getHash(), RemovalCause.SIZE)) {
                    throw new AssertionError();
                }
            }
        }

        ReferenceEntry<K, V> getNextEvictable() {
            for (ReferenceEntry<K, V> referenceEntry : this.accessQueue) {
                int n2 = referenceEntry.getValueReference().getWeight();
                if (n2 <= 0) continue;
                return referenceEntry;
            }
            throw new AssertionError();
        }

        ReferenceEntry<K, V> getFirst(int n2) {
            AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
            return atomicReferenceArray.get(n2 & atomicReferenceArray.length() - 1);
        }

        ReferenceEntry<K, V> getEntry(Object object, int n2) {
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
            return null;
        }

        ReferenceEntry<K, V> getLiveEntry(Object object, int n2, long l2) {
            ReferenceEntry<K, V> referenceEntry = this.getEntry(object, n2);
            if (referenceEntry == null) {
                return null;
            }
            if (this.map.isExpired(referenceEntry, l2)) {
                this.tryExpireEntries(l2);
                return null;
            }
            return referenceEntry;
        }

        V getLiveValue(ReferenceEntry<K, V> referenceEntry, long l2) {
            if (referenceEntry.getKey() == null) {
                this.tryDrainReferenceQueues();
                return null;
            }
            V v2 = referenceEntry.getValueReference().get();
            if (v2 == null) {
                this.tryDrainReferenceQueues();
                return null;
            }
            if (this.map.isExpired(referenceEntry, l2)) {
                this.tryExpireEntries(l2);
                return null;
            }
            return v2;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        V get(Object object, int n2) {
            try {
                if (this.count != 0) {
                    long l2 = this.map.ticker.read();
                    ReferenceEntry<K, V> referenceEntry = this.getLiveEntry(object, n2, l2);
                    if (referenceEntry == null) {
                        V v2 = null;
                        return v2;
                    }
                    V v3 = referenceEntry.getValueReference().get();
                    if (v3 != null) {
                        this.recordRead(referenceEntry, l2);
                        V v4 = this.scheduleRefresh(referenceEntry, referenceEntry.getKey(), n2, v3, l2, this.map.defaultLoader);
                        return v4;
                    }
                    this.tryDrainReferenceQueues();
                }
                V v5 = null;
                return v5;
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
                    long l2 = this.map.ticker.read();
                    ReferenceEntry<K, V> referenceEntry = this.getLiveEntry(object, n2, l2);
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
                ReferenceEntry<K, V> referenceEntry;
                K k3;
                ReferenceEntry<K, V> referenceEntry2;
                long l2 = this.map.ticker.read();
                this.preWriteCleanup(l2);
                int n3 = this.count + 1;
                if (n3 > this.threshold) {
                    this.expand();
                    n3 = this.count + 1;
                }
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int n4 = n2 & atomicReferenceArray.length() - 1;
                for (referenceEntry2 = referenceEntry = atomicReferenceArray.get((int)n4); referenceEntry2 != null; referenceEntry2 = referenceEntry2.getNext()) {
                    k3 = referenceEntry2.getKey();
                    if (referenceEntry2.getHash() != n2 || k3 == null || !this.map.keyEquivalence.equivalent(k2, k3)) continue;
                    ValueReference<K, V> valueReference = referenceEntry2.getValueReference();
                    V v3 = valueReference.get();
                    if (v3 == null) {
                        ++this.modCount;
                        if (valueReference.isActive()) {
                            this.enqueueNotification(k2, n2, valueReference, RemovalCause.COLLECTED);
                            this.setValue(referenceEntry2, k2, v2, l2);
                            n3 = this.count;
                        } else {
                            this.setValue(referenceEntry2, k2, v2, l2);
                            n3 = this.count + 1;
                        }
                        this.count = n3;
                        this.evictEntries(referenceEntry2);
                        V v4 = null;
                        return v4;
                    }
                    if (bl) {
                        this.recordLockedRead(referenceEntry2, l2);
                        V v5 = v3;
                        return v5;
                    }
                    ++this.modCount;
                    this.enqueueNotification(k2, n2, valueReference, RemovalCause.REPLACED);
                    this.setValue(referenceEntry2, k2, v2, l2);
                    this.evictEntries(referenceEntry2);
                    V v6 = v3;
                    return v6;
                }
                ++this.modCount;
                referenceEntry2 = this.newEntry(k2, n2, referenceEntry);
                this.setValue(referenceEntry2, k2, v2, l2);
                atomicReferenceArray.set(n4, referenceEntry2);
                this.count = n3 = this.count + 1;
                this.evictEntries(referenceEntry2);
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
                ReferenceEntry<K, V> referenceEntry;
                int n5;
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
                long l2 = this.map.ticker.read();
                this.preWriteCleanup(l2);
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int n3 = n2 & atomicReferenceArray.length() - 1;
                for (ReferenceEntry<K, V> referenceEntry = referenceEntry2 = atomicReferenceArray.get((int)n3); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                    K k3 = referenceEntry.getKey();
                    if (referenceEntry.getHash() != n2 || k3 == null || !this.map.keyEquivalence.equivalent(k2, k3)) continue;
                    ValueReference<K, V> valueReference = referenceEntry.getValueReference();
                    V v4 = valueReference.get();
                    if (v4 == null) {
                        int n4;
                        if (valueReference.isActive()) {
                            ReferenceEntry<K, V> referenceEntry2;
                            n4 = this.count - 1;
                            ++this.modCount;
                            ReferenceEntry<K, V> referenceEntry3 = this.removeValueFromChain(referenceEntry2, referenceEntry, k3, n2, valueReference, RemovalCause.COLLECTED);
                            n4 = this.count - 1;
                            atomicReferenceArray.set(n3, referenceEntry3);
                            this.count = n4;
                        }
                        n4 = 0;
                        return (boolean)n4;
                    }
                    if (this.map.valueEquivalence.equivalent(v2, v4)) {
                        ++this.modCount;
                        this.enqueueNotification(k2, n2, valueReference, RemovalCause.REPLACED);
                        this.setValue(referenceEntry, k2, v3, l2);
                        this.evictEntries(referenceEntry);
                        boolean bl = true;
                        return bl;
                    }
                    this.recordLockedRead(referenceEntry, l2);
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
                long l2 = this.map.ticker.read();
                this.preWriteCleanup(l2);
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int n3 = n2 & atomicReferenceArray.length() - 1;
                for (referenceEntry = referenceEntry2 = atomicReferenceArray.get((int)n3); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                    K k3 = referenceEntry.getKey();
                    if (referenceEntry.getHash() != n2 || k3 == null || !this.map.keyEquivalence.equivalent(k2, k3)) continue;
                    ValueReference<K, V> valueReference = referenceEntry.getValueReference();
                    V v3 = valueReference.get();
                    if (v3 == null) {
                        if (valueReference.isActive()) {
                            ReferenceEntry<K, V> referenceEntry2;
                            int n4 = this.count - 1;
                            ++this.modCount;
                            ReferenceEntry<K, V> referenceEntry3 = this.removeValueFromChain(referenceEntry2, referenceEntry, k3, n2, valueReference, RemovalCause.COLLECTED);
                            n4 = this.count - 1;
                            atomicReferenceArray.set(n3, referenceEntry3);
                            this.count = n4;
                        }
                        V v4 = null;
                        return v4;
                    }
                    ++this.modCount;
                    this.enqueueNotification(k2, n2, valueReference, RemovalCause.REPLACED);
                    this.setValue(referenceEntry, k2, v2, l2);
                    this.evictEntries(referenceEntry);
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
                long l2 = this.map.ticker.read();
                this.preWriteCleanup(l2);
                int n3 = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int n4 = n2 & atomicReferenceArray.length() - 1;
                for (referenceEntry = referenceEntry2 = atomicReferenceArray.get((int)n4); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                    RemovalCause removalCause;
                    ReferenceEntry<K, V> referenceEntry2;
                    K k2 = referenceEntry.getKey();
                    if (referenceEntry.getHash() != n2 || k2 == null || !this.map.keyEquivalence.equivalent(object, k2)) continue;
                    ValueReference<K, V> valueReference = referenceEntry.getValueReference();
                    V v2 = valueReference.get();
                    if (v2 != null) {
                        removalCause = RemovalCause.EXPLICIT;
                    } else if (valueReference.isActive()) {
                        removalCause = RemovalCause.COLLECTED;
                    } else {
                        V v3 = null;
                        return v3;
                    }
                    ++this.modCount;
                    ReferenceEntry<K, V> referenceEntry3 = this.removeValueFromChain(referenceEntry2, referenceEntry, k2, n2, valueReference, removalCause);
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
        boolean storeLoadedValue(K k2, int n2, LoadingValueReference<K, V> loadingValueReference, V v2) {
            this.lock();
            try {
                ReferenceEntry<K, V> referenceEntry;
                ReferenceEntry<K, V> referenceEntry2;
                long l2 = this.map.ticker.read();
                this.preWriteCleanup(l2);
                int n3 = this.count + 1;
                if (n3 > this.threshold) {
                    this.expand();
                    n3 = this.count + 1;
                }
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int n4 = n2 & atomicReferenceArray.length() - 1;
                for (referenceEntry2 = referenceEntry = atomicReferenceArray.get((int)n4); referenceEntry2 != null; referenceEntry2 = referenceEntry2.getNext()) {
                    K k3 = referenceEntry2.getKey();
                    if (referenceEntry2.getHash() != n2 || k3 == null || !this.map.keyEquivalence.equivalent(k2, k3)) continue;
                    ValueReference<K, V> valueReference = referenceEntry2.getValueReference();
                    V v3 = valueReference.get();
                    if (loadingValueReference == valueReference || v3 == null && valueReference != LocalCache.UNSET) {
                        ++this.modCount;
                        if (loadingValueReference.isActive()) {
                            RemovalCause removalCause = v3 == null ? RemovalCause.COLLECTED : RemovalCause.REPLACED;
                            this.enqueueNotification(k2, n2, loadingValueReference, removalCause);
                            --n3;
                        }
                        this.setValue(referenceEntry2, k2, v2, l2);
                        this.count = n3;
                        this.evictEntries(referenceEntry2);
                        boolean bl = true;
                        return bl;
                    }
                    valueReference = new WeightedStrongValueReference(v2, 0);
                    this.enqueueNotification(k2, n2, valueReference, RemovalCause.REPLACED);
                    boolean bl = false;
                    return bl;
                }
                ++this.modCount;
                referenceEntry2 = this.newEntry(k2, n2, referenceEntry);
                this.setValue(referenceEntry2, k2, v2, l2);
                atomicReferenceArray.set(n4, referenceEntry2);
                this.count = n3;
                this.evictEntries(referenceEntry2);
                boolean bl = true;
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
        boolean remove(Object object, int n2, Object object2) {
            this.lock();
            try {
                long l2 = this.map.ticker.read();
                this.preWriteCleanup(l2);
                int n3 = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int n4 = n2 & atomicReferenceArray.length() - 1;
                for (ReferenceEntry<K, V> referenceEntry = referenceEntry2 = atomicReferenceArray.get((int)n4); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                    RemovalCause removalCause;
                    ReferenceEntry<K, V> referenceEntry2;
                    K k2 = referenceEntry.getKey();
                    if (referenceEntry.getHash() != n2 || k2 == null || !this.map.keyEquivalence.equivalent(object, k2)) continue;
                    ValueReference<K, V> valueReference = referenceEntry.getValueReference();
                    V v2 = valueReference.get();
                    if (this.map.valueEquivalence.equivalent(object2, v2)) {
                        removalCause = RemovalCause.EXPLICIT;
                    } else if (v2 == null && valueReference.isActive()) {
                        removalCause = RemovalCause.COLLECTED;
                    } else {
                        boolean bl = false;
                        return bl;
                    }
                    ++this.modCount;
                    ReferenceEntry<K, V> referenceEntry3 = this.removeValueFromChain(referenceEntry2, referenceEntry, k2, n2, valueReference, removalCause);
                    n3 = this.count - 1;
                    atomicReferenceArray.set(n4, referenceEntry3);
                    this.count = n3;
                    boolean bl = removalCause == RemovalCause.EXPLICIT;
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
                    for (n2 = 0; n2 < atomicReferenceArray.length(); ++n2) {
                        for (ReferenceEntry<K, V> referenceEntry = atomicReferenceArray.get((int)n2); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                            if (!referenceEntry.getValueReference().isActive()) continue;
                            this.enqueueNotification(referenceEntry, RemovalCause.EXPLICIT);
                        }
                    }
                    for (n2 = 0; n2 < atomicReferenceArray.length(); ++n2) {
                        atomicReferenceArray.set(n2, null);
                    }
                    this.clearReferenceQueues();
                    this.writeQueue.clear();
                    this.accessQueue.clear();
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

        ReferenceEntry<K, V> removeValueFromChain(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2, K k2, int n2, ValueReference<K, V> valueReference, RemovalCause removalCause) {
            this.enqueueNotification(k2, n2, valueReference, removalCause);
            this.writeQueue.remove(referenceEntry2);
            this.accessQueue.remove(referenceEntry2);
            if (valueReference.isLoading()) {
                valueReference.notifyNewValue(null);
                return referenceEntry;
            }
            return this.removeEntryFromChain(referenceEntry, referenceEntry2);
        }

        ReferenceEntry<K, V> removeEntryFromChain(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
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
            this.enqueueNotification(referenceEntry, RemovalCause.COLLECTED);
            this.writeQueue.remove(referenceEntry);
            this.accessQueue.remove(referenceEntry);
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
                    ReferenceEntry<K, V> referenceEntry4 = this.removeValueFromChain(referenceEntry3, referenceEntry2, referenceEntry2.getKey(), n2, referenceEntry2.getValueReference(), RemovalCause.COLLECTED);
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
                        ReferenceEntry<K, V> referenceEntry3 = this.removeValueFromChain(referenceEntry2, referenceEntry, k3, n2, valueReference, RemovalCause.COLLECTED);
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
        boolean removeLoadingValue(K k2, int n2, LoadingValueReference<K, V> loadingValueReference) {
            this.lock();
            try {
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int n3 = n2 & atomicReferenceArray.length() - 1;
                for (ReferenceEntry<K, V> referenceEntry = referenceEntry2 = atomicReferenceArray.get((int)n3); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                    K k3 = referenceEntry.getKey();
                    if (referenceEntry.getHash() != n2 || k3 == null || !this.map.keyEquivalence.equivalent(k2, k3)) continue;
                    ValueReference<K, V> valueReference = referenceEntry.getValueReference();
                    if (valueReference == loadingValueReference) {
                        if (loadingValueReference.isActive()) {
                            referenceEntry.setValueReference(loadingValueReference.getOldValue());
                        } else {
                            ReferenceEntry<K, V> referenceEntry2;
                            ReferenceEntry<K, V> referenceEntry3 = this.removeEntryFromChain(referenceEntry2, referenceEntry);
                            atomicReferenceArray.set(n3, referenceEntry3);
                        }
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

        boolean removeEntry(ReferenceEntry<K, V> referenceEntry, int n2, RemovalCause removalCause) {
            int n3 = this.count - 1;
            AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
            int n4 = n2 & atomicReferenceArray.length() - 1;
            for (ReferenceEntry<K, V> referenceEntry2 = referenceEntry3 = atomicReferenceArray.get((int)n4); referenceEntry2 != null; referenceEntry2 = referenceEntry2.getNext()) {
                ReferenceEntry<K, V> referenceEntry3;
                if (referenceEntry2 != referenceEntry) continue;
                ++this.modCount;
                ReferenceEntry<K, V> referenceEntry4 = this.removeValueFromChain(referenceEntry3, referenceEntry2, referenceEntry2.getKey(), n2, referenceEntry2.getValueReference(), removalCause);
                n3 = this.count - 1;
                atomicReferenceArray.set(n4, referenceEntry4);
                this.count = n3;
                return true;
            }
            return false;
        }

        void postReadCleanup() {
            if ((this.readCount.incrementAndGet() & 63) == 0) {
                this.cleanUp();
            }
        }

        void preWriteCleanup(long l2) {
            this.runLockedCleanup(l2);
        }

        void postWriteCleanup() {
            this.runUnlockedCleanup();
        }

        void cleanUp() {
            long l2 = this.map.ticker.read();
            this.runLockedCleanup(l2);
            this.runUnlockedCleanup();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        void runLockedCleanup(long l2) {
            if (this.tryLock()) {
                try {
                    this.drainReferenceQueues();
                    this.expireEntries(l2);
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

    static final class WeightedStrongValueReference<K, V>
    extends StrongValueReference<K, V> {
        final int weight;

        WeightedStrongValueReference(V v2, int n2) {
            super(v2);
            this.weight = n2;
        }

        @Override
        public int getWeight() {
            return this.weight;
        }
    }

    static final class WeightedSoftValueReference<K, V>
    extends SoftValueReference<K, V> {
        final int weight;

        WeightedSoftValueReference(ReferenceQueue<V> referenceQueue, V v2, ReferenceEntry<K, V> referenceEntry, int n2) {
            super(referenceQueue, v2, referenceEntry);
            this.weight = n2;
        }

        @Override
        public int getWeight() {
            return this.weight;
        }

        @Override
        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v2, ReferenceEntry<K, V> referenceEntry) {
            return new WeightedSoftValueReference<K, V>(referenceQueue, v2, referenceEntry, this.weight);
        }
    }

    static final class WeightedWeakValueReference<K, V>
    extends WeakValueReference<K, V> {
        final int weight;

        WeightedWeakValueReference(ReferenceQueue<V> referenceQueue, V v2, ReferenceEntry<K, V> referenceEntry, int n2) {
            super(referenceQueue, v2, referenceEntry);
            this.weight = n2;
        }

        @Override
        public int getWeight() {
            return this.weight;
        }

        @Override
        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v2, ReferenceEntry<K, V> referenceEntry) {
            return new WeightedWeakValueReference<K, V>(referenceQueue, v2, referenceEntry, this.weight);
        }
    }

    static class StrongValueReference<K, V>
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
        public int getWeight() {
            return 1;
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
        public boolean isLoading() {
            return false;
        }

        @Override
        public boolean isActive() {
            return true;
        }

        @Override
        public V waitForValue() {
            return this.get();
        }

        @Override
        public void notifyNewValue(V v2) {
        }
    }

    static class SoftValueReference<K, V>
    extends SoftReference<V>
    implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        SoftValueReference(ReferenceQueue<V> referenceQueue, V v2, ReferenceEntry<K, V> referenceEntry) {
            super(v2, referenceQueue);
            this.entry = referenceEntry;
        }

        @Override
        public int getWeight() {
            return 1;
        }

        @Override
        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }

        @Override
        public void notifyNewValue(V v2) {
        }

        @Override
        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v2, ReferenceEntry<K, V> referenceEntry) {
            return new SoftValueReference<K, V>(referenceQueue, v2, referenceEntry);
        }

        @Override
        public boolean isLoading() {
            return false;
        }

        @Override
        public boolean isActive() {
            return true;
        }

        @Override
        public V waitForValue() {
            return (V)this.get();
        }
    }

    static class WeakValueReference<K, V>
    extends WeakReference<V>
    implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        WeakValueReference(ReferenceQueue<V> referenceQueue, V v2, ReferenceEntry<K, V> referenceEntry) {
            super(v2, referenceQueue);
            this.entry = referenceEntry;
        }

        @Override
        public int getWeight() {
            return 1;
        }

        @Override
        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }

        @Override
        public void notifyNewValue(V v2) {
        }

        @Override
        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v2, ReferenceEntry<K, V> referenceEntry) {
            return new WeakValueReference<K, V>(referenceQueue, v2, referenceEntry);
        }

        @Override
        public boolean isLoading() {
            return false;
        }

        @Override
        public boolean isActive() {
            return true;
        }

        @Override
        public V waitForValue() {
            return (V)this.get();
        }
    }

    static final class WeakAccessWriteEntry<K, V>
    extends WeakEntry<K, V> {
        volatile long accessTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextAccess = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousAccess = LocalCache.nullEntry();
        volatile long writeTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextWrite = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousWrite = LocalCache.nullEntry();

        WeakAccessWriteEntry(ReferenceQueue<K> referenceQueue, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k2, n2, referenceEntry);
        }

        @Override
        public long getAccessTime() {
            return this.accessTime;
        }

        @Override
        public void setAccessTime(long l2) {
            this.accessTime = l2;
        }

        @Override
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        @Override
        public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextAccess = referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        @Override
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousAccess = referenceEntry;
        }

        @Override
        public long getWriteTime() {
            return this.writeTime;
        }

        @Override
        public void setWriteTime(long l2) {
            this.writeTime = l2;
        }

        @Override
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        @Override
        public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextWrite = referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        @Override
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousWrite = referenceEntry;
        }
    }

    static final class WeakWriteEntry<K, V>
    extends WeakEntry<K, V> {
        volatile long writeTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextWrite = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousWrite = LocalCache.nullEntry();

        WeakWriteEntry(ReferenceQueue<K> referenceQueue, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k2, n2, referenceEntry);
        }

        @Override
        public long getWriteTime() {
            return this.writeTime;
        }

        @Override
        public void setWriteTime(long l2) {
            this.writeTime = l2;
        }

        @Override
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        @Override
        public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextWrite = referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        @Override
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousWrite = referenceEntry;
        }
    }

    static final class WeakAccessEntry<K, V>
    extends WeakEntry<K, V> {
        volatile long accessTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextAccess = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousAccess = LocalCache.nullEntry();

        WeakAccessEntry(ReferenceQueue<K> referenceQueue, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k2, n2, referenceEntry);
        }

        @Override
        public long getAccessTime() {
            return this.accessTime;
        }

        @Override
        public void setAccessTime(long l2) {
            this.accessTime = l2;
        }

        @Override
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        @Override
        public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextAccess = referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        @Override
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousAccess = referenceEntry;
        }
    }

    static class WeakEntry<K, V>
    extends WeakReference<K>
    implements ReferenceEntry<K, V> {
        final int hash;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference = LocalCache.unset();

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
        public long getAccessTime() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setAccessTime(long l2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getWriteTime() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setWriteTime(long l2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        @Override
        public void setValueReference(ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
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

    static final class StrongAccessWriteEntry<K, V>
    extends StrongEntry<K, V> {
        volatile long accessTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextAccess = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousAccess = LocalCache.nullEntry();
        volatile long writeTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextWrite = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousWrite = LocalCache.nullEntry();

        StrongAccessWriteEntry(K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
            super(k2, n2, referenceEntry);
        }

        @Override
        public long getAccessTime() {
            return this.accessTime;
        }

        @Override
        public void setAccessTime(long l2) {
            this.accessTime = l2;
        }

        @Override
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        @Override
        public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextAccess = referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        @Override
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousAccess = referenceEntry;
        }

        @Override
        public long getWriteTime() {
            return this.writeTime;
        }

        @Override
        public void setWriteTime(long l2) {
            this.writeTime = l2;
        }

        @Override
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        @Override
        public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextWrite = referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        @Override
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousWrite = referenceEntry;
        }
    }

    static final class StrongWriteEntry<K, V>
    extends StrongEntry<K, V> {
        volatile long writeTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextWrite = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousWrite = LocalCache.nullEntry();

        StrongWriteEntry(K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
            super(k2, n2, referenceEntry);
        }

        @Override
        public long getWriteTime() {
            return this.writeTime;
        }

        @Override
        public void setWriteTime(long l2) {
            this.writeTime = l2;
        }

        @Override
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        @Override
        public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextWrite = referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        @Override
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousWrite = referenceEntry;
        }
    }

    static final class StrongAccessEntry<K, V>
    extends StrongEntry<K, V> {
        volatile long accessTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextAccess = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousAccess = LocalCache.nullEntry();

        StrongAccessEntry(K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
            super(k2, n2, referenceEntry);
        }

        @Override
        public long getAccessTime() {
            return this.accessTime;
        }

        @Override
        public void setAccessTime(long l2) {
            this.accessTime = l2;
        }

        @Override
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        @Override
        public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextAccess = referenceEntry;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        @Override
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousAccess = referenceEntry;
        }
    }

    static class StrongEntry<K, V>
    extends AbstractReferenceEntry<K, V> {
        final K key;
        final int hash;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference = LocalCache.unset();

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
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        @Override
        public void setValueReference(ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
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
        public long getAccessTime() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setAccessTime(long l2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getWriteTime() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setWriteTime(long l2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
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
        public long getAccessTime() {
            return 0;
        }

        @Override
        public void setAccessTime(long l2) {
        }

        @Override
        public ReferenceEntry<Object, Object> getNextInAccessQueue() {
            return this;
        }

        @Override
        public void setNextInAccessQueue(ReferenceEntry<Object, Object> referenceEntry) {
        }

        @Override
        public ReferenceEntry<Object, Object> getPreviousInAccessQueue() {
            return this;
        }

        @Override
        public void setPreviousInAccessQueue(ReferenceEntry<Object, Object> referenceEntry) {
        }

        @Override
        public long getWriteTime() {
            return 0;
        }

        @Override
        public void setWriteTime(long l2) {
        }

        @Override
        public ReferenceEntry<Object, Object> getNextInWriteQueue() {
            return this;
        }

        @Override
        public void setNextInWriteQueue(ReferenceEntry<Object, Object> referenceEntry) {
        }

        @Override
        public ReferenceEntry<Object, Object> getPreviousInWriteQueue() {
            return this;
        }

        @Override
        public void setPreviousInWriteQueue(ReferenceEntry<Object, Object> referenceEntry) {
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

        public long getAccessTime();

        public void setAccessTime(long var1);

        public ReferenceEntry<K, V> getNextInAccessQueue();

        public void setNextInAccessQueue(ReferenceEntry<K, V> var1);

        public ReferenceEntry<K, V> getPreviousInAccessQueue();

        public void setPreviousInAccessQueue(ReferenceEntry<K, V> var1);

        public long getWriteTime();

        public void setWriteTime(long var1);

        public ReferenceEntry<K, V> getNextInWriteQueue();

        public void setNextInWriteQueue(ReferenceEntry<K, V> var1);

        public ReferenceEntry<K, V> getPreviousInWriteQueue();

        public void setPreviousInWriteQueue(ReferenceEntry<K, V> var1);
    }

    static interface ValueReference<K, V> {
        public V get();

        public V waitForValue() throws ExecutionException;

        public int getWeight();

        public ReferenceEntry<K, V> getEntry();

        public ValueReference<K, V> copyFor(ReferenceQueue<V> var1, V var2, ReferenceEntry<K, V> var3);

        public void notifyNewValue(V var1);

        public boolean isLoading();

        public boolean isActive();
    }

    static abstract class EntryFactory
    extends Enum<EntryFactory> {
        public static final /* enum */ EntryFactory STRONG = new EntryFactory("STRONG", 0){

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
                return new StrongEntry<K, V>(k2, n2, referenceEntry);
            }
        };
        public static final /* enum */ EntryFactory STRONG_ACCESS = new EntryFactory("STRONG_ACCESS", 1){

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
                return new StrongAccessEntry<K, V>(k2, n2, referenceEntry);
            }

            @Override
            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                ReferenceEntry<K, V> referenceEntry3 = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyAccessEntry(referenceEntry, referenceEntry3);
                return referenceEntry3;
            }
        };
        public static final /* enum */ EntryFactory STRONG_WRITE = new EntryFactory("STRONG_WRITE", 2){

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
                return new StrongWriteEntry<K, V>(k2, n2, referenceEntry);
            }

            @Override
            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                ReferenceEntry<K, V> referenceEntry3 = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyWriteEntry(referenceEntry, referenceEntry3);
                return referenceEntry3;
            }
        };
        public static final /* enum */ EntryFactory STRONG_ACCESS_WRITE = new EntryFactory("STRONG_ACCESS_WRITE", 3){

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
                return new StrongAccessWriteEntry<K, V>(k2, n2, referenceEntry);
            }

            @Override
            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                ReferenceEntry<K, V> referenceEntry3 = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyAccessEntry(referenceEntry, referenceEntry3);
                this.copyWriteEntry(referenceEntry, referenceEntry3);
                return referenceEntry3;
            }
        };
        public static final /* enum */ EntryFactory WEAK = new EntryFactory("WEAK", 4){

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
                return new WeakEntry(segment.keyReferenceQueue, k2, n2, referenceEntry);
            }
        };
        public static final /* enum */ EntryFactory WEAK_ACCESS = new EntryFactory("WEAK_ACCESS", 5){

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
                return new WeakAccessEntry(segment.keyReferenceQueue, k2, n2, referenceEntry);
            }

            @Override
            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                ReferenceEntry<K, V> referenceEntry3 = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyAccessEntry(referenceEntry, referenceEntry3);
                return referenceEntry3;
            }
        };
        public static final /* enum */ EntryFactory WEAK_WRITE = new EntryFactory("WEAK_WRITE", 6){

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
                return new WeakWriteEntry(segment.keyReferenceQueue, k2, n2, referenceEntry);
            }

            @Override
            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                ReferenceEntry<K, V> referenceEntry3 = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyWriteEntry(referenceEntry, referenceEntry3);
                return referenceEntry3;
            }
        };
        public static final /* enum */ EntryFactory WEAK_ACCESS_WRITE = new EntryFactory("WEAK_ACCESS_WRITE", 7){

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k2, int n2, ReferenceEntry<K, V> referenceEntry) {
                return new WeakAccessWriteEntry(segment.keyReferenceQueue, k2, n2, referenceEntry);
            }

            @Override
            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                ReferenceEntry<K, V> referenceEntry3 = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyAccessEntry(referenceEntry, referenceEntry3);
                this.copyWriteEntry(referenceEntry, referenceEntry3);
                return referenceEntry3;
            }
        };
        static final EntryFactory[] factories;
        private static final /* synthetic */ EntryFactory[] $VALUES;

        public static EntryFactory[] values() {
            return (EntryFactory[])$VALUES.clone();
        }

        private EntryFactory() {
            super(string, n2);
        }

        static EntryFactory getFactory(Strength strength, boolean bl, boolean bl2) {
            int n2 = (strength == Strength.WEAK ? 4 : 0) | (bl ? 1 : 0) | (bl2 ? 2 : 0);
            return factories[n2];
        }

        abstract <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> var1, K var2, int var3, ReferenceEntry<K, V> var4);

        <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            return this.newEntry(segment, referenceEntry.getKey(), referenceEntry.getHash(), referenceEntry2);
        }

        <K, V> void copyAccessEntry(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            referenceEntry2.setAccessTime(referenceEntry.getAccessTime());
            LocalCache.connectAccessOrder(referenceEntry.getPreviousInAccessQueue(), referenceEntry2);
            LocalCache.connectAccessOrder(referenceEntry2, referenceEntry.getNextInAccessQueue());
            LocalCache.nullifyAccessOrder(referenceEntry);
        }

        <K, V> void copyWriteEntry(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            referenceEntry2.setWriteTime(referenceEntry.getWriteTime());
            LocalCache.connectWriteOrder(referenceEntry.getPreviousInWriteQueue(), referenceEntry2);
            LocalCache.connectWriteOrder(referenceEntry2, referenceEntry.getNextInWriteQueue());
            LocalCache.nullifyWriteOrder(referenceEntry);
        }

        static {
            $VALUES = new EntryFactory[]{STRONG, STRONG_ACCESS, STRONG_WRITE, STRONG_ACCESS_WRITE, WEAK, WEAK_ACCESS, WEAK_WRITE, WEAK_ACCESS_WRITE};
            factories = new EntryFactory[]{STRONG, STRONG_ACCESS, STRONG_WRITE, STRONG_ACCESS_WRITE, WEAK, WEAK_ACCESS, WEAK_WRITE, WEAK_ACCESS_WRITE};
        }

    }

    static abstract class Strength
    extends Enum<Strength> {
        public static final /* enum */ Strength STRONG = new Strength("STRONG", 0){

            @Override
            <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V v2, int n2) {
                return n2 == 1 ? new StrongValueReference(v2) : new WeightedStrongValueReference(v2, n2);
            }

            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.equals();
            }
        };
        public static final /* enum */ Strength SOFT = new Strength("SOFT", 1){

            @Override
            <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V v2, int n2) {
                return n2 == 1 ? new SoftValueReference(segment.valueReferenceQueue, v2, referenceEntry) : new WeightedSoftValueReference(segment.valueReferenceQueue, v2, referenceEntry, n2);
            }

            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
        };
        public static final /* enum */ Strength WEAK = new Strength("WEAK", 2){

            @Override
            <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V v2, int n2) {
                return n2 == 1 ? new WeakValueReference(segment.valueReferenceQueue, v2, referenceEntry) : new WeightedWeakValueReference(segment.valueReferenceQueue, v2, referenceEntry, n2);
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

        abstract <K, V> ValueReference<K, V> referenceValue(Segment<K, V> var1, ReferenceEntry<K, V> var2, V var3, int var4);

        abstract Equivalence<Object> defaultEquivalence();

        static {
            $VALUES = new Strength[]{STRONG, SOFT, WEAK};
        }

    }

}

