/*
 * Decompiled with CFR 0_119.
 */
package a.a.a.a;

import a.a.a.a.a;
import a.a.a.a.g;
import a.a.b.d;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class b<V>
implements d,
Iterator<V> {
    private final g<V> d;
    protected final a a;
    protected int b;
    protected int c;

    protected b(g<V> g2) {
        this.a = g2;
        this.b = this.a.size();
        this.c = this.a.a();
        this.d = g2;
    }

    @Override
    public V next() {
        this.a();
        return this.a(this.c);
    }

    @Override
    public boolean hasNext() {
        return this.b() >= 0;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
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

    protected final void a() {
        this.c = this.b();
        if (this.c < 0) {
            throw new NoSuchElementException();
        }
    }

    protected final int b() {
        if (this.b != this.a.size()) {
            throw new ConcurrentModificationException();
        }
        Object[] arrobject = this.d.h;
        int n2 = this.c;
        while (n2-- > 0 && (arrobject[n2] == g.j || arrobject[n2] == g.i)) {
        }
        return n2;
    }

    protected abstract V a(int var1);
}

