/*
 * Decompiled with CFR 0_119.
 */
package com.a.c.b.a;

import com.a.a.b.b.f;
import com.a.c.a;
import com.a.c.b.a.b;
import com.a.c.d;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;

class c
extends com.a.a.b.d.b {
    final /* synthetic */ d a;
    final /* synthetic */ b b;

    c(b b2, d d2) {
        this.b = b2;
        this.a = d2;
    }

    @Override
    public boolean a(com.a.a.b.a a2, com.a.a.b.a a3) {
        com.a.c.c c2 = this.b.d(a2.a());
        com.a.c.c c3 = this.b.d(a3.a());
        if (c2 == null || c3 == null) {
            return false;
        }
        return this.a.a(c2, c3);
    }

    @Override
    public boolean a(f f2) {
        com.a.c.c c2 = this.b.d(f2.a().a());
        com.a.c.c c3 = this.b.d(f2.b().a());
        if (c2 == null || c3 == null) {
            return false;
        }
        return this.a.a(new a(c2, c3, f2.c(), f2.d()));
    }

    @Override
    public void b(f f2) {
        com.a.c.c c2 = this.b.d(f2.a().a());
        com.a.c.c c3 = this.b.d(f2.b().a());
        if (c2 == null || c3 == null) {
            return;
        }
        this.a.afterCollision(new a(c2, c3, f2.c(), f2.d()));
    }
}

