/*
 * Decompiled with CFR 0_119.
 */
package a.a.c.b;

import a.a.c.b.a;
import java.util.NoSuchElementException;

class b
implements a.a.b.b {
    a.a a;
    a.a b;
    final /* synthetic */ a c;

    b(a a2) {
        this.c = a2;
        this.a = this.c.c;
    }

    public int a() {
        if (a.b(this.a)) {
            throw new NoSuchElementException();
        }
        int n2 = this.a.a();
        this.b = this.a;
        this.a = this.a.c();
        return n2;
    }

    public boolean hasNext() {
        return a.a(this.a);
    }

    public void remove() {
        if (this.b == null) {
            throw new IllegalStateException();
        }
        a.a(this.c, this.b);
        this.b = null;
    }
}

