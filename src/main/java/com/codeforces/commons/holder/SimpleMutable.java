/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.holder;

import com.codeforces.commons.holder.Mutable;

public class SimpleMutable<T>
extends Mutable<T> {
    private T value;

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public T set(T t2) {
        this.value = t2;
        return this.value;
    }
}

