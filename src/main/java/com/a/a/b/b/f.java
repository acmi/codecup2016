/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.b;

import com.a.a.b.a;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.text.StringUtil;
import org.apache.log4j.Logger;

public class f {
    private static final Logger a = Logger.getLogger(f.class);
    private final a b;
    private final a c;
    private final Point2D d;
    private final Vector2D e;
    private final double f;

    public f(a a2, a a3, Point2D point2D, Vector2D vector2D, double d2, double d3) {
        this.b = a2;
        this.c = a3;
        this.d = point2D;
        this.e = vector2D;
        this.f = d2 < 0.0 && d2 > - d3 ? 0.0 : d2;
        if (Double.isNaN(this.f) || Double.isInfinite(this.f) || this.f < 0.0) {
            a.error(String.format("Argument 'depth' should be non-negative number but got %s (%s and %s).", this.f, a2, a3));
        }
    }

    public a a() {
        return this.b;
    }

    public a b() {
        return this.c;
    }

    public Point2D c() {
        return this.d.copy();
    }

    public Vector2D d() {
        return this.e.copy();
    }

    public double e() {
        return this.f;
    }

    public String toString() {
        return StringUtil.toString((Object)this, false, new String[0]);
    }
}

