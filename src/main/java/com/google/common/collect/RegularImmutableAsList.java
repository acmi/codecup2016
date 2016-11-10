/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  com.google.j2objc.annotations.Weak
 */
package com.google.common.collect;

import com.google.common.collect.ImmutableAsList;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableListIterator;
import com.google.j2objc.annotations.Weak;
import java.util.ListIterator;

class RegularImmutableAsList<E>
extends ImmutableAsList<E> {
    @Weak
    private final ImmutableCollection<E> delegate;
    private final ImmutableList<? extends E> delegateList;

    RegularImmutableAsList(ImmutableCollection<E> immutableCollection, ImmutableList<? extends E> immutableList) {
        this.delegate = immutableCollection;
        this.delegateList = immutableList;
    }

    RegularImmutableAsList(ImmutableCollection<E> immutableCollection, Object[] arrobject) {
        this(immutableCollection, ImmutableList.asImmutableList(arrobject));
    }

    @Override
    ImmutableCollection<E> delegateCollection() {
        return this.delegate;
    }

    @Override
    public UnmodifiableListIterator<E> listIterator(int n2) {
        return this.delegateList.listIterator(n2);
    }

    @Override
    int copyIntoArray(Object[] arrobject, int n2) {
        return this.delegateList.copyIntoArray(arrobject, n2);
    }

    @Override
    public E get(int n2) {
        return this.delegateList.get(n2);
    }
}

