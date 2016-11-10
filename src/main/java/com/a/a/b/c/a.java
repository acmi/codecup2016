/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.c;

import com.a.a.b.c.f;
import com.a.a.b.c.g;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.text.StringUtil;

public class a
extends g {
    private final double a;
    private final double b;
    private final double c;

    public a(double d2, double d3, double d4, boolean bl) {
        super(f.d, bl);
        if (Double.isNaN(d2) || Double.isInfinite(d2) || d2 <= 0.0) {
            throw new IllegalArgumentException(String.format("Argument 'radius' should be a positive finite number but got %s.", d2));
        }
        if (Double.isNaN(d3) || Double.isInfinite(d3)) {
            throw new IllegalArgumentException(String.format("Argument 'angle' should be a finite number but got %s.", d3));
        }
        if (Double.isNaN(d4) || Double.isInfinite(d4) || d4 <= 0.0 || d4 > 6.283185307179586) {
            throw new IllegalArgumentException(String.format("Argument 'sector' should be between 0.0 exclusive and 2 * PI inclusive but got %s.", d4));
        }
        this.b = com.a.a.b.f.a.a(d3);
        this.a = d2;
        this.c = d4;
    }

    public double a() {
        return this.a;
    }

    public double b() {
        return this.b;
    }

    public double c() {
        return this.c;
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
        if (Double.isInfinite(d2) && d2 != Double.NEGATIVE_INFINITY) {
            return d2;
        }
        throw new IllegalArgumentException("Arc form is only supported for static bodies.");
    }

    @Override
    public String toString() {
        return StringUtil.toString((Object)this, false, "radius", "angle", "sector");
    }
}

