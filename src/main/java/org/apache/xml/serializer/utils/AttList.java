/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer.utils;

import org.apache.xml.serializer.utils.DOM2Helper;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;

public final class AttList
implements Attributes {
    NamedNodeMap m_attrs;
    int m_lastIndex;
    DOM2Helper m_dh;

    public AttList(NamedNodeMap namedNodeMap, DOM2Helper dOM2Helper) {
        this.m_attrs = namedNodeMap;
        this.m_lastIndex = this.m_attrs.getLength() - 1;
        this.m_dh = dOM2Helper;
    }

    public int getLength() {
        return this.m_attrs.getLength();
    }

    public String getURI(int n2) {
        String string = this.m_dh.getNamespaceOfNode((Attr)this.m_attrs.item(n2));
        if (null == string) {
            string = "";
        }
        return string;
    }

    public String getLocalName(int n2) {
        return this.m_dh.getLocalNameOfNode((Attr)this.m_attrs.item(n2));
    }

    public String getQName(int n2) {
        return ((Attr)this.m_attrs.item(n2)).getName();
    }

    public String getType(int n2) {
        return "CDATA";
    }

    public String getValue(int n2) {
        return ((Attr)this.m_attrs.item(n2)).getValue();
    }

    public String getValue(String string) {
        Attr attr = (Attr)this.m_attrs.getNamedItem(string);
        return null != attr ? attr.getValue() : null;
    }

    public int getIndex(String string) {
        for (int i2 = this.m_attrs.getLength() - 1; i2 >= 0; --i2) {
            Node node = this.m_attrs.item(i2);
            if (!node.getNodeName().equals(string)) continue;
            return i2;
        }
        return -1;
    }
}

