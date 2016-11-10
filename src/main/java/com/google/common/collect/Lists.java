/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

public final class Lists {
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList();
    }

    public static <E> ArrayList<E> newArrayList(Iterable<? extends E> iterable) {
        Preconditions.checkNotNull(iterable);
        return iterable instanceof Collection ? new ArrayList<E>(Collections2.cast(iterable)) : Lists.newArrayList(iterable.iterator());
    }

    public static <E> ArrayList<E> newArrayList(Iterator<? extends E> iterator) {
        ArrayList<E> arrayList = Lists.newArrayList();
        Iterators.addAll(arrayList, iterator);
        return arrayList;
    }

    public static <E> ArrayList<E> newArrayListWithCapacity(int n2) {
        CollectPreconditions.checkNonnegative(n2, "initialArraySize");
        return new ArrayList(n2);
    }

    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList();
    }

    public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> iterable) {
        LinkedList<E> linkedList = Lists.newLinkedList();
        Iterables.addAll(linkedList, iterable);
        return linkedList;
    }

    public static <E> List<E> asList(E e2, E[] arrE) {
        return new OnePlusArrayList<E>(e2, arrE);
    }

    static boolean equalsImpl(List<?> list, Object object) {
        if (object == Preconditions.checkNotNull(list)) {
            return true;
        }
        if (!(object instanceof List)) {
            return false;
        }
        List list2 = (List)object;
        int n2 = list.size();
        if (n2 != list2.size()) {
            return false;
        }
        if (list instanceof RandomAccess && list2 instanceof RandomAccess) {
            for (int i2 = 0; i2 < n2; ++i2) {
                if (Objects.equal(list.get(i2), list2.get(i2))) continue;
                return false;
            }
            return true;
        }
        return Iterators.elementsEqual(list.iterator(), list2.iterator());
    }

    static int indexOfImpl(List<?> list, Object object) {
        if (list instanceof RandomAccess) {
            return Lists.indexOfRandomAccess(list, object);
        }
        ListIterator listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            if (!Objects.equal(object, listIterator.next())) continue;
            return listIterator.previousIndex();
        }
        return -1;
    }

    private static int indexOfRandomAccess(List<?> list, Object object) {
        int n2 = list.size();
        if (object == null) {
            for (int i2 = 0; i2 < n2; ++i2) {
                if (list.get(i2) != null) continue;
                return i2;
            }
        } else {
            for (int i3 = 0; i3 < n2; ++i3) {
                if (!object.equals(list.get(i3))) continue;
                return i3;
            }
        }
        return -1;
    }

    static int lastIndexOfImpl(List<?> list, Object object) {
        if (list instanceof RandomAccess) {
            return Lists.lastIndexOfRandomAccess(list, object);
        }
        ListIterator listIterator = list.listIterator(list.size());
        while (listIterator.hasPrevious()) {
            if (!Objects.equal(object, listIterator.previous())) continue;
            return listIterator.nextIndex();
        }
        return -1;
    }

    private static int lastIndexOfRandomAccess(List<?> list, Object object) {
        if (object == null) {
            for (int i2 = list.size() - 1; i2 >= 0; --i2) {
                if (list.get(i2) != null) continue;
                return i2;
            }
        } else {
            for (int i3 = list.size() - 1; i3 >= 0; --i3) {
                if (!object.equals(list.get(i3))) continue;
                return i3;
            }
        }
        return -1;
    }

    private static class OnePlusArrayList<E>
    extends AbstractList<E>
    implements Serializable,
    RandomAccess {
        final E first;
        final E[] rest;

        OnePlusArrayList(E e2, E[] arrE) {
            this.first = e2;
            this.rest = Preconditions.checkNotNull(arrE);
        }

        @Override
        public int size() {
            return this.rest.length + 1;
        }

        @Override
        public E get(int n2) {
            Preconditions.checkElementIndex(n2, this.size());
            return n2 == 0 ? this.first : this.rest[n2 - 1];
        }
    }

}

