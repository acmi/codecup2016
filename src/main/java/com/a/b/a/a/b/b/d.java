/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.b;

import com.a.b.a.a.b.d.d.e;
import com.a.b.a.a.b.d.f.a;
import com.a.b.a.a.b.e.d;
import com.a.b.a.a.b.e.r;
import com.a.b.a.a.c.l;
import com.a.b.b;
import com.a.b.g;
import com.a.c.c;
import com.codeforces.commons.collection.MapBuilder;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class d
implements b {
    private final com.a.b.a.a.a.b a;
    private final Map<l, List<a>> b;
    private final Map<l, d.a> c;

    public d(com.a.b.a.a.a.b b2, Map<l, List<a>> map, d.a a2, d.a a3) {
        this.a = b2;
        this.b = Collections.unmodifiableMap(map);
        this.c = new MapBuilder<l, d.a>(new EnumMap(l.class)).put(l.ACADEMY, a2).put(l.RENEGADES, a3).buildUnmodifiable();
    }

    @Override
    public void a(g g2, int n2) {
        List<com.a.b.d> list = g2.a();
        int n3 = list.size();
        while (--n3 >= 0) {
            e e2;
            com.a.b.d d2 = list.get(n3);
            if (!(d2 instanceof e) || (e2 = (e)d2).o() <= e2.n()) continue;
            if (e2 instanceof com.a.b.a.a.b.d.d.b) {
                c c2 = e2.b();
                Point2D point2D = new Point2D(e2.k(), e2.l()).add(new Vector2D(e2.k(), e2.l(), c2.c(), c2.d()).setLength(e2.n()));
                c2.a(point2D.getX());
                c2.b(point2D.getY());
                g2.c(e2);
                List<a> list2 = this.b.get((Object)e2.c());
                r.a(this.a, g2, (com.a.b.a.a.b.d.d.b)e2, list2, this.b, this.c);
            }
            g2.b(e2);
        }
    }
}

