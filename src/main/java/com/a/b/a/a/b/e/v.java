/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.e;

import com.a.b.a.a.b.d.f.a;
import com.a.b.a.a.b.e.l;
import com.a.b.a.a.b.e.x;
import com.a.b.a.a.b.e.z;
import com.a.b.a.a.b.f;
import com.a.b.a.a.c.B;
import com.a.b.a.a.c.C;
import com.a.b.a.a.c.D;
import com.a.b.a.a.c.E;
import com.a.b.a.a.c.q;
import com.a.b.d;
import com.a.b.g;
import com.a.c.a.b;
import com.a.c.a.c;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.math.NumberUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class v {
    public static boolean a(d d2) {
        com.a.c.c c2 = d2.b();
        double d3 = c2.c();
        double d4 = c2.d();
        c c3 = c2.t();
        if (!(c3 instanceof b)) {
            throw new IllegalArgumentException("Unsupported unit form: " + c3 + '.');
        }
        return v.a(d3, d4, ((b)c3).a());
    }

    public static boolean a(double d2, double d3, double d4) {
        return d2 - d4 < 0.0 || d2 + d4 > 4000.0 || d3 - d4 < 0.0 || d3 + d4 > 4000.0;
    }

    public static List<d> a(d d2, g g2, double d3, double d4) {
        return v.a(d2, g2.a(), d3, d4);
    }

    private static List<d> a(d d2, Iterable<? extends d> iterable, double d3, double d4) {
        ArrayList<d> arrayList = new ArrayList<d>();
        double d5 = d3 / 2.0;
        for (d d6 : iterable) {
            if (d6.equals(d2) || !v.a(d2, d6, d5, d4)) continue;
            arrayList.add(d6);
        }
        return arrayList;
    }

    public static List<d> a(d d2, d[] arrd, double d3, double d4) {
        ArrayList<d> arrayList = new ArrayList<d>();
        double d5 = d3 / 2.0;
        for (d d6 : arrd) {
            if (d6.equals(d2) || !v.a(d2, d6, d5, d4)) continue;
            arrayList.add(d6);
        }
        return arrayList;
    }

    private static boolean a(d d2, d d3, double d4, double d5) {
        double d6 = d2.b(d3);
        if (d6 > d5 + l.a(d3)) {
            return false;
        }
        double d7 = d2.a(d3);
        return d7 >= - d4 && d7 <= d4;
    }

    public static List<C> a(C c2, E e2, double d2, double d3) {
        return v.a(c2, z.a(e2).values(), d2, d3);
    }

    private static List<C> a(C c2, Iterable<? extends C> iterable, double d2, double d3) {
        ArrayList<C> arrayList = new ArrayList<C>();
        double d4 = d2 / 2.0;
        for (C c3 : iterable) {
            if (c3.getId() == c2.getId() || !v.a(c2, c3, d4, d3)) continue;
            arrayList.add(c3);
        }
        return arrayList;
    }

    private static boolean a(C c2, C c3, double d2, double d3) {
        double d4 = c2.getDistanceTo(c3);
        if (d4 > d3 + l.a(c3)) {
            return false;
        }
        double d5 = c2.getAngleTo(c3);
        return d5 >= - d2 && d5 <= d2;
    }

    public static boolean a(C c2, C c3) {
        if (c2 == null && c3 == null) {
            return true;
        }
        if (c2 == null ^ c3 == null) {
            return false;
        }
        if (c2 instanceof D && c3 instanceof D) {
            return D.areFieldEquals((D)c2, (D)c3);
        }
        if (c2 instanceof q && c3 instanceof q) {
            return q.areFieldEquals((q)c2, (q)c3);
        }
        if (c2 instanceof com.a.b.a.a.c.v && c3 instanceof com.a.b.a.a.c.v) {
            return com.a.b.a.a.c.v.areFieldEquals((com.a.b.a.a.c.v)c2, (com.a.b.a.a.c.v)c3);
        }
        if (c2 instanceof com.a.b.a.a.c.b && c3 instanceof com.a.b.a.a.c.b) {
            return com.a.b.a.a.c.b.areFieldEquals((com.a.b.a.a.c.b)c2, (com.a.b.a.a.c.b)c3);
        }
        if (c2 instanceof com.a.b.a.a.c.d && c3 instanceof com.a.b.a.a.c.d) {
            return com.a.b.a.a.c.d.areFieldEquals((com.a.b.a.a.c.d)c2, (com.a.b.a.a.c.d)c3);
        }
        if (c2 instanceof B && c3 instanceof B) {
            return B.areFieldEquals((B)c2, (B)c3);
        }
        throw new IllegalArgumentException(String.format("Unsupported classes of units %s and %s.", c2, c3));
    }

    static void a(com.a.b.a.a.b.d.b b2, f f2, List<a> list, double d2, double d3) {
        if (f2 == null || f2.k() == b2.c()) {
            return;
        }
        com.a.b.a.a.b.g g2 = null;
        if (list != null) {
            for (a a2 : list) {
                if (!a2.s().equals(f2)) continue;
                if (x.a(a2)) break;
                g2 = a2;
                break;
            }
        }
        if (g2 == null) {
            g2 = f2;
        }
        g2.a(NumberUtil.toInt(d2 * d3));
    }

    static void a(com.a.b.a.a.b.d.b b2, List<a> list, f f2, List<a> list2, double d2) {
        boolean bl;
        if (list2 == null || list2.isEmpty()) {
            return;
        }
        boolean bl2 = bl = list2.get(0).c() == b2.c();
        if (bl && (!(b2 instanceof a) || list == null)) {
            return;
        }
        ArrayList<a> arrayList = new ArrayList<a>(bl ? list : list2);
        com.a.a.a.a.c.a(arrayList);
        ArrayList<com.a.b.a.a.b.g> arrayList2 = new ArrayList<com.a.b.a.a.b.g>();
        if (f2 != null && f2.k() != b2.c()) {
            for (a a2 : arrayList) {
                if (!a2.s().equals(f2)) continue;
                if (x.a(a2)) break;
                arrayList2.add(a2);
                break;
            }
            if (arrayList2.isEmpty()) {
                arrayList2.add(f2);
            }
        }
        for (a a2 : arrayList) {
            if (a2.s().equals(f2) || x.a(a2) || a2.b(b2) > 600.0) continue;
            arrayList2.add(a2);
        }
        if (!arrayList2.isEmpty()) {
            int n2;
            int n3 = NumberUtil.toInt(b2.k() * d2 * ((n2 = arrayList2.size()) > 1 ? 1.67 : 1.0));
            int n4 = n3 / n2;
            ((com.a.b.a.a.b.g)arrayList2.get(0)).a(n4 + n3 % n2);
            for (int i2 = 1; i2 < n2; ++i2) {
                ((com.a.b.a.a.b.g)arrayList2.get(i2)).a(n4);
            }
        }
    }

    public static com.a.b.a.a.c.l b(d d2) {
        switch (d2.c()) {
            case ACADEMY: {
                return com.a.b.a.a.c.l.RENEGADES;
            }
            case RENEGADES: {
                return com.a.b.a.a.c.l.ACADEMY;
            }
        }
        throw new IllegalArgumentException("Can't get opposite faction of " + d2 + '.');
    }

    static a.a.d.a.f<Point2D> a() {
        return new a.a.d.a.f<Point2D>(10, 0.5f, Long.MIN_VALUE);
    }
}

