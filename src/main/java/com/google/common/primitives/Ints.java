/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.primitives;

public final class Ints {
    public static int checkedCast(long l2) {
        int n2 = (int)l2;
        if ((long)n2 != l2) {
            throw new IllegalArgumentException("Out of range: " + l2);
        }
        return n2;
    }

    public static int saturatedCast(long l2) {
        if (l2 > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (l2 < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int)l2;
    }

    public static int compare(int n2, int n3) {
        return n2 < n3 ? -1 : (n2 > n3 ? 1 : 0);
    }
}

