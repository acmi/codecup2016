/*
 * Decompiled with CFR 0_119.
 */
package a.a.f.a;

import a.a.a.a.g;
import a.a.f.a.d;
import a.a.f.a.e;
import java.io.IOException;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Iterator;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class c<E>
extends a.a.f.a.a<E> {
    a.a.c.a l;

    public c() {
    }

    public c(int n2) {
        super(n2);
    }

    @Override
    public int c(int n2) {
        this.l = new d(this, n2);
        return super.c(n2);
    }

    @Override
    public void clear() {
        super.clear();
        this.l.c();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{");
        boolean bl = true;
        a.a.b.a.a<E> a2 = this.d();
        while (a2.hasNext()) {
            if (bl) {
                bl = false;
            } else {
                stringBuilder.append(", ");
            }
            stringBuilder.append(a2.next());
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public boolean add(E e2) {
        int n2 = this.b(e2);
        if (n2 < 0) {
            return false;
        }
        if (!this.l.a(n2)) {
            throw new IllegalStateException("Order not changed after insert");
        }
        this.b(this.k);
        return true;
    }

    @Override
    protected void b(int n2) {
        this.l.b(n2);
        super.b(n2);
    }

    @Override
    protected void d(int n2) {
        a.a.c.b.a a2 = new a.a.c.b.a(this.l);
        int n3 = this.size();
        Object[] arrobject = this.h;
        this.l.c();
        this.h = new Object[n2];
        Arrays.fill(this.h, j);
        a.a.b.b b2 = a2.a();
        while (b2.hasNext()) {
            int n4 = b2.a();
            Object object = arrobject[n4];
            if (object == j || object == i) {
                throw new IllegalStateException("Iterating over empty location while rehashing");
            }
            if (object == j || object == i) continue;
            int n5 = this.b(object);
            if (n5 < 0) {
                this.a(this.h[- n5 - 1], object, this.size(), n3, arrobject);
            }
            if (this.l.a(n5)) continue;
            throw new IllegalStateException("Order not changed after insert");
        }
    }

    @Override
    protected void a(ObjectOutput objectOutput) throws IOException {
        b b2 = new b(objectOutput);
        if (!this.l.a(b2)) {
            throw b2.a();
        }
    }

    @Override
    public a.a.b.a.a<E> d() {
        return new e(this, this);
    }

    @Override
    public boolean a(a.a.e.e<? super E> e2) {
        a a2 = new a(this.h, e2);
        return this.l.a(a2);
    }

    @Override
    public /* synthetic */ Iterator iterator() {
        return this.d();
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    class a
    implements a.a.e.b {
        boolean a;
        final Object[] b;
        final a.a.e.e<? super E> c;

        public a(Object[] arrobject, a.a.e.e<? super E> e2) {
            this.a = false;
            this.b = arrobject;
            this.c = e2;
        }

        @Override
        public boolean a(int n2) {
            return this.c.a((Object)this.b[n2]);
        }
    }

    class b
    implements a.a.e.b {
        final ObjectOutput a;
        IOException b;

        b(ObjectOutput objectOutput) {
            this.a = objectOutput;
        }

        public IOException a() {
            return this.b;
        }

        public boolean a(int n2) {
            try {
                this.a.writeObject(c.this.h[n2]);
            }
            catch (IOException iOException) {
                this.b = iOException;
                return false;
            }
            return true;
        }
    }

}

