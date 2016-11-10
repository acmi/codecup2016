/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

public class SymbolHash {
    protected int fTableSize = 101;
    protected Entry[] fBuckets;
    protected int fNum = 0;

    public SymbolHash() {
        this.fBuckets = new Entry[this.fTableSize];
    }

    public SymbolHash(int n2) {
        this.fTableSize = n2;
        this.fBuckets = new Entry[this.fTableSize];
    }

    public void put(Object object, Object object2) {
        int n2 = (object.hashCode() & Integer.MAX_VALUE) % this.fTableSize;
        Entry entry = this.search(object, n2);
        if (entry != null) {
            entry.value = object2;
        } else {
            this.fBuckets[n2] = entry = new Entry(object, object2, this.fBuckets[n2]);
            ++this.fNum;
        }
    }

    public Object get(Object object) {
        int n2 = (object.hashCode() & Integer.MAX_VALUE) % this.fTableSize;
        Entry entry = this.search(object, n2);
        if (entry != null) {
            return entry.value;
        }
        return null;
    }

    public int getLength() {
        return this.fNum;
    }

    public int getValues(Object[] arrobject, int n2) {
        int n3 = 0;
        int n4 = 0;
        while (n3 < this.fTableSize && n4 < this.fNum) {
            Entry entry = this.fBuckets[n3];
            while (entry != null) {
                arrobject[n2 + n4] = entry.value;
                ++n4;
                entry = entry.next;
            }
            ++n3;
        }
        return this.fNum;
    }

    public Object[] getEntries() {
        Object[] arrobject = new Object[this.fNum << 1];
        int n2 = 0;
        int n3 = 0;
        while (n2 < this.fTableSize && n3 < this.fNum << 1) {
            Entry entry = this.fBuckets[n2];
            while (entry != null) {
                arrobject[n3] = entry.key;
                arrobject[++n3] = entry.value;
                ++n3;
                entry = entry.next;
            }
            ++n2;
        }
        return arrobject;
    }

    public SymbolHash makeClone() {
        SymbolHash symbolHash = new SymbolHash(this.fTableSize);
        symbolHash.fNum = this.fNum;
        int n2 = 0;
        while (n2 < this.fTableSize) {
            if (this.fBuckets[n2] != null) {
                symbolHash.fBuckets[n2] = this.fBuckets[n2].makeClone();
            }
            ++n2;
        }
        return symbolHash;
    }

    public void clear() {
        int n2 = 0;
        while (n2 < this.fTableSize) {
            this.fBuckets[n2] = null;
            ++n2;
        }
        this.fNum = 0;
    }

    protected Entry search(Object object, int n2) {
        Entry entry = this.fBuckets[n2];
        while (entry != null) {
            if (object.equals(entry.key)) {
                return entry;
            }
            entry = entry.next;
        }
        return null;
    }

    protected static final class Entry {
        public Object key;
        public Object value;
        public Entry next;

        public Entry() {
            this.key = null;
            this.value = null;
            this.next = null;
        }

        public Entry(Object object, Object object2, Entry entry) {
            this.key = object;
            this.value = object2;
            this.next = entry;
        }

        public Entry makeClone() {
            Entry entry = new Entry();
            entry.key = this.key;
            entry.value = this.value;
            if (this.next != null) {
                entry.next = this.next.makeClone();
            }
            return entry;
        }
    }

}

