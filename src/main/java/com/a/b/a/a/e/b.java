/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.e;

import com.a.b.a.a.c.C;
import com.a.b.a.a.c.D;
import com.a.b.a.a.c.E;
import com.a.b.a.a.c.d;
import com.a.b.a.a.c.l;
import com.a.b.a.a.c.m;
import com.a.b.a.a.c.n;
import com.a.b.a.a.c.o;
import com.a.b.a.a.c.q;
import com.a.b.a.a.c.s;
import com.a.b.a.a.e.c;
import com.a.b.a.a.e.e;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.lang3.ArrayUtils;

public class b
implements e {
    private final Map<n, a[]> a = new EnumMap<n, a[]>(n.class);
    private Random b;
    private n c;
    private a[] d;
    private D e;
    private E f;
    private m g;
    private s h;

    @Override
    public void a(D d2, E e2, m m2, s s2) {
        double d3;
        this.a(d2, m2);
        this.b(d2, e2, m2, s2);
        s2.setStrafeSpeed(this.b.nextBoolean() ? m2.getWizardStrafeSpeed() : - m2.getWizardStrafeSpeed());
        if ((double)d2.getLife() < (double)d2.getMaxLife() * 0.25) {
            this.a(this.b());
            return;
        }
        o o2 = this.c();
        if (o2 != null && (d3 = d2.getDistanceTo(o2)) <= d2.getCastRange()) {
            double d4 = d2.getAngleTo(o2);
            s2.setTurn(d4);
            if (StrictMath.abs(d4) < m2.getStaffSector() / 2.0) {
                s2.setAction(com.a.b.a.a.c.a.MAGIC_MISSILE);
                s2.setCastAngle(d4);
                s2.setMinCastDistance(d3 - o2.getRadius() + m2.getMagicMissileRadius());
            }
            return;
        }
        this.a(this.a());
    }

    private void a(D d2, m m2) {
        if (this.b == null) {
            this.b = new Random(m2.getRandomSeed());
            double d3 = m2.getMapSize();
            a[] arra = new a[4];
            arra[0] = new a(100.0, d3 - 100.0, null);
            arra[1] = this.b.nextBoolean() ? new a(600.0, d3 - 200.0, null) : new a(200.0, d3 - 600.0, null);
            arra[2] = new a(800.0, d3 - 800.0, null);
            arra[3] = new a(d3 - 600.0, 600.0, null);
            this.a.put(n.MIDDLE, arra);
            this.a.put(n.TOP, new a[]{new a(100.0, d3 - 100.0, null), new a(100.0, d3 - 400.0, null), new a(200.0, d3 - 800.0, null), new a(200.0, d3 * 0.75, null), new a(200.0, d3 * 0.5, null), new a(200.0, d3 * 0.25, null), new a(200.0, 200.0, null), new a(d3 * 0.25, 200.0, null), new a(d3 * 0.5, 200.0, null), new a(d3 * 0.75, 200.0, null), new a(d3 - 200.0, 200.0, null)});
            this.a.put(n.BOTTOM, new a[]{new a(100.0, d3 - 100.0, null), new a(400.0, d3 - 100.0, null), new a(800.0, d3 - 200.0, null), new a(d3 * 0.25, d3 - 200.0, null), new a(d3 * 0.5, d3 - 200.0, null), new a(d3 * 0.75, d3 - 200.0, null), new a(d3 - 200.0, d3 - 200.0, null), new a(d3 - 200.0, d3 * 0.75, null), new a(d3 - 200.0, d3 * 0.5, null), new a(d3 - 200.0, d3 * 0.25, null), new a(d3 - 200.0, 200.0, null)});
            switch ((int)d2.getId()) {
                case 1: 
                case 2: 
                case 6: 
                case 7: {
                    this.c = n.TOP;
                    break;
                }
                case 3: 
                case 8: {
                    this.c = n.MIDDLE;
                    break;
                }
                case 4: 
                case 5: 
                case 9: 
                case 10: {
                    this.c = n.BOTTOM;
                    break;
                }
            }
            this.d = this.a.get((Object)this.c);
            a a2 = this.d[this.d.length - 1];
            Preconditions.checkState(ArrayUtils.isSorted(this.d, (a3, a4) -> Double.compare(a4.a(a2), a3.a(a2))));
        }
    }

    private void b(D d2, E e2, m m2, s s2) {
        this.e = d2;
        this.f = e2;
        this.g = m2;
        this.h = s2;
    }

    private a a() {
        int n2 = this.d.length - 1;
        a a2 = this.d[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            a a3 = this.d[i2];
            if (a3.a(this.e) <= 100.0) {
                return this.d[i2 + 1];
            }
            if (a2.a(a3) >= a2.a(this.e)) continue;
            return a3;
        }
        return a2;
    }

    private a b() {
        a a2 = this.d[0];
        for (int i2 = this.d.length - 1; i2 > 0; --i2) {
            a a3 = this.d[i2];
            if (a3.a(this.e) <= 100.0) {
                return this.d[i2 - 1];
            }
            if (a2.a(a3) >= a2.a(this.e)) continue;
            return a3;
        }
        return a2;
    }

    private void a(a a2) {
        double d2 = this.e.getAngleTo(a2.a(), a2.b());
        this.h.setTurn(d2);
        if (StrictMath.abs(d2) < this.g.getStaffSector() / 4.0) {
            this.h.setSpeed(this.g.getWizardForwardSpeed());
        }
    }

    private o c() {
        ArrayList<o> arrayList = new ArrayList<o>();
        arrayList.addAll(Arrays.asList(this.f.getBuildings()));
        arrayList.addAll(Arrays.asList(this.f.getWizards()));
        arrayList.addAll(Arrays.asList(this.f.getMinions()));
        o o2 = null;
        double d2 = Double.MAX_VALUE;
        for (o o3 : arrayList) {
            double d3;
            if (o3.getFaction() == l.NEUTRAL || o3.getFaction() == this.e.getFaction() || (d3 = this.e.getDistanceTo(o3)) >= d2) continue;
            o2 = o3;
            d2 = d3;
        }
        return o2;
    }

    private static final class a {
        private final double a;
        private final double b;

        private a(double d2, double d3) {
            this.a = d2;
            this.b = d3;
        }

        public double a() {
            return this.a;
        }

        public double b() {
            return this.b;
        }

        public double a(double d2, double d3) {
            return StrictMath.hypot(this.a - d2, this.b - d3);
        }

        public double a(a a2) {
            return this.a(a2.a, a2.b);
        }

        public double a(C c2) {
            return this.a(c2.getX(), c2.getY());
        }

        /* synthetic */ a(double d2, double d3, c c2) {
            this(d2, d3);
        }
    }

}

