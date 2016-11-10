/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.math;

import org.apache.commons.math3.util.FastMath;

public final class Math {
    public static final double SQRT_2 = Math.sqrt(2.0);
    public static final double SQRT_3 = Math.sqrt(3.0);
    public static final double SQRT_5 = Math.sqrt(5.0);
    public static final double SQRT_6 = Math.sqrt(6.0);
    public static final double SQRT_7 = Math.sqrt(7.0);
    public static final double SQRT_8 = Math.sqrt(8.0);
    public static final double CBRT_2 = Math.cbrt(2.0);
    public static final double CBRT_3 = Math.cbrt(3.0);
    public static final double CBRT_4 = Math.cbrt(4.0);
    public static final double CBRT_5 = Math.cbrt(5.0);
    public static final double CBRT_6 = Math.cbrt(6.0);
    public static final double CBRT_7 = Math.cbrt(7.0);
    public static final double CBRT_9 = Math.cbrt(9.0);

    public static int avg(int n2, int n3, int n4, int n5) {
        return n2 / 4 + n3 / 4 + n4 / 4 + n5 / 4 + (n2 % 4 + n3 % 4 + n4 % 4 + n5 % 4) / 4;
    }

    public static double sqr(double d2) {
        return d2 * d2;
    }

    public static double sumSqr(double d2, double d3) {
        return d2 * d2 + d3 * d3;
    }

    public static double pow(double d2, double d3) {
        return StrictMath.pow(d2, d3);
    }

    public static int min(int n2, int n3) {
        return n2 <= n3 ? n2 : n3;
    }

    public static double min(double d2, double d3) {
        return java.lang.Math.min(d2, d3);
    }

    public static int max(int n2, int n3) {
        return n2 >= n3 ? n2 : n3;
    }

    public static long max(long l2, long l3) {
        return l2 >= l3 ? l2 : l3;
    }

    public static double max(double d2, double d3) {
        return java.lang.Math.max(d2, d3);
    }

    public static int abs(int n2) {
        return n2 < 0 ? - n2 : n2;
    }

    public static float abs(float f2) {
        return java.lang.Math.abs(f2);
    }

    public static double abs(double d2) {
        return java.lang.Math.abs(d2);
    }

    public static double sqrt(double d2) {
        return StrictMath.sqrt(d2);
    }

    public static double cbrt(double d2) {
        return StrictMath.cbrt(d2);
    }

    public static int round(float f2) {
        return java.lang.Math.round(f2);
    }

    public static long round(double d2) {
        return java.lang.Math.round(d2);
    }

    public static double floor(double d2) {
        return FastMath.floor(d2);
    }

    public static double ceil(double d2) {
        return FastMath.ceil(d2);
    }

    public static double hypot(double d2, double d3) {
        return FastMath.hypot(d2, d3);
    }

    public static double sin(double d2) {
        return FastMath.sin(d2);
    }

    public static double cos(double d2) {
        return FastMath.cos(d2);
    }

    public static double atan2(double d2, double d3) {
        return StrictMath.atan2(d2, d3);
    }
}

