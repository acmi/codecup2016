/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

final class ExplicitOrdering<T>
extends Ordering<T>
implements Serializable {
    final ImmutableMap<T, Integer> rankMap;

    ExplicitOrdering(List<T> list) {
        this(Maps.indexMap(list));
    }

    ExplicitOrdering(ImmutableMap<T, Integer> immutableMap) {
        this.rankMap = immutableMap;
    }

    @Override
    public int compare(T t2, T t3) {
        return this.rank(t2) - this.rank(t3);
    }

    private int rank(T t2) {
        Integer n2 = this.rankMap.get(t2);
        if (n2 == null) {
            throw new Ordering.IncomparableValueException(t2);
        }
        return n2;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ExplicitOrdering) {
            ExplicitOrdering explicitOrdering = (ExplicitOrdering)object;
            return this.rankMap.equals(explicitOrdering.rankMap);
        }
        return false;
    }

    public int hashCode() {
        return this.rankMap.hashCode();
    }

    public String toString() {
        return "Ordering.explicit(" + this.rankMap.keySet() + ")";
    }
}

