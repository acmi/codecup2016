/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.a;

import com.a.a.b.a.d;
import com.a.a.b.a.e;
import com.a.a.b.b;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.math.NumberUtil;
import com.codeforces.commons.pair.IntPair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import org.apache.commons.lang3.ArrayUtils;

public class c
extends com.a.a.b.a.b {
    private final Set<com.a.a.b.a> a = new HashSet<com.a.a.b.a>();
    private final Map<Long, com.a.a.b.a> b = new HashMap<Long, com.a.a.b.a>();
    private final int[] c = new int[10000];
    private final int[] d = new int[10000];
    private final Point2D[] e = new Point2D[10000];
    private final Point2D[] f = new Point2D[10000];
    private final com.a.a.b.a[][][] g = new com.a.a.b.a[2001][2001][];
    private final Map<IntPair, com.a.a.b.a[]> h = new HashMap<IntPair, com.a.a.b.a[]>();
    private final Set<com.a.a.b.a> i = new HashSet<com.a.a.b.a>();
    private double j;
    private final double k;

    public c(double d2, double d3) {
        this.j = d2;
        this.k = d3;
    }

    @Override
    public void a(com.a.a.b.a a2) {
        c.e(a2);
        if (this.a.contains(a2)) {
            throw new IllegalStateException(a2 + " is already added.");
        }
        double d2 = a2.c().d();
        double d3 = 2.0 * d2;
        if (d3 > this.j && d3 <= this.k) {
            this.j = d3;
            this.b();
        }
        this.a.add(a2);
        this.b.put(a2.a(), a2);
        this.f(a2);
        a2.o().a(new d(this, d3, a2), this.getClass().getSimpleName() + "Listener");
    }

    @Override
    public void b(com.a.a.b.a a2) {
        if (a2 == null) {
            return;
        }
        if (this.b.remove(a2.a()) == null) {
            return;
        }
        this.a.remove(a2);
        this.g(a2);
    }

    @Override
    public boolean c(com.a.a.b.a a2) {
        c.e(a2);
        return this.a.contains(a2);
    }

    @Override
    public List<com.a.a.b.a> a() {
        return new a<com.a.a.b.a>(this.a, null);
    }

    @Override
    public List<com.a.a.b.a> d(com.a.a.b.a a2) {
        int n2;
        int n3;
        int n4;
        c.e(a2);
        if (!this.a.contains(a2)) {
            throw new IllegalStateException("Can't find " + a2 + '.');
        }
        ArrayList<com.a.a.b.a> arrayList = new ArrayList<com.a.a.b.a>();
        if (!this.i.isEmpty()) {
            for (com.a.a.b.a a3 : this.i) {
                c.a(a2, a3, arrayList);
            }
        }
        if (a2.a() >= 0 && a2.a() <= 9999) {
            n4 = (int)a2.a();
            n2 = this.c[n4];
            n3 = this.d[n4];
        } else {
            n2 = this.a(a2.s());
            n3 = this.b(a2.t());
        }
        for (n4 = -1; n4 <= 1; ++n4) {
            for (int i2 = -1; i2 <= 1; ++i2) {
                com.a.a.b.a[] arra = this.a(n2 + n4, n3 + i2);
                c.a(a2, arra, arrayList);
            }
        }
        return Collections.unmodifiableList(arrayList);
    }

    private static void a(com.a.a.b.a a2, com.a.a.b.a[] arra, List<com.a.a.b.a> list) {
        if (arra == null) {
            return;
        }
        int n2 = arra.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            c.a(a2, arra[i2], list);
        }
    }

    private static void a(com.a.a.b.a a2, com.a.a.b.a a3, List<com.a.a.b.a> list) {
        if (a3.b(a2)) {
            return;
        }
        if (a2.e() && a3.e()) {
            return;
        }
        if (Math.sqr(a3.c().d() + a2.c().d()) < a3.a(a2)) {
            return;
        }
        list.add(a3);
    }

    private void b() {
        for (int i2 = -1000; i2 <= 1000; ++i2) {
            for (int i3 = -1000; i3 <= 1000; ++i3) {
                this.g[i2 - -1000][i3 - -1000] = null;
            }
        }
        this.h.clear();
        this.i.clear();
        this.a.forEach(this::f);
    }

    private void f(com.a.a.b.a a2) {
        double d2 = a2.c().d();
        double d3 = 2.0 * d2;
        if (d3 > this.j) {
            if (!this.i.add(a2)) {
                throw new IllegalStateException("Can't add Body {id=" + a2.a() + "} to index.");
            }
        } else {
            this.a(a2, this.a(a2.s()), this.b(a2.t()));
        }
    }

    private void a(com.a.a.b.a a2, int n2, int n3) {
        com.a.a.b.a[] arra;
        if (n2 >= -1000 && n2 <= 1000 && n3 >= -1000 && n3 <= 1000) {
            arra = this.g[n2 - -1000][n3 - -1000];
            arra = c.a(arra, a2);
            this.g[n2 - -1000][n3 - -1000] = arra;
        } else {
            arra = new com.a.a.b.a[](n2, n3);
            com.a.a.b.a[] arra2 = this.h.get(arra);
            arra2 = c.a(arra2, a2);
            this.h.put((IntPair)arra, arra2);
        }
        if (a2.a() >= 0 && a2.a() <= 9999) {
            int n4 = (int)a2.a();
            this.c[n4] = n2;
            this.d[n4] = n3;
            this.e[n4] = new Point2D((double)n2 * this.j, (double)n3 * this.j);
            this.f[n4] = this.e[n4].copy().add(this.j, this.j);
        }
    }

    private void g(com.a.a.b.a a2) {
        double d2 = a2.c().d();
        double d3 = 2.0 * d2;
        if (d3 > this.j) {
            if (!this.i.remove(a2)) {
                throw new IllegalStateException("Can't remove Body {id=" + a2.a() + "} from index.");
            }
        } else {
            this.b(a2, this.a(a2.s()), this.b(a2.t()));
        }
    }

    private void b(com.a.a.b.a a2, int n2, int n3) {
        if (n2 >= -1000 && n2 <= 1000 && n3 >= -1000 && n3 <= 1000) {
            com.a.a.b.a[] arra = this.g[n2 - -1000][n3 - -1000];
            arra = c.b(arra, a2);
            this.g[n2 - -1000][n3 - -1000] = arra;
        } else {
            IntPair intPair = new IntPair(n2, n3);
            com.a.a.b.a[] arra = this.h.get(intPair);
            if ((arra = c.b(arra, a2)) == null) {
                this.h.remove(intPair);
            } else {
                this.h.put(intPair, arra);
            }
        }
    }

    private static com.a.a.b.a[] a(com.a.a.b.a[] arra, com.a.a.b.a a2) {
        if (arra == null) {
            return new com.a.a.b.a[]{a2};
        }
        int n2 = ArrayUtils.indexOf(arra, a2);
        if (n2 != -1) {
            throw new IllegalStateException("Can't add Body {id=" + a2.a() + "} to index.");
        }
        int n3 = arra.length;
        com.a.a.b.a[] arra2 = new com.a.a.b.a[n3 + 1];
        System.arraycopy(arra, 0, arra2, 0, n3);
        arra2[n3] = a2;
        return arra2;
    }

    private static com.a.a.b.a[] b(com.a.a.b.a[] arra, com.a.a.b.a a2) {
        int n2 = ArrayUtils.indexOf(arra, a2);
        if (n2 == -1) {
            throw new IllegalStateException("Can't remove Body {id=" + a2.a() + "} from index.");
        }
        int n3 = arra.length;
        if (n3 == 1) {
            return null;
        }
        com.a.a.b.a[] arra2 = new com.a.a.b.a[n3 - 1];
        System.arraycopy(arra, 0, arra2, 0, n2);
        System.arraycopy(arra, n2 + 1, arra2, n2, n3 - n2 - 1);
        return arra2;
    }

    private com.a.a.b.a[] a(int n2, int n3) {
        if (n2 >= -1000 && n2 <= 1000 && n3 >= -1000 && n3 <= 1000) {
            return this.g[n2 - -1000][n3 - -1000];
        }
        return this.h.get(new IntPair(n2, n3));
    }

    private int a(double d2) {
        return NumberUtil.toInt(Math.floor(d2 / this.j));
    }

    private int b(double d2) {
        return NumberUtil.toInt(Math.floor(d2 / this.j));
    }

    static /* synthetic */ double a(c c2) {
        return c2.j;
    }

    static /* synthetic */ Point2D[] b(c c2) {
        return c2.e;
    }

    static /* synthetic */ Point2D[] c(c c2) {
        return c2.f;
    }

    static /* synthetic */ int a(c c2, double d2) {
        return c2.a(d2);
    }

    static /* synthetic */ int b(c c2, double d2) {
        return c2.b(d2);
    }

    static /* synthetic */ void a(c c2, com.a.a.b.a a2, int n2, int n3) {
        c2.b(a2, n2, n3);
    }

    static /* synthetic */ void b(c c2, com.a.a.b.a a2, int n2, int n3) {
        c2.a(a2, n2, n3);
    }

    private static final class a<E>
    implements List<E> {
        private final Collection<E> a;

        private a(Collection<E> collection) {
            this.a = collection;
        }

        @Override
        public int size() {
            return this.a.size();
        }

        @Override
        public boolean isEmpty() {
            return this.a.isEmpty();
        }

        @Override
        public boolean contains(Object object) {
            return this.a.contains(object);
        }

        @Override
        public Iterator<E> iterator() {
            Iterator<E> iterator = this.a.iterator();
            return new e(this, iterator);
        }

        @Override
        public Object[] toArray() {
            return this.a.toArray();
        }

        @Override
        public <T> T[] toArray(T[] arrT) {
            return this.a.toArray(arrT);
        }

        @Override
        public boolean add(E e2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object object) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return this.a.containsAll(collection);
        }

        @Override
        public boolean addAll(Collection<? extends E> collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(int n2, Collection<? extends E> collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public E get(int n2) {
            if (this.a instanceof List) {
                return ((List)this.a).get(n2);
            }
            if (n2 < 0 || n2 >= this.a.size()) {
                throw new IndexOutOfBoundsException("Illegal index: " + n2 + ", size: " + this.a.size() + '.');
            }
            Iterator<E> iterator = this.a.iterator();
            for (int i2 = 0; i2 < n2; ++i2) {
                iterator.next();
            }
            return iterator.next();
        }

        @Override
        public E set(int n2, E e2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(int n2, E e2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public E remove(int n2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int indexOf(Object object) {
            Iterator<E> iterator = this.a.iterator();
            int n2 = 0;
            if (object == null) {
                while (iterator.hasNext()) {
                    if (iterator.next() == null) {
                        return n2;
                    }
                    ++n2;
                }
            } else {
                while (iterator.hasNext()) {
                    if (object.equals(iterator.next())) {
                        return n2;
                    }
                    ++n2;
                }
            }
            return -1;
        }

        @Override
        public int lastIndexOf(Object object) {
            if (this.a instanceof List) {
                return ((List)this.a).lastIndexOf(object);
            }
            Iterator<E> iterator = this.a.iterator();
            int n2 = 0;
            int n3 = -1;
            if (object == null) {
                while (iterator.hasNext()) {
                    if (iterator.next() == null) {
                        n3 = n2;
                    }
                    ++n2;
                }
            } else {
                while (iterator.hasNext()) {
                    if (object.equals(iterator.next())) {
                        n3 = n2;
                    }
                    ++n2;
                }
            }
            return n3;
        }

        @Override
        public ListIterator<E> listIterator() {
            return this.a instanceof List ? Collections.unmodifiableList((List)this.a).listIterator() : Collections.unmodifiableList(new ArrayList<E>(this.a)).listIterator();
        }

        @Override
        public ListIterator<E> listIterator(int n2) {
            return this.a instanceof List ? Collections.unmodifiableList((List)this.a).listIterator(n2) : Collections.unmodifiableList(new ArrayList<E>(this.a)).listIterator(n2);
        }

        @Override
        public List<E> subList(int n2, int n3) {
            return this.a instanceof List ? Collections.unmodifiableList(((List)this.a).subList(n2, n3)) : Collections.unmodifiableList(new ArrayList<E>(this.a)).subList(n2, n3);
        }

        /* synthetic */ a(Collection collection, d d2) {
            this(collection);
        }
    }

}

