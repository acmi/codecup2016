/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.matcher;

import com.google.inject.matcher.Matcher;
import java.io.Serializable;

public abstract class AbstractMatcher<T>
implements Matcher<T> {
    @Override
    public Matcher<T> and(Matcher<? super T> matcher) {
        return new AndMatcher<T>(this, matcher);
    }

    @Override
    public Matcher<T> or(Matcher<? super T> matcher) {
        return new OrMatcher<T>(this, matcher);
    }

    private static class OrMatcher<T>
    extends AbstractMatcher<T>
    implements Serializable {
        private final Matcher<? super T> a;
        private final Matcher<? super T> b;
        private static final long serialVersionUID = 0;

        public OrMatcher(Matcher<? super T> matcher, Matcher<? super T> matcher2) {
            this.a = matcher;
            this.b = matcher2;
        }

        @Override
        public boolean matches(T t2) {
            return this.a.matches(t2) || this.b.matches(t2);
        }

        public boolean equals(Object object) {
            return object instanceof OrMatcher && ((OrMatcher)object).a.equals(this.a) && ((OrMatcher)object).b.equals(this.b);
        }

        public int hashCode() {
            return 37 * (this.a.hashCode() ^ this.b.hashCode());
        }

        public String toString() {
            return "or(" + this.a + ", " + this.b + ")";
        }
    }

    private static class AndMatcher<T>
    extends AbstractMatcher<T>
    implements Serializable {
        private final Matcher<? super T> a;
        private final Matcher<? super T> b;
        private static final long serialVersionUID = 0;

        public AndMatcher(Matcher<? super T> matcher, Matcher<? super T> matcher2) {
            this.a = matcher;
            this.b = matcher2;
        }

        @Override
        public boolean matches(T t2) {
            return this.a.matches(t2) && this.b.matches(t2);
        }

        public boolean equals(Object object) {
            return object instanceof AndMatcher && ((AndMatcher)object).a.equals(this.a) && ((AndMatcher)object).b.equals(this.b);
        }

        public int hashCode() {
            return 41 * (this.a.hashCode() ^ this.b.hashCode());
        }

        public String toString() {
            return "and(" + this.a + ", " + this.b + ")";
        }
    }

}

