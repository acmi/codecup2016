/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.c;

import com.a.a.b.c.c;
import com.a.a.b.c.f;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.text.StringUtil;

public class e
extends c {
    private final double a;
    private final double b;
    private final double c;
    private final double d;
    private final double e;
    private final double f;

    public e(double d2, double d3) {
        super(f.b);
        if (Double.isNaN(d2) || Double.isInfinite(d2) || d2 <= 0.0) {
            throw new IllegalArgumentException(String.format("Argument 'width' should be positive finite number but got %s.", d2));
        }
        if (Double.isNaN(d3) || Double.isInfinite(d3) || d3 <= 0.0) {
            throw new IllegalArgumentException(String.format("Argument 'height' should be positive finite number but got %s.", d3));
        }
        this.a = d2;
        this.b = d3;
        this.c = d2 / 2.0;
        this.d = d3 / 2.0;
        this.e = Math.hypot(d2, d3) / 2.0;
        this.f = Math.sumSqr(d2, d3) / 12.0;
    }

    public double a() {
        return this.a;
    }

    public double b() {
        return this.b;
    }

    public Point2D[] a(Point2D point2D, double d2, double d3) {
        if (Double.isNaN(d2) || Double.isInfinite(d2)) {
            throw new IllegalArgumentException("Argument 'angle' is not a finite number.");
        }
        if (Double.isNaN(d3) || Double.isInfinite(d3) || d3 < 1.0E-100 || d3 > 1.0) {
            throw new IllegalArgumentException("Argument 'epsilon' should be between 1.0E-100 and 1.0.");
        }
        double d4 = e.a(Math.sin(d2), d3);
        double d5 = e.a(Math.cos(d2), d3);
        double d6 = d5 * this.c;
        double d7 = d4 * this.c;
        double d8 = d4 * this.d;
        double d9 = (- d5) * this.d;
        return new Point2D[]{new Point2D(point2D.getX() - d6 + d8, point2D.getY() - d7 + d9), new Point2D(point2D.getX() + d6 + d8, point2D.getY() + d7 + d9), new Point2D(point2D.getX() + d6 - d8, point2D.getY() + d7 - d9), new Point2D(point2D.getX() - d6 - d8, point2D.getY() - d7 - d9)};
    }

    @Override
    public double d() {
        return this.e;
    }

    @Override
    public Point2D a(Point2D point2D, double d2) {
        return point2D;
    }

    @Override
    public double a(double d2) {
        return d2 * this.f;
    }

    @Override
    public String toString() {
        return StringUtil.toString((Object)this, false, "width", "height");
    }
}

