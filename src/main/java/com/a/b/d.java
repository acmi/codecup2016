/*
 * Decompiled with CFR 0_119.
 */
package com.a.b;

import com.a.b.a.a.c.l;
import com.a.c.c;
import com.a.c.f;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.reflection.Name;
import com.codeforces.commons.text.StringUtil;
import java.util.concurrent.atomic.AtomicLong;

public abstract class d {
    private static final AtomicLong a = new AtomicLong();
    @Name(value="id")
    private final long b = a.incrementAndGet();
    @Name(value="body")
    private c c = new f(this.b);
    @Name(value="faction")
    private final l d;
    private Double e;
    private Double f;
    private Double g;
    private Vector2D h = new Vector2D(0.0, 0.0);

    protected d(com.a.c.a.c c2, l l2) {
        this.c.a(this.getClass().getSimpleName() + '#' + this.b);
        this.c.a(c2);
        this.d = l2;
    }

    public final long a() {
        return this.b;
    }

    public c b() {
        return this.c;
    }

    public void a(c c2) {
        this.c = c2;
    }

    public l c() {
        return this.d;
    }

    public Double d() {
        return this.e;
    }

    public void a(Double d2) {
        this.e = d2;
    }

    public Double e() {
        return this.f;
    }

    public void b(Double d2) {
        this.f = d2;
    }

    public Double f() {
        return this.g;
    }

    public void c(Double d2) {
        this.g = d2;
    }

    public void a(Vector2D vector2D) {
        this.h = vector2D;
    }

    public final Point2D g() {
        return new Point2D(this.c.c(), this.c.d());
    }

    public double a(double d2, double d3) {
        double d4 = com.a.b.a.a.b.e.l.a(this.c.c(), this.c.d(), d2, d3);
        return com.a.b.a.a.b.e.l.a(d4 - this.c.e());
    }

    public double a(d d2) {
        return this.a(d2.c.c(), d2.c.d());
    }

    public double b(double d2, double d3) {
        return Math.hypot(d2 - this.c.c(), d3 - this.c.d());
    }

    public double a(Point2D point2D) {
        return point2D.getDistanceTo(this.c.c(), this.c.d());
    }

    public double b(d d2) {
        return this.b(d2.c.c(), d2.c.d());
    }

    public double c(double d2, double d3) {
        return Math.sumSqr(d2 - this.c.c(), d3 - this.c.d());
    }

    public double c(d d2) {
        return this.c(d2.c.c(), d2.c.d());
    }

    public final boolean equals(Object object) {
        return this == object || object != null && this.getClass() == object.getClass() && this.b == ((d)object).b;
    }

    public final int hashCode() {
        return Long.hashCode(this.b);
    }

    public String toString() {
        return StringUtil.toString((Object)this, false, "id", "body.name");
    }
}

