/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIndexedListIterator;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.RegularImmutableList;
import com.google.common.collect.SingletonImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.UnmodifiableListIterator;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

public abstract class ImmutableList<E>
extends ImmutableCollection<E>
implements List<E>,
RandomAccess {
    public static <E> ImmutableList<E> of() {
        return RegularImmutableList.EMPTY;
    }

    public static <E> ImmutableList<E> of(E e2) {
        return new SingletonImmutableList<E>(e2);
    }

    public static <E> ImmutableList<E> of(E e2, E e3) {
        return ImmutableList.construct(e2, e3);
    }

    public static <E> ImmutableList<E> of(E e2, E e3, E e4) {
        return ImmutableList.construct(e2, e3, e4);
    }

    public static <E> ImmutableList<E> copyOf(Iterable<? extends E> iterable) {
        Preconditions.checkNotNull(iterable);
        return iterable instanceof Collection ? ImmutableList.copyOf((Collection)iterable) : ImmutableList.copyOf(iterable.iterator());
    }

    public static <E> ImmutableList<E> copyOf(Collection<? extends E> collection) {
        if (collection instanceof ImmutableCollection) {
            ImmutableList immutableList = ((ImmutableCollection)collection).asList();
            return immutableList.isPartialView() ? ImmutableList.asImmutableList(immutableList.toArray()) : immutableList;
        }
        return ImmutableList.construct(collection.toArray());
    }

    public static <E> ImmutableList<E> copyOf(Iterator<? extends E> iterator) {
        if (!iterator.hasNext()) {
            return ImmutableList.of();
        }
        E e2 = iterator.next();
        if (!iterator.hasNext()) {
            return ImmutableList.of(e2);
        }
        return new Builder().add((Object)e2).addAll(iterator).build();
    }

    public static <E> ImmutableList<E> copyOf(E[] arrE) {
        switch (arrE.length) {
            case 0: {
                return ImmutableList.of();
            }
            case 1: {
                return new SingletonImmutableList<E>(arrE[0]);
            }
        }
        return new RegularImmutableList(ObjectArrays.checkElementsNotNull((Object[])arrE.clone()));
    }

    private static /* varargs */ <E> ImmutableList<E> construct(Object ... arrobject) {
        return ImmutableList.asImmutableList(ObjectArrays.checkElementsNotNull(arrobject));
    }

    static <E> ImmutableList<E> asImmutableList(Object[] arrobject) {
        return ImmutableList.asImmutableList(arrobject, arrobject.length);
    }

    static <E> ImmutableList<E> asImmutableList(Object[] arrobject, int n2) {
        switch (n2) {
            case 0: {
                return ImmutableList.of();
            }
            case 1: {
                SingletonImmutableList<Object> singletonImmutableList = new SingletonImmutableList<Object>(arrobject[0]);
                return singletonImmutableList;
            }
        }
        if (n2 < arrobject.length) {
            arrobject = ObjectArrays.arraysCopyOf(arrobject, n2);
        }
        return new RegularImmutableList(arrobject);
    }

    ImmutableList() {
    }

    @Override
    public UnmodifiableIterator<E> iterator() {
        return this.listIterator();
    }

    @Override
    public UnmodifiableListIterator<E> listIterator() {
        return this.listIterator(0);
    }

    @Override
    public UnmodifiableListIterator<E> listIterator(int n2) {
        return new AbstractIndexedListIterator<E>(this.size(), n2){

            @Override
            protected E get(int n2) {
                return ImmutableList.this.get(n2);
            }
        };
    }

    @Override
    public int indexOf(Object object) {
        return object == null ? -1 : Lists.indexOfImpl(this, object);
    }

    @Override
    public int lastIndexOf(Object object) {
        return object == null ? -1 : Lists.lastIndexOfImpl(this, object);
    }

    @Override
    public boolean contains(Object object) {
        return this.indexOf(object) >= 0;
    }

    @Override
    public ImmutableList<E> subList(int n2, int n3) {
        Preconditions.checkPositionIndexes(n2, n3, this.size());
        int n4 = n3 - n2;
        if (n4 == this.size()) {
            return this;
        }
        switch (n4) {
            case 0: {
                return ImmutableList.of();
            }
            case 1: {
                return ImmutableList.of(this.get(n2));
            }
        }
        return this.subListUnchecked(n2, n3);
    }

    ImmutableList<E> subListUnchecked(int n2, int n3) {
        return new SubList(n2, n3 - n2);
    }

    @Deprecated
    @Override
    public final boolean addAll(int n2, Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public final E set(int n2, E e2) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public final void add(int n2, E e2) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public final E remove(int n2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final ImmutableList<E> asList() {
        return this;
    }

    @Override
    int copyIntoArray(Object[] arrobject, int n2) {
        int n3 = this.size();
        for (int i2 = 0; i2 < n3; ++i2) {
            arrobject[n2 + i2] = this.get(i2);
        }
        return n2 + n3;
    }

    public ImmutableList<E> reverse() {
        return this.size() <= 1 ? this : new ReverseImmutableList(this);
    }

    @Override
    public boolean equals(Object object) {
        return Lists.equalsImpl(this, object);
    }

    @Override
    public int hashCode() {
        int n2 = 1;
        int n3 = this.size();
        for (int i2 = 0; i2 < n3; ++i2) {
            n2 = 31 * n2 + this.get(i2).hashCode();
            n2 = ~ (~ n2);
        }
        return n2;
    }

    public static <E> Builder<E> builder() {
        return new Builder();
    }

    public static final class Builder<E>
    extends ImmutableCollection.ArrayBasedBuilder<E> {
        public Builder() {
            this(4);
        }

        Builder(int n2) {
            super(n2);
        }

        @Override
        public Builder<E> add(E e2) {
            super.add((Object)e2);
            return this;
        }

        @Override
        public Builder<E> addAll(Iterable<? extends E> iterable) {
            super.addAll(iterable);
            return this;
        }

        @Override
        public Builder<E> addAll(Iterator<? extends E> iterator) {
            super.addAll(iterator);
            return this;
        }

        public ImmutableList<E> build() {
            return ImmutableList.asImmutableList(this.contents, this.size);
        }
    }

    private static class ReverseImmutableList<E>
    extends ImmutableList<E> {
        private final transient ImmutableList<E> forwardList;

        ReverseImmutableList(ImmutableList<E> immutableList) {
            this.forwardList = immutableList;
        }

        private int reverseIndex(int n2) {
            return this.size() - 1 - n2;
        }

        private int reversePosition(int n2) {
            return this.size() - n2;
        }

        @Override
        public ImmutableList<E> reverse() {
            return this.forwardList;
        }

        @Override
        public boolean contains(Object object) {
            return this.forwardList.contains(object);
        }

        @Override
        public int indexOf(Object object) {
            int n2 = this.forwardList.lastIndexOf(object);
            return n2 >= 0 ? this.reverseIndex(n2) : -1;
        }

        @Override
        public int lastIndexOf(Object object) {
            int n2 = this.forwardList.indexOf(object);
            return n2 >= 0 ? this.reverseIndex(n2) : -1;
        }

        @Override
        public ImmutableList<E> subList(int n2, int n3) {
            Preconditions.checkPositionIndexes(n2, n3, this.size());
            return this.forwardList.subList(this.reversePosition(n3), this.reversePosition(n2)).reverse();
        }

        @Override
        public E get(int n2) {
            Preconditions.checkElementIndex(n2, this.size());
            return this.forwardList.get(this.reverseIndex(n2));
        }

        @Override
        public int size() {
            return this.forwardList.size();
        }

        @Override
        boolean isPartialView() {
            return this.forwardList.isPartialView();
        }
    }

    class SubList
    extends ImmutableList<E> {
        final transient int offset;
        final transient int length;

        SubList(int n2, int n3) {
            this.offset = n2;
            this.length = n3;
        }

        @Override
        public int size() {
            return this.length;
        }

        @Override
        public E get(int n2) {
            Preconditions.checkElementIndex(n2, this.length);
            return ImmutableList.this.get(n2 + this.offset);
        }

        @Override
        public ImmutableList<E> subList(int n2, int n3) {
            Preconditions.checkPositionIndexes(n2, n3, this.length);
            return ImmutableList.this.subList(n2 + this.offset, n3 + this.offset);
        }

        @Override
        boolean isPartialView() {
            return true;
        }
    }

}

