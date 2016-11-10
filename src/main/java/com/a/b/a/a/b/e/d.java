/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.e;

import com.a.b.a.a.b.d.b;
import com.a.b.a.a.b.e.t;
import com.a.b.a.a.b.e.v;
import com.a.b.a.a.b.f;
import com.a.b.a.a.c.e;
import com.a.b.a.a.c.l;
import com.a.b.a.a.c.z;
import com.a.b.g;
import com.a.c.a.b;
import com.a.c.a.c;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.math.NumberUtil;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class d {
    private static final Logger a = LoggerFactory.getLogger(d.class);

    private d() {
        throw new UnsupportedOperationException();
    }

    public static com.a.b.a.a.c.d a(com.a.b.a.a.b.d.b.a a2, int n2, boolean bl) {
        com.a.c.c c2 = a2.b();
        c c3 = c2.t();
        if (!(c3 instanceof b)) {
            throw new IllegalArgumentException("Unsupported building form: " + c3 + '.');
        }
        b b2 = (b)c3;
        return new com.a.b.a.a.c.d(a2.a(), bl ? 4000.0 - c2.c() : c2.c(), bl ? 4000.0 - c2.d() : c2.d(), 0.0, 0.0, bl ? c2.e() + 3.141592653589793 : c2.e(), a2.c(), b2.a(), d.a(a2) ? 0 : Math.max(NumberUtil.toInt(a2.j()), 1), NumberUtil.toInt(a2.k()), t.a(a2.l()), a2.h(), a2.q(), a2.i(), a2.s(), a2.t(), a2.c(n2));
    }

    public static void a(g g2, com.a.b.a.a.b.d.b.a a2, f f2, List<com.a.b.a.a.b.d.f.a> list, double d2, a a3) {
        if (d.a(a2) || d2 < 1.0E-7) {
            return;
        }
        if (a3.a(a2)) {
            return;
        }
        if (f2 != null && f2.k() == a2.c()) {
            return;
        }
        d2 = Math.min(a2.j(), d2);
        a2.b(a2.j() - d2);
        v.a((com.a.b.a.a.b.d.b)a2, f2, list, d2, 0.25);
        if (!d.a(a2)) {
            return;
        }
        if (f2 == null) {
            a.info(a2 + " has been destroyed by non-player unit.");
        } else {
            a.info(a2 + " has been destroyed by " + f2 + '.');
        }
        v.a((com.a.b.a.a.b.d.b)a2, null, f2, list, 0.25);
        a2.b(0.0);
        a2.b(0);
        a2.m();
        g2.b(a2);
    }

    public static boolean a(com.a.b.a.a.b.d.b.a a2) {
        return d.a(a2.j());
    }

    public static boolean a(double d2) {
        return d2 < 1.0E-7;
    }

    public static int a(e e2) {
        switch (e2) {
            case GUARDIAN_TOWER: {
                return 240;
            }
            case FACTION_BASE: {
                return 240;
            }
        }
        throw new IllegalStateException("Unsupported minion type: " + (Object)((Object)e2) + '.');
    }

    public static final class a {
        private com.a.b.a.a.b.d.b.b a;
        private com.a.b.a.a.b.d.b.c b;
        private com.a.b.a.a.b.d.b.c c;
        private com.a.b.a.a.b.d.b.c d;
        private com.a.b.a.a.b.d.b.c e;
        private com.a.b.a.a.b.d.b.c f;
        private com.a.b.a.a.b.d.b.c g;

        public com.a.b.a.a.b.d.b.b a() {
            return this.a;
        }

        public void a(com.a.b.a.a.b.d.b.b b2) {
            this.a = b2;
        }

        public void a(com.a.b.a.a.b.d.b.c c2) {
            this.b = c2;
        }

        public void b(com.a.b.a.a.b.d.b.c c2) {
            this.c = c2;
        }

        public void c(com.a.b.a.a.b.d.b.c c2) {
            this.d = c2;
        }

        public void d(com.a.b.a.a.b.d.b.c c2) {
            this.e = c2;
        }

        public void e(com.a.b.a.a.b.d.b.c c2) {
            this.f = c2;
        }

        public void f(com.a.b.a.a.b.d.b.c c2) {
            this.g = c2;
        }

        public boolean a(com.a.b.a.a.b.d.b.a a2) {
            if (a2.c() != this.b()) {
                throw new IllegalArgumentException("Can't check damage immunity for building of another faction.");
            }
            if (a2.equals(this.a)) {
                return !d.a(this.c) && !d.a(this.e) && !d.a(this.g);
            }
            if (a2.equals(this.c)) {
                return !d.a(this.b);
            }
            if (a2.equals(this.e)) {
                return !d.a(this.d);
            }
            if (a2.equals(this.g)) {
                return !d.a(this.f);
            }
            if (a2.equals(this.b) || a2.equals(this.d) || a2.equals(this.f)) {
                return false;
            }
            throw new IllegalArgumentException("Can't find specified building among the faction buildings.");
        }

        public l b() {
            if (this.a == null) {
                if (this.b == null && this.c == null && this.d == null && this.e == null && this.f == null && this.g == null) {
                    return null;
                }
            } else {
                l l2 = this.a.c();
                if (this.b != null && this.b.c() == l2 && this.c != null && this.c.c() == l2 && this.d != null && this.d.c() == l2 && this.e != null && this.e.c() == l2 && this.f != null && this.f.c() == l2 && this.g != null && this.g.c() == l2) {
                    return l2;
                }
            }
            throw new IllegalStateException("Can't get common faction of buildings.");
        }
    }

}

