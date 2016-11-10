/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.c;

import com.a.b.a.a.c.l;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.reflection.Name;
import com.google.gson.annotations.Until;

public abstract class C {
    private final long id;
    private final double x;
    private final double y;
    private final double speedX;
    private final double speedY;
    private final double angle;
    @Until(value=1.0)
    private final l faction;

    protected C(@Name(value="id") long l2, @Name(value="x") double d2, @Name(value="y") double d3, @Name(value="speedX") double d4, @Name(value="speedY") double d5, @Name(value="angle") double d6, @Name(value="faction") l l3) {
        this.id = l2;
        this.x = d2;
        this.y = d3;
        this.speedX = d4;
        this.speedY = d5;
        this.angle = d6;
        this.faction = l3;
    }

    public long getId() {
        return this.id;
    }

    public final double getX() {
        return this.x;
    }

    public final double getY() {
        return this.y;
    }

    public final double getSpeedX() {
        return this.speedX;
    }

    public final double getSpeedY() {
        return this.speedY;
    }

    public final double getAngle() {
        return this.angle;
    }

    public l getFaction() {
        return this.faction;
    }

    public double getAngleTo(double d2, double d3) {
        double d4;
        double d5 = Math.atan2(d3 - this.y, d2 - this.x);
        for (d4 = d5 - this.angle; d4 > 3.141592653589793; d4 -= 6.283185307179586) {
        }
        while (d4 < -3.141592653589793) {
            d4 += 6.283185307179586;
        }
        return d4;
    }

    public double getAngleTo(C c2) {
        return this.getAngleTo(c2.x, c2.y);
    }

    public double getDistanceTo(double d2, double d3) {
        return Math.hypot(d2 - this.x, d3 - this.y);
    }

    public double getDistanceTo(C c2) {
        return this.getDistanceTo(c2.x, c2.y);
    }

    protected static boolean areFieldEquals(C c2, C c3) {
        return c2.id == c3.id && Double.compare(c2.x, c3.x) == 0 && Double.compare(c2.y, c3.y) == 0 && Double.compare(c2.speedX, c3.speedX) == 0 && Double.compare(c2.speedY, c3.speedY) == 0 && Double.compare(c2.angle, c3.angle) == 0 && c2.faction == c3.faction;
    }
}

