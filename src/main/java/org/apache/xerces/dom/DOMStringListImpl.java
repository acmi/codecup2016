/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import org.w3c.dom.DOMStringList;

public class DOMStringListImpl
implements DOMStringList {
    private final ArrayList fStrings;

    public DOMStringListImpl() {
        this.fStrings = new ArrayList();
    }

    public DOMStringListImpl(ArrayList arrayList) {
        this.fStrings = arrayList;
    }

    public DOMStringListImpl(Vector vector) {
        this.fStrings = new ArrayList(vector);
    }

    public String item(int n2) {
        int n3 = this.getLength();
        if (n2 >= 0 && n2 < n3) {
            return (String)this.fStrings.get(n2);
        }
        return null;
    }

    public int getLength() {
        return this.fStrings.size();
    }

    public boolean contains(String string) {
        return this.fStrings.contains(string);
    }

    public void add(String string) {
        this.fStrings.add(string);
    }
}

