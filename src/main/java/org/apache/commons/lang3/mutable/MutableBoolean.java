/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.lang3.mutable;

import java.io.Serializable;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.mutable.Mutable;

public class MutableBoolean
implements Serializable,
Comparable<MutableBoolean>,
Mutable<Boolean> {
    private boolean value;

    public MutableBoolean() {
    }

    public MutableBoolean(boolean bl) {
        this.value = bl;
    }

    @Override
    public void setValue(boolean bl) {
        this.value = bl;
    }

    @Override
    public void setValue(Boolean bl) {
        this.value = bl;
    }

    public boolean booleanValue() {
        return this.value;
    }

    public boolean equals(Object object) {
        if (object instanceof MutableBoolean) {
            return this.value == ((MutableBoolean)object).booleanValue();
        }
        return false;
    }

    public int hashCode() {
        return this.value ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode();
    }

    @Override
    public int compareTo(MutableBoolean mutableBoolean) {
        return BooleanUtils.compare(this.value, mutableBoolean.value);
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}

