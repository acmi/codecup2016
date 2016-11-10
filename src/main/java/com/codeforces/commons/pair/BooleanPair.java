/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.pair;

import com.codeforces.commons.text.StringUtil;

public class BooleanPair
implements Comparable<BooleanPair> {
    private boolean first;
    private boolean second;

    public boolean getFirst() {
        return this.first;
    }

    public boolean getSecond() {
        return this.second;
    }

    @Override
    public int compareTo(BooleanPair booleanPair) {
        int n2 = Boolean.compare(this.first, booleanPair.first);
        return n2 == 0 ? Boolean.compare(this.second, booleanPair.second) : n2;
    }

    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof BooleanPair)) {
            return false;
        }
        BooleanPair booleanPair = (BooleanPair)object;
        return this.first == booleanPair.first && this.second == booleanPair.second;
    }

    public int hashCode() {
        return 32323 * Boolean.hashCode(this.first) + Boolean.hashCode(this.second);
    }

    public String toString() {
        return BooleanPair.toString(this);
    }

    public static String toString(BooleanPair booleanPair) {
        return BooleanPair.toString(BooleanPair.class, booleanPair);
    }

    public static <T extends BooleanPair> String toString(Class<T> class_, T t2) {
        return StringUtil.toString(class_, t2, false, "first", "second");
    }
}

