/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.Hashing;
import com.google.common.collect.ImmutableAsList;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableEnumSet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.RegularImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.SingletonImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

public abstract class ImmutableSet<E>
extends ImmutableCollection<E>
implements Set<E> {
    public static <E> ImmutableSet<E> of() {
        return RegularImmutableSet.EMPTY;
    }

    public static <E> ImmutableSet<E> of(E e2) {
        return new SingletonImmutableSet<E>(e2);
    }

    public static <E> ImmutableSet<E> of(E e2, E e3, E e4) {
        return ImmutableSet.construct(3, e2, e3, e4);
    }

    public static /* varargs */ <E> ImmutableSet<E> of(E e2, E e3, E e4, E e5, E e6, E e7, E ... arrE) {
        int n2 = 6;
        Object[] arrobject = new Object[6 + arrE.length];
        arrobject[0] = e2;
        arrobject[1] = e3;
        arrobject[2] = e4;
        arrobject[3] = e5;
        arrobject[4] = e6;
        arrobject[5] = e7;
        System.arraycopy(arrE, 0, arrobject, 6, arrE.length);
        return ImmutableSet.construct(arrobject.length, arrobject);
    }

    private static /* varargs */ <E> ImmutableSet<E> construct(int n2, Object ... arrobject) {
        switch (n2) {
            case 0: {
                return ImmutableSet.of();
            }
            case 1: {
                Object object = arrobject[0];
                return ImmutableSet.of(object);
            }
        }
        int n3 = ImmutableSet.chooseTableSize(n2);
        Object[] arrobject2 = new Object[n3];
        int n4 = n3 - 1;
        int n5 = 0;
        int n6 = 0;
        block4 : for (int i2 = 0; i2 < n2; ++i2) {
            Object object = ObjectArrays.checkElementNotNull(arrobject[i2], i2);
            int n7 = object.hashCode();
            int n8 = Hashing.smear(n7);
            do {
                Object object2;
                int n9;
                if ((object2 = arrobject2[n9 = n8 & n4]) == null) {
                    arrobject[n6++] = object;
                    arrobject2[n9] = object;
                    n5 += n7;
                    continue block4;
                }
                if (object2.equals(object)) continue block4;
                ++n8;
            } while (true);
        }
        Arrays.fill(arrobject, n6, n2, null);
        if (n6 == 1) {
            Object object = arrobject[0];
            return new SingletonImmutableSet<Object>(object, n5);
        }
        if (n3 != ImmutableSet.chooseTableSize(n6)) {
            return ImmutableSet.construct(n6, arrobject);
        }
        Object[] arrobject3 = n6 < arrobject.length ? ObjectArrays.arraysCopyOf(arrobject, n6) : arrobject;
        return new RegularImmutableSet(arrobject3, n5, arrobject2, n4);
    }

    static int chooseTableSize(int n2) {
        if (n2 < 751619276) {
            int n3 = Integer.highestOneBit(n2 - 1) << 1;
            while ((double)n3 * 0.7 < (double)n2) {
                n3 <<= 1;
            }
            return n3;
        }
        Preconditions.checkArgument(n2 < 1073741824, "collection too large");
        return 1073741824;
    }

    public static <E> ImmutableSet<E> copyOf(Collection<? extends E> collection) {
        Object[] arrobject;
        if (collection instanceof ImmutableSet && !(collection instanceof ImmutableSortedSet)) {
            arrobject = (Object[])collection;
            if (!arrobject.isPartialView()) {
                return arrobject;
            }
        } else if (collection instanceof EnumSet) {
            return ImmutableSet.copyOfEnumSet((EnumSet)collection);
        }
        arrobject = collection.toArray();
        return ImmutableSet.construct(arrobject.length, arrobject);
    }

    public static <E> ImmutableSet<E> copyOf(Iterable<? extends E> iterable) {
        return iterable instanceof Collection ? ImmutableSet.copyOf((Collection)iterable) : ImmutableSet.copyOf(iterable.iterator());
    }

    public static <E> ImmutableSet<E> copyOf(Iterator<? extends E> iterator) {
        if (!iterator.hasNext()) {
            return ImmutableSet.of();
        }
        E e2 = iterator.next();
        if (!iterator.hasNext()) {
            return ImmutableSet.of(e2);
        }
        return new Builder().add((Object)e2).addAll(iterator).build();
    }

    public static <E> ImmutableSet<E> copyOf(E[] arrE) {
        switch (arrE.length) {
            case 0: {
                return ImmutableSet.of();
            }
            case 1: {
                return ImmutableSet.of(arrE[0]);
            }
        }
        return ImmutableSet.construct(arrE.length, (Object[])arrE.clone());
    }

    private static ImmutableSet copyOfEnumSet(EnumSet enumSet) {
        return ImmutableEnumSet.asImmutable(EnumSet.copyOf(enumSet));
    }

    ImmutableSet() {
    }

    boolean isHashCodeFast() {
        return false;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof ImmutableSet && this.isHashCodeFast() && ((ImmutableSet)object).isHashCodeFast() && this.hashCode() != object.hashCode()) {
            return false;
        }
        return Sets.equalsImpl(this, object);
    }

    @Override
    public int hashCode() {
        return Sets.hashCodeImpl(this);
    }

    @Override
    public abstract UnmodifiableIterator<E> iterator();

    public static <E> Builder<E> builder() {
        return new Builder<E>();
    }

    public static class Builder<E>
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

        public ImmutableSet<E> build() {
            ImmutableSet immutableSet = ImmutableSet.construct(this.size, this.contents);
            this.size = immutableSet.size();
            return immutableSet;
        }
    }

    static abstract class Indexed<E>
    extends ImmutableSet<E> {
        Indexed() {
        }

        abstract E get(int var1);

        @Override
        public UnmodifiableIterator<E> iterator() {
            return this.asList().iterator();
        }

        @Override
        ImmutableList<E> createAsList() {
            return new ImmutableAsList<E>(){

                @Override
                public E get(int n2) {
                    return Indexed.this.get(n2);
                }

                @Override
                Indexed<E> delegateCollection() {
                    return Indexed.this;
                }
            };
        }

    }

}

