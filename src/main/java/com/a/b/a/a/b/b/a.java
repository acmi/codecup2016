/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.b;

import com.a.a.a.a.c;
import com.a.b.b;
import com.a.b.d;
import com.a.b.g;
import com.codeforces.commons.geometry.Point2D;
import java.util.List;

public class a
implements b {
    private static final com.a.b.a.a.c.c[] a = com.a.b.a.a.c.c.values();

    @Override
    public void a(g g2, int n2) {
        Point2D[] arrpoint2D;
        Point2D[] arrpoint2D2;
        if (n2 <= 0 || n2 % 2500 != 0) {
            return;
        }
        List<d> list = g2.a();
        if (a.a(list)) {
            return;
        }
        Point2D point2D = new Point2D(1200.0, 1200.0);
        Point2D point2D2 = new Point2D(2800.0, 2800.0);
        if (c.c()) {
            Point2D[] arrpoint2D3 = new Point2D[2];
            arrpoint2D3[0] = point2D;
            arrpoint2D2 = arrpoint2D3;
            arrpoint2D3[1] = point2D2;
        } else {
            Point2D[] arrpoint2D4 = new Point2D[2];
            arrpoint2D4[0] = point2D2;
            arrpoint2D2 = arrpoint2D4;
            arrpoint2D4[1] = point2D;
        }
        for (Point2D point2D3 : arrpoint2D = arrpoint2D2) {
            if (!a.a(point2D3, list)) continue;
            com.a.b.a.a.c.c c2 = a[c.a(0, a.length - 1)];
            g2.a(new com.a.b.a.a.b.d.a.a(c2, point2D3.getX(), point2D3.getY()));
            return;
        }
    }

    private static boolean a(List<d> list) {
        int n2 = list.size();
        while (--n2 >= 0) {
            d d2 = list.get(n2);
            if (!(d2 instanceof com.a.b.a.a.b.d.a.a)) continue;
            return true;
        }
        return false;
    }

    private static boolean a(Point2D point2D, List<d> list) {
        int n2 = list.size();
        while (--n2 >= 0) {
            d d2 = list.get(n2);
            if (!(d2 instanceof com.a.b.a.a.b.d.f.a) || d2.a(point2D) > 55.0) continue;
            return false;
        }
        return true;
    }
}

