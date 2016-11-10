/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.e;

import com.a.b.a.a.b.a.a;
import com.a.b.a.a.c.k;
import com.a.b.d;
import java.util.Map;

public final class j {
    public static com.a.b.a.a.c.j a(a a2, int n2) {
        Map<String, Object> map = a2.g();
        if (map.isEmpty()) {
            return new com.a.b.a.a.c.j(a2.a(), a2.b(), a2.a(n2), a2.c() == null ? null : Long.valueOf(a2.c().a()), a2.d(), a2.e(), a2.f());
        }
        return new com.a.b.a.a.c.j(a2.a(), a2.b(), a2.a(n2), a2.c() == null ? null : Long.valueOf(a2.c().a()), a2.d(), a2.e(), a2.f(), map);
    }
}

