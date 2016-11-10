/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.a.a;

import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.math.Math;

public final class b {
    public static Vector2D a(Vector2D vector2D, double d2) {
        double d3 = Math.cos(d2);
        double d4 = Math.sin(d2);
        return new Vector2D(vector2D.getX() * d3 - vector2D.getY() * d4, vector2D.getX() * d4 + vector2D.getY() * d3);
    }
}

