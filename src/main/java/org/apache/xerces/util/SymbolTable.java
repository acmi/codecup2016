/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

public class SymbolTable {
    protected static final int TABLE_SIZE = 101;
    protected Entry[] fBuckets = null;
    protected int fTableSize;
    protected transient int fCount;
    protected int fThreshold;
    protected float fLoadFactor;

    public SymbolTable(int n2, float f2) {
        if (n2 < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + n2);
        }
        if (f2 <= 0.0f || Float.isNaN(f2)) {
            throw new IllegalArgumentException("Illegal Load: " + f2);
        }
        if (n2 == 0) {
            n2 = 1;
        }
        this.fLoadFactor = f2;
        this.fTableSize = n2;
        this.fBuckets = new Entry[this.fTableSize];
        this.fThreshold = (int)((float)this.fTableSize * f2);
        this.fCount = 0;
    }

    public SymbolTable(int n2) {
        this(n2, 0.75f);
    }

    public SymbolTable() {
        this(101, 0.75f);
    }

    public String addSymbol(String string) {
        Entry entry;
        int n2 = this.hash(string) % this.fTableSize;
        Entry entry2 = this.fBuckets[n2];
        while (entry2 != null) {
            if (entry2.symbol.equals(string)) {
                return entry2.symbol;
            }
            entry2 = entry2.next;
        }
        if (this.fCount >= this.fThreshold) {
            this.rehash();
            n2 = this.hash(string) % this.fTableSize;
        }
        this.fBuckets[n2] = entry = new Entry(string, this.fBuckets[n2]);
        ++this.fCount;
        return entry.symbol;
    }

    public String addSymbol(char[] arrc, int n2, int n3) {
        Entry entry;
        int n4 = this.hash(arrc, n2, n3) % this.fTableSize;
        Entry entry2 = this.fBuckets[n4];
        while (entry2 != null) {
            block5 : {
                if (n3 == entry2.characters.length) {
                    int n5 = 0;
                    while (n5 < n3) {
                        if (arrc[n2 + n5] == entry2.characters[n5]) {
                            ++n5;
                            continue;
                        }
                        break block5;
                    }
                    return entry2.symbol;
                }
            }
            entry2 = entry2.next;
        }
        if (this.fCount >= this.fThreshold) {
            this.rehash();
            n4 = this.hash(arrc, n2, n3) % this.fTableSize;
        }
        this.fBuckets[n4] = entry = new Entry(arrc, n2, n3, this.fBuckets[n4]);
        ++this.fCount;
        return entry.symbol;
    }

    public int hash(String string) {
        return string.hashCode() & 134217727;
    }

    public int hash(char[] arrc, int n2, int n3) {
        int n4 = 0;
        int n5 = 0;
        while (n5 < n3) {
            n4 = n4 * 31 + arrc[n2 + n5];
            ++n5;
        }
        return n4 & 134217727;
    }

    protected void rehash() {
        int n2 = this.fBuckets.length;
        Entry[] arrentry = this.fBuckets;
        int n3 = n2 * 2 + 1;
        Entry[] arrentry2 = new Entry[n3];
        this.fThreshold = (int)((float)n3 * this.fLoadFactor);
        this.fBuckets = arrentry2;
        this.fTableSize = this.fBuckets.length;
        int n4 = n2;
        while (n4-- > 0) {
            Entry entry = arrentry[n4];
            while (entry != null) {
                Entry entry2 = entry;
                entry = entry.next;
                int n5 = this.hash(entry2.characters, 0, entry2.characters.length) % n3;
                entry2.next = arrentry2[n5];
                arrentry2[n5] = entry2;
            }
        }
    }

    public boolean containsSymbol(String string) {
        int n2 = this.hash(string) % this.fTableSize;
        int n3 = string.length();
        Entry entry = this.fBuckets[n2];
        while (entry != null) {
            block4 : {
                if (n3 == entry.characters.length) {
                    int n4 = 0;
                    while (n4 < n3) {
                        if (string.charAt(n4) == entry.characters[n4]) {
                            ++n4;
                            continue;
                        }
                        break block4;
                    }
                    return true;
                }
            }
            entry = entry.next;
        }
        return false;
    }

    public boolean containsSymbol(char[] arrc, int n2, int n3) {
        int n4 = this.hash(arrc, n2, n3) % this.fTableSize;
        Entry entry = this.fBuckets[n4];
        while (entry != null) {
            block4 : {
                if (n3 == entry.characters.length) {
                    int n5 = 0;
                    while (n5 < n3) {
                        if (arrc[n2 + n5] == entry.characters[n5]) {
                            ++n5;
                            continue;
                        }
                        break block4;
                    }
                    return true;
                }
            }
            entry = entry.next;
        }
        return false;
    }

    protected static final class Entry {
        public final String symbol;
        public final char[] characters;
        public Entry next;

        public Entry(String string, Entry entry) {
            this.symbol = string.intern();
            this.characters = new char[string.length()];
            string.getChars(0, this.characters.length, this.characters, 0);
            this.next = entry;
        }

        public Entry(char[] arrc, int n2, int n3, Entry entry) {
            this.characters = new char[n3];
            System.arraycopy(arrc, n2, this.characters, 0, n3);
            this.symbol = new String(this.characters).intern();
            this.next = entry;
        }
    }

}

