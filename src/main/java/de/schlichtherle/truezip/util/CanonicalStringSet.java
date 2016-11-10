/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.util;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class CanonicalStringSet
extends AbstractSet<String> {
    private final Canonicalizer canonicalizer;
    private final char separator;
    private final Set<String> set = new TreeSet<String>();

    public CanonicalStringSet(Canonicalizer canonicalizer, char c2) {
        if (null == canonicalizer) {
            throw new NullPointerException();
        }
        this.canonicalizer = canonicalizer;
        this.separator = c2;
    }

    @Override
    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    @Override
    public int size() {
        return this.set.size();
    }

    @Override
    public Iterator<String> iterator() {
        return this.set.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.set.toArray();
    }

    @Override
    public <T> T[] toArray(T[] arrT) {
        return this.set.toArray(arrT);
    }

    @Override
    public String toString() {
        int n2 = this.size() * 11;
        if (0 >= n2) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(n2);
        for (String string : this) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(this.separator);
            }
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean contains(Object object) {
        return this.set.contains(this.canonicalizer.map(object));
    }

    @Override
    public boolean add(String string) {
        return this.set.add(this.canonicalizer.map(string));
    }

    @Override
    public boolean remove(Object object) {
        return this.set.remove(this.canonicalizer.map(object));
    }

    @Override
    public void clear() {
        this.set.clear();
    }

    public boolean addAll(String string) {
        boolean bl = false;
        CanonicalStringIterator canonicalStringIterator = new CanonicalStringIterator(string);
        while (canonicalStringIterator.hasNext()) {
            bl |= this.set.add((String)canonicalStringIterator.next());
        }
        return bl;
    }

    public boolean retainAll(CanonicalStringSet canonicalStringSet) {
        return this.set.retainAll(canonicalStringSet.set);
    }

    public boolean removeAll(CanonicalStringSet canonicalStringSet) {
        return this.set.removeAll(canonicalStringSet.set);
    }

    private class CanonicalStringIterator
    implements Iterator<String> {
        private final StringTokenizer tokenizer;
        private String canonical;

        private CanonicalStringIterator(String string) {
            this.tokenizer = new StringTokenizer(string, "" + CanonicalStringSet.this.separator);
            this.advance();
        }

        private void advance() {
            while (this.tokenizer.hasMoreTokens()) {
                this.canonical = CanonicalStringSet.this.canonicalizer.map(this.tokenizer.nextToken());
                if (null == this.canonical) continue;
                return;
            }
            this.canonical = null;
        }

        @Override
        public boolean hasNext() {
            return null != this.canonical;
        }

        @Override
        public String next() {
            String string = this.canonical;
            if (null == string) {
                throw new NoSuchElementException();
            }
            this.advance();
            return string;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static interface Canonicalizer {
        public String map(Object var1);
    }

}

