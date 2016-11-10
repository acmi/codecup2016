/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.c;

import com.a.a.b.a;
import com.a.a.b.c.f;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.math.Math;
import com.google.common.base.Preconditions;

public abstract class c {
    private final f a;

    protected c(f f2) {
        Preconditions.checkNotNull(f2, "Argument 'shape' is null.");
        this.a = f2;
    }

    public f e() {
        return this.a;
    }

    public abstract double d();

    public abstract Point2D a(Point2D var1, double var2);

    public final Point2D a(a a2) {
        return this.a(a2.r(), a2.x());
    }

    public abstract double a(double var1);

    public abstract String toString();

    public static String a(c c2) {
        return c2 == null ? "Form {null}" : c2.toString();
    }

    protected static double a(double d2, double d3) {
        return Math.abs(d2) < d3 ? 0.0 : (Math.abs(1.0 - d2) < d3 ? 1.0 : (Math.abs(-1.0 - d2) < d3 ? -1.0 : d2));
    }
}

