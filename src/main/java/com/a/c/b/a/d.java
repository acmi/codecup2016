/*
 * Decompiled with CFR 0_119.
 */
package com.a.c.b.a;

import com.a.a.b.b.f;
import com.a.c.a;
import com.a.c.b;
import com.a.c.c;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;

class d
extends com.a.a.b.d.b {
    final /* synthetic */ b a;
    final /* synthetic */ com.a.c.b.a.b b;

    d(com.a.c.b.a.b b2, b b3) {
        this.b = b2;
        this.a = b3;
    }

    @Override
    public void b(f f2) {
        c c2 = this.b.d(f2.a().a());
        c c3 = this.b.d(f2.b().a());
        if (c2 == null || c3 == null) {
            return;
        }
        this.a.afterCollision(new a(c2, c3, f2.c(), f2.d()));
    }
}

