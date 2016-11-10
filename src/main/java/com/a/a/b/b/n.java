/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.b;

import com.a.a.b.a;
import com.a.a.b.b.e;
import com.a.a.b.b.f;
import com.a.a.b.c.c;
import com.codeforces.commons.geometry.Line2D;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;

public class n
extends e {
    public n(double d2) {
        super(d2);
    }

    @Override
    protected boolean a(a a2, a a3) {
        return a2.c().e() == com.a.a.b.c.f.b && a3.c().e() == com.a.a.b.c.f.b;
    }

    @Override
    protected f b(a a2, a a3) {
        Point2D[] arrpoint2D;
        com.a.a.b.c.e e2 = (com.a.a.b.c.e)a2.c();
        com.a.a.b.c.e e3 = (com.a.a.b.c.e)a3.c();
        Point2D[] arrpoint2D2 = e2.a(a2.r(), a2.x(), this.a);
        f f2 = this.a(a2, a3, arrpoint2D2, arrpoint2D = e3.a(a3.r(), a3.x(), this.a));
        if (f2 == null) {
            return null;
        }
        f f3 = this.a(a3, a2, arrpoint2D, arrpoint2D2);
        if (f3 == null) {
            return null;
        }
        if (f3.e() < f2.e()) {
            return new f(a2, a3, f3.c(), f3.d().negate(), f3.e(), this.a);
        }
        return f2;
    }

    private f a(a a2, a a3, Point2D[] arrpoint2D, Point2D[] arrpoint2D2) {
        int n2 = arrpoint2D.length;
        int n3 = arrpoint2D2.length;
        double d2 = Double.POSITIVE_INFINITY;
        Point2D point2D = null;
        Vector2D vector2D = null;
        for (int i2 = 0; i2 < n2; ++i2) {
            Point2D point2D2 = arrpoint2D[i2];
            Point2D point2D3 = arrpoint2D[i2 == n2 - 1 ? 0 : i2 + 1];
            Line2D line2D = Line2D.getLineByTwoPoints(point2D2, point2D3);
            if (line2D.getSignedDistanceFrom(a2.r()) > - this.a) {
                throw new IllegalStateException(String.format("%s of %s is too small, does not represent a convex polygon, or its points are going in wrong order.", c.a(a2.c()), a2));
            }
            double d3 = Double.POSITIVE_INFINITY;
            Point2D point2D4 = null;
            Vector2D vector2D2 = null;
            for (int i3 = 0; i3 < n3; ++i3) {
                Point2D point2D5 = arrpoint2D2[i3];
                double d4 = line2D.getSignedDistanceFrom(point2D5);
                if (d4 >= d3) continue;
                d3 = d4;
                point2D4 = point2D5;
                vector2D2 = line2D.getUnitNormalFrom(a2.r(), this.a).negate();
            }
            if (d3 > 0.0) {
                return null;
            }
            double d5 = - d3;
            if (d5 >= d2) continue;
            d2 = d5;
            point2D = point2D4;
            vector2D = vector2D2;
        }
        if (point2D == null || vector2D == null) {
            return null;
        }
        return new f(a2, a3, point2D, vector2D, d2, this.a);
    }
}

