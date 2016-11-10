/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.runtime;

class HashtableEntry {
    int hash;
    Object key;
    Object value;
    HashtableEntry next;

    HashtableEntry() {
    }

    protected Object clone() {
        HashtableEntry hashtableEntry = new HashtableEntry();
        hashtableEntry.hash = this.hash;
        hashtableEntry.key = this.key;
        hashtableEntry.value = this.value;
        hashtableEntry.next = this.next != null ? (HashtableEntry)this.next.clone() : null;
        return hashtableEntry;
    }
}

