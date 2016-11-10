/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.base;

import com.google.common.base.Optional;

final class Absent<T>
extends Optional<T> {
    static final Absent<Object> INSTANCE = new Absent<T>();

    static <T> Optional<T> withType() {
        return INSTANCE;
    }

    private Absent() {
    }

    @Override
    public boolean isPresent() {
        return false;
    }

    @Override
    public T get() {
        throw new IllegalStateException("Optional.get() cannot be called on an absent value");
    }

    public boolean equals(Object object) {
        return object == this;
    }

    public int hashCode() {
        return 2040732332;
    }

    public String toString() {
        return "Optional.absent()";
    }
}

