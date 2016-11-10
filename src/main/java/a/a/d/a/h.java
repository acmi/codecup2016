/*
 * Decompiled with CFR 0_119.
 */
package a.a.d.a;

import a.a.d.a.f;
import a.a.e.d;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class h
implements d<V> {
    private boolean c;
    final /* synthetic */ StringBuilder a;
    final /* synthetic */ f b;

    h(f f2, StringBuilder stringBuilder) {
        this.b = f2;
        this.a = stringBuilder;
        this.c = true;
    }

    @Override
    public boolean a(long l2, Object object) {
        if (this.c) {
            this.c = false;
        } else {
            this.a.append(",");
        }
        this.a.append(l2);
        this.a.append("=");
        this.a.append(object);
        return true;
    }
}

