/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.d.e;

import com.a.b.a.a.b.d.b;
import com.a.b.a.a.c.l;
import com.a.c.c;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.math.NumberUtil;
import com.codeforces.commons.text.StringUtil;

public final class a
extends b {
    public a(double d2, double d3, double d4) {
        super(new com.a.c.a.b(d4), l.OTHER, a.a(d4));
        this.b().a(d2);
        this.b().b(d3);
        this.b().a(true);
    }

    public a(Point2D point2D, double d2) {
        this(point2D.getX(), point2D.getY(), d2);
    }

    @Override
    public String toString() {
        return StringUtil.toString((Object)this, false, "id", "body.x", "body.y");
    }

    private static int a(double d2) {
        if (Double.isNaN(d2) || Double.isInfinite(d2) || d2 < 20.0 || d2 > 50.0) {
            throw new IllegalArgumentException("Argument 'radius' is not a valid number or beyond the boundaries.");
        }
        return 6 + NumberUtil.toInt(Math.round(30.0 * Math.sqr((d2 - 20.0) / 30.0)));
    }
}

