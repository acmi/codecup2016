/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.d.d;

import com.a.b.a.a.b.d.b;
import com.a.b.a.a.b.d.f.a;
import com.a.b.a.a.b.f;
import com.a.b.a.a.c.l;
import com.a.b.a.a.c.w;
import com.a.b.d;
import com.a.c.c;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.reflection.Name;

public abstract class e
extends d {
    @Name(value="owner")
    private final b a;
    @Name(value="type")
    private final w b;
    @Name(value="creationX")
    private final double c;
    @Name(value="creationY")
    private final double d;
    @Name(value="minRange")
    private final double e;
    @Name(value="maxRange")
    private final double f;

    protected e(double d2, b b2, w w2, double d3, double d4, double d5, double d6) {
        super(new com.a.c.a.b(d2), b2.c());
        c c2 = b2.b();
        double d7 = com.a.b.a.a.b.e.l.a(c2.e() + d6);
        this.a = b2;
        this.b = w2;
        this.c = c2.c();
        this.d = c2.d();
        this.e = d3;
        this.f = d4;
        this.b().a(c2.c());
        this.b().b(c2.d());
        this.b().c(d7);
        this.b().d(1.0);
        this.b().a(new Vector2D(d5, 0.0).rotate(d7));
    }

    public b h() {
        return this.a;
    }

    public f i() {
        return this.a instanceof a ? ((a)this.a).s() : null;
    }

    public w j() {
        return this.b;
    }

    public double k() {
        return this.c;
    }

    public double l() {
        return this.d;
    }

    public double m() {
        return this.e;
    }

    public double n() {
        return this.f;
    }

    public final double o() {
        return this.b(this.c, this.d);
    }
}

