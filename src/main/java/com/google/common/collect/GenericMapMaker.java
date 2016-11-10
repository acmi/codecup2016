/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.collect.MapMaker;
import java.util.concurrent.ConcurrentMap;

@Deprecated
abstract class GenericMapMaker<K0, V0> {
    MapMaker.RemovalListener<K0, V0> removalListener;

    GenericMapMaker() {
    }

    <K extends K0, V extends V0> MapMaker.RemovalListener<K, V> getRemovalListener() {
        return MoreObjects.firstNonNull(this.removalListener, NullListener.INSTANCE);
    }

    @Deprecated
    abstract <K extends K0, V extends V0> ConcurrentMap<K, V> makeComputingMap(Function<? super K, ? extends V> var1);

    static final class NullListener
    extends Enum<NullListener>
    implements MapMaker.RemovalListener<Object, Object> {
        public static final /* enum */ NullListener INSTANCE = new NullListener();
        private static final /* synthetic */ NullListener[] $VALUES;

        public static NullListener[] values() {
            return (NullListener[])$VALUES.clone();
        }

        private NullListener() {
            super(string, n2);
        }

        @Override
        public void onRemoval(MapMaker.RemovalNotification<Object, Object> removalNotification) {
        }

        static {
            $VALUES = new NullListener[]{INSTANCE};
        }
    }

}

