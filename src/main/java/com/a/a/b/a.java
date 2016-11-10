/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b;

import com.a.a.b.b;
import com.a.a.b.c.c;
import com.a.a.b.e.d;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.text.StringUtil;
import java.util.concurrent.atomic.AtomicLong;

public class a {
    private static final AtomicLong a = new AtomicLong();
    private final long b = a.incrementAndGet();
    private String c;
    private c d;
    private double e;
    private double f;
    private double g;
    private double h;
    private d i = new com.a.a.b.e.b(0.0);
    private double j;
    private double k = 1.0;
    private double l;
    private final b m = new b();
    private b n;
    private b o;
    private double p;
    private double q;
    private Double r;
    private double s;
    private double t;
    private Double u;
    private final int v = Long.hashCode(this.b);

    public long a() {
        return this.b;
    }

    public String b() {
        return this.c;
    }

    public void a(String string) {
        this.c = string;
    }

    public c c() {
        return this.d;
    }

    public void a(c c2) {
        this.d = c2;
    }

    public double d() {
        return this.e;
    }

    public void a(double d2) {
        if (Double.isNaN(d2) || d2 == Double.NEGATIVE_INFINITY || d2 <= 0.0) {
            throw new IllegalArgumentException(this + ": argument 'mass' should be positive.");
        }
        this.e = d2;
        this.f = Double.isInfinite(d2) ? 0.0 : 1.0 / d2;
    }

    public boolean e() {
        return Double.isInfinite(this.e);
    }

    public double f() {
        return this.f;
    }

    public double g() {
        if (Double.isNaN(this.e) || this.e == Double.NEGATIVE_INFINITY || this.e <= 0.0) {
            throw new IllegalStateException(this + ": field 'mass' should be positive.");
        }
        if (Double.isInfinite(this.e)) {
            return Double.POSITIVE_INFINITY;
        }
        return this.d.a(this.e);
    }

    public double h() {
        double d2 = this.g();
        if (Double.isInfinite(d2)) {
            return 0.0;
        }
        return 1.0 / d2;
    }

    public double i() {
        return this.g;
    }

    public void b(double d2) {
        if (Double.isNaN(d2) || Double.isInfinite(d2) || d2 < 0.0 || d2 > 1.0) {
            throw new IllegalArgumentException(String.format("%s: argument 'movementAirFrictionFactor' should be between 0.0 and 1.0 both inclusive but got %s.", this, d2));
        }
        this.g = d2;
    }

    public double j() {
        return this.h;
    }

    public void c(double d2) {
        if (Double.isNaN(d2) || Double.isInfinite(d2) || d2 < 0.0 || d2 > 1.0) {
            throw new IllegalArgumentException(String.format("%s: argument 'rotationAirFrictionFactor' should be between 0.0 and 1.0 both inclusive but got %s.", this, d2));
        }
        this.h = d2;
    }

    public d k() {
        return this.i;
    }

    public void a(d d2) {
        if (d2 == null) {
            throw new IllegalArgumentException(String.format("%s: argument 'movementFrictionProvider' is null.", this));
        }
        this.i = d2;
    }

    public void d(double d2) {
        this.a(new com.a.a.b.e.b(d2));
    }

    public void e(double d2) {
        this.i.a(this, d2);
    }

    public double l() {
        return this.j;
    }

    public void f(double d2) {
        if (Double.isNaN(d2) || d2 < 0.0) {
            throw new IllegalArgumentException(String.format("%s: argument 'rotationFrictionFactor' should be zero or positive but got %s.", this, d2));
        }
        this.j = d2;
    }

    public double m() {
        return this.k;
    }

    public void g(double d2) {
        if (Double.isNaN(d2) || Double.isInfinite(d2) || d2 < 0.0 || d2 > 1.0) {
            throw new IllegalArgumentException(String.format("%s: argument 'momentumTransferFactor' should be between 0.0 and 1.0 both inclusive but got %s.", this, d2));
        }
        this.k = d2;
    }

    public double n() {
        return this.l;
    }

    public void h(double d2) {
        if (Double.isNaN(d2) || Double.isInfinite(d2) || d2 < 0.0 || d2 > 1.0) {
            throw new IllegalArgumentException(String.format("%s: argument 'surfaceFrictionFactor' should be between 0.0 and 1.0 both inclusive but got %s.", this, d2));
        }
        this.l = d2;
    }

