/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b;

import com.a.b.a.a.b.g;
import com.a.b.a.a.c.l;
import com.a.b.a.a.e.a.c;
import com.a.b.a.a.e.a.e;
import com.codeforces.commons.io.IoUtil;
import com.codeforces.commons.reflection.Name;
import com.codeforces.commons.text.StringUtil;
import java.util.concurrent.atomic.AtomicLong;

public class f
implements g {
    private static final AtomicLong a = new AtomicLong();
    @Name(value="id")
    private final long b = a.incrementAndGet();
    @Name(value="name")
    private final String c;
    private final long d = com.a.a.a.a.c.b();
    private final e e;
    private boolean f;
    private String g;
    private long h;
    private long i;
    private long j;
    private int k;
    private final l l;
    private boolean m;

    public f(String string, e e2, l l2, boolean bl) {
        this.c = string;
        this.e = e2;
        this.l = l2;
        this.m = bl;
    }

    public final long a() {
        return this.b;
    }

    public String b() {
        return this.c;
    }

    public long c() {
        return this.d;
    }

    public e d() {
        return this.e;
    }

    public boolean e() {
        return this.e instanceof c;
    }

    public boolean f() {
        return this.f;
    }

    public String g() {
        return this.g;
    }

    public void a(String string) {
        if (this.e()) {
            return;
        }
        this.f = true;
        this.g = string;
        IoUtil.closeQuietly((AutoCloseable)this.e);
    }

    public long h() {
        return this.h;
    }

    public long i() {
        return this.i;
    }

    public void a(long l2) {
        this.h = l2;
        this.i = l2;
    }

    public void b(long l2) {
        this.j = l2;
        this.h -= l2;
    }

    public int j() {
        return this.k;
    }

    @Override
    public void a(int n2) {
        this.k += n2;
    }

    public l k() {
        return this.l;
    }

    public boolean l() {
        return this.m;
    }

    public final boolean equals(Object object) {
        return this == object || object != null && this.getClass() == object.getClass() && this.b == ((f)object).b;
    }

    public final int hashCode() {
        return Long.hashCode(this.b);
    }

    public String toString() {
        return StringUtil.toString((Object)this, false, "id", "name");
    }
}

