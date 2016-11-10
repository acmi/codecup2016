/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.DTM;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DTMNamedNodeMap
implements NamedNodeMap {
    DTM dtm;
    int element;
    short m_count = -1;

    public DTMNamedNodeMap(DTM dTM, int n2) {
        this.dtm = dTM;
        this.element = n2;
    }

    public int getLength() {
        if (this.m_count == -1) {
            int n2 = 0;
            int n3 = this.dtm.getFirstAttribute(this.element);
            while (n3 != -1) {
                n2 = (short)(n2 + 1);
                n3 = this.dtm.getNextAttribute(n3);
            }
            this.m_count = n2;
        }
        return this.m_count;
    }

    public Node getNamedItem(String string) {
        int n2 = this.dtm.getFirstAttribute(this.element);
        while (n2 != -1) {
            if (this.dtm.getNodeName(n2).equals(string)) {
                return this.dtm.getNode(n2);
            }
            n2 = this.dtm.getNextAttribute(n2);
        }
        return null;
    }

    public Node item(int n2) {
        int n3 = 0;
        int n4 = this.dtm.getFirstAttribute(this.element);
        while (n4 != -1) {
            if (n3 == n2) {
                return this.dtm.getNode(n4);
            }
            ++n3;
            n4 = this.dtm.getNextAttribute(n4);
        }
        return null;
    }

    public Node setNamedItem(Node node) {
        throw new DTMException(7);
    }

    public Node removeNamedItem(String string) {
        throw new DTMException(7);
    }

    public Node getNamedItemNS(String string, String string2) {
        Node node = null;
        int n2 = this.dtm.getFirstAttribute(this.element);
        while (n2 != -1) {
            if (string2.equals(this.dtm.getLocalName(n2))) {
                String string3 = this.dtm.getNamespaceURI(n2);
                if (string == null && string3 == null || string != null && string.equals(string3)) {
                    node = this.dtm.getNode(n2);
                    break;
                }
            }
            n2 = this.dtm.getNextAttribute(n2);
        }
        return node;
    }

    public Node setNamedItemNS(Node node) throws DOMException {
        throw new DTMException(7);
    }

    public Node removeNamedItemNS(String string, String string2) throws DOMException {
        throw new DTMException(7);
    }

    public static class DTMException
    extends DOMException {
        public DTMException(short s2) {
            super(s2, "");
        }
    }

}

