/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.jaxp.validation;

import org.apache.xerces.jaxp.validation.AbstractXMLSchema;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

final class SimpleXMLSchema
extends AbstractXMLSchema
implements XMLGrammarPool {
    private static final Grammar[] ZERO_LENGTH_GRAMMAR_ARRAY = new Grammar[0];
    private final Grammar fGrammar;
    private final Grammar[] fGrammars;
    private final XMLGrammarDescription fGrammarDescription;

    public SimpleXMLSchema(Grammar grammar) {
        this.fGrammar = grammar;
        this.fGrammars = new Grammar[]{grammar};
        this.fGrammarDescription = grammar.getGrammarDescription();
    }

    public Grammar[] retrieveInitialGrammarSet(String string) {
        return "http://www.w3.org/2001/XMLSchema".equals(string) ? (Grammar[])this.fGrammars.clone() : ZERO_LENGTH_GRAMMAR_ARRAY;
    }

    public void cacheGrammars(String string, Grammar[] arrgrammar) {
    }

    public Grammar retrieveGrammar(XMLGrammarDescription xMLGrammarDescription) {
        return this.fGrammarDescription.equals(xMLGrammarDescription) ? this.fGrammar : null;
    }

    public void lockPool() {
    }

    public void unlockPool() {
    }

    public void clear() {
    }

    public XMLGrammarPool getGrammarPool() {
        return this;
    }

    public boolean isFullyComposed() {
        return true;
    }
}

