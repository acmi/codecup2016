/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.b;

import com.a.a.b.a;
import com.a.a.b.b.e;
import com.a.a.b.b.f;
import com.a.a.b.c.b;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;

public class c
extends e {
    public c(double d2) {
        super(d2);
    }

    @Override
    protected boolean a(a a2, a a3) {
        return a2.c().e() == com.a.a.b.c.f.a && a3.c().e() == com.a.a.b.c.f.a;
    }

    @Override
    protected f b(a a2, a a3) {
        Vector2D vector2D;
        Point2D point2D;
        b b2 = (b)a2.c();
        b b3 = (b)a3.c();
        double d2 = b2.a();
        double d3 = b3.a();
        double d4 = a2.r().getDistanceTo(a3.r());
        if (d4 > d2 + d3) {
            return null;
        }
        if (d4 >= this.a) {
            Vector2D vector2D2 = new Vector2D(a3.r(), a2.r());
            vector2D = vector2D2.copy().normalize();
            point2D = a3.r().copy().add(vector2D2.copy().multiply(d3 / (d2 + d3)));
        } else {
            Vector2D vector2D3 = a3.u().copy().subtract(a2.u());
            vector2D = vector2D3.getLength() >= this.a ? vector2D3.normalize() : (a3.u().getLength() >= this.a ? a3.u().copy().normalize() : new Vector2D(1.0, 0.0));
            point2D = a3.r().copy();
        }
        return new f(a2, a3, point2D, vector2D, d2 + d3 - d4, this.a);
    }
}

