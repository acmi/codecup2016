/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.runtime;

import java.util.Enumeration;
import org.apache.xalan.xsltc.runtime.HashtableEntry;

public class Hashtable {
    private transient HashtableEntry[] table;
    private transient int count;
    private int threshold;
    private float loadFactor;

    public Hashtable(int n2, float f2) {
        if (n2 <= 0) {
            n2 = 11;
        }
        if ((double)f2 <= 0.0) {
            f2 = 0.75f;
        }
        this.loadFactor = f2;
        this.table = new HashtableEntry[n2];
        this.threshold = (int)((float)n2 * f2);
    }

    public Hashtable(int n2) {
        this(n2, 0.75f);
    }

    public Hashtable() {
        this(101, 0.75f);
    }

    public int size() {
        return this.count;
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    public Enumeration keys() {
        return new HashtableEnumerator(this.table, true);
    }

    public Enumeration elements() {
        return new HashtableEnumerator(this.table, false);
    }

    public boolean contains(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
        HashtableEntry[] arrhashtableEntry = this.table;
        int n2 = arrhashtableEntry.length;
        while (n2-- > 0) {
            HashtableEntry hashtableEntry = arrhashtableEntry[n2];
            while (hashtableEntry != null) {
                if (hashtableEntry.value.equals(object)) {
                    return true;
                }
                hashtableEntry = hashtableEntry.next;
            }
        }
        return false;
    }

    public boolean containsKey(Object object) {
        HashtableEntry[] arrhashtableEntry = this.table;
        int n2 = object.hashCode();
        int n3 = (n2 & Integer.MAX_VALUE) % arrhashtableEntry.length;
        HashtableEntry hashtableEntry = arrhashtableEntry[n3];
        while (hashtableEntry != null) {
            if (hashtableEntry.hash == n2 && hashtableEntry.key.equals(object)) {
                return true;
            }
            hashtableEntry = hashtableEntry.next;
        }
        return false;
    }

    public Object get(Object object) {
        HashtableEntry[] arrhashtableEntry = this.table;
        int n2 = object.hashCode();
        int n3 = (n2 & Integer.MAX_VALUE) % arrhashtableEntry.length;
        HashtableEntry hashtableEntry = arrhashtableEntry[n3];
        while (hashtableEntry != null) {
            if (hashtableEntry.hash == n2 && hashtableEntry.key.equals(object)) {
                return hashtableEntry.value;
            }
            hashtableEntry = hashtableEntry.next;
        }
        return null;
    }

    protected void rehash() {
        int n2 = this.table.length;
        HashtableEntry[] arrhashtableEntry = this.table;
        int n3 = n2 * 2 + 1;
        HashtableEntry[] arrhashtableEntry2 = new HashtableEntry[n3];
        this.threshold = (int)((float)n3 * this.loadFactor);
        this.table = arrhashtableEntry2;
        int n4 = n2;
        while (n4-- > 0) {
            HashtableEntry hashtableEntry = arrhashtableEntry[n4];
            while (hashtableEntry != null) {
                HashtableEntry hashtableEntry2 = hashtableEntry;
                hashtableEntry = hashtableEntry.next;
                int n5 = (hashtableEntry2.hash & Integer.MAX_VALUE) % n3;
                hashtableEntry2.next = arrhashtableEntry2[n5];
                arrhashtableEntry2[n5] = hashtableEntry2;
            }
        }
    }

    public Object put(Object object, Object object2) {
        if (object2 == null) {
            throw new NullPointerException();
        }
        HashtableEntry[] arrhashtableEntry = this.table;
        int n2 = object.hashCode();
        int n3 = (n2 & Integer.MAX_VALUE) % arrhashtableEntry.length;
        HashtableEntry hashtableEntry = arrhashtableEntry[n3];
        while (hashtableEntry != null) {
            if (hashtableEntry.hash == n2 && hashtableEntry.key.equals(object)) {
                Object object3 = hashtableEntry.value;
                hashtableEntry.value = object2;
                return object3;
            }
            hashtableEntry = hashtableEntry.next;
        }
        if (this.count >= this.threshold) {
            this.rehash();
            return this.put(object, object2);
        }
        hashtableEntry = new HashtableEntry();
        hashtableEntry.hash = n2;
        hashtableEntry.key = object;
        hashtableEntry.value = object2;
        hashtableEntry.next = arrhashtableEntry[n3];
        arrhashtableEntry[n3] = hashtableEntry;
        ++this.count;
        return null;
    }

    public Object remove(Object object) {
        HashtableEntry[] arrhashtableEntry = this.table;
        int n2 = object.hashCode();
        int n3 = (n2 & Integer.MAX_VALUE) % arrhashtableEntry.length;
        HashtableEntry hashtableEntry = arrhashtableEntry[n3];
        HashtableEntry hashtableEntry2 = null;
        while (hashtableEntry != null) {
            if (hashtableEntry.hash == n2 && hashtableEntry.key.equals(object)) {
                if (hashtableEntry2 != null) {
                    hashtableEntry2.next = hashtableEntry.next;
                } else {
                    arrhashtableEntry[n3] = hashtableEntry.next;
                }
                --this.count;
                return hashtableEntry.value;
            }
            hashtableEntry2 = hashtableEntry;
            hashtableEntry = hashtableEntry.next;
        }
        return null;
    }

    public void clear() {
        HashtableEntry[] arrhashtableEntry = this.table;
        int n2 = arrhashtableEntry.length;
        while (--n2 >= 0) {
            arrhashtableEntry[n2] = null;
        }
        this.count = 0;
    }

    public String toString() {
        int n2 = this.size() - 1;
        StringBuffer stringBuffer = new StringBuffer();
        Enumeration enumeration = this.keys();
        Enumeration enumeration2 = this.elements();
        stringBuffer.append("{");
        for (int i2 = 0; i2 <= n2; ++i2) {
            String string = enumeration.nextElement().toString();
            String string2 = enumeration2.nextElement().toString();
            stringBuffer.append(string + "=" + string2);
            if (i2 >= n2) continue;
            stringBuffer.append(", ");
        }
        stringBuffer.append("}");
        return stringBuffer.toString();
    }

    static class HashtableEnumerator
    implements Enumeration {
        boolean keys;
        int index;
        HashtableEntry[] table;
        HashtableEntry entry;

        HashtableEnumerator(HashtableEntry[] arrhashtableEntry, boolean bl) {
            this.table = arrhashtableEntry;
            this.keys = bl;
            this.index = arrhashtableEntry.length;
        }

        public boolean hasMoreElements() {
            if (this.entry != null) {
                return true;
            }
            while (this.index-- > 0) {
                this.entry = this.table[this.index];
                if (this.entry == null) continue;
                return true;
            }
            return false;
        }

        public Object nextElement() {
            if (this.entry == null) {
                while (this.index-- > 0 && (this.entry = this.table[this.index]) == null) {
                }
            }
            if (this.entry != null) {
                HashtableEntry hashtableEntry = this.entry;
                this.entry = hashtableEntry.next;
                return this.keys ? hashtableEntry.key : hashtableEntry.value;
            }
            return null;
        }
    }

}

