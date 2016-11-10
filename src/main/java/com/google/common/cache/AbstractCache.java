/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.LongAddable;
import com.google.common.cache.LongAddables;

public abstract class AbstractCache<K, V>
implements Cache<K, V> {

    public static final class SimpleStatsCounter
    implements StatsCounter {
        private final LongAddable hitCount = LongAddables.create();
        private final LongAddable missCount = LongAddables.create();
        private final LongAddable loadSuccessCount = LongAddables.create();
        private final LongAddable loadExceptionCount = LongAddables.create();
        private final LongAddable totalLoadTime = LongAddables.create();
        private final LongAddable evictionCount = LongAddables.create();

        @Override
        public void recordHits(int n2) {
            this.hitCount.add(n2);
        }

        @Override
        public void recordMisses(int n2) {
            this.missCount.add(n2);
        }

        @Override
        public void recordLoadSuccess(long l2) {
            this.loadSuccessCount.increment();
            this.totalLoadTime.add(l2);
        }

        @Override
        public void recordLoadException(long l2) {
            this.loadExceptionCount.increment();
            this.totalLoadTime.add(l2);
        }

        @Override
        public void recordEviction() {
            this.evictionCount.increment();
        }
    }

    public static interface StatsCounter {
        public void recordHits(int var1);

        public void recordMisses(int var1);

        public void recordLoadSuccess(long var1);

        public void recordLoadException(long var1);

        public void recordEviction();
    }

}

