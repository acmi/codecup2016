/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Ascii;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.base.Ticker;
import com.google.common.collect.ComputationException;
import com.google.common.collect.ComputingConcurrentHashMap;
import com.google.common.collect.GenericMapMaker;
import com.google.common.collect.ImmutableEntry;
import com.google.common.collect.MapMakerInternalMap;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

public final class MapMaker
extends GenericMapMaker<Object, Object> {
    boolean useCustomMap;
    int initialCapacity = -1;
    int concurrencyLevel = -1;
    int maximumSize = -1;
    MapMakerInternalMap.Strength keyStrength;
    MapMakerInternalMap.Strength valueStrength;
    long expireAfterWriteNanos = -1;
    long expireAfterAccessNanos = -1;
    RemovalCause nullRemovalCause;
    Equivalence<Object> keyEquivalence;
    Ticker ticker;

    Equivalence<Object> getKeyEquivalence() {
        return MoreObjects.firstNonNull(this.keyEquivalence, this.getKeyStrength().defaultEquivalence());
    }

    int getInitialCapacity() {
        return this.initialCapacity == -1 ? 16 : this.initialCapacity;
    }

    int getConcurrencyLevel() {
        return this.concurrencyLevel == -1 ? 4 : this.concurrencyLevel;
    }

    public MapMaker weakKeys() {
        return this.setKeyStrength(MapMakerInternalMap.Strength.WEAK);
    }

    MapMaker setKeyStrength(MapMakerInternalMap.Strength strength) {
        Preconditions.checkState(this.keyStrength == null, "Key strength was already set to %s", new Object[]{this.keyStrength});
        this.keyStrength = Preconditions.checkNotNull(strength);
        Preconditions.checkArgument(this.keyStrength != MapMakerInternalMap.Strength.SOFT, "Soft keys are not supported");
        if (strength != MapMakerInternalMap.Strength.STRONG) {
            this.useCustomMap = true;
        }
        return this;
    }

    MapMakerInternalMap.Strength getKeyStrength() {
        return MoreObjects.firstNonNull(this.keyStrength, MapMakerInternalMap.Strength.STRONG);
    }

    MapMakerInternalMap.Strength getValueStrength() {
        return MoreObjects.firstNonNull(this.valueStrength, MapMakerInternalMap.Strength.STRONG);
    }

    long getExpireAfterWriteNanos() {
        return this.expireAfterWriteNanos == -1 ? 0 : this.expireAfterWriteNanos;
    }

    long getExpireAfterAccessNanos() {
        return this.expireAfterAccessNanos == -1 ? 0 : this.expireAfterAccessNanos;
    }

    Ticker getTicker() {
        return MoreObjects.firstNonNull(this.ticker, Ticker.systemTicker());
    }

    public <K, V> ConcurrentMap<K, V> makeMap() {
        if (!this.useCustomMap) {
            return new ConcurrentHashMap(this.getInitialCapacity(), 0.75f, this.getConcurrencyLevel());
        }
        return this.nullRemovalCause == null ? new MapMakerInternalMap(this) : new NullConcurrentMap(this);
    }

    @Deprecated
    @Override
    <K, V> ConcurrentMap<K, V> makeComputingMap(Function<? super K, ? extends V> function) {
        return this.nullRemovalCause == null ? new ComputingMapAdapter<K, V>(this, function) : new NullComputingConcurrentMap<K, V>(this, function);
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
        if (this.removalListener != null) {
            toStringHelper.addValue("removalListener");
        }
        return toStringHelper.toString();
    }

    static final class ComputingMapAdapter<K, V>
    extends ComputingConcurrentHashMap<K, V>
    implements Serializable {
        ComputingMapAdapter(MapMaker mapMaker, Function<? super K, ? extends V> function) {
            super(mapMaker, function);
        }

        @Override
        public V get(Object object) {
            Object v2;
            try {
                v2 = this.getOrCompute(object);
            }
            catch (ExecutionException executionException) {
                Throwable throwable = executionException.getCause();
                Throwables.propagateIfInstanceOf(throwable, ComputationException.class);
                throw new ComputationException(throwable);
            }
            if (v2 == null) {
                throw new NullPointerException(this.computingFunction + " returned null for key " + object + ".");
            }
            return v2;
        }
    }

    static final class NullComputingConcurrentMap<K, V>
    extends NullConcurrentMap<K, V> {
        final Function<? super K, ? extends V> computingFunction;

        NullComputingConcurrentMap(MapMaker mapMaker, Function<? super K, ? extends V> function) {
            super(mapMaker);
            this.computingFunction = Preconditions.checkNotNull(function);
        }

        @Override
        public V get(Object object) {
            Object object2 = object;
            V v2 = this.compute(object2);
            Preconditions.checkNotNull(v2, "%s returned null for key %s.", this.computingFunction, object2);
            this.notifyRemoval(object2, v2);
            return v2;
        }

        private V compute(K k2) {
            Preconditions.checkNotNull(k2);
            try {
                return this.computingFunction.apply(k2);
            }
            catch (ComputationException computationException) {
                throw computationException;
            }
            catch (Throwable throwable) {
                throw new ComputationException(throwable);
            }
        }
    }

    static class NullConcurrentMap<K, V>
    extends AbstractMap<K, V>
    implements Serializable,
    ConcurrentMap<K, V> {
        private final RemovalListener<K, V> removalListener;
        private final RemovalCause removalCause;

        NullConcurrentMap(MapMaker mapMaker) {
            this.removalListener = mapMaker.getRemovalListener();
            this.removalCause = mapMaker.nullRemovalCause;
        }

        @Override
        public boolean containsKey(Object object) {
            return false;
        }

        @Override
        public boolean containsValue(Object object) {
            return false;
        }

        @Override
        public V get(Object object) {
            return null;
        }

        void notifyRemoval(K k2, V v2) {
            RemovalNotification<K, V> removalNotification = new RemovalNotification<K, V>(k2, v2, this.removalCause);
            this.removalListener.onRemoval(removalNotification);
        }

        @Override
        public V put(K k2, V v2) {
            Preconditions.checkNotNull(k2);
            Preconditions.checkNotNull(v2);
            this.notifyRemoval(k2, v2);
            return null;
        }

        @Override
        public V putIfAbsent(K k2, V v2) {
            return this.put(k2, v2);
        }

        @Override
        public V remove(Object object) {
            return null;
        }

        @Override
        public boolean remove(Object object, Object object2) {
            return false;
        }

        @Override
        public V replace(K k2, V v2) {
            Preconditions.checkNotNull(k2);
            Preconditions.checkNotNull(v2);
            return null;
        }

        @Override
        public boolean replace(K k2, V v2, V v3) {
            Preconditions.checkNotNull(k2);
            Preconditions.checkNotNull(v3);
            return false;
        }

        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            return Collections.emptySet();
        }
    }

    static abstract class RemovalCause
    extends Enum<RemovalCause> {
        public static final /* enum */ RemovalCause EXPLICIT = new RemovalCause("EXPLICIT", 0){};
        public static final /* enum */ RemovalCause REPLACED = new RemovalCause("REPLACED", 1){};
        public static final /* enum */ RemovalCause COLLECTED = new RemovalCause("COLLECTED", 2){};
        public static final /* enum */ RemovalCause EXPIRED = new RemovalCause("EXPIRED", 3){};
        public static final /* enum */ RemovalCause SIZE = new RemovalCause("SIZE", 4){};
        private static final /* synthetic */ RemovalCause[] $VALUES;

        public static RemovalCause[] values() {
            return (RemovalCause[])$VALUES.clone();
        }

        private RemovalCause() {
            super(string, n2);
        }

        static {
            $VALUES = new RemovalCause[]{EXPLICIT, REPLACED, COLLECTED, EXPIRED, SIZE};
        }

    }

    static final class RemovalNotification<K, V>
    extends ImmutableEntry<K, V> {
        private final RemovalCause cause;

        RemovalNotification(K k2, V v2, RemovalCause removalCause) {
            super(k2, v2);
            this.cause = removalCause;
        }
    }

    static interface RemovalListener<K, V> {
        public void onRemoval(RemovalNotification<K, V> var1);
    }

}

