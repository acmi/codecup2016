/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.c;

public final class g {
    private final boolean keyboardPlayer;

    public g(boolean bl) {
        this.keyboardPlayer = bl;
    }

    public boolean isKeyboardPlayer() {
        return this.keyboardPlayer;
    }

    public boolean equals(Object object) {
        return this == object || object instanceof g && this.keyboardPlayer == ((g)object).keyboardPlayer;
    }

    public int hashCode() {
        return this.keyboardPlayer ? 1 : 0;
    }
}

