/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import java.util.Comparator;

interface SortedIterable<T>
extends Iterable<T> {
    public Comparator<? super T> comparator();
}

