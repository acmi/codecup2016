/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b;

import com.a.a.b.c;
import com.a.a.b.e;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.text.StringUtil;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class d {
    private a a;
    private double b;
    private Map<String, b> c;
    private SortedSet<b> d;

    public d() {
        this.a = new a(this, 0.0, 0.0, null);
    }

    public d(d d2) {
        this.a = new a(this, d2.a, null);
        this.b = d2.b;
    }

    public Point2D g() {
        return this.a;
    }

    public void a(Point2D point2D) {
        Point2D point2D2 = this.a.copy();
        Point2D point2D3 = point2D.copy();
        if (this.d != null) {
            for (b b2 : this.d) {
                if (b2.c.b(point2D2.copy(), point2D3)) continue;
                return;
            }
        }
        this.a = new a(this, point2D3, null);
        if (this.d != null) {
            for (b b2 : this.d) {
                b2.c.a(point2D2.copy(), point2D3.copy());
            }
        }
    }

    public double h() {
        return this.b;
    }

    public void d(double d2) {
        this.b = d2;
    }

    public void i() {
        while (this.b > 3.141592653589793) {
            this.b -= 6.283185307179586;
        }
        while (this.b < -3.141592653589793) {
            this.b += 6.283185307179586;
        }
    }

    public void a(com.a.a.b.d.c c2, String string, double d2) {
        c.a(string);
        if (this.c == null) {
            this.c = new HashMap<String, b>(1);
            this.d = new TreeSet<b>(d);
        } else if (this.c.containsKey(string)) {
            throw new IllegalArgumentException("Listener '" + string + "' is already registered.");
        }
        b b2 = new b(string, d2, c2, null);
        this.c.put(string, b2);
        this.d.add(b2);
    }

    public void a(com.a.a.b.d.c c2, String string) {
        this.a(c2, string, 0.0);
    }

    public String toString() {
        return StringUtil.toString((Object)this, false, new String[0]);
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
        public final com.a.a.b.d.c c;

        private b(String string, double d2, com.a.a.b.d.c c2) {
            super(string);
            this.b = d2;
            this.c = c2;
        }

        /* synthetic */ b(String string, double d2, com.a.a.b.d.c c2, e e2) {
            this(string, d2, c2);
        }
    }

    private final class a
    extends Point2D {
        final /* synthetic */ d a;

        private a(d d2, double d3, double d4) {
            this.a = d2;
            super(d3, d4);
        }

        private a(d d2, Point2D point2D) {
            this.a = d2;
            super(point2D);
        }

        @Override
        public void setX(double d2) {
            this.setFirst(d2);
        }

        @Override
        public void setY(double d2) {
            this.setSecond(d2);
        }

        @Override
        public Point2D add(Vector2D vector2D) {
            Point2D point2D = super.copy();
            Point2D point2D2 = super.copy().add(vector2D);
            return this.a(point2D, point2D2);
        }

        @Override
        public Point2D add(double d2, double d3) {
            Point2D point2D = super.copy();
            Point2D point2D2 = super.copy().add(d2, d3);
            return this.a(point2D, point2D2);
        }

        @Override
        public Point2D subtract(Vector2D vector2D) {
            Point2D point2D = super.copy();
            Point2D point2D2 = super.copy().subtract(vector2D);
            return this.a(point2D, point2D2);
        }

        @Override
        public Point2D subtract(double d2, double d3) {
            Point2D point2D = super.copy();
            Point2D point2D2 = super.copy().subtract(d2, d3);
            return this.a(point2D, point2D2);
        }

        @Override
        public void setFirst(double d2) {
            Point2D point2D = super.copy();
            Point2D point2D2 = super.copy();
            point2D2.setFirst(d2);
            this.a(point2D, point2D2);
        }

        @Override
        public void setSecond(double d2) {
            Point2D point2D = super.copy();
            Point2D point2D2 = super.copy();
            point2D2.setSecond(d2);
            this.a(point2D, point2D2);
        }

        private Point2D a(Point2D point2D, Point2D point2D2) {
            if (this.a.d != null) {
                for (b b2 : this.a.d) {
                    if (b2.c.b(point2D.copy(), point2D2)) continue;
                    return this;
                }
            }
            super.setFirst(point2D2.getFirst());
            super.setSecond(point2D2.getSecond());
            if (this.a.d != null) {
                for (b b2 : this.a.d) {
                    b2.c.a(point2D.copy(), point2D2.copy());
                }
            }
            return this;
        }

        /* synthetic */ a(d d2, double d3, double d4, e e2) {
            this(d2, d3, d4);
        }

        /* synthetic */ a(d d2, Point2D point2D, e e2) {
            this(d2, point2D);
        }
    }

}

