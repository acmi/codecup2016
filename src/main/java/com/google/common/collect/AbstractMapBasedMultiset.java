/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractMultiset;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Count;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

abstract class AbstractMapBasedMultiset<E>
extends AbstractMultiset<E>
implements Serializable {
    private transient Map<E, Count> backingMap;
    private transient long size;

    protected AbstractMapBasedMultiset(Map<E, Count> map) {
        this.backingMap = Preconditions.checkNotNull(map);
        this.size = super.size();
    }

    @Override
    public Set<Multiset.Entry<E>> entrySet() {
        return super.entrySet();
    }

    @Override
    Iterator<Multiset.Entry<E>> entryIterator() {
        final Iterator<Map.Entry<E, Count>> iterator = this.backingMap.entrySet().iterator();
        return new Iterator<Multiset.Entry<E>>(){
            Map.Entry<E, Count> toRemove;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Multiset.Entry<E> next() {
                Map.Entry entry;
                this.toRemove = entry = (Map.Entry)iterator.next();
                return new Multisets.AbstractEntry<E>(){

                    @Override
                    public E getElement() {
                        return (E)entry.getKey();
                    }

                    @Override
                    public int getCount() {
                        Count count;
                        Count count2 = (Count)entry.getValue();
                        if ((count2 == null || count2.get() == 0) && (count = (Count)AbstractMapBasedMultiset.this.backingMap.get(this.getElement())) != null) {
                            return count.get();
                        }
                        return count2 == null ? 0 : count2.get();
                    }
                };
            }

            @Override
            public void remove() {
                CollectPreconditions.checkRemove(this.toRemove != null);
                AbstractMapBasedMultiset.access$122(AbstractMapBasedMultiset.this, this.toRemove.getValue().getAndSet(0));
                iterator.remove();
                this.toRemove = null;
            }

        };
    }

    @Override
    public void clear() {
        for (Count count : this.backingMap.values()) {
            count.set(0);
        }
        this.backingMap.clear();
        this.size = 0;
    }

    @Override
    int distinctElements() {
        return this.backingMap.size();
    }

    @Override
    public int size() {
        return Ints.saturatedCast(this.size);
    }

    @Override
    public Iterator<E> iterator() {
        return new MapBasedMultisetIterator();
    }

    @Override
    public int count(Object object) {
        Count count = Maps.safeGet(this.backingMap, object);
        return count == null ? 0 : count.get();
    }

    @Override
    public int add(E e2, int n2) {
        int n3;
        if (n2 == 0) {
            return this.count(e2);
        }
        Preconditions.checkArgument(n2 > 0, "occurrences cannot be negative: %s", n2);
        Count count = this.backingMap.get(e2);
        if (count == null) {
            n3 = 0;
            this.backingMap.put(e2, new Count(n2));
        } else {
            n3 = count.get();
            long l2 = (long)n3 + (long)n2;
            Preconditions.checkArgument(l2 <= Integer.MAX_VALUE, "too many occurrences: %s", l2);
            count.getAndAdd(n2);
        }
        this.size += (long)n2;
        return n3;
    }

    @Override
    public int remove(Object object, int n2) {
        int n3;
        if (n2 == 0) {
            return this.count(object);
        }
        Preconditions.checkArgument(n2 > 0, "occurrences cannot be negative: %s", n2);
        Count count = this.backingMap.get(object);
        if (count == null) {
            return 0;
        }
        int n4 = count.get();
        if (n4 > n2) {
            n3 = n2;
        } else {
            n3 = n4;
            this.backingMap.remove(object);
        }
        count.addAndGet(- n3);
        this.size -= (long)n3;
        return n4;
    }

    @Override
    public int setCount(E e2, int n2) {
        int n3;
        CollectPreconditions.checkNonnegative(n2, "count");
        if (n2 == 0) {
            Count count = this.backingMap.remove(e2);
            n3 = AbstractMapBasedMultiset.getAndSet(count, n2);
        } else {
            Count count = this.backingMap.get(e2);
            n3 = AbstractMapBasedMultiset.getAndSet(count, n2);
            if (count == null) {
                this.backingMap.put(e2, new Count(n2));
            }
        }
        this.size += (long)(n2 - n3);
        return n3;
    }

    private static int getAndSet(Count count, int n2) {
        if (count == null) {
            return 0;
        }
        return count.getAndSet(n2);
    }

    static /* synthetic */ long access$122(AbstractMapBasedMultiset abstractMapBasedMultiset, long l2) {
        return abstractMapBasedMultiset.size -= l2;
    }

    private class MapBasedMultisetIterator
    implements Iterator<E> {
        final Iterator<Map.Entry<E, Count>> entryIterator;
        Map.Entry<E, Count> currentEntry;
        int occurrencesLeft;
        boolean canRemove;

        MapBasedMultisetIterator() {
            this.entryIterator = AbstractMapBasedMultiset.this.backingMap.entrySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return this.occurrencesLeft > 0 || this.entryIterator.hasNext();
        }

        @Override
        public E next() {
            if (this.occurrencesLeft == 0) {
                this.currentEntry = this.entryIterator.next();
                this.occurrencesLeft = this.currentEntry.getValue().get();
            }
            --this.occurrencesLeft;
            this.canRemove = true;
            return this.currentEntry.getKey();
        }

        @Override
        public void remove() {
            CollectPreconditions.checkRemove(this.canRemove);
            int n2 = this.currentEntry.getValue().get();
            if (n2 <= 0) {
                throw new ConcurrentModificationException();
            }
            if (this.currentEntry.getValue().addAndGet(-1) == 0) {
                this.entryIterator.remove();
            }
            AbstractMapBasedMultiset.this.size--;
            this.canRemove = false;
        }
    }

}

