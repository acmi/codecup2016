/*
 * Decompiled with CFR 0_119.
 */
package a.a.d.a;

import a.a.d.a.a;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class c
implements a.a.e.a<V> {
    private boolean c;
    final /* synthetic */ StringBuilder a;
    final /* synthetic */ a b;

    c(a a2, StringBuilder stringBuilder) {
        this.b = a2;
        this.a = stringBuilder;
        this.c = true;
    }

    @Override
    public boolean a(int n2, Object object) {
        if (this.c) {
            this.c = false;
        } else {
            this.a.append(",");
        }
        this.a.append(n2);
        this.a.append("=");
        this.a.append(object);
        return true;
    }
}

