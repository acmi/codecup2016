/*
 * Decompiled with CFR 0_119.
 */
package a.a.a.a;

import a.a.a.a;
import a.a.a.a.h;
import a.a.a.b;
import java.util.Arrays;

public abstract class e
extends h {
    public transient long[] h;
    protected long i = a.e;
    protected boolean j;

    public e() {
        if (this.i != 0) {
            Arrays.fill(this.h, this.i);
        }
    }

    public e(int n2) {
        super(n2);
        if (this.i != 0) {
            Arrays.fill(this.h, this.i);
        }
    }

    public e(int n2, float f2) {
        super(n2, f2);
        if (this.i != 0) {
            Arrays.fill(this.h, this.i);
        }
    }

    protected int c(int n2) {
        int n3 = super.c(n2);
        this.h = new long[n3];
        return n3;
    }

    public boolean a(long l2) {
        return this.c_(l2) >= 0;
    }

    protected void b(int n2) {
        this.h[n2] = this.i;
        super.b(n2);
    }

    protected int c_(long l2) {
        byte[] arrby = this.l;
        long[] arrl = this.h;
        int n2 = arrby.length;
        int n3 = b.a(l2) & Integer.MAX_VALUE;
        int n4 = n3 % n2;
        byte by = arrby[n4];
        if (by == 0) {
            return -1;
        }
        if (by == 1 && arrl[n4] == l2) {
            return n4;
        }
        return this.a(l2, n4, n3, by);
    }

    int a(long l2, int n2, int n3, byte by) {
        int n4 = this.h.length;
        int n5 = 1 + n3 % (n4 - 2);
        int n6 = n2;
        do {
            if ((n2 -= n5) < 0) {
                n2 += n4;
            }
            if ((by = this.l[n2]) == 0) {
                return -1;
            }
            if (l2 != this.h[n2] || by == 2) continue;
            return n2;
        } while (n2 != n6);
        return -1;
    }

    protected int c(long l2) {
        int n2 = b.a(l2) & Integer.MAX_VALUE;
        int n3 = n2 % this.l.length;
        byte by = this.l[n3];
        this.j = false;
        if (by == 0) {
            this.j = true;
            this.a(n3, l2);
            return n3;
        }
        if (by == 1 && this.h[n3] == l2) {
            return - n3 - 1;
        }
        return this.b(l2, n3, n2, by);
    }

    int b(long l2, int n2, int n3, byte by) {
        int n4 = this.h.length;
        int n5 = 1 + n3 % (n4 - 2);
        int n6 = n2;
        int n7 = -1;
        do {
            if (by == 2 && n7 == -1) {
                n7 = n2;
            }
            if ((n2 -= n5) < 0) {
                n2 += n4;
            }
            if ((by = this.l[n2]) == 0) {
                if (n7 != -1) {
                    this.a(n7, l2);
                    return n7;
                }
                this.j = true;
                this.a(n2, l2);
                return n2;
            }
            if (by != 1 || this.h[n2] != l2) continue;
            return - n2 - 1;
        } while (n2 != n6);
        if (n7 != -1) {
            this.a(n7, l2);
            return n7;
        }
        throw new IllegalStateException("No free or removed slots available. Key set full?!!");
    }

    void a(int n2, long l2) {
        this.h[n2] = l2;
        this.l[n2] = 1;
    }
}

