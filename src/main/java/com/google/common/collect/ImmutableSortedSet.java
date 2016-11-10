/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.DescendingImmutableSortedSet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSetFauxverideShim;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Ordering;
import com.google.common.collect.RegularImmutableSortedSet;
import com.google.common.collect.SortedIterable;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;

public abstract class ImmutableSortedSet<E>
extends ImmutableSortedSetFauxverideShim<E>
implements SortedIterable<E>,
NavigableSet<E> {
    private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
    private static final RegularImmutableSortedSet<Comparable> NATURAL_EMPTY_SET = new RegularImmutableSortedSet<Comparable>(ImmutableList.<E>of(), NATURAL_ORDER);
    final transient Comparator<? super E> comparator;
    transient ImmutableSortedSet<E> descendingSet;

    static <E> RegularImmutableSortedSet<E> emptySet(Comparator<? super E> comparator) {
        if (NATURAL_ORDER.equals(comparator)) {
            return NATURAL_EMPTY_SET;
        }
        return new RegularImmutableSortedSet<E>(ImmutableList.of(), comparator);
    }

    int unsafeCompare(Object object, Object object2) {
        return ImmutableSortedSet.unsafeCompare(this.comparator, object, object2);
    }

    static int unsafeCompare(Comparator<?> comparator, Object object, Object object2) {
        Comparator comparator2 = comparator;
        return comparator2.compare((Object)object, (Object)object2);
    }

    ImmutableSortedSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public Comparator<? super E> comparator() {
        return this.comparator;
    }

    @Override
    public abstract UnmodifiableIterator<E> iterator();

    @Override
    public ImmutableSortedSet<E> headSet(E e2) {
        return this.headSet((Object)e2, false);
    }

    @Override
    public ImmutableSortedSet<E> headSet(E e2, boolean bl) {
        return this.headSetImpl(Preconditions.checkNotNull(e2), bl);
    }

    @Override
    public ImmutableSortedSet<E> subSet(E e2, E e3) {
        return this.subSet((Object)e2, true, (Object)e3, false);
    }

    @Override
    public ImmutableSortedSet<E> subSet(E e2, boolean bl, E e3, boolean bl2) {
        Preconditions.checkNotNull(e2);
        Preconditions.checkNotNull(e3);
        Preconditions.checkArgument(this.comparator.compare(e2, e3) <= 0);
        return this.subSetImpl(e2, bl, e3, bl2);
    }

    @Override
    public ImmutableSortedSet<E> tailSet(E e2) {
        return this.tailSet((Object)e2, true);
    }

    @Override
    public ImmutableSortedSet<E> tailSet(E e2, boolean bl) {
        return this.tailSetImpl(Preconditions.checkNotNull(e2), bl);
    }

    abstract ImmutableSortedSet<E> headSetImpl(E var1, boolean var2);

    abstract ImmutableSortedSet<E> subSetImpl(E var1, boolean var2, E var3, boolean var4);

    abstract ImmutableSortedSet<E> tailSetImpl(E var1, boolean var2);

    @Override
    public E lower(E e2) {
        return Iterators.getNext(this.headSet((Object)e2, false).descendingIterator(), null);
    }

    @Override
    public E floor(E e2) {
        return Iterators.getNext(this.headSet((Object)e2, true).descendingIterator(), null);
    }

    @Override
    public E ceiling(E e2) {
        return Iterables.getFirst(this.tailSet((Object)e2, true), null);
    }

    @Override
    public E higher(E e2) {
        return Iterables.getFirst(this.tailSet((Object)e2, false), null);
    }

    @Override
    public E first() {
        return this.iterator().next();
    }

    @Override
    public E last() {
        return this.descendingIterator().next();
    }

    @Deprecated
    @Override
    public final E pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public final E pollLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSortedSet<E> descendingSet() {
        ImmutableSortedSet<E> immutableSortedSet = this.descendingSet;
        if (immutableSortedSet == null) {
            immutableSortedSet = this.descendingSet = this.createDescendingSet();
            immutableSortedSet.descendingSet = this;
        }
        return immutableSortedSet;
    }

    ImmutableSortedSet<E> createDescendingSet() {
        return new DescendingImmutableSortedSet<E>(this);
    }

    @Override
    public abstract UnmodifiableIterator<E> descendingIterator();

    abstract int indexOf(Object var1);
}

