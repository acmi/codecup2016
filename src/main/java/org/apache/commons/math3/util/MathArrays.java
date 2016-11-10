/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.math3.util;

public class MathArrays {
    public static double linearCombination(double d2, double d3, double d4, double d5) {
        double d6;
        double d7;
        double d8;
        double d9;
        double d10;
        double d11;
        double d12;
        double d13;
        double d14;
        double d15 = d2 * d3;
        double d16 = d4 * d5;
        double d17 = d15 + d16;
        double d18 = Double.longBitsToDouble(Double.doubleToRawLongBits(d2) & -134217728);
        double d19 = d2 - d18;
        double d20 = d19 * (d13 = d3 - (d7 = Double.longBitsToDouble(Double.doubleToRawLongBits(d3) & -134217728))) - (d15 - d18 * d7 - d19 * d7 - d18 * d13);
        double d21 = d17 + (d20 + (d6 = (d10 = d4 - (d14 = Double.longBitsToDouble(Double.doubleToRawLongBits(d4) & -134217728))) * (d12 = d5 - (d9 = Double.longBitsToDouble(Double.doubleToRawLongBits(d5) & -134217728))) - (d16 - d14 * d9 - d10 * d9 - d14 * d12)) + (d8 = d16 - (d17 - (d11 = d17 - d16)) + (d15 - d11)));
        if (Double.isNaN(d21)) {
            d21 = d2 * d3 + d4 * d5;
        }
        return d21;
    }

    public static double linearCombination(double d2, double d3, double d4, double d5, double d6, double d7) {
        double d8;
        double d9;
        double d10;
        double d11;
        double d12;
        double d13;
        double d14;
        double d15;
        double d16;
        double d17;
        double d18;
        double d19;
        double d20;
        double d21;
        double d22;
        double d23;
        double d24 = d2 * d3;
        double d25 = d4 * d5;
        double d26 = d24 + d25;
        double d27 = d6 * d7;
        double d28 = d26 + d27;
        double d29 = Double.longBitsToDouble(Double.doubleToRawLongBits(d2) & -134217728);
        double d30 = d2 - d29;
        double d31 = d30 * (d20 = d3 - (d8 = Double.longBitsToDouble(Double.doubleToRawLongBits(d3) & -134217728))) - (d24 - d29 * d8 - d30 * d8 - d29 * d20);
        double d32 = d28 + (d31 + (d15 = (d22 = d4 - (d18 = Double.longBitsToDouble(Double.doubleToRawLongBits(d4) & -134217728))) * (d10 = d5 - (d23 = Double.longBitsToDouble(Double.doubleToRawLongBits(d5) & -134217728))) - (d25 - d18 * d23 - d22 * d23 - d18 * d10)) + (d17 = (d21 = d6 - (d13 = Double.longBitsToDouble(Double.doubleToRawLongBits(d6) & -134217728))) * (d9 = d7 - (d19 = Double.longBitsToDouble(Double.doubleToRawLongBits(d7) & -134217728))) - (d27 - d13 * d19 - d21 * d19 - d13 * d9)) + (d12 = d25 - (d26 - (d11 = d26 - d25)) + (d24 - d11)) + (d14 = d27 - (d28 - (d16 = d28 - d27)) + (d26 - d16)));
        if (Double.isNaN(d32)) {
            d32 = d2 * d3 + d4 * d5 + d6 * d7;
        }
        return d32;
    }
}

