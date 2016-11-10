/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

import com.google.inject.internal.asm.$Type;

public abstract class $ClassInfo {
    protected $ClassInfo() {
    }

    public abstract $Type getType();

    public abstract $Type getSuperType();

    public abstract $Type[] getInterfaces();

    public abstract int getModifiers();

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof $ClassInfo)) {
            return false;
        }
        return this.getType().equals((($ClassInfo)object).getType());
    }

    public int hashCode() {
        return this.getType().hashCode();
    }

    public String toString() {
        return this.getType().getClassName();
    }
}

