/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.pair;

import com.codeforces.commons.text.StringUtil;

public class BytePair
implements Comparable<BytePair> {
    private byte first;
    private byte second;

    public byte getFirst() {
        return this.first;
    }

    public byte getSecond() {
        return this.second;
    }

    @Override
    public int compareTo(BytePair bytePair) {
        int n2 = Byte.compare(this.first, bytePair.first);
        return n2 == 0 ? Byte.compare(this.second, bytePair.second) : n2;
    }

    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof BytePair)) {
            return false;
        }
        BytePair bytePair = (BytePair)object;
        return this.first == bytePair.first && this.second == bytePair.second;
    }

    public int hashCode() {
        return 32323 * Byte.hashCode(this.first) + Byte.hashCode(this.second);
    }

    public String toString() {
        return BytePair.toString(this);
    }

    public static String toString(BytePair bytePair) {
        return BytePair.toString(BytePair.class, bytePair);
    }

    public static <T extends BytePair> String toString(Class<T> class_, T t2) {
        return StringUtil.toString(class_, t2, false, "first", "second");
    }
}

