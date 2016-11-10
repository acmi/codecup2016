/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import java.util.Iterator;

public interface PeekingIterator<E>
extends Iterator<E> {
    public E peek();

    @Override
    public E next();
}

