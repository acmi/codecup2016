/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.b;

import com.a.a.b.a;
import com.a.a.b.b.e;
import com.a.a.b.b.f;
import com.a.a.b.c.c;
import com.a.a.b.c.d;
import com.codeforces.commons.geometry.Line2D;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.math.Math;
import java.util.ArrayList;

public class j
extends e {
    public j(double d2) {
        super(d2);
    }

    @Override
    protected boolean a(a a2, a a3) {
        return a2.c().e() == com.a.a.b.c.f.c && a3.c().e() == com.a.a.b.c.f.b;
    }

    @Override
    protected f b(a a2, a a3) {
        Vector2D vector2D;
        double d5;
        double d2;
        Point2D point2D;
        double d3;
        double d4;
        Object object3;
        Object object2;
        Object object;
        d d6 = (d)a2.c();
        com.a.a.b.c.e e2 = (com.a.a.b.c.e)a3.c();
        Point2D point2D2 = d6.a(a2.r(), a2.x(), this.a);
        Line2D line2D = Line2D.getLineByTwoPoints(point2D2, point2D = d6.b(a2.r(), a2.x(), this.a));
        if (line2D.getDistanceFrom(a3.r()) > e2.d()) {
            return null;
        }
        Point2D[] arrpoint2D = e2.a(a3.r(), a3.x(), this.a);
        int n2 = arrpoint2D.length;
        Object object4 = null;
        ArrayList<Object> arrayList = new ArrayList<Object>(n2);
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            object3 = arrpoint2D[i2];
            Point2D point2D3 = arrpoint2D[i2 == n2 - 1 ? 0 : i2 + 1];
            object = Line2D.getLineByTwoPoints((Point2D)object3, point2D3);
            object2 = line2D.getIntersectionPoint((Line2D)object, this.a);
            if (object2 == null) continue;
            d2 = Math.max(Math.min(point2D2.getX(), point2D.getX()), Math.min(object3.getX(), point2D3.getX()));
            d5 = Math.max(Math.min(point2D2.getY(), point2D.getY()), Math.min(object3.getY(), point2D3.getY()));
            d4 = Math.min(Math.max(point2D2.getX(), point2D.getX()), Math.max(object3.getX(), point2D3.getX()));
            d3 = Math.min(Math.max(point2D2.getY(), point2D.getY()), Math.max(object3.getY(), point2D3.getY()));
            if (object2.getX() <= d2 - this.a || object2.getX() >= d4 + this.a || object2.getY() <= d5 - this.a || object2.getY() >= d3 + this.a) continue;
            object4 = object;
            boolean bl = false;
            for (Point2D point2D4 : arrayList) {
                if (!point2D4.nearlyEquals((Point2D)object2, this.a)) continue;
                bl = true;
                break;
            }
            if (!bl) {
                arrayList.add(object2);
            }
            ++n3;
        }
        if (!(n3 != 1 || !d6.f() || com.a.a.b.f.a.b(point2D2, arrpoint2D, this.a) && com.a.a.b.f.a.b(point2D, arrpoint2D, this.a))) {
            Vector2D vector2D2 = new Vector2D(a3.r(), object4.getProjectionOf(a3.r())).normalize();
            object3 = object4.getParallelLine(point2D2);
            double d7 = object3.getDistanceFrom(a3.r());
            d5 = (d7 < (d2 = (object2 = object4.getParallelLine(point2D)).getDistanceFrom(a3.r())) ? object3 : object2).getDistanceFrom((Line2D)object4, this.a);
            return new f(a2, a3, (Point2D)arrayList.get(0), vector2D2, d5, this.a);
        }
        Point2D point2D5 = arrpoint2D[0];
        double d8 = line2D.getSignedDistanceFrom(point2D5);
        object = point2D5;
        double d9 = d8;
        for (int i3 = 1; i3 < n2; ++i3) {
            Point2D point2D6 = arrpoint2D[i3];
            double d10 = line2D.getSignedDistanceFrom(point2D6);
            if (d10 < d8) {
                d8 = d10;
                point2D5 = point2D6;
            }
            if (d10 <= d9) continue;
            d9 = d10;
            object = point2D6;
        }
        if (d8 < 0.0 && d9 < 0.0 || d8 > 0.0 && d9 > 0.0) {
            return null;
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        if (line2D.getSignedDistanceFrom(a3.r()) > 0.0) {
            vector2D = line2D.getParallelLine(point2D5).getUnitNormalFrom((Point2D)object);
            d5 = Math.abs(d8);
        } else {
            vector2D = line2D.getParallelLine((Point2D)object).getUnitNormalFrom(point2D5);
            d5 = d9;
        }
        d4 = 0.0;
        d3 = 0.0;
        for (Object object5 : arrayList) {
            d4 += object5.getX() / (double)arrayList.size();
            d3 += object5.getY() / (double)arrayList.size();
        }
        return new f(a2, a3, new Point2D(d4, d3), vector2D, d5, this.a);
    }
}

