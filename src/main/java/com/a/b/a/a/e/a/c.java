/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.e.a;

import com.a.b.a.a.b.e.v;
import com.a.b.a.a.b.e.x;
import com.a.b.a.a.c.A;
import com.a.b.a.a.c.B;
import com.a.b.a.a.c.C;
import com.a.b.a.a.c.D;
import com.a.b.a.a.c.E;
import com.a.b.a.a.c.d;
import com.a.b.a.a.c.l;
import com.a.b.a.a.c.m;
import com.a.b.a.a.c.o;
import com.a.b.a.a.c.q;
import com.a.b.a.a.c.s;
import com.a.b.a.a.c.y;
import com.a.b.a.a.c.z;
import com.a.b.a.a.e.a.e;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;

public class c
implements e {
    private static final com.a.b.a.a.c.a[] a = new com.a.b.a.a.c.a[]{com.a.b.a.a.c.a.MAGIC_MISSILE, com.a.b.a.a.c.a.FROST_BOLT, com.a.b.a.a.c.a.FIREBALL};
    private static final y[][] b = new y[][]{{y.ADVANCED_MAGIC_MISSILE, y.FROST_BOLT, y.FIREBALL}, {y.HASTE, y.SHIELD}};
    private final int c;
    private final a d;
    private volatile Random e;
    private final y[] f = new y[x.a.size()];

    public c(int n2, a a2) {
        this.c = n2;
        this.d = a2;
    }

    @Override
    public int a() {
        return 1;
    }

    @Override
    public void a(m m2) {
        if (this.e == null) {
            int n2;
            this.e = new Random(m2.getRandomSeed());
            EnumSet<y> enumSet = EnumSet.copyOf(x.a);
            for (n2 = 0; n2 < b.length; ++n2) {
                this.f[n2] = b[n2][this.e.nextInt(b[n2].length)];
                enumSet.remove((Object)this.f[n2]);
            }
            for (n2 = c.b.length; n2 < this.f.length; ++n2) {
                y[] arry = enumSet.toArray((T[])new y[enumSet.size()]);
                this.f[n2] = arry[this.e.nextInt(arry.length)];
                enumSet.remove((Object)this.f[n2]);
            }
        }
    }

    @Override
    public s[] a(D[] arrd, E e2) {
        if (arrd.length != this.c) {
            throw new IllegalArgumentException(String.format("Strategy adapter '%s' got %d wizards while team size is %d.", this.getClass().getSimpleName(), arrd.length, this.c));
        }
        s[] arrs = new s[this.c];
        for (int i2 = 0; i2 < this.c; ++i2) {
            s s2 = new s();
            this.a(e2, arrd[i2], s2);
            arrs[i2] = s2;
        }
        return arrs;
    }

    @Override
    public void close() {
    }

    private void a(E e2, D d4, s s2) {
        D[] arrd;
        if (d4.getSkillsUnsafe().length < d4.getLevel()) {
            for (int i2 = 0; i2 < this.f.length; ++i2) {
                arrd = this.f[i2];
                if (x.a(d4, (y)arrd)) continue;
                s2.setSkillToLearn(x.b(d4, (y)arrd));
                break;
            }
        }
        if (this.d.a(0)) {
            s2.setSpeed(32767.0);
        } else if (this.d.b(0)) {
            s2.setSpeed(-32768.0);
        }
        if (this.d.c(0)) {
            s2.setStrafeSpeed(-32768.0);
        } else if (this.d.d(0)) {
            s2.setStrafeSpeed(32767.0);
        }
        if (this.d.e(0)) {
            s2.setTurn(-32768.0);
        } else if (this.d.f(0)) {
            s2.setTurn(32767.0);
        }
        if (d4.getRemainingActionCooldownTicks() > 0) {
            return;
        }
        int[] arrn = d4.getRemainingCooldownTicksByAction();
        if (this.d.g(0) && arrn[com.a.b.a.a.c.a.STAFF.ordinal()] == 0) {
            s2.setAction(com.a.b.a.a.c.a.STAFF);
            return;
        }
        if (this.d.h(0) && arrn[com.a.b.a.a.c.a.MAGIC_MISSILE.ordinal()] == 0) {
            s2.setAction(com.a.b.a.a.c.a.MAGIC_MISSILE);
        } else if (this.d.i(0) && arrn[com.a.b.a.a.c.a.FROST_BOLT.ordinal()] == 0) {
            s2.setAction(com.a.b.a.a.c.a.FROST_BOLT);
        } else if (this.d.j(0) && arrn[com.a.b.a.a.c.a.FIREBALL.ordinal()] == 0) {
            s2.setAction(com.a.b.a.a.c.a.FIREBALL);
        } else if (this.d.k(0) && arrn[com.a.b.a.a.c.a.HASTE.ordinal()] == 0) {
            s2.setAction(com.a.b.a.a.c.a.HASTE);
        } else if (this.d.l(0) && arrn[com.a.b.a.a.c.a.SHIELD.ordinal()] == 0) {
            s2.setAction(com.a.b.a.a.c.a.SHIELD);
        } else {
            return;
        }
        arrd = (D[])Arrays.stream(e2.getWizards()).filter(d3 -> d3.getFaction() == d4.getFaction()).toArray(n2 -> new D[n2]);
        List<C> list = v.a((C)d4, e2, 0.5235987755982988, x.a(d4, arrd));
        if (ArrayUtils.contains((Object[])a, (Object)s2.getAction())) {
            o o4 = list.stream().filter(c2 -> c2.getFaction() != d4.getFaction() && c2 instanceof o).map(c2 -> (o)c2).max((o2, o3) -> {
                double d3;
                double d4 = c.a(o2);
                if (d4 > (d3 = c.a(o3))) {
                    return 1;
                }
                if (d4 < d3) {
                    return -1;
                }
                return Double.compare(d4.getDistanceTo(o3), d4.getDistanceTo(o2));
            }
            ).orElse(null);
            if (o4 != null) {
                s2.setMaxCastDistance(d4.getDistanceTo(o4));
                s2.setMinCastDistance(s2.getMaxCastDistance() - o4.getRadius());
                s2.setCastAngle(d4.getAngleTo(o4));
            }
        } else if (s2.getAction() == com.a.b.a.a.c.a.HASTE || s2.getAction() == com.a.b.a.a.c.a.SHIELD) {
            A a2;
            A a3 = a2 = s2.getAction() == com.a.b.a.a.c.a.HASTE ? A.HASTENED : A.SHIELDED;
            if (!com.a.b.a.a.b.e.m.c(d4, a2)) {
                return;
            }
            D d5 = list.stream().filter(c2 -> c2.getFaction() == d4.getFaction() && c2 instanceof D).map(c2 -> (D)c2).min((d2, d3) -> {
                z z2 = com.a.b.a.a.b.e.m.a(d2, a2);
                z z3 = com.a.b.a.a.b.e.m.a(d3, a2);
                int n2 = z2 == null ? 0 : z2.getRemainingDurationTicks();
                int n3 = z3 == null ? 0 : z3.getRemainingDurationTicks();
                return Integer.compare(n2, n3);
            }
            ).orElse(null);
            if (d5 != null) {
                s2.setStatusTargetId(d5.getId());
            }
        }
    }

    private static double a(o o2) {
        if (o2 instanceof D) {
            return 4.0;
        }
        if (o2 instanceof q) {
            return 3.0;
        }
        if (o2 instanceof d) {
            return 2.0;
        }
        if (o2 instanceof B) {
            return 1.0;
        }
        throw new IllegalArgumentException("Unsupported living unit class: " + o2.getClass() + '.');
    }

    public static final class a {
        private final AtomicLong a = new AtomicLong();

        public long a() {
            return this.a.get();
        }

        public boolean a(int n2) {
            return this.a(n2, 0);
        }

        public void a(int n2, boolean bl) {
            this.a(n2, 0, bl);
        }

        public boolean b(int n2) {
            return this.a(n2, 1);
        }

        public void b(int n2, boolean bl) {
            this.a(n2, 1, bl);
        }

        public boolean c(int n2) {
            return this.a(n2, 2);
        }

        public void c(int n2, boolean bl) {
            this.a(n2, 2, bl);
        }

        public boolean d(int n2) {
            return this.a(n2, 3);
        }

        public void d(int n2, boolean bl) {
            this.a(n2, 3, bl);
        }

        public boolean e(int n2) {
            return this.a(n2, 4);
        }

        public void e(int n2, boolean bl) {
            this.a(n2, 4, bl);
        }

        public boolean f(int n2) {
            return this.a(n2, 5);
        }

        public void f(int n2, boolean bl) {
            this.a(n2, 5, bl);
        }

        public boolean g(int n2) {
            return this.a(n2, 6);
        }

        public void g(int n2, boolean bl) {
            this.a(n2, 6, bl);
        }

        public boolean h(int n2) {
            return this.a(n2, 7);
        }

        public void h(int n2, boolean bl) {
            this.a(n2, 7, bl);
        }

        public boolean i(int n2) {
            return this.a(n2, 8);
        }

        public void i(int n2, boolean bl) {
            this.a(n2, 8, bl);
        }

        public boolean j(int n2) {
            return this.a(n2, 9);
        }

        public void j(int n2, boolean bl) {
            this.a(n2, 9, bl);
        }

        public boolean k(int n2) {
            return this.a(n2, 10);
        }

        public void k(int n2, boolean bl) {
            this.a(n2, 10, bl);
        }

        public boolean l(int n2) {
            return this.a(n2, 11);
        }

        public void l(int n2, boolean bl) {
            this.a(n2, 11, bl);
        }

        private boolean a(int n2, int n3) {
            a.n(n2);
            return this.m(n3 + n2 * 12);
        }

        private void a(int n2, int n3, boolean bl) {
            a.n(n2);
            this.m(n3 + n2 * 12, bl);
        }

        private boolean m(int n2) {
            return (this.a() & 1 << n2) != 0;
        }

        private void m(int n2, boolean bl) {
            long l2;
            long l3;
            while (!this.a.compareAndSet(l2 = this.a(), l3 = bl ? l2 | 1 << n2 : l2 & (1 << n2 ^ -1))) {
            }
        }

        private static void n(int n2) {
            if (n2 < 0 || n2 > 5) {
                throw new IllegalArgumentException("Unsupported teammate index: " + n2 + '.');
            }
        }
    }

}

