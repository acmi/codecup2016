/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import java.lang.reflect.Array;
import java.util.NavigableMap;
import java.util.SortedMap;

final class Platform {
    static <T> T[] newArray(T[] arrT, int n2) {
        Class class_ = arrT.getClass().getComponentType();
        Object[] arrobject = (Object[])Array.newInstance(class_, n2);
        return arrobject;
    }

    static MapMaker tryWeakKeys(MapMaker mapMaker) {
        return mapMaker.weakKeys();
    }

    static <K, V1, V2> SortedMap<K, V2> mapsTransformEntriesSortedMap(SortedMap<K, V1> sortedMap, Maps.EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
        return sortedMap instanceof NavigableMap ? Maps.transformEntries((NavigableMap)sortedMap, entryTransformer) : Maps.transformEntriesIgnoreNavigable(sortedMap, entryTransformer);
    }
}

