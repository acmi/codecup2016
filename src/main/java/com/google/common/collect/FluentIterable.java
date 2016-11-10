/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.collect.Iterables;

public abstract class FluentIterable<E>
implements Iterable<E> {
    private final Iterable<E> iterable;

    protected FluentIterable() {
        this.iterable = this;
    }

    public String toString() {
        return Iterables.toString(this.iterable);
    }
}

