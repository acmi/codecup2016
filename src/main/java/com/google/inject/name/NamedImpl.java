/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.name;

import com.google.common.base.Preconditions;
import com.google.inject.name.Named;
import java.io.Serializable;
import java.lang.annotation.Annotation;

class NamedImpl
implements Named,
Serializable {
    private final String value;
    private static final long serialVersionUID = 0;

    public NamedImpl(String string) {
        this.value = Preconditions.checkNotNull(string, "name");
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return 127 * "value".hashCode() ^ this.value.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Named)) {
            return false;
        }
        Named named = (Named)object;
        return this.value.equals(named.value());
    }

    @Override
    public String toString() {
        return "@" + Named.class.getName() + "(value=" + this.value + ")";
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Named.class;
    }
}

