/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.d.c;

import com.a.b.a.a.b.d.d;
import com.a.b.a.a.b.e.o;
import com.a.b.a.a.c.l;
import com.a.b.a.a.c.n;
import com.a.b.a.a.c.r;
import com.a.c.c;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.reflection.Name;
import com.codeforces.commons.text.StringUtil;
import com.google.common.base.Preconditions;

public abstract class b
extends d {
    @Name(value="type")
    private final r a;
    private a b;
    private int c;
    private n d;
    private Integer e;

    protected b(double d2, double d3, double d4, l l2, r r2) {
        super(new com.a.c.a.b(25.0), l2, 100.0, 400.0);
        this.a = r2;
        if (Double.isNaN(d2) || Double.isInfinite(d2)) {
            throw new IllegalArgumentException("Argument 'x' is not a valid number.");
        }
        if (Double.isNaN(d3) || Double.isInfinite(d3)) {
            throw new IllegalArgumentException("Argument 'y' is not a valid number.");
        }
        if (Double.isNaN(d4) || Double.isInfinite(d4)) {
            throw new IllegalArgumentException("Argument 'angle' is not a valid number.");
        }
        this.b().a(d2);
        this.b().b(d3);
        this.b().c(d4);
        this.b().d(1.0);
    }

    public r h() {
        return this.a;
    }

    public a i() {
        return this.b;
    }

    public int s() {
        return this.c;
    }

    public void a(a a2, int n2) {
        this.b = a2;
        this.c = n2;
    }

    public n t() {
        return this.d;
    }

    public void a(n n2) {
        this.d = n2;
    }

    public void a(int n2) {
        this.e = n2;
    }

    public int c(int n2) {
        if (this.e == null) {
            return 0;
        }
        return Math.max(o.a(this.a) - n2 + this.e, 0);
    }

    @Override
    public String toString() {
        return StringUtil.toString((Object)this, false, "id", "body.name", "type", "faction");
    }

    public static final class b
    extends Enum<b> {
        public static final /* enum */ b a = new b();
        private static final /* synthetic */ b[] $VALUES;

        public static b[] values() {
            return (b[])$VALUES.clone();
        }

        private b() {
            super(string, n2);
        }

        static {
            $VALUES = new b[]{a};
        }
    }

    public static final class a {
        @Name(value="type")
        private final b a;
        @Name(value="unitId")
        private final com.a.b.d b;
        @Name(value="locationX")
        private final Double c;
        @Name(value="locationY")
        private final Double d;

        public a(b b2, com.a.b.d d2) {
            this.a = Preconditions.checkNotNull(b2);
            this.b = Preconditions.checkNotNull(d2);
            this.c = null;
            this.d = null;
        }

        public a(b b2, double d2, double d3) {
            this.a = Preconditions.checkNotNull(b2);
            this.b = null;
            this.c = d2;
            this.d = d3;
        }

        public b a() {
            return this.a;
        }

        public com.a.b.d b() {
            return this.b;
        }

        public Double c() {
            return this.c;
        }

        public Double d() {
            return this.d;
        }

        public String toString() {
            return StringUtil.toString((Object)this, true, "type", "unit.id", "location");
        }
    }

}

