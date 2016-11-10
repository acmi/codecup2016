/*
 * Decompiled with CFR 0_119.
 */
package com.a.b;

import com.a.b.d;
import com.a.b.e;
import com.a.b.g;

public abstract class f<A extends d, B extends d> {
    public boolean beforeCollision(g g2, A a2, B b2) {
        return true;
    }

    public boolean beforeResolvingCollision(e<A, B> e2) {
        return true;
    }

    public void afterCollision(e<A, B> e2) {
    }
}

