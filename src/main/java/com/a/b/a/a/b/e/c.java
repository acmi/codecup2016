/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.e;

import com.a.b.a.a.b.d.a.a;
import com.a.b.a.a.c.b;
import com.a.b.a.a.c.l;

public final class c {
    public static b a(a a2, boolean bl) {
        com.a.c.c c2 = a2.b();
        com.a.c.a.c c3 = c2.t();
        if (!(c3 instanceof com.a.c.a.b)) {
            throw new IllegalArgumentException("Unsupported bonus form: " + c3 + '.');
        }
        com.a.c.a.b b2 = (com.a.c.a.b)c3;
        return new b(a2.a(), bl ? 4000.0 - c2.c() : c2.c(), bl ? 4000.0 - c2.d() : c2.d(), 0.0, 0.0, bl ? c2.e() + 3.141592653589793 : c2.e(), a2.c(), b2.a(), a2.h());
    }
}

