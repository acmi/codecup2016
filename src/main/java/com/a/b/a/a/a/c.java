/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.a;

import com.a.b.a.a.a.b;
import java.util.Collections;

public final class c {
    public static final int[] a = new int[25];

    public static boolean a(Integer n2) {
        return n2 != null && (n2 == 1 || Collections.singletonList(1).contains(n2));
    }

    public static double a(b b2) {
        if (b2.s()) {
            return 1.0;
        }
        if (b2.r()) {
            return 0.5;
        }
        return 0.25;
    }

    static {
        int n2 = a.length;
        while (--n2 >= 0) {
            c.a[n2] = 50 * (n2 + 1);
        }
    }
}

