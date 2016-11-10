/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.c;

import com.a.a.b.c.c;
import com.a.a.b.c.f;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.text.StringUtil;

public class b
extends c {
    private final double a;
    private final double b;

    public b(double d2) {
        super(f.a);
        if (Double.isNaN(d2) || Double.isInfinite(d2) || d2 <= 0.0) {
            throw new IllegalArgumentException(String.format("Argument 'radius' should be positive finite number but got %s.", d2));
        }
        this.a = d2;
        this.b = d2 * d2 / 2.0;
    }

    public double a() {
        return this.a;
    }

    @Override
    public double d() {
        return this.a;
    }

    @Override
    public Point2D a(Point2D point2D, double d2) {
        return point2D;
    }

    @Override
    public double a(double d2) {
        return d2 * this.b;
    }

    @Override
    public String toString() {
        return StringUtil.toString((Object)this, false, "radius");
    }
}

