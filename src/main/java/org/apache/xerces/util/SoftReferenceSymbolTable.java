/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import org.apache.xerces.util.SymbolTable;

public class SoftReferenceSymbolTable
extends SymbolTable {
    protected SREntry[] fBuckets = null;
    private final ReferenceQueue fReferenceQueue;

    public SoftReferenceSymbolTable(int n2, float f2) {
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
        this.fBuckets = new SREntry[this.fTableSize];
        this.fThreshold = (int)((float)this.fTableSize * f2);
        this.fCount = 0;
        this.fReferenceQueue = new ReferenceQueue();
    }

    public SoftReferenceSymbolTable(int n2) {
        this(n2, 0.75f);
    }

    public SoftReferenceSymbolTable() {
        this(101, 0.75f);
    }

    public String addSymbol(String string) {
        Object object;
        this.clean();
        int n2 = this.hash(string) % this.fTableSize;
        SREntry sREntry = this.fBuckets[n2];
        while (sREntry != null) {
            object = (SREntryData)sREntry.get();
            if (object != null && object.symbol.equals(string)) {
                return object.symbol;
            }
            sREntry = sREntry.next;
        }
        if (this.fCount >= this.fThreshold) {
            this.rehash();
            n2 = this.hash(string) % this.fTableSize;
        }
        string = string.intern();
        this.fBuckets[n2] = object = new SREntry(string, this.fBuckets[n2], n2, this.fReferenceQueue);
        ++this.fCount;
        return string;
    }

    public String addSymbol(char[] arrc, int n2, int n3) {
        Object object;
        SREntry sREntry;
        this.clean();
        int n4 = this.hash(arrc, n2, n3) % this.fTableSize;
        SREntry sREntry2 = this.fBuckets[n4];
        while (sREntry2 != null) {
            block5 : {
                object = (SREntryData)sREntry2.get();
                if (object != null && n3 == object.characters.length) {
                    int n5 = 0;
                    while (n5 < n3) {
                        if (arrc[n2 + n5] == object.characters[n5]) {
                            ++n5;
                            continue;
                        }
                        break block5;
                    }
                    return object.symbol;
                }
            }
            sREntry2 = sREntry2.next;
        }
        if (this.fCount >= this.fThreshold) {
            this.rehash();
            n4 = this.hash(arrc, n2, n3) % this.fTableSize;
        }
        object = new String(arrc, n2, n3).intern();
        this.fBuckets[n4] = sREntry = new SREntry((String)object, arrc, n2, n3, this.fBuckets[n4], n4, this.fReferenceQueue);
        ++this.fCount;
        return object;
    }

    protected void rehash() {
        int n2 = this.fBuckets.length;
        SREntry[] arrsREntry = this.fBuckets;
        int n3 = n2 * 2 + 1;
        SREntry[] arrsREntry2 = new SREntry[n3];
        this.fThreshold = (int)((float)n3 * this.fLoadFactor);
        this.fBuckets = arrsREntry2;
        this.fTableSize = this.fBuckets.length;
        int n4 = n2;
        while (n4-- > 0) {
            SREntry sREntry = arrsREntry[n4];
            while (sREntry != null) {
                SREntry sREntry2 = sREntry;
                sREntry = sREntry.next;
                SREntryData sREntryData = (SREntryData)sREntry2.get();
                if (sREntryData != null) {
                    int n5 = this.hash(sREntryData.characters, 0, sREntryData.characters.length) % n3;
                    if (arrsREntry2[n5] != null) {
                        arrsREntry2[n5].prev = sREntry2;
                    }
                    sREntry2.next = arrsREntry2[n5];
                    sREntry2.prev = null;
                    arrsREntry2[n5] = sREntry2;
                    continue;
                }
                --this.fCount;
            }
        }
    }

    public boolean containsSymbol(String string) {
        int n2 = this.hash(string) % this.fTableSize;
        int n3 = string.length();
        SREntry sREntry = this.fBuckets[n2];
        while (sREntry != null) {
            block4 : {
                SREntryData sREntryData = (SREntryData)sREntry.get();
                if (sREntryData != null && n3 == sREntryData.characters.length) {
                    int n4 = 0;
                    while (n4 < n3) {
                        if (string.charAt(n4) == sREntryData.characters[n4]) {
                            ++n4;
                            continue;
                        }
                        break block4;
                    }
                    return true;
                }
            }
            sREntry = sREntry.next;
        }
        return false;
    }

    public boolean containsSymbol(char[] arrc, int n2, int n3) {
        int n4 = this.hash(arrc, n2, n3) % this.fTableSize;
        SREntry sREntry = this.fBuckets[n4];
        while (sREntry != null) {
            block4 : {
                SREntryData sREntryData = (SREntryData)sREntry.get();
                if (sREntryData != null && n3 == sREntryData.characters.length) {
                    int n5 = 0;
                    while (n5 < n3) {
                        if (arrc[n2 + n5] == sREntryData.characters[n5]) {
                            ++n5;
                            continue;
                        }
                        break block4;
                    }
                    return true;
                }
            }
            sREntry = sREntry.next;
        }
        return false;
    }

    private void removeEntry(SREntry sREntry) {
        if (sREntry.next != null) {
            sREntry.next.prev = sREntry.prev;
        }
        if (sREntry.prev != null) {
            sREntry.prev.next = sREntry.next;
        } else {
            this.fBuckets[sREntry.bucket] = sREntry.next;
        }
        --this.fCount;
    }

    private void clean() {
        SREntry sREntry = (SREntry)this.fReferenceQueue.poll();
        while (sREntry != null) {
            this.removeEntry(sREntry);
            sREntry = (SREntry)this.fReferenceQueue.poll();
        }
    }

    protected static final class SREntryData {
        public final String symbol;
        public final char[] characters;

        public SREntryData(String string) {
            this.symbol = string;
            this.characters = new char[this.symbol.length()];
            this.symbol.getChars(0, this.characters.length, this.characters, 0);
        }

        public SREntryData(String string, char[] arrc, int n2, int n3) {
            this.symbol = string;
            this.characters = new char[n3];
            System.arraycopy(arrc, n2, this.characters, 0, n3);
        }
    }

    protected static final class SREntry
    extends SoftReference {
        public SREntry next;
        public SREntry prev;
        public int bucket;

        public SREntry(String string, SREntry sREntry, int n2, ReferenceQueue referenceQueue) {
            super(new SREntryData(string), referenceQueue);
            this.initialize(sREntry, n2);
        }

        public SREntry(String string, char[] arrc, int n2, int n3, SREntry sREntry, int n4, ReferenceQueue referenceQueue) {
            super(new SREntryData(string, arrc, n2, n3), referenceQueue);
            this.initialize(sREntry, n4);
        }

        private void initialize(SREntry sREntry, int n2) {
            this.next = sREntry;
            if (sREntry != null) {
                sREntry.prev = this;
            }
            this.prev = null;
            this.bucket = n2;
        }
    }

}

