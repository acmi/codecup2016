/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import java.util.List;

final class SingletonImmutableList<E>
extends ImmutableList<E> {
    final transient E element;

    SingletonImmutableList(E e2) {
        this.element = Preconditions.checkNotNull(e2);
    }

    @Override
    public E get(int n2) {
        Preconditions.checkElementIndex(n2, 1);
        return this.element;
    }

    @Override
    public UnmodifiableIterator<E> iterator() {
        return Iterators.singletonIterator(this.element);
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public ImmutableList<E> subList(int n2, int n3) {
        Preconditions.checkPositionIndexes(n2, n3, 1);
        return n2 == n3 ? ImmutableList.of() : this;
    }

    @Override
    public String toString() {
        String string = this.element.toString();
        return new StringBuilder(string.length() + 2).append('[').append(string).append(']').toString();
    }

    @Override
    boolean isPartialView() {
        return false;
    }
}

