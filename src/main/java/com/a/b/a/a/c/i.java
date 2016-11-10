/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.c;

import com.a.b.a.a.c.B;
import com.a.b.a.a.c.D;
import com.a.b.a.a.c.E;
import com.a.b.a.a.c.b;
import com.a.b.a.a.c.d;
import com.a.b.a.a.c.g;
import com.a.b.a.a.c.h;
import com.a.b.a.a.c.j;
import com.a.b.a.a.c.q;
import com.a.b.a.a.c.t;
import com.a.b.a.a.c.v;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Until;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class i
extends E {
    @Until(value=1.0)
    private final Long randomSeed;
    @Until(value=1.0)
    private final double speedFactor;
    private final Boolean lastTick;
    private final j[] effects;
    private final Map<Long, h> decoratedWizardById;
    private final Map<Long, g> decoratedPlayerById;
    @Expose(serialize=0, deserialize=0)
    private final Object systemData;

    public i(E e2, Long l2, double d2, boolean bl, j[] arrj, Map<Long, h> map, Map<Long, g> map2, Object object) {
        super(e2.getTickIndex(), e2.getTickCount(), e2.getWidth(), e2.getHeight(), e2.getPlayersUnsafe(), e2.getWizardsUnsafe(), e2.getMinionsUnsafe(), e2.getProjectilesUnsafe(), e2.getBonusesUnsafe(), e2.getBuildingsUnsafe(), e2.getTreesUnsafe());
        this.randomSeed = l2;
        this.speedFactor = d2;
        this.lastTick = bl ? Boolean.valueOf(true) : null;
        this.effects = Arrays.copyOf(arrj, arrj.length);
        this.decoratedWizardById = map == null ? null : new HashMap<Long, h>(map);
        this.decoratedPlayerById = map2 == null ? null : new HashMap<Long, g>(map2);
        this.systemData = object;
    }

    public Long getRandomSeed() {
        return this.randomSeed;
    }

    public double getSpeedFactor() {
        return this.speedFactor;
    }

    public boolean isLastTick() {
        return this.lastTick != null && this.lastTick != false;
    }

    public j[] getEffects() {
        return Arrays.copyOf(this.effects, this.effects.length);
    }

    public Map<Long, h> getDecoratedWizardById() {
        return this.decoratedWizardById == null ? null : Collections.unmodifiableMap(this.decoratedWizardById);
    }

    public Map<Long, g> getDecoratedPlayerById() {
        return this.decoratedPlayerById == null ? null : Collections.unmodifiableMap(this.decoratedPlayerById);
    }

    public Object getSystemData() {
        return this.systemData;
    }

    public j[] getEffectsUnsafe() {
        return this.effects;
    }
}

