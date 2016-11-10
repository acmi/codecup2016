/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedAsList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.PeekingIterator;
import com.google.common.collect.SortedIterables;
import com.google.common.collect.SortedLists;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

final class RegularImmutableSortedSet<E>
extends ImmutableSortedSet<E> {
    private final transient ImmutableList<E> elements;

    RegularImmutableSortedSet(ImmutableList<E> immutableList, Comparator<? super E> comparator) {
        super(comparator);
        this.elements = immutableList;
    }

    @Override
    public UnmodifiableIterator<E> iterator() {
        return this.elements.iterator();
    }

    @Override
    public UnmodifiableIterator<E> descendingIterator() {
        return this.elements.reverse().iterator();
    }

    @Override
    public int size() {
        return this.elements.size();
    }

    @Override
    public boolean contains(Object object) {
        try {
            return object != null && this.unsafeBinarySearch(object) >= 0;
        }
        catch (ClassCastException classCastException) {
            return false;
        }
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        if (collection instanceof Multiset) {
            collection = ((Multiset)collection).elementSet();
        }
        if (!SortedIterables.hasSameComparator(this.comparator(), collection) || collection.size() <= 1) {
            return super.containsAll(collection);
        }
        PeekingIterator peekingIterator = Iterators.peekingIterator(this.iterator());
        Iterator iterator = collection.iterator();
        Object obj = iterator.next();
        try {
            while (peekingIterator.hasNext()) {
                int n2 = this.unsafeCompare(peekingIterator.peek(), obj);
                if (n2 < 0) {
                    peekingIterator.next();
                    continue;
                }
                if (n2 == 0) {
                    if (!iterator.hasNext()) {
                        return true;
                    }
                    obj = iterator.next();
                    continue;
                }
                if (n2 <= 0) continue;
                return false;
            }
        }
        catch (NullPointerException nullPointerException) {
            return false;
        }
        catch (ClassCastException classCastException) {
            return false;
        }
        return false;
    }

    private int unsafeBinarySearch(Object object) throws ClassCastException {
        return Collections.binarySearch(this.elements, object, this.unsafeComparator());
    }

    @Override
    boolean isPartialView() {
        return this.elements.isPartialView();
    }

    @Override
    int copyIntoArray(Object[] arrobject, int n2) {
        return this.elements.copyIntoArray(arrobject, n2);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Set)) {
            return false;
        }
        Set set = (Set)object;
        if (this.size() != set.size()) {
            return false;
        }
        if (this.isEmpty()) {
            return true;
        }
        if (SortedIterables.hasSameComparator(this.comparator, set)) {
            Iterator iterator = set.iterator();
            try {
                for (Object e2 : this) {
                    Object e3 = iterator.next();
                    if (e3 != null && this.unsafeCompare(e2, e3) == 0) continue;
                    return false;
                }
                return true;
            }
            catch (ClassCastException classCastException) {
                return false;
            }
            catch (NoSuchElementException noSuchElementException) {
                return false;
            }
        }
        return this.containsAll(set);
    }

    @Override
    public E first() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.elements.get(0);
    }

    @Override
    public E last() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.elements.get(this.size() - 1);
    }

    @Override
    public E lower(E e2) {
        int n2 = this.headIndex(e2, false) - 1;
        return n2 == -1 ? null : (E)this.elements.get(n2);
    }

    @Override
    public E floor(E e2) {
        int n2 = this.headIndex(e2, true) - 1;
        return n2 == -1 ? null : (E)this.elements.get(n2);
    }

    @Override
    public E ceiling(E e2) {
        int n2 = this.tailIndex(e2, true);
        return n2 == this.size() ? null : (E)this.elements.get(n2);
    }

    @Override
    public E higher(E e2) {
        int n2 = this.tailIndex(e2, false);
        return n2 == this.size() ? null : (E)this.elements.get(n2);
    }

    @Override
    ImmutableSortedSet<E> headSetImpl(E e2, boolean bl) {
        return this.getSubSet(0, this.headIndex(e2, bl));
    }

    int headIndex(E e2, boolean bl) {
        return SortedLists.binarySearch(this.elements, Preconditions.checkNotNull(e2), this.comparator(), bl ? SortedLists.KeyPresentBehavior.FIRST_AFTER : SortedLists.KeyPresentBehavior.FIRST_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
    }

    @Override
    ImmutableSortedSet<E> subSetImpl(E e2, boolean bl, E e3, boolean bl2) {
        return this.tailSetImpl(e2, bl).headSetImpl(e3, bl2);
    }

    @Override
    ImmutableSortedSet<E> tailSetImpl(E e2, boolean bl) {
        return this.getSubSet(this.tailIndex(e2, bl), this.size());
    }

    int tailIndex(E e2, boolean bl) {
        return SortedLists.binarySearch(this.elements, Preconditions.checkNotNull(e2), this.comparator(), bl ? SortedLists.KeyPresentBehavior.FIRST_PRESENT : SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
    }

    Comparator<Object> unsafeComparator() {
        return this.comparator;
    }

    RegularImmutableSortedSet<E> getSubSet(int n2, int n3) {
        if (n2 == 0 && n3 == this.size()) {
            return this;
        }
        if (n2 < n3) {
            return new RegularImmutableSortedSet<E>((ImmutableList<E>)this.elements.subList(n2, n3), this.comparator);
        }
        return RegularImmutableSortedSet.emptySet(this.comparator);
    }

    @Override
    int indexOf(Object object) {
        int n2;
        if (object == null) {
            return -1;
        }
        try {
            n2 = SortedLists.binarySearch(this.elements, object, this.unsafeComparator(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.INVERTED_INSERTION_INDEX);
        }
        catch (ClassCastException classCastException) {
            return -1;
        }
        return n2 >= 0 ? n2 : -1;
    }

    @Override
    ImmutableList<E> createAsList() {
        return this.size() <= 1 ? this.elements : new ImmutableSortedAsList<E>(this, this.elements);
    }

    @Override
    ImmutableSortedSet<E> createDescendingSet() {
        Ordering ordering = Ordering.from(this.comparator).reverse();
        return this.isEmpty() ? RegularImmutableSortedSet.emptySet(ordering) : new RegularImmutableSortedSet<E>(this.elements.reverse(), ordering);
    }
}

