/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.c;

import com.a.b.a.a.c.C;
import com.a.b.a.a.c.f;
import com.a.b.a.a.c.l;
import com.a.b.a.a.c.z;
import com.codeforces.commons.reflection.Name;
import java.util.Arrays;

public abstract class o
extends f {
    private final int life;
    private final int maxLife;
    private final z[] statuses;

    protected o(@Name(value="id") long l2, @Name(value="x") double d2, @Name(value="y") double d3, @Name(value="speedX") double d4, @Name(value="speedY") double d5, @Name(value="angle") double d6, @Name(value="faction") l l3, @Name(value="radius") double d7, @Name(value="life") int n2, @Name(value="maxLife") int n3, @Name(value="statuses") z[] arrz) {
        super(l2, d2, d3, d4, d5, d6, l3, d7);
        this.life = n2;
        this.maxLife = n3;
        this.statuses = Arrays.copyOf(arrz, arrz.length);
    }

    public int getLife() {
        return this.life;
    }

    public int getMaxLife() {
        return this.maxLife;
    }

    public z[] getStatuses() {
        return Arrays.copyOf(this.statuses, this.statuses.length);
    }

    protected static boolean areFieldEquals(o o2, o o3) {
        return C.areFieldEquals(o2, o3) && o2.life == o3.life && o2.maxLife == o3.maxLife && Arrays.equals(o2.statuses, o3.statuses);
    }
}

