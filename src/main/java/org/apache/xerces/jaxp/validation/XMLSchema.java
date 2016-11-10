/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.jaxp.validation;

import org.apache.xerces.jaxp.validation.AbstractXMLSchema;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

final class XMLSchema
extends AbstractXMLSchema {
    private final XMLGrammarPool fGrammarPool;
    private final boolean fFullyComposed;

    public XMLSchema(XMLGrammarPool xMLGrammarPool) {
        this(xMLGrammarPool, true);
    }

    public XMLSchema(XMLGrammarPool xMLGrammarPool, boolean bl) {
        this.fGrammarPool = xMLGrammarPool;
        this.fFullyComposed = bl;
    }

    public XMLGrammarPool getGrammarPool() {
        return this.fGrammarPool;
    }

    public boolean isFullyComposed() {
        return this.fFullyComposed;
    }
}

