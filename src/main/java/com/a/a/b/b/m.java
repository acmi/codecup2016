/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.b;

import com.a.a.b.a;
import com.a.a.b.b.e;
import com.a.a.b.b.f;
import com.a.a.b.b.h;
import com.a.a.b.c.b;
import com.a.a.b.c.c;
import com.codeforces.commons.geometry.Line2D;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;

public class m
extends e {
    public m(double d2) {
        super(d2);
    }

    @Override
    protected boolean a(a a2, a a3) {
        return a2.c().e() == com.a.a.b.c.f.b && a3.c().e() == com.a.a.b.c.f.a;
    }

    @Override
    protected f b(a a2, a a3) {
        Object object;
        Object object2;
        com.a.a.b.c.e e2 = (com.a.a.b.c.e)a2.c();
        b b2 = (b)a3.c();
        Point2D[] arrpoint2D = e2.a(a2.r(), a2.x(), this.a);
        int n2 = arrpoint2D.length;
        if (!com.a.a.b.f.a.b(a3.r(), arrpoint2D, this.a)) {
            double d2 = Double.POSITIVE_INFINITY;
            object2 = null;
            for (int i2 = 0; i2 < n2; ++i2) {
                object = arrpoint2D[i2];
                Point2D point2D = arrpoint2D[i2 == n2 - 1 ? 0 : i2 + 1];
                Line2D line2D = Line2D.getLineByTwoPoints((Point2D)object, point2D);
                double d3 = line2D.getDistanceFrom(a3.r());
                if (d3 >= d2) continue;
                d2 = d3;
                object2 = line2D;
            }
            if (object2 != null) {
                return new f(a2, a3, a3.r(), object2.getUnitNormal().negate(), b2.a() - object2.getSignedDistanceFrom(a3.r()), this.a);
            }
        }
        Object object3 = null;
        for (int i3 = 0; i3 < n2; ++i3) {
            object2 = arrpoint2D[i3];
            Point2D point2D = arrpoint2D[i3 == n2 - 1 ? 0 : i3 + 1];
            object = h.a(a2, a3, (Point2D)object2, point2D, b2, this.a);
            if (object == null || object3 != null && object.e() <= object3.e()) continue;
            object3 = object;
        }
        return object3;
    }
}

