/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.AbstractIndexedListIterator;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.PeekingIterator;
import com.google.common.collect.TransformedIterator;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.UnmodifiableListIterator;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Iterators {
    static final UnmodifiableListIterator<Object> EMPTY_LIST_ITERATOR = new UnmodifiableListIterator<Object>(){

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            throw new NoSuchElementException();
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public Object previous() {
            throw new NoSuchElementException();
        }

        @Override
        public int nextIndex() {
            return 0;
        }

        @Override
        public int previousIndex() {
            return -1;
        }
    };
    private static final Iterator<Object> EMPTY_MODIFIABLE_ITERATOR = new Iterator<Object>(){

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            CollectPreconditions.checkRemove(false);
        }
    };

    @Deprecated
    public static <T> UnmodifiableIterator<T> emptyIterator() {
        return Iterators.emptyListIterator();
    }

    static <T> UnmodifiableListIterator<T> emptyListIterator() {
        return EMPTY_LIST_ITERATOR;
    }

    static <T> Iterator<T> emptyModifiableIterator() {
        return EMPTY_MODIFIABLE_ITERATOR;
    }

    public static <T> UnmodifiableIterator<T> unmodifiableIterator(final Iterator<T> iterator) {
        Preconditions.checkNotNull(iterator);
        if (iterator instanceof UnmodifiableIterator) {
            return (UnmodifiableIterator)iterator;
        }
        return new UnmodifiableIterator<T>(){

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return (T)iterator.next();
            }
        };
    }

    public static boolean contains(Iterator<?> iterator, Object object) {
        return Iterators.any(iterator, Predicates.equalTo(object));
    }

    public static boolean removeAll(Iterator<?> iterator, Collection<?> collection) {
        return Iterators.removeIf(iterator, Predicates.in(collection));
    }

    public static <T> boolean removeIf(Iterator<T> iterator, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate);
        boolean bl = false;
        while (iterator.hasNext()) {
            if (!predicate.apply(iterator.next())) continue;
            iterator.remove();
            bl = true;
        }
        return bl;
    }

    public static boolean elementsEqual(Iterator<?> iterator, Iterator<?> iterator2) {
        while (iterator.hasNext()) {
            Object obj;
            if (!iterator2.hasNext()) {
                return false;
            }
            Object obj2 = iterator.next();
            if (Objects.equal(obj2, obj = iterator2.next())) continue;
            return false;
        }
        return !iterator2.hasNext();
    }

    public static String toString(Iterator<?> iterator) {
        return Collections2.STANDARD_JOINER.appendTo(new StringBuilder().append('['), iterator).append(']').toString();
    }

    public static <T> T getOnlyElement(Iterator<T> iterator) {
        T t2 = iterator.next();
        if (!iterator.hasNext()) {
            return t2;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("expected one element but was: <" + t2);
        for (int i2 = 0; i2 < 4 && iterator.hasNext(); ++i2) {
            stringBuilder.append(", " + iterator.next());
        }
        if (iterator.hasNext()) {
            stringBuilder.append(", ...");
        }
        stringBuilder.append('>');
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static <T> boolean addAll(Collection<T> collection, Iterator<? extends T> iterator) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(iterator);
        boolean bl = false;
        while (iterator.hasNext()) {
            bl |= collection.add(iterator.next());
        }
        return bl;
    }

    public static <T> Iterator<T> concat(final Iterator<? extends Iterator<? extends T>> iterator) {
        Preconditions.checkNotNull(iterator);
        return new Iterator<T>(){
            Iterator<? extends T> current;
            Iterator<? extends T> removeFrom;

            @Override
            public boolean hasNext() {
                boolean bl;
                while (!(bl = Preconditions.checkNotNull(this.current).hasNext()) && iterator.hasNext()) {
                    this.current = (Iterator)iterator.next();
                }
                return bl;
            }

            @Override
            public T next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                this.removeFrom = this.current;
                return this.current.next();
            }

            @Override
            public void remove() {
                CollectPreconditions.checkRemove(this.removeFrom != null);
                this.removeFrom.remove();
                this.removeFrom = null;
            }
        };
    }

    public static <T> boolean any(Iterator<T> iterator, Predicate<? super T> predicate) {
        return Iterators.indexOf(iterator, predicate) != -1;
    }

    public static <T> int indexOf(Iterator<T> iterator, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate, "predicate");
        int n2 = 0;
        while (iterator.hasNext()) {
            T t2 = iterator.next();
            if (predicate.apply(t2)) {
                return n2;
            }
            ++n2;
        }
        return -1;
    }

    public static <F, T> Iterator<T> transform(Iterator<F> iterator, final Function<? super F, ? extends T> function) {
        Preconditions.checkNotNull(function);
        return new TransformedIterator<F, T>(iterator){

            @Override
            T transform(F f2) {
                return function.apply(f2);
            }
        };
    }

    public static <T> T getNext(Iterator<? extends T> iterator, T t2) {
        return iterator.hasNext() ? iterator.next() : t2;
    }

    static void clear(Iterator<?> iterator) {
        Preconditions.checkNotNull(iterator);
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

    public static /* varargs */ <T> UnmodifiableIterator<T> forArray(T ... arrT) {
        return Iterators.forArray(arrT, 0, arrT.length, 0);
    }

    static <T> UnmodifiableListIterator<T> forArray(final T[] arrT, final int n2, int n3, int n4) {
        Preconditions.checkArgument(n3 >= 0);
        int n5 = n2 + n3;
        Preconditions.checkPositionIndexes(n2, n5, arrT.length);
        Preconditions.checkPositionIndex(n4, n3);
        if (n3 == 0) {
            return Iterators.emptyListIterator();
        }
        return new AbstractIndexedListIterator<T>(n3, n4){

            @Override
            protected T get(int n22) {
                return (T)arrT[n2 + n22];
            }
        };
    }

    public static <T> UnmodifiableIterator<T> singletonIterator(final T t2) {
        return new UnmodifiableIterator<T>(){
            boolean done;

            @Override
            public boolean hasNext() {
                return !this.done;
            }

            @Override
            public T next() {
                if (this.done) {
                    throw new NoSuchElementException();
                }
                this.done = true;
                return (T)t2;
            }
        };
    }

    public static <T> PeekingIterator<T> peekingIterator(Iterator<? extends T> iterator) {
        if (iterator instanceof PeekingImpl) {
            PeekingImpl peekingImpl = (PeekingImpl)iterator;
            return peekingImpl;
        }
        return new PeekingImpl<T>(iterator);
    }

    private static class PeekingImpl<E>
    implements PeekingIterator<E> {
        private final Iterator<? extends E> iterator;
        private boolean hasPeeked;
        private E peekedElement;

        public PeekingImpl(Iterator<? extends E> iterator) {
            this.iterator = Preconditions.checkNotNull(iterator);
        }

        @Override
        public boolean hasNext() {
            return this.hasPeeked || this.iterator.hasNext();
        }

        @Override
        public E next() {
            if (!this.hasPeeked) {
                return this.iterator.next();
            }
            E e2 = this.peekedElement;
            this.hasPeeked = false;
            this.peekedElement = null;
            return e2;
        }

        @Override
        public void remove() {
            Preconditions.checkState(!this.hasPeeked, "Can't remove after you've peeked at next");
            this.iterator.remove();
        }

        @Override
        public E peek() {
            if (!this.hasPeeked) {
                this.peekedElement = this.iterator.next();
                this.hasPeeked = true;
            }
            return this.peekedElement;
        }
    }

}

