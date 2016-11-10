/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.c;

import com.a.b.a.a.c.B;
import com.a.b.a.a.c.D;
import com.a.b.a.a.c.b;
import com.a.b.a.a.c.d;
import com.a.b.a.a.c.q;
import com.a.b.a.a.c.t;
import com.a.b.a.a.c.v;
import com.google.gson.annotations.Until;
import java.util.Arrays;

public class E {
    private final int tickIndex;
    @Until(value=1.0)
    private final int tickCount;
    @Until(value=1.0)
    private final double width;
    @Until(value=1.0)
    private final double height;
    private final t[] players;
    private final D[] wizards;
    private final q[] minions;
    private final v[] projectiles;
    private final b[] bonuses;
    private final d[] buildings;
    private final B[] trees;

    public E(int n2, int n3, double d2, double d3, t[] arrt, D[] arrd, q[] arrq, v[] arrv, b[] arrb, d[] arrd2, B[] arrb2) {
        this.tickIndex = n2;
        this.tickCount = n3;
        this.width = d2;
        this.height = d3;
        this.players = Arrays.copyOf(arrt, arrt.length);
        this.wizards = Arrays.copyOf(arrd, arrd.length);
        this.minions = Arrays.copyOf(arrq, arrq.length);
        this.projectiles = Arrays.copyOf(arrv, arrv.length);
        this.bonuses = Arrays.copyOf(arrb, arrb.length);
        this.buildings = Arrays.copyOf(arrd2, arrd2.length);
        this.trees = Arrays.copyOf(arrb2, arrb2.length);
    }

    public int getTickIndex() {
        return this.tickIndex;
    }

    public int getTickCount() {
        return this.tickCount;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public t[] getPlayers() {
        return Arrays.copyOf(this.players, this.players.length);
    }

    public D[] getWizards() {
        return Arrays.copyOf(this.wizards, this.wizards.length);
    }

    public q[] getMinions() {
        return Arrays.copyOf(this.minions, this.minions.length);
    }

    public v[] getProjectiles() {
        return Arrays.copyOf(this.projectiles, this.projectiles.length);
    }

    public b[] getBonuses() {
        return Arrays.copyOf(this.bonuses, this.bonuses.length);
    }

    public d[] getBuildings() {
        return Arrays.copyOf(this.buildings, this.buildings.length);
    }

    public B[] getTrees() {
        return Arrays.copyOf(this.trees, this.trees.length);
    }

    public t getMyPlayer() {
        for (int i2 = this.players.length - 1; i2 >= 0; --i2) {
            t t2 = this.players[i2];
            if (!t2.isMe()) continue;
            return t2;
        }
        return null;
    }

    public t[] getPlayersUnsafe() {
        return this.players;
    }

    public D[] getWizardsUnsafe() {
        return this.wizards;
    }

    public q[] getMinionsUnsafe() {
        return this.minions;
    }

    public v[] getProjectilesUnsafe() {
        return this.projectiles;
    }

    public b[] getBonusesUnsafe() {
        return this.bonuses;
    }

    public d[] getBuildingsUnsafe() {
        return this.buildings;
    }

    public B[] getTreesUnsafe() {
        return this.trees;
    }
}

