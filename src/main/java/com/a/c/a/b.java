/*
 * Decompiled with CFR 0_119.
 */
package com.a.c.a;

import com.a.c.a.c;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.text.StringUtil;

public class b
extends c {
    private double a;

    public b(double d2) {
        this.a = d2;
    }

    public b(b b2) {
        this.a = b2.a;
    }

    public double a() {
        return this.a;
    }

    @Override
    public c d() {
        return new b(this);
    }

    public String toString() {
        return StringUtil.toString((Object)this, false, new String[0]);
    }

    @Override
    public boolean a(c c2, double d2) {
        if (c2 == null || this.getClass() != c2.getClass()) {
            return false;
        }
        b b2 = (b)c2;
        return Math.abs(this.a - b2.a) < d2;
    }
}

