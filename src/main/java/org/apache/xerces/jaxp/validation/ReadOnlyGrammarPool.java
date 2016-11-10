/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.jaxp.validation;

import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

final class ReadOnlyGrammarPool
implements XMLGrammarPool {
    private final XMLGrammarPool core;

    public ReadOnlyGrammarPool(XMLGrammarPool xMLGrammarPool) {
        this.core = xMLGrammarPool;
    }

    public void cacheGrammars(String string, Grammar[] arrgrammar) {
    }

    public void clear() {
    }

    public void lockPool() {
    }

    public Grammar retrieveGrammar(XMLGrammarDescription xMLGrammarDescription) {
        return this.core.retrieveGrammar(xMLGrammarDescription);
    }

    public Grammar[] retrieveInitialGrammarSet(String string) {
        return this.core.retrieveInitialGrammarSet(string);
    }

    public void unlockPool() {
    }
}

