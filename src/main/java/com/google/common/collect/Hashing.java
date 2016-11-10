/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

final class Hashing {
    private static int MAX_TABLE_SIZE = 1073741824;

    static int smear(int n2) {
        return 461845907 * Integer.rotateLeft(n2 * -862048943, 15);
    }

    static int smearedHash(Object object) {
        return Hashing.smear(object == null ? 0 : object.hashCode());
    }

    static int closedTableSize(int n2, double d2) {
        int n3;
        if ((n2 = Math.max(n2, 2)) > (int)(d2 * (double)(n3 = Integer.highestOneBit(n2)))) {
            return (n3 <<= 1) > 0 ? n3 : MAX_TABLE_SIZE;
        }
        return n3;
    }

    static boolean needsResizing(int n2, int n3, double d2) {
        return (double)n2 > d2 * (double)n3 && n3 < MAX_TABLE_SIZE;
    }
}

