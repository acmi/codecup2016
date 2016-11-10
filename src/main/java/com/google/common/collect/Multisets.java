/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.google.common.collect.TransformedIterator;
import com.google.common.primitives.Ints;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public final class Multisets {
    private static final Ordering<Multiset.Entry<?>> DECREASING_COUNT_ORDERING = new Ordering<Multiset.Entry<?>>(){

        @Override
        public int compare(Multiset.Entry<?> entry, Multiset.Entry<?> entry2) {
            return Ints.compare(entry2.getCount(), entry.getCount());
        }
    };

    static boolean equalsImpl(Multiset<?> multiset, Object object) {
        if (object == multiset) {
            return true;
        }
        if (object instanceof Multiset) {
            Multiset multiset2 = (Multiset)object;
            if (multiset.size() != multiset2.size() || multiset.entrySet().size() != multiset2.entrySet().size()) {
                return false;
            }
            for (Multiset.Entry entry : multiset2.entrySet()) {
                if (multiset.count(entry.getElement()) == entry.getCount()) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    static <E> boolean addAllImpl(Multiset<E> multiset, Collection<? extends E> collection) {
        if (collection.isEmpty()) {
            return false;
        }
        if (collection instanceof Multiset) {
            Multiset<E> multiset2 = Multisets.cast(collection);
            for (Multiset.Entry<E> entry : multiset2.entrySet()) {
                multiset.add(entry.getElement(), entry.getCount());
            }
        } else {
            Iterators.addAll(multiset, collection.iterator());
        }
        return true;
    }

    static boolean removeAllImpl(Multiset<?> multiset, Collection<?> collection) {
        Collection collection2 = collection instanceof Multiset ? ((Multiset)collection).elementSet() : collection;
        return multiset.elementSet().removeAll(collection2);
    }

    static boolean retainAllImpl(Multiset<?> multiset, Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        Collection collection2 = collection instanceof Multiset ? ((Multiset)collection).elementSet() : collection;
        return multiset.elementSet().retainAll(collection2);
    }

    static <E> int setCountImpl(Multiset<E> multiset, E e2, int n2) {
        CollectPreconditions.checkNonnegative(n2, "count");
        int n3 = multiset.count(e2);
        int n4 = n2 - n3;
        if (n4 > 0) {
            multiset.add(e2, n4);
        } else if (n4 < 0) {
            multiset.remove(e2, - n4);
        }
        return n3;
    }

    static <E> boolean setCountImpl(Multiset<E> multiset, E e2, int n2, int n3) {
        CollectPreconditions.checkNonnegative(n2, "oldCount");
        CollectPreconditions.checkNonnegative(n3, "newCount");
        if (multiset.count(e2) == n2) {
            multiset.setCount(e2, n3);
            return true;
        }
        return false;
    }

    static <E> Iterator<E> iteratorImpl(Multiset<E> multiset) {
        return new MultisetIteratorImpl<E>(multiset, multiset.entrySet().iterator());
    }

    static int sizeImpl(Multiset<?> multiset) {
        long l2 = 0;
        for (Multiset.Entry entry : multiset.entrySet()) {
            l2 += (long)entry.getCount();
        }
        return Ints.saturatedCast(l2);
    }

    static <T> Multiset<T> cast(Iterable<T> iterable) {
        return (Multiset)iterable;
    }

    static final class MultisetIteratorImpl<E>
    implements Iterator<E> {
        private final Multiset<E> multiset;
        private final Iterator<Multiset.Entry<E>> entryIterator;
        private Multiset.Entry<E> currentEntry;
        private int laterCount;
        private int totalCount;
        private boolean canRemove;

        MultisetIteratorImpl(Multiset<E> multiset, Iterator<Multiset.Entry<E>> iterator) {
            this.multiset = multiset;
            this.entryIterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return this.laterCount > 0 || this.entryIterator.hasNext();
        }

        @Override
        public E next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            if (this.laterCount == 0) {
                this.currentEntry = this.entryIterator.next();
                this.totalCount = this.laterCount = this.currentEntry.getCount();
            }
            --this.laterCount;
            this.canRemove = true;
            return this.currentEntry.getElement();
        }

        @Override
        public void remove() {
            CollectPreconditions.checkRemove(this.canRemove);
            if (this.totalCount == 1) {
                this.entryIterator.remove();
            } else {
                this.multiset.remove(this.currentEntry.getElement());
            }
            --this.totalCount;
            this.canRemove = false;
        }
    }

    static abstract class EntrySet<E>
    extends Sets.ImprovedAbstractSet<Multiset.Entry<E>> {
        EntrySet() {
        }

        abstract Multiset<E> multiset();

        @Override
        public boolean contains(Object object) {
            if (object instanceof Multiset.Entry) {
                Multiset.Entry entry = (Multiset.Entry)object;
                if (entry.getCount() <= 0) {
                    return false;
                }
                int n2 = this.multiset().count(entry.getElement());
                return n2 == entry.getCount();
            }
            return false;
        }

        @Override
        public boolean remove(Object object) {
            if (object instanceof Multiset.Entry) {
                Multiset.Entry entry = (Multiset.Entry)object;
                E e2 = entry.getElement();
                int n2 = entry.getCount();
                if (n2 != 0) {
                    Multiset<E> multiset = this.multiset();
                    return multiset.setCount(e2, n2, 0);
                }
            }
            return false;
        }

        @Override
        public void clear() {
            this.multiset().clear();
        }
    }

    static abstract class ElementSet<E>
    extends Sets.ImprovedAbstractSet<E> {
        ElementSet() {
        }

        abstract Multiset<E> multiset();

        @Override
        public void clear() {
            this.multiset().clear();
        }

        @Override
        public boolean contains(Object object) {
            return this.multiset().contains(object);
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return this.multiset().containsAll(collection);
        }

        @Override
        public boolean isEmpty() {
            return this.multiset().isEmpty();
        }

        @Override
        public Iterator<E> iterator() {
            return new TransformedIterator<Multiset.Entry<E>, E>(this.multiset().entrySet().iterator()){

                @Override
                E transform(Multiset.Entry<E> entry) {
                    return entry.getElement();
                }
            };
        }

        @Override
        public boolean remove(Object object) {
            return this.multiset().remove(object, Integer.MAX_VALUE) > 0;
        }

        @Override
        public int size() {
            return this.multiset().entrySet().size();
        }

    }

    static abstract class AbstractEntry<E>
    implements Multiset.Entry<E> {
        AbstractEntry() {
        }

        public boolean equals(Object object) {
            if (object instanceof Multiset.Entry) {
                Multiset.Entry entry = (Multiset.Entry)object;
                return this.getCount() == entry.getCount() && Objects.equal(this.getElement(), entry.getElement());
            }
            return false;
        }

        public int hashCode() {
            Object e2 = this.getElement();
            return (e2 == null ? 0 : e2.hashCode()) ^ this.getCount();
        }

        public String toString() {
            String string = String.valueOf(this.getElement());
            int n2 = this.getCount();
            return n2 == 1 ? string : string + " x " + n2;
        }
    }

}

