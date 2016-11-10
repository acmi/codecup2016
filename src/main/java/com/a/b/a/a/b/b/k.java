/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.b;

import com.a.b.a.a.b.d.f.a;
import com.a.b.a.a.b.e.x;
import com.a.b.b;
import com.a.b.d;
import com.a.b.g;
import com.codeforces.commons.math.Math;
import java.util.List;

public class k
implements b {
    @Override
    public void a(g g2, int n2) {
        List<d> list = g2.a();
        int n3 = list.size();
        while (--n3 >= 0) {
            a a2;
            d d2 = list.get(n3);
            if (!(d2 instanceof com.a.b.a.a.b.d.b)) continue;
            com.a.b.a.a.b.d.b b2 = (com.a.b.a.a.b.d.b)d2;
            b2.n();
            b2.p();
            if (!(d2 instanceof a) || x.a(a2 = (a)d2)) continue;
            double d3 = 0.05 + (double)a2.u() * 0.005;
            a2.b(Math.min(a2.j() + d3, a2.k()));
            double d4 = 0.2 + (double)a2.u() * 0.02;
            a2.a(Math.min(a2.h() + d4, a2.i()));
        }
    }
}

