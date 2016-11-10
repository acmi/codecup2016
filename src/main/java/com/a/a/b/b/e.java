/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.b;

import com.a.a.b.a;
import com.a.a.b.b.d;
import com.a.a.b.b.f;
import com.a.a.b.c.c;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;

public abstract class e
implements d {
    protected final double a;

    protected e(double d2) {
        this.a = d2;
    }

    @Override
    public final boolean c(a a2, a a3) {
        return this.a(a2, a3) || this.a(a3, a2);
    }

    @Override
    public final f d(a a2, a a3) {
        if (this.a(a2, a3)) {
            return this.b(a2, a3);
        }
        if (this.a(a3, a2)) {
            f f2 = this.b(a3, a2);
            return f2 == null ? null : new f(a2, a3, f2.c(), f2.d().negate(), f2.e(), this.a);
        }
        throw new IllegalArgumentException(String.format("Unsupported %s of %s or %s of %s.", c.a(a2.c()), a2, c.a(a3.c()), a3));
    }

    protected abstract boolean a(a var1, a var2);

    protected abstract f b(a var1, a var2);
}

