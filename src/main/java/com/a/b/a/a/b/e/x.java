/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.e;

import com.a.b.a.a.b.d.b;
import com.a.b.a.a.b.e.m;
import com.a.b.a.a.b.e.n;
import com.a.b.a.a.b.e.t;
import com.a.b.a.a.b.e.v;
import com.a.b.a.a.b.f;
import com.a.b.a.a.c.A;
import com.a.b.a.a.c.C;
import com.a.b.a.a.c.D;
import com.a.b.a.a.c.a;
import com.a.b.a.a.c.l;
import com.a.b.a.a.c.p;
import com.a.b.a.a.c.y;
import com.a.b.a.a.c.z;
import com.a.b.d;
import com.a.b.g;
import com.a.c.a.b;
import com.a.c.a.c;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.math.NumberUtil;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class x {
    private static final Logger b = LoggerFactory.getLogger(x.class);
    private static final a[] c = a.values();
    private static final int d = c.length;
    public static final Set<y> a = Collections.unmodifiableSet(EnumSet.of(y.ADVANCED_MAGIC_MISSILE, y.FROST_BOLT, y.FIREBALL, y.HASTE, y.SHIELD));
    private static final a.a.d.a.a<a.a.d.a.f<Point2D>> e = new a.a.d.a.a();

    public static D a(com.a.b.a.a.b.d.f.a a2, int n2, f f2, List<com.a.b.a.a.b.d.f.a> list) {
        a.a.d.a.f<Point2D> f3;
        Point2D point2D;
        com.a.c.c c2 = a2.b();
        c c3 = c2.t();
        if (!(c3 instanceof b)) {
            throw new IllegalArgumentException("Unsupported wizard form: " + c3 + '.');
        }
        b b2 = (b)c3;
        Set<y> set = a2.x();
        a.a.d.a.f<Point2D> f4 = e.a_(n2);
        if (f4 == null) {
            f4 = v.a();
            e.a(n2, f4);
        }
        if ((point2D = f4.b(a2.a())) == null) {
            point2D = new Point2D(c2.c(), c2.d());
            f4.a(a2.a(), point2D);
        }
        Point2D point2D2 = (f3 = e.a_(n2 - 1)) == null ? null : f3.b(a2.a());
        Vector2D vector2D = point2D2 == null ? c2.f() : point2D.subtract(point2D2);
        boolean bl = f2 != null && f2.k() == l.RENEGADES;
        int[] arrn = new int[d];
        int n3 = d;
        while (--n3 >= 0) {
            a a3 = c[n3];
            arrn[a3.ordinal()] = a2.a(a3, n2);
        }
        f f5 = a2.s();
        return new D(a2.a(), bl ? 4000.0 - c2.c() : c2.c(), bl ? 4000.0 - c2.d() : c2.d(), bl ? - vector2D.getX() : vector2D.getX(), bl ? - vector2D.getY() : vector2D.getY(), bl ? c2.e() + 3.141592653589793 : c2.e(), a2.c(), b2.a(), NumberUtil.toInt(a2.j()), NumberUtil.toInt(a2.k()), t.a(a2.l()), f5.a(), f5.equals(f2), NumberUtil.toInt(a2.h()), NumberUtil.toInt(a2.i()), a2.q(), x.b(a2, list), a2.t(), a2.u(), set.toArray((T[])new y[set.size()]), a2.c(n2), arrn, f5.l(), f5.equals(f2) ? a2.A() : n.a);
    }

    public static Point2D a(int n2, int n3, int n4, int n5) {
        x.c(n2, n3, n4, n5);
        switch (n2) {
            case 0: {
                return new Point2D(100.0, 3700.0);
            }
            case 1: {
                return new Point2D(200.0, 3700.0);
            }
            case 2: {
                return new Point2D(200.0, 3800.0);
            }
            case 3: {
                return new Point2D(300.0, 3800.0);
            }
            case 4: {
                return new Point2D(300.0, 3900.0);
            }
            case 5: {
                return new Point2D(3900.0, 300.0);
            }
            case 6: {
                return new Point2D(3800.0, 300.0);
            }
            case 7: {
                return new Point2D(3800.0, 200.0);
            }
            case 8: {
                return new Point2D(3700.0, 200.0);
            }
            case 9: {
                return new Point2D(3700.0, 100.0);
            }
        }
        throw new IllegalArgumentException("Unsupported player index: " + n2 + '.');
    }

    public static double b(int n2, int n3, int n4, int n5) {
        x.c(n2, n3, n4, n5);
        switch (n2) {
            case 0: 
            case 1: {
                return -1.5707963267948966;
            }
            case 2: {
                return -0.7853981633974483;
            }
            case 3: 
            case 4: {
                return 0.0;
            }
            case 5: 
            case 6: {
                return 1.5707963267948966;
            }
            case 7: {
                return 2.356194490192345;
            }
            case 8: 
            case 9: {
                return 3.141592653589793;
            }
        }
        throw new IllegalArgumentException("Unsupported player index: " + n2 + '.');
    }

    public static void a(com.a.b.a.a.a.b b2, g g2, com.a.b.a.a.b.d.f.a a2, f f2, List<com.a.b.a.a.b.d.f.a> list, List<com.a.b.a.a.b.d.f.a> list2, double d2) {
        if (x.a(a2) || d2 < 1.0E-7) {
            return;
        }
        if (f2 != null && f2.k() == a2.c() && (d2 *= com.a.b.a.a.a.c.a(b2)) < 1.0E-7) {
            return;
        }
        d2 = Math.min(Math.floor(a2.j()), d2);
        a2.b(a2.j() - d2);
        v.a((com.a.b.a.a.b.d.b)a2, f2, list, d2, 0.25);
        if (!x.a(a2)) {
            a2.b(a2.o() + NumberUtil.toInt(d2 * 2.5 * 100.0 / a2.k()));
            return;
        }
        if (f2 == null) {
            b.info(a2 + " has been killed by non-player unit.");
        } else {
            b.info(a2 + " has been killed by " + f2 + '.');
        }
        v.a((com.a.b.a.a.b.d.b)a2, list2, f2, list, 1.0);
        a2.b(0.0);
        a2.a(0.0);
        a2.b(0);
        a2.m();
        g2.b(a2);
    }

    public static double a(com.a.b.a.a.b.d.f.a a2, double d2, Map<l, List<com.a.b.a.a.b.d.f.a>> map) {
        List<com.a.b.a.a.b.d.f.a> list = map.get((Object)a2.c());
        Preconditions.checkArgument(list.stream().allMatch(a3 -> a3.c() == a2.c()));
        if (a2.a(y.MAGICAL_DAMAGE_BONUS_PASSIVE_2)) {
            d2 += 2.0;
        } else if (a2.a(y.MAGICAL_DAMAGE_BONUS_PASSIVE_1)) {
            d2 += 1.0;
        }
        if (a2.a(y.MAGICAL_DAMAGE_BONUS_AURA_2, list)) {
            d2 += 2.0;
        } else if (a2.a(y.MAGICAL_DAMAGE_BONUS_AURA_1, list)) {
            d2 += 1.0;
        }
        return d2;
    }

    public static double b(com.a.b.a.a.b.d.f.a a2, double d2, Map<l, List<com.a.b.a.a.b.d.f.a>> map) {
        List<com.a.b.a.a.b.d.f.a> list = map.get((Object)a2.c());
        Preconditions.checkArgument(list.stream().allMatch(a3 -> a3.c() == a2.c()));
        if (a2.a(y.MAGICAL_DAMAGE_ABSORPTION_PASSIVE_2)) {
            d2 -= 2.0;
        } else if (a2.a(y.MAGICAL_DAMAGE_ABSORPTION_PASSIVE_1)) {
            d2 -= 1.0;
        }
        if (a2.a(y.MAGICAL_DAMAGE_ABSORPTION_AURA_2, list)) {
            d2 -= 2.0;
        } else if (a2.a(y.MAGICAL_DAMAGE_ABSORPTION_AURA_1, list)) {
            d2 -= 1.0;
        }
        return Math.max(d2, 0.0);
    }

    public static boolean a(com.a.b.a.a.b.d.f.a a2) {
        return x.a(a2.j());
    }

    public static boolean a(D d2) {
        return x.a(d2.getLife());
    }

    public static boolean a(double d2) {
        return d2 < 1.0;
    }

    public static boolean a(double d2, double d3) {
        return v.a(d2, d3, 35.0);
    }

    public static List<d> a(com.a.b.a.a.b.d.f.a a2, g g2) {
        return v.a((d)a2, g2, 0.5235987755982988, 70.0);
    }

    public static double a(com.a.b.a.a.b.d.f.a a2, List<com.a.b.a.a.b.d.f.a> list) {
        Preconditions.checkArgument(list.stream().allMatch(a3 -> a3.c() == a2.c()));
        double d2 = 1.0;
        if (a2.b(A.HASTENED)) {
            d2 += 0.3;
        }
        if (a2.a(y.MOVEMENT_BONUS_FACTOR_PASSIVE_2)) {
            d2 += 0.1;
        } else if (a2.a(y.MOVEMENT_BONUS_FACTOR_PASSIVE_1)) {
            d2 += 0.05;
        }
        if (a2.a(y.MOVEMENT_BONUS_FACTOR_AURA_2, list)) {
            d2 += 0.1;
        } else if (a2.a(y.MOVEMENT_BONUS_FACTOR_AURA_1, list)) {
            d2 += 0.05;
        }
        return d2;
    }

    public static double b(com.a.b.a.a.b.d.f.a a2, List<com.a.b.a.a.b.d.f.a> list) {
        Preconditions.checkArgument(list.stream().allMatch(a3 -> a3.c() == a2.c()));
        double d2 = 500.0;
        if (a2.a(y.RANGE_BONUS_PASSIVE_2)) {
            d2 += 50.0;
        } else if (a2.a(y.RANGE_BONUS_PASSIVE_1)) {
            d2 += 25.0;
        }
        if (a2.a(y.RANGE_BONUS_AURA_2, list)) {
            d2 += 50.0;
        } else if (a2.a(y.RANGE_BONUS_AURA_1, list)) {
            d2 += 25.0;
        }
        return d2;
    }

    public static double a(D d2, D[] arrd) {
        Preconditions.checkArgument(Arrays.stream(arrd).allMatch(d3 -> d3.getFaction() == d2.getFaction()));
        double d4 = 500.0;
        if (ArrayUtils.contains((Object[])d2.getSkillsUnsafe(), (Object)y.RANGE_BONUS_PASSIVE_2)) {
            d4 += 50.0;
        } else if (ArrayUtils.contains((Object[])d2.getSkillsUnsafe(), (Object)y.RANGE_BONUS_PASSIVE_1)) {
            d4 += 25.0;
        }
        if (x.a(d2, y.RANGE_BONUS_AURA_2, arrd)) {
            d4 += 50.0;
        } else if (x.a(d2, y.RANGE_BONUS_AURA_1, arrd)) {
            d4 += 25.0;
        }
        return d4;
    }

    private static boolean a(D d2, y y2, D[] arrd) {
        if (ArrayUtils.contains((Object[])d2.getSkillsUnsafe(), (Object)y2)) {
            return true;
        }
        int n2 = arrd.length;
        while (--n2 >= 0) {
            D d3 = arrd[n2];
            if (x.a(d3) || !ArrayUtils.contains((Object[])d3.getSkillsUnsafe(), (Object)y2) || d2.getDistanceTo(d3) > 500.0) continue;
            return true;
        }
        return false;
    }

    public static double a(com.a.b.a.a.b.d.f.a a2, com.a.b.a.a.b.d.b b2, List<com.a.b.a.a.b.d.f.a> list) {
        Preconditions.checkArgument(list.stream().allMatch(a3 -> a3.c() == a2.c()));
        double d2 = 12.0;
        if (a2.a(y.STAFF_DAMAGE_BONUS_PASSIVE_2)) {
            d2 += 6.0;
        } else if (a2.a(y.STAFF_DAMAGE_BONUS_PASSIVE_1)) {
            d2 += 3.0;
        }
        if (a2.a(y.STAFF_DAMAGE_BONUS_AURA_2, list)) {
            d2 += 6.0;
        } else if (a2.a(y.STAFF_DAMAGE_BONUS_AURA_1, list)) {
            d2 += 3.0;
        }
        if (a2.b(A.EMPOWERED)) {
            d2 *= 2.0;
        }
        if (b2.b(A.SHIELDED)) {
            d2 *= 0.75;
        }
        return d2;
    }

    public static boolean a(com.a.b.a.a.b.d.f.a a2, g g2, boolean bl) {
        return m.a(a2, g2, bl);
    }

    private static void c(int n2, int n3, int n4, int n5) {
        Preconditions.checkArgument(n3 == 10, String.format("Argument 'playerCount' should be either 10 or 2 (automatically expanded to 10), but got %d.", n3));
        Preconditions.checkArgument(n2 >= 0 && n2 < n3, String.format("Argument 'playerIndex' should be non-negative and less than 'playerCount', but got %d.", n2));
        Preconditions.checkArgument(n5 == 1, String.format("Argument 'wizardCount' should be equal to 1, but got %d.", n5));
        Preconditions.checkArgument(n4 >= 0 && n4 < n5, String.format("Argument 'wizardIndex' should be non-negative and less than 'wizardCount', but got %d.", n4));
    }

    public static boolean a(D d2, y y2) {
        y[] arry = d2.getSkillsUnsafe();
        int n2 = arry.length;
        while (--n2 >= 0) {
            if (arry[n2] != y2) continue;
            return true;
        }
        return false;
    }

    public static y b(D d2, y y2) {
        y y3;
        y y4 = y2;
        while ((y3 = x.a(y4)) != null && !x.a(d2, y3)) {
            y4 = y3;
        }
        return y4;
    }

    public static y a(y y2) {
        switch (y2) {
            case RANGE_BONUS_PASSIVE_1: {
                return null;
            }
            case RANGE_BONUS_AURA_1: {
                return y.RANGE_BONUS_PASSIVE_1;
            }
            case RANGE_BONUS_PASSIVE_2: {
                return y.RANGE_BONUS_AURA_1;
            }
            case RANGE_BONUS_AURA_2: {
                return y.RANGE_BONUS_PASSIVE_2;
            }
            case ADVANCED_MAGIC_MISSILE: {
                return y.RANGE_BONUS_AURA_2;
            }
            case MAGICAL_DAMAGE_BONUS_PASSIVE_1: {
                return null;
            }
            case MAGICAL_DAMAGE_BONUS_AURA_1: {
                return y.MAGICAL_DAMAGE_BONUS_PASSIVE_1;
            }
            case MAGICAL_DAMAGE_BONUS_PASSIVE_2: {
                return y.MAGICAL_DAMAGE_BONUS_AURA_1;
            }
            case MAGICAL_DAMAGE_BONUS_AURA_2: {
                return y.MAGICAL_DAMAGE_BONUS_PASSIVE_2;
            }
            case FROST_BOLT: {
                return y.MAGICAL_DAMAGE_BONUS_AURA_2;
            }
            case STAFF_DAMAGE_BONUS_PASSIVE_1: {
                return null;
            }
            case STAFF_DAMAGE_BONUS_AURA_1: {
                return y.STAFF_DAMAGE_BONUS_PASSIVE_1;
            }
            case STAFF_DAMAGE_BONUS_PASSIVE_2: {
                return y.STAFF_DAMAGE_BONUS_AURA_1;
            }
            case STAFF_DAMAGE_BONUS_AURA_2: {
                return y.STAFF_DAMAGE_BONUS_PASSIVE_2;
            }
            case FIREBALL: {
                return y.STAFF_DAMAGE_BONUS_AURA_2;
            }
            case MOVEMENT_BONUS_FACTOR_PASSIVE_1: {
                return null;
            }
            case MOVEMENT_BONUS_FACTOR_AURA_1: {
                return y.MOVEMENT_BONUS_FACTOR_PASSIVE_1;
            }
            case MOVEMENT_BONUS_FACTOR_PASSIVE_2: {
                return y.MOVEMENT_BONUS_FACTOR_AURA_1;
            }
            case MOVEMENT_BONUS_FACTOR_AURA_2: {
                return y.MOVEMENT_BONUS_FACTOR_PASSIVE_2;
            }
            case HASTE: {
                return y.MOVEMENT_BONUS_FACTOR_AURA_2;
            }
            case MAGICAL_DAMAGE_ABSORPTION_PASSIVE_1: {
                return null;
            }
            case MAGICAL_DAMAGE_ABSORPTION_AURA_1: {
                return y.MAGICAL_DAMAGE_ABSORPTION_PASSIVE_1;
            }
            case MAGICAL_DAMAGE_ABSORPTION_PASSIVE_2: {
                return y.MAGICAL_DAMAGE_ABSORPTION_AURA_1;
            }
            case MAGICAL_DAMAGE_ABSORPTION_AURA_2: {
                return y.MAGICAL_DAMAGE_ABSORPTION_PASSIVE_2;
            }
            case SHIELD: {
                return y.MAGICAL_DAMAGE_ABSORPTION_AURA_2;
            }
        }
        throw new IllegalArgumentException("Unsupported skill type: " + (Object)((Object)y2) + '.');
    }

    private x() {
        throw new UnsupportedOperationException();
    }
}

