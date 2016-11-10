/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.d;

import com.a.b.a.a.b.d.b;
import com.a.b.a.a.c.l;
import com.a.c.c;
import com.codeforces.commons.math.Math;
import com.google.common.base.Preconditions;

public abstract class d
extends b {
    private final double a;
    private final double b;

    protected d(com.a.c.a.c c2, l l2, double d2, double d3) {
        super(c2, l2, d2);
        Preconditions.checkArgument(!Double.isNaN(d3) && !Double.isInfinite(d3) && d3 >= 0.0);
        this.a = d3;
        this.b = d3 * d3;
    }

    public final double q() {
        return this.a;
    }

    public double r() {
        return this.b;
    }

    public final boolean d(com.a.b.d d2) {
        c c2 = this.b();
        c c3 = d2.b();
        return Math.sumSqr(c3.c() - c2.c(), c3.d() - c2.d()) <= this.b;
    }

    public static boolean a(com.a.b.d d2, d[] arrd) {
        for (int i2 = arrd.length - 1; i2 >= 0; --i2) {
            d d3 = arrd[i2];
            if (!d3.d(d2)) continue;
            return true;
        }
        return false;
    }
}

