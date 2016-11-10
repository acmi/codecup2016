/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.geometry;

import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.text.StringUtil;
import org.apache.commons.lang3.ArrayUtils;

public class Circle2D {
    public static final Circle2D[] EMPTY_CIRCLE_ARRAY = new Circle2D[0];
    private final double a;
    private final double b;
    private final double c;
    private final double squaredRadius;
    private final double radius;

    public Circle2D(Point2D point2D, double d2) {
        if (d2 < 0.0) {
            throw new IllegalArgumentException("Argument 'radius' is negative.");
        }
        this.squaredRadius = d2 * d2;
        this.radius = d2;
        this.a = -2.0 * point2D.getX();
        this.b = -2.0 * point2D.getY();
        this.c = (this.a * this.a + this.b * this.b) / 4.0 - this.squaredRadius;
    }

    public double getA() {
        return this.a;
    }

    public double getB() {
        return this.b;
    }

    public double getC() {
        return this.c;
    }

    public double[] getXs(double d2, double d3) {
        double d4 = this.a * this.a - 4.0 * (d2 * d2 + this.b * d2 + this.c);
        if (d4 < - d3) {
            return ArrayUtils.EMPTY_DOUBLE_ARRAY;
        }
        if (Math.abs(d4) <= d3) {
            return new double[]{(- this.a) / 2.0};
        }
        double d5 = Math.sqrt(d4);
        return new double[]{(- d5 - this.a) / 2.0, (d5 - this.a) / 2.0};
    }

    public double[] getYs(double d2, double d3) {
        double d4 = this.b * this.b - 4.0 * (d2 * d2 + this.a * d2 + this.c);
        if (d4 < - d3) {
            return ArrayUtils.EMPTY_DOUBLE_ARRAY;
        }
        if (Math.abs(d4) <= d3) {
            return new double[]{(- this.b) / 2.0};
        }
        double d5 = Math.sqrt(d4);
        return new double[]{(- d5 - this.b) / 2.0, (d5 - this.b) / 2.0};
    }

    public String toString() {
        return StringUtil.toString((Object)this, false, "a", "b", "c");
    }
}

