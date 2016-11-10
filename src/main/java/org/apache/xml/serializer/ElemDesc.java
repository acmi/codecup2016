/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import org.apache.xml.serializer.utils.StringToIntTable;

public final class ElemDesc {
    private int m_flags;
    private StringToIntTable m_attrs = null;

    ElemDesc(int n2) {
        this.m_flags = n2;
    }

    int getFlags() {
        return this.m_flags;
    }

    void setAttr(String string, int n2) {
        if (null == this.m_attrs) {
            this.m_attrs = new StringToIntTable();
        }
        this.m_attrs.put(string, n2);
    }

    public boolean isAttrFlagSet(String string, int n2) {
        return null != this.m_attrs ? (this.m_attrs.getIgnoreCase(string) & n2) != 0 : false;
    }
}

