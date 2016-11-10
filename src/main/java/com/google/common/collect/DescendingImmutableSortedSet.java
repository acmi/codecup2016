/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;

class DescendingImmutableSortedSet<E>
extends ImmutableSortedSet<E> {
    private final ImmutableSortedSet<E> forward;

    DescendingImmutableSortedSet(ImmutableSortedSet<E> immutableSortedSet) {
        super(Ordering.from(immutableSortedSet.comparator()).reverse());
        this.forward = immutableSortedSet;
    }

    @Override
    public boolean contains(Object object) {
        return this.forward.contains(object);
    }

    @Override
    public int size() {
        return this.forward.size();
    }

    @Override
    public UnmodifiableIterator<E> iterator() {
        return this.forward.descendingIterator();
    }

    @Override
    ImmutableSortedSet<E> headSetImpl(E e2, boolean bl) {
        return this.forward.tailSet((Object)e2, bl).descendingSet();
    }

    @Override
    ImmutableSortedSet<E> subSetImpl(E e2, boolean bl, E e3, boolean bl2) {
        return this.forward.subSet((Object)e3, bl2, (Object)e2, bl).descendingSet();
    }

    @Override
    ImmutableSortedSet<E> tailSetImpl(E e2, boolean bl) {
        return this.forward.headSet((Object)e2, bl).descendingSet();
    }

    @Override
    public ImmutableSortedSet<E> descendingSet() {
        return this.forward;
    }

    @Override
    public UnmodifiableIterator<E> descendingIterator() {
        return this.forward.iterator();
    }

    @Override
    ImmutableSortedSet<E> createDescendingSet() {
        throw new AssertionError((Object)"should never be called");
    }

    @Override
    public E lower(E e2) {
        return this.forward.higher(e2);
    }

    @Override
    public E floor(E e2) {
        return this.forward.ceiling(e2);
    }

    @Override
    public E ceiling(E e2) {
        return this.forward.floor(e2);
    }

    @Override
    public E higher(E e2) {
        return this.forward.lower(e2);
    }

    @Override
    int indexOf(Object object) {
        int n2 = this.forward.indexOf(object);
        if (n2 == -1) {
            return n2;
        }
        return this.size() - 1 - n2;
    }

    @Override
    boolean isPartialView() {
        return this.forward.isPartialView();
    }
}

