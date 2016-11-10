/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.e;

import com.a.a.a.a.c;
import com.a.b.a.a.b.d.b;
import com.a.b.a.a.b.d.e.a;
import com.a.b.a.a.b.e.t;
import com.a.b.a.a.c.B;
import com.a.b.a.a.c.l;
import com.a.b.a.a.c.z;
import com.a.b.d;
import com.a.b.g;
import com.a.c.a.b;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.math.NumberUtil;
import java.util.List;
import org.apache.commons.lang3.mutable.MutableDouble;

public final class u {
    static final Point2D[][] a = new Point2D[4][];

    public static B a(a a2, boolean bl) {
        com.a.c.c c2 = a2.b();
        com.a.c.a.c c3 = c2.t();
        if (!(c3 instanceof b)) {
            throw new IllegalArgumentException("Unsupported tree form: " + c3 + '.');
        }
        b b2 = (b)c3;
        return new B(a2.a(), bl ? 4000.0 - c2.c() : c2.c(), bl ? 4000.0 - c2.d() : c2.d(), 0.0, 0.0, bl ? c2.e() + 3.141592653589793 : c2.e(), a2.c(), b2.a(), u.b(a2) ? 0 : Math.max(NumberUtil.toInt(a2.j()), 1), NumberUtil.toInt(a2.k()), t.a(a2.l()));
    }

    public static double a() {
        return 20.0 + c.d() * 30.0;
    }

    public static Point2D a(int n2) {
        MutableDouble mutableDouble = new MutableDouble();
        MutableDouble mutableDouble2 = new MutableDouble();
        MutableDouble mutableDouble3 = new MutableDouble();
        MutableDouble mutableDouble4 = new MutableDouble();
        Point2D[] arrpoint2D = u.a(n2, mutableDouble, mutableDouble2, mutableDouble3, mutableDouble4);
        for (int i2 = 100; i2 > 0; --i2) {
            Point2D point2D = new Point2D(mutableDouble.doubleValue() + c.d() * (mutableDouble3.doubleValue() - mutableDouble.doubleValue()), mutableDouble2.doubleValue() + c.d() * (mutableDouble4.doubleValue() - mutableDouble2.doubleValue()));
            if (!com.a.a.b.f.a.a(point2D, arrpoint2D, 1.0E-7)) continue;
            return point2D;
        }
        return null;
    }

    private static Point2D[] a(int n2, MutableDouble mutableDouble, MutableDouble mutableDouble2, MutableDouble mutableDouble3, MutableDouble mutableDouble4) {
        double d2 = 2000.0;
        if (mutableDouble == null) {
            mutableDouble = new MutableDouble();
        }
        if (mutableDouble2 == null) {
            mutableDouble2 = new MutableDouble();
        }
        if (mutableDouble3 == null) {
            mutableDouble3 = new MutableDouble();
        }
        if (mutableDouble4 == null) {
            mutableDouble4 = new MutableDouble();
        }
        switch (n2) {
            case 0: {
                mutableDouble.setValue(800.0);
                mutableDouble2.setValue(400.0);
                mutableDouble3.setValue(3200.0);
                mutableDouble4.setValue(d2 - 400.0);
                return new Point2D[]{new Point2D(mutableDouble3.doubleValue(), mutableDouble2.doubleValue()), new Point2D(d2, mutableDouble4.doubleValue()), new Point2D(mutableDouble.doubleValue(), mutableDouble2.doubleValue())};
            }
            case 1: {
                mutableDouble.setValue(d2 + 400.0);
                mutableDouble2.setValue(800.0);
                mutableDouble3.setValue(3600.0);
                mutableDouble4.setValue(3200.0);
                return new Point2D[]{new Point2D(mutableDouble3.doubleValue(), mutableDouble4.doubleValue()), new Point2D(mutableDouble.doubleValue(), d2), new Point2D(mutableDouble3.doubleValue(), mutableDouble2.doubleValue())};
            }
            case 2: {
                mutableDouble.setValue(800.0);
                mutableDouble2.setValue(d2 + 400.0);
                mutableDouble3.setValue(3200.0);
                mutableDouble4.setValue(3600.0);
                return new Point2D[]{new Point2D(mutableDouble.doubleValue(), mutableDouble4.doubleValue()), new Point2D(d2, mutableDouble2.doubleValue()), new Point2D(mutableDouble3.doubleValue(), mutableDouble4.doubleValue())};
            }
            case 3: {
                mutableDouble.setValue(400.0);
                mutableDouble2.setValue(800.0);
                mutableDouble3.setValue(d2 - 400.0);
                mutableDouble4.setValue(3200.0);
                return new Point2D[]{new Point2D(mutableDouble.doubleValue(), mutableDouble2.doubleValue()), new Point2D(mutableDouble3.doubleValue(), d2), new Point2D(mutableDouble.doubleValue(), mutableDouble4.doubleValue())};
            }
        }
        throw new IllegalArgumentException("Unsupported zone index: " + n2 + '.');
    }

    public static int a(a a2) {
        Point2D point2D = new Point2D(a2.b().c(), a2.b().d());
        for (int i2 = 0; i2 < 4; ++i2) {
            if (!com.a.a.b.f.a.a(point2D, a[i2], 1.0E-7)) continue;
            return i2;
        }
        throw new IllegalArgumentException("Unexpected tree position: " + point2D + '.');
    }

    public static void a(g g2, a a2, double d2) {
        if (u.b(a2) || d2 < 1.0E-7) {
            return;
        }
        a2.b(a2.j() - d2);
        if (u.b(a2)) {
            g2.b(a2);
        }
    }

    public static boolean b(a a2) {
        return u.a(a2.j());
    }

    public static boolean a(double d2) {
        return d2 < 1.0E-7;
    }

    public static a[] a(d[] arrd) {
        int n2 = arrd.length;
        a[] arra = new a[n2];
        int n3 = 0;
        int n4 = n2;
        while (--n4 >= 0) {
            d d2 = arrd[n4];
            if (!(d2 instanceof a)) continue;
            arra[n3++] = (a)d2;
        }
        if (n3 == n2) {
            return arra;
        }
        a[] arra2 = new a[n3];
        System.arraycopy(arra, 0, arra2, 0, n3);
        return arra2;
    }

    static {
        for (int i2 = 0; i2 < 4; ++i2) {
            u.a[i2] = u.a(i2, null, null, null, null);
        }
    }
}

