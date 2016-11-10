/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.b;

import com.a.a.a.a.c;
import com.a.b.a.a.b.d.b.a;
import com.a.b.a.a.b.d.c.b;
import com.a.b.a.a.b.e.d;
import com.a.b.a.a.b.e.m;
import com.a.b.a.a.b.e.o;
import com.a.b.a.a.b.e.x;
import com.a.b.a.a.c.A;
import com.a.b.a.a.c.e;
import com.a.b.a.a.c.k;
import com.a.b.a.a.c.l;
import com.a.b.d;
import com.a.b.g;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class b
implements com.a.b.b {
    private final com.a.b.a.a.a.b a;
    private final Map<l, List<com.a.b.a.a.b.d.f.a>> b;
    private final d.a c;
    private final d.a d;

    public b(com.a.b.a.a.a.b b2, Map<l, List<com.a.b.a.a.b.d.f.a>> map, d.a a2, d.a a3) {
        this.a = b2;
        this.b = Collections.unmodifiableMap(map);
        this.c = a2;
        this.d = a3;
    }

    @Override
    public void a(g g2, int n2) {
        List<d> list = g2.a();
        com.a.b.a.a.b.d.b[] arrb = b.a(list);
        c.a(arrb);
        list.forEach(d2 -> {
            if (!(d2 instanceof a)) {
                return;
            }
            a a2 = (a)d2;
            if (a2.c(n2) > 0 || a2.b(A.FROZEN)) {
                return;
            }
            com.a.b.a.a.b.d.b b2 = b.a(a2, arrb);
            if (b2 == null) {
                return;
            }
            this.a(a2, b2, g2, n2);
            a2.a(n2);
        }
        );
    }

    private static com.a.b.a.a.b.d.b[] a(List<d> list) {
        int n2 = list.size();
        ArrayList<com.a.b.a.a.b.d.b> arrayList = new ArrayList<com.a.b.a.a.b.d.b>(n2);
        int n3 = n2;
        while (--n3 >= 0) {
            d d2 = list.get(n3);
            if (!(d2 instanceof com.a.b.a.a.b.d.b) || d2 instanceof com.a.b.a.a.b.d.e.a || d2.c() == l.OTHER || d2 instanceof com.a.b.a.a.b.d.c.b && ((com.a.b.a.a.b.d.c.b)d2).i() == null) continue;
            arrayList.add((com.a.b.a.a.b.d.b)d2);
        }
        return arrayList.toArray(new com.a.b.a.a.b.d.b[arrayList.size()]);
    }

    private static com.a.b.a.a.b.d.b a(a a2, com.a.b.a.a.b.d.b[] arrb) {
        int n2 = arrb.length;
        while (--n2 >= 0) {
            com.a.b.a.a.b.d.b b2 = arrb[n2];
            if (b2.c() == a2.c() || a2.b(b2) > a2.i() || b2 instanceof com.a.b.a.a.b.d.f.a && x.a((com.a.b.a.a.b.d.f.a)b2) || b2 instanceof com.a.b.a.a.b.d.c.b && o.b((com.a.b.a.a.b.d.c.b)b2) || b2 instanceof a && com.a.b.a.a.b.e.d.a((a)b2)) continue;
            return b2;
        }
        return null;
    }

    private void a(a a2, com.a.b.a.a.b.d.b b2, g g2, int n2) {
        m.a(this.a, b2, g2, this.b.get((Object)a2.c()), this.b, a2.s(), true, this.c, this.d);
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("buildingId", a2.a());
        hashMap.put("buildingX", a2.b().c());
        hashMap.put("buildingY", a2.b().d());
        hashMap.put("buildingFaction", (Object)a2.c());
        hashMap.put("buildingType", (Object)a2.h());
        hashMap.put("targetId", b2.a());
        hashMap.put("targetX", b2.b().c());
        hashMap.put("targetY", b2.b().d());
        hashMap.put("targetFaction", (Object)b2.c());
        g2.a(new com.a.b.a.a.b.a.a(k.BUILDING_ATTACK, n2, hashMap));
    }
}

