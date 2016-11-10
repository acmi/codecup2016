/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.reflection;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MethodSignature {
    private final String name;
    private final List<Class<?>> parameterTypes;
    private final int hashCode;

    public /* varargs */ MethodSignature(String string, Class<?> ... arrclass) {
        this.name = string;
        this.parameterTypes = Collections.unmodifiableList(Arrays.asList(arrclass));
        int n2 = this.name.hashCode();
        this.hashCode = n2 = 32323 * n2 + this.parameterTypes.hashCode();
    }

    public String getName() {
        return this.name;
    }

    public List<Class<?>> getParameterTypes() {
        return this.parameterTypes;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        MethodSignature methodSignature = (MethodSignature)object;
        return this.name.equals(methodSignature.name) && this.parameterTypes.equals(methodSignature.parameterTypes);
    }

    public int hashCode() {
        return this.hashCode;
    }
}

