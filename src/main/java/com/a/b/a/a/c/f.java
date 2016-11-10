/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.c;

import com.a.b.a.a.c.C;
import com.a.b.a.a.c.l;
import com.codeforces.commons.reflection.Name;
import com.google.gson.annotations.Until;

public abstract class f
extends C {
    @Until(value=1.0)
    private final double radius;

    protected f(@Name(value="id") long l2, @Name(value="x") double d2, @Name(value="y") double d3, @Name(value="speedX") double d4, @Name(value="speedY") double d5, @Name(value="angle") double d6, @Name(value="faction") l l3, @Name(value="radius") double d7) {
        super(l2, d2, d3, d4, d5, d6, l3);
        this.radius = d7;
    }

    public double getRadius() {
        return this.radius;
    }

    protected static boolean areFieldEquals(f f2, f f3) {
        return C.areFieldEquals(f2, f3) && Double.compare(f2.radius, f3.radius) == 0;
    }
}

