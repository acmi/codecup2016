/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.b;

import com.a.b.a.a.b.d.f.a;
import com.a.b.a.a.b.e.x;
import com.a.b.a.a.b.f;
import com.a.b.b;
import com.a.b.d;
import com.a.b.g;
import com.a.c.c;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.math.Math;
import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class m
implements b {
    private final Map<f, List<a>> a;

    public m(Map<f, List<a>> map) {
        this.a = Collections.unmodifiableMap(map);
    }

    @Override
    public void a(g g2, int n2) {
        int n3 = this.a.size();
        int n4 = 0;
        for (Map.Entry<f, List<a>> entry : this.a.entrySet()) {
            List<a> list = entry.getValue();
            int n5 = list.size();
            for (int i2 = 0; i2 < n5; ++i2) {
                a a2 = list.get(i2);
                if (!x.a(a2)) continue;
                if (a2.w() == null) {
                    if (a2.v() == null) {
                        a2.a((Integer)(n2 + 1200));
                    } else {
                        a2.a((Integer)Math.max(a2.v() + 2400, n2 + 1200));
                    }
                }
                if (n2 < Preconditions.checkNotNull(a2.w())) continue;
                a2.a((Integer)null);
                a2.b(a2.k());
                a2.a(a2.i());
                m.a(a2, g2, n4, n3, i2, n5);
                if (x.a(a2, g2, false)) continue;
                com.a.b.a.a.b.e.m.a(a2, g2);
            }
            ++n4;
        }
    }

    private static void a(a a2, g g2, int n2, int n3, int n4, int n5) {
        Point2D point2D = x.a(n2, n3, n4, n5);
        double d2 = x.b(n2, n3, n4, n5);
        a2.a(point2D.getX());
        a2.b(point2D.getY());
        g2.a(a2);
        c c2 = a2.b();
        c2.a(point2D.getX());
        c2.b(point2D.getY());
        c2.c(d2);
        g2.c(a2);
    }
}

