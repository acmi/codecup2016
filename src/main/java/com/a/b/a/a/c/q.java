/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.c;

import com.a.b.a.a.c.l;
import com.a.b.a.a.c.o;
import com.a.b.a.a.c.r;
import com.a.b.a.a.c.z;
import com.codeforces.commons.reflection.Name;
import com.google.gson.annotations.Until;

public class q
extends o {
    @Until(value=1.0)
    private final r type;
    @Until(value=1.0)
    private final double visionRange;
    @Until(value=1.0)
    private final int damage;
    @Until(value=1.0)
    private final int cooldownTicks;
    private final int remainingActionCooldownTicks;

    public q(@Name(value="id") long l2, @Name(value="x") double d2, @Name(value="y") double d3, @Name(value="speedX") double d4, @Name(value="speedY") double d5, @Name(value="angle") double d6, @Name(value="faction") l l3, @Name(value="radius") double d7, @Name(value="life") int n2, @Name(value="maxLife") int n3, @Name(value="statuses") z[] arrz, @Name(value="type") r r2, @Name(value="visionRange") double d8, @Name(value="damage") int n4, @Name(value="cooldownTicks") int n5, @Name(value="remainingActionCooldownTicks") int n6) {
        super(l2, d2, d3, d4, d5, d6, l3, d7, n2, n3, arrz);
        this.type = r2;
        this.visionRange = d8;
        this.damage = n4;
        this.cooldownTicks = n5;
        this.remainingActionCooldownTicks = n6;
    }

    public r getType() {
        return this.type;
    }

    public double getVisionRange() {
        return this.visionRange;
    }

    public int getDamage() {
        return this.damage;
    }

    public int getCooldownTicks() {
        return this.cooldownTicks;
    }

    public int getRemainingActionCooldownTicks() {
        return this.remainingActionCooldownTicks;
    }

    public boolean equals(Object object) {
        return this == object || object instanceof q && q.areFieldEquals(this, (q)object);
    }

    public static boolean areFieldEquals(q q2, q q3) {
        return q2 == q3 || q2 != null && q3 != null && o.areFieldEquals(q2, q3) && q2.type == q3.type && Double.compare(q2.visionRange, q3.visionRange) == 0 && Double.compare(q2.damage, q3.damage) == 0 && q2.cooldownTicks == q3.cooldownTicks && q2.remainingActionCooldownTicks == q3.remainingActionCooldownTicks;
    }
}

