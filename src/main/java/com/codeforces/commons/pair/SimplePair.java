/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.pair;

import com.codeforces.commons.text.StringUtil;

public class SimplePair<F, S> {
    protected F first;
    protected S second;

    public SimplePair() {
    }

    public SimplePair(F f2, S s2) {
        this.first = f2;
        this.second = s2;
    }

    public F getFirst() {
        return this.first;
    }

    public S getSecond() {
        return this.second;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof SimplePair)) {
            return false;
        }
        SimplePair simplePair = (SimplePair)object;
        return (this.first == null ? simplePair.first == null : this.first.equals(simplePair.first)) && (this.second == null ? simplePair.second == null : this.second.equals(simplePair.second));
    }

    public int hashCode() {
        int n2 = this.first == null ? 0 : this.first.hashCode();
        n2 = 32323 * n2 + (this.second == null ? 0 : this.second.hashCode());
        return n2;
    }

    public String toString() {
        return StringUtil.toString((Object)this, false, "first", "second");
    }
}

