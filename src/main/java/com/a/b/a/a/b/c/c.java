/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.c;

import com.a.b.a.a.b.d.a.a;
import com.a.b.a.a.b.d.b;
import com.a.b.a.a.c.A;
import com.a.b.d;
import com.a.b.e;
import com.a.b.f;
import com.a.b.g;
import com.codeforces.commons.math.NumberUtil;

public class c
extends f<com.a.b.a.a.b.d.f.a, a> {
    @Override
    public boolean beforeResolvingCollision(e<com.a.b.a.a.b.d.f.a, a> e2) {
        com.a.b.a.a.b.d.f.a a2 = e2.b();
        a a3 = e2.c();
        a2.a(100);
        switch (a3.h()) {
            case EMPOWER: {
                a2.a(A.EMPOWERED, null, 2400);
                break;
            }
            case HASTE: {
                a2.a(A.HASTENED, null, NumberUtil.toInt(2400.0));
                break;
            }
            case SHIELD: {
                a2.a(A.SHIELDED, null, NumberUtil.toInt(2400.0));
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported bonus type: " + (Object)((Object)a3.h()) + '.');
            }
        }
        e2.a().b(a3);
        return false;
    }
}

