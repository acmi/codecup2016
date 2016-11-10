/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import java.util.ArrayList;
import org.apache.xerces.dom.DOMImplementationListImpl;
import org.apache.xerces.dom.DOMImplementationSourceImpl;
import org.apache.xerces.dom.PSVIDOMImplementationImpl;
import org.apache.xerces.impl.xs.XSImplementationImpl;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DOMImplementationList;

public class DOMXSImplementationSourceImpl
extends DOMImplementationSourceImpl {
    public DOMImplementation getDOMImplementation(String string) {
        DOMImplementation dOMImplementation = super.getDOMImplementation(string);
        if (dOMImplementation != null) {
            return dOMImplementation;
        }
        dOMImplementation = PSVIDOMImplementationImpl.getDOMImplementation();
        if (this.testImpl(dOMImplementation, string)) {
            return dOMImplementation;
        }
        dOMImplementation = XSImplementationImpl.getDOMImplementation();
        if (this.testImpl(dOMImplementation, string)) {
            return dOMImplementation;
        }
        return null;
    }

    public DOMImplementationList getDOMImplementationList(String string) {
        ArrayList<DOMImplementation> arrayList = new ArrayList<DOMImplementation>();
        DOMImplementationList dOMImplementationList = super.getDOMImplementationList(string);
        int n2 = 0;
        while (n2 < dOMImplementationList.getLength()) {
            arrayList.add(dOMImplementationList.item(n2));
            ++n2;
        }
        DOMImplementation dOMImplementation = PSVIDOMImplementationImpl.getDOMImplementation();
        if (this.testImpl(dOMImplementation, string)) {
            arrayList.add(dOMImplementation);
        }
        if (this.testImpl(dOMImplementation = XSImplementationImpl.getDOMImplementation(), string)) {
            arrayList.add(dOMImplementation);
        }
        return new DOMImplementationListImpl(arrayList);
    }
}

