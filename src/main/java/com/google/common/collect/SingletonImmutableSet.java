/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;

final class SingletonImmutableSet<E>
extends ImmutableSet<E> {
    final transient E element;
    private transient int cachedHashCode;

    SingletonImmutableSet(E e2) {
        this.element = Preconditions.checkNotNull(e2);
    }

    SingletonImmutableSet(E e2, int n2) {
        this.element = e2;
        this.cachedHashCode = n2;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean contains(Object object) {
        return this.element.equals(object);
    }

    @Override
    public UnmodifiableIterator<E> iterator() {
        return Iterators.singletonIterator(this.element);
    }

    @Override
    boolean isPartialView() {
        return false;
    }

    @Override
    int copyIntoArray(Object[] arrobject, int n2) {
        arrobject[n2] = this.element;
        return n2 + 1;
    }

    @Override
    public final int hashCode() {
        int n2 = this.cachedHashCode;
        if (n2 == 0) {
            this.cachedHashCode = n2 = this.element.hashCode();
        }
        return n2;
    }

    @Override
    boolean isHashCodeFast() {
        return this.cachedHashCode != 0;
    }

    @Override
    public String toString() {
        String string = this.element.toString();
        return new StringBuilder(string.length() + 2).append('[').append(string).append(']').toString();
    }
}

