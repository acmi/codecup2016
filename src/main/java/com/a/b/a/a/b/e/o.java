/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.e;

import com.a.b.a.a.b.d.b;
import com.a.b.a.a.b.d.c.b;
import com.a.b.a.a.b.d.f.a;
import com.a.b.a.a.b.e.m;
import com.a.b.a.a.b.e.t;
import com.a.b.a.a.b.e.u;
import com.a.b.a.a.b.e.v;
import com.a.b.a.a.b.f;
import com.a.b.a.a.c.l;
import com.a.b.a.a.c.q;
import com.a.b.a.a.c.r;
import com.a.b.a.a.c.z;
import com.a.b.d;
import com.a.b.g;
import com.a.c.a.b;
import com.a.c.a.c;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.math.NumberUtil;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class o {
    private static final a.a.d.a.a<a.a.d.a.f<Point2D>> a = new a.a.d.a.a();

    public static q a(com.a.b.a.a.b.d.c.b b2, int n2, boolean bl) {
        int n3;
        a.a.d.a.f<Point2D> f2;
        Point2D point2D;
        com.a.c.c c2 = b2.b();
        c c3 = c2.t();
        if (!(c3 instanceof b)) {
            throw new IllegalArgumentException("Unsupported minion form: " + c3 + '.');
        }
        b b3 = (b)c3;
        switch (b2.h()) {
            case ORC_WOODCUTTER: {
                n3 = 12;
                break;
            }
            case FETISH_BLOWDART: {
                n3 = 6;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported minion type: " + (Object)((Object)b2.h()) + '.');
            }
        }
        a.a.d.a.f<Point2D> f3 = a.a_(n2);
        if (f3 == null) {
            f3 = v.a();
            a.a(n2, f3);
        }
        if ((point2D = f3.b(b2.a())) == null) {
            point2D = new Point2D(c2.c(), c2.d());
            f3.a(b2.a(), point2D);
        }
        Point2D point2D2 = (f2 = a.a_(n2 - 1)) == null ? null : f2.b(b2.a());
        Vector2D vector2D = point2D2 == null ? c2.f() : point2D.subtract(point2D2);
        return new q(b2.a(), bl ? 4000.0 - c2.c() : c2.c(), bl ? 4000.0 - c2.d() : c2.d(), bl ? - vector2D.getX() : vector2D.getX(), bl ? - vector2D.getY() : vector2D.getY(), bl ? c2.e() + 3.141592653589793 : c2.e(), b2.c(), b3.a(), o.b(b2) ? 0 : Math.max(NumberUtil.toInt(b2.j()), 1), NumberUtil.toInt(b2.k()), t.a(b2.l()), b2.h(), b2.q(), n3, o.c(b2), b2.c(n2));
    }

    public static Point2D a(int n2) {
        return u.a(n2);
    }

    public static Integer a(com.a.b.a.a.b.d.c.b b2) {
        Point2D point2D = new Point2D(b2.b().c(), b2.b().d());
        for (int i2 = 0; i2 < 4; ++i2) {
            if (!com.a.a.b.f.a.a(point2D, u.a[i2], 1.0E-7)) continue;
            return i2;
        }
        return null;
    }

    public static void a(g g2, com.a.b.a.a.b.d.c.b b2, f f2, List<a> list, double d3) {
        if (o.b(b2) || d3 < 1.0E-7) {
            return;
        }
        if (f2 != null && f2.k() == b2.c()) {
            return;
        }
        d3 = Math.min(b2.j(), d3);
        b2.b(b2.j() - d3);
        v.a((com.a.b.a.a.b.d.b)b2, f2, list, d3, 0.0);
        if (!o.b(b2)) {
            b2.b(b2.o() + NumberUtil.toInt(d3 * 2.5));
            if (f2 != null && f2.k() != b2.c() && list != null && b2.c() == l.NEUTRAL) {
                a a3 = list.stream().filter(a2 -> f2.equals(a2.s())).findAny().orElse(null);
                if (a3 != null) {
                    b.a a4 = new b.a(b.b.a, a3);
                    g2.a().stream().filter(d2 -> d2 instanceof com.a.b.a.a.b.d.c.b && ((com.a.b.a.a.b.d.c.b)d2).i() == null && d2.c() == b2.c() && d2.b(b2) <= 400.0).forEach(d2 -> {
                        ((com.a.b.a.a.b.d.c.b)d2).a(a4, 0);
                    }
                    );
                }
            }
            return;
        }
        v.a((com.a.b.a.a.b.d.b)b2, null, f2, list, 0.25);
        b2.b(0.0);
        b2.b(0);
        b2.m();
        g2.b(b2);
    }

    public static boolean b(com.a.b.a.a.b.d.c.b b2) {
        return o.a(b2.j());
    }

    public static boolean a(q q2) {
        return o.a((double)q2.getLife());
    }

    public static boolean a(double d2) {
        return d2 < 1.0E-7;
    }

    public static boolean a(com.a.b.a.a.b.d.c.b b2, g g2, boolean bl) {
        return m.a(b2, g2, bl);
    }

    public static com.a.b.a.a.b.d.c.b[] a(d[] arrd) {
        int n2 = arrd.length;
        com.a.b.a.a.b.d.c.b[] arrb = new com.a.b.a.a.b.d.c.b[n2];
        int n3 = 0;
        int n4 = n2;
        while (--n4 >= 0) {
            d d2 = arrd[n4];
            if (!(d2 instanceof com.a.b.a.a.b.d.c.b)) continue;
            arrb[n3++] = (com.a.b.a.a.b.d.c.b)d2;
        }
        if (n3 == n2) {
            return arrb;
        }
        com.a.b.a.a.b.d.c.b[] arrb2 = new com.a.b.a.a.b.d.c.b[n3];
        System.arraycopy(arrb, 0, arrb2, 0, n3);
        return arrb2;
    }

    public static int c(com.a.b.a.a.b.d.c.b b2) {
        return o.a(b2.h());
    }

    public static int b(q q2) {
        return o.a(q2.getType());
    }

    public static int a(r r2) {
        switch (r2) {
            case ORC_WOODCUTTER: {
                return 60;
            }
            case FETISH_BLOWDART: {
                return 30;
            }
        }
        throw new IllegalStateException("Unsupported minion type: " + (Object)((Object)r2) + '.');
    }

    public static double b(r r2) {
        switch (r2) {
            case ORC_WOODCUTTER: {
                return 0.5235987755982988;
            }
            case FETISH_BLOWDART: {
                return 0.5235987755982988;
            }
        }
        throw new IllegalArgumentException("Unsupported minion type: " + (Object)((Object)r2) + '.');
    }
}

