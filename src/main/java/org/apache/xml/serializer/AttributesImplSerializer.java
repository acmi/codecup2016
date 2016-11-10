/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.util.Hashtable;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

public final class AttributesImplSerializer
extends AttributesImpl {
    private final Hashtable m_indexFromQName = new Hashtable();
    private final StringBuffer m_buff = new StringBuffer();

    public final int getIndex(String string) {
        if (super.getLength() < 12) {
            int n2 = super.getIndex(string);
            return n2;
        }
        Integer n3 = (Integer)this.m_indexFromQName.get(string);
        int n4 = n3 == null ? -1 : n3;
        return n4;
    }

    public final void addAttribute(String string, String string2, String string3, String string4, String string5) {
        int n2 = super.getLength();
        super.addAttribute(string, string2, string3, string4, string5);
        if (n2 < 11) {
            return;
        }
        if (n2 == 11) {
            this.switchOverToHash(12);
        } else {
            Integer n3 = new Integer(n2);
            this.m_indexFromQName.put(string3, n3);
            this.m_buff.setLength(0);
            this.m_buff.append('{').append(string).append('}').append(string2);
            String string6 = this.m_buff.toString();
            this.m_indexFromQName.put(string6, n3);
        }
    }

    private void switchOverToHash(int n2) {
        for (int i2 = 0; i2 < n2; ++i2) {
            String string = super.getQName(i2);
            Integer n3 = new Integer(i2);
            this.m_indexFromQName.put(string, n3);
            String string2 = super.getURI(i2);
            String string3 = super.getLocalName(i2);
            this.m_buff.setLength(0);
            this.m_buff.append('{').append(string2).append('}').append(string3);
            String string4 = this.m_buff.toString();
            this.m_indexFromQName.put(string4, n3);
        }
    }

    public final void clear() {
        int n2 = super.getLength();
        super.clear();
        if (12 <= n2) {
            this.m_indexFromQName.clear();
        }
    }

    public final void setAttributes(Attributes attributes) {
        super.setAttributes(attributes);
        int n2 = attributes.getLength();
        if (12 <= n2) {
            this.switchOverToHash(n2);
        }
    }

    public final int getIndex(String string, String string2) {
        if (super.getLength() < 12) {
            int n2 = super.getIndex(string, string2);
            return n2;
        }
        this.m_buff.setLength(0);
        this.m_buff.append('{').append(string).append('}').append(string2);
        String string3 = this.m_buff.toString();
        Integer n3 = (Integer)this.m_indexFromQName.get(string3);
        int n4 = n3 == null ? -1 : n3;
        return n4;
    }
}

