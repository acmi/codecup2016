/*
 * Decompiled with CFR 0_119.
 */
package a.a.b.a;

import a.a.a.a.b;
import a.a.a.a.g;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class a<E>
extends b<E> {
    protected final g d;

    public a(g<E> g2) {
        super(g2);
        this.d = g2;
    }

    @Override
    protected E a(int n2) {
        Object object = this.d.h[n2];
        if (object == g.j || object == g.i) {
            return null;
        }
        return (E)object;
    }
}

