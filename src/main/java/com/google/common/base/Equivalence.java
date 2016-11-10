/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.base;

import com.google.common.base.Function;
import com.google.common.base.FunctionalEquivalence;
import com.google.common.base.Objects;
import com.google.common.base.PairwiseEquivalence;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.io.Serializable;

public abstract class Equivalence<T> {
    protected Equivalence() {
    }

    public final boolean equivalent(T t2, T t3) {
        if (t2 == t3) {
            return true;
        }
        if (t2 == null || t3 == null) {
            return false;
        }
        return this.doEquivalent(t2, t3);
    }

    protected abstract boolean doEquivalent(T var1, T var2);

    public final int hash(T t2) {
        if (t2 == null) {
            return 0;
        }
        return this.doHash(t2);
    }

    protected abstract int doHash(T var1);

    public final <F> Equivalence<F> onResultOf(Function<F, ? extends T> function) {
        return new FunctionalEquivalence<F, T>(function, this);
    }

    public final <S extends T> Wrapper<S> wrap(S s2) {
        return new Wrapper<T>(this, s2);
    }

    public final <S extends T> Equivalence<Iterable<S>> pairwise() {
        return new PairwiseEquivalence<T>(this);
    }

    public final Predicate<T> equivalentTo(T t2) {
        return new EquivalentToPredicate<T>(this, t2);
    }

    public static Equivalence<Object> equals() {
        return Equals.INSTANCE;
    }

    public static Equivalence<Object> identity() {
        return Identity.INSTANCE;
    }

    static final class Identity
    extends Equivalence<Object>
    implements Serializable {
        static final Identity INSTANCE = new Identity();

        Identity() {
        }

        @Override
        protected boolean doEquivalent(Object object, Object object2) {
            return false;
        }

        @Override
        protected int doHash(Object object) {
            return System.identityHashCode(object);
        }
    }

    static final class Equals
    extends Equivalence<Object>
    implements Serializable {
        static final Equals INSTANCE = new Equals();

        Equals() {
        }

        @Override
        protected boolean doEquivalent(Object object, Object object2) {
            return object.equals(object2);
        }

        @Override
        protected int doHash(Object object) {
            return object.hashCode();
        }
    }

    private static final class EquivalentToPredicate<T>
    implements Predicate<T>,
    Serializable {
        private final Equivalence<T> equivalence;
        private final T target;

        EquivalentToPredicate(Equivalence<T> equivalence, T t2) {
            this.equivalence = Preconditions.checkNotNull(equivalence);
            this.target = t2;
        }

        @Override
        public boolean apply(T t2) {
            return this.equivalence.equivalent(t2, this.target);
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object instanceof EquivalentToPredicate) {
                EquivalentToPredicate equivalentToPredicate = (EquivalentToPredicate)object;
                return this.equivalence.equals(equivalentToPredicate.equivalence) && Objects.equal(this.target, equivalentToPredicate.target);
            }
            return false;
        }

        public int hashCode() {
            return Objects.hashCode(this.equivalence, this.target);
        }

        public String toString() {
            return this.equivalence + ".equivalentTo(" + this.target + ")";
        }
    }

    public static final class Wrapper<T>
    implements Serializable {
        private final Equivalence<? super T> equivalence;
        private final T reference;

        private Wrapper(Equivalence<? super T> equivalence, T t2) {
            this.equivalence = Preconditions.checkNotNull(equivalence);
            this.reference = t2;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (object instanceof Wrapper) {
                Wrapper wrapper = (Wrapper)object;
                if (this.equivalence.equals(wrapper.equivalence)) {
                    Equivalence<T> equivalence = this.equivalence;
                    return equivalence.equivalent(this.reference, wrapper.reference);
                }
            }
            return false;
        }

        public int hashCode() {
            return this.equivalence.hash(this.reference);
        }

        public String toString() {
            return this.equivalence + ".wrap(" + this.reference + ")";
        }
    }

}

