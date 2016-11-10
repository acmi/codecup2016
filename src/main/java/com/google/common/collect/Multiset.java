/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import java.util.Collection;
import java.util.Set;

public interface Multiset<E>
extends Collection<E> {
    public int count(Object var1);

    public int add(E var1, int var2);

    public int remove(Object var1, int var2);

    public int setCount(E var1, int var2);

    public boolean setCount(E var1, int var2, int var3);

    public Set<E> elementSet();

    public Set<Entry<E>> entrySet();

    @Override
    public boolean contains(Object var1);

    @Override
    public boolean containsAll(Collection<?> var1);

    @Override
    public boolean add(E var1);

    @Override
    public boolean remove(Object var1);

    public static interface Entry<E> {
        public E getElement();

        public int getCount();
    }

}

