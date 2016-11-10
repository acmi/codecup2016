/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.pair;

import com.codeforces.commons.text.StringUtil;

public class ShortPair
implements Comparable<ShortPair> {
    private short first;
    private short second;

    public short getFirst() {
        return this.first;
    }

    public short getSecond() {
        return this.second;
    }

    @Override
    public int compareTo(ShortPair shortPair) {
        int n2 = Short.compare(this.first, shortPair.first);
        return n2 == 0 ? Short.compare(this.second, shortPair.second) : n2;
    }

    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ShortPair)) {
            return false;
        }
        ShortPair shortPair = (ShortPair)object;
        return this.first == shortPair.first && this.second == shortPair.second;
    }

    public int hashCode() {
        return 32323 * Short.hashCode(this.first) + Short.hashCode(this.second);
    }

    public String toString() {
        return ShortPair.toString(this);
    }

    public static String toString(ShortPair shortPair) {
        return ShortPair.toString(ShortPair.class, shortPair);
    }

    public static <T extends ShortPair> String toString(Class<T> class_, T t2) {
        return StringUtil.toString(class_, t2, false, "first", "second");
    }
}

