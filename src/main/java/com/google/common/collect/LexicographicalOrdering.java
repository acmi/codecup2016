/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.collect.Ordering;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;

final class LexicographicalOrdering<T>
extends Ordering<Iterable<T>>
implements Serializable {
    final Comparator<? super T> elementOrder;

    LexicographicalOrdering(Comparator<? super T> comparator) {
        this.elementOrder = comparator;
    }

    @Override
    public int compare(Iterable<T> iterable, Iterable<T> iterable2) {
        Iterator<T> iterator = iterable.iterator();
        Iterator<T> iterator2 = iterable2.iterator();
        while (iterator.hasNext()) {
            if (!iterator2.hasNext()) {
                return 1;
            }
            int n2 = this.elementOrder.compare(iterator.next(), iterator2.next());
            if (n2 == 0) continue;
            return n2;
        }
        if (iterator2.hasNext()) {
            return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof LexicographicalOrdering) {
            LexicographicalOrdering lexicographicalOrdering = (LexicographicalOrdering)object;
            return this.elementOrder.equals(lexicographicalOrdering.elementOrder);
        }
        return false;
    }

    public int hashCode() {
        return this.elementOrder.hashCode() ^ 2075626741;
    }

    public String toString() {
        return this.elementOrder + ".lexicographical()";
    }
}

