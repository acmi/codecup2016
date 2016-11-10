/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.e;

import com.a.a.b.f.a;
import com.a.b.a.a.c.C;
import com.a.b.a.a.c.f;
import com.a.b.a.a.c.x;
import com.a.b.d;
import com.a.c.a.b;
import com.a.c.a.e;
import com.a.c.c;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.math.Math;

public final class l {
    public static double a(com.a.c.a.c c2) {
        if (c2 instanceof b) {
            return ((b)c2).a();
        }
        if (c2 instanceof e) {
            e e2 = (e)c2;
            return 0.5 * Math.hypot(e2.a(), e2.b());
        }
        throw new IllegalArgumentException("Unsupported form: " + c2 + '.');
    }

    public static double a(d d2) {
        return l.a(d2.b().t());
    }

    public static double a(C c2) {
        if (c2 instanceof f) {
            return ((f)c2).getRadius();
        }
        if (c2 instanceof x) {
            x x2 = (x)c2;
            return 0.5 * Math.hypot(x2.getWidth(), x2.getHeight());
        }
        throw new IllegalArgumentException("Unsupported unit class: " + c2.getClass() + '.');
    }

    public static double a(double d2, double d3, double d4, double d5) {
        return Math.atan2(d5 - d3, d4 - d2);
    }

    public static double a(double d2) {
        return a.a(d2);
    }

    public static Vector2D a(Vector2D vector2D, double d2) {
        return com.a.a.a.a.b.a(vector2D, d2);
    }
}

