/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.collect.UnmodifiableIterator;
import java.util.ListIterator;

public abstract class UnmodifiableListIterator<E>
extends UnmodifiableIterator<E>
implements ListIterator<E> {
    protected UnmodifiableListIterator() {
    }

    @Deprecated
    @Override
    public final void add(E e2) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public final void set(E e2) {
        throw new UnsupportedOperationException();
    }
}

