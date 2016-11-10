/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.base;

import com.google.common.base.Optional;

final class Present<T>
extends Optional<T> {
    private final T reference;

    Present(T t2) {
        this.reference = t2;
    }

    @Override
    public boolean isPresent() {
        return true;
    }

    @Override
    public T get() {
        return this.reference;
    }

    public boolean equals(Object object) {
        if (object instanceof Present) {
            Present present = (Present)object;
            return this.reference.equals(present.reference);
        }
        return false;
    }

    public int hashCode() {
        return 1502476572 + this.reference.hashCode();
    }

    public String toString() {
        return "Optional.of(" + this.reference + ")";
    }
}

