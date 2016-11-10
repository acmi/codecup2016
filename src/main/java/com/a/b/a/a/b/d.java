/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b;

import com.a.b.a.a.b.a.a;
import com.a.b.e;
import com.a.b.f;
import com.a.b.g;
import com.a.c.b;
import com.a.c.c;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;
import com.google.inject.Inject;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class d
implements g {
    @Inject
    private com.a.c.e a;
    private final Map<Long, com.a.b.d> b = new HashMap<Long, com.a.b.d>();
    private final Map<Long, com.a.b.d> c = new HashMap<Long, com.a.b.d>();
    private final List<a> d = new LinkedList<a>();
    private final boolean e;

    public d(boolean bl) {
        this.e = bl;
    }

    @Override
    public <U extends com.a.b.d> U a(U u2) {
        c c2 = this.a.a(u2.b());
        this.a.b(c2);
        this.b.put(u2.a(), (com.a.b.d)u2);
        this.c.put(c2.a(), (com.a.b.d)u2);
        u2.a(c2);
        return u2;
    }

    @Override
    public void b(com.a.b.d d2) {
        this.a.d(d2.b());
        this.b.remove(d2.a());
        this.c.remove(d2.b().a());
    }

    @Override
    public void c(com.a.b.d d2) {
        this.a.b(d2.b());
    }

    @Override
    public List<com.a.b.d> d(com.a.b.d d2) {
        if (!this.a.e(d2.b())) {
            return Collections.emptyList();
        }
        List<c> list = this.a.f(d2.b());
        int n2 = list.size();
        ArrayList<com.a.b.d> arrayList = new ArrayList<com.a.b.d>(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            arrayList.add(this.c.get(list.get(i2).a()));
        }
        return Collections.unmodifiableList(arrayList);
    }

    @Override
    public List<com.a.b.d> a() {
        return Collections.unmodifiableList(new ArrayList<com.a.b.d>(this.b.values()));
    }

    @Override
    public a a(a a2) {
        this.d.add(a2);
        return a2;
    }

    @Override
    public List<a> b() {
        return Collections.unmodifiableList(this.d);
    }

    @Override
    public void c() {
        if (this.e) {
            for (com.a.b.d d2 : this.b.values()) {
                this.a.b(d2.b());
            }
        } else {
            for (com.a.b.d d2 : this.b.values()) {
                if (d2.b().u()) continue;
                this.a.b(d2.b());
            }
        }
        this.a.a();
        if (this.e) {
            for (com.a.b.d d2 : this.b.values()) {
                d2.a(this.a.a(d2.b()));
            }
        } else {
            for (com.a.b.d d2 : this.b.values()) {
                if (d2.b().u()) continue;
                d2.a(this.a.a(d2.b()));
            }
        }
    }

    @Override
    public void a(int n2) {
        Iterator<a> iterator = this.d.iterator();
        while (iterator.hasNext()) {
            a a2 = iterator.next();
            if (!a2.b(n2)) continue;
            iterator.remove();
        }
    }

    @Override
    public <U extends com.a.b.d> void a(Class<U> class_, f<U, U> f2) {
        this.a(class_, class_, f2);
    }

    @Override
    public <A extends com.a.b.d, B extends com.a.b.d> void a(Class<A> class_, Class<B> class_2, f<A, B> f2) {
        Method method;
        Method method2;
        Method method3;
        try {
            method = f2.getClass().getMethod("beforeCollision", g.class, com.a.b.d.class, com.a.b.d.class);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new IllegalArgumentException(String.format("Listener %s doesn't implement beforeCollision(...) method.", f2.getClass().getSimpleName()), noSuchMethodException);
        }
        try {
            method3 = f2.getClass().getMethod("beforeResolvingCollision", e.class);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new IllegalArgumentException(String.format("Listener %s doesn't implement beforeResolvingCollision(...) method.", f2.getClass().getSimpleName()), noSuchMethodException);
        }
        try {
            method2 = f2.getClass().getMethod("afterCollision", e.class);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new IllegalArgumentException(String.format("Listener %s doesn't implement afterCollision(...) method.", f2.getClass().getSimpleName()), noSuchMethodException);
        }
        b b2 = a2 -> {
            com.a.b.d d2;
            if (method2.getDeclaringClass() == f.class) {
                return;
            }
            com.a.b.d d3 = a2.a() == null ? null : this.c.get(a2.a().a());
            com.a.b.d d4 = d2 = a2.b() == null ? null : this.c.get(a2.b().a());
            if (class_.isInstance(d3) && class_2.isInstance(d2)) {
                this.a(d3, d2);
                f2.afterCollision(new e<com.a.b.d, com.a.b.d>(this, d3, d2, a2.c().copy(), a2.d().copy()));
                this.b(d3, d2);
            } else if (class_.isInstance(d2) && class_2.isInstance(d3)) {
                this.a(d3, d2);
                f2.afterCollision(new e<com.a.b.d, com.a.b.d>(this, d2, d3, a2.c().copy(), a2.d().copyNegate()));
                this.b(d3, d2);
            }
        };
        if (method.getDeclaringClass() == f.class && method3.getDeclaringClass() == f.class) {
            this.a.a(b2);
        } else {
            this.a.a(new com.a.b.a.a.b.e(this, method, class_, class_2, f2, method3, b2));
        }
    }

    @Override
    public int d() {
        return this.a.b();
    }

    private void a(com.a.b.d d2, com.a.b.d d3) {
        if (this.e) {
            d2.a(this.a.a(d2.b()));
            d3.a(this.a.a(d3.b()));
        } else {
            if (!d2.b().u()) {
                d2.a(this.a.a(d2.b()));
            }
            if (!d3.b().u()) {
                d3.a(this.a.a(d3.b()));
            }
        }
    }

    private void b(com.a.b.d d2, com.a.b.d d3) {
        if (this.e) {
            this.a.c(d2.b());
            this.a.c(d3.b());
        } else {
            if (!d2.b().u()) {
                this.a.c(d2.b());
            }
            if (!d3.b().u()) {
                this.a.c(d3.b());
            }
        }
    }

    static /* synthetic */ Map a(d d2) {
        return d2.c;
    }

    static /* synthetic */ void a(d d2, com.a.b.d d3, com.a.b.d d4) {
        d2.a(d3, d4);
    }

    static /* synthetic */ void b(d d2, com.a.b.d d3, com.a.b.d d4) {
        d2.b(d3, d4);
    }
}

