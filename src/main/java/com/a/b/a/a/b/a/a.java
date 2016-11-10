/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.a;

import com.a.b.a.a.c.k;
import com.a.b.d;
import com.a.c.c;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.lang3.ObjectUtils;

public class a {
    private static final AtomicLong a = new AtomicLong();
    private final long b = a.incrementAndGet();
    private final k c;
    private final int d;
    private d e;
    private Double f;
    private Double g;
    private Double h;
    private final Map<String, Object> i;

    public a(k k2, int n2, Map<String, Object> map) {
        this.c = k2;
        this.d = n2;
        this.i = new HashMap<String, Object>(map);
    }

    public a(k k2, int n2, d d2, Map<String, Object> map) {
        this(k2, n2, map);
        this.e = d2;
        this.f = 0.0;
        this.g = 0.0;
        this.h = 0.0;
    }

    public final long a() {
        return this.b;
    }

    public k b() {
        return this.c;
    }

    public int a(int n2) {
        return n2 - this.d;
    }

    public boolean b(int n2) {
        return this.a(n2) >= this.c.getDuration();
    }

    public d c() {
        return this.e;
    }

    public Double d() {
        return this.e == null || this.f == null ? ObjectUtils.defaultIfNull(this.f, 0.0) : Double.valueOf(this.e.b().c() + this.f);
    }

    public Double e() {
        return this.e == null || this.g == null ? ObjectUtils.defaultIfNull(this.g, 0.0) : Double.valueOf(this.e.b().d() + this.g);
    }

    public Double f() {
        return this.e == null || this.h == null ? ObjectUtils.defaultIfNull(this.h, 0.0) : Double.valueOf(this.e.b().e() + this.h);
    }

    public Map<String, Object> g() {
        return Collections.unmodifiableMap(this.i);
    }
}

