/*
 * Decompiled with CFR 0_119.
 */
package com.a.c.a;

import com.a.c.a.c;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.text.StringUtil;

public class e
extends c {
    private double a;
    private double b;

    public e(double d2, double d3) {
        this.a = d2;
        this.b = d3;
    }

    public e(e e2) {
        this.a = e2.a;
        this.b = e2.b;
    }

    public double a() {
        return this.a;
    }

    public double b() {
        return this.b;
    }

    @Override
    public c d() {
        return new e(this);
    }

    public String toString() {
        return StringUtil.toString((Object)this, false, new String[0]);
    }

    @Override
    public boolean a(c c2, double d2) {
        if (c2 == null || this.getClass() != c2.getClass()) {
            return false;
        }
        e e2 = (e)c2;
        return Math.abs(this.a - e2.a) < d2 && Math.abs(this.b - e2.b) < d2;
    }
}

