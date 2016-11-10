/*
 * Decompiled with CFR 0_119.
 */
package a.a.f.a;

import a.a.a.a.a;
import a.a.a.a.g;
import a.a.b.b;
import a.a.f.a.c;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class e
extends a.a.b.a.a<E> {
    b e;
    int f;
    final /* synthetic */ c g;

    e(c c2, g g2) {
        this.g = c2;
        super(g2);
        this.e = this.g.l.a();
    }

    @Override
    public E next() {
        this.f = this.e.a();
        return this.a(this.f);
    }

    @Override
    public boolean hasNext() {
        return this.e.hasNext();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void remove() {
        this.e.remove();
        try {
            this.a.c();
            this.g.b(this.f);
        }
        finally {
            this.a.a(false);
        }
    }
}

