/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class JointIterator<E>
implements Iterator<E> {
    private Iterator<? extends E> i1;
    private Iterator<? extends E> i2;

    public JointIterator(Iterator<? extends E> iterator, Iterator<? extends E> iterator2) {
        if (iterator == null || iterator2 == null) {
            throw new NullPointerException();
        }
        this.i1 = iterator;
        this.i2 = iterator2;
    }

    @Override
    public boolean hasNext() {
        return this.i1.hasNext() || this.i1 != this.i2 && (this.i1 = this.i2).hasNext();
    }

    @Override
    public E next() {
        try {
            return this.i1.next();
        }
        catch (NoSuchElementException noSuchElementException) {
            if (this.i1 == this.i2) {
                throw noSuchElementException;
            }
            this.i1 = this.i2;
            return this.i1.next();
        }
    }

    @Override
    public void remove() {
        this.i1.remove();
    }
}

