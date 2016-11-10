/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.geometry;

import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.math.NumberUtil;
import com.codeforces.commons.pair.DoublePair;
import com.codeforces.commons.text.StringUtil;

public class Point2D
extends DoublePair {
    public static final Point2D[] EMPTY_POINT_ARRAY = new Point2D[0];

    public Point2D(double d2, double d3) {
        super(d2, d3);
    }

    public Point2D(Point2D point2D) {
        super(point2D.getX(), point2D.getY());
    }

    public double getX() {
        return this.getFirst();
    }

    public void setX(double d2) {
        this.setFirst(d2);
    }

    public double getY() {
        return this.getSecond();
    }

    public void setY(double d2) {
        this.setSecond(d2);
    }

    public Point2D add(Vector2D vector2D) {
        this.setX(this.getX() + vector2D.getX());
        this.setY(this.getY() + vector2D.getY());
        return this;
    }

    public Point2D add(double d2, double d3) {
        this.setX(this.getX() + d2);
        this.setY(this.getY() + d3);
        return this;
    }

    public Point2D subtract(Vector2D vector2D) {
        this.setX(this.getX() - vector2D.getX());
        this.setY(this.getY() - vector2D.getY());
        return this;
    }

    public Point2D subtract(double d2, double d3) {
        this.setX(this.getX() - d2);
        this.setY(this.getY() - d3);
        return this;
    }

    public Vector2D subtract(Point2D point2D) {
        return new Vector2D(point2D.getX() - this.getX(), point2D.getY() - this.getY());
    }

    public double getDistanceTo(Point2D point2D) {
        return Math.hypot(this.getX() - point2D.getX(), this.getY() - point2D.getY());
    }

    public double getDistanceTo(double d2, double d3) {
        return Math.hypot(this.getX() - d2, this.getY() - d3);
    }

    public double getSquaredDistanceTo(Point2D point2D) {
        return Math.sumSqr(this.getX() - point2D.getX(), this.getY() - point2D.getY());
    }

    public double getSquaredDistanceTo(double d2, double d3) {
        return Math.sumSqr(this.getX() - d2, this.getY() - d3);
    }

    public Point2D copy() {
        return new Point2D(this);
    }

    public boolean nearlyEquals(Point2D point2D, double d2) {
        return point2D != null && NumberUtil.nearlyEquals(this.getX(), point2D.getX(), d2) && NumberUtil.nearlyEquals(this.getY(), point2D.getY(), d2);
    }

    @Override
    public String toString() {
        return StringUtil.toString((Object)this, false, "x", "y");
    }
}

