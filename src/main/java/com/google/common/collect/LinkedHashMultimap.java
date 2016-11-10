/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.collect.AbstractSetMultimap;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Hashing;
import com.google.common.collect.ImmutableEntry;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public final class LinkedHashMultimap<K, V>
extends AbstractSetMultimap<K, V> {
    transient int valueSetCapacity = 2;
    private transient ValueEntry<K, V> multimapHeaderEntry;

    public static <K, V> LinkedHashMultimap<K, V> create() {
        return new LinkedHashMultimap<K, V>(16, 2);
    }

    private static <K, V> void succeedsInValueSet(ValueSetLink<K, V> valueSetLink, ValueSetLink<K, V> valueSetLink2) {
        valueSetLink.setSuccessorInValueSet(valueSetLink2);
        valueSetLink2.setPredecessorInValueSet(valueSetLink);
    }

    private static <K, V> void succeedsInMultimap(ValueEntry<K, V> valueEntry, ValueEntry<K, V> valueEntry2) {
        valueEntry.setSuccessorInMultimap(valueEntry2);
        valueEntry2.setPredecessorInMultimap(valueEntry);
    }

    private static <K, V> void deleteFromValueSet(ValueSetLink<K, V> valueSetLink) {
        LinkedHashMultimap.succeedsInValueSet(valueSetLink.getPredecessorInValueSet(), valueSetLink.getSuccessorInValueSet());
    }

    private static <K, V> void deleteFromMultimap(ValueEntry<K, V> valueEntry) {
        LinkedHashMultimap.succeedsInMultimap(valueEntry.getPredecessorInMultimap(), valueEntry.getSuccessorInMultimap());
    }

    private LinkedHashMultimap(int n2, int n3) {
        super(new LinkedHashMap(n2));
        CollectPreconditions.checkNonnegative(n3, "expectedValuesPerKey");
        this.valueSetCapacity = n3;
        this.multimapHeaderEntry = new ValueEntry<Object, Object>(null, null, 0, null);
        LinkedHashMultimap.succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
    }

    @Override
    Set<V> createCollection() {
        return new LinkedHashSet(this.valueSetCapacity);
    }

    @Override
    Collection<V> createCollection(K k2) {
        return new ValueSet(k2, this.valueSetCapacity);
    }

    @Override
    public Set<Map.Entry<K, V>> entries() {
        return super.entries();
    }

    @Override
    public Collection<V> values() {
        return super.values();
    }

    @Override
    Iterator<Map.Entry<K, V>> entryIterator() {
        return new Iterator<Map.Entry<K, V>>(){
            ValueEntry<K, V> nextEntry;
            ValueEntry<K, V> toRemove;

            @Override
            public boolean hasNext() {
                return this.nextEntry != LinkedHashMultimap.this.multimapHeaderEntry;
            }

            @Override
            public Map.Entry<K, V> next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                ValueEntry<K, V> valueEntry = this.nextEntry;
                this.toRemove = valueEntry;
                this.nextEntry = this.nextEntry.successorInMultimap;
                return valueEntry;
            }

            @Override
            public void remove() {
                CollectPreconditions.checkRemove(this.toRemove != null);
                LinkedHashMultimap.this.remove(this.toRemove.getKey(), this.toRemove.getValue());
                this.toRemove = null;
            }
        };
    }

    @Override
    Iterator<V> valueIterator() {
        return Maps.valueIterator(this.entryIterator());
    }

    @Override
    public void clear() {
        super.clear();
        LinkedHashMultimap.succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
    }

    final class ValueSet
    extends Sets.ImprovedAbstractSet<V>
    implements ValueSetLink<K, V> {
        private final K key;
        ValueEntry<K, V>[] hashTable;
        private int size;
        private int modCount;
        private ValueSetLink<K, V> firstEntry;
        private ValueSetLink<K, V> lastEntry;

        ValueSet(K k2, int n2) {
            this.size = 0;
            this.modCount = 0;
            this.key = k2;
            this.firstEntry = this;
            this.lastEntry = this;
            int n3 = Hashing.closedTableSize(n2, 1.0);
            ValueEntry[] arrvalueEntry = new ValueEntry[n3];
            this.hashTable = arrvalueEntry;
        }

        private int mask() {
            return this.hashTable.length - 1;
        }

        @Override
        public ValueSetLink<K, V> getPredecessorInValueSet() {
            return this.lastEntry;
        }

        @Override
        public ValueSetLink<K, V> getSuccessorInValueSet() {
            return this.firstEntry;
        }

        @Override
        public void setPredecessorInValueSet(ValueSetLink<K, V> valueSetLink) {
            this.lastEntry = valueSetLink;
        }

        @Override
        public void setSuccessorInValueSet(ValueSetLink<K, V> valueSetLink) {
            this.firstEntry = valueSetLink;
        }

        @Override
        public Iterator<V> iterator() {
            return new Iterator<V>(){
                ValueSetLink<K, V> nextEntry;
                ValueEntry<K, V> toRemove;
                int expectedModCount;

                private void checkForComodification() {
                    if (ValueSet.this.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    }
                }

                @Override
                public boolean hasNext() {
                    this.checkForComodification();
                    return this.nextEntry != ValueSet.this;
                }

                @Override
                public V next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    ValueEntry valueEntry = (ValueEntry)this.nextEntry;
                    Object v2 = valueEntry.getValue();
                    this.toRemove = valueEntry;
                    this.nextEntry = valueEntry.getSuccessorInValueSet();
                    return v2;
                }

                @Override
                public void remove() {
                    this.checkForComodification();
                    CollectPreconditions.checkRemove(this.toRemove != null);
                    ValueSet.this.remove(this.toRemove.getValue());
                    this.expectedModCount = ValueSet.this.modCount;
                    this.toRemove = null;
                }
            };
        }

        @Override
        public int size() {
            return this.size;
        }

        @Override
        public boolean contains(Object object) {
            int n2 = Hashing.smearedHash(object);
            ValueEntry<K, V> valueEntry = this.hashTable[n2 & this.mask()];
            while (valueEntry != null) {
                if (valueEntry.matchesValue(object, n2)) {
                    return true;
                }
                valueEntry = valueEntry.nextInValueBucket;
            }
            return false;
        }

        @Override
        public boolean add(V v2) {
            ValueEntry<K, V> valueEntry;
            int n2 = Hashing.smearedHash(v2);
            int n3 = n2 & this.mask();
            ValueEntry<K, V> valueEntry2 = valueEntry = this.hashTable[n3];
            while (valueEntry2 != null) {
                if (valueEntry2.matchesValue(v2, n2)) {
                    return false;
                }
                valueEntry2 = valueEntry2.nextInValueBucket;
            }
            valueEntry2 = new ValueEntry<K, V>(this.key, v2, n2, valueEntry);
            LinkedHashMultimap.succeedsInValueSet(this.lastEntry, valueEntry2);
            LinkedHashMultimap.succeedsInValueSet(valueEntry2, this);
            LinkedHashMultimap.succeedsInMultimap(LinkedHashMultimap.this.multimapHeaderEntry.getPredecessorInMultimap(), valueEntry2);
            LinkedHashMultimap.succeedsInMultimap(valueEntry2, LinkedHashMultimap.this.multimapHeaderEntry);
            this.hashTable[n3] = valueEntry2;
            ++this.size;
            ++this.modCount;
            this.rehashIfNecessary();
            return true;
        }

        private void rehashIfNecessary() {
            if (Hashing.needsResizing(this.size, this.hashTable.length, 1.0)) {
                ValueEntry[] arrvalueEntry = new ValueEntry[this.hashTable.length * 2];
                this.hashTable = arrvalueEntry;
                int n2 = arrvalueEntry.length - 1;
                for (ValueSetLink<K, V> valueSetLink = this.firstEntry; valueSetLink != this; valueSetLink = valueSetLink.getSuccessorInValueSet()) {
                    ValueEntry valueEntry = (ValueEntry)valueSetLink;
                    int n3 = valueEntry.smearedValueHash & n2;
                    valueEntry.nextInValueBucket = arrvalueEntry[n3];
                    arrvalueEntry[n3] = valueEntry;
                }
            }
        }

        @Override
        public boolean remove(Object object) {
            int n2 = Hashing.smearedHash(object);
            int n3 = n2 & this.mask();
            ValueEntry<K, V> valueEntry = null;
            ValueEntry<K, V> valueEntry2 = this.hashTable[n3];
            while (valueEntry2 != null) {
                if (valueEntry2.matchesValue(object, n2)) {
                    if (valueEntry == null) {
                        this.hashTable[n3] = valueEntry2.nextInValueBucket;
                    } else {
                        valueEntry.nextInValueBucket = valueEntry2.nextInValueBucket;
                    }
                    LinkedHashMultimap.deleteFromValueSet(valueEntry2);
                    LinkedHashMultimap.deleteFromMultimap(valueEntry2);
                    --this.size;
                    ++this.modCount;
                    return true;
                }
                valueEntry = valueEntry2;
                valueEntry2 = valueEntry2.nextInValueBucket;
            }
            return false;
        }

        @Override
        public void clear() {
            Arrays.fill(this.hashTable, null);
            this.size = 0;
            for (ValueSetLink<K, V> valueSetLink = this.firstEntry; valueSetLink != this; valueSetLink = valueSetLink.getSuccessorInValueSet()) {
                ValueEntry valueEntry = (ValueEntry)valueSetLink;
                LinkedHashMultimap.deleteFromMultimap(valueEntry);
            }
            LinkedHashMultimap.succeedsInValueSet(this, this);
            ++this.modCount;
        }

    }

    static final class ValueEntry<K, V>
    extends ImmutableEntry<K, V>
    implements ValueSetLink<K, V> {
        final int smearedValueHash;
        ValueEntry<K, V> nextInValueBucket;
        ValueSetLink<K, V> predecessorInValueSet;
        ValueSetLink<K, V> successorInValueSet;
        ValueEntry<K, V> predecessorInMultimap;
        ValueEntry<K, V> successorInMultimap;

        ValueEntry(K k2, V v2, int n2, ValueEntry<K, V> valueEntry) {
            super(k2, v2);
            this.smearedValueHash = n2;
            this.nextInValueBucket = valueEntry;
        }

        boolean matchesValue(Object object, int n2) {
            return this.smearedValueHash == n2 && Objects.equal(this.getValue(), object);
        }

        @Override
        public ValueSetLink<K, V> getPredecessorInValueSet() {
            return this.predecessorInValueSet;
        }

        @Override
        public ValueSetLink<K, V> getSuccessorInValueSet() {
            return this.successorInValueSet;
        }

        @Override
        public void setPredecessorInValueSet(ValueSetLink<K, V> valueSetLink) {
            this.predecessorInValueSet = valueSetLink;
        }

        @Override
        public void setSuccessorInValueSet(ValueSetLink<K, V> valueSetLink) {
            this.successorInValueSet = valueSetLink;
        }

        public ValueEntry<K, V> getPredecessorInMultimap() {
            return this.predecessorInMultimap;
        }

        public ValueEntry<K, V> getSuccessorInMultimap() {
            return this.successorInMultimap;
        }

        public void setSuccessorInMultimap(ValueEntry<K, V> valueEntry) {
            this.successorInMultimap = valueEntry;
        }

        public void setPredecessorInMultimap(ValueEntry<K, V> valueEntry) {
            this.predecessorInMultimap = valueEntry;
        }
    }

    private static interface ValueSetLink<K, V> {
        public ValueSetLink<K, V> getPredecessorInValueSet();

        public ValueSetLink<K, V> getSuccessorInValueSet();

        public void setPredecessorInValueSet(ValueSetLink<K, V> var1);

        public void setSuccessorInValueSet(ValueSetLink<K, V> var1);
    }

}

