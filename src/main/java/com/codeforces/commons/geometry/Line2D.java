/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.geometry;

import com.codeforces.commons.geometry.Circle2D;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.text.StringUtil;

public class Line2D {
    public static final Line2D[] EMPTY_LINE_ARRAY = new Line2D[0];
    private final double a;
    private final double b;
    private final double c;
    private final double pseudoLength;

    public Line2D(double d2, double d3, double d4) {
        this.a = d2;
        this.b = d3;
        this.c = d4;
        this.pseudoLength = Math.hypot(this.a, this.b);
    }

    public Line2D[] getParallelLines(double d2) {
        double d3 = d2 * this.pseudoLength;
        return new Line2D[]{new Line2D(this.a, this.b, this.c + d3), new Line2D(this.a, this.b, this.c - d3)};
    }

    public Line2D getParallelLine(double d2, double d3) {
        double d4 = this.a * d2 + this.b * d3 + this.c;
        return new Line2D(this.a, this.b, this.c - d4);
    }

    public Line2D getParallelLine(Point2D point2D) {
        return this.getParallelLine(point2D.getX(), point2D.getY());
    }

    public double getDistanceFrom(double d2, double d3) {
        return Math.abs((this.a * d2 + this.b * d3 + this.c) / this.pseudoLength);
    }

    public double getDistanceFrom(Point2D point2D) {
        return this.getDistanceFrom(point2D.getX(), point2D.getY());
    }

    public double getDistanceFrom(Line2D line2D, double d2) {
        if (this.getIntersectionPoint(line2D, d2) != null) {
            return Double.NaN;
        }
        return Math.abs(this.c - line2D.c) / this.pseudoLength;
    }

    public double getSignedDistanceFrom(double d2, double d3) {
        return (this.a * d2 + this.b * d3 + this.c) / this.pseudoLength;
    }

    public double getSignedDistanceFrom(Point2D point2D) {
        return this.getSignedDistanceFrom(point2D.getX(), point2D.getY());
    }

    public Vector2D getUnitNormal() {
        return new Vector2D(this.a / this.pseudoLength, this.b / this.pseudoLength);
    }

    public Vector2D getUnitNormalFrom(double d2, double d3, double d4) {
        double d5 = this.getSignedDistanceFrom(d2, d3);
        if (d5 < - d4) {
            return new Vector2D(this.a / this.pseudoLength, this.b / this.pseudoLength);
        }
        if (d5 > d4) {
            return new Vector2D((- this.a) / this.pseudoLength, (- this.b) / this.pseudoLength);
        }
        throw new IllegalArgumentException(String.format("Point {x=%s, y=%s} is on the %s.", d2, d3, this));
    }

    public Vector2D getUnitNormalFrom(Point2D point2D, double d2) {
        return this.getUnitNormalFrom(point2D.getX(), point2D.getY(), d2);
    }

    public Vector2D getUnitNormalFrom(Point2D point2D) {
        return this.getUnitNormalFrom(point2D.getX(), point2D.getY(), 1.0E-6);
    }

    public Point2D getProjectionOf(double d2, double d3, double d4) {
        double d5 = this.getDistanceFrom(d2, d3);
        if (d5 <= d4) {
            return new Point2D(d2, d3);
        }
        Vector2D vector2D = this.getUnitNormalFrom(d2, d3, d4);
        return new Point2D(d2 + vector2D.getX() * d5, d3 + vector2D.getY() * d5);
    }

    public Point2D getProjectionOf(Point2D point2D, double d2) {
        return this.getProjectionOf(point2D.getX(), point2D.getY(), d2);
    }

    public Point2D getProjectionOf(Point2D point2D) {
        return this.getProjectionOf(point2D.getX(), point2D.getY(), 1.0E-6);
    }

    public Point2D getIntersectionPoint(Line2D line2D, double d2) {
        double d3 = this.a * line2D.b - line2D.a * this.b;
        return Math.abs(d3) <= d2 ? null : new Point2D((this.b * line2D.c - line2D.b * this.c) / d3, (line2D.a * this.c - this.a * line2D.c) / d3);
    }

    public Point2D[] getIntersectionPoints(Circle2D circle2D, double d2) {
        double d3;
        double d4;
        double d5;
        Point2D[] arrpoint2D;
        double d6;
        double d7 = this.a * this.a;
        double d8 = this.b * this.b;
        double d9 = this.c * this.c;
        double d10 = this.a * this.b;
        double d11 = this.a * this.c;
        double d12 = this.b * this.c;
        double d13 = circle2D.getA();
        double d14 = 2.0 * d11 + d8 * d13 - d10 * (d5 = circle2D.getB());
        double d15 = d14 * d14 - 4.0 * (d4 = d7 + d8) * (d3 = d8 * (d6 = circle2D.getC()) - d12 * d5 + d9);
        if (d15 < - d2) {
            return Point2D.EMPTY_POINT_ARRAY;
        }
        double d16 = 2.0 * d12 + d7 * d5 - d10 * d13;
        double d17 = d7 * d6 - d11 * d13 + d9;
        double d18 = d16 * d16 - 4.0 * d4 * d17;
        if (d18 < - d2) {
            return Point2D.EMPTY_POINT_ARRAY;
        }
        if (Math.abs(d15) <= d2 && Math.abs(d18) <= d2) {
            return new Point2D[]{new Point2D((- d14) / d4 / 2.0, (- d16) / d4 / 2.0)};
        }
        double d19 = Math.sqrt(d15);
        double d20 = Math.sqrt(d18);
        if (d10 <= 0.0) {
            Point2D[] arrpoint2D2 = new Point2D[2];
            arrpoint2D2[0] = new Point2D((- d14 - d19) / d4 / 2.0, (- d16 - d20) / d4 / 2.0);
            arrpoint2D = arrpoint2D2;
            arrpoint2D2[1] = new Point2D((- d14 + d19) / d4 / 2.0, (- d16 + d20) / d4 / 2.0);
        } else {
            Point2D[] arrpoint2D3 = new Point2D[2];
            arrpoint2D3[0] = new Point2D((- d14 - d19) / d4 / 2.0, (- d16 + d20) / d4 / 2.0);
            arrpoint2D = arrpoint2D3;
            arrpoint2D3[1] = new Point2D((- d14 + d19) / d4 / 2.0, (- d16 - d20) / d4 / 2.0);
        }
        return arrpoint2D;
    }

    public String toString() {
        return StringUtil.toString((Object)this, false, "a", "b", "c");
    }

    public static Line2D getLineByTwoPoints(double d2, double d3, double d4, double d5) {
        return new Line2D(d5 - d3, d2 - d4, (d3 - d5) * d2 + (d4 - d2) * d3);
    }

    public static Line2D getLineByTwoPoints(Point2D point2D, Point2D point2D2) {
        return Line2D.getLineByTwoPoints(point2D.getX(), point2D.getY(), point2D2.getX(), point2D2.getY());
    }
}

