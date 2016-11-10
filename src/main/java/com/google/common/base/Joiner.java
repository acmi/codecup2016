/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.base;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Joiner {
    private final String separator;

    public static Joiner on(String string) {
        return new Joiner(string);
    }

    public static Joiner on(char c2) {
        return new Joiner(String.valueOf(c2));
    }

    private Joiner(String string) {
        this.separator = Preconditions.checkNotNull(string);
    }

    private Joiner(Joiner joiner) {
        this.separator = joiner.separator;
    }

    public <A extends Appendable> A appendTo(A a2, Iterator<?> iterator) throws IOException {
        Preconditions.checkNotNull(a2);
        if (iterator.hasNext()) {
            a2.append(this.toString(iterator.next()));
            while (iterator.hasNext()) {
                a2.append(this.separator);
                a2.append(this.toString(iterator.next()));
            }
        }
        return a2;
    }

    public final StringBuilder appendTo(StringBuilder stringBuilder, Iterator<?> iterator) {
        try {
            this.appendTo((A)stringBuilder, iterator);
        }
        catch (IOException iOException) {
            throw new AssertionError(iOException);
        }
        return stringBuilder;
    }

    public final String join(Iterable<?> iterable) {
        return this.join(iterable.iterator());
    }

    public final String join(Iterator<?> iterator) {
        return this.appendTo(new StringBuilder(), iterator).toString();
    }

    public Joiner useForNull(final String string) {
        Preconditions.checkNotNull(string);
        return new Joiner(this){

            @Override
            CharSequence toString(Object object) {
                return object == null ? string : Joiner.this.toString(object);
            }

            @Override
            public Joiner useForNull(String string2) {
                throw new UnsupportedOperationException("already specified useForNull");
            }
        };
    }

    public MapJoiner withKeyValueSeparator(String string) {
        return new MapJoiner(this, string);
    }

    CharSequence toString(Object object) {
        Preconditions.checkNotNull(object);
        return object instanceof CharSequence ? (CharSequence)object : object.toString();
    }

    public static final class MapJoiner {
        private final Joiner joiner;
        private final String keyValueSeparator;

        private MapJoiner(Joiner joiner, String string) {
            this.joiner = joiner;
            this.keyValueSeparator = Preconditions.checkNotNull(string);
        }

        public StringBuilder appendTo(StringBuilder stringBuilder, Map<?, ?> map) {
            return this.appendTo(stringBuilder, map.entrySet());
        }

        public <A extends Appendable> A appendTo(A a2, Iterator<? extends Map.Entry<?, ?>> iterator) throws IOException {
            Preconditions.checkNotNull(a2);
            if (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                a2.append(this.joiner.toString(entry.getKey()));
                a2.append(this.keyValueSeparator);
                a2.append(this.joiner.toString(entry.getValue()));
                while (iterator.hasNext()) {
                    a2.append(this.joiner.separator);
                    Map.Entry entry2 = iterator.next();
                    a2.append(this.joiner.toString(entry2.getKey()));
                    a2.append(this.keyValueSeparator);
                    a2.append(this.joiner.toString(entry2.getValue()));
                }
            }
            return a2;
        }

        public StringBuilder appendTo(StringBuilder stringBuilder, Iterable<? extends Map.Entry<?, ?>> iterable) {
            return this.appendTo(stringBuilder, iterable.iterator());
        }

        public StringBuilder appendTo(StringBuilder stringBuilder, Iterator<? extends Map.Entry<?, ?>> iterator) {
            try {
                this.appendTo((A)stringBuilder, iterator);
            }
            catch (IOException iOException) {
                throw new AssertionError(iOException);
            }
            return stringBuilder;
        }
    }

}

