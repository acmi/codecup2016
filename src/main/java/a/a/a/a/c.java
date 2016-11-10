/*
 * Decompiled with CFR 0_119.
 */
package a.a.a.a;

import a.a.a.a.h;
import a.a.b.f;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public abstract class c
implements f {
    protected final h a;
    protected int b;
    protected int c;

    public c(h h2) {
        this.a = h2;
        this.b = this.a.size();
        this.c = this.a.a();
    }

    protected final int a_() {
        if (this.b != this.a.size()) {
            throw new ConcurrentModificationException();
        }
        byte[] arrby = this.a.l;
        int n2 = this.c;
        while (n2-- > 0 && arrby[n2] != 1) {
        }
        return n2;
    }

    public boolean hasNext() {
        return this.a_() >= 0;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void remove() {
        if (this.b != this.a.size()) {
            throw new ConcurrentModificationException();
        }
        try {
            this.a.c();
            this.a.b(this.c);
        }
        finally {
            this.a.a(false);
        }
        --this.b;
    }

    protected final void b_() {
        this.c = this.a_();
        if (this.c < 0) {
            throw new NoSuchElementException();
        }
    }
}

