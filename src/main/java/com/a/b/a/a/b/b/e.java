/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.b;

import com.a.b.a.a.b.a.a;
import com.a.b.a.a.c.k;
import com.a.b.b;
import com.a.b.d;
import com.a.b.g;
import com.codeforces.commons.math.NumberUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class e
implements b {
    private a.a.d.b a;
    private a.a.d.b b;

    @Override
    public void a(g g2, int n2) {
        a.a.d.a.d d2 = e.a();
        a.a.d.a.d d3 = e.a();
        for (d d4 : g2.a()) {
            int n3;
            int n4;
            if (!(d4 instanceof com.a.b.a.a.b.d.b)) continue;
            com.a.b.a.a.b.d.b b2 = (com.a.b.a.a.b.d.b)d4;
            com.a.b.a.a.b.d.a a2 = d4 instanceof com.a.b.a.a.b.d.a ? (com.a.b.a.a.b.d.a)d4 : null;
            int n5 = NumberUtil.toInt(b2.j());
            int n6 = a2 == null ? 0 : NumberUtil.toInt(a2.h());
            d2.a(b2.a(), n5);
            d3.a(b2.a(), n6);
            if (this.a == null || this.b == null) continue;
            HashMap hashMap = null;
            int n7 = this.a.a(b2.a());
            if (n7 != Integer.MIN_VALUE && n7 != n5 && (n3 = n5 - n7) != 0) {
                hashMap = new HashMap<String, Object>();
                hashMap.put("lifeChange", n3);
            }
            if ((n3 = this.b.a(b2.a())) != Integer.MIN_VALUE && n3 != n6 && (n4 = n6 - n3) != 0) {
                if (hashMap == null) {
                    hashMap = new HashMap();
                }
                hashMap.put("manaChange", n4);
            }
            if (hashMap == null) continue;
            g2.a(new a(k.WIZARD_CONDITION_CHANGE, n2, b2, hashMap));
        }
        this.a = d2;
        this.b = d3;
    }

    private static a.a.d.a.d a() {
        return new a.a.d.a.d(10, 10.0f, Long.MIN_VALUE, Integer.MIN_VALUE);
    }
}

