/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.b;

import com.a.b.a.a.b.d.b;
import com.a.b.a.a.b.d.b.a;
import com.a.b.a.a.b.e.d;
import com.a.b.a.a.b.e.o;
import com.a.b.a.a.b.e.u;
import com.a.b.a.a.b.e.v;
import com.a.b.a.a.b.e.x;
import com.a.b.a.a.b.f;
import com.a.b.a.a.c.A;
import com.a.b.a.a.c.l;
import com.a.b.b;
import com.a.b.d;
import com.a.b.g;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class c
implements b {
    private final com.a.b.a.a.a.b a;
    private final Map<l, List<com.a.b.a.a.b.d.f.a>> b;
    private final d.a c;
    private final d.a d;

    public c(com.a.b.a.a.a.b b2, Map<l, List<com.a.b.a.a.b.d.f.a>> map, d.a a2, d.a a3) {
        this.a = b2;
        this.b = Collections.unmodifiableMap(map);
        this.c = a2;
        this.d = a3;
    }

    @Override
    public void a(g g2, int n2) {
        List<d> list = g2.a();
        int n3 = list.size();
        while (--n3 >= 0) {
            d d2 = list.get(n3);
            if (!(d2 instanceof com.a.b.a.a.b.d.b)) continue;
            com.a.b.a.a.b.d.b b2 = (com.a.b.a.a.b.d.b)d2;
            double d3 = 0.1;
            for (b.a a2 : b2.a(A.BURNING)) {
                List<com.a.b.a.a.b.d.f.a> list2;
                if (d2 instanceof com.a.b.a.a.b.d.e.a) {
                    u.a(g2, (com.a.b.a.a.b.d.e.a)d2, d3);
                    continue;
                }
                f f2 = a2.c() instanceof com.a.b.a.a.b.d.f.a ? ((com.a.b.a.a.b.d.f.a)a2.c()).s() : null;
                List<com.a.b.a.a.b.d.f.a> list3 = list2 = f2 == null ? null : this.b.get((Object)f2.k());
                if (d2 instanceof com.a.b.a.a.b.d.f.a) {
                    x.a(this.a, g2, (com.a.b.a.a.b.d.f.a)d2, f2, list2, this.b.get((Object)v.b(d2)), d3);
                    continue;
                }
                if (d2 instanceof com.a.b.a.a.b.d.c.b) {
                    o.a(g2, (com.a.b.a.a.b.d.c.b)d2, f2, list2, d3);
                    continue;
                }
                if (d2 instanceof a) {
                    com.a.b.a.a.b.e.d.a(g2, (a)d2, f2, list2, d3, d2.c() == l.ACADEMY ? this.c : this.d);
                    continue;
                }
                throw new IllegalArgumentException("Unsupported living unit class: " + d2.getClass() + '.');
            }
        }
    }
}

