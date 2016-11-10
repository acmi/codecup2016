/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.e.a;

import com.a.b.a.a.c.D;
import com.a.b.a.a.c.E;
import com.a.b.a.a.c.m;
import com.a.b.a.a.c.s;
import com.a.b.a.a.e.a.e;

public class b
implements e {
    private final int a;

    public b(int n2) {
        this.a = n2;
    }

    @Override
    public int a() {
        return 1;
    }

    @Override
    public void a(m m2) {
    }

    @Override
    public s[] a(D[] arrd, E e2) {
        if (arrd.length != this.a) {
            throw new IllegalArgumentException(String.format("Strategy adapter '%s' got %d wizards while team size is %d.", this.getClass().getSimpleName(), arrd.length, this.a));
        }
        return new s[this.a];
    }

    @Override
    public void close() {
    }
}

