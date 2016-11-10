/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.e;

import com.a.b.a.a.b.e.x;
import com.a.b.a.a.c.A;
import com.a.b.a.a.c.D;
import com.a.b.a.a.c.E;
import com.a.b.a.a.c.a;
import com.a.b.a.a.c.m;
import com.a.b.a.a.c.s;
import com.a.b.a.a.c.y;
import com.a.b.a.a.e.b;
import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

public class d
extends b {
    private static final y[][] a = new y[][]{{y.ADVANCED_MAGIC_MISSILE, y.FROST_BOLT, y.FIREBALL}, {y.HASTE, y.SHIELD}};
    private volatile Random b;
    private final y[] c = new y[3];
    private volatile double d = Double.NaN;
    private volatile double e = Double.NaN;

    @Override
    public void a(D d2, E e2, m m2, s s2) {
        y[] arry;
        int n2;
        super.a(d2, e2, m2, s2);
        if (this.b == null) {
            this.b = new Random(m2.getRandomSeed());
            this.c[0] = a[0][this.b.nextInt(a[0].length)];
            this.c[1] = a[1][this.b.nextInt(a[1].length)];
            EnumSet<y> enumSet = EnumSet.copyOf(x.a);
            enumSet.remove((Object)this.c[0]);
            enumSet.remove((Object)this.c[1]);
            arry = enumSet.toArray((T[])new y[enumSet.size()]);
            this.c[2] = arry[this.b.nextInt(arry.length)];
        }
        for (n2 = 0; n2 < this.c.length; ++n2) {
            arry = this.c[n2];
            if (x.a(d2, (y)arry)) continue;
            s2.setSkillToLearn(x.b(d2, (y)arry));
            break;
        }
        s2.setSpeed(s2.getSpeed() * 2.0);
        s2.setStrafeSpeed(s2.getStrafeSpeed() * 2.0);
        if (d2.getX() == this.d && d2.getY() == this.e && this.b.nextBoolean()) {
            s2.setSpeed(-32768.0);
        }
        this.d = d2.getX();
        this.e = d2.getY();
        n2 = 0;
        if (x.a(d2, y.HASTE) && !com.a.b.a.a.b.e.m.c(d2, A.HASTENED) && (double)d2.getMana() >= 0.75 * (double)d2.getMaxMana()) {
            n2 = StrictMath.max(n2, m2.getHasteManacost());
        }
        if (x.a(d2, y.SHIELD) && !com.a.b.a.a.b.e.m.c(d2, A.SHIELDED) && (double)d2.getLife() < 0.75 * (double)d2.getMaxLife()) {
            n2 = StrictMath.max(n2, m2.getShieldManacost());
        }
        if (s2.getAction() == a.MAGIC_MISSILE) {
            if (x.a(d2, y.FIREBALL) && d2.getRemainingCooldownTicksByAction()[a.FIREBALL.ordinal()] == 0 && d2.getMana() >= m2.getFireballManacost() + n2) {
                s2.setAction(a.FIREBALL);
                s2.setMinCastDistance(s2.getMinCastDistance() - m2.getMagicMissileRadius() + m2.getFireballRadius());
                return;
            }
            if (x.a(d2, y.FROST_BOLT) && d2.getRemainingCooldownTicksByAction()[a.FROST_BOLT.ordinal()] == 0 && d2.getMana() >= m2.getFrostBoltManacost() + n2) {
                s2.setAction(a.FROST_BOLT);
                s2.setMinCastDistance(s2.getMinCastDistance() - m2.getMagicMissileRadius() + m2.getFrostBoltRadius());
                return;
            }
        }
        if (s2.getAction() == a.NONE || s2.getAction() == a.MAGIC_MISSILE && d2.getRemainingCooldownTicksByAction()[a.MAGIC_MISSILE.ordinal()] > 0) {
            if (x.a(d2, y.HASTE) && d2.getRemainingCooldownTicksByAction()[a.HASTE.ordinal()] == 0 && d2.getMana() >= m2.getHasteManacost() && !com.a.b.a.a.b.e.m.c(d2, A.HASTENED) && (double)d2.getMana() >= 0.75 * (double)d2.getMaxMana()) {
                s2.setAction(a.HASTE);
                return;
            }
            if (x.a(d2, y.SHIELD) && d2.getRemainingCooldownTicksByAction()[a.SHIELD.ordinal()] == 0 && d2.getMana() >= m2.getShieldManacost() && !com.a.b.a.a.b.e.m.c(d2, A.SHIELDED) && (double)d2.getLife() < 0.75 * (double)d2.getMaxLife()) {
                s2.setAction(a.SHIELD);
                return;
            }
        }
    }
}

