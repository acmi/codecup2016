/*
 * Decompiled with CFR 0_119.
 */
package a.a.f.a;

import a.a.e.e;
import a.a.f.a.a;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class b
implements e<E> {
    private boolean c;
    final /* synthetic */ StringBuilder a;
    final /* synthetic */ a b;

    b(a a2, StringBuilder stringBuilder) {
        this.b = a2;
        this.a = stringBuilder;
        this.c = true;
    }

    @Override
    public boolean a(Object object) {
        if (this.c) {
            this.c = false;
        } else {
            this.a.append(", ");
        }
        this.a.append(object);
        return true;
    }
}

