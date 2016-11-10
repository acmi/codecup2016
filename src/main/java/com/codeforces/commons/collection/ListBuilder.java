/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.collection;

import java.util.ArrayList;
import java.util.List;

public class ListBuilder<E> {
    private final List<E> list = new ArrayList();

    public ListBuilder<E> add(E e2) {
        this.list.add(e2);
        return this;
    }

    public List<E> build() {
        return this.list;
    }
}

