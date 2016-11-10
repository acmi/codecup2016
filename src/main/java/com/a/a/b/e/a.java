/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.e;

import com.a.a.b.e.d;
import com.codeforces.commons.geometry.Vector2D;

public class a
implements d {
    private final double a;
    private final double b;

    public a(double d2, double d3) {
        if (d2 < 0.0) {
            throw new IllegalArgumentException("Argument 'lengthwiseMovementFrictionFactor' should be zero or positive.");
        }
        if (d3 < 0.0) {
            throw new IllegalArgumentException("Argument 'crosswiseMovementFrictionFactor' should be zero or positive.");
        }
        this.a = d2;
        this.b = d3;
    }

    public double a() {
        return this.a;
    }

    public double b() {
        return this.b;
    }

    @Override
    public void a(com.a.a.b.a a2, double d2) {
        double d3;
        Vector2D vector2D = a2.u();
        double d4 = vector2D.getLength();
        if (d4 <= 0.0) {
            return;
        }
        double d5 = this.a * d2;
        double d6 = this.b * d2;
        Vector2D vector2D2 = new Vector2D(1.0, 0.0).rotate(a2.x());
        Vector2D vector2D3 = vector2D2.copy().rotateHalfPi();
        double d7 = vector2D.dotProduct(vector2D2);
        if (d7 >= 0.0) {
            if ((d7 -= d5) < 0.0) {
                d7 = 0.0;
            }
        } else if ((d7 += d5) > 0.0) {
            d7 = 0.0;
        }
        if ((d3 = vector2D.dotProduct(vector2D3)) >= 0.0) {
            if ((d3 -= d6) < 0.0) {
                d3 = 0.0;
            }
        } else if ((d3 += d6) > 0.0) {
            d3 = 0.0;
        }
        a2.a(vector2D2.multiply(d7).add(vector2D3.multiply(d3)));
    }
}

