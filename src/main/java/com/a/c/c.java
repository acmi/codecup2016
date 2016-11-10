/*
 * Decompiled with CFR 0_119.
 */
package com.a.c;

import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.reflection.Name;
import com.codeforces.commons.text.StringUtil;
import java.util.Collections;
import java.util.Set;

public abstract class c {
    @Name(value="id")
    private final long a;
    @Name(value="name")
    private String b;
    @Name(value="mass")
    private double c;
    @Name(value="form")
    private com.a.c.a.c d;
    @Name(value="staticBody")
    private boolean e;
    @Name(value="excludedBodyIds")
    private Set<Long> f;

    protected c(long l2) {
        this.a = l2;
    }

    public final long a() {
        return this.a;
    }

    public final String b() {
        return this.b;
    }

    public final void a(String string) {
        this.b = string;
    }

    public abstract double c();

    public abstract void a(double var1);

    public abstract double d();

    public abstract void b(double var1);

    public abstract double e();

    public abstract void c(double var1);

    public abstract Vector2D f();

    public abstract void a(Vector2D var1);

    public abstract Vector2D g();

    public abstract double h();

    public abstract double i();

    public abstract Vector2D j();

    public abstract double k();

    public abstract double l();

    public abstract double m();

    public abstract double n();

    public abstract Double o();

    public abstract double p();

    public abstract double q();

    public abstract double r();

    public double s() {
        return this.c;
    }

    public void d(double d2) {
        this.c = d2;
    }

    public com.a.c.a.c t() {
        return this.d;
    }

    public void a(com.a.c.a.c c2) {
        this.d = c2;
    }

    public boolean u() {
        return this.e;
    }

    public void a(boolean bl) {
        this.e = bl;
    }

    public Set<Long> v() {
        return this.f == null ? Collections.emptySet() : Collections.unmodifiableSet(this.f);
    }

    public String toString() {
        return StringUtil.toString((Object)this, false, "id", "name", "x", "y");
    }
}

