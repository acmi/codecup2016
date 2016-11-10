/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.b;

import com.a.b.b;
import com.a.b.d;
import com.a.b.g;
import com.a.c.c;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.math.Math;
import java.util.List;

public class l
implements b {
    @Override
    public void a(g g2, int n2) {
        for (d d2 : g2.a()) {
            double d3 = d2.b().c();
            double d4 = d2.b().d();
            if (d2.f() == null) {
                d2.c(0.0);
                d2.a(new Vector2D(0.0, 0.0));
            } else {
                double d5 = Math.hypot(d3 - d2.d(), d4 - d2.e());
                d2.c(d2.f() + d5);
                d2.a(new Vector2D(d2.d(), d2.e(), d3, d4));
            }
            d2.a(d3);
            d2.b(d4);
        }
    }
}

