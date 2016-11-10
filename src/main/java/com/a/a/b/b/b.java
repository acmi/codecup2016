/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.b;

import com.a.a.b.a;
import com.a.a.b.b.e;
import com.a.a.b.b.f;
import com.a.a.b.c.c;
import com.codeforces.commons.geometry.Line2D;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.math.Math;

public class b
extends e {
    public b(double d2) {
        super(d2);
    }

    @Override
    protected boolean a(a a2, a a3) {
        return a2.c().e() == com.a.a.b.c.f.d && a3.c().e() == com.a.a.b.c.f.a;
    }

    @Override
    protected f b(a a2, a a3) {
        com.a.a.b.c.a a4 = (com.a.a.b.c.a)a2.c();
        com.a.a.b.c.b b2 = (com.a.a.b.c.b)a3.c();
        double d2 = a4.a();
        double d3 = b2.a();
        double d4 = a2.r().getDistanceTo(a3.r());
        if (d4 > d2 + d3) {
            return null;
        }
        if (d4 < Math.abs(d2 - d3)) {
            return null;
        }
        a2.C();
        a3.C();
        double d5 = a2.x() + a4.b();
        double d6 = d5 + a4.c();
        f f2 = this.a(a2, a3, d2, d3, d4, d5, d6);
        if (f2 != null) {
            return f2;
        }
        if (d4 >= this.a) {
            double d7 = Math.sqrt((d4 + d2 + d3) * (d4 + d2 - d3) * (d4 - d2 + d3) * (- d4 + d2 + d3)) / 4.0;
            double d8 = a2.r().getSquaredDistanceTo(a3.r());
            double d9 = Math.sqr(d2) - Math.sqr(d3);
            double d10 = (a2.s() + a3.s()) / 2.0 + (a3.s() - a2.s()) * d9 / (2.0 * d8);
            double d11 = (a2.t() + a3.t()) / 2.0 + (a3.t() - a2.t()) * d9 / (2.0 * d8);
            double d12 = 2.0 * (a2.t() - a3.t()) * d7 / d8;
            double d13 = 2.0 * (a2.s() - a3.s()) * d7 / d8;
            Point2D point2D = new Point2D(d10, d11);
            if (Math.abs(d12) < this.a && Math.abs(d13) < this.a) {
                double d14 = new Vector2D(a2.r(), point2D).getAngle();
                if (d14 < d5) {
                    d14 += 6.283185307179586;
                }
                if (d14 >= d5 && d14 <= d6) {
                    return new f(a2, a3, point2D, new Vector2D(a3.r(), point2D).normalize(), d3 - a3.a(point2D), this.a);
                }
            } else {
                double d15;
                Point2D point2D2 = point2D.copy().add(d12, - d13);
                Point2D point2D3 = point2D.copy().add(- d12, d13);
                double d16 = new Vector2D(a2.r(), point2D2).getAngle();
                if (d16 < d5) {
                    d16 += 6.283185307179586;
                }
                if ((d15 = new Vector2D(a2.r(), point2D3).getAngle()) < d5) {
                    d15 += 6.283185307179586;
                }
                if (d16 >= d5 && d16 <= d6 && d15 >= d5 && d15 <= d6) {
                    if (d4 > d2 - this.a) {
                        return new f(a2, a3, point2D, new Vector2D(a3.r(), a2.r()).normalize(), d2 + d3 - d4, this.a);
                    }
                    return new f(a2, a3, point2D, new Vector2D(a2.r(), a3.r()).normalize(), d4 + d3 - d2, this.a);
                }
            }
            return null;
        }
        return this.a(a3, a2, a4, d2, d5, d6, d3);
    }

    private f a(a a2, a a3, double d2, double d3, double d4, double d5, double d6) {
        Point2D point2D = a2.r().copy().add(new Vector2D(d2, 0.0).setAngle(d5));
        Point2D point2D2 = a2.r().copy().add(new Vector2D(d2, 0.0).setAngle(d6));
        double d7 = a3.a(point2D);
        double d8 = a3.a(point2D2);
        if (d7 <= d3 && d8 <= d3) {
            Line2D line2D;
            Vector2D vector2D;
            Point2D point2D3 = new Point2D((point2D.getX() + point2D2.getX()) / 2.0, (point2D.getY() + point2D2.getY()) / 2.0);
            if (a3.a(point2D3) >= this.a) {
                vector2D = new Vector2D(a3.r(), point2D3).normalize();
                line2D = Line2D.getLineByTwoPoints(a3.r(), point2D3);
            } else {
                vector2D = new Vector2D(a3.r(), a2.r()).normalize();
                line2D = Line2D.getLineByTwoPoints(a3.r(), a2.r());
            }
            Point2D point2D4 = line2D.getProjectionOf(point2D, this.a);
            double d9 = line2D.getDistanceFrom(point2D);
            double d10 = Math.sqrt(Math.sqr(d3) - Math.sqr(d9)) - a3.a(point2D4);
            Point2D point2D5 = line2D.getProjectionOf(point2D2, this.a);
            double d11 = line2D.getDistanceFrom(point2D2);
            double d12 = Math.sqrt(Math.sqr(d3) - Math.sqr(d11)) - a3.a(point2D5);
            return new f(a2, a3, point2D3, vector2D, Math.max(d10, d12), this.a);
        }
        if (d7 <= d3) {
            if (d7 >= this.a) {
                return new f(a2, a3, point2D, new Vector2D(a3.r(), point2D).normalize(), d3 - d7, this.a);
            }
            return new f(a2, a3, point2D, new Vector2D(a3.r(), a2.r()).normalize(), d2 + d3 - d4, this.a);
        }
        if (d8 <= d3) {
            if (d8 >= this.a) {
                return new f(a2, a3, point2D2, new Vector2D(a3.r(), point2D2).normalize(), d3 - d8, this.a);
            }
            return new f(a2, a3, point2D2, new Vector2D(a3.r(), a2.r()).normalize(), d2 + d3 - d4, this.a);
        }
        return null;
    }

    private f a(a a2, a a3, com.a.a.b.c.a a4, double d2, double d3, double d4, double d5) {
        if (d5 >= d2) {
            Vector2D vector2D = a2.u().copy().subtract(a3.u());
            Vector2D vector2D2 = vector2D.getLength() >= this.a && com.a.a.b.f.a.a(vector2D.getAngle(), d3, d4) ? vector2D.normalize() : (a2.u().getLength() >= this.a && com.a.a.b.f.a.a(a2.u().getAngle(), d3, d4) ? a2.u().copy().normalize() : new Vector2D(1.0, 0.0).setAngle(a3.x() + a4.b() + a4.c() / 2.0));
            return new f(a3, a2, a2.r().copy(), vector2D2, d5 - d2, this.a);
        }
        return null;
    }
}

