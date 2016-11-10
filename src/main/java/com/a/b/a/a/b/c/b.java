/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.c;

import com.a.b.a.a.b.d.b.a;
import com.a.b.a.a.b.e.d;
import com.a.b.a.a.b.e.o;
import com.a.b.a.a.b.e.r;
import com.a.b.a.a.b.e.u;
import com.a.b.a.a.b.e.v;
import com.a.b.a.a.b.e.x;
import com.a.b.a.a.c.A;
import com.a.b.a.a.c.l;
import com.a.b.a.a.c.w;
import com.a.b.d;
import com.a.b.e;
import com.a.b.f;
import com.a.b.g;
import com.codeforces.commons.collection.MapBuilder;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class b
extends f<com.a.b.a.a.b.d.d.e, com.a.b.a.a.b.d.b> {
    private final com.a.b.a.a.a.b a;
    private final g b;
    private final Map<l, List<com.a.b.a.a.b.d.f.a>> c;
    private final d.a d;
    private final d.a e;
    private final Map<l, d.a> f;

    public b(com.a.b.a.a.a.b b2, g g2, Map<l, List<com.a.b.a.a.b.d.f.a>> map, d.a a2, d.a a3) {
        this.a = b2;
        this.b = g2;
        this.c = Collections.unmodifiableMap(map);
        this.d = a2;
        this.e = a3;
        this.f = new MapBuilder<l, d.a>(new EnumMap(l.class)).put(l.ACADEMY, a2).put(l.RENEGADES, a3).buildUnmodifiable();
    }

    @Override
    public boolean beforeResolvingCollision(e<com.a.b.a.a.b.d.d.e, com.a.b.a.a.b.d.b> e2) {
        com.a.b.a.a.b.d.d.e e3 = e2.b();
        com.a.b.a.a.b.d.b b2 = e2.c();
        double d2 = r.a(e3.h(), b2, this.c, e3.j());
        List<com.a.b.a.a.b.d.f.a> list = this.c.get((Object)e3.c());
        if (b2 instanceof com.a.b.a.a.b.d.e.a) {
            u.a(this.b, (com.a.b.a.a.b.d.e.a)b2, d2);
        } else if (b2 instanceof com.a.b.a.a.b.d.f.a) {
            com.a.b.a.a.b.d.f.a a2 = (com.a.b.a.a.b.d.f.a)b2;
            x.a(this.a, this.b, a2, e3.i(), list, this.c.get((Object)v.b(a2)), d2);
            if (e3.j() == w.FROST_BOLT && !x.a(a2)) {
                a2.a(A.FROZEN, e3.h(), 60);
            }
        } else if (b2 instanceof com.a.b.a.a.b.d.c.b) {
            com.a.b.a.a.b.d.c.b b3 = (com.a.b.a.a.b.d.c.b)b2;
            o.a(this.b, b3, e3.i(), list, d2);
            if (e3.j() == w.FROST_BOLT && !o.b(b3) && b3.c() != e3.c()) {
                b3.a(A.FROZEN, e3.h(), 60);
            }
        } else if (b2 instanceof a) {
            com.a.b.a.a.b.e.d.a(this.b, (a)b2, e3.i(), list, d2, b2.c() == l.ACADEMY ? this.d : this.e);
        } else {
            throw new IllegalArgumentException("Unsupported living unit class: " + b2.getClass() + '.');
        }
        if (e3 instanceof com.a.b.a.a.b.d.d.b) {
            r.a(this.a, e2.a(), (com.a.b.a.a.b.d.d.b)e3, list, this.c, this.f);
        }
        this.b.b(e3);
        return false;
    }
}

