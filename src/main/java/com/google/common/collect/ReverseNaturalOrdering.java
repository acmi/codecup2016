/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.NaturalOrdering;
import com.google.common.collect.Ordering;
import java.io.Serializable;
import java.util.Iterator;

final class ReverseNaturalOrdering
extends Ordering<Comparable>
implements Serializable {
    static final ReverseNaturalOrdering INSTANCE = new ReverseNaturalOrdering();

    @Override
    public int compare(Comparable comparable, Comparable comparable2) {
        Preconditions.checkNotNull(comparable);
        if (comparable == comparable2) {
            return 0;
        }
        return comparable2.compareTo(comparable);
    }

    @Override
    public <S extends Comparable> Ordering<S> reverse() {
        return Ordering.natural();
    }

    @Override
    public <E extends Comparable> E min(E e2, E e3) {
        return (E)((Comparable)NaturalOrdering.INSTANCE.max(e2, e3));
    }

    @Override
    public /* varargs */ <E extends Comparable> E min(E e2, E e3, E e4, E ... arrE) {
        return (E)((Comparable)NaturalOrdering.INSTANCE.max(e2, e3, e4, arrE));
    }

    @Override
    public <E extends Comparable> E min(Iterator<E> iterator) {
        return (E)((Comparable)NaturalOrdering.INSTANCE.max(iterator));
    }

    @Override
    public <E extends Comparable> E min(Iterable<E> iterable) {
        return (E)((Comparable)NaturalOrdering.INSTANCE.max(iterable));
    }

    @Override
    public <E extends Comparable> E max(E e2, E e3) {
        return (E)((Comparable)NaturalOrdering.INSTANCE.min(e2, e3));
    }

    @Override
    public /* varargs */ <E extends Comparable> E max(E e2, E e3, E e4, E ... arrE) {
        return (E)((Comparable)NaturalOrdering.INSTANCE.min(e2, e3, e4, arrE));
    }

    @Override
    public <E extends Comparable> E max(Iterator<E> iterator) {
        return (E)((Comparable)NaturalOrdering.INSTANCE.min(iterator));
    }

    @Override
    public <E extends Comparable> E max(Iterable<E> iterable) {
        return (E)((Comparable)NaturalOrdering.INSTANCE.min(iterable));
    }

    public String toString() {
        return "Ordering.natural().reverse()";
    }

    private ReverseNaturalOrdering() {
    }
}

