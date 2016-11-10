/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.jaxp.validation;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.grammars.XMLSchemaDescription;

final class SoftReferenceGrammarPool
implements XMLGrammarPool {
    protected static final int TABLE_SIZE = 11;
    protected static final Grammar[] ZERO_LENGTH_GRAMMAR_ARRAY = new Grammar[0];
    protected Entry[] fGrammars = null;
    protected boolean fPoolIsLocked;
    protected int fGrammarCount = 0;
    protected final ReferenceQueue fReferenceQueue = new ReferenceQueue();

    public SoftReferenceGrammarPool() {
        this.fGrammars = new Entry[11];
        this.fPoolIsLocked = false;
    }

    public SoftReferenceGrammarPool(int n2) {
        this.fGrammars = new Entry[n2];
        this.fPoolIsLocked = false;
    }

    public Grammar[] retrieveInitialGrammarSet(String string) {
        Entry[] arrentry = this.fGrammars;
        synchronized (arrentry) {
            this.clean();
            Grammar[] arrgrammar = ZERO_LENGTH_GRAMMAR_ARRAY;
            return arrgrammar;
        }
    }

    public void cacheGrammars(String string, Grammar[] arrgrammar) {
        if (!this.fPoolIsLocked) {
            int n2 = 0;
            while (n2 < arrgrammar.length) {
                this.putGrammar(arrgrammar[n2]);
                ++n2;
            }
        }
    }

    public Grammar retrieveGrammar(XMLGrammarDescription xMLGrammarDescription) {
        return this.getGrammar(xMLGrammarDescription);
    }

    public void putGrammar(Grammar grammar) {
        if (!this.fPoolIsLocked) {
            Entry[] arrentry = this.fGrammars;
            synchronized (arrentry) {
                Entry entry;
                this.clean();
                XMLGrammarDescription xMLGrammarDescription = grammar.getGrammarDescription();
                int n2 = this.hashCode(xMLGrammarDescription);
                int n3 = (n2 & Integer.MAX_VALUE) % this.fGrammars.length;
                Entry entry2 = this.fGrammars[n3];
                while (entry2 != null) {
                    if (entry2.hash == n2 && this.equals(entry2.desc, xMLGrammarDescription)) {
                        if (entry2.grammar.get() != grammar) {
                            entry2.grammar = new SoftGrammarReference(entry2, grammar, this.fReferenceQueue);
                        }
                        return;
                    }
                    entry2 = entry2.next;
                }
                this.fGrammars[n3] = entry = new Entry(n2, n3, xMLGrammarDescription, grammar, this.fGrammars[n3], this.fReferenceQueue);
                ++this.fGrammarCount;
            }
        }
    }

    public Grammar getGrammar(XMLGrammarDescription xMLGrammarDescription) {
        Entry[] arrentry = this.fGrammars;
        synchronized (arrentry) {
            Grammar grammar;
            this.clean();
            int n2 = this.hashCode(xMLGrammarDescription);
            int n3 = (n2 & Integer.MAX_VALUE) % this.fGrammars.length;
            Entry entry = this.fGrammars[n3];
            while (entry != null) {
                grammar = (Grammar)entry.grammar.get();
                if (grammar == null) {
                    this.removeEntry(entry);
                } else if (entry.hash == n2 && this.equals(entry.desc, xMLGrammarDescription)) {
                    Grammar grammar2 = grammar;
                    return grammar2;
                }
                entry = entry.next;
            }
            grammar = null;
            return grammar;
        }
    }

    public Grammar removeGrammar(XMLGrammarDescription xMLGrammarDescription) {
        Entry[] arrentry = this.fGrammars;
        synchronized (arrentry) {
            this.clean();
            int n2 = this.hashCode(xMLGrammarDescription);
            int n3 = (n2 & Integer.MAX_VALUE) % this.fGrammars.length;
            Entry entry = this.fGrammars[n3];
            while (entry != null) {
                if (entry.hash == n2 && this.equals(entry.desc, xMLGrammarDescription)) {
                    Grammar grammar = this.removeEntry(entry);
                    return grammar;
                }
                entry = entry.next;
            }
            Grammar grammar = null;
            return grammar;
        }
    }

    public boolean containsGrammar(XMLGrammarDescription xMLGrammarDescription) {
        Entry[] arrentry = this.fGrammars;
        synchronized (arrentry) {
            this.clean();
            int n2 = this.hashCode(xMLGrammarDescription);
            int n3 = (n2 & Integer.MAX_VALUE) % this.fGrammars.length;
            Entry entry = this.fGrammars[n3];
            while (entry != null) {
                Grammar grammar = (Grammar)entry.grammar.get();
                if (grammar == null) {
                    this.removeEntry(entry);
                } else if (entry.hash == n2 && this.equals(entry.desc, xMLGrammarDescription)) {
                    boolean bl = true;
                    return bl;
                }
                entry = entry.next;
            }
            boolean bl = false;
            return bl;
        }
    }

    public void lockPool() {
        this.fPoolIsLocked = true;
    }

    public void unlockPool() {
        this.fPoolIsLocked = false;
    }

    public void clear() {
        int n2 = 0;
        while (n2 < this.fGrammars.length) {
            if (this.fGrammars[n2] != null) {
                this.fGrammars[n2].clear();
                this.fGrammars[n2] = null;
            }
            ++n2;
        }
        this.fGrammarCount = 0;
    }

    public boolean equals(XMLGrammarDescription xMLGrammarDescription, XMLGrammarDescription xMLGrammarDescription2) {
        if (xMLGrammarDescription instanceof XMLSchemaDescription) {
            if (!(xMLGrammarDescription2 instanceof XMLSchemaDescription)) {
                return false;
            }
            XMLSchemaDescription xMLSchemaDescription = (XMLSchemaDescription)xMLGrammarDescription;
            XMLSchemaDescription xMLSchemaDescription2 = (XMLSchemaDescription)xMLGrammarDescription2;
            String string = xMLSchemaDescription.getTargetNamespace();
            if (string != null ? !string.equals(xMLSchemaDescription2.getTargetNamespace()) : xMLSchemaDescription2.getTargetNamespace() != null) {
                return false;
            }
            String string2 = xMLSchemaDescription.getExpandedSystemId();
            if (string2 != null ? !string2.equals(xMLSchemaDescription2.getExpandedSystemId()) : xMLSchemaDescription2.getExpandedSystemId() != null) {
                return false;
            }
            return true;
        }
        return xMLGrammarDescription.equals(xMLGrammarDescription2);
    }

    public int hashCode(XMLGrammarDescription xMLGrammarDescription) {
        if (xMLGrammarDescription instanceof XMLSchemaDescription) {
            XMLSchemaDescription xMLSchemaDescription = (XMLSchemaDescription)xMLGrammarDescription;
            String string = xMLSchemaDescription.getTargetNamespace();
            String string2 = xMLSchemaDescription.getExpandedSystemId();
            int n2 = string != null ? string.hashCode() : 0;
            return n2 ^= string2 != null ? string2.hashCode() : 0;
        }
        return xMLGrammarDescription.hashCode();
    }

    private Grammar removeEntry(Entry entry) {
        if (entry.prev != null) {
            entry.prev.next = entry.next;
        } else {
            this.fGrammars[entry.bucket] = entry.next;
        }
        if (entry.next != null) {
            entry.next.prev = entry.prev;
        }
        --this.fGrammarCount;
        entry.grammar.entry = null;
        return (Grammar)entry.grammar.get();
    }

    private void clean() {
        Reference reference = this.fReferenceQueue.poll();
        while (reference != null) {
            Entry entry = ((SoftGrammarReference)reference).entry;
            if (entry != null) {
                this.removeEntry(entry);
            }
            reference = this.fReferenceQueue.poll();
        }
    }

    static final class SoftGrammarReference
    extends SoftReference {
        public Entry entry;

        protected SoftGrammarReference(Entry entry, Grammar grammar, ReferenceQueue referenceQueue) {
            super(grammar, referenceQueue);
            this.entry = entry;
        }
    }

    static final class Entry {
        public int hash;
        public int bucket;
        public Entry prev;
        public Entry next;
        public XMLGrammarDescription desc;
        public SoftGrammarReference grammar;

        protected Entry(int n2, int n3, XMLGrammarDescription xMLGrammarDescription, Grammar grammar, Entry entry, ReferenceQueue referenceQueue) {
            this.hash = n2;
            this.bucket = n3;
            this.prev = null;
            this.next = entry;
            if (entry != null) {
                entry.prev = this;
            }
            this.desc = xMLGrammarDescription;
            this.grammar = new SoftGrammarReference(this, grammar, referenceQueue);
        }

        protected void clear() {
            this.desc = null;
            this.grammar = null;
            if (this.next != null) {
                this.next.clear();
                this.next = null;
            }
        }
    }

}

