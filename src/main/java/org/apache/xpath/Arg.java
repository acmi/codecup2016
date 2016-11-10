/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath;

import org.apache.xml.utils.QName;
import org.apache.xpath.objects.XObject;

public class Arg {
    private QName m_qname;
    private XObject m_val;
    private String m_expression;
    private boolean m_isFromWithParam;
    private boolean m_isVisible;

    public final QName getQName() {
        return this.m_qname;
    }

    public final XObject getVal() {
        return this.m_val;
    }

    public Arg() {
        this.m_qname = new QName("");
        this.m_val = null;
        this.m_expression = null;
        this.m_isVisible = true;
        this.m_isFromWithParam = false;
    }

    public boolean equals(Object object) {
        if (object instanceof QName) {
            return this.m_qname.equals(object);
        }
        return super.equals(object);
    }

    public Arg(QName qName, XObject xObject, boolean bl) {
        this.m_qname = qName;
        this.m_val = xObject;
        this.m_isFromWithParam = bl;
        this.m_isVisible = !bl;
        this.m_expression = null;
    }
}

