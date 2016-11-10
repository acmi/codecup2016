/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.base;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.io.Serializable;
import java.util.Collection;

public final class Predicates {
    private static final Joiner COMMA_JOINER = Joiner.on(',');

    public static <T> Predicate<T> isNull() {
        return ObjectPredicate.IS_NULL.withNarrowedType();
    }

    public static <T> Predicate<T> equalTo(T t2) {
        return t2 == null ? Predicates.isNull() : new IsEqualToPredicate(t2);
    }

    public static <T> Predicate<T> in(Collection<? extends T> collection) {
        return new InPredicate(collection);
    }

    private static class InPredicate<T>
    implements Predicate<T>,
    Serializable {
        private final Collection<?> target;

        private InPredicate(Collection<?> collection) {
            this.target = Preconditions.checkNotNull(collection);
        }

        @Override
        public boolean apply(T t2) {
            try {
                return this.target.contains(t2);
            }
            catch (NullPointerException nullPointerException) {
                return false;
            }
            catch (ClassCastException classCastException) {
                return false;
            }
        }

        public boolean equals(Object object) {
            if (object instanceof InPredicate) {
                InPredicate inPredicate = (InPredicate)object;
                return this.target.equals(inPredicate.target);
            }
            return false;
        }

        public int hashCode() {
            return this.target.hashCode();
        }

        public String toString() {
            return "Predicates.in(" + this.target + ")";
        }
    }

    private static class IsEqualToPredicate<T>
    implements Predicate<T>,
    Serializable {
        private final T target;

        private IsEqualToPredicate(T t2) {
            this.target = t2;
        }

        @Override
        public boolean apply(T t2) {
            return this.target.equals(t2);
        }

        public int hashCode() {
            return this.target.hashCode();
        }

        public boolean equals(Object object) {
            if (object instanceof IsEqualToPredicate) {
                IsEqualToPredicate isEqualToPredicate = (IsEqualToPredicate)object;
                return this.target.equals(isEqualToPredicate.target);
            }
            return false;
        }

        public String toString() {
            return "Predicates.equalTo(" + this.target + ")";
        }
    }

    static abstract class ObjectPredicate
    extends Enum<ObjectPredicate>
    implements Predicate<Object> {
        public static final /* enum */ ObjectPredicate ALWAYS_TRUE = new ObjectPredicate("ALWAYS_TRUE", 0){

            @Override
            public boolean apply(Object object) {
                return true;
            }

            public String toString() {
                return "Predicates.alwaysTrue()";
            }
        };
        public static final /* enum */ ObjectPredicate ALWAYS_FALSE = new ObjectPredicate("ALWAYS_FALSE", 1){

            @Override
            public boolean apply(Object object) {
                return false;
            }

            public String toString() {
                return "Predicates.alwaysFalse()";
            }
        };
        public static final /* enum */ ObjectPredicate IS_NULL = new ObjectPredicate("IS_NULL", 2){

            @Override
            public boolean apply(Object object) {
                return object == null;
            }

            public String toString() {
                return "Predicates.isNull()";
            }
        };
        public static final /* enum */ ObjectPredicate NOT_NULL = new ObjectPredicate("NOT_NULL", 3){

            @Override
            public boolean apply(Object object) {
                return object != null;
            }

            public String toString() {
                return "Predicates.notNull()";
            }
        };
        private static final /* synthetic */ ObjectPredicate[] $VALUES;

        public static ObjectPredicate[] values() {
            return (ObjectPredicate[])$VALUES.clone();
        }

        private ObjectPredicate() {
            super(string, n2);
        }

        <T> Predicate<T> withNarrowedType() {
            return this;
        }

        static {
            $VALUES = new ObjectPredicate[]{ALWAYS_TRUE, ALWAYS_FALSE, IS_NULL, NOT_NULL};
        }

    }

}

