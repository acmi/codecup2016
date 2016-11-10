/*
 * Decompiled with CFR 0_119.
 */
package a.a.d.a;

import a.a.d.a.d;
import a.a.e.c;

class e
implements c {
    private boolean c;
    final /* synthetic */ StringBuilder a;
    final /* synthetic */ d b;

    e(d d2, StringBuilder stringBuilder) {
        this.b = d2;
        this.a = stringBuilder;
        this.c = true;
    }

    public boolean a(long l2, int n2) {
        if (this.c) {
            this.c = false;
        } else {
            this.a.append(", ");
        }
        this.a.append(l2);
        this.a.append("=");
        this.a.append(n2);
        return true;
    }
}

