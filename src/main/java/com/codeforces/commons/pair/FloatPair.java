/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.pair;

import com.codeforces.commons.text.StringUtil;

public class FloatPair
implements Comparable<FloatPair> {
    private float first;
    private float second;

    public float getFirst() {
        return this.first;
    }

    public float getSecond() {
        return this.second;
    }

    @Override
    public int compareTo(FloatPair floatPair) {
        int n2 = Float.compare(this.first, floatPair.first);
        return n2 == 0 ? Float.compare(this.second, floatPair.second) : n2;
    }

    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof FloatPair)) {
            return false;
        }
        FloatPair floatPair = (FloatPair)object;
        return Float.compare(this.first, floatPair.first) == 0 && Float.compare(this.second, floatPair.second) == 0;
    }

    public int hashCode() {
        return 32323 * Float.hashCode(this.first) + Float.hashCode(this.second);
    }

    public String toString() {
        return FloatPair.toString(this);
    }

    public static String toString(FloatPair floatPair) {
        return FloatPair.toString(FloatPair.class, floatPair);
    }

    public static <T extends FloatPair> String toString(Class<T> class_, T t2) {
        return StringUtil.toString(class_, t2, false, "first", "second");
    }
}

