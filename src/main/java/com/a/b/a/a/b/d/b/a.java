/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.d.b;

import com.a.b.a.a.b.d.d;
import com.a.b.a.a.c.e;
import com.a.b.a.a.c.l;
import com.a.b.a.a.c.n;
import com.a.c.a.b;
import com.a.c.c;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.reflection.Name;
import com.codeforces.commons.text.StringUtil;

public abstract class a
extends d {
    private final e a;
    private final double b;
    private final int c;
    private final int d;
    @Name(value="lane")
    private final n e;
    private Integer f;

    protected a(double d2, double d3, double d4, l l2, e e2, double d5, double d6, double d7, int n2, int n3, n n4) {
        super(new b(d4), l2, d5, d6);
        if (Double.isNaN(d2) || Double.isInfinite(d2)) {
            throw new IllegalArgumentException("Argument 'x' is not a valid number.");
        }
        if (Double.isNaN(d3) || Double.isInfinite(d3)) {
            throw new IllegalArgumentException("Argument 'y' is not a valid number.");
        }
        if (Double.isNaN(d4) || Double.isInfinite(d4)) {
            throw new IllegalArgumentException("Argument 'radius' is not a valid number.");
        }
        this.a = e2;
        this.b = d7;
        this.c = n2;
        this.d = n3;
        this.e = n4;
        this.b().a(d2);
        this.b().b(d3);
        this.b().a(true);
    }

    public e h() {
        return this.a;
    }

    public double i() {
        return this.b;
    }

    public int s() {
        return this.c;
    }

    public int t() {
        return this.d;
    }

    public void a(int n2) {
        this.f = n2;
    }

    public int c(int n2) {
        if (this.f == null) {
            return 0;
        }
        return Math.max(com.a.b.a.a.b.e.d.a(this.a) - n2 + this.f, 0);
    }

    @Override
    public String toString() {
        return StringUtil.toString((Object)this, true, "id", "faction", "lane");
    }
}

