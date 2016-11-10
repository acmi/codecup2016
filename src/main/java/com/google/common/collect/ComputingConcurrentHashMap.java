/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.MapMaker;
import com.google.common.collect.MapMakerInternalMap;
import java.lang.ref.ReferenceQueue;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReferenceArray;

class ComputingConcurrentHashMap<K, V>
extends MapMakerInternalMap<K, V> {
    final Function<? super K, ? extends V> computingFunction;

    ComputingConcurrentHashMap(MapMaker mapMaker, Function<? super K, ? extends V> function) {
        super(mapMaker);
        this.computingFunction = Preconditions.checkNotNull(function);
    }

    @Override
    MapMakerInternalMap.Segment<K, V> createSegment(int n2, int n3) {
        return new ComputingSegment(this, n2, n3);
    }

    @Override
    ComputingSegment<K, V> segmentFor(int n2) {
        return (ComputingSegment)super.segmentFor(n2);
    }

    V getOrCompute(K k2) throws ExecutionException {
        int n2 = this.hash(Preconditions.checkNotNull(k2));
        return this.segmentFor(n2).getOrCompute(k2, n2, this.computingFunction);
    }

    private static final class ComputingValueReference<K, V>
    implements MapMakerInternalMap.ValueReference<K, V> {
        final Function<? super K, ? extends V> computingFunction;
        volatile MapMakerInternalMap.ValueReference<K, V> computedReference = MapMakerInternalMap.unset();

        public ComputingValueReference(Function<? super K, ? extends V> function) {
            this.computingFunction = function;
        }

        @Override
        public V get() {
            return null;
        }

        @Override
        public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() {
            return null;
        }

        @Override
        public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v2, MapMakerInternalMap.ReferenceEntry<K, V> referenceEntry) {
            return this;
        }

        @Override
        public boolean isComputingReference() {
            return true;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public V waitForValue() throws ExecutionException {
            if (this.computedReference == MapMakerInternalMap.UNSET) {
                boolean bl = false;
                try {
                    ComputingValueReference computingValueReference = this;
                    synchronized (computingValueReference) {
                        while (this.computedReference == MapMakerInternalMap.UNSET) {
                            try {
                                this.wait();
                            }
                            catch (InterruptedException interruptedException) {
                                bl = true;
                            }
                        }
                    }
                }
                finally {
                    if (bl) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            return this.computedReference.waitForValue();
        }

        @Override
        public void clear(MapMakerInternalMap.ValueReference<K, V> valueReference) {
            this.setValueReference(valueReference);
        }

        V compute(K k2, int n2) throws ExecutionException {
            V v2;
            try {
                v2 = this.computingFunction.apply(k2);
            }
            catch (Throwable throwable) {
                this.setValueReference(new ComputationExceptionReference(throwable));
                throw new ExecutionException(throwable);
            }
            this.setValueReference(new ComputedReference(v2));
            return v2;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        void setValueReference(MapMakerInternalMap.ValueReference<K, V> valueReference) {
            ComputingValueReference computingValueReference = this;
            synchronized (computingValueReference) {
                if (this.computedReference == MapMakerInternalMap.UNSET) {
                    this.computedReference = valueReference;
                    this.notifyAll();
                }
            }
        }
    }

    private static final class ComputedReference<K, V>
    implements MapMakerInternalMap.ValueReference<K, V> {
        final V value;

        ComputedReference(V v2) {
            this.value = v2;
        }

        @Override
        public V get() {
            return this.value;
        }

        @Override
        public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() {
            return null;
        }

        @Override
        public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v2, MapMakerInternalMap.ReferenceEntry<K, V> referenceEntry) {
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
        public void clear(MapMakerInternalMap.ValueReference<K, V> valueReference) {
        }
    }

    private static final class ComputationExceptionReference<K, V>
    implements MapMakerInternalMap.ValueReference<K, V> {
        final Throwable t;

        ComputationExceptionReference(Throwable throwable) {
            this.t = throwable;
        }

        @Override
        public V get() {
            return null;
        }

        @Override
        public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() {
            return null;
        }

        @Override
        public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v2, MapMakerInternalMap.ReferenceEntry<K, V> referenceEntry) {
            return this;
        }

        @Override
        public boolean isComputingReference() {
            return false;
        }

        @Override
        public V waitForValue() throws ExecutionException {
            throw new ExecutionException(this.t);
        }

        @Override
        public void clear(MapMakerInternalMap.ValueReference<K, V> valueReference) {
        }
    }

    static final class ComputingSegment<K, V>
    extends MapMakerInternalMap.Segment<K, V> {
        ComputingSegment(MapMakerInternalMap<K, V> mapMakerInternalMap, int n2, int n3) {
            super(mapMakerInternalMap, n2, n3);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        V getOrCompute(K k2, int n2, Function<? super K, ? extends V> function) throws ExecutionException {
            try {
                ComputingValueReference<? super K, ? extends V> computingValueReference;
                MapMakerInternalMap.ReferenceEntry<Object, Object> referenceEntry;
                Object v2;
                do {
                    if ((referenceEntry = this.getEntry(k2, n2)) != null && (v2 = this.getLiveValue(referenceEntry)) != null) {
                        this.recordRead(referenceEntry);
                        computingValueReference = (ComputingValueReference<? super K, ? extends V>)v2;
                        return (V)computingValueReference;
                    }
                    if (referenceEntry == null || !referenceEntry.getValueReference().isComputingReference()) {
                        boolean bl;
                        bl = true;
                        computingValueReference = null;
                        this.lock();
                        try {
                            this.preWriteCleanup();
                            int n3 = this.count - 1;
                            AtomicReferenceArray atomicReferenceArray = this.table;
                            int n4 = n2 & atomicReferenceArray.length() - 1;
                            for (referenceEntry = referenceEntry2 = (MapMakerInternalMap.ReferenceEntry<Object, Object>)atomicReferenceArray.get((int)n4); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                                Object k3 = referenceEntry.getKey();
                                if (referenceEntry.getHash() != n2 || k3 == null || !this.map.keyEquivalence.equivalent(k2, k3)) continue;
                                MapMakerInternalMap.ValueReference valueReference = referenceEntry.getValueReference();
                                if (valueReference.isComputingReference()) {
                                    bl = false;
                                    break;
                                }
                                Object v3 = referenceEntry.getValueReference().get();
                                if (v3 == null) {
                                    this.enqueueNotification(k3, n2, v3, MapMaker.RemovalCause.COLLECTED);
                                } else if (this.map.expires() && this.map.isExpired(referenceEntry)) {
                                    this.enqueueNotification(k3, n2, v3, MapMaker.RemovalCause.EXPIRED);
                                } else {
                                    this.recordLockedRead(referenceEntry);
                                    Object v4 = v3;
                                    return v4;
                                }
                                this.evictionQueue.remove(referenceEntry);
                                this.expirationQueue.remove(referenceEntry);
                                this.count = n3;
                                break;
                            }
                            if (bl) {
                                computingValueReference = new ComputingValueReference<K, V>(function);
                                if (referenceEntry == null) {
                                    MapMakerInternalMap.ReferenceEntry<Object, Object> referenceEntry2;
                                    referenceEntry = this.newEntry(k2, n2, referenceEntry2);
                                    referenceEntry.setValueReference(computingValueReference);
                                    atomicReferenceArray.set(n4, referenceEntry);
                                } else {
                                    referenceEntry.setValueReference(computingValueReference);
                                }
                            }
                        }
                        finally {
                            this.unlock();
                            this.postWriteCleanup();
                        }
                        if (bl) {
                            V v5 = this.compute(k2, n2, referenceEntry, computingValueReference);
                            return v5;
                        }
                    }
                    Preconditions.checkState(!Thread.holdsLock(referenceEntry), "Recursive computation");
                } while ((v2 = referenceEntry.getValueReference().waitForValue()) == null);
                this.recordRead(referenceEntry);
                computingValueReference = (ComputingValueReference<? super K, ? extends V>)v2;
                return (V)computingValueReference;
            }
            finally {
                this.postReadCleanup();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        V compute(K k2, int n2, MapMakerInternalMap.ReferenceEntry<K, V> referenceEntry, ComputingValueReference<K, V> computingValueReference) throws ExecutionException {
            Object v2 = null;
            long l2 = System.nanoTime();
            long l3 = 0;
            try {
                MapMakerInternalMap.ReferenceEntry<K, V> referenceEntry2 = referenceEntry;
                synchronized (referenceEntry2) {
                    v2 = computingValueReference.compute(k2, n2);
                    l3 = System.nanoTime();
                }
                if (v2 != null && (referenceEntry2 = this.put(k2, n2, v2, true)) != null) {
                    this.enqueueNotification(k2, n2, v2, MapMaker.RemovalCause.REPLACED);
                }
                referenceEntry2 = v2;
                return (V)referenceEntry2;
            }
            finally {
                if (l3 == 0) {
                    l3 = System.nanoTime();
                }
                if (v2 == null) {
                    this.clearValue(k2, n2, computingValueReference);
                }
            }
        }
    }

}

