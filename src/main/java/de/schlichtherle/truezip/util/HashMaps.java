/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.util;

public class HashMaps {
    public static int initialCapacity(int n2) {
        long l2;
        if (n2 < 47) {
            n2 = 47;
        }
        return Integer.MAX_VALUE >= (l2 = (long)n2 * 4 / 3 + 1) ? HashMaps.powerOfTwo((int)l2) : Integer.MAX_VALUE;
    }

    private static int powerOfTwo(int n2) {
        --n2;
        n2 |= n2 >>> 1;
        n2 |= n2 >>> 2;
        n2 |= n2 >>> 4;
        n2 |= n2 >>> 8;
        n2 |= n2 >>> 16;
        return n2 + 1;
    }
}

