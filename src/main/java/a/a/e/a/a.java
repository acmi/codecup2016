/*
 * Decompiled with CFR 0_119.
 */
package a.a.e.a;

import a.a.e.e;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class a<T>
implements e<T> {
    private final T[] a;
    private int b = 0;

    public a(T[] arrT) {
        this.a = arrT;
    }

    @Override
    public final boolean a(T t2) {
        this.a[this.b++] = t2;
        return true;
    }
}

