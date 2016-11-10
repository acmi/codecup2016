/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.e;

import com.a.b.a.a.c.p;

public final class n {
    public static final p[] a = new p[0];

    public static boolean a(p[] arrp, p[] arrp2) {
        if (arrp == arrp2) {
            return true;
        }
        if (arrp == null || arrp2 == null) {
            return false;
        }
        int n2 = arrp.length;
        if (n2 != arrp2.length) {
            return false;
        }
        int n3 = n2;
        while (--n3 >= 0) {
            if (p.areFieldEquals(arrp[n3], arrp2[n3])) continue;
            return false;
        }
        return true;
    }
}

