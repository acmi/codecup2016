/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.objects;

import javax.xml.transform.TransformerException;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;

public class XBooleanStatic
extends XBoolean {
    private final boolean m_val;

    public XBooleanStatic(boolean bl) {
        super(bl);
        this.m_val = bl;
    }

    public boolean equals(XObject xObject) {
        try {
            return this.m_val == xObject.bool();
        }
        catch (TransformerException transformerException) {
            throw new WrappedRuntimeException(transformerException);
        }
    }
}

