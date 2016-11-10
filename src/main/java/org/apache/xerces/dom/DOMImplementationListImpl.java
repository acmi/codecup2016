/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DOMImplementationList;

public class DOMImplementationListImpl
implements DOMImplementationList {
    private final ArrayList fImplementations;

    public DOMImplementationListImpl() {
        this.fImplementations = new ArrayList();
    }

    public DOMImplementationListImpl(ArrayList arrayList) {
        this.fImplementations = arrayList;
    }

    public DOMImplementationListImpl(Vector vector) {
        this.fImplementations = new ArrayList(vector);
    }

    public DOMImplementation item(int n2) {
        int n3 = this.getLength();
        if (n2 >= 0 && n2 < n3) {
            return (DOMImplementation)this.fImplementations.get(n2);
        }
        return null;
    }

    public int getLength() {
        return this.fImplementations.size();
    }
}

