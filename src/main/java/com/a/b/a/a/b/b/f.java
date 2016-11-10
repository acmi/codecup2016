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
import com.a.b.a.a.b.e.u;
import com.a.b.a.a.b.e.v;
import com.a.b.a.a.c.A;
import com.a.b.a.a.c.l;
import com.a.b.a.a.c.n;
import com.a.b.a.a.c.r;
import com.a.b.b;
import com.a.b.d;
import com.a.b.g;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.math.Math;
import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class f
implements b {
    private static final double[] a = new double[8];
    private final com.a.b.a.a.a.b b;
    private final Map<l, List<com.a.b.a.a.b.d.f.a>> c;
    private final d.a d;
    private final d.a e;

    public f(com.a.b.a.a.a.b b2, Map<l, List<com.a.b.a.a.b.d.f.a>> map, d.a a2, d.a a3) {
        this.b = b2;
        this.c = Collections.unmodifiableMap(map);
        this.d = a2;
        this.e = a3;
    }

    @Override
    public void a(g g2, int n2) {
        List<d> list = g2.a();
        d[] arrd = m.a(list);
        com.a.b.a.a.b.d.e.a[] arra = u.a(arrd);
        com.a.b.a.a.b.d.c.b[] arrb = o.a(arrd);
        c.a(arrb);
        int n3 = arrb.length;
        while (--n3 >= 0) {
            double d2;
            double d3;
            double d4;
            double d5;
            double d6;
            com.a.b.a.a.b.d.c.b b2 = arrb[n3];
            b.a a2 = f.a(b2);
            if (b2.b(A.FROZEN) || a2 == null || o.b(b2)) continue;
            if (a2.a() != b.b.a) {
                throw new IllegalArgumentException("Unsupported minion order type: " + (Object)((Object)a2.a()) + '.');
            }
            Double d7 = null;
            double d8 = o.b(b2.h());
            com.a.b.a.a.b.d.b b3 = f.a(b2, (com.a.b.a.a.b.d.b[])arrd, false);
            if (b3 == null && b2.h() == r.ORC_WOODCUTTER && b2.c() == l.NEUTRAL) {
                b3 = f.a(b2, (com.a.b.a.a.b.d.b[])arrd, true);
            }
            if (b3 == null) {
                d d9 = a2.b();
                if (d9 == null) {
                    d3 = Preconditions.checkNotNull(a2.c());
                    d2 = Preconditions.checkNotNull(a2.d());
                } else {
                    d3 = d9.b().c();
                    d2 = d9.b().d();
                }
                this.a(b2, g2, n2, arra);
            } else {
                d4 = b2.b(b3);
                double d10 = b2.a(b3);
                d5 = com.a.b.a.a.b.e.l.a(b3);
                switch (b2.h()) {
                    case ORC_WOODCUTTER: {
                        d7 = 50.0 + d5;
                        break;
                    }
                    case FETISH_BLOWDART: {
                        d7 = 300.0;
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Unsupported minion type: " + (Object)((Object)b2.h()) + '.');
                    }
                }
                if (d7 >= d4) {
                    f.a(b2, b3.b().c(), b3.b().d());
                    if (b2.c(n2) > 0 || Math.abs(d10) >= d8 / 2.0) continue;
                    switch (b2.h()) {
                        case ORC_WOODCUTTER: {
                            this.a((com.a.b.a.a.b.d.c.c)b2, g2);
                            break;
                        }
                        case FETISH_BLOWDART: {
                            d6 = 300.0;
                            double d11 = d4 - d5 + 5.0 + 1.0E-7;
                            d11 = Math.max(Math.min(d11, d6), 0.0);
                            g2.a(new com.a.b.a.a.b.d.d.a((a)b2, d11, d6, d10));
                            break;
                        }
                        default: {
                            throw new IllegalArgumentException("Unsupported minion type: " + (Object)((Object)b2.h()) + '.');
                        }
                    }
                    b2.a(n2);
                    continue;
                }
                this.a(b2, g2, n2, arra);
                d3 = b3.b().c();
                d2 = b3.b().d();
            }
            d4 = f.a(g2, b2, d3, d2, d7);
            if (Double.isNaN(d4)) {
                f.a(b2, d3, d2);
                continue;
            }
            if (Math.abs(d4) < d8 / 2.0) {
                com.a.c.c c2 = b2.b();
                Vector2D vector2D = new Vector2D(3.0, 0.0).rotate(c2.e());
                d5 = c2.c();
                d6 = c2.d();
                c2.a(d5 + vector2D.getX());
                c2.b(d6 + vector2D.getY());
                g2.c(b2);
                if (!o.a(b2, g2, true)) {
                    c2.a(d5);
                    c2.b(d6);
                    g2.c(b2);
                }
            }
            f.a(b2, d4);
        }
    }

    private static b.a a(com.a.b.a.a.b.d.c.b b2) {
        if (b2.c() != l.ACADEMY && b2.c() != l.RENEGADES || b2.s() >= 2) {
            return b2.i();
        }
        if (b2.t() == n.TOP) {
            if (b2.s() == 1) {
                if (b2.c() == l.ACADEMY) {
                    if (b2.b().d() < 200.0) {
                        b2.a(new b.a(b.b.a, 4000.0, 200.0), 2);
                    }
                } else if (b2.b().c() < 200.0) {
                    b2.a(new b.a(b.b.a, 200.0, 4000.0), 2);
                }
            } else if (b2.c() == l.ACADEMY) {
                if (b2.b().d() < 600.0) {
                    b2.a(new b.a(b.b.a, 800.0, 0.0), 1);
                }
            } else if (b2.b().c() < 600.0) {
                b2.a(new b.a(b.b.a, 0.0, 800.0), 1);
            }
        } else if (b2.t() == n.BOTTOM) {
            if (b2.s() == 1) {
                if (b2.c() == l.ACADEMY) {
                    if (b2.b().c() > 3800.0) {
                        b2.a(new b.a(b.b.a, 3800.0, 0.0), 2);
                    }
                } else if (b2.b().d() > 3800.0) {
                    b2.a(new b.a(b.b.a, 0.0, 3800.0), 2);
                }
            } else if (b2.c() == l.ACADEMY) {
                if (b2.b().c() > 3400.0) {
                    b2.a(new b.a(b.b.a, 4000.0, 3200.0), 1);
                }
            } else if (b2.b().d() > 3400.0) {
                b2.a(new b.a(b.b.a, 3200.0, 4000.0), 1);
            }
        }
        return b2.i();
    }

    private static com.a.b.a.a.b.d.b a(com.a.b.a.a.b.d.c.b b2, com.a.b.a.a.b.d.b[] arrb, boolean bl) {
        com.a.b.a.a.b.d.b b3 = null;
        double d2 = Double.MAX_VALUE;
        int n2 = arrb.length;
        while (--n2 >= 0) {
            double d3;
            com.a.b.a.a.b.d.b b4 = arrb[n2];
            if (b4.c() == b2.c() || b4.c() == l.OTHER && (!bl || !com.a.b.a.a.b.d.e.a.class.isInstance(b4)) || b4 instanceof com.a.b.a.a.b.d.c.b && ((com.a.b.a.a.b.d.c.b)b4).i() == null || m.a(b4) || (d3 = b2.c(b4)) > b2.r() || d3 >= d2) continue;
            b3 = b4;
            d2 = d3;
        }
        return b3;
    }

    private static void a(com.a.b.a.a.b.d.c.b b2, double d2, double d3) {
        f.a(b2, b2.a(d2, d3));
    }

    private static void a(com.a.b.a.a.b.d.c.b b2, double d2) {
        if (Math.abs(d2 = Math.max(Math.min(d2, 0.10471975511965978), -0.10471975511965978)) < 1.0E-7) {
            return;
        }
        com.a.c.c c2 = b2.b();
        c2.c(com.a.b.a.a.b.e.l.a(c2.e() + d2));
    }

    private void a(com.a.b.a.a.b.d.c.b b2, g g2, int n2, com.a.b.a.a.b.d.e.a[] arra) {
        if (b2.h() != r.ORC_WOODCUTTER || b2.c(n2) > 0) {
            return;
        }
        if (!v.a((d)b2, arra, 0.5235987755982988, 50.0).isEmpty()) {
            this.a((com.a.b.a.a.b.d.c.c)b2, g2);
            b2.a(n2);
        }
    }

    private void a(com.a.b.a.a.b.d.c.c c2, g g2) {
        for (d d2 : v.a((d)c2, g2, 0.5235987755982988, 50.0)) {
            com.a.b.a.a.b.d.b b2;
            if (!(d2 instanceof com.a.b.a.a.b.d.b) || m.a(b2 = (com.a.b.a.a.b.d.b)d2)) continue;
            m.a(this.b, b2, g2, this.c.get((Object)c2.c()), this.c, 12.0, false, this.d, this.e);
        }
    }

    private static double a(g g2, com.a.b.a.a.b.d.c.b b2, double d2, double d3, Double d4) {
        double d5 = b2.a(d2, d3);
        for (int i2 = 61; i2 >= 1; i2 -= 2) {
            double d6 = f.a(g2, b2, d2, d3, d4, d5, i2);
            if (Double.isNaN(d6)) continue;
            return d6;
        }
        return Double.NaN;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static double a(g g2, com.a.b.a.a.b.d.c.b b2, double d2, double d3, Double d4, double d5, int n2) {
        double d6 = d4 == null ? Double.NaN : d4 * d4;
        com.a.c.c c2 = b2.b();
        Point2D point2D = new Point2D(c2.c(), c2.d());
        try {
            for (int i2 = 0; i2 < 8; ++i2) {
                double d7 = a[i2];
                double[] arrd = new double[]{com.a.b.a.a.b.e.l.a(d5 + d7), com.a.b.a.a.b.e.l.a(d5 - d7)};
                int n3 = arrd.length;
                while (--n3 >= 0) {
                    double d8 = arrd[n3];
                    boolean bl = true;
                    Vector2D vector2D = new Vector2D(3.0, 0.0).rotate(b2.b().e() + d8);
                    for (int i3 = 1; i3 <= n2; ++i3) {
                        Point2D point2D2 = point2D.copy().add(vector2D.copy().multiply(i3));
                        c2.a(point2D2.getX());
                        c2.b(point2D2.getY());
                        g2.c(b2);
                        if (!o.a(b2, g2, true)) {
                            bl = false;
                            break;
                        }
                        if (d4 != null && point2D2.getSquaredDistanceTo(d2, d3) <= d6) break;
                    }
                    if (!bl) continue;
                    double d9 = d8;
                    return d9;
                }
            }
            double d10 = Double.NaN;
            return d10;
        }
        finally {
            c2.a(point2D.getX());
            c2.b(point2D.getY());
            g2.c(b2);
        }
    }

    static {
        for (int i2 = 0; i2 < 8; ++i2) {
            f.a[i2] = 3.141592653589793 * (double)i2 / 8.0;
        }
    }
}

