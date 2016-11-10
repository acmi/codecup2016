/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.pair;

import com.codeforces.commons.text.StringUtil;

public class LongPair
implements Comparable<LongPair> {
    private long first;
    private long second;

    public LongPair() {
    }

    public LongPair(long l2, long l3) {
        this.first = l2;
        this.second = l3;
    }

    public long getFirst() {
        return this.first;
    }

    public long getSecond() {
        return this.second;
    }

    @Override
    public int compareTo(LongPair longPair) {
        int n2 = Long.compare(this.first, longPair.first);
        return n2 == 0 ? Long.compare(this.second, longPair.second) : n2;
    }

    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof LongPair)) {
            return false;
        }
        LongPair longPair = (LongPair)object;
        return this.first == longPair.first && this.second == longPair.second;
    }

    public int hashCode() {
        return 32323 * Long.hashCode(this.first) + Long.hashCode(this.second);
    }

    public String toString() {
        return LongPair.toString(this);
    }

    public static String toString(LongPair longPair) {
        return LongPair.toString(LongPair.class, longPair);
    }

    public static <T extends LongPair> String toString(Class<T> class_, T t2) {
        return StringUtil.toString(class_, t2, false, "first", "second");
    }
}

