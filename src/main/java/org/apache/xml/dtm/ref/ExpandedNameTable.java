/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.ref.ExtendedType;

public class ExpandedNameTable {
    private ExtendedType[] m_extendedTypes;
    private static int m_initialSize = 128;
    private int m_nextType;
    ExtendedType hashET = new ExtendedType(-1, "", "");
    private static ExtendedType[] m_defaultExtendedTypes;
    private static float m_loadFactor;
    private static int m_initialCapacity;
    private int m_capacity = m_initialCapacity;
    private int m_threshold = (int)((float)this.m_capacity * m_loadFactor);
    private HashEntry[] m_table = new HashEntry[this.m_capacity];

    public ExpandedNameTable() {
        this.initExtendedTypes();
    }

    private void initExtendedTypes() {
        this.m_extendedTypes = new ExtendedType[m_initialSize];
        for (int i2 = 0; i2 < 14; ++i2) {
            this.m_extendedTypes[i2] = m_defaultExtendedTypes[i2];
            this.m_table[i2] = new HashEntry(m_defaultExtendedTypes[i2], i2, i2, null);
        }
        this.m_nextType = 14;
    }

    public int getExpandedTypeID(String string, String string2, int n2) {
        return this.getExpandedTypeID(string, string2, n2, false);
    }

    public int getExpandedTypeID(String string, String string2, int n2, boolean bl) {
        Object object;
        if (null == string) {
            string = "";
        }
        if (null == string2) {
            string2 = "";
        }
        int n3 = n2 + string.hashCode() + string2.hashCode();
        this.hashET.redefine(n2, string, string2, n3);
        int n4 = n3 % this.m_capacity;
        if (n4 < 0) {
            n4 = - n4;
        }
        Object object2 = this.m_table[n4];
        while (object2 != null) {
            if (object2.hash == n3 && object2.key.equals(this.hashET)) {
                return object2.value;
            }
            object2 = object2.next;
        }
        if (bl) {
            return -1;
        }
        if (this.m_nextType > this.m_threshold) {
            this.rehash();
            n4 = n3 % this.m_capacity;
            if (n4 < 0) {
                n4 = - n4;
            }
        }
        object2 = new ExtendedType(n2, string, string2, n3);
        if (this.m_extendedTypes.length == this.m_nextType) {
            object = new ExtendedType[this.m_extendedTypes.length * 2];
            System.arraycopy(this.m_extendedTypes, 0, object, 0, this.m_extendedTypes.length);
            this.m_extendedTypes = object;
        }
        this.m_extendedTypes[this.m_nextType] = object2;
        this.m_table[n4] = object = new HashEntry((ExtendedType)object2, this.m_nextType, n3, this.m_table[n4]);
        return this.m_nextType++;
    }

    private void rehash() {
        int n2;
        int n3 = this.m_capacity;
        HashEntry[] arrhashEntry = this.m_table;
        this.m_capacity = n2 = 2 * n3 + 1;
        this.m_threshold = (int)((float)n2 * m_loadFactor);
        this.m_table = new HashEntry[n2];
        for (int i2 = n3 - 1; i2 >= 0; --i2) {
            HashEntry hashEntry = arrhashEntry[i2];
            while (hashEntry != null) {
                HashEntry hashEntry2 = hashEntry;
                hashEntry = hashEntry.next;
                int n4 = hashEntry2.hash % n2;
                if (n4 < 0) {
                    n4 = - n4;
                }
                hashEntry2.next = this.m_table[n4];
                this.m_table[n4] = hashEntry2;
            }
        }
    }

    public int getExpandedTypeID(int n2) {
        return n2;
    }

    public String getLocalName(int n2) {
        return this.m_extendedTypes[n2].getLocalName();
    }

    public final int getLocalNameID(int n2) {
        if (this.m_extendedTypes[n2].getLocalName().length() == 0) {
            return 0;
        }
        return n2;
    }

    public String getNamespace(int n2) {
        String string = this.m_extendedTypes[n2].getNamespace();
        return string.length() == 0 ? null : string;
    }

    public final int getNamespaceID(int n2) {
        if (this.m_extendedTypes[n2].getNamespace().length() == 0) {
            return 0;
        }
        return n2;
    }

    public final short getType(int n2) {
        return (short)this.m_extendedTypes[n2].getNodeType();
    }

    public int getSize() {
        return this.m_nextType;
    }

    public ExtendedType[] getExtendedTypes() {
        return this.m_extendedTypes;
    }

    static {
        m_loadFactor = 0.75f;
        m_initialCapacity = 203;
        m_defaultExtendedTypes = new ExtendedType[14];
        for (int i2 = 0; i2 < 14; ++i2) {
            ExpandedNameTable.m_defaultExtendedTypes[i2] = new ExtendedType(i2, "", "");
        }
    }

    private static final class HashEntry {
        ExtendedType key;
        int value;
        int hash;
        HashEntry next;

        protected HashEntry(ExtendedType extendedType, int n2, int n3, HashEntry hashEntry) {
            this.key = extendedType;
            this.value = n2;
            this.hash = n3;
            this.next = hashEntry;
        }
    }

}

