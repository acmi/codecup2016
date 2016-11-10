/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.e;

import com.a.b.a.a.b.d.b;
import com.a.b.a.a.b.d.f.a;
import com.a.b.a.a.b.f;
import com.a.b.a.a.c.A;
import com.a.b.a.a.c.z;
import java.util.List;

public final class t {
    public static z[] a(List<b.a> list) {
        int n2 = list.size();
        z[] arrz = new z[n2];
        int n3 = n2;
        while (--n3 >= 0) {
            long l2;
            long l3;
            b.a a2 = list.get(n3);
            b b2 = a2.c();
            if (b2 != null && !(b2 instanceof a)) {
                throw new IllegalArgumentException("Unsupported status caster: " + b2 + '.');
            }
            a a3 = (a)b2;
            if (a3 == null) {
                l2 = -1;
                l3 = -1;
            } else {
                l2 = a3.a();
                f f2 = a3.s();
                l3 = f2 == null ? -1 : f2.a();
            }
            arrz[n3] = new z(a2.a(), a2.b(), l2, l3, a2.d());
        }
        return arrz;
    }
}

