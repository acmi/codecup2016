/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.geometry;

import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.math.NumberUtil;
import com.codeforces.commons.pair.DoublePair;
import com.codeforces.commons.text.StringUtil;
import org.apache.commons.math3.util.MathArrays;

public class Vector2D
extends DoublePair {
    public static final Vector2D[] EMPTY_VECTOR_ARRAY = new Vector2D[0];

    public Vector2D(double d2, double d3) {
        super(d2, d3);
    }

    public Vector2D(double d2, double d3, double d4, double d5) {
        super(d4 - d2, d5 - d3);
    }

    public Vector2D(Point2D point2D, Point2D point2D2) {
        super(point2D2.getX() - point2D.getX(), point2D2.getY() - point2D.getY());
    }

    public Vector2D(Vector2D vector2D) {
        super(vector2D.getX(), vector2D.getY());
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

    public Vector2D add(Vector2D vector2D) {
        this.setX(this.getX() + vector2D.getX());
        this.setY(this.getY() + vector2D.getY());
        return this;
    }

    public Vector2D add(double d2, double d3) {
        this.setX(this.getX() + d2);
        this.setY(this.getY() + d3);
        return this;
    }

    public Vector2D subtract(Vector2D vector2D) {
        this.setX(this.getX() - vector2D.getX());
        this.setY(this.getY() - vector2D.getY());
        return this;
    }

    public Vector2D multiply(double d2) {
        this.setX(d2 * this.getX());
        this.setY(d2 * this.getY());
        return this;
    }

    public Vector2D rotate(double d2) {
        double d3 = Math.cos(d2);
        double d4 = Math.sin(d2);
        double d5 = this.getX();
        double d6 = this.getY();
        this.setX(d5 * d3 - d6 * d4);
        this.setY(d5 * d4 + d6 * d3);
        return this;
    }

    public Vector2D rotateHalfPi() {
        double d2 = this.getX();
        double d3 = this.getY();
        this.setX(- d3);
        this.setY(d2);
        return this;
    }

    public double dotProduct(Vector2D vector2D) {
        return MathArrays.linearCombination(this.getX(), vector2D.getX(), this.getY(), vector2D.getY());
    }

    public Vector2D negate() {
        this.setX(- this.getX());
        this.setY(- this.getY());
        return this;
    }

    public Vector2D normalize() {
        double d2 = this.getLength();
        if (d2 == 0.0) {
            throw new IllegalStateException("Can't set angle of zero-width vector.");
        }
        this.setX(this.getX() / d2);
        this.setY(this.getY() / d2);
        return this;
    }

    public double getAngle() {
        return Math.atan2(this.getY(), this.getX());
    }

    public Vector2D setAngle(double d2) {
        double d3 = this.getLength();
        if (d3 == 0.0) {
            throw new IllegalStateException("Can't set angle of zero-width vector.");
        }
        this.setX(Math.cos(d2) * d3);
        this.setY(Math.sin(d2) * d3);
        return this;
    }

    public double getLength() {
        return Math.hypot(this.getX(), this.getY());
    }

    public Vector2D setLength(double d2) {
        double d3 = this.getLength();
        if (d3 == 0.0) {
            throw new IllegalStateException("Can't resize zero-width vector.");
        }
        return this.multiply(d2 / d3);
    }

    public double getSquaredLength() {
        return this.getX() * this.getX() + this.getY() * this.getY();
    }

    public Vector2D copy() {
        return new Vector2D(this);
    }

    public Vector2D copyNegate() {
        return new Vector2D(- this.getX(), - this.getY());
    }

    public boolean nearlyEquals(Vector2D vector2D, double d2) {
        return vector2D != null && NumberUtil.nearlyEquals(this.getX(), vector2D.getX(), d2) && NumberUtil.nearlyEquals(this.getY(), vector2D.getY(), d2);
    }

    @Override
    public String toString() {
        return StringUtil.toString((Object)this, false, "x", "y");
    }
}

