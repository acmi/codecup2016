/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.d;

import com.a.b.a.a.b.d.c;
import com.a.b.a.a.c.A;
import com.a.b.a.a.c.l;
import com.a.b.d;
import com.codeforces.commons.reflection.Name;
import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class b
extends d {
    private double a;
    private double b;
    private final List<a> c = new LinkedList<a>();
    private int d;

    protected b(com.a.c.a.c c2, l l2, double d2) {
        super(c2, l2);
        Preconditions.checkArgument(!Double.isNaN(d2) && !Double.isInfinite(d2) && d2 > 0.0);
        this.a = d2;
        this.b = d2;
    }

    public final double j() {
        return this.a;
    }

    public final void b(double d2) {
        this.a = d2;
    }

    public double k() {
        return this.b;
    }

    public List<a> l() {
        return Collections.unmodifiableList(this.c);
    }

    public List<a> a(A a2) {
        return this.c.stream().filter(a3 -> a3.c == a2).collect(Collectors.toList());
    }

    public boolean b(A a2) {
        for (a a3 : this.c) {
            if (a3.c != a2) continue;
            return true;
        }
        return false;
    }

    public void a(A a2, b b2, int n2) {
        this.c.add(new a(a2, b2, n2, null));
    }

    public void m() {
        this.c.clear();
    }

    public void n() {
        Iterator<a> iterator = this.c.iterator();
        while (iterator.hasNext()) {
            a a2 = iterator.next();
            if (a2.e > 1) {
                --a2.e;
                continue;
            }
            iterator.remove();
        }
    }

    public int o() {
        return this.d;
    }

    public void b(int n2) {
        this.d = n2;
    }

    public void p() {
        if (this.d > 0) {
            --this.d;
        }
    }

    public static final class a {
        private static final AtomicLong a = new AtomicLong();
        @Name(value="id")
        private final long b = a.incrementAndGet();
        @Name(value="type")
        private final A c;
        @Name(value="caster")
        private final b d;
        @Name(value="remainingDurationTicks")
        private int e;

        private a(A a2, b b2, int n2) {
            this.c = Preconditions.checkNotNull(a2);
            this.d = b2;
            this.e = n2;
        }

        public long a() {
            return this.b;
        }

        public A b() {
            return this.c;
        }

        public b c() {
            return this.d;
        }

        public int d() {
            return this.e;
        }

        /* synthetic */ a(A a2, b b2, int n2, c c2) {
            this(a2, b2, n2);
        }
    }

}

