/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.d.b;

import com.a.b.a.a.a.b;
import com.a.b.a.a.b.d.b.a;
import com.a.b.a.a.c.e;
import com.a.b.a.a.c.l;
import com.a.b.a.a.c.n;
import com.codeforces.commons.geometry.Point2D;

public final class c
extends a {
    public c(double d2, double d3, l l2, n n2, b b2) {
        super(d2, d3, 50.0, l2, e.GUARDIAN_TOWER, b2.r() ? 1000.0 : 500.0, 600.0, 600.0, 24, 240, n2);
    }

    public c(Point2D point2D, l l2, n n2, b b2) {
        this(point2D.getX(), point2D.getY(), l2, n2, b2);
    }
}

