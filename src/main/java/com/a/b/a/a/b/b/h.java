/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.b;

import com.a.a.a.a.c;
import com.a.b.a.a.b.d.c.a;
import com.a.b.a.a.b.d.c.b;
import com.a.b.a.a.b.e.d;
import com.a.b.a.a.b.e.m;
import com.a.b.a.a.b.e.o;
import com.a.b.a.a.c.l;
import com.a.b.a.a.c.n;
import com.a.b.a.a.c.r;
import com.a.b.b;
import com.a.b.d;
import com.a.b.g;
import com.codeforces.commons.geometry.Point2D;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class h
implements b {
    private static final r[] a = new r[]{r.ORC_WOODCUTTER, r.ORC_WOODCUTTER, r.ORC_WOODCUTTER, r.FETISH_BLOWDART};
    private final d.a b;
    private final d.a c;

    public h(d.a a2, d.a a3) {
        this.b = a2;
        this.c = a3;
    }

    @Override
    public void a(g g2, int n2) {
        h.a(g2);
        this.b(g2, n2);
    }

    private static void a(g g2) {
        int n2;
        ArrayList arrayList = new ArrayList(4);
        for (n2 = 0; n2 < 4; ++n2) {
            arrayList.add(new ArrayList());
        }
        g2.a().stream().filter(d2 -> d2 instanceof com.a.b.a.a.b.d.c.b && d2.c() == l.NEUTRAL).forEachOrdered(d2 -> {
            Integer n2 = o.a((com.a.b.a.a.b.d.c.b)d2);
            if (n2 != null) {
                ((List)arrayList.get(n2)).add((com.a.b.a.a.b.d.c.b)d2);
            }
        }
        );
        for (n2 = 0; n2 < 4; ++n2) {
            if (((List)arrayList.get(n2)).size() >= 5 || c.d() >= 0.001) continue;
            h.a(g2, n2, (List)arrayList.get(n2));
        }
    }

    private static boolean a(g g2, int n2, List<com.a.b.a.a.b.d.c.b> list) {
        int n3 = 100;
        while (--n3 >= 0) {
            com.a.b.a.a.b.d.c.b b2;
            Point2D point2D;
            Point2D point2D2 = null;
            double d2 = Double.MAX_VALUE;
            int n4 = 3;
            while (--n4 >= 0) {
                point2D = o.a(n2);
                if (point2D == null) continue;
                double d3 = h.a(list, point2D);
                if (point2D2 != null && d3 >= d2) continue;
                point2D2 = point2D;
                d2 = d3;
            }
            if (point2D2 == null || !h.a(b2 = c.d() < 0.25 ? new a(point2D2.getX(), point2D2.getY(), c.f(), l.NEUTRAL) : new com.a.b.a.a.b.d.c.c(point2D2.getX(), point2D2.getY(), c.f(), l.NEUTRAL), (double)(point2D = (Point2D)com.a.b.a.a.b.e.l.a(b2)), g2)) continue;
            g2.a(b2);
            return true;
        }
        return false;
    }

    private static boolean a(com.a.b.a.a.b.d.c.b b2, double d2, g g2) {
        List<d> list = g2.a();
        int n2 = list.size();
        while (--n2 >= 0) {
            d d3 = list.get(n2);
            if (d3.b(b2) > d2 + com.a.b.a.a.b.e.l.a(d3)) continue;
            return false;
        }
        return true;
    }

    private static double a(List<? extends d> list, Point2D point2D) {
        double d2 = Double.MAX_VALUE;
        for (d d3 : list) {
            double d4 = d3.a(point2D);
            if (d4 >= d2) continue;
            d2 = d4;
        }
        return d2;
    }

    private void b(g g2, int n2) {
        if (n2 <= 0 || n2 % 750 != 0) {
            return;
        }
        Preconditions.checkState(true, "Minion spawn area is too small.");
        h.a(g2, 600.0, 3000.0, l.ACADEMY, new b.a(b.b.a, this.c.a()), n.MIDDLE);
        h.a(g2, 0.0, 2800.0, l.ACADEMY, new b.a(b.b.a, 200.0, 0.0), n.TOP);
        h.a(g2, 800.0, 3600.0, l.ACADEMY, new b.a(b.b.a, 4000.0, 3800.0), n.BOTTOM);
        h.a(g2, 3000.0, 600.0, l.RENEGADES, new b.a(b.b.a, this.b.a()), n.MIDDLE);
        h.a(g2, 2800.0, 0.0, l.RENEGADES, new b.a(b.b.a, 0.0, 200.0), n.TOP);
        h.a(g2, 3600.0, 800.0, l.RENEGADES, new b.a(b.b.a, 3800.0, 4000.0), n.BOTTOM);
    }

    private static void a(g g2, double d2, double d3, l l2, b.a a2, n n2) {
        double d4 = d2 + 400.0;
        double d5 = d3 + 400.0;
        int n3 = a.length;
        while (--n3 >= 0) {
            com.a.b.a.a.b.d.c.b b2;
            r r2 = a[n3];
            double d6 = d2 + 25.0 + c.d() * (d4 - d2 - 50.0);
            double d7 = d3 + 25.0 + c.d() * (d5 - d3 - 50.0);
            switch (r2) {
                case ORC_WOODCUTTER: {
                    b2 = new com.a.b.a.a.b.d.c.c(d6, d7, c.f(), l2);
                    break;
                }
                case FETISH_BLOWDART: {
                    b2 = new a(d6, d7, c.f(), l2);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unsupported minion type: " + (Object)((Object)r2) + '.');
                }
            }
            b2.a(a2, 0);
            b2.a(n2);
            g2.a(b2);
            if (o.a(b2, g2, false)) continue;
            m.a(b2, g2);
        }
    }
}

