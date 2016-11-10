/*
 * Decompiled with CFR 0_119.
 */
package a.a.a.a;

import a.a.a.a;
import a.a.a.a.h;
import a.a.a.b;
import java.util.Arrays;

public abstract class d
extends h {
    public transient int[] h;
    protected int i = a.d;
    protected boolean j;

    public d() {
        if (this.i != 0) {
            Arrays.fill(this.h, this.i);
        }
    }

    protected int c(int n2) {
        int n3 = super.c(n2);
        this.h = new int[n3];
        return n3;
    }

    public boolean g(int n2) {
        return this.h(n2) >= 0;
    }

    protected void b(int n2) {
        this.h[n2] = this.i;
        super.b(n2);
    }

    protected int h(int n2) {
        byte[] arrby = this.l;
        int[] arrn = this.h;
        int n3 = arrby.length;
        int n4 = b.a(n2) & Integer.MAX_VALUE;
        int n5 = n4 % n3;
        byte by = arrby[n5];
        if (by == 0) {
            return -1;
        }
        if (by == 1 && arrn[n5] == n2) {
            return n5;
        }
        return this.a(n2, n5, n4, by);
    }

    int a(int n2, int n3, int n4, byte by) {
        int n5 = this.h.length;
        int n6 = 1 + n4 % (n5 - 2);
        int n7 = n3;
        do {
            if ((n3 -= n6) < 0) {
                n3 += n5;
            }
            if ((by = this.l[n3]) == 0) {
                return -1;
            }
            if (n2 != this.h[n3] || by == 2) continue;
            return n3;
        } while (n3 != n7);
        return -1;
    }

    protected int i(int n2) {
        int n3 = b.a(n2) & Integer.MAX_VALUE;
        int n4 = n3 % this.l.length;
        byte by = this.l[n4];
        this.j = false;
        if (by == 0) {
            this.j = true;
            this.a(n4, n2);
            return n4;
        }
        if (by == 1 && this.h[n4] == n2) {
            return - n4 - 1;
        }
        return this.b(n2, n4, n3, by);
    }

    int b(int n2, int n3, int n4, byte by) {
        int n5 = this.h.length;
        int n6 = 1 + n4 % (n5 - 2);
        int n7 = n3;
        int n8 = -1;
        do {
            if (by == 2 && n8 == -1) {
                n8 = n3;
            }
            if ((n3 -= n6) < 0) {
                n3 += n5;
            }
            if ((by = this.l[n3]) == 0) {
                if (n8 != -1) {
                    this.a(n8, n2);
                    return n8;
                }
                this.j = true;
                this.a(n3, n2);
                return n3;
            }
            if (by != 1 || this.h[n3] != n2) continue;
            return - n3 - 1;
        } while (n3 != n7);
        if (n8 != -1) {
            this.a(n8, n2);
            return n8;
        }
        throw new IllegalStateException("No free or removed slots available. Key set full?!!");
    }

    void a(int n2, int n3) {
        this.h[n2] = n3;
        this.l[n2] = 1;
    }
}

