/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.b;

import com.a.a.b.a;
import com.a.a.b.b.e;
import com.a.a.b.b.f;
import com.a.a.b.c.b;
import com.a.a.b.c.c;
import com.a.a.b.c.d;
import com.codeforces.commons.geometry.Line2D;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.math.Math;

public class h
extends e {
    public h(double d2) {
        super(d2);
    }

    @Override
    protected boolean a(a a2, a a3) {
        return a2.c().e() == com.a.a.b.c.f.c && a3.c().e() == com.a.a.b.c.f.a;
    }

    @Override
    protected f b(a a2, a a3) {
        d d2 = (d)a2.c();
        b b2 = (b)a3.c();
        Point2D point2D = d2.a(a2.r(), a2.x(), this.a);
        Point2D point2D2 = d2.b(a2.r(), a2.x(), this.a);
        return h.a(a2, a3, point2D, point2D2, b2, this.a);
    }

    static f a(a a2, a a3, Point2D point2D, Point2D point2D2, b b2, double d2) {
        double d3;
        boolean bl;
        Point2D point2D3;
        double d4;
        double d5;
        Line2D line2D = Line2D.getLineByTwoPoints(point2D, point2D2);
        double d6 = line2D.getDistanceFrom(a3.r());
        if (d6 > (d4 = b2.a())) {
            return null;
        }
        double d7 = Math.min(point2D.getX(), point2D2.getX());
        double d8 = Math.min(point2D.getY(), point2D2.getY());
        double d9 = Math.max(point2D.getX(), point2D2.getX());
        double d10 = Math.max(point2D.getY(), point2D2.getY());
        Point2D point2D4 = line2D.getProjectionOf(a3.r());
        boolean bl2 = bl = point2D4.getX() > d7 - d2 && point2D4.getX() < d9 + d2 && point2D4.getY() > d8 - d2 && point2D4.getY() < d10 + d2;
        if (bl) {
            Vector2D vector2D;
            if (d6 >= d2) {
                vector2D = new Vector2D(a3.r(), point2D4).normalize();
            } else {
                Vector2D vector2D2 = line2D.getUnitNormal();
                Vector2D vector2D3 = a3.u().copy().subtract(a2.u());
                vector2D = vector2D3.getLength() >= d2 ? (vector2D3.dotProduct(vector2D2) >= d2 ? vector2D2 : vector2D2.negate()) : (a3.u().getLength() >= d2 ? (a3.u().dotProduct(vector2D2) >= d2 ? vector2D2 : vector2D2.negate()) : vector2D2);
            }
            return new f(a2, a3, point2D4, vector2D, d4 - d6, d2);
        }
        double d11 = a3.a(point2D);
        if (d11 < (d5 = a3.a(point2D2))) {
            point2D3 = point2D;
            d3 = d11;
        } else {
            point2D3 = point2D2;
            d3 = d5;
        }
        if (d3 > d4) {
            return null;
        }
        return new f(a2, a3, point2D3, new Vector2D(a3.r(), point2D3).normalize(), d4 - d3, d2);
    }
}

