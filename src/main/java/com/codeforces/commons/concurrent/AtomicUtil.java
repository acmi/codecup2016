/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.concurrent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicUtil {
    public static void invert(AtomicBoolean atomicBoolean) {
        boolean bl;
        while (!atomicBoolean.compareAndSet(bl, !(bl = atomicBoolean.get()))) {
        }
    }

    public static void decrement(AtomicInteger atomicInteger, int n2) {
        int n3;
        int n4;
        while (!atomicInteger.compareAndSet(n3 = atomicInteger.get(), n4 = AtomicUtil.normalizeValue(n3 - 1, n2))) {
        }
    }

    private static int normalizeValue(int n2, int n3) {
        while (n2 > n3) {
            n2 -= n3 + 1;
        }
        while (n2 < 0) {
            n2 += n3 + 1;
        }
        return n2;
    }
}