    public b o() {
        return this.m;
    }

    public void p() {
        this.n = new b(this.m);
    }

    public void q() {
        this.o = new b(this.m);
    }

    public Point2D r() {
        return this.m.g();
    }

    public void a(double d2, double d3) {
        Point2D point2D = this.m.g();
        if (point2D == null) {
            this.m.a(new Point2D(d2, d3));
        } else {
            point2D.setX(d2);
            point2D.setY(d3);
        }
    }

    public double s() {
        Point2D point2D = this.m.g();
        return point2D == null ? 0.0 : point2D.getX();
    }

    public void i(double d2) {
        Point2D point2D = this.m.g();
        if (point2D == null) {
            this.m.a(new Point2D(d2, 0.0));
        } else {
            point2D.setX(d2);
        }
    }

    public double t() {
        Point2D point2D = this.m.g();
        return point2D == null ? 0.0 : point2D.getY();
    }

    public void j(double d2) {
        Point2D point2D = this.m.g();
        if (point2D == null) {
            this.m.a(new Point2D(0.0, d2));
        } else {
            point2D.setY(d2);
        }
    }

    public Vector2D u() {
        return this.m.a();
    }

    public void a(Vector2D vector2D) {
        this.m.a(vector2D);
    }

    public void b(double d2, double d3) {
        Vector2D vector2D = this.m.a();
        if (vector2D == null) {
            this.m.a(new Vector2D(d2, d3));
        } else {
            vector2D.setX(d2);
            vector2D.setY(d3);
        }
    }

    public Vector2D v() {
        return this.m.b();
    }

    public void b(Vector2D vector2D) {
        this.m.b(vector2D);
    }

    public Vector2D w() {
        return this.m.c();
    }

    public void c(Vector2D vector2D) {
        this.m.c(vector2D);
    }

    public void c(double d2, double d3) {
        Vector2D vector2D = this.m.c();
        if (vector2D == null) {
            this.m.c(new Vector2D(d2, d3));
        } else {
            vector2D.setX(d2);
            vector2D.setY(d3);
        }
    }

    public double x() {
        return this.m.h();
    }

    public void k(double d2) {
        this.m.d(d2);
    }

    public double y() {
        return this.m.d();
    }

    public void l(double d2) {
        this.m.a(d2);
    }

    public double z() {
        return this.m.e();
    }

    public void m(double d2) {
        this.m.b(d2);
    }

    public double A() {
        return this.m.f();
    }

    public void n(double d2) {
        this.m.c(d2);
    }

    public double a(Point2D point2D) {
        return this.m.g().getDistanceTo(point2D);
    }

    public double a(a a2) {
        return this.m.g().getSquaredDistanceTo(a2.m.g());
    }

    public Point2D B() {
        Point2D point2D = this.m.g();
        if (point2D == null) {
            throw new IllegalStateException("Can't calculate center of mass for body with no position.");
        }
        if (this.d == null) {
            throw new IllegalStateException("Can't calculate center of mass for body with no form.");
        }
        return this.d.a(this);
    }

    public void C() {
        this.m.i();
    }

    void o(double d2) {
        if (this.r == null || this.g != this.p || d2 != this.q) {
            this.p = this.g;
            this.q = d2;
            this.r = Math.pow(1.0 - this.g, d2);
        }
        this.u().subtract(this.v()).multiply(this.r).add(this.v());
    }

    void p(double d2) {
        if (this.u == null || this.h != this.s || d2 != this.t) {
            this.s = this.h;
            this.t = d2;
            this.u = Math.pow(1.0 - this.h, d2);
        }
        this.l((this.y() - this.z()) * this.u + this.z());
    }

    public boolean b(a a2) {
        return a2 != null && this.b == a2.b;
    }

    public int hashCode() {
        return this.v;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        a a2 = (a)object;
        return this.b == a2.b;
    }

    public String toString() {
        return a.c(this);
    }

    public static String c(a a2) {
        return StringUtil.toString(a.class, a2, true, "id", "name", "position", "angle", "velocity", "angularVelocity");
    }
}

