/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.util;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xs.XSObject;

public final class XSInputSource
extends XMLInputSource {
    private SchemaGrammar[] fGrammars;
    private XSObject[] fComponents;

    public XSInputSource(SchemaGrammar[] arrschemaGrammar) {
        super(null, null, null);
        this.fGrammars = arrschemaGrammar;
        this.fComponents = null;
    }

    public XSInputSource(XSObject[] arrxSObject) {
        super(null, null, null);
        this.fGrammars = null;
        this.fComponents = arrxSObject;
    }

    public SchemaGrammar[] getGrammars() {
        return this.fGrammars;
    }

    public void setGrammars(SchemaGrammar[] arrschemaGrammar) {
        this.fGrammars = arrschemaGrammar;
    }

    public XSObject[] getComponents() {
        return this.fComponents;
    }

    public void setComponents(XSObject[] arrxSObject) {
        this.fComponents = arrxSObject;
    }
}

