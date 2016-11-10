/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

public class XMLGrammarPoolImpl
implements XMLGrammarPool {
    protected static final int TABLE_SIZE = 11;
    protected Entry[] fGrammars = null;
    protected boolean fPoolIsLocked;
    protected int fGrammarCount = 0;
    private static final boolean DEBUG = false;

    public XMLGrammarPoolImpl() {
        this.fGrammars = new Entry[11];
        this.fPoolIsLocked = false;
    }

    public XMLGrammarPoolImpl(int n2) {
        this.fGrammars = new Entry[n2];
        this.fPoolIsLocked = false;
    }

    public Grammar[] retrieveInitialGrammarSet(String string) {
        Entry[] arrentry = this.fGrammars;
        synchronized (arrentry) {
            Object object;
            int n2 = this.fGrammars.length;
            Grammar[] arrgrammar = new Grammar[this.fGrammarCount];
            int n3 = 0;
            int n4 = 0;
            while (n4 < n2) {
                object = this.fGrammars[n4];
                while (object != null) {
                    if (object.desc.getGrammarType().equals(string)) {
                        arrgrammar[n3++] = object.grammar;
                    }
                    object = object.next;
                }
                ++n4;
            }
            object = new Grammar[n3];
            System.arraycopy(arrgrammar, 0, object, 0, n3);
            Object object2 = object;
            return object2;
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
                XMLGrammarDescription xMLGrammarDescription = grammar.getGrammarDescription();
                int n2 = this.hashCode(xMLGrammarDescription);
                int n3 = (n2 & Integer.MAX_VALUE) % this.fGrammars.length;
                Entry entry2 = this.fGrammars[n3];
                while (entry2 != null) {
                    if (entry2.hash == n2 && this.equals(entry2.desc, xMLGrammarDescription)) {
                        entry2.grammar = grammar;
                        return;
                    }
                    entry2 = entry2.next;
                }
                this.fGrammars[n3] = entry = new Entry(n2, xMLGrammarDescription, grammar, this.fGrammars[n3]);
                ++this.fGrammarCount;
            }
        }
    }

    public Grammar getGrammar(XMLGrammarDescription xMLGrammarDescription) {
        Entry[] arrentry = this.fGrammars;
        synchronized (arrentry) {
            int n2 = this.hashCode(xMLGrammarDescription);
            int n3 = (n2 & Integer.MAX_VALUE) % this.fGrammars.length;
            Entry entry = this.fGrammars[n3];
            while (entry != null) {
                if (entry.hash == n2 && this.equals(entry.desc, xMLGrammarDescription)) {
                    Grammar grammar = entry.grammar;
                    return grammar;
                }
                entry = entry.next;
            }
            Grammar grammar = null;
            return grammar;
        }
    }

    public Grammar removeGrammar(XMLGrammarDescription xMLGrammarDescription) {
        Entry[] arrentry = this.fGrammars;
        synchronized (arrentry) {
            int n2 = this.hashCode(xMLGrammarDescription);
            int n3 = (n2 & Integer.MAX_VALUE) % this.fGrammars.length;
            Entry entry = this.fGrammars[n3];
            Entry entry2 = null;
            while (entry != null) {
                if (entry.hash == n2 && this.equals(entry.desc, xMLGrammarDescription)) {
                    if (entry2 != null) {
                        entry2.next = entry.next;
                    } else {
                        this.fGrammars[n3] = entry.next;
                    }
                    Grammar grammar = entry.grammar;
                    entry.grammar = null;
                    --this.fGrammarCount;
                    Grammar grammar2 = grammar;
                    return grammar2;
                }
                entry2 = entry;
                entry = entry.next;
            }
            Grammar grammar = null;
            return grammar;
        }
    }

    public boolean containsGrammar(XMLGrammarDescription xMLGrammarDescription) {
        Entry[] arrentry = this.fGrammars;
        synchronized (arrentry) {
            int n2 = this.hashCode(xMLGrammarDescription);
            int n3 = (n2 & Integer.MAX_VALUE) % this.fGrammars.length;
            Entry entry = this.fGrammars[n3];
            while (entry != null) {
                if (entry.hash == n2 && this.equals(entry.desc, xMLGrammarDescription)) {
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
        return xMLGrammarDescription.equals(xMLGrammarDescription2);
    }

    public int hashCode(XMLGrammarDescription xMLGrammarDescription) {
        return xMLGrammarDescription.hashCode();
    }

    protected static final class Entry {
        public int hash;
        public XMLGrammarDescription desc;
        public Grammar grammar;
        public Entry next;

        protected Entry(int n2, XMLGrammarDescription xMLGrammarDescription, Grammar grammar, Entry entry) {
            this.hash = n2;
            this.desc = xMLGrammarDescription;
            this.grammar = grammar;
            this.next = entry;
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

