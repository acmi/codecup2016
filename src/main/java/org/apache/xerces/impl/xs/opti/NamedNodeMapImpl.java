/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.opti;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NamedNodeMapImpl
implements NamedNodeMap {
    Attr[] attrs;

    public NamedNodeMapImpl(Attr[] arrattr) {
        this.attrs = arrattr;
    }

    public Node getNamedItem(String string) {
        int n2 = 0;
        while (n2 < this.attrs.length) {
            if (this.attrs[n2].getName().equals(string)) {
                return this.attrs[n2];
            }
            ++n2;
        }
        return null;
    }

    public Node item(int n2) {
        if (n2 < 0 && n2 > this.getLength()) {
            return null;
        }
        return this.attrs[n2];
    }

    public int getLength() {
        return this.attrs.length;
    }

    public Node getNamedItemNS(String string, String string2) {
        int n2 = 0;
        while (n2 < this.attrs.length) {
            if (this.attrs[n2].getName().equals(string2) && this.attrs[n2].getNamespaceURI().equals(string)) {
                return this.attrs[n2];
            }
            ++n2;
        }
        return null;
    }

    public Node setNamedItemNS(Node node) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public Node setNamedItem(Node node) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public Node removeNamedItem(String string) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public Node removeNamedItemNS(String string, String string2) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }
}

