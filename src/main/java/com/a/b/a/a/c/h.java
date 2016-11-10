/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.c;

import com.a.b.a.a.c.a;
import com.codeforces.commons.math.NumberUtil;
import com.google.gson.annotations.Expose;

public final class h {
    @Expose(serialize=0, deserialize=0)
    private final double distanceTraveled;
    @Expose(serialize=0, deserialize=0)
    private final Integer remainingHitRecoverTicks;
    private final a lastAction;
    private final Integer lastActionTickIndex;

    public h(double d2, Integer n2, a a2, Integer n3) {
        this.distanceTraveled = d2;
        this.remainingHitRecoverTicks = n2;
        this.lastAction = a2;
        this.lastActionTickIndex = n3;
    }

    public double getDistanceTraveled() {
        return this.distanceTraveled;
    }

    public Integer getRemainingHitRecoverTicks() {
        return this.remainingHitRecoverTicks;
    }

    public a getLastAction() {
        return this.lastAction;
    }

    public Integer getLastActionTickIndex() {
        return this.lastActionTickIndex;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof h)) {
            return false;
        }
        h h2 = (h)object;
        return Double.compare(this.distanceTraveled, h2.distanceTraveled) == 0 && NumberUtil.equals(this.remainingHitRecoverTicks, h2.remainingHitRecoverTicks) && this.lastAction == h2.lastAction && NumberUtil.equals(this.lastActionTickIndex, h2.lastActionTickIndex);
    }

    public int hashCode() {
        long l2 = Double.doubleToLongBits(this.distanceTraveled);
        int n2 = (int)(l2 ^ l2 >>> 32);
        n2 = 31 * n2 + (this.remainingHitRecoverTicks == null ? 0 : this.remainingHitRecoverTicks.hashCode());
        n2 = 31 * n2 + this.lastAction.hashCode();
        n2 = 31 * n2 + (this.lastActionTickIndex == null ? 0 : this.lastActionTickIndex.hashCode());
        return n2;
    }
}

