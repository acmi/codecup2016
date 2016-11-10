/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.c;

import com.a.b.a.a.c.D;
import com.a.b.a.a.c.E;
import java.util.Arrays;

public final class u {
    private final D[] wizards;
    private final E world;

    public u(D[] arrd, E e2) {
        this.wizards = Arrays.copyOf(arrd, arrd.length);
        this.world = e2;
    }

    public D[] getWizards() {
        return Arrays.copyOf(this.wizards, this.wizards.length);
    }

    public E getWorld() {
        return this.world;
    }
}

