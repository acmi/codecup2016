/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.base;

import com.google.common.base.Equivalence;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Iterator;

final class PairwiseEquivalence<T>
extends Equivalence<Iterable<T>>
implements Serializable {
    final Equivalence<? super T> elementEquivalence;

    PairwiseEquivalence(Equivalence<? super T> equivalence) {
        this.elementEquivalence = Preconditions.checkNotNull(equivalence);
    }

    @Override
    protected boolean doEquivalent(Iterable<T> iterable, Iterable<T> iterable2) {
        Iterator<T> iterator = iterable.iterator();
        Iterator<T> iterator2 = iterable2.iterator();
        while (iterator.hasNext() && iterator2.hasNext()) {
            if (this.elementEquivalence.equivalent(iterator.next(), iterator2.next())) continue;
            return false;
        }
        return !iterator.hasNext() && !iterator2.hasNext();
    }

    @Override
    protected int doHash(Iterable<T> iterable) {
        int n2 = 78721;
        for (T t2 : iterable) {
            n2 = n2 * 24943 + this.elementEquivalence.hash(t2);
        }
        return n2;
    }

    public boolean equals(Object object) {
        if (object instanceof PairwiseEquivalence) {
            PairwiseEquivalence pairwiseEquivalence = (PairwiseEquivalence)object;
            return this.elementEquivalence.equals(pairwiseEquivalence.elementEquivalence);
        }
        return false;
    }

    public int hashCode() {
        return this.elementEquivalence.hashCode() ^ 1185147655;
    }

    public String toString() {
        return this.elementEquivalence + ".pairwise()";
    }
}

