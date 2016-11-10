/*
 * Decompiled with CFR 0_119.
 */
package com.a.c.b.a;

import com.a.a.b.c.f;
import com.a.c.a.d;
import com.a.c.b.a.a;
import com.a.c.b.a.b;
import com.a.c.c;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.math.NumberUtil;
import com.codeforces.commons.text.StringUtil;
import com.google.common.base.Preconditions;
import java.util.Set;

class e {
    static com.a.a.b.a a(c c2) {
        com.a.a.b.a a2 = new com.a.a.b.a();
        a2.a(c2.b());
        com.a.c.a.c c3 = c2.t();
        a2.a(e.a(c3));
        if (c3 instanceof d) {
            d d2 = (d)c3;
            a2.a((d2.a() + d2.c()) / 2.0, (d2.b() + d2.f()) / 2.0);
            a2.k(new Vector2D(d2.a(), d2.b(), d2.c(), d2.f()).getAngle());
            c2.a(a2.s());
            c2.b(a2.t());
            c2.c(a2.x());
        }
        if (c2.u()) {
            a2.a(Double.POSITIVE_INFINITY);
            c2.d(Double.POSITIVE_INFINITY);
        } else {
            a2.a(c2.s());
        }
        return a2;
    }

    static c a(com.a.a.b.a a2, b b2) {
        double d2 = b2.c().b();
        return new a(Preconditions.checkNotNull(b2.b(a2.a())), a2, a2.b(), a2.d(), e.a(a2.c(), a2.r(), a2.x(), d2), a2.e());
    }

    static void a(com.a.a.b.a a2, c c2, b b2) {
        com.a.c.a.c c3;
        if (!NumberUtil.equals(b2.a(c2.a()), a2.a())) {
            throw new IllegalArgumentException("Can't update body ID.");
        }
        if (!StringUtil.equals(c2.b(), a2.b())) {
            throw new IllegalArgumentException("Can't update body name.");
        }
        if (!c2.v().isEmpty()) {
            throw new IllegalArgumentException("Excluded (for colliding) body IDs are not supported.");
        }
        if (Math.abs(a2.d() - c2.s()) > 0.0) {
            throw new IllegalArgumentException("Can't update body mass.");
        }
        double d2 = b2.c().b();
        if (!(c2 instanceof a)) {
            double d3;
            Double d4;
            if (!a2.r().equals(c2.c(), c2.d())) {
                a2.a(c2.c(), c2.d());
            }
            if (!NumberUtil.equals(c2.e(), a2.x())) {
                a2.k(c2.e());
            }
            if (!a2.u().equals(c2.f())) {
                a2.a(c2.f().copy());
            }
            if (!a2.v().equals(c2.g())) {
                a2.b(c2.g().copy());
            }
            if (!NumberUtil.equals(c2.h(), a2.y())) {
                a2.l(c2.h());
            }
            if (!NumberUtil.equals(c2.i(), a2.z())) {
                a2.m(c2.i());
            }
            if (!a2.w().equals(c2.j())) {
                a2.c(c2.j().copy());
            }
            if (!NumberUtil.equals(c2.k(), a2.A())) {
                a2.n(c2.k());
            }
            if (!NumberUtil.equals(a2.i(), c2.l())) {
                a2.b(c2.l());
            }
            if (!NumberUtil.equals(a2.j(), c2.m())) {
                a2.c(c2.m());
            }
            if (a2.k() instanceof com.a.a.b.e.b) {
                com.a.a.b.e.b b3 = (com.a.a.b.e.b)a2.k();
                d3 = b3.a();
                d4 = null;
            } else if (a2.k() instanceof com.a.a.b.e.a) {
                com.a.a.b.e.a a3 = (com.a.a.b.e.a)a2.k();
                d3 = a3.a();
                d4 = a3.b();
            } else {
                throw new IllegalArgumentException(String.format("Unsupported movement friction provider: %s.", a2.k()));
            }
            if (!NumberUtil.equals(d3, c2.n()) || !NumberUtil.equals(d4, c2.o())) {
                if (c2.o() == null) {
                    a2.d(c2.n());
                } else {
                    a2.a(new com.a.a.b.e.a(c2.n(), c2.o()));
                }
            }
            if (!NumberUtil.equals(a2.l(), c2.p())) {
                a2.f(c2.p());
            }
            if (!NumberUtil.equals(a2.m(), c2.q())) {
                a2.g(c2.q());
            }
            if (!NumberUtil.equals(a2.n(), c2.r())) {
                a2.h(c2.r());
            }
        }
        if (!(c3 = c2.t()).a(e.a(a2.c(), a2.r(), a2.x(), d2), d2)) {
            a2.a(e.a(c3));
            if (c3 instanceof d) {
                d d5 = (d)c3;
                a2.a((d5.a() + d5.c()) / 2.0, (d5.b() + d5.f()) / 2.0);
                a2.k(new Vector2D(d5.a(), d5.b(), d5.c(), d5.f()).getAngle());
                c2.a(a2.s());
                c2.b(a2.t());
                c2.c(a2.x());
            }
        }
    }

    private static com.a.a.b.c.c a(com.a.c.a.c c2) {
        if (c2 == null) {
            return null;
        }
        if (c2 instanceof com.a.c.a.b) {
            com.a.c.a.b b2 = (com.a.c.a.b)c2;
            return new com.a.a.b.c.b(b2.a());
        }
        if (c2 instanceof com.a.c.a.e) {
            com.a.c.a.e e2 = (com.a.c.a.e)c2;
            return new com.a.a.b.c.e(e2.a(), e2.b());
        }
        if (c2 instanceof d) {
            d d2 = (d)c2;
            return new com.a.a.b.c.d(Math.hypot(d2.c() - d2.a(), d2.f() - d2.b()), c2.e());
        }
        if (c2 instanceof com.a.c.a.a) {
            com.a.c.a.a a2 = (com.a.c.a.a)c2;
            return new com.a.a.b.c.a(a2.a(), a2.b(), a2.c(), c2.e());
        }
        throw new IllegalArgumentException("Unsupported form: " + c2 + '.');
    }

    private static com.a.c.a.c a(com.a.a.b.c.c c2, Point2D point2D, double d2, double d3) {
        if (c2 == null) {
            return null;
        }
        switch (c2.e()) {
            case a: {
                com.a.a.b.c.b b2 = (com.a.a.b.c.b)c2;
                return new com.a.c.a.b(b2.a());
            }
            case b: {
                com.a.a.b.c.e e2 = (com.a.a.b.c.e)c2;
                return new com.a.c.a.e(e2.a(), e2.b());
            }
            case c: {
                com.a.a.b.c.d d4 = (com.a.a.b.c.d)c2;
                d d5 = new d(d4.a(point2D, d2, d3), d4.b(point2D, d2, d3));
                d5.a(d4.f());
                return d5;
            }
            case d: {
                com.a.a.b.c.a a2 = (com.a.a.b.c.a)c2;
                com.a.c.a.a a3 = new com.a.c.a.a(a2.a(), a2.b(), a2.c());
                a3.a(a2.f());
                return a3;
            }
        }
        throw new IllegalArgumentException("Unsupported form: " + c2 + '.');
    }
}

