/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.f;

import com.codeforces.commons.geometry.Line2D;
import com.codeforces.commons.geometry.Point2D;

public final class a {
    public static double a(double d2) {
        while (d2 > 3.141592653589793) {
            d2 -= 6.283185307179586;
        }
        while (d2 < -3.141592653589793) {
            d2 += 6.283185307179586;
        }
        return d2;
    }

    public static boolean a(double d2, double d3, double d4) {
        while (d4 < d3) {
            d4 += 6.283185307179586;
        }
        while (d4 - 6.283185307179586 > d3) {
            d4 -= 6.283185307179586;
        }
        while (d2 < d3) {
            d2 += 6.283185307179586;
        }
        while (d2 - 6.283185307179586 > d3) {
            d2 -= 6.283185307179586;
        }
        return d2 >= d3 && d2 <= d4;
    }

    public static boolean a(Point2D point2D, Point2D[] arrpoint2D, double d2) {
        int n2 = arrpoint2D.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Point2D point2D2 = arrpoint2D[i2];
            Point2D point2D3 = arrpoint2D[i2 == n2 - 1 ? 0 : i2 + 1];
            Line2D line2D = Line2D.getLineByTwoPoints(point2D2, point2D3);
            if (line2D.getSignedDistanceFrom(point2D) <= - d2) continue;
            return false;
        }
        return true;
    }

    public static boolean b(Point2D point2D, Point2D[] arrpoint2D, double d2) {
        int n2 = arrpoint2D.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Point2D point2D2 = arrpoint2D[i2];
            Point2D point2D3 = arrpoint2D[i2 == n2 - 1 ? 0 : i2 + 1];
            Line2D line2D = Line2D.getLineByTwoPoints(point2D2, point2D3);
            if (line2D.getSignedDistanceFrom(point2D) < d2) continue;
            return true;
        }
        return false;
    }
}

