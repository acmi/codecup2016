/*
 * Decompiled with CFR 0_119.
 */
package a.a.f.a;

import a.a.c.a.a;
import a.a.f.a.c;

class d
extends a {
    final /* synthetic */ c d;

    d(c c2, int n2) {
        this.d = c2;
        super(n2);
    }

    public void c(int n2) {
        if (n2 > this.a.length) {
            int n3 = Math.max(this.d.h.length, n2);
            int[] arrn = new int[n3];
            System.arraycopy(this.a, 0, arrn, 0, this.a.length);
            this.a = arrn;
        }
    }
}

