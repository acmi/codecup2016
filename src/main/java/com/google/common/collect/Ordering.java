/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.AllEqualOrdering;
import com.google.common.collect.ByFunctionOrdering;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.ComparatorOrdering;
import com.google.common.collect.CompoundOrdering;
import com.google.common.collect.ExplicitOrdering;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.LexicographicalOrdering;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.google.common.collect.NaturalOrdering;
import com.google.common.collect.NullsFirstOrdering;
import com.google.common.collect.NullsLastOrdering;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Platform;
import com.google.common.collect.ReverseOrdering;
import com.google.common.collect.UsingToStringOrdering;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Ordering<T>
implements Comparator<T> {
    static final int LEFT_IS_GREATER = 1;
    static final int RIGHT_IS_GREATER = -1;

    public static <C extends Comparable> Ordering<C> natural() {
        return NaturalOrdering.INSTANCE;
    }

    public static <T> Ordering<T> from(Comparator<T> comparator) {
        return comparator instanceof Ordering ? (Ordering)comparator : new ComparatorOrdering<T>(comparator);
    }

    @Deprecated
    public static <T> Ordering<T> from(Ordering<T> ordering) {
        return Preconditions.checkNotNull(ordering);
    }

    public static <T> Ordering<T> explicit(List<T> list) {
        return new ExplicitOrdering<T>(list);
    }

    public static /* varargs */ <T> Ordering<T> explicit(T t2, T ... arrT) {
        return Ordering.explicit(Lists.asList(t2, arrT));
    }

    public static Ordering<Object> allEqual() {
        return AllEqualOrdering.INSTANCE;
    }

    public static Ordering<Object> usingToString() {
        return UsingToStringOrdering.INSTANCE;
    }

    public static Ordering<Object> arbitrary() {
        return ArbitraryOrderingHolder.ARBITRARY_ORDERING;
    }

    protected Ordering() {
    }

    public <S extends T> Ordering<S> reverse() {
        return new ReverseOrdering(this);
    }

    public <S extends T> Ordering<S> nullsFirst() {
        return new NullsFirstOrdering(this);
    }

    public <S extends T> Ordering<S> nullsLast() {
        return new NullsLastOrdering(this);
    }

    public <F> Ordering<F> onResultOf(Function<F, ? extends T> function) {
        return new ByFunctionOrdering<F, T>(function, this);
    }

    <T2 extends T> Ordering<Map.Entry<T2, ?>> onKeys() {
        return this.onResultOf(Maps.keyFunction());
    }

    public <U extends T> Ordering<U> compound(Comparator<? super U> comparator) {
        return new CompoundOrdering<U>(this, Preconditions.checkNotNull(comparator));
    }

    public static <T> Ordering<T> compound(Iterable<? extends Comparator<? super T>> iterable) {
        return new CompoundOrdering(iterable);
    }

    public <S extends T> Ordering<Iterable<S>> lexicographical() {
        return new LexicographicalOrdering(this);
    }

    @Override
    public abstract int compare(T var1, T var2);

    public <E extends T> E min(Iterator<E> iterator) {
        E e2 = iterator.next();
        while (iterator.hasNext()) {
            e2 = this.min(e2, iterator.next());
        }
        return e2;
    }

    public <E extends T> E min(Iterable<E> iterable) {
        return this.min(iterable.iterator());
    }

    public <E extends T> E min(E e2, E e3) {
        return this.compare(e2, e3) <= 0 ? e2 : e3;
    }

    public /* varargs */ <E extends T> E min(E e2, E e3, E e4, E ... arrE) {
        E e5 = this.min(this.min(e2, e3), e4);
        for (E e6 : arrE) {
            e5 = this.min(e5, e6);
        }
        return e5;
    }

    public <E extends T> E max(Iterator<E> iterator) {
        E e2 = iterator.next();
        while (iterator.hasNext()) {
            e2 = this.max(e2, iterator.next());
        }
        return e2;
    }

    public <E extends T> E max(Iterable<E> iterable) {
        return this.max(iterable.iterator());
    }

    public <E extends T> E max(E e2, E e3) {
        return this.compare(e2, e3) >= 0 ? e2 : e3;
    }

    public /* varargs */ <E extends T> E max(E e2, E e3, E e4, E ... arrE) {
        E e5 = this.max(this.max(e2, e3), e4);
        for (E e6 : arrE) {
            e5 = this.max(e5, e6);
        }
        return e5;
    }

    public <E extends T> List<E> leastOf(Iterable<E> iterable, int n2) {
        Collection collection;
        if (iterable instanceof Collection && (long)(collection = (Collection)iterable).size() <= 2 * (long)n2) {
            Object[] arrobject = collection.toArray();
            Arrays.sort(arrobject, this);
            if (arrobject.length > n2) {
                arrobject = ObjectArrays.arraysCopyOf(arrobject, n2);
            }
            return Collections.unmodifiableList(Arrays.asList(arrobject));
        }
        return this.leastOf(iterable.iterator(), n2);
    }

    public <E extends T> List<E> leastOf(Iterator<E> iterator, int n2) {
        E e2;
        Object object;
        Preconditions.checkNotNull(iterator);
        CollectPreconditions.checkNonnegative(n2, "k");
        if (n2 == 0 || !iterator.hasNext()) {
            return ImmutableList.of();
        }
        if (n2 >= 1073741823) {
            ArrayList<E> arrayList = Lists.newArrayList(iterator);
            Collections.sort(arrayList, this);
            if (arrayList.size() > n2) {
                arrayList.subList(n2, arrayList.size()).clear();
            }
            arrayList.trimToSize();
            return Collections.unmodifiableList(arrayList);
        }
        int n3 = n2 * 2;
        Object[] arrobject = new Object[n3];
        arrobject[0] = object = iterator.next();
        int n4 = 1;
        while (n4 < n2 && iterator.hasNext()) {
            e2 = iterator.next();
            arrobject[n4++] = e2;
            object = this.max(object, e2);
        }
        while (iterator.hasNext()) {
            int n5;
            e2 = iterator.next();
            if (this.compare(e2, object) >= 0) continue;
            arrobject[n4++] = e2;
            if (n4 != n3) continue;
            int n6 = 0;
            int n7 = n3 - 1;
            int n8 = 0;
            while (n6 < n7) {
                n5 = n6 + n7 + 1 >>> 1;
                int n9 = this.partition(arrobject, n6, n7, n5);
                if (n9 > n2) {
                    n7 = n9 - 1;
                    continue;
                }
                if (n9 >= n2) break;
                n6 = Math.max(n9, n6 + 1);
                n8 = n9;
            }
            n4 = n2;
            object = arrobject[n8];
            for (n5 = n8 + 1; n5 < n4; ++n5) {
                object = this.max(object, arrobject[n5]);
            }
        }
        Arrays.sort(arrobject, 0, n4, this);
        n4 = Math.min(n4, n2);
        return Collections.unmodifiableList(Arrays.asList(ObjectArrays.arraysCopyOf(arrobject, n4)));
    }

    private <E extends T> int partition(E[] arrE, int n2, int n3, int n4) {
        E e2 = arrE[n4];
        arrE[n4] = arrE[n3];
        arrE[n3] = e2;
        int n5 = n2;
        for (int i2 = n2; i2 < n3; ++i2) {
            if (this.compare(arrE[i2], e2) >= 0) continue;
            ObjectArrays.swap(arrE, n5, i2);
            ++n5;
        }
        ObjectArrays.swap(arrE, n3, n5);
        return n5;
    }

    public <E extends T> List<E> greatestOf(Iterable<E> iterable, int n2) {
        return this.reverse().leastOf(iterable, n2);
    }

    public <E extends T> List<E> greatestOf(Iterator<E> iterator, int n2) {
        return this.reverse().leastOf(iterator, n2);
    }

    public <E extends T> List<E> sortedCopy(Iterable<E> iterable) {
        Object[] arrobject = Iterables.toArray(iterable);
        Arrays.sort(arrobject, this);
        return Lists.newArrayList(Arrays.asList(arrobject));
    }

    public <E extends T> ImmutableList<E> immutableSortedCopy(Iterable<E> iterable) {
        Object[] arrobject;
        for (Object object : arrobject = Iterables.toArray(iterable)) {
            Preconditions.checkNotNull(object);
        }
        Arrays.sort(arrobject, this);
        return ImmutableList.asImmutableList(arrobject);
    }

    public boolean isOrdered(Iterable<? extends T> iterable) {
        Iterator<T> iterator = iterable.iterator();
        if (iterator.hasNext()) {
            T t2 = iterator.next();
            while (iterator.hasNext()) {
                T t3 = iterator.next();
                if (this.compare(t2, t3) > 0) {
                    return false;
                }
                t2 = t3;
            }
        }
        return true;
    }

    public boolean isStrictlyOrdered(Iterable<? extends T> iterable) {
        Iterator<T> iterator = iterable.iterator();
        if (iterator.hasNext()) {
            T t2 = iterator.next();
            while (iterator.hasNext()) {
                T t3 = iterator.next();
                if (this.compare(t2, t3) >= 0) {
                    return false;
                }
                t2 = t3;
            }
        }
        return true;
    }

    public int binarySearch(List<? extends T> list, T t2) {
        return Collections.binarySearch(list, t2, this);
    }

    static class IncomparableValueException
    extends ClassCastException {
        final Object value;

        IncomparableValueException(Object object) {
            super("Cannot compare value: " + object);
            this.value = object;
        }
    }

    static class ArbitraryOrdering
    extends Ordering<Object> {
        private Map<Object, Integer> uids;

        ArbitraryOrdering() {
            this.uids = Platform.tryWeakKeys(new MapMaker()).makeComputingMap(new Function<Object, Integer>(){
                final AtomicInteger counter;

                @Override
                public Integer apply(Object object) {
                    return this.counter.getAndIncrement();
                }
            });
        }

        @Override
        public int compare(Object object, Object object2) {
            int n2;
            if (object == object2) {
                return 0;
            }
            if (object == null) {
                return -1;
            }
            if (object2 == null) {
                return 1;
            }
            int n3 = this.identityHashCode(object);
            if (n3 != (n2 = this.identityHashCode(object2))) {
                return n3 < n2 ? -1 : 1;
            }
            int n4 = this.uids.get(object).compareTo(this.uids.get(object2));
            if (n4 == 0) {
                throw new AssertionError();
            }
            return n4;
        }

        public String toString() {
            return "Ordering.arbitrary()";
        }

        int identityHashCode(Object object) {
            return System.identityHashCode(object);
        }

    }

    private static class ArbitraryOrderingHolder {
        static final Ordering<Object> ARBITRARY_ORDERING = new ArbitraryOrdering();
    }

}

