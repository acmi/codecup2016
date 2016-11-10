/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class FilteringIterator<T>
implements Iterator<T> {
    private final Iterator<T> it;
    private Boolean hasNext;
    private T next;

    protected FilteringIterator(Iterator<T> iterator) {
        this.it = iterator;
        if (null == this.it) {
            throw new NullPointerException();
        }
    }

    protected abstract boolean accept(T var1);

    @Override
    public boolean hasNext() {
        if (null != this.hasNext) {
            return this.hasNext;
        }
        while (this.it.hasNext()) {
            this.next = this.it.next();
            if (!this.accept(this.next)) continue;
            this.hasNext = true;
            return this.hasNext;
        }
        this.hasNext = false;
        return this.hasNext;
    }

    @Override
    public T next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        this.hasNext = null;
        return this.next;
    }

    @Override
    public void remove() {
        this.it.remove();
    }
}

