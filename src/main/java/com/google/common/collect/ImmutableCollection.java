/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.RegularImmutableAsList;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

public abstract class ImmutableCollection<E>
extends AbstractCollection<E>
implements Serializable {
    private transient ImmutableList<E> asList;

    ImmutableCollection() {
    }

    @Override
    public abstract UnmodifiableIterator<E> iterator();

    @Override
    public final Object[] toArray() {
        int n2 = this.size();
        if (n2 == 0) {
            return ObjectArrays.EMPTY_ARRAY;
        }
        Object[] arrobject = new Object[n2];
        this.copyIntoArray(arrobject, 0);
        return arrobject;
    }

    @Override
    public final <T> T[] toArray(T[] arrT) {
        Preconditions.checkNotNull(arrT);
        int n2 = this.size();
        if (arrT.length < n2) {
            arrT = ObjectArrays.newArray(arrT, n2);
        } else if (arrT.length > n2) {
            arrT[n2] = null;
        }
        this.copyIntoArray(arrT, 0);
        return arrT;
    }

    @Override
    public abstract boolean contains(Object var1);

    @Deprecated
    @Override
    public final boolean add(E e2) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public final boolean remove(Object object) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public final boolean addAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public final boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public final boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    public ImmutableList<E> asList() {
        ImmutableList<E> immutableList = this.asList;
        ImmutableList<E> immutableList2 = immutableList == null ? (this.asList = this.createAsList()) : immutableList;
        return immutableList2;
    }

    ImmutableList<E> createAsList() {
        switch (this.size()) {
            case 0: {
                return ImmutableList.of();
            }
            case 1: {
                return ImmutableList.of(this.iterator().next());
            }
        }
        return new RegularImmutableAsList<E>(this, this.toArray());
    }

    abstract boolean isPartialView();

    int copyIntoArray(Object[] arrobject, int n2) {
        for (E e2 : this) {
            arrobject[n2++] = e2;
        }
        return n2;
    }

    static abstract class ArrayBasedBuilder<E>
    extends Builder<E> {
        Object[] contents;
        int size;

        ArrayBasedBuilder(int n2) {
            CollectPreconditions.checkNonnegative(n2, "initialCapacity");
            this.contents = new Object[n2];
            this.size = 0;
        }

        private void ensureCapacity(int n2) {
            if (this.contents.length < n2) {
                this.contents = ObjectArrays.arraysCopyOf(this.contents, ArrayBasedBuilder.expandedCapacity(this.contents.length, n2));
            }
        }

        @Override
        public ArrayBasedBuilder<E> add(E e2) {
            Preconditions.checkNotNull(e2);
            this.ensureCapacity(this.size + 1);
            this.contents[this.size++] = e2;
            return this;
        }

        @Override
        public Builder<E> addAll(Iterable<? extends E> iterable) {
            if (iterable instanceof Collection) {
                Collection collection = (Collection)iterable;
                this.ensureCapacity(this.size + collection.size());
            }
            super.addAll(iterable);
            return this;
        }
    }

    public static abstract class Builder<E> {
        static int expandedCapacity(int n2, int n3) {
            if (n3 < 0) {
                throw new AssertionError((Object)"cannot store more than MAX_VALUE elements");
            }
            int n4 = n2 + (n2 >> 1) + 1;
            if (n4 < n3) {
                n4 = Integer.highestOneBit(n3 - 1) << 1;
            }
            if (n4 < 0) {
                n4 = Integer.MAX_VALUE;
            }
            return n4;
        }

        Builder() {
        }

        public abstract Builder<E> add(E var1);

        public Builder<E> addAll(Iterable<? extends E> iterable) {
            for (E e2 : iterable) {
                this.add(e2);
            }
            return this;
        }

        public Builder<E> addAll(Iterator<? extends E> iterator) {
            while (iterator.hasNext()) {
                this.add(iterator.next());
            }
            return this;
        }
    }

}

