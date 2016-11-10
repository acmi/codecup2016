/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.pair;

import com.codeforces.commons.text.StringUtil;

public class DoublePair
implements Comparable<DoublePair> {
    private double first;
    private double second;

    public DoublePair() {
    }

    public DoublePair(double d2, double d3) {
        this.first = d2;
        this.second = d3;
    }

    public double getFirst() {
        return this.first;
    }

    public void setFirst(double d2) {
        this.first = d2;
    }

    public double getSecond() {
        return this.second;
    }

    public void setSecond(double d2) {
        this.second = d2;
    }

    @Override
    public int compareTo(DoublePair doublePair) {
        int n2 = Double.compare(this.first, doublePair.first);
        return n2 == 0 ? Double.compare(this.second, doublePair.second) : n2;
    }

    public boolean equals(double d2, double d3) {
        return Double.compare(this.first, d2) == 0 && Double.compare(this.second, d3) == 0;
    }

    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof DoublePair)) {
            return false;
        }
        DoublePair doublePair = (DoublePair)object;
        return Double.compare(this.first, doublePair.first) == 0 && Double.compare(this.second, doublePair.second) == 0;
    }

    public int hashCode() {
        return 32323 * Double.hashCode(this.first) + Double.hashCode(this.second);
    }

    public String toString() {
        return DoublePair.toString(this);
    }

    public static String toString(DoublePair doublePair) {
        return DoublePair.toString(DoublePair.class, doublePair);
    }

    public static <T extends DoublePair> String toString(Class<T> class_, T t2) {
        return StringUtil.toString(class_, t2, false, "first", "second");
    }
}

