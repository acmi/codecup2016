/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.objects;

import javax.xml.transform.TransformerException;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.objects.XBooleanStatic;
import org.apache.xpath.objects.XObject;

public class XBoolean
extends XObject {
    public static final XBoolean S_TRUE = new XBooleanStatic(true);
    public static final XBoolean S_FALSE = new XBooleanStatic(false);
    private final boolean m_val;

    public XBoolean(boolean bl) {
        this.m_val = bl;
    }

    public XBoolean(Boolean bl) {
        this.m_val = bl;
        this.setObject(bl);
    }

    public int getType() {
        return 1;
    }

    public String getTypeString() {
        return "#BOOLEAN";
    }

    public double num() {
        return this.m_val ? 1.0 : 0.0;
    }

    public boolean bool() {
        return this.m_val;
    }

    public String str() {
        return this.m_val ? "true" : "false";
    }

    public Object object() {
        if (null == this.m_obj) {
            this.setObject(this.m_val ? Boolean.TRUE : Boolean.FALSE);
        }
        return this.m_obj;
    }

    public boolean equals(XObject xObject) {
        if (xObject.getType() == 4) {
            return xObject.equals(this);
        }
        try {
            return this.m_val == xObject.bool();
        }
        catch (TransformerException transformerException) {
            throw new WrappedRuntimeException(transformerException);
        }
    }
}

