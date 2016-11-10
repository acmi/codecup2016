/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.c;

import com.a.b.a.a.c.C;
import com.a.b.a.a.c.l;
import com.codeforces.commons.reflection.Name;
import com.google.gson.annotations.Until;

public abstract class x
extends C {
    @Until(value=1.0)
    private final double width;
    @Until(value=1.0)
    private final double height;

    protected x(@Name(value="id") long l2, @Name(value="x") double d2, @Name(value="y") double d3, @Name(value="speedX") double d4, @Name(value="speedY") double d5, @Name(value="angle") double d6, @Name(value="faction") l l3, @Name(value="width") double d7, @Name(value="height") double d8) {
        super(l2, d2, d3, d4, d5, d6, l3);
        this.width = d7;
        this.height = d8;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    protected static boolean areFieldEquals(x x2, x x3) {
        return C.areFieldEquals(x2, x3) && Double.compare(x2.width, x3.width) == 0 && Double.compare(x2.height, x3.height) == 0;
    }
}

