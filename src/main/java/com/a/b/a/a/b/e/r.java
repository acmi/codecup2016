/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.e;

import com.a.b.a.a.b.d.b.a;
import com.a.b.a.a.b.d.d.e;
import com.a.b.a.a.b.e.d;
import com.a.b.a.a.b.e.l;
import com.a.b.a.a.b.e.o;
import com.a.b.a.a.b.e.u;
import com.a.b.a.a.b.e.v;
import com.a.b.a.a.b.e.x;
import com.a.b.a.a.b.f;
import com.a.b.a.a.c.A;
import com.a.b.a.a.c.w;
import com.a.b.g;
import com.a.c.a.b;
import com.a.c.a.c;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.math.Math;
import java.util.List;
import java.util.Map;

public final class r {
    public static com.a.b.a.a.c.v a(e e2, double d2, boolean bl) {
        com.a.c.c c2 = e2.b();
        c c3 = c2.t();
        if (!(c3 instanceof b)) {
            throw new IllegalArgumentException("Unsupported projectile form: " + c3 + '.');
        }
        b b2 = (b)c3;
        Vector2D vector2D = c2.f();
        return new com.a.b.a.a.c.v(e2.a(), bl ? 4000.0 - c2.c() : c2.c(), bl ? 4000.0 - c2.d() : c2.d(), (bl ? - vector2D.getX() : vector2D.getX()) * d2, (bl ? - vector2D.getY() : vector2D.getY()) * d2, bl ? c2.e() + 3.141592653589793 : c2.e(), e2.c(), b2.a(), e2.j(), e2.h().a(), e2.i() == null ? -1 : e2.i().a());
    }

    public static int a(com.a.b.a.a.b.d.f.a a2, w w2) {
        switch (w2) {
            case MAGIC_MISSILE: {
                return 12;
            }
            case FROST_BOLT: {
                return 36;
            }
            case FIREBALL: {
                return 48;
            }
        }
        throw new IllegalArgumentException("Unsupported projectile type: " + (Object)((Object)w2) + '.');
    }

    public static double a(com.a.b.a.a.b.d.b b2, com.a.b.a.a.b.d.b b3, Map<com.a.b.a.a.c.l, List<com.a.b.a.a.b.d.f.a>> map, w w2) {
        double d2;
        switch (w2) {
            case MAGIC_MISSILE: {
                d2 = 12.0;
                break;
            }
            case FROST_BOLT: {
                d2 = 24.0;
                break;
            }
            case FIREBALL: {
                return 0.0;
            }
            case DART: {
                d2 = 6.0;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported projectile type: " + (Object)((Object)w2) + '.');
            }
        }
        if (b2.b(A.EMPOWERED)) {
            d2 *= 2.0;
        }
        if (b3.b(A.SHIELDED)) {
            d2 *= 0.75;
        }
        if (w2 != w.DART) {
            if (b2 instanceof com.a.b.a.a.b.d.f.a) {
                d2 = x.a((com.a.b.a.a.b.d.f.a)b2, d2, map);
            }
            if (b3 instanceof com.a.b.a.a.b.d.f.a) {
                d2 = x.b((com.a.b.a.a.b.d.f.a)b3, d2, map);
            }
        }
        return Math.max(d2, 0.0);
    }

    public static void a(com.a.b.a.a.a.b b2, g g2, com.a.b.a.a.b.d.d.b b3, List<com.a.b.a.a.b.d.f.a> list, Map<com.a.b.a.a.c.l, List<com.a.b.a.a.b.d.f.a>> map, Map<com.a.b.a.a.c.l, d.a> map2) {
        boolean bl = b3.h().b(A.EMPOWERED);
        for (com.a.b.d d2 : g2.a()) {
            double d3;
            if (!(d2 instanceof com.a.b.a.a.b.d.b) || (d3 = b3.b(d2) - l.a(d2)) > 100.0) continue;
            com.a.b.a.a.b.d.b b4 = (com.a.b.a.a.b.d.b)d2;
            double d4 = 24.0;
            if (d3 > 20.0) {
                d4 -= 12.0 * (d3 - 20.0) / 80.0;
            }
            if (bl) {
                d4 *= 2.0;
            }
            if (b4.b(A.SHIELDED)) {
                d4 *= 0.75;
            }
            if (b3.h() instanceof com.a.b.a.a.b.d.f.a) {
                d4 = x.a((com.a.b.a.a.b.d.f.a)b3.h(), d4, map);
            }
            if (d2 instanceof com.a.b.a.a.b.d.e.a) {
                u.a(g2, (com.a.b.a.a.b.d.e.a)d2, d4);
                if (u.b((com.a.b.a.a.b.d.e.a)d2)) continue;
                b4.a(A.BURNING, b3.h(), 240);
                continue;
            }
            if (d2 instanceof com.a.b.a.a.b.d.f.a) {
                com.a.b.a.a.b.d.f.a a2 = (com.a.b.a.a.b.d.f.a)d2;
                d4 = x.b(a2, d4, map);
                x.a(b2, g2, a2, b3.i(), list, map.get((Object)v.b(d2)), d4);
                if (x.a(a2)) continue;
                b4.a(A.BURNING, b3.h(), 240);
                continue;
            }
            if (d2 instanceof com.a.b.a.a.b.d.c.b) {
                o.a(g2, (com.a.b.a.a.b.d.c.b)d2, b3.i(), list, d4);
                if (o.b((com.a.b.a.a.b.d.c.b)d2) || b3.i() != null && d2.c() == b3.i().k()) continue;
                b4.a(A.BURNING, b3.h(), 240);
                continue;
            }
            if (d2 instanceof a) {
                d.a(g2, (a)d2, b3.i(), list, d4, map2.get((Object)d2.c()));
                if (d.a((a)d2) || b3.i() != null && d2.c() == b3.i().k()) continue;
                b4.a(A.BURNING, b3.h(), 240);
                continue;
            }
            throw new IllegalArgumentException("Unsupported living unit class: " + d2.getClass() + '.');
        }
    }
}

