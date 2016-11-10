/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b;

import com.a.b.d;
import com.a.b.f;
import com.a.b.g;
import com.a.c.a;
import com.a.c.b;
import com.a.c.c;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;
import java.lang.reflect.Method;

class e
implements com.a.c.d {
    final /* synthetic */ Method a;
    final /* synthetic */ Class b;
    final /* synthetic */ Class c;
    final /* synthetic */ f d;
    final /* synthetic */ Method e;
    final /* synthetic */ b f;
    final /* synthetic */ com.a.b.a.a.b.d g;

    e(com.a.b.a.a.b.d d2, Method method, Class class_, Class class_2, f f2, Method method2, b b2) {
        this.g = d2;
        this.a = method;
        this.b = class_;
        this.c = class_2;
        this.d = f2;
        this.e = method2;
        this.f = b2;
    }

    @Override
    public boolean a(c c2, c c3) {
        d d2;
        if (this.a.getDeclaringClass() == f.class) {
            return true;
        }
        d d3 = c2 == null ? null : (d)com.a.b.a.a.b.d.a(this.g).get(c2.a());
        d d4 = d2 = c3 == null ? null : (d)com.a.b.a.a.b.d.a(this.g).get(c3.a());
        if (this.b.isInstance(d3) && this.c.isInstance(d2)) {
            com.a.b.a.a.b.d.a(this.g, d3, d2);
            boolean bl = this.d.beforeCollision(this.g, d3, d2);
            com.a.b.a.a.b.d.b(this.g, d3, d2);
            return bl;
        }
        if (this.b.isInstance(d2) && this.c.isInstance(d3)) {
            com.a.b.a.a.b.d.a(this.g, d3, d2);
            boolean bl = this.d.beforeCollision(this.g, d2, d3);
            com.a.b.a.a.b.d.b(this.g, d3, d2);
            return bl;
        }
        return true;
    }

    @Override
    public boolean a(a a2) {
        d d2;
        if (this.e.getDeclaringClass() == f.class) {
            return true;
        }
        d d3 = a2.a() == null ? null : (d)com.a.b.a.a.b.d.a(this.g).get(a2.a().a());
        d d4 = d2 = a2.b() == null ? null : (d)com.a.b.a.a.b.d.a(this.g).get(a2.b().a());
        if (this.b.isInstance(d3) && this.c.isInstance(d2)) {
            com.a.b.a.a.b.d.a(this.g, d3, d2);
            boolean bl = this.d.beforeResolvingCollision(new com.a.b.e<d, d>(this.g, d3, d2, a2.c().copy(), a2.d().copy()));
            com.a.b.a.a.b.d.b(this.g, d3, d2);
            return bl;
        }
        if (this.b.isInstance(d2) && this.c.isInstance(d3)) {
            com.a.b.a.a.b.d.a(this.g, d3, d2);
            boolean bl = this.d.beforeResolvingCollision(new com.a.b.e<d, d>(this.g, d2, d3, a2.c().copy(), a2.d().copyNegate()));
            com.a.b.a.a.b.d.b(this.g, d3, d2);
            return bl;
        }
        return true;
    }

    @Override
    public void afterCollision(a a2) {
        this.f.afterCollision(a2);
    }
}

