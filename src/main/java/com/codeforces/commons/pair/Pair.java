/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.pair;

import com.codeforces.commons.pair.SimplePair;
import com.codeforces.commons.text.StringUtil;

public class Pair<F extends Comparable<? super F>, S extends Comparable<? super S>>
extends SimplePair<F, S>
implements Comparable<Pair<F, S>> {
    public Pair() {
    }

    public Pair(F f2, S s2) {
        super(f2, s2);
    }

    @Override
    public int compareTo(Pair<F, S> pair) {
        int n2;
        if (this.first != pair.first) {
            if (this.first == null) {
                return -1;
            }
            if (pair.first == null) {
                return 1;
            }
            n2 = ((Comparable)this.first).compareTo(pair.first);
            if (n2 != 0) {
                return n2;
            }
        }
        if (this.second != pair.second) {
            if (this.second == null) {
                return -1;
            }
            if (pair.second == null) {
                return 1;
            }
            n2 = ((Comparable)this.second).compareTo(pair.second);
            if (n2 != 0) {
                return n2;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return Pair.toString(this);
    }

    public static String toString(Pair pair) {
        return Pair.toString(Pair.class, pair);
    }

    public static <T extends Pair> String toString(Class<T> class_, T t2) {
        return StringUtil.toString(class_, t2, false, "first", "second");
    }
}

