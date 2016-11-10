/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.pair;

import com.codeforces.commons.text.StringUtil;

public class IntPair
implements Comparable<IntPair> {
    private int first;
    private int second;

    public IntPair() {
    }

    public IntPair(int n2, int n3) {
        this.first = n2;
        this.second = n3;
    }

    public int getFirst() {
        return this.first;
    }

    public int getSecond() {
        return this.second;
    }

    @Override
    public int compareTo(IntPair intPair) {
        int n2 = Integer.compare(this.first, intPair.first);
        return n2 == 0 ? Integer.compare(this.second, intPair.second) : n2;
    }

    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof IntPair)) {
            return false;
        }
        IntPair intPair = (IntPair)object;
        return this.first == intPair.first && this.second == intPair.second;
    }

    public int hashCode() {
        return 32323 * this.first + this.second;
    }

    public String toString() {
        return IntPair.toString(this);
    }

    public static String toString(IntPair intPair) {
        return IntPair.toString(IntPair.class, intPair);
    }

    public static <T extends IntPair> String toString(Class<T> class_, T t2) {
        return StringUtil.toString(class_, t2, false, "first", "second");
    }
}

