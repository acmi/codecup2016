/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.runtime;

import org.apache.xalan.xsltc.DOM;
import org.xml.sax.AttributeList;

public final class Attributes
implements AttributeList {
    private int _element;
    private DOM _document;

    public Attributes(DOM dOM, int n2) {
        this._element = n2;
        this._document = dOM;
    }

    public int getLength() {
        return 0;
    }

    public String getName(int n2) {
        return null;
    }

    public String getType(int n2) {
        return null;
    }

    public String getType(String string) {
        return null;
    }

    public String getValue(int n2) {
        return null;
    }

    public String getValue(String string) {
        return null;
    }
}

