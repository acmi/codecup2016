/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.TransformedIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public final class Iterables {
    public static String toString(Iterable<?> iterable) {
        return Iterators.toString(iterable.iterator());
    }

    public static <T> T getOnlyElement(Iterable<T> iterable) {
        return Iterators.getOnlyElement(iterable.iterator());
    }

    static <T> T[] toArray(Iterable<? extends T> iterable, T[] arrT) {
        Collection<T> collection = Iterables.toCollection(iterable);
        return collection.toArray(arrT);
    }

    static Object[] toArray(Iterable<?> iterable) {
        return Iterables.toCollection(iterable).toArray();
    }

    private static <E> Collection<E> toCollection(Iterable<E> iterable) {
        return iterable instanceof Collection ? (ArrayList<E>)iterable : Lists.newArrayList(iterable.iterator());
    }

    public static <T> boolean addAll(Collection<T> collection, Iterable<? extends T> iterable) {
        if (iterable instanceof Collection) {
            Collection<? extends T> collection2 = Collections2.cast(iterable);
            return collection.addAll(collection2);
        }
        return Iterators.addAll(collection, Preconditions.checkNotNull(iterable).iterator());
    }

    public static <T> Iterable<T> concat(Iterable<? extends T> iterable, Iterable<? extends T> iterable2) {
        return Iterables.concat(ImmutableList.of(iterable, iterable2));
    }

    public static <T> Iterable<T> concat(final Iterable<? extends Iterable<? extends T>> iterable) {
        Preconditions.checkNotNull(iterable);
        return new FluentIterable<T>(){

            @Override
            public Iterator<T> iterator() {
                return Iterators.concat(Iterables.iterators(iterable));
            }
        };
    }

    private static <T> Iterator<Iterator<? extends T>> iterators(Iterable<? extends Iterable<? extends T>> iterable) {
        return new TransformedIterator<Iterable<? extends T>, Iterator<? extends T>>(iterable.iterator()){

            @Override
            Iterator<? extends T> transform(Iterable<? extends T> iterable) {
                return iterable.iterator();
            }
        };
    }

    public static <T> T getFirst(Iterable<? extends T> iterable, T t2) {
        return Iterators.getNext(iterable.iterator(), t2);
    }

}

