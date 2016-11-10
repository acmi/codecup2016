/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b;

import com.a.a.b.b.d;
import com.a.a.b.b.h;
import com.a.a.b.b.i;
import com.a.a.b.b.j;
import com.a.a.b.b.k;
import com.a.a.b.b.m;
import com.a.a.b.b.n;
import com.a.a.b.c;
import com.a.a.b.g;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.math.NumberUtil;
import com.codeforces.commons.pair.LongPair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class f {
    private static final Logger a = Logger.getLogger(f.class);
    private static final com.a.a.b.b.f b = new com.a.a.b.b.f(null, null, null, null, 0.0, 0.0);
    private final int c;
    private final int d;
    private final double e;
    private final double f;
    private final double g;
    private final com.a.a.b.a.a h;
    private final com.a.a.b.e.c i;
    private final ExecutorService j;
    private final Map<String, a> k = new HashMap<String, a>();
    private final SortedSet<a> l = new TreeSet<a>(a.a());
    private final Map<String, b> m = new HashMap<String, b>();
    private final SortedSet<b> n = new TreeSet<b>(b.a());

    public f() {
        this(10);
    }

    public f(int n2) {
        this(n2, 60);
    }

    public f(int n2, int n3) {
        this(n2, n3, 1.0E-7);
    }

    public f(int n2, int n3, double d2) {
        this(n2, n3, d2, new com.a.a.b.a.f());
    }

    public f(int n2, int n3, double d2, com.a.a.b.a.a a2) {
        this(n2, n3, d2, a2, null);
    }

    public f(int n2, int n3, double d2, com.a.a.b.a.a a2, com.a.a.b.e.c c2) {
        this(n2, n3, d2, a2, c2, false);
    }

    public f(int n2, int n3, double d2, com.a.a.b.a.a a2, com.a.a.b.e.c c2, boolean bl) {
        if (n2 < 1) {
            throw new IllegalArgumentException("Argument 'iterationCountPerStep' is zero or negative.");
        }
        if (n3 < 1) {
            throw new IllegalArgumentException("Argument 'stepCountPerTimeUnit' is zero or negative.");
        }
        if (Double.isNaN(d2) || Double.isInfinite(d2) || d2 < 1.0E-100 || d2 > 1.0) {
            throw new IllegalArgumentException("Argument 'epsilon' should be between 1.0E-100 and 1.0.");
        }
        if (a2 == null) {
            throw new IllegalArgumentException("Argument 'bodyList' is null.");
        }
        this.d = n3;
        this.c = n2;
        this.e = 1.0 / (double)(n3 * n2);
        this.f = d2;
        this.g = d2 * d2;
        this.h = a2;
        this.i = c2;
        this.j = bl ? new ThreadPoolExecutor(0, 1, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new g(this)) : null;
        this.a(new com.a.a.b.b.a(d2));
        this.a(new com.a.a.b.b.b(d2));
        this.a(new com.a.a.b.b.c(d2));
        this.a(new com.a.a.b.b.g(d2));
        this.a(new h(d2));
        this.a(new i(d2));
        this.a(new j(d2));
        this.a(new k(d2));
        this.a(new m(d2));
        this.a(new n(d2));
    }

    public int a() {
        return this.d;
    }

    public double b() {
        return this.f;
    }

    public void a(com.a.a.b.a a2) {
        if (a2.c() == null || a2.d() == 0.0) {
            throw new IllegalArgumentException("Specify form and mass of 'body' before adding to the world.");
        }
        this.h.a(a2);
    }

    public void b(com.a.a.b.a a2) {
        this.h.b(a2);
    }

    public boolean c(com.a.a.b.a a2) {
        return this.h.c(a2);
    }

    public List<com.a.a.b.a> c() {
        return this.h.a();
    }

    public List<com.a.a.b.b.f> d(com.a.a.b.a a2) {
        if (!this.h.c(a2)) {
            return Collections.emptyList();
        }
        ArrayList<com.a.a.b.b.f> arrayList = new ArrayList<com.a.a.b.b.f>();
        block0 : for (com.a.a.b.a a3 : this.h.d(a2)) {
            if (a2.e() && a3.e()) continue;
            for (a a4 : this.l) {
                if (!a4.c.c(a2, a3)) continue;
                com.a.a.b.b.f f2 = a4.c.d(a2, a3);
                if (f2 == null) continue block0;
                arrayList.add(f2);
                continue block0;
            }
        }
        return Collections.unmodifiableList(arrayList);
    }

    public void d() {
        List<com.a.a.b.a> list = this.c();
        int n2 = list.size();
        com.a.a.b.a[] arra = list.toArray(new com.a.a.b.a[n2]);
        if (n2 < 1000 || this.j == null) {
            this.a(arra, 0, n2);
            int n3 = this.c;
            while (--n3 >= 0) {
                this.b(arra, 0, n2);
                this.a(arra);
            }
            this.c(arra, 0, n2);
        } else {
            int n4 = n2 / 2;
            Future future = this.j.submit(() -> {
                this.a(arra, 0, n4);
            }
            );
            this.a(arra, n4, n2);
            f.a(future);
            int n5 = this.c;
            while (--n5 >= 0) {
                future = this.j.submit(() -> {
                    this.b(arra, 0, n4);
                }
                );
                this.b(arra, n4, n2);
                f.a(future);
                this.a(arra);
            }
            future = this.j.submit(() -> {
                this.c(arra, 0, n4);
            }
            );
            this.c(arra, n4, n2);
            f.a(future);
        }
    }

    private static void a(Future<?> future) {
        try {
            future.get(5, TimeUnit.MINUTES);
        }
        catch (InterruptedException interruptedException) {
            future.cancel(true);
            a.error("Thread has been interrupted while executing parallel task.", interruptedException);
            throw new RuntimeException("Thread has been interrupted while executing parallel task.", interruptedException);
        }
        catch (ExecutionException executionException) {
            future.cancel(true);
            a.error("Thread has failed while executing parallel task.", executionException);
            throw new RuntimeException("Thread has failed while executing parallel task.", executionException);
        }
        catch (TimeoutException timeoutException) {
            future.cancel(true);
            a.error("Thread has timed out while executing parallel task.", timeoutException);
            throw new RuntimeException("Thread has timed out while executing parallel task.", timeoutException);
        }
    }

    private void a(com.a.a.b.a[] arra, int n2, int n3) {
        for (int i2 = n2; i2 < n3; ++i2) {
            com.a.a.b.a a2 = arra[i2];
            if (!this.c(a2)) continue;
            a2.C();
            a2.p();
        }
    }

    private void b(com.a.a.b.a[] arra, int n2, int n3) {
        for (int i2 = n2; i2 < n3; ++i2) {
            com.a.a.b.a a2 = arra[i2];
            if (!this.c(a2)) continue;
            a2.q();
            this.e(a2);
            a2.C();
        }
    }

    private void a(com.a.a.b.a[] arra) {
        HashMap<LongPair, com.a.a.b.b.f> hashMap = new HashMap<LongPair, com.a.a.b.b.f>();
        block0 : for (com.a.a.b.a a2 : arra) {
            if (a2.e() || !this.c(a2)) continue;
            for (com.a.a.b.a a3 : this.h.d(a2)) {
                if (!this.c(a2)) continue block0;
                if (!this.c(a3)) continue;
                this.a(a2, a3, hashMap);
            }
        }
    }

    private void c(com.a.a.b.a[] arra, int n2, int n3) {
        for (int i2 = n2; i2 < n3; ++i2) {
            com.a.a.b.a a2 = arra[i2];
            if (!this.c(a2)) continue;
            a2.c(0.0, 0.0);
            a2.n(0.0);
        }
    }

    private void a(com.a.a.b.a a2, com.a.a.b.a a3, Map<LongPair, com.a.a.b.b.f> map) {
        com.a.a.b.a a5;
        com.a.a.b.a a4;
        if (a2.a() > a3.a()) {
            a5 = a3;
            a4 = a2;
        } else {
            a5 = a2;
            a4 = a3;
        }
        LongPair longPair = new LongPair(a5.a(), a4.a());
        com.a.a.b.b.f f2 = map.get(longPair);
        if (f2 != null) {
            return;
        }
        for (b b2 : this.n) {
            if (!b2.c.a(a5, a4)) {
                map.put(longPair, b);
                return;
            }
            if (this.c(a5) && this.c(a4)) continue;
            return;
        }
        for (a a6 : this.l) {
            if (!a6.c.c(a5, a4)) continue;
            f2 = a6.c.d(a5, a4);
            break;
        }
        if (f2 == null) {
            map.put(longPair, b);
        } else {
            map.put(longPair, f2);
            this.a(f2);
        }
    }

    private void a(com.a.a.b.b.f f2) {
        com.a.a.b.a a2 = f2.a();
        com.a.a.b.a a3 = f2.b();
        if (a2.e() && a3.e()) {
            throw new IllegalArgumentException("Both " + a2 + " and " + a3 + " are static.");
        }
        for (b object2 : this.n) {
            if (!object2.c.a(f2)) {
                return;
            }
            if (this.c(a2) && this.c(a3)) continue;
            return;
        }
        f.b(f2);
        Vector3D vector3D = f.a(f2.d());
        Vector3D vector3D2 = f.a(a2.B(), f2.c());
        Vector3D vector3D3 = f.a(a3.B(), f2.c());
        Vector3D vector3D4 = f.a(a2.y()).crossProduct(vector3D2);
        Vector3D vector3D5 = f.a(a3.y()).crossProduct(vector3D3);
        Vector3D vector3D6 = f.a(a2.u()).add(vector3D4);
        Vector3D vector3D7 = f.a(a3.u()).add(vector3D5);
        Vector3D vector3D8 = vector3D6.subtract(vector3D7);
        double d2 = - vector3D8.dotProduct(vector3D);
        if (d2 > - this.f) {
            this.a(a2, a3, vector3D, vector3D2, vector3D3, vector3D8);
            this.b(a2, a3, vector3D, vector3D2, vector3D3, vector3D8);
        }
        if (f2.e() >= this.f) {
            this.a(a2, a3, f2);
        }
        a2.C();
        a3.C();
        for (b b2 : this.n) {
            b2.c.b(f2);
        }
    }

    private void a(com.a.a.b.a a2, com.a.a.b.a a3, Vector3D vector3D, Vector3D vector3D2, Vector3D vector3D3, Vector3D vector3D4) {
        Vector3D vector3D5;
        Vector3D vector3D6;
        Vector3D vector3D7;
        Vector3D vector3D8;
        Double d2;
        if (this.i == null || (d2 = this.i.a(a2, a3)) == null) {
            d2 = a2.m() * a3.m();
        }
        Vector3D vector3D9 = vector3D2.crossProduct(vector3D).scalarMultiply(a2.h()).crossProduct(vector3D2);
        Vector3D vector3D10 = vector3D3.crossProduct(vector3D).scalarMultiply(a3.h()).crossProduct(vector3D3);
        double d3 = a2.f() + a3.f() + vector3D.dotProduct(vector3D9.add(vector3D10));
        double d4 = -1.0 * (1.0 + d2) * vector3D4.dotProduct(vector3D) / d3;
        if (Math.abs(d4) < this.f) {
            return;
        }
        if (!a2.e()) {
            vector3D5 = vector3D.scalarMultiply(d4 * a2.f());
            vector3D8 = f.a(a2.u()).add(vector3D5);
            a2.b(vector3D8.getX(), vector3D8.getY());
            vector3D6 = vector3D2.crossProduct(vector3D.scalarMultiply(d4)).scalarMultiply(a2.h());
            vector3D7 = f.a(a2.y()).add(vector3D6);
            a2.l(vector3D7.getZ());
        }
        if (!a3.e()) {
            vector3D5 = vector3D.scalarMultiply(d4 * a3.f());
            vector3D8 = f.a(a3.u()).subtract(vector3D5);
            a3.b(vector3D8.getX(), vector3D8.getY());
            vector3D6 = vector3D3.crossProduct(vector3D.scalarMultiply(d4)).scalarMultiply(a3.h());
            vector3D7 = f.a(a3.y()).subtract(vector3D6);
            a3.l(vector3D7.getZ());
        }
    }

    private void b(com.a.a.b.a a2, com.a.a.b.a a3, Vector3D vector3D, Vector3D vector3D2, Vector3D vector3D3, Vector3D vector3D4) {
        Vector3D vector3D5;
        Vector3D vector3D6;
        Vector3D vector3D7;
        Vector3D vector3D8;
        Vector3D vector3D9 = vector3D4.subtract(vector3D.scalarMultiply(vector3D4.dotProduct(vector3D)));
        if (vector3D9.getNormSq() < this.g) {
            return;
        }
        vector3D9 = vector3D9.normalize();
        double d2 = Math.sqrt(a2.n() * a3.n()) * Math.SQRT_2 * Math.abs(vector3D4.dotProduct(vector3D)) / vector3D4.getNorm();
        if (d2 < this.f) {
            return;
        }
        Vector3D vector3D10 = vector3D2.crossProduct(vector3D9).scalarMultiply(a2.h()).crossProduct(vector3D2);
        Vector3D vector3D11 = vector3D3.crossProduct(vector3D9).scalarMultiply(a3.h()).crossProduct(vector3D3);
        double d3 = a2.f() + a3.f() + vector3D9.dotProduct(vector3D10.add(vector3D11));
        double d4 = -1.0 * d2 * vector3D4.dotProduct(vector3D9) / d3;
        if (Math.abs(d4) < this.f) {
            return;
        }
        if (!a2.e()) {
            vector3D5 = vector3D9.scalarMultiply(d4 * a2.f());
            vector3D7 = f.a(a2.u()).add(vector3D5);
            a2.b(vector3D7.getX(), vector3D7.getY());
            vector3D8 = vector3D2.crossProduct(vector3D9.scalarMultiply(d4)).scalarMultiply(a2.h());
            vector3D6 = f.a(a2.y()).add(vector3D8);
            a2.l(vector3D6.getZ());
        }
        if (!a3.e()) {
            vector3D5 = vector3D9.scalarMultiply(d4 * a3.f());
            vector3D7 = f.a(a3.u()).subtract(vector3D5);
            a3.b(vector3D7.getX(), vector3D7.getY());
            vector3D8 = vector3D3.crossProduct(vector3D9.scalarMultiply(d4)).scalarMultiply(a3.h());
            vector3D6 = f.a(a3.y()).subtract(vector3D8);
            a3.l(vector3D6.getZ());
        }
    }

    private void e(com.a.a.b.a a2) {
        this.f(a2);
        this.g(a2);
    }

    private void f(com.a.a.b.a a2) {
        if (a2.u().getSquaredLength() > 0.0) {
            a2.r().add(a2.u().copy().multiply(this.e));
        }
        if (a2.w().getSquaredLength() > 0.0) {
            a2.u().add(a2.w().copy().multiply(a2.f()).multiply(this.e));
        }
        if (a2.i() >= 1.0) {
            a2.a(a2.v().copy());
        } else if (a2.i() > 0.0) {
            a2.o(this.e);
            if (a2.u().nearlyEquals(a2.v(), this.f)) {
                a2.a(a2.v().copy());
            }
        }
        a2.u().subtract(a2.v());
        a2.e(this.e);
        a2.u().add(a2.v());
    }

    private void g(com.a.a.b.a a2) {
        a2.k(a2.x() + a2.y() * this.e);
        a2.l(a2.y() + a2.A() * a2.h() * this.e);
        if (a2.j() >= 1.0) {
            a2.l(a2.z());
        } else if (a2.j() > 0.0) {
            a2.p(this.e);
            if (NumberUtil.nearlyEquals(a2.y(), a2.z(), this.f)) {
                a2.l(a2.z());
            }
        }
        double d2 = a2.y() - a2.z();
        if (Math.abs(d2) > 0.0) {
            double d3 = a2.l() * this.e;
            if (d3 >= Math.abs(d2)) {
                a2.l(a2.z());
            } else if (d3 > 0.0) {
                if (d2 > 0.0) {
                    a2.l(d2 - d3 + a2.z());
                } else {
                    a2.l(d2 + d3 + a2.z());
                }
            }
        }
    }

    private void a(com.a.a.b.a a2, com.a.a.b.a a3, com.a.a.b.b.f f2) {
        if (a2.e()) {
            a3.r().subtract(f2.d().multiply(f2.e() + this.f));
        } else if (a3.e()) {
            a2.r().add(f2.d().multiply(f2.e() + this.f));
        } else {
            Vector2D vector2D = f2.d().multiply(0.5 * (f2.e() + this.f));
            a2.r().add(vector2D);
            a3.r().subtract(vector2D);
        }
    }

    public void a(d d2, String string, double d3) {
        c.a(string);
        if (this.k.containsKey(string)) {
            throw new IllegalArgumentException("Collider '" + string + "' is already registered.");
        }
        a a2 = new a(string, d3, d2, null);
        this.k.put(string, a2);
        this.l.add(a2);
    }

    public void a(d d2, String string) {
        this.a(d2, string, 0.0);
    }

    private void a(d d2) {
        this.a(d2, d2.getClass().getSimpleName());
    }

    public void a(com.a.a.b.d.a a2, String string, double d2) {
        c.a(string);
        if (this.m.containsKey(string)) {
            throw new IllegalArgumentException("Listener '" + string + "' is already registered.");
        }
        b b2 = new b(string, d2, a2, null);
        this.m.put(string, b2);
        this.n.add(b2);
    }

    public void a(com.a.a.b.d.a a2, String string) {
        this.a(a2, string, 0.0);
    }

    public boolean a(String string) {
        c.a(string);
        return this.m.containsKey(string);
    }

    private static void b(com.a.a.b.b.f f2) {
        if (f2.e() >= f2.a().c().d() * 0.25 || f2.e() >= f2.b().c().d() * 0.25) {
            if (a.isEnabledFor(Level.WARN)) {
                a.warn("Resolving collision (big depth) " + f2 + '.');
            }
        } else if (a.isDebugEnabled()) {
            a.debug("Resolving collision " + f2 + '.');
        }
    }

    private static Vector3D a(double d2) {
        return new Vector3D(0.0, 0.0, d2);
    }

    private static Vector3D a(Vector2D vector2D) {
        return new Vector3D(vector2D.getX(), vector2D.getY(), 0.0);
    }

    private static Vector3D a(Point2D point2D, Point2D point2D2) {
        return f.a(new Vector2D(point2D, point2D2));
    }

    static /* synthetic */ Logger e() {
        return a;
    }

    private static final class b
    extends c {
        private static final Comparator<b> d = (b2, b3) -> {
            int n2 = Double.compare(b3.b, b2.b);
            if (n2 != 0) {
                return n2;
            }
            return b2.a.compareTo(b3.a);
        };
        public final double b;
        public final com.a.a.b.d.a c;

        private b(String string, double d2, com.a.a.b.d.a a2) {
            super(string);
            this.b = d2;
            this.c = a2;
        }

        static /* synthetic */ Comparator a() {
            return d;
        }

        /* synthetic */ b(String string, double d2, com.a.a.b.d.a a2, g g2) {
            this(string, d2, a2);
        }
    }

    private static final class a
    extends c {
        private static final Comparator<a> d = (a2, a3) -> {
            int n2 = Double.compare(a3.b, a2.b);
            if (n2 != 0) {
                return n2;
            }
            return a2.a.compareTo(a3.a);
        };
        public final double b;
        public final d c;

        private a(String string, double d2, d d3) {
            super(string);
            this.b = d2;
            this.c = d3;
        }

        static /* synthetic */ Comparator a() {
            return d;
        }

        /* synthetic */ a(String string, double d2, d d3, g g2) {
            this(string, d2, d3);
        }
    }

}

