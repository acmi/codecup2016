/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.base;

import com.google.common.base.Absent;
import com.google.common.base.Present;
import java.io.Serializable;

public abstract class Optional<T>
implements Serializable {
    public static <T> Optional<T> absent() {
        return Absent.withType();
    }

    public static <T> Optional<T> fromNullable(T t2) {
        return t2 == null ? Optional.absent() : new Present<T>(t2);
    }

    Optional() {
    }

    public abstract boolean isPresent();

    public abstract T get();
}

