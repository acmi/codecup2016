/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.cache;

import com.google.common.base.Ascii;
import com.google.common.base.Equivalence;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.base.Ticker;
import com.google.common.cache.AbstractCache;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.LocalCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.cache.Weigher;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CacheBuilder<K, V> {
    static final Supplier<? extends AbstractCache.StatsCounter> NULL_STATS_COUNTER = Suppliers.ofInstance(new AbstractCache.StatsCounter(){

        @Override
        public void recordHits(int n2) {
        }

        @Override
        public void recordMisses(int n2) {
        }

        @Override
        public void recordLoadSuccess(long l2) {
        }

        @Override
        public void recordLoadException(long l2) {
        }

        @Override
        public void recordEviction() {
        }
    });
    static final CacheStats EMPTY_STATS = new CacheStats(0, 0, 0, 0, 0, 0);
    static final Supplier<AbstractCache.StatsCounter> CACHE_STATS_COUNTER = new Supplier<AbstractCache.StatsCounter>(){

        @Override
        public AbstractCache.StatsCounter get() {
            return new AbstractCache.SimpleStatsCounter();
        }
    };
    static final Ticker NULL_TICKER = new Ticker(){

        @Override
        public long read() {
            return 0;
        }
    };
    private static final Logger logger = Logger.getLogger(CacheBuilder.class.getName());
    boolean strictParsing = true;
    int initialCapacity = -1;
    int concurrencyLevel = -1;
    long maximumSize = -1;
    long maximumWeight = -1;
    Weigher<? super K, ? super V> weigher;
    LocalCache.Strength keyStrength;
    LocalCache.Strength valueStrength;
    long expireAfterWriteNanos = -1;
    long expireAfterAccessNanos = -1;
    long refreshNanos = -1;
    Equivalence<Object> keyEquivalence;
    Equivalence<Object> valueEquivalence;
    RemovalListener<? super K, ? super V> removalListener;
    Ticker ticker;
    Supplier<? extends AbstractCache.StatsCounter> statsCounterSupplier = NULL_STATS_COUNTER;

    CacheBuilder() {
    }

    public static CacheBuilder<Object, Object> newBuilder() {
        return new CacheBuilder<Object, Object>();
    }

    Equivalence<Object> getKeyEquivalence() {
        return MoreObjects.firstNonNull(this.keyEquivalence, this.getKeyStrength().defaultEquivalence());
    }

    Equivalence<Object> getValueEquivalence() {
        return MoreObjects.firstNonNull(this.valueEquivalence, this.getValueStrength().defaultEquivalence());
    }

    int getInitialCapacity() {
        return this.initialCapacity == -1 ? 16 : this.initialCapacity;
    }

    int getConcurrencyLevel() {
        return this.concurrencyLevel == -1 ? 4 : this.concurrencyLevel;
    }

    public CacheBuilder<K, V> maximumSize(long l2) {
        Preconditions.checkState(this.maximumSize == -1, "maximum size was already set to %s", this.maximumSize);
        Preconditions.checkState(this.maximumWeight == -1, "maximum weight was already set to %s", this.maximumWeight);
        Preconditions.checkState(this.weigher == null, "maximum size can not be combined with weigher");
        Preconditions.checkArgument(l2 >= 0, "maximum size must not be negative");
        this.maximumSize = l2;
        return this;
    }

    long getMaximumWeight() {
        if (this.expireAfterWriteNanos == 0 || this.expireAfterAccessNanos == 0) {
            return 0;
        }
        return this.weigher == null ? this.maximumSize : this.maximumWeight;
    }

    <K1 extends K, V1 extends V> Weigher<K1, V1> getWeigher() {
        return MoreObjects.firstNonNull(this.weigher, OneWeigher.INSTANCE);
    }

    public CacheBuilder<K, V> weakKeys() {
        return this.setKeyStrength(LocalCache.Strength.WEAK);
    }

    CacheBuilder<K, V> setKeyStrength(LocalCache.Strength strength) {
        Preconditions.checkState(this.keyStrength == null, "Key strength was already set to %s", new Object[]{this.keyStrength});
        this.keyStrength = Preconditions.checkNotNull(strength);
        return this;
    }

    LocalCache.Strength getKeyStrength() {
        return MoreObjects.firstNonNull(this.keyStrength, LocalCache.Strength.STRONG);
    }

    public CacheBuilder<K, V> weakValues() {
        return this.setValueStrength(LocalCache.Strength.WEAK);
    }

    public CacheBuilder<K, V> softValues() {
        return this.setValueStrength(LocalCache.Strength.SOFT);
    }

    CacheBuilder<K, V> setValueStrength(LocalCache.Strength strength) {
        Preconditions.checkState(this.valueStrength == null, "Value strength was already set to %s", new Object[]{this.valueStrength});
        this.valueStrength = Preconditions.checkNotNull(strength);
        return this;
    }

    LocalCache.Strength getValueStrength() {
        return MoreObjects.firstNonNull(this.valueStrength, LocalCache.Strength.STRONG);
    }

    long getExpireAfterWriteNanos() {
        return this.expireAfterWriteNanos == -1 ? 0 : this.expireAfterWriteNanos;
    }

    long getExpireAfterAccessNanos() {
        return this.expireAfterAccessNanos == -1 ? 0 : this.expireAfterAccessNanos;
    }

    long getRefreshNanos() {
        return this.refreshNanos == -1 ? 0 : this.refreshNanos;
    }

    Ticker getTicker(boolean bl) {
        if (this.ticker != null) {
            return this.ticker;
        }
        return bl ? Ticker.systemTicker() : NULL_TICKER;
    }

    public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> removalListener(RemovalListener<? super K1, ? super V1> removalListener) {
        Preconditions.checkState(this.removalListener == null);
        CacheBuilder cacheBuilder = this;
        cacheBuilder.removalListener = Preconditions.checkNotNull(removalListener);
        return cacheBuilder;
    }

    <K1 extends K, V1 extends V> RemovalListener<K1, V1> getRemovalListener() {
        return MoreObjects.firstNonNull(this.removalListener, NullListener.INSTANCE);
    }

    Supplier<? extends AbstractCache.StatsCounter> getStatsCounterSupplier() {
        return this.statsCounterSupplier;
    }

    public <K1 extends K, V1 extends V> LoadingCache<K1, V1> build(CacheLoader<? super K1, V1> cacheLoader) {
        this.checkWeightWithWeigher();
        return new LocalCache.LocalLoadingCache<K1, V1>(this, cacheLoader);
    }

    public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
        this.checkWeightWithWeigher();
        this.checkNonLoadingCache();
        return new LocalCache.LocalManualCache(this);
    }

    private void checkNonLoadingCache() {
        Preconditions.checkState(this.refreshNanos == -1, "refreshAfterWrite requires a LoadingCache");
    }

    private void checkWeightWithWeigher() {
        if (this.weigher == null) {
            Preconditions.checkState(this.maximumWeight == -1, "maximumWeight requires weigher");
        } else if (this.strictParsing) {
            Preconditions.checkState(this.maximumWeight != -1, "weigher requires maximumWeight");
        } else if (this.maximumWeight == -1) {
            logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
        }
    }

    public String toString() {
        MoreObjects.ToStringHelper toStringHelper = MoreObjects.toStringHelper(this);
        if (this.initialCapacity != -1) {
            toStringHelper.add("initialCapacity", this.initialCapacity);
        }
        if (this.concurrencyLevel != -1) {
            toStringHelper.add("concurrencyLevel", this.concurrencyLevel);
        }
        if (this.maximumSize != -1) {
            toStringHelper.add("maximumSize", this.maximumSize);
        }
        if (this.maximumWeight != -1) {
            toStringHelper.add("maximumWeight", this.maximumWeight);
        }
        if (this.expireAfterWriteNanos != -1) {
            toStringHelper.add("expireAfterWrite", "" + this.expireAfterWriteNanos + "ns");
        }
        if (this.expireAfterAccessNanos != -1) {
            toStringHelper.add("expireAfterAccess", "" + this.expireAfterAccessNanos + "ns");
        }
        if (this.keyStrength != null) {
            toStringHelper.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
        }
        if (this.valueStrength != null) {
            toStringHelper.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
        }
        if (this.keyEquivalence != null) {
            toStringHelper.addValue("keyEquivalence");
        }
        if (this.valueEquivalence != null) {
            toStringHelper.addValue("valueEquivalence");
        }
        if (this.removalListener != null) {
            toStringHelper.addValue("removalListener");
        }
        return toStringHelper.toString();
    }

    static final class OneWeigher
    extends Enum<OneWeigher>
    implements Weigher<Object, Object> {
        public static final /* enum */ OneWeigher INSTANCE = new OneWeigher();
        private static final /* synthetic */ OneWeigher[] $VALUES;

        public static OneWeigher[] values() {
            return (OneWeigher[])$VALUES.clone();
        }

        private OneWeigher() {
            super(string, n2);
        }

        @Override
        public int weigh(Object object, Object object2) {
            return 1;
        }

        static {
            $VALUES = new OneWeigher[]{INSTANCE};
        }
    }

    static final class NullListener
    extends Enum<NullListener>
    implements RemovalListener<Object, Object> {
        public static final /* enum */ NullListener INSTANCE = new NullListener();
        private static final /* synthetic */ NullListener[] $VALUES;

        public static NullListener[] values() {
            return (NullListener[])$VALUES.clone();
        }

        private NullListener() {
            super(string, n2);
        }

        @Override
        public void onRemoval(RemovalNotification<Object, Object> removalNotification) {
        }

        static {
            $VALUES = new NullListener[]{INSTANCE};
        }
    }

}

