/*
 * Decompiled with CFR 0_119.
 */
package a.a.a.a;

import a.a.a.a.a;
import a.a.a.b;

public abstract class h
extends a {
    public transient byte[] l;

    public h() {
    }

    public h(int n2) {
        this(n2, 0.5f);
    }

    public h(int n2, float f2) {
        n2 = Math.max(1, n2);
        this.c = f2;
        this.c(b.a((float)n2 / f2));
    }

    public int a() {
        return this.l.length;
    }

    protected void b(int n2) {
        this.l[n2] = 2;
        super.b(n2);
    }

    protected int c(int n2) {
        int n3 = super.c(n2);
        this.l = new byte[n3];
        return n3;
    }
}

