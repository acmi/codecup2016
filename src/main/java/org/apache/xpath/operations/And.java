/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.operations;

import javax.xml.transform.TransformerException;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.operations.Operation;

public class And
extends Operation {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        XObject xObject = this.m_left.execute(xPathContext);
        if (xObject.bool()) {
            XObject xObject2 = this.m_right.execute(xPathContext);
            return xObject2.bool() ? XBoolean.S_TRUE : XBoolean.S_FALSE;
        }
        return XBoolean.S_FALSE;
    }

    public boolean bool(XPathContext xPathContext) throws TransformerException {
        return this.m_left.bool(xPathContext) && this.m_right.bool(xPathContext);
    }
}

