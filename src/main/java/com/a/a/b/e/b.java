/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.e;

import com.a.a.b.a;
import com.a.a.b.e.d;
import com.codeforces.commons.geometry.Vector2D;

public class b
implements d {
    private final double a;

    public b(double d2) {
        if (d2 < 0.0) {
            throw new IllegalArgumentException("Argument 'movementFrictionFactor' should be zero or positive.");
        }
        this.a = d2;
    }

    public double a() {
        return this.a;
    }

    @Override
    public void a(a a2, double d2) {
        if (this.a <= 0.0) {
            return;
        }
        double d3 = a2.u().getLength();
        if (d3 <= 0.0) {
            return;
        }
        double d4 = this.a * d2;
        if (d4 >= d3) {
            a2.b(0.0, 0.0);
        } else if (d4 > 0.0) {
            a2.u().multiply(1.0 - d4 / d3);
        }
    }
}

