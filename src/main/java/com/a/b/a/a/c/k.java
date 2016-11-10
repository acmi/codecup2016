/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.c;

public enum k {
    WIZARD_CONDITION_CHANGE(60),
    BUILDING_ATTACK(30);
    
    private static final int DEFAULT_EFFECT_DURATION = 1;
    private final int duration;

    private k(int n3) {
        if (n3 < 1) {
            throw new IllegalArgumentException("Argument 'duration' is less than 1.");
        }
        if (n3 > 32767) {
            throw new IllegalArgumentException("Argument 'duration' is greater than 32767.");
        }
        this.duration = n3;
    }

    private k() {
        this(1);
    }

    public int getDuration() {
        return this.duration;
    }
}

