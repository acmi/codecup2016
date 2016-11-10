/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.base;

import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;

final class FunctionalEquivalence<F, T>
extends Equivalence<F>
implements Serializable {
    private final Function<F, ? extends T> function;
    private final Equivalence<T> resultEquivalence;

    FunctionalEquivalence(Function<F, ? extends T> function, Equivalence<T> equivalence) {
        this.function = Preconditions.checkNotNull(function);
        this.resultEquivalence = Preconditions.checkNotNull(equivalence);
    }

    @Override
    protected boolean doEquivalent(F f2, F f3) {
        return this.resultEquivalence.equivalent(this.function.apply(f2), this.function.apply(f3));
    }

    @Override
    protected int doHash(F f2) {
        return this.resultEquivalence.hash(this.function.apply(f2));
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof FunctionalEquivalence) {
            FunctionalEquivalence functionalEquivalence = (FunctionalEquivalence)object;
            return this.function.equals(functionalEquivalence.function) && this.resultEquivalence.equals(functionalEquivalence.resultEquivalence);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(this.function, this.resultEquivalence);
    }

    public String toString() {
        return this.resultEquivalence + ".onResultOf(" + this.function + ")";
    }
}

