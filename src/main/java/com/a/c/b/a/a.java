/*
 * Decompiled with CFR 0_119.
 */
package com.a.c.b.a;

import com.a.a.b.e.b;
import com.a.a.b.e.d;
import com.a.c.c;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.reflection.Name;

final class a
extends c {
    @Name(value="body")
    private final com.a.a.b.a a;

    a(long l2, com.a.a.b.a a2, String string, double d2, com.a.c.a.c c2, boolean bl) {
        super(l2);
        this.a = a2;
        this.a(string);
        this.d(d2);
        this.a(c2 == null ? null : c2.d());
        this.a(bl);
    }

    @Override
    public double c() {
        return this.a.s();
    }

    @Override
    public void a(double d2) {
        this.a.i(d2);
    }

    @Override
    public double d() {
        return this.a.t();
    }

    @Override
    public void b(double d2) {
        this.a.j(d2);
    }

    @Override
    public double e() {
        return this.a.x();
    }

    @Override
    public void c(double d2) {
        this.a.k(d2);
        this.a.C();
    }

    @Override
    public Vector2D f() {
        return this.a.u();
    }

    @Override
    public void a(Vector2D vector2D) {
        this.a.a(vector2D);
    }

    @Override
    public Vector2D g() {
        return this.a.v();
    }

    @Override
    public double h() {
        return this.a.y();
    }

    @Override
    public double i() {
        return this.a.z();
    }

    @Override
    public Vector2D j() {
        return this.a.w();
    }

    @Override
    public double k() {
        return this.a.A();
    }

    @Override
    public double l() {
        return this.a.i();
    }

    @Override
    public double m() {
        return this.a.j();
    }

    @Override
    public double n() {
        d d2 = this.a.k();
        if (d2 instanceof b) {
            return ((b)d2).a();
        }
        if (d2 instanceof com.a.a.b.e.a) {
            return ((com.a.a.b.e.a)d2).a();
        }
        throw new IllegalArgumentException(String.format("Unsupported movement friction provider: %s.", d2));
    }

    @Override
    public Double o() {
        d d2 = this.a.k();
        if (d2 instanceof b) {
            return null;
        }
        if (d2 instanceof com.a.a.b.e.a) {
            return ((com.a.a.b.e.a)d2).b();
        }
        throw new IllegalArgumentException(String.format("Unsupported movement friction provider: %s.", d2));
    }

    @Override
    public double p() {
        return this.a.l();
    }

    @Override
    public double q() {
        return this.a.m();
    }

    @Override
    public double r() {
        return this.a.n();
    }
}

