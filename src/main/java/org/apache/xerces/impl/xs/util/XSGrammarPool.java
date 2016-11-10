/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.util;

import java.util.ArrayList;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.XSModelImpl;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xs.XSModel;

public class XSGrammarPool
extends XMLGrammarPoolImpl {
    public XSModel toXSModel() {
        return this.toXSModel(1);
    }

    public XSModel toXSModel(short s2) {
        ArrayList<Grammar> arrayList = new ArrayList<Grammar>();
        int n2 = 0;
        while (n2 < this.fGrammars.length) {
            XMLGrammarPoolImpl.Entry entry = this.fGrammars[n2];
            while (entry != null) {
                if (entry.desc.getGrammarType().equals("http://www.w3.org/2001/XMLSchema")) {
                    arrayList.add(entry.grammar);
                }
                entry = entry.next;
            }
            ++n2;
        }
        int n3 = arrayList.size();
        if (n3 == 0) {
            return this.toXSModel(new SchemaGrammar[0], s2);
        }
        SchemaGrammar[] arrschemaGrammar = arrayList.toArray(new SchemaGrammar[n3]);
        return this.toXSModel(arrschemaGrammar, s2);
    }

    protected XSModel toXSModel(SchemaGrammar[] arrschemaGrammar, short s2) {
        return new XSModelImpl(arrschemaGrammar, s2);
    }
}

