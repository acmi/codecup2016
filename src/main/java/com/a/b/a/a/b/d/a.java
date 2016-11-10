/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.d;

import com.a.b.a.a.b.d.d;
import com.a.b.a.a.c.l;
import com.a.c.a.c;
import com.google.common.base.Preconditions;

public abstract class a
extends d {
    private double a;
    private double b;

    protected a(c c2, l l2, double d2, double d3, double d4) {
        super(c2, l2, d2, d3);
        Preconditions.checkArgument(!Double.isNaN(d4) && !Double.isInfinite(d4) && d4 > 0.0);
        this.a = d4;
        this.b = d4;
    }

    public final double h() {
        return this.a;
    }

    public final void a(double d2) {
        this.a = d2;
    }

    public double i() {
        return this.b;
    }
}

