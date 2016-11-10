/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.c;

import com.a.a.b.c.f;
import com.a.a.b.c.g;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.text.StringUtil;

public class d
extends g {
    private final double a;
    private final double b;
    private final double c;
    private double d;
    private double e;
    private Double f;
    private Double g;

    public d(double d2, boolean bl) {
        super(f.c, bl);
        if (Double.isNaN(d2) || Double.isInfinite(d2) || d2 <= 0.0) {
            throw new IllegalArgumentException(String.format("Argument 'length' should be positive finite number but got %s.", d2));
        }
        this.a = d2;
        this.b = d2 / 2.0;
        this.c = d2 * d2 / 12.0;
    }

    public Point2D a(Point2D point2D, double d2, double d3) {
        this.b(d2, d3);
        return new Point2D(point2D.getX() - this.f, point2D.getY() - this.g);
    }

    public Point2D b(Point2D point2D, double d2, double d3) {
        this.b(d2, d3);
        return new Point2D(point2D.getX() + this.f, point2D.getY() + this.g);
    }

    @Override
    public double d() {
        return this.b;
    }

    @Override
    public Point2D a(Point2D point2D, double d2) {
        return point2D;
    }

    @Override
    public double a(double d2) {
        return d2 * this.c;
    }

    @Override
    public String toString() {
        return StringUtil.toString((Object)this, false, "length");
    }

    private void b(double d2, double d3) {
        if (this.f == null || this.g == null || d2 != this.d || d3 != this.e) {
            if (Double.isNaN(d2) || Double.isInfinite(d2)) {
                throw new IllegalArgumentException("Argument 'angle' is not a finite number.");
            }
            if (Double.isNaN(d3) || Double.isInfinite(d3) || d3 < 1.0E-100 || d3 > 1.0) {
                throw new IllegalArgumentException("Argument 'epsilon' should be between 1.0E-100 and 1.0.");
            }
            this.d = d2;
            this.e = d3;
            if (Math.abs(this.a) < d3) {
                this.f = 0.0;
                this.g = 0.0;
            } else {
                this.f = Math.abs(1.5707963267948966 - Math.abs(d2)) < d3 ? Double.valueOf(0.0) : Double.valueOf(d.a(Math.cos(d2), d3) * this.b);
                this.g = Math.abs(3.141592653589793 - Math.abs(d2)) < d3 || Math.abs(d2) < d3 ? Double.valueOf(0.0) : Double.valueOf(d.a(Math.sin(d2), d3) * this.b);
            }
        }
    }
}

