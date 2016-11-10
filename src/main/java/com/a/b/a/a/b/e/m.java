/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.e;

import com.a.b.a.a.b.d.b;
import com.a.b.a.a.b.d.b.a;
import com.a.b.a.a.b.d.d.e;
import com.a.b.a.a.b.e.d;
import com.a.b.a.a.b.e.l;
import com.a.b.a.a.b.e.o;
import com.a.b.a.a.b.e.u;
import com.a.b.a.a.b.e.v;
import com.a.b.a.a.b.e.x;
import com.a.b.a.a.c.A;
import com.a.b.a.a.c.z;
import com.a.b.g;
import com.a.c.a.c;
import com.codeforces.commons.geometry.Point2D;
import java.util.List;
import java.util.Map;

public final class m {
    public static z a(com.a.b.a.a.c.o o2, A a2) {
        z[] arrz = o2.getStatuses();
        z z2 = null;
        int n2 = arrz.length;
        while (--n2 >= 0) {
            z z3 = arrz[n2];
            if (z3.getType() != a2 || z2 != null && z3.getRemainingDurationTicks() <= z2.getRemainingDurationTicks()) continue;
            z2 = z3;
        }
        return z2;
    }

    public static z b(com.a.b.a.a.c.o o2, A a2) {
        z[] arrz = o2.getStatuses();
        z z2 = null;
        int n2 = arrz.length;
        while (--n2 >= 0) {
            z z3 = arrz[n2];
            if (z3.getType() != a2 || z2 != null && z3.getRemainingDurationTicks() >= z2.getRemainingDurationTicks()) continue;
            z2 = z3;
        }
        return z2;
    }

    public static boolean c(com.a.b.a.a.c.o o2, A a2) {
        z[] arrz = o2.getStatuses();
        int n2 = arrz.length;
        while (--n2 >= 0) {
            if (arrz[n2].getType() != a2) continue;
            return true;
        }
        return false;
    }

    public static void a(com.a.b.a.a.a.b b2, b b3, g g2, List<com.a.b.a.a.b.d.f.a> list, Map<com.a.b.a.a.c.l, List<com.a.b.a.a.b.d.f.a>> map, double d2, boolean bl, d.a a2, d.a a3) {
        if (b3.b(A.SHIELDED)) {
            d2 *= 0.75;
        }
        if (bl && b3 instanceof com.a.b.a.a.b.d.f.a) {
            d2 = x.b((com.a.b.a.a.b.d.f.a)b3, d2, map);
        }
        if (b3 instanceof com.a.b.a.a.b.d.f.a) {
            x.a(b2, g2, (com.a.b.a.a.b.d.f.a)b3, null, list, map.get((Object)v.b(b3)), d2);
        } else if (b3 instanceof com.a.b.a.a.b.d.c.b) {
            o.a(g2, (com.a.b.a.a.b.d.c.b)b3, null, list, d2);
        } else if (b3 instanceof a) {
            d.a(g2, (a)b3, null, list, d2, b3.c() == com.a.b.a.a.c.l.ACADEMY ? a2 : a3);
        } else if (b3 instanceof com.a.b.a.a.b.d.e.a) {
            u.a(g2, (com.a.b.a.a.b.d.e.a)b3, d2);
        } else {
            throw new IllegalArgumentException("Unsupported unit class: " + b3.getClass() + '.');
        }
    }

    public static boolean a(b b2, g g2, boolean bl) {
        if (bl && v.a(b2)) {
            return false;
        }
        for (com.a.b.d d2 : g2.d(b2)) {
            if (d2 instanceof com.a.b.a.a.b.d.f.a || d2 instanceof com.a.b.a.a.b.d.c.b || d2 instanceof a || d2 instanceof com.a.b.a.a.b.d.e.a) {
                return false;
            }
            if (d2 instanceof e || d2 instanceof com.a.b.a.a.b.d.a.a) continue;
            throw new IllegalArgumentException("Unsupported unit class: " + d2.getClass() + '.');
        }
        return true;
    }

    public static void a(b b2, g g2) {
        com.a.c.c c2 = b2.b();
        double d2 = c2.c();
        double d3 = c2.d();
        double d4 = l.a(c2.t());
        for (int i2 = 1; i2 <= 100; ++i2) {
            int n2;
            Point2D[] arrpoint2D = new Point2D[8 * i2];
            int n3 = 0;
            for (n2 = - i2; n2 <= i2; ++n2) {
                arrpoint2D[n3++] = new Point2D(d2 + (double)n2 * d4, d3 - (double)i2 * d4);
                arrpoint2D[n3++] = new Point2D(d2 + (double)n2 * d4, d3 + (double)i2 * d4);
            }
            for (n2 = - i2 + 1; n2 < i2; ++n2) {
                arrpoint2D[n3++] = new Point2D(d2 - (double)i2 * d4, d3 + (double)n2 * d4);
                arrpoint2D[n3++] = new Point2D(d2 + (double)i2 * d4, d3 + (double)n2 * d4);
            }
            com.a.a.a.a.c.a(arrpoint2D);
            do {
                Point2D point2D = arrpoint2D[--n3];
                c2.a(point2D.getX());
                c2.b(point2D.getY());
                g2.c(b2);
                if (!m.a(b2, g2, true)) continue;
                return;
            } while (n3 > 0);
        }
        throw new IllegalStateException("There is no free location to move living unit.");
    }

    public static boolean a(b b2) {
        if (b2 instanceof com.a.b.a.a.b.d.e.a) {
            return u.b((com.a.b.a.a.b.d.e.a)b2);
        }
        if (b2 instanceof com.a.b.a.a.b.d.c.b) {
            return o.b((com.a.b.a.a.b.d.c.b)b2);
        }
        if (b2 instanceof com.a.b.a.a.b.d.f.a) {
            return x.a((com.a.b.a.a.b.d.f.a)b2);
        }
        if (b2 instanceof a) {
            return d.a((a)b2);
        }
        throw new IllegalArgumentException("Unsupported living unit class: " + b2.getClass() + '.');
    }

    public static b[] a(List<? extends com.a.b.d> list) {
        int n2 = list.size();
        b[] arrb = new b[n2];
        int n3 = 0;
        int n4 = n2;
        while (--n4 >= 0) {
            com.a.b.d d2 = list.get(n4);
            if (!(d2 instanceof b)) continue;
            arrb[n3++] = (b)d2;
        }
        if (n3 == n2) {
            return arrb;
        }
        b[] arrb2 = new b[n3];
        System.arraycopy(arrb, 0, arrb2, 0, n3);
        return arrb2;
    }
}

