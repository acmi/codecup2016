/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.c;

import com.a.b.a.a.c.A;
import com.codeforces.commons.reflection.Name;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Until;

public class z {
    private final long id;
    @Until(value=1.0)
    private final A type;
    @Expose(serialize=0, deserialize=0)
    private final long wizardId;
    @Expose(serialize=0, deserialize=0)
    private final long playerId;
    private final int remainingDurationTicks;

    public z(@Name(value="id") long l2, @Name(value="type") A a2, @Name(value="wizardId") long l3, @Name(value="playerId") long l4, @Name(value="remainingDurationTicks") int n2) {
        this.id = l2;
        this.type = a2;
        this.wizardId = l3;
        this.playerId = l4;
        this.remainingDurationTicks = n2;
    }

    public long getId() {
        return this.id;
    }

    public A getType() {
        return this.type;
    }

    public long getWizardId() {
        return this.wizardId;
    }

    public long getPlayerId() {
        return this.playerId;
    }

    public int getRemainingDurationTicks() {
        return this.remainingDurationTicks;
    }

    public boolean equals(Object object) {
        return this == object || object instanceof z && z.areFieldEquals(this, (z)object);
    }

    public int hashCode() {
        int n2 = (int)(this.id ^ this.id >>> 32);
        n2 = 31 * n2 + (this.type == null ? 0 : this.type.hashCode());
        n2 = 31 * n2 + (int)(this.wizardId ^ this.wizardId >>> 32);
        n2 = 31 * n2 + (int)(this.playerId ^ this.playerId >>> 32);
        n2 = 31 * n2 + this.remainingDurationTicks;
        return n2;
    }

    public static boolean areFieldEquals(z z2, z z3) {
        return z2 == z3 || z2 != null && z3 != null && z2.id == z3.id && z2.type == z3.type && z2.wizardId == z3.wizardId && z2.playerId == z3.playerId && z2.remainingDurationTicks == z3.remainingDurationTicks;
    }
}

