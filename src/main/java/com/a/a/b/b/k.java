/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.b;

import com.a.a.b.b.e;
import com.a.a.b.b.f;
import com.a.a.b.b.l;
import com.a.a.b.c.c;
import com.codeforces.commons.geometry.Line2D;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.holder.Mutable;
import com.codeforces.commons.holder.SimpleMutable;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.pair.Pair;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.mutable.MutableDouble;

public class k
extends e {
    public k(double d2) {
        super(d2);
    }

    @Override
    protected boolean a(com.a.a.b.a a2, com.a.a.b.a a3) {
        return a2.c().e() == com.a.a.b.c.f.b && a3.c().e() == com.a.a.b.c.f.d;
    }

    @Override
    protected f b(com.a.a.b.a a2, com.a.a.b.a a3) {
        double d2;
        double d3;
        int n2;
        Object object;
        Point2D point2D;
        Object object2;
        com.a.a.b.c.e e2 = (com.a.a.b.c.e)a2.c();
        com.a.a.b.c.a a4 = (com.a.a.b.c.a)a3.c();
        double d4 = e2.d();
        double d5 = a4.a();
        double d6 = a2.r().getDistanceTo(a3.r());
        if (d6 > d4 + d5) {
            return null;
        }
        if (d6 < Math.abs(d4 - d5)) {
            return null;
        }
        Point2D[] arrpoint2D = e2.a(a2.r(), a2.x(), this.a);
        int n3 = arrpoint2D.length;
        double d7 = d5 * d5;
        double d8 = a3.x() + a4.b();
        double d9 = d8 + a4.c();
        Point2D point2D2 = a3.r().copy().add(new Vector2D(d5, 0.0).setAngle(d8));
        Point2D point2D3 = a3.r().copy().add(new Vector2D(d5, 0.0).setAngle(d9));
        ArrayList<a> arrayList = new ArrayList<a>();
        for (n2 = 0; n2 < n3; ++n2) {
            Point2D point2D4;
            object = arrpoint2D[n2];
            point2D = arrpoint2D[n2 == n3 - 1 ? 0 : n2 + 1];
            object2 = Line2D.getLineByTwoPoints((Point2D)object, point2D);
            if (object2.getSignedDistanceFrom(a2.r()) > - this.a) {
                throw new IllegalStateException(String.format("%s of %s is too small, does not represent a convex polygon, or its points are going in wrong order.", c.a(a2.c()), a2));
            }
            d3 = object2.getSignedDistanceFrom(a3.r());
            if (d3 > d5) continue;
            d2 = Math.min(object.getX(), point2D.getX());
            double d10 = Math.min(object.getY(), point2D.getY());
            double d11 = Math.max(object.getX(), point2D.getX());
            double d12 = Math.max(object.getY(), point2D.getY());
            Point2D point2D5 = object2.getProjectionOf(a3.r());
            double d13 = Math.sqrt(d7 - d3 * d3);
            Vector2D vector2D = new Vector2D((Point2D)object, point2D).copy().setLength(d13);
            Point2D point2D6 = point2D5.copy().add(vector2D);
            if (this.a(point2D6, d2, d10, d11, d12, a3, d8, d9)) {
                this.a(point2D6, (Point2D)object, point2D, (Line2D)object2, (List<a>)arrayList);
            }
            if (!this.a(point2D4 = point2D5.copy().add(vector2D.copy().negate()), d2, d10, d11, d12, a3, d8, d9)) continue;
            this.a(point2D4, (Point2D)object, point2D, (Line2D)object2, (List<a>)arrayList);
        }
        n2 = arrayList.size();
        if (n2 == 0) {
            return null;
        }
        if (!(n2 != 1 || !a4.f() || com.a.a.b.f.a.b(point2D2, arrpoint2D, this.a) && com.a.a.b.f.a.b(point2D3, arrpoint2D, this.a))) {
            object = (a)arrayList.get(0);
            int n4 = object.b.size();
            if (n4 == 1 || n4 == 2) {
                object2 = object.b.get(0);
                d3 = object2.getSignedDistanceFrom(point2D2);
                d2 = object2.getSignedDistanceFrom(point2D3);
                for (int i2 = 0; i2 < n3; ++i2) {
                    Point2D point2D7 = arrpoint2D[i2];
                    Point2D point2D8 = arrpoint2D[i2 == n3 - 1 ? 0 : i2 + 1];
                    Line2D line2D = Line2D.getLineByTwoPoints(point2D7, point2D8);
                    if (line2D.getSignedDistanceFrom(point2D2) >= this.a) {
                        return new f(a2, a3, point2D3, object2.getUnitNormal().negate(), - d2, this.a);
                    }
                    if (line2D.getSignedDistanceFrom(point2D3) < this.a) continue;
                    return new f(a2, a3, point2D2, object2.getUnitNormal().negate(), - d3, this.a);
                }
                if (d3 < d2) {
                    return new f(a2, a3, point2D2, object2.getUnitNormal().negate(), - d3, this.a);
                }
                return new f(a2, a3, point2D3, object2.getUnitNormal().negate(), - d2, this.a);
            }
            throw new IllegalStateException(String.format("%s of %s is too small, does not represent a convex polygon, or its points are going in wrong order.", c.a(a2.c()), a2));
        }
        object = new Vector2D(((a)arrayList.get((int)0)).a, a3.r());
        point2D = new Vector2D(((a)arrayList.get((int)0)).a, a2.r());
        if (d6 > d5 - this.a && object.dotProduct((Vector2D)((Object)point2D)) < 0.0) {
            object2 = new SimpleMutable<Point2D>();
            MutableDouble mutableDouble = new MutableDouble();
            for (a a5 : arrayList) {
                this.a(a3, a5.a, (Mutable<Point2D>)object2, mutableDouble);
                for (Pair<Point2D, Point2D> pair : a5.c) {
                    this.a(a3, pair.getFirst(), (Mutable<Point2D>)object2, mutableDouble);
                    this.a(a3, pair.getSecond(), (Mutable<Point2D>)object2, mutableDouble);
                }
            }
            return object2.get() == null ? null : new f(a2, a3, object2.get(), new Vector2D(a3.r(), object2.get()).normalize(), d5 - mutableDouble.doubleValue(), this.a);
        }
        object2 = new SimpleMutable();
        MutableDouble mutableDouble = new MutableDouble();
        for (a a6 : arrayList) {
            k.a(a3, a6.a, object2, mutableDouble, d8, d9);
            for (Pair<Point2D, Point2D> pair : a6.c) {
                k.a(a3, pair.getFirst(), object2, mutableDouble, d8, d9);
                k.a(a3, pair.getSecond(), object2, mutableDouble, d8, d9);
            }
        }
        return object2.get() == null ? null : new f(a2, a3, (Point2D)object2.get(), new Vector2D(object2.get(), a3.r()).normalize(), mutableDouble.doubleValue() - d5, this.a);
    }

    private void a(com.a.a.b.a a2, Point2D point2D, Mutable<Point2D> mutable, MutableDouble mutableDouble) {
        double d2 = a2.a(point2D);
        if (d2 >= this.a && (mutable.get() == null || d2 < mutableDouble.doubleValue())) {
            mutable.set(point2D);
            mutableDouble.setValue(d2);
        }
    }

    private static void a(com.a.a.b.a a2, Point2D point2D, Mutable<Point2D> mutable, MutableDouble mutableDouble, double d2, double d3) {
        double d4 = a2.a(point2D);
        if (com.a.a.b.f.a.a(new Vector2D(a2.r(), point2D).getAngle(), d2, d3) && (mutable.get() == null || d4 > mutableDouble.doubleValue())) {
            mutable.set(point2D);
            mutableDouble.setValue(d4);
        }
    }

    private boolean a(Point2D point2D, double d2, double d3, double d4, double d5, com.a.a.b.a a2, double d6, double d7) {
        boolean bl = point2D.getX() > d2 - this.a && point2D.getX() < d4 + this.a && point2D.getY() > d3 - this.a && point2D.getY() < d5 + this.a;
        double d8 = new Vector2D(a2.r(), point2D).getAngle();
        if (d8 < d6) {
            d8 += 6.283185307179586;
        }
        boolean bl2 = d8 >= d6 && d8 <= d7;
        return bl && bl2;
    }

    private void a(Point2D point2D, Point2D point2D2, Point2D point2D3, Line2D line2D, List<a> list) {
        boolean bl = false;
        for (a a2 : list) {
            if (!a2.a.nearlyEquals(point2D, this.a)) continue;
            a2.b.add(line2D);
            a2.c.add(new Pair<Point2D, Point2D>(point2D2, point2D3));
            bl = true;
            break;
        }
        if (!bl) {
            a a3 = new a(point2D, null);
            a3.b.add(line2D);
            a3.c.add(new Pair<Point2D, Point2D>(point2D2, point2D3));
            list.add(a3);
        }
    }

    private static final class a {
        public final Point2D a;
        public final List<Line2D> b = new ArrayList<Line2D>();
        public final List<Pair<Point2D, Point2D>> c = new ArrayList<Pair<Point2D, Point2D>>();

        private a(Point2D point2D) {
            this.a = point2D;
        }

        /* synthetic */ a(Point2D point2D, l l2) {
            this(point2D);
        }
    }

}

