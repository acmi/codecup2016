/*
 * Decompiled with CFR 0_119.
 */
package com.a.c.b.a;

import com.a.a.b.a;
import com.a.a.b.f;
import com.a.c.b.a.c;
import com.a.c.b.a.d;
import com.a.c.b.a.e;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class b
implements com.a.c.e {
    private final Lock a = new ReentrantLock();
    private final Map<Long, a> b = new HashMap<Long, a>();
    private final Map<Long, Long> c = new HashMap<Long, Long>();
    private final Map<Long, Long> d = new HashMap<Long, Long>();
    private final f e;

    public b() {
        this.e = new f();
    }

    public b(int n2, int n3, double d2, com.a.a.b.a.a a2) {
        this.e = new f(n2, n3, d2, a2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public com.a.c.c a(com.a.c.c c2) {
        this.a.lock();
        try {
            a a2 = this.c(c2.a());
            if (a2 == null) {
                a2 = e.a(c2);
                this.e.a(a2);
                this.b.put(a2.a(), a2);
                this.c.put(c2.a(), a2.a());
                this.d.put(a2.a(), c2.a());
                e.a(a2, c2, this);
            }
            com.a.c.c c3 = Preconditions.checkNotNull(this.e(c2.a()));
            return c3;
        }
        finally {
            this.a.unlock();
        }
    }

    @Override
    public void b(com.a.c.c c2) {
        this.a.lock();
        try {
            a a2 = this.c(c2.a());
            if (a2 == null) {
                throw new IllegalArgumentException("No body with ID " + c2.a() + '.');
            }
            e.a(a2, c2, this);
        }
        finally {
            this.a.unlock();
        }
    }

    @Override
    public void c(com.a.c.c c2) {
        this.a.lock();
        try {
            a a2 = this.c(c2.a());
            if (a2 != null) {
                e.a(a2, c2, this);
            }
        }
        finally {
            this.a.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public com.a.c.c d(com.a.c.c c2) {
        this.a.lock();
        try {
            Long l2 = this.a(c2.a());
            if (l2 == null) {
                com.a.c.c c3 = null;
                return c3;
            }
            a a2 = this.b.remove(l2);
            if (a2 == null) {
                com.a.c.c c4 = null;
                return c4;
            }
            com.a.c.c c5 = e.a(a2, this);
            this.e.b(a2);
            this.c.remove(c2.a());
            this.d.remove(l2);
            com.a.c.c c6 = c5;
            return c6;
        }
        finally {
            this.a.unlock();
        }
    }

    @Override
    public boolean e(com.a.c.c c2) {
        this.a.lock();
        try {
            boolean bl = this.c(c2.a()) != null;
            return bl;
        }
        finally {
            this.a.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public List<com.a.c.c> f(com.a.c.c c2) {
        this.a.lock();
        try {
            a a2 = this.c(c2.a());
            if (a2 == null) {
                throw new IllegalArgumentException("No body with ID " + c2.a() + '.');
            }
            List<com.a.a.b.b.f> list = this.e.d(a2);
            ArrayList<com.a.c.c> arrayList = new ArrayList<com.a.c.c>(list.size());
            for (com.a.a.b.b.f f2 : list) {
                arrayList.add(e.a(f2.b(), this));
            }
            ArrayList<com.a.c.c> arrayList2 = arrayList;
            return arrayList2;
        }
        finally {
            this.a.unlock();
        }
    }

    @Override
    public void a() {
        this.a.lock();
        try {
            this.e.d();
        }
        finally {
            this.a.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void a(com.a.c.b b2) {
        block5 : {
            this.a.lock();
            try {
                String string;
                while (this.e.a(string = com.a.a.a.a.c.e())) {
                }
                if (b2 instanceof com.a.c.d) {
                    com.a.c.d d2 = (com.a.c.d)b2;
                    this.e.a(new c(this, d2), string);
                    break block5;
                }
                this.e.a(new d(this, b2), string);
            }
            finally {
                this.a.unlock();
            }
        }
    }

    @Override
    public int b() {
        return this.e.a();
    }

    f c() {
        return this.e;
    }

    Long a(long l2) {
        return this.c.get(l2);
    }

    Long b(long l2) {
        return this.d.get(l2);
    }

    a c(long l2) {
        Long l3 = this.a(l2);
        return l3 == null ? null : this.b.get(l3);
    }

    com.a.c.c d(long l2) {
        a a2 = this.b.get(l2);
        return a2 == null ? null : e.a(a2, this);
    }

    com.a.c.c e(long l2) {
        Long l3 = this.a(l2);
        return l3 == null ? null : this.d(l3);
    }
}

