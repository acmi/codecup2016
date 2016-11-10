/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.a;

import com.a.a.b.a.c;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;

class e
extends UnmodifiableIterator<E> {
    final /* synthetic */ Iterator a;
    final /* synthetic */ c.a b;

    e(c.a a2, Iterator iterator) {
        this.b = a2;
        this.a = iterator;
    }

    @Override
    public boolean hasNext() {
        return this.a.hasNext();
    }

    @Override
    public E next() {
        return this.a.next();
    }
}

