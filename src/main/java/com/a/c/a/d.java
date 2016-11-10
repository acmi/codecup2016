/*
 * Decompiled with CFR 0_119.
 */
package com.a.c.a;

import com.a.c.a.c;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.text.StringUtil;

public class d
extends c {
    private double a;
    private double b;
    private double c;
    private double d;

    public d(Point2D point2D, Point2D point2D2) {
        this.a = point2D.getX();
        this.b = point2D.getY();
        this.c = point2D2.getX();
        this.d = point2D2.getY();
    }

    public d(d d2) {
        this.a = d2.a;
        this.b = d2.b;
        this.c = d2.c;
        this.d = d2.d;
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

    public double f() {
        return this.d;
    }

    @Override
    public c d() {
        return new d(this);
    }

    public String toString() {
        return StringUtil.toString((Object)this, false, new String[0]);
    }

    @Override
    public boolean a(c c2, double d2) {
        if (c2 == null || this.getClass() != c2.getClass()) {
            return false;
        }
        d d3 = (d)c2;
        return Math.abs(this.a - d3.a) < d2 && Math.abs(this.b - d3.b) < d2 && Math.abs(this.c - d3.c) < d2 && Math.abs(this.d - d3.d) < d2;
    }
}

