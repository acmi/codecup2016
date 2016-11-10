/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.Ordering;
import java.io.Serializable;
import java.util.Iterator;

final class ReverseOrdering<T>
extends Ordering<T>
implements Serializable {
    final Ordering<? super T> forwardOrder;

    ReverseOrdering(Ordering<? super T> ordering) {
        this.forwardOrder = Preconditions.checkNotNull(ordering);
    }

    @Override
    public int compare(T t2, T t3) {
        return this.forwardOrder.compare(t3, t2);
    }

    @Override
    public <S extends T> Ordering<S> reverse() {
        return this.forwardOrder;
    }

    @Override
    public <E extends T> E min(E e2, E e3) {
        return this.forwardOrder.max(e2, e3);
    }

    @Override
    public /* varargs */ <E extends T> E min(E e2, E e3, E e4, E ... arrE) {
        return this.forwardOrder.max(e2, e3, e4, arrE);
    }

    @Override
    public <E extends T> E min(Iterator<E> iterator) {
        return this.forwardOrder.max(iterator);
    }

    @Override
    public <E extends T> E min(Iterable<E> iterable) {
        return this.forwardOrder.max(iterable);
    }

    @Override
    public <E extends T> E max(E e2, E e3) {
        return this.forwardOrder.min(e2, e3);
    }

    @Override
    public /* varargs */ <E extends T> E max(E e2, E e3, E e4, E ... arrE) {
        return this.forwardOrder.min(e2, e3, e4, arrE);
    }

    @Override
    public <E extends T> E max(Iterator<E> iterator) {
        return this.forwardOrder.min(iterator);
    }

    @Override
    public <E extends T> E max(Iterable<E> iterable) {
        return this.forwardOrder.min(iterable);
    }

    public int hashCode() {
        return - this.forwardOrder.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof ReverseOrdering) {
            ReverseOrdering reverseOrdering = (ReverseOrdering)object;
            return this.forwardOrder.equals(reverseOrdering.forwardOrder);
        }
        return false;
    }

    public String toString() {
        return this.forwardOrder + ".reverse()";
    }
}

