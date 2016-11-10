/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.inject.Key;
import com.google.inject.internal.MoreTypes;
import com.google.inject.spi.InjectionPoint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class Dependency<T> {
    private final InjectionPoint injectionPoint;
    private final Key<T> key;
    private final boolean nullable;
    private final int parameterIndex;

    Dependency(InjectionPoint injectionPoint, Key<T> key, boolean bl, int n2) {
        this.injectionPoint = injectionPoint;
        this.key = Preconditions.checkNotNull(key, "key");
        this.nullable = bl;
        this.parameterIndex = n2;
    }

    public static <T> Dependency<T> get(Key<T> key) {
        return new Dependency<T>(null, MoreTypes.canonicalizeKey(key), true, -1);
    }

    public static Set<Dependency<?>> forInjectionPoints(Set<InjectionPoint> set) {
        ArrayList arrayList = Lists.newArrayList();
        for (InjectionPoint injectionPoint : set) {
            arrayList.addAll(injectionPoint.getDependencies());
        }
        return ImmutableSet.copyOf(arrayList);
    }

    public Key<T> getKey() {
        return this.key;
    }

    public boolean isNullable() {
        return this.nullable;
    }

    public InjectionPoint getInjectionPoint() {
        return this.injectionPoint;
    }

    public int getParameterIndex() {
        return this.parameterIndex;
    }

    public int hashCode() {
        return Objects.hashCode(this.injectionPoint, this.parameterIndex, this.key);
    }

    public boolean equals(Object object) {
        if (object instanceof Dependency) {
            Dependency dependency = (Dependency)object;
            return Objects.equal(this.injectionPoint, dependency.injectionPoint) && Objects.equal(this.parameterIndex, dependency.parameterIndex) && Objects.equal(this.key, dependency.key);
        }
        return false;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.key);
        if (this.injectionPoint != null) {
            stringBuilder.append("@").append(this.injectionPoint);
            if (this.parameterIndex != -1) {
                stringBuilder.append("[").append(this.parameterIndex).append("]");
            }
        }
        return stringBuilder.toString();
    }
}

