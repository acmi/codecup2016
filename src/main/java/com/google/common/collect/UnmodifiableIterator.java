/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import java.util.Iterator;

public abstract class UnmodifiableIterator<E>
implements Iterator<E> {
    protected UnmodifiableIterator() {
    }

    @Deprecated
    @Override
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}

