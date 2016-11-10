/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.d.f;

import a.a.f.a.c;
import com.a.b.a.a.b.e.n;
import com.a.b.a.a.b.e.x;
import com.a.b.a.a.b.f;
import com.a.b.a.a.b.g;
import com.a.b.a.a.c.l;
import com.a.b.a.a.c.p;
import com.a.b.a.a.c.y;
import com.a.b.d;
import com.a.c.a.b;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.reflection.Name;
import com.codeforces.commons.text.StringUtil;
import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class a
extends com.a.b.a.a.b.d.a
implements g {
    private static final Logger a = LoggerFactory.getLogger(a.class);
    @Name(value="player")
    private final f b;
    private final int c;
    private final com.a.b.a.a.a.b d;
    private int e;
    private int f;
    private Integer g;
    private Integer h;
    private final Map<com.a.b.a.a.c.a, Integer> i = new EnumMap<com.a.b.a.a.c.a, Integer>(com.a.b.a.a.c.a.class);
    private final Set<y> j = new c<y>(com.a.b.a.a.a.c.a.length);
    private com.a.b.a.a.c.a k = com.a.b.a.a.c.a.NONE;
    private Integer l;
    private p[] m = n.a;

    public a(f f2, int n2, double d2, double d3, double d4, l l2, com.a.b.a.a.a.b b2) {
        super(new b(35.0), l2, 100.0, 600.0, 100.0);
        if (Double.isNaN(d2) || Double.isInfinite(d2)) {
            throw new IllegalArgumentException("Argument 'x' is not a valid number.");
        }
        if (Double.isNaN(d3) || Double.isInfinite(d3)) {
            throw new IllegalArgumentException("Argument 'y' is not a valid number.");
        }
        if (Double.isNaN(d4) || Double.isInfinite(d4)) {
            throw new IllegalArgumentException("Argument 'angle' is not a valid number.");
        }
        this.b = f2;
        this.c = n2;
        this.d = b2;
        this.e = 0;
        this.f = 0;
        this.b().a(d2);
        this.b().b(d3);
        this.b().c(d4);
        this.b().d(1.0);
    }

    public f s() {
        return this.b;
    }

    public int t() {
        return this.e;
    }

    public int u() {
        return this.f;
    }

    private void d(int n2) {
        this.e += n2;
        if (this.d.r()) {
            int n3 = 0;
            for (int i2 = 0; i2 < com.a.b.a.a.a.c.a.length && this.e >= (n3 += com.a.b.a.a.a.c.a[i2]); ++i2) {
                this.f = Math.max(this.f, i2 + 1);
            }
        }
    }

    @Override
    public void a(int n2) {
        this.d(n2);
        this.b.a(n2);
    }

    @Override
    public double k() {
        return 100 + this.f * 10;
    }

    @Override
    public double i() {
        return 100 + this.f * 10;
    }

    public Integer v() {
        return this.g;
    }

    public Integer w() {
        return this.h;
    }

    public void a(Integer n2) {
        this.h = n2;
    }

    public int a(com.a.b.a.a.c.a a2, int n2) {
        Integer n3 = this.i.get((Object)a2);
        if (n3 == null) {
            return 0;
        }
        int n4 = com.a.b.a.a.b.e.a.a(this, a2);
        int n5 = n4 - n2 + n3;
        return n5 < 0 ? 0 : n5;
    }

    public int c(int n2) {
        if (this.k == com.a.b.a.a.c.a.NONE || this.l == null) {
            return 0;
        }
        int n3 = 30 - n2 + this.l;
        return n3 < 0 ? 0 : n3;
    }

    public Set<y> x() {
        return Collections.unmodifiableSet(this.j);
    }

    public boolean a(y y2) {
        return this.j.contains((Object)Preconditions.checkNotNull(y2));
    }

    public boolean b(y y2) {
        if (this.a(y2) || this.j.size() >= this.f) {
            return false;
        }
        y y3 = x.a(y2);
        return y3 == null || this.a(y3);
    }

    public void c(y y2) {
        if (!this.b(y2)) {
            throw new IllegalStateException(String.format("Can't learn skill '%s': hasSkill=%b, level=%d, skills.size()=%d, skills=%s.", new Object[]{y2, this.a(y2), this.f, this.j.size(), this.j}));
        }
        this.j.add(y2);
        if (x.a.contains((Object)y2)) {
            a.info(this + " has learned " + (Object)((Object)y2) + '.');
        }
    }

    public boolean a(y y2, List<a> list) {
        if (this.a(y2)) {
            return true;
        }
        for (a a2 : list) {
            if (x.a(a2) || !a2.a(y2) || this.b(a2) > 500.0) continue;
            return true;
        }
        return false;
    }

    public com.a.b.a.a.c.a y() {
        return this.k;
    }

    public Integer z() {
        return this.l;
    }

    public void b(com.a.b.a.a.c.a a2, int n2) {
        this.k = Preconditions.checkNotNull(a2);
        this.l = n2;
        this.i.put(a2, n2);
    }

    public p[] A() {
        return this.m;
    }

    public void a(p[] arrp) {
        this.m = arrp;
    }

    public void B() {
        this.m = n.a;
    }

    @Override
    public String toString() {
        return StringUtil.toString((Object)this, false, "id", "player.name", "faction");
    }
}

