/*
 * Decompiled with CFR 0_119.
 */
package com.a.c.a;

import com.a.c.a.c;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.text.StringUtil;

public class a
extends c {
    private double a;
    private double b;
    private double c;

    public a(double d2, double d3, double d4) {
        this.a = d2;
        this.b = d3;
        this.c = d4;
    }

    public a(a a2) {
        this.a = a2.a;
        this.b = a2.b;
        this.c = a2.c;
    }

    public double a() {
        return this.a;
    }

    public double b() {
        return this.b;
    }

    public double c() {
        return this.c;
    }

    @Override
    public c d() {
        return new a(this);
    }

    public String toString() {
        return StringUtil.toString((Object)this, false, new String[0]);
    }

    @Override
    public boolean a(c c2, double d2) {
        if (c2 == null || this.getClass() != c2.getClass()) {
            return false;
        }
        a a2 = (a)c2;
        return Math.abs(this.a - a2.a) < d2 && Math.abs(this.c - a2.c) < d2;
    }
}

