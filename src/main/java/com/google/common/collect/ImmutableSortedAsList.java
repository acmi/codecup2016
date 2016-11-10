/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.RegularImmutableAsList;
import com.google.common.collect.RegularImmutableSortedSet;
import com.google.common.collect.SortedIterable;
import java.util.Comparator;

final class ImmutableSortedAsList<E>
extends RegularImmutableAsList<E>
implements SortedIterable<E> {
    ImmutableSortedAsList(ImmutableSortedSet<E> immutableSortedSet, ImmutableList<E> immutableList) {
        super(immutableSortedSet, immutableList);
    }

    @Override
    ImmutableSortedSet<E> delegateCollection() {
        return (ImmutableSortedSet)super.delegateCollection();
    }

    @Override
    public Comparator<? super E> comparator() {
        return this.delegateCollection().comparator();
    }

    @Override
    public int indexOf(Object object) {
        int n2 = this.delegateCollection().indexOf(object);
        return n2 >= 0 && this.get(n2).equals(object) ? n2 : -1;
    }

    @Override
    public int lastIndexOf(Object object) {
        return this.indexOf(object);
    }

    @Override
    public boolean contains(Object object) {
        return this.indexOf(object) >= 0;
    }

    @Override
    ImmutableList<E> subListUnchecked(int n2, int n3) {
        ImmutableList immutableList = super.subListUnchecked(n2, n3);
        return new RegularImmutableSortedSet(immutableList, this.comparator()).asList();
    }
}

